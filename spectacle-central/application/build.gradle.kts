import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

val jsWrapperVersion = "pre.304-kotlin-1.6.10"
val serializationVersion = "1.3.1"
val ktorVersion = "2.0.3"
val logbackVersion = "1.2.3"

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("io.github.turansky.kfc.webpack") version "5.0.0"
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
                implementation(project(":spectacle-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")
                // KTOR
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-auth:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
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
                implementation("io.ktor:ktor-server-core:$ktorVersion")
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-server-auth:$ktorVersion")
                implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-server-compression:$ktorVersion")
                implementation("io.ktor:ktor-server-cors:$ktorVersion")
                implementation("ch.qos.logback:logback-classic:$logbackVersion")
                implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
                // DI
                implementation("org.kodein.di:kodein-di:7.10.0")
                // DATA
                implementation("org.jetbrains.exposed:exposed-core:0.39.1")
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
                implementation("org.junit.jupiter:junit-jupiter-engine:5.9.0")
                implementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
                implementation("io.ktor:ktor-server-test-host:${ktorVersion}")
                implementation("org.testcontainers:testcontainers:1.16.3")
                implementation("org.testcontainers:junit-jupiter:1.17.3")
                implementation("org.testcontainers:postgresql:1.17.3")
            }
        }

        val jsMain by getting {
            dependencies {
                // KTOR
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.6.0")
                implementation("io.ktor:ktor-client-js:$ktorVersion")
                // REACT
                implementation(kotlinw("react:17.0.2"))
                implementation(kotlinw("react-dom:17.0.2"))
                implementation(kotlinw("react-css:17.0.2"))
                implementation(kotlinw("react-router-dom:6.2.1"))
                implementation(kotlinw("mui:5.4.3"))
                implementation(kotlinw("mui-icons:5.4.2"))
                implementation(npm("@emotion/react", "11.7.1"))
                implementation(npm("@emotion/styled", "11.6.0"))
                // Mermaid Diagrams
                implementation(npm("mermaid", "8.14.0"))
                // Markdown
                implementation(npm("react-markdown", "8.0.0"))
                implementation(npm("remark-gfm", "3.0.1"))
                implementation(npm("rehype-highlight", "5.0.2"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(project(":spectacle-dsl"))
                implementation(kotlin("test-js"))
            }
        }
    }
}

/**
 * Kotlin JS Wrappers
 */
fun kotlinw(target: String): String = "org.jetbrains.kotlin-wrappers:kotlin-$target-$jsWrapperVersion"

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
    classpath(tasks.getByName<Jar>("jvmJar")) // so that the JS artifacts generated by `jvmJar` can be found and served
}
