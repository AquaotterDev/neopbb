package dev.rosalyn.neopbb.profile.key

import dev.rosalyn.neopbb.instance
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataHolder
import org.bukkit.persistence.PersistentDataType
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.typeOf

typealias Setter<T> = (T) -> Unit

inline fun <reified T : Any> createKey(fallbackValue: T, persistent: Boolean = true, noinline onSet: Setter<T> = { _ -> }): Key<T> {
    if (!persistent)
        return NonPersistentKey(fallbackValue, onSet)

    val persistentType = getPersistentDataType<T>()
    return PersistentKey(persistentType, fallbackValue, onSet)
}

inline fun <reified T> createKey(persistent: Boolean = true, noinline onSet: Setter<T?> = { _ -> }): Key<T?> {
    if (!persistent)
        return NonPersistentKey(null, onSet)

    val persistentType = getPersistentDataType<T?>()
    return PersistentKey(persistentType, null, onSet)
}

interface Key<T> {
    operator fun getValue(thisRef: PersistentDataContainer, property: KProperty<*>): T
    operator fun getValue(thisRef: PersistentDataHolder, property: KProperty<*>): T
    operator fun setValue(thisRef: PersistentDataContainer, property: KProperty<*>, value: T)
    operator fun setValue(thisRef: PersistentDataHolder, property: KProperty<*>, value: T)
}

class PersistentKey<T>(
    val type: PersistentDataType<*, T>,
    val fallbackValue: T?,
    val onSet: Setter<T>
) : Key<T> {
    lateinit var key: NamespacedKey; private set

    @Suppress("UNCHECKED_CAST")
    override operator fun getValue(thisRef: PersistentDataContainer, property: KProperty<*>): T {
        if (!::key.isInitialized)
            key = NamespacedKey(instance, property.name)

        return thisRef.get(key, type as PersistentDataType<*, *>) as T?
            ?: fallbackValue
            ?: null as T
    }

    override operator fun setValue(thisRef: PersistentDataContainer, property: KProperty<*>, value: T) {
        if (!::key.isInitialized)
            key = NamespacedKey(instance, property.name)

        onSet(value)

        if (value == null)
            return thisRef.remove(key)

        thisRef.set(key, type, value)
    }

    @Suppress("UNCHECKED_CAST")
    override operator fun getValue(thisRef: PersistentDataHolder, property: KProperty<*>): T {
        return getValue(thisRef.persistentDataContainer, property)
    }

    override operator fun setValue(thisRef: PersistentDataHolder, property: KProperty<*>, value: T) {
        return setValue(thisRef.persistentDataContainer, property, value)
    }
}

class NonPersistentKey<T>(val fallbackValue: T?, val onSet: Setter<T>) : Key<T> {
    private var backing = mutableMapOf<UUID, T>()

    override fun getValue(thisRef: PersistentDataContainer, property: KProperty<*>): T {
        throw IllegalStateException("Non persistent keys can only be stored on holders")
    }

    override fun setValue(thisRef: PersistentDataContainer, property: KProperty<*>, value: T) {
        throw IllegalStateException("Non persistent keys can only be stored on holders")
    }

    @Suppress("UNCHECKED_CAST")
    override operator fun getValue(thisRef: PersistentDataHolder, property: KProperty<*>): T {
        thisRef as Player
        return backing.getOrPut(thisRef.uniqueId) { fallbackValue as T }
    }

    override operator fun setValue(thisRef: PersistentDataHolder, property: KProperty<*>, value: T) {
        thisRef as Player
        onSet(value)
        backing[thisRef.uniqueId] = value
    }

    fun cleanUp(thisRef: PersistentDataHolder) {
        thisRef as Player
        backing -= thisRef.uniqueId
    }
}

inline fun <reified T> getPersistentDataType(): PersistentDataType<*, T> {
    val clazz = T::class.java

    @Suppress("UNCHECKED_CAST")
    return when (clazz) {
        List::class.java -> {
            val tree = mutableListOf<KClass<*>>()
            var type = typeOf<T>()

            while (type.arguments.isNotEmpty()) {
                val component = type.arguments[0].type!!
                val classifier = component.classifier as KClass<*>
                tree += classifier
                type = component
            }

            tree.reverse()

            var listType: PersistentDataType<*, *>? = null

            for (clazz in tree) {
                val dataType = getSimplePersistentDataType(clazz.java)

                listType = if (listType == null)
                    PersistentDataType.LIST.listTypeFrom(dataType)
                else PersistentDataType.LIST.listTypeFrom(listType)
            }

            listType
        }
        else -> getSimplePersistentDataType(clazz)
    } as PersistentDataType<*, T>
}

fun <T> getSimplePersistentDataType(clazz: Class<T>): PersistentDataType<*, T> {
    if (clazz.isEnum)
        return EnumDataType(clazz as Class<out Enum<*>>) as PersistentDataType<*, T>

    @Suppress("UNCHECKED_CAST")
    return when (clazz) {
        Byte::class.javaObjectType, Byte::class.javaPrimitiveType -> PersistentDataType.BYTE
        Short::class.javaObjectType, Short::class.javaPrimitiveType -> PersistentDataType.SHORT
        Int::class.javaObjectType, Int::class.javaPrimitiveType -> PersistentDataType.INTEGER
        Long::class.javaObjectType, Long::class.javaPrimitiveType -> PersistentDataType.LONG
        Float::class.javaObjectType, Float::class.javaPrimitiveType -> PersistentDataType.FLOAT
        Double::class.javaObjectType, Double::class.javaPrimitiveType -> PersistentDataType.DOUBLE
        Boolean::class.javaObjectType, Boolean::class.javaPrimitiveType -> PersistentDataType.BOOLEAN
        String::class.java -> PersistentDataType.STRING
        Location::class.java -> LocationDataType
        Player::class.java -> PlayerDataType
        else -> throw IllegalArgumentException("Unknown persistent data type ${clazz.name}")
    } as PersistentDataType<*, T>
}