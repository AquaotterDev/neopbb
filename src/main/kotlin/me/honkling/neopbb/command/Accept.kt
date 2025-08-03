@file:Command("accept")

package me.honkling.neopbb.command

import me.honkling.commando.spigot.command.Command
import me.honkling.neopbb.lib.mm
import me.honkling.neopbb.profile.invite
import me.honkling.neopbb.profile.prepare
import me.honkling.neopbb.profile.role
import org.bukkit.entity.Player

private fun accept(player: Player) {
    val invite = player.invite
        ?: return player.sendMessage("<p>You don't have any invitations.".mm)

    player.role = invite.role
    player.prepare(true)
    invite.cancel()
}