@file:Listener

package dev.rosalyn.neopbb.event

import me.honkling.commando.spigot.event.Listener
import dev.rosalyn.neopbb.instance
import dev.rosalyn.neopbb.lib.cloak
import dev.rosalyn.neopbb.lib.compareWithoutDurability
import dev.rosalyn.neopbb.lib.mm
import dev.rosalyn.neopbb.profile.role
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.potion.PotionEffectType

private fun onBreak(event: BlockBreakEvent) {
    val player = event.player
    val itemStack = player.inventory.itemInMainHand

    if (itemStack.type != Material.IRON_PICKAXE || event.block.type != Material.IRON_BARS)
        return

    if (event.player.inventory.chestplate?.compareWithoutDurability(cloak) != true) {
        val guards = Audience.audience(Bukkit.getOnlinePlayers().filter { it.role.isAuthority })
        player.addPotionEffect(PotionEffectType.GLOWING.createEffect(20 * 30, 0))
        player.sendMessage("<p>You were caught breaking bars, get a cloak next time!".mm)
        guards.sendMessage("<p><s>${player.name}</s> was caught breaking bars!".mm)

        val sound = Sound.sound {
            it.type(Key.key("minecraft:entity.silverfish.death"))
        }

        player.playSound(sound)
        guards.playSound(sound)
    }

    event.isCancelled = false
    event.isDropItems = false
    Bukkit.getScheduler().scheduleSyncDelayedTask(instance, {
        event.block.type = Material.IRON_BARS
    }, 20L * 30)
}