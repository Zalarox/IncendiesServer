package incendius.handlers;

import incendius.game.players.Player;

/**
 *
 * Skill Handler
 */
public class Skill {

	private Player c;

	public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2, HITPOINTS = 3, RANGED = 4, PRAYER = 5, MAGIC = 6,
			COOKING = 7, WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11, CRAFTING = 12, SMITHING = 13,
			MINING = 14, HERBLORE = 15, AGILITY = 16, THIEVING = 17, SLAYER = 18, FARMING = 19, RUNECRAFTING = 20;

	public Skill(Player c) {
		this.c = c;
	}

	public int getLevel()[] {
		return c.getInstance().playerLevel;
	}

	public void addExp(int skill, double xp) {
		c.getPA().addSkillXP((int) Math.ceil(xp), skill);
	}

	public int getPlayerLevel(int id) {
		return c.getInstance().playerLevel[id];
	}

	public int getClientLevel(int id) {
		return c.getInstance().playerLevel[id];
	}

}
