import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

val serializationVersion = "1.3.1"
val ktorServerVersion = "2.0.0-beta-1"
val ktorClientVersion = "1.6.7"
val logbackVersion = "1.2.3"

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    application
}

group = "io.github.gianluigip"
version = rootProject.version

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
        val jvmJar by tasks.getting(org.gradle.jvm.tasks.Jar::class) {
            doFirst {
                manifest {
                    attributes["Main-Class"] = "io.gianluigip.spectacle.ApplicationKt"
                }
                from(configurations.getByName("runtimeClasspath").map { if (it.isDirectory) it else zipTree(it) })
            }
        }
    }
    js(IR) {
        browser {
            binaries.executable()
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
                implementation("io.ktor:ktor-client-core:$ktorClientVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(project(":spectacle-central:domain"))
                implementation(kotlin("stdlib-jdk8"))
                // KTOR
                implementation("io.ktor:ktor-server-core:$ktorServerVersion")
                implementation("io.ktor:ktor-server-netty:$ktorServerVersion")
                implementation("io.ktor:ktor-server-content-negotiation:$ktorServerVersion")
                implementation("io.ktor:ktor-server-compression:$ktorServerVersion")
                implementation("io.ktor:ktor-server-cors:$ktorServerVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorServerVersion")
                implementation("ch.qos.logback:logback-classic:$logbackVersion")
                implementation("io.ktor:ktor-server-call-logging:$ktorServerVersion")
                // DI
                implementation("org.kodein.di:kodein-di:7.10.0")
                // DATA
                implementation("org.jetbrains.exposed:exposed-core:0.37.3")
                implementation("org.jetbrains.exposed:exposed-jdbc:0.37.3")
                implementation("org.jetbrains.exposed:exposed-java-time:0.37.3")
                implementation("com.zaxxer:HikariCP:5.0.1")
                implementation("org.postgresql:postgresql:42.3.2")
                implementation("org.flywaydb:flyway-core:8.4.4")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(project(":spectacle-dsl"))
                implementation(project(":spectacle-central:domain", "testClasses"))
                implementation("org.junit.jupiter:junit-jupiter-engine:5.7.2")
                implementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
                implementation("io.ktor:ktor-server-test-host:${ktorServerVersion}")
                implementation("io.ktor:ktor-client-content-negotiation:${ktorServerVersion}")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorServerVersion")
                implementation("org.testcontainers:testcontainers:1.16.3")
                implementation("org.testcontainers:junit-jupiter:1.16.3")
                implementation("org.testcontainers:postgresql:1.16.3")
            }
        }

        val jsMain by getting {
            dependencies {
                // KTOR
                implementation("io.ktor:ktor-client-js:$ktorClientVersion")
                implementation("io.ktor:ktor-client-json:$ktorClientVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorClientVersion")
                // REACT
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react:17.0.2-pre.294-kotlin-1.6.10")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:17.0.2-pre.294-kotlin-1.6.10")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-styled-next:1.0-pre.294-kotlin-1.6.10")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-mui:5.4.0-pre.294-kotlin-1.6.10")
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

application {
    mainClass.set("io.gianluigip.spectacle.ApplicationKt")
}

// include JS artifacts in any JAR we generate
tasks.getByName<Jar>("jvmJar") {
    val taskName = if (project.hasProperty("isProduction")
        || project.gradle.startParameter.taskNames.contains("installDist")
        || project.gradle.startParameter.taskNames.contains("stage")
    ) {
        "jsBrowserProductionWebpack"
    } else {
        "jsBrowserDevelopmentWebpack"
    }
    val webpackTask = tasks.getByName<KotlinWebpack>(taskName)
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
                rename("${rootProject.name}-jvm", rootProject.name)
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
    classpath(tasks.getByName<Jar>("jvmJar")) // so that the JS artifacts generated by `jvmJar` can be found and served
}
