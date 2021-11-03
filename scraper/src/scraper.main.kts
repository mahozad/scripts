#!kotlinc -script -jvm-target 11 -cp "path\to\kotlinc\lib\kotlin-main-kts.jar"

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

val apiUrl = "https://www.mckinseyenergyinsights.com/Umbraco/Api/Glossary/GetKeywords"
val baseUrl = "https://www.mckinseyenergyinsights.com"

val output = File("C:/Users/Mahdi/Desktop/result.txt")
System.setOut(PrintStream(output))

var totalWordCount = 0
for (character in 'A'..'Z') {
    println("============$character============")
    // TODO: Use .parallelStream() or coroutines
    val entries = character.getEntries()
    totalWordCount += entries.size
    for (entry in entries) {
        println("* ${entry.name}\t${entry.meaning}")
    }
    println()
}
println("-------------------------")
println("Total word count: $totalWordCount")

data class Entry(val name: String, val url: String)

/**
 * See [this stackoverflow post](https://stackoverflow.com/a/69821965)
 * and [this MDN article](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/POST).
 */
fun Char.getEntries(): List<Entry> {
    val document = Jsoup.connect(apiUrl)
        .userAgent("Mozilla")
        .header("content-type", "application/json")
        .header("accept", "application/json")
        .requestBody("""{key: "$this", nodeID: 4324}""")
        .ignoreContentType(true)
        .post()

/*
    val document = Jsoup.connect(apiUrl)
            .userAgent("Mozilla")
            .header("content-type", "application/x-www-form-urlencoded")
            .header("accept", "application/json")
            .data("key", "$this")
            .data("nodeID", "4324")
            // OR
            //.data(mapOf("key" to "$this", "nodeID" to "4324"))
            .ignoreContentType(true)
            .ignoreHttpErrors(true)
            .post()
*/

    val response = document.body()
        .toString()
        .removePrefix("<body>")
        .removeSuffix("</body>")
        .replace("""{"keyWords":""", "")
        .replace("]}", "]")
        .trim()

    return Klaxon().parseArray(response)!!
}

//fun Entry.getMeaning(): String {
//    val content = Jsoup.connect("$baseUrl$url")
//            .userAgent("Mozilla")
//            .get()
//            .select(".content-wrapper")
//            .single()
//            .children()
//    content.removeIf { it.`is`("h1") || it.text().contains("Author:") }
//    return content.text()
//}

val Entry.meaning: String
    get() {
        val content = Jsoup.connect("$baseUrl$url")
            .userAgent("Mozilla")
            .get()
            .select(".content-wrapper")
            .single()
            .children()
        content.removeIf { it.`is`("h1") || it.text().contains("Author:") }
        return content.text()
    }
