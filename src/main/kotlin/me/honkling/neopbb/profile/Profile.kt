package me.honkling.neopbb.profile

import me.honkling.neopbb.currentPrison
import me.honkling.neopbb.lib.mm
import me.honkling.neopbb.profile.key.NonPersistentKey
import me.honkling.neopbb.profile.key.createKey
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.TitlePart
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import kotlin.reflect.jvm.isAccessible
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

var Player.role by createKey(Role.Prisoner, persistent = false)
var Player.invite by createKey<Invite?>(false)
var Player.money by createKey(0.0f)

var Player.solitaryTask by createKey<Int?>(persistent = false)
var Player.handcuffTask by createKey<Int?>(false)
var Player.respawnTask by createKey<Int?>(false)

val Player.inSolitary get() = solitaryTask != null
val Player.isRespawning get() = respawnTask != null

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

fun Player.forceRespawn() {
    gameMode = GameMode.ADVENTURE

    if (this == warden)
        role = Role.Prisoner

    sendTitlePart(TitlePart.TITLE, Component.empty())
    sendTitlePart(TitlePart.SUBTITLE, Component.empty())
    prepare(true)
    teleport(
        if (inSolitary) currentPrison.solitary
        else currentPrison.respawn
    )
    respawnTask?.let { Bukkit.getScheduler().cancelTask(it) }
    respawnTask = null
}

fun Player.cleanUp() {
    val nonPersistentFields = listOf(
        Player::role,
        Player::invite,
        Player::handcuffTask,
        Player::respawnTask
    )

    for (field in nonPersistentFields) {
        field.isAccessible = true
        val key = field.getDelegate(this) as NonPersistentKey<*>
        key.cleanUp(this)
    }
}