package cn.cyanbukkit.block.game

import cn.cyanbukkit.block.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import cn.cyanbukkit.block.cyanlib.scoreboard.*
import cn.cyanbukkit.block.data.DataCache.completed
import cn.cyanbukkit.block.data.DataLoader
import cn.cyanbukkit.block.game.PlaceHandle.percent
import cn.cyanbukkit.block.game.PlaceHandle.put
import cn.cyanbukkit.block.utils.BossBar
import cn.cyanbukkit.block.utils.isDev
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.ArrayList

object PlaceUseListener : Listener {


    @EventHandler
    fun handle(event: PlayerJoinEvent) {
        val player = event.player // 给op
        if (!player.isOp) {
            player.isOp = true
        }
        player.teleport(DataLoader.spawn)
        val bossBar = BossBar(player, "§6还原进度", 1.0f, BossBar.Color.YELLOW, BossBar.Style.NOTCHED_20)
        bossBar.update {
            it.color = BossBar.Color.entries.random()
            it.percent = DataLoader.arena.percent().toFloat()
        }
        val b = SidebarBoard.of(cyanPlugin, player)
        player.scoreboard = b.scoreboard
        object : BukkitRunnable() {
            override fun run() {
                b.setHead(TextLine.of(""))
                b.setBody(
                    FixedBody.of(
                        TextLine.of("§a本回合还原进度: "),
                        TextLine.of("  §f${String.format("%.01f", DataLoader.arena.percent() * 100)}%"),
                        TextLine.of("§a已完成次数: "),
                        TextLine.of("  §f$completed")
                    )
                )
                b.update()
            }
        }.runTaskTimer(cyanPlugin, 0, 20)
//        Bukkit.getScheduler().runTaskTimer(cyanPlugin, Board(this), 0, 20)
    }


    @EventHandler
    fun handle(event: BlockPlaceEvent) {
        if (isDev(event.player)) return
        if (!DataLoader.arena.contain(event.block)) {
            event.isCancelled = true
        } else {
            DataLoader.arena.put(event.block)
        }
    }


    @EventHandler
    fun handle(event: BlockBreakEvent) {
        if (isDev(event.player)) return
        if (!DataLoader.arena.contain(event.block)) event.isCancelled = true
    }

}