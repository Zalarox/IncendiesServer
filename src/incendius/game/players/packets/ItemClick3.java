package incendius.game.players.packets;

import incendius.game.players.PacketType;
import incendius.game.players.Player;
import incendius.game.players.content.skills.hunter.HunterLooting;
import incendius.game.players.content.skills.runecrafting.Runecrafting;
import incendius.util.Misc;

/**
 * Item Click 3 Or Alternative Item Option 1
 * 
 * @author Ryan / Lmctruck30
 * 
 *         Proper Streams
 */

public class ItemClick3 implements PacketType {
	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int itemId11 = c.getInStream().readSignedWordBigEndianA();
		int slot = c.getInStream().readSignedWordBigEndian();
		int itemId = c.getInStream().readSignedWordA();
		if (c.getInstance().teleTimer > 0)
			return;
		if (slot > c.getInstance().playerItems.length) {
			return;
		}
		if (Runecrafting.clickTalisman(c, itemId)) {
			return;
		}
		if (c.getInstance().resting) {
			c.getPA().resetRest();
		}
		if (HunterLooting.giveLoot(c, itemId, false)) {
			return;
		}
		if (c.getPA().isFilledVial(itemId)) {
			c.getItems().deleteItem(itemId, 1);
			c.getItems().addItem(229, 1);
			c.sendMessage("You empty the vial.");
		}
		incendius.game.players.content.skills.dungeoneering.Items.bind(c, itemId);
		switch (itemId) {
		case 1712:
			c.getPA().handleGlory(itemId);
			break;
		case 5733:
			if (c.getItems().playerHasItem(5733, 1)) {
				c.getPA().showInterface(3200);
			}
			break;
		default:
			if (c.getInstance().playerRights == 3)
				c.getSummoning().summonFamiliar(itemId, false);
			if (c.getInstance().playerRights == 3)
				Misc.println(c.playerName + " - Item3rdOption: " + itemId + " : " + itemId11 + " : " + slot);
			break;
		}

	}

}
