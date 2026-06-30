package dev.rosalyn.neopbb

import dev.rosalyn.neopbb.config.PrisonsToml
import dev.rosalyn.neopbb.lib.getRandomCell
import dev.rosalyn.neopbb.profile.Role
import dev.rosalyn.neopbb.profile.role
import org.bukkit.Bukkit
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

lateinit var currentPrison: PrisonsToml.Prison; internal set
var lastLockdown = 0L
var lastMapSwitch = 0L

@OptIn(ExperimentalTime::class)
fun switchMap(newPrison: PrisonsToml.Prison) {
    currentPrison = newPrison
    lastMapSwitch = Clock.System.now().epochSeconds
    bertrude.teleport(currentPrison.bertrude)

    for (player in Bukkit.getOnlinePlayers()) {
        val role = player.role
        player.teleport(when (role) {
            Role.Warden -> newPrison.wardenSpawn
            Role.Solitary -> getRandomCell(newPrison.solitaryCells)
            else -> getRandomCell(newPrison.prisonerCells)
        })
    }
}