package me.honkling.neopbb.task

import me.honkling.neopbb.lib.formatCurrency
import me.honkling.neopbb.lib.mm
import me.honkling.neopbb.profile.money
import me.honkling.neopbb.profile.warden
import me.honkling.neopbb.profile.wardenCooldown
import org.bukkit.Bukkit

internal fun executeActionBar() {
    if (wardenCooldown > 0)
        wardenCooldown--

    val wardenStatus = if (warden != null) "Current warden: <s>${warden!!.name}"
        else if (wardenCooldown <= 0) "Say <s>/warden</s> to become warden"
        else "Please wait <s>${wardenCooldown / 20} seconds</s>"

    for (player in Bukkit.getOnlinePlayers())
        player.sendActionBar("$wardenStatus <gray>|</gray> <s>${formatCurrency(player.money)}".mm)
}