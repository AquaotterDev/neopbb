@file:Listener

package me.honkling.neopbb.event

import me.honkling.commando.spigot.event.Listener
import me.honkling.neopbb.lib.getAllSignLines
import me.honkling.neopbb.lib.mm
import me.honkling.neopbb.profile.Role
import me.honkling.neopbb.profile.prepare
import me.honkling.neopbb.profile.role
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