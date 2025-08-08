@file:Listener

package me.honkling.neopbb.event

import me.honkling.commando.spigot.event.Listener
import org.bukkit.Material
import org.bukkit.event.player.PlayerItemConsumeEvent

private fun onConsume(event: PlayerItemConsumeEvent) {
    if (event.item.type == Material.GOLDEN_APPLE)
        event.player.setCooldown(Material.GOLDEN_APPLE, 20 * 60)
}