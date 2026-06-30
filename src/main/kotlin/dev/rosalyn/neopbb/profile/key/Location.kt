package dev.rosalyn.neopbb.profile.key

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

private var PersistentDataContainer.world by createKey<String>()
private var PersistentDataContainer.x by createKey<Double>(0.0)
private var PersistentDataContainer.y by createKey<Double>(0.0)
private var PersistentDataContainer.z by createKey<Double>(0.0)
private var PersistentDataContainer.yaw by createKey<Float>(0f)
private var PersistentDataContainer.pitch by createKey<Float>(0f)

object LocationDataType : PersistentDataType<PersistentDataContainer, Location> {
    override fun getPrimitiveType(): Class<PersistentDataContainer> {
        return PersistentDataContainer::class.java
    }

    override fun getComplexType(): Class<Location> {
        return Location::class.java
    }

    override fun toPrimitive(
        complex: Location,
        context: PersistentDataAdapterContext
    ): PersistentDataContainer {
        val container = context.newPersistentDataContainer()
        complex.world?.let { container.world = it.name }
        container.x = complex.x
        container.y = complex.y
        container.z = complex.z
        container.yaw = complex.yaw
        container.pitch = complex.pitch
        return container
    }

    override fun fromPrimitive(
        primitive: PersistentDataContainer,
        context: PersistentDataAdapterContext
    ): Location {
        return Location(
            primitive.world?.let { Bukkit.getWorld(it) },
            primitive.x,
            primitive.y,
            primitive.z,
            primitive.yaw,
            primitive.pitch
        )
    }

}