package me.honkling.neopbb

import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.scoreboard.Team

val prisonersTeam = getOrCreateTeam("Prisoners", NamedTextColor.GOLD)
val criminalsTeam = getOrCreateTeam("Criminals", NamedTextColor.DARK_RED)
val guardsTeam = getOrCreateTeam("Guards", NamedTextColor.BLUE)
val nursesTeam = getOrCreateTeam("Nurses", NamedTextColor.LIGHT_PURPLE)
val swatsTeam = getOrCreateTeam("Guards", NamedTextColor.DARK_GRAY)
val wardenTeam = getOrCreateTeam("Warden", NamedTextColor.RED)
val solitaryTeam = getOrCreateTeam("Solitary", NamedTextColor.BLACK)

private fun getOrCreateTeam(name: String, color: NamedTextColor): Team {
    val scoreboard = Bukkit.getScoreboardManager().mainScoreboard
    scoreboard.getTeam(name)?.let { return it }
    val team = scoreboard.registerNewTeam(name)
    team.color(color)
    return team
}