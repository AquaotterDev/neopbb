@file:Listener

package me.honkling.neopbb.event

import me.honkling.commando.spigot.event.Listener
import me.honkling.neopbb.instance
import me.honkling.neopbb.lib.mm
import me.honkling.neopbb.profile.handcuffTask
import me.honkling.neopbb.profile.handcuffs
import me.honkling.neopbb.profile.role
import net.kyori.adventure.title.TitlePart
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDismountEvent
import org.bukkit.event.entity.EntityMountEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.potion.PotionEffectType

private fun onDeath(event: PlayerDeathEvent) {
    val victim = event.player
    val attacker = event.damageSource.causingEntity as? Player
        ?: return

    if (!attacker.role.isAuthority || attacker.passengers.isNotEmpty() || attacker.vehicle is Player ||
        victim.passengers.isNotEmpty() || victim.vehicle is Player)
        return

    val mainItem = attacker.inventory.getItem(EquipmentSlot.HAND)
    val offHandItem = attacker.inventory.getItem(EquipmentSlot.OFF_HAND)

    if (mainItem.type != Material.IRON_SHOVEL && offHandItem.type != Material.IRON_SHOVEL)
        return

    event.isCancelled = true
    victim.health = victim.getAttribute(Attribute.MAX_HEALTH)!!.value
    attacker.addPassenger(victim)
    victim.handcuffTask = Bukkit.getScheduler().scheduleSyncDelayedTask(instance, {
        victim.handcuffTask = null
        attacker.removePassenger(victim)
    }, 20L * 20)
}

private fun onEnterVehicle(event: EntityMountEvent) {
    val player = event.entity as? Player
        ?: return

    if (player.handcuffTask != null)
        event.isCancelled = true
}

private fun onShift(event: PlayerToggleSneakEvent) {
    val player = event.player

    if (!event.isSneaking || player.passengers.isEmpty())
        return

    for (passenger in player.passengers) {
        val isPlayer = passenger is Player

        if (isPlayer) {
            passenger.handcuffTask?.let { Bukkit.getScheduler().cancelTask(it) }
            passenger.handcuffTask = null
        }

        player.removePassenger(passenger)

        if (isPlayer && (player.velocity.y != 0.0 || player.isSprinting)) {
            player.sendMessage("<p>You threw <s>${passenger.name}</s>!".mm)
            passenger.sendTitlePart(TitlePart.TITLE, "<s>Thrown!".mm)
            passenger.teleport(player)
            passenger.noDamageTicks = 20 * 3
            passenger.damage(0.0)
            passenger.health = 3.0
            passenger.addPotionEffect(PotionEffectType.SLOWNESS.createEffect(20 * 15, 2))
            Bukkit.getScheduler().scheduleSyncDelayedTask(instance, {
                passenger.velocity = player.location.direction.multiply(1.25).setY(0)
            }, 4)
        }
    }
}

private fun onDismount(event: EntityDismountEvent) {
    val player = event.entity as? Player
        ?: return

    val isDisconnected = (player as CraftPlayer).handle.connection.processedDisconnect
    if (!isDisconnected && player.handcuffTask != null)
        event.isCancelled = true
}

private fun onDamage(event: EntityDamageEvent) {
    val player = event.entity as? Player
        ?: return

    if (player.vehicle is Player)
        event.isCancelled = true
}