package dev.rosalyn.neopbb.task

import dev.rosalyn.neopbb.currentPrison
import dev.rosalyn.neopbb.lib.isInCell
import dev.rosalyn.neopbb.lib.mm
import dev.rosalyn.neopbb.profile.Role
import dev.rosalyn.neopbb.profile.isRespawning
import dev.rosalyn.neopbb.profile.role
import dev.rosalyn.neopbb.profile.solitaryTask
import net.kyori.adventure.audience.Audience
import org.bukkit.Bukkit
import org.bukkit.potion.PotionEffectType

internal fun executeSolitary() {
    val solitaryPlayers = Bukkit.getOnlinePlayers().filter { it.role == Role.Solitary }

    for (player in solitaryPlayers) {
        if (isInCell(player, currentPrison.solitaryCells) || player.isRespawning)
            continue

        player.role = Role.Prisoner
        player.role.team.addPlayer(player)
        player.solitaryTask?.let { Bukkit.getScheduler().cancelTask(it) }
        player.solitaryTask = null

        val guards = Audience.audience(Bukkit.getOnlinePlayers().filter { it.role.isAuthority })
        player.addPotionEffect(PotionEffectType.GLOWING.createEffect(20 * 30, 0))
        player.sendMessage("<p>You were caught escaping solitary!".mm)
        guards.sendMessage("<p><s>${player.name}</s> was caught escaping solitary!".mm)
    }
}