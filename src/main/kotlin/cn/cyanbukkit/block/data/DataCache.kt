package cn.cyanbukkit.block.data

object DataCache {

    var completed = 0
    var checking = false

    fun change(i: Int) {
        completed += i
    }

}