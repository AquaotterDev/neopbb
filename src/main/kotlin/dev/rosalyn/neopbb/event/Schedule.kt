@file:Listener

package dev.rosalyn.neopbb.event

import me.honkling.commando.spigot.event.Listener
import dev.rosalyn.neopbb.schedule.bossBars
import dev.rosalyn.neopbb.schedule.createBossBar
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

private fun onPlayerJoin(event: PlayerJoinEvent) {
    val player = event.player
    player.showBossBar(createBossBar(player))
}

private fun onPlayerQuit(event: PlayerQuitEvent) {
    val player = event.player
    val bossBar = bossBars[player]
        ?: return

    player.hideBossBar(bossBar)
    bossBars -= player
}