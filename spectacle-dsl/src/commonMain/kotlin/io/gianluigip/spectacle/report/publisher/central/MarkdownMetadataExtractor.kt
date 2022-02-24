package io.gianluigip.spectacle.report.publisher.central

private val TITLE_REGEX = Regex("""\{\{\s*title\s*:\s*([\w\s\d,]*)\s*\}\}""")
private val TEAM_REGEX = Regex("""\{\{\s*team\s*:\s*([\w\s\d,]*)\s*\}\}""")
private val FEATURES_REGEX = Regex("""\{\{\s*features\s*:\s*([\w\s\d,]*)\s*\}\}""")
private val TAGS_REGEX = Regex("""\{\{\s*tags\s*:\s*([\w\s\d,]*)\s*\}\}""")

fun String.extractTitleFromWikiPage(): String? {
    return TITLE_REGEX.find(this)?.groupValues?.last()?.trim()
}

fun String.extractTeamFromWikiPage(): String? {
    return TEAM_REGEX.find(this)?.groupValues?.last()?.trim()
}

fun String.extractFeaturesFromWikiPage(): List<String> {
    return FEATURES_REGEX.find(this)?.groupValues?.last()?.trim()?.split(",")?.map { it.trim() } ?: emptyList()
}

fun String.extractTagsFromWikiPage(): List<String> {
    return TAGS_REGEX.find(this)?.groupValues?.last()?.trim()?.split(",")?.map { it.trim() } ?: emptyList()
}