package incendius.game.players.commands;

import incendius.Connection;
import incendius.Constants;
import incendius.Connection.ConnectionType;
import incendius.game.players.JailHandler;
import incendius.game.players.Player;
import incendius.game.players.PlayerHandler;
import incendius.game.players.packets.Commands;

public class Moderator extends Commands {

	/**
	 * Handles commands that are available to players with a permission level of
	 * 1. These players are known as Moderators.
	 * 
	 * Moderators are defined as Normal Players with the ability to: 
	 * > issue or quash mutes
	 * > issue or quash timed mutes
	 * > issue or quash IP-mutes
	 * > send players to and from jail
	 * > teleport to players (but not teleport players to them)
	 * 
	 * @param c
	 *            The player executing the command.
	 * @param cmd
	 *            The command being executed.
	 * 
	 * @author Branon McClellan (KeepBotting)
	 */
	public static void handleCommands(Player c, String cmd) {
		
		/**
		 * Check permission level. These commands are available for permission
		 * levels of 1 and above.
		 */
		if (c.getRights() >= Player.RIGHTS_MODERATOR) {
			
			/**
			 * Prevent a player from speaking. Optional argument dictates the
			 * number of hours the mute should last.
			 * 
			 * @usage ::mute [name]
			 *        Mute a player indefinitely (until they are manually unmuted).
			 * 
			 * @usage ::mute [name] 12
			 *        Mute a player for 12 hours, after which the mute will automatically expire.
			 *        
			 * TODO support for timed mutes.
			 */
			if (cmd.startsWith("mute")) {
				String playerToMute = cmd.substring(5);
				try {
			
					for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName.equalsIgnoreCase(playerToMute)) {
								
								Player c2 = PlayerHandler.players[i];
								Connection.addConnection(c2, ConnectionType.MUTE);
								
								c2.sendMessage("You have been muted by " + c.getDisplayName() + ".");
								c.sendMessage("You have muted " + c.getDisplayName() + ".");
								break;
							}
						}
					}
					
				} catch (Exception e) {
					c.sendMessage("Exception!");
				}
			}
			
			
			/**
			 * Remove a mute that was placed on a player.
			 */
			if (cmd.startsWith("unmute")) {
				String playerToUnmute = cmd.substring(6);
				try {
					
					Connection.removeConnection(playerToUnmute, ConnectionType.MUTE);
					c.sendMessage(playerToUnmute + " has been unmuted.");
					
				} catch (Exception e) {
					c.sendMessage("Exception!");
				}
			}
			
			/**
			 * Prevent all players on an IP from speaking.
			 */
			if (cmd.startsWith("ipmute")) {
				String ipToMute = cmd.substring(7);
				
				try {
				
					for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
						if (PlayerHandler.getPlayer(i) != null) {
							Player c2 = PlayerHandler.getPlayer(i);
							if (c2.getDisplayName().equalsIgnoreCase(ipToMute)) {
								Connection.addConnection(c2, ConnectionType.forName("IPMUTE"));
								
								c.sendMessage("You have IP-muted " + c2.getDisplayName() + ".");
								c2.sendMessage("You have been muted by " + c.getDisplayName() + ".");
								break;
							}
						}
					}
					
				} catch (Exception e) {
					c.sendMessage("Exception!");
				}
			}

			/**
			 * Remove an mute that was placed on an IP.
			 */
			if (cmd.startsWith("unipmute")) {
				String ipToUnmute = cmd.substring(9);
				try {
					
					for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
						if (PlayerHandler.getPlayer(i) != null) {
							
							Player c2 = PlayerHandler.getPlayer(i);
							if (c2.getDisplayName().equalsIgnoreCase(ipToUnmute)) {
								Connection.removeConnection(ipToUnmute, ConnectionType.forName("IPMUTE"));
								c.sendMessage("You have un-IP-muted " + c2.getDisplayName() + ".");
								break;
								
							} else {
								c.sendMessage("No player by that display name.");
							}
						}
					}
					
				} catch (Exception e) {
					c.sendMessage("Exception!");
				}
			}
			
			/**
			 * Demote a player.
			 */
			if (cmd.startsWith("demote")) {
				String playerToDemote = cmd.substring(7);

				try {

					for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
						if (PlayerHandler.getPlayer(i) != null) {
							Player c2 = PlayerHandler.getPlayer(i);
							if (c2.getDisplayName().equalsIgnoreCase(playerToDemote)) {

								if ((c2.getRights() == c.getRights()) && (c.getRights() < Player.RIGHTS_DEVELOPER)) {
									c.sendMessage("You may not demote a staff member with a rank equal to yourself.");
									return;
								} else if ((c2.getRights() >= c.getRights()) && (c.getRights() < Player.RIGHTS_DEVELOPER)) {
									c.sendMessage("You may not demote a staff member with a higher rank than you.");
									return;
								}
								
								c2.setRights(Player.RIGHTS_PLAYER);
								
								if (c2.hasRights(Player.RIGHTS_PLAYER)) {
									c.sendMessage("You have demoted " + c2.getDisplayName() + ".");
								} else {
									c.sendMessage("Unable to demote " + c2.getDisplayName() + ".");
								}
								
								c2.logout();
								break;
								
							} else {
								c.sendMessage("No player by that display name.");
							}
						}
					}

				} catch (Exception e) {
					c.sendMessage("Exception!");
				}
			}
			
			/**
			 * Teleport to a player.
			 */
			if (cmd.startsWith("goto")) {
				String name = cmd.substring(5);

				if (name.equalsIgnoreCase(c.getDisplayName())) {
					c.sendMessage("Why would you want to teleport to yourself?");
					return;
				}
				
				Player c2 = PlayerHandler.getPlayer(name);
				
				if (c2 != null) {
					
					c.getPA().movePlayer(c2.getX(), c2.getY(), c2.getZ());
					c.sendMessage("You have teleported to " + c2.getDisplayName() + ".");
					c2.sendMessage(c.getDisplayName() + " has teleported to you.");
					
				} else {
					c.sendMessage("Unable to find " + name + ".");
				}

			}
			
			if (cmd.startsWith("jail")) {
				String name = cmd.substring(5);

				Player c2 = PlayerHandler.getPlayer(name);
				
				if (name.equalsIgnoreCase(c.getDisplayName())) {
					c.sendMessage("Why would you want to jail yourself?");
					return;
				}

				if (c2 != null) {
					
					if ((c2.getRights() == c.getRights()) && (c.getRights() < Player.RIGHTS_DEVELOPER)) {
						c.sendMessage("You may not jail a staff member with a rank equal to yourself.");
						return;
					} else if ((c2.getRights() >= c.getRights()) && (c.getRights() < Player.RIGHTS_DEVELOPER)) {
						c.sendMessage("You may not jail a staff member with a higher rank than you.");
						return;
					}
					
					if (c2.isJailed() && JailHandler.isJailed(c2)) {
						c.sendMessage("That player is already in jail!");
						return;
					}

					/**
					 * Since the Jail class does all our work for us, we can use
					 * a single simple conditional over here in the command.
					 */
					if (JailHandler.jail(c2)) {
						c.sendMessage("You have sent " + c2.getDisplayName() + " to jail.");
					} else {
						c.sendMessage("Unable to jail " + c2.getDisplayName() + ".");
					}
					
				} else {
					c.sendMessage("Unable to find " + name + ".");
				}

			}
			
			if (cmd.startsWith("unjail")) {
				String name = cmd.substring(7);

				Player c2 = PlayerHandler.getPlayer(name);

				if (c2 != null) {
					
					if (!c2.isJailed() && !JailHandler.isJailed(c2)) {
						c.sendMessage("That player isn't in jail!");
						return;
					}
					
					/**
					 * Since the Jail class does all our work for us, we can use
					 * a single simple conditional over here in the command.
					 */
					if (JailHandler.unjail(c2)) {
						c.sendMessage("You have freed " + c2.getDisplayName() + " from jail.");
					} else {
						c.sendMessage("Unable to unjail " + c2.getDisplayName() + ".");
					}
					
				} else {
					c.sendMessage("Unable to find " + name + ".");
				}

			}
		}
	}
}
