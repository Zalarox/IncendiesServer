package incendius.game.items.degrade;

import incendius.game.players.Player;

/**
 * 
 * @author Tringan/Rafael
 *
 */
public class Chaotic {

	public final static int DEGRADE_TIME = 60000;

	public final static int[] brokenIds = { 18350, 18352, 18354, 18356, 18358, 18360, 18362, 18364, 18366, 18368, 18370,
			18372, 18374 };

	public static void degrade(Player c, int attackTimer) {
		for (int i = 0; i < brokenIds.length; i++) {
			if (c.playerEquipment[c.playerWeapon] == brokenIds[i] - 1
					|| c.playerEquipment[c.playerShield] == brokenIds[i] - 1) {
				c.chaoticDegrade[i] += attackTimer;
				if (c.chaoticDegrade[i] >= DEGRADE_TIME) {
					c.chaoticDegrade[i] = DEGRADE_TIME;
					c.playerEquipment[c.playerEquipment[c.playerWeapon] == brokenIds[i] - 1 ? c.playerWeapon
							: c.playerShield] = brokenIds[i];
					c.setAppearanceUpdateRequired(true);
					c.sendMessage("Your " + c.getItems().getItemName(brokenIds[i] - 1) + " has fully degraded.");
				}
			}
		}
	}

	public static void checkCharges(Player player, int clickedItem) {
		for (int i = 0; i < brokenIds.length; i++) {
			if (clickedItem == brokenIds[i] - 1) {
				player.sendMessage("Your " + player.getItems().getItemName(clickedItem) + " has "
						+ (100 - (int) ((player.chaoticDegrade[i] * 100.0f) / DEGRADE_TIME)) + "% of its charge left.");
				break;
			} else if (clickedItem == brokenIds[i]) {
				player.sendMessage("Your " + player.getItems().getItemName(clickedItem) + " is fully degraded");
				break;
			}
		}
	}

}
