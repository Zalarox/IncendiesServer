package main.game.players.actions.combat;

import main.Constants;
import main.game.npcs.NPC;
import main.game.npcs.NPCHandler;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.game.players.content.minigames.DuelArena;
import main.util.Misc;

public class Curses {
	private Player c;

	public Curses(Player player) {
		this.c = player;
	}

	// private boolean appendedLeeches; // UNUSED

	public final int[] PRAYER_LEVEL_REQUIRED = { 50, 50, 52, 54, 56, 59, 62, 65, 68, 71, 74, 76, 78, 80, 82, 84, 86, 89,
			92, 95 };
	public final String[] CURSE_NAME = { "Protect Item", "Sap Warrior", "Sap Ranger", "Sap Mage", "Sap Spirit",
			"Berserker", "Deflect Summoning", "Deflect Magic", "Deflect Missiles", "Deflect Melee", "Leech Attack",
			"Leech Ranged", "Leech Magic", "Leech Defence", "Leech Strength", "Leech Energy", "Leech Special Attack",
			"Wrath", "Soul Split", "Turmoil" };
	public final int[] GLOW = { 83, 84, 85, 101, 102, 86, 87, 88, 89, 90, 91, 103, 104, 92, 93, 94, 95, 96, 97, 105 };
	public final int[] HEADICONS = { -1, -1, -1, -1, -1, -1, 12, 10, 11, 9, -1, -1, -1, -1, -1, -1, -1, 16, 17, -1 };
	public double[] curseData = { 0.6, // Protect Item
			3.5, // Sap Warrior
			3.5, // Sap Ranger
			3.5, // Sap Mage
			3.5, // Sap Spirit
			0.6, // Berserker
			4, // Deflect Summoning
			4, // Deflect Magic
			4, // Deflect Missiles
			4, // Deflect Melee
			3, // Leech Attack
			3, // Leech Ranged
			3, // Leech Magic
			3, // Leech Defence
			3, // Leech Strength
			3, // Leech Energy
			3, // Leech Special
			0.9, // Wrath
			6, // SS
			6, // Turmoil
	};

	public final int PROTECT_ITEM = 0, SAP_WARRIOR = 1, SAP_RANGER = 2, SAP_MAGE = 3, SAP_SPIRIT = 4, BERSERKER = 5,
			DEFLECT_SUMMONING = 6, DEFLECT_MAGIC = 7, DEFLECT_MISSILES = 8, DEFLECT_MELEE = 9, LEECH_ATTACK = 10,
			LEECH_RANGED = 11, LEECH_MAGIC = 12, LEECH_DEFENCE = 13, LEECH_STRENGTH = 14, LEECH_ENERGY = 15,
			LEECH_SPEC = 16, WRATH = 17, SOUL_SPLIT = 18, TURMOIL = 19;

	public void resetCurse() {
		for (int i = 0; i <= 19; i++) {
			deactivate(i);
		}
	}

