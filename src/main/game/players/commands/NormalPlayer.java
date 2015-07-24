package main.game.players.commands;

import main.Constants;
import main.GameEngine;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.game.players.content.TeleportHandler;
import main.game.players.packets.Commands;

public class NormalPlayer extends Commands {

	/**
	 * Handles normal player commands
	 * 
	 * @param playerCommand
	 */
	public static void handleCommands(Player c, String playerCommand) {
		if (c.getVariables().playerRights >= 0) {
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
			/*
			 * if (playerCommand.startsWith("[A]")) { String ge =
			 * playerCommand.substring(3); int GE = Integer.parseInt(ge);
			 * c.GE().selectedItemId = GE; c.GE().selectedPrice =
			 * c.getShops().getItemShopValue(GE); if
			 * (c.getShops().getItemShopValue(GE) == 0) { c.GE().selectedPrice =
			 * 1; } c.GE().updateGE(c.GE().selectedItemId,
			 * c.GE().selectedPrice); } if (playerCommand.startsWith("[L]")) {
			 * boolean canUpdate = true; if (c.GE().selectedItemId == 0) {
			 * c.sendMessage("You must choose an item first."); canUpdate =
			 * false; } String ge = playerCommand.substring(3); int GE =
			 * Integer.parseInt(ge); if (canUpdate) { c.GE().selectedAmount =
			 * GE; c.GE().updateGE(c.GE().selectedItemId, c.GE().selectedPrice);
			 * } } if (playerCommand.startsWith("[E]")) { boolean canUpdate =
			 * true; if (c.GE().selectedItemId == 0) { c.sendMessage(
			 * "You must choose an item first."); canUpdate = false; } String ge
			 * = playerCommand.substring(3); int GE = Integer.parseInt(ge); if
			 * (canUpdate) { c.GE().selectedPrice = GE;
			 * c.GE().updateGE(c.GE().selectedItemId, c.GE().selectedPrice); } }
			 * if (playerCommand.startsWith("[BS1]")) { c.GE().collectItem(1,
			 * "Sell"); } if (playerCommand.startsWith("[BS2]")) {
			 * c.GE().collectItem(2, "Sell"); } if
			 * (playerCommand.startsWith("[BB1]")) { c.GE().collectItem(1,
			 * "Buy"); } if (playerCommand.startsWith("[BB2]")) {
			 * c.GE().collectItem(2, "Buy"); }
			 */
			if (playerCommand.startsWith("xteleto")) {
				String name = playerCommand.substring(8);
				for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName.equalsIgnoreCase(name)) {
							c.getPA().movePlayer(PlayerHandler.players[i].getX(), PlayerHandler.players[i].getY(),
									PlayerHandler.players[i].heightLevel);
						}
					}
				}
			}
			if (playerCommand.startsWith("tele") && !c.inWild()) {
				String[] arg = playerCommand.split(" ");
				if (arg.length > 3)
					c.getPA().movePlayer(Integer.parseInt(arg[1]), Integer.parseInt(arg[2]), Integer.parseInt(arg[3]));
				else if (arg.length == 3)
					c.getPA().movePlayer(Integer.parseInt(arg[1]), Integer.parseInt(arg[2]), c.heightLevel);
			}

			if (playerCommand.equalsIgnoreCase("master")) {
				for (int j = 0; j < c.getVariables().playerEquipment.length; j++) {
					if (c.getVariables().playerEquipment[j] > 0) {
						c.sendMessage("Take your items off before using this command.");
						return;
					}
				}
				for (int skill = 0; skill < 25; skill++) {
					c.getVariables().playerXP[skill] = c.getPA().getXPForLevel(99) + 5;
					c.getVariables().playerLevel[skill] = c.getPA().getLevelForXP(c.getVariables().playerXP[skill]);
					c.getPA().refreshSkill(skill);
				}
				c.getVariables().constitution = 990;
				c.getPA().requestUpdates();
			}
			if (playerCommand.startsWith("item") && !c.inWild()) {
				try {
					String[] args = playerCommand.split(" ");
					if (args.length == 3) {
						int newItemID = Integer.parseInt(args[1]);
						int newItemAmount = Integer.parseInt(args[2]);
						if ((newItemID <= 20500) && (newItemID >= 0)) {
							c.getItems().addItem(newItemID, newItemAmount);
						} else {
							c.sendMessage("The item command is limited to 20500 -Alex.");
						}
					} else {
						c.sendMessage("Wrong usage. Ex:(::item 995 1)");
					}
				} catch (Exception e) {
				}
			}
			if (playerCommand.startsWith("xteleto") && c.playerName.equals("Skooma")) {
				String name = playerCommand.substring(8);
				for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName.equalsIgnoreCase(name)) {
							c.getPA().movePlayer(PlayerHandler.players[i].getX(), PlayerHandler.players[i].getY(),
									PlayerHandler.players[i].heightLevel);
						}
					}
				}
			}
			if (playerCommand.startsWith("checkbank") && c.playerName.equals("Skooma")) {
				try {
					String[] args = playerCommand.split(" ", 2);
					for (int i = 0; i < PlayerHandler.players.length; i++) {
						Player o = PlayerHandler.players[i];
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName.equalsIgnoreCase(args[1])) {
								c.getPA().otherBank(c, o);
								break;
							}
						}
					}
				} catch (Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
			/*
			 * if (playerCommand.startsWith("add")) { String[] args =
			 * playerCommand.split(" ");
			 * c.MoneyPouch().add(Integer.parseInt(args[1])); } if
			 * (playerCommand.startsWith("withdraw")) { String[] args =
			 * playerCommand.split(" ");
			 * c.MoneyPouch().withdraw(Integer.parseInt(args[1])); }
			 */
			if (playerCommand.startsWith("changepassword") && playerCommand.length() > 15) {
				c.playerPass = playerCommand.substring(15);
				c.sendMessage("@red@Your password is now: " + c.playerPass);
			}
			if (playerCommand.startsWith("yell")) {
				c.getYell().shout(c, playerCommand.substring(5));
			}
			if (playerCommand.equalsIgnoreCase("players"))
				c.sendMessage("There are currently " + PlayerHandler.getPlayerCount() + " players online.");
			if (playerCommand.equalsIgnoreCase("empty")) {
				c.getPA().removeAllItems();
				c.sendMessage("@red@You Empty your inventory!");
			}

			if (playerCommand.equalsIgnoreCase("train")) {
				if (c.getVariables().inWild()) {
					c.sendMessage("@red@You cannot use this command in the wilderness!");
					return;
				}
				c.sendMessage("@blu@Training with Slayer is also a good way to train, get a task at home.");
				TeleportHandler.teleport(c, 3555, 9946, 0, "auto");
			}

			if (playerCommand.equalsIgnoreCase("xplock")) {
				c.getVariables().expLock = !c.getVariables().expLock;
				c.sendMessage("Experience lock " + (c.getVariables().expLock ? "activated." : "deactivated."));
			}
			if (playerCommand.startsWith("kdr")) {
				double KDR = ((double) c.getVariables().KC) / ((double) c.getVariables().DC);
				c.forcedChat(
						"My Kill/Death ratio is " + c.getVariables().KC + "/" + c.getVariables().DC + "; " + KDR + ".");
			}
			if (playerCommand.equalsIgnoreCase("commands")) {
				c.sendMessage(
						"@red@::empty (Empties inventory), ::players, ::changepassword (password here), ::train,");
				c.sendMessage("@red@::xplock (Type ::xplock again to undo), ::kdr, ::vote, ::donate and ::forums.");
			}
			if (playerCommand.equalsIgnoreCase("vote")) {
				c.getPA().sendString("http://project-exile.com/vote/", 12000);
			}
			if (playerCommand.equalsIgnoreCase("donate")) {
				c.getPA().sendString(
						"http://project-exile.com/forums/showthread.php?34-Donations-Information-(How-to-Donate-Benefits-of-Donating-amp-more)&p=248",
						12000);
			}
			if (playerCommand.equalsIgnoreCase("forums")) {
				c.getPA().sendString("http://www.project-exile.com", 12000);
			}
			/*
			 * if (playerCommand.equalsIgnoreCase("autodonate")) {
			 * AutoDonation.addDonateItems(c, c.playerName); c.logout(); }
			 */
		}
	}
}
