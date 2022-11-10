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

    // HTTP CLIENT
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-auth:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:${ktorVersion}")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    // TEST
    testImplementation(project(":spectacle-dsl-bdd"))
    testImplementation(project(":spectacle-dsl-assertions"))
    testImplementation(project(":spectacle-dsl-publisher"))
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        languageVersion = "1.6"
    }
}
