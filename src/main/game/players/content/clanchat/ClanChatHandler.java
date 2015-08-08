package main.game.players.content.clanchat;

import java.util.concurrent.TimeUnit;

import main.Connection;
import main.Connection.ConnectionType;
import main.Constants;
import main.GameEngine;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.game.players.content.clanchat.load.ClanDataWriter;
import main.handlers.ItemHandler;
import main.util.Misc;

/**
 * @author Sanity, PJNoMore, Thelife, Tringan/Rafael Credits Swag Tringan -
 *         Clean up code and adding Documentation and Lootshare/Coinshare
 *
 */

public class ClanChatHandler {

	/**
	 * The max ammount of clans possible to create
	 */
	public ClanDataWriter[] clans = new ClanDataWriter[Constants.MAX_CLANS];

	/**
	 * Adds a player to the clan
	 * 
	 * @param playerId
	 * @param clanId
	 */
	public void addToClan(int playerId, int clanId) {
		if (clans[clanId] != null) {
			for (int j = 0; j < clans[clanId].members.length; j++) {
				if (clans[clanId].members[j] <= 0) {
					clans[clanId].members[j] = playerId;
					PlayerHandler.players[playerId].getInstance().clanId = clanId;
					Player p = PlayerHandler.players[playerId];
					p.sendMessage("Now talking in clan channel: " + clans[clanId].name);
					p.sendMessage("To talk, start each line of chat with the / symbol.");
					GameEngine.clanChat.clans[clanId].membersNumber += 1;
					messageClan(PlayerHandler.players[playerId].playerName + " has joined the channel.", clanId);
					p.getInstance().savedClan = PlayerHandler.players[playerId].playerName;
					p.getPA().sendFrame126("Leave chat", 18135);
					updateClanChat(clanId);
					return;
				}
			}
		}
	}

	/**
	 * Basicly handles the clan chat joining
	 * 
	 * @param p
	 * @param name
	 */
	public void handleClanChatJoin(Player p, String name) {
		p.sendMessage("Attempting to join channel...");
		if (p.getInstance().clanId != -1) {
			p.sendMessage("You are already in a clan!");
			p.getPA().sendFrame126("Leave chat", 18135);
			return;
		}
		for (int j = 0; j < clans.length; j++) {
			if (clans[j] != null) {
				if (clans[j].owner.equalsIgnoreCase(name)) {
					int rank;
					rank = getRanks(p.playerName, p, p.playerId, j);
					if (rank == -1) {
						p.sendMessage("You have been banned from this channel.");
						p.sendMessage("The ban will be removed in one hour.");
						return;
					}
					if (rank < clans[j].whoCanEnterChat && !clans[j].owner.equalsIgnoreCase(p.playerName)) {
						p.sendMessage("You don't have high enough rank to enter this channel!");
						return;
					}
					p.getPA().sendFrame126("Leave chat", 18135);
					addToClan(p.playerId, j);
					return;
				}
			}
		}
		p.sendMessage("A channel with this name does not exist.");
	}

	/**
	 * Opens the clan
	 * 
	 * @return
	 */
	public int openClan() {
		for (int j = 0; j < clans.length; j++) {
			if (clans[j] == null || clans[j].owner == "")
				return j;
		}
		return -1;
	}

	/**
	 * Checks if a clan Name is available
	 * 
	 * @param name
	 * @return
	 */
	public boolean validName(String name) {
		for (int j = 0; j < clans.length; j++) {
			if (clans[j] != null) {
				if (clans[j].name.equalsIgnoreCase(name))
					return false;
			}
		}
		return true;
	}

	/**
	 * Loads some clan chat details
	 * 
	 * @param owner
	 * @param name
	 * @param lootshare
	 */
	public void loadClan(String owner, String name, int lootshare) {
		if (openClan() >= 0) {
			if (validName(name)) {
				clans[openClan()] = new ClanDataWriter(owner, name, lootshare);
			}
		}
	}

