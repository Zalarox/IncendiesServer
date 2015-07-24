package main.game.npcs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import main.Constants;
import main.GameEngine;
import main.event.CycleEvent;
import main.event.CycleEventContainer;
import main.event.CycleEventHandler;
import main.game.npcs.data.EmoteHandler;
import main.game.npcs.data.NPCDrops;
import main.game.npcs.data.SummoningData;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.game.players.content.minigames.impl.FightCaves;
import main.game.players.content.minigames.impl.Godwars;
import main.game.players.content.minigames.impl.PestControl;
import main.game.players.content.minigames.impl.barrows.Barrows;
import main.game.players.content.skills.hunter.HunterHandler;
import main.game.players.content.skills.hunter.HunterNpcs;
import main.handlers.ItemHandler;
import main.util.Misc;
import main.world.TileControl;
import main.world.map.Region;

public class NPCHandler {

	public static int maxNPCs = 6000;
	public static NPC npcs[] = new NPC[maxNPCs];
	public static int npcSizes[] = new int[11601];

	public NPCHandler() {
		for (int i = 0; i < maxNPCs; i++) {
			npcs[i] = null;
			NPCDefinitions.getDefinitions()[i] = null;
		}
	}

	public void loadNpcs() {
		try {
			loadSizes();
		} catch (IOException e) {
		}
		loadNPCList("./Data/cfg/npc.cfg");
		loadAutoSpawn("./Data/cfg/spawn-config.cfg");
		HunterNpcs.hunterStartup();
	}

