@file:Command("teamchat", "tc")

package me.honkling.neopbb.command

import me.honkling.commando.spigot.command.Command
import me.honkling.neopbb.lib.mm
import me.honkling.neopbb.profile.role
import me.honkling.neopbb.profile.teamChat
import net.kyori.adventure.audience.Audience
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

    Audience.audience(others).sendMessage(
        "$prefix <gray>${sender.name}: ".mm.append(Component.text(message)
            .color(NamedTextColor.GRAY))
    )
}