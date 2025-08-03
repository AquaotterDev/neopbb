package me.honkling.neopbb.profile

import me.honkling.neopbb.lib.mm
import me.honkling.neopbb.profile.key.NonPersistentKey
import me.honkling.neopbb.profile.key.createKey
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

var Player.role by createKey(Role.Prisoner, persistent = false)
var Player.invite by createKey<Invite?>(false)
var Player.money by createKey(0.0f)

fun Player.prepare(reset: Boolean) {
    if (reset) {
        inventory.clear()
        health = getAttribute(Attribute.MAX_HEALTH)!!.value
        foodLevel = 20
        invite = null
    }

    if (role.isAuthority) {
        val display = if (role == Role.Warden) "the warden" else "a ${role.name.lowercase()}"
        Bukkit.getServer().sendMessage("<p><s>$name</s> is now $display!".mm)
    }

    role.prepare(this, reset)
}

fun Player.cleanUp() {
    val nonPersistentFields = listOf(
        Player::role,
        Player::invite
    )

    for (field in nonPersistentFields) {
        val key = field.get(this) as NonPersistentKey<*>
        key.cleanUp(this)
    }
}