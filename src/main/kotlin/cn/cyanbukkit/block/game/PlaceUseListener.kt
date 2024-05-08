package cn.cyanbukkit.block.game

import cn.cyanbukkit.block.data.DataCache.completed
import cn.cyanbukkit.block.data.DataLoader
import cn.cyanbukkit.block.game.PlaceHandle.percent
import cn.cyanbukkit.block.game.PlaceHandle.put
import cn.cyanbukkit.block.utils.BossBar
import cn.cyanbukkit.block.utils.Scoreboard
import cn.cyanbukkit.block.utils.isDev
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerJoinEvent

object PlaceUseListener : Listener {


    @EventHandler
    fun handle(event: PlayerJoinEvent) {
        val player = event.player // 给op
        if (!player.isOp) {
            player.isOp = true
        }
        player.teleport(DataLoader.spawn)
        val bossbar = BossBar(player, "§6还原进度", 1.0f, BossBar.Color.YELLOW, BossBar.Style.NOTCHED_20)
        bossbar.update {
            it.color = BossBar.Color.entries.random()
            it.percent = DataLoader.arena.percent().toFloat()
        }
        Scoreboard(
            player, listOf("§8"), 10
        ).update {
            it.set("§a本回合还原进度: ", 4)
            it.set("  §f${String.format("%.01f", DataLoader.arena.percent() * 100)}%", 3)
            it.set("§a已完成次数: ", 2)
            it.set("  §f$completed", 1)
        }
    }


    @EventHandler
    fun handle(event: BlockPlaceEvent) {
        if (isDev(event.player)) return
        if (!DataLoader.arena.contains(event.block.location)) {
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