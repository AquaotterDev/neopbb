@file:Listener

package dev.rosalyn.neopbb.event

import io.papermc.paper.event.player.AsyncChatEvent
import me.honkling.commando.spigot.event.Listener
import dev.rosalyn.neopbb.command.teamchat
import dev.rosalyn.neopbb.lib.mm
import dev.rosalyn.neopbb.profile.Role
import dev.rosalyn.neopbb.profile.rankAndName
import dev.rosalyn.neopbb.profile.role
import dev.rosalyn.neopbb.profile.teamChat
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

private fun onChat(event: AsyncChatEvent) {
    val player = event.player
    val message = event.message()
    val role = player.role

    if (role == Role.Solitary) {
        event.isCancelled = true
        player.sendMessage("<p>People in solitary cannot speak.".mm)
        return
    }

    if (player.teamChat) {
        teamchat(player, PlainTextComponentSerializer.plainText().serialize(message))
        event.isCancelled = true
        return
    }

    event.renderer { _, _, _, _ ->
        player.rankAndName()
            .append(Component.text(": ")
                .color(if (role == Role.Warden) NamedTextColor.RED else NamedTextColor.GRAY)
                .append(message))
    }
}