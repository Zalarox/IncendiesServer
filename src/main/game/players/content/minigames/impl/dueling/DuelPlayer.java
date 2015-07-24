package main.game.players.content.minigames.impl.dueling;

import java.util.ArrayList;
import java.util.List;

import main.Constants;
import main.GameEngine;
import main.event.CycleEvent;
import main.event.CycleEventContainer;
import main.event.CycleEventHandler;
import main.event.Task;
import main.game.items.GameItem;
import main.game.items.ItemLoader;
import main.game.players.Player;
import main.game.players.actions.combat.CombatPrayer;
import main.handlers.ItemHandler;
import main.util.Misc;

/**
 * 
 * @author TheLife
 *
 */

public class DuelPlayer {

	public static int RULE_WALKING = 1, RULE_RANGED = 2, RULE_MELEE = 3, RULE_MAGIC = 4, RULE_POTIONS = 5,
			RULE_FOOD = 6, RULE_PRAYER = 7, RULE_OBSTACLES = 8, RULE_FUN_WEAPONS = 9, RULE_SPECIAL_ATTACK = 10,
			RULE_HATS = 11, RULE_CAPES = 12, RULE_AMULETS = 13, RULE_WEAPONS = 14, RULE_BODIES = 15, RULE_SHIELDS = 16,
			RULE_LEGS = 17, RULE_GLOVES = 18, RULE_BOOTS = 19, RULE_RINGS = 20, RULE_ARROWS = 21;

	public static int WINNER_X_COORD = 3377, WINNER_Y_COORD = 3271, LOSS_X_COORD = 3358, LOSS_Y_COORD = 3276;

	public static List<Player> players = new ArrayList<Player>();
	public static List<Player> duelScreenFirst = new ArrayList<Player>();
	public static List<Player> duelScreenSecond = new ArrayList<Player>();

	public static void addToDuel(Player player) {
		players.add(player);
		player.getVariables().canOffer = false;
	}

	public static void addToFirstScreen(Player opponent, Player player) {
		opponent.setBusy(true);
		player.setBusy(true);
		duelScreenFirst.add(player);
		duelScreenFirst.add(opponent);
		player.getVariables().canOffer = true;
		opponent.getVariables().canOffer = true;
	}

	public static void addToSecondScreen(Player opponent, Player player) {
		opponent.setBusy(true);
		player.setBusy(true);
		duelScreenSecond.add(player);
		duelScreenSecond.add(opponent);
		player.getVariables().canOffer = false;
		opponent.getVariables().canOffer = false;
	}

	public static boolean contains(Player player) {
		return players.contains(player);
	}

	public static boolean isInFirstScreen(Player player) {
		return duelScreenFirst.contains(player);
	}

	public static boolean isInSecondScreen(Player player) {
		return duelScreenSecond.contains(player);
	}

	public static boolean remove(Player player) {
		return players.remove(player);
	}

	public static boolean removeFromFirstScreen(Player player) {
		return duelScreenFirst.remove(player);
	}

	public static boolean removeFromSecondScreen(Player player) {
		return duelScreenSecond.remove(player);
	}

	public void requestDuel(Player opponent, Player player, boolean face) {
		try {
			player.getVariables().playerIndex = 0;
			declineDuel(player, true, true);
			if (!canDuel(opponent, player)) {
				player.opponent = null;
				return;
			}
			resetDuel(player);
			resetDuelItems(player);
			if (face)
				player.turnPlayerTo(opponent.getX(), opponent.getY());
			player.opponent = opponent;
			player.getVariables().duelRequested = true;
			player.getVariables().killedDuelOpponent = false;
			opponent.getVariables().killedDuelOpponent = false;
			if (player.opponent == opponent && opponent.opponent == player && !contains(player) && !contains(opponent)
					&& player.getVariables().duelRequested && opponent.getVariables().duelRequested) {
				addToFirstScreen(opponent, player);
				openDuel(player);
				openDuel(opponent);
				player.turnPlayerTo(opponent.getX(), opponent.getY());
				opponent.turnPlayerTo(player.getX(), player.getY());
			} else {
				player.sendMessage("Sending duel request...");
				opponent.sendMessage(player.playerName + ":duelreq:");
			}
		} catch (Exception e) {
			Misc.println("Error requesting duel.");
		}
	}

