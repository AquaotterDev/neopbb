package dev.rosalyn.neopbb.type

import me.honkling.commando.common.node.Node
import me.honkling.commando.common.platform.User
import me.honkling.commando.common.type.Type
import dev.rosalyn.neopbb.config.PrisonsToml.Prison
import dev.rosalyn.neopbb.config.prisonsToml

object PrisonType : Type<Prison>() {
    override fun parse(
        user: User<*>,
        node: Node<*>,
        input: String,
        autoCompleting: Boolean
    ): Result<Pair<Prison, String>> {
        val prison = prisonsToml.prisons
            .find { input.startsWith(it.name, true) }

        if (prison == null)
            return Result.failure(IllegalArgumentException("Expected a prison"))

        val rest = input(input, prison.name.count { it == ' ' } + 1, true)
        return Result.success(prison to rest)
    }

    override fun suggest(
        user: User<*>,
        node: Node<*>,
        input: String
    ): List<String> {
        return prisonsToml.prisons.map(Prison::name)
            .filter { it.contains(input, true) }
    }
}