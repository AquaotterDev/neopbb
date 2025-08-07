@file:Listener

package me.honkling.neopbb.event

import me.honkling.commando.spigot.event.Listener
import me.honkling.neopbb.profile.Role
import me.honkling.neopbb.profile.role
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryAction.*
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BundleMeta

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
    Material.IRON_PICKAXE,
    Material.BUCKET
)

private val blacklistedPredicates = listOf<(Player, ItemStack) -> Boolean>(
    { _, it -> "Prisoner Uniform" in PlainTextComponentSerializer.plainText().serialize(it.displayName()) },
    { _, it -> it.enchantments.containsKey(Enchantment.VANISHING_CURSE) },
    { _, it -> it.type == Material.DIAMOND_SWORD && it.enchantments.containsKey(Enchantment.SHARPNESS) }
)

private fun onDrop(event: PlayerDropItemEvent) {
    val player = event.player
    val itemStack = event.itemDrop.itemStack

    if (isBlacklisted(player, itemStack)) {
        event.itemDrop.itemStack = ItemStack(Material.AIR)
        playNo(player)
    }
}

private fun onTransfer(event: InventoryClickEvent) {
    val player = event.whoClicked as? Player ?: return
    val currentItem = event.currentItem
    val cursorItem = event.cursor

    if (player.gameMode == GameMode.CREATIVE || player.openInventory.topInventory.type == InventoryType.CRAFTING)
        return

    // if statements instead of when for reasons (it's a long story)

    if (event.action in listOf(
        PICKUP_ALL, PICKUP_SOME, PICKUP_HALF, PICKUP_ONE, DROP_ALL_SLOT, DROP_ONE_SLOT,
        MOVE_TO_OTHER_INVENTORY, PICKUP_ALL_INTO_BUNDLE, PICKUP_SOME_INTO_BUNDLE
    )) {
        if (currentItem == null || (event.clickedInventory == player.openInventory.bottomInventory && event.action != MOVE_TO_OTHER_INVENTORY))
            return

        if (isBlacklisted(player, currentItem)) {
            event.isCancelled = true
            playNo(player)
        }
    }

    if (event.action in listOf(
        PLACE_ALL, PLACE_SOME, PLACE_ONE, DROP_ALL_CURSOR, DROP_ONE_CURSOR,
        COLLECT_TO_CURSOR, PLACE_ALL_INTO_BUNDLE, PLACE_SOME_INTO_BUNDLE
    )) {
        if (event.clickedInventory == player.inventory)
            return

        if (isBlacklisted(player, cursorItem)) {
            event.isCancelled = true
            playNo(player)
        }
    }

    @Suppress("removal")
    if (event.action in listOf(
        SWAP_WITH_CURSOR, HOTBAR_MOVE_AND_READD, HOTBAR_SWAP
    )) {
        val hotbarItem = if (event.hotbarButton != -1) player.inventory.getItem(event.hotbarButton)
            else null

        val hotbarItemBlacklisted = hotbarItem?.let { isBlacklisted(player, it) } ?: false
        val currentItemBlacklisted = currentItem?.let { isBlacklisted(player, it) } ?: false

        if (isBlacklisted(player, cursorItem) || hotbarItemBlacklisted || currentItemBlacklisted) {
            event.isCancelled = true
            playNo(player)
        }
    }

    if (event.action == PICKUP_FROM_BUNDLE) {
        val meta = currentItem?.itemMeta as? BundleMeta

        if (meta == null) {
            event.isCancelled = true
            return
        }

        if (meta.items.firstOrNull()?.let { isBlacklisted(player, it) } == true) {
            event.isCancelled = true
            playNo(player)
        }
    }

    if (event.action == PLACE_FROM_BUNDLE) {
        val meta = cursorItem.itemMeta as? BundleMeta

        if (meta == null) {
            event.isCancelled = true
            return
        }

        if (meta.items.firstOrNull()?.let { isBlacklisted(player, it) } == true) {
            event.isCancelled = true
            playNo(player)
        }
    }
}

private fun onDeath(event: PlayerDeathEvent) {
    event.drops.removeIf { isBlacklisted(event.player, it) }

    if (event.player.role == Role.Warden)
        event.drops.clear()
}

private fun isBlacklisted(player: Player, itemStack: ItemStack): Boolean {
    return itemStack.type in blacklistedMaterials || blacklistedPredicates.any { it(player, itemStack) }
}

private fun playNo(player: Player) {
    player.playSound(Sound.sound {
        it.type(Key.key("minecraft:entity.villager.no"))
    })
}