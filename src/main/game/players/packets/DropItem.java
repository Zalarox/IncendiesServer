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
		if (c.getInstance().teleTimer > 0)
			return;
		c.getInstance().alchDelay = System.currentTimeMillis();
		/*
		 * if (itemId >= 0 && itemId <= 22000) { c.getItems().deleteItem(itemId,
		 * slot, c.playerItemsN[slot]); c.sendMessage(
		 * "@blu@You drop the item to the floor and it vanishes!"); return; }
		 */
		if (slot > c.getInstance().playerItems.length) {
			return;
		}
		if (c.getInstance().resting) {
			c.getPA().resetRest();
		}
		if (c.getInstance().inTrade) {
			c.getTradeHandler().declineTrade(true);
		}
		if (c.isDead) {
			c.sendMessage("You can't drop items when you are dead.");
			return;
		}
		if (c.getInstance().playerSkilling[c.getInstance().playerFiremaking]) {
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
		if (c.getInstance().playerItemsN[slot] != 0 && itemId != -1
				&& c.getInstance().playerItems[slot] == itemId + 1) {
			if (destroyable) {
				c.getPA().destroyInterface(itemId);
				return;
			}
			if (droppable) {
				if (c.getInstance().underAttackBy > 0) {
					if (c.getShops().getItemShopValue(itemId) > 1000) {
						c.sendMessage("You may not drop items worth more than 1000 while in combat.");
						return;
					}
				}
				int amount = c.getInstance().playerItemsN[slot];
				
				ItemHandler.createGroundItem(c, itemId, c.getX(), c.getY(), c.heightLevel,
						amount, c.getId());
				
				
				int x = c.getX();
				int y = c.getY();
				
				c.getItems().deleteItem(itemId, slot, amount);	
				
				/**
				 * Log the drop.
				 */
				c.getLogging().logDrop(itemId, amount, x, y, true);
				
			} else {
				c.sendMessage("This item cannot be dropped.");
			}
		}
		c.getPA().closeAllWindows();
		CycleEventHandler.getInstance().stopEvent(c.getSkilling());
	}
}
