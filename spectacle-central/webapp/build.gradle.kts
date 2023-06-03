val jsWrapperVersion = "pre.365"
val serializationVersion = "1.3.1"
val ktorVersion = "2.3.0"

plugins {
    kotlin("js")
}

group = "io.github.gianluigip"
version = rootProject.version

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

kotlin.js {
    browser()
    binaries.executable()
}

dependencies {
    // Internal
    implementation(project(":spectacle-common"))
    implementation(project(":spectacle-central:common"))

    // KTOR
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-auth:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.7.0")
    implementation("io.ktor:ktor-client-js:$ktorVersion")

    // REACT
    implementation(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:1.0.0-pre.556"))
    implementation(kotlinw("emotion"))
    implementation(kotlinw("react"))
    implementation(kotlinw("react-dom"))
    implementation(kotlinw("react-router-dom"))
    implementation(kotlinw("mui"))
    implementation(kotlinw("mui-icons"))
    implementation(npm("@emotion/react", "11.7.1"))
    implementation(npm("@emotion/styled", "11.6.0"))
    implementation(npm("date-fns", "2.28.0"))
    implementation(npm("@date-io/date-fns", "2.14.0"))

    // Mermaid Diagrams
    implementation(npm("mermaid", "8.14.0"))

    // Markdown
    implementation(npm("react-markdown", "8.0.0"))
    implementation(npm("remark-gfm", "3.0.1"))
    implementation(npm("rehype-highlight", "5.0.2"))

    // Tests
    testImplementation(project(":spectacle-dsl:spectacle-dsl-bdd"))
    testImplementation(project(":spectacle-dsl:spectacle-dsl-assertions"))
    testImplementation(project(":spectacle-dsl:spectacle-dsl-publisher"))
    testImplementation(kotlin("test-js"))
}

fun kotlinw(target: String): String = "org.jetbrains.kotlin-wrappers:kotlin-$target"
