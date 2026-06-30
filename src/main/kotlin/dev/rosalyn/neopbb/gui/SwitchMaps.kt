package dev.rosalyn.neopbb.gui

import dev.rosalyn.neopbb.config.prisonsToml
import dev.rosalyn.neopbb.currentPrison
import dev.rosalyn.neopbb.instance
import dev.rosalyn.neopbb.lib.builder
import dev.rosalyn.neopbb.lib.mm
import dev.rosalyn.neopbb.switchMap
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import kotlin.math.ceil

class SwitchMaps {
    private fun buildInventory(): Inventory {
        val size = ceil(prisonsToml.prisons.size / 9.0).toInt().coerceIn(0, 6)* 9
        val inventory = Bukkit.createInventory(null, size, Component.text("Switch Maps"))

        for ((index, prison) in prisonsToml.prisons.withIndex()) {
            val itemStack = ItemStack(prison.icon)
                .builder()
                .displayName(prison.name.mm)
                .build()

            inventory.setItem(index, itemStack)
        }

        return inventory
    }

    class EventNode(val inventory: Inventory, val player: Player) : Listener {
        @EventHandler
        fun onClick(event: InventoryClickEvent) {
            if (event.whoClicked != player || event.inventory != inventory)
                return

            val index = event.slot
            val prison = prisonsToml.prisons.getOrNull(index)
                ?: return

            if (prison.name == currentPrison.name) {
                player.sendMessage("<p>This map is already selected.".mm)
                return
            }

            switchMap(prison)
        }

        @EventHandler
        fun onClose(event: InventoryCloseEvent) {
            if (event.player == player && event.inventory == inventory)
                HandlerList.unregisterAll(this)
        }
    }

    fun Player.openGUI() {
        val inventory = buildInventory()
        val events = EventNode(inventory, this)
        Bukkit.getPluginManager().registerEvents(events, instance)
        openInventory(inventory)
    }
}