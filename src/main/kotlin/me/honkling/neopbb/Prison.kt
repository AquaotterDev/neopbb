package me.honkling.neopbb

import me.honkling.neopbb.config.PrisonsToml
import me.honkling.neopbb.profile.Role
import me.honkling.neopbb.profile.role
import org.bukkit.Bukkit

lateinit var currentPrison: PrisonsToml.Prison; internal set
var lastMapSwitch = 0L

fun switchMap(newPrison: PrisonsToml.Prison) {
    currentPrison = newPrison

    for (player in Bukkit.getOnlinePlayers()) {
        val role = player.role
        player.teleport(when (role) {
            Role.Warden -> newPrison.wardenSpawn
            else -> newPrison.prisonerSpawn
        })
    }
}