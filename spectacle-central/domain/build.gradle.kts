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
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    testImplementation(project(":spectacle-dsl:spectacle-dsl-bdd"))
    testImplementation(project(":spectacle-dsl:spectacle-dsl-assertions"))
    testImplementation(project(":spectacle-dsl:spectacle-dsl-publisher"))
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testImplementation("io.mockk:mockk:1.13.5")
}

kotlin {
    target.compilations.all {
        kotlinOptions.jvmTarget = "17"
    }
}

tasks.withType<Test> {
    inputs.property("Spectacle Enabled") {
        System.getenv("SPECIFICATION_PUBLISHER_CENTRAL_ENABLED") ?: "false"
    }
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
