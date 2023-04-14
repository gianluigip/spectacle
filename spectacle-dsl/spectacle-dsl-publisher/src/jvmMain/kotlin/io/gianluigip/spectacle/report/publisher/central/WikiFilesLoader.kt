package io.gianluigip.spectacle.report.publisher.central

import io.gianluigip.spectacle.report.config.ReportConfiguration
import io.gianluigip.spectacle.wiki.api.model.WikiPageRequest
import java.io.File
import java.nio.file.Files
import java.security.MessageDigest


actual object WikiFilesLoader {

    actual fun loadWikiPages(config: ReportConfiguration): List<WikiPageRequest> {
        val baseFolder = File(config.centralConfig.localWikiLocation!!)
        if (!baseFolder.exists()) {
            println("The wiki folder doesn't exist ${baseFolder.absolutePath}")
        }
        println("Looking wiki pages in '${baseFolder.absolutePath}'")
        val files = findAllFiles(baseFolder, mutableListOf())
        val markdownFiles = files.filter { it.absolutePath.endsWith(".md") }
        markdownFiles.forEach {
            println("Found markdown files ${it.absolutePath}")
        }
        return markdownFiles.map { it.toWikiPage(baseFolder, config) }
    }
}

private fun findAllFiles(file: File, files: MutableList<File>): List<File> {
    if (file.isFile) {
        files += file
        return files
    }
    file.listFiles()?.forEach {
        findAllFiles(it, files)
    }
    return files
}

private fun File.toWikiPage(baseFolder: File, config: ReportConfiguration): WikiPageRequest {
    val path = absolutePath.replace(baseFolder.absolutePath, "").replace(name, "").run {
        if (length > 1 && endsWith("/")) {
            substring(0, length - 1)
        } else this
    }
    val content = String(Files.readAllBytes(toPath()))
    val checksum = getSHA256Hash(content)
    return WikiPageRequest(
        title = content.extractTitleFromWikiPage() ?: name,
        fileName = name,
        path = path,
        content = content,
        checksum = checksum,
        team = content.extractTeamFromWikiPage() ?: config.team,
        tags = content.extractTagsFromWikiPage(),
        features = content.extractFeaturesFromWikiPage(),
        source = config.source,
        component = config.component,
    )
}

internal fun getSHA256Hash(data: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hash = digest.digest(data.toByteArray(charset("UTF-8")))
    return bytesToHex(hash)

}

private fun bytesToHex(hash: ByteArray): String {
    val hexString = StringBuilder(2 * hash.size)
    for (i in hash.indices) {
        val hex = Integer.toHexString(0xff and hash[i].toInt())
        if (hex.length == 1) {
            hexString.append('0')
        }
        hexString.append(hex)
    }
    return hexString.toString()
}
