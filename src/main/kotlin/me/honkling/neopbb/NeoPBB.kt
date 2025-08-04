package me.honkling.neopbb

import me.honkling.commando.spigot.SpigotCommando
import me.honkling.neopbb.config.prisonsToml
import me.honkling.neopbb.config.reloadPrisonsToml
import me.honkling.neopbb.schedule.registerScheduler
import me.honkling.neopbb.task.registerTasks
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.World
import org.bukkit.plugin.java.JavaPlugin

val instance = JavaPlugin.getPlugin(NeoPBB::class.java)
lateinit var world: World; private set

class NeoPBB : JavaPlugin() {
    override fun onEnable() {
        world = Bukkit.getWorlds()[0]
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true)

        reloadPrisonsToml()
        currentPrison = prisonsToml.prisons[0]

        registerScheduler()
        registerTasks()

        val commando = SpigotCommando(this)
        commando.register("me.honkling.neopbb", "command", "event")
    }

    override fun onDisable() {

    }
}