	public void activateCurse(int i) {
		// Inside duel arena
		if (c.getVariables().duelRule[DuelArena.RULE_PRAYER]) {
			for (int p = 0; p < 19; p++) {
				c.getVariables().curseActive[p] = false;
				c.getPA().sendFrame36(GLOW[p], 0);
			}
			c.sendMessage("Prayer has been disabled in this duel!");
			return;
		}

		// Inside barbarian defence/assault
		if (c.getVariables().inBarbDef) {
			c.getPA().sendFrame36(GLOW[i], 0);
			c.sendMessage("The barbarians are strongly against the use of prayers!");
			return;
		}

		int[] leeches = { LEECH_ATTACK, LEECH_RANGED, LEECH_MAGIC, LEECH_DEFENCE, LEECH_STRENGTH, LEECH_ENERGY,
				LEECH_SPEC };
		int[] saps = { SAP_WARRIOR, SAP_RANGER, SAP_MAGE, SAP_SPIRIT };
		int[] isHeadIcon = { DEFLECT_MAGIC, DEFLECT_MISSILES, DEFLECT_MELEE, WRATH, SOUL_SPLIT };
		if (c.getVariables().playerLevel[5] > 0 || !Constants.PRAYER_POINTS_REQUIRED) {
			if (c.getPA().getLevelForXP(c.getVariables().playerXP[5]) >= PRAYER_LEVEL_REQUIRED[i]
					|| !Constants.PRAYER_LEVEL_REQUIRED) {
				if (!c.getVariables().curseActive[i])
					curseEmote(i);
				boolean headIcon = false;
				switch (i) {
				case PROTECT_ITEM:
					c.getVariables().lastProtItem = System.currentTimeMillis();
					break;
				
					// Sap curses TODO code functionality
				case SAP_WARRIOR:
				case SAP_RANGER:
				case SAP_MAGE:
				case SAP_SPIRIT:
					if (!c.getVariables().curseActive[i]) {
						for (int j = 0; j < leeches.length; j++) {
							if (leeches[j] != i)
								deactivate(leeches[j]);
						}
						deactivate(TURMOIL);
					}
					break;
					
					// Leech curses TODO code functionality
				case LEECH_ATTACK:
				case LEECH_RANGED:
				case LEECH_MAGIC:
				case LEECH_DEFENCE:
				case LEECH_STRENGTH:
				case LEECH_ENERGY:
				case LEECH_SPEC:
					if (!c.getVariables().curseActive[i]) {
						for (int j = 0; j < saps.length; j++) {
							if (saps[j] != i) {
								deactivate(saps[j]);
							}
						}
						deactivate(TURMOIL);
					}
					break;
					
					// Deflect curses TODO code functionality
				case DEFLECT_SUMMONING:
				case DEFLECT_MAGIC:
				case DEFLECT_MISSILES:
				case DEFLECT_MELEE: // Horrible code, TODO fix
					if (System.currentTimeMillis() - c.getVariables().stopPrayerDelay < 5000) {
						c.sendMessage("You have been injured and can't use this prayer!");
						deactivate(i);
						return;
					}
					if (i == DEFLECT_MAGIC)
						c.getVariables().protMageDelay = System.currentTimeMillis();
					else if (i == DEFLECT_MISSILES)
						c.getVariables().protRangeDelay = System.currentTimeMillis();
					else if (i == DEFLECT_MELEE)
						c.getVariables().protMeleeDelay = System.currentTimeMillis();
					
				case WRATH: // TODO code functionality
					
				case SOUL_SPLIT:
					headIcon = true;
					if (i != DEFLECT_SUMMONING) {
						deactivate(DEFLECT_SUMMONING);
					}
					if (!c.getVariables().curseActive[i]) {
						for (int j = 0; j < isHeadIcon.length; j++) {
							if (isHeadIcon[j] != i)
								deactivate(isHeadIcon[j]);
						}
					}
					break;

				case TURMOIL:
					if (!c.getVariables().curseActive[i]) {
						for (int j = 0; j < leeches.length; j++) {
							if (leeches[j] != i)
								deactivate(leeches[j]);
						}
						for (int j = 0; j < saps.length; j++) {
							if (saps[j] != i)
								deactivate(saps[j]);
						}
					}
					break;
				}
				
				if (i == DEFLECT_MAGIC)
					c.getVariables().protMageDelay = System.currentTimeMillis();
				else if (i == DEFLECT_MISSILES)
					c.getVariables().protRangeDelay = System.currentTimeMillis();
				else if (i == DEFLECT_MELEE)
					c.getVariables().protMeleeDelay = System.currentTimeMillis();
				if (!headIcon) {
					if (!c.getVariables().curseActive[i]) {
						c.getVariables().curseActive[i] = true;
						c.getPA().sendFrame36(GLOW[i], 1);
					} else {
						c.getVariables().curseActive[i] = false;
						c.getPA().sendFrame36(GLOW[i], 0);
					}
				} else {
					if (!c.getVariables().curseActive[i]) {
						c.getVariables().curseActive[i] = true;
						c.getPA().sendFrame36(GLOW[i], 1);
						c.getVariables().headIcon = HEADICONS[i];
						c.getPA().requestUpdates();
					} else {
						deactivate(i);
						c.getVariables().headIcon = -1;
						c.getPA().requestUpdates();
					}
				}
			} else {
				deactivate(i);
				c.getPA().sendString("You need a @blu@Prayer level of " + PRAYER_LEVEL_REQUIRED[i] + " to use "
						+ CURSE_NAME[i] + ".", 357);
				c.getPA().sendString("Click here to continue", 358);
				c.getPA().sendChatInterface(356);
			}
		} else {
			c.getPA().sendFrame36(GLOW[i], 0);
			c.sendMessage("You have run out of prayer points!");
		}

	}

	public void curseButtons(int buttonId) {
		int[] buttonIds = { 87231, 87233, 87235, 87237, 87239, 87241, 87243, 87245, 87247, 87249, 87251, 87253, 87255,
				88001, 88003, 88005, 88007, 88009, 88011, 88013 };
		for (int i = 0; i < buttonIds.length; i++) {
			if (buttonIds[i] == buttonId)
				activateCurse(i);
		}
	}

