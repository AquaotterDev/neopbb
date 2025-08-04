@file:Listener

package me.honkling.neopbb.event

import me.honkling.commando.spigot.event.Listener
import me.honkling.neopbb.lib.pebble
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

private fun onInteract(event: PlayerInteractEvent) {
    val player = event.player

    if (event.clickedBlock?.type?.name?.contains("COBBLESTONE") != true || event.hand == EquipmentSlot.OFF_HAND)
        return

    player.give(pebble)
    player.playSound(Sound.sound {
        it.type(Key.key("minecraft:entity.item.pickup"))
    })
}