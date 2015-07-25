package main.game.players.commands;

import main.Connection;
import main.Constants;
import main.Connection.ConnectionType;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.game.players.packets.Commands;

public class Developer extends Commands {

	/**
	 * Handles commands that are available to players with a permission level of
	 * 3. These players are known as Developers.
	 * 
	 * Developers are defined as the highest permission level, and have access
	 * to all commands.
	 * 
	 * @param c
	 *            The player executing the command.
	 * @param playerCommand
	 *            The command being executed.
	 * 
	 * @author KeepBotting
	 */
	public static void handleCommands(Player c, String playerCommand) {
		boolean success = false;

		/**
		 * Check permission level. These commands are available for permission
		 * levels of 3.
		 */
		if (c.getVariables().playerRights == 3) {

			/**
			 * Returns your position.
			 */
			if (playerCommand.equalsIgnoreCase("mypos")) {
				c.sendMessage("Your position is {X = " + c.absX + ", Y = " + c.absY + ", Z = " + c.heightLevel + "}");
			}

			/**
			 * UUID-bans a user.
			 */
			if (playerCommand.startsWith("macban")) {
				try {
					String playerToBan = playerCommand.substring(7);

					for (int i = 0; i < PlayerHandler.players.length; i++) {
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Connection.addConnection(PlayerHandler.players[i], ConnectionType.IDENTITY_BAN);
								c.sendMessage("You have UUID-banned the player: " + PlayerHandler.players[i].playerName
										+ " with the host: " + PlayerHandler.players[i].connectedFrom);
								PlayerHandler.players[i].disconnected = true;
							}
						}
					}
				} catch (Exception e) {
					c.sendMessage("Exception!");
				}
			}

			/**
			 * Un-UUID-bans a user.
			 */
			if (playerCommand.startsWith("unmacban")) {
				try {
					String playerToBan = playerCommand.substring(9);

					for (int i = 0; i < PlayerHandler.players.length; i++) {
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Connection.removeConnection(playerToBan, ConnectionType.IDENTITY_BAN);
								c.sendMessage(
										"You have un-UUID-banned the user: " + PlayerHandler.players[i].playerName);
								break;
							}
						}
					}
				} catch (Exception e) {
					c.sendMessage("Exception!");
				}
			}

			/**
			 * Tell the players something (essentially sends a server message)
			 */
			if (playerCommand.startsWith("tell")) {
				String message = playerCommand.substring(5);

				try {
					for (int i = 0; i < PlayerHandler.players.length; i++) {
						if (PlayerHandler.players[i] != null) {
							Player c2 = PlayerHandler.players[i];
							c2.sendMessage(message);
						}
					}
				} catch (Exception e) {
					c.sendMessage("Exception!");
				}
			}

		}

		/**
		 * Change a player's name in yell
		 */
		if (playerCommand.startsWith("impersonate")) {
			/**
			 * This got a little complicated. First we pull the entire
			 * substring, everything after "::impersonate " (with that space at
			 * the end)
			 */
			String str = playerCommand.substring(12);

			/**
			 * Then we split it by what's before the first space, this gives us
			 * "<player>"
			 */
			String player = str.substring(0, str.indexOf(" "));

			/**
			 * Then split it again by what's after the first space, this gives
			 * us "<impersonateTo>"
			 */
			String impersonateTo = str.substring(str.indexOf(" ") + 1);

			try {
				for (int i = 0; i < PlayerHandler.players.length; i++) {
					if (PlayerHandler.players[i] != null) {
						Player c2 = PlayerHandler.players[i];
						if (c2.playerName.equalsIgnoreCase(player)) {
							c2.getVariables().isImpersonated = true;
							c2.getVariables().impersonationText = impersonateTo;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Exception!");
			}
		}

		/**
		 * Promote a player to Administrator.
		 */
		if (playerCommand.startsWith("giveadmin")) {
			String playerToGiveAdmin = playerCommand.substring(10);
			success = false;

			try {
				for (int i = 0; i < PlayerHandler.players.length; i++) {
					if (PlayerHandler.players[i] != null) {
						Player c2 = PlayerHandler.players[i];
						if (c2.playerName.equalsIgnoreCase(playerToGiveAdmin)) {
							c2.getVariables().playerRights = 2;
							if (c2.getVariables().playerRights == 2) {
								success = true;
							}
							c2.logout();
							break;
						}
						if (success) {
							c.sendMessage("You have granted Administrator rights to " + c2.playerName + ".");
							c2.sendMessage("Congratulations! You have been granted Administrator permissions by "
									+ c.playerName + ".");
						}

					}
				}
			} catch (Exception e) {
				c.sendMessage("Exception!");
			}

			/**
			 * Promote a player to Moderator.
			 */
			if (playerCommand.startsWith("givemod")) {
				String playerToGiveMod = playerCommand.substring(8);
				success = false;

				try {
					for (int i = 0; i < PlayerHandler.players.length; i++) {
						if (PlayerHandler.players[i] != null) {
							Player c2 = PlayerHandler.players[i];
							if (c2.playerName.equalsIgnoreCase(playerToGiveMod)) {
								c2.getVariables().playerRights = 1;
								if (c2.getVariables().playerRights == 1) {
									success = true;
								}
								c2.logout();
								break;
							}
							if (success) {
								c.sendMessage("You have granted Moderator rights to " + c2.playerName + ".");
								c2.sendMessage("Congratulations! You have been granted Moderator permissions by "
										+ c.playerName + ".");
							}
						}
					}
				} catch (Exception e) {
					c.sendMessage("Exception!");
				}

				/**
				 * Demote a player.
				 */
				if (playerCommand.startsWith("demote")) {
					String playerToDemote = playerCommand.substring(10);
					success = false;

					try {
						for (int i = 0; i < PlayerHandler.players.length; i++) {
							if (PlayerHandler.players[i] != null) {
								Player c2 = PlayerHandler.players[i];
								if (c2.playerName.equalsIgnoreCase(playerToDemote)) {
									c2.getVariables().playerRights = 0;
									if (c2.getVariables().playerRights == 0) {
										success = true;
									}
									c2.logout();
									break;
								}
								if (success) {
									c.sendMessage("You have demoted " + c2.playerName + ".");
								}
							}
						}
					} catch (Exception e) {
						c.sendMessage("Exception!");
					}
				}

				/**
				 * View detailed statistics on a player.
				 */
				if (playerCommand.startsWith("audit")) {
					String playerToAudit = playerCommand.substring(6);

					try {
						for (int i = 0; i < PlayerHandler.players.length; i++) {
							if (PlayerHandler.players[i] != null) {
								Player c2 = PlayerHandler.players[i];
								if (c2.playerName.equalsIgnoreCase(playerToAudit)) {
									break;
								}
							}
						}
					} catch (Exception e) {
						c.sendMessage("Exception!");
					}
				}

			}
		}
	}

}