	public void curseEmote(int curseId) {
		switch (curseId) {
		case PROTECT_ITEM:
			c.startAnimation(12567);
			c.gfx0(2213);
			break;
		case BERSERKER:
			c.startAnimation(12589);
			c.gfx0(2266);
			break;
		case TURMOIL:
			c.startAnimation(12565);
			c.gfx0(2226);
			break;
		}
	}

	public void deactivate(int i) {
		c.getVariables().curseActive[i] = false;
		c.getPA().sendFrame36(GLOW[i], 0);
	}

	/* Actual curse content */
	public double getTurmoilMultiplier(String stat) {
		NPC n = null;
		Player c2 = null;
		double otherLevel = 0;
		double turmoilMultiplier = stat.equalsIgnoreCase("Strength") ? 1.23 : 1.15;
		if (c.getVariables().oldPlayerIndex > 0)
			c2 = PlayerHandler.players[c.getVariables().oldPlayerIndex];
		else if (c.getVariables().oldNpcIndex > 0)
			n = NPCHandler.npcs[c.getVariables().oldNpcIndex];
		if (stat.equalsIgnoreCase("Defence")) {
			if (c2 != null)
				otherLevel = c2.getLevelForXP(c2.getVariables().playerXP[1]) * 0.15;
			else if (n != null)
				otherLevel = n.getCombatLevel() * 0.15;
			else
				otherLevel = 0;
		} else if (stat.equalsIgnoreCase("Strength")) {
			if (c2 != null)
				otherLevel = c2.getLevelForXP(c2.getVariables().playerXP[2]) * 0.10;
			else if (n != null)
				otherLevel = n.getCombatLevel() * 0.10;
			else
				otherLevel = 0;
		} else if (stat.equalsIgnoreCase("Attack")) {
			if (c2 != null)
				otherLevel = c2.getLevelForXP(c2.getVariables().playerXP[0]) * 0.15;
			else if (n != null)
				otherLevel = n.getCombatLevel() * 0.15;
			else
				otherLevel = 0;
		}
		if (otherLevel > 14)
			otherLevel = 14;
		turmoilMultiplier += otherLevel * .01;
		return turmoilMultiplier;
	}

	public void soulSplit(int id, int damage) {
		if (c.getVariables().curseActive[SOUL_SPLIT] && c.getVariables().ssDelay <= 0) {
			if (c.getVariables().oldPlayerIndex > 0) {
				c.ssTarget = PlayerHandler.players[id];
				if (c.ssTarget == null)
					return;
				c.getPA().createPlayersProjectile(c.getX(), c.getY(), (c.getX() - c.ssTarget.getX()) * -1,
						(c.getY() - c.ssTarget.getY()) * -1, 50, 75, 2263, 25, 25, -id - 1, 0);
				c.ssTarget.getVariables().playerLevel[5] -= (c.ssTarget.getVariables().playerLevel[5] < 6)
						? c.ssTarget.getVariables().playerLevel[5] : 5;
				c.ssTarget.getPA().refreshSkill(5);
			} else if (c.getVariables().oldNpcIndex > 0) {
				c.getVariables().ssTargetNPC = NPCHandler.npcs[id];
				c.getPA().createPlayersProjectile(c.getX(), c.getY(),
						(c.getX() - c.getVariables().ssTargetNPC.getX()) * -1,
						(c.getY() - c.getVariables().ssTargetNPC.getY()) * -1, 50, 75, 2263, 25, 25, id + 1, 0);
			}
			c.getVariables().ssHeal = damage / 5;
			c.getVariables().ssDelay = 5;
		}
	}