	/**
	 * Actually creates the clan
	 * 
	 * @param p
	 * @param name
	 */
	public void makeClan(Player p, String name) {
		if (openClan() >= 0) {
			if (validName(name)) {
				p.getInstance().clanId = openClan();
				loadClan(p.playerName, name, 0);
				addToClan(p.playerId, p.getInstance().clanId);
				// Server.pJClans.saveClan(p.playerName, name, loot);
			} else {
				p.sendMessage("A clan with this name already exists.");
			}
		} else {
			p.sendMessage("Your clan chat request could not be completed.");
		}
	}

	/**
	 * Leaves a clan chat
	 * 
	 * @param playerId
	 * @param clanId
	 */
	public void leaveClan(int playerId, int clanId, boolean logout) {
		if (clanId < 0) {
			Player p = PlayerHandler.players[playerId];
			p.sendMessage("You are not in a clan.");
			p.getPA().sendFrame126("Join chat", 18135);
			return;
		}
		if (clans[clanId] != null) {
			if (PlayerHandler.players[playerId] != null) {
				Player p = PlayerHandler.players[playerId];
				if (!logout) {
					p.getInstance().savedClan = null;
					p.sendMessage("You have left the channel.");
				}
				p.getPA().sendFrame126("Join chat", 18135);
				p.getPA().clearClanChat();
				for (int j = 0; j < clans[clanId].members.length; j++) {
					if (clans[clanId].members[j] == playerId) {
						clans[clanId].members[j] = -1;
					}
				}
				/*
				 * for (int j = 0; j < clans[clanId].members.length; j++) {
				 * Player p2 = PlayerHandler.players[playerId];
				 * //c2.sendMessage(c.playerName+" has left the clan."); }
				 */
			}
			messageClan(PlayerHandler.players[playerId].playerName + " has left the channel.", clanId);
			updateClanChat(clanId);
			GameEngine.clanChat.clans[clanId].membersNumber -= 1;
			PlayerHandler.players[playerId].getInstance().clanId = -1;
		} else {
			Player p = PlayerHandler.players[playerId];
			PlayerHandler.players[playerId].getInstance().clanId = -1;
			p.sendMessage("You are not in a clan.");
		}
	}

