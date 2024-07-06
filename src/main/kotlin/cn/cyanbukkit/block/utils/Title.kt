package cn.cyanbukkit.block.utils

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player

object Title : Feature() {

    /**
     * Send a title to a player
     * @param player The player to send the title to
     * @param title The title text
     * @param subtitle The subtitle text
     * @param fadeIn The fade in time
     * @param stay The stay time
     * @param fadeOut The fade out time
     */
    @Suppress("DEPRECATION")
    fun title(
        player: Player,
        title: String,
        subtitle: String = "",
        fadeIn: Int = 20,
        stay: Int = 60,
        fadeOut: Int = 20
    ) {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut)
    }

    /**
     * Send an actionbar to a player
     * @param player The player to send the actionbar to
     * @param text The actionbar text
     */
    fun actionbar(
        player: Player,
        text: String
    ) {
       player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(text))
    }

}