package dev.rosalyn.neopbb.profile.key

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import java.util.UUID

object PlayerDataType : PersistentDataType<LongArray, Player> {
    override fun getPrimitiveType(): Class<LongArray> {
        return LongArray::class.java
    }

    override fun getComplexType(): Class<Player> {
        return Player::class.java
    }

    override fun toPrimitive(
        complex: Player,
        context: PersistentDataAdapterContext
    ): LongArray {
        val uuid = complex.uniqueId
        return longArrayOf(uuid.mostSignificantBits, uuid.leastSignificantBits)
    }

    override fun fromPrimitive(
        primitive: LongArray,
        context: PersistentDataAdapterContext
    ): Player {
        return Bukkit.getPlayer(UUID(primitive[0], primitive[1]))!!
    }
}