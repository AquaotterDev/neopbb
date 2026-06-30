package dev.rosalyn.neopbb.task

import dev.rosalyn.neopbb.currentPrison
import dev.rosalyn.neopbb.lib.isInCell
import dev.rosalyn.neopbb.profile.Role
import dev.rosalyn.neopbb.profile.inCell
import dev.rosalyn.neopbb.profile.isRespawning
import dev.rosalyn.neopbb.profile.role
import dev.rosalyn.neopbb.schedule.Period
import dev.rosalyn.neopbb.schedule.period
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.TitlePart
import org.bukkit.Bukkit
import org.bukkit.potion.PotionEffectType

internal fun executeLightsOut() {
    if (period != Period.LightsOut && period != Period.Lockdown)
        return

    for (player in Bukkit.getOnlinePlayers()) {
        val role = player.role

        if (role.isAuthority || role == Role.Criminal || role == Role.Solitary
            || ((player.getPotionEffect(PotionEffectType.GLOWING)?.duration ?: 0) >= 5))
            continue

        val isInCell = isInCell(player, currentPrison.prisonerCells) && !player.isRespawning
        player.inCell = isInCell

        if (isInCell) {
            player.sendTitlePart(TitlePart.TITLE, Component.empty())
            player.sendTitlePart(TitlePart.SUBTITLE, Component.empty())
            continue
        }

        player.addPotionEffect(PotionEffectType.GLOWING.createEffect(5, 0))
    }
}