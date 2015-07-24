package main.game.players.commands;

import main.Connection;
import main.Connection.ConnectionType;
import main.Constants;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.game.players.PlayerSave;
import main.game.players.packets.Commands;

public class PlayerModerator extends Commands {

	/**
	 * Handles Player Moderator commands
	 * 
	 * @param c
	 * @param playerCommand
	 */
	public static void handleCommands(Player c, String playerCommand) {
		if ((c.getVariables().playerRights >= 1 && c.getVariables().playerRights <= 3)
				|| c.getVariables().playerRights == 7 || c.getVariables().playerRights == 8) {
			if (playerCommand.startsWith("mypos")) {
				c.sendMessage("X: " + c.absX);
				c.sendMessage("Y: " + c.absY);
				c.sendMessage("mapregionX: " + c.mapRegionX);
				c.sendMessage("mapregionY: " + c.mapRegionY);
			}
			if (playerCommand.startsWith("xteleto")) {
				String name = playerCommand.substring(8);
				for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName.equalsIgnoreCase(name)) {
							c.getPA().movePlayer(PlayerHandler.players[i].getX(), PlayerHandler.players[i].getY(),
									PlayerHandler.players[i].heightLevel);
						}
					}
				}
			}
			if (playerCommand.startsWith("xteletome")) {
				try {
					String playerToTele = playerCommand.substring(10);
					for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].getVariables().properName.equalsIgnoreCase(playerToTele)) {
								Player c2 = PlayerHandler.players[i];
								if (c.getVariables().inWild() && (c.getVariables().playerRights != 3)) {
									c.sendMessage("You cannot move players when you are in the Wilderness.");
									return;
								}
								if (c2.getVariables().inWild() && (c.getVariables().playerRights != 3)) {
									c.sendMessage("You cannot move players when they are in the Wilderness.");
									return;
								}
								c2.sendMessage("You have been teleported to " + c.getVariables().properName);
								c2.getPA().movePlayer(c.getX(), c.getY(), c.heightLevel);
								break;
							}
						}
					}
				} catch (Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
			if (playerCommand.equals("saveall")) {
				for (final Player player : PlayerHandler.players) {
					if (player != null) {
						final Player c2 = player;
						PlayerSave.saveGame(c2);
						c2.sendMessage("<col=255>A Staff Member has saved your account.</col>");
					}
				}
				c.sendMessage("Don't use the command too often, may cause lag.");
			}
			if (playerCommand.startsWith("kick") && playerCommand.charAt(4) == ' ') {
				try {
					String playerToBan = playerCommand.substring(5);
					for (int i = 0; i < PlayerHandler.players.length; i++) {
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								PlayerHandler.players[i].disconnected = true;
							}
						}
					}
				} catch (Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
			if (playerCommand.startsWith("mute")) {
				try {
					String playerToBan = playerCommand.substring(5);
					for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Player c2 = PlayerHandler.players[i];
								Connection.addConnection(c2, ConnectionType.MUTE);
								c2.sendMessage("You have been muted by: " + c.playerName);
								c.sendMessage("You have muted: " + c2.playerName);
								break;
							}
						}
					}
				} catch (Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
			if (playerCommand.startsWith("unmute")) {
				try {
					String playerToBan = playerCommand.substring(7);
					Connection.removeConnection(playerToBan, ConnectionType.MUTE);
				} catch (Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
			if (playerCommand.startsWith("checkbank")) {
				try {
					String[] args = playerCommand.split(" ", 2);
					for (int i = 0; i < PlayerHandler.players.length; i++) {
						Player o = PlayerHandler.players[i];
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName.equalsIgnoreCase(args[1])) {
								c.getPA().otherBank(c, o);
								break;
							}
						}
					}
				} catch (Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
		}
	}
}
