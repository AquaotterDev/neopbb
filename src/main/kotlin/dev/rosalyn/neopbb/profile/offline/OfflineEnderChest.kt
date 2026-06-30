package dev.rosalyn.neopbb.profile.offline

import net.minecraft.world.ItemStackWithSlot
import net.minecraft.world.inventory.PlayerEnderChestContainer
import org.bukkit.OfflinePlayer
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