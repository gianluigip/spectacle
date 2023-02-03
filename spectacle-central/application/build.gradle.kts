import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

val ktorVersion = "2.1.3"
val logbackVersion = "1.4.4"

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    application
}

group = "io.github.gianluigip"
version = rootProject.version

repositories {
    mavenCentral()
}

kotlin {
    target.compilations.all {
        kotlinOptions.jvmTarget = "11"
    }
}

tasks.withType<Test> {
    inputs.property("Spectacle Enabled") {
        System.getenv("SPECIFICATION_PUBLISHER_CENTRAL_ENABLED") ?: "false"
    }
    useJUnitPlatform()
}

dependencies {
    // Internal
    implementation(project(":spectacle-common"))
    implementation(project(":spectacle-central:common"))
    implementation(project(":spectacle-central:domain"))

    // KOTLIN
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

    // KTOR
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-server-compression:$ktorVersion")
    implementation("io.ktor:ktor-server-cors:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    // DI
    implementation("org.kodein.di:kodein-di:7.16.0")

    // DATA
    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.41.1")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.postgresql:postgresql:42.5.3")
    implementation("org.flywaydb:flyway-core:9.8.2")

    // TEST
    testImplementation(project(":spectacle-dsl-bdd"))
    testImplementation(project(":spectacle-dsl-assertions"))
    testImplementation(project(":spectacle-dsl-publisher"))
    testImplementation(project(":spectacle-dsl-http"))
    testImplementation(project(":spectacle-central:domain", "testClasses"))
    testImplementation("io.ktor:ktor-client-core:$ktorVersion")
    testImplementation("io.ktor:ktor-client-auth:$ktorVersion")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testImplementation("io.ktor:ktor-server-test-host:${ktorVersion}")
    testImplementation("org.testcontainers:testcontainers:1.17.6")
    testImplementation("org.testcontainers:junit-jupiter:1.17.6")
    testImplementation("org.testcontainers:postgresql:1.17.6")
}

application {
    mainClass.set("io.gianluigip.spectacle.ApplicationKt")
}

// include JS artifacts in any JAR we generate
tasks.getByName<Jar>("jar") {
    val taskName = if (project.hasProperty("isProduction")
        || project.gradle.startParameter.taskNames.contains("installDist")
        || project.gradle.startParameter.taskNames.contains("stage")
    ) {
        ":spectacle-central:webapp:browserProductionWebpack"
    } else {
        ":spectacle-central:webapp:browserDevelopmentWebpack"
    }
    val webpackTask = tasks.getByPath(taskName) as KotlinWebpack
    dependsOn(webpackTask) // make sure JS gets compiled first
    from(File(webpackTask.destinationDirectory, webpackTask.outputFileName)) // bring output file along into the JAR
}

tasks.withType<Tar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Zip> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Sync> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

distributions {
    main {
        contents {
            from("$buildDir/libs") {
                rename("${project.name}-jvm", project.name)
                into("lib")
            }
        }
    }
}

// Alias "installDist" as "stage" (for cloud providers)
tasks.create("stage") {
    dependsOn(tasks.getByName("installDist"))
}

tasks.getByName<JavaExec>("run") {
    classpath(tasks.getByName<Jar>("jar")) // so that the JS artifacts generated by `jvmJar` can be found and served
}