	/**
	 * Forces a player to leave a clan
	 * 
	 * @param c
	 * @param name
	 */
	public void kickPlayerFromClan(Player c, String name) {
		if (c.playerName.equalsIgnoreCase(name)) {
			c.sendMessage("You cannot kick yourself from a channel!");
			return;
		}
		if (c.getInstance().clanId < 0) {
			c.sendMessage("You are not in a clan.");
			return;
		}
		int rank;
		rank = getRanks(c.playerName, c, c.playerId, c.getInstance().clanId);
		if (rank < clans[c.getInstance().clanId].whoCanKickOnChat && !isOwner(c)) {
			c.sendMessage("You don't have high enough rank to kick.");
			return;
		}
		if (isOwnerByName(c, name)) {
			c.sendMessage("You cannot kick the host!");
			return;
		}
		for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				if (PlayerHandler.players[i].playerName.equalsIgnoreCase(name)) {
					Player c2 = PlayerHandler.players[i];

					if (c2.getInstance().playerRights >= 2) {
						c.sendMessage("You may not kick a staff member from your clan.");
						return;
					}

					GameEngine.clanChat.clans[c.getInstance().clanId].addName(c2.playerName, -1);
					GameEngine.clanChat.clans[c2.getInstance().clanId].membersNumber -= 1;
					
					c2.getInstance().clanId = -1;
					c2.getInstance().savedClan = null;
					c2.getPA().sendFrame126("Join chat", 18135);
					c2.sendMessage("You have been kicked from the channel.");
					c2.getPA().clearClanChat();
					
					c.sendMessage("You have kicked " + c2.playerName + " from the channel.");
					
					for (int j = 0; j < clans[c.getInstance().clanId].members.length; j++) {
						if (clans[c.getInstance().clanId].members[j] == i) {
							clans[c.getInstance().clanId].members[j] = -1;
						}
					}
				}
			}
		}
		updateClanChat(c.getInstance().clanId);
	}

	/**
	 * Changes the clan name
	 * 
	 * @param p
	 * @param name
	 */
	public void changeClanName(Player p, String name) {
		if (name.length() > 12) {
			p.sendMessage("Maximum length of the channel name is 12.");
			return;
		}
		for (int i = 0; i < clans.length; i++) {
			if (clans[i] != null) {
				if (GameEngine.clanChat.clans[i].owner.equalsIgnoreCase(p.playerName)) {
					clans[i].name = name;
					p.updateClanChatEditInterface(true);
					clans[i].changesMade = true;
				}
			}
		}
	}

	/**
	 * Checks if a player is owner of the clan
	 * 
	 * @param p
	 * @return
	 */
	public boolean isOwner(Player p) {
		return (clans[p.getInstance().clanId].owner.equalsIgnoreCase(p.playerName)
		/** || p.getVariables().playerRights >= 2 */
		);
	}

	/**
	 * Checks if a name of a player is owner of the clan
	 * 
	 * @param p
	 * @param name
	 * @return
	 */
	public boolean isOwnerByName(Player p, String name) {
		return (clans[p.getInstance().clanId].owner.equalsIgnoreCase(name));
	}

	/**
	 * Updates the clan chat interface
	 * 
	 * @param clanId
	 */
	public void updateClanChat(int clanId) {
		for (int j = 0; j < clans[clanId].members.length; j++) {
			if (clans[clanId].members[j] <= 0)
				continue;
			if (PlayerHandler.players[clans[clanId].members[j]] != null) {
				Player c = PlayerHandler.players[clans[clanId].members[j]];
				c.getPA().sendFrame126("Talking in: " + Misc.capitalize(clans[clanId].name), 18139);
				c.getPA().sendFrame126("Owner: " + clans[clanId].owner, 18140);
				String name = "";
				int slotToFill = 18144;
				for (int i = 0; i < clans[clanId].members.length; i++) {
					if (clans[clanId].members[i] > 0) {
						if (PlayerHandler.players[clans[clanId].members[i]] != null) {
							if (c.getInstance().clanId != -1) {
								if (GameEngine.clanChat.clans[c.getInstance().clanId].owner
										.equalsIgnoreCase(PlayerHandler.players[clans[clanId].members[i]].playerName)) {
									name = "[OWN]" + PlayerHandler.players[clans[clanId].members[i]].playerName;
								} else {
									name = getRankTag(PlayerHandler.players[clans[clanId].members[i]].playerName, c,
											PlayerHandler.players[clans[clanId].members[i]].playerId) + ""
											+ PlayerHandler.players[clans[clanId].members[i]].playerName;
								}
								c.getPA().sendFrame126(name, slotToFill);
								slotToFill++;
							}
						}
					}
				}
				for (int k = slotToFill; k < 18244; k++) {
					c.getPA().sendFrame126("[REG]", k);
				}
			}
		}
	}

	/*
	 * public boolean isInClan(Player p) { for(int i = 0; i < clans.length; i++)
	 * { if(clans[i] != null){ for(int j = 0; i < clans[i].members.length; j++)
	 * { if(clans[i].members[j] == p.playerId){ return true; } } } } return
	 * false; }
	 */

	/**
	 * Sends a message to the clan Usually informing something for example that
	 * a player left
	 * 
	 * @param message
	 * @param clanId
	 */
	public void messageClan(String message, int clanId) {
		if (clanId < 0)
			return;
		for (int j = 0; j < clans[clanId].members.length; j++) {
			if (clans[clanId].members[j] < 0)
				continue;
			if (PlayerHandler.players[clans[clanId].members[j]] != null) {
				Player p = PlayerHandler.players[clans[clanId].members[j]];
				p.sendMessage("@red@" + message);
			}
		}
	}

	/**
	 * Sends a player message to the clan
	 * 
	 * @param playerId
	 * @param message
	 * @param clanId
	 */
	public void playerMessageToClan(int playerId, String message, int clanId) {
		if (clanId < 0)
			return;
		Player p = PlayerHandler.players[playerId];
		int rank;
		rank = getRanks(p.playerName, p, playerId, clanId);
		if (rank < clans[clanId].whoCanTalkOnChat && !isOwner(p)) {
			p.sendMessage("You don't have a high enough rank to speak in this channel!");
			return;
		}
		if (Connection.containsConnection(PlayerHandler.players[playerId].playerName, ConnectionType.forName("MUTE"),
				false)
				|| Connection.containsConnection(PlayerHandler.players[playerId].connectedFrom,
						ConnectionType.forName("IPMUTE"), false)
				|| Connection.containsConnection(p.getInstance().identityPunishment,
						ConnectionType.forName("IDENTITY_MUTE"), false)) {
			p.sendMessage("You are muted.");
			return;
		}
		for (int j = 0; j < clans[clanId].members.length; j++) {
			if (clans[clanId].members[j] <= 0)
				continue;
			if (PlayerHandler.players[clans[clanId].members[j]] != null) {
				Player b = PlayerHandler.players[clans[clanId].members[j]];
				b.sendClan(PlayerHandler.players[playerId].playerName, Misc.capitalizeFirstLetter(message),
						Misc.capitalizeFirstLetter(clans[clanId].name),
						PlayerHandler.players[playerId].getInstance().playerRights);
			}
		}
	}

	/**
	 * Sends a message when coinshare/lootshare is toggled
	 * 
	 * @param clanId
	 * @param message
	 * @param lootShare
	 */
	public void sendToggleMessage(int clanId, String message, boolean lootShare) {
		if (clanId >= 0) {
			for (int j = 0; j < clans[clanId].members.length; j++) {
				if (clans[clanId].members[j] <= 0)
					continue;
				if (PlayerHandler.players[clans[clanId].members[j]] != null) {
					Player p = PlayerHandler.players[clans[clanId].members[j]];
					p.sendClan(lootShare ? "LootShare" : "CoinShare", message, clans[clanId].name, 2);
				}
			}
		}
	}

	/**
	 * Handles the lootshare/coinshare button
	 * 
	 * @param p
	 */
	public void handleShareButton(Player p) {
		if (System.currentTimeMillis() - p.getInstance().clanDelay >= 1500) {
			if (p.getInstance().clanId >= 0
					&& (GameEngine.clanChat.clans[p.getInstance().clanId].owner.equalsIgnoreCase(p.playerName)
							|| PlayerHandler.players[p.playerId].getInstance().playerRights == 2
							|| PlayerHandler.players[p.playerId].getInstance().playerRights == 3)) {
				boolean lootShareOn = GameEngine.clanChat.clans[p.getInstance().clanId].lootshare == 1;
				boolean bothOff = GameEngine.clanChat.clans[p.getInstance().clanId].lootshare == 0;
				if (bothOff && GameEngine.clanChat.clans[p.getInstance().clanId].CSLS == 0) {
					GameEngine.clanChat.clans[p.getInstance().clanId].CSLS = 1;
					GameEngine.clanChat.clans[p.getInstance().clanId].lootshare = 1;
					GameEngine.clanChat.sendToggleMessage(p.getInstance().clanId,
							"LootShare has been toggled to ON by the clan leader.", true);
					GameEngine.clanChat.updateClanChat(p.getInstance().clanId);
					return;
				} else if (lootShareOn && GameEngine.clanChat.clans[p.getInstance().clanId].CSLS == 1) {
					GameEngine.clanChat.clans[p.getInstance().clanId].CSLS = 2;
					GameEngine.clanChat.clans[p.getInstance().clanId].lootshare = 0;
					GameEngine.clanChat.sendToggleMessage(p.getInstance().clanId,
							"LootShare has been toggled to OFF by the clan leader.", true);
					GameEngine.clanChat.updateClanChat(p.getInstance().clanId);
					return;
				} else if (GameEngine.clanChat.clans[p.getInstance().clanId].CSLS == 2) {
					if (GameEngine.clanChat.clans[p.getInstance().clanId].membersNumber <= 1) {
						p.sendMessage("There must be at least two members in the channel to toggle CoinShare ON.");
						GameEngine.clanChat.clans[p.getInstance().clanId].CSLS = 0;
						GameEngine.clanChat.clans[p.getInstance().clanId].lootshare = 0;
						GameEngine.clanChat.updateClanChat(p.getInstance().clanId);
						return;
					}
					GameEngine.clanChat.clans[p.getInstance().clanId].CSLS = 3;
					GameEngine.clanChat.clans[p.getInstance().clanId].lootshare = 2;
					GameEngine.clanChat.sendToggleMessage(p.getInstance().clanId,
							"CoinShare has been toggled to ON by the clan leader.", false);
					GameEngine.clanChat.updateClanChat(p.getInstance().clanId);
					return;
				} else if (GameEngine.clanChat.clans[p.getInstance().clanId].lootshare == 2
						&& GameEngine.clanChat.clans[p.getInstance().clanId].CSLS == 3) {
					GameEngine.clanChat.clans[p.getInstance().clanId].CSLS = 0;
					GameEngine.clanChat.clans[p.getInstance().clanId].lootshare = 0;
					GameEngine.clanChat.sendToggleMessage(p.getInstance().clanId,
							"CoinShare has been toggled to OFF by the clan leader.", false);
					GameEngine.clanChat.updateClanChat(p.getInstance().clanId);
					return;
				}
			} else {
				p.sendMessage("Only the owner of the clan has the power to do that.");
			}
			p.getInstance().clanDelay = System.currentTimeMillis();
		}
	}

	/**
	 * Sends a message when a player gets a loot using LootShare or Coinshare
	 * 
	 * @param p
	 * @param clanId
	 * @param ownMessage
	 * @param message
	 * @param coinShare
	 */
	public void sendLootMessage(final Player p, final int clanId, final String ownMessage, final String message,
			boolean coinShare, final int itemReceived, final int amount) {
		if (clanId > -1) {
			for (final int member : clans[clanId].members) {
				if (member <= 0)
					continue;
				if (PlayerHandler.players[member] != null) {
					final Player members = PlayerHandler.players[member];
					if (p != members && !coinShare) {
						members.sendMessage(message);
						if (members.getInstance().goodLootDistance && !coinShare) {
							setLootSharePotential(members,
									(members.getShops().getItemShopValue(itemReceived) * amount) / 5000);
							members.sendMessage("Your chance of receiving loot has improved.");
						}
					}
				}
			}
			p.sendMessage("<col=008800>" + ownMessage + "</col>");
			setLootSharePotential(p, -((p.getShops().getItemShopValue(itemReceived) * amount) / 5000));
		}
	}

	/**
	 * Items that can't be shared example(Bones)
	 */
	public static final int[] unallowed = { 592, 530, 526, 536, 1333, 995, 1247, 1089, 1047, 1319 };

	/**
	 * Handles coinShare/lootShare
	 * 
	 * @param p
	 * @param item
	 * @param amount
	 * @param x
	 * @param y
	 * @param coinShare
	 */
	public void handleShare(final Player p, final int item, final int amount, final int x, final int y,
			final int height, boolean coinShare) {
		for (final int element : unallowed) {
			if (item == element) {
				return;
			}
		}
		p.getInstance().goodLootDistance = false;
		if (p.getInstance().clanId > -1) {
			final int[] playersCC = new int[100];
			int players = 0;
			for (final int member : clans[p.getInstance().clanId].members) {
				if (member < 1) {
					continue;
				}
				if (PlayerHandler.players[member] != null) {
					final Player o = PlayerHandler.players[member];
					if (Constants.goodDistance(o.absX, o.absY, x, y, 20) && p.heightLevel == o.heightLevel) {
						playersCC[players] = o.playerId;
						players++;
						o.getInstance().goodLootDistance = true;
					}
					if (players > 98) {
						break;
					}
				}
			}
			if (players < 2 || !p.getInstance().inMulti()) {
				ItemHandler.createGroundItem(p, item, x, y, height, amount, p.playerId);
				if (!coinShare) {
					sendLootMessage(p, p.getInstance().clanId,
							"You received: " + amount + "x " + p.getItems().getItemName(item) + ".",
							p.playerName + " has just received " + amount + "x " + p.getItems().getItemName(item) + ".",
							false, item, amount);
				}
			} else {
				if (coinShare) {
					int total = (p.getShops().getItemShopValue(item) * amount) / players;
					for (final int member : clans[p.getInstance().clanId].members) {
						if (member < 1) {
							continue;
						}
						Player i = PlayerHandler.players[member];
						if (i.getInstance().goodLootDistance) {
							ItemHandler.createGroundItem(i, 995, x, y, height, total, i.playerId);
							sendLootMessage(i, i.getInstance().clanId,
									"You received " + total + " gold as your split of this drop: " + amount + " x "
											+ i.getItems().getItemName(item) + ".",
									"You received " + total + " gold as your split of this drop: " + amount + " x "
											+ i.getItems().getItemName(item) + ".",
									true, item, amount);
						}
					}
					return;
				}
				Player highest = null;
				for (int playerID : playersCC) {
					Player player = PlayerHandler.players[playerID];
					if (player != null)
						if (highest == null || (player.getInstance().lootSharePotential > highest
								.getInstance().lootSharePotential))
							highest = player;
				}
				ItemHandler.createGroundItem(highest, item, x, y, height, amount, highest.playerId);
				sendLootMessage(highest, highest.getInstance().clanId,
						"You received: " + amount + "x " + highest.getItems().getItemName(item) + ".",
						highest.playerName + " has just received " + amount + "x "
								+ highest.getItems().getItemName(item) + ".",
						false, item, amount);
				return;
			}
		}
	}

	/**
	 * Sets the LootShare Potential
	 * 
	 * @param p
	 * @param toAdd
	 */
	public void setLootSharePotential(Player p, int toAdd) {
		int daysWithoutKill = (int) TimeUnit.MILLISECONDS
				.toDays(System.currentTimeMillis() - p.getInstance().lastLootDate);
		if (daysWithoutKill > 0) {
			float percentFromLSP = (float) (daysWithoutKill * (p.getInstance().lootSharePotential * 0.10));
			p.getInstance().lootSharePotential -= percentFromLSP;
		}
		p.getInstance().lootSharePotential += toAdd;
		p.getInstance().lastLootDate = System.currentTimeMillis();
	}

	/**
	 * The existant clan ranks
	 * 
	 * @param i
	 * @param clanId
	 * @param name
	 * @return
	 */
	private boolean existantRanks(int i, int clanId, String name) {
		boolean[] ranks = { clans[clanId].isBanned(name), clans[clanId].isFriend(name), clans[clanId].isRecruit(name),
				clans[clanId].isCorporal(name), clans[clanId].isSergeant(name), clans[clanId].isLieutenant(name),
				clans[clanId].isCaptain(name), clans[clanId].isGeneral(name) };
		return ranks[i];
	}

	/**
	 * Gets the String for the ranks (Client sided - image showing)
	 * 
	 * @param name
	 * @param p
	 * @param playerId
	 * @return
	 */
	public String getRankTag(String name, Player p, int playerId) {
		String[] ranks = { "[MOD]", "[FRI]", "[REC]", "[COR]", "[SER]", "[LIE]", "[BER]", "[VR]" };
		if (PlayerHandler.players[playerId].getInstance().playerRights == 2
				|| PlayerHandler.players[playerId].getInstance().playerRights == 3)
			return ranks[0];
		for (int rankNr = 2; rankNr < 8; rankNr++)
			if (existantRanks(rankNr, p.getInstance().clanId, name))
				return ranks[rankNr];
		if (clans[p.getInstance().clanId].isFriend(name))
			return "[FRI]";
		return "[REG]";
	}

	/**
	 * Gets the rank id's
	 * 
	 * @param name
	 * @param p
	 * @param playerId
	 * @param clanId
	 * @return
	 */
	public int getRanks(String name, Player p, int playerId, int clanId) {
		if (PlayerHandler.players[playerId].getInstance().playerRights == 2
				|| PlayerHandler.players[playerId].getInstance().playerRights == 3)
			return 9;
		if (existantRanks(0, clanId, name))
			return -1;
		for (int rankNr = 2; rankNr < 8; rankNr++)
			if (existantRanks(rankNr, clanId, name))
				return rankNr;
		if (existantRanks(1, clanId, name))
			return 1;
		return 0;
	}

}
