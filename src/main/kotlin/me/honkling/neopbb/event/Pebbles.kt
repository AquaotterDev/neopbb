@file:Listener

package me.honkling.neopbb.event

import me.honkling.commando.spigot.event.Listener
import me.honkling.neopbb.lib.pebble
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.bukkit.event.player.PlayerInteractEvent

private fun onInteract(event: PlayerInteractEvent) {
    val player = event.player

    if (event.clickedBlock?.type?.name?.contains("COBBLESTONE") != true)
        return

    player.give(pebble)
    player.playSound(Sound.sound {
        it.type(Key.key("minecraft:entity.item.pickup"))
    })
}