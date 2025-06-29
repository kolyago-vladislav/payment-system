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

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    implementation("org.springframework.boot:spring-boot-starter-validation")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")


	implementation("javax.validation:validation-api:2.0.0.Final")
	implementation("javax.annotation:javax.annotation-api:1.3.2")
	compileOnly("javax.servlet:servlet-api:2.5")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$rootDir/openapi/individuals-api.yaml")
    outputDir.set("$buildDir/generated-sources/openapi")
    apiPackage.set("com.example.api")
    modelPackage.set("com.example.dto")
    configOptions.set(
        mapOf(
            "interfaceOnly" to "true",
            "library" to "spring-boot",
            "skipDefaultInterface" to "true",
            "useBeanValidation" to "true",
            "openApiNullable" to "false"
        )
    )
}

tasks.named("compileJava") {
    dependsOn("openApiGenerate")
}

sourceSets {
    main {
        java {
            srcDir("$buildDir/generated-sources/openapi/src/main/java")
        }
    }
}