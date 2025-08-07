@file:Command("balance", "bal")

package me.honkling.neopbb.command

import me.honkling.commando.spigot.command.Command
import me.honkling.neopbb.lib.formatCurrency
import me.honkling.neopbb.lib.mm
import me.honkling.neopbb.profile.money
import org.bukkit.entity.Player

private fun balance(sender: Player, target: Player = sender) {
    if (target == sender)
        return sender.sendMessage("<p>You have <s>${formatCurrency(sender.money)}</s>.".mm)

    sender.sendMessage("<p><s>${target.name}</s> has <s>${formatCurrency(target.money)}</s>.".mm)
}