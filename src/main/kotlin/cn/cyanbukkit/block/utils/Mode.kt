package cn.cyanbukkit.block.utils

import org.bukkit.entity.Player

annotation class Mode(val value: String)

fun isDev(p: Player): Boolean {
    return p.name == "CyanBukkit"
}