	public static void loadSizes() throws IOException {
		File parentDir = new File("Data");
		parentDir.mkdir();
		final String filename = "npcSizes.txt";
		File file = new File(parentDir, filename);
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			String text = null;
			int i = 0;
			// repeat until all lines is read
			while ((text = reader.readLine()) != null) {
				npcSizes[i] = Integer.parseInt(text);
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean getsPulled(int i) {
		switch (NPCHandler.npcs[i].npcType) {
		case 6260:
			if (npcs[i].firstAttacker > 0)
				return false;
			break;
		}
		return true;
	}

	public static boolean pathBlocked(NPC attacker, Player victim) {

		double offsetX = Math.abs(attacker.absX - victim.absX);
		double offsetY = Math.abs(attacker.absY - victim.absY);

		int distance = TileControl.calculateDistance(attacker, victim);

		if (distance == 0) {
			return true;
		}

		offsetX = offsetX > 0 ? offsetX / distance : 0;
		offsetY = offsetY > 0 ? offsetY / distance : 0;

		int[][] path = new int[distance][5];

		int curX = attacker.absX;
		int curY = attacker.absY;
		int next = 0;
		int nextMoveX = 0;
		int nextMoveY = 0;

		double currentTileXCount = 0.0;
		double currentTileYCount = 0.0;

		while (distance > 0) {
			distance--;
			nextMoveX = 0;
			nextMoveY = 0;
			if (curX > victim.absX) {
				currentTileXCount += offsetX;
				if (currentTileXCount >= 1.0) {
					nextMoveX--;
					curX--;
					currentTileXCount -= offsetX;
				}
			} else if (curX < victim.absX) {
				currentTileXCount += offsetX;
				if (currentTileXCount >= 1.0) {
					nextMoveX++;
					curX++;
					currentTileXCount -= offsetX;
				}
			}
			if (curY > victim.absY) {
				currentTileYCount += offsetY;
				if (currentTileYCount >= 1.0) {
					nextMoveY--;
					curY--;
					currentTileYCount -= offsetY;
				}
			} else if (curY < victim.absY) {
				currentTileYCount += offsetY;
				if (currentTileYCount >= 1.0) {
					nextMoveY++;
					curY++;
					currentTileYCount -= offsetY;
				}
			}
			path[next][0] = curX;
			path[next][1] = curY;
			path[next][2] = attacker.heightLevel;// getHeightLevel();
			path[next][3] = nextMoveX;
			path[next][4] = nextMoveY;
			next++;
		}
		for (int i = 0; i < path.length; i++) {
			if (!Region.getClipping(path[i][0], path[i][1], path[i][2], path[i][3], path[i][4])) {
				return true;
			}
		}
		return false;
	}

	public void handleBarricades(int i) {
		/*
		 * switch (npcs[i].npcType) { case 1532 : case 1533 :
		 * CastleWars.saraBarricades--; break; case 1534 : case 1535 :
		 * CastleWars.zammyBarricades--; break; }
		 */
	}

	/**
	 * 
	 * Get close to player
	 */
	public int getClosePlayer(int i) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (j == npcs[i].spawnedBy)
					return j;
				if (goodDistance(PlayerHandler.players[j].absX, PlayerHandler.players[j].absY, npcs[i].absX,
						npcs[i].absY, 2 + distanceRequired(i) + followDistance(i)) || isFightCaveNpc(i)) {
					if ((PlayerHandler.players[j].getVariables().underAttackBy <= 0
							&& PlayerHandler.players[j].getVariables().underAttackBy2 <= 0)
							|| PlayerHandler.players[j].getVariables().inMulti())
						if (PlayerHandler.players[j].heightLevel == npcs[i].heightLevel)
							return j;
				}
			}
		}
		return 0;
	}

	/**
	 * 
	 * Get close random player
	 */
	public int getCloseRandomPlayer(int i) {
		ArrayList<Integer> players = new ArrayList<Integer>();
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (goodDistance(PlayerHandler.players[j].absX, PlayerHandler.players[j].absY, npcs[i].absX,
						npcs[i].absY, 2 + distanceRequired(i) + followDistance(i)) || isFightCaveNpc(i)) {
					if ((PlayerHandler.players[j].getVariables().underAttackBy <= 0
							&& PlayerHandler.players[j].getVariables().underAttackBy2 <= 0)
							|| PlayerHandler.players[j].getVariables().inMulti() || NPCHandler.npcs[i].type == 1)
						if (PlayerHandler.players[j].heightLevel == npcs[i].heightLevel)
							players.add(j);
				}
			}
		}
		if (players.size() > 0)
			return players.get(Misc.random(players.size() - 1));
		else
			return 0;
	}

	/**
	 * Summon npc, barrows, etc
	 **/
	public static void spawnNpc(Player c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP,
			int maxHit, int attack, int defence, boolean attackPlayer, boolean headIcon) {
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			return;
		}
		NPC newNPC = new NPC(slot, npcType, getNpcListName(npcType));
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit * 10;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.spawnedBy = c.getId();
		if (headIcon)
			c.getPA().drawHeadicon(1, slot, 0, 0);
		if (attackPlayer) {
			newNPC.underAttack = true;
			if (c != null) {

				newNPC.killerId = c.playerId;
			}
		}
		npcs[slot] = newNPC;
		/*
		 * if (Region.REGION_UPDATING_ENABLED) { try { if
		 * (Region.getRegion(newNPC.absX, newNPC.absY) != null) {
		 * Region.getRegion(newNPC.absX, newNPC.absY).addNpc(newNPC);
		 * newNPC.lastRegion = Region.getRegion(newNPC.absX, newNPC.absY); } }
		 * catch (Exception e) {
		 * 
		 * } }
		 */
	}

	/**
	 * 
	 * Spawn an npc
	 */
	public void spawnNpc(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack,
			int defence) {
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			return;
		}
		NPC newNPC = new NPC(slot, npcType, getNpcListName(npcType));
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit * 10;
		newNPC.attack = attack;
		newNPC.defence = defence;
		npcs[slot] = newNPC;
		/*
		 * if (Region.REGION_UPDATING_ENABLED) { try { if
		 * (Region.getRegion(newNPC.absX, newNPC.absY) != null) {
		 * Region.getRegion(newNPC.absX, newNPC.absY).addNpc(newNPC);
		 * newNPC.lastRegion = Region.getRegion(newNPC.absX, newNPC.absY); } }
		 * catch (Exception e) {
		 * 
		 * } }
		 */
	}

	/**
	 * 
	 * Create an npc
	 */
	public void createNpc(Player c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
			int attack, int defence, boolean attackPlayer, boolean headIcon) {
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				c.getVariables().barbLeader = slot = i;
				break;
			}
		}
		if (slot == -1) {
			return;
		}
		NPC newNPC = new NPC(slot, npcType, getNpcListName(npcType));
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit * 10;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.spawnedBy = c.getId();
		if (headIcon)
			c.getPA().drawHeadicon(1, slot, 0, 0);
		if (attackPlayer) {
			newNPC.underAttack = true;
			if (c != null) {

				newNPC.killerId = c.playerId;
			}
		}
		npcs[slot] = newNPC;
		/*
		 * if (Region.REGION_UPDATING_ENABLED) { try { if
		 * (Region.getRegion(newNPC.absX, newNPC.absY) != null) {
		 * Region.getRegion(newNPC.absX, newNPC.absY).addNpc(newNPC);
		 * newNPC.lastRegion = Region.getRegion(newNPC.absX, newNPC.absY); } }
		 * catch (Exception e) {
		 * 
		 * } }
		 */
	}

	/**
	 * 
	 * Summon a new npc
	 */
	public NPC summonNPC(Player c, int npcType, int x, int y, int heightLevel, int walkingType, int HP, int maxHit,
			int attack, int defence) {

		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		NPC newNPC = new NPC(slot, npcType, getNpcListName(npcType));
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = walkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit * 10;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.spawnedBy = newNPC.summonedFor = c.getId();
		c.getVariables().summoningMonsterId = slot;
		npcs[slot] = newNPC;
		/*
		 * if (Region.REGION_UPDATING_ENABLED) { try { if
		 * (Region.getRegion(newNPC.absX, newNPC.absY) != null) {
		 * Region.getRegion(newNPC.absX, newNPC.absY).addNpc(newNPC);
		 * newNPC.lastRegion = Region.getRegion(newNPC.absX, newNPC.absY); } }
		 * catch (Exception e) {
		 * 
		 * } }
		 */
		return newNPC;
	}

	/**
	 * 
	 * New npc
	 */

	public void newNPC(int npcType, int x, int y, int heightLevel, int WalkingType) {
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}

		if (slot == -1)
			return;

		NPC newNPC = new NPC(slot, npcType, getNpcListName(npcType));
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = getNpcListHP(npcType);
		newNPC.MaxHP = getNpcListHP(npcType);
		npcs[slot] = newNPC;
		/*
		 * if (Region.REGION_UPDATING_ENABLED) { try { if
		 * (Region.getRegion(newNPC.absX, newNPC.absY) != null) {
		 * Region.getRegion(newNPC.absX, newNPC.absY).addNpc(newNPC);
		 * newNPC.lastRegion = Region.getRegion(newNPC.absX, newNPC.absY); } }
		 * catch (Exception e) {
		 * 
		 * } }
		 */
	}

	/**
	 * 
	 * New npc
	 */
	public void newNPC(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack,
			int defence, int type) {
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1)
			return;
		NPC newNPC = new NPC(slot, npcType, getNpcListName(npcType));
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit * 10;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.type = type;
		npcs[slot] = newNPC;
		if (newNPC.walkingType >= 0 && newNPC.walkingType != 1) {
			switch (newNPC.walkingType) {
			case 5:
				newNPC.lastX = newNPC.absX - 1;
				newNPC.lastY = newNPC.absY;
				newNPC.turnNpc(newNPC.absX - 1, newNPC.absY);
				break;
			case 4:
				newNPC.lastX = newNPC.absX + 1;
				newNPC.lastY = newNPC.absY;
				newNPC.turnNpc(newNPC.absX + 1, newNPC.absY);
				break;
			case 3:
				newNPC.lastX = newNPC.absX;
				newNPC.lastY = newNPC.absY - 1;
				newNPC.turnNpc(newNPC.absX, newNPC.absY - 1);
				break;
			case 2:
				newNPC.lastX = newNPC.absX;
				newNPC.lastY = newNPC.absY + 1;
				newNPC.turnNpc(newNPC.absX, newNPC.absY + 1);
				break;
			case 1:
				newNPC.lastX = newNPC.absX;
				newNPC.lastY = newNPC.absY;
				break;
			}
		}
	}

	public void newNPC(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack,
			int defence, Player c) {
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1)
			return;
		System.out.println("NPC SPAWNED,  TYPE : " + npcType);
		NPC newNPC = new NPC(slot, npcType, getNpcListName(npcType));
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit * 10;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.owner = c.playerId;
		newNPC.spawnedBy = c.playerId;
		c.getVariables().waveAmount++;
		// c.sendMessage(""+c.waveAmount);
		npcs[slot] = newNPC;
		/*
		 * if (Region.REGION_UPDATING_ENABLED) { try { if
		 * (Region.getRegion(newNPC.absX, newNPC.absY) != null) {
		 * Region.getRegion(newNPC.absX, newNPC.absY).addNpc(newNPC);
		 * newNPC.lastRegion = Region.getRegion(newNPC.absX, newNPC.absY); } }
		 * catch (Exception e) {
		 * 
		 * } }
		 */
	}

	public void newNPCList(int npcType, String npcName, int combat, int HP) {
		NPCDefinitions newNPCList = new NPCDefinitions(npcType);
		newNPCList.setNpcName(npcName);
		newNPCList.setNpcCombat(combat);
		newNPCList.setNpcHealth(HP * 10);
		NPCDefinitions.getDefinitions()[npcType] = newNPCList;
	}

	/**
	 * Attack delays
	 **/
	public int getNpcDelay(int i) {
		if (NPCHandler.npcs[i].npcType >= 10328 && NPCHandler.npcs[i].npcType <= 10363) {
			return 4;
		}
		switch (npcs[i].npcType) {
		case 2028:
			return 2;
		case 2026:
			return 6;
		case 2025:
		case 9995:
			return 7;
		case 8133: // Corporeal beast
		case 3101: // Melee
		case 3102: // Range
		case 3103: // Mage
			return 7;
		case 8349:
		case 8350:
		case 8351:
			if (npcs[i].attackType == 2)
				return 4;
			else if (npcs[i].attackType == 1)
				return 6;
			else if (npcs[i].attackType == 0)
				return 7;
		case 3495:
			return 3;
		case 2745:
			return 8;
		case 50:
		case 10770:
		case 10771:
		case 10772:
		case 10773:
		case 10774:
		case 10775:
		case 10219:
		case 10220:
		case 10221:
		case 10222:
		case 10223:
		case 10224:
		case 10604:
		case 10605:
		case 10606:
		case 10607:
		case 10608:
		case 10609:
		case 10776:
		case 10777:
		case 10778:
		case 10779:
		case 10780:
		case 10781:
		case 10815:
		case 10816:
		case 10817:
		case 10818:
		case 10819:
		case 10820:
		case 53:
		case 54:
		case 55:
		case 941:
		case 1590:
		case 1591:
		case 1592:
		case 9964:
			return 8;

		case 6222:
		case 6223:
		case 6206:
		case 6208:
		case 6204:
		case 6225:
		case 6227:
		case 6260:
			return 6;
		// saradomin gw boss
		case 6247:
			return 3;
		default:
			return 5;
		}
	}

	/**
	 * Hit delays
	 **/
	public int getHitDelay(int i) {
		switch (npcs[i].npcType) {
		case 2881:
		case 2882:
		case 3200:
		case 2892:
		case 2894:
			return 3;
		case 2743:
		case 2631:
		case 2558:
		case 2559:
		case 2560:
			return 3;
		case 5993:
			return 3;
		case 5229:
		case 5230:
		case 5231:
		case 5232:
		case 5233:
		case 5234:
		case 5235:
		case 5236:
		case 5237: // Penance ranger
			return 3;
		case 2745:
			if (npcs[i].attackType == 1 || npcs[i].attackType == 2)
				return 5;
			else
				return 2;
		case 2025:
			return 4;
		case 2028:
			return 3;

		default:
			return 2;
		}
	}

	/**
	 * Npc respawn time
	 **/
	public int getRespawnTime(int i) {
		switch (npcs[i].npcType) {
		case 8133:
			return 140;
		case 2881:
		case 2882:
		case 2883:
		case 2558:
		case 2559:
		case 2560:
		case 2561:
		case 2562:
		case 2563:
		case 2564:
		case 2550:
		case 2551:
		case 2552:
		case 2553:
			return 100;
		case 3777:
		case 3778:
		case 3779:
		case 3780:
			return 500;
		case 51:
		case 50:// drags
		case 9964:
		case 10770:
		case 10771:
		case 10772:
		case 10773:
		case 10774:
		case 10775:
		case 10219:
		case 10220:
		case 10221:
		case 10222:
		case 10223:
		case 10224:
		case 10604:
		case 10605:
		case 10606:
		case 10607:
		case 10608:
		case 10609:
		case 10776:
		case 10777:
		case 10778:
		case 10779:
		case 10780:
		case 10781:
		case 10815:
		case 10816:
		case 10817:
		case 10818:
		case 10819:
		case 10820:
		case 4291: // Cyclops
		case 4292: // Ice cyclops
		case 53:
		case 54:
		case 55:
		case 941:
		case 1590:
		case 1591:
		case 1592:
			return 50;
		default:
			return 25;
		}
	}

	/**
	 * Npc max hit
	 */
	public int getMaxHit(int i) {
		switch (npcs[i].npcType) {
		case 2558:
			if (npcs[i].attackType == 2)
				return 28;
			else
				return 68;
		case 8133:
			if (npcs[i].attackType == 0)
				return 48;
			else if (npcs[i].attackType == 1)
				return 48;
			else if (npcs[i].attackType == 2)
				return 60;
		case 2562:
			return 31;
		case 2550:
			return 36;
		}
		return 1;
	}

	/**
	 * @param i
	 */
	public Player allPlayers(int i) {
		return PlayerHandler.players[i];
	}

	private Player c2 = null;

	/**
	 * The process
	 */
	public void process() {
		for (int i = 0; i < maxNPCs; i++) {
			if (npcs[i] == null)
				continue;
			npcs[i].clearUpdateFlags();

		}
		for (int i = 0; i < maxNPCs; i++) {
			if (npcs[i] != null) {
				NPC n = npcs[i];
				HunterHandler.checkTrap(n);
				if (npcs[i].splashDelay > 0)
					npcs[i].splashDelay--;
				int dam = 50 + Misc.random(80);
				if (npcs[i].splashDelay == 4) {
					corpSplash(n);
					for (int p = 1; p < PlayerHandler.players.length; p++) {
						c2 = allPlayers(p);
						if (c2 != null) {
							if (c2.getX() == npcs[i].splashCoord[0][0] && c2.getY() == npcs[i].splashCoord[0][1])
								c2.getCombat().appendHit(c2, dam, 0, 2, false); // fix
						}
					}
				} else if (npcs[i].splashDelay == 0) {
					for (int p = 1; p < PlayerHandler.players.length; p++) {
						c2 = allPlayers(p);
						for (int coords = 1; coords < npcs[i].splashCoord.length; coords++) {
							if (c2 != null) {
								if (c2.getX() == npcs[i].splashCoord[coords][0]
										&& c2.getY() == npcs[i].splashCoord[coords][1])
									c2.getCombat().appendHit(c2, dam, 0, 2, false); // fix
								c2.getPA().stillGfx(1808, npcs[i].splashCoord[coords][0],
										npcs[i].splashCoord[coords][1], 0, 0);
							}
						}
					}
					npcs[i].splashDelay = -1;
				}
				Player other = PlayerHandler.players[npcs[i].summonedFor];
				if (npcs[i].summonedFor > 0 && !npcs[i].isDead && NPCHandler.npcs[i].isAttackingAPerson != true
						&& NPCHandler.npcs[i].isAttackedByPerson != true && NPCHandler.npcs[i].IsAttackingNPC != true) {
					summoningFollow(i, npcs[i].summonedFor);
				} else if (npcs[i].summonedFor > 0 && !npcs[i].isDead && NPCHandler.npcs[i].isAttackingAPerson == true
						&& NPCHandler.npcs[i].isAttackedByPerson != true && other.getVariables().inMulti()
						&& NPCHandler.npcs[i].IsAttackingNPC != true) {
					followPlayer(i, other.getVariables().playerIndex, npcs[i].summonedFor);
				} else if (npcs[i].summonedFor > 0 && !npcs[i].isDead && NPCHandler.npcs[i].isAttackingAPerson != true
						&& NPCHandler.npcs[i].isAttackedByPerson == true && other.getVariables().inMulti()
						&& NPCHandler.npcs[i].IsAttackingNPC != true) {
					followPlayer(i, other.getVariables().underAttackBy, npcs[i].summonedFor);
				} else if (npcs[i].summonedFor > 0 && !npcs[i].isDead && NPCHandler.npcs[i].isAttackingAPerson == true
						&& NPCHandler.npcs[i].isAttackedByPerson == true && other.getVariables().inMulti()
						&& NPCHandler.npcs[i].IsAttackingNPC != true) {
					followPlayer(i, other.getVariables().underAttackBy, npcs[i].summonedFor);
				}
				if (npcs[i].npcType == 3782 && PestControl.gameStarted) {
					if (Misc.random(10) == 4)
						npcs[i].forceChat(voidKnightTalk[Misc.random3(voidKnightTalk.length)]);
				}
				if (npcs[i].actionTimer > 0) {
					npcs[i].actionTimer--;
				}
				if (npcs[i].freezeTimer > 0) {
					npcs[i].freezeTimer--;
				}
				if (npcs[i].hitDelayTimer > 0) {
					npcs[i].hitDelayTimer--;
				}
				if (npcs[i].hitDelayTimer == 1) {
					npcs[i].hitDelayTimer = 0;
					if (!(npcs[i].npcType >= 6142 && npcs[i].npcType <= 6145)) {
						applyDamage(i);
					}
				}
				if (npcs[i].killerId == 0 && !npcs[i].walkingHome && npcs[i].npcType == 1265) {
					npcs[i].untransformTimer--;
				}
				if (npcs[i].killerId == 0 && !npcs[i].walkingHome && npcs[i].npcType == 1265
						&& npcs[i].untransformTimer <= 0) {
					npcs[i].requestTransform(1266);
					npcs[i].npcType = 1266;
					npcs[i].walkingType = 0;
				}
				if (npcs[i].attackTimer > 0) {
					npcs[i].attackTimer--;
				}
				if (npcs[i].spawnedBy > 0) {
					if (PlayerHandler.players[npcs[i].spawnedBy] == null
							|| PlayerHandler.players[npcs[i].spawnedBy].heightLevel != npcs[i].heightLevel
							|| PlayerHandler.players[npcs[i].spawnedBy].getVariables().respawnTimer > 0
							|| !PlayerHandler.players[npcs[i].spawnedBy].goodDistance(npcs[i].getX(), npcs[i].getY(),
									PlayerHandler.players[npcs[i].spawnedBy].getX(),
									PlayerHandler.players[npcs[i].spawnedBy].getY(), 20)) {
						npcs[i] = null;
					}
				}
				if (npcs[i] == null)
					continue;
				if (isAggressive(i) && !npcs[i].underAttack && !npcs[i].isDead && !switchesAttackers(i)) {
					npcs[i].killerId = getCloseRandomPlayer(i);
				} else if (isAggressive(i) && !npcs[i].underAttack && !npcs[i].isDead && switchesAttackers(i)) {
					npcs[i].killerId = getCloseRandomPlayer(i);
				}
				if (System.currentTimeMillis() - npcs[i].lastDamageTaken > 5000)
					npcs[i].underAttackBy = 0;
				if ((npcs[i].killerId > 0 || npcs[i].underAttack) && !npcs[i].walkingHome
						&& retaliates(npcs[i].npcType)) {
					if (!npcs[i].isDead) {
						int p = npcs[i].killerId;
						if (PlayerHandler.players[p] != null) {
							Player c = PlayerHandler.players[p];
							if (!(npcs[i].npcType >= 6142 && npcs[i].npcType <= 6145)) {
								followPlayer(i, c.playerId);
							}
							if (npcs[i] == null)
								continue;
							if (npcs[i].attackTimer == 0) {
								if (c != null) {
									attackPlayer(c, i);
								} else {
									npcs[i].killerId = 0;
									npcs[i].underAttack = false;
									npcs[i].facePlayer(0);
								}
							}
						} else {
							npcs[i].killerId = 0;
							npcs[i].underAttack = false;
							npcs[i].facePlayer(0);
						}
					}
				}
				if (npcs[i] == null)
					continue;
				if ((!npcs[i].underAttack || npcs[i].walkingHome) && !isFightCaveNpc(i) && npcs[i].randomWalk
						&& !npcs[i].isDead) {
					npcs[i].facePlayer(0);
					npcs[i].killerId = 0;
					if (npcs[i].spawnedBy == 0) {
						if (!isFightCaveNpc(i) && (npcs[i].absX > npcs[i].makeX + Constants.NPC_RANDOM_WALK_DISTANCE)
								|| (npcs[i].absX < npcs[i].makeX - Constants.NPC_RANDOM_WALK_DISTANCE)
								|| (npcs[i].absY > npcs[i].makeY + Constants.NPC_RANDOM_WALK_DISTANCE)
								|| (npcs[i].absY < npcs[i].makeY - Constants.NPC_RANDOM_WALK_DISTANCE)) {
							npcs[i].walkingHome = true;
						}
					}
					if (npcs[i].walkingHome && npcs[i].absX == npcs[i].makeX && npcs[i].absY == npcs[i].makeY) {
						npcs[i].walkingHome = false;
					} else if (npcs[i].walkingHome) {
						npcs[i].moveX = GetMove(npcs[i].absX, npcs[i].makeX);
						npcs[i].moveY = GetMove(npcs[i].absY, npcs[i].makeY);

						npcs[i].getNextNPCMovement(i);
						npcs[i].updateRequired = true;
					}
					if (npcs[i].walkingType == 1) {
						if (Misc.random(3) == 1 && !npcs[i].walkingHome) {
							int MoveX = 0;
							int MoveY = 0;
							npcs[i].lastX = npcs[i].getX();
							npcs[i].lastY = npcs[i].getY();
							int Rnd = Misc.random(9);
							if (Rnd == 1) {
								MoveX = 1;
								MoveY = 1;
							} else if (Rnd == 2) {
								MoveX = -1;
							} else if (Rnd == 3) {
								MoveY = -1;
							} else if (Rnd == 4) {
								MoveX = 1;
							} else if (Rnd == 5) {
								MoveY = 1;
							} else if (Rnd == 6) {
								MoveX = -1;
								MoveY = -1;
							} else if (Rnd == 7) {
								MoveX = -1;
								MoveY = 1;
							} else if (Rnd == 8) {
								MoveX = 1;
								MoveY = -1;
							}
							if (MoveX == 1) {
								if (npcs[i].absX + MoveX < npcs[i].makeX + 1) {
									npcs[i].moveX = MoveX;
								} else {
									npcs[i].moveX = 0;
								}
							}
							if (MoveX == -1) {
								if (npcs[i].absX - MoveX > npcs[i].makeX - 1) {
									npcs[i].moveX = MoveX;
								} else {
									npcs[i].moveX = 0;
								}
							}
							if (MoveY == 1) {
								if (npcs[i].absY + MoveY < npcs[i].makeY + 1) {
									npcs[i].moveY = MoveY;
								} else {
									npcs[i].moveY = 0;
								}
							}
							if (MoveY == -1) {
								if (npcs[i].absY - MoveY > npcs[i].makeY - 1) {
									npcs[i].moveY = MoveY;
								} else {
									npcs[i].moveY = 0;
								}
							}

							npcs[i].getNextNPCMovement(i);
							npcs[i].updateRequired = true;
						}
					}
				}
				npcs[i].faceCurrentDirection();
				if (npcs[i].isDead && SummoningData.isSummonNpc(npcs[i].npcType)) {
					Player o = PlayerHandler.players[npcs[i].spawnedBy];
					o.getSummoning().dismissFamiliar();
				}
				if (npcs[i].isDead && !SummoningData.isSummonNpc(npcs[i].npcType)) {
					if (npcs[i].actionTimer == 0 && npcs[i].applyDead == false && npcs[i].needRespawn == false) {
						npcs[i].updateRequired = true;
						npcs[i].facePlayer(0);
						npcs[i].killedBy = getNpcKillerId(i);
						if (npcs[i].npcType >= 6142 && npcs[i].npcType <= 6145) {
						} else {
							if (!npcs[i].noDeathEmote)
								npcs[i].animNumber = getCombatEmote(i, "Death"); // dead
																					// emote
						}
						if (!npcs[i].noDeathEmote)
							npcs[i].animUpdateRequired = true;
						npcs[i].freezeTimer = 0;
						npcs[i].applyDead = true;
						npcs[i].splashDelay = -1;
						Barrows.appendBarrowsDeath(i);
						if (isFightCaveNpc(i)) {
							int p = npcs[i].killerId;
							Player c = PlayerHandler.players[p];
							if (c != null)
								killedTzhaar(i, c);
						}
						if (npcs[i].npcType == 1265) {
							Player c = PlayerHandler.players[NPCHandler.npcs[i].killerId];
							c.getVariables().rockCrabKills++;
						}
						if (!npcs[i].noDeathEmote)
							npcs[i].actionTimer = 4; // delete time
						else
							npcs[i].actionTimer = 0;
						resetPlayersInCombat(i);
					} else if (npcs[i].actionTimer == 0 && npcs[i].applyDead == true && npcs[i].needRespawn == false
							&& !SummoningData.isSummonNpc(npcs[i].npcType)) {
						npcs[i].needRespawn = true;
						npcs[i].actionTimer = getRespawnTime(i); // respawn time
						if (!npcs[i].inBarbDef())
							dropItems(i);
						tzhaarDeathHandler(i);
						Player p = PlayerHandler.players[NPCHandler.npcs[i].killedBy];
						if (NPCHandler.npcs[i] != null && p != null) {
							p.getSlayer().appendSlayerExperience(p, i, npcs[i]);
						}
						appendKillCount(i);
						npcs[i].absX = npcs[i].makeX;
						npcs[i].absY = npcs[i].makeY;
						npcs[i].HP = npcs[i].MaxHP;
						if (!npcs[i].noDeathEmote)
							npcs[i].animNumber = 0x328;
						npcs[i].updateRequired = true;
						if (!npcs[i].noDeathEmote)
							npcs[i].animUpdateRequired = true;
						if (npcs[i].npcType >= 2440 && npcs[i].npcType <= 2446) {
							GameEngine.objectManager.removeObject(npcs[i].absX, npcs[i].absY);
						}
						if (npcs[i].npcType == 1265) {
							Player c = PlayerHandler.players[NPCHandler.npcs[i].killerId];
							c.getVariables().rockCrabKills++;
						}
						if (npcs[i].npcType == 2745) {
							handleJadDeath(i);
						}
						if (npcs[i].type == 1) {
							npcs[i] = null;
						}
					} else if (npcs[i].actionTimer == 0 && npcs[i].needRespawn == true && npcs[i].npcType != 6142
							&& npcs[i].npcType != 6143 && npcs[i].npcType != 6144 && npcs[i].npcType != 6145) {
						if (npcs[i].spawnedBy > 0 || npcs[i].summonedFor > 0 || isFightCaveNpc(i)
								|| npcs[i].npcType >= 1532 && npcs[i].npcType <= 1535) {
							npcs[i] = null;
						} else {
							if (npcs[i].caught) {
								npcs[i] = null;
								return;
							}
							int type = npcs[i].npcType;
							if (type == 1160)
								type = 11158;
							int x = npcs[i].makeX;
							int y = npcs[i].makeY;
							int height = npcs[i].heightLevel;
							int walk = npcs[i].walkingType;
							int mHp = npcs[i].MaxHP;
							int mHit = npcs[i].maxHit / 10;
							int attack = npcs[i].attack;
							int defence = npcs[i].defence;

							npcs[i] = null;
							newNPC(type, x, y, height, walk, mHp, mHit, attack, defence, 0);
						}
					}
				}
				try {
					if (npcs[i].teleportDelay > 0) {
						npcs[i].teleportDelay--;
					} else if (npcs[i].teleportDelay == 0) {
						npcs[i].absX = npcs[i].teleX;
						npcs[i].absY = npcs[i].teleY;
						npcs[i].heightLevel = npcs[i].teleHeight;
						npcs[i].needRespawn = false;
						for (int plr = 0; plr < PlayerHandler.players.length; plr++) {
							if (PlayerHandler.players[plr] != null)
								PlayerHandler.players[plr].getVariables().RebuildNPCList = true;
						}
						npcs[i].gfx0(1315);
						npcs[i].updateRequired = true;
						npcs[i].teleportDelay = -1;
					}
				} catch (Exception _ex) {
				}
			}
		}
	}

	/**
	 * 
	 * Summoning follow
	 */
	public void summoningFollow(int i, int playerId) {
		if (PlayerHandler.players[playerId] == null) {
			return;
		}
		NPC n = npcs[i];
		Player c = PlayerHandler.players[playerId];
		if (!c.goodDistance(c.getX(), c.getY(), c.getVariables().summoned.getX(), c.getVariables().summoned.getY(),
				8)) {// testing to see if this shit works when u teleport
			c.getSummoning().callFamiliar();
			n.underAttackBy = 0;
			return;
		}
		if (n.underAttackBy == 0) {

			int playerX = c.getX();
			int playerY = c.getY();
			n.randomWalk = false;
			if (n.heightLevel == PlayerHandler.players[playerId].heightLevel
					&& goodDistance(n.getX(), n.getY(), playerX, playerY, 8)) {
				if (c != null && n != null) {
					if (playerY < n.absY) {
						n.moveX = GetMove(n.absX, playerX);
						n.moveY = GetMove(n.absY, playerY + 2);
					} else if (playerY > n.absY) {
						n.moveX = GetMove(n.absX, playerX);
						n.moveY = GetMove(n.absY, playerY - 2);
					} else if (playerX < n.absX) {
						n.moveX = GetMove(n.absX, playerX + 2);
						n.moveY = GetMove(n.absY, playerY);
					} else if (playerX > n.absX) {
						n.moveX = GetMove(n.absX, playerX - 2);
						n.moveY = GetMove(n.absY, playerY);
					} else if (playerX == n.absX || playerY == n.absY) {
						int o = Misc.random(3);
						switch (o) {
						case 0:
							n.moveX = GetMove(n.absX, playerX);
							n.moveY = GetMove(n.absY, playerY + 1);
							break;

						case 1:
							n.moveX = GetMove(n.absX, playerX);
							n.moveY = GetMove(n.absY, playerY - 1);
							break;

						case 2:
							n.moveX = GetMove(n.absX, playerX + 1);
							n.moveY = GetMove(n.absY, playerY);
							break;

						case 3:
							n.moveX = GetMove(n.absX, playerX - 1);
							n.moveY = GetMove(n.absY, playerY);
							break;
						}
					}
				}
			} else {
				c.getSummoning().callFamiliar();
				n.underAttackBy = 0;
			}

			n.getNextNPCMovement(i);
			if (n.IsAttackingNPC != true)
				n.facePlayer(playerId);
			n.randomWalk = false;
			n.updateRequired = true;
		}
	}

	/**
	 * 
	 * Npc following player
	 */
	public void followPlayer(int i, int playerId, int otherId) {
		Player O = PlayerHandler.players[otherId];
		if (PlayerHandler.players[playerId] == null) {
			return;
		}
		if (npcs[i].underAttackBy != 0 || O.getVariables().underAttackBy != 0 || O.getVariables().playerIndex != 0) {
			if (PlayerHandler.players[playerId].getVariables().respawnTimer > 0) {
				npcs[i].facePlayer(0);
				npcs[i].randomWalk = true;
				npcs[i].underAttack = false;
				return;
			}
			if (!followPlayer(i)) {
				if (!npcs[i].IsAttackingNPC)
					npcs[i].facePlayer(playerId);
				return;
			}
			int playerX = PlayerHandler.players[playerId].absX;
			int playerY = PlayerHandler.players[playerId].absY;
			npcs[i].randomWalk = false;
			if (goodDistance(npcs[i].getX(), npcs[i].getY(), playerX, playerY, distanceRequired(i))
					&& goodDistance(npcs[i].getX(), npcs[i].getY(), playerX, playerY, distanceRequired(i)))
				return;
			if ((npcs[i].spawnedBy > 0) || ((npcs[i].absX < npcs[i].makeX + Constants.NPC_FOLLOW_DISTANCE)
					&& (npcs[i].absX > npcs[i].makeX - Constants.NPC_FOLLOW_DISTANCE)
					&& (npcs[i].absY < npcs[i].makeY + Constants.NPC_FOLLOW_DISTANCE)
					&& (npcs[i].absY > npcs[i].makeY - Constants.NPC_FOLLOW_DISTANCE))) {
				if (npcs[i].heightLevel == PlayerHandler.players[playerId].heightLevel) {
					if (PlayerHandler.players[playerId] != null && npcs[i] != null) {
						if (playerY < npcs[i].absY) {
							npcs[i].moveX = GetMove(npcs[i].absX, playerX);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY + 1);
						} else if (playerY > npcs[i].absY) {
							npcs[i].moveX = GetMove(npcs[i].absX, playerX);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY - 1);
						} else if (playerX < npcs[i].absX) {
							npcs[i].moveX = GetMove(npcs[i].absX, playerX + 1);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY);
						} else if (playerX > npcs[i].absX) {
							npcs[i].moveX = GetMove(npcs[i].absX, playerX - 1);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY);
						} else if (playerX == npcs[i].absX || playerY == npcs[i].absY) {
							int o = Misc.random(3);
							switch (o) {
							case 0:
								npcs[i].moveX = GetMove(npcs[i].absX, playerX);
								npcs[i].moveY = GetMove(npcs[i].absY, playerY + 1);
								break;
							case 1:
								npcs[i].moveX = GetMove(npcs[i].absX, playerX);
								npcs[i].moveY = GetMove(npcs[i].absY, playerY - 1);
								break;
							case 2:
								npcs[i].moveX = GetMove(npcs[i].absX, playerX + 1);
								npcs[i].moveY = GetMove(npcs[i].absY, playerY);
								break;
							case 3:
								npcs[i].moveX = GetMove(npcs[i].absX, playerX - 1);
								npcs[i].moveY = GetMove(npcs[i].absY, playerY);
								break;
							}
						}

						npcs[i].getNextNPCMovement(i);
						if (npcs[i].IsAttackingNPC != true)
							npcs[i].facePlayer(playerId);
						npcs[i].updateRequired = true;
					}
				}
			}
		}
	}

	/**
	 * Npc killer id?
	 **/
	public int getNpcKillerId(int npcId) {
		int oldDamage = 0;
		int killerId = 0;
		for (int p = 1; p < Constants.MAX_PLAYERS; p++) {
			if (PlayerHandler.players[p] != null) {
				if (PlayerHandler.players[p].getVariables().lastNpcAttacked == npcId) {
					if (PlayerHandler.players[p].getVariables().totalDamageDealt > oldDamage) {
						oldDamage = PlayerHandler.players[p].getVariables().totalDamageDealt;
						killerId = p;
					}
					PlayerHandler.players[p].getVariables().totalDamageDealt = 0;
				}
			}
		}
		return killerId;
	}

	/**
	 * 
	 * @param i
	 */
	public void dropItems(int i) {
		Player c = PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			Godwars.giveKillcount(c, npcs[i].npcType);
			if (npcs[i].npcType == 912 || npcs[i].npcType == 913 || npcs[i].npcType == 914)
				c.getVariables().magePoints += 1;
			if (NPCDrops.constantDrops.get(npcs[i].npcType) != null) {
				for (final int item : NPCDrops.constantDrops.get(npcs[i].npcType)) {
					ItemHandler.createGroundItem(c, item, npcs[i].absX, npcs[i].absY, npcs[i].heightLevel, 1,
							c.playerId);
				}
			}
			if (NPCDrops.dropRarity.get(npcs[i].npcType) != null) {
				try {
					int random = Misc.random(NPCDrops.rareDrops.get(npcs[i].npcType).length - 1);
					int item = 0;
					int amount = 0;
					if (rareDrops(i)) {
						item = NPCDrops.rareDrops.get(npcs[i].npcType)[random][0];
						amount = NPCDrops.rareDrops.get(npcs[i].npcType)[random][1];
					} else {
						random = Misc.random(NPCDrops.normalDrops.get(npcs[i].npcType).length - 1);
						item = NPCDrops.normalDrops.get(npcs[i].npcType)[random][0];
						amount = NPCDrops.normalDrops.get(npcs[i].npcType)[random][1];
					}
					if (c.getVariables().clanId > -1 && c.getShops().getItemShopValue(item) * amount > 5000) {
						if (GameEngine.clanChat.clans[c.getVariables().clanId].lootshare == 1
								|| GameEngine.clanChat.clans[c.getVariables().clanId].lootshare == 2) {
							GameEngine.clanChat.handleShare(c, item, amount, npcs[i].absX, npcs[i].absY,
									npcs[i].heightLevel,
									GameEngine.clanChat.clans[c.getVariables().clanId].lootshare == 1 ? false : true);
							return;
						}
					}
					ItemHandler.createGroundItem(c, item, npcs[i].absX, npcs[i].absY, npcs[i].heightLevel, amount,
							c.playerId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 * @param i
	 */
	public void appendKillCount(int i) {
		Player c = PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			int[] kcMonsters = { 122, 49, 2558, 2559, 2560, 2561, 2550, 2551, 2552, 2553, 2562, 2563, 2564, 2565 };
			for (int j : kcMonsters) {
				if (npcs[i].npcType == j) {
					if (c.getVariables().killCount < 20) {
						c.getVariables().killCount++;
						c.sendMessage("Killcount: " + c.getVariables().killCount);
					} else {
						c.sendMessage("You already have 20 kill count");
					}
					break;
				}
			}
		}
	}

	/**
	 * Resets players in combat
	 */
	public void resetPlayersInCombat(int i) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null)
				if (PlayerHandler.players[j].getVariables().underAttackBy2 == i)
					PlayerHandler.players[j].getVariables().underAttackBy2 = 0;
		}
	}

	/**
	 * Npc Follow Player
	 **/
	public int GetMove(int Place1, int Place2) {
		if ((Place1 - Place2) == 0) {
			return 0;
		} else if ((Place1 - Place2) < 0) {
			return 1;
		} else if ((Place1 - Place2) > 0) {
			return -1;
		}
		return 0;
	}

	/**
	 * 
	 * @param i
	 * @return
	 */
	public boolean followPlayer(int i) {
		switch (npcs[i].npcType) {
		case 2892:
		case 2894:
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param i
	 * @param playerId
	 */
	public void followPlayer(int i, int playerId) {
		if (PlayerHandler.players[playerId] == null) {
			return;
		}
		if (PlayerHandler.players[playerId].getVariables().respawnTimer > 0) {
			npcs[i].facePlayer(0);
			npcs[i].randomWalk = true;
			npcs[i].underAttack = false;
			return;
		}

		if (!followPlayer(i)) {
			if (!npcs[i].IsAttackingNPC)
				npcs[i].facePlayer(playerId);
			return;
		}
		int playerX = PlayerHandler.players[playerId].absX;
		int playerY = PlayerHandler.players[playerId].absY;
		npcs[i].randomWalk = false;
		if (goodDistance(npcs[i].getX(), npcs[i].getY(), playerX, playerY, distanceRequired(
				i))/*
					 * && goodDistance(playerX, playerY,npcs[i].getX(),
					 * npcs[i].getY(), NPCHandler.npcs[i].getNpcSize()-1)
					 */)
			return;
		if ((isFightCaveNpc(i)
				|| ((npcs[i].spawnedBy > 0) || ((npcs[i].absX < npcs[i].makeX + Constants.NPC_FOLLOW_DISTANCE)
						&& (npcs[i].absX > npcs[i].makeX - Constants.NPC_FOLLOW_DISTANCE)
						&& (npcs[i].absY < npcs[i].makeY + Constants.NPC_FOLLOW_DISTANCE)
						&& (npcs[i].absY > npcs[i].makeY - Constants.NPC_FOLLOW_DISTANCE))))
				|| npcs[i].type == 1) {
			if (npcs[i].heightLevel == PlayerHandler.players[playerId].heightLevel) {
				if (PlayerHandler.players[playerId] != null && npcs[i] != null) {
					if (playerY < npcs[i].absY) {
						npcs[i].moveX = GetMove(npcs[i].absX, playerX);
						npcs[i].moveY = GetMove(npcs[i].absY, playerY + 1);
					} else if (playerY > npcs[i].absY) {
						npcs[i].moveX = GetMove(npcs[i].absX, playerX);
						npcs[i].moveY = GetMove(npcs[i].absY, playerY - 1);
					} else if (playerX < npcs[i].absX) {
						npcs[i].moveX = GetMove(npcs[i].absX, playerX + 1);
						npcs[i].moveY = GetMove(npcs[i].absY, playerY);
					} else if (playerX > npcs[i].absX) {
						npcs[i].moveX = GetMove(npcs[i].absX, playerX - 1);
						npcs[i].moveY = GetMove(npcs[i].absY, playerY);
					} else if (playerX == npcs[i].absX || playerY == npcs[i].absY) {
						int o = Misc.random(3);
						switch (o) {
						case 0:
							npcs[i].moveX = GetMove(npcs[i].absX, playerX);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY + 1);
							break;
						case 1:
							npcs[i].moveX = GetMove(npcs[i].absX, playerX);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY - 1);
							break;
						case 2:
							npcs[i].moveX = GetMove(npcs[i].absX, playerX + 1);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY);
							break;
						case 3:
							npcs[i].moveX = GetMove(npcs[i].absX, playerX - 1);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY);
							break;
						}
					}
					/*
					 * if(playerY < npcs[i].absY) { npcs[i].moveX =
					 * GetMove(npcs[i].absX, playerX); npcs[i].moveY =
					 * GetMove(npcs[i].absY, playerY + distance(npcs[i].absY,
					 * playerY, npcs[i].getNpcSize())); } else if(playerY >
					 * npcs[i].absY) { npcs[i].moveX = GetMove(npcs[i].absX,
					 * playerX); npcs[i].moveY = GetMove(npcs[i].absY, playerY -
					 * distance(npcs[i].absY, playerY, npcs[i].getNpcSize())); }
					 * else if(playerX < npcs[i].absX) { npcs[i].moveX =
					 * GetMove(npcs[i].absX, playerX + distance(npcs[i].absX,
					 * playerX, npcs[i].getNpcSize())); npcs[i].moveY =
					 * GetMove(npcs[i].absY, playerY); } else if(playerX >
					 * npcs[i].absX) { npcs[i].moveX = GetMove(npcs[i].absX,
					 * playerX - distance(npcs[i].absX, playerX,
					 * npcs[i].getNpcSize())); npcs[i].moveY =
					 * GetMove(npcs[i].absY, playerY); } else if(playerX ==
					 * npcs[i].absX || playerY == npcs[i].absY) { int o =
					 * Misc.random(3); switch(o) { case 0: npcs[i].moveX =
					 * GetMove(npcs[i].absX, playerX); npcs[i].moveY =
					 * GetMove(npcs[i].absY, playerY+distance(npcs[i].absY,
					 * playerY, npcs[i].getNpcSize())); break;
					 * 
					 * case 1: npcs[i].moveX = GetMove(npcs[i].absX, playerX);
					 * npcs[i].moveY = GetMove(npcs[i].absY,
					 * playerY-distance(npcs[i].absY, playerY,
					 * npcs[i].getNpcSize())); break;
					 * 
					 * case 2: npcs[i].moveX = GetMove(npcs[i].absX,
					 * playerX+distance(npcs[i].absX, playerX,
					 * npcs[i].getNpcSize())); npcs[i].moveY =
					 * GetMove(npcs[i].absY, playerY); break;
					 * 
					 * case 3: npcs[i].moveX = GetMove(npcs[i].absX,
					 * playerX-distance(npcs[i].absX, playerX,
					 * npcs[i].getNpcSize())); npcs[i].moveY =
					 * GetMove(npcs[i].absY, playerY); break; } }
					 */
					if (!npcs[i].IsAttackingNPC)
						npcs[i].facePlayer(playerId);

					npcs[i].getNextNPCMovement(i);
					if (!npcs[i].IsAttackingNPC)
						npcs[i].facePlayer(playerId);
					npcs[i].updateRequired = true;
				}
			}
		} else {
			npcs[i].facePlayer(0);
			npcs[i].randomWalk = true;
			npcs[i].underAttack = false;
		}
	}

	/**
	 * NPC Attacking Player
	 *
	 **/

	public void attackPlayer(Player c, int i) {
		if (npcs[i] != null) {
			if (npcs[i].isDead)
				return;
			if (c == null)
				return;
			if (!npcs[i].inMulti() && npcs[i].underAttackBy > 0 && npcs[i].underAttackBy != c.playerId) {
				npcs[i].killerId = 0;
				return;
			}
			if (!npcs[i].inMulti() && (c.getVariables().underAttackBy > 0
					|| (c.getVariables().underAttackBy2 > 0 && c.getVariables().underAttackBy2 != i))) {
				npcs[i].killerId = 0;
				return;
			}
			if (npcs[i].heightLevel != c.heightLevel) {
				npcs[i].killerId = 0;
				return;
			}
			npcs[i].facePlayer(c.playerId);
			if (!goodDistance(npcs[i].getX(), npcs[i].getY(), c.getY(), c.getX(), 1) && npcs[i].npcType == 8133
					&& npcs[i].attackType == 0) {
				npcs[i].attackType = 1 + Misc.random(1);
				return;
			}
			boolean special = false;// specialCase(c,i);
			if (goodDistance(npcs[i].getX(), npcs[i].getY(), c.getX(), c.getY(), distanceRequired(i)) || special) {
				if (c.getVariables().respawnTimer <= 0) {
					handleSpecialNPC(npcs[i]);
					/*
					 * if(Region.blockedShot(c.getX(), c.getY(), c.heightLevel,
					 * NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY()) ) {
					 * if((npcs[i].projectileId > 0)) { followPlayer(i,
					 * c.playerId); npcs[i].attackTimer = 0; return; } }
					 */
					if (pathBlocked(npcs[i], c) && (npcs[i].projectileId > 0))
						return;
					npcs[i].facePlayer(c.playerId);
					npcs[i].attackTimer = getNpcDelay(i);
					npcs[i].hitDelayTimer = getHitDelay(i);
					npcs[i].attackType = 0;
					if (npcs[i].npcType == 8133) {
						corpAttack(c, i, Misc.random(2));
						if (npcs[i].attackType == 5)
							npcs[i].hitDelayTimer = -1;
					} else {
						npcs[i].id = Misc.random(15);
						loadSpell(c, i);
					}
					usingSpecial = false;
					if (npcs[i].attackType == 3)
						npcs[i].hitDelayTimer += 2;
					if (multiAttacks(i)) {
						multiAttackGfx(i, npcs[i].projectileId);
						startAnimation(getCombatEmote(i, "Attack"), i);
						npcs[i].oldIndex = c.playerId;
						return;
					}
					if (npcs[i].projectileId > 0) {
						int nX = NPCHandler.npcs[i].getX() + offset(i);
						int nY = NPCHandler.npcs[i].getY() + offset(i);
						int pX = c.getX();
						int pY = c.getY();
						int offX = (nY - pY) * -1;
						int offY = (nX - pX) * -1;
						// c.getPA().createPlayersProjectile(nX, nY, offX, offY,
						// 50, getProjectileSpeed(i), npcs[i].projectileId, 43,
						// 31, !npcs[i].splash ? -c.getId() - 1 : 0, 65);
						c.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, getProjectileSpeed(i),
								npcs[i].projectileId, 43, 31, !npcs[i].splash ? -c.getId() - 1 : 0, 65);

					}
					c.getVariables().underAttackBy2 = i;
					c.getVariables().singleCombatDelay2 = System.currentTimeMillis();
					npcs[i].oldIndex = c.playerId;
					if (npcs[i].npcType != 8133 && !usingSpecial)
						startAnimation(getCombatEmote(i, "Attack"), i);
					c.getPA().removeAllWindows();
				}
			} else {
				followPlayer(i, c.playerId);
			}
		}
	}

	public static boolean multiAttacks(int i) {
		switch (NPCHandler.npcs[i].npcType) {
		case 6222:// armadyl?
			return true;
		case 8565:// lesser reborn mage
			return true;
		case 8133: // corporeal beast
			if (npcs[i].attackType == 2)
				return true;
		case 6247:// saradomin?
			if (npcs[i].attackType == 2)
				return true;
		case 6260:// bandos?
			if (npcs[i].attackType == 1)
				return true;
		case 8528:// nomad
			if (npcs[i].attackType == 2)
				return true;
		default:
			return false;
		}

	}

	public static void multiAttackGfx(int i, int gfx) {
		if (NPCHandler.npcs[i].projectileId < 0)
			return;
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c = PlayerHandler.players[j];
				if (c.heightLevel != NPCHandler.npcs[i].heightLevel)
					continue;
				if (PlayerHandler.players[j].goodDistance(c.absX, c.absY, NPCHandler.npcs[i].absX,
						NPCHandler.npcs[i].absY, 15)) {
					int nX = NPCHandler.npcs[i].getX() + NPCHandler.offset(i);
					int nY = NPCHandler.npcs[i].getY() + NPCHandler.offset(i);
					int pX = c.getX();
					int pY = c.getY();
					int offX = (nY - pY) * -1;
					int offY = (nX - pX) * -1;
					c.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, getProjectileSpeed(i),
							NPCHandler.npcs[i].projectileId, 43, 31, -c.getId() - 1, 65);
				}
			}
		}
	}

	public static int getProjectileSpeed(int i) {
		switch (npcs[i].npcType) {
		case 2881:
		case 2882:
		case 3200:
			return 85;
		case 8133:
			return 100;
		case 2745:
			return 130;

		case 50:
			return 90;

		case 2025:
			return 85;

		case 2028:
			return 80;

		default:
			return 85;
		}
	}

	public void loadSpell(Player c, int i) {
		try {

			if (npcs[i] == null)
				return;
			if (c == null)
				return;
			if (NPCHandler.npcs[i].npcType >= 10328 && NPCHandler.npcs[i].npcType <= 10363) {
				npcs[i].projectileId = 13;
				npcs[i].attackType = 1;
			}
			if (npcs[i].npcType >= 10570 && NPCHandler.npcs[i].npcType <= 10603) {
				npcs[i].gfx0(Player.MAGIC_SPELLS[NPCHandler.npcs[i].id][3]);
				npcs[i].projectileId = Player.MAGIC_SPELLS[NPCHandler.npcs[i].id][4];
				npcs[i].attackType = 2;
				npcs[i].endGfx = Player.MAGIC_SPELLS[NPCHandler.npcs[i].id][5];
			}
			switch (npcs[i].npcType) {
			/* 0 - melee, 1 - range, 2 - mage */
			case 5247: // Penance queen
				int penance = Misc.random(1);
				if (penance == 0) {
					npcs[i].projectileId = 871;
					npcs[i].endGfx = 872;
					npcs[i].attackType = 1;
				} else {
					npcs[i].projectileId = -1;
					npcs[i].endGfx = -1;
					npcs[i].attackType = 0;
				}
				break;
			case 1183: // Elf warrior (ranged)
				npcs[i].gfx100(250);
				npcs[i].projectileId = 249;
				npcs[i].attackType = 1;
				break;
			case 5229:
			case 5230:
			case 5231:
			case 5232:
			case 5233:
			case 5234:
			case 5235:
			case 5236:
			case 5237: // Penance ranger (ranged)
				npcs[i].projectileId = 866;
				npcs[i].endGfx = 865;
				npcs[i].attackType = 1;
				break;
			case 2892: // Spinolyp (mage)
				npcs[i].projectileId = 94;
				npcs[i].attackType = 2;
				npcs[i].endGfx = 95;
				break;
			case 2894: // Spinolyp (ranged)
				npcs[i].projectileId = 298;
				npcs[i].attackType = 1;
				break;
			case 50:
				int r5 = 0;
				if (goodDistance(npcs[i].absX, npcs[i].absY, PlayerHandler.players[npcs[i].killerId].absX,
						PlayerHandler.players[npcs[i].killerId].absY, 2))
					r5 = Misc.random(5);
				else
					r5 = Misc.random(3);
				if (r5 == 0) {
					npcs[i].projectileId = 393; // red
					npcs[i].attackType = 3;
				} else if (r5 == 1) {
					npcs[i].projectileId = 394; // green
					npcs[i].attackType = 2;
					if (c.getVariables().poisonDamage <= 0) {
						c.getPA().appendPoison(8);
					}
				} else if (r5 == 2) {
					npcs[i].projectileId = 395; // white
					npcs[i].attackType = 2;
					if (c.getVariables().freezeTimer <= 0) {
						c.getVariables().freezeTimer = 19;
						c.sendMessage("You have been Frozen!");
					}
				} else if (r5 == 3) {
					npcs[i].projectileId = 396; // blue
					npcs[i].attackType = 2;
				} else if (r5 == 4) {
					npcs[i].projectileId = -1; // melee
					npcs[i].attackType = 0;
				} else if (r5 == 5) {
					npcs[i].projectileId = -1; // melee
					npcs[i].attackType = 0;
				}
				break;
			case 2025:
				npcs[i].attackType = 2;
				int r = Misc.random(3);
				if (r == 0) {
					npcs[i].gfx100(158);
					npcs[i].projectileId = 159;
					npcs[i].endGfx = 160;
				}
				if (r == 1) {
					npcs[i].gfx100(161);
					npcs[i].projectileId = 162;
					npcs[i].endGfx = 163;
				}
				if (r == 2) {
					npcs[i].gfx100(164);
					npcs[i].projectileId = 165;
					npcs[i].endGfx = 166;
				}
				if (r == 3) {
					npcs[i].gfx100(155);
					npcs[i].projectileId = 156;
				}
				break;
			case 2881:// supreme
				npcs[i].attackType = 1;
				npcs[i].projectileId = 298;
				break;

			case 2882:// prime
				npcs[i].attackType = 2;
				npcs[i].projectileId = 162;
				npcs[i].endGfx = 477;
				break;

			case 2028:
				npcs[i].attackType = 1;
				npcs[i].projectileId = 27;
				break;

			case 3200:
				int r2 = Misc.random(1);
				if (r2 == 0) {
					npcs[i].attackType = 1;
					npcs[i].gfx100(550);
					npcs[i].projectileId = 551;
					npcs[i].endGfx = 552;
				} else {
					npcs[i].attackType = 2;
					npcs[i].gfx100(553);
					npcs[i].projectileId = 554;
					npcs[i].endGfx = 555;
				}
				break;
			case 2745:
				int r3 = 0;
				if (goodDistance(npcs[i].absX, npcs[i].absY, PlayerHandler.players[npcs[i].spawnedBy].absX,
						PlayerHandler.players[npcs[i].spawnedBy].absY, 1))
					r3 = Misc.random(2);
				else
					r3 = Misc.random(1);
				if (r3 == 0) {
					npcs[i].attackType = 2;
					npcs[i].endGfx = 157;
					npcs[i].projectileId = 1627;
				} else if (r3 == 1) {
					npcs[i].attackType = 1;
					npcs[i].endGfx = 451;
					npcs[i].gfx100(1625);
					npcs[i].projectileId = -1;
				} else if (r3 == 2) {
					npcs[i].attackType = 0;
					npcs[i].projectileId = -1;
				}
				break;
			case 6261:// strongstack
				npcs[i].attackType = 0;
				break;
			case 6263:// steelwill
				npcs[i].attackType = 2;
				npcs[i].projectileId = 1203;
				break;
			case 6265:// grimspike
				npcs[i].attackType = 1;
				npcs[i].projectileId = 1206;
				break;
			case 53:
			case 54:
			case 55:
			case 941:
			case 1590:
			case 1591:
			case 1592:
			case 10604:
			case 10605:
			case 10606:
			case 10607:
			case 10608:
			case 10609:
			case 10219:
			case 10220:
			case 10221:
			case 10222:
			case 10223:
			case 10224:
			case 10776:
			case 10777:
			case 10778:
			case 10779:
			case 10780:
			case 10781:
			case 10815:
			case 10816:
			case 10817:
			case 10818:
			case 10819:
			case 10820:
				int r6 = 0;
				r6 = Misc.random(2);
				if (r6 == 0) {
					npcs[i].attackType = 3;
					npcs[i].projectileId = 393; // red
				} else if (r6 == 1) {
					npcs[i].attackType = 3;
					npcs[i].projectileId = 393; // red
				} else if (r6 == 2) {
					npcs[i].attackType = 0;
					npcs[i].projectileId = 0; // melee
				}
				break;
			// arma npcs
			case 6227:// kilisa
				npcs[i].attackType = 0;
				break;
			case 6225:// geerin
			case 6233:
			case 6230:
				npcs[i].attackType = 1;
				npcs[i].projectileId = 1190;
				break;
			case 6239:
				npcs[i].attackType = 1;
				npcs[i].projectileId = 1191;
				break;
			case 6232:
				npcs[i].attackType = 1;
				npcs[i].projectileId = 1191;
				break;
			case 6276:
				npcs[i].attackType = 1;
				npcs[i].projectileId = 1195;
				break;
			case 6223:// skree
				npcs[i].attackType = 2;
				npcs[i].projectileId = 1199;
				break;
			case 6257:// saradomin strike
				npcs[i].attackType = 2;
				npcs[i].endGfx = 76;
				break;
			case 6221:// zamorak strike
				npcs[i].attackType = 2;
				npcs[i].endGfx = 78;
				break;
			case 6231:// arma
				npcs[i].attackType = 2;
				npcs[i].projectileId = 1199;
				break;
			case 6222:// kree
				int random = Misc.random(1);
				npcs[i].attackType = 1 + random;
				if (npcs[i].attackType == 1) {
					npcs[i].projectileId = 1197;
				} else {
					npcs[i].attackType = 2;
					npcs[i].projectileId = 1198;
				}
				break;
			// sara npcs
			case 6247: // sara
				random = Misc.random(1);
				if (random == 0) {
					npcs[i].attackType = 2;
					npcs[i].endGfx = 1224;
					npcs[i].projectileId = -1;
				} else if (random == 1)
					npcs[i].attackType = 0;
				break;
			case 6248: // star
				npcs[i].attackType = 0;
				break;
			case 6250: // growler
				npcs[i].attackType = 2;
				npcs[i].projectileId = 1203;
				break;
			case 6252: // bree
				npcs[i].attackType = 1;
				npcs[i].projectileId = 9;
				break;
			// bandos npcs
			case 6260:// bandos
				random = Misc.random(2);
				if (random == 0 || random == 1) {
					npcs[i].attackType = 0;
				} else {
					npcs[i].attackType = 1;
					// npcs[i].projectileId = 1200;
				}
				break;
			case 3102:
				npcs[i].attackType = 1;
				npcs[i].projectileId = 1839;
				break;
			case 3103:
				npcs[i].attackType = 2;
				npcs[i].projectileId = 1828;
				break;
			case 2743:
				npcs[i].attackType = 2;
				npcs[i].projectileId = 445;
				npcs[i].endGfx = 446;
				break;

			case 2631:
				npcs[i].attackType = 1;
				npcs[i].projectileId = 443;
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Distanced required to attack
	 **/
	public static int distanceRequired(int i) {
		if (NPCHandler.npcs[i].npcType >= 10328 && NPCHandler.npcs[i].npcType <= 10363) {
			return 8;
		}
		if (npcs[i].npcType >= 10570 && NPCHandler.npcs[i].npcType <= 10603) {
			return 8;
		}
		switch (NPCHandler.npcs[i].npcType) {
		case 8349:
		case 8350:
		case 8351:
			return 1;
		case 6247:
			return 2;
		case 6220:
		case 6276:
		case 6256:
		case 6230:
		case 6239:
		case 6221:
		case 6231:
		case 6257:
		case 6278:
		case 8133:
		case 6233:
		case 6232:
			return 5;
		case 6263:
		case 6265:
		case 6206:
		case 6208:
		case 6222:
		case 6223:
		case 6225:
		case 6250:
		case 6252:
			return 15;
		case 2025:
		case 2028:
			return 6;
		case 50:
		case 2562:
			return 2;
		case 2881:// dag kings
		case 2882:
		case 3200:// chaos ele
		case 2743:
		case 2631:
		case 2745:
		case 5229:
		case 5230:
		case 5231:
		case 5232:
		case 5233:
		case 5234:
		case 5235:
		case 5236:
		case 5237: // Penance rangers
		case 2883:// rex
			return 1;
		// things around dags
		case 2892:
		case 2894:
			return 10;
		default:
			return npcSizes.length > NPCHandler.npcs[i].npcType ? npcSizes[NPCHandler.npcs[i].npcType] : 1;
		}
	}

	public static void killedTzhaar(int i, final Player c2) {
		CycleEventHandler.getInstance().addEvent(c2, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (c2 != null) {
					GameEngine.fightCaves.spawnNextWave(c2);
				}
				container.stop();
			}

			@Override
			public void stop() {
			}
		}, 13);
	}

	public boolean switchesAttackers(int i) {
		switch (npcs[i].npcType) {
		case 2892:
		case 2894:
		case 8133:
		case 50:
		case 6261:
		case 6263:
		case 6265:
		case 6223:
		case 6225:
		case 6227:
		case 6248:
		case 6250:
		case 6252:
		case 6206:
		case 6208:
		case 6204:
		case 8528:
		case 10057:
		case 10141:
		case 9964:
		case 9947:
		case 10127:
		case 10219:
		case 10220:
		case 10221:
		case 10222:
		case 10223:
		case 10776:
		case 10777:
		case 10778:
		case 10779:
		case 10780:
		case 10781:
		case 10815:
		case 10816:
		case 10817:
		case 10818:
		case 10819:
		case 10820:
		case 10224:
		case 10604:
		case 10605:
		case 10606:
		case 10607:
		case 10608:
		case 10609:
		case 10770:
		case 10771:
		case 10772:
		case 10773:
		case 10774:
		case 10775:
			return true;
		}

		return false;
	}

	public static boolean isAggressive(int i) {
		if (NPCHandler.npcs[i].type == 1 && NPCHandler.npcs[i].npcType != 11226)
			return true;
		if (PestControl.npcIsPCMonster(NPCHandler.npcs[i].npcType)) {
			return true;
		}
		switch (NPCHandler.npcs[i].npcType) {
		case 1265:
		case 2550:
		case 3622:
		case 2892:
		case 2894:
		case 2881:
		case 2882:
		case 2883:
		case 75:
		case 90:
		case 103:
		case 941:
		case 82:
		case 86:
		case 78:
		case 84:
		case 1593:
		case 1590:
		case 1591:
		case 1592:
		case 55:
		case 8133:
		case 8528:
		case 1158:
		case 1160:
		case 1154:
		case 1157:
		case 1156:
		case 795:
		case 5210:
		case 3101:
		case 3102:
		case 5666:
		case 8565:
		case 50:
		case 10141:
		case 3340:
		case 3103:
		case 5219:
		case 5237:
			return true;
		case 6203:
		case 6204:
		case 6206:
		case 6208:
		case 6260:
		case 6261:
		case 6263:
		case 6265:
		case 6222:
		case 6223:
		case 6225:
		case 6227:
			if (NPCHandler.npcs[i].heightLevel == 6)
				return true;
			break;
		case 6247:
		case 6248:
		case 6250:
		case 6252:
			if (NPCHandler.npcs[i].heightLevel == 4)
				return true;
			break;

		}
		if (isFightCaveNpc(i))
			return true;
		return false;
	}

	public static boolean rareDrops(int i) {
		return Misc.random(NPCDrops.dropRarity.get(NPCHandler.npcs[i].npcType)) == 0;
	}

	public String[] voidKnightTalk = { "We must not fail!", "Take down the portals", "The Void Knights will not fall!",
			"Hail the Void Knights!", "We are beating these scum!" };

	public int followDistance(int i) {
		switch (npcs[i].npcType) {
		case 6260:
		case 6261:
		case 6247:
		case 6248:
		case 6223:
		case 6225:
		case 6227:
		case 6203:
		case 6204:
		case 6206:
		case 6208:
		case 6250:
		case 6252:
		case 6263:
		case 8133:
		case 6265:
			return 15;
		case 3247:
		case 6270:
		case 6219:
		case 6255:
		case 6229:
		case 6277:
		case 6233:
		case 6232:
		case 6218:
		case 6269:
		case 3248:
		case 6212:
		case 6220:
		case 6276:
		case 6256:
		case 6230:
		case 6239:
		case 6221:
		case 6231:
		case 6257:
		case 6278:
		case 6272:
		case 6274:
		case 6254:
		case 4291: // Cyclops
		case 4292: // Ice cyclops
		case 6258:
		case 8349:
		case 8350:
		case 8351:
		case 10141:
			return 7;
		case 50:
		case 10770:
		case 10771:
		case 10772:
		case 10773:
		case 10774:
		case 10775:
		case 10604:
		case 10776:
		case 10777:
		case 10778:
		case 10779:
		case 10780:
		case 10781:
		case 10815:
		case 10816:
		case 10817:
		case 10818:
		case 10819:
		case 10820:
		case 10219:
		case 10220:
		case 10221:
		case 10222:
		case 10223:
		case 10224:
		case 10605:
		case 10606:
		case 10607:
		case 10608:
		case 10609:
			return 18;
		case 2883:
			return 4;
		case 2881:
		case 2882:
			return 1;

		}
		return 0;

	}

	public boolean usingSpecial;

	private void handleSpecialNPC(NPC n) {
		if (Misc.random(2) != 0)
			return;
		switch (n.npcType) {
		case 51:
			n.requestAnimation(13155, 0);
			n.gfx0(1);
			n.attackType = 3;
			usingSpecial = true;
			break;
		default:
			break;
		}
	}

	public void corpAttack(Player c, int npc, int type) {
		NPC n = NPCHandler.npcs[npc];
		n.projectile = c;
		switch (type) {
		case 0: // Melee
			startAnimation(10058, npc);
			n.attackType = 0;
			break;
		case 1: // Magic (Single target)
			startAnimation(10053, npc);
			n.projectileId = 1825;
			n.endGfx = -1;
			n.attackType = 2;
			break;
		case 2: // Magic (Splash (no intended target))
			startAnimation(10053, npc);
			n.attackType = 2;
			n.splashCoord[0][0] = c.getX();
			n.splashCoord[0][1] = c.getY();
			n.projectileId = 1824;
			n.splash = true;
			n.splashDelay = 8;
			n.attackType = 5;
			break;
		}
	}

	private void corpSplash(NPC n) {
		if (n == null)
			return;
		for (int i = 1; i < n.splashCoord.length; i++) {
			boolean neg = Misc.random(1) == 1;
			n.splashCoord[i][0] = n.splashCoord[0][0] + (neg ? (-1 - Misc.random(2)) : (1 + Misc.random(2)));
			neg = Misc.random(1) == 1;
			n.splashCoord[i][1] = n.splashCoord[0][1] + (neg ? (-1 - Misc.random(2)) : (1 + Misc.random(2)));
			n.projectile.getPA().createPlayersProjectile(n.splashCoord[0][0], n.splashCoord[0][1],
					(n.splashCoord[0][1] - n.splashCoord[i][1]), (n.splashCoord[0][0] - n.splashCoord[i][0]), 50, 100,
					1824, 43, 31, 0, 65);
		}
	}

	public static int offset(int i) {
		switch (npcs[i].npcType) {
		case 50:
			return 2;
		case 2881:
		case 2882:
			return 1;
		case 2745:
		case 2743:
		case 8133:
			return 1;
		}
		return 0;
	}

	public boolean specialCase(Player c, int i) { // responsible for npcs that
													// much
		if (goodDistance(npcs[i].getX(), npcs[i].getY(), c.getX(), c.getY(), 8)
				&& !goodDistance(npcs[i].getX(), npcs[i].getY(), c.getX(), c.getY(), distanceRequired(i)))
			return true;
		return false;
	}

	public boolean retaliates(int npcType) {
		return npcType < 3777 || npcType > 3780 && !(npcType >= 2440 && npcType <= 2446);
	}

	public void getFightCavesAttackStyles(int i) {
		switch (npcs[i].npcType) {
		case FightCaves.TZTOK_JAD:
			int r3 = 0;
			if (goodDistance(npcs[i].absX, npcs[i].absY, PlayerHandler.players[npcs[i].spawnedBy].absX,
					PlayerHandler.players[npcs[i].spawnedBy].absY, 1))
				r3 = Misc.random(2);
			else
				r3 = Misc.random(1);
			if (r3 == 0) {
				npcs[i].attackStyle = "Magic";
			} else if (r3 == 1) {
				npcs[i].attackStyle = "Ranged";
			} else if (r3 == 2) {
				npcs[i].attackStyle = "Melee";
			}
			break;
		case FightCaves.KET_ZEK:
			r3 = Misc.random(1);
			if (r3 == 0) {
				npcs[i].attackStyle = "Magic";
			} else {
				npcs[i].attackStyle = "Melee";
			}
			break;
		case FightCaves.TOK_XIL:
			r3 = Misc.random(1);
			if (r3 == 0) {
				npcs[i].attackStyle = "Ranged";
			} else {
				npcs[i].attackStyle = "Melee";
			}
			break;
		default:
			npcs[i].attackStyle = "Melee";
			break;
		}
	}

	public void multiAttackDamage(int i) {
		int max = getMaxHit(i) * 10;
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c = PlayerHandler.players[j];
				if (c.isDead || c.heightLevel != npcs[i].heightLevel)
					continue;
				if (PlayerHandler.players[j].goodDistance(c.absX, c.absY, npcs[i].absX, npcs[i].absY, 15)) {
					if (npcs[i].attackType == 2) {
						if (!c.getVariables().prayerActive[16]
								|| !c.getVariables().curseActive[c.curses().DEFLECT_MAGIC]) {
							if (Misc.random(500) + 200 > Misc.random(c.getCombat().mageDef())) {
								int dam = Misc.random(max);
								c.getCombat().appendHit(c, dam, 0, 2, false);
							} else {
								c.getCombat().appendHit(c, 0, 0, 2, false);
							}
						} else {
							if (c.getVariables().curseActive[c.curses().DEFLECT_MAGIC])
								c.curses().deflectNPC(npcs[i], 0, 2);
							c.getCombat().appendHit(c, 0, 0, 2, false);
						}
					} else if (npcs[i].attackType == 1) {
						if (!c.getVariables().prayerActive[17]
								|| !c.getVariables().curseActive[c.curses().DEFLECT_MISSILES]) {
							int dam = Misc.random(max);
							if (Misc.random(500) + 200 > Misc.random(c.getCombat().calculateRangeDefence())) {
								c.getCombat().appendHit(c, dam, 0, 1, false);
							} else {
								c.getCombat().appendHit(c, 0, 0, 2, false);
							}
						} else {
							if (c.getVariables().curseActive[c.curses().DEFLECT_MISSILES])
								c.curses().deflectNPC(npcs[i], 0, 1);
							c.getCombat().appendHit(c, 0, 0, 2, false);
						}
					}
					if (npcs[i].endGfx > 0) {
						c.gfx0(npcs[i].endGfx);
					}
				}
				c.getPA().refreshSkill(3);
			}
		}
	}

	public void applyDamage(int i) {
		if (npcs[i] != null) {
			if (PlayerHandler.players[npcs[i].oldIndex] == null) {
				return;
			}
			if (npcs[i].isDead)
				return;
			Player c = PlayerHandler.players[npcs[i].oldIndex];
			if (multiAttacks(i)) {
				multiAttackDamage(i);
				return;
			}
			if (c.getVariables().playerIndex <= 0 && c.getVariables().npcIndex <= 0)
				if (c.getVariables().autoRet == 1)
					c.getVariables().npcIndex = i;
			if (c.getVariables().attackTimer <= 3 || c.getVariables().attackTimer == 0 && c.getVariables().npcIndex == 0
					&& c.getVariables().oldNpcIndex == 0) {
				c.startAnimation(c.getCombat().getBlockEmote());
			}
			if (c.getVariables().respawnTimer <= 0) {
				// if (npcs[i].maxHit == 0) {
				// if (getNpcListHP(npcs[i].npcType) < 17) {
				// npcs[i].maxHit = getNpcListHP(npcs[i].npcType)/5;
				// } else {
				// npcs[i].maxHit = getNpcListHP(npcs[i].npcType)/9;
				// }
				// }
				// if (npcs[i].attack == 0)
				// npcs[i].attack = (getNpcListCombat(npcs[i].npcType)/5)*4;
				int damage = 0;
				if (npcs[i].attackType == 0 || npcs[i].attackType == 4) {
					damage = Misc.random(npcs[i].maxHit);
					if (10 + Misc.random(c.getCombat().calculateMeleeDefence()) > Misc
							.random(NPCHandler.npcs[i].attack)) {
						damage = 0;
					}
					if (c.getVariables().prayerActive[18] || c.getVariables().curseActive[c.curses().DEFLECT_MELEE]) { // protect
																														// from
																														// melee
						if (c.getVariables().curseActive[c.curses().DEFLECT_MELEE])
							c.curses().deflectNPC(npcs[i], damage, 0);
						damage = 0;
					}
					if (c.getVariables().playerEquipment[c.getVariables().playerShield] == 13740) {
						damage = damage * 70 / 100;
					}
					if (c.getVariables().playerEquipment[c.getVariables().playerShield] == 13742) {
						if (Misc.random(3) != 2)
							damage = damage * 65 / 100;
					}
					if (c.getVariables().constitution - damage < 0) {
						damage = c.getVariables().constitution;
					}
				}
				if (npcs[i].attackType == 1) { // range
					damage = Misc.random(npcs[i].maxHit);
					if (10 + Misc.random(c.getCombat().calculateRangeDefence()) > Misc
							.random(NPCHandler.npcs[i].attack)) {
						damage = 0;
					}
					if (c.getVariables().prayerActive[17]
							|| c.getVariables().curseActive[c.curses().DEFLECT_MISSILES]) {
						if (c.getVariables().curseActive[c.curses().DEFLECT_MISSILES])
							c.curses().deflectNPC(npcs[i], 0, 1);
						damage = 0;
					}
					if (c.getVariables().playerEquipment[c.getVariables().playerShield] == 13740) {
						damage = damage * 70 / 100;
					}
					if (c.getVariables().playerEquipment[c.getVariables().playerShield] == 13742) {
						if (Misc.random(3) != 2)
							damage = damage * 65 / 100;
					}
					if (c.getVariables().constitution - damage < 0) {
						damage = c.getVariables().constitution;
					}
					if (npcs[i].endGfx > 0) {
						c.gfx100(npcs[i].endGfx);
					}
				}

				if (npcs[i].attackType == 2) { // magic
					damage = Misc.random(npcs[i].maxHit);
					boolean magicFailed = false;
					if (10 + Misc.random(c.getCombat().mageDef()) > Misc.random(NPCHandler.npcs[i].attack)) {
						damage = 0;
						magicFailed = true;
					}
					if (c.getVariables().prayerActive[16] || c.getVariables().curseActive[c.curses().DEFLECT_MAGIC]) { // protect
																														// from
																														// magic
						if (c.getVariables().curseActive[c.curses().DEFLECT_MAGIC])
							c.curses().deflectNPC(npcs[i], 0, 2);
						damage = 0;
						magicFailed = true;
					}
					if (c.getVariables().playerEquipment[c.getVariables().playerShield] == 13740) {
						damage = damage * 70 / 100;
					}
					if (c.getVariables().playerEquipment[c.getVariables().playerShield] == 13742) {
						if (Misc.random(3) != 2)
							damage = damage * 65 / 100;
					}
					if (c.getVariables().constitution - damage < 0) {
						damage = c.getVariables().constitution;
					}
					if (npcs[i].endGfx > 0 && (!magicFailed || isFightCaveNpc(i))) {
						c.gfx100(npcs[i].endGfx);
					} else {
						c.gfx100(85);
					}
				}
				if (npcs[i].attackType == 3) {
					int anti = c.getPA().antiFire();
					switch (anti) {
					case 0:
						damage = Misc.random(30 * 10) + 10;
						c.sendMessage("You are badly burnt by the dragon fire!");
						break;
					case 1:
						c.sendMessage("You deflect some of the dragon's fire.");
						damage = Misc.random(10 * 10);
						break;
					case 2:
						c.sendMessage("You deflect some of the dragon's fire.");
						damage = Misc.random(5 * 10);
						break;
					}
					if (c.getVariables().constitution - damage < 0)
						damage = c.getVariables().constitution;
					c.gfx100(npcs[i].endGfx);
				}
				handleSpecialEffects(c, i, damage);
				c.getVariables().logoutDelay = System.currentTimeMillis(); // logout
																			// delay
				int soak = c.getCombat().damageSoaked(damage, soakType(i));
				damage -= soak;
				if (!SummoningData.isSummonNpc(npcs[i].npcType))
					c.getCombat().appendHit(c, damage, 0, npcs[i].attackType, false);
				else
					c.getSA().attackPlayer(i);
			}
		}
	}

	public String soakType(int i) {
		if (npcs[i].attackType == 0)
			return "Melee";
		if (npcs[i].attackType == 1)
			return "Range";
		if (npcs[i].attackType == 2) {
			return "Magic";
		}
		return "";
	}

	public void attackNPC(int c, int i, Player b) {
		if (npcs[i] != null) {
			if (npcs[i].isDead)
				return;
			if (!npcs[i].inMulti() && npcs[i].underAttackBy > 0 && npcs[i].underAttackBy != npcs[c].npcId) {
				npcs[i].killerId = 0;
				return;
			}
			if (!npcs[i].inMulti()
					&& (npcs[c].underAttackBy > 0 || (npcs[c].underAttackBy2 > 0 && npcs[c].underAttackBy2 != i))) {
				npcs[i].killerId = 0;
				return;
			}
			if (npcs[i].heightLevel != npcs[c].heightLevel) {
				npcs[i].killerId = 0;
				return;
			}
			followNpcCB(i, c);
			boolean special = false;// specialCase(c,i);
			if (goodDistance(npcs[i].getX(), npcs[i].getY(), npcs[c].getX(), npcs[c].getY(), distanceRequired(i))
					|| special) {
				if (npcs[c].actionTimer <= 0) {
					npcs[i].attackTimer = getNpcDelay(i);
					npcs[i].hitDelayTimer = getHitDelay(i);
					npcs[c].underAttackBy2 = i;
					npcs[i].actionTimer = 7;
					npcs[i].oldIndexNPC = npcs[c].npcId;
					startAnimation(i, getCombatEmote(i, "Attack"));
					if (SummoningData.isSummonNpc(npcs[i].npcType)) {
						b.getSA().attackNpc(i, c);
					}
					startAnimation(getCombatEmote(i, "Attack"), i);
					// c.getPA().removeAllWindows();
				}
			}
		}
	}

	public static int getCombatEmote(int i, String type) {
		return EmoteHandler.getCombatEmote(i, type);
	}

	/**
	 * 
	 * @param i
	 * @param z
	 */
	public void followNpcCB(int i, int z) {
		NPC c = npcs[z];
		NPC n = npcs[i];
		if (n.underAttackBy == 0) {

			int playerX = c.getX();
			int playerY = c.getY();
			n.randomWalk = false;
			npcs[i].turnNpc(playerX, playerY);
			if (n.heightLevel == c.heightLevel && goodDistance(n.getX(), n.getY(), playerX, playerY, 8)) {
				npcs[i].turnNpc(playerX, playerY);
				if (c != null && n != null) {
					if (playerY < n.absY) {
						n.moveX = GetMove(n.absX, playerX);
						n.moveY = GetMove(n.absY, playerY + 2);
					} else if (playerY > n.absY) {
						n.moveX = GetMove(n.absX, playerX);
						n.moveY = GetMove(n.absY, playerY - 2);
					} else if (playerX < n.absX) {
						n.moveX = GetMove(n.absX, playerX + 2);
						n.moveY = GetMove(n.absY, playerY);
					} else if (playerX > n.absX) {
						n.moveX = GetMove(n.absX, playerX - 2);
						n.moveY = GetMove(n.absY, playerY);
					} else if (playerX == n.absX || playerY == n.absY) {
						int o = Misc.random(3);
						switch (o) {
						case 0:
							n.moveX = GetMove(n.absX, playerX);
							n.moveY = GetMove(n.absY, playerY + 1);
							break;

						case 1:
							n.moveX = GetMove(n.absX, playerX);
							n.moveY = GetMove(n.absY, playerY - 1);
							break;

						case 2:
							n.moveX = GetMove(n.absX, playerX + 1);
							n.moveY = GetMove(n.absY, playerY);
							break;

						case 3:
							n.moveX = GetMove(n.absX, playerX - 1);
							n.moveY = GetMove(n.absY, playerY);
							break;
						}
					}
				}
			} else {
				n.underAttackBy = 0;
			}
			npcs[i].turnNpc(playerX, playerY);

			n.getNextNPCMovement(i);
			npcs[i].turnNpc(playerX, playerY);
			n.randomWalk = false;
			n.updateRequired = true;
		}
	}

	/**
	 * 
	 * @param NPCID
	 * @return
	 */
	public boolean ResetAttackNPC(int NPCID) {
		npcs[NPCID].IsUnderAttackNpc = false;
		npcs[NPCID].IsAttackingNPC = false;
		return true;
	}

	/**
	 * 
	 * @param c
	 * @param i
	 * @param damage
	 */
	public void handleSpecialEffects(Player c, int i, int damage) {
		if (npcs[i].npcType == 2892 || npcs[i].npcType == 2894) {
			if (damage > 0) {
				if (c != null) {
					if (c.getVariables().playerLevel[5] > 0) {
						c.getVariables().playerLevel[5]--;
						c.getPA().refreshSkill(5);
						c.getPA().appendPoison(12);
					}
				}
			}
		}

	}

	/**
	 * 
	 * @param animId
	 * @param i
	 */
	public void startAnimation(int animId, int i) {
		npcs[i].animNumber = animId;
		npcs[i].animUpdateRequired = true;
		npcs[i].updateRequired = true;

	}

	/**
	 * 
	 * @param objectX
	 * @param objectY
	 * @param playerX
	 * @param playerY
	 * @param distance
	 * @return
	 */
	public static boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		return ((objectX - playerX <= distance && objectX - playerX >= -distance)
				&& (objectY - playerY <= distance && objectY - playerY >= -distance));
	}

	public static int distance(int object, int player, int distance) {
		int i = object - player;
		if (i < 0)
			i = -i;
		distance -= i;
		if (distance < 0)
			distance = -distance;
		return distance;
	}

	public static int getNpcListCombat(int npcId) {
		if (npcId <= -1) {
			return 0;
		}
		if (NPCDefinitions.getDefinitions()[npcId] == null) {
			return 0;
		}
		return NPCDefinitions.getDefinitions()[npcId].getNpcCombat();

	}

	public static int getNpcListHP(int npcId) {
		if (npcId <= -1) {
			return 0;
		}
		if (NPCDefinitions.getDefinitions()[npcId] == null) {
			return 0;
		}
		return NPCDefinitions.getDefinitions()[npcId].getNpcHealth();

	}

	/**
	 * 
	 * @param FileName
	 * @return
	 */
	
	@SuppressWarnings("unused")
	public boolean loadAutoSpawn(String FileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		int npcs = 0;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./" + FileName));
		} catch (FileNotFoundException fileex) {
			Misc.println(FileName + ": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			Misc.println(FileName + ": error loading file.");
			try {
				characterfile.close();
			} catch (IOException e) {
			}
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("spawn")) {
					newNPC(Integer.parseInt(token3[0]), Integer.parseInt(token3[1]), Integer.parseInt(token3[2]),
							Integer.parseInt(token3[3]), Integer.parseInt(token3[4]),
							getNpcListHP(Integer.parseInt(token3[0])), Integer.parseInt(token3[5]),
							Integer.parseInt(token3[6]), Integer.parseInt(token3[7]), 0);
					npcs++;
				}
			} else {
				if (line.equals("[ENDOFSPAWNLIST]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {
					}
					return true;
				}
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {
		}
		return false;
	}

	/**
	 * 
	 * @param npcId
	 * @return
	 */
	public static String getNpcListName(int npcId) {
		if (npcId <= -1) {
			return "None";
		}
		if (NPCDefinitions.getDefinitions()[npcId] == null) {
			return "None";
		}
		return Misc
				.optimizeText(NPCDefinitions.getDefinitions()[npcId].getNpcName().toLowerCase().replaceAll("_", " "));
	}

	/**
	 * 
	 * @param FileName
	 * @return
	 */
	@SuppressWarnings("unused")
	public boolean loadNPCList(String FileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		int npcs = 0;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./" + FileName));
		} catch (FileNotFoundException fileex) {
			Misc.println(FileName + ": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			Misc.println(FileName + ": error loading file.");
			try {
				characterfile.close();
			} catch (IOException e) {
			}
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("npc")) {
					newNPCList(Integer.parseInt(token3[0]), token3[1], Integer.parseInt(token3[2]),
							Integer.parseInt(token3[3]));
					// npcCombat[Integer.parseInt(token3[0])] =
					// Integer.parseInt(token3[2]);
					npcs++;
				}
			} else {
				if (line.equals("[ENDOFNPCLIST]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {
					}
					return true;
				}
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {
		}
		return false;
	}

	/**
	 * Checks if a tzhaar npc has been killed, if so then it checks if it needs
	 * to do the tz-kek effect. If tzKek spawn has been killed twice or didn't
	 * need to be killed it calls killedTzhaar.
	 * 
	 * @param i
	 *            The npc.
	 */
	private void tzhaarDeathHandler(int i) {// hold a vit plz
		if (isFightCaveNpc(i) && npcs[i].npcType != FightCaves.TZ_KEK)
			killedTzhaar(i);
		if (npcs[i].npcType == FightCaves.TZ_KEK) {
			int p = npcs[i].killerId;
			if (PlayerHandler.players[p] != null) {
				Player c = PlayerHandler.players[p];
				FightCaves.tzKekEffect(c, i);
			}
		}
	}

	/**
	 * Raises the count of tzhaarKilled, if tzhaarKilled is equal to the amount
	 * needed to kill to move onto the next wave it raises wave id then starts
	 * next wave.
	 * 
	 * @param i
	 *            The npc.
	 */
	private void killedTzhaar(int i) {
		final Player c2 = PlayerHandler.players[npcs[i].spawnedBy];
		c2.getVariables().tzhaarKilled++;
		if (c2.getVariables().tzhaarKilled == c2.getVariables().tzhaarToKill) {
			c2.getVariables().waveId++;

			CycleEventHandler.getSingleton().addEvent(c2, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					GameEngine.fightCaves.spawnNextWave(c2);
					container.stop();
				}

				@Override
				public void stop() {
				}
			}, 17);

		}
	}

	/**
	 * Handles the death of tztok-jad by ending the game and rewarding the
	 * player with a fire cape.
	 * 
	 * @param i
	 *            The npc.
	 */
	public void handleJadDeath(int i) {
		Player c = PlayerHandler.players[npcs[i].spawnedBy];
		c.getItems().addItem(6570, 1);
		c.sendMessage("Congratulations on completing the fight caves minigame!");
		c.getPA().resetTzhaar();
		c.getVariables().waveId = 300;
	}

	/**
	 * Checks if something is a fight cave npc.
	 * 
	 * @param i
	 *            The npc.
	 * @return Whether or not it is a fight caves npc.
	 */
	public static boolean isFightCaveNpc(int i) {
		switch (npcs[i].npcType) {
		case FightCaves.TZ_KEK_SPAWN:
		case FightCaves.TZ_KIH:
		case FightCaves.TZ_KEK:
		case FightCaves.TOK_XIL:
		case FightCaves.YT_MEJKOT:
		case FightCaves.KET_ZEK:
		case FightCaves.TZTOK_JAD:
			return true;
		}
		return false;
	}

	public void FollowNpcCB(int i, int z) {
		NPC c = npcs[z];
		NPC n = npcs[i];
		if (n.underAttackBy == 0) {

			int playerX = c.getX();
			int playerY = c.getY();
			n.randomWalk = false;
			npcs[i].turnNpc(playerX, playerY);
			if (n.heightLevel == c.heightLevel && goodDistance(n.getX(), n.getY(), playerX, playerY, 8)) {
				npcs[i].turnNpc(playerX, playerY);
				if (c != null && n != null) {
					if (playerY < n.absY) {
						n.moveX = GetMove(n.absX, playerX);
						n.moveY = GetMove(n.absY, playerY + 2);
					} else if (playerY > n.absY) {
						n.moveX = GetMove(n.absX, playerX);
						n.moveY = GetMove(n.absY, playerY - 2);
					} else if (playerX < n.absX) {
						n.moveX = GetMove(n.absX, playerX + 2);
						n.moveY = GetMove(n.absY, playerY);
					} else if (playerX > n.absX) {
						n.moveX = GetMove(n.absX, playerX - 2);
						n.moveY = GetMove(n.absY, playerY);
					} else if (playerX == n.absX || playerY == n.absY) {
						int o = Misc.random(3);
						switch (o) {
						case 0:
							n.moveX = GetMove(n.absX, playerX);
							n.moveY = GetMove(n.absY, playerY + 1);
							break;

						case 1:
							n.moveX = GetMove(n.absX, playerX);
							n.moveY = GetMove(n.absY, playerY - 1);
							break;

						case 2:
							n.moveX = GetMove(n.absX, playerX + 1);
							n.moveY = GetMove(n.absY, playerY);
							break;

						case 3:
							n.moveX = GetMove(n.absX, playerX - 1);
							n.moveY = GetMove(n.absY, playerY);
							break;
						}
					}
					npcs[i].turnNpc(playerX, playerY);
				}
			} else {
				// c.getSummoning().callFamiliar();
				n.underAttackBy = 0;
			}
			npcs[i].turnNpc(playerX, playerY);
			// n.facePlayer(playerId);
			n.getNextNPCMovement(i);
			npcs[i].turnNpc(playerX, playerY);
			n.randomWalk = false;
			n.updateRequired = true;
			// n.underAttackBy = 0;
		}
	}

	@SuppressWarnings("unused")
	public boolean NpcVersusNpc(int NPCID, int killingId, Player c) {
		if (NPCID < 0)
			return false;
		if (npcs[NPCID] != null) {
			if (npcs[killingId] != null) {
				int EnemyX = npcs[killingId].absX;
				int EnemyY = npcs[killingId].absY;
				int EnemyHP = npcs[killingId].HP;
				int hitDiff = Misc.random(npcs[NPCID].MaxHP / 8);
				NPC n = npcs[killingId];
				FollowNpcCB(NPCID, killingId);
				// npcs[NPCID].turnNpc(EnemyX, EnemyY);
				if (npcs[NPCID].actionTimer == 0) {
					if (goodDistance(EnemyX, EnemyY, npcs[NPCID].absX, npcs[NPCID].absY, 2)) {
						if (NPCHandler.npcs[killingId].isDead == true) {
							ResetAttackNPC(NPCID);
						} else {
							startAnimation(getCombatEmote(c.getVariables().summoningMonsterId, "Attack"),
									c.getVariables().summoningMonsterId);
							if (SummoningData.isSummonNpc(npcs[NPCID].npcType)) {
								c.getSA().attackNpc(NPCID, killingId);
							}
							npcs[NPCID].actionTimer = 7;
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
