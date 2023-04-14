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
    js { browser() }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":spectacle-common"))
                implementation(project(":spectacle-dsl:spectacle-dsl-bdd"))
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(project(":spectacle-dsl:spectacle-dsl-assertions"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")
                implementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
                implementation("com.github.tomakehurst:wiremock-jre8:2.35.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
            }
        }
        val jsMain by getting {
            dependencies {
                // HTTP CLIENT
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-auth:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:${ktorVersion}")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
            }
        }
        val jsTest by getting {}
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        languageVersion = "1.4"
    }
}