	public void duelVictory(final Player c) {
		if (c.opponent != null) {
			c.getPA().sendString("" + c.opponent.calculateCombatLevel(), 6839);
			c.getPA().sendString(c.opponent.playerName, 6840);
		} else {
			c.getPA().sendString("", 6839);
			c.getPA().sendString("", 6840);
		}
		c.getPA().movePlayer(WINNER_X_COORD, WINNER_Y_COORD, 0);
		CombatPrayer.resetPrayers(c);
		for (int i = 0; i < 20; i++) {
			c.getVariables().playerLevel[i] = c.getPA().getLevelForXP(c.getVariables().playerXP[i]);
			c.getPA().refreshSkill(i);
		}
		c.getVariables().castVengeance = 0;
		c.getVariables().vengOn = false;
		c.getPA().refreshSkill(3);
		if (!c.getVariables().autoGive)
			duelRewardInterface(c);
		c.getPA().showInterface(6733);
		c.getPA().requestUpdates();
		c.getPA().showOption(3, 0, "Challenge", 3);
		c.getPA().createPlayerHints(10, -1);
		c.getVariables().canOffer = true;
		c.getVariables().duelSpaceReq = 0;
		c.getCombat().resetPlayerAttack();
		c.getVariables().duelRequested = false;
		GameEngine.getScheduler().schedule(new Task(1) {
			@Override
			public void execute() {
				for (GameItem item : c.getVariables().stakedItems) {
					if (item.id > 0 && item.amount > 0) {
						if (ItemLoader.isStackable(item.id)) {
							c.getInventory().add(item.id, item.amount);
						} else {
							int amount = item.amount;
							for (int a = 1; a <= amount; a++) {
								if (!c.getItems().addItem(item.id, 1)) {
									ItemHandler.createGroundItem(c, item.id, c.getX(), c.getY(), c.heightLevel, 1,
											c.getId());
								}
							}
						}
					}
				}
				c.getVariables().stakedItems.clear();
				this.stop();
			}
		});
		if (c.getVariables().autoGive)
			claimStakedItems(c);
	}

