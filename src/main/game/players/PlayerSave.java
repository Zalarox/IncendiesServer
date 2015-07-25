package main.game.players;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import main.util.Misc;

public class PlayerSave {

	/**
	 * Loading
	 **/
	public static int loadGame(Player p, String playerName, String playerPass) {
		String line = "";
		String token = "";
		String token2 = "";
		String[] token3 = new String[3];
		boolean EndOfFile = false;
		int ReadMode = 0;
		BufferedReader characterfile = null;
		boolean File1 = false;

		try {
			characterfile = new BufferedReader(new FileReader(("./Data/characters/") + playerName + ".txt"));
			File1 = true;
		} catch (FileNotFoundException fileex1) {
		}

		if (!File1) {
			Misc.println(playerName + ": character file not found.");
			p.getVariables().newPlayer = false;
			return 0;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			Misc.println(playerName + ": error loading file.");
			return 3;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token3 = token2.split("\t");
				switch (ReadMode) {
				case 1:
					if (token.equals("character-password")) {
						if (playerPass
								.equalsIgnoreCase(
										token2)
								|| Misc.basicEncrypt(playerPass).equals(token2)
								|| Misc.md5Hash(playerPass + playerName + (playerName.length() * playerPass.length()))
										.equals(token2)) {
							playerPass = token2;
						} else {
							return 3;
						}
					}
					break;
				case 2:
					if (token.equals("character-height")) {
						p.heightLevel = Integer.parseInt(token2);
					} else if (token.equals("character-posx")) {
						p.teleportToX = (Integer.parseInt(token2) <= 0 ? 3210 : Integer.parseInt(token2));
						p.getVariables().lastAbsX = p.teleportToX;
					} else if (token.equals("character-posy")) {
						p.teleportToY = (Integer.parseInt(token2) <= 0 ? 3424 : Integer.parseInt(token2));
						p.getVariables().lastAbsY = p.teleportToY;
					} else if (token.equals("character-rights")) {
						p.getVariables().playerRights = Integer.parseInt(token2);
					} else if (token.equals("hasSavedClan")) {
						p.getVariables().hasSavedClan = Boolean.parseBoolean(token2);
					} else if (token.equals("savedClan")) {
						p.getVariables().savedClan = token2.equals("null") ? null : token2;
					} else if (token.equals("lootSharePotential")) {
						p.getVariables().lootSharePotential = Long.parseLong(token2);
					} else if (token.equals("lastLootDate")) {
						p.getVariables().lastLootDate = Long.parseLong(token2);
					} else if (token.equals("RockCrabKills")) {
						p.getVariables().rockCrabKills = Integer.parseInt(token2);
					} else if (token.equals("Donator")) {
						p.getVariables().isDonator = Integer.parseInt(token2);
					} else if (token.equals("vengeanceCasted")) {
						p.getVariables().castVengeance = Integer.parseInt(token2);
					} else if (token.equals("character-title")) {
						p.getVariables().playerTitle = Integer.parseInt(token2);
					} else if (token.equals("expLock")) {
						p.getVariables().expLock = Boolean.parseBoolean(token2);
					} else if (token.equals("totalxp")) {
						p.getVariables().totalxp = Integer.parseInt(token2);
					} else if (token.equals("tutorial-progress")) {
						p.getVariables().tutorial = Byte.parseByte(token2);
					} else if (token.equals("crystal-bow-shots")) {
						p.getVariables().crystalBowArrowCount = Integer.parseInt(token2);
					} else if (token.equals("barrows-npc-dead")) {
						for (int j = 0; j < token3.length; j++) {
							p.getVariables().brotherKilled[j] = Boolean.parseBoolean(token3[j]);
						}
					} else if (token.equals("skull-timer")) {
						p.getVariables().skullTimer = Integer.parseInt(token2);
					} else if (token.equals("magic-book")) {
						p.getVariables().playerMagicBook = Integer.parseInt(token2);
					} else if (token.equals("prayer-book")) {
						p.getVariables().playerPrayerBook = Boolean.parseBoolean(token2);
					} else if (token.equals("isDonePicking")) {
						p.getVariables().isDonePicking = Boolean.parseBoolean(token2);
					} else if (token.equals("Picked")) {
						p.getVariables().Picked = Integer.parseInt(token2);
					} else if (token.equals("KC")) {
						p.getVariables().KC = Integer.parseInt(token2);
					} else if (token.equals("DC")) {
						p.getVariables().DC = Integer.parseInt(token2);
					} else if (token.equals("special-amount")) {
						p.getVariables().specAmount = Double.parseDouble(token2);
					} else if (token.equals("selected-coffin")) {
						p.getVariables().randomCoffin = Integer.parseInt(token2);
					} else if (token.equals("lastBrother")) {
						p.getVariables().lastBrother = Integer.parseInt(token2);
					} else if (token.equals("teleblock-length")) {
						p.getVariables().teleBlockDelay = System.currentTimeMillis();
						p.getVariables().teleBlockLength = Integer.parseInt(token2);
					} else if (token.equals("votingPoints")) {
						p.getVariables().votingPoints = Integer.parseInt(token2);
					} else if (token.equals("timesVoted")) {
						p.getVariables().timesVoted = Integer.parseInt(token2);
					} else if (token.equals("Amount-Donated")) {
						p.getVariables().amountDonated = Integer.parseInt(token2);
					} else if (token.equals("pc-points")) {
						p.getVariables().pcPoints = Integer.parseInt(token2);
					} else if (token.equals("SlayerPoints")) {
						p.getVariables().SlayerPoints = Integer.parseInt(token2);
					} else if (token.equals("DonatorPoints")) {
						p.getVariables().DonatorPoints = Integer.parseInt(token2);
					} else if (token.equals("splitChat")) {
						p.splitChat = Boolean.parseBoolean(token2);
					} else if (token.equals("SLAY-TASK")) {
						try {
							if (!token3[0].equals("null"))
								p.getSlayer().setTask(token3[0], Integer.parseInt(token3[1]));
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (token.equals("totalTasks")) {
						p.getVariables().totalTasks = Integer.parseInt(token2);
					} else if (token.equals("taskDifficulty")) {
						p.getVariables().taskDifficulty = Integer.parseInt(token2);
					} else if (token.equals("magePoints")) {
						p.getVariables().magePoints = Integer.parseInt(token2);
					} else if (token.equals("autoRet")) {
						p.getVariables().autoRet = Integer.parseInt(token2);
					} else if (token.equals("godwars")) {
						for (int j = 0; j < token3.length; j++) {
							p.getVariables().gwKills[j] = Integer.parseInt(token3[j]);
						}
					} else if (token.equals("quickCurses")) {
						for (int j = 0; j < token3.length; j++) {
							p.getVariables().quickCurses[j] = Boolean.parseBoolean(token3[j]);
						}
					} else if (token.equals("quickPrayers")) {
						for (int j = 0; j < token3.length; j++) {
							p.getVariables().quickPrayers[j] = Boolean.parseBoolean(token3[j]);
						}
					} else if (token.equals("flagged")) {
						p.getVariables().accountFlagged = Boolean.parseBoolean(token2);
					} else if (token.equals("hasOverloadBoost")) {
						p.getVariables().hasOverloadBoost = Boolean.parseBoolean(token2);
					} else if (token.equals("wave")) {
						p.getVariables().waveId = Integer.parseInt(token2);
					} else if (token.equals("void")) {
						for (int j = 0; j < token3.length; j++) {
							p.getVariables().voidStatus[j] = Integer.parseInt(token3[j]);
						}
					} else if (token.equals("gwkc")) {
						p.getVariables().killCount = Integer.parseInt(token2);
					} else if (token.equals("fightMode")) {
						p.getVariables().fightMode = Integer.parseInt(token2);
					} else if (token.equals("PKP")) {
						p.getVariables().pkp = Integer.parseInt(token2);
					} else if (token.equals("yellTag")) {
						p.getVariables().donorTag = token2;
					} else if (token.equals("constitution")) {
						p.getVariables().constitution = Integer.parseInt(token2);
					} else if (token.equals("familiarSpecialEnergy")) {
						p.getSummoning().familiarSpecialEnergy = Integer.parseInt(token2);
					} else if (token.equals("familiar")) {
						try {
							p.getSummoning().summonFamiliar(Integer.parseInt(token2), true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (token.equals("run-energy")) {
						p.getVariables().runEnergy = Integer.parseInt(token2);
					}
					break;
				case 3:
					if (token.equals("character-equip")) {
						p.getVariables().playerEquipment[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						p.getVariables().playerEquipmentN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
					}
					break;
				case 4:
					if (token.equals("character-look")) {
						p.getVariables().playerAppearance[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
					}
					break;
				case 5:
					if (token.equals("character-skill")) {
						p.getVariables().playerLevel[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						p.getVariables().playerXP[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
					}
					break;
				case 6:
					if (token.equals("character-item")) {
						p.getVariables().playerItems[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						p.getVariables().playerItemsN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
					}
					break;
				case 7:
					if (token.equals("character-bank")) {
						p.getVariables().bankItems[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						p.getVariables().bankItemsN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
					}
					break;
				case 8:
					if (token.equals("character-friend")) {
						p.getVariables().friends[Integer.parseInt(token3[0])] = Long.parseLong(token3[1]);
					}
					break;
				case 9:
					/*
					 * if (token.equals("character-ignore")) {
					 * ignores[Integer.parseInt(token3[0])] =
					 * Long.parseLong(token3[1]); }
					 */
					break;
				case 10:
					if (token.equals("character-bob")) {
						p.getSummoning().burdenedItems[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
					}
					break;
				case 11:
					if (token.equals("slot")) {
						p.GE().Slots[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						p.GE().SlotType[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
					}
					break;
				case 12:
					if (token.equals("chaotic")) {
						p.chaoticDegrade[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
					}
					break;
				case 13:
					if (token.equals("bound")) {
						p.boundItems[Integer.parseInt(token3[0])][0] = Integer.parseInt(token3[1]);
						p.boundItems[Integer.parseInt(token3[0])][1] = Integer.parseInt(token3[2]);
					}
					break;
				}
			} else {
				if (line.equals("[ACCOUNT]")) {
					ReadMode = 1;
				} else if (line.equals("[CHARACTER]")) {
					ReadMode = 2;
				} else if (line.equals("[EQUIPMENT]")) {
					ReadMode = 3;
				} else if (line.equals("[LOOK]")) {
					ReadMode = 4;
				} else if (line.equals("[SKILLS]")) {
					ReadMode = 5;
				} else if (line.equals("[ITEMS]")) {
					ReadMode = 6;
				} else if (line.equals("[BANK]")) {
					ReadMode = 7;
				} else if (line.equals("[FRIENDS]")) {
					ReadMode = 8;
				} else if (line.equals("[IGNORES]")) {
					ReadMode = 9;
				} else if (line.equals("[BOB]")) {
					ReadMode = 10;
				} else if (line.equals("[GRANDEXCHANGE]")) {
					ReadMode = 11;
				} else if (line.equals("[DEGRADEABLES]")) {
					ReadMode = 12;
				} else if (line.equals("[DUNGEONEERING]")) {
					ReadMode = 13;
				} else if (line.equals("[EOF]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {
					}
					return 1;
				}
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {
		}
		return 13;
	}

	/**
	 * Saving
	 **/
	public static boolean saveGame(Player p) {
		if (PlayerHandler.players[p.playerId] == null || p.playerName == null) {
			System.out.println(PlayerHandler.players[p.playerId] + " " + p.playerName);
			return false;
		}
		if (!p.getVariables().saveFile || p.getVariables().newPlayer || !p.getVariables().saveCharacter) {
			System.out.println("ugh.. 2");
			return false;
		}
		p.playerName = p.playerName2;
		int tbTime = (int) (p.getVariables().teleBlockDelay - System.currentTimeMillis()
				+ p.getVariables().teleBlockLength);
		if (tbTime > 300000 || tbTime < 0) {
			tbTime = 0;
		}

		BufferedWriter characterfile = null;
		try {
			characterfile = new BufferedWriter(new FileWriter(("./Data/characters/") + p.playerName + ".txt"));

			/* ACCOUNT */
			characterfile.write("[ACCOUNT]", 0, 9);
			characterfile.newLine();
			characterfile.write("character-username = ", 0, 21);
			characterfile.write(p.playerName, 0, p.playerName.length());
			characterfile.newLine();
			characterfile.write("character-password = ", 0, 21);
			characterfile.write(
					Misc.md5Hash(p.playerPass + p.playerName + (p.playerName.length() * p.playerPass.length())), 0,
					Misc.md5Hash(p.playerPass + p.playerName + (p.playerName.length() * p.playerPass.length()))
							.length());
			characterfile.newLine();
			characterfile.newLine();

			/* CHARACTER */
			characterfile.write("[CHARACTER]", 0, 11);
			characterfile.newLine();
			characterfile.write("character-height = ", 0, 19);
			characterfile.write(Integer.toString(p.heightLevel), 0, Integer.toString(p.heightLevel).length());
			characterfile.newLine();
			characterfile.write("character-posx = ", 0, 17);
			characterfile.write(Integer.toString(p.absX), 0, Integer.toString(p.absX).length());
			characterfile.newLine();
			characterfile.write("character-posy = ", 0, 17);
			characterfile.write(Integer.toString(p.absY), 0, Integer.toString(p.absY).length());
			characterfile.newLine();
			characterfile.write("character-rights = ", 0, 19);
			characterfile.write(Integer.toString(p.getVariables().playerRights), 0,
					Integer.toString(p.getVariables().playerRights).length());
			characterfile.newLine();
			characterfile.write("Donator = ", 0, 10);
			characterfile.write(Integer.toString(p.getVariables().isDonator), 0,
					Integer.toString(p.getVariables().isDonator).length());
			characterfile.newLine();
			characterfile.write("character-title = ", 0, 18);
			characterfile.write(Integer.toString(p.getVariables().playerTitle), 0,
					Integer.toString(p.getVariables().playerTitle).length());
			characterfile.newLine();
			characterfile.write("expLock = ", 0, 10);
			characterfile.write(Boolean.toString(p.getVariables().expLock), 0,
					Boolean.toString(p.getVariables().expLock).length());
			characterfile.newLine();
			characterfile.write("totalxp = ", 0, 9);
			characterfile.write(Integer.toString(p.getVariables().totalxp), 0,
					Integer.toString(p.getVariables().totalxp).length());
			characterfile.newLine();
			characterfile.write("crystal-bow-shots = ", 0, 20);
			characterfile.write(Integer.toString(p.getVariables().crystalBowArrowCount), 0,
					Integer.toString(p.getVariables().crystalBowArrowCount).length());
			characterfile.newLine();
			characterfile.write("skull-timer = ", 0, 14);
			characterfile.write(Integer.toString(p.getVariables().skullTimer), 0,
					Integer.toString(p.getVariables().skullTimer).length());
			characterfile.newLine();
			characterfile.write("magic-book = ", 0, 13);
			characterfile.write(Integer.toString(p.getVariables().playerMagicBook), 0,
					Integer.toString(p.getVariables().playerMagicBook).length());
			characterfile.newLine();
			characterfile.write("prayer-book = ", 0, 14);
			characterfile.write(Boolean.toString(p.getVariables().playerPrayerBook), 0,
					Boolean.toString(p.getVariables().playerPrayerBook).length());
			characterfile.newLine();
			characterfile.write("isDonePicking = ", 0, 16);
			characterfile.write(Boolean.toString(p.getVariables().isDonePicking), 0,
					Boolean.toString(p.getVariables().isDonePicking).length());
			characterfile.newLine();
			characterfile.write("Picked = ", 0, 9);
			characterfile.write(Integer.toString(p.getVariables().Picked), 0,
					Integer.toString(p.getVariables().Picked).length());
			characterfile.newLine();
			characterfile.write("KC = ", 0, 5);
			characterfile.write(Integer.toString(p.getVariables().KC), 0,
					Integer.toString(p.getVariables().KC).length());
			characterfile.newLine();
			characterfile.write("DC = ", 0, 5);
			characterfile.write(Integer.toString(p.getVariables().DC), 0,
					Integer.toString(p.getVariables().DC).length());
			characterfile.newLine();
			characterfile.write("special-amount = ", 0, 17);
			characterfile.write(Double.toString(p.getVariables().specAmount), 0,
					Double.toString(p.getVariables().specAmount).length());
			characterfile.newLine();
			characterfile.write("selected-coffin = ", 0, 18);
			characterfile.write(Integer.toString(p.getVariables().randomCoffin), 0,
					Integer.toString(p.getVariables().randomCoffin).length());
			characterfile.newLine();
			characterfile.write("lastBrother = ", 0, 14);
			characterfile.write(Integer.toString(p.getVariables().lastBrother), 0,
					Integer.toString(p.getVariables().lastBrother).length());
			characterfile.newLine();
			characterfile.write("barrows-npc-dead = ", 0, 19);
			characterfile.write(p.getVariables().brotherKilled[0] + "\t" + p.getVariables().brotherKilled[1] + "\t"
					+ p.getVariables().brotherKilled[2] + "\t" + p.getVariables().brotherKilled[3] + "\t"
					+ p.getVariables().brotherKilled[4] + "\t" + p.getVariables().brotherKilled[5]);
			characterfile.newLine();
			characterfile.write("teleblock-length = ", 0, 19);
			characterfile.write(Integer.toString(tbTime), 0, Integer.toString(tbTime).length());
			characterfile.newLine();
			characterfile.write("votingPoints = ", 0, 15);
			characterfile.write(Integer.toString(p.getVariables().votingPoints), 0,
					Integer.toString(p.getVariables().votingPoints).length());
			characterfile.newLine();
			characterfile.write("timesVoted = ", 0, 13);
			characterfile.write(Integer.toString(p.getVariables().timesVoted), 0,
					Integer.toString(p.getVariables().timesVoted).length());
			characterfile.newLine();
			characterfile.write("Amount-Donated = ", 0, 17);
			characterfile.write(Integer.toString(p.getVariables().amountDonated), 0,
					Integer.toString(p.getVariables().amountDonated).length());
			characterfile.newLine();
			characterfile.write("pc-points = ", 0, 12);
			characterfile.write(Integer.toString(p.getVariables().pcPoints), 0,
					Integer.toString(p.getVariables().pcPoints).length());
			characterfile.newLine();
			characterfile.write("SlayerPoints = ", 0, 15);
			characterfile.write(Integer.toString(p.getVariables().SlayerPoints), 0,
					Integer.toString(p.getVariables().SlayerPoints).length());
			characterfile.newLine();
			characterfile.write("DonatorPoints = ", 0, 16);
			characterfile.write(Integer.toString(p.getVariables().DonatorPoints), 0,
					Integer.toString(p.getVariables().DonatorPoints).length());
			characterfile.newLine();
			characterfile.write("splitChat = ", 0, 12);
			characterfile.write(Boolean.toString(p.splitChat), 0, Boolean.toString(p.splitChat).length());
			characterfile.newLine();
			if (p.getSlayer().getTask() != null) {
				characterfile.write("SLAY-TASK = ", 0, 12);
				characterfile.write(p.getSlayer().getTask().toString(), 0, p.getSlayer().getTask().toString().length());
				characterfile.write("\t", 0, 1);
				characterfile.write(Integer.toString(p.getSlayer().getTask().leftToKill()), 0,
						Integer.toString(p.getSlayer().getTask().leftToKill()).length());
			} else {
				characterfile.write("SLAY-TASK = ", 0, 12);
				characterfile.write("null", 0, 4);
				characterfile.write("\t", 0, 1);
				characterfile.write("0", 0, 1);
			}
			characterfile.newLine();
			characterfile.write("totalTasks = ", 0, 13);
			characterfile.write(Integer.toString(p.getVariables().totalTasks), 0,
					Integer.toString(p.getVariables().totalTasks).length());
			characterfile.newLine();
			characterfile.write("taskDifficulty = ", 0, 17);
			characterfile.write(Integer.toString(p.getVariables().taskDifficulty), 0,
					Integer.toString(p.getVariables().taskDifficulty).length());
			characterfile.newLine();
			characterfile.write("magePoints = ", 0, 13);
			characterfile.write(Integer.toString(p.getVariables().magePoints), 0,
					Integer.toString(p.getVariables().magePoints).length());
			characterfile.newLine();
			characterfile.write("autoRet = ", 0, 10);
			characterfile.write(Integer.toString(p.getVariables().autoRet), 0,
					Integer.toString(p.getVariables().autoRet).length());
			characterfile.newLine();
			characterfile.write("flagged = ", 0, 10);
			characterfile.write(Boolean.toString(p.getVariables().accountFlagged), 0,
					Boolean.toString(p.getVariables().accountFlagged).length());
			characterfile.newLine();
			characterfile.write("hasOverloadBoost = ", 0, 10);
			characterfile.write(Boolean.toString(p.getVariables().hasOverloadBoost), 0,
					Boolean.toString(p.getVariables().hasOverloadBoost).length());
			characterfile.newLine();
			characterfile.write("wave = ", 0, 7);
			characterfile.write(Integer.toString(p.getVariables().waveId), 0,
					Integer.toString(p.getVariables().waveId).length());
			characterfile.newLine();
			characterfile.write("gwkc = ", 0, 7);
			characterfile.write(Integer.toString(p.getVariables().killCount), 0,
					Integer.toString(p.getVariables().killCount).length());
			characterfile.newLine();
			characterfile.write("fightMode = ", 0, 12);
			characterfile.write(Integer.toString(p.getVariables().fightMode), 0,
					Integer.toString(p.getVariables().fightMode).length());
			characterfile.newLine();
			characterfile.write("PKP = ", 0, 6);
			characterfile.write(Integer.toString(p.getVariables().pkp), 0,
					Integer.toString(p.getVariables().pkp).length());
			characterfile.newLine();
			characterfile.write("yellTag = ", 0, 10);
			characterfile.write(p.getVariables().donorTag, 0, p.getVariables().donorTag.length());
			characterfile.newLine();
			characterfile.write("constitution = ", 0, 15);
			characterfile.write(Integer.toString(p.getVariables().constitution), 0,
					Integer.toString(p.getVariables().constitution).length());
			characterfile.newLine();
			characterfile.write("void = ", 0, 7);
			String toWrite = p.getVariables().voidStatus[0] + "\t" + p.getVariables().voidStatus[1] + "\t"
					+ p.getVariables().voidStatus[2] + "\t" + p.getVariables().voidStatus[3] + "\t"
					+ p.getVariables().voidStatus[4];
			characterfile.write(toWrite);
			characterfile.newLine();
			characterfile.write("savedClan = ", 0, 12);
			characterfile.write(p.getVariables().savedClan == null ? "null" : p.getVariables().savedClan, 0,
					p.getVariables().savedClan == null ? 4 : p.getVariables().savedClan.length());
			characterfile.newLine();
			characterfile.write("hasSavedClan = ", 0, 15);
			characterfile.write(Boolean.toString(p.getVariables().hasSavedClan), 0,
					Boolean.toString(p.getVariables().hasSavedClan).length());
			characterfile.newLine();
			characterfile.write("lootSharePotential = ", 0, 21);
			characterfile.write(Long.toString(p.getVariables().lootSharePotential), 0,
					Long.toString(p.getVariables().lootSharePotential).length());
			characterfile.newLine();
			characterfile.write("lastLootDate = ", 0, 15);
			characterfile.write(Long.toString(p.getVariables().lastLootDate), 0,
					Long.toString(p.getVariables().lastLootDate).length());
			characterfile.newLine();
			characterfile.write("vengeanceCasted = ", 0, 18);
			characterfile.write(Integer.toString(p.getVariables().castVengeance), 0,
					Integer.toString(p.getVariables().castVengeance).length());
			characterfile.newLine();
			characterfile.write("familiarSpecialEnergy = ", 0, 24);
			characterfile.write(Integer.toString(p.getSummoning().familiarSpecialEnergy), 0,
					Integer.toString(p.getSummoning().familiarSpecialEnergy).length());
			characterfile.newLine();
			characterfile.write("familiar = ", 0, 11);
			characterfile.write(
					Integer.toString(
							p.getSummoning().summonedFamiliar == null ? -1 : p.getSummoning().summonedFamiliar.pouchId),
					0,
					Integer.toString(
							p.getSummoning().summonedFamiliar == null ? -1 : p.getSummoning().summonedFamiliar.pouchId)
							.length());
			characterfile.newLine();
			characterfile.write("RockCrabKills = ", 0, 16);
			characterfile.write(Integer.toString(p.getVariables().rockCrabKills), 0,
					Integer.toString(p.getVariables().rockCrabKills).length());
			characterfile.newLine();
			characterfile.write("run-energy = ", 0, 13);
			characterfile.write(Integer.toString(p.getVariables().runEnergy), 0,
					Integer.toString(p.getVariables().runEnergy).length());
			characterfile.newLine();
			characterfile.newLine();

			characterfile.write("[GODWARS]");
			characterfile.newLine();
			for (int i = 0; i < p.getVariables().gwKills.length; i++) {
				characterfile.write("godwars = ", 0, 10);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.getVariables().gwKills[i]), 0,
						Integer.toString(p.getVariables().gwKills[i]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* QUICK-PRAYERS */
			characterfile.write("[QUICKS]", 0, 8);
			characterfile.newLine();
			characterfile.write("quickCurses = ", 0, 14);
			String toWrite1 = "";
			for (int i1 = 0; i1 < p.getVariables().quickCurses.length; i1++) {
				toWrite1 += p.getVariables().quickCurses[i1] + "\t";
			}
			characterfile.write(toWrite1);
			characterfile.newLine();
			characterfile.write("quickPrayers = ", 0, 15);
			String toWrite2 = "";
			for (int i1 = 0; i1 < p.getVariables().quickPrayers.length; i1++) {
				toWrite2 += p.getVariables().quickPrayers[i1] + "\t";
			}
			characterfile.write(toWrite2);
			characterfile.newLine();
			characterfile.newLine();

			/* EQUIPMENT */
			characterfile.write("[EQUIPMENT]", 0, 11);
			characterfile.newLine();
			for (int i = 0; i < p.getVariables().playerEquipment.length; i++) {
				characterfile.write("character-equip = ", 0, 18);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.getVariables().playerEquipment[i]), 0,
						Integer.toString(p.getVariables().playerEquipment[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.getVariables().playerEquipmentN[i]), 0,
						Integer.toString(p.getVariables().playerEquipmentN[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.newLine();
			}
			characterfile.newLine();

			/* LOOK */
			characterfile.write("[LOOK]", 0, 6);
			characterfile.newLine();
			for (int i = 0; i < p.getVariables().playerAppearance.length; i++) {
				characterfile.write("character-look = ", 0, 17);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.getVariables().playerAppearance[i]), 0,
						Integer.toString(p.getVariables().playerAppearance[i]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* SKILLS */
			characterfile.write("[SKILLS]", 0, 8);
			characterfile.newLine();
			for (int i = 0; i < p.getVariables().playerLevel.length; i++) {
				characterfile.write("character-skill = ", 0, 18);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.getVariables().playerLevel[i]), 0,
						Integer.toString(p.getVariables().playerLevel[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.getVariables().playerXP[i]), 0,
						Integer.toString(p.getVariables().playerXP[i]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* ITEMS */
			characterfile.write("[ITEMS]", 0, 7);
			characterfile.newLine();
			for (int i = 0; i < p.getVariables().playerItems.length; i++) {
				if (p.getVariables().playerItems[i] > 0) {
					characterfile.write("character-item = ", 0, 17);
					characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.getVariables().playerItems[i]), 0,
							Integer.toString(p.getVariables().playerItems[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.getVariables().playerItemsN[i]), 0,
							Integer.toString(p.getVariables().playerItemsN[i]).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();

			/* BANK */
			characterfile.write("[BANK]", 0, 6);
			characterfile.newLine();
			for (int i = 0; i < p.getVariables().bankItems.length; i++) {
				if (p.getVariables().bankItems[i] > 0) {
					characterfile.write("character-bank = ", 0, 17);
					characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.getVariables().bankItems[i]), 0,
							Integer.toString(p.getVariables().bankItems[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.getVariables().bankItemsN[i]), 0,
							Integer.toString(p.getVariables().bankItemsN[i]).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();

			/* FRIENDS */
			characterfile.write("[FRIENDS]", 0, 9);
			characterfile.newLine();
			for (int i = 0; i < p.getVariables().friends.length; i++) {
				if (p.getVariables().friends[i] > 0) {
					characterfile.write("character-friend = ", 0, 19);
					characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write("" + p.getVariables().friends[i]);
					characterfile.newLine();
				}
			}
			characterfile.newLine();

			/* IGNORES */
			/*
			 * characterfile.write("[IGNORES]", 0, 9); characterfile.newLine();
			 * for (int i = 0; i < ignores.length; i++) { if (ignores[i] > 0) {
			 * characterfile.write("character-ignore = ", 0, 19);
			 * characterfile.write(Integer.toString(i), 0,
			 * Integer.toString(i).length()); characterfile.write("	", 0, 1);
			 * characterfile.write(Long.toString(ignores[i]), 0,
			 * Long.toString(ignores[i]).length()); characterfile.newLine(); } }
			 * characterfile.newLine();
			 */

			characterfile.write("[BOB]", 0, 5);
			characterfile.newLine();
			if (p.getSummoning().burdenedItems != null) {
				for (int i = 0; i < p.getSummoning().burdenedItems.length; i++) {
					if (p.getSummoning().burdenedItems[i] > 0) {
						characterfile.write("character-bob = ", 0, 16);
						characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
						characterfile.write("	", 0, 1);
						characterfile.write(Integer.toString(p.getSummoning().burdenedItems[i]), 0,
								Integer.toString(p.getSummoning().burdenedItems[i]).length());
						characterfile.newLine();
					}
				}
			}
			characterfile.newLine();

			characterfile.write("[GRANDEXCHANGE]", 0, 15);
			characterfile.newLine();
			for (int i = 1; i < p.GE().Slots.length; i++) {
				characterfile.write("slot = ", 0, 7);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.GE().Slots[i]), 0, Integer.toString(p.GE().Slots[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.GE().SlotType[i]), 0,
						Integer.toString(p.GE().SlotType[i]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			characterfile.write("[DEGRADEABLES]", 0, 14);
			characterfile.newLine();
			for (int i = 1; i < p.chaoticDegrade.length; i++) {
				characterfile.write("chaotic = ", 0, 10);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.chaoticDegrade[i]), 0,
						Integer.toString(p.chaoticDegrade[i]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			characterfile.write("[DUNGEONEERING]", 0, 15);
			characterfile.newLine();
			for (int i = 1; i < 6; i++) {
				characterfile.write("bound = ", 0, 8);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.boundItems[i][0]), 0,
						Integer.toString(p.boundItems[i][0]).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.boundItems[i][1]), 0,
						Integer.toString(p.boundItems[i][1]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* EOF */
			characterfile.write("[EOF]", 0, 5);
			characterfile.newLine();
			characterfile.newLine();
			characterfile.close();
		} catch (IOException ioexception) {
			Misc.println(p.playerName + ": error writing file.");
			ioexception.printStackTrace();
			return false;
		}
		return true;
	}

}