package main.handlers;

import java.util.Random;

import main.game.players.Player;
import main.util.Misc;

public class SkillHandler {

	public static final Random r = new Random(System.currentTimeMillis());

	/**
	 * Skill Experience Multipliers
	 */
	public enum XPRates {
		MELEE(250), PRAYER(200), RANGE(250), MAGIC(220), COOKING(60), WOODCUTTING(60), FLETCHING(12), FISHING(
				90), FIREMAKING(40), CRAFTING(30), SMITHING(60), MINING(35), HERBLORE(35), AGILITY(40), THIEVING(
						60), SLAYER(6), FARMING(40), RUNECRAFTING(35), SUMMONING(30);

		private int rate;

		private XPRates(int rate) {
			this.rate = rate;
		}

		public int getXPRate() {
			return rate;
		}
	}

	public static final String[] skillNames = { "Attack", "Defence", "Strength", "Hitpoints", "Range", "Prayer",
			"Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining",
			"Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting" };

	public static String getLine() {
		return "\\n\\n\\n\\n\\n";
	}

	public static boolean noInventorySpace(Player c, String skill) {
		if (c.getItems().freeSlots() < 1) {
			c.sendMessage("You don't have enough inventory space to continue " + skill + "!");
			c.getPA().resetPlayerSkillVariables();
			return false;
		}
		return true;
	}

	public static boolean skillCheck(int level, int levelRequired, int itemBonus) {
		double chance = 0.0;
		double baseChance = levelRequired < 11 ? 15 : levelRequired < 51 ? 10 : 5;// Math.pow(10d-levelRequired/10d,
																					// 2d)/2d;
		chance = baseChance + ((level - levelRequired) / 2d) + (itemBonus / 10d);
		return chance >= (new Random().nextDouble() * 100.0);
	}

	public static boolean hasRequiredLevel(final Player c, int skillId, int lvlReq, String event) {
		if (c.getVariables().playerLevel[skillId] < lvlReq) {
			c.sendMessage("You need at least " + lvlReq + " " + skillNames[skillId] + " to " + event + ".");
			c.getPA().resetPlayerSkillVariables();
			return false;
		}
		return true;
	}

	public static boolean hasRequiredLevel(final Player c, int id, int lvlReq, String skill, String event) {
		if (c.getVariables().playerLevel[id] < lvlReq) {
			c.sendMessage("You haven't got high enough " + skill + " level to " + event + "");
			c.sendMessage("You at least need the " + skill + " level of " + lvlReq + ".");
			return false;
		}
		return true;
	}

	public static boolean checkChance(int itemBonus, int levelReq, int level) {
		final int random = Misc.random(100);
		int chance = 20;
		chance += level - levelReq;
		chance += itemBonus;
		chance = chance > 95 ? 95 : chance < 5 ? 5 : chance;
		return chance > random;
	}

	public static boolean playerIsBusy(final Player c) {
		SkillHandler handler = new SkillHandler();
		if (handler.playerIsBusy) {
			c.sendMessage("You are doing something else.");
			return true;
		}
		return false;
	}

	public static void setBusy(boolean status) {
		SkillHandler handler = new SkillHandler();
		handler.playerIsBusy = status;
	}

	public boolean playerIsBusy = false;

}