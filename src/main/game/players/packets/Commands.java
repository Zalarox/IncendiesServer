package main.game.players.packets;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import main.Constants;
import main.GameEngine;
import main.game.players.PacketType;
import main.game.players.Player;
import main.game.players.commands.Administrator;
import main.game.players.commands.Developer;
import main.game.players.commands.Donator;
//import main.game.players.commands.ExtremeDonator;
//import main.game.players.commands.GFXDesigner;
import main.game.players.commands.NormalPlayer;
import main.game.players.commands.Moderator;
//import main.game.players.commands.PlayerModerator;
//import main.game.players.commands.SuperDonator;
import main.util.Misc;

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
	@SuppressWarnings("unused")
	public void processPacket(Player p, int packetType, int packetSize) {
		String playerCommand = p.getInStream().readString();
		if (p.getVariables().playerRights >= 1 && p.getVariables().playerRights != 4
				&& !playerCommand.startsWith("/")) {
			try {
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				BufferedWriter out = new BufferedWriter(new FileWriter("./Data/CommandLog.txt", true));
				try {
					out.newLine();
					out.write("[] " + p.getVariables().properName + " used command (" + playerCommand + ")");
				} finally {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (Constants.SERVER_DEBUG)
			Misc.println(p.playerName + " playerCommand: " + playerCommand);
		if (playerCommand.startsWith("/") && playerCommand.length() > 1) {
			if (p.getVariables().clanId >= 0) {
				System.out.println(playerCommand);
				playerCommand = playerCommand.substring(1);
				GameEngine.clanChat.playerMessageToClan(p.playerId, playerCommand, p.getVariables().clanId);
			} else {
				if (p.getVariables().clanId != -1)
					p.getVariables().clanId = -1;
				p.sendMessage("You are not in a clan.");
			}
			return;
		}
		loadCommands(p, playerCommand);
	}
}