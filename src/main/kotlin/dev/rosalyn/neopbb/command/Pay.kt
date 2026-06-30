@file:Command("pay")

package dev.rosalyn.neopbb.command

import me.honkling.commando.spigot.command.Command
import dev.rosalyn.neopbb.lib.formatCurrency
import dev.rosalyn.neopbb.lib.mm
import dev.rosalyn.neopbb.profile.money
import org.bukkit.entity.Player

private fun pay(sender: Player, target: Player, amount: Float) {
    if (sender == target)
        return sender.sendMessage("<p>You can't pay yourself.".mm)

    if (amount < 100.0)
        return sender.sendMessage("<p>You must pay <s>$100</s> or more.".mm)

    if (sender.money < amount)
        return sender.sendMessage("<p>You don't have that much money.".mm)

    sender.money -= amount
    target.money += amount
    sender.sendMessage("<p>You paid <s>${formatCurrency(amount)}</s> to <s>${target.name}</s>.".mm)
    target.sendMessage("<p>You received <s>${formatCurrency(amount)}</s> from <s>${sender   .name}</s>.".mm)
}