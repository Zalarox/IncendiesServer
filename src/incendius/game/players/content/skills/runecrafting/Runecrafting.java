package incendius.game.players.content.skills.runecrafting;

import java.awt.Point;

import incendius.Constants;
import incendius.Server;
import incendius.event.Task;
import incendius.game.items.Item;
import incendius.game.npcs.NPC;
import incendius.game.players.Player;
import incendius.handlers.Skill;
import incendius.handlers.SkillHandler;

/**
 * Runecrafting class
 */

public class Runecrafting {
	// TODO
	// http://www.rune-server.org/runescape-development/rs2-server/tutorials/471275-pi-100-runecrafting-skill-training-rcr-mixing-abyss-pouches-tiara-effects.html
	// DO IT 100%
	// tali id, tiara id, ruin object id, x coord next to ruin, y coord next to
	// ruin, rune altar, rune altar portal, x coord inside rune altar, y coord
	// inside rune altar
	public final static int RC_DATA[][] = { { 1438, 5527, 2452, 2987, 3292, 2478, 2465, 2844, 4830 }, // air
			{ 1448, 5529, 2453, 2982, 3512, 2479, 2466, 2782, 4841 }, // mind
			{ 1444, 5531, 2454, 3183, 3165, 2480, 2467, 2712, 4836 }, // water
			{ 1440, 5535, 2455, 3304, 3474, 2481, 2468, 2654, 4841 }, // earth
			{ 1442, 5537, 2456, 3312, 3253, 2482, 2469, 2585, 4834 }, // fire
			{ 1446, 5533, 2457, 3051, 3445, 2483, 2470, 2525, 4828 }, // body
			{ 1454, 5539, 2458, 2407, 4379, 2484, 2471, 2138, 4833 }, // cosmic
			{ 1452, 5543, 2461, 3062, 3591, 2487, 2474, 2267, 4842 }, // chaos
			{ 1462, 5541, 2460, 2868, 3017, 2486, 2473, 2396, 4841 }, // nature
			{ 1458, 5545, 2459, 2859, 3379, 2485, 2472, 2464, 4827 }, // law
			{ 1456, 5547, 2462, 1862, 4639, 2488, 2475, 2207, 4834 }, // death
			// {1450, 5549, -1, -1, -1, -1, -1, -1, -1,}, // blood
			// {1460, 5551 - 1, -1, -1, -1, -1, -1, -1,}, // soul
	};

	public final static int RuneEss = 1436;
	public final static int PureEss = 7936;
	public final Point[] RUNE_ESSENCE_LOCATIONS = { new Point(2899, 4818), new Point(2910, 4832), new Point(2898, 4844),
			new Point(2921, 4845), new Point(2924, 4818), };

	public static enum Runes {
		FIRE(554, 14, 550, 2482, 35), WATER(555, 5, 450, 2480, 19), AIR(556, 1, 300, 2478, 11), EARTH(557, 9, 500, 2481,
				26), MIND(558, 2, 400, 2479, 14), BODY(559, 20, 600, 2483, 46), DEATH(560, 65, 1500, 2488, -1), NATURE(
						561, 44, 900, 2486, 91), CHAOS(562, 35, 750, 2487, 74), LAW(563, 54, 1000, 2485, -1), COSMIC(
								564, 27, 650, 2484, 59), BLOOD(565, 77, 2000, 7141, -1), SOUL(566, 88, 320, 7138, -1);

		private int id;
		private int level;
		private int xp;
		private int altar;
		private int multiplier;

		private Runes(int id, int level, int xp, int altar, int multiplier) {
			this.id = id;
			this.level = level;
			this.xp = xp;
			this.altar = altar;
			this.multiplier = multiplier;
		}

		public int getId() {
			return id;
		}

		public int getLevel() {
			return level;
		}

		public int getXp() {
			return xp;
		}

		public int getAltar() {
			return altar;
		}

		public int getMultiplier() {
			return multiplier;
		}

	}

	public static void craftRunes(Player player, Runes rune) { // TODO - fix
																// pure essence
																// only making 1
																// at a time
		if (!Constants.RUNECRAFTING_ENABLED) {
			player.getPA().sendMessage("This skill is currently disabled.");
			return;
		}
		if (!SkillHandler.hasRequiredLevel(player, Skill.RUNECRAFTING, rune.getLevel(), "craft this rune")) {
			return;
		}
		int runeAmount = player.getInventory().getItemAmount(RuneEss);
		int pureAmount = player.getInventory().getItemAmount(PureEss);
		double multiplier = rune.getMultiplier() < 0 ? 0
				: player.getSkill().getClientLevel(Skill.RUNECRAFTING) / rune.getMultiplier();
		int realMultiplier = (int) Math.floor(multiplier) + 1;
		if (rune.getId() >= 560) {
			if (pureAmount < 1) {
				player.getPA().sendMessage("You need pure essence to make these kinds of runes.");
				return;
			}
			for (int p = 0; p < 28; p++) {
				player.getInventory().removeItem(new Item(PureEss, pureAmount));
			}
			player.getInventory().addItem(new Item(rune.getId(), pureAmount * realMultiplier));
			player.getSkill().addExp(Skill.RUNECRAFTING, rune.getXp() * pureAmount);
		} else {
			if (pureAmount < 1 && runeAmount < 1) {
				player.getPA().sendMessage("You need rune or pure essence to make these kinds of runes.");
				return;
			}
			for (int p = 0; p < 28; p++) {
				player.getInventory().removeItem(new Item(RuneEss, runeAmount));
				player.getInventory().removeItem(new Item(PureEss, pureAmount));
			}
			player.getInventory().addItem(new Item(rune.getId(), runeAmount * realMultiplier));
			player.getInventory().addItem(new Item(rune.getId(), pureAmount * realMultiplier));
			player.getSkill().addExp(Skill.RUNECRAFTING, rune.getXp() * runeAmount);
			player.getSkill().addExp(Skill.RUNECRAFTING, rune.getXp() * pureAmount);
		}
		player.getUpdateFlags().sendAnimation(791);
		player.getUpdateFlags().gfx100(186);
	}

	public static void teleportRunecraft(final Player player, final NPC npc) {

		if (npc != null) {
			npc.forceChat("Senventior disthine molenko!");
			npc.sendAnimation(1818);
			npc.gfx0(343);
		}
		Server.getScheduler().schedule(new Task(3) {
			@Override
			public void execute() {
				player.getPA().startTeleport2(2911, 4832, 0);
				this.stop();
			}
		});
	}

	public static boolean clickTalisman(Player player, int itemId) {
		for (int[] data : RC_DATA) {
			if (data[0] == itemId) {
				locate(player, data[3], data[4]);
				return true;
			}
		}
		return false;
	}

	public static void locate(Player player, int xPos, int yPos) {
		String X = "";
		String Y = "";
		if (player.absX >= xPos) {
			X = "west";
		}
		if (player.absY > yPos) {
			Y = "South";
		}
		if (player.absX < xPos) {
			X = "east";
		}
		if (player.absY <= yPos) {
			Y = "North";
		}
		player.getPA().sendMessage("You feel a slight pull towards " + Y + "-" + X + "...");
	}

}