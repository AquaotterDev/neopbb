package me.honkling.neopbb.task

import me.honkling.neopbb.instance
import org.bukkit.Bukkit

fun registerTasks() {
    Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, {
        executeRollCall()
    }, 0L, 1L)
}