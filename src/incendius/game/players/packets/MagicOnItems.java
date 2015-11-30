package incendius.game.players.packets;

import incendius.game.players.PacketType;
import incendius.game.players.Player;

/**
 * Magic on items
 **/
public class MagicOnItems implements PacketType {

	@Override
	@SuppressWarnings("unused")
	public void processPacket(Player c, int packetType, int packetSize) {
		int slot = c.getInStream().readSignedWord();
		int itemId = c.getInStream().readSignedWordA();
		int junk = c.getInStream().readSignedWord();
		int spellId = c.getInStream().readSignedWordA();
		if (c.getInstance().teleTimer > 0)
			return;
		if (slot > c.getInstance().playerItems.length) {
			return;
		}
		c.getInstance().usingMagic = true;
		c.getPA().magicOnItems(slot, itemId, spellId);
		c.getInstance().usingMagic = false;

	}

}