	public void duelRewardInterface(Player c) {
		if (c.getOutStream() != null && c != null) {
			// synchronized(c) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(6822);
			c.getOutStream().writeWord(c.getVariables().otherStakedItems.toArray().length);
			for (GameItem item : c.getVariables().otherStakedItems) {
				if (item.amount > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(item.amount);
				} else {
					c.getOutStream().writeByte(item.amount);
				}
				if (item.id > Constants.ITEM_LIMIT || item.id < 0) {
					item.id = Constants.ITEM_LIMIT;
				}
				c.getOutStream().writeWordBigEndianA(item.id + 1);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
		// }
	}

	public void startDuel(Player c) {
		if (c.opponent == null) {
			duelVictory(c);
		}
		c.getVariables().headIconHints = 2;
		if (c.getVariables().duelRule[RULE_PRAYER]) {
			for (int p = 0; p < CombatPrayer.PRAYER.length; p++) {
				c.getVariables().prayerActive[p] = false;
				c.getPA().sendFrame36(CombatPrayer.PRAYER_GLOW[p], 0);
			}
			c.getVariables().headIcon = -1;
			c.getPA().requestUpdates();
		}
		if (c.getVariables().duelRule[RULE_HATS]) {
			c.getItems().removeItem(c.getVariables().playerEquipment[0], 0);
		}
		if (c.getVariables().duelRule[RULE_CAPES]) {
			c.getItems().removeItem(c.getVariables().playerEquipment[1], 1);
		}
		if (c.getVariables().duelRule[RULE_AMULETS]) {
			c.getItems().removeItem(c.getVariables().playerEquipment[2], 2);
		}
		if (c.getVariables().duelRule[RULE_WEAPONS]) {
			c.getItems().removeItem(c.getVariables().playerEquipment[3], 3);
		}
		if (c.getVariables().duelRule[RULE_BODIES]) {
			c.getItems().removeItem(c.getVariables().playerEquipment[4], 4);
		}
		if (c.getVariables().duelRule[RULE_SHIELDS]) {
			c.getItems().removeItem(c.getVariables().playerEquipment[5], 5);
		}
		if (c.getVariables().duelRule[RULE_SHIELDS]
				&& c.getItems().is2handed(c.getItems().getItemName(c.getVariables().playerEquipment[3]).toLowerCase(),
						c.getVariables().playerEquipment[3])) {
			c.getItems().removeItem(c.getVariables().playerEquipment[3], 3);
		}
		if (c.getVariables().duelRule[RULE_LEGS]) {
			c.getItems().removeItem(c.getVariables().playerEquipment[7], 7);
		}
		if (c.getVariables().duelRule[RULE_GLOVES]) {
			c.getItems().removeItem(c.getVariables().playerEquipment[9], 9);
		}
		if (c.getVariables().duelRule[RULE_BOOTS]) {
			c.getItems().removeItem(c.getVariables().playerEquipment[10], 10);
		}
		if (c.getVariables().duelRule[RULE_RINGS]) {
			c.getItems().removeItem(c.getVariables().playerEquipment[12], 12);
		}
		if (c.getVariables().duelRule[RULE_ARROWS]) {
			c.getItems().removeItem(c.getVariables().playerEquipment[13], 13);
		}
		c.getPA().removeAllWindows();
		c.getVariables().specAmount = 10;
		c.getItems().addSpecialBar(c.getVariables().playerEquipment[c.getVariables().playerWeapon]);
		if (c.getVariables().duelRule[RULE_OBSTACLES]) {
			if (c.getVariables().duelRule[RULE_WALKING]) {
				c.getPA().movePlayer(c.getVariables().duelTeleX, c.getVariables().duelTeleY, 0);
			} else {
				c.getPA().movePlayer(3366 + Misc.random(12), 3246 + Misc.random(6), 0);
			}
		} else {
			if (c.getVariables().duelRule[RULE_WALKING]) {
				c.getPA().movePlayer(c.getVariables().duelTeleX, c.getVariables().duelTeleY, 0);
			} else {
				c.getPA().movePlayer(3335 + Misc.random(12), 3246 + Misc.random(6), 0);
			}
		}

		c.getVariables().freezeTimer = 0;
		CombatPrayer.resetPrayers(c);
		c.getVariables().constitution = c.getVariables().calculateMaxLifePoints(c);
		for (int i = 0; i < 20; i++) {
			c.getVariables().playerLevel[i] = c.getPA().getLevelForXP(c.getVariables().playerXP[i]);
			c.getPA().refreshSkill(i);
		}
		c.getCombat().resetPlayerAttack();
		c.getVariables().isSkulled = false;
		c.getPA().resetTb();
		c.getPA().resetAnimation();
		c.startAnimation(65535);
		c.getVariables().attackedPlayers.clear();
		c.getVariables().headIconPk = -1;
		c.getVariables().skullTimer = -1;
		c.getVariables().damageTaken = new int[Constants.MAX_PLAYERS];
		c.getVariables().isFullHelm = ItemLoader
				.isFullHelm(c.opponent.getVariables().playerEquipment[c.getVariables().playerHat]);
		c.getVariables().isFullMask = ItemLoader
				.isFullMask(c.opponent.getVariables().playerEquipment[c.getVariables().playerHat]);
		c.getVariables().isFullBody = ItemLoader
				.isFullBody(c.opponent.getVariables().playerEquipment[c.getVariables().playerChest]);
		c.getPA().createPlayerHints(10, c.opponent.playerId);
		addToDuel(c);
		for (int i = 0; i < 20; i++) {
			c.getVariables().playerLevel[i] = c.getPA().getLevelForXP(c.getVariables().playerXP[i]);
			c.getPA().refreshSkill(i);
		}
		for (GameItem item : c.opponent.getVariables().stakedItems) {
			c.getVariables().otherStakedItems.add(new GameItem(item.id, item.amount));
		}
		c.getItems().sendWeapon(c.getVariables().playerEquipment[c.getVariables().playerWeapon],
				c.getItems().getItemName(c.getVariables().playerEquipment[c.getVariables().playerWeapon]));
		c.getItems().resetBonus();
		c.getItems().getBonus();
		c.getItems().writeBonus();
		c.getCombat().getPlayerAnimIndex(c.getItems()
				.getItemName(c.getVariables().playerEquipment[c.getVariables().playerWeapon]).toLowerCase());
		c.getPA().requestUpdates();
		c.getVariables().castVengeance = 0;
		c.getVariables().vengOn = false;
	}

	public void openDuel(Player player) {
		if (player.opponent == null) {
			return;
		}
		player.getVariables().acceptedFirst = false;
		player.getVariables().acceptedSecond = false;
		refreshduelRules(player);
		refreshDuelScreen(player);
		player.getVariables().canOffer = true;
		for (int i = 0; i < player.getVariables().playerEquipment.length; i++) {
			sendDuelEquipment(player.getVariables().playerEquipment[i], player.getVariables().playerEquipmentN[i], i,
					player);
		}
		player.getPA().sendString("Dueling with: " + player.opponent.playerName + " (level: "
				+ player.opponent.calculateCombatLevel() + ")", 6671);
		player.getPA().sendString("", 6684);
		player.getPA().sendFrame248(6575, 3321);
		player.getItems().resetItems(3322);
	}

	public boolean stakeItem(int itemID, int fromSlot, int amount, Player c) {
		if (itemID != c.getVariables().playerItems[fromSlot] - 1) {
			return false;
		}
		if (!DuelPlayer.isInFirstScreen(c)) {
			return false;
		}
		if (DuelPlayer.isInSecondScreen(c)) {
			return false;
		}
		if (DuelPlayer.contains(c)) {
			return false;
		}
		if (!c.getTradeHandler().tradeable(itemID)) {
			c.sendMessage("You can't stake this item.");
			return false;
		}
		if (!c.getItems().playerHasItem(itemID, amount))
			return false;
		if (amount <= 0)
			return false;
		if (c.opponent == null) {
			declineDuel(c, false, false);
			return false;
		}
		if (!c.getVariables().canOffer) {
			return false;
		}
		changeDuelStuff(c);
		if (!ItemLoader.isStackable(itemID)) {
			for (int a = 0; a < amount; a++) {
				if (c.getItems().playerHasItem(itemID, 1)) {
					c.getVariables().stakedItems.add(new GameItem(itemID, 1));
					c.getItems().deleteItem(itemID, c.getItems().getItemSlot(itemID), 1);
				}
			}
			c.getItems().resetItems(3214);
			c.getItems().resetItems(3322);
			c.opponent.getItems().resetItems(3214);
			c.opponent.getItems().resetItems(3322);
			refreshDuelScreen(c);
			refreshDuelScreen(c.opponent);
			c.getPA().sendString("", 6684);
			c.opponent.getPA().sendString("", 6684);
		}

		if (!c.getItems().playerHasItem(itemID, amount)) {
			return false;
		}
		if (ItemLoader.isStackable(itemID) || ItemLoader.isNote(itemID)) {
			boolean found = false;
			for (GameItem item : c.getVariables().stakedItems) {
				if (item.id == itemID) {
					found = true;
					item.amount += amount;
					c.getItems().deleteItem(itemID, fromSlot, amount);
					break;
				}
			}
			if (!found) {
				c.getItems().deleteItem(itemID, fromSlot, amount);
				c.getVariables().stakedItems.add(new GameItem(itemID, amount));
			}
		}

		c.getItems().resetItems(3214);
		c.getItems().resetItems(3322);
		c.opponent.getItems().resetItems(3214);
		c.opponent.getItems().resetItems(3322);
		refreshDuelScreen(c);
		refreshDuelScreen(c.opponent);
		c.getPA().sendString("", 6684);
		c.opponent.getPA().sendString("", 6684);
		return true;
	}

	public void selectRule(int i, Player c) { // rules
		if (c.opponent == null) {
			return;
		}
		if (!c.getVariables().canOffer)
			return;
		c.getPA().sendString("", 6684);
		c.opponent.getPA().sendString("", 6684);
		c.getVariables().acceptedFirst = false;
		c.opponent.getVariables().acceptedFirst = false;
		changeDuelStuff(c);
		c.opponent.getVariables().duelSlot = c.getVariables().duelSlot;
		if (i >= 11 && c.getVariables().duelSlot > -1) {
			if (c.getVariables().playerEquipment[c.getVariables().duelSlot] > 0) {
				if (!c.getVariables().duelRule[i]) {
					c.getVariables().duelSpaceReq++;
				} else {
					c.getVariables().duelSpaceReq--;
				}
			}
			if (c.opponent.getVariables().playerEquipment[c.opponent.getVariables().duelSlot] > 0) {
				if (!c.opponent.getVariables().duelRule[i]) {
					c.opponent.getVariables().duelSpaceReq++;
				} else {
					c.opponent.getVariables().duelSpaceReq--;
				}
			}
		}

		if (i >= 11) {
			if (c.getItems().freeSlots() < (c.getVariables().duelSpaceReq)
					|| c.opponent.getItems().freeSlots() < (c.opponent.getVariables().duelSpaceReq)) {
				c.sendMessage("You or your opponent don't have the required space to set this rule.");
				if (c.getVariables().playerEquipment[c.getVariables().duelSlot] > 0) {
					c.getVariables().duelSpaceReq--;
				}
				if (c.opponent.getVariables().playerEquipment[c.opponent.getVariables().duelSlot] > 0) {
					c.opponent.getVariables().duelSpaceReq--;
				}
				return;
			}
		}

		if (!c.getVariables().duelRule[i]) {
			c.getVariables().duelRule[i] = true;
			c.getVariables().duelOption += c.getVariables().DUEL_RULE_ID[i];
		} else {
			c.getVariables().duelRule[i] = false;
			c.getVariables().duelOption -= c.getVariables().DUEL_RULE_ID[i];
		}

		c.getPA().sendFrame87(286, c.getVariables().duelOption);
		c.opponent.getVariables().duelOption = c.getVariables().duelOption;
		c.opponent.getVariables().duelRule[i] = c.getVariables().duelRule[i];
		c.opponent.getPA().sendFrame87(286, c.opponent.getVariables().duelOption);
		if (c.getVariables().duelRule[RULE_OBSTACLES]) {
			if (c.getVariables().duelRule[RULE_WALKING]) {
				c.getVariables().duelTeleX = 3366 + Misc.random(12);
				c.opponent.getVariables().duelTeleX = c.getVariables().duelTeleX - 1;
				c.getVariables().duelTeleY = 3246 + Misc.random(6);
				c.opponent.getVariables().duelTeleY = c.getVariables().duelTeleY;
			}
		} else {
			if (c.getVariables().duelRule[RULE_WALKING]) {
				c.getVariables().duelTeleX = 3335 + Misc.random(12);
				c.opponent.getVariables().duelTeleX = c.getVariables().duelTeleX - 1;
				c.getVariables().duelTeleY = 3246 + Misc.random(6);
				c.opponent.getVariables().duelTeleY = c.getVariables().duelTeleY;
			}
		}

	}

	public boolean fromDuel(int itemID, int fromSlot, int amount, Player c) {
		if (!DuelPlayer.isInFirstScreen(c)) {
			return false;
		}
		if (DuelPlayer.contains(c) || DuelPlayer.isInSecondScreen(c)) {
			return false;
		}
		if (c.opponent == null) {
			declineDuel(c, false, false);
			return false;
		}
		if (ItemLoader.isStackable(itemID)) {
			if (c.getItems().freeSlots() - 1 < (c.getVariables().duelSpaceReq)) {
				c.sendMessage("You have too many rules set to remove that item.");
				return false;
			}
		}

		if (!c.getVariables().canOffer) {
			return false;
		}

		changeDuelStuff(c);
		boolean goodSpace = true;
		if (!ItemLoader.isStackable(itemID)) {
			for (int a = 0; a < amount; a++) {
				for (GameItem item : c.getVariables().stakedItems) {
					if (item.id == itemID) {
						if (!item.stackable) {
							if (c.getItems().freeSlots() - 1 < (c.getVariables().duelSpaceReq)) {
								goodSpace = false;
								break;
							}
							c.getVariables().stakedItems.remove(item);
							c.getItems().addItem(itemID, 1);
						} else {
							if (c.getItems().freeSlots() - 1 < (c.getVariables().duelSpaceReq)) {
								goodSpace = false;
								break;
							}
							if (item.amount > amount) {
								item.amount -= amount;
								c.getItems().addItem(itemID, amount);
							} else {
								if (c.getItems().freeSlots() - 1 < (c.getVariables().duelSpaceReq)) {
									goodSpace = false;
									break;
								}
								amount = item.amount;
								c.getVariables().stakedItems.remove(item);
								c.getItems().addItem(itemID, amount);
							}
						}
						break;
					}
					c.getItems().resetItems(3214);
					c.getItems().resetItems(3322);
					c.opponent.getItems().resetItems(3214);
					c.opponent.getItems().resetItems(3322);
					refreshDuelScreen(c);
					refreshDuelScreen(c.opponent);
					c.opponent.getPA().sendString("", 6684);
				}
			}
		}

		for (GameItem item : c.getVariables().stakedItems) {
			if (item.id == itemID) {
				if (!item.stackable) {
				} else {
					if (item.amount > amount) {
						item.amount -= amount;
						c.getItems().addItem(itemID, amount);
					} else {
						amount = item.amount;
						c.getVariables().stakedItems.remove(item);
						c.getItems().addItem(itemID, amount);
					}
				}
				break;
			}
		}
		c.getItems().resetItems(3214);
		c.getItems().resetItems(3322);
		c.opponent.getItems().resetItems(3214);
		c.opponent.getItems().resetItems(3322);
		refreshDuelScreen(c);
		refreshDuelScreen(c.opponent);
		c.opponent.getPA().sendString("", 6684);
		if (!goodSpace) {
			c.sendMessage("You have too many rules set to remove that item.");
			return true;
		}
		return true;
	}

	public void changeDuelStuff(Player player) {
		if (player.opponent == null) {
			return;
		}
		player.getVariables().acceptedFirst = false;
		player.opponent.getVariables().acceptedFirst = false;
		player.opponent.getPA().sendString("", 6684);
		player.getPA().sendString("", 6684);
	}

	public void confirmDuel(Player c) {
		if (c.opponent == null) {
			declineDuel(c, false, false);
			return;
		}
		String itemId = "";
		for (GameItem item : c.getVariables().stakedItems) {
			if (ItemLoader.isStackable(item.id) || ItemLoader.isNote(item.id)) {
				itemId += c.getItems().getItemName(item.id) + " x " + Misc.format(item.amount) + "\\n";
			} else {
				itemId += c.getItems().getItemName(item.id) + "\\n";
			}
		}
		c.getPA().sendString(itemId, 6516);
		itemId = "";
		for (GameItem item : c.opponent.getVariables().stakedItems) {
			if (ItemLoader.isStackable(item.id) || ItemLoader.isNote(item.id)) {
				itemId += c.getItems().getItemName(item.id) + " x " + Misc.format(item.amount) + "\\n";
			} else {
				itemId += c.getItems().getItemName(item.id) + "\\n";
			}
		}
		c.getPA().sendString(itemId, 6517);
		c.getPA().sendString("", 8242);
		for (int i = 8238; i <= 8253; i++) {
			c.getPA().sendString("", i);
		}
		c.getPA().sendString("Hitpoints will be restored.", 8250);
		c.getPA().sendString("Boosted stats will be restored.", 8238);
		if (c.getVariables().duelRule[RULE_OBSTACLES]) {
			c.getPA().sendString("There will be obstacles in the arena.", 8239);
		}
		c.getPA().sendString("", 8240);
		c.getPA().sendString("", 8241);

		String[] rulesOption = { "Players cannot forfeit!", "Players cannot move.", "Players cannot use range.",
				"Players cannot use melee.", "Players cannot use magic.", "Players cannot drink pots.",
				"Players cannot eat food.", "Players cannot use prayer." };

		int lineNumber = 8242;
		for (int i = 0; i < 8; i++) {
			if (c.getVariables().duelRule[i]) {
				c.getPA().sendString("" + rulesOption[i], lineNumber);
				lineNumber++;
			}
		}
		c.getPA().sendString("", 6571);
		c.getPA().sendFrame248(6412, 197);
	}

	public void declineDuel(Player c, boolean Throw, boolean r) {
		if (DuelPlayer.contains(c)) {
			return;
		}
		if (c.opponent != null && DuelPlayer.contains(c.opponent)) {
			return;
		}
		if (!r)
			c.getPA().removeAllWindows();
		if (c.opponent != null && c.opponent.opponent == c) {
			c.opponent.sendMessage("The duel has been declined.");
		}
		if (Throw) {
			if (c.opponent != null)
				declineDuel(c.opponent, false, false);
		}
		c.opponent = null;
		c.getVariables().canOffer = true;
		removeFromFirstScreen(c);
		removeFromSecondScreen(c);
		remove(c);
		c.getVariables().duelSpaceReq = 0;
		c.getVariables().duelRequested = false;
		for (GameItem item : c.getVariables().stakedItems) {
			if (item.amount < 1)
				continue;
			if (ItemLoader.isStackable(item.id) || ItemLoader.isNote(item.id)) {
				c.getItems().addItem(item.id, item.amount);
			} else {
				c.getItems().addItem(item.id, 1);
			}
		}
		c.getVariables().otherStakedItems.clear();
		c.getVariables().stakedItems.clear();
		for (int i = 0; i < c.getVariables().duelRule.length; i++) {
			c.getVariables().duelRule[i] = false;
		}
	}

	public void sendDuelEquipment(int itemId, int amount, int slot, Player player) {
		synchronized (player) {
			if (itemId != 0) {
				player.getOutStream().createFrameVarSizeWord(34);
				player.getOutStream().writeWord(13824);
				player.getOutStream().writeByte(slot);
				player.getOutStream().writeWord(itemId + 1);

				if (amount > 254) {
					player.getOutStream().writeByte(255);
					player.getOutStream().writeDWord(amount);
				} else {
					player.getOutStream().writeByte(amount);
				}
				player.getOutStream().endFrameVarSizeWord();
				player.flushOutStream();
			}
		}
	}

	public void refreshduelRules(Player player) {
		for (int i = 0; i < player.getVariables().duelRule.length; i++) {
			player.getVariables().duelRule[i] = false;
		}
		player.getPA().sendFrame87(286, 0);
		player.getVariables().duelOption = 0;
	}

	public void refreshDuelScreen(Player player) {
		synchronized (player) {
			if (player.opponent == null) {
				return;
			}
			player.getOutStream().createFrameVarSizeWord(53);
			player.getOutStream().writeWord(6669);
			player.getOutStream().writeWord(player.getVariables().stakedItems.toArray().length);
			int current = 0;
			for (GameItem item : player.getVariables().stakedItems) {
				if (item.amount > 254) {
					player.getOutStream().writeByte(255);
					player.getOutStream().writeDWord_v2(item.amount);
				} else {
					player.getOutStream().writeByte(item.amount);
				}
				if (item.id > Constants.ITEM_LIMIT || item.id < 0) {
					item.id = Constants.ITEM_LIMIT;
				}
				player.getOutStream().writeWordBigEndianA(item.id + 1);

				current++;
			}

			if (current < 27) {
				for (int i = current; i < 28; i++) {
					player.getOutStream().writeByte(1);
					player.getOutStream().writeWordBigEndianA(-1);
				}
			}
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();

			player.getOutStream().createFrameVarSizeWord(53);
			player.getOutStream().writeWord(6670);
			player.getOutStream().writeWord(player.opponent.getVariables().stakedItems.toArray().length);
			current = 0;
			for (GameItem item : player.opponent.getVariables().stakedItems) {
				if (item.amount > 254) {
					player.getOutStream().writeByte(255);
					player.getOutStream().writeDWord_v2(item.amount);
				} else {
					player.getOutStream().writeByte(item.amount);
				}
				if (item.id > Constants.ITEM_LIMIT || item.id < 0) {
					item.id = Constants.ITEM_LIMIT;
				}
				player.getOutStream().writeWordBigEndianA(item.id + 1);
				current++;
			}

			if (current < 27) {
				for (int i = current; i < 28; i++) {
					player.getOutStream().writeByte(1);
					player.getOutStream().writeWordBigEndianA(-1);
				}
			}
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();
		}
	}

	public boolean canDuel(final Player opponent, final Player player) {
		if (opponent == null || player == null) {
			return false;
		}
		if (player.playerName == opponent.playerName) {
			return false;
		}
		if (player.isBusy(opponent)) {
			player.sendMessage("Other player is currently busy.");
			return false;
		}
		if (!player.goodDistance(opponent.getX(), opponent.getY(), player.getX(), player.getY(), 1)
				|| !opponent.goodDistance(player.getX(), player.getY(), opponent.getX(), opponent.getY(), 1)) {
			player.copyOfOpponent = opponent;
			player.getVariables().headingTowardsPlayer = true;
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				int timer = 0;

				@Override
				public void execute(CycleEventContainer container) {
					if (timer >= 20)
						container.stop();
					if (!player.getVariables().headingTowardsPlayer)
						container.stop();
					if (player.goodDistance(player.copyOfOpponent.getX(), player.copyOfOpponent.getY(), player.getX(),
							player.getY(), 1)
							|| player.copyOfOpponent.goodDistance(player.getX(), player.getY(),
									player.copyOfOpponent.getX(), player.copyOfOpponent.getY(), 1)) {
						if (player != null && player.copyOfOpponent != null)
							requestDuel(player.copyOfOpponent, player, false);
						container.stop();
						timer++;
					}
				}

				@Override
				public void stop() {
					if (player != null)
						player.getVariables().headingTowardsPlayer = false;
				}
			}, 1);
			return false;
		}
		return true;
	}