	public void appendRandomLeech(int id, int leechType) {
		int projectile = 0;
		int endGFX = 0;
		int[] curseTypes = { LEECH_ATTACK, LEECH_RANGED, LEECH_MAGIC, LEECH_DEFENCE, LEECH_STRENGTH, LEECH_ENERGY,
				LEECH_SPEC };
		if (!c.getVariables().curseActive[curseTypes[leechType]] || Misc.random(3) < 3) {
			c.getVariables().leechDelay = 0;
			return;
		}
		switch (leechType) {
		case 0: // Leech attack
			projectile = 2231;
			endGFX = 2232;
			break;
		case 1: // Leech ranged
			projectile = 2236;
			endGFX = 2238;
			break;
		case 2: // Leech magic
			projectile = 2240;
			endGFX = 2242;
			break;
		case 3: // Leech defence
			projectile = 2244;
			endGFX = 2246;
			break;
		case 4: // Leech strength
			projectile = 2248;
			endGFX = 2250;
			break;
		case 5: // Leech energy
			projectile = 2252;
			endGFX = 2254;
			break;
		case 6: // Leech special
			projectile = 2256;
			endGFX = 2258;
			break;
		}
		if (c.getVariables().oldPlayerIndex > 0) {
			c.leechTarget = PlayerHandler.players[id];
			c.getPA().createPlayersProjectile(c.getX(), c.getY(), (c.getX() - c.leechTarget.getX()) * -1,
					(c.getY() - c.leechTarget.getY()) * -1, 50, 75, projectile, 25, 25,
					-c.getVariables().oldPlayerIndex - 1, 0);
		} else if (c.getVariables().oldNpcIndex > 0) {
			c.getVariables().leechTargetNPC = NPCHandler.npcs[id];
			c.getPA().createPlayersProjectile(c.getX(), c.getY(),
					(c.getX() - c.getVariables().leechTargetNPC.getX()) * -1,
					(c.getY() - c.getVariables().leechTargetNPC.getY()) * -1, 50, 75, projectile, 25, 25,
					c.getVariables().oldNpcIndex + 1, 0);
		}
		c.startAnimation(12575);
		c.getVariables().leechEndGFX = endGFX;
		c.getVariables().leechType = leechType;
	}

	private void leechEffect(int leechType) {
	}

	private void leechEffectNPC(int leechType) {
	}

	public void deflect(Player c2, int damage, int damageType) {
		int[] gfx = { 2230, 2229, 2228, 2228 };
		if (Misc.random(3) == 0) {
			int deflect = (damage < 10) ? 1 : (int) (damage / 10);
			c.getCombat().appendHit(c2, deflect, 0, 3, false);
			c.gfx0(gfx[damageType]);
			c.startAnimation(12573);
		}
	}

	public void deflectNPC(NPC n, int damage, int damageType) {
		int[] gfx = { 2230, 2229, 2228, 2228 };
		if (Misc.random(3) == 0) {
			int deflect = (damage < 10) ? 1 : (int) (damage / 10);
			c.getCombat().appendHit(n, deflect, 0, 3, 2);
			c.gfx0(gfx[damageType]);
			c.startAnimation(12573);
		}
	}

	public void handleProcess() {
		if (c.getVariables().ssDelay > 0)
			c.getVariables().ssDelay--;
		if (c.getVariables().ssDelay == 3) {
			if (c.ssTarget != null) {
				c.getPA().createPlayersProjectile(c.ssTarget.getX(), c.ssTarget.getY(),
						(c.ssTarget.getY() - c.getY()) * -1, (c.ssTarget.getX() - c.getX()) * -1, 50, 75, 2263, 25, 25,
						-c.getId() - 1, 40);
				c.ssTarget.gfx0(2264);
			} else if (c.getVariables().ssTargetNPC != null) {
				c.getPA().createPlayersProjectile(c.getVariables().ssTargetNPC.getX(),
						c.getVariables().ssTargetNPC.getY(), (c.getVariables().ssTargetNPC.getY() - c.getY()) * -1,
						(c.getVariables().ssTargetNPC.getX() - c.getX()) * -1, 50, 75, 2263, 25, 25, -c.getId() - 1,
						40);
				c.getVariables().ssTargetNPC.gfx0(2264);
			}
			if (c.getVariables().constitution < c.getVariables().maxConstitution)
				c.addToHp(c.getVariables().ssHeal);
		}
		if (c.getVariables().leechDelay > 0)
			c.getVariables().leechDelay--;
		if (c.getVariables().leechDelay == 5) {
			if (c.getVariables().oldPlayerIndex > 0)
				appendRandomLeech(c.getVariables().oldPlayerIndex, Misc.random(6));
			else if (c.getVariables().oldNpcIndex > 0)
				appendRandomLeech(c.getVariables().oldNpcIndex, Misc.random(6));
		} else if (c.getVariables().leechDelay == 3) {
			if (c.leechTarget != null) {
				c.leechTarget.gfx0(c.getVariables().leechEndGFX);
				leechEffect(c.getVariables().leechType);
			} else if (c.getVariables().leechTargetNPC != null) {
				c.getVariables().leechTargetNPC.gfx0(c.getVariables().leechEndGFX);
				leechEffectNPC(c.getVariables().leechType);
			}
		}
	}
}
