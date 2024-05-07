package cn.cyanbukkit.block.data

import org.bukkit.Effect
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block


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
    fun contains(location: Location): Boolean =
        location.blockX in x && location.blockY >= y.min && location.blockZ in z
    val maxY = world.maxHeight

    /**
     * 光清理区域的所有 直接连顶上的方块也一并清楚
     */
    private fun clean() {
        for (dy in y.min..maxY) {
            for (dx in x.min..x.max) {
                for (dz in z.min..z.max) {
                    val b = world.getBlockAt(dx, dy, dz)
                    world.playEffect(b.location, Effect.STEP_SOUND, b.type)
                    b.type = Material.AIR
                }
            }
        }
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

}
