package me.honkling.neopbb.profile.offline

import com.mojang.logging.LogUtils
import me.honkling.neopbb.instance
import me.honkling.neopbb.world
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtAccounter
import net.minecraft.nbt.NbtIo
import net.minecraft.util.ProblemReporter
import net.minecraft.world.level.storage.TagValueInput
import net.minecraft.world.level.storage.TagValueOutput
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.craftbukkit.CraftWorld
import java.util.UUID

class DataAccess(uniqueId: UUID) {
    constructor(player: OfflinePlayer) : this(player.uniqueId)

    private val file = world.worldFolder.resolve("playerdata/$uniqueId.dat")
    val valueInput: TagValueInput
    val valueOutput: TagValueOutput

    init {
        val compound = if (file.exists())
            NbtIo.readCompressed(file.toPath(), NbtAccounter.unlimitedHeap())
        else CompoundTag()

        val problemReporter = ProblemReporter.ScopedCollector({ "DataAccess()" }, LogUtils.getLogger())
        val registryAccess = (world as CraftWorld).handle.registryAccess()

        valueInput = TagValueInput.create(problemReporter, registryAccess, compound) as TagValueInput
        valueOutput = TagValueOutput.createWrappingWithContext(problemReporter, registryAccess, compound)
    }

    fun save() {
        NbtIo.writeCompressed(valueOutput.buildResult(), file.toPath())
    }
}