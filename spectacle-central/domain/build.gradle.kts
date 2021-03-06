plugins {
    kotlin("jvm")
}

group = "io.github.gianluigip"
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":spectacle-common"))
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

/**
 * Generate test classes for the application module
 */
configurations {
    register("testClasses") {
        extendsFrom(testImplementation.get())
    }
}
val testJar = tasks.register<Jar>("testJar") {
    archiveBaseName.set("domain-tests")
    from(project.the<SourceSetContainer>()["test"].output)
}
artifacts.add("testClasses", testJar)
