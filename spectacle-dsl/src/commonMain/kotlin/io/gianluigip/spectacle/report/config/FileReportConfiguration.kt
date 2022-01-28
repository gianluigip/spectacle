package io.gianluigip.spectacle.report.config

import io.gianluigip.spectacle.report.publisher.SpecificationPublisher
import io.gianluigip.spectacle.report.publisher.TerminalPublisher
import java.io.FileInputStream
import java.net.URL
import java.util.Properties

private const val PROPERTIES_FILE_NAME = "spectacle.properties"

object FileReportConfiguration : ReportConfiguration {

    override val team: String
    override val source: String
    override val component: String
    override val publishers: List<SpecificationPublisher>

    init {
        val propertyResource = javaClass.classLoader.getResource(PROPERTIES_FILE_NAME)
        if (propertyResource != null) {
            val properties = loadProperties(propertyResource)
            team = properties.getTeam()
            source = properties.getSource()
            component = properties.getSource()
            publishers = properties.getPublishers()
        } else {
            team = "Other"
            source = "Other"
            component = "Other"
            publishers = listOf(TerminalPublisher)
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
}
