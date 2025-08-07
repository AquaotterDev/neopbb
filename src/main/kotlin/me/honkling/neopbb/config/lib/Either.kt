package me.honkling.neopbb.config.lib

import cc.ekblad.toml.model.TomlValue
import kotlin.reflect.KType

fun <A : Any, B : Any> either(): Decoder = Either::class to { type: KType, it: TomlValue ->
    Either<A, B>(it.value())
}

@JvmInline
value class Either<A : Any, B : Any>(val value: Any)