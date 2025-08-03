package me.honkling.neopbb

import me.honkling.commando.spigot.SpigotCommando
import me.honkling.neopbb.config.prisonsToml
import me.honkling.neopbb.config.reloadPrisonsToml
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.plugin.java.JavaPlugin

val instance = JavaPlugin.getPlugin(NeoPBB::class.java)
lateinit var world: World; private set

class NeoPBB : JavaPlugin() {
    override fun onEnable() {
        world = Bukkit.getWorlds()[0]

        reloadPrisonsToml()
        currentPrison = prisonsToml.prisons[0]

        val commando = SpigotCommando(this)
        commando.register("me.honkling.neopbb", "command", "event")
    }

    override fun onDisable() {

    }
}
