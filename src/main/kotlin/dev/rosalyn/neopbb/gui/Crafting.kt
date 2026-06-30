package dev.rosalyn.neopbb.gui

import dev.rosalyn.neopbb.instance
import dev.rosalyn.neopbb.lib.builder
import dev.rosalyn.neopbb.lib.cloak
import dev.rosalyn.neopbb.lib.coal
import dev.rosalyn.neopbb.lib.formatCurrency
import dev.rosalyn.neopbb.lib.lumber
import dev.rosalyn.neopbb.lib.mm
import dev.rosalyn.neopbb.lib.paper
import dev.rosalyn.neopbb.lib.pebble
import dev.rosalyn.neopbb.lib.rock
import dev.rosalyn.neopbb.lib.scrapMetal
import dev.rosalyn.neopbb.lib.supremeStick
import dev.rosalyn.neopbb.lib.wireCutters
import dev.rosalyn.neopbb.lib.makeshiftSword
import dev.rosalyn.neopbb.profile.keycard
import dev.rosalyn.neopbb.profile.money
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

val recipes = mutableListOf(
    recipe(makeshiftSword, 3 to lumber, cost = 25f),
    recipe(rock, 9 to pebble),
    recipe(paper, 1 to coal, 1 to scrapMetal, cost = 15f),
    recipe(keycard, 3 to paper, 2 to supremeStick),
    recipe(wireCutters, 4 to scrapMetal, 2 to supremeStick, 1 to rock),
    recipe(cloak, 1 to coal, cost = 15f)
)

data class Recipe(
    val outcome: ItemStack,
    val ingredients: List<Pair<Int, ItemStack>>,
    val cost: Float = 0f
)

fun recipe(outcome: ItemStack, vararg ingredients: Pair<Int, ItemStack>, cost: Float = 0f)
    = Recipe(outcome, ingredients.toList(), cost)

class Crafting {
    val inventory = Bukkit.createInventory(null, 9, Component.text("Crafting"))

    class EventNode(val gui: Crafting, val player: Player) : Listener {
        @EventHandler
        fun onClick(event: InventoryClickEvent) {
            if (event.whoClicked != player || event.inventory != gui.inventory)
                return

            fun tryCraft(recipe: Recipe) {
                val (result, ingredients, cost) = recipe
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
            val recipe = recipes.getOrNull(event.slot)
                ?: return

            tryCraft(recipe)
        }

        @EventHandler
        fun onClose(event: InventoryCloseEvent) {
            if (event.player == player && event.inventory == gui.inventory)
                HandlerList.unregisterAll(this)
        }
    }

    init {
        fun ItemStack.name()
            = (displayName() as TranslatableComponent).arguments()[0].asComponent()
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)

        for ((index, recipe) in recipes.withIndex()) {
            val (item, ingredients, cost) = recipe
            val ingredientList = ingredients.map { (amount, it) ->
                val name = PlainTextComponentSerializer.plainText().serialize(it.name())
                "${amount}x <s>$name".mm
            }.toMutableList()

            if (cost > 0f)
                ingredientList += "<s>${formatCurrency(cost)}".mm

            inventory.setItem(index, ItemStack(item.type)
                .builder()
                .displayName(item.name())
                .lore("Recipe:".mm, *ingredientList.toTypedArray())
                .build())
        }
    }

    fun Player.openGUI() {
        val events = EventNode(this@Crafting, this)
        Bukkit.getPluginManager().registerEvents(events, instance)
        openInventory(this@Crafting.inventory)
    }
}