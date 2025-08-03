package me.honkling.neopbb.profile.key

import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import java.lang.IllegalArgumentException

class EnumDataType<T : Enum<T>>(private val clazz: Class<T>) : PersistentDataType<String, T> {
    override fun getPrimitiveType(): Class<String> {
        return String::class.java
    }

    override fun getComplexType(): Class<T> {
        return clazz
    }

    override fun toPrimitive(complex: T, context: PersistentDataAdapterContext): String {
        return complex.name
    }

    override fun fromPrimitive(
        primitive: String,
        context: PersistentDataAdapterContext
    ): T {
        return clazz.enumConstants.find { it.name == primitive }
            ?: throw IllegalArgumentException("Couldn't find enum value '$primitive'. Was the entry removed?")
    }
}