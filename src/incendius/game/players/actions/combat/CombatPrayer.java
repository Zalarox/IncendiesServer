package incendius.game.players.actions.combat;

import incendius.Constants;
import incendius.game.players.Player;
import incendius.game.players.content.minigames.DuelArena;

public class CombatPrayer {

	static double[] PRAYER_DRAIN_RATE = { 0.5, // Thick Skin.
			0.5, // Burst of Strength.
			0.5, // Clarity of Thought.
			0.5, // Sharp Eye.
			0.5, // Mystic Will.
			1, // Rock Skin.
			1, // SuperHuman Strength.
			1, // Improved Reflexes.
			0.15, // Rapid restore
			0.3, // Rapid Heal.
			0.3, // Protect Items
			1, // Hawk eye.
			1, // Mystic Lore.
			2, // Steel Skin.
			2, // Ultimate Strength.
			2, // Incredible Reflexes.
			2, // Protect from Magic.
			2, // Protect from Missiles.
			2, // Protect from Melee.
			2, // Eagle Eye.
			2, // Mystic Might.
			0.5, // Retribution.
			1, // Redemption.
			2, // Smite
			2, // Chivalry.
			4, // Piety.
	};

	public static final double[] PRAYER_DRAIN_RATEa = { 1.2, 1.2, 1.2, 1.2, 1.2, 1.2, 0.6, 0.6, 0.6, 3.6, 1.8, 0.6, 0.6,
			0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 1.2, 0.6, 0.18, 0.24, 0.15, 0.2, 0.18 };

