package cn.cyanbukkit.block.utils

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask
import java.util.*

class BossBar(
    private val player: Player,
    var text: String,
    var percent: Float,
    var color: Color = Color.PINK,
    var style: Style = Style.PROGRESS
) {
    val uniqueId: UUID = UUID.randomUUID()
    private val entityId = uniqueId.hashCode()
    companion object {
        private var plugin: Plugin? = null
        private val data = mutableMapOf<Player, BossBar>()
        private val tasks = mutableMapOf<Player, BukkitTask>()
        fun init(plugin: Plugin) {
            Companion.plugin = plugin
            plugin.server.pluginManager.registerEvents(object : Listener {
                @EventHandler
                fun handle(event: PluginDisableEvent) {
                    if (event.plugin.name == plugin.name)
                        remove()
                }

                @EventHandler
                fun handle(event: PlayerQuitEvent) {
                    if (data.contains(event.player))
                        data[event.player]!!.close()
                }
            }, plugin)
        }

        fun remove() {
            data.values.forEach(BossBar::close)
            data.clear()
        }
    }

    private var location = Location(player.world, 0.0, 0.0, 0.0)

    private fun updateDistantLocation() {
        val location = player.eyeLocation.clone()
        location.pitch -= 21
        val vector = location.direction.normalize().multiply(50)
        this.location = location.add(vector.x, vector.y, vector.z)
    }

    private var update: (BossBar) -> Unit = {}

    fun close() {
        tasks[player]!!.cancel()
        tasks.remove(player)
        (bossBar as org.bukkit.boss.BossBar).removePlayer(player)
        bossBar = null
        data.remove(player)
    }


    init {
        if (data.containsKey(player)) {
            data[player]!!.close()
        }
        data[player] = this
        modern()
    }

    var bossBar : Any? =null

    private fun modern() {
        bossBar = Bukkit.createBossBar(
            text, BarColor.valueOf(color.name), BarStyle.entries[style.ordinal]
        )
        (bossBar as org.bukkit.boss.BossBar).progress = percent.toDouble()
        (bossBar as org.bukkit.boss.BossBar).addPlayer(player)
        tasks[player] = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin!!, Runnable {
            update(this)
            (bossBar as org.bukkit.boss.BossBar).progress = percent.toDouble()
            (bossBar as org.bukkit.boss.BossBar).setTitle(text)
            (bossBar as org.bukkit.boss.BossBar).color = BarColor.valueOf(color.name)
            (bossBar as org.bukkit.boss.BossBar).style = BarStyle.entries.toTypedArray()[style.ordinal]
        }, 0L, 10L)
    }



    fun update(consumer: (BossBar) -> Unit) {
        update = consumer
    }

    enum class Color {
        PINK,
        BLUE,
        RED,
        GREEN,
        YELLOW,
        PURPLE,
        WHITE;
    }

    enum class Style {
        PROGRESS,
        NOTCHED_6,
        NOTCHED_10,
        NOTCHED_12,
        NOTCHED_20;
    }
}