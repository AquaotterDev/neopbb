package me.honkling.neopbb.lib

import net.kyori.adventure.text.Component
import org.bukkit.Color
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta

class ItemStackBuilder(private val itemStack: ItemStack) {
    private val meta = itemStack.itemMeta

    fun displayName(displayName: String) = displayName(Component.text(displayName))
    fun displayName(displayName: Component): ItemStackBuilder {
        meta.displayName(displayName)
        return this
    }

    fun enchant(enchantment: Enchantment) = enchant(enchantment, 1)
    fun enchant(enchantment: Enchantment, level: Int): ItemStackBuilder {
        meta.addEnchant(enchantment, level, true)
        return this
    }

    fun color(r: Int, g: Int, b: Int) = color(Color.fromRGB(r, g, b))
    fun color(color: Color): ItemStackBuilder {
        (meta as? LeatherArmorMeta)?.setColor(color)
        return this
    }

    fun build(): ItemStack {
        itemStack.itemMeta = meta
        return itemStack
    }
}

fun ItemStack.builder(): ItemStackBuilder {
    return ItemStackBuilder(this)
}