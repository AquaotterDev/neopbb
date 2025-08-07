@file:Command("discord")

package me.honkling.neopbb.command

import me.honkling.commando.spigot.command.Command
import me.honkling.neopbb.lib.mm
import org.bukkit.command.CommandSender

private fun discord(sender: CommandSender) {
    sender.sendMessage("<p><s><u><click:open_url:https://discord.gg/A5eNE4Hs3v>Click here</s> to join our Discord server.".mm)
}