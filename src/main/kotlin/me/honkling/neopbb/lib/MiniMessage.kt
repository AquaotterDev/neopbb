package me.honkling.neopbb.lib

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags

val secondaryColor = TextColor.color(27, 217, 106)

val miniMessage = MiniMessage.builder()
    .tags(TagResolver.resolver(
        StandardTags.defaults(),
        Placeholder.parsed("p", "<s>neopbb <gray>»</s> "),
        TagResolver.resolver("s", Tag.styling(secondaryColor))
    )).build()

val String.mm: Component
    get() = miniMessage.deserialize(this)
        .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)