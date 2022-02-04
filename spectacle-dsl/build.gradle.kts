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
            useJUnitPlatform()
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common"))
                implementation(kotlin("test"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter-engine:5.7.2")
                implementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
            }
        }
    }
}
