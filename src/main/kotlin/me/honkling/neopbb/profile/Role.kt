package me.honkling.neopbb.profile

import me.honkling.neopbb.instance
import me.honkling.neopbb.lib.builder
import me.honkling.neopbb.lib.illegalGoldenApple
import me.honkling.neopbb.lib.mm
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.title.TitlePart
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffectType

data class Invite(val player: Player, val role: Role) {
    var taskID: Int? = null

    fun schedule(): Invite {
        taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(instance, {
            taskID = null
            expire()
        }, 30 * 20L)

        return this
    }

    fun cancel() {
        taskID?.let { Bukkit.getScheduler().cancelTask(it) }
    }

    fun expire() {
        cancel()
        player.sendMessage("<p>Your invitation to become a ${role.name.lowercase()} has expired.".mm)
        player.invite = null
    }
}

val keycard = ItemStack(Material.TRIPWIRE_HOOK)
    .builder()
    .displayName("Keycard <red>[CONTRABAND]".mm)
    .build()

val handcuffs = ItemStack(Material.IRON_SHOVEL)
    .builder()
    .displayName("Handcuffs <red>[CONTRABAND]".mm)
    .enchant(Enchantment.KNOCKBACK)
    .build()

enum class Role(
    val isAuthority: Boolean,
    val prepare: Player.(Boolean) -> Unit = {}
) {
    Warden(true, {
        for (player in Bukkit.getOnlinePlayers()) {
            println("Warden: $warden ($player/${player.role})")
            if (player != warden && player.role.isAuthority) {
                player.role = Prisoner
                player.prepare(true)
            }

            player.invite?.expire()
        }

        val server = Bukkit.getServer()

        world.getEntitiesByClass(Item::class.java).forEach(Entity::remove)
        server.sendTitlePart(TitlePart.TITLE, "<s>$name</s> is the new warden!".mm)
        server.playSound(Sound.sound {
            it.type(Key.key("minecraft:block.end_portal.spawn"))
        })

        swatUnlocked = true

        val sword = ItemStack(Material.DIAMOND_SWORD)
            .builder()
            .enchant(Enchantment.SHARPNESS, 2)
            .enchant(Enchantment.UNBREAKING, 2)
            .build()

        inventory.setItem(EquipmentSlot.HEAD, ItemStack(Material.CHAINMAIL_HELMET))
        inventory.setItem(EquipmentSlot.CHEST, ItemStack(Material.IRON_CHESTPLATE))
        inventory.setItem(EquipmentSlot.LEGS, ItemStack(Material.IRON_LEGGINGS))
        inventory.setItem(EquipmentSlot.FEET, ItemStack(Material.NETHERITE_BOOTS))
        inventory.addItem(
            sword,
            handcuffs,
            ItemStack(Material.BOW),
            ItemStack(Material.ARROW, 64),
            ItemStack(Material.COOKED_BEEF, 64),
            keycard,
        )
    }),
    Guard(true, {
        val helmet = ItemStack(Material.IRON_HELMET)
        val chestplate = ItemStack(Material.LEATHER_CHESTPLATE)
            .builder()
            .enchant(Enchantment.PROTECTION)
            .color(126, 135, 245)
            .build()

        val leggings = ItemStack(Material.LEATHER_LEGGINGS)
            .builder()
            .enchant(Enchantment.PROTECTION)
            .color(126, 135, 245)
            .build()

        val boots = ItemStack(Material.LEATHER_BOOTS)
            .builder()
            .enchant(Enchantment.PROTECTION)
            .color(126, 135, 245)
            .build()

        val sword = ItemStack(Material.IRON_SWORD)
            .builder()
            .enchant(Enchantment.SHARPNESS)
            .enchant(Enchantment.UNBREAKING)
            .build()

        inventory.setItem(EquipmentSlot.HEAD, helmet)
        inventory.setItem(EquipmentSlot.CHEST, chestplate)
        inventory.setItem(EquipmentSlot.LEGS, leggings)
        inventory.setItem(EquipmentSlot.FEET, boots)
        inventory.addItem(
            sword,
            handcuffs,
            ItemStack(Material.CROSSBOW),
            ItemStack(Material.ARROW, 16),
            ItemStack(Material.COOKED_BEEF, 32),
            keycard
        )
    }),
    Nurse(true, {
        val helmet = ItemStack(Material.CHAINMAIL_HELMET)
        val chestplate = ItemStack(Material.LEATHER_CHESTPLATE)
            .builder()
            .enchant(Enchantment.PROTECTION)
            .color(Color.PURPLE)
            .build()

        val leggings = ItemStack(Material.LEATHER_LEGGINGS)
            .builder()
            .enchant(Enchantment.PROTECTION)
            .color(Color.PURPLE)
            .build()

        val boots = ItemStack(Material.LEATHER_BOOTS)
            .builder()
            .enchant(Enchantment.PROTECTION)
            .color(Color.PURPLE)
            .build()

        val sword = ItemStack(Material.STONE_SWORD)
            .builder()
            .enchant(Enchantment.SHARPNESS)
            .enchant(Enchantment.UNBREAKING)
            .build()

        val potion = ItemStack(Material.SPLASH_POTION)
        potion.editMeta(PotionMeta::class.java) {
            it.addCustomEffect(PotionEffectType.INSTANT_HEALTH.createEffect(10, 2), true)
        }

        inventory.setItem(EquipmentSlot.HEAD, helmet)
        inventory.setItem(EquipmentSlot.CHEST, chestplate)
        inventory.setItem(EquipmentSlot.LEGS, leggings)
        inventory.setItem(EquipmentSlot.FEET, boots)
        inventory.addItem(
            sword,
            handcuffs,
            ItemStack(Material.CROSSBOW),
            ItemStack(Material.ARROW, 16),
            ItemStack(Material.COOKED_BEEF, 32),
            keycard,
            potion
        )
    }),
    Swat(true, {
        val helmet = ItemStack(Material.NETHERITE_HELMET)
        val chestplate = ItemStack(Material.NETHERITE_CHESTPLATE)
        chestplate.addEnchantment(Enchantment.PROTECTION, 1)
        val leggings = ItemStack(Material.NETHERITE_LEGGINGS)
        val boots = ItemStack(Material.LEATHER_BOOTS)
            .builder()
            .enchant(Enchantment.PROTECTION)
            .color(Color.GRAY)
            .build()

        val sword = ItemStack(Material.DIAMOND_SWORD)
        inventory.setItem(EquipmentSlot.HEAD, helmet)
        inventory.setItem(EquipmentSlot.CHEST, chestplate)
        inventory.setItem(EquipmentSlot.LEGS, leggings)
        inventory.setItem(EquipmentSlot.FEET, boots)
        inventory.addItem(
            sword,
            handcuffs,
            ItemStack(Material.BOW),
            ItemStack(Material.ARROW, 16),
            ItemStack(Material.COOKED_BEEF, 32),
            keycard
        )
    }),
    Criminal(false, {
        val name = "Armor <red>[CONTRABAND]".mm
        val helmet = ItemStack(Material.CHAINMAIL_HELMET)
            .builder()
            .displayName(name)
            .build()

        val chestplate = ItemStack(Material.LEATHER_CHESTPLATE)
            .builder()
            .displayName(name)
            .enchant(Enchantment.PROTECTION)
            .color(Color.RED)
            .build()

        val leggings = ItemStack(Material.CHAINMAIL_LEGGINGS)
            .builder()
            .displayName(name)
            .enchant(Enchantment.PROTECTION)
            .build()

        val boots = ItemStack(Material.CHAINMAIL_BOOTS)
            .builder()
            .displayName(name)
            .build()

        val sword = ItemStack(Material.STONE_SWORD)
            .builder()
            .enchant(Enchantment.SHARPNESS, 2)
            .enchant(Enchantment.UNBREAKING)
            .build()

        inventory.setItem(EquipmentSlot.HEAD, helmet)
        inventory.setItem(EquipmentSlot.CHEST, chestplate)
        inventory.setItem(EquipmentSlot.LEGS, leggings)
        inventory.setItem(EquipmentSlot.FEET, boots)
        inventory.addItem(sword)
        inventory.addItem(illegalGoldenApple.asQuantity(4))
    }),
    Prisoner(false, {
        val chestplate = ItemStack(Material.LEATHER_CHESTPLATE)
            .builder()
            .displayName("Prisoner Uniform")
            .color(208, 133, 22)
            .build()

        val leggings = ItemStack(Material.LEATHER_LEGGINGS)
            .builder()
            .displayName("Prisoner Uniform")
            .color(208, 133, 22)
            .build()

        val boots = ItemStack(Material.LEATHER_BOOTS)
            .builder()
            .displayName("Prisoner Uniform")
            .color(40, 20, 2)
            .build()

        inventory.setItem(EquipmentSlot.CHEST, chestplate)
        inventory.setItem(EquipmentSlot.LEGS, leggings)
        inventory.setItem(EquipmentSlot.FEET, boots)
    })
}