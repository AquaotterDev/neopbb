@file:Listener

package me.honkling.neopbb.event

import me.honkling.commando.spigot.event.Listener
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.inventory.ItemStack

private val blacklistedMaterials = listOf(
    Material.BOWL,
    Material.TRIPWIRE_HOOK,
    Material.WOODEN_AXE,
    Material.WOODEN_SWORD,
    Material.CARROT_ON_A_STICK,
    Material.IRON_DOOR,
    Material.STONE_BUTTON,
    Material.GLASS_BOTTLE,
    Material.IRON_SHOVEL,
    Material.BUCKET
)

private val blacklistedPredicates = listOf<(ItemStack) -> Boolean>(
    { "Prisoner Uniform" in PlainTextComponentSerializer.plainText().serialize(it.displayName()) },
    { it.enchantments.containsKey(Enchantment.VANISHING_CURSE) }
)

private fun onDrop(event: PlayerDropItemEvent) {
    val itemStack = event.itemDrop.itemStack

    if (blacklistedPredicates.any { it(itemStack) } || itemStack.type in blacklistedMaterials)
        event.itemDrop.itemStack = ItemStack(Material.AIR)
}

private fun onDeath(event: PlayerDeathEvent) {
    event.drops.removeIf { itemStack ->
        blacklistedPredicates.any { it(itemStack) } || itemStack.type in blacklistedMaterials
    }
}