package cn.cyanbukkit.block.game

import cn.cyanbukkit.block.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import cn.cyanbukkit.block.data.DataLoader
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.entity.Frog
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.concurrent.CopyOnWriteArrayList

object TNTBoomHandle : Listener {

    fun load() {
        cyanPlugin.server.pluginManager.registerEvents(this, cyanPlugin)
        Effects.init()
        Bukkit.getScheduler().runTaskTimer(cyanPlugin, Runnable {
            tnt.forEach {
                if (it.isOnGround) {
                    // 移除坐在上面的
                    it.passengers.forEach { e -> e.remove() }
                    it.fuseTicks = 1
                    tnt.remove(it)
                }
                it.world.playEffect(it.location, org.bukkit.Effect.MOBSPAWNER_FLAMES, 0)
            }
        }, 0L, 4L)
    }

    fun batchBoom(multiply: Int, group: Int) {
        // 有BUG 比如 10 个的时候 就出了5波还连着出
        var temp = multiply
        object : BukkitRunnable() {
            override fun run() {
                if (temp == 0) {
                    cancel()
                } else {
                    for (i in 0 until group) {
                        boom()
                    }
                    temp--
                }
            }
        }.runTaskTimer(cyanPlugin, 0, 20L)
    }

    fun simpleBoom(amount: Int) {
        var temp = amount
        object : BukkitRunnable() {
            override fun run() {
                if (temp == 0) {
                    cancel()
                } else {
                    boom()
                    temp--
                }
            }
        }.runTaskTimer(cyanPlugin, 0, 20L)
    }

    private val tnt = CopyOnWriteArrayList<TNTPrimed>()

    fun boom() {
        val loc = DataLoader.arena.randomLoc()
        val tE = (loc.world!!.spawnEntity(loc, EntityType.PRIMED_TNT) as TNTPrimed).apply {
            fallDistance = 0.5f
            fuseTicks = 100
        }
        val frog = (loc.world!!.spawnEntity(loc, EntityType.FROG) as Frog)
        tE.addPassenger(frog)
        tnt.add(tE)
    }

    @EventHandler
    fun onTNTExplode(event: EntityExplodeEvent) {
        // 爆炸时remove 不在region的范围的方块 爆炸范围为3以外的方块不删
        event.blockList().removeIf {
            !DataLoader.arena.contains(it.location) || it.location.distance(event.location) > 3
        }
        event.location.playEffect()
    }

    private fun Location.playEffect() {
        // 在这个位置绽放粒子颜色随机
        this.world!!.spawn(this, Firework::class.java).apply {
            // 设置火焰球的颜色
            val fm = fireworkMeta.apply {
                addEffect(
                    FireworkEffect.builder().withColor(
                        Color.fromRGB(
                            (0..255).random(), (0..255).random(), (0..255).random()
                        )
                    ).withFlicker().with(FireworkEffect.Type.BALL).build()
                )
            }
            fireworkMeta = fm
            //    设置立刻爆炸
            this.detonate()
            fireworkMeta.power = 0
        }
    }

}