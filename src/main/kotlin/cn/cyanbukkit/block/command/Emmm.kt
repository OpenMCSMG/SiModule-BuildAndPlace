package cn.cyanbukkit.block.command

fun anise(
    info: Array<out String>,
    parameterTypes: Array<Class<*>>
): Array<Any> {
    val convertedArgs = Array<Any>(info.size) { i ->
        when (parameterTypes[i + 1]) { // i + 1 because the first parameter is Player
            Double::class.java -> info[i].toDouble()
            Int::class.java -> info[i].toInt()
            String::class.java -> info[i]
            else -> throw IllegalArgumentException("Unsupported parameter type")
        }
    }
    return convertedArgs
}