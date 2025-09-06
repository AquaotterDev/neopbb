@file:Listener

package me.honkling.neopbb.event

import me.honkling.commando.spigot.event.Listener
import me.honkling.neopbb.lib.*
import me.honkling.neopbb.profile.purchaseItem
import org.bukkit.block.Sign
import org.bukkit.event.player.PlayerInteractEvent

private fun onInteract(event: PlayerInteractEvent) {
    val player = event.player
    val state = event.clickedBlock?.state as? Sign
        ?: return
    val lines = getAllSignLines(state)

    for (line in lines)
    when (line) {
        "Soup" -> return player.purchaseItem(2f, soup)
        "Steak" -> return player.purchaseItem(5f, steak)
        "Milk" -> return player.purchaseItem(5f, milk)
        "Nausea Potion" -> return player.purchaseItem(30f, nauseaPotion)
        "Honey Bottle" -> return player.purchaseItem(30f, honeyBottle)
        "Legal Healing" -> return player.purchaseItem(70f, legalGoldenApple)
    }
}