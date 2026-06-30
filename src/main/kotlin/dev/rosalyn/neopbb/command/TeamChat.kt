@file:Command("teamchat", "tc")

package dev.rosalyn.neopbb.command

import io.papermc.paper.event.player.AsyncChatEvent
import me.honkling.commando.spigot.command.Command
import dev.rosalyn.neopbb.event.runFilter
import dev.rosalyn.neopbb.lib.mm
import dev.rosalyn.neopbb.profile.role
import dev.rosalyn.neopbb.profile.teamChat
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.chat.SignedMessage
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player

private fun teamchat(sender: Player) {
    sender.teamChat = !sender.teamChat
    sender.sendMessage("<p>Team chat has been toggled ${if (sender.teamChat) "on" else "off"}.".mm)
}

internal fun teamchat(sender: Player, message: String) {
    val prefix = if (sender.role.isAuthority) "<blue>[GUARD CHAT]</blue>" else "<gold>[PRISONER CHAT]</gold>"
    val others = Bukkit.getOnlinePlayers().filter { it.role.isAuthority == sender.role.isAuthority }
    val viewers = Audience.audience(others)

    val component = Component.text(message)
    val event = AsyncChatEvent(
        false,
        sender,
        others.toSet(),
        { _, _, _, _ ->
            "$prefix <gray>${sender.name}: ".mm.append(Component.text(message)
                .color(NamedTextColor.GRAY))
        },
        component,
        component,
        SignedMessage.system(message, component)
    )

    runFilter(event)
    if (!event.isCancelled)
        viewers.sendMessage(event.renderer().render(
            sender,
            sender.displayName(),
            component,
            viewers
        ))
}