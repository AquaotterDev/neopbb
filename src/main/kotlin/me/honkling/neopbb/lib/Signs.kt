package me.honkling.neopbb.lib

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.block.Sign
import org.bukkit.block.sign.Side

fun getAllSignLines(sign: Sign): List<String>{
    val frontSideLines = getSignLines(sign, Side.FRONT)
    val backSideLines = getSignLines(sign, Side.BACK)

    return frontSideLines+backSideLines
}

fun getSignLines(sign: Sign, side: Side): List<String>{
    val side = sign.getSide(side)
    return side.lines().map { PlainTextComponentSerializer.plainText().serialize(it) }
}