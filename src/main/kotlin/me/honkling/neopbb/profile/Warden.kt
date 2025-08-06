package me.honkling.neopbb.profile

import org.bukkit.Bukkit
import org.bukkit.entity.Player

val warden: Player?
    get() = Bukkit.getOnlinePlayers().find { it.role == Role.Warden }

var wardenCooldown = 0
var swatUnlocked = false