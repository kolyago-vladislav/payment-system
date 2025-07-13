import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

val versions = mapOf(
    "keycloakAdminClientVersion" to "22.0.3",
    "springdocOpenapiStarterWebmvcUiVersion" to "2.5.0",
    "mapstructVersion" to "1.5.5.Final",
    "javaxAnnotationApiVersion" to "1.3.2",
    "javaxValidationApiVersion" to "2.0.0.Final",
    "javaxServletApiVersion" to "2.5",
    "logbackClassicVersion" to "1.5.18"
)

plugins {
	java
	id("org.springframework.boot") version "3.5.0"
	id("io.spring.dependency-management") version "1.1.7"
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
	mavenLocal()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2025.0.0")
    }
}

dependencies {
    implementation ("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation ("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation ("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation ("org.keycloak:keycloak-admin-client:${versions["keycloakAdminClientVersion"]}")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${versions["springdocOpenapiStarterWebmvcUiVersion"]}")
    implementation("org.springframework.boot:spring-boot-starter-validation")


    compileOnly("org.projectlombok:lombok")
    compileOnly("org.mapstruct:mapstruct:${versions["mapstructVersion"]}")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:${versions["mapstructVersion"]}")

    implementation("com.example:common:1.0.0-SNAPSHOT")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testCompileOnly ("org.projectlombok:lombok")
    testAnnotationProcessor ("org.projectlombok:lombok")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.testcontainers:testcontainers:1.19.3")
    testImplementation("org.testcontainers:postgresql:1.19.3")
    testImplementation("org.testcontainers:junit-jupiter:1.19.3")

    implementation("ch.qos.logback:logback-classic:${versions["logbackClassicVersion"]}")

	implementation("javax.validation:validation-api:${versions["javaxValidationApiVersion"]}")
	implementation("javax.annotation:javax.annotation-api:${versions["javaxAnnotationApiVersion"]}")

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

val foundSpecifications = findApiYamlFiles(openApiDir)

fun findApiYamlFiles(directory: File): Array<File> {
    return directory.listFiles { file ->
        file.isFile && file.name.matches(Regex(".*-api\\.ya?ml$", RegexOption.IGNORE_CASE))
    } ?: emptyArray()
}

logger.lifecycle("Found ${foundSpecifications.size} specifications: " + foundSpecifications.joinToString { it.name })

foundSpecifications.forEach { specFile ->
    val outDir = getAbsolutePath(specFile.nameWithoutExtension)
    val packageName = defineJavaPackageName(specFile.nameWithoutExtension)

    val taskName = buildTaskName(specFile.nameWithoutExtension)
    logger.lifecycle("Register task $taskName from ${outDir.get()}")
    val basePackage = "com.example.${packageName}"

    tasks.register(taskName, GenerateTask::class) {
        generatorName.set("spring")
        inputSpec.set(specFile.absolutePath)
        outputDir.set(outDir)

        configOptions.set(
            mapOf(
                "apiPackage"           to "${basePackage}.api",
                "modelPackage"         to "${basePackage}.dto",
                "configPackage"        to "${basePackage}.config",
                "interfaceOnly"        to "true",
                "library"              to "spring-boot",
                "skipDefaultInterface" to  "true",
                "useBeanValidation"    to "true",
                "openApiNullable"      to "false",
                "reactive"             to "true"
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


fun buildTaskName(name: String): String {
    val prepareName = name
        .split(Regex("[^A-Za-z0-9]"))
        .filter { it.isNotBlank() }
        .joinToString("") { it.replaceFirstChar(Char::uppercase) }

    return "generate${prepareName}"
}


val withoutExtensionNames = foundSpecifications.map { it.nameWithoutExtension }

sourceSets.named("main") {
    withoutExtensionNames.forEach { name ->
        logger.lifecycle("generated-sources/openapi/$name/src/main/java")
        java.srcDir(layout.buildDirectory.dir("generated-sources/openapi/$name/src/main/java"))
    }
}

tasks.register("generateAllOpenApi") {
    foundSpecifications.forEach { specFile ->
        dependsOn(buildTaskName(specFile.nameWithoutExtension))
    }
    doLast {
        logger.lifecycle("generateAllOpenApi: all specifications has been generated")
    }
}

tasks.named("compileJava") {
    dependsOn("generateAllOpenApi")
}

repositories {
	mavenCentral()
	maven {
        url = uri(System.getenv("NEXUS_URL") ?: "http://localhost:8081/repository/maven-snapshots/")
        isAllowInsecureProtocol = true
        credentials {
            username = System.getenv("NEXUS_USER") ?: "admin"
            password = System.getenv("NEXUS_PASSWORD") ?: "admin"
        }
    }
}