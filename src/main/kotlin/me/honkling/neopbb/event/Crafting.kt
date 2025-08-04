@file:Listener

package me.honkling.neopbb.event

import me.honkling.commando.spigot.event.Listener
import me.honkling.neopbb.gui.Crafting
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent

private fun onInteract(event: PlayerInteractEvent) {
    val player = event.player

    if (event.clickedBlock?.type != Material.CRAFTING_TABLE)
        return

    val gui = Crafting()

    with (gui) {
        player.openGUI()
    }
}