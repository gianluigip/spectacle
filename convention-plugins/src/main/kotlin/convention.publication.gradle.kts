import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.`maven-publish`
import org.gradle.kotlin.dsl.signing
import java.util.*

plugins {
    `maven-publish`
    signing
}

// Stub secrets to let the project sync and build without the publication values set up
ext["signing.keyId"] = null
ext["signing.password"] = null
ext["signing.key"] = null
ext["ossrhUsername"] = null
ext["ossrhPassword"] = null
ext["sonatypeStagingProfileId"] = null

// Grabbing secrets from local.properties file or from environment variables, which could be used on CI
val secretPropsFile = project.rootProject.file("local.properties")
if (secretPropsFile.exists()) {
    secretPropsFile.reader().use {
        Properties().apply {
            load(it)
        }
    }.onEach { (name, value) ->
        ext[name.toString()] = value
    }
} else {
    ext["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
    ext["signing.password"] = System.getenv("SIGNING_PASSWORD")
    ext["signing.key"] = System.getenv("SIGNING_KEY")
    ext["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
    ext["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
    ext["sonatypeStagingProfileId"] = System.getenv("SONATYPE_STAGING_PROFILE_ID")
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

fun getExtraString(name: String) = ext[name]?.toString()

publishing {
    // Configure maven central repository
    repositories {
        maven {
            name = "sonatype"
            setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = getExtraString("ossrhUsername")
                password = getExtraString("ossrhPassword")
            }
        }
    }

    // Configure all publications
    publications.withType<MavenPublication> {

        // Stub javadoc.jar artifact
        artifact(javadocJar.get())

        // Provide artifacts information requited by Maven Central
        pom {
            name.set("Spectacle DSL")
            description.set("DSL for writing BDD tests")
            url.set("https://github.com/gianluigip/spectacle")

            licenses {
                license {
                    name.set("GNU Lesser General Public License v3.0")
                    url.set("https://opensource.org/licenses/LGPL-3.0")
                }
            }
            developers {
                developer {
                    id.set("gianluigip")
                    name.set("Gianluigi Pierini")
                    email.set("gluigip@hotmail.com")
                }
            }
            scm {
                url.set("https://github.com/gianluigip/spectacle")
            }

        }
    }
}

// Signing artifacts. Signing.* extra properties values will be used

signing {
    useInMemoryPgpKeys(
        getExtraString("signing.keyId"),
        getExtraString("signing.key"),
        getExtraString("signing.password"),
    )
    sign(publishing.publications)
}
