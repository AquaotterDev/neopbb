@file:Listener

package me.honkling.neopbb.event

import me.honkling.commando.spigot.event.Listener
import me.honkling.neopbb.profile.role
import org.bukkit.Material
import org.bukkit.event.player.PlayerItemConsumeEvent

private fun onConsume(event: PlayerItemConsumeEvent) {
    val player = event.player

    if (event.item.type == Material.GOLDEN_APPLE)
        player.setCooldown(Material.GOLDEN_APPLE, 20 * if (player.role.isAuthority) 12 else 10)
}