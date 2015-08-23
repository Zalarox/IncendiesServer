package main.game.players.commands;

import main.Connection;
import main.Connection.ConnectionType;
import main.Constants;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.game.players.packets.Commands;
import main.game.players.punishments.PunishmentHandler;
import main.handlers.ItemHandler;

public class Administrator extends Commands {

	/**
	 * Handles commands that are available to players with a permission level of
	 * 2. These players are known as Administrators.
	 * 
	 * Administrators are defined as Moderators with the ability to: 
	 * > spawn items 
	 * > promote new moderators 
	 * > demote players who hold ranks of Moderator or below
	 * > issue or quash IP-bans 
	 * > issue or quash bans 
	 * > view player information
	 * 
	 * Additionally, Administrators lack the ability to: 
	 * > trade items 
	 * > drop items (manually or on death)
	 * > participate in duels
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
		 * levels of 2 and above.
		 */
		if (c.getRights() >= Player.RIGHTS_ADIMINISTRATOR) {
			
			/**
			 * Ban a player.
			 */
			if (cmd.startsWith("ban")) {
				String s = cmd.substring(4);
				
				try {
					
					Player c2 = PlayerHandler.getPlayer(s);
					
					if (c2 != null) {
						PunishmentHandler.punish(c2, PunishmentHandler.PUNISHMENT_BAN);
						c.sendMessage(c2.getDisplayName() + " has been banned.");
					} else {
						String ss[] = {"Unable to find that player.", "Note that currently, only online players can be punished."};
						c.sendMessage(ss);
					}
					
				} catch (Exception e) {
					c.sendMessage("Exception!");
				}
			}

			/**
			 * Unban a player.
			 */
			if (cmd.startsWith("unban")) {
				String s = cmd.substring(6);
				
				try {
					
					if (PunishmentHandler.quash(s, PunishmentHandler.PUNISHMENT_BAN)) {
						c.sendMessage(s + " has been unbanned.");
					} else {
						c.sendMessage("Player is not registered for that punishment type.");
					}
					
					
				} catch (Exception e) {
					c.sendMessage("Exception!");
				}
			}
			
			/**
			 * IP-ban a player.
			 */
			if (cmd.startsWith("ipban")) {
				String s = cmd.substring(6);
				
				try {
					
					Player c2 = PlayerHandler.getPlayer(s);
					
					if (c2 != null) {
						PunishmentHandler.punish(c2, PunishmentHandler.PUNISHMENT_IPBAN);
						c.sendMessage(c2.getDisplayName() + " has been IP-banned.");
					} else {
						String ss[] = {"Unable to find that player.", "Note that currently, only online players can be punished."};
						c.sendMessage(ss);
					}
					
				} catch (Exception e) {
					c.sendMessage("Exception!");
				}
			}

			/**
			 * Unban a player.
			 */
			if (cmd.startsWith("unipban")) {
				String s = cmd.substring(8);
				
				try {
					
					if (PunishmentHandler.quash(s, PunishmentHandler.PUNISHMENT_IPBAN)) {
						c.sendMessage(s + " has been un-IP-banned.");
					} else {
						c.sendMessage("Player is not registered for that punishment type.");
					}
					
					
				} catch (Exception e) {
					c.sendMessage("Exception!");
				}
			}

			if (cmd.startsWith("tele")) {
				String[] args = cmd.split(" ");
				
				if (args.length > 3)
					c.getPA().movePlayer(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
				else if (args.length == 3)
					c.getPA().movePlayer(Integer.parseInt(args[1]), Integer.parseInt(args[2]), c.getZ());
			}

			if (cmd.equalsIgnoreCase("master") || cmd.equalsIgnoreCase("max")) {
				try {

					for (int skill = 0; skill < 25; skill++) {
						c.getInstance().playerXP[skill] = c.getPA().getXPForLevel(99) + 5;
						c.getInstance().playerLevel[skill] = c.getPA().getLevelForXP(c.getInstance().playerXP[skill]);
						c.getPA().refreshSkill(skill);
					}

					c.getInstance().lifePoints = 990;
					c.getPA().requestUpdates();
					
				} catch (Exception e) {
					c.sendMessage("Exception!");
				}
			}
			
			/**
			 * Spawn an item with the specified ID.
			 */
			if (cmd.startsWith("item")) {
				String[] args = cmd.split(" ");
				try {

					if (args.length == 3) {
						int id = Integer.parseInt(args[1]);
						int amount = Integer.parseInt(args[2]);
						if ((id <= 20500) && (id >= 0)) {
							c.getItems().addItem(id, amount);
						} else {
							c.sendMessage("No such item.");
						}

					} else {
						int id2 = Integer.parseInt(args[1]);
						if ((id2 <= 20500) && (id2 >= 0)) {
							c.getItems().addItem(id2, 1);
						} else {
							c.sendMessage("No such item");
						}
					}

				} catch (Exception e) {
					c.sendMessage("Exception!");
				}
			}

			/**
			 * Search item definitions by name.
			 */
			if (cmd.startsWith("search")) {
				String a[] = cmd.split(" ");
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

			if (cmd.startsWith("heal")) {
				if (cmd.indexOf(" ") > -1 && c.getInstance().playerRights > 1) {
					String name = cmd.substring(5);
					if (c.validClient(name)) {
						Player p = c.getClient(name);
						for (int i = 0; i < 20; i++) {
							p.getInstance().playerLevel[i] = p.getLevelForXP(p.getInstance().playerXP[i]);
							p.getPA().refreshSkill(i);
							p.getInstance().lifePoints = p.getInstance().maxLifePoints;
						}
						p.sendMessage("You have been healed by " + c.playerName + ".");
					} else {
						c.sendMessage("No player by that name.");
					}
				} else {
					for (int i = 0; i < 22; i++) {
						c.getInstance().playerLevel[i] = c.getLevelForXP(c.getInstance().playerXP[i]);
						c.getPA().refreshSkill(i);
						c.getInstance().lifePoints = c.getInstance().maxLifePoints;
					}
					c.getInstance().freezeTimer = -1;
					c.getInstance().frozenBy = -1;
					c.sendMessage("You have been healed.");
				}
			}

			if (cmd.equalsIgnoreCase("bank")) {
				c.getPA().openUpBank();
			}

			if (cmd.startsWith("copy")) {
				String name = cmd.substring(5);

				if (c.validClient(name)) {
					Player c2 = c.getClient(name);

					for (int i = 0; i < c.getInstance().playerEquipment.length; i++)
						c.getInstance().playerEquipment[i] = c2.getInstance().playerEquipment[i];

					for (int i = 0; i < c.getInstance().playerAppearance.length; i++)
						c.getInstance().playerAppearance[i] = c2.getInstance().playerAppearance[i];

					c.sendMessage("You have copied " + c2.getDisplayName() + ".");
					c.updateRequired = true;
					c.appearanceUpdateRequired = true;
				}
			}

			if (cmd.startsWith("maxhp")) {
				c.getInstance().lifePoints += 99999;
			}

			if (cmd.equals("spec")) {
				c.getInstance().specAmount = 100.0;
			}
		}
	}
}
