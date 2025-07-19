import org.openapitools.generator.gradle.plugin.tasks.GenerateTask
import org.gradle.api.publish.maven.MavenPublication

val versions = mapOf(
    "mapstructVersion" to "1.5.5.Final",
    "springdocOpenapiStarterWebmvcUiVersion" to "2.5.0",
    "javaxAnnotationApiVersion" to "1.3.2",
    "javaxValidationApiVersion" to "2.0.0.Final",
    "comGoogleCodeFindbugs" to "3.0.2",
    "springCloudStarterOpenfeign" to "4.1.1",
    "javaxServletApiVersion" to "2.5",
    "logbackClassicVersion" to "1.5.18",
    "comGoogleCodeFindbugs" to "3.0.2",
    "springCloudStarterOpenfeign" to "4.1.1"
)

plugins {
	java
	id("org.springframework.boot") version "3.5.0"
	id("io.spring.dependency-management") version "1.1.7"
	id("maven-publish")
    id("org.openapi.generator") version "7.13.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(24)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2025.0.0")
        mavenBom("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom:2.15.0")
    }
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}

dependencies {
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation ("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")

	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${versions["springdocOpenapiStarterWebmvcUiVersion"]}")

    implementation("org.postgresql:postgresql")

    compileOnly("org.projectlombok:lombok")
    compileOnly("org.mapstruct:mapstruct:${versions["mapstructVersion"]}")
    compileOnly("com.google.code.findbugs:jsr305:${versions["comGoogleCodeFindbugs"]}")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:${versions["mapstructVersion"]}")

    implementation("org.hibernate.orm:hibernate-envers:6.4.4.Final")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("ch.qos.logback:logback-classic:${versions["logbackClassicVersion"]}")

    testCompileOnly ("org.projectlombok:lombok")
    testAnnotationProcessor ("org.projectlombok:lombok")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.testcontainers:testcontainers:1.19.3")
    testImplementation("org.testcontainers:postgresql:1.19.3")
    testImplementation("org.testcontainers:junit-jupiter:1.19.3")

    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:${versions["springCloudStarterOpenfeign"]}")
    implementation("javax.validation:validation-api:${versions["javaxValidationApiVersion"]}")
    implementation("javax.annotation:javax.annotation-api:${versions["javaxAnnotationApiVersion"]}")

    implementation("io.github.openfeign:feign-micrometer:13.6")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")
    implementation("io.micrometer:micrometer-observation")
    implementation("io.micrometer:micrometer-tracing")
    implementation("io.micrometer:micrometer-tracing-bridge-otel")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    implementation ("io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

/*
──────────────────────────────────────────────────────
============== Api generation ==============
──────────────────────────────────────────────────────
*/

val openApiDir = file("$rootDir/openapi")

val foundSpecifications = openApiDir.listFiles { f -> f.extension in listOf("yaml", "yml") } ?: emptyArray()
logger.lifecycle("Found ${foundSpecifications.size} specifications: " + foundSpecifications.joinToString { it.name })

foundSpecifications.forEach { specFile ->
    val outDir = getAbsolutePath(specFile.nameWithoutExtension)
    val packageName = defineJavaPackageName(specFile.nameWithoutExtension)

    val taskName = buildGenerateApiTaskName(specFile.nameWithoutExtension)
    logger.lifecycle("Register task $taskName from ${outDir.get()}")
    val basePackage = "com.example.${packageName}"

    tasks.register(taskName, GenerateTask::class) {
        generatorName.set("spring")
        inputSpec.set(specFile.absolutePath)
        outputDir.set(outDir)

        configOptions.set(
            mapOf(
                "library"              to "spring-cloud",
                "skipDefaultInterface" to "true",
                "useBeanValidation"    to "true",
                "openApiNullable"      to "false",
                "useFeignClientUrl"    to "true",
                "useTags"              to "true",
                "apiPackage"           to "${basePackage}.api",
                "modelPackage"         to "${basePackage}.dto",
                "configPackage"        to "${basePackage}.config"
            )
        )

        doFirst {
            logger.lifecycle("$taskName: starting generation from ${specFile.name}")
        }
    }
}

fun getAbsolutePath(nameWithoutExtension: String): Provider<String> {
    return layout.buildDirectory
            .dir("generated-sources/openapi/${nameWithoutExtension}")
            .map { it.asFile.absolutePath }
}

fun defineJavaPackageName(name: String): String {
     val beforeDash = name.substringBefore('-')
     val match = Regex("^[a-z]+").find(beforeDash)
     return match?.value ?: beforeDash.lowercase()
}


fun buildGenerateApiTaskName(name: String): String {
    return buildTaskName("generate", name)
}

fun buildJarTaskName(name: String): String {
    return buildTaskName("jar", name)
}

fun buildTaskName(taskPrefix: String, name: String): String {
    val prepareName = name
        .split(Regex("[^A-Za-z0-9]"))
        .filter { it.isNotBlank() }
        .joinToString("") { it.replaceFirstChar(Char::uppercase) }

    return "${taskPrefix}-${prepareName}"
}


val withoutExtensionNames = foundSpecifications.map { it.nameWithoutExtension }

sourceSets.named("main") {
    withoutExtensionNames.forEach { name ->
        java.srcDir(layout.buildDirectory.dir("generated-sources/openapi/$name/src/main/java"))
    }
}

tasks.register("generateAllOpenApi") {
    foundSpecifications.forEach { specFile ->
        dependsOn(buildGenerateApiTaskName(specFile.nameWithoutExtension))
    }
    doLast {
        logger.lifecycle("generateAllOpenApi: all specifications has been generated")
    }
}

tasks.named("compileJava") {
    dependsOn("generateAllOpenApi")
}

/*
──────────────────────────────────────────────────────
============== Building jars ==============
──────────────────────────────────────────────────────
*/

tasks.named("build") {
    dependsOn(generatedJars)
}

val generatedJars = foundSpecifications.map { specFile ->
    val name = specFile.nameWithoutExtension
    val generateTaskName = buildGenerateApiTaskName(name)
    val jarTaskName = buildJarTaskName(name)
    val outDirProvider = getAbsolutePath(name)
    val generatedSrcDir = outDirProvider.map { File(it).resolve("src/main/java") }

    val sourceSetName = "${name}"

    val sourceSet = sourceSets.create(sourceSetName) {
        java.srcDir(generatedSrcDir)
        compileClasspath += sourceSets["main"].compileClasspath
    }

    val compileTaskName = "compile${sourceSetName.replaceFirstChar(Char::uppercase)}Java"
    tasks.register<JavaCompile>(compileTaskName) {
        source = sourceSet.java
        classpath = sourceSet.compileClasspath
        destinationDirectory.set(layout.buildDirectory.dir("classes/$sourceSetName"))
        dependsOn(generateTaskName)
    }

    tasks.register<Jar>(jarTaskName) {
        group = "build"
        archiveBaseName.set("${name}")
        destinationDirectory.set(layout.buildDirectory.dir("libs"))

        val classOutput = layout.buildDirectory.dir("classes/$sourceSetName")
        from(classOutput)
        dependsOn(compileTaskName)

        doFirst {
            println("Building JAR for $name from compiled classes in ${classOutput.get().asFile}")
        }
    }
}

/*
──────────────────────────────────────────────────────
============== Resolve NEXUS credentials ==============
──────────────────────────────────────────────────────
*/

file("../.env").takeIf { it.exists() }?.readLines()?.forEach {
    val (k, v) = it.split("=", limit = 2)
    System.setProperty(k.trim(), v.trim())
    logger.lifecycle("${k.trim()}=${v.trim()}")
}

val nexusUrl = System.getenv("NEXUS_URL") ?: System.getProperty("NEXUS_URL")
val nexusUser = System.getenv("NEXUS_USERNAME") ?: System.getProperty("NEXUS_USERNAME")
val nexusPassword = System.getenv("NEXUS_PASSWORD") ?: System.getProperty("NEXUS_PASSWORD")

if (nexusUrl.isNullOrBlank() || nexusUser.isNullOrBlank() || nexusPassword.isNullOrBlank()) {
    throw GradleException(
        "NEXUS_URL or NEXUS_USER or NEXUS_PASSWORD not set. " +
        "Please create a .env file with these properties or set environment variables."
    )
}


/*
──────────────────────────────────────────────────────
============== Nexus Publishing ==============
──────────────────────────────────────────────────────
*/

publishing {
    publications {
        foundSpecifications.forEach { specFile ->
            val name = specFile.nameWithoutExtension
            val jarBaseName = "$name"
            val jarFile = file("build/libs")
                .listFiles()
                ?.firstOrNull { it.name.contains(name) && (it.extension == "jar" || it.extension == "zip") }

            if (jarFile != null) {
                logger.lifecycle("publishing: ${jarFile?.name}")

                create<MavenPublication>("publish${name.replaceFirstChar(Char::uppercase)}Jar") {
                    artifact(jarFile)
                    groupId = "com.example"
                    artifactId = jarBaseName
                    version = "1.0.0-SNAPSHOT"

                    pom {
                        this.name.set("Generated API $jarBaseName")
                        this.description.set("OpenAPI generated code for $jarBaseName")
                    }
                }
            }
        }
    }

    repositories {
        maven {
            name = "nexus"
            url = uri(nexusUrl)
            isAllowInsecureProtocol = true
            credentials {
                username = nexusUser
                password = nexusPassword
            }
        }
    }
}