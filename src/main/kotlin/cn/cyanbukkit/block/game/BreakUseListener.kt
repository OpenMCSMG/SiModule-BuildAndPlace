package cn.cyanbukkit.block.game

import cn.cyanbukkit.block.data.DataCache.completed
import cn.cyanbukkit.block.data.DataLoader
import cn.cyanbukkit.block.game.BreakHandle.fallingBlocks
import cn.cyanbukkit.block.game.BreakHandle.percent
import cn.cyanbukkit.block.utils.BossBar
import cn.cyanbukkit.block.utils.Scoreboard
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
    fun handle(event: PlayerJoinEvent) {
        val player = event.player // 给op
        if (!player.isOp) {
            player.isOp = true
        }
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

}