package me.honkling.neopbb.gui

import me.honkling.neopbb.instance
import me.honkling.neopbb.lib.builder
import me.honkling.neopbb.lib.cloak
import me.honkling.neopbb.lib.coal
import me.honkling.neopbb.lib.mm
import me.honkling.neopbb.lib.paper
import me.honkling.neopbb.lib.pebble
import me.honkling.neopbb.lib.rock
import me.honkling.neopbb.lib.scrapMetal
import me.honkling.neopbb.lib.supremeStick
import me.honkling.neopbb.lib.wireCutters
import me.honkling.neopbb.profile.keycard
import me.honkling.neopbb.profile.money
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class Crafting {
    val inventory = Bukkit.createInventory(null, 9, Component.text("Crafting"))

    class EventNode(val gui: Crafting, val player: Player) : Listener {
        @EventHandler
        fun onClick(event: InventoryClickEvent) {
            if (event.whoClicked != player || event.inventory != gui.inventory)
                return

            fun tryCraft(result: ItemStack, vararg ingredients: Pair<Int, ItemStack>, cost: Float = 0f) {
                if (ingredients.any { !player.inventory.containsAtLeast(it.second, it.first) } || cost > player.money) {
                    player.playSound(Sound.sound {
                        it.type(Key.key("minecraft:entity.villager.no"))
                    })
                    return
                }

                player.money -= cost
                player.inventory.removeItemAnySlot(*ingredients.map { it.second.asQuantity(it.first) }.toTypedArray())
                player.give(result)
                player.playSound(Sound.sound {
                    it.type(Key.key("minecraft:entity.item.pickup"))
                })
            }

            event.isCancelled = true

            when (event.slot) {
                0 -> tryCraft(rock, 9 to pebble)
                1 -> tryCraft(paper, 1 to coal, 1 to scrapMetal, cost = 15f)
                2 -> tryCraft(keycard, 3 to paper, 2 to supremeStick)
                3 -> tryCraft(wireCutters, 4 to scrapMetal, 2 to supremeStick, 1 to rock)
                4 -> tryCraft(cloak, 1 to coal, cost = 15f)
            }
        }

        @EventHandler
        fun onClose(event: InventoryCloseEvent) {
            if (event.player == player && event.inventory == gui.inventory)
                HandlerList.unregisterAll(this)
        }
    }

    init {
        inventory.setItem(0, ItemStack(Material.COBBLESTONE)
            .builder()
            .displayName("Rock")
            .lore("Recipe:".mm, "9x <s>Pebbles".mm)
            .build())

        inventory.setItem(1, ItemStack(Material.PAPER)
            .builder()
            .lore("Recipe:".mm, "1x <s>Coal".mm, "1x <s>Scrap Metal".mm, "15$".mm)
            .build())

        inventory.setItem(2, ItemStack(Material.TRIPWIRE_HOOK)
            .builder()
            .displayName("Keycard")
            .lore("Recipe:".mm, "3x <s>Paper".mm, "2x <s>Sticks".mm)
            .build())

        inventory.setItem(3, ItemStack(Material.SHEARS)
            .builder()
            .displayName("Wire Cutters")
            .lore("Recipe:".mm, "4x <s>Scrap Metal".mm, "2x <s>Sticks".mm, "1x <s>Rock".mm)
            .build())

        inventory.setItem(4, ItemStack(Material.LEATHER_CHESTPLATE)
            .builder()
            .displayName("Cloak")
            .lore("Recipe:".mm, "1x <s>Coal".mm, "15$".mm)
            .build())
    }

    fun Player.openGUI() {
        val events = EventNode(this@Crafting, this)
        Bukkit.getPluginManager().registerEvents(events, instance)
        openInventory(this@Crafting.inventory)
    }
}