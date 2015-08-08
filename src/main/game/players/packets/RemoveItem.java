package main.game.players.packets;

import main.game.players.PacketType;
import main.game.players.Player;
import main.game.players.content.minigames.DuelArena;
import main.game.players.content.skills.crafting.GemCrafting;
import main.game.players.content.skills.crafting.GemData;

/**
 * Remove Item
 **/
public class RemoveItem implements PacketType {

	@Override
	@SuppressWarnings("unused")
	public void processPacket(Player c, int packetType, int packetSize) {
		int interfaceId = c.getInStream().readUnsignedWordA();
		int slot = c.getInStream().readUnsignedWordA();
		int removeId = c.getInStream().readUnsignedWordA();
		int shop = 0;
		int value = 0;
		String name = "null";
		if (c.getInstance().teleTimer > 0)
			return;
		switch (interfaceId) {
		case 24006:
			c.getSummoning().depositItem(removeId, slot, 1);
			break;
		case 24002:
			c.getSummoning().withdrawItem(removeId, 1);
			break;
		case 1688:
			c.getItems().removeItem(removeId, slot);
			break;

		case 5064:
			c.getItems().bankItem(removeId, slot, 1);
			break;

		case 5382:
			c.getItems().fromBank(slot, 1);
			break;
		case 4233: // make 1 ring crafting
			GemCrafting.startCrafter(c, GemData.getGemSlot()[slot], 1, 0);
			break;
		case 4239: // make 1 neckalce crafting
			GemCrafting.startCrafter(c, GemData.getGemSlot()[slot], 1, 1);
			break;
		case 4245: // make 1 amulet crafting
			GemCrafting.startCrafter(c, GemData.getGemSlot()[slot], 1, 2);
			break;

		case 3900:
			c.getShops().buyFromShopPrice(removeId, slot);
			break;

		case 3823:
			c.getShops().sellToShopPrice(removeId, slot);
			break;

		case 3322:
			if (!DuelArena.isDueling(c) && !DuelArena.isInFirstInterface(c) && !DuelArena.isInSecondInterface(c)) {
				if (!c.getInstance().inTrade) {
					return;
				}
				c.getTradeHandler().tradeItem(removeId, slot, 1);
			} else {
				c.Dueling.addStakedItem(removeId, slot, 1, c);
			}
			break;

		case 3415:
			if (!DuelArena.isDueling(c) && !DuelArena.isInFirstInterface(c) && !DuelArena.isInSecondInterface(c)) {
				if (!c.getInstance().inTrade) {
					return;
				}
				c.getTradeHandler().fromTrade(removeId, slot, 1);
			}
			break;

		case 6669:
			c.Dueling.removeStakedItem(removeId, slot, 1, c);
			break;

		case 1119:
		case 1120:
		case 1121:
		case 1122:
		case 1123:
			c.getSmithing().readInput(c.getInstance().playerLevel[c.getInstance().playerSmithing],
					Integer.toString(removeId), c, 1);
			break;
		}
	}
}
