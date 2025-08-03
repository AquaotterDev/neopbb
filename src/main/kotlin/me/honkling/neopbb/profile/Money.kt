package me.honkling.neopbb.profile

import me.honkling.neopbb.lib.mm
import org.bukkit.entity.Player

fun Player.purchase(cost: Float, alreadyHasPurchased: Boolean = false, block: () -> Unit) {
    if (alreadyHasPurchased)
        return sendMessage("<p>You cannot buy that again.".mm)

    if (money < cost)
        return sendMessage("<p>You don't have enough money to purchase that.".mm)

    money -= cost
    block()
}