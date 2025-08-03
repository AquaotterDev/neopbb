@file:Listener

package me.honkling.neopbb.event

import me.honkling.commando.spigot.event.Listener
import me.honkling.neopbb.gui.SwitchMaps
import me.honkling.neopbb.lib.mm
import me.honkling.neopbb.profile.purchase
import me.honkling.neopbb.profile.swatUnlocked
import me.honkling.neopbb.profile.warden
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.block.Sign
import org.bukkit.block.sign.Side
import org.bukkit.event.player.PlayerInteractEvent

private fun onInteract(event: PlayerInteractEvent) {
    val player = event.player
    val state = event.clickedBlock?.state as? Sign
        ?: return

    val side = state.getSide(Side.FRONT)
    val line = PlainTextComponentSerializer.plainText().serialize(side.line(1))

    when (line) {
        "SWAT Guards" -> player.purchase(2500f, swatUnlocked) {
            Bukkit.getServer().sendMessage("<p><s>${player.name}</s> has unlocked SWAT guards!".mm)
            swatUnlocked = true
        }
        "Switch Maps" -> {
            if (player != warden)
                return player.sendMessage("<p>Only the warden can switch maps.".mm)

            val gui = SwitchMaps()

            with (gui) {
                player.openGUI()
            }
        }
    }
}