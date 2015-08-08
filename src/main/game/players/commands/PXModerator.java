package main.game.players.commands;

import main.Connection;
import main.Connection.ConnectionType;
import main.Constants;
import main.game.npcs.NPCHandler;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.game.players.packets.Commands;
import main.handlers.ItemHandler;

public class PXModerator extends Commands {

	/**
	 * Handles Project-Exile Moderator commands
	 * 
	 * @param c
	 * @param playerCommand
	 */
	public static void handleCommands(Player c, String playerCommand) {
		if (c.getInstance().playerRights >= 2 && c.getInstance().playerRights <= 3) {
			if (playerCommand.startsWith("interface")) {
				String[] args = playerCommand.split(" ");
				c.getPA().showInterface(Integer.parseInt(args[1]));
			}
			if (playerCommand.startsWith("object")) {
				String[] args = playerCommand.split(" ");
				c.getPA().object(Integer.parseInt(args[1]), c.absX, c.absY, 0, 10);
			}
			if (playerCommand.startsWith("dialogue")) {
				try {
					int newNPC = Integer.parseInt(playerCommand.substring(9));
					c.getInstance().talkingNpc = newNPC;
					c.getDH().sendDialogues(11, c.getInstance().talkingNpc);
				} catch (Exception e) {
				}
			}
			if (playerCommand.startsWith("gfx")) {
				String[] args = playerCommand.split(" ");
				c.gfx0(Integer.parseInt(args[1]));
			}
			if (playerCommand.startsWith("anim")) {
				String[] args = playerCommand.split(" ");
				c.startAnimation(Integer.parseInt(args[1]));
				c.getPA().requestUpdates();
			}
			if (playerCommand.startsWith("tele")) {
				String[] arg = playerCommand.split(" ");
				if (arg.length > 3)
					c.getPA().movePlayer(Integer.parseInt(arg[1]), Integer.parseInt(arg[2]), Integer.parseInt(arg[3]));
				else if (arg.length == 3)
					c.getPA().movePlayer(Integer.parseInt(arg[1]), Integer.parseInt(arg[2]), c.heightLevel);
			}
			if (playerCommand.startsWith("ban")) {
				try {
					String playerToBan = playerCommand.substring(4);
					for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Connection.addConnection(PlayerHandler.players[i], ConnectionType.BAN);
								PlayerHandler.players[i].disconnected = true;
							}
						}
					}
				} catch (Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
			if (playerCommand.startsWith("unban")) {
				try {
					String playerToBan = playerCommand.substring(6);
					Connection.removeConnection(playerToBan, ConnectionType.BAN);
					c.sendMessage(playerToBan + " has been unbanned.");
				} catch (Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
			if (playerCommand.startsWith("unmute")) {
				try {
					String playerToBan = playerCommand.substring(6);
					Connection.removeConnection(playerToBan, ConnectionType.MUTE);
					c.sendMessage(playerToBan + " has been unmuted.");
				} catch (Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
			if (playerCommand.startsWith("getip")) {
				String getPlayerIP = playerCommand.substring(6);
				for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName.equalsIgnoreCase(getPlayerIP))
							c.sendMessage(PlayerHandler.players[i].playerName + "'s IP is "
									+ PlayerHandler.players[i].connectedFrom);
					}
				}
			}
			if (playerCommand.startsWith("ipmute")) {
				try {
					String playerToBan = playerCommand.substring(7);
					for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Connection.addConnection(PlayerHandler.players[i], ConnectionType.forName("IPMUTE"));
								c.sendMessage("You have IP Muted the user: " + PlayerHandler.players[i].playerName);
								Player c2 = PlayerHandler.players[i];
								c2.sendMessage("You have been muted by: " + c.playerName);
								break;
							}
						}
					}
				} catch (Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
			if (playerCommand.startsWith("bpane")) {
				String opacity = playerCommand.substring(6);// my mistake
				c.getPA().drawBlackPane(Integer.parseInt(opacity), true);
			}
			if (playerCommand.startsWith("search")) {
				String a[] = playerCommand.split(" ");
				String name = "";
				int results = 0;
				for (int i = 1; i < a.length; i++)
					name = name + a[i] + " ";
				name = name.substring(0, name.length() - 1);
				c.sendMessage("Searching: " + name);
				for (int j = 0; j < ItemHandler.ItemList.length; j++) {
					if (ItemHandler.ItemList[j] != null)
						if (ItemHandler.ItemList[j].itemName.replace("_", " ").toLowerCase()
								.contains(name.toLowerCase())) {
							c.sendMessage("<col=16711680>" + ItemHandler.ItemList[j].itemName.replace("_", " ") + " - "
									+ ItemHandler.ItemList[j].itemId);
							results++;
						}
				}
				c.sendMessage(results + " results found...");
			}
			if (playerCommand.startsWith("movehome")) {
				try {
					String playerToBan = playerCommand.substring(9);
					for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Player c2 = PlayerHandler.players[i];
								c2.teleportToX = 3096;
								c2.teleportToY = 3468;
								c2.heightLevel = c.heightLevel;
								c.sendMessage("You have teleported " + c2.playerName + " to home.");
								c2.sendMessage("You have been teleported to home.");
							}
						}
					}
				} catch (Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
			if (playerCommand.startsWith("unipmute")) {
				try {
					String playerToBan = playerCommand.substring(9);
					for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Connection.removeConnection(playerToBan, ConnectionType.forName("IPMUTE"));
								c.sendMessage("You have Un Ip-Muted the user: " + PlayerHandler.players[i].playerName);
								break;
							}
						}
					}
				} catch (Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
			if (playerCommand.startsWith("pnpc")) {
				try {
					int newNPC = Integer.parseInt(playerCommand.substring(5));
					c.getInstance().npcId2 = newNPC;
					c.getPA().requestUpdates();
				} catch (Exception e) {
				}
			}
			if (playerCommand.startsWith("npcname")) {
				int npc = Integer.parseInt(playerCommand.substring(8));
				c.sendMessage("The name of the npc with the id " + npc + " is " + NPCHandler.getNpcListName(npc) + ".");
			}
			if (playerCommand.startsWith("npc")) {
				try {
					int newNPC = Integer.parseInt(playerCommand.substring(4));
					if (newNPC > 0) {
						NPCHandler.spawnNpc(c, newNPC, c.absX - 4, c.absY, c.heightLevel, 0, 120, 7, 70, 70, false,
								false);
						c.sendMessage("You spawn a Npc.");
					} else {
						c.sendMessage("No such NPC.");
					}
				} catch (Exception e) {

				}
			}
		}
	}
}
