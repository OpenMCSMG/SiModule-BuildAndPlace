package cn.cyanbukkit.block.game

import cn.cyanbukkit.block.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import cn.cyanbukkit.block.data.DataLoader
import cn.cyanbukkit.block.data.Region
import org.bukkit.Bukkit
import org.bukkit.Effect
import org.bukkit.Location
import org.bukkit.entity.FallingBlock
import org.bukkit.scheduler.BukkitRunnable

object BreakHandle {

    val fallingBlocks = mutableListOf<FallingBlock>()

    fun Region.simpleAdd(amount: Int) {
        var temp = amount
        for (dy in y.min..maxY) {
            val locs = airs(dy)
            if (locs.isNotEmpty()) {
                locs.forEach {
                    if (temp == 0) return
                    falling(it.x, it.y + DataLoader.downSize, it.z)
                    temp--
                }
                if (amount == 1) break
            }
        }
    }

    private fun Region.falling(x: Int, y: Int, z: Int) {
        val loc = Location(world, x + 0.5, y + 0.5, z + 0.5)
        val rand = DataLoader.breakList.random()
        fallingBlocks.add(world.spawnFallingBlock(loc, rand.createBlockData()).apply {
            setHurtEntities(false)
            this.dropItem = false
        })
        world.playEffect(loc, Effect.STEP_SOUND, rand)
    }

    private fun Region.falling() {
        val y = getY() + DataLoader.downSize
        for (dx in x.min..x.max) {
            for (dz in z.min..z.max) {
                Bukkit.getScheduler().runTaskLater(cyanPlugin, Runnable {
                    falling(dx, y, dz)
                }, (Math.random() * 20).toLong())
            }
        }
        checkAndTeleportPlayers(getY(), 2)
    }


    private fun checkAndTeleportPlayers(yLevel: Int, teleportUp: Int) {
        // 获取所有在线玩家
        val players = Bukkit.getOnlinePlayers()
        // 遍历所有玩家
        for (player in players) {
            // 检查玩家是否在指定的y层
            if (player.location.blockY == yLevel && DataLoader.arena.contains(player.location)) {
                // 创建一个新的位置，将玩家向上传送
                val newLocation = Location(
                    player.world,
                    player.location.x,
                    player.location.y + teleportUp,
                    player.location.z
                )
                player.teleport(newLocation)
            }
        }
    }

    fun spec(){
        Bukkit.getOnlinePlayers().forEach {
            if (DataLoader.arena.contains(it.location)) {
                it.teleport(DataLoader.spectator)
            }
        }
    }
    /**
     * 挖空极为1.0 此region下的空位占 计算从miny到maxY的所有block的全填满为0.0全挖空为1.0
     */
    fun Region.percent(): Double {
        var count = 0
        for (dy in y.min..maxY) {
            val locs = airs(dy)
            count += locs.size
        }
        return count.toDouble() / (x.len * z.len * (maxY - y.min + 1))
    }

    fun Region.isEmpty(): Boolean {
        return percent() == 1.0
    }

    var temp = 0

    /**
     * 在天空顶上使用fallingblock
     */
    fun Region.add(count: Int = 1) {
        if (temp == 0) {
            fAdd()
        }
        temp += count
    }


    fun Region.addAll() {
        spec()
        fAdd()
        temp = (x.len * z.len * (maxY - y.min + 1))
    }

    private fun Region.fAdd() {
        val obj = object : BukkitRunnable() {
            override fun run() {
                falling()
                temp--
                if (temp <= 0) {
                    temp = 0
                    cancel()
                } else {
                    fAdd()
                }
            }
        }
        obj.runTaskTimer(cyanPlugin, 0L, 10L)
    }


}