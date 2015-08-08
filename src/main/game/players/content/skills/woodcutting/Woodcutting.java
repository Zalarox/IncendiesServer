package main.game.players.content.skills.woodcutting;

import java.util.HashMap;

import main.Constants;
import main.GameEngine;
import main.event.CycleEvent;
import main.event.CycleEventContainer;
import main.event.CycleEventHandler;
import main.game.objects.Objects;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.game.players.content.InfernoAdze;
import main.handlers.ItemHandler;
import main.handlers.SkillHandler;
import main.util.Misc;
import main.world.ObjectHandler;

/**
 * 
 * @author Thock & Tringan & James
 *
 */
public class Woodcutting extends SkillHandler {

	private final static int WOODCUTTING_XP = SkillHandler.XPRates.WOODCUTTING.getXPRate();

	public static final int[] COMMON_SEEDS = { 5312, 5283, 5284, 5285, 5286, 5313 };
	public static final int[] UNCOMMON_SEEDS = { 5314, 5288, 5287, 5315, 5289 };
	public static final int[] RARE_SEEDS = { 5316, 5290 };
	public static final int[] VERY_RARE_SEEDS = { 5317 };

	public static final int[] COMMON_RING = { 1635, 1637 };
	public static final int[] UNCOMMON_RING = { 1639 };
	public static final int[] RARE_RING = { 1641 };
	public static final int[] VERY_RARE_RING = { 1643 };

	public enum Trees {
		NORMAL(new int[] { 1276, 1277, 1278, 1279, 1280, 1282, 1283, 1284, 1285, 1286, 1289, 1290, 1291, 1315, 1316,
				1318, 1319, 1330, 1331, 1332, 1333, 1365, 1383, 1384, 2409, 3033, 3034, 3035, 3036, 3881, 3882, 3883,
				5902, 5903, 5904 }, 1, 25, 1511, 1342, 75,
				100), OAK(new int[] { 1281, 2037 }, 15, 37.5, 1521, 1356, 14, 25), WILLOW(
						new int[] { 1308, 5551, 5552, 5553 }, 30, 67.5, 1519, 7399, 14,
						5), MAPLE(new int[] { 1307, 4677 }, 45, 100, 1517, 1343, 59, 15), YEW(new int[] { 1309 }, 60,
								175, 1515, 7402, 100, 5), MAGIC(new int[] { 1306 }, 75, 250, 1513, 7401, 125, 3), ACHEY(
										new int[] { 2023 }, 1, 25, 2862, 3371, 75,
										100), MAHOGANY(new int[] { 9034 }, 50, 125, 6332, 9035, 80, 10), ARCTIC(
												new int[] { 21273 }, 55, 175, 10810, 21274, 80,
												6), TEAK(new int[] { 9036 }, 35, 85, 6333, 9037, 14, 20), HOLLOW(
														new int[] { 2289, 4060 }, 45, 83, 3239, 2310, 59,
														15), DRAWMEN(new int[] { 1292 }, 36, 0, 771, 1513, 59, 100);
		// VINES(new int[]{5107}, 1, 0, -1, -1, 30, 100);

		private int[] objectId;
		private int reqLvl, logId, stumpId, respawnTime, decayChance;
		private double xp;

		private Trees(int[] objectId, int reqLvl, double xp, int logId, int stumpId, int respawnTime, int decayChance) {
			this.objectId = objectId;
			this.reqLvl = reqLvl;
			this.xp = xp;
			this.logId = logId;
			this.stumpId = stumpId;
			this.respawnTime = respawnTime;
			this.decayChance = decayChance;
		}

		private static HashMap<Integer, Trees> trees = new HashMap<Integer, Trees>();

		static {
			for (Trees t : Trees.values()) {
				for (int i : t.objectId) {
					trees.put(i, t);
				}
			}
		}

		public static Trees getTree(int id) {
			return trees.get(id);
		}

		public int[] getObjectId() {
			return objectId;
		}

		public int getReqLvl() {
			return reqLvl;
		}

		public double getXp() {
			return xp;
		}

		public int getLogId() {
			return logId;
		}

		public int getStumpId() {
			return stumpId;
		}

		public int getRespawnTime() {
			return respawnTime;
		}

		public int getDecayChance() {
			return decayChance;
		}
	}

