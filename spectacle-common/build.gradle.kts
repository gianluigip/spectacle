plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("convention.publication")
}

group = "io.github.gianluigip"
version = rootProject.version

repositories {
    mavenCentral()
}

kotlin {
    jvm {}
    js { browser() }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
            }
        }
    }
}
