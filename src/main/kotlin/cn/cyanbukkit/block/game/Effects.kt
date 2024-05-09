package cn.cyanbukkit.block.game

import cn.cyanbukkit.block.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import cn.cyanbukkit.block.data.DataLoader
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import org.checkerframework.checker.units.qual.t

/**
 * @author LC_Official
 */
object Effects : Listener {

    // 列表Map  任务  持续时间 tick
    val queue = LinkedHashMap<BukkitRunnable, Int>()

    fun init() {
        cyanPlugin.server.pluginManager.registerEvents(this, cyanPlugin)
        Bukkit.getScheduler().runTaskTimer(cyanPlugin, Runnable {
            // 持续时间播完在删 没播完的继续播 20tick 1s 而持续时间是存的秒
            queue.forEach {
                val taskId = try {
                    it.key.taskId
                } catch (e: Exception) {
                    -1
                }
                if (taskId != -1 && (Bukkit.getScheduler().isCurrentlyRunning(taskId) || Bukkit.getScheduler().isQueued(taskId))) {
                    if (it.value <= 0) {
                        it.key.cancel()
                        queue.remove(it.key)
                    } else {
                        queue[it.key] = it.value - 1
                    }
                } else if (taskId == -1) {
                    it.key.runTaskTimer(cyanPlugin, 0, 1L)
                }
            }
        }, 0, 1L)
    }


    fun push(times: Int, mode: Int) {
        val runnable = when (mode) {
            1 -> modeA()
            2 -> modeB()
            else -> modeA()
        }
        queue[runnable] = times * 20
    }

    private fun modeB() : BukkitRunnable{
        return object : BukkitRunnable() {
            override fun run() {
                Bukkit.getOnlinePlayers().forEach {
                    (DataLoader.arenaWorld.spawnEntity(
                        it.location.add(0.0, -0.7, 0.0), EntityType.PRIMED_TNT
                    ) as TNTPrimed).apply {
                        this.fuseTicks = 40
                    } //  向上推0.5 不要直接使用
                    it.velocity = it.velocity.setY(0.5)
                }
            }
        }
    }


    private fun modeA() : BukkitRunnable{
        return object : BukkitRunnable() {
            override fun run() {
                Bukkit.getOnlinePlayers().forEach {
                    it.velocity = Vector.getRandom()
                    (DataLoader.arenaWorld.spawnEntity(
                        it.location.add(0.5, 0.0, 0.0),
                        EntityType.PRIMED_TNT
                    ) as TNTPrimed).apply {
                        this.velocity = Vector(0.5, 0.3, 0.0)
                        this.fuseTicks = 30
                    }
                    (DataLoader.arenaWorld.spawnEntity(
                        it.location.add(-0.5, 0.0, 0.0),
                        EntityType.PRIMED_TNT
                    ) as TNTPrimed).apply {
                        this.velocity = Vector(-0.5, 0.3, 0.0)
                        this.fuseTicks = 30
                    }
                    (DataLoader.arenaWorld.spawnEntity(
                        it.location.add(0.0, 0.0, -0.5),
                        EntityType.PRIMED_TNT
                    ) as TNTPrimed).apply {
                        this.velocity = Vector(0.0, 0.3, -0.5)
                        this.fuseTicks = 30
                    }
                    (DataLoader.arenaWorld.spawnEntity(
                        it.location.add(0.0, 0.0, 0.5),
                        EntityType.PRIMED_TNT
                    ) as TNTPrimed).apply {
                        this.velocity = Vector(0.0, 0.3, 0.5)
                        this.fuseTicks = 30
                    }
                }
            }
        }
    }
}
