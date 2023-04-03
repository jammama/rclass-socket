import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.2"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.0"
//    kotlin("plugin.allopen") version "1.8.0"
    kotlin("plugin.spring") version "1.8.0"
//    kotlin("kapt") version "1.8.0"
}

group = "com.learnershi"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    // webflux
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // web socket
    implementation("org.springframework.boot:spring-boot-starter-websocket")

    // mongo
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")

    // springdoc: openApi
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.0.4")
    implementation("org.springdoc:springdoc-openapi-starter-common")

    // jackson for kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // kafka
    implementation("org.springframework.kafka:spring-kafka")

    // actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("org.apache.pdfbox:pdfbox:2.0.16")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += "-Xjsr305=strict"
    }
}
tasks.withType<Test> {
    useJUnitPlatform()
}


