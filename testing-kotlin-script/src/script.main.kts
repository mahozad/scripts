@file:CompilerOptions("-jvm-target", "11")

import kotlin.reflect.KProperty

val os by EnvironmentVariable()
println(os)
println(capitalizeArgs())

fun capitalizeArgs() = args.map { it.toUpperCase() }

class EnvironmentVariable {
    operator fun getValue(ref: Any, prop: KProperty<*>) = System.getenv(prop.name) ?: ""
}
