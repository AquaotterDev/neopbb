package me.honkling.neopbb.profile.offline

import com.mojang.logging.LogUtils
import me.honkling.neopbb.world
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.util.ProblemReporter
import net.minecraft.world.ItemStackWithSlot
import net.minecraft.world.inventory.PlayerEnderChestContainer
import net.minecraft.world.level.storage.TagValueOutput
import org.bukkit.OfflinePlayer
import org.bukkit.craftbukkit.CraftWorld
import org.bukkit.craftbukkit.entity.CraftHumanEntity

class OfflineEnderChest(owner: OfflinePlayer) : PlayerEnderChestContainer(VoodooPlayer(owner)) {
    private val dataAccess = DataAccess(owner)

    init {
        val data = dataAccess.valueInput.listOrEmpty("EnderItems", ItemStackWithSlot.CODEC)
        fromSlots(data)
    }

    override fun onClose(player: CraftHumanEntity) {
        super.onClose(player)
        storeAsSlots(dataAccess.valueOutput.list("EnderItems", ItemStackWithSlot.CODEC))
        dataAccess.save()
    }
}