package cn.cyanbukkit.block.game

import cn.cyanbukkit.block.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import cn.cyanbukkit.block.data.DataLoader
import cn.cyanbukkit.block.data.Region
import org.bukkit.Bukkit
import org.bukkit.Effect
import org.bukkit.Location
import org.bukkit.block.Block
import kotlin.math.floor
import kotlin.math.min

object PlaceHandle {

    fun Region.fix(layer: Boolean, count: Int = 1) {
        count@ for (i in 0 until count) {
            for (dy in y.min..y.max) {
                val locs = airs(dy)
                if (locs.isNotEmpty()) {
                    locs.forEach {
                        put(it)
                    }
                    if (layer) continue@count
                }
            }
        }
    }

    fun Region.simpleBlockFix(amount: Int) {
        var temp = amount
        for (dy in y.min..y.max) {
            val locs = airs(dy)
            if (locs.isNotEmpty()) {
                locs.forEach {
                    put(it)
                    temp--
                    if (temp == 0) return
                }
            }
        }
    }

    fun Region.put(b: Block) {
        Bukkit.getScheduler().runTask(cyanPlugin, Runnable {
            val size = if (DataLoader.placeList.size != 0) y.len / DataLoader.placeList.size else 1
            val index = floor((b.y - y.min) / size * 1.0).toInt()
            val safeIndex = min(index, DataLoader.placeList.size - 1)
            val type = DataLoader.placeList[safeIndex]
            world.playEffect(b.location, Effect.STEP_SOUND, type)
            b.type = type
        })
    }

    fun Region.getCrown(): List<Location> {
        val locs = mutableListOf<Location>()
        for (dx in x.min..x.max) {
            for (dz in z.min..z.max) {
                val b = world.getBlockAt(dx, y.max + 1, dz)
                locs.add(b.location)
            }
        }
        return locs
    }

    fun Region.percent(): Double {
        val total = x.len * z.len * y.len
        val plate = x.len * z.len
        var completed = 0.0
        for (dy in y.min..y.max) {
            completed += plate - airs(dy).size
        }
        return completed / total * 1.0
    }

    fun Region.full(): Boolean {
        return percent() == 1.0
    }


}