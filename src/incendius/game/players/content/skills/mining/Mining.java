package incendius.game.players.content.skills.mining;

import java.util.HashMap;

import incendius.Constants;
import incendius.Server;
import incendius.event.CycleEvent;
import incendius.event.CycleEventContainer;
import incendius.event.CycleEventHandler;
import incendius.game.objects.Objects;
import incendius.game.players.Player;
import incendius.game.players.content.InfernoAdze;
import incendius.handlers.SkillHandler;
import incendius.util.Misc;
import incendius.world.ObjectHandler;

public class Mining extends SkillHandler {

	private enum Rocks {
		COPPER(new int[] { 31082, 31080, 31081, 2090, 2091, 3042, 9708, 9709, 9710, 11936, 11937, 11938, 11960, 11961,
				11962 }, 436, 1,
				2000,
				7), TIN(new int[] { 31078, 31081, 31079, 31077, 2094, 2095, 3043, 9714, 9715, 9716, 11933, 11934, 11935,
						11957, 11958, 11959 }, 438, 1, 2000,
						7), IRON(new int[] { 31073, 31071, 31072, 2092, 2093, 9717, 9718, 9719, 11954, 11955, 11956 },
								440, 15, 4000, 12), COAL(
										new int[] { 14832, 2096, 2097, 10948, 11930, 11931, 11932, 11963, 11964, 11965,
												31069, 31068, 31070 },
										453, 30, 5000, 15), GEM(new int[] { 2111 }, -1, 40, 65, 50), SANDSTONE(
												new int[] { 10946 }, -1, 35, 0,
												15), GRANITE(new int[] { 10947 }, -1, 45, 0, 15), GOLD(new int[] {
														31065, 31066, 2098, 2099, 9720, 9721, 9722, 11951, 11952,
														11953 }, 444, 40, 6000,
														20), SILVER(new int[] { 2100, 2101, 11948, 11949, 11950 }, 442,
																20, 4500,
																15), MITHRIL(new int[] { 31088, 2102, 2103, 11942,
																		11943, 11944, 11945, 11946, 11947, 31086,
																		31087 }, 447, 55,
																		8500,
																		30), ADAMANTITE(new int[] { 31083, 31085, 2104,
																				2105, 11939, 11940, 11941 }, 449, 70,
																				10000,
																				60), RUNITE(new int[] { 2106, 2107,
																						14859, 14860, 14861 }, 451, 85,
																						12500,
																						90), CLAY(new int[] { 2108,
																								15504, 15503, 15505,
																								2109, 9711, 9712, 9713,
																								10949 }, 434, 1,
																								2000,
																								3), EMPTY(new int[] {
																										10944, 9723,
																										9724, 9725,
																										11555, 11552,
																										11553, 11554,
																										11557, 11556,
																										450, 451 }, 0,
																										0, 0, 0);

		private int[] objectIds;
		private int oreItemId, levelReq, respawnTimer;
		private double xp;

		private Rocks(int[] objectIds, int oreItemId, int levelReq, double xp, int respawnTimer) {
			this.objectIds = objectIds;
			this.oreItemId = oreItemId;
			this.levelReq = levelReq;
			this.xp = xp;
			this.respawnTimer = respawnTimer;
		}

		private static HashMap<Integer, Rocks> rocks = new HashMap<Integer, Rocks>();

		static {
			for (Rocks r : Rocks.values()) {
				for (int i : r.objectIds)
					rocks.put(i, r);
			}
		}

		private static Rocks getRock(int objectId) {
			return rocks.get(objectId);
		}
	}

	private enum PickAxes {
		INFERNO_ADZE(InfernoAdze.itemID, 41, 4, 10222, 3), DRAGON(15259, 61, 6, 12188, 3), RUNE(1275, 41, 4, 624,
				3), ADAMANT(1271, 31, 2, 628, 4), MITHRIL(1273, 21, 2, 629, 6), STEEL(1269, 6, 1, 627, 7), IRON(1267, 1,
						0, 626, 9), BRONZE(1265, 1, 0, 625, 11);

		private int itemId, levelReq, bonus, anim, essMineCycles;

		private PickAxes(int itemId, int levelReq, int bonus, int anim, int essMineCycles) {
			this.itemId = itemId;
			this.levelReq = levelReq;
			this.bonus = bonus;
			this.anim = anim;
			this.essMineCycles = essMineCycles;
		}

		public int getAnim() {
			return anim;
		}

	}

	public static boolean hasPick(Player c) {
		for (PickAxes p : PickAxes.values()) {
			if (c.getInstance().playerEquipment[c.getInstance().playerWeapon] == p.itemId
					|| c.getItems().playerHasItem(p.itemId))
				return true;
		}
		return false;
	}

