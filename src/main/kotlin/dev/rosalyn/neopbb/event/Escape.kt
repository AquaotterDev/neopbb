@file:Listener

package dev.rosalyn.neopbb.event

import me.honkling.commando.spigot.event.Listener
import dev.rosalyn.neopbb.lib.getAllSignLines
import dev.rosalyn.neopbb.lib.mm
import dev.rosalyn.neopbb.profile.Role
import dev.rosalyn.neopbb.profile.isRespawning
import dev.rosalyn.neopbb.profile.prepare
import dev.rosalyn.neopbb.profile.role
import org.bukkit.block.Sign
import org.bukkit.event.player.PlayerInteractEvent

private fun onInteract(event: PlayerInteractEvent) {
    val player = event.player
    val state = event.clickedBlock?.state as? Sign
        ?: return
    val lines = getAllSignLines(state)

    for (line in lines)
    when (line) {
        "Get Gear" -> {
            if (player.role != Role.Prisoner)
                return player.sendMessage("<p>Only prisoners can escape.".mm)

            if(player.isRespawning)
                return player.sendMessage("<p>You can't escape while respawning.".mm)

            player.role = Role.Criminal
            return player.prepare(false, broadcast = true)
        }
        "Restore Kit" -> {
            if (player.role == Role.Warden || !player.role.isAuthority)
                return player.sendMessage("<p>Only guards, nurses, and swats can restore their kit.".mm)

            return player.prepare(true)
        }
    }
}