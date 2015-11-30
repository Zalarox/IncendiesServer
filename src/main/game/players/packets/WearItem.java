package main.game.players.packets;

import main.game.players.PacketType;
import main.game.players.Player;
import main.game.players.content.skills.runecrafting.Pouches;

/**
 * Wear Item
 **/
public class WearItem implements PacketType {

	@Override
	@SuppressWarnings("unused")
	public void processPacket(Player c, int packetType, int packetSize) {
		c.getInstance().wearId = c.getInStream().readUnsignedWord();
		c.getInstance().wearSlot = c.getInStream().readUnsignedWordA();
		c.getInstance().interfaceId = c.getInStream().readUnsignedWordA();
		// c.sendMessage(c.getItems().getItemName(c.wearId));
		if (c.getInstance().wearSlot > c.getInstance().playerItems.length) {
			c.getInstance().wearSlot = -1;
			return;
		}
		if (c.getInstance().teleTimer > 0)
			return;
		c.getPA().closeActivities();
		int oldCombatTimer = c.getInstance().attackTimer;
		if (c.getInstance().playerIndex > 0 || c.getInstance().npcIndex > 0)
			c.getCombat().resetPlayerAttack();
		/*
		 * if(c.interfaceIdOpen == 24700) {
		 * if(c.getShops().getItemShopValue(c.wearId) == 0) { c.sendMessage(
		 * "This item cannot be sold on the Grand Exchange."); return; }
		 * if(ItemDefinition.forId(c.wearId). itemIsNote) c.wearId -= 1;
		 * c.GE().selectedItemId = c.wearId; c.GE().selectedPrice =
		 * c.getShops().getItemShopValue(c.wearId);
		 * c.GE().updateGE(c.GE().selectedItemId, c.GE().selectedPrice); return;
		 * }
		 */
		for (int[] element : Pouches.POUCHES) {
			if (c.getInstance().wearId == element[0]) {
				Pouches.emptyEssencePouch(c, c.getInstance().wearId);
				return;
			}
		}
		// c.attackTimer = oldCombatTimer;
		c.getPA().resetSkills();
		c.getItems().wearItem(c.getInstance().wearId, c.getInstance().wearSlot);
		c.setMaxLP(c.calculateMaxLP()); //Re-calculate max LP value - Nex armors
		c.getPA().refreshSkill(3);
		if (c.getInstance().interfaceIdOpen == 15106) {
		} else {
			c.getPA().removeAllWindows();
		}
	}

}
