plugins {
    kotlin("multiplatform") version "1.7.10" apply false
    kotlin("plugin.serialization") version "1.7.10" apply false
    kotlin("jvm") version "1.7.10" apply false
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

group = "io.github.gianluigip"
version = "2.1.1"

repositories {
    mavenCentral()
}

/**
 * Sonatype Release Config
 */
ext["ossrhUsername"] = ""
ext["ossrhPassword"] = ""
ext["sonatypeStagingProfileId"] = ""
// Grabbing secrets from local.properties file or from environment variables, which could be used on CI
val secretPropsFile = project.rootProject.file("local.properties")
if (secretPropsFile.exists()) {
    secretPropsFile.reader().use {
        java.util.Properties().apply {
            load(it)
        }
    }.onEach { (name, value) ->
        ext[name.toString()] = value
    }
} else {
    ext["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
    ext["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
    ext["sonatypeStagingProfileId"] = System.getenv("SONATYPE_STAGING_PROFILE_ID")
}
fun getExtraString(name: String) = ext[name]?.toString()

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            stagingProfileId.set(getExtraString("sonatypeStagingProfileId"))
            username.set(getExtraString("ossrhUsername"))
            password.set(getExtraString("ossrhPassword"))
        }
    }
}
