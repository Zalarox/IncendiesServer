package main.game.players.packets;

import main.game.items.GameItem;
import main.game.items.ItemLoader;
import main.game.players.PacketType;
import main.game.players.Player;
import main.game.players.content.minigames.DuelArena;

/**
 * Bank All Items
 **/
public class BankAll implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int removeSlot = c.getInStream().readUnsignedWordA();
		int interfaceId = c.getInStream().readUnsignedWord();
		int removeId = c.getInStream().readUnsignedWordA();
		if (c.getInstance().teleTimer > 0) {
			return;
		}
		switch (interfaceId) {
		case 3900:
			c.getShops().buyItem(removeId, removeSlot, 10);
			break;

		case 3823:
			c.getShops().sellItem(removeId, removeSlot, 10);
			break;

		case 5064:
			if (!c.getInstance().usingBoB) {
				if (c.getInstance().inTrade) {
					c.sendMessage("You can't store items while trading!");
					return;
				}
				if (ItemLoader.isStackable(removeId)) {
					c.getItems().bankItem(c.getInstance().playerItems[removeSlot], removeSlot,
							c.getInstance().playerItemsN[removeSlot]);
				} else {
					c.getItems().bankItem(c.getInstance().playerItems[removeSlot], removeSlot,
							c.getItems().itemAmount(c.getInstance().playerItems[removeSlot]));
				}
			} else {
				c.sendMessage("" + removeSlot + " " + interfaceId + " " + removeId);
			}
			break;

		case 5382:
			c.getItems().fromBank(removeSlot, c.getInstance().bankItemsN[removeSlot]);
			break;

		case 3322:
			if (!DuelArena.isDueling(c) && !DuelArena.isInFirstInterface(c) && !DuelArena.isInSecondInterface(c)) {
				if (ItemLoader.isStackable(removeId)) {
					c.getTradeHandler().tradeItem(removeId, removeSlot, c.getInstance().playerItemsN[removeSlot]);
				} else {
					c.getTradeHandler().tradeItem(removeId, removeSlot, 28);
				}
			} else {
				if (ItemLoader.isStackable(removeId) || ItemLoader.isNote(removeId)) {
					c.Dueling.addStakedItem(removeId, removeSlot, c.getInstance().playerItemsN[removeSlot], c);
				} else {
					c.Dueling.addStakedItem(removeId, removeSlot, 28, c);
				}
			}
			break;

		case 3415:
			if (!DuelArena.isDueling(c) && !DuelArena.isInFirstInterface(c) && !DuelArena.isInSecondInterface(c)) {
				if (ItemLoader.isStackable(removeId)) {
					for (GameItem item : c.getTradeHandler().offeredItems) {
						if (item.id == removeId) {
							c.getTradeHandler().fromTrade(removeId, removeSlot,
									c.getTradeHandler().offeredItems.get(removeSlot).amount);
						}
					}
				} else {
					for (GameItem item : c.getTradeHandler().offeredItems) {
						if (item.id == removeId) {
							c.getTradeHandler().fromTrade(removeId, removeSlot, 28);
						}
					}
				}
			}
			break;

		case 6669:
			if (ItemLoader.isStackable(removeId) || ItemLoader.isNote(removeId)) {
				for (GameItem item : c.getInstance().stakedItems) {
					if (item.id == removeId) {
						c.sendMessage("bankAll");
						c.Dueling.removeStakedItem(removeId, removeSlot,
								c.getInstance().stakedItems.get(removeSlot).amount, c);
					}
				}

			} else {
				c.sendMessage("bankAll");
				c.Dueling.removeStakedItem(removeId, removeSlot, 28, c);
			}
			break;

		}
	}

}
