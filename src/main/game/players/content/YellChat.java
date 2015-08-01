package main.game.players.content;

import main.Connection;
import main.Connection.ConnectionType;
import main.GameEngine;
import main.event.Task;
import main.game.players.Player;
import main.game.players.PlayerHandler;

/**
 * @author Tringan Credits: Octave
 */

public class YellChat {

	/**
	 * The text that players are not allowed to use
	 */
	private static final String[] forbiddenText = { "<col", "<shad", "<img", "<str", "<tran", "tradereq", "duelreq",
			"clanreq", "chalreq", "@str@" };

	/**
	 * Player rank names
	 */
	private static final String[] rankNames = { "Player", "Moderator", "Administrator", "Developer" };

	/**
	 * The color codes used to create the prefix
	 */
	private static final String[] colorCodes = { "255", "562688", "15604003", "16274188", "42575", "3327", "1800639",
			"3327", "3327", "16730502" };

	/**
	 * Sends the yell shout
	 * 
	 * @param p
	 * @param inputText
	 */
	public void shout(final Player p, String inputText) {
		if (!canYell(p, inputText))
			return;

		String message = null;

		message = getRankPrefix(p.getVariables().playerRights) + "<col=255>" + "<img=" + p.getVariables().playerRights
				+ "></img> " + formatPlayerName(p.playerName) + "</col>: " + formatChat(inputText.replaceAll("/", ""));
		
		/**
		 * The player is impersonated - change their yell name!
		 */
		if (p.getVariables().impersonation != "") {
			message = getRankPrefix(p.getVariables().playerRights) + "<col=255>" + "<img="
					+ p.getVariables().playerRights + "></img> " + p.getVariables().impersonation + "</col>: "
					+ formatChat(inputText.replaceAll("/", ""));
		}

		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player p2 = PlayerHandler.players[j];
				p2.sendMessage(message);
			}
		}

		p.getVariables().ableToYell = false;
		GameEngine.getScheduler().schedule(new Task(5) {
			@Override
			public void execute() {
				p.getVariables().ableToYell = true;
				this.stop();
			}
		});

		message = null;
	}

	/**
	 * Checks if a player is able to yell
	 * 
	 * @param p
	 * @param inputText
	 * @return
	 */
	private boolean canYell(final Player p, String inputText) {
		if (p.hasRights(Player.RIGHTS_PLAYER) && p.isDonator == 0) {
			p.sendMessage("@red@You must be a donator to use the yell channel.");
			p.sendMessage("@blu@Use ::donate for more information.");
			return false;
		}
		if (Connection.containsConnection(p.playerName, ConnectionType.forName("MUTE"), false)
				|| Connection.containsConnection(p.connectedFrom, ConnectionType.forName("IPMUTE"), false)
				|| Connection.containsConnection(p.getVariables().identityPunishment,
						ConnectionType.forName("IDENTITY_MUTE"), false)) {
			p.sendMessage("You are muted.");
			return false;
		}
		if (!p.getVariables().ableToYell && p.hasRights(Player.RIGHTS_PLAYER)) {
			p.sendMessage("Please wait a few seconds between yells.");
			return false;
		}
		for (int i = 0; i < forbiddenText.length; i++) {
			if (inputText.substring(1).contains(forbiddenText[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets the rank name
	 * 
	 * @param rights
	 * @return
	 */
	private String getRankPrefix(int rights) {
		String rank = "Player";
		for (int i = 0; i < rankNames.length; i++)
			rank = rankNames[rights];
		return "[" + getRankColor(rank, rights) + "]";
	}

	/**
	 * Gets the rank color
	 * 
	 * @param toColor
	 * @param rights
	 * @return
	 */
	private String getRankColor(String toColor, int rights) {
		String color = "";
		for (int i = 0; i < colorCodes.length; i++)
			color = colorCodes[rights];
		return "<col=" + color + ">" + toColor + "</col>";
	}

	/**
	 * Capitalizes the first letter between spaces Used to properly format
	 * character names
	 * 
	 * @param in
	 * @return
	 */
	private static String formatPlayerName(String in) {
		final StringBuilder output = new StringBuilder(in.length());
		String[] words = in.split("\\s");
		for (byte b = 0, l = (byte) words.length; b < l; ++b) {
			if (b > 0)
				output.append(" ");
			output.append(Character.toUpperCase(words[b].charAt(0))).append(words[b].substring(1));
		}
		return output.toString();
	}

	/**
	 * Capitalizes the first letter and does not disturb the rest of the
	 * sentence capitalizations
	 * 
	 * @param in
	 * @return
	 */
	private static String formatChat(String in) {
		return in.substring(0, 1).toUpperCase() + in.substring(1);
	}

}
