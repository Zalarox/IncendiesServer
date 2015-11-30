package incendius.game.players.packets;

import incendius.game.players.PacketType;
import incendius.game.players.Player;
import incendius.game.players.PlayerHandler;

public class ItemOnPlayer implements PacketType {

	@Override
	@SuppressWarnings("unused")
	public void processPacket(final Player c, final int packetType, final int packetSize) {
		int a = c.getInStream().readUnsignedWordBigEndianA();
		int playerIndex = c.getInStream().readUnsignedWord();
		int item = c.getInStream().readUnsignedWord();
		int slot = c.getInStream().readUnsignedWordBigEndian();
		if (c.getInstance().teleTimer > 0)
			return;
		if (playerIndex > PlayerHandler.players.length) {
			return;
		}
		if (slot > c.getInstance().playerItems.length) {
			return;
		}
		if (PlayerHandler.players[playerIndex] == null) {
			return;
		}
		if (!c.getItems().playerHasItem(item, 1, slot)) {
			return;
		}

		Player o = PlayerHandler.players[playerIndex];
		switch (item) {
		default:
			c.sendMessage("Nothing interesting happenes.");
			break;
		}
	}

}