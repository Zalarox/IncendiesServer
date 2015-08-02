package main.game.players.commands;

import main.game.players.Player;
import main.game.players.packets.Commands;

public class Donator extends Commands {

	/**
	 * Handles commands that are available to players with a Donator level of 1.
	 * These players are known as Donators.
	 * 
	 * Donators are defined as Normal Players with the ability to:
	 * > travel to the donator zone
	 * > yell
	 * 
	 * @param c The player executing the command.
	 * @param playerCommand The command being executed.
	 * 
	 * @author Branon McClellan (KeepBotting)
	 */
	public static void handleCommands(Player c, String playerCommand) {
		/**
		 * Check permission level. These commands are available for Donator levels of 1, or permission levels of 2 and above.
		 */
		if (c.getVariables().isDonator == 1 || c.getVariables().playerRights >= 1) {
			
			if (playerCommand.startsWith("yell")) {
				c.getYell().shout(c, playerCommand.substring(5));
			}

		}
	}

}
