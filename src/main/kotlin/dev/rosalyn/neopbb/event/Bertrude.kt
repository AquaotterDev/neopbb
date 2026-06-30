@file:Listener

package dev.rosalyn.neopbb.event

import me.honkling.commando.spigot.event.Listener
import dev.rosalyn.neopbb.bertrude
import dev.rosalyn.neopbb.gui.Bertrude
import dev.rosalyn.neopbb.lib.mm
import org.bukkit.event.player.PlayerInteractAtEntityEvent

private fun onInteract(event: PlayerInteractAtEntityEvent) {
    val player = event.player
    val entity = event.rightClicked

    if (entity != bertrude)
        return

    event.isCancelled = true
    player.sendMessage("<s>bertrude <gray>»</s> hello i am bertrude".mm)
    val gui = Bertrude(player)
    gui.openGUI()
}