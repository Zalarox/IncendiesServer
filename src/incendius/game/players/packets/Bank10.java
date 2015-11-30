package incendius.game.players.packets;

import incendius.game.players.PacketType;
import incendius.game.players.Player;
import incendius.game.players.content.minigames.DuelArena;
import incendius.game.players.content.skills.crafting.GemCrafting;
import incendius.game.players.content.skills.crafting.GemData;
import incendius.game.players.content.skills.runecrafting.Pouches;

/**
 * Bank 10 Items
 **/
public class Bank10 implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int interfaceId = c.getInStream().readUnsignedWordBigEndian();
		int removeId = c.getInStream().readUnsignedWordA();
		int slot = c.getInStream().readUnsignedWordA();
		if (c.getInstance().teleTimer > 0) {
			return;
		}
		if (removeId >= 5509 && removeId <= 5514) {
			Pouches.fillEssencePouch(c, removeId);
			return;
		}
		switch (interfaceId) {

		case 4233: // make 1 ring crafting
			GemCrafting.startCrafter(c, GemData.getGemSlot()[slot], 10, 0);
			break;
		case 4239: // make 1 neckalce crafting
			GemCrafting.startCrafter(c, GemData.getGemSlot()[slot], 10, 1);
			break;
		case 4245: // make 1 amulet crafting
			GemCrafting.startCrafter(c, GemData.getGemSlot()[slot], 10, 2);
			break;
		case 24006:
			c.getSummoning().depositItem(removeId, slot, 5);
			break;
		case 24002:
			c.getSummoning().withdrawItem(removeId, 5);
			break;

		case 1688:
			c.getPA().useOperate(removeId);
			break;
		case 3900:
			c.getShops().buyItem(removeId, slot, 5);
			break;

		case 3823:
			c.getShops().sellItem(removeId, slot, 5);
			break;

		case 5064:
			c.getItems().bankItem(removeId, slot, 10);
			break;

		case 5382:
			c.getItems().fromBank(slot, 10);
			break;

		case 3322:
			if (!DuelArena.isDueling(c) && !DuelArena.isInFirstInterface(c) && !DuelArena.isInSecondInterface(c)) {
				c.getTradeHandler().tradeItem(removeId, slot, 10);
			} else {
				c.Dueling.addStakedItem(removeId, slot, 10, c);
			}
			break;

		case 3415:
			if (!DuelArena.isDueling(c) && !DuelArena.isInFirstInterface(c) && !DuelArena.isInSecondInterface(c)) {
				c.getTradeHandler().fromTrade(removeId, slot, 10);
			}
			break;

		case 6669:
			c.Dueling.removeStakedItem(removeId, slot, 10, c);
			break;

		case 1119:
		case 1120:
		case 1121:
		case 1122:
		case 1123:
			c.getSmithing().readInput(c.getInstance().playerLevel[c.getInstance().playerSmithing],
					Integer.toString(removeId), c, 10);
			break;
		}
	}
}