	private static final int[] PRAYER_LEVEL_REQUIRED = { 1, 4, 7, 8, 9, 10, 13, 16, 19, 22, 25, 26, 27, 28, 31, 34, 37,
			40, 43, 44, 45, 46, 49, 52, 60, 70 };
	public static final int[] PRAYER = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
			22, 23, 24, 25 };
	private static final String[] PRAYER_NAME = { "Thick Skin", "Burst of Strength", "Clarity of Thought", "Sharp Eye",
			"Mystic Will", "Rock Skin", "Superhuman Strength", "Improved Reflexes", "Rapid Restore", "Rapid Heal",
			"Protect Item", "Hawk Eye", "Mystic Lore", "Steel Skin", "Ultimate Strength", "Incredible Reflexes",
			"Protect from Magic", "Protect from Missiles", "Protect from Melee", "Eagle Eye", "Mystic Might",
			"Retribution", "Redemption", "Smite", "Chivalry", "Piety" };
	public static final int[] PRAYER_GLOW = { 83, 84, 85, 601, 602, 86, 87, 88, 89, 90, 91, 603, 604, 92, 93, 94, 95,
			96, 97, 605, 606, 98, 99, 100, 607, 608 };
	private static final int[] PRAYER_HEAD_ICONS = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 2,
			1, 0, -1, -1, 3, 5, 4, -1, -1 };

	public static void activatePrayer(Player c, int i) {
		if (c.getInstance().duelRule[DuelArena.RULE_PRAYER]) {
			for (int p = 0; p < PRAYER.length; p++) {
				c.getInstance().prayerActive[p] = false;
				c.getPA().sendFrame36(PRAYER_GLOW[p], 0);
			}
			c.sendMessage("Prayer has been disabled in this duel!");
			return;
		}
		switch (i) {
		case 24:
			if (c.getPA().getLevelForXP(c.getInstance().playerXP[5]) < 60
					|| c.getPA().getLevelForXP(c.getInstance().playerXP[1]) < 65) {
				c.getPA().sendFrame36(PRAYER_GLOW[i], 0);
				c.sendMessage("You need a Prayer level of 60 and a Defence level of 65 to use Chivalry.");
				return;
			}
			break;
		case 25:
			if (c.getPA().getLevelForXP(c.getInstance().playerXP[5]) < 70
					|| c.getPA().getLevelForXP(c.getInstance().playerXP[1]) < 70) {
				c.getPA().sendFrame36(PRAYER_GLOW[i], 0);
				c.sendMessage("You need a Prayer level of 70 and Defence level of 70 to use Piety.");
				return;
			}
			break;
		}
		if (c.getInstance().inBarbDef) {
			c.getPA().sendFrame36(PRAYER_GLOW[i], 0);
			c.sendMessage("The barbarians are strongly against the use of prayers!");
			return;
		}
		int[] defPray = { 0, 5, 13, 24, 25 };
		int[] strPray = { 1, 6, 14, 24, 25 };
		int[] atkPray = { 2, 7, 15, 24, 25 };
		int[] rangePray = { 3, 11, 19 };
		int[] magePray = { 4, 12, 20 };

		if (c.getInstance().playerLevel[5] > 0 || !Constants.PRAYER_POINTS_REQUIRED) {
			if (c.getPA().getLevelForXP(c.getInstance().playerXP[5]) >= PRAYER_LEVEL_REQUIRED[i]
					|| !Constants.PRAYER_LEVEL_REQUIRED) {
				boolean headIcon = false;
				switch (i) {
				case 0:
				case 5:
				case 13:
					if (c.getInstance().prayerActive[i] == false) {
						for (int j = 0; j < defPray.length; j++) {
							if (defPray[j] != i) {
								c.getInstance().prayerActive[defPray[j]] = false;
								c.getPA().sendFrame36(PRAYER_GLOW[defPray[j]], 0);
							}
						}
					}
					break;

				case 1:
				case 6:
				case 14:
					if (c.getInstance().prayerActive[i] == false) {
						for (int j = 0; j < strPray.length; j++) {
							if (strPray[j] != i) {
								c.getInstance().prayerActive[strPray[j]] = false;
								c.getPA().sendFrame36(PRAYER_GLOW[strPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								c.getInstance().prayerActive[rangePray[j]] = false;
								c.getPA().sendFrame36(PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								c.getInstance().prayerActive[magePray[j]] = false;
								c.getPA().sendFrame36(PRAYER_GLOW[magePray[j]], 0);
							}
						}
					}
					break;

				case 2:
				case 7:
				case 15:
					if (c.getInstance().prayerActive[i] == false) {
						for (int j = 0; j < atkPray.length; j++) {
							if (atkPray[j] != i) {
								c.getInstance().prayerActive[atkPray[j]] = false;
								c.getPA().sendFrame36(PRAYER_GLOW[atkPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								c.getInstance().prayerActive[rangePray[j]] = false;
								c.getPA().sendFrame36(PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								c.getInstance().prayerActive[magePray[j]] = false;
								c.getPA().sendFrame36(PRAYER_GLOW[magePray[j]], 0);
							}
						}
					}
					break;

				case 3:// range prays
				case 11:
				case 19:
					if (c.getInstance().prayerActive[i] == false) {
						for (int j = 0; j < atkPray.length; j++) {
							if (atkPray[j] != i) {
								c.getInstance().prayerActive[atkPray[j]] = false;
								c.getPA().sendFrame36(PRAYER_GLOW[atkPray[j]], 0);
							}
						}
						for (int j = 0; j < strPray.length; j++) {
							if (strPray[j] != i) {
								c.getInstance().prayerActive[strPray[j]] = false;
								c.getPA().sendFrame36(PRAYER_GLOW[strPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								c.getInstance().prayerActive[rangePray[j]] = false;
								c.getPA().sendFrame36(PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								c.getInstance().prayerActive[magePray[j]] = false;
								c.getPA().sendFrame36(PRAYER_GLOW[magePray[j]], 0);
							}
						}
					}
					break;
				case 4:
				case 12:
				case 20:
					if (c.getInstance().prayerActive[i] == false) {
						for (int j = 0; j < atkPray.length; j++) {
							if (atkPray[j] != i) {
								c.getInstance().prayerActive[atkPray[j]] = false;
								c.getPA().sendFrame36(PRAYER_GLOW[atkPray[j]], 0);
							}
						}
						for (int j = 0; j < strPray.length; j++) {
							if (strPray[j] != i) {
								c.getInstance().prayerActive[strPray[j]] = false;
								c.getPA().sendFrame36(PRAYER_GLOW[strPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								c.getInstance().prayerActive[rangePray[j]] = false;
								c.getPA().sendFrame36(PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								c.getInstance().prayerActive[magePray[j]] = false;
								c.getPA().sendFrame36(PRAYER_GLOW[magePray[j]], 0);
							}
						}
					}
					break;
				case 10:
					c.getInstance().lastProtItem = System.currentTimeMillis();
					break;

				case 16:
				case 17:
				case 18:
					if (System.currentTimeMillis() - c.getInstance().stopPrayerDelay < 5000) {
						c.sendMessage("You have been injured and can't use this prayer!");
						c.getPA().sendFrame36(PRAYER_GLOW[16], 0);
						c.getPA().sendFrame36(PRAYER_GLOW[17], 0);
						c.getPA().sendFrame36(PRAYER_GLOW[18], 0);
						return;
					}
					if (i == 16)
						c.getInstance().protMageDelay = System.currentTimeMillis();
					else if (i == 17)
						c.getInstance().protRangeDelay = System.currentTimeMillis();
					else if (i == 18)
						c.getInstance().protMeleeDelay = System.currentTimeMillis();
				case 21:
				case 22:
				case 23:
					headIcon = true;
					for (int p = 16; p < 24; p++) {
						if (i != p && p != 19 && p != 20) {
							c.getInstance().prayerActive[p] = false;
							c.getPA().sendFrame36(PRAYER_GLOW[p], 0);
						}
					}
					break;
				case 24:
				case 25:
					if (c.getInstance().prayerActive[i] == false) {
						for (int j = 0; j < atkPray.length; j++) {
							if (atkPray[j] != i) {
								c.getInstance().prayerActive[atkPray[j]] = false;
								c.getPA().sendFrame36(PRAYER_GLOW[atkPray[j]], 0);
							}
						}
						for (int j = 0; j < strPray.length; j++) {
							if (strPray[j] != i) {
								c.getInstance().prayerActive[strPray[j]] = false;
								c.getPA().sendFrame36(PRAYER_GLOW[strPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								c.getInstance().prayerActive[rangePray[j]] = false;
								c.getPA().sendFrame36(PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								c.getInstance().prayerActive[magePray[j]] = false;
								c.getPA().sendFrame36(PRAYER_GLOW[magePray[j]], 0);
							}
						}
						for (int j = 0; j < defPray.length; j++) {
							if (defPray[j] != i) {
								c.getInstance().prayerActive[defPray[j]] = false;
								c.getPA().sendFrame36(PRAYER_GLOW[defPray[j]], 0);
							}
						}
					}
					break;
				}

				if (!headIcon) {
					if (c.getInstance().prayerActive[i] == false) {
						c.getInstance().prayerActive[i] = true;
						c.getPA().sendFrame36(PRAYER_GLOW[i], 1);
					} else {
						c.getInstance().prayerActive[i] = false;
						c.getPA().sendFrame36(PRAYER_GLOW[i], 0);
					}
				} else {
					if (c.getInstance().prayerActive[i] == false) {
						c.getInstance().prayerActive[i] = true;
						c.getPA().sendFrame36(PRAYER_GLOW[i], 1);
						c.getInstance().headIcon = PRAYER_HEAD_ICONS[i];
						c.getPA().requestUpdates();
					} else {
						c.getInstance().prayerActive[i] = false;
						c.getPA().sendFrame36(PRAYER_GLOW[i], 0);
						c.getInstance().headIcon = -1;
						c.getPA().requestUpdates();
					}
				}
			} else {
				c.getPA().sendFrame36(PRAYER_GLOW[i], 0);
				c.getPA().sendString("You need a @blu@Prayer level of " + PRAYER_LEVEL_REQUIRED[i] + " to use "
						+ PRAYER_NAME[i] + ".", 357);
				c.getPA().sendString("Click here to continue", 358);
				c.getPA().sendChatInterface(356);
			}
		} else {
			c.getPA().sendFrame36(PRAYER_GLOW[i], 0);
			c.sendMessage("You have run out of prayer points!s");
			c.curses().resetCurse();
		}

	}

	public static void resetPrayers(Player c) {
		for (int i = 0; i < c.getInstance().prayerActive.length; i++) {
			c.getInstance().prayerActive[i] = false;
			c.getPA().sendFrame36(PRAYER_GLOW[i], 0);
		}
		c.sendMessage(":quicks:off");
		c.curses().resetCurse();
		c.getInstance().headIcon = -1;
		c.getPA().requestUpdates();
	}

	public static void reducePrayerLevel(Player c) {
		if (c.getInstance().playerLevel[5] - 1 > 0) {
			c.getInstance().playerLevel[5] -= 1;
		} else {
			c.sendMessage("You have run out of prayer points!");
			c.getInstance().playerLevel[5] = 0;
			resetPrayers(c);
			c.getInstance().prayerId = -1;
		}
		c.getPA().refreshSkill(5);
	}

	public static void handlePrayerDrain(Player c) {
		if (c.isDead) {
			return;
		}
		c.getInstance().usingPrayer = false;
		double toRemove = 0.0;
		for (int j = 0; j < c.getInstance().prayerActive.length; j++) {
			if (c.getInstance().prayerActive[j]) {
				toRemove += PRAYER_DRAIN_RATE[j] / 10;
				c.getInstance().usingPrayer = true;
			}
		}
		for (int j = 0; j < c.getInstance().curseActive.length; j++) {
			if (c.getInstance().curseActive[j]) {
				toRemove += (c.curses().curseData[j] / 10);
				c.getInstance().usingPrayer = true;
			}
		}
		if (!c.getInstance().usingPrayer)
			return;
		if (toRemove > 0) {
			toRemove /= (1 + (0.035 * c.getInstance().playerBonus[11]));
		}
		c.getInstance().prayerPoint -= toRemove;
		if (c.getInstance().prayerPoint <= 0) {
			c.getInstance().prayerPoint = 1.0 + c.getInstance().prayerPoint;
			reducePrayerLevel(c);
		}
	}

}
