val keycloakAdminClientVersion: String by project
val springdocOpenapiStarterWebmvcUiVersion: String by project
val mapstructVersion: String by project
val javaxAnnotationApiVersion: String by project
val javaxValidationApiVersion: String by project
val javaxServletApiVersion: String by project
val logbackClassicVersion: String by project

plugins {
	java
	id("org.springframework.boot") version "3.5.0"
	id("io.spring.dependency-management") version "1.1.7"
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
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
	maven {
        url = uri(System.getenv("NEXUS_URL") ?: "http://localhost:8081/repository/maven-snapshots/")
        isAllowInsecureProtocol = true
        credentials {
            username = System.getenv("NEXUS_USER") ?: "admin"
            password = System.getenv("NEXUS_PASSWORD") ?: "admin"
        }
    }
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2025.0.0")
    }
}

dependencies {
    implementation ("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springdocOpenapiStarterWebmvcUiVersion")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("com.example:common:1.0.0-SNAPSHOT")

    implementation("org.postgresql:postgresql")

    compileOnly("org.projectlombok:lombok")
    compileOnly("org.mapstruct:mapstruct:$mapstructVersion")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")


	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("ch.qos.logback:logback-classic:$logbackClassicVersion")

    testCompileOnly ("org.projectlombok:lombok")
    testAnnotationProcessor ("org.projectlombok:lombok")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.testcontainers:testcontainers:1.19.3")
    testImplementation("org.testcontainers:postgresql:1.19.3")
    testImplementation("org.testcontainers:junit-jupiter:1.19.3")

    implementation("javax.validation:validation-api:$javaxValidationApiVersion")
    implementation("javax.annotation:javax.annotation-api:$javaxAnnotationApiVersion")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

