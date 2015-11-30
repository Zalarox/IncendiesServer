package incendius.game.players.content.minigames.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import incendius.game.players.Player;
import incendius.util.Misc;

/**
 *
 * @author ArrowzFtw
 */
public class FightPits {

	/**
	 * @note States of minigames
	 */
	private static final String PLAYING = "PLAYING";
	private static final String WAITING = "WAITING";
	/**
	 * @note Current fight pits champion
	 */
	private static String pitsChampion = "None";
	/**
	 * @note Countdown for game to start
	 */
	private static int gameStartTimer = 80;
	/**
	 * @note Elapsed Game start time
	 */
	private static int elapsedGameTime = 0;
	private static final int END_GAME_TIME = 400;
	/*
	 * @note Game started or not?
	 */
	private static boolean gameStarted = false;
	/**
	 * @note Stores player and State
	 */
	private static ConcurrentHashMap<Player, String> playerMap = new ConcurrentHashMap<Player, String>();
	/**
	 * @note Where to spawn when pits game starts
	 */
	private static final int MINIGAME_START_POINT_X = 2392;
	private static final int MINIGAME_START_POINT_Y = 5139;
	/**
	 * @note Exit game area
	 */
	private static final int EXIT_GAME_X = 2399;
	private static final int EXIT_GAME_Y = 5169;
	/**
	 * @note Exit waiting room
	 */
	private static final int EXIT_WAITING_X = 2399;
	private static final int EXIT_WAITING_Y = 5177;
	/**
	 * @note Waiting room coordinates
	 */
	private static final int WAITING_ROOM_X = 2399;
	private static final int WAITING_ROOM_Y = 5175;

	/**
	 * @return HashMap Value
	 */
	public static String getState(Player c) {
		return playerMap.get(c);
	}

	private static final int TOKKUL_ID = 6529;

	/**
	 * @note Adds player to waiting room.
	 */
	public static void addPlayer(Player c) {
		playerMap.put(c, WAITING);
		c.teleportToX = WAITING_ROOM_X;
		c.teleportToY = WAITING_ROOM_Y;
	}

	/**
	 * @note Starts the game and moves players to arena
	 */
	private static void enterGame(Player c) {
		playerMap.remove(c);
		playerMap.put(c, PLAYING);
		c.teleportToX = MINIGAME_START_POINT_X + Misc.random(12);
		c.teleportToY = MINIGAME_START_POINT_Y + Misc.random(12);
	}

	/**
	 * @note Removes player from pits if there in waiting or in game
	 */
	public static void removePlayer(Player c, boolean forceRemove) {
		if (forceRemove) {
			c.absX = EXIT_WAITING_X;
			c.absY = EXIT_WAITING_Y;
			playerMap.remove(c);
			return;
		}
		String state = playerMap.get(c);
		if (state == null) {
			c.teleportToX = EXIT_WAITING_X;
			c.teleportToY = EXIT_WAITING_Y;
			return;
		}

		if (state.equals(PLAYING)) {
			if (getListCount(PLAYING) - 1 == 0 && !forceRemove) {
				pitsChampion = c.playerName;
				c.getItems().addItem(TOKKUL_ID, 1500 + Misc.random(500));

			}
			c.teleportToX = EXIT_GAME_X;
			c.teleportToY = EXIT_GAME_Y;
		} else if (state.equals(WAITING)) {
			c.teleportToX = EXIT_WAITING_X;
			c.teleportToY = EXIT_WAITING_Y;
			c.getPA().walkableInterface(-1);
		}
		playerMap.remove(c);

		if (state.equals(PLAYING)) {
			if (!forceRemove) {
				playerMap.put(c, WAITING);
			}
		}
	}

	/**
	 * @return Players playing fight pits
	 */
	@SuppressWarnings("rawtypes")
	public static int getListCount(String state) {
		int count = 0;
		Iterator it = playerMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			if (pairs.getValue().equals(state)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * @note Updates players
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	private static void update() {
		Iterator it = playerMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			Player c = (Player) pairs.getKey();
			boolean updated = pairs.getValue() == WAITING ? updateWaitingRoom(c) : updateGame(c);
		}
	}

	/**
	 * @note Updates waiting room interfaces etc.
	 */
	public static boolean updateWaitingRoom(Player c) {
		c.getPA().sendFrame126("Next Game Begins In : " + gameStartTimer, 2805);
		c.getPA().sendFrame126("Champion: JalYt-Ket-" + pitsChampion, 2806);
		c.getPA().sendFrame36(560, 1);
		c.getPA().walkableInterface(2804);
		return true;
	}

	/**
	 * @note Updates players in game interfaces etc.
	 */
	public static boolean updateGame(Player c) {
		c.getPA().sendFrame126("Foes Remaining: " + getListCount(PLAYING), 2805);
		c.getPA().sendFrame126("Champion: JalYt-Ket-" + pitsChampion, 2806);
		c.getPA().sendFrame36(560, 1);
		c.getPA().walkableInterface(2804);
		return true;
	}

	/**
	 * @note Handles death and respawn rubbish.
	 */
	public static void handleDeath(Player c) {
		removePlayer(c, true);
	}

	/*
	 * @process 600ms Tick
	 */
	public static void process() {
		update();

		if (!gameStarted) {
			if (gameStartTimer > 0) {
				gameStartTimer--;
			} else if (gameStartTimer == 0) {
				if (getListCount(WAITING) != 1) {
					beginGame();
				}
				gameStartTimer = 80;
			}
		}

		if (gameStarted) {
			elapsedGameTime++;
			if (elapsedGameTime == END_GAME_TIME) {
				endGame();
				elapsedGameTime = 0;
				gameStarted = false;
				gameStartTimer = 80;
			}
		}
	}

	/**
	 * @note Starts game for the players in waiting room
	 */
	@SuppressWarnings("rawtypes")
	private static void beginGame() {
		Iterator it = playerMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			Player c = (Player) pairs.getKey();
			enterGame(c);
		}
	}

	/**
	 * @note Ends game and returns player to their normal spot.
	 */
	@SuppressWarnings("rawtypes")
	private static void endGame() {
		Iterator it = playerMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			Player c = (Player) pairs.getKey();
			removePlayer(c, true);
		}
	}
}
