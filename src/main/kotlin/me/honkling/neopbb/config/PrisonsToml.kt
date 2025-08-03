package me.honkling.neopbb.config

import org.bukkit.Location
import org.bukkit.Material

data class PrisonsToml(
    val prisons: Map<String, Prison>
) {
    data class Prison(
        val name: String,
        val icon: Material,
        val wardenSpawn: Location,
        val prisonerSpawn: Location,
        val respawn: Location,
        val bertrude: Location
    )
}
