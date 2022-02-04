plugins {
    kotlin("jvm")
}

group = "io.github.gianluigip"
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation(project(":spectacle-dsl"))
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testImplementation("io.mockk:mockk:1.12.0")
}

kotlin {
    target.compilations.all {
        kotlinOptions.jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
