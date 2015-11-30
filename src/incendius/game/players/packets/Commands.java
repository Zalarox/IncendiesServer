package incendius.game.players.packets;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import incendius.Constants;
import incendius.Data;
import incendius.Server;
import incendius.game.players.PacketType;
import incendius.game.players.Player;
import incendius.game.players.commands.Administrator;
import incendius.game.players.commands.Developer;
import incendius.game.players.commands.Donator;
import incendius.game.players.commands.Moderator;
import incendius.game.players.commands.NormalPlayer;
import incendius.util.Misc;

/**
 * Commands
 **/
public class Commands implements PacketType {

	/**
	 * Loads every command. Make sure this always loads in DECENDING order of permission level.
	 */
	private void loadCommands(Player p, String playerCommand) {
		Developer.handleCommands(p, playerCommand);
		Administrator.handleCommands(p, playerCommand);
		Moderator.handleCommands(p, playerCommand);
		Donator.handleCommands(p, playerCommand);
		NormalPlayer.handleCommands(p, playerCommand);
		//SuperDonator.handleCommands(p, playerCommand);
		//ExtremeDonator.handleCommands(p, playerCommand);
		//PlayerModerator.handleCommands(p, playerCommand);
		//GFXDesigner.handleCommands(p, playerCommand);
		
		

	}

	@Override
	public void processPacket(Player p, int packetType, int packetSize) {
		String playerCommand = p.getInStream().readString();
		if (p.getInstance().playerRights >= 1 && p.getInstance().playerRights != 4
				&& !playerCommand.startsWith("/")) {
			try {
				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy 'at' HH:mm:ss");
				Date date = new Date();
				BufferedWriter out = new BufferedWriter(new FileWriter(Data.COMMAND_LOG, true));
				try {
					out.newLine();
					out.write("On " + dateFormat.format(date) + ", " + p.getDisplayName() + " used the command ["
							+ playerCommand + "]");
				} finally {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (Constants.SERVER_DEBUG_VERBOSE)
			Misc.println(p.playerName + " playerCommand: " + playerCommand);
		if (playerCommand.startsWith("/") && playerCommand.length() > 1) {
			if (p.getInstance().clanId >= 0) {
				System.out.println(playerCommand);
				playerCommand = playerCommand.substring(1);
				Server.clanChat.playerMessageToClan(p.playerId, playerCommand, p.getInstance().clanId);
			} else {
				if (p.getInstance().clanId != -1)
					p.getInstance().clanId = -1;
				p.sendMessage("You are not in a clan.");
			}
			return;
		}
		loadCommands(p, playerCommand);
	}
}