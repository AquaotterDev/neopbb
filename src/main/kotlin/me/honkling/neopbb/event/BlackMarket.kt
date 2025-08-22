@file:Listener

package me.honkling.neopbb.event

import me.honkling.commando.spigot.event.Listener
import me.honkling.neopbb.currentPrison
import me.honkling.neopbb.lib.*
import me.honkling.neopbb.profile.Role
import me.honkling.neopbb.profile.isInBlackMarket
import me.honkling.neopbb.profile.purchaseItem
import me.honkling.neopbb.profile.role
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.title.TitlePart
import org.bukkit.Material
import org.bukkit.block.Sign
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

private fun onInteract(event: PlayerInteractEvent) {
    val player = event.player
    val block = event.clickedBlock
        ?: return

    if (block.type == Material.CAULDRON) {
        if (player.role.isAuthority && player.role != Role.Warden)
            return player.sendMessage("<p>You can't go in there..!".mm)

        if (player.passengers.isNotEmpty())
            return player.sendMessage("<p>You can't go in the black market while somebody is handcuffed.".mm)

        player.isInBlackMarket = true
        player.teleport(currentPrison.blackMarketIn)
        player.sendTitlePart(TitlePart.TITLE, "<gray>-= Black Market =-".mm)
        player.playSound(Sound.sound {
            it.type(Key.key("minecraft:ambient.underwater.enter"))
            it.pitch(0.75f)
        })
        return
    }

    val state = block.state as? Sign
        ?: return
    val lines = getAllSignLines(state)

    for (line in lines)
    when (line) {
        "Leave Market" -> {
            if (player.passengers.isNotEmpty())
                return player.sendMessage("<p>You can't go out of the black market while somebody is handcuffed.".mm)

            player.isInBlackMarket = false
            player.teleport(currentPrison.blackMarketOut)
            return player.playSound(Sound.sound {
                it.type(Key.key("minecraft:entity.ender_pearl.throw"))
            })
        }
        "Dagger" -> return player.purchaseItem(400f, dagger)
        "Scrap Metal" -> return player.purchaseItem(150f, scrapMetal)
        "Supreme Stick" -> return player.purchaseItem(50f, supremeStick)
        "Illegal Healing" -> return player.purchaseItem(30f, illegalGoldenApple)
        "Coal" -> return player.purchaseItem(30f, coal)
        "Arrows" -> return player.purchaseItem(16f, ItemStack(Material.ARROW, 8))
        "Strong Chest" -> return player.purchaseItem(1000f, ItemStack(Material.IRON_CHESTPLATE))
    }
}