package dev.rosalyn.neopbb.profile.offline

import com.mojang.authlib.GameProfile
import dev.rosalyn.neopbb.world
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.GameType
import org.bukkit.OfflinePlayer
import org.bukkit.craftbukkit.CraftWorld

class VoodooPlayer(backingEntity: OfflinePlayer) : Player(
    (world as CraftWorld).handle,
    GameProfile(backingEntity.uniqueId, backingEntity.name)
) {
    override fun gameMode(): GameType? {
        throw IllegalStateException("Illegal operation on voodoo player")
    }
}