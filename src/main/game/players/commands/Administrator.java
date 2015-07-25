package main.game.players.commands;

import main.Connection;
import main.Connection.ConnectionType;
import main.Constants;
import main.GameEngine;
import main.game.npcs.NPCHandler;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.game.players.PlayerSave;
import main.game.players.actions.combat.CombatPrayer;
import main.game.players.content.minigames.impl.barrows.Barrows;
import main.game.players.content.skills.dungeoneering.Dungeon;
import main.game.players.packets.Commands;
import main.handlers.ItemHandler;
import main.util.Misc;

public class Administrator extends Commands {

	/**
	 * Handles commands that are available to players with a permission level of 2.
	 * These players are known as Administrators.
	 * 
	 * Administrators are defined as Moderators with the ability to:
	 * > give or take moderator
	 * > issue or quash ipbans
	 * > view player information
	 * 
	 * Additionally, Administrators lack the ability to:
	 * > trade items
	 * > drop items (manually or on death)
	 * > participate in duels
	 * 
	 * @param c The player executing the command.
	 * @param playerCommand The command being executed.
	 * 
	 * @author KeepBotting
	 */
	public static void handleCommands(Player c, String playerCommand) {
		/**
		 * Check permission level. These commands are available for permission levels of 2 and above.
		 */
		if (c.getVariables().playerRights >= 2) {
				
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

			/**
			 * Jesus Christ, PI commands are structured fucking horribly.
			 * 
			 * Previously, the command would ONLY process correctly if
			 * two arguments were given -- ID and amount. I modified the
			 * command to use an understood-1 for the amount.
			 * 
			 * This means we can do ::item 4151 to spawn one whip, as 
			 * opposed to ::item 4151 1, always having to append the
			 * number 1 onto the end.
			 * 
			 * - KeepBotting
			 */
			if (playerCommand.startsWith("item") && !c.inWild()) {
				try {
					String[] args = playerCommand.split(" ");
					if(args.length == 2 || args.length == 3) {
						int newItemID = Integer.parseInt(args[1]);
						int newItemAmount = 1;
						if(args.length == 3) {
							newItemAmount = Integer.parseInt(args[2]);
						}
						if ((newItemID <= 20500) && (newItemID >= 0)) {
							c.getItems().addItem(newItemID, newItemAmount);
						} else {
							c.sendMessage("The item command is apparently limited to 20500.");
						}
					} else {
						c.sendMessage("Syntax is ::item [id] [count].");
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
			
			if (playerCommand.startsWith("dung")) {
				String[] args = playerCommand.split(" ");
				int i2 = Integer.parseInt(args[1]);
				for (int i = 0; i <= 21; i++) {
					main.game.players.content.skills.dungeoneering.Items.addItem(c, i, i2, 1);
				}
			}
			if (playerCommand.startsWith("set")) {
				String[] args = playerCommand.split(" ");
				if (c.party != null)
					c.party.level = Integer.parseInt(args[1]);
			}
			if (playerCommand.equals("start")) {
				Dungeon.start(c);
			}
			if (playerCommand.equals("k")) {
				for (int i = 0; i < 30000; i++) {
					c.getPA().sendFrame126("" + i, i);
					c.sendMessage("" + i);
				}
			}
			if (playerCommand.equals("allresetbarrows")) {
				for (final Player player : PlayerHandler.players) {
					if (player != null) {
						final Player c2 = player;
						Barrows.resetBarrows(c2);
						PlayerSave.saveGame(c2);
						c2.sendMessage("<col=255>Your barrows kill count have been reset.</col>");
					}
				}
			}
			if (playerCommand.equals("lootshare")) {
				GameEngine.clanChat.handleShare(c, 1050, 1, c.absX, c.absY, c.heightLevel, true);
			}
			if (playerCommand.equals("quake")) {
				c.getPA().sendFrame35(Misc.random(5), Misc.random(5), Misc.random(15), Misc.random(15));// sends
																										// earthquake
			}
			if (playerCommand.startsWith("heal")) {
				if (playerCommand.indexOf(" ") > -1 && c.getVariables().playerRights > 1) {
					String name = playerCommand.substring(5);
					if (c.validClient(name)) {
						Player p = c.getClient(name);
						for (int i = 0; i < 20; i++) {
							p.getVariables().playerLevel[i] = p.getLevelForXP(p.getVariables().playerXP[i]);
							p.getPA().refreshSkill(i);
							p.getVariables().constitution = p.getVariables().maxConstitution;
						}
						p.sendMessage("You have been healed by " + c.playerName + ".");
					} else {
						c.sendMessage("Player must be offline.");
					}
				} else {
					for (int i = 0; i < 22; i++) {
						c.getVariables().playerLevel[i] = c.getLevelForXP(c.getVariables().playerXP[i]);
						c.getPA().refreshSkill(i);
						c.getVariables().constitution = c.getVariables().maxConstitution;
					}
					c.getVariables().freezeTimer = -1;
					c.getVariables().frozenBy = -1;
					c.sendMessage("You have been healed.");
				}
			}
			if (playerCommand.equalsIgnoreCase("bank")) {
				c.getPA().openUpBank();
			}
			if (playerCommand.startsWith("giveadmin")) {
				try {
					String playerToAdmin = playerCommand.substring(10);
					for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName.equalsIgnoreCase(playerToAdmin)) {
								Player c2 = PlayerHandler.players[i];
								c2.sendMessage("You have been given administrator by " + c.playerName);
								c2.getVariables().playerRights = 2;
								c2.logout();
								break;
							}
						}
					}
				} catch (Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
			if (playerCommand.startsWith("givemod")) {
				try {
					String playerToMod = playerCommand.substring(8);
					for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName.equalsIgnoreCase(playerToMod)) {
								Player c2 = PlayerHandler.players[i];
								c2.sendMessage("You have been given mod status by " + c.playerName);
								c2.getVariables().playerRights = 1;
								c2.logout();
								break;
							}
						}
					}
				} catch (Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
			if (playerCommand.startsWith("givedonor")) {
				String name = playerCommand.substring(10);
				for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName.equalsIgnoreCase(name)) {
							PlayerHandler.players[i].getVariables().playerRights = 4;
							PlayerHandler.players[i].getPA().requestUpdates();
						}
					}
				}
			}
			if (playerCommand.startsWith("demote")) {
				try {
					String playerToDemote = playerCommand.substring(7);
					for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName.equalsIgnoreCase(playerToDemote)) {
								Player c2 = PlayerHandler.players[i];
								c2.sendMessage("You have been demoted by " + c.playerName);
								c2.getVariables().playerRights = 0;
								c2.logout();
								break;
							}
						}
					}
				} catch (Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
			if (playerCommand.startsWith("givepkp")) {
				String name = playerCommand.substring(8);
				for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName.equalsIgnoreCase(name))
							PlayerHandler.players[i].getVariables().pkp += 100;
					}
				}
			}
			if (playerCommand.equals("xteleall")) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						c2.teleportToX = c.absX;
						c2.teleportToY = c.absY;
						c2.heightLevel = c.heightLevel;
						c2.sendMessage("Mass teleport to: " + c.playerName + "");
					}
				}
			}
			if (playerCommand.startsWith("getip")) {
				String[] args = playerCommand.split(" ");

				if (args.length > 0) {
					String name = args[0];

					for (Player p : PlayerHandler.players) {
						String ip = p.connectedFrom;

						if (p != null) {
							if (p.playerName.equalsIgnoreCase(name)) {
								c.sendMessage(name + " is currently connecting from: " + ip + ".");
							}
						}
					}
				}
			}
			if (playerCommand.startsWith("update")) {
				String[] args = playerCommand.split(" ");
				int a = Integer.parseInt(args[1]);
				PlayerHandler.updateSeconds = a;
				PlayerHandler.updateAnnounced = false;
				PlayerHandler.updateRunning = true;
				PlayerHandler.updateStartTime = System.currentTimeMillis();
				c.declinePlayerTrades();
			}
			if (playerCommand.startsWith("ipban")) {
				try {
					String playerToBan = playerCommand.substring(6);
					for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Connection.addConnection(PlayerHandler.players[i], ConnectionType.IPBAN);
								c.sendMessage("You have IP banned the user: " + PlayerHandler.players[i].playerName
										+ " with the host: " + PlayerHandler.players[i].connectedFrom);
								PlayerHandler.players[i].disconnected = true;
							}
						}
					}
				} catch (Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
			if (playerCommand.startsWith("unipban")) {
				try {
					String playerToBan = playerCommand.substring(8);
					for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Connection.removeConnection(playerToBan, ConnectionType.IPBAN);
								c.sendMessage("You have Un Ip-Banned the user: " + PlayerHandler.players[i].playerName);
								break;
							}
						}
					}
				} catch (Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}

			if (playerCommand.startsWith("item")) {
				try {
					String[] args = playerCommand.split(" ");
					if (args.length == 3) {
						int newItemID = Integer.parseInt(args[1]);
						int newItemAmount = Integer.parseInt(args[2]);
						if ((newItemID <= 25500) && (newItemID >= 0)) {
							c.getItems().addItem(newItemID, newItemAmount);
						} else {
							c.sendMessage("That item ID does not exist.");
						}
					} else {
						c.sendMessage("Wrong usage. Ex:(::item 995 1)");
					}
				} catch (Exception e) {
				}
			}
			if (playerCommand.equals("reloaditems")) {
				for (int i = 0; i < Constants.ITEM_LIMIT; i++)
					ItemHandler.ItemList[i] = null;
				ItemHandler.loadItemList("item.cfg");
				ItemHandler.loadItemPrices("prices.txt");
				c.sendMessage("Items reloaded.");
			}
			if (playerCommand.equals("reloadnpcs")) {
				for (int i = 0; i < NPCHandler.maxNPCs; i++) {
					NPCHandler.npcs[i] = null;
				}
				GameEngine.npcHandler.loadNPCList("./Data/CFG/npc.cfg");
				GameEngine.npcHandler.loadAutoSpawn("./Data/CFG/spawn-config.cfg");
				c.sendMessage("NPCs reloaded.");
			}
			if (playerCommand.startsWith("reloaddrops")) {
				GameEngine.npcDrops = null;
				GameEngine.npcDrops = new main.game.npcs.data.NPCDrops();
			}
			if (playerCommand.startsWith("reloadshops")) {
				GameEngine.shopHandler = new main.world.ShopHandler();
			}
			if (playerCommand.startsWith("copy")) {
				String name = playerCommand.substring(5);
				if (c.validClient(name)) {
					Player p = c.getClient(name);
					for (int i = 0; i < c.getVariables().playerEquipment.length; i++)
						c.getVariables().playerEquipment[i] = p.getVariables().playerEquipment[i];
					for (int i = 0; i < c.getVariables().playerAppearance.length; i++)
						c.getVariables().playerAppearance[i] = p.getVariables().playerAppearance[i];
					c.sendMessage("You have copied " + p.playerName + ".");
					c.updateRequired = true;
					c.appearanceUpdateRequired = true;
				}
			}
			if (playerCommand.startsWith("xcopy")) {
				String name = playerCommand.substring(6);
				if (c.validClient(name)) {
					Player p = c.getClient(name);
					for (int i = 0; i < c.getVariables().playerEquipment.length; i++)
						p.getVariables().playerEquipment[i] = c.getVariables().playerEquipment[i];
					for (int i = 0; i < c.getVariables().playerAppearance.length; i++)
						p.getVariables().playerAppearance[i] = c.getVariables().playerAppearance[i];
					c.sendMessage("You have xcopied " + p.playerName + ".");
					p.sendMessage("You have been xcopied by " + c.playerName + ".");
					p.updateRequired = true;
					p.appearanceUpdateRequired = true;
				}
			}
			if (playerCommand.equalsIgnoreCase("pure")) {
				if (c.getVariables().inWild())
					return;
				for (int j = 0; j < c.getVariables().playerEquipment.length; j++) {
					if (c.getVariables().playerEquipment[j] > 0) {
						c.sendMessage("Take your items off before using this command.");
						return;
					}
				}
				c.getVariables().playerXP[0] = c.getPA().getXPForLevel(60) + 5;
				c.getVariables().playerXP[1] = c.getPA().getXPForLevel(1) + 5;
				c.getVariables().playerXP[2] = c.getPA().getXPForLevel(99) + 5;
				c.getVariables().playerXP[3] = c.getPA().getXPForLevel(99) + 5;
				c.getVariables().playerXP[4] = c.getPA().getXPForLevel(99) + 5;
				c.getVariables().playerXP[5] = c.getPA().getXPForLevel(52) + 5;
				c.getVariables().playerXP[6] = c.getPA().getXPForLevel(99) + 5;
				for (int skill = 0; skill < 7; skill++) {
					c.getVariables().playerLevel[skill] = c.getPA().getLevelForXP(c.getVariables().playerXP[skill]);
					c.getPA().refreshSkill(skill);
				}
				c.getVariables().constitution = 990;
				c.getPA().requestUpdates();
				CombatPrayer.resetPrayers(c);
			}
			if (playerCommand.equalsIgnoreCase("zerker")) {
				if (c.getVariables().inWild())
					return;
				for (int j = 0; j < c.getVariables().playerEquipment.length; j++) {
					if (c.getVariables().playerEquipment[j] > 0) {
						c.sendMessage("Take your items off before using this command.");
						return;
					}
				}
				c.getVariables().playerXP[0] = c.getPA().getXPForLevel(60) + 5;
				c.getVariables().playerXP[1] = c.getPA().getXPForLevel(45) + 5;
				c.getVariables().playerXP[2] = c.getPA().getXPForLevel(99) + 5;
				c.getVariables().playerXP[3] = c.getPA().getXPForLevel(99) + 5;
				c.getVariables().playerXP[4] = c.getPA().getXPForLevel(99) + 5;
				c.getVariables().playerXP[5] = c.getPA().getXPForLevel(95) + 5;
				c.getVariables().playerXP[6] = c.getPA().getXPForLevel(99) + 5;
				for (int skill = 0; skill < 7; skill++) {
					c.getVariables().playerLevel[skill] = c.getPA().getLevelForXP(c.getVariables().playerXP[skill]);
					c.getPA().refreshSkill(skill);
				}
				c.getVariables().constitution = 990;
				c.getPA().requestUpdates();
				CombatPrayer.resetPrayers(c);
			}

			if (playerCommand.equals("voteall")) {
				for (int j = 0; j < PlayerHandler.players.length; j++)
					if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						c2.getPA().sendString("http://google.com/", 12000);
					}
			}
			if (playerCommand.equals("donateall")) {
				for (int j = 0; j < PlayerHandler.players.length; j++)
					if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						c2.getPA().sendString(
								"http://google.com/",
								12000);
					}
			}
			if (playerCommand.equals("forumsall")) {
				for (int j = 0; j < PlayerHandler.players.length; j++)
					if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						c2.getPA().sendString("http://www.project-exile.com", 12000);
					}
			}

			if (playerCommand.equalsIgnoreCase("raw envyy")) {
				if (c.getVariables().inWild())
					return;
				int itemsToAdd[] = { 15442, 20072, 18335, 11694, 6570, 4736, 4751, 4749 };
				for (int i = 0; i < itemsToAdd.length; i++) {
					c.getItems().addItem(itemsToAdd[i], 1);
				}
				int[] equip = { 12681, 2412, 6585, 15486, 4712, 13738, -1, 4714, -1, 7462, 11732, -1, 15220, -1 };
				for (int i = 0; i < equip.length; i++) {
					c.getVariables().playerEquipment[i] = equip[i];
					c.getVariables().playerEquipmentN[i] = 1;
					c.getItems().setEquipment(equip[i], 1, i);
				}
				c.getItems().addItem(15272, 7);
				c.getItems().addItem(565, 4000);
				c.getItems().addItem(3024, 3);
				c.getItems().addItem(6685, 2);
				c.getItems().addItem(2436, 1);
				c.getItems().addItem(3040, 1);
				c.getItems().addItem(2440, 1);
				c.getItems().addItem(555, 12000);
				c.getItems().addItem(560, 8008);
				c.getVariables().playerMagicBook = 1;
				c.getItems().resetItems(3214);
				c.getItems().resetBonus();
				c.getItems().getBonus();
				c.getItems().writeBonus();
				c.updateRequired = true;
				c.handler.updatePlayer(c, c.outStream);
				c.handler.updateNPC(c, c.outStream);
				c.flushOutStream();
				// c.appearanceUpdateRequired = false;
			}
			if (playerCommand.equalsIgnoreCase("brid")) {
				int[] equip = { 10828, 2412, 6585, 6914, 4712, 6889, -1, 4714, -1, 7462, 6920, -1, 6737 };
				int[][] gearandpots = { { 4749, 1 }, { 20072, 1 }, { 4151, 1 }, { 4736, 1 }, { 4751, 1 }, { 11732, 1 },
						{ 6570, 1 }, { 6685, 1 }, { 3024, 2 }, { 3040, 1 }, { 2440, 1 }, { 2436, 1 }, { 5698, 1 } };
				int[][] food = { { 15272, 11 } };
				int[][] barrageandpots = { { 560, 800 }, { 555, 1200 }, { 565, 400 } };
				if (c.getItems().freeSlots() < 28) {
					c.sendMessage("Please empty your inventory before using this function.");
					return;
				}
				for (int i = 0; i < equip.length; i++) {
					if (c.getVariables().playerEquipment[i] > 0) {
						c.sendMessage("Take your items off before using this function.");
						return;
					}
					c.getVariables().playerEquipment[i] = equip[i];
					c.getVariables().playerEquipmentN[i] = 1;
					c.getItems().setEquipment(equip[i], 1, i);
				}
				c.getItems().removeAllItems();
				c.getVariables().playerMagicBook = 2;
				for (int i = 0; i < gearandpots.length; i++)
					c.getItems().addItem(gearandpots[i][0], gearandpots[i][1]);
				for (int i = 0; i < food.length; i++)
					c.getItems().addItem(food[i][0], food[i][1]);
				for (int i = 0; i < barrageandpots.length; i++)
					c.getItems().addItem(barrageandpots[i][0], barrageandpots[i][1]);
				c.getItems().resetItems(3214);
				c.getItems().resetBonus();
				c.getItems().getBonus();
				c.getItems().writeBonus();
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
			if (playerCommand.startsWith("setlevel")) {
				try {
					String[] args = playerCommand.split(" ");
					int skill = Integer.parseInt(args[1]);
					int level = Integer.parseInt(args[2]);
					String otherplayer = args[3];
					Player target = null;
					for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName.equalsIgnoreCase(otherplayer)) {
								target = PlayerHandler.players[i];
								break;
							}
						}
					}
					if (target == null) {
						c.sendMessage("Player doesn't exist.");
						return;
					}
					c.sendMessage("You have just set one of " + Misc.ucFirst(target.playerName) + "'s skills.");
					target.sendMessage("" + Misc.ucFirst(c.playerName) + " has just set one of your skills.");
					target.getVariables().playerXP[skill] = target.getPA().getXPForLevel(level) + 5;
					target.getVariables().playerLevel[skill] = target.getPA()
							.getLevelForXP(target.getVariables().playerXP[skill]);
					target.getPA().refreshSkill(skill);
				} catch (Exception e) {
					c.sendMessage("Use as ::setlevel SKILLID LEVEL PLAYERNAME.");
				}
			}
			if (playerCommand.startsWith("maxhp")) {
				c.getVariables().constitution += 99999;
			}
			if (playerCommand.equalsIgnoreCase("vengrunes")) {
				c.getItems().addItem(560, 200);
				c.getItems().addItem(9075, 400);
				c.getItems().addItem(557, 1000);
			}
			if (playerCommand.equalsIgnoreCase("brunes")) {
				c.getItems().addItem(560, 800);
				c.getItems().addItem(565, 400);
				c.getItems().addItem(555, 1200);
			}
			if (playerCommand.equals("spec")) {
				c.getVariables().specAmount = 10.0;
			}
		}
	}
}
