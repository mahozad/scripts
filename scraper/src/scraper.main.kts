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
import org.jsoup.nodes.Document
import java.io.File
import java.io.PrintStream

val baseUrl = "https://www.mckinseyenergyinsights.com"
val apiUrl = "$baseUrl/Umbraco/Api/Glossary/GetKeywords"
val output = File("result.txt")
val parser = Klaxon()
var totalWordCount = 0

data class Entry(val name: String, val url: String)

// TODO: Use .parallelStream() or coroutines
System.setOut(PrintStream(output))
('A'..'Z')
    .asSequence()
    .onEach { println("\n============ $it ============") }
    .flatMap { it.getEntries() }
    .onEach { totalWordCount++ }
    .onEach { println("* ${it.name}\t${it.getMeaning()}") }
    .count()
println()
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
    val json = document.extractJsonArray()
    return parser.parseArray(json) ?: error("Error parsing the JSON")
}

fun Document.extractJsonArray() = this
    .body()
    .text()
    .removePrefix("""{"keyWords":""")
    .removeSuffix("}")

/**
 * See [jsoup selector syntax](https://jsoup.org/cookbook/extracting-data/selector-syntax).
 */
fun Entry.getMeaning() =
    Jsoup.connect("$baseUrl$url")
        .userAgent("Mozilla")
        .get()
        .select(".content-wrapper")
        .single()
        .children()
        .not("h1") // Title
        .not(":contains(Author:)")
        .text()