	public static boolean chopTree(final Player c, final int tree, final int obX, final int obY) {
		c.turnPlayerTo(obX, obY);
		final Trees Tree = Trees.getTree(tree);

		if (Tree == null)
			return false;

		if (!noInventorySpace(c, "woodcutting")) {
			resetWoodcutting(c);
			return false;
		}
		if (hasAxe(c) && !canUseAxe(c)) {
			c.sendMessage("Your Woodcutting level is too low to use this axe.");
			return false;
		}
		if (!hasAxe(c)) {
			c.sendMessage("You need an axe to chop this.");
			return false;
		}

		if (c.getInstance().playerIsWoodcutting) {
			c.getInstance().playerIsWoodcutting = false;
			return false;
		}

		c.getInstance().playerIsWoodcutting = true;
		c.getInstance().stopPlayerSkill = true;

		c.getInstance().playerSkillProp[8][1] = Tree.getRespawnTime(); 
		c.getInstance().playerSkillProp[8][2] = Tree.getReqLvl();
		c.getInstance().playerSkillProp[8][3] = (int) Tree.getXp();
		c.getInstance().playerSkillProp[8][4] = getAnimId(c);
		c.getInstance().playerSkillProp[8][5] = Tree.getRespawnTime();
		c.getInstance().playerSkillProp[8][6] = Tree.getLogId();

		c.getInstance().woodcuttingTree = obX + obY;

		if (!hasRequiredLevel(c, 8, c.getInstance().playerSkillProp[8][2], "woodcutting", "cut this tree")) {
			resetWoodcutting(c);
			return false;
		}

		c.startAnimation(c.getInstance().playerSkillProp[8][4]);

		if (Tree != null) {
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (!noInventorySpace(c, "woodcutting") || !c.getInstance().stopPlayerSkill
							|| !c.getInstance().playerIsWoodcutting
							|| !Constants.goodDistance(obX, obY, c.absX, c.absY, 3)) {
						container.stop();
					}
					if (c.getInstance().playerSkillProp[8][6] > 0) {
						if (c.getInstance().playerEquipment[c.getInstance().playerWeapon] == InfernoAdze.itemID)
							c.getAdze().cutAndFire(c, c.getInstance().playerSkillProp[8][6]);
						else
							c.getItems().addItem(c.getInstance().playerSkillProp[8][6], 1);
					}
					if (c.getInstance().playerSkillProp[8][3] > 0) {
						c.getPA().addSkillXP(c.getInstance().playerSkillProp[8][3] * WOODCUTTING_XP, 8);
					}
					if (c.getInstance().playerSkillProp[8][6] > 0) {
						c.sendMessage("You get some " + c.getItems().getItemName(c.getInstance().playerSkillProp[8][6])
								+ ".");
					}
					if (Misc.random(100) == 0) {
						recieveBirdsNest(c);
					}
					if (!hasAxe(c)) {
						c.sendMessage("You need an axe which you have the Woodcutting level to use.");
						container.stop();
					}
					if ((tree != 1292 && Misc.random(100) <= Tree.getDecayChance())
							&& c.getInstance().playerIsWoodcutting) {
						createStump(c, tree, obX, obY);
						container.stop();
					}
				}

				@Override
				public void stop() {
					resetWoodcutting(c);
				}
			}, (Misc.random(5) + getAxeTime(c) + (10 - (int) Math.floor(c.getInstance().playerLevel[8] / 10))));
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (!c.getInstance().stopPlayerSkill) {
						resetWoodcutting(c);
						container.stop();
					}
					if (c.getInstance().playerSkillProp[8][4] > 0 && c.getInstance().playerIsWoodcutting) {
						c.startAnimation(c.getInstance().playerSkillProp[8][4]);
					}
				}

				@Override
				public void stop() {

				}
			}, 4);
		}
		return true;
	}

	private static void treeLocated(Player c, int obX, int obY) {
		for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				Player person = PlayerHandler.players[i];
				if (person != null) {
					if (person.distanceToPoint(c.absX, c.absY) <= 10) {
						if (c.getInstance().woodcuttingTree == person.getInstance().woodcuttingTree) {
							person.getInstance().woodcuttingTree = -1;
							resetWoodcutting(person);
						}
					}
				}
			}
		}
	}

	public static void createStump(final Player c, final int tree, final int obX, final int obY) {
		final Trees Tree = Trees.getTree(tree);
		Objects t = null;
		for (Objects o : ObjectHandler.globalObjects) {
			if (o.objectId == tree && o.objectX == obX && o.objectY == obY) {
				t = o;
			}
		}
		GameEngine.objectHandler.placeObject(new Objects(Tree.getStumpId(), obX, obY, c.heightLevel,
				t != null ? t.getObjectFace() : 0, t != null ? t.getObjectType() : 10));
		treeLocated(c, obX, obY);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				container.stop();
			}

			@Override
			public void stop() {
				GameEngine.objectHandler.placeObject(new Objects(tree, obX, obY, c.heightLevel, 0, 10));
				resetWoodcutting(c);
			}
		}, Tree.getRespawnTime());
	}

	public static void recieveBirdsNest(Player c) {
		ItemHandler.createGroundItem(c, 5070 + Misc.random(4), c.getX(), c.getY(), c.heightLevel, 1, c.getId());
		c.sendMessage("A bird's nest falls out of the tree!");
	}

	public static void resetWoodcutting(Player c) {
		c.startAnimation(65535);
		c.getPA().removeAllWindows();
		c.getInstance().playerIsWoodcutting = false;
		for (int i = 0; i < 9; i++) {
			c.getInstance().playerSkillProp[8][i] = -1;
		}
	}

	private static boolean hasAxe(Player c) {
		boolean has = false;
		for (int i = 0; i < axes.length; i++) {
			if (c.getItems().playerHasItem(axes[i][0]) || c.getInstance().playerEquipment[3] == axes[i][0]) {
				has = true;
			}
		}
		return has;
	}

	private static int getAxeTime(Player c) {
		int axe = -1;
		for (int i = 0; i < axes.length; i++) {
			if (c.getInstance().playerLevel[8] >= axes[i][1]) {
				if (c.getItems().playerHasItem(axes[i][0]) || c.getInstance().playerEquipment[3] == axes[i][0]) {
					axe = axes[i][3];
				}
			}
		}
		return axe;
	}

	private static int getAnimId(Player c) {
		int anim = -1;
		for (int i = 0; i < axes.length; i++) {
			if (c.getInstance().playerLevel[8] >= axes[i][1]) {
				if (c.getItems().playerHasItem(axes[i][0]) || c.getInstance().playerEquipment[3] == axes[i][0]) {
					anim = axes[i][2];
				}
			}
		}
		return anim;
	}

	private static boolean canUseAxe(Player c) {
		if ((performCheck(c, 1349, 1) || performCheck(c, 1351, 1) || performCheck(c, 1353, 6)
				|| performCheck(c, 1361, 6) || performCheck(c, 1355, 21) || performCheck(c, 1357, 31)
				|| performCheck(c, 1359, 41) || performCheck(c, 6739, 61))
				&& !(c.getInstance().playerEquipment[c.getInstance().playerWeapon] == InfernoAdze.itemID)) {
			return true;
		}
		if (c.getInstance().playerEquipment[c.getInstance().playerWeapon] == InfernoAdze.itemID) {
			if (c.getAdze().getRequirements(c))
				return true;
		}
		return false;
	}

	private static boolean performCheck(Player c, int i, int l) {
		return (c.getItems().playerHasItem(i) || c.getInstance().playerEquipment[3] == i)
				&& c.getInstance().playerLevel[8] >= l;
	}

	private static int[][] axes = { { 1351, 1, 879, 10 }, { 1349, 1, 877, 10 }, { 1353, 6, 875, 10 },
			{ 1361, 6, 873, 9 }, { 1355, 21, 871, 8 }, { 1357, 31, 869, 7 }, { 1359, 41, 867, 5 },
			{ 6739, 61, 2846, 4 }, { InfernoAdze.itemID, 61, 10251, 4 }, };

	public static boolean handleNest(Player c, int itemId) {
		int[] commonItems, uncommonItems, rareItems, veryRareItems;
		switch (itemId) {
		case 5070:
			c.getItems().deleteItem(itemId, 1);
			c.getItems().addItem(5075, 1);
			c.getItems().addItem(5076, 1);
			return true;
		case 5071:
			c.getItems().deleteItem(itemId, 1);
			c.getItems().addItem(5075, 1);
			c.getItems().addItem(5078, 1);
			return true;
		case 5072:
			c.getItems().deleteItem(itemId, 1);
			c.getItems().addItem(5075, 1);
			c.getItems().addItem(5077, 1);
			return true;
		case 5073:
			commonItems = COMMON_SEEDS;
			uncommonItems = UNCOMMON_SEEDS;
			rareItems = RARE_SEEDS;
			veryRareItems = VERY_RARE_SEEDS;
			break;
		case 5074:
			commonItems = COMMON_RING;
			uncommonItems = UNCOMMON_RING;
			rareItems = RARE_RING;
			veryRareItems = VERY_RARE_RING;
			break;
		default:
			return false;
		}
		int randomNumber = Misc.random(100), finalItem;
		if (randomNumber <= 60)
			finalItem = commonItems[Misc.random(commonItems.length - 1)];
		else if (randomNumber <= 80)
			finalItem = uncommonItems[Misc.random(uncommonItems.length - 1)];
		else if (randomNumber <= 95)
			finalItem = rareItems[Misc.random(rareItems.length - 1)];
		else
			finalItem = veryRareItems[Misc.random(veryRareItems.length - 1)];

		c.sendMessage("You search the nest...and find something in it!");
		c.getItems().deleteItem(itemId, 1);
		c.getItems().addItem(5075, 1);
		c.getItems().addItem(finalItem, 1);
		return true;
	}

}