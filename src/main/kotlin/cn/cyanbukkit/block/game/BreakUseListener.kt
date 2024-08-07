package cn.cyanbukkit.block.game

import cn.cyanbukkit.block.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import cn.cyanbukkit.block.cyanlib.scoreboard.FixedBody
import cn.cyanbukkit.block.cyanlib.scoreboard.Line
import cn.cyanbukkit.block.cyanlib.scoreboard.SidebarBoard
import cn.cyanbukkit.block.cyanlib.scoreboard.TextLine
import cn.cyanbukkit.block.data.DataCache.completed
import cn.cyanbukkit.block.data.DataLoader
import cn.cyanbukkit.block.game.BreakHandle.fallingBlocks
import cn.cyanbukkit.block.game.BreakHandle.percent
import cn.cyanbukkit.block.utils.BossBar
import cn.cyanbukkit.block.utils.Title
import cn.cyanbukkit.block.utils.isDev
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.FallingBlock
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityChangeBlockEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.ArrayList

/**
 * 挖沙子的监听类
 */
object BreakUseListener  : Listener{

    @EventHandler
    fun changeBlock(event: EntityChangeBlockEvent) {
        if (event.entityType == EntityType.FALLING_BLOCK) {
            if (fallingBlocks.contains(event.entity)) {
                fallingBlocks.remove(event.entity)
                event.isCancelled = true
                val b = event.entity as FallingBlock
                event.block.type = b.blockData.material
                event.block.blockData = b.blockData
                event.block.state.update(true)
                return
            }
        }
    }


    @EventHandler
    fun xiaLuoToBlock(e: EntityChangeBlockEvent) {
        if (e.entityType == EntityType.FALLING_BLOCK) {
            val fallingBlock = e.entity as FallingBlock
            // 如果下落的不在区域内就取消
            if (!DataLoader.arena.contains(fallingBlock.location)) {
                e.isCancelled = true
                return
            }
        }
    }

    /**
     * AntiCheating
     */
    private val antiBlock = listOf(
        Material.FIRE,
        Material.REDSTONE_TORCH,
        Material.REDSTONE_WALL_TORCH,
        Material.SOUL_FIRE,
        Material.SOUL_TORCH,
        Material.SOUL_WALL_TORCH,
        Material.LEVER
    )

    @EventHandler
    fun handle(event: BlockPlaceEvent) {
        // 禁止放置火把红石火把灵魂火把等这种让沙子变成物品的
        if (antiBlock.contains(event.block.type)) {
            event.isCancelled = true
            Title.title(event.player, "§c你还想作弊？", "§7大家让么？")
            return
        }
        // 不在指定的区域禁止放
        if (!DataLoader.arena.contains(event.block.location)) {
            if (isDev(event.player)) return
            event.isCancelled = true
        }
    }





    @EventHandler
    fun handle(event: BlockBreakEvent) {
        // 如果Tools不是空的
        if (DataLoader.breakToolsList.isNotEmpty()) {
            // 看是用哪个手挖的
            if (event.player.inventory.itemInMainHand.type !in DataLoader.breakToolsList) {
                event.isCancelled = true
                event.player.sendMessage("§c你没有使用正确的工具 支持")
                DataLoader.breakToolsList.forEach {
                    event.player.sendMessage("§c${it.name}")
                }
                return
            }
        }
        if (event.block.type in DataLoader.breakList)  return
        if (!DataLoader.arena.contains(event.block.location)) {
            if (isDev(event.player)) return
            event.isCancelled = true
        }
    }


    @EventHandler
    fun handle(event: PlayerJoinEvent) {
        val player = event.player // 给op
        if (!player.isOp) {
            player.isOp = true
        }
        player.teleport(DataLoader.spawn)
        val bossBar = BossBar(player, "§6挖掘进度", 1.0f, BossBar.Color.YELLOW, BossBar.Style.NOTCHED_20)
        bossBar.update {
            it.color = BossBar.Color.entries.random()
            it.percent = DataLoader.arena.percent().toFloat()
        }
        val b  = SidebarBoard.of(cyanPlugin, player)
        player.scoreboard = b.scoreboard
        object : BukkitRunnable() {
            override fun run() {
                b.setHead(TextLine.of(""))
                b.setBody(FixedBody.of(
                    TextLine.of("§a当前已挖掘: "),
                    TextLine.of("  §f${String.format("%.01f", DataLoader.arena.percent() * 100)}%"),
                    TextLine.of("§a已完成次数: "),
                    TextLine.of("  §f$completed")
                ))
                b.update()
            }
        }.runTaskTimer(cyanPlugin, 0, 20)
    }


}