@file:Listener

package me.honkling.neopbb.event

import me.honkling.commando.spigot.event.Listener
import me.honkling.neopbb.lib.honeyBottle
import me.honkling.neopbb.lib.legalGoldenApple
import me.honkling.neopbb.lib.milk
import me.honkling.neopbb.lib.nauseaPotion
import me.honkling.neopbb.lib.soup
import me.honkling.neopbb.lib.steak
import me.honkling.neopbb.profile.purchaseItem
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.block.Sign
import org.bukkit.block.sign.Side
import org.bukkit.event.player.PlayerInteractEvent

private fun onInteract(event: PlayerInteractEvent) {
    val player = event.player
    val state = event.clickedBlock?.state as? Sign
        ?: return

    val side = state.getSide(Side.FRONT)
    val line = PlainTextComponentSerializer.plainText().serialize(side.line(2))

    when (line) {
        "Soup" -> player.purchaseItem(2f, soup)
        "Steak" -> player.purchaseItem(5f, steak)
        "Milk" -> player.purchaseItem(5f, milk)
        "Nausea Potion" -> player.purchaseItem(30f, nauseaPotion)
        "Honey Bottle" -> player.purchaseItem(30f, honeyBottle)
        "Legal Healing" -> player.purchaseItem(70f, legalGoldenApple)
    }
}