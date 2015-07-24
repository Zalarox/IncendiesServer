package main.game.players.content.minigames.impl;

import java.util.HashMap;
import java.util.Iterator;

import main.GameEngine;
import main.game.npcs.NPCHandler;
import main.game.players.Player;
import main.game.players.actions.combat.CombatPrayer;
import main.util.Misc;

/**
 * @author Harlan Credits to Sanity
 */

public class PestControl {

	/**
	 * how long the game is going for
	 */
	private final int GAME_TIMER = 300;
	/**
	 * how long before were put into the game from lobby
	 */
	private final int WAIT_TIMER = 60;
	/**
	 * How many players we need to start a game
	 */
	private final int PLAYERS_REQUIRED = 1;
	/**
	 * How many points a player should receive each game
	 */
	private final int POINT_REWARD = 3;
	/**
	 * How much health should the knight have
	 */
	private final int KNIGHTS_HEALTH = 2000;
	/**
	 * Hashmap for the players in lobby
	 */
	public static HashMap<Player, Integer> waitingBoat = new HashMap<Player, Integer>();
	/**
	 * hashmap for the players in game
	 */
	private static HashMap<Player, Integer> gamePlayers = new HashMap<Player, Integer>();

	private int gameTimer = -1;
	private int waitTimer = 60;
	public static int spawnWave = 0;
	public static boolean gameStarted = false;

	/**
	 * Array used for storing the portals health
	 */
	public static int[] portalHealth = { 2000, 2000, 2000, 2000 };
	/**
	 * array used for storing the npcs used in the minigame
	 * 
	 * @order npcId, xSpawn, ySpawn, health
	 */
	private int[][] pcNPCData = { { 6142, 2628, 2591 }, // portal
			{ 6143, 2680, 2588 }, // portal
			{ 6144, 2669, 2570 }, // portal
			{ 6145, 2645, 2569 }, // portal
			{ 3782, 2656, 2592 } // knight
	};

	/**
	 * Process of the minigame handles game start / end
	 */
	public void process() {
		try {
			setBoatInterface();
			/**
			 * handling the wait time in lobby, if timer is done then attempt to
			 * start game
			 */
			if (waitTimer > 0)
				waitTimer--;
			else if (waitTimer == 0)
				startGame();
			if (gameStarted && playersInGame() < 1)
				endGame(false);
			/**
			 * if the game has started handle in game aspects
			 */
			if (gameTimer > 0 && gameStarted) {
				gameTimer--;
				if (spawnWave > 0)
					spawnWave--;
				if (spawnWave == 0) {
					pestSpawns();
					spawnWave = 10;
				}
				setGameInterface();
				if (allPortalsDead())
					endGame(true);
			} else if (gameTimer <= 0 && gameStarted)
				endGame(false);
		} catch (RuntimeException e) {
			System.out.println("Failed to set process");
			e.printStackTrace();
		}
	}

	/**
	 * Method we use for removing a player from the pc game
	 * 
	 * @param player
	 */
	public static void removePlayerGame(Player player) {
		if (gamePlayers.containsKey(player)) {
			player.getPA().movePlayer(2657, 2639, 0);
			gamePlayers.remove(player);
		}
	}

