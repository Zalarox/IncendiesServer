package main.game.players.commands;

import main.GameEngine;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.game.players.content.TeleportHandler;
import main.game.players.packets.Commands;

public class NormalPlayer extends Commands {

	/**
	 * Handles commands that are available to players with a permission level of
	 * 0. These players are known as Normal Players.
	 * 
	 * Normal Players are defined as Normal Players with the ability to: 
	 * > view the rules
	 * > view the commands 
	 * > teleport home 
	 * > teleport to the trading area
	 * > empty their inventory 
	 * > reset their kill/death ratio 
	 * > view the number of online players 
	 * > view the names of online staff members 
	 * > open the vote page 
	 * > open the donation page 
	 * > open the forums page 
	 * > claim vote and donation rewards
	 * 
	 * @param c
	 *            The player executing the command.
	 * @param playerCommand
	 *            The command being executed.
	 * 
	 * @author Branon McClellan (KeepBotting)
	 */
	public static void handleCommands(Player c, String playerCommand) {
		if (c.getVariables().playerRights >= 0) {
			// Clan chat commands
			if (playerCommand.startsWith("[NOT]")) {
				playerCommand = playerCommand.substring(5);
				c.getCC().process(c, playerCommand, 0);
			}
			if (playerCommand.startsWith("[REC]")) {
				playerCommand = playerCommand.substring(5);
				c.getCC().process(c, playerCommand, 1);
			}
			if (playerCommand.startsWith("[COR]")) {
				playerCommand = playerCommand.substring(5);
				c.getCC().process(c, playerCommand, 2);
			}
			if (playerCommand.startsWith("[SER]")) {
				playerCommand = playerCommand.substring(5);
				c.getCC().process(c, playerCommand, 3);
			}
			if (playerCommand.startsWith("[LIE]")) {
				playerCommand = playerCommand.substring(5);
				c.getCC().process(c, playerCommand, 4);
			}
			if (playerCommand.startsWith("[CAP]")) {
				playerCommand = playerCommand.substring(5);
				c.getCC().process(c, playerCommand, 5);
			}
			if (playerCommand.startsWith("[GEN]")) {
				playerCommand = playerCommand.substring(5);
				c.getCC().process(c, playerCommand, 6);
			}
			if (playerCommand.startsWith("[FRI]")) {
				playerCommand = playerCommand.substring(5);
				c.getCC().process(c, playerCommand, 7);
			}
			if (playerCommand.startsWith("[DFR]")) {
				playerCommand = playerCommand.substring(5);
				c.getCC().process(c, playerCommand, 8);
			}
			if (playerCommand.startsWith("[CN]")) {
				playerCommand = playerCommand.substring(5);
				GameEngine.clanChat.changeClanName(c, playerCommand);
			}
			if (playerCommand.startsWith("[NC]")) {
				playerCommand = playerCommand.substring(5);
				GameEngine.clanChat.kickPlayerFromClan(c, playerCommand);
			}

			/**
			 * View the number of online players.
			 */
			if (playerCommand.equalsIgnoreCase("players"))
				c.sendMessage("There are currently " + PlayerHandler.getPlayerCount() + " players online.");

			/**
			 * Empty their inventory.
			 */
			if (playerCommand.equalsIgnoreCase("empty")) {
				c.getPA().removeAllItems();
				c.sendMessage("Your inventory has been emptied.");
			}

			/*
			 * if (playerCommand.equalsIgnoreCase("xplock")) {
			 * c.getVariables().expLock = !c.getVariables().expLock;
			 * c.sendMessage("Experience lock " + (c.getVariables().expLock ?
			 * "activated." : "deactivated.")); }
			 */

			/**
			 * Reset their kill/death ratio.
			 */
			if (playerCommand.equalsIgnoreCase("resetkdr")) {
				c.getVariables().KC = 0;
				c.getVariables().DC = 0;
			}

			/**
			 * View their available commands.
			 */
			if (playerCommand.equalsIgnoreCase("commands")) {
				c.sendMessage("Commands go here m8.");
			}

			if (playerCommand.equalsIgnoreCase("vote")) {
				c.getPA().sendString("http://google.com/", 12000);
			}
			if (playerCommand.equalsIgnoreCase("donate")) {
				c.getPA().sendString("http://google.com/", 12000);
			}
			if (playerCommand.equalsIgnoreCase("forums")) {
				c.getPA().sendString("http://google.com/", 12000);
			}
		}
	}
}
