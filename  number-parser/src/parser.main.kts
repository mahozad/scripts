// Use NumberFormat and similar Java/Kotlin localization classes instead

/**
 * How to parse a number string in almost any format in Kotlin (solution by me)
 *
 * Also see [this stackoverflow post](https://stackoverflow.com/q/5316131/8583692)
 * and [this post](https://stackoverflow.com/q/39385746/8583692)
 * and [this post](https://codereview.stackexchange.com/q/166750)
 */

/**
 * Parses any valid number string to a [Double].
 * The number can be in Persian/Urdu, Eastern Arabic, or Westerns Arabic numerals.
 * The number can have thousands separators (Persian/Urdu/Eastern Arabic `٬` or English `,` or others).
 * The number can be a mix of the above; for example,
 * it can have Persian numerals, [thin space](https://en.wikipedia.org/wiki/Thin_space) ` ` as thousands separator, and point `.` as decimal separator.
 *
 * Also see [this Wikipedia article](https://en.wikipedia.org/wiki/Arabic_script_in_Unicode)
 */
fun String.parseToDouble() = this
    .normalizeDigits()
    .normalizeDecimalSeparator()
    .removeOptionalCharacters()
    .toDouble()

/**
 * Converts [Persian/Urdu and Eastern Arabic digits](https://en.wikipedia.org/wiki/Eastern_Arabic_numerals#Numerals) to Western Arabic digits
 */
fun String.normalizeDigits() = this
    // Replace Persian/Urdu numerals
    .replace(Regex("[۰-۹]")) { match -> (match.value.single() - '۰').toString() }
    // Replace Eastern Arabic numerals
    .replace(Regex("[٠-٩]")) { match -> (match.value.single() - '٠').toString() }

/**
 * Converts [Persian/Urdu/Eastern Arabic decimal separator](https://en.wikipedia.org/wiki/Decimal_separator#Other_numeral_systems) `٫` or slash `/` (invalid and non-standard) to `.` (decimal separator in English)
 */
fun String.normalizeDecimalSeparator() = this.replace('٫', '.').replace('/', '.')

/**
 * Removes everything except Western Arabic digits and point `.` (for example, thousands separators)
 */
fun String.removeOptionalCharacters() = this.replace(Regex("[^\\d.]"), "")
