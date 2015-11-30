package incendius.game.players.content.skills.fishing;

import incendius.event.CycleEvent;
import incendius.event.CycleEventContainer;
import incendius.event.CycleEventHandler;
import incendius.game.players.Player;
import incendius.util.Misc;

public class Fishing {

	private final static int FISHING_XP = 75;

	private final static int[][] data = { { 1, 1, 303, -1, 317, 10, 621, 321, 15, 30 }, // SHRIMP
																						// +
																						// ANCHOVIES
			{ 2, 5, 309, 313, 327, 20, 622, 345, 10, 30 }, // SARDINE + HERRING
			{ 3, 16, 305, -1, 353, 20, 620, -1, -1, 20 }, // MACKEREL
			{ 4, 20, 309, -1, 335, 50, 622, 331, 30, 70 }, // TROUT
			{ 5, 23, 305, -1, 341, 45, 619, 363, 46, 100 }, // BASS + COD
			{ 6, 25, 309, 313, 349, 60, 622, -1, -1, 60 }, // PIKE
			{ 7, 35, 311, -1, 359, 80, 618, 371, 50, 100 }, // TUNA + SWORDIE
			{ 8, 40, 301, -1, 377, 90, 619, -1, -1, 90 }, // LOBSTER
			{ 9, 62, 305, -1, 7944, 120, 620, 389, 81, 46 }, // MONKFISH
			{ 10, 76, 311, -1, 383, 110, 618, -1, -1, 110 }, // SHARK
			{ 11, 79, 305, -1, 395, 38, 619, -1, -1, 140 }, // SEA TURTLE
			{ 12, 81, 305, -1, 389, 46, 621, -1, -1, 180 }, // MANTA RAY
			{ 13, 90, 309, 313, 15270, 300, 622, -1, -1, 250 }, // ROCKTAIL
	};
	private static boolean doingLoop = false;

