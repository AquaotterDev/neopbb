@file:Listener

package dev.rosalyn.neopbb.event

import me.honkling.commando.spigot.event.Listener
import dev.rosalyn.neopbb.lib.pebble
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

private fun onInteract(event: PlayerInteractEvent) {
    val player = event.player

    if (event.clickedBlock?.type?.name?.contains("COBBLESTONE") != true || event.hand == EquipmentSlot.OFF_HAND || !event.action.isRightClick)
        return

    player.give(pebble)
    player.playSound(Sound.sound {
        it.type(Key.key("minecraft:entity.item.pickup"))
    })
}