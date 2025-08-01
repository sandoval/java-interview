plugins {
    java
    id("org.springframework.boot") version "3.4.6"
    id("io.spring.dependency-management") version "1.1.7"
    id("io.freefair.lombok") version "8.14"
}

group = "com.vingcard.athos"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.amazonaws:aws-java-sdk-cognitoidp:1.12.788")

    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("com.h2database:h2")

    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register<JavaExec>("populateDummyData") {
    group = "application"
    description = "Populates the database with dummy data"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("com.vingcard.athos.interview.InterviewApplication")
    args = listOf()
    systemProperties = mapOf("spring.profiles.active" to "dummydata")
}
