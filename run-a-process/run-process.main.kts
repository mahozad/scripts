import java.io.File
import java.util.concurrent.TimeUnit

/**
 * See
 * https://stackoverflow.com/q/35421699 and
 * https://stackoverflow.com/q/21281354
 */

val command = "java --version"
// val command = "cmd.exe /c dir"

val process: Process = ProcessBuilder(command.split(" "))
    .directory(File("."))
    .redirectOutput(ProcessBuilder.Redirect.PIPE)
    .redirectError(ProcessBuilder.Redirect.PIPE)
    .start()
process.waitFor(1, TimeUnit.SECONDS)

val (vendor, version, date) = process
    .inputStream
    .reader()
    .readText()
    .lines()
    .first()
    .split(" ")

fun String.pad(length: Int) = "%-${length}s".format(this)
val valueLine = "│ ${vendor.pad(6)} │ ${version.pad(7)} │ ${date.pad(12)} │"
val template = """
    :┌──────────────────────────────────┐
    :│              Java                │
    :├─────────┬─────────┬──────────────┤
    :│ Vendor  │ Version │ Release date │
    :├╌╌╌╌╌╌╌╌╌┼╌╌╌╌╌╌╌╌╌┼╌╌╌╌╌╌╌╌╌╌╌╌╌╌┤
               :$valueLine
    :└─────────┴─────────┴──────────────┘
"""

println(template.trimMargin(":"))
