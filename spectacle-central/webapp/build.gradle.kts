val jsWrapperVersion = "pre.304-kotlin-1.6.10"
val serializationVersion = "1.3.1"
val ktorVersion = "2.0.3"

plugins {
    kotlin("js")
}

group = "io.github.gianluigip"
version = rootProject.version

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

kotlin {
    js(IR) {
        browser {
            binaries.executable()
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
}

dependencies {
    // Internal
    implementation(project(":spectacle-central:common"))

    // KTOR
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-auth:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.6.4")
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

    // Tests
    testImplementation(project(":spectacle-dsl"))
    testImplementation(kotlin("test-js"))
}

/**
 * Kotlin JS Wrappers
 */
fun kotlinw(target: String): String = "org.jetbrains.kotlin-wrappers:kotlin-$target-$jsWrapperVersion"
