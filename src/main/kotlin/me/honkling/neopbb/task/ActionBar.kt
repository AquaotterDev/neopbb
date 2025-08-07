package me.honkling.neopbb.task

import me.honkling.neopbb.lib.formatCurrency
import me.honkling.neopbb.lib.mm
import me.honkling.neopbb.profile.money
import me.honkling.neopbb.profile.warden
import me.honkling.neopbb.profile.wardenCooldown
import org.bukkit.Bukkit
import kotlin.math.ceil

internal fun executeActionBar() {
    if (wardenCooldown > 0)
        wardenCooldown--

    for (player in Bukkit.getOnlinePlayers()) {
        val wardenStatus = if (warden != null && warden != player) "Current warden: <s>${warden!!.name}"
            else if (warden != null) "Current warden: <s>You! Say \"/warden help\"</s>"
            else if (wardenCooldown <= 0) "Say <s>/warden</s> to become warden"
            else {
                val cooldown = ceil(wardenCooldown / 20.0).toInt()
                val unit = if (cooldown == 1) "second" else "seconds"
                "Please wait <s>$cooldown $unit</s>"
            }

        player.sendActionBar("$wardenStatus <gray>|</gray> <s>${formatCurrency(player.money)}".mm)
    }
}