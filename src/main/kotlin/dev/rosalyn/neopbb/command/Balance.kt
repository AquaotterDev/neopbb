@file:Command("balance", "bal")

package dev.rosalyn.neopbb.command

import me.honkling.commando.spigot.command.Command
import dev.rosalyn.neopbb.lib.formatCurrency
import dev.rosalyn.neopbb.lib.mm
import dev.rosalyn.neopbb.profile.money
import org.bukkit.entity.Player

private fun balance(sender: Player, target: Player = sender) {
    if (target == sender)
        return sender.sendMessage("<p>You have <s>${formatCurrency(sender.money)}</s>.".mm)

    sender.sendMessage("<p><s>${target.name}</s> has <s>${formatCurrency(target.money)}</s>.".mm)
}