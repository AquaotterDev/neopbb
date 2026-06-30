package dev.rosalyn.neopbb.task

import dev.rosalyn.neopbb.profile.Role
import dev.rosalyn.neopbb.profile.attendedRollCall
import dev.rosalyn.neopbb.profile.role
import dev.rosalyn.neopbb.schedule.Period
import dev.rosalyn.neopbb.schedule.period
import dev.rosalyn.neopbb.schedule.tickSchedule
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.TitlePart
import org.bukkit.Bukkit
import org.bukkit.Material

internal fun executeRollCall() {
    if (period != Period.RollCall)
        return

    for (player in Bukkit.getOnlinePlayers()) {
        if (player.attendedRollCall || player.role.isAuthority || player.role == Role.Criminal || player.role == Role.Solitary)
            continue

        val below = player.location.clone().add(0.0, -1.0, 0.0)
        val block = player.world.getBlockAt(below).type

        if (block == Material.RED_SAND || block == Material.RED_SANDSTONE || block == Material.CUT_RED_SANDSTONE) {
            player.attendedRollCall = true
            player.sendTitlePart(TitlePart.SUBTITLE, Component.empty())
            player.playSound(Sound.sound {
                it.type(Key.key("block.note_block.chime"))
            })
            tickSchedule()
        }
    }
}