plugins {
	java
	id("org.springframework.boot") version "3.4.6"
	id("io.spring.dependency-management") version "1.1.7"
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
	// implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	// implementation("org.springframework.boot:spring-boot-starter-security")
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	// testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testRuntimeOnly("com.h2database:h2")
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

// React frontend build tasks
tasks.register<Exec>("npmInstall") {
    group = "frontend"
    description = "Install React dependencies"
    workingDir = file("frontend")
    commandLine = if (System.getProperty("os.name").lowercase().contains("windows")) {
        listOf("cmd", "/c", "npm", "install")
    } else {
        listOf("npm", "install")
    }
    inputs.file("frontend/package.json")
    inputs.file("frontend/package-lock.json")
    outputs.dir("frontend/node_modules")
}

tasks.register<Exec>("npmBuild") {
    group = "frontend"
    description = "Build React application"
    workingDir = file("frontend")
    commandLine = if (System.getProperty("os.name").lowercase().contains("windows")) {
        listOf("cmd", "/c", "npm", "run", "build")
    } else {
        listOf("npm", "run", "build")
    }
    dependsOn("npmInstall")
    inputs.dir("frontend/src")
    inputs.dir("frontend/public")
    inputs.file("frontend/package.json")
    outputs.dir("frontend/build")
}

tasks.register<Copy>("copyFrontend") {
    group = "frontend"
    description = "Copy React build to Spring Boot static resources"
    dependsOn("npmBuild")
    from("frontend/build")
    into("src/main/resources/static")
}

tasks.named("processResources") {
    dependsOn("copyFrontend")
}

