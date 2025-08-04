@file:Listener

package me.honkling.neopbb.event

import io.papermc.paper.event.player.PlayerItemFrameChangeEvent
import io.papermc.paper.event.player.PlayerOpenSignEvent
import me.honkling.commando.spigot.event.Listener
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent

private fun onBlockPlace(event: BlockPlaceEvent) {
    if (event.player.gameMode != GameMode.CREATIVE)
        event.isCancelled = true
}

private fun onBlockBreak(event: BlockBreakEvent) {
    if (event.player.gameMode != GameMode.CREATIVE)
        event.isCancelled = true
}

private fun onFlipBlock(event: PlayerInteractEvent) {
    val blacklist = listOf("TRAPDOOR", "FENCE_GATE")
    val type = event.clickedBlock?.type
        ?: return

    if (blacklist.any { it in type.name } && event.player.gameMode != GameMode.CREATIVE)
        event.isCancelled = true
}

private fun onRotateItemFrame(event: PlayerItemFrameChangeEvent) {
    if (event.player.gameMode != GameMode.CREATIVE)
        event.isCancelled = true
}

private fun onSignEdit(event: PlayerOpenSignEvent) {
    if (event.player.gameMode != GameMode.CREATIVE)
        event.isCancelled = true
}