	public void claimStakedItems(Player player) {
		for (GameItem item : player.getVariables().otherStakedItems) {
			if (item.id > 0 && item.amount > 0) {
				if (ItemLoader.isStackable(item.id)) {
					player.getInventory().add(item.id, item.amount);
				} else {
					int amount = item.amount;
					for (int a = 1; a <= amount; a++) {
						if (!player.getItems().addItem(item.id, 1)) {
							ItemHandler.createGroundItem(player, item.id, player.getX(), player.getY(),
									player.heightLevel, 1, player.getId());
						}
					}
				}
			}
		}
		remove(player);
		removeFromFirstScreen(player);
		removeFromSecondScreen(player);
		player.getVariables().killedDuelOpponent = false;
		resetDuel(player);
		resetDuelItems(player);
	}

	public void resetDuel(Player player) {
		player.getPA().showOption(3, 0, "Challenge", 3);
		player.getVariables().headIconHints = 0;
		for (int i = 0; i < player.getVariables().duelRule.length; i++) {
			player.getVariables().duelRule[i] = false;
		}
		player.getPA().createPlayerHints(10, -1);
		player.getVariables().canOffer = true;
		player.getVariables().duelSpaceReq = 0;
		player.getPA().requestUpdates();
		player.getCombat().resetPlayerAttack();
		player.getVariables().duelRequested = false;
		if (player.opponent != null) {
			player.opponent.opponent = null;
		}
		remove(player);
		removeFromFirstScreen(player);
		removeFromSecondScreen(player);
		player.getVariables().killedDuelOpponent = false;
		player.opponent = null;
	}

	public void resetDuelItems(Player player) {
		player.getVariables().stakedItems.clear();
		player.getVariables().otherStakedItems.clear();
	}

}
