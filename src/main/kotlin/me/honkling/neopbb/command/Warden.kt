@file:Command("warden")

package me.honkling.neopbb.command

import me.honkling.commando.common.command.node.ParameterNode
import me.honkling.commando.spigot.command.Command
import me.honkling.neopbb.lib.mm
import me.honkling.neopbb.profile.Invite
import me.honkling.neopbb.profile.Role
import me.honkling.neopbb.profile.invite
import me.honkling.neopbb.profile.prepare
import me.honkling.neopbb.profile.role
import me.honkling.neopbb.profile.warden
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

private fun warden(player: Player) {
    if (warden != null)
        return player.sendMessage("<p>There is already a warden.".mm)

    player.role = Role.Warden
    player.prepare(true)
}

private fun hire(player: Player, target: Player, role: Role) {
    if (warden != player)
        return player.sendMessage("<p>You aren't the warden.".mm)

    if (target.role.isAuthority)
        return player.sendMessage("<p><s>${target.name}</s> is already a guard.".mm)

    if (target.invite != null)
        return player.sendMessage("<p><s>${target.name}</s> already has an ongoing invitation.".mm)

    player.sendMessage("<p><s>${target.name}</s> has been sent an invitation.".mm)
    target.sendMessage("\n<p>The warden wants you to be a guard!\n<p><s><u><click:run_command:/accept>Accept</s>\n".mm)
    target.invite = Invite(target, role).schedule()
}

private fun `hire$complete`(sender: CommandSender, node: ParameterNode<Command>, input: String): List<String> {
    return when (node.name) {
        "target" -> Bukkit.getOnlinePlayers()
            .filter { !it.role.isAuthority }
            .map { it.name }
            .filter { it.contains(input, true) }
        "role" -> Role.entries
            .filter { it.isAuthority && it != Role.Warden }
            .map { it.name }
            .filter { it.contains(input, true) }
        else -> emptyList()
    }
}

private fun fire(player: Player, target: Player) {
    if (warden != player)
        return player.sendMessage("<p>You aren't the warden.".mm)

    if (!target.role.isAuthority)
        return player.sendMessage("<p><s>${target.name}</s> isn't a guard.".mm)

    Bukkit.getServer().sendMessage("<p><s>${target.name}</s> has been fired!".mm)
    target.role = Role.Prisoner
    target.prepare(true)
}

private fun `fire$complete`(sender: CommandSender, node: ParameterNode<Command>, input: String): List<String> {
    return Bukkit.getOnlinePlayers()
        .filter { it.role.isAuthority }
        .map { it.name }
        .filter { it.contains(input, true) }
}