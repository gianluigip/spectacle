package io.gianluigip.spectacle.report.config

import io.gianluigip.spectacle.report.publisher.Publishers
import io.gianluigip.spectacle.report.publisher.SpecificationPublisher
import io.gianluigip.spectacle.report.publisher.TerminalPublisher
import java.io.FileInputStream
import java.net.URL
import java.util.Properties

actual object ConfigLoader {

    private const val PROPERTIES_FILE_NAME = "spectacle.properties"
    actual val CONFIG: ReportConfiguration

    init {
        Publishers.loadPublishers()
        val propertyResource = javaClass.classLoader.getResource(PROPERTIES_FILE_NAME)
        if (propertyResource != null) {
            val properties = loadProperties(propertyResource)
            CONFIG = ReportConfiguration(
                team = properties.getTeam(),
                source = properties.getSource(),
                component = properties.getComponent(),
                publishers = properties.getPublishers(),
                centralConfig = CentralPublisherConfig(
                    enabled = properties.getCentralEnabled(),
                    host = properties.getCentralHost(),
                    username = properties.getCentralUsername(),
                    password = properties.getCentralPassword(),
                    wikiEnabled = properties.getCentralWikiEnabled(),
                    localWikiLocation = properties.getLocalWikiLocation(),
                ),
            )
        } else {
            CONFIG = ReportConfiguration(
                team = "Other",
                source = "Other",
                component = "Other",
                publishers = listOf(TerminalPublisher),
                centralConfig = CentralPublisherConfig(
                    enabled = false,
                    host = null,
                    username = "",
                    password = "",
                    wikiEnabled = false,
                    localWikiLocation = null,
                )
            )
            println("If you want to customize the behavior of Spectacle Reports create a file $PROPERTIES_FILE_NAME in the test resource folder")
        }
    }

    private fun loadProperties(propertiesUrl: URL): Properties {
        val reportProperties = Properties()
        reportProperties.load(FileInputStream(propertiesUrl.file))
        return reportProperties
    }

    private fun Properties.getTeam(): String = getProperty("specification.team") ?: "Other"
    private fun Properties.getSource(): String = getProperty("specification.source") ?: "Other"
    private fun Properties.getComponent(): String = getProperty("specification.component") ?: "Other"

    private fun Properties.getPublishers(): List<SpecificationPublisher> {
        val publisherProperties = getProperty("specification.publisher") ?: return listOf(TerminalPublisher)
        return publisherProperties.split(",").map { it.trim() }.map {
            val publisher = SpecificationPublisher.findPublisher(it)
            if (publisher == null) {
                println("The publisher $it is not supported")
                return@map null
            }
            publisher
        }.filterNotNull()
    }

    private fun Properties.getCentralEnabled(): Boolean {
        val envValue: String? = System.getenv(CENTRAL_ENABLE_ENV_VARIABLE)
        if (envValue != null) {
            return envValue.toBoolean()
        }
        return getProperty("specification.publisher.central.enabled")?.toBoolean() ?: false
    }

    private fun Properties.getCentralHost(): Url? {
        val envValue: String? = System.getenv(CENTRAL_HOST_ENV_VARIABLE)
        if (envValue != null) {
            return Url(envValue)
        }
        return getProperty("specification.publisher.central.host")?.let { Url(it) }
    }

    private fun Properties.getCentralUsername(): String {
        val envValue: String? = System.getenv(CENTRAL_USERNAME_ENV_VARIABLE)
        if (envValue != null) {
            return envValue
        }
        return getProperty("specification.publisher.central.username") ?: ""
    }

    private fun Properties.getCentralPassword(): String {
        val envValue: String? = System.getenv(CENTRAL_PASSWORD_ENV_VARIABLE)
        if (envValue != null) {
            return envValue
        }
        return getProperty("specification.publisher.central.password") ?: ""
    }

    private fun Properties.getCentralWikiEnabled(): Boolean = getProperty("specification.publisher.central.wiki.enabled")?.toBoolean() ?: false
    private fun Properties.getLocalWikiLocation(): String? = getProperty("specification.publisher.central.wiki.localFolderLocation")

}