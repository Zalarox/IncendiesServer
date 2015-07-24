package main.game.players.packets;

import main.game.items.degrade.Chaotic;
import main.game.players.PacketType;
import main.game.players.Player;
import main.game.players.content.skills.hunter.HunterLooting;
import main.util.Misc;

/**
 * Item Click 2 Or Alternative Item Option 1
 * 
 * @author Ryan / Lmctruck30
 * 
 *         Proper Streams
 */

public class ItemClick2 implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int itemId = c.getInStream().readSignedWordA();
		if (c.getVariables().teleTimer > 0)
			return;
		if (!c.getItems().playerHasItem(itemId, 1))
			return;
		if (c.getVariables().resting) {
			c.getPA().resetRest();
		}
		if (HunterLooting.giveLoot(c, itemId, false)) {
			return;
		}
		main.game.players.content.skills.dungeoneering.Items.bind(c, itemId);
		switch (itemId) {
		case 18355:
		case 18349:
		case 18351:
			;
		case 18353:
		case 18357:
		case 18359:
		case 18363:
		case 18361:
			Chaotic.checkCharges(c, itemId);
			break;
		case 11694:// ags
		case 11696:// bgs
		case 11698:// sgs
		case 11700:// zgs
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You need atleast 2 free slots to dismantle your godsword.");
				return;
			}
			if (c.getItems().playerHasItem(itemId, 1)) {
				c.getItems().deleteItem(itemId, 1);
				c.getItems().addItem(itemId + 8, 1);
				c.getItems().addItem(11690, 1);
			}
			break;
		case 15707:
			main.game.players.content.skills.dungeoneering.Items.teleport(c, false);
			break;
		default:
			if (c.getVariables().playerRights == 3)
				Misc.println(c.playerName + " - Item2ndOption: " + itemId);
			break;
		}

	}

}
