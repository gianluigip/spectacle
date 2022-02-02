plugins {
    kotlin("jvm") version "1.6.10"
}

group = "io.github.gianluigip"
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:1.6.1")
    implementation("io.ktor:ktor-server-netty:1.6.1")
    implementation("io.ktor:ktor-serialization:1.6.1")
    implementation("io.ktor:ktor-html-builder:1.6.1")
    implementation("io.ktor:ktor-freemarker:1.6.1")
    implementation("ch.qos.logback:logback-classic:1.2.3")

    testImplementation("io.ktor:ktor-server-test-host:1.6.1")
    testImplementation(project(":spectacle-dsl"))
}

kotlin {
    target.compilations.all {
        kotlinOptions.jvmTarget = "11"
    }
}
