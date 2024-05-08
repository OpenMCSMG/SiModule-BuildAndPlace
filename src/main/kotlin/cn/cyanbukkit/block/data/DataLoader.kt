package cn.cyanbukkit.block.data

import cn.cyanbukkit.block.cyanlib.launcher.CyanPluginLauncher
import cn.cyanbukkit.block.game.BreakUseListener
import cn.cyanbukkit.block.game.PlaceUseListener
import org.bukkit.Location
import org.bukkit.Material

object DataLoader {

    lateinit var arenaWorld : org.bukkit.World
    lateinit var arena      : Region
    lateinit var spectator  : Location
    lateinit var spawn      : Location
    var downSize = 0
    var keepTime = 0
    val breakList      = mutableListOf<Material>()
    val breakToolsList = mutableListOf<Material>()
    val placeList      = mutableListOf<Material>()
    var isBreak = false

    fun load() {
        val setting = CyanPluginLauncher.cyanPlugin.config.getConfigurationSection("Setting")
        arenaWorld = setting!!.getString("Arena.World")?.let { CyanPluginLauncher.cyanPlugin.server.getWorld(it) }!!
        val pos1 = setting.getString("Arena.Pos1")!!
        val pos2 = setting.getString("Arena.Pos2")!!
        arena = Region(Location(arenaWorld, pos1.split(",")[0].toDouble(), pos1.split(",")[1].toDouble(), pos1.split(",")[2].toDouble()),
            Location(arenaWorld, pos2.split(",")[0].toDouble(), pos2.split(",")[1].toDouble(), pos2.split(",")[2].toDouble()), arenaWorld)
        val spectatorPos = setting.getString("Spectator")!!
        spectator = Location(arenaWorld, spectatorPos.split(",")[0].toDouble(), spectatorPos.split(",")[1].toDouble(), spectatorPos.split(",")[2].toDouble(), spectatorPos.split(",")[3].toFloat(), spectatorPos.split(",")[4].toFloat())
        val spawnPos = setting.getString("Spawn")!!
        spawn = Location(arenaWorld, spawnPos.split(",")[0].toDouble(), spawnPos.split(",")[1].toDouble(), spawnPos.split(",")[2].toDouble(), spawnPos.split(",")[3].toFloat(), spawnPos.split(",")[4].toFloat())
        downSize = setting.getInt("Down")
        keepTime = setting.getInt("KeepTime")
        breakList.addAll(setting.getStringList("Break.BlockList").map { Material.getMaterial(it)!! })
        breakToolsList.addAll(setting.getStringList("Break.Tools").map { Material.getMaterial(it)!! })
        placeList.addAll(setting.getStringList("Place.BlockList").map { Material.getMaterial(it)!! })
        // 识别模式
        val mode =  CyanPluginLauncher.cyanPlugin.config.getString("Mode")!!
        when (mode) {
            "挖" -> { // 注册挖的监听
                isBreak = true
                CyanPluginLauncher.cyanPlugin.server.pluginManager.registerEvents(BreakUseListener, CyanPluginLauncher.cyanPlugin)
            }
            "建" -> { // 注册放的监听
                isBreak = false
                CyanPluginLauncher.cyanPlugin.server.pluginManager.registerEvents(PlaceUseListener, CyanPluginLauncher.cyanPlugin)
            }
        }
    }

}

