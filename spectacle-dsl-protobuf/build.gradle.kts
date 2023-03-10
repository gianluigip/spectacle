val ktorVersion = "2.2.3"

plugins {
    kotlin("multiplatform")
    id("convention.publication")
}

group = "io.github.gianluigip"
version = rootProject.version

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            inputs.property("Spectacle Enabled") {
                System.getenv("SPECIFICATION_PUBLISHER_CENTRAL_ENABLED") ?: "false"
            }
            useJUnitPlatform()
        }
    }

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":spectacle-common"))
                implementation(project(":spectacle-dsl-bdd"))
                implementation(project(":spectacle-dsl-publisher"))

                implementation("io.grpc:grpc-protobuf:1.53.0")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(project(":spectacle-dsl-assertions"))
                implementation("org.junit.jupiter:junit-jupiter-engine:5.9.1")
                implementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        languageVersion = "1.4"
    }
}
