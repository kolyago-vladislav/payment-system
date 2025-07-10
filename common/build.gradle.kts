import org.openapitools.generator.gradle.plugin.tasks.GenerateTask
import java.io.File

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
}

tasks.register("generateAllOpenApi") {
    openApiDir
        .listFiles { file -> file.extension in listOf("yaml", "yml") }
        ?.forEach { specFile ->
            val nameWithoutExt = specFile.nameWithoutExtension.replace(Regex("[^A-Za-z0-9]"), "").capitalize()
            dependsOn("generate${nameWithoutExt}Api")
        }
}

val openApiDir = file("$rootDir/openapi")

openApiDir
    .listFiles { file -> file.extension in listOf("yaml", "yml") }
    ?.forEach { specFile ->
        val nameWithoutExt = specFile.nameWithoutExtension.replace(Regex("[^A-Za-z0-9]"), "").capitalize()
        val taskName = "generate${nameWithoutExt}Api"

        tasks.register(taskName, GenerateTask::class) {
            generatorName.set("spring")
            inputSpec.set(specFile.absolutePath)
            outputDir.set("$buildDir/generated-sources/openapi/${specFile.nameWithoutExtension}")
            apiPackage.set("com.example.${specFile.nameWithoutExtension}.api")
            modelPackage.set("com.example.${specFile.nameWithoutExtension}.dto")
            configOptions.set(
                mapOf(
                    "interfaceOnly" to "true",
                    "library" to "spring-cloud",
                    "skipDefaultInterface" to "true",
                    "useBeanValidation" to "true",
                    "openApiNullable" to "false"
                )
            )
        }
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

sourceSets {
    main {
        java {
            srcDir("$buildDir/generated-sources/openapi/src/main/java")
        }
    }
}