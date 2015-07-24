package main.game.players.packets;

import main.GameEngine;
import main.game.players.PacketType;
import main.game.players.Player;
import main.util.Misc;

/**
 * Chat
 **/
public class ClanChat implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		String textSent = Misc.longToPlayerName2(c.getInStream().readQWord());
		textSent = textSent.replaceAll("_", " ");
		GameEngine.clanChat.handleClanChatJoin(c, textSent);
	}
}
