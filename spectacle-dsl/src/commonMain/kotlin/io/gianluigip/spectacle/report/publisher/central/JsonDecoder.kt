package io.gianluigip.spectacle.report.publisher.central

private val JSON_KEY_REGEX = Regex("""\s*"\w+"\s*:""")
private val JSON_ARRAY_STRINGS_REGEX = Regex(""""\s*,\s*""")
private val JSON_ARRAY_OBJECTS_REGEX = Regex("""\}\s*,\s*""")

internal fun extractMapFromJson(json: String): Map<String, String> {

    val innerJson = json.replaceFirst("{", "")
        .substring(0, json.lastIndexOf("}") - 1)

    val keyPairs = mutableMapOf<String, String>()
    val keys = JSON_KEY_REGEX.findAll(innerJson).toList()
    var startKey = keys.first().range.first
    for (i in 1 until keys.size) {
        val endKey = keys[i].range.first
        innerJson.substring(startKey, endKey)
            .extractKeyValuePair()
            .also { keyPairs[it.first] = it.second }
        startKey = endKey
    }
    innerJson.substring(startKey)
        .extractKeyValuePair()
        .also { keyPairs[it.first] = it.second }

    return keyPairs
}

private fun String.extractKeyValuePair(): Pair<String, String> {
    val pair = split(":", limit = 2)
    return pair.first().replace("\"", "").trim() to pair.last().trim().extractValue()
}

private fun String.extractValue(): String {
    val raw = if (endsWith(",")) substring(0, length - 1) else this
    return when {
        raw.startsWith("\"") && raw.endsWith("\"") -> raw.removeFirstAndLastChar()
        raw.startsWith("[") && raw.endsWith("]") -> raw.removeFirstAndLastChar()
        else -> raw
    }
}

internal fun String.decodeArrayOfStrings(): List<String> {
    val array = trim().run {
        when {
            startsWith("[") && endsWith("]") -> removeFirstAndLastChar()
            else -> this
        }
    }
    if (array.isEmpty() || array.isBlank()) return emptyList()

    val splitPoints = JSON_ARRAY_STRINGS_REGEX.findAll(array).toList()
    if (splitPoints.isEmpty()) return listOf(array.removeFirstAndLastChar())

    val values = mutableListOf<String>()
    var start = 0
    splitPoints.forEachIndexed { index, matchResult ->

        if (array[matchResult.range.first - 1] != '\\') {
            values += array.substring(start + 1, matchResult.range.first)
            start = matchResult.range.last + 1
        }
    }
    values += array.substring(start + 1, array.length - 1)
    return values
}

internal fun String.decodeArrayOfObjects(): List<String> {
    val array = trim().run {
        when {
            startsWith("[") && endsWith("]") -> removeFirstAndLastChar().trim()
            else -> this
        }
    }
    if (array.isEmpty() || array.isBlank()) return emptyList()

    val splitPoints = JSON_ARRAY_OBJECTS_REGEX.findAll(array).toList()
    if (splitPoints.isEmpty()) return listOf(array)

    val values = mutableListOf<String>()
    var start = 0
    splitPoints.forEach { matchResult ->
        if (array[matchResult.range.first - 1] != '\\') {
            values += array.substring(start, matchResult.range.first + 1)
            start = matchResult.range.last + 1
        }
    }
    values += array.substring(start, array.length)
    return values
}

private fun String.removeFirstAndLastChar() = substring(1, length - 1)
