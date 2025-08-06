package me.honkling.neopbb.task

import me.honkling.neopbb.instance
import org.bukkit.Bukkit

fun registerTasks() {
    Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, {
        executeRollCall()
        executeActionBar()
    }, 0L, 1L)
}