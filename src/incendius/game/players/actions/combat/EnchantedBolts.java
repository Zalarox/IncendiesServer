package incendius.game.players.actions.combat;

import incendius.game.npcs.NPC;
import incendius.game.npcs.NPCHandler;
import incendius.game.players.Player;

/**
 * 
 * @author 2012
 * 
 */
public class EnchantedBolts {

	/**
	 * Creating a combat gfx for entity
	 * 
	 * @param c
	 *            the player
	 * @param i
	 *            the victim index
	 * @param gfx
	 *            the gfx
	 * @param height100
	 *            the gfx height
	 */
	private static void createCombatGFX(final Player c, final int i, final int gfx, final boolean height100) {
		final NPC n = NPCHandler.npcs[i];
		if (c.getInstance().playerIndex > 0) {
			if (height100) {
				c.gfx100(gfx);
			} else {
				c.gfx0(gfx);
			}
		} else if (c.getInstance().npcIndex > 0) {
			if (height100) {
				n.gfx100(gfx);
			} else {
				n.gfx0(gfx);
			}
		}
	}

	/**
	 * Using the special
	 * 
	 * @param c
	 *            the player
	 * @param i
	 *            the victim entity index
	 */
	public static void crossbowSpecial(final Player c, final int i, int damage) {
		final NPC n = NPCHandler.npcs[i];
		c.getCombat().crossbowDamage = 1.0;
		switch (c.getInstance().lastArrowUsed) {
		case 9236: // Lucky Lightning
			createCombatGFX(c, i, 749, false);
			c.getCombat().crossbowDamage = 1.25;
			break;
		case 9237: // Earth's Fury
			createCombatGFX(c, i, 755, false);
			break;
		case 9238: // Sea Curse
			createCombatGFX(c, i, 750, false);
			c.getCombat().crossbowDamage = 1.10;
			break;
		case 9239: // Down to Earth
			createCombatGFX(c, i, 757, false);
			if (c.getInstance().playerIndex > 0) {
				c.getInstance().playerLevel[6] -= 2;
				c.getPA().refreshSkill(6);
				c.getPA().sendMessage("Your magic has been lowered!");
			}
			break;
		case 9240: // Clear Mind
			createCombatGFX(c, i, 751, false);
			if (c.getInstance().playerIndex > 0) {
				c.getInstance().playerLevel[5] -= 2;
				c.getPA().refreshSkill(5);
				c.getPA().sendMessage("Your prayer has been lowered!");
				c.getInstance().playerLevel[5] += 2;
				if (c.getInstance().playerLevel[5] >= c.getLevelForXP(c.getInstance().playerXP[5])) {
					c.getInstance().playerLevel[5] = c.getLevelForXP(c.getInstance().playerXP[5]);
				}
				c.getPA().refreshSkill(5);
			}
			break;
		case 9241: // Magical Poison
			createCombatGFX(c, i, 752, false);
			if (c.getInstance().playerIndex > 0) {
				c.getPA().appendPoison(6);
			}
			break;
		case 9242: // Blood Forfeit // TODO Fix the ruby bolts enchanted hit
			createCombatGFX(c, i, 754, false);
			if (c.lifePoints - c.lifePoints / 20 < 1) {
				break;
			}
			if (c.getInstance().npcIndex > 0) {
				c.getCombat().appendHit(n, n.HP / 10, 1, 0, 0);
			} else if (c.getInstance().playerIndex > 0) {
				c.getCombat().appendHit(c, c.lifePoints / 10, 0, 1, false);
			}
			break;
		case 9243: // Armour Piercing
			createCombatGFX(c, i, 758, true);
			c.getCombat().crossbowDamage = 1.15;
			c.getCombat().ignoreDefence = true;
			break;
		case 9244: // Dragon's Breath
			createCombatGFX(c, i, 756, false);
			if (c.getInstance().playerEquipment[c.getInstance().playerShield] != 1540
					|| c.getInstance().playerEquipment[c.getInstance().playerShield] != 11283
					|| c.getInstance().playerEquipment[c.getInstance().playerShield] != 11284) {
				c.getCombat().crossbowDamage = 1.45;
			}
			break;
		case 9245: // Life Leech
			createCombatGFX(c, i, 753, false);
			c.getCombat().crossbowDamage = 1.15;
			c.getInstance().playerLevel[3] += damage / 25;
			if (c.getInstance().playerLevel[3] >= 112) {
				c.getInstance().playerLevel[3] = 112;
			}
			c.getPA().refreshSkill(3);
			break;
		}
	}
}
