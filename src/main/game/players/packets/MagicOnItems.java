package main.game.players.packets;

import main.game.players.PacketType;
import main.game.players.Player;

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
		if (c.getVariables().teleTimer > 0)
			return;
		if (slot > c.getVariables().playerItems.length) {
			return;
		}
		c.getVariables().usingMagic = true;
		c.getPA().magicOnItems(slot, itemId, spellId);
		c.getVariables().usingMagic = false;

	}

}
