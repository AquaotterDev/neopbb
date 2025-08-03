@file:Command("resign")

package me.honkling.neopbb.command

import me.honkling.commando.spigot.command.Command
import me.honkling.neopbb.lib.mm
import me.honkling.neopbb.profile.Role
import me.honkling.neopbb.profile.prepare
import me.honkling.neopbb.profile.role
import me.honkling.neopbb.profile.warden
import org.bukkit.Bukkit
import org.bukkit.entity.Player

private fun resign(player: Player) {
    if (warden == player)
        Bukkit.getServer().sendMessage("<p>The warden has resigned!".mm)

    player.role = Role.Prisoner
    player.prepare(true)
}