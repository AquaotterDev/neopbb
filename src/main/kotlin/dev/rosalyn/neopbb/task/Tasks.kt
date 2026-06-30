package dev.rosalyn.neopbb.task

import dev.rosalyn.neopbb.instance
import dev.rosalyn.neopbb.refreshTab
import org.bukkit.Bukkit

fun registerTasks() {
    val scheduler = Bukkit.getScheduler()
    scheduler.scheduleSyncRepeatingTask(instance, {
        refreshTab()
        executeSolitary()
    }, 0L, 20L)
    scheduler.scheduleSyncRepeatingTask(instance, {
        executeRollCall()
        executeActionBar()
        executeLightsOut()
    }, 0L, 1L)
}