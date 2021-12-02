#!/usr/bin/env kotlin

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DecimalStyle
import java.time.format.FormatStyle
import java.util.*

/**
 * This is a showcase for a problem in Java [DateTimeFormatter.localizedBy]
 * method which did not localize numbers.
 * It was fixed in Java 15 (versions before 15 still has this problem unless the fix is backported to them).
 *
 * [DateTimeFormatter.withLocale] only localizes text and format patterns.
 * Java 10 introduced the new [DateTimeFormatter.localizedBy] method for full localization.
 *
 * We can also explicitly localize numbers (when using either *localizedBy* or *withLocale*)
 * with the [DateTimeFormatter.withDecimalStyle] method.
 *
 * See [this issue](https://bugs.java.com/bugdatabase/view_bug.do?bug_id=JDK-8243162)
 * and [this issue](https://github.com/thymeleaf/thymeleaf/issues/782)
 * and [Java 15 release notes](https://www.oracle.com/java/technologies/javase/15-relnote-issues.html#JDK-8244245).
 */

val date = LocalDate.now()
val locale = Locale.forLanguageTag("fa")
val decimalStyle = DecimalStyle.of(locale)

// Using withLocale|localizedBy and withDecimalStyle for all Java versions
val first: String = DateTimeFormatter
    .ofPattern("MMMM yyyy")
    .withLocale(locale) // OR localizedBy
    .withDecimalStyle(decimalStyle)
    .format(date)

// Using localizedBy for Java 15 and higher
val second: String = DateTimeFormatter
    .ofLocalizedDate(FormatStyle.FULL)
    .localizedBy(locale)
    .format(date)

println(first)
println(second)
