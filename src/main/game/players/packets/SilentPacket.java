package main.game.players.packets;

import main.game.players.PacketType;
import main.game.players.Player;

/**
 * Slient Packet
 **/
public class SilentPacket implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {

	}
}
