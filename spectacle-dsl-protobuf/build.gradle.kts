val ktorVersion = "2.1.3"

plugins {
    kotlin("jvm")
    id("convention.publication")
}

group = "io.github.gianluigip"
version = rootProject.version

repositories {
    mavenCentral()
}

kotlin {
    target.compilations.all {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    inputs.property("Spectacle Enabled") {
        System.getenv("SPECIFICATION_PUBLISHER_CENTRAL_ENABLED") ?: "false"
    }
    useJUnitPlatform()
}

dependencies {
    implementation(project(":spectacle-common"))
    implementation(project(":spectacle-dsl-bdd"))
    implementation(project(":spectacle-dsl-publisher"))

    implementation("io.grpc:grpc-protobuf:1.47.0")

    testImplementation(project(":spectacle-dsl-assertions"))
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        languageVersion = "1.4"
    }
}
