package main.game.players.packets;

import main.game.players.PacketType;
import main.game.players.Player;

public class IdleLogout implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		/*
		 * switch(packetType) { case 202: if (c.underAttackBy > 0 ||
		 * c.underAttackBy2 > 0) { return; } else { c.logout(); } break; }
		 */
	}
}