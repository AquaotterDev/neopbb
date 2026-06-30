package dev.rosalyn.neopbb.discord

import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.rosalyn.neopbb.config.configToml
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit

internal fun initializeEvents() {
    kord.on<MessageCreateEvent> {
        val author = message.author

        if (author?.isBot != false || message.channelId != configToml.discord.channelId)
            return@on

        val name = author.globalName ?: author.username
        val message = message.content.replace("\n", " ")
            .ifEmpty { "(Image)" }

        Bukkit.getServer().sendMessage(Component.text("$name: ")
            .color(TextColor.color(0xB3B9FC))
            .append(Component.text(message)
                .color(TextColor.color(0xD6DAFF))))
    }
}