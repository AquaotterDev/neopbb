@file:Listener

package me.honkling.neopbb.event

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent
import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent
import me.honkling.commando.spigot.event.Listener
import me.honkling.neopbb.currentPrison
import me.honkling.neopbb.instance
import me.honkling.neopbb.lib.mm
import me.honkling.neopbb.profile.Role
import me.honkling.neopbb.profile.cleanUp
import me.honkling.neopbb.profile.forceRespawn
import me.honkling.neopbb.profile.inSolitary
import me.honkling.neopbb.profile.isRespawning
import me.honkling.neopbb.profile.money
import me.honkling.neopbb.profile.prepare
import me.honkling.neopbb.profile.respawnTask
import me.honkling.neopbb.profile.role
import me.honkling.neopbb.profile.warden
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerRespawnEvent
import java.time.Duration
import kotlin.properties.Delegates

private fun onJoin(event: PlayerJoinEvent) {
    val player = event.player

    if (player.inSolitary)
        player.role = Role.Solitary

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

private fun onDeath(event: PlayerDeathEvent) {
    val player = event.player
    val attacker = event.damageSource.causingEntity as? Player
        ?: return

    if (player.isGlowing) {
        attacker.money += 100
        attacker.sendMessage("<p><s>+100$</s> for killing a glowing player.".mm)
        player.isGlowing = false
    }
}

private fun onRespawn(event: PlayerPostRespawnEvent) {
    val player = event.player
    val attacker = player.lastDamageCause?.damageSource?.causingEntity

    player.sendTitlePart(TitlePart.TITLE, "<red>Respawning...".mm)
    player.sendTitlePart(TitlePart.SUBTITLE, "<gray>Wait 10 seconds.".mm)
    player.sendTitlePart(TitlePart.TIMES, Title.Times.times(
        Duration.ZERO,
        Duration.ofSeconds(10),
        Duration.ZERO
    ))

    player.gameMode = GameMode.SPECTATOR
    var ticks = 20L * 10
    player.respawnTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, {
        ticks--

        if (ticks == 0L) {
            player.forceRespawn()
            return@scheduleSyncRepeatingTask
        }

        if (attacker != null) {
            // Set to null and then the attacker to refresh the camera
            // (fixes bug where the client might not actually spectate
            //  if the client hasn't finished loading chunks yet)
            player.spectatorTarget = null
            player.spectatorTarget = attacker
        } else player.teleport(
            if (player.inSolitary) currentPrison.solitary
            else currentPrison.respawn
        )
    }, 0L, 1L)
}

private fun onCancelSpectate(event: PlayerStopSpectatingEntityEvent) {
    if (event.player.isRespawning)
        event.isCancelled = true
}