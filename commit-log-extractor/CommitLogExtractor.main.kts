@file:CompilerOptions("-jvm-target", "11")
@file:Repository("https://jcenter.bintray.com")
@file:DependsOn("com.github.mfathi91:persian-date-time:4.1.0")

import com.github.mfathi91.time.PersianDate
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.time.format.DateTimeFormatter.ofPattern
import java.time.format.DecimalStyle
import java.time.temporal.ChronoUnit.DAYS
import java.util.*

val targetDay = LocalDateTime.of(2020, 8, 13, 0, 0)
val commandBase = "git log --pretty=format:\"%%s\" --after=\"%s\" --before=\"%s\""
val outputDirectory = "./"

val start = targetDay.format(ISO_LOCAL_DATE_TIME)
val end = targetDay.plus(1, DAYS).format(ISO_LOCAL_DATE_TIME)
val command = String.format(commandBase, start, end)
val process = Runtime.getRuntime().exec(command)

val locale = Locale.forLanguageTag("fa")
val formatter = ofPattern("yyyy-MM-dd").localizedBy(locale).withDecimalStyle(DecimalStyle.of(locale))
val persianDate = PersianDate.fromGregorian(LocalDate.from(targetDay))
val date = formatter.format(persianDate)
val fileName = "$date.txt"
val outputPath = Path.of(outputDirectory + fileName)

val logs = BufferedReader(InputStreamReader(process.inputStream, StandardCharsets.UTF_8))
    .lineSequence()
    .joinToString("\r\n")

Files.deleteIfExists(outputPath)
Files.createFile(outputPath)
Files.writeString(outputPath, logs)
