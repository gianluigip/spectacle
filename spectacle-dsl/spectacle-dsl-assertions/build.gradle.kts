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
                implementation(kotlin("test"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(project(":spectacle-common"))
                implementation(project(":spectacle-dsl:spectacle-dsl-bdd"))
                implementation(project(":spectacle-dsl:spectacle-dsl-publisher"))
                implementation("org.junit.jupiter:junit-jupiter-engine:5.9.3")
                implementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
            }
        }
    }
}
