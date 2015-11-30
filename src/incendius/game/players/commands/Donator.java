package incendius.game.players.commands;

import incendius.game.players.Player;
import incendius.game.players.packets.Commands;

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
	public static void handleCommands(Player c, String cmd) {
		/**
		 * Check permission level. These commands are available for Donator levels of 1, or permission levels of 1 and above.
		 */
		if (c.getInstance().isDonator == 1 || c.getRights() >= Player.RIGHTS_MODERATOR) {
			
			if (cmd.startsWith("yell")) {
				if (c.isJailed()) {
					c.sendMessage("Rule-breakers lack the privelege of using the yell channel.");
				} else {
					c.getYell().shout(c, cmd.substring(5));
				}
			}
		}
	}
}
