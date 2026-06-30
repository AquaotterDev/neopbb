package dev.rosalyn.neopbb.profile

import dev.rosalyn.neopbb.lib.mm
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

fun Player.purchase(cost: Float, alreadyHasPurchased: Boolean = false, block: () -> Unit) {
    if (alreadyHasPurchased)
        return sendMessage("<p>You cannot buy that again.".mm)

    if (money < cost)
        return sendMessage("<p>You don't have enough money to purchase that.".mm)

    money -= cost
    block()
}

fun Player.purchaseItem(cost: Float, itemStack: ItemStack) = purchase(cost) {
    inventory.addItem(itemStack)
}