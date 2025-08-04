package me.honkling.neopbb.lib

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffectType

val paper = ItemStack(Material.PAPER)
val soup = ItemStack(Material.MUSHROOM_STEW)
val coal = ItemStack(Material.COAL)
val steak = ItemStack(Material.COOKED_BEEF)
val milk = ItemStack(Material.MILK_BUCKET)
val honeyBottle = ItemStack(Material.HONEY_BOTTLE)
val nauseaPotion = ItemStack(Material.SPLASH_POTION)
    .builder()
    .displayName("Nausea Potion".mm)
    .potionEffect(PotionEffectType.NAUSEA, 20 * 10, 0)
    .color(Color.LIME)
    .build()

val pebble = ItemStack(Material.STONE_BUTTON)
    .builder()
    .displayName("Pebble".mm)
    .build()

val rock = ItemStack(Material.COBBLESTONE)
    .builder()
    .displayName("Rock".mm)
    .build()

val legalGoldenApple = ItemStack(Material.GOLDEN_APPLE)
val illegalGoldenApple = ItemStack(Material.GOLDEN_APPLE)
    .builder()
    .displayName("Golden Apple <red>[CONTRABAND]".mm)
    .build()

val dagger = ItemStack(Material.IRON_SWORD)
    .builder()
    .displayName("Dagger <red>[CONTRABAND]".mm)
    .enchant(Enchantment.SHARPNESS, 2)
    .build()

val wireCutters = ItemStack(Material.IRON_PICKAXE)
    .builder()
    .displayName("Wire Cutters <red>[CONTRABAND]".mm)
    .canDestroy(Material.IRON_BARS)
    .enchant(Enchantment.EFFICIENCY, 5)
    .damage(245)
    .build()

val supremeStick = ItemStack(Material.STICK)
    .builder()
    .displayName("Supreme Stick".mm)
    .build()

val scrapMetal = ItemStack(Material.RAW_IRON)
    .builder()
    .displayName("Scrap Metal <red>[CONTRABAND]".mm)
    .build()

val cloak = ItemStack(Material.LEATHER_CHESTPLATE)
    .builder()
    .displayName("Cloak <red>[CONTRABAND]".mm)
    .color(Color.BLACK)
    .build()

class ItemStackBuilder(private val itemStack: ItemStack) {
    private val meta = itemStack.itemMeta

    fun displayName(displayName: String) = displayName(Component.text(displayName)
        .decoration(TextDecoration.ITALIC, false))

    fun displayName(displayName: Component): ItemStackBuilder {
        meta.displayName(displayName)
        return this
    }

    fun enchant(enchantment: Enchantment) = enchant(enchantment, 1)
    fun enchant(enchantment: Enchantment, level: Int): ItemStackBuilder {
        meta.addEnchant(enchantment, level, true)
        return this
    }

    fun potionEffect(type: PotionEffectType, duration: Int, amplifier: Int): ItemStackBuilder {
        (meta as? PotionMeta)?.addCustomEffect(type.createEffect(duration, amplifier), true)
        return this
    }

    fun color(r: Int, g: Int, b: Int) = color(Color.fromRGB(r, g, b))
    fun color(color: Color): ItemStackBuilder {
        (meta as? LeatherArmorMeta)?.setColor(color)
        (meta as? PotionMeta)?.color = color
        return this
    }

    fun lore(vararg lines: String) = lore(*lines.map { Component.text(it) }.toTypedArray())
    fun lore(vararg lines: Component): ItemStackBuilder {
        meta.lore(lines.toMutableList())
        return this
    }

    fun damage(damage: Int): ItemStackBuilder {
        (meta as? Damageable)?.damage = damage
        return this
    }

    fun canDestroy(vararg materials: Material): ItemStackBuilder {
        @Suppress("removal", "DEPRECATION")
        meta.canDestroy = materials.toMutableSet()
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