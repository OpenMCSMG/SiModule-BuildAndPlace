package cn.cyanbukkit.block.gamehandle

import cn.cyanbukkit.block.game.Effects.push
import cn.cyanbukkit.block.game.TNTBoomHandle
import cn.cyanbukkit.block.utils.Mode

class TNTBoom {

    @Mode("1")
    fun mode1(amount: Int) {
        TNTBoomHandle.simpleBoom(amount)
    }

    @Mode("2")
    fun mode2(amount: Int, group: Int) {
        TNTBoomHandle.batchBoom(amount, group)
    }

    @Mode("3")
    fun mode3(second: Int) {
        push(second, 1)
    }

    @Mode("4")
    fun mode4(second: Int) {
        push(second, 2)
    }

}