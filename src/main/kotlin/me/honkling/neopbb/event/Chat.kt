@file:Listener

package me.honkling.neopbb.event

import io.papermc.paper.event.player.AsyncChatEvent
import me.honkling.commando.spigot.event.Listener
import me.honkling.neopbb.lib.mm
import me.honkling.neopbb.profile.Role
import me.honkling.neopbb.profile.role
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

private fun onChat(event: AsyncChatEvent) {
    val player = event.player
    val message = event.message()
    val role = player.role

    if (role == Role.Solitary) {
        event.isCancelled = true
        player.sendMessage("<p>People in solitary cannot speak.".mm)
        return
    }

    val prefix = role.prefix
    val name = player.name()

    event.renderer { _, _, _, _ ->
        prefix.appendSpace()
            .append(name.color(if (role == Role.Warden) NamedTextColor.WHITE else NamedTextColor.GRAY))
            .append(Component.text(": ")
                .color(if (role == Role.Warden) NamedTextColor.RED else NamedTextColor.GRAY)
                .append(message))
    }
}