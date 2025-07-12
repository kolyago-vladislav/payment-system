import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

val versions = mapOf(
    "springdocOpenapiStarterWebmvcUi" to "2.5.0",
    "javaxAnnotationApi" to "1.3.2",
    "javaxValidationApi" to "2.0.0.Final",
    "comGoogleCodeFindbugs" to "3.0.2",
    "springCloudStarterOpenfeign" to "4.1.1",
)

plugins {
    id("java-library")
    id("maven-publish")
	id("org.openapi.generator") version "7.13.0"
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(24)
	}
}

repositories {
    mavenCentral()
}

dependencies {
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${versions["springdocOpenapiStarterWebmvcUi"]}")
	implementation("javax.validation:validation-api:${versions["javaxValidationApi"]}")
    implementation("javax.annotation:javax.annotation-api:${versions["javaxAnnotationApi"]}")

    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:${versions["springCloudStarterOpenfeign"]}")

    compileOnly("com.google.code.findbugs:jsr305:${versions["comGoogleCodeFindbugs"]}")
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

    val taskName = buildTaskName(specFile.nameWithoutExtension)
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

/*
──────────────────────────────────────────────────────
============== Nexus Publishing ==============
──────────────────────────────────────────────────────
*/

val nexusUrl = System.getenv("NEXUS_URL") ?: "http://localhost:8081/repository/maven-snapshots/"
val nexusUsername = System.getenv("NEXUS_USERNAME") ?: "admin"
val nexusPassword = System.getenv("NEXUS_PASSWORD") ?: "admin"

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "com.example"
            artifactId = "common"
            version = System.getenv("ARTEFACT_VERSION") ?: "1.0.0-SNAPSHOT"
        }
    }
    repositories {
        maven {
            name = "nexus"
            url = uri(nexusUrl)
            isAllowInsecureProtocol = true
            credentials {
                username = nexusUsername
                password = nexusPassword
            }
        }
    }
}
