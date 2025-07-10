import org.openapitools.generator.gradle.plugin.tasks.GenerateTask
import java.io.File
import java.io.FileFilter

val springdocOpenapiStarterWebmvcUiVersion: String by project
val javaxAnnotationApiVersion: String by project
val javaxValidationApiVersion: String by project

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
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springdocOpenapiStarterWebmvcUiVersion")
	implementation("javax.validation:validation-api:$javaxValidationApiVersion")
    implementation("javax.annotation:javax.annotation-api:$javaxAnnotationApiVersion")

    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
}

val openApiDir = file("$rootDir/openapi")

val foundSpecifications = openApiDir.listFiles { f -> f.extension in listOf("yaml", "yml") } ?: emptyArray()
logger.lifecycle("Found ${foundSpecifications.size} specifications: " + foundSpecifications.joinToString { it.name })

fun buildTaskName(name: String): String {
    val prepareName = name
        .split(Regex("[^A-Za-z0-9]"))
        .filter { it.isNotBlank() }
        .joinToString("") { it.replaceFirstChar(Char::uppercase) }

    return "generate${prepareName}"
}

fun defineJavaPackageName(name: String): String {
     val beforeDash = name.substringBefore('-')
     val match = Regex("^[a-z]+").find(beforeDash)
     return match?.value ?: beforeDash.lowercase()
}

fun getAbsolutePath(nameWithoutExtension: String): Provider<String> {
    return layout.buildDirectory
            .dir("generated-sources/openapi/${nameWithoutExtension}")
            .map { it.asFile.absolutePath }
}

foundSpecifications.forEach { specFile ->
    val taskName = buildTaskName(specFile.nameWithoutExtension)
    val outDir = getAbsolutePath(specFile.nameWithoutExtension)
    logger.lifecycle("Register task $taskName from ${outDir.get()}")

    val packageName = defineJavaPackageName(specFile.nameWithoutExtension)

    tasks.register(taskName, GenerateTask::class) {
        generatorName.set("spring")
        inputSpec.set(specFile.absolutePath)
        outputDir.set(outDir)
        apiPackage.set("com.example.${packageName}.api")
        modelPackage.set("com.example.${packageName}.dto")

        configOptions.set(
            mapOf(
                "interfaceOnly"        to "true",
                "library"              to "spring-cloud",
                "skipDefaultInterface" to "true",
                "useBeanValidation"    to "true",
                "openApiNullable"      to "false"
            )
        )

        doFirst {
            logger.lifecycle("$taskName: starting generation from ${specFile.name}")
        }
    }
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

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "com.example"
            artifactId = "common"
            version = "1.0.0"
        }
    }
    repositories {
        maven {
            name = "nexus"
            url = uri("http://localhost:8081/repository/maven-releases/")
            isAllowInsecureProtocol = true
            credentials {
                username = "admin"
                password = "admin"
            }
        }
    }
}
