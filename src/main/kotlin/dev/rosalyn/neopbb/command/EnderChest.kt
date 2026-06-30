@file:Command("enderchest", permission = "neopbb.enderchest")

package dev.rosalyn.neopbb.command

import kotlinx.coroutines.launch
import me.honkling.commando.spigot.command.Command
import dev.rosalyn.neopbb.instance
import dev.rosalyn.neopbb.lib.mm
import dev.rosalyn.neopbb.profile.offline.DataAccess
import dev.rosalyn.neopbb.profile.offline.OfflineEnderChest
import dev.rosalyn.neopbb.scope
import net.minecraft.world.ItemStackWithSlot
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.inventory.CraftInventory
import org.bukkit.entity.Player

private fun inspect(sender: Player, name: String) {
    scope.launch {
        val player = Bukkit.getOfflinePlayer(name)
        val enderChest = OfflineEnderChest(player)

        Bukkit.getScheduler().runTask(instance, Runnable {
            sender.openInventory(CraftInventory(enderChest))
        })
    }
}

private fun clear(sender: Player, name: String) {
    scope.launch {
        val player = Bukkit.getOfflinePlayer(name)
        val dataAccess = DataAccess(player)

        dataAccess.valueOutput.list("EnderItems", ItemStackWithSlot.CODEC)
        dataAccess.save()
        sender.sendMessage("<p>Cleared <s>${player.name}</s>'s ender chest.".mm)
    }
}