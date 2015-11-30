package incendius.game.players.packets;

import incendius.Server;
import incendius.game.players.PacketType;
import incendius.game.players.Player;
import incendius.util.Misc;

/**
 * Chat
 **/
public class ClanChat implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		String textSent = Misc.longToPlayerName2(c.getInStream().readQWord());
		textSent = textSent.replaceAll("_", " ");
		Server.clanChat.handleClanChatJoin(c, textSent);
	}
}
