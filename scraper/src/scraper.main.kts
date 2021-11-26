#!/usr/bin/env kotlin

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
//  See [this post](https://stackoverflow.com/a/60269837/8583692)
System.setOut(PrintStream(output))
('A'..'Z')
    .asSequence()
    .onEach { println("\n============ $it ============") }
    .flatMap(::fetchEntries)
    .onEach { totalWordCount++ }
    .forEach { println("${it.name} | ${it.fetchMeaning()}") }
println()
println("---------------------------")
println("Total word count: $totalWordCount")

/**
 * See [this stackoverflow post](https://stackoverflow.com/a/69821965)
 * and [this MDN article](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/POST).
 */
fun fetchEntries(char: Char): List<Entry> {
    val document = Jsoup.connect(apiUrl)
        .userAgent("jsoup/1.14.3")
        .referrer(baseUrl)
        .header("content-type", "application/json")
        .header("accept", "application/json")
        .requestBody("""{"key": "$char", "nodeID": 4324}""")
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
fun Entry.fetchMeaning() =
    Jsoup.connect("$baseUrl$url")
        .userAgent("jsoup/1.14.3")
        .referrer(baseUrl)
        .get()
        .select(".content-wrapper")
        .single()
        .children()
        .not("h1") // Title
        .not(":contains(Author:)")
        .text()

/**
 * Here is another way to scrape a dynamic page.
 * We are using Selenium WebDriver to get the full HTML page and then parse the document with jsoup.
 * We could also have used HtmlUnit, but it does not seem to work well with XHR or AJAX requests in page.
 *
 * To use this approach, add Selenium to dependencies:
 * `org.seleniumhq.selenium:selenium-java:4.0.0`
 *
 * Also download the Chrome driver (for example, version 95) from
 * [here](https://www.selenium.dev/documentation/getting_started/installing_browser_drivers/#quick-reference)
 * and place it beside this script.
 *
 * See [this post](https://stackoverflow.com/q/50189638)
 * and [this post](https://stackoverflow.com/q/33982064)
 * and [this gist](https://gist.github.com/alexislucena/ce3bdccf334f5cd0da41920725947b5c).
 */
/*
System.setProperty("webdriver.chrome.driver", "chromedriver.exe")
val result = File("output.html")
val driver = ChromeDriver() // OR FirefoxDriver(); download its driver and set the appropriate system property above
*/
/* Not needed
driver.manage()
    .timeouts()
    .implicitlyWait(Duration.of(10, ChronoUnit.SECONDS))
*/
/*
driver.get("https://www.singaporepools.com.sg/en/product/sr/Pages/toto_results.aspx?sppl=RHJhd051bWJlcj0zNjYx")
result.writeText(driver.pageSource)
driver.close()

// Could also have used Jsoup.parse(driver.pageSource) instead of writing to and reading from a file
val document = Jsoup.parse(result, "UTF-8")
val targetElement = document
    .body()
    .children()
    .select(":containsOwn(Next Jackpot)")
    .single()
    .parent()!!

val phrase = targetElement.text()
val prize = targetElement.select("span").text().removeSuffix(" est")

println(phrase)
println(prize)
*/
