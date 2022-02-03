plugins {
    kotlin("jvm") version "1.6.10"
}

group = "io.github.gianluigip"
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
}

kotlin {
    target.compilations.all {
        kotlinOptions.jvmTarget = "11"
    }
}
