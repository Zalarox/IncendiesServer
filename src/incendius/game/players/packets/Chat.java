package incendius.game.players.packets;

import incendius.Connection;
import incendius.Connection.ConnectionType;
import incendius.game.items.BankSearch;
import incendius.game.players.PacketType;
import incendius.game.players.Player;
import incendius.util.Misc;

/**
 * Chat
 **/
public class Chat implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		c.setChatTextEffects(c.getInStream().readUnsignedByteS());
		c.setChatTextColor(c.getInStream().readUnsignedByteS());
		c.setChatTextSize((byte) (c.packetSize - 2));
		c.inStream.readBytes_reverseA(c.getChatText(), c.getChatTextSize(), 0);
		
		String text = Misc.textUnpack(c.getChatText(), c.getChatTextSize());
		c.getLogging().logChat(text);

		if (c.getInstance().isSearching) {
			BankSearch.clearItems(c);
			BankSearch.inputText(c, text);
		}

		if (!Connection.containsConnection(c.playerName, ConnectionType.MUTE, false)
				&& !Connection.containsConnection(c.connectedFrom, ConnectionType.IPMUTE, false)
				&& !Connection.containsConnection(c.getInstance().identityPunishment,
						ConnectionType.forName("IDENTITY_MUTE"), false)) {
			
			if (!c.isJailed()) {
				c.setChatTextUpdateRequired(true);
			} else {
				c.sendMessage("Rule-breakers lack the privelege of speech.");
			}
			
		} else {
			c.sendMessage("You are muted.");
		}
	}
}
