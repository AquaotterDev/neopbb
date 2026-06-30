@file:Command("accept")

package dev.rosalyn.neopbb.command

import me.honkling.commando.spigot.command.Command
import dev.rosalyn.neopbb.currentPrison
import dev.rosalyn.neopbb.lib.mm
import dev.rosalyn.neopbb.profile.*
import org.bukkit.entity.Player

private fun accept(player: Player) {
    if (player.isRespawning)
        return player.sendMessage("<p>You cannot accept invitations while dead.".mm)

    if (player.inSolitary)
        return player.sendMessage("<p>You cannot accept invitations while in solitary.".mm)

    if (player.health <= 10)
        return player.sendMessage("<p>You cannot accept invitations while under half health.".mm)

    val invite = player.invite
        ?: return player.sendMessage("<p>You don't have any invitations.".mm)

    if (invite.role == Role.Warden && warden != null) {
        val pastWarden = warden!!

        if (pastWarden.health <= 10)
            return player.sendMessage("<p>Warden cannot be passed to you while the current warden is under half health.".mm)

        pastWarden.role = Role.Prisoner
        pastWarden.prepare(true, broadcast = false)
    }

    val isInBlackMarket = player.isInBlackMarket
    player.role = invite.role
    player.prepare(true, broadcast = true)
    invite.cancel()
    player.invite = null

    if (player.role.isAuthority && isInBlackMarket)
        player.teleport(currentPrison.blackMarketOut)
}