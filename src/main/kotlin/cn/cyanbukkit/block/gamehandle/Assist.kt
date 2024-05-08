package cn.cyanbukkit.block.gamehandle

import cn.cyanbukkit.block.data.DataCache.change
import cn.cyanbukkit.block.data.DataLoader
import cn.cyanbukkit.block.game.BreakHandle.add
import cn.cyanbukkit.block.game.BreakHandle.addAll
import cn.cyanbukkit.block.game.BreakHandle.simpleAdd
import cn.cyanbukkit.block.game.PlaceHandle.fix
import cn.cyanbukkit.block.game.PlaceHandle.simpleBlockFix
import cn.cyanbukkit.block.utils.Mode

/**
 *  智能判定现在的模式是什么进行
 */
class Assist {
    @Mode("1")
    fun mode1(amount: Int) {
        if (DataLoader.isBreak) {
            // 在挖的模式情况下是 添加一层
            DataLoader.arena.add(amount)
        } else {
            DataLoader.arena.fix(true,amount)
        }
    }

    @Mode("2")
    fun mode2(amount: Int) {
        if (DataLoader.isBreak) { // 在挖的模式情况下是 添加一层
            DataLoader.arena.simpleAdd(amount)
        } else {
            DataLoader.arena.simpleBlockFix(amount)
        }
    }


    @Mode("3")
    fun mode3(amount: Int) {
        DataLoader.arena.spawnPig(amount)
    }


    @Mode("4")
    fun clean() {
        DataLoader.arena.clean()
    }

    @Mode("5")
    fun completedChange(amount: Int) {
        change(amount)
    }

    @Mode("6")
    fun all() {
        if (DataLoader.isBreak) {
            DataLoader.arena.addAll()
        } else {
            DataLoader.arena.fix(false)
        }
    }

}