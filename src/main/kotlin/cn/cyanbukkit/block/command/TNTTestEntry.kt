package cn.cyanbukkit.block.command

import cn.cyanbukkit.block.gamehandle.TNTBoom
import cn.cyanbukkit.block.utils.Mode
import cn.cyanbukkit.block.utils.anise
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

object TNTTestEntry : Command("tnttest") {
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (sender !is org.bukkit.entity.Player) {
            sender.sendMessage("§c只有玩家才能执行此命令")
            return true
        }
        val p = sender
        val hand = TNTBoom()
        val methods = hand.javaClass.declaredMethods
        for (method in methods) {
            val mode = method.getAnnotation(Mode::class.java)
            if (mode != null) {
                if (mode.value == args[0]) {
                    val info = args.sliceArray(1 until args.size)
                    val parameterTypes = method.parameterTypes
                    val convertedArgs = anise(info, parameterTypes, p)
                    method.invoke(hand, *convertedArgs)
                    return true
                }
            }
        }
        p.sendMessage("§c未找到模式")
        return true
    }


}