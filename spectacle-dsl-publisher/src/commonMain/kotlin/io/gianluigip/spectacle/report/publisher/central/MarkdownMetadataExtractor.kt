package io.gianluigip.spectacle.report.publisher.central

/**
 * Valid Chars:
 *      * Any word
 *      * Any number
 *      * Space
 *      * ,
 *      * .
 *      * -
 *      * [
 *      * ]
 *      * /
 *      * \
 */
private val VALID_CHARS = """[\[\]\w\s\d,\\\/\.-]"""
private val TITLE_REGEX = Regex("""\{\{\s*title\s*:\s*($VALID_CHARS*)\s*\}\}""")
private val TEAM_REGEX = Regex("""\{\{\s*team\s*:\s*($VALID_CHARS*)\s*\}\}""")
private val FEATURES_REGEX = Regex("""\{\{\s*features\s*:\s*($VALID_CHARS*)\s*\}\}""")
private val TAGS_REGEX = Regex("""\{\{\s*tags\s*:\s*($VALID_CHARS*)\s*\}\}""")

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
