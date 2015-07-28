package main.game.players.packets;

import main.Constants;
import main.event.CycleEventHandler;
import main.game.players.PacketType;
import main.game.players.Player;
import main.handlers.ItemHandler;

/**
 * Drop Item
 **/
public class DropItem implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int itemId = c.getInStream().readUnsignedWordA();
		c.getInStream().readUnsignedByte();
		c.getInStream().readUnsignedByte();
		int slot = c.getInStream().readUnsignedWordA();
		if (c.getVariables().teleTimer > 0)
			return;
		c.getVariables().alchDelay = System.currentTimeMillis();
		/*
		 * if (itemId >= 0 && itemId <= 22000) { c.getItems().deleteItem(itemId,
		 * slot, c.playerItemsN[slot]); c.sendMessage(
		 * "@blu@You drop the item to the floor and it vanishes!"); return; }
		 */
		if (slot > c.getVariables().playerItems.length) {
			return;
		}
		if (c.getVariables().resting) {
			c.getPA().resetRest();
		}
		if (c.getVariables().inTrade) {
			c.getTradeHandler().declineTrade(true);
		}
		if (c.isDead) {
			c.sendMessage("You can't drop items when you are dead.");
			return;
		}
		if (c.getVariables().playerSkilling[c.getVariables().playerFiremaking]) {
			return;
		}
		boolean droppable = true;
		for (int i : Constants.UNDROPPABLE_ITEMS) {
			if (i == itemId) {
				droppable = false;
				break;
			}
		}
		boolean destroyable = false;
		for (int i : Constants.DESTROYABLES) {
			if (i == itemId) {
				destroyable = true;
				break;
			}
		}
		if (c.getVariables().playerItemsN[slot] != 0 && itemId != -1
				&& c.getVariables().playerItems[slot] == itemId + 1) {
			if (destroyable) {
				c.getPA().destroyInterface(itemId);
				return;
			}
			if (droppable) {
				if (c.getVariables().underAttackBy > 0) {
					if (c.getShops().getItemShopValue(itemId) > 1000) {
						c.sendMessage("You may not drop items worth more than 1000 while in combat.");
						return;
					}
				}
				ItemHandler.createGroundItem(c, itemId, c.getX(), c.getY(), c.heightLevel,
						c.getVariables().playerItemsN[slot], c.getId());
				c.getItems().deleteItem(itemId, slot, c.getVariables().playerItemsN[slot]);			
			} else {
				c.sendMessage("This item cannot be dropped.");
			}
		}
		c.getPA().closeAllWindows();
		CycleEventHandler.getInstance().stopEvent(c.getSkilling());
	}
}
