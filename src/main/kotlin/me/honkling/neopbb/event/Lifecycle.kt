@file:Listener

package me.honkling.neopbb.event

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent
import me.honkling.commando.spigot.event.Listener
import me.honkling.neopbb.lib.mm
import me.honkling.neopbb.profile.Role
import me.honkling.neopbb.profile.cleanUp
import me.honkling.neopbb.profile.prepare
import me.honkling.neopbb.profile.role
import me.honkling.neopbb.profile.warden
import org.bukkit.Bukkit
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerRespawnEvent

private fun onJoin(event: PlayerJoinEvent) {
    val player = event.player
    player.prepare(true)

    event.joinMessage("<p><s>${player.name}</s> is now in prison.".mm)
}

private fun onQuit(event: PlayerQuitEvent) {
    val player = event.player

    if (warden == player) {
        player.role = Role.Prisoner
        player.prepare(true)
        Bukkit.getServer().sendMessage("<p>The warden has left!".mm)
    }

    player.cleanUp()
    event.quitMessage("<p><s>${player.name}</s> has ran off.".mm)
}

private fun onRespawn(event: PlayerPostRespawnEvent) {
    val player = event.player

    if (player == warden)
        player.role = Role.Prisoner

    player.prepare(true)
}