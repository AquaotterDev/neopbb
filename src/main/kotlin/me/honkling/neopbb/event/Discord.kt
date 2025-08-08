@file:Listener

package me.honkling.neopbb.event

import io.papermc.paper.event.player.AsyncChatEvent
import kotlinx.coroutines.launch
import me.honkling.commando.spigot.event.Listener
import me.honkling.commando.spigot.event.Priority
import me.honkling.neopbb.discord.channel
import me.honkling.neopbb.scope
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.event.EventPriority

@Priority(EventPriority.HIGHEST)
private fun onChat(event: AsyncChatEvent) {
    if (event.isCancelled)
        return

    val username = event.player.name.replace("_", "\\_")
    val message = PlainTextComponentSerializer.plainText().serialize(event.message())
        .replace("@", "`@`")
        .replace("_", "\\_")

    scope.launch {
        channel.createMessage("**$username**: $message")
    }
}