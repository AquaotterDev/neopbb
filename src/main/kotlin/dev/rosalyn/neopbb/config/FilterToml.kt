package dev.rosalyn.neopbb.config

import cc.ekblad.toml.decode
import cc.ekblad.toml.model.TomlValue
import cc.ekblad.toml.tomlMapper
import io.papermc.paper.event.player.AsyncChatEvent
import dev.rosalyn.neopbb.config.lib.Either
import dev.rosalyn.neopbb.config.lib.either
import dev.rosalyn.neopbb.config.lib.use
import dev.rosalyn.neopbb.config.lib.value
import dev.rosalyn.neopbb.instance
import dev.rosalyn.neopbb.lib.mm
import me.honkling.ruby.config.punishmentsToml
import me.honkling.ruby.punishment.calculateDuration
import me.honkling.ruby.punishment.issuePunishment
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit

lateinit var filterToml: FilterToml; private set

data class FilterToml(
    val rules: List<Rule>
) {
    data class Rule(
        val name: String,
        val input: InputType,
        val matcher: Either<String, Regex>,
        val action: Action
    ) {
        enum class InputType {
            Plain,
            Processed
        }

        interface Action {
            fun act(event: AsyncChatEvent, rule: Rule)
        }

        data class BlockMessage(
            val message: String
        ) : Action {
            override fun act(event: AsyncChatEvent, rule: Rule) {
                event.player.sendMessage("<p>$message".mm)
            }
        }

        data class IssuePunishment(
            val id: String
        ) : Action {
            override fun act(event: AsyncChatEvent, rule: Rule) {
                val reason = requireNotNull(punishmentsToml.reasons[id]) { "Failed to find punishment reason '$id'" }
                val message = PlainTextComponentSerializer.plainText().serialize(event.message())
                issuePunishment(
                    Bukkit.getConsoleSender(),
                    event.player,
                    reason,
                    calculateDuration(event.player, reason),
                    "Violated chat filter ${rule.name}:\n$message"
                )
            }
        }

        fun test(input: String): Boolean {
            val input = (if (this.input == InputType.Plain) input else processInput(input)).lowercase()

            return when (matcher.value) {
                is String -> {
                    val matcher = if (this.input == InputType.Plain) matcher.value else processInput(matcher.value)
                    matcher.lowercase() in input
                }
                is Regex -> matcher.value.containsMatchIn(input)
                else -> false
            }
        }

        fun processInput(input: String): String {
            val builder = StringBuilder()
            var last: Char? = null
            var index = 0

            while (index < input.length) {
                if (last == input[index] || input[index].isWhitespace()) {
                    index++
                    continue
                }

                last = input[index]
                builder.append(last)
                index++
            }

            return builder.toString()
        }
    }
}

fun reloadFilterToml() {
    val file = instance.dataFolder.resolve("filter.toml")
    val mapper = tomlMapper {
        use(either<String, Regex>())
        use(FilterToml.Rule.Action::class to { type, value ->
            if (value !is TomlValue.Map || "type" !in value.properties)
                value
            else if (value.properties["type"]!!.value<String>() == "BlockMessage")
                FilterToml.Rule.BlockMessage(value.properties["message"]?.value() ?: "Your message violated the chat filter.")
            else if (value.properties["type"]!!.value<String>() == "IssuePunishment")
                FilterToml.Rule.IssuePunishment(value.properties["punishment"]!!.value())
            else value
        })
    }

    if (!file.exists()) {
        instance.dataFolder.mkdirs()
        instance.saveResource(file.name, true)
    }

    filterToml = mapper.decode(file.toPath())
}