	private static void loopFishAnim(final Player c, final int anim) {
		if (doingLoop)
			return;
		doingLoop = true;
		c.startAnimation(anim);
		CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!c.getInstance().playerIsFishing) {
					container.stop();
				} else {
					c.startAnimation(anim);
					// c.sendMessage("Doing anim cycle");
				}
			}

			@Override
			public void stop() {
				// c.sendMessage("Stopping");
				resetFishing(c);
			}
		}, 10);
	}

	private static void attemptdata(final Player c, int npcId) {
		if (c.getInstance().fishingProp[4] > 0) {
			c.getInstance().playerIsFishing = false;
			return;
		}
		if (c.getItems().freeSlots() == 0) {
			c.sendMessage("You haven't got enough inventory space to continue fishing!");
			c.getDH().sendStatement("You haven't got enough inventory space to continue fishing!");
			resetFishing(c);
			return;
		}
		for (int i = 0; i < data.length; i++) {
			if (npcId == data[i][0]) {
				if (c.getInstance().playerLevel[c.getInstance().playerFishing] < data[i][1]) {
					c.sendMessage("You haven't got high enough fishing level to fish here!");
					c.sendMessage("You atleast need the fishing level of " + data[i][1] + ".");
					c.getDH().sendStatement("You need the fishing level of " + data[i][1] + " to fish here.");
					return;
				}
				if (!c.getItems().playerHasItem(data[i][2], 1)) {
					c.sendMessage("You need a " + c.getItems().getItemName(data[i][2]) + " to fish here.");
					return;
				}
				if (data[i][3] > 0) {
					if (!c.getItems().playerHasItem(data[i][3], 1)) {
						c.sendMessage("You haven't got any " + c.getItems().getItemName(data[i][3]) + "!");
						c.sendMessage("You need " + c.getItems().getItemName(data[i][3]) + " to fish here.");
						return;
					}
				}

				c.getInstance().fishingProp[0] = data[i][6]; // ANIM
				c.getInstance().fishingProp[1] = data[i][4]; // FISH
				c.getInstance().fishingProp[2] = data[i][5]; // XP
				c.getInstance().fishingProp[3] = data[i][3]; // BAIT
				c.getInstance().fishingProp[4] = data[i][2]; // EQUIP
				c.getInstance().fishingProp[5] = data[i][7]; // sFish
				c.getInstance().fishingProp[6] = data[i][8]; // sLvl
				c.getInstance().fishingProp[7] = data[i][4]; // FISH
				c.getInstance().fishingProp[8] = data[i][9]; // sXP

				c.getInstance().fishingProp[9] = Misc.random(1) == 0 ? 7 : 5;

				c.getInstance().fishingProp[10] = data[i][0]; // INDEX

				c.sendMessage("You start fishing.");
				// c.startAnimation(c.fishingProp[0]);
				Fishing.loopFishAnim(c, c.getInstance().fishingProp[0]);
				c.getInstance().stopPlayerSkill = true;
				if (c.getInstance().playerIsFishing) {
					return;
				}
				c.getInstance().playerIsFishing = true;
				CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						c.getInstance().fishingProp[2] = c
								.getInstance().fishingProp[c.getInstance().fishingProp[9] == 7 ? 8 : 2];
						if (c.getInstance().fishingProp[1] > 0) {
							c.sendMessage(
									"You catch a " + c.getItems().getItemName(c.getInstance().fishingProp[1]) + ".");
						}
						c.getItems().addItem(c.getInstance().fishingProp[1], 1);
						c.getPA().addSkillXP(c.getInstance().fishingProp[2] * FISHING_XP,
								c.getInstance().playerFishing);
						c.startAnimation(c.getInstance().fishingProp[0]);
						if (c.getInstance().fishingProp[3] > 0) {
							c.getItems().deleteItem(c.getInstance().fishingProp[3],
									c.getItems().getItemSlot(c.getInstance().fishingProp[3]), 1);
							if (!c.getItems().playerHasItem(c.getInstance().fishingProp[3], 1)) {
								c.sendMessage("You do not have any "
										+ c.getItems().getItemName(c.getInstance().fishingProp[3])
										+ " in your inventory.");
								c.sendMessage("You need " + c.getItems().getItemName(c.getInstance().fishingProp[3])
										+ " to fish here.");
								resetFishing(c);
								container.stop();
							}
						}
						if (c.getInstance().fishingProp[5] > 0) {
							if (c.getInstance().playerLevel[c.getInstance().playerFishing] >= c
									.getInstance().fishingProp[6]) {
								c.getInstance().fishingProp[1] = c.getInstance().fishingProp[Misc.random(1) == 0 ? 7
										: 5];
							}
						}
						if (!c.getItems().playerHasItem(c.getInstance().fishingProp[4], 1)) {
							c.sendMessage("You need a " + c.getItems().getItemName(c.getInstance().fishingProp[4])
									+ " to fish from this spot.");
							resetFishing(c);
							container.stop();
						}
						if (c.getItems().freeSlots() == 0) {
							resetFishing(c);
							c.sendMessage("Not enough inventory space.");
							c.getDH().sendStatement("Not enough inventory space.");
							container.stop();
						}
						// if (Misc.random(15) == 0) {
						// resetFishing(c);
						// container.stop();
						// }
						if (!c.getInstance().stopPlayerSkill) {
							resetFishing(c);
							container.stop();
						}
						if (!c.getInstance().playerIsFishing) {
							container.stop();
						}
					}

					@Override
					public void stop() {

					}
				}, (getTimer(c, npcId) + playerFishingLevel(c)));
			}
		}
	}

	private static void resetFishing(Player c) {
		c.startAnimation(65535);
		doingLoop = false;
		c.getPA().removeAllWindows();
		c.getInstance().playerIsFishing = false;
		for (int i = 0; i < 11; i++) {
			c.getInstance().fishingProp[i] = -1;
		}
	}

	private static int playerFishingLevel(Player c) {
		return (10 - (int) Math.floor(c.getInstance().playerLevel[c.getInstance().playerFishing] / 10));
	}

	private final static int getTimer(Player c, int npcId) {
		switch (npcId) {
		case 1:
			return 6;
		case 2:
			return 8;
		case 3:
			return 10;
		case 4:
			return 12;
		case 5:
			return 14;
		case 6:
			return 16;
		case 7:
			return 20;
		case 8:
			return 23;
		case 9:
			return 25;
		case 10:
			return 30;
		case 11:
			return 33;
		case 12:
			return 35;
		case 13:
			return 40;
		default:
			return -1;
		}
	}

	public static void fishingNPC(Player c, int i, int l) {
		switch (i) {
		case 1:
			switch (l) {
			case 319:
			case 329:
			case 323:
			case 325:
			case 326:
			case 327:
			case 330:
			case 332:
			case 316: // NET + BAIT
				Fishing.attemptdata(c, 1);
				break;
			case 334:
			case 313: // NET + HARPOON
				Fishing.attemptdata(c, 3);
				break;
			case 322: // NET + HARPOON
				Fishing.attemptdata(c, 5);
				break;

			case 309: // LURE
			case 310:
			case 311:
			case 314:
			case 315:
			case 317:
			case 318:
			case 328:
			case 331:
				Fishing.attemptdata(c, 4);
				break;

			case 312:
			case 321:
			case 324: // CAGE + HARPOON
				Fishing.attemptdata(c, 8);
				break;
			case 236:
				Fishing.attemptdata(c, 13);
				break;
			case 10091:
				Fishing.attemptdata(c, 9);
			}
			break;
		case 2:
			switch (l) {
			case 326:
			case 327:
			case 330:
			case 332:
			case 316: // BAIT + NET
				Fishing.attemptdata(c, 2);
				break;
			case 319:
			case 323:
			case 325: // BAIT + NET
				Fishing.attemptdata(c, 9);
				break;
			case 310:
			case 311:
			case 314:
			case 315:
			case 317:
			case 318:
			case 328:
			case 329:
			case 331:
			case 309: // BAIT + LURE
				Fishing.attemptdata(c, 6);
				break;
			case 312:
			case 321:
			case 324:// SWORDIES+TUNA-CAGE+HARPOON
				Fishing.attemptdata(c, 7);
				break;
			case 313:
			case 322:
			case 334: // NET+HARPOON
				Fishing.attemptdata(c, 10);
				break;
			}
			break;
		}
	}
}