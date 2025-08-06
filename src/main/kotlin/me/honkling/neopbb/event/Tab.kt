package me.honkling.neopbb.event

import com.github.retrooper.packetevents.event.PacketSendEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate
import me.honkling.neopbb.event.packet.ClientboundPacket

@ClientboundPacket(PacketType.Play.Server.PLAYER_INFO_UPDATE)
private fun onPlayerInfoUpdate(event: PacketSendEvent) {
    val packet = WrapperPlayServerPlayerInfoUpdate(event)

    for (entry in packet.entries)
        entry.isListed = false

    event.markForReEncode(true)
}