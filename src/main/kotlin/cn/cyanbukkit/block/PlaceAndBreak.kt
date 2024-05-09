package cn.cyanbukkit.block

import cn.cyanbukkit.block.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import cn.cyanbukkit.block.data.DataCache.checking
import cn.cyanbukkit.block.data.DataCache.completed
import cn.cyanbukkit.block.data.DataLoader
import cn.cyanbukkit.block.game.BreakHandle.gameStart
import cn.cyanbukkit.block.game.BreakHandle.isEmpty
import cn.cyanbukkit.block.game.Effects
import cn.cyanbukkit.block.game.PlaceHandle.full
import cn.cyanbukkit.block.game.PlaceHandle.getCrown
import cn.cyanbukkit.block.game.TNTBoomHandle
import cn.cyanbukkit.block.utils.BossBar
import cn.cyanbukkit.block.utils.Scoreboard
import cn.cyanbukkit.block.utils.Title
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.scheduler.BukkitRunnable

object PlaceAndBreak {
    fun init() {
        BossBar.init(cyanPlugin)
        Scoreboard.init(cyanPlugin)
        DataLoader.load()
        TNTBoomHandle.load()
        Effects.init()
    }

    fun breakStatus() : BukkitRunnable {
        var temp = DataLoader.keepTime
        return object : BukkitRunnable() {
            override fun run() {
                if (DataLoader.arena.isEmpty()) {
                    Bukkit.getOnlinePlayers().forEach {
                        Title.title(it, "§c再坚持下", "§7${temp}", 10, 40, 10)
                    }
                    temp--
                    checking = true
                    if (temp < 0) {// 下个回合
                        Bukkit.getOnlinePlayers().forEach {
                            Title.title(it, "§c保持完毕", "§7挖掘区域已经被挖空", 10, 40, 10)
                        }
                        DataLoader.arena.gameStart()
                        completed++
                        checking = false
                        temp = DataLoader.keepTime
                    }
                } else {
                    if (checking) { // 中途被断了
                        Bukkit.getOnlinePlayers().forEach {
                            Title.title(it, "§c没保持住o~", "§7加油", 10, 40, 10)
                        }
                        checking = false
                        temp = DataLoader.keepTime
                    }
                }
            }
        }
    }

    fun placeStatus() : BukkitRunnable {
        var temp = DataLoader.keepTime
        return object : BukkitRunnable() {
            override fun run() {
                if (DataLoader.arena.full()) {
                    Bukkit.getOnlinePlayers().forEach {
                        Title.title(it, "§a${temp}", "§c保持成功，继续保持！", 10, 40, 10)
                        Title.actionbar(it, "§c本回合距离最终完成还剩余 $temp 秒！！！")
                        it.sendMessage("§c本回合距离最终完成还剩余 $temp 秒！！！")
                        // 在区域的最高层 +1 spawnParticle 效果音符粒子 随机音调符号
                        DataLoader.arena.getCrown().forEach { loc ->
                            loc.world!!.spawnParticle(Particle.NOTE, loc.add(0.5, 0.5, 0.5), 1)
                        }
                    }
                    temp--
                    checking = true
                    if (temp < 0) { // 完成清零
                        DataLoader.arena.clean()
                        (0..10).forEach { _ ->
                            DataLoader.arena.randomFirework()
                        }
                        completed++
                        checking = false
                        temp = DataLoader.keepTime
                        Bukkit.getOnlinePlayers().forEach {
                            Title.actionbar(it, "§a本回合“顺利”完成，已完成次数: $completed")
                            it.sendMessage("§a本回合“顺利”完成，已完成次数: $completed")
                        }
                    }
                }else {
                    if (checking) {
                        Bukkit.getOnlinePlayers().forEach {
                            Title.title(it, "§c保持失败", "§a保持失败，尽快补充剩余的方块吧!", 10, 40, 10)
                            Title.actionbar(it, "§a保持失败，尽快补充剩余的方块吧!")
                            it.sendMessage("§a保持失败，尽快补充剩余的方块吧!")
                        }
                        checking = false
                        temp = DataLoader.keepTime
                    }
                }
            }
        }
    }


}