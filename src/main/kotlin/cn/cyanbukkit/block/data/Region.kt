package cn.cyanbukkit.block.data

import cn.cyanbukkit.block.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import org.apache.commons.lang3.RandomUtils
import org.bukkit.Bukkit
import org.bukkit.Effect
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Pig
import java.awt.Color.cyan


class NumberPair(a: Int, b: Int) {
    val min = minOf(a, b)
    val max = maxOf(a, b)
    operator fun contains(n: Int): Boolean {
        return n in min .. max
    }

    fun random(): Int {
        return (min .. max).random()
    }

    val len = max - min + 1
}

data class Region(val pos1: Location, val pos2: Location, val world: org.bukkit.World) {
    val x = NumberPair(pos1.blockX, pos2.blockX)
    val y = NumberPair(pos1.blockY, pos2.blockY)
    val z = NumberPair(pos1.blockZ, pos2.blockZ)
    fun contains(location: Location): Boolean = location.blockX in x && location.blockY >= y.min && location.blockZ in z
    fun contain(b: Block): Boolean = b.x in x && b.y in y && b.z in z
    val maxY = world.maxHeight

    /**
     * 光清理区域的所有 直接连顶上的方块也一并清楚
     */
    fun clean() {
        for (dy in y.min..maxY) {
            for (dx in x.min..x.max) {
                for (dz in z.min..z.max) {
                    Bukkit.getScheduler().runTask(cyanPlugin, Runnable {
                        val b = world.getBlockAt(dx, dy, dz)
                        world.playEffect(b.location, Effect.STEP_SOUND, b.type)
                        b.type = Material.AIR
                    })
                }
            }
        }
    }

    /**
     * 区域顶上随机烟花
     */
    fun randomFirework() {
        val b = world.getBlockAt(x.random(), getY() + DataLoader.downSize, z.random())
        b.location.world!!.spawn(b.location, org.bukkit.entity.Firework::class.java)
    }

    /**
     * 已经达到y轴的
     */
    fun getY(): Int {
        for (dy in y.min..maxY) {
            val locs = airs(dy)
            if (locs.size >= (x.len * z.len)) {
                return dy
            }
        }
        return maxY
    }

    /**
     * 获取指定层数上无方块的
     */
    fun airs(y: Int): List<Block> {
        val locs = mutableListOf<Block>()
        for (dx in x.min..x.max) {
            for (dz in z.min..z.max) {
                val b = world.getBlockAt(dx, y, dz)
                if (b.isEmpty) {
                    locs.add(b)
                }
            }
        }
        return locs
    }

    fun spawnPig(amount: Int = 10) {
        for (c in 0 until amount) {
            Bukkit.getScheduler().runTask(cyanPlugin, Runnable {
                val loc = Location(world, x.min + (x.len * Math.random()), getY() + 3.5, z.min + (z.len * Math.random()))
                (world.spawnEntity(loc, org.bukkit.entity.EntityType.PIG) as Pig).apply {
                    getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH)!!.baseValue = 50.0
                    health = 50.0
                }
            })
        }
    }
    private fun random(): Int {
        return if (RandomUtils.nextBoolean()) 1 else -1
    }

    /**
     * 根据最低的y + add 高度在随机x z 生成 一个Location
     */
    fun randomLoc() : Location {
        val x = RandomUtils.nextInt(0, x.len / 2) + 0.15
        val z = RandomUtils.nextInt(0, z.len / 2) + 0.15
        return Location(world, x * random(), (getY() + DataLoader.downSize).toDouble(), z * random())
    }


}
