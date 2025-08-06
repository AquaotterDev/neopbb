@file:Listener

package me.honkling.neopbb.event

import io.papermc.paper.event.player.AsyncChatEvent
import me.honkling.commando.spigot.event.Listener
import me.honkling.neopbb.command.teamchat
import me.honkling.neopbb.lib.mm
import me.honkling.neopbb.profile.Role
import me.honkling.neopbb.profile.rankAndName
import me.honkling.neopbb.profile.role
import me.honkling.neopbb.profile.teamChat
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