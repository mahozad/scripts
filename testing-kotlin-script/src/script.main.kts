// NOTE: Targets > 1.8 do not work. Is the problem from https://github.com/fwilhe2/setup-kotlin action?
@file:CompilerOptions("-jvm-target", "1.8")

import kotlin.reflect.KProperty

println("This is TODO to Issue converter!")

val cenoDatabasePassword by EnvironmentVariable()
val var2 = getEnvironmentVariable("CENO_DATABASE_PASSWORD")

println(cenoDatabasePassword)
println(var2)

fun String.toSnakeCase() = Regex("(?<=\\w)[A-Z]").replace(this) { "_${it.value}" }

class EnvironmentVariable {
    operator fun getValue(ref: Any, prop: KProperty<*>) = System.getenv(prop.name.toSnakeCase()) ?: ""
}

fun getEnvironmentVariable(key: String) = System.getenv(key) ?: ""