	/**
	 * Setting the interfaces for the waiting lobby
	 */
	private void setBoatInterface() {
		try {
			Iterator<Player> iterator = waitingBoat.keySet().iterator();
			while (iterator.hasNext()) {
				Player c = iterator.next();
				if (c != null) {
					try {
						if (gameStarted)
							c.getPA().sendString("Next Departure: " + (waitTimer + gameTimer) + "", 21120);
						else
							c.getPA().sendString("Next Departure: " + waitTimer + "", 21120);
						c.getPA().sendString("Players Ready: " + playersInBoat() + "", 21121);
						c.getPA().sendString("(Need " + PLAYERS_REQUIRED + " to 25 players)", 21122);
						c.getPA().sendString("Points: " + c.getVariables().pcPoints + "", 21123);

					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (RuntimeException e) {
			System.out.println("Failed to set interfaces");
			e.printStackTrace();
		}
	}

	/**
	 * Setting the interface for in game players
	 */
	private void setGameInterface() {
		Iterator<Player> iterator = gamePlayers.keySet().iterator();
		while (iterator.hasNext()) {
			Player player = iterator.next();
			if (player != null) {
				for (int i = 0; i < portalHealth.length; i++) {
					if (portalHealth[i] > 0) {
						player.getPA().sendString("" + portalHealth[i] + "", 21111 + i);
					} else
						player.getPA().sendString("Dead", 21111 + i);

				}
				player.getPA().sendString("" + KNIGHTS_HEALTH, 21115);
				player.getPA().sendString("" + player.getVariables().pcDamage, 21116);
				player.getPA().sendString("Time remaining: " + gameTimer + "", 21117);

			}
		}
	}

	/***
	 * Moving players to arena if there's enough players
	 */
	private void startGame() {
		// if we dont have
		if (playersInBoat() < PLAYERS_REQUIRED) {
			waitTimer = WAIT_TIMER;
			return;
		}
		for (int i = 0; i < portalHealth.length; i++)
			portalHealth[i] = 2000;
		gameTimer = GAME_TIMER;
		waitTimer = -1;
		spawnNpcs();
		gameStarted = true;
		Iterator<Player> iterator = waitingBoat.keySet().iterator();
		while (iterator.hasNext()) {
			Player player = iterator.next();
			if (player == null) {
				continue;
			}
			if (!inPcBoat(player) && waitingBoat.containsKey(player)) {
				waitingBoat.remove(player);
			}

			player.getPA().movePlayer(2656 + Misc.random3(3), 2614 - Misc.random3(4), 0);
			gamePlayers.put(player, 1);
			player.sendMessage("@red@The Pest Control Game has begun!");
		}
		waitingBoat.clear();
	}

	public boolean inPcBoat(Player c) {
		if (c.absX >= 2660 && c.absX <= 2663 && c.absY >= 2638 && c.absY <= 2643) {
			return true;
		}
		return false;
	}

	/**
	 * Checks how many players are in the waiting lobby
	 * 
	 * @return players waiting
	 */
	private int playersInBoat() {
		int players = 0;
		Iterator<Player> iterator = waitingBoat.keySet().iterator();
		while (iterator.hasNext()) {
			Player player = iterator.next();
			if (player != null) {
				players++;
			}
		}
		return players;
	}

	/**
	 * Checks how many players are in the game
	 * 
	 * @return players in the game
	 */
	private int playersInGame() {
		int players = 0;
		Iterator<Player> inGamePlayers = gamePlayers.keySet().iterator();
		while (inGamePlayers.hasNext()) {
			Player player = inGamePlayers.next();
			if (player != null) {
				players++;
			}
		}
		return players;
	}

	/**
	 * Ends the game
	 * 
	 * @param won
	 */
	private void endGame(boolean won) {
		Iterator<Player> players = gamePlayers.keySet().iterator();
		while (players.hasNext()) {
			Player player = players.next();
			if (player == null) {
				continue;
			}
			player.getPA().movePlayer(2657, 2639, 0);
			if (won && player.getVariables().pcDamage > 500) {
				player.getDH().sendDialogues(79, 3790);
				player.sendMessage("You have won the pest control game and have been awarded " + POINT_REWARD
						+ " Pest Control points.");
				player.getVariables().pcPoints += POINT_REWARD + player.getVariables().getDonarPointbonus(POINT_REWARD);
				player.getItems().addItem(995, player.getVariables().CombatLevel * 400);
			} else if (won) {
				player.getDH().sendDialogues(77, 3790);
				player.sendMessage("The void knights notice your lack of zeal.");
			} else {
				player.getDH().sendDialogues(78, 3790);
				player.sendMessage(
						"You failed to kill all the portals in 3 minutes and have not been awarded any points.");
			}
			cleanUpPlayer(player);
		}
		cleanUp();
	}

	/**
	 * Resets the game variables and map
	 */
	private void cleanUp() {
		gameTimer = -1;
		waitTimer = WAIT_TIMER;
		gameStarted = false;
		gamePlayers.clear();
		/*
		 * Removes the npcs from the game if any left over for whatever reason
		 */
		for (int i = 0; i < pcNPCData.length; i++) {
			for (int j = 0; j < NPCHandler.npcs.length; j++) {
				if (NPCHandler.npcs[j] != null) {
					if (NPCHandler.npcs[j].npcType == pcNPCData[i][0])
						NPCHandler.npcs[j] = null;
				}
			}
		}
	}

	/**
	 * Cleans the player of any damage, loss they may of received
	 */
	private void cleanUpPlayer(Player player) {
		player.getVariables().poisonDamage = 0;
		CombatPrayer.resetPrayers(player);
		for (int i = 0; i < 24; i++) {
			player.getVariables().playerLevel[i] = player.getPA().getLevelForXP(player.getVariables().playerXP[i]);
			player.getPA().refreshSkill(i);
		}
		player.getVariables().specAmount = 10;
		player.getVariables().pcDamage = 0;
		player.getItems().addSpecialBar(player.getVariables().playerEquipment[player.getVariables().playerWeapon]);
	}

	/**
	 * Checks if the portals are dead
	 * 
	 * @return players dead
	 */
	private boolean allPortalsDead() {
		int count = 0;
		for (int i = 0; i < portalHealth.length; i++) {
			if (portalHealth[i] <= 0)
				count++;
		}
		return count >= 4;
	}

	/**
	 * Moves a player out of the waiting boat
	 * 
	 * @param c
	 */
	public static void leaveWaitingBoat(Player c) {
		if (waitingBoat.containsKey(c)) {
			waitingBoat.remove(c);
			c.getPA().movePlayer(2657, 2639, 0);
		}
	}

	/**
	 * Moves a player into the hash and into the lobby
	 * 
	 * @param player
	 */
	public static void addToWaitRoom(Player player) {
		if (player != null) {
			waitingBoat.put(player, 1);
			player.sendMessage("You have joined the Pest Control boat.");
			player.getPA().movePlayer(2661, 2639, 0);
		}
	}

	/**
	 * Checks if a player is in the game
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isInGame(Player player) {
		return gamePlayers.containsKey(player);
	}

	/**
	 * Checks if a player is in the pc boat (lobby)
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isInPcBoat(Player player) {
		return waitingBoat.containsKey(player);
	}

	public static boolean npcIsPortal(int type) {
		for (int i = 6142; i < 6146; i++) {
			if (type == i)
				return true;
		}
		return false;
	}

	public static boolean npcIsPCMonster(int npcType) {
		return npcType >= 3727 && npcType <= 3776;
	}

	public boolean voidDead() {
		boolean dead = false;
		for (int j = 0; j < NPCHandler.npcs.length; j++) {
			if (NPCHandler.npcs[j] != null) {
				if (NPCHandler.npcs[j].npcType == 3782)
					if (NPCHandler.npcs[j].needRespawn)
						dead = true;
			}
		}
		return dead;
	}

	public void pestSpawns() {
		int[] pest = { 3761, 3771, 3751, 3776 };
		int[][] coords = { { 2630, 2591 }, { 2678, 2588 }, { 2645, 2571 }, { 2669, 2572 } };
		for (int i = 0; i < pest.length; i++) {
			GameEngine.npcHandler.spawnNpc(pest[Misc.random(3)], coords[i][0], coords[i][1], 0, 0, 75, 8, 60,
					50 + Misc.random(25));
		}
		GameEngine.npcHandler.spawnNpc(3741, 2655 + Misc.random(2), 2592 + Misc.random(2), 0, 0, 70, 4, 100, 50);
		GameEngine.npcHandler.spawnNpc(3741, 2657 - Misc.random(2), 2592 - Misc.random(2), 0, 0, 70, 4, 100, 50);
		GameEngine.npcHandler.spawnNpc(3741, 2657 - Misc.random(2), 2593 + Misc.random(2), 0, 0, 70, 4, 100, 50);
	}

	private void spawnNpcs() {
		for (int i = 0; i < pcNPCData.length; i++) {
			GameEngine.npcHandler.newNPC(pcNPCData[i][0], pcNPCData[i][1], pcNPCData[i][2], 0, 0, 2000, 0, 0, 100, 0);

		}
	}
}