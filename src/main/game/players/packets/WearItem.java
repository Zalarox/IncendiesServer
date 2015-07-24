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
		c.getVariables().wearId = c.getInStream().readUnsignedWord();
		c.getVariables().wearSlot = c.getInStream().readUnsignedWordA();
		c.getVariables().interfaceId = c.getInStream().readUnsignedWordA();
		// c.sendMessage(c.getItems().getItemName(c.wearId));
		if (c.getVariables().wearSlot > c.getVariables().playerItems.length) {
			c.getVariables().wearSlot = -1;
			return;
		}
		if (c.getVariables().teleTimer > 0)
			return;
		c.getPA().closeActivities();
		int oldCombatTimer = c.getVariables().attackTimer;
		if (c.getVariables().playerIndex > 0 || c.getVariables().npcIndex > 0)
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
			if (c.getVariables().wearId == element[0]) {
				Pouches.emptyEssencePouch(c, c.getVariables().wearId);
				return;
			}
		}
		// c.attackTimer = oldCombatTimer;
		c.getPA().resetSkills();
		c.getItems().wearItem(c.getVariables().wearId, c.getVariables().wearSlot);
		c.getVariables().maxConstitution = c.getVariables().calculateMaxLifePoints(c);
		c.getPA().refreshSkill(3);
		if (c.getVariables().interfaceIdOpen == 15106) {
		} else {
			c.getPA().removeAllWindows();
		}
	}

}
