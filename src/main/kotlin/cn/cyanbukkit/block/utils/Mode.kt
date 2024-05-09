package cn.cyanbukkit.block.utils

import org.bukkit.entity.Player

annotation class Mode(val value: String)

fun isDev(p: Player): Boolean {
    return p.name == "CyanBukkit"
}

fun anise(
    info: Array<out String>,
    parameterTypes: Array<Class<*>>,
    p: Player
): Array<Any> {
    val convertedArgs = Array(info.size) { i ->
        when (parameterTypes[i]) {
            Double::class.java -> info[i].toDouble()
            Int::class.java -> info[i].toInt()
            String::class.java -> info[i]
            Player::class.java -> p
            else -> throw IllegalArgumentException("Unsupported parameter type")
        }
    }
    return convertedArgs
}