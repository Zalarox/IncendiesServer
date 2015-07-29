package main.game.players.packets;

import main.game.players.PacketType;
import main.game.players.Player;
import main.game.players.content.minigames.DuelArena;
import main.game.players.content.skills.crafting.GemCrafting;
import main.game.players.content.skills.crafting.GemData;

/**
 * Bank 5 Items
 **/
public class Bank5 implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int interfaceId = c.getInStream().readSignedWordBigEndianA();
		int removeId = c.getInStream().readSignedWordBigEndianA();
		int slot = c.getInStream().readSignedWordBigEndian();
		if (c.getVariables().teleTimer > 0) {
			return;
		}
		switch (interfaceId) {
		case 24006:
			c.getSummoning().depositItem(removeId, slot, 5);
			break;
		case 24002:
			c.getSummoning().withdrawItem(removeId, 5);
			break;
		case 4233: // make 1 ring crafting
			GemCrafting.startCrafter(c, GemData.getGemSlot()[slot], 5, 0);
			break;
		case 4239: // make 1 neckalce crafting
			GemCrafting.startCrafter(c, GemData.getGemSlot()[slot], 5, 1);
			break;
		case 4245: // make 1 amulet crafting
			GemCrafting.startCrafter(c, GemData.getGemSlot()[slot], 5, 2);
			break;

		case 3900:
			c.getShops().buyItem(removeId, slot, 1);
			break;

		case 3823:
			c.getShops().sellItem(removeId, slot, 1);
			break;

		case 5064:
			c.getItems().bankItem(removeId, slot, 5);
			break;

		case 5382:
			c.getItems().fromBank(slot, 5);
			break;

		case 3322:
			if (!DuelArena.isDueling(c) && !DuelArena.isInFirstInterface(c) && !DuelArena.isInSecondInterface(c)) {
				c.getTradeHandler().tradeItem(removeId, slot, 5);
			} else {
				c.Dueling.addStakedItem(removeId, slot, 5, c);
			}
			break;

		case 3415:
			if (!DuelArena.isDueling(c) && !DuelArena.isInFirstInterface(c) && !DuelArena.isInSecondInterface(c)) {
				c.getTradeHandler().fromTrade(removeId, slot, 5);
			}
			break;

		case 6669:
			c.Dueling.removeStakedItem(removeId, slot, 5, c);
			break;

		case 1119:
		case 1120:
		case 1121:
		case 1122:
		case 1123:
			c.getSmithing().readInput(c.getVariables().playerLevel[c.getVariables().playerSmithing],
					Integer.toString(removeId), c, 5);
			break;

		}
	}

}
