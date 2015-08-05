package main.game.players.commands;

import main.*;
import main.Connection.ConnectionType;
import main.game.npcs.NPCHandler;
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
	 * @param cmd
	 *            The command being executed.
	 * 
	 * @author Branon McClellan (KeepBotting)
	 */
	public static void handleCommands(Player c, String cmd) {
		boolean success = false;

		/**
		 * Check permission level. These commands are available for permission
		 * levels of 3.
		 */
		if (c.hasRights(Player.RIGHTS_DEVELOPER)) {

			/**
			 * Returns your position.
			 */
			if (cmd.equalsIgnoreCase("mypos")) {
				c.sendMessage("{X = " + c.getX() + ", Y = " + c.getY() + ", Z = " + c.getZ() + "}");
			}
			
			/**
			 * Schedule a game update. Argument is interpreted in minutes, e.g.
			 * ::update 5 will initiate a system update in 5 minutes.
			 */
			if (cmd.startsWith("update")) {
				String[] args = cmd.split(" ");
				int a = Integer.parseInt(args[1]);
				
				PlayerHandler.updateSeconds = (a * 60);
				PlayerHandler.updateAnnounced = false;
				PlayerHandler.updateRunning = true;
				PlayerHandler.updateStartTime = System.currentTimeMillis();
				
				c.declinePlayerTrades();
			}

			/**
			 * MAC-bans a user.
			 */
			if (cmd.startsWith("macban")) {
				try {
					String playerToMacBan = cmd.substring(7);

					for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
						if (PlayerHandler.getPlayer(i) != null) {
							if (PlayerHandler.getPlayer(i).getDisplayName().equalsIgnoreCase(playerToMacBan)) {
								Player c2 = PlayerHandler.getPlayer(i);
								Connection.addConnection(c2, ConnectionType.IDENTITY_BAN);
								c.sendMessage("You have MAC-banned " + c2.getDisplayName() + ", whose host is "
										+ c2.getHost() + ".");
								c2.disconnect();
							}
						}
					}
				} catch (Exception e) {
					c.sendMessage("Exception!");
				}
			}

			/**
			 * Un-MAC-bans a user.
			 */
			if (cmd.startsWith("unmacban")) {
				try {
					String playerToBan = cmd.substring(9);

					for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
						if (PlayerHandler.getPlayer(i) != null) {
							if (PlayerHandler.getPlayer(i).getDisplayName().equalsIgnoreCase(playerToBan)) {
								Player c2 = PlayerHandler.getPlayer(i);
								Connection.removeConnection(playerToBan, ConnectionType.IDENTITY_BAN);
								c.sendMessage("You have un-MAC-banned the user: " + c2.getDisplayName());
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
			if (cmd.startsWith("tell")) {
				String message = cmd.substring(5);

				try {
					
					for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
						if (PlayerHandler.getPlayer(i) != null) {
							PlayerHandler.getPlayer(i).sendMessage(message);
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
		if (cmd.startsWith("impersonate")) {
			/**
			 * This got a little complicated. First we pull the entire
			 * substring, everything after "::impersonate " (with that space at
			 * the end)
			 */
			String str = cmd.substring(12);

			/**
			 * Then we split it by what's before the first space, this gives us
			 * "<player>"
			 */
			String player = str.substring(0, str.indexOf(" "));

			/**
			 * Then split it again by what's after the first space, this gives
			 * us "<impersonation>"
			 */
			String impersonation = str.substring(str.indexOf(" ") + 1);

			try {
				
				for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
					if (PlayerHandler.getPlayer(i) != null) {
						Player c2 = PlayerHandler.getPlayer(i);
						if (c2.getDisplayName().equalsIgnoreCase(player)) {
							c2.getVariables().impersonation = impersonation;
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
		if (cmd.startsWith("giveadmin")) {
			String playerToGiveAdmin = cmd.substring(10);
			success = false;

			try {
				
				for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
					if (PlayerHandler.getPlayer(i) != null) {
						Player c2 = PlayerHandler.getPlayer(i);
						if (c2.getDisplayName().equalsIgnoreCase(playerToGiveAdmin)) {
							c2.setRights(Player.RIGHTS_ADIMINISTRATOR);
							
							if (c2.hasRights(Player.RIGHTS_ADIMINISTRATOR)) {
								success = true;
							}
							
							break;
						}
						
						if (success) {
							c.sendMessage("You have granted Administrator rights to " + c2.getDisplayName() + ".");
							c2.sendMessage("Congratulations! You have been granted Administrator rights by "
									+ c.getDisplayName() + ".");
							c2.sendMessage("You must refresh your game session for the changes to take effect.");
						} else {
							c.sendMessage("Unable to promote " + c2.getDisplayName() + ".");
						}

					}
				}
				
			} catch (Exception e) {
				c.sendMessage("Exception!");
			}

			/**
			 * Promote a player to Moderator.
			 */
			if (cmd.startsWith("givemod")) {
				String playerToGiveMod = cmd.substring(8);
				success = false;

				try {
					
					for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
						if (PlayerHandler.getPlayer(i) != null) {
							Player c2 = PlayerHandler.getPlayer(i);
							if (c2.getDisplayName().equalsIgnoreCase(playerToGiveMod)) {
								c2.setRights(Player.RIGHTS_MODERATOR);
								
								if (c2.hasRights(Player.RIGHTS_MODERATOR)) {
									success = true;
								}
								
								break;
							}
							
							if (success) {
								c.sendMessage("You have granted Moderator rights to " + c2.getDisplayName() + ".");
								c2.sendMessage("Congratulations! You have been granted Moderator rights by "
										+ c.getDisplayName() + ".");
								c2.sendMessage("You must refresh your game session for the changes to take effect.");
							} else {
								c.sendMessage("Unable to promote " + c2.getDisplayName() + ".");
							}
						}
					}
					
				} catch (Exception e) {
					c.sendMessage("Exception!");
				}

				/**
				 * View detailed statistics on a player.
				 */
				if (cmd.startsWith("audit")) {
					String playerToAudit = cmd.substring(6);

					try {
						
						for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
							if (PlayerHandler.getPlayer(i) != null) {
								Player c2 = PlayerHandler.getPlayer(i);
								if (c2.getDisplayName().equalsIgnoreCase(playerToAudit)) {
									break;
								}
							}
						}
						
					} catch (Exception e) {
						c.sendMessage("Exception!");
					}
				}
				
				/**
				 * Open the specified interface.
				 */
				if (cmd.startsWith("interface")) {
					String[] args = cmd.split(" ");
					c.getPA().showInterface(Integer.parseInt(args[1]));
				}
				
				/**
				 * Spawn the specified object.
				 */
				if (cmd.startsWith("object")) {
					String[] args = cmd.split(" ");					
					c.getPA().object(Integer.parseInt(args[1]), c.getX(), c.getY(), 0, 10);
				}
				
				/**
				 * Spawn the specified NPC.
				 */
				if (cmd.startsWith("npc")) {
					int npc = Integer.parseInt(cmd.substring(4));
					
					try {
						
						if (npc > 0) {
							NPCHandler.spawnNpc(c, npc, c.getX(), c.getY(), c.getZ(), 0, 120, 7, 70, 70, false,
									false);
							c.sendMessage("You spawn an NPC.");
						} else {
							c.sendMessage("No NPC by that ID.");
						}
						
					} catch (Exception e) {
						c.sendMessage("Exception!");
					}
				}
				
				/**
				 * Assume the appearance of the specified NPC.
				 */
				if (cmd.startsWith("pnpc")) {
					int pnpc = Integer.parseInt(cmd.substring(5));
					
					try {	
						
						c.getVariables().npcId2 = pnpc;
						c.getPA().requestUpdates();
						
					} catch (Exception e) {
						c.sendMessage("Exception!");
					}
				}
				
				/**
				 * Initiate the specified dialogue.
				 */
				if (cmd.startsWith("dialogue")) {
					int dialogue = Integer.parseInt(cmd.substring(9));
					
					try {
						
						c.getVariables().talkingNpc = dialogue;
						c.getDH().sendDialogues(11, c.getVariables().talkingNpc);
						
					} catch (Exception e) {
						c.sendMessage("Exception!");
					}
				}
				
				/**
				 * Play the specified graphic.
				 */
				if (cmd.startsWith("gfx")) {
					String[] args = cmd.split(" ");

					try {

						c.gfx0(Integer.parseInt(args[1]));

					} catch (Exception e) {
						c.sendMessage("Exception!");
					}
				}

				/**
				 * Play the specified animation.
				 */
				if (cmd.startsWith("anim")) {
					String[] args = cmd.split(" ");

					try {

						c.startAnimation(Integer.parseInt(args[1]));
						c.getPA().requestUpdates();

					} catch (Exception e) {
						c.sendMessage("Exception!");
					}
				}
			}
		}
	}
}
