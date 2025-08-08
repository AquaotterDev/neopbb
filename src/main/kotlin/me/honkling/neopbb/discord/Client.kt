package me.honkling.neopbb.discord

import dev.kord.core.Kord
import dev.kord.core.entity.channel.TextChannel
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import me.honkling.neopbb.config.configToml
import me.honkling.neopbb.instance

internal lateinit var kord: Kord; private set
internal lateinit var channel: TextChannel; private set

suspend fun initializeKord() {
    kord = Kord(configToml.discord.token)
    channel = kord.getChannelOf<TextChannel>(configToml.discord.channelId)
        ?: return instance.logger.severe("Kord couldn't find the chat channel. Is the channel ID set?")

    initializeEvents()

    kord.login {
        @OptIn(PrivilegedIntent::class)
        intents += Intent.MessageContent
    }
}