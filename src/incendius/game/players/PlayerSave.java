package incendius.game.players;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import incendius.Data;
import incendius.util.Misc;

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
			characterfile = new BufferedReader(new FileReader(Data.CHARACTER_DIRECTORY + playerName + ".txt"));
			File1 = true;
		} catch (FileNotFoundException fileex1) {
		}

		if (!File1) {
			Misc.println(playerName + ": character file not found.");
			p.getInstance().newPlayer = false;
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
						p.getInstance().lastAbsX = p.teleportToX;
					} else if (token.equals("character-posy")) {
						p.teleportToY = (Integer.parseInt(token2) <= 0 ? 3424 : Integer.parseInt(token2));
						p.getInstance().lastAbsY = p.teleportToY;
					} else if (token.equals("character-rights")) {
						p.getInstance().playerRights = Integer.parseInt(token2);
					} else if (token.equals("isJailed")) {
						p.setJailed(Boolean.parseBoolean(token2));
					} else if (token.equals("hasSavedClan")) {
						p.getInstance().hasSavedClan = Boolean.parseBoolean(token2);
					} else if (token.equals("savedClan")) {
						p.getInstance().savedClan = token2.equals("null") ? null : token2;
					} else if (token.equals("lootSharePotential")) {
						p.getInstance().lootSharePotential = Long.parseLong(token2);
					} else if (token.equals("lastLootDate")) {
						p.getInstance().lastLootDate = Long.parseLong(token2);
					} else if (token.equals("RockCrabKills")) {
						p.getInstance().rockCrabKills = Integer.parseInt(token2);
					} else if (token.equals("Donator")) {
						p.getInstance().isDonator = Integer.parseInt(token2);
					} else if (token.equals("vengeanceCasted")) {
						p.getInstance().castVengeance = Integer.parseInt(token2);
					} else if (token.equals("character-title")) {
						p.getInstance().playerTitle = Integer.parseInt(token2);
					} else if (token.equals("expLock")) {
						p.getInstance().expLock = Boolean.parseBoolean(token2);
					} else if (token.equals("totalxp")) {
						p.getInstance().totalxp = Integer.parseInt(token2);
					} else if (token.equals("tutorial-progress")) {
						p.getInstance().tutorial = Byte.parseByte(token2);
					} else if (token.equals("crystal-bow-shots")) {
						p.getInstance().crystalBowArrowCount = Integer.parseInt(token2);
					} else if (token.equals("barrows-npc-dead")) {
						for (int j = 0; j < token3.length; j++) {
							p.getInstance().brotherKilled[j] = Boolean.parseBoolean(token3[j]);
						}
					} else if (token.equals("skull-timer")) {
						p.getInstance().skullTimer = Integer.parseInt(token2);
					} else if (token.equals("magic-book")) {
						p.getInstance().playerMagicBook = Integer.parseInt(token2);
					} else if (token.equals("prayer-book")) {
						p.getInstance().playerPrayerBook = Boolean.parseBoolean(token2);
					} else if (token.equals("isDonePicking")) {
						p.getInstance().isDonePicking = Boolean.parseBoolean(token2);
					} else if (token.equals("Picked")) {
						p.getInstance().Picked = Integer.parseInt(token2);
					} else if (token.equals("KC")) {
						p.getInstance().KC = Integer.parseInt(token2);
					} else if (token.equals("DC")) {
						p.getInstance().DC = Integer.parseInt(token2);
					} else if (token.equals("special-amount")) {
						p.getInstance().specAmount = Double.parseDouble(token2);
					} else if (token.equals("selected-coffin")) {
						p.getInstance().randomCoffin = Integer.parseInt(token2);
					} else if (token.equals("lastBrother")) {
						p.getInstance().lastBrother = Integer.parseInt(token2);
					} else if (token.equals("teleblock-length")) {
						p.getInstance().teleBlockDelay = System.currentTimeMillis();
						p.getInstance().teleBlockLength = Integer.parseInt(token2);
					} else if (token.equals("votingPoints")) {
						p.getInstance().votingPoints = Integer.parseInt(token2);
					} else if (token.equals("timesVoted")) {
						p.getInstance().timesVoted = Integer.parseInt(token2);
					} else if (token.equals("Amount-Donated")) {
						p.getInstance().amountDonated = Integer.parseInt(token2);
					} else if (token.equals("pc-points")) {
						p.getInstance().pcPoints = Integer.parseInt(token2);
					} else if (token.equals("SlayerPoints")) {
						p.getInstance().SlayerPoints = Integer.parseInt(token2);
					} else if (token.equals("DonatorPoints")) {
						p.getInstance().DonatorPoints = Integer.parseInt(token2);
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
						p.getInstance().totalTasks = Integer.parseInt(token2);
					} else if (token.equals("taskDifficulty")) {
						p.getInstance().taskDifficulty = Integer.parseInt(token2);
					} else if (token.equals("magePoints")) {
						p.getInstance().magePoints = Integer.parseInt(token2);
					} else if (token.equals("autoRet")) {
						p.getInstance().autoRet = Integer.parseInt(token2);
					} else if (token.equals("godwars")) {
						for (int j = 0; j < token3.length; j++) {
							p.getInstance().gwKills[j] = Integer.parseInt(token3[j]);
						}
					} else if (token.equals("quickCurses")) {
						for (int j = 0; j < token3.length; j++) {
							p.getInstance().quickCurses[j] = Boolean.parseBoolean(token3[j]);
						}
					} else if (token.equals("quickPrayers")) {
						for (int j = 0; j < token3.length; j++) {
							p.getInstance().quickPrayers[j] = Boolean.parseBoolean(token3[j]);
						}
					} else if (token.equals("flagged")) {
						p.getInstance().accountFlagged = Boolean.parseBoolean(token2);
					} else if (token.equals("hasOverloadBoost")) {
						p.getInstance().hasOverloadBoost = Boolean.parseBoolean(token2);
					} else if (token.equals("wave")) {
						p.getInstance().waveId = Integer.parseInt(token2);
					} else if (token.equals("void")) {
						for (int j = 0; j < token3.length; j++) {
							p.getInstance().voidStatus[j] = Integer.parseInt(token3[j]);
						}
					} else if (token.equals("gwkc")) {
						p.getInstance().killCount = Integer.parseInt(token2);
					} else if (token.equals("fightMode")) {
						p.getInstance().fightMode = Integer.parseInt(token2);
					} else if (token.equals("PKP")) {
						p.getInstance().pkp = Integer.parseInt(token2);
					} else if (token.equals("yellTag")) {
						p.getInstance().donorTag = token2;
					} else if (token.equals("constitution")) {
						p.getInstance().lifePoints = Integer.parseInt(token2);
					} else if (token.equals("familiarSpecialEnergy")) {
						p.getSummoning().familiarSpecialEnergy = Integer.parseInt(token2);
					} else if (token.equals("familiar")) {
						try {
							p.getSummoning().summonFamiliar(Integer.parseInt(token2), true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (token.equals("run-energy")) {
						p.getInstance().runEnergy = Integer.parseInt(token2);
						
					/**
					 * Support for saving display names to character files.
					 * 
					 * - Branon McClellan (KeepBotting)
					 */
					} else if (line.startsWith("displayName")) {
						p.displayName = token2;
					}
					
					break;
				case 3:
					if (token.equals("character-equip")) {
						p.getInstance().playerEquipment[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						p.getInstance().playerEquipmentN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
					}
					break;
				case 4:
					if (token.equals("character-look")) {
						p.getInstance().playerAppearance[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
					}
					break;
				case 5:
					if (token.equals("character-skill")) {
						p.getInstance().playerLevel[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						p.getInstance().playerXP[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
					}
					break;
				case 6:
					if (token.equals("character-item")) {
						p.getInstance().playerItems[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						p.getInstance().playerItemsN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
					}
					break;
				case 7:
					if (token.equals("character-bank")) {
						p.getInstance().bankItems[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						p.getInstance().bankItemsN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
					}
					break;
				case 8:
					if (token.equals("character-friend")) {
						p.getInstance().friends[Integer.parseInt(token3[0])] = Long.parseLong(token3[1]);
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
		/**
		 * If the player's name or ID happens to be null, don't continue.
		 */
		if (PlayerHandler.players[p.playerId] == null || p.playerName == null) {
			System.out.println(PlayerHandler.players[p.playerId] + " " + p.playerName);
			return false;
		}
		
		/**
		 * saveFile is initialized as false, and never set anywhere else.
		 * 
		 * newPlayer is set to false when this class fails to find a player's
		 * existing character file, and thus creates a character file for the
		 * first time, OR when a seemingly un-implemented "Rules Interface" is
		 * closed.
		 * 
		 * saveCharacter seems to be a flag dictating that a player should be saved.
		 */
		if (!p.getInstance().saveFile || p.getInstance().newPlayer || !p.getInstance().saveCharacter) {
			System.out.println("ugh.. 2");
			return false;
		}
		
		/**
		 * playerName and playerName2 are exactly the same...
		 */
		p.playerName = p.playerName2;
		
		/**
		 * If our tele-block time is over 30000 or less than 0, reset it to 0. I
		 * guess this is to prevent teleblocks from persisting across logins?
		 */
		int tbTime = (int) (p.getInstance().teleBlockDelay - System.currentTimeMillis()
				+ p.getInstance().teleBlockLength);
		if (tbTime > 300000 || tbTime < 0) {
			tbTime = 0;
		}

		BufferedWriter characterfile = null;
		try {
			characterfile = new BufferedWriter(new FileWriter(Data.CHARACTER_DIRECTORY + p.playerName + ".txt"));

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
			characterfile.write(Integer.toString(p.getInstance().playerRights), 0,
					Integer.toString(p.getInstance().playerRights).length());
			characterfile.newLine();
			characterfile.write("isJailed = ", 0, 11);
			characterfile.write(String.valueOf(p.isJailed()), 0, String.valueOf(p.isJailed()).length());
			characterfile.newLine();
			characterfile.write("Donator = ", 0, 10);
			characterfile.write(Integer.toString(p.getInstance().isDonator), 0,
					Integer.toString(p.getInstance().isDonator).length());
			characterfile.newLine();
			characterfile.write("character-title = ", 0, 18);
			characterfile.write(Integer.toString(p.getInstance().playerTitle), 0,
					Integer.toString(p.getInstance().playerTitle).length());
			characterfile.newLine();
			characterfile.write("expLock = ", 0, 10);
			characterfile.write(Boolean.toString(p.getInstance().expLock), 0,
					Boolean.toString(p.getInstance().expLock).length());
			characterfile.newLine();
			characterfile.write("totalxp = ", 0, 9);
			characterfile.write(Integer.toString(p.getInstance().totalxp), 0,
					Integer.toString(p.getInstance().totalxp).length());
			characterfile.newLine();
			characterfile.write("crystal-bow-shots = ", 0, 20);
			characterfile.write(Integer.toString(p.getInstance().crystalBowArrowCount), 0,
					Integer.toString(p.getInstance().crystalBowArrowCount).length());
			characterfile.newLine();
			characterfile.write("skull-timer = ", 0, 14);
			characterfile.write(Integer.toString(p.getInstance().skullTimer), 0,
					Integer.toString(p.getInstance().skullTimer).length());
			characterfile.newLine();
			characterfile.write("magic-book = ", 0, 13);
			characterfile.write(Integer.toString(p.getInstance().playerMagicBook), 0,
					Integer.toString(p.getInstance().playerMagicBook).length());
			characterfile.newLine();
			characterfile.write("prayer-book = ", 0, 14);
			characterfile.write(Boolean.toString(p.getInstance().playerPrayerBook), 0,
					Boolean.toString(p.getInstance().playerPrayerBook).length());
			characterfile.newLine();
			characterfile.write("isDonePicking = ", 0, 16);
			characterfile.write(Boolean.toString(p.getInstance().isDonePicking), 0,
					Boolean.toString(p.getInstance().isDonePicking).length());
			characterfile.newLine();
			characterfile.write("Picked = ", 0, 9);
			characterfile.write(Integer.toString(p.getInstance().Picked), 0,
					Integer.toString(p.getInstance().Picked).length());
			characterfile.newLine();
			characterfile.write("KC = ", 0, 5);
			characterfile.write(Integer.toString(p.getInstance().KC), 0,
					Integer.toString(p.getInstance().KC).length());
			characterfile.newLine();
			characterfile.write("DC = ", 0, 5);
			characterfile.write(Integer.toString(p.getInstance().DC), 0,
					Integer.toString(p.getInstance().DC).length());
			characterfile.newLine();
			characterfile.write("special-amount = ", 0, 17);
			characterfile.write(Double.toString(p.getInstance().specAmount), 0,
					Double.toString(p.getInstance().specAmount).length());
			characterfile.newLine();
			characterfile.write("selected-coffin = ", 0, 18);
			characterfile.write(Integer.toString(p.getInstance().randomCoffin), 0,
					Integer.toString(p.getInstance().randomCoffin).length());
			characterfile.newLine();
			characterfile.write("lastBrother = ", 0, 14);
			characterfile.write(Integer.toString(p.getInstance().lastBrother), 0,
					Integer.toString(p.getInstance().lastBrother).length());
			characterfile.newLine();
			characterfile.write("barrows-npc-dead = ", 0, 19);
			characterfile.write(p.getInstance().brotherKilled[0] + "\t" + p.getInstance().brotherKilled[1] + "\t"
					+ p.getInstance().brotherKilled[2] + "\t" + p.getInstance().brotherKilled[3] + "\t"
					+ p.getInstance().brotherKilled[4] + "\t" + p.getInstance().brotherKilled[5]);
			characterfile.newLine();
			characterfile.write("teleblock-length = ", 0, 19);
			characterfile.write(Integer.toString(tbTime), 0, Integer.toString(tbTime).length());
			characterfile.newLine();
			characterfile.write("votingPoints = ", 0, 15);
			characterfile.write(Integer.toString(p.getInstance().votingPoints), 0,
					Integer.toString(p.getInstance().votingPoints).length());
			characterfile.newLine();
			characterfile.write("timesVoted = ", 0, 13);
			characterfile.write(Integer.toString(p.getInstance().timesVoted), 0,
					Integer.toString(p.getInstance().timesVoted).length());
			characterfile.newLine();
			characterfile.write("Amount-Donated = ", 0, 17);
			characterfile.write(Integer.toString(p.getInstance().amountDonated), 0,
					Integer.toString(p.getInstance().amountDonated).length());
			characterfile.newLine();
			characterfile.write("pc-points = ", 0, 12);
			characterfile.write(Integer.toString(p.getInstance().pcPoints), 0,
					Integer.toString(p.getInstance().pcPoints).length());
			characterfile.newLine();
			characterfile.write("SlayerPoints = ", 0, 15);
			characterfile.write(Integer.toString(p.getInstance().SlayerPoints), 0,
					Integer.toString(p.getInstance().SlayerPoints).length());
			characterfile.newLine();
			characterfile.write("DonatorPoints = ", 0, 16);
			characterfile.write(Integer.toString(p.getInstance().DonatorPoints), 0,
					Integer.toString(p.getInstance().DonatorPoints).length());
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
			characterfile.write(Integer.toString(p.getInstance().totalTasks), 0,
					Integer.toString(p.getInstance().totalTasks).length());
			characterfile.newLine();
			characterfile.write("taskDifficulty = ", 0, 17);
			characterfile.write(Integer.toString(p.getInstance().taskDifficulty), 0,
					Integer.toString(p.getInstance().taskDifficulty).length());
			characterfile.newLine();
			characterfile.write("magePoints = ", 0, 13);
			characterfile.write(Integer.toString(p.getInstance().magePoints), 0,
					Integer.toString(p.getInstance().magePoints).length());
			characterfile.newLine();
			characterfile.write("autoRet = ", 0, 10);
			characterfile.write(Integer.toString(p.getInstance().autoRet), 0,
					Integer.toString(p.getInstance().autoRet).length());
			characterfile.newLine();
			characterfile.write("flagged = ", 0, 10);
			characterfile.write(Boolean.toString(p.getInstance().accountFlagged), 0,
					Boolean.toString(p.getInstance().accountFlagged).length());
			characterfile.newLine();
			characterfile.write("hasOverloadBoost = ", 0, 10);
			characterfile.write(Boolean.toString(p.getInstance().hasOverloadBoost), 0,
					Boolean.toString(p.getInstance().hasOverloadBoost).length());
			characterfile.newLine();
			characterfile.write("wave = ", 0, 7);
			characterfile.write(Integer.toString(p.getInstance().waveId), 0,
					Integer.toString(p.getInstance().waveId).length());
			characterfile.newLine();
			characterfile.write("gwkc = ", 0, 7);
			characterfile.write(Integer.toString(p.getInstance().killCount), 0,
					Integer.toString(p.getInstance().killCount).length());
			characterfile.newLine();
			characterfile.write("fightMode = ", 0, 12);
			characterfile.write(Integer.toString(p.getInstance().fightMode), 0,
					Integer.toString(p.getInstance().fightMode).length());
			characterfile.newLine();
			characterfile.write("PKP = ", 0, 6);
			characterfile.write(Integer.toString(p.getInstance().pkp), 0,
					Integer.toString(p.getInstance().pkp).length());
			characterfile.newLine();
			characterfile.write("yellTag = ", 0, 10);
			characterfile.write(p.getInstance().donorTag, 0, p.getInstance().donorTag.length());
			characterfile.newLine();
			characterfile.write("constitution = ", 0, 15);
			characterfile.write(Integer.toString(p.getInstance().lifePoints), 0,
					Integer.toString(p.getInstance().lifePoints).length());
			characterfile.newLine();
			characterfile.write("void = ", 0, 7);
			String toWrite = p.getInstance().voidStatus[0] + "\t" + p.getInstance().voidStatus[1] + "\t"
					+ p.getInstance().voidStatus[2] + "\t" + p.getInstance().voidStatus[3] + "\t"
					+ p.getInstance().voidStatus[4];
			characterfile.write(toWrite);
			characterfile.newLine();
			characterfile.write("savedClan = ", 0, 12);
			characterfile.write(p.getInstance().savedClan == null ? "null" : p.getInstance().savedClan, 0,
					p.getInstance().savedClan == null ? 4 : p.getInstance().savedClan.length());
			characterfile.newLine();
			characterfile.write("hasSavedClan = ", 0, 15);
			characterfile.write(Boolean.toString(p.getInstance().hasSavedClan), 0,
					Boolean.toString(p.getInstance().hasSavedClan).length());
			characterfile.newLine();
			characterfile.write("lootSharePotential = ", 0, 21);
			characterfile.write(Long.toString(p.getInstance().lootSharePotential), 0,
					Long.toString(p.getInstance().lootSharePotential).length());
			characterfile.newLine();
			characterfile.write("lastLootDate = ", 0, 15);
			characterfile.write(Long.toString(p.getInstance().lastLootDate), 0,
					Long.toString(p.getInstance().lastLootDate).length());
			characterfile.newLine();
			characterfile.write("vengeanceCasted = ", 0, 18);
			characterfile.write(Integer.toString(p.getInstance().castVengeance), 0,
					Integer.toString(p.getInstance().castVengeance).length());
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
			characterfile.write(Integer.toString(p.getInstance().rockCrabKills), 0,
					Integer.toString(p.getInstance().rockCrabKills).length());
			characterfile.newLine();
			characterfile.write("run-energy = ", 0, 13);
			characterfile.write(Integer.toString(p.getInstance().runEnergy), 0,
					Integer.toString(p.getInstance().runEnergy).length());
			characterfile.newLine();
			
			/**
			 * Support for writing display names to the character file.
			 * 
			 * - Branon McClellan (KeepBotting)
			 */
			characterfile.write("displayName = ", 0, 14);
			characterfile.write(p.displayName, 0, p.displayName.length());	
			
			characterfile.newLine();
			characterfile.newLine();

			characterfile.write("[GODWARS]");
			characterfile.newLine();
			for (int i = 0; i < p.getInstance().gwKills.length; i++) {
				characterfile.write("godwars = ", 0, 10);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.getInstance().gwKills[i]), 0,
						Integer.toString(p.getInstance().gwKills[i]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* QUICK-PRAYERS */
			characterfile.write("[QUICKS]", 0, 8);
			characterfile.newLine();
			characterfile.write("quickCurses = ", 0, 14);
			String toWrite1 = "";
			for (int i1 = 0; i1 < p.getInstance().quickCurses.length; i1++) {
				toWrite1 += p.getInstance().quickCurses[i1] + "\t";
			}
			characterfile.write(toWrite1);
			characterfile.newLine();
			characterfile.write("quickPrayers = ", 0, 15);
			String toWrite2 = "";
			for (int i1 = 0; i1 < p.getInstance().quickPrayers.length; i1++) {
				toWrite2 += p.getInstance().quickPrayers[i1] + "\t";
			}
			characterfile.write(toWrite2);
			characterfile.newLine();
			characterfile.newLine();

			/* EQUIPMENT */
			characterfile.write("[EQUIPMENT]", 0, 11);
			characterfile.newLine();
			for (int i = 0; i < p.getInstance().playerEquipment.length; i++) {
				characterfile.write("character-equip = ", 0, 18);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.getInstance().playerEquipment[i]), 0,
						Integer.toString(p.getInstance().playerEquipment[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.getInstance().playerEquipmentN[i]), 0,
						Integer.toString(p.getInstance().playerEquipmentN[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.newLine();
			}
			characterfile.newLine();

			/* LOOK */
			characterfile.write("[LOOK]", 0, 6);
			characterfile.newLine();
			for (int i = 0; i < p.getInstance().playerAppearance.length; i++) {
				characterfile.write("character-look = ", 0, 17);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.getInstance().playerAppearance[i]), 0,
						Integer.toString(p.getInstance().playerAppearance[i]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* SKILLS */
			/**
			 * So, as near as I can gather, PI saves skills like the following:
			 * 
			 * character-skill = [id] [lvl] [xp]
			 * 
			 * Where [id] is the ID of the skill (1 to 25), [lvl] is the skill
			 * level (1 to 99), and [xp] is the experience points of the skill
			 * (0 to 200000000).
			 */
			characterfile.write("[SKILLS]", 0, 8);
			characterfile.newLine();
			/**
			 * playerLevel seems to be the amount of skills a player has, it's
			 * hardcoded to 25.
			 */
			for (int i = 0; i < p.getInstance().playerLevel.length; i++) {
				/**
				 * Write the prefix.
				 */
				characterfile.write("character-skill = ", 0, 18);
				/**
				 * All integers are written as strings, which makes sense since
				 * they're being written to a textfile.
				 * 
				 * write() is used here, which writes like the following:
				 * 
				 * file.write([string], [offset], [length]);
				 * 
				 * Where [string] is the string to be written, [offset] is the
				 * offset from which to start reading characters, and [length]
				 * is the number of characters to be written.
				 * 
				 * So here, we're writing the ID of the skill. 
				 */
				characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
				/**
				 * Next, we write a tab. Note that this is actually a hard-tab
				 * and not five spaces. Note also that a new line is not created
				 * until characterfile.newLine(); is called, so this is all on
				 * the same line.
				 */
				characterfile.write("	", 0, 1);
				/**
				 * Now, we're writing the level of the skill, ascertained from
				 * playerLevel. At this point, I can only include that
				 * playerLevel represents both the ID of a skill (its index) and
				 * its level (its value).
				 */
				characterfile.write(Integer.toString(p.getInstance().playerLevel[i]), 0,
						Integer.toString(p.getInstance().playerLevel[i]).length());
				/**
				 * Another hard-tab.
				 */
				characterfile.write("	", 0, 1);
				/**
				 * Last thing write is the player's xp for the given skill,
				 * ascertained from playerXP.
				 */
				characterfile.write(Integer.toString(p.getInstance().playerXP[i]), 0,
						Integer.toString(p.getInstance().playerXP[i]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* ITEMS */
			characterfile.write("[ITEMS]", 0, 7);
			characterfile.newLine();
			for (int i = 0; i < p.getInstance().playerItems.length; i++) {
				if (p.getInstance().playerItems[i] > 0) {
					characterfile.write("character-item = ", 0, 17);
					characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.getInstance().playerItems[i]), 0,
							Integer.toString(p.getInstance().playerItems[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.getInstance().playerItemsN[i]), 0,
							Integer.toString(p.getInstance().playerItemsN[i]).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();

			/* BANK */
			characterfile.write("[BANK]", 0, 6);
			characterfile.newLine();
			for (int i = 0; i < p.getInstance().bankItems.length; i++) {
				if (p.getInstance().bankItems[i] > 0) {
					characterfile.write("character-bank = ", 0, 17);
					characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.getInstance().bankItems[i]), 0,
							Integer.toString(p.getInstance().bankItems[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.getInstance().bankItemsN[i]), 0,
							Integer.toString(p.getInstance().bankItemsN[i]).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();

			/* FRIENDS */
			characterfile.write("[FRIENDS]", 0, 9);
			characterfile.newLine();
			for (int i = 0; i < p.getInstance().friends.length; i++) {
				if (p.getInstance().friends[i] > 0) {
					characterfile.write("character-friend = ", 0, 19);
					characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write("" + p.getInstance().friends[i]);
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
			
		} catch (IOException ioe) {
			Misc.println(p.playerName + ": error writing file.");
			ioe.printStackTrace();
			return false;
		}
		return true;
	}
}