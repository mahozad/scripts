#!kotlinc -script -jvm-target 11 -cp "path\to\kotlinc\lib\kotlin-main-kts.jar"

/*
 * A Kotlin script for extracting (scraping) all the words and their meanings from
 * https://www.mckinseyenergyinsights.com/resources/refinery-reference-desk/
 */

@file:JvmName("Scraper")
@file:CompilerOptions("-jvm-target", "11")
@file:Repository("https://repo.maven.apache.org/maven2")
@file:Repository("https://jcenter.bintray.com")
@file:Repository("https://jitpack.io")
@file:DependsOn("org.jsoup:jsoup:1.14.3")
@file:DependsOn("com.beust:klaxon:5.5")

import com.beust.klaxon.Klaxon
import org.jsoup.Jsoup
import java.io.File
import java.io.PrintStream

val baseUrl = "https://www.mckinseyenergyinsights.com"
val apiUrl = "$baseUrl/Umbraco/Api/Glossary/GetKeywords"
val output = File("result.txt")
val parser = Klaxon()
var totalWordCount = 0

data class Entry(val name: String, val url: String)

System.setOut(PrintStream(output))
for (character in 'A'..'Z') {
    println("============ $character ============")
    // TODO: Use .parallelStream() or coroutines
    val entries = character.getEntries()
    totalWordCount += entries.size
    for (entry in entries) {
        println("* ${entry.name}\t${entry.meaning}")
    }
    println()
}
println("---------------------------")
println("Total word count: $totalWordCount")

/**
 * See [this stackoverflow post](https://stackoverflow.com/a/69821965)
 * and [this MDN article](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/POST).
 */
fun Char.getEntries(): List<Entry> {
    val document = Jsoup.connect(apiUrl)
        .userAgent("Mozilla")
        .header("content-type", "application/json")
        .header("accept", "application/json")
        .requestBody("""{"key": "$this", "nodeID": 4324}""")
        .ignoreContentType(true)
        .post()

    val json = document
        .body()
        .text()
        .removePrefix("""{"keyWords":""")
        .removeSuffix("}")

    return parser.parseArray(json) ?: error("Error parsing the JSON")
}

val Entry.meaning: String
    get() = Jsoup.connect("$baseUrl$url")
        .userAgent("Mozilla")
        .get()
        .select(".content-wrapper")
        .single()
        .children()
        .not("h1") // Title
        .not(":contains(Author:)")
        .text()