	private static boolean canMine(Player player, int objectId) {
		if (Rocks.getRock(objectId) == null) {
			return false;
		}
		if (player.getItems().freeSlots() == 0) {
			return false;
		}
		if (!hasPick(player)) {
			return false;
		}
		if (player.getInstance().playerEquipment[player.getInstance().playerWeapon] == InfernoAdze.itemID) {
			if (!player.getAdze().getRequirements(player))
				return false;
		}
		if (player.getInstance().playerLevel[Constants.MINING] < Rocks.getRock(objectId).levelReq
				&& !(player.getInstance().playerEquipment[player.getInstance().playerWeapon] == InfernoAdze.itemID)) {
			return false;
		}
		return true;
	}

	public static boolean mineOre(final Player player, final int objectId, final int obX, final int obY) {
		final Rocks r = Rocks.getRock(objectId);
		if (!canMine(player, objectId) || player == null || r == null
				|| player.getInstance().playerSkilling[Constants.MINING])
			return false;
		PickAxes pick = null;
		for (PickAxes o : PickAxes.values()) {
			if (player.getInstance().playerEquipment[player.getInstance().playerWeapon] == o.itemId
					|| player.getItems().playerHasItem(o.itemId)) {
				if (player.getInstance().playerLevel[Constants.MINING] >= o.levelReq)
					pick = o;
			}
		}
		final PickAxes p = pick;
		if (p == null) {
			player.sendMessage("You do not have a pickaxe in which you have the correct mining level to use.");
			return false;
		}
		if (r.name().equalsIgnoreCase("empty")) {
			player.sendMessage("This rock has run out of ore!");
			return false;
		}
		player.sendMessage("You swing your pick at the rock.");
		player.getPA().resetSkills();
		player.startAnimation(p.getAnim());
		player.getInstance().playerSkilling[Constants.MINING] = true;
		CycleEventHandler.getSingleton().addEvent(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				if (!player.getInstance().playerSkilling[Constants.MINING] || !canMine(player, objectId))
					container.stop();
				if (SkillHandler.skillCheck(player.getInstance().playerLevel[Constants.MINING], r.levelReq,
						p.bonus * 20)) {
					player.getItems().addItem(getItemReceived(objectId, r.oreItemId), 1);
					player.sendMessage(
							"You manage to mine some " + player.getItems().getItemName(r.oreItemId).toLowerCase());
					player.getPA().addSkillXP(getExp(getItemReceived(objectId, r.oreItemId), r.xp), Constants.MINING);
					Objects ob = null;
					for (Objects o : ObjectHandler.globalObjects) {
						if (o.objectId == objectId && o.objectX == obX && o.objectY == obY
								&& o.objectHeight == player.heightLevel) {
							ob = o;
						}
					}
					final Objects obj = ob;
					Server.objectHandler.placeObject(new Objects(emptyOre(objectId), obX, obY, player.heightLevel,
							obj != null ? obj.objectFace : 0, 10));
					CycleEventHandler.getInstance().addEvent(new CycleEvent() {

						@Override
						public void execute(CycleEventContainer container) {
							container.stop();
						}

						@Override
						public void stop() {
							Server.objectHandler.placeObject(new Objects(objectId, obX, obY, player.heightLevel,
									obj != null ? obj.objectFace : 0, 10));
						}

					}, r.respawnTimer);
					container.stop();
				}

			}

			@Override
			public void stop() {
				resetMining(player);
			}

		}, 1);
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (player.getInstance().playerSkilling[Constants.MINING]) {
					player.startAnimation(p.anim);
				}
				if (!player.getInstance().playerSkilling[Constants.MINING]) {
					resetMining(player);
					container.stop();
				}
			}

			@Override
			public void stop() {

			}
		}, 9);
		return true;
	}

	private static void resetMining(Player player) {
		player.getPA().resetSkills();
		player.startAnimation(65535);
	}

	private static double getExp(int itemReceived, double xp) {
		switch (itemReceived) {
		// granite
		case 6979:// 2kg
			return 50;
		case 6981:// 5kg
			return 60;
		case 6983: // 5kg
			return 75;
		// sandstone
		case 6971:// 1kg
			return 30;
		case 6973:// 2kg
			return 40;
		case 6975:// 5kg
			return 50;
		case 6977:// 10kg
			return 60;
		}
		return xp;
	}

	public static final int[] normalGems = { 1623, 1621, 1619, 1617 };

	public static final int[] specialGems = { 1625, 1627, 1629, 1625, 1627, 1629 };

	public static final int[] granites = { 6981, 6979, 6983 };

	public static final int[] sandstone = { 6977, 6971, 6975, 6973 };

	public static int getItemReceived(int object, int itemReceived) {
		switch (object) {
		case 0: // random gem event
			int gem = 0;
			while (true) {
				if (Misc.random(9) < 7) {
					return normalGems[gem];
				} else {
					gem++;
					if (gem > normalGems.length) {
						return normalGems[0];
					}
				}
			}
		case 2111:
			int gem1 = 0;
			while (true) {
				if (Misc.random(9) < 7) {
					return specialGems[gem1];
				} else {
					gem1++;
					if (gem1 > specialGems.length) {
						return specialGems[0];
					}
				}
			}
		case 10947:
			return granites[Misc.random(granites.length - 1)];
		case 10946:
			return sandstone[Misc.random(sandstone.length - 1)];
		}
		return itemReceived;
	}

	private static int emptyOre(int object) {
		int[] ore1 = { 9708, 9711, 9714, 9717, 9720 };
		int[] ore2 = { 9709, 9712, 9715, 9718, 9721 };
		int[] ore3 = { 9710, 9713, 9716, 9719, 9722 };

		int[] ore4 = { 11930, 11933, 11936, 11939, 11942, 11945, 11948, 11951, 11954, 11957, 11960, 11963 };
		int[] ore5 = { 11931, 11934, 11937, 11940, 11943, 11946, 11949, 11952, 11955, 11958, 11961, 11964 };
		int[] ore6 = { 11932, 11935, 11938, 11941, 11944, 11947, 11950, 11953, 11956, 11959, 11962, 11965 };

		int[] ore7 = { 14859 };
		int[] ore8 = { 14860 };
		int[] ore9 = { 14861 };
		if (object == 10946 || object == 10948) {
			return 10944;
		}
		if (object == 10947 || object == 10949) {
			return 10945;
		}
		for (int i : ore1) {
			if (object == i) {
				return 9723;
			}
		}
		for (int i : ore2) {
			if (object == i) {
				return 9724;
			}
		}
		for (int i : ore3) {
			if (object == i) {
				return 9725;
			}
		}
		for (int i : ore4) {
			if (object == i) {
				return object >= 11945 ? 11555 : 11552;
			}
		}
		for (int i : ore5) {
			if (object == i) {
				return object >= 11945 ? 11556 : 11553;
			}
		}
		for (int i : ore6) {
			if (object == i) {
				return object >= 11945 ? 11557 : 11554;
			}
		}
		for (int i : ore7) {
			if (object == i) {
				return 14832;
			}
		}
		for (int i : ore8) {
			if (object == i) {
				return 14833;
			}
		}
		for (int i : ore9) {
			if (object == i) {
				return 14834;
			}
		}
		if (object % 2 == 0 || object == 3043) {
			return 450;
		} else {
			return 451;
		}
	}

	public static int getAnimation(Player c) {
		for (PickAxes p : PickAxes.values()) {
			if (c.getInstance().playerEquipment[c.getInstance().playerWeapon] == p.itemId
					|| c.getItems().playerHasItem(p.itemId)) {
				if (c.getInstance().playerLevel[Constants.MINING] >= p.levelReq)
					return p.anim;
			}
		}
		return -1;
	}

	public static void mineEss(final Player c) {
		if (c.getItems().freeSlots() == 0 || c.getInstance().playerSkilling[Constants.MINING])
			return;
		if (!hasPick(c))
			return;
		PickAxes q = null;
		for (PickAxes o : PickAxes.values()) {
			if (c.getInstance().playerEquipment[c.getInstance().playerWeapon] == o.itemId
					|| c.getItems().playerHasItem(o.itemId)) {
				if (c.getInstance().playerLevel[Constants.MINING] >= o.levelReq)
					q = o;
			}
		}
		final PickAxes p = q;
		if (p == null) {
			c.sendMessage("You do not have a pickaxe in which you have the correct mining level to use.");
			return;
		}
		c.getPA().resetSkills();
		final int essenceMined = c.getInstance().playerLevel[Constants.MINING] < 30 ? 1436 : 7936;
		c.sendMessage("You swing your pick at the rock.");
		c.getInstance().playerSkilling[Constants.MINING] = true;
		c.startAnimation(p.anim);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			int timer = p.essMineCycles;

			@Override
			public void execute(CycleEventContainer container) {
				if (!c.getInstance().playerSkilling[Constants.MINING] || c.getItems().freeSlots() == 0)
					container.stop();
				if (timer > 0)
					timer--;
				else {
					c.getItems().addItem(essenceMined, 1);
					c.getPA().addSkillXP(5, Constants.MINING);
					timer = p.essMineCycles;
				}
			}

			@Override
			public void stop() {

			}

		}, 1);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (c.getInstance().playerSkilling[Constants.MINING]) {
					c.startAnimation(p.anim);
				}
				if (!c.getInstance().playerSkilling[Constants.MINING]) {
					resetMining(c);
					container.stop();
				}
			}

			@Override
			public void stop() {

			}
		}, 15);
	}
}