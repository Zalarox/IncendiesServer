package main.game.players;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;

import main.Constants;
import main.GameEngine;
import main.game.npcs.NPCHandler;
import main.game.players.content.minigames.DuelArena;
import main.util.Misc;
import main.util.Stream;

public class PlayerHandler {

	public static Player players[] = new Player[Constants.MAX_PLAYERS];
	public static String messageToAll = "";
	public static int playerCount = 0;
	public static boolean updateAnnounced;
	public static boolean updateRunning;
	public static int updateSeconds;
	public static long updateStartTime;
	public static boolean kickAllPlayers = false;

	static {
		for (int i = 0; i < players.length; i++) {
			players[i] = null;
		}
	}

	public static Player[] getPlayers() {
		return players;
	}
	
	/**
	 * Returns a Player object based on their index in the array.
	 * 
	 * @param idx
	 *            The index of the specified player.
	 *            
	 * @return The Player object.
	 */
	public static Player getPlayer(int idx) {
		return players[idx];
	}
	
	/**
	 * Returns a Player object based on their name.
	 * 
	 * @param name
	 *            The name of the specified player.
	 *            
	 * @return The Player object.
	 */
	public static Player getPlayerByName(String name) {
		Player c = null;
		
		for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
			if (getPlayer(i) != null) {
				if (getPlayer(i).getDisplayName().equalsIgnoreCase(name)) {
					c = getPlayer(i);
					break;
				}
			}
		}
		
		return c;
	}

	public boolean newPlayerPlayer(Player player1) {
		int slot = -1;
		for (int i = 1; i < players.length; i++) {
			if ((players[i] == null) || players[i].disconnected) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			return false;
		}
		player1.handler = this;
		player1.playerId = slot;
		players[slot] = player1;
		players[slot].isActive = true;
		players[slot].connectedFrom = ((InetSocketAddress) player1.getSession().getRemoteAddress()).getAddress()
				.getHostAddress();
		if (Constants.SERVER_DEBUG) {
			Misc.println("Player Slot " + slot + " slot 0 " + players[0] + " Player Hit " + players[slot]);
		}
		return true;
	}

	public static int[] toIntArray(ArrayList<Integer> integerList) {
		int[] intArray = new int[integerList.size()];

		for (int i = 0; i < integerList.size(); i++) {
			intArray[i] = integerList.get(i);
		}

		return intArray;
	}

	public void destruct() {
		for (int i = 0; i < players.length; i++) {
			if (players[i] == null) {
				continue;
			}
			players[i].destruct();
			players[i] = null;
		}
	}

	public static int getPlayerCount() {
		int count = 0;

		for (int i = 0; i < players.length; i++) {
			if (players[i] != null) {
				count = count + 1;
			}
		}
		return count;
	}

	public static boolean isPlayerOn(final String playerName) {
		for (int d = 0; d < players.length; d++) {
			if (PlayerHandler.players[d] != null) {
				final Player p = PlayerHandler.players[d];
				if (p.playerName.toLowerCase().equals(playerName.toLowerCase())) {
					return true;
				}
			}
		}
		return false;
	}

	public static void process(int i, ScheduledExecutorService thread) {
		if (players[i] == null || !players[i].isActive) {
			thread.shutdownNow();
			return;
		}
		if (kickAllPlayers) {
			players[i].disconnected = true;
		}

		Player c = PlayerHandler.players[i];
		if (c != null && c.disconnected && (System.currentTimeMillis() - c.getInstance().logoutDelay > 10000
				|| c.getInstance().properLogout || kickAllPlayers)) {
			if (players[i].getInstance().inTrade) {
				if (c.opponent != null) {
					c.opponent.Dueling.declineDuel(c.opponent, true, false);
				}
			}
			if (c != null && DuelArena.isDueling(c)) {
				if (c.opponent != null) {
					c.opponent.Dueling.endDuel(c.opponent);
				}
			} else if (c != null && !DuelArena.isDueling(c)) {
				if (c.opponent != null) {
					c.opponent.Dueling.declineDuel(c.opponent, true, false);
				}
			}
			Player o = PlayerHandler.players[i];
			if (PlayerSave.saveGame(o)) {
				System.out.println("Game saved for player " + players[i].playerName);
			} else {
				System.out.println("Could not save for " + players[i].playerName);
			}
			removePlayer(players[i]);
			players[i] = null;
			return;
		}

		players[i].preProcessing();
		while (players[i].processQueuedPackets())
			;
		players[i].process();
		players[i].postProcessing();
		players[i].getNextPlayerMovement();

		if (players[i] == null || !players[i].isActive) {
			players[i] = null;
			thread.shutdownNow();
			return;
		}
		if (players[i].disconnected && (System.currentTimeMillis() - players[i].getInstance().logoutDelay > 10000
				|| players[i].getInstance().properLogout || kickAllPlayers)) {
			if (players[i].getInstance().inTrade) {
				Player o = PlayerHandler.players[players[i].getInstance().tradeWith];
				if (o != null) {
					o.getTradeHandler().declineTrade(false);
				}
			}
			c = PlayerHandler.players[i];
			if (c != null && DuelArena.isDueling(c)) {
				if (c.opponent != null) {
					c.opponent.Dueling.endDuel(c.opponent);
				}
			} else if (c != null && !DuelArena.isDueling(c)) {
				if (c.opponent != null) {
					c.opponent.Dueling.declineDuel(c.opponent, true, false);
				}
			}

			Player o1 = PlayerHandler.players[i];
			if (PlayerSave.saveGame(o1)) {
				System.out.println("Game saved for player " + players[i].playerName);
			} else {
				System.out.println("Could not save for " + players[i].playerName);
			}
			removePlayer(players[i]);
			players[i] = null;
			return;
		} else {
			if (!players[i].getInstance().initialized) {
				players[i].initialize();
				players[i].getInstance().initialized = true;
			} else {
				players[i].update();
			}

		}

		if (updateRunning && !updateAnnounced) {
			updateAnnounced = true;
			GameEngine.UpdateServer = true;
		}
		if (updateRunning && (System.currentTimeMillis() - updateStartTime > (updateSeconds * 1000))) {
			kickAllPlayers = true;
		}

		if (players[i] == null || !players[i].isActive) {
			players[i] = null;
			thread.shutdownNow();
			return;
		}
		try {
			players[i].clearUpdateFlags();
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * if (updateRunning && !updateAnnounced) { updateAnnounced = true;
		 * GameEngine.UpdateServer = true; } if (updateRunning &&
		 * (System.currentTimeMillis() - updateStartTime > (updateSeconds *
		 * 1000))) { kickAllPlayers = true; }
		 */

	}

	public void process() {

		if (kickAllPlayers) {
			for (int i = 1; i < players.length; i++) {
				if (players[i] != null) {
					players[i].disconnected = true;
				}
			}
		}

		for (int i = 0; i < players.length; i++) {
			if (players[i] == null || !players[i].isActive) {
				continue;
			}
			Player c = PlayerHandler.players[i];
			if (c != null && c.disconnected && (System.currentTimeMillis() - c.getInstance().logoutDelay > 10000
					|| c.getInstance().properLogout || kickAllPlayers)) {
				if (players[i].getInstance().inTrade) {
					if (c.opponent != null) {
						c.opponent.Dueling.declineDuel(c.opponent, true, false);
					}
				}
				if (c != null && DuelArena.isDueling(c)) {
					if (c.opponent != null) {
						c.opponent.Dueling.endDuel(c.opponent);
					}
				} else if (c != null && !DuelArena.isDueling(c)) {
					if (c.opponent != null) {
						c.opponent.Dueling.declineDuel(c.opponent, true, false);
					}
				}
				Player o = PlayerHandler.players[i];
				if (PlayerSave.saveGame(o)) {
					System.out.println("Game saved for player " + players[i].playerName);
				} else {
					System.out.println("Could not save for " + players[i].playerName);
				}
				removePlayer(players[i]);
				players[i] = null;
				continue;
			}

			players[i].preProcessing();
			while (players[i].processQueuedPackets())
				;
			players[i].process();
			players[i].postProcessing();
			players[i].getNextPlayerMovement();

		}

		for (int i = 0; i < players.length; i++) {
			if (players[i] == null || !players[i].isActive) {
				continue;
			}
			if (players[i].disconnected && (System.currentTimeMillis() - players[i].getInstance().logoutDelay > 10000
					|| players[i].getInstance().properLogout || kickAllPlayers)) {
				if (players[i].getInstance().inTrade) {
					Player o = PlayerHandler.players[players[i].getInstance().tradeWith];
					if (o != null) {
						o.getTradeHandler().declineTrade(false);
					}
				}
				Player c = PlayerHandler.players[i];
				if (c != null && DuelArena.isDueling(c)) {
					if (c.opponent != null) {
						c.opponent.Dueling.endDuel(c.opponent);
					}
				} else if (c != null && !DuelArena.isDueling(c)) {
					if (c.opponent != null) {
						c.opponent.Dueling.declineDuel(c.opponent, true, false);
					}
				}

				Player o1 = PlayerHandler.players[i];
				if (PlayerSave.saveGame(o1)) {
					System.out.println("Game saved for player " + players[i].playerName);
				} else {
					System.out.println("Could not save for " + players[i].playerName);
				}
				removePlayer(players[i]);
				players[i] = null;
			} else {
				if (!players[i].getInstance().initialized) {
					players[i].initialize();
					players[i].getInstance().initialized = true;
				} else {
					players[i].update();
				}

			}
		}

		if (updateRunning && !updateAnnounced) {
			updateAnnounced = true;
			GameEngine.UpdateServer = true;
		}
		if (updateRunning && (System.currentTimeMillis() - updateStartTime > (updateSeconds * 1000))) {
			kickAllPlayers = true;
		}

		for (int i = 0; i < players.length; i++) {
			if (players[i] == null || !players[i].isActive) {
				continue;
			}
			try {
				players[i].clearUpdateFlags();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/*
		 * if (updateRunning && !updateAnnounced) { updateAnnounced = true;
		 * GameEngine.UpdateServer = true; } if (updateRunning &&
		 * (System.currentTimeMillis() - updateStartTime > (updateSeconds *
		 * 1000))) { kickAllPlayers = true; }
		 */

	}

	public void updateNPC(Player plr, Stream str) {

		updateBlock.currentOffset = 0;

		str.createFrameVarSizeWord(65);
		str.initBitAccess();

		str.writeBits(8, plr.npcListSize);
		int size = plr.npcListSize;
		plr.npcListSize = 0;
		for (int i = 0; i < size; i++) {
			if (plr.getInstance().RebuildNPCList == false && plr.withinDistance(plr.npcList[i]) == true) {
				plr.npcList[i].updateNPCMovement(str);
				plr.npcList[i].appendNPCUpdateBlock(updateBlock, plr);
				plr.npcList[plr.npcListSize++] = plr.npcList[i];
			} else {
				int id = plr.npcList[i].npcId;
				plr.npcInListBitmap[id >> 3] &= ~(1 << (id & 7));
				str.writeBits(1, 1);
				str.writeBits(2, 3);
			}
		}
		/*
		 * if (Region.REGION_UPDATING_ENABLED) { if (Region.getRegion(plr.absX,
		 * plr.absY) != null) { for (NPC n : Region.getRegion(plr.absX,
		 * plr.absY).npcs) { if (n != null) { final int id = n.npcId; if
		 * ((plr.RebuildNPCList || (plr.npcInListBitmap[id >> 3] & 1 << (id &
		 * 7)) == 0) && plr.withinDistance(n)) { plr.addNewNPC(n, str,
		 * updateBlock); } } } } } else { for (int i = 0; i <
		 * NPCHandler.maxNPCs; i++) { if (NPCHandler.npcs[i] != null) { int id =
		 * NPCHandler.npcs[i].npcId; if (plr.withinDistance(NPCHandler.npcs[i])
		 * && (plr.RebuildNPCList == false && (plr.npcInListBitmap[id >> 3] & (1
		 * << (id & 7))) == 0)) { plr.addNewNPC(NPCHandler.npcs[i], str,
		 * updateBlock); } } } }
		 */
		for (int i = 0; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] != null) {
				int id = NPCHandler.npcs[i].npcId;
				if (plr.getInstance().RebuildNPCList == false
						&& (plr.npcInListBitmap[id >> 3] & (1 << (id & 7))) != 0) {
				} else if (plr.withinDistance(NPCHandler.npcs[i]) == false) {
				} else {
					plr.addNewNPC(NPCHandler.npcs[i], str, updateBlock);
				}
			}
		}

		plr.getInstance().RebuildNPCList = false;

		if (updateBlock.currentOffset > 0) {
			str.writeBits(14, 16383);
			str.finishBitAccess();
			str.writeBytes(updateBlock.buffer, updateBlock.currentOffset, 0);
		} else {
			str.finishBitAccess();
		}
		str.endFrameVarSizeWord();
	}

	public Stream updateBlock = new Stream(new byte[Constants.BUFFER_SIZE]);
	public int hitIcon, hitIcon2, hitMask, hitMask2;

	public void updatePlayer(Player plr, Stream str) {

		updateBlock.currentOffset = 0;
		if (updateRunning && !updateAnnounced) {
			str.createFrame(114);
			str.writeWordBigEndian(updateSeconds * 50 / 30);
		}
		plr.updateThisPlayerMovement(str);
		boolean saveChatTextUpdate = plr.isChatTextUpdateRequired();
		plr.setChatTextUpdateRequired(false);
		plr.appendPlayerUpdateBlock(updateBlock);
		plr.setChatTextUpdateRequired(saveChatTextUpdate);
		str.writeBits(8, plr.playerListSize);
		int size = plr.playerListSize;
		if (size > 255) {
			size = 255;
		}
		plr.playerListSize = 0;
		for (int i = 0; i < size; i++) {
			if (!plr.didTeleport && !plr.playerList[i].didTeleport && plr.withinDistance(plr.playerList[i])) {
				plr.playerList[i].updatePlayerMovement(str);
				plr.playerList[i].appendPlayerUpdateBlock(updateBlock);
				plr.playerList[plr.playerListSize++] = plr.playerList[i];
			} else {
				int id = plr.playerList[i].playerId;
				plr.playerInListBitmap[id >> 3] &= ~(1 << (id & 7));
				str.writeBits(1, 1);
				str.writeBits(2, 3);
			}
		}
		/*
		 * if (Region.REGION_UPDATING_ENABLED) { if (Region.getRegion(plr.absX,
		 * plr.absY) != null) { for (Player p : Region.getRegion(plr.absX,
		 * plr.absY).players) { if (p != null && p.isActive && p != plr) { if
		 * ((plr.playerInListBitmap[p.playerId >> 3] & 1 << (p.playerId & 7)) ==
		 * 0 && plr.withinDistance(p)) { plr.addNewPlayer(p, str, updateBlock);
		 * } } } } } else { for (int i = 0; i < Constants.MAX_PLAYERS; i++) { if
		 * (players[i] == null || !players[i].isActive || players[i] == plr) {
		 * continue; } int id = players[i].playerId; if
		 * ((plr.playerInListBitmap[id >> 3] & (1 << (id & 7))) != 0) {
		 * continue; } if (!plr.withinDistance(players[i])) { continue; }
		 * plr.addNewPlayer(players[i], str, updateBlock); } }
		 */
		/*
		 * for (int i = 0; i < Constants.MAX_PLAYERS; i++) { if (players[i] !=
		 * null && players[i].isActive && players[i] != plr) { final int id =
		 * players[i].playerId; if ((plr.playerInListBitmap[id >> 3] & 1 << (id
		 * & 7)) == 0 && plr.withinDistance(players[i])) {
		 * plr.addNewPlayer(players[i], str, updateBlock); } } }
		 */

		if (plr.didTeleport) {
			plr.updateVisiblePlayers(); // so if we teleport and we are in our
										// original region we are added back to
										// the list for all the players that can
										// see us
		}

		int[] addPlayers = toIntArray(plr.addPlayerList);
		int addSize = plr.getInstance().addPlayerSize;

		if (size + addSize > 255) {
			addSize = size - 255;
		}

		for (int i = 0; i < addSize; i++) {
			int id = addPlayers[i];

			if (players[id] == null || !players[id].isActive || players[id] == plr)
				continue;

			if (!plr.withinDistance(players[id]) || (plr.playerInListBitmap[id >> 3] & (1 << (id & 7))) != 0) {
				continue;
			}

			plr.addNewPlayer(players[id], str, updateBlock);
			plr.getInstance().addPlayerSize--; // you could just put these in
												// player.java
			plr.addPlayerList.remove((Integer) id); // but for the sake of the
													// tutorial, it's right
													// here.
		}

		if (plr.getInstance().addPlayerSize > 0) {
			plr.getInstance().addPlayerSize = 0;
			plr.addPlayerList.clear();
		}
		// here
		if (updateBlock.currentOffset > 0) {
			str.writeBits(11, 2047);
			str.finishBitAccess();
			str.writeBytes(updateBlock.buffer, updateBlock.currentOffset, 0);
		} else {
			str.finishBitAccess();
		}
		str.endFrameVarSizeWord();

	}

	public static void removePlayer(final Player plr) {
		if (plr.getInstance().privateChat != 2) {
			for (int d = 0; d < Constants.MAX_PLAYERS; d++) {
				if (PlayerHandler.players[d] != null && PlayerHandler.players[d].isActive) {
					final Player o = PlayerHandler.players[d];
					if (o != null) {
						o.getPA().updatePM(plr.playerId, 0);
					}
				}
			}
		}
		plr.destruct();
	}
}
