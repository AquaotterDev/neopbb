package dev.rosalyn.neopbb

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.PacketEventsAPI
import com.github.retrooper.packetevents.event.PacketListenerPriority
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.honkling.commando.spigot.SpigotCommando
import dev.rosalyn.neopbb.config.PrisonsToml
import dev.rosalyn.neopbb.config.prisonsToml
import dev.rosalyn.neopbb.config.reloadConfigToml
import dev.rosalyn.neopbb.config.reloadFilterToml
import dev.rosalyn.neopbb.config.reloadPrisonsToml
import dev.rosalyn.neopbb.discord.initializeKord
import dev.rosalyn.neopbb.event.packet.PacketInteraction
import dev.rosalyn.neopbb.schedule.registerScheduler
import dev.rosalyn.neopbb.task.registerTasks
import dev.rosalyn.neopbb.type.PrisonType
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.World
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

val instance = JavaPlugin.getPlugin(NeoPBB::class.java)
val scope = CoroutineScope(Dispatchers.IO)

lateinit var world: World; private set
lateinit var packetEvents: PacketEventsAPI<Plugin>; private set

class NeoPBB : JavaPlugin() {
    override fun onLoad() {
        val pluginManager = Bukkit.getPluginManager()
        val cosmetics = pluginManager.getPlugin("MinehutCosmetics")
        cosmetics?.let { pluginManager.disablePlugin(it) }

        packetEvents = SpigotPacketEventsBuilder.build(this)
        PacketEvents.setAPI(packetEvents)
        packetEvents.load()
    }

    override fun onEnable() {
        packetEvents.init()

        world = Bukkit.getWorlds()[0]
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true)

        reloadConfigToml()
        reloadFilterToml()
        reloadPrisonsToml()
        currentPrison = prisonsToml.prisons[0]

        spawnBertrude()
        registerScheduler()
        registerTasks()

        scope.launch {
            initializeKord()
        }

        val commando = SpigotCommando(this)
        val packetInteraction = PacketInteraction(commando)
        commando.interactionRegistry.register(packetInteraction)
        commando.typeRegistry.register(PrisonType, PrisonsToml.Prison::class)
        commando.register("dev.rosalyn.neopbb", "command", "event")
        packetEvents.eventManager.registerListener(packetInteraction, PacketListenerPriority.NORMAL)
    }

    override fun onDisable() {

    }
}
