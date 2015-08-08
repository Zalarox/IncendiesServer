package main.game.players.content.minigames;

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
 * @author Sanity, TheLife, Branon McClellan (KeepBotting) 
 */

public class DuelArena {

	/**
	 * Some integers representing various duel rules.
	 */
	public static int
	/**
	 * Global rules.
	 */
	RULE_MOVEMENT = 1, 
	RULE_RANGED = 2, 
	RULE_MELEE = 3, 
	RULE_MAGIC = 4, 
	RULE_POTIONS = 5,
	RULE_FOOD = 6, 
	RULE_PRAYER = 7, 
	RULE_OBSTACLES = 8, 
	RULE_FUN_WEAPONS = 9, 
	RULE_SPECIAL_ATTACK = 10,
	/**
	 * Equipment rules.
	 */
	RULE_HATS = 11, 
	RULE_CAPES = 12, 
	RULE_AMULETS = 13, 
	RULE_WEAPONS = 14, 
	RULE_BODIES = 15, 
	RULE_SHIELDS = 16,
	RULE_LEGS = 17, 
	RULE_GLOVES = 18, 
	RULE_BOOTS = 19, 
	RULE_RINGS = 20, 
	RULE_ARROWS = 21;
	
	/**
	 * Some integers representing where players are placed after winning or
	 * losing a duel.
	 */
	public static int 
	WINNER_X_COORD = 3377, 
	WINNER_Y_COORD = 3271, 
	LOSER_X_COORD = 3358, 
	LOSER_Y_COORD = 3276;
	
	/**
	 * Some integers representing various duel interfaces.
	 */
	public static int
	FIRST_INTERFACE = 6575,
	SECOND_INTERFACE = 6412,
	REWARD_INTERFACE = 6733,
	DEFEAT_INTERFACE = 9999;
	
	/**
	 * Some ArrayLists that keep track of what portion of the dueling process
	 * players are in.
	 */
	public static List<Player> isDueling = new ArrayList<Player>();
	public static List<Player> isInFirstInterface = new ArrayList<Player>();
	public static List<Player> isInSecondInterface = new ArrayList<Player>();
	
	/**
	 * Adds a player to the "duelingPlayers" ArrayList.
	 * 
	 * @param player
	 *            The player to add.
	 */
	public static void addToDuel(Player player) {
		isDueling.add(player);
		player.getInstance().canOffer = false;
	}

	/**
	 * Adds both a player and their opponent to the 1st dueling interface.
	 * 
	 * @param opponent
	 *            The opponent to add.
	 * @param player
	 *            The player to add.
	 */
	public static void addToFirstInterface(Player opponent, Player player) {
		/**
		 * Ensure both players are set as "busy" -- other offers sent while
		 * they are preparing to duel should be ignored.
		 */
		opponent.setBusy(true);
		player.setBusy(true);
		
		isInFirstInterface.add(player);
		isInFirstInterface.add(opponent);
		
		/**
		 * Ensure that neither player can send offers once they enter the
		 * dueling screen.
		 */
		player.getInstance().canOffer = true;
		opponent.getInstance().canOffer = true;
	}
	
	/**
	 * Adds both a player and their opponent to the 2nd dueling interface.
	 * 
	 * @param opponent
	 *            The opponent to add.
	 * @param player
	 *            The player to add.
	 */
	public static void addToSecondInterface(Player opponent, Player player) {
		opponent.setBusy(true);
		player.setBusy(true);
		
		isInSecondInterface.add(player);
		isInSecondInterface.add(opponent);
		
		player.getInstance().canOffer = false;
		opponent.getInstance().canOffer = false;
	}
	
	/**
	 * Checks if a player is in the duelingPlayers ArrayList.
	 * 
	 * @param player
	 *            The player to check.
	 * @return Whether or not the player is in the duelingPlayers ArrayList.
	 */
	public static boolean isDueling(Player player) {
		return isDueling.contains(player);
	}
	
	/**
	 * Checks if a player is in the 1st dueling interface.
	 * 
	 * @param player
	 *            The player to check.
	 * @return Whether or not the player is in the 1st dueling interface.
	 */
	public static boolean isInFirstInterface(Player player) {
		return isInFirstInterface.contains(player);
	}

	/**
	 * Checks if a player is in the 2nd dueling interface.
	 * 
	 * @param player
	 *            The player to check.
	 * @return Whether or not the player is in the 2nd dueling interface.
	 */
	public static boolean isInSecondInterface(Player player) {
		return isInSecondInterface.contains(player);
	}
	
	/**
	 * Removes a player from the isDueling ArrayList.
	 * 
	 * @param player
	 *            The player to remove.
	 */
	public static boolean removeFromDueling(Player player) {
		return isDueling.remove(player);
	}
	
	/**
	 * Removes a player from the 1st dueling interface.
	 * 
	 * @param player
	 *            The player to remove.
	 */
	public static boolean removeFromFirstInterface(Player player) {
		return isInFirstInterface.remove(player);
	}
	
	/**
	 * Removes a player from the 2nd dueling interface.
	 * 
	 * @param player
	 *            The player to remove.
	 */
	public static boolean removeFromSecondInterface(Player player) {
		return isInSecondInterface.remove(player);
	}
	
	/**
	 * Determines whether or not two players can duel.
	 * 
	 * @param opponent
	 *            The opponent fighting the duel.
	 * @param player
	 *            The player fighting the duel.
	 * @return True if both players can duel. False otherwise.
	 */
	public boolean canDuel(final Player opponent, final Player player) {
		/**
		 * Null check, return false if either player has become null.
		 */
		if (opponent == null || player == null) {
			return false;
		}
		
		/**
		 * Self-check, return false if the player is attempting to duel
		 * themself.
		 */
		if (player.playerName == opponent.playerName) {
			return false;
		}
		
		/**
		 * Busy check, return false if the opponent is busy.
		 */
		if (player.isBusy(opponent)) {
			player.sendMessage("Other player is currently busy.");
			return false;
		}
		
		/**
		 * Distance check, if either player is too far away from the other, then
		 * we have to move closer to our opponent.
		 */
		if (!player.goodDistance(opponent.getX(), opponent.getY(), player.getX(), player.getY(), 1)
				|| !opponent.goodDistance(player.getX(), player.getY(), opponent.getX(), opponent.getY(), 1)) {
			/**
			 * We use a copy of the opponent's Player object here because we
			 * wish to base X and Y values off of it -- shouldn't do that if
			 * we're using the ACTUAL Player object, since players tend to move
			 * around.
			 */
			player.copyOfOpponent = opponent;
			player.getInstance().headingTowardsPlayer = true;
			
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				int timer = 0;

				@Override
				public void execute(CycleEventContainer container) {
					if (timer >= 20)
						container.stop();
					if (!player.getInstance().headingTowardsPlayer)
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
						player.getInstance().headingTowardsPlayer = false;
				}
			}, 1);
			return false;
		}
		return true;
	}
	
	/**
	 * Sends a duel request.
	 * 
	 * @param opponent
	 *            The player who will receive the request.
	 * @param player
	 *            The player who has sent the request.
	 * @param face
	 *            Whether or not the sender should turn to face the receiver.
	 */
	public void requestDuel(Player opponent, Player player, boolean face) {
		try {
			player.getInstance().playerIndex = 0;
			
			/**
			 * If a player is already in the dueling interface, decline it.
			 */
			declineDuel(player, true, true);
			
			/**
			 * If either player is null, clear the opponent and don't continue.
			 */
			if (!canDuel(opponent, player)) {
				player.opponent = null;
				return;
			}
			
			/**
			 * If a player is requesting a duel, they're most likely in need of a duel reset.
			 */
			resetDuel(player);
			resetDuelItems(player);
			
			/**
			 * Face the opponent, if appropriate.
			 */
			if (face)
				player.turnPlayerTo(opponent.getX(), opponent.getY());
			
			/**
			 * Assign the opponent.
			 */
			player.opponent = opponent;
			
			/**
			 * The player has now requested a duel.
			 */
			player.getInstance().duelRequested = true;
			
			/**
			 * The duel's outcome is not yet decided, so these variables shall
			 * be set false for now.
			 */
			player.getInstance().killedDuelOpponent = false;
			opponent.getInstance().killedDuelOpponent = false;
			
			/**
			 * Opponent check. Make sure both players are opponents of one another.
			 */
			if (player.opponent == opponent && opponent.opponent == player 
					/**
					 * ArrayList check. Make sure both players are in the duelingPlayers ArrayList.
					 */
					&& !isDueling(player) && !isDueling(opponent)
					/**
					 * Duel request check. Make sure both players have requested
					 * this duel.
					 * 
					 * This conditional is special because it handles a scenario
					 * where both players have right-clicked and <Challenge>'d
					 * one another rather than answering requests in the
					 * chatbox.
					 */
					&& player.getInstance().duelRequested && opponent.getInstance().duelRequested) {
				
				/**
				 * If everything checks out, add them to the ArrayList and open
				 * the interface on both ends.
				 */
				addToFirstInterface(opponent, player);
				openDuelInterface(player);
				openDuelInterface(opponent);
				
				/**
				 * Contestants, face each other!
				 */
				player.turnPlayerTo(opponent.getX(), opponent.getY());
				opponent.turnPlayerTo(player.getX(), player.getY());
			} else {
				/**
				 * If the above conditional doesn't check out, then these two
				 * aren't quite ready to duel yet. Let's send a request, to help
				 * them on their way.
				 */
				player.sendMessage("Sending duel request...");
				opponent.sendMessage(player.getDisplayName() + ":duelreq:");
			}
			
		} catch (Exception e) {
			Misc.println("Error requesting duel.");
		}
	}
	
	/**
	 * Opens the dueling interface for the specified player.
	 * 
	 * @param player
	 *            The player to whom this interface belongs.
	 */
	public void openDuelInterface(Player player) {
		/**
		 * If they don't have an opponent, they shouldn't be opening this
		 * interface.
		 */
		if (player.opponent == null) {
			return;
		}
		
		/**
		 * If they've just opened this interface, there's no possible way they
		 * could have clicked either <Accept> button.
		 */
		player.getInstance().acceptedFirst = false;
		player.getInstance().acceptedSecond = false;
		
		refreshDuelRules(player);
		refreshDuelScreen(player);
		
		player.getInstance().canOffer = true;
		
		/**
		 * Populate the interface.
		 */
		for (int i = 0; i < player.getInstance().playerEquipment.length; i++) {
			getDuelEquipment(player.getInstance().playerEquipment[i], player.getInstance().playerEquipmentN[i], i,
					player);
		}
		
		player.getPA().sendString("Dueling with: " + player.opponent.getDisplayName() + " (level: "
				+ player.opponent.calculateCombatLevel() + ")", 6671);
		
		player.getPA().sendString("", 6684);
		player.getPA().sendFrame248(FIRST_INTERFACE, 3321);
		player.getItems().resetItems(3322);
	}
	
	public void confirmDuel(Player c) {
		if (c.opponent == null) {
			declineDuel(c, false, false);
			return;
		}
		String itemId = "";
		for (GameItem item : c.getInstance().stakedItems) {
			if (ItemLoader.isStackable(item.id) || ItemLoader.isNote(item.id)) {
				itemId += c.getItems().getItemName(item.id) + " x " + Misc.format(item.amount) + "\\n";
			} else {
				itemId += c.getItems().getItemName(item.id) + "\\n";
			}
		}
		c.getPA().sendString(itemId, 6516);
		itemId = "";
		for (GameItem item : c.opponent.getInstance().stakedItems) {
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
		if (c.getInstance().duelRule[RULE_OBSTACLES]) {
			c.getPA().sendString("There will be obstacles in the arena.", 8239);
		}
		c.getPA().sendString("", 8240);
		c.getPA().sendString("", 8241);

		String[] rulesOption = { "Players cannot forfeit!", "Players cannot move.", "Players cannot use range.",
				"Players cannot use melee.", "Players cannot use magic.", "Players cannot drink pots.",
				"Players cannot eat food.", "Players cannot use prayer." };

		int lineNumber = 8242;
		for (int i = 0; i < 8; i++) {
			if (c.getInstance().duelRule[i]) {
				c.getPA().sendString("" + rulesOption[i], lineNumber);
				lineNumber++;
			}
		}
		c.getPA().sendString("", 6571);
		c.getPA().sendFrame248(SECOND_INTERFACE, 197);
	}

	public void declineDuel(Player c, boolean Throw, boolean r) {
		if (isDueling(c)) {
			return;
		}
		if (c.opponent != null && isDueling(c.opponent)) {
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
		c.getInstance().canOffer = true;
		removeFromFirstInterface(c);
		removeFromSecondInterface(c);
		removeFromDueling(c);
		c.getInstance().duelSpaceReq = 0;
		c.getInstance().duelRequested = false;
		for (GameItem item : c.getInstance().stakedItems) {
			if (item.amount < 1)
				continue;
			if (ItemLoader.isStackable(item.id) || ItemLoader.isNote(item.id)) {
				c.getItems().addItem(item.id, item.amount);
			} else {
				c.getItems().addItem(item.id, 1);
			}
		}
		c.getInstance().otherStakedItems.clear();
		c.getInstance().stakedItems.clear();
		resetDuelRules(c);
	}
	
	/**
	 * Starts a duel for the specified player.
	 * @param player The player who is dueling.
	 */
	public void startDuel(Player player) {
		/**
		 * If the opponent has been nulled for whatever reason, the player wins
		 * by default.
		 */
		if (player.opponent == null) {
			endDuel(player);
		}
		
		/**
		 * Set head-icon hints.
		 */
		player.getInstance().headIconHints = 2;
		
		/**
		 * If applicable, enforce the rule dictating that prayer may not be
		 * used.
		 */
		if (player.getInstance().duelRule[RULE_PRAYER]) {
			/**
			 * Iterate through prayers. If they're active, deactivate them, turn
			 * the glowing sprites off, and remove head-icons. Then request an
			 * appearance update.
			 */
			for (int p = 0; p < CombatPrayer.PRAYER.length; p++) {
				player.getInstance().prayerActive[p] = false;
				player.getPA().sendFrame36(CombatPrayer.PRAYER_GLOW[p], 0);
			}
			player.getInstance().headIcon = -1;
			player.getPA().requestUpdates();
		}
		
		/**
		 * If applicable, enforce the rule dictating that hats may not be
		 * used.
		 */
		if (player.getInstance().duelRule[RULE_HATS]) {
			player.getItems().removeItem(player.getInstance().playerEquipment[0], 0);
		}
		
		/**
		 * If applicable, enforce the rule dictating that capes may not be
		 * used.
		 */
		if (player.getInstance().duelRule[RULE_CAPES]) {
			player.getItems().removeItem(player.getInstance().playerEquipment[1], 1);
		}
		
		/**
		 * If applicable, enforce the rule dictating that amulets may not be
		 * used.
		 */
		if (player.getInstance().duelRule[RULE_AMULETS]) {
			player.getItems().removeItem(player.getInstance().playerEquipment[2], 2);
		}
		
		/**
		 * If applicable, enforce the rule dictating that weapons may not be
		 * used.
		 */
		if (player.getInstance().duelRule[RULE_WEAPONS]) {
			player.getItems().removeItem(player.getInstance().playerEquipment[3], 3);
		}
		
		/**
		 * If applicable, enforce the rule dictating that bodies may not be
		 * used.
		 */
		if (player.getInstance().duelRule[RULE_BODIES]) {
			player.getItems().removeItem(player.getInstance().playerEquipment[4], 4);
		}
		
		/**
		 * If applicable, enforce the rule dictating that shields may not be
		 * used.
		 * 
		 * Requires extra handling for two-handed weapons. Since they occupy
		 * both hands and are used to block, they are considered shields.
		 */
		if (player.getInstance().duelRule[RULE_SHIELDS]) {
			/**
			 * Remove the shield.
			 */
			player.getItems().removeItem(player.getInstance().playerEquipment[5], 5);
			
			/**
			 * Check if the weapon is two-handed. If it is, remove the weapon.
			 */
			if (player.getItems().is2handed(
					player.getItems().getItemName(player.getInstance().playerEquipment[3]).toLowerCase(),
					player.getInstance().playerEquipment[3])) {
				player.getItems().removeItem(player.getInstance().playerEquipment[3], 3);
			}
		}
		
		/**
		 * If applicable, enforce the rule dictating that legs may not be
		 * used.
		 */
		if (player.getInstance().duelRule[RULE_LEGS]) {
			player.getItems().removeItem(player.getInstance().playerEquipment[7], 7);
		}
		
		/**
		 * If applicable, enforce the rule dictating that gloves may not be
		 * used.
		 */
		if (player.getInstance().duelRule[RULE_GLOVES]) {
			player.getItems().removeItem(player.getInstance().playerEquipment[9], 9);
		}
		
		/**
		 * If applicable, enforce the rule dictating that boots may not be
		 * used.
		 */
		if (player.getInstance().duelRule[RULE_BOOTS]) {
			player.getItems().removeItem(player.getInstance().playerEquipment[10], 10);
		}
		
		/**
		 * If applicable, enforce the rule dictating that rings may not be
		 * used.
		 */
		if (player.getInstance().duelRule[RULE_RINGS]) {
			player.getItems().removeItem(player.getInstance().playerEquipment[12], 12);
		}
		
		/**
		 * If applicable, enforce the rule dictating that arrows may not be
		 * used.
		 */
		if (player.getInstance().duelRule[RULE_ARROWS]) {
			player.getItems().removeItem(player.getInstance().playerEquipment[13], 13);
		}
		
		/**
		 * Close all interfaces, you're about to fight to the death!
		 */
		player.getPA().removeAllWindows();
		
		/**
		 * Reset special energy.
		 */
		player.getInstance().specAmount = 10;
		player.getItems().addSpecialBar(player.getInstance().playerEquipment[player.getInstance().playerWeapon]);
		
		/**
		 * Rules for obstacles/movement are handled here, since they
		 * conveniently line up with the point at which we teleport the
		 * contestants to the arena.
		 */
		if (player.getInstance().duelRule[RULE_OBSTACLES]) {
			/**
			 * If both obstacles and no-movement are enabled, move the players
			 * to the appropriate position.
			 */
			if (player.getInstance().duelRule[RULE_MOVEMENT]) {
				player.getPA().movePlayer(player.getInstance().duelTeleX, player.getInstance().duelTeleY, 0);
			} else {
				player.getPA().movePlayer(3366 + Misc.random(12), 3246 + Misc.random(6), 0);
			}
			
		} else {
			/**
			 * If no-movement is enabled but obstacles are not, move the players
			 * to the appropriate position.
			 */
			if (player.getInstance().duelRule[RULE_MOVEMENT]) {
				player.getPA().movePlayer(player.getInstance().duelTeleX, player.getInstance().duelTeleY, 0);
			} else {
				player.getPA().movePlayer(3335 + Misc.random(12), 3246 + Misc.random(6), 0);
			}
			
		}
		
		/**
		 * Reset the freeze timer, the player isn't frozen just yet!
		 */
		player.getInstance().freezeTimer = 0;
		
		/**
		 * Reset prayers.
		 */
		CombatPrayer.resetPrayers(player);
		
		/**
		 * Boost their hitpoints up to maximum.
		 */
		player.getInstance().lifePoints = player.getInstance().maxLP();
		
		/**
		 * Refresh skills once more, now that they're in the arena.
		 */
		for (int i = 0; i < 20; i++) {
			player.getInstance().playerLevel[i] = player.getPA().getLevelForXP(player.getInstance().playerXP[i]);
			player.getPA().refreshSkill(i);
		}
		
		/**
		 * Reset combat-related variables.
		 */
		player.getCombat().resetPlayerAttack();
		
		/**
		 * Not skulled, since we're not in PvP.
		 */
		player.getInstance().isSkulled = false;
		
		/**
		 * Not tele-blocked, either.
		 */
		player.getPA().resetTb();
		
		/**
		 * Reset idle animation.
		 */
		player.getPA().resetAnimation();
		player.startAnimation(65535);
		
		/**
		 * Reset some more stuff.
		 */
		player.getInstance().attackedPlayers.clear();
		player.getInstance().headIconPk = -1;
		player.getInstance().skullTimer = -1;
		player.getInstance().damageTaken = new int[Constants.MAX_PLAYERS];
		
		player.getInstance().isFullHelm = ItemLoader
				.isFullHelm(player.opponent.getInstance().playerEquipment[player.getInstance().playerHat]);
		player.getInstance().isFullMask = ItemLoader
				.isFullMask(player.opponent.getInstance().playerEquipment[player.getInstance().playerHat]);
		player.getInstance().isFullBody = ItemLoader
				.isFullBody(player.opponent.getInstance().playerEquipment[player.getInstance().playerChest]);
		
		/**
		 * Add the head-icon hint to the opponent.
		 */
		player.getPA().createPlayerHints(10, player.opponent.playerId);
		
		/**
		 * Add the player to the duel they're going to fight.
		 */
		addToDuel(player);
		
		/**
		 * Refresh skills.
		 */
		for (int i = 0; i < 20; i++) {
			player.getInstance().playerLevel[i] = player.getPA().getLevelForXP(player.getInstance().playerXP[i]);
			player.getPA().refreshSkill(i);
		}
		
		/**
		 * Populate the player's otherStakedItems list the the opponent's staked items.
		 */
		for (GameItem item : player.opponent.getInstance().stakedItems) {
			player.getInstance().otherStakedItems.add(new GameItem(item.id, item.amount));
		}
		
		/**
		 * More resetting.
		 */
		player.getItems().sendWeapon(player.getInstance().playerEquipment[player.getInstance().playerWeapon],
				player.getItems().getItemName(player.getInstance().playerEquipment[player.getInstance().playerWeapon]));
		
		player.getItems().resetBonus();
		player.getItems().getBonus();
		player.getItems().writeBonus();
		
		player.getCombat().getPlayerAnimIndex(player.getItems()
				.getItemName(player.getInstance().playerEquipment[player.getInstance().playerWeapon]).toLowerCase());
		
		player.getPA().requestUpdates();
		
		player.getInstance().castVengeance = 0;
		player.getInstance().vengOn = false;
	}
	
	/**
	 * Handles a duel's conclucion for the specified player.
	 * 
	 * @param c
	 *            The player who has won the duel.
	 */
	public void endDuel(final Player player) {
		
		/**
		 * Reset interfaces.
		 */
		if (player.opponent != null) {
			player.getPA().sendString("" + player.opponent.calculateCombatLevel(), 6839);
			player.getPA().sendString(player.opponent.getDisplayName(), 6840);
		} else {
			player.getPA().sendString("", 6839);
			player.getPA().sendString("", 6840);
		}
		
		/**
		 * Teleport them to the winner location.
		 */
		player.getPA().movePlayer(WINNER_X_COORD, WINNER_Y_COORD, 0);
		
		/**
		 * Turn off their prayers.
		 */
		CombatPrayer.resetPrayers(player);
		
		/**
		 * Reset any boosted stats to the level dictated by the amount of XP
		 * they have.
		 */
		for (int i = 0; i < 20; i++) {
			player.getInstance().playerLevel[i] = player.getPA().getLevelForXP(player.getInstance().playerXP[i]);
			player.getPA().refreshSkill(i);
		}
		
		/**
		 * Reset Vengeance.
		 */
		player.getInstance().castVengeance = 0;
		player.getInstance().vengOn = false;
		
		/**
		 * Refresh their hitpoints. 
		 * 
		 * TODO unneeded? All skills are already refreshed above...
		 */
		player.getPA().refreshSkill(3);
		
		/**
		 * Populate & display the dueling reward interface.
		 */
		if (!player.getInstance().autoGive)
			getDuelRewards(player);
		player.getPA().showInterface(REWARD_INTERFACE);
		
		/**
		 * Request apperance updates.
		 */
		player.getPA().requestUpdates();
		
		/**
		 * Display relevant options.
		 */
		player.getPA().showOption(3, 0, "Challenge", 3);
		
		/**
		 * Reset head-icon hints.
		 */
		player.getPA().createPlayerHints(10, -1);
		
		/**
		 * The player is permitted to send offers, and has a space requirement
		 * of 0
		 */
		player.getInstance().canOffer = true;
		player.getInstance().duelSpaceReq = 0;
		
		/**
		 * Reset combat-related variables.
		 */
		player.getCombat().resetPlayerAttack();
		
		/**
		 * A duel has just been won -- the player has NOT requested a duel.
		 */
		player.getInstance().duelRequested = false;
		
		/**
		 * Schedule a new task that will give the player their items.
		 */
		GameEngine.getScheduler().schedule(new Task(1) {
			@Override
			public void execute() {
				/**
				 * Iterate through the staked items.
				 */
				for (GameItem item : player.getInstance().stakedItems) {
					/**
					 * If neither the ID nor the amount is null...
					 */
					if (item.id > 0 && item.amount > 0) {
						/**
						 * And if the item is stackable...
						 */
						if (ItemLoader.isStackable(item.id)) {
							/**
							 * Then add the items to the inventory.
							 */
							player.getInventory().add(item.id, item.amount);
						} else {
							/**
							 * If the item isn't stackable, then we need to
							 * begin taking amounts into consideration.
							 */
							int amount = item.amount;
							for (int a = 1; a <= amount; a++) {
								/**
								 * Attempt to add the item to the inventory, but
								 * if unsuccessful...
								 */
								if (!player.getItems().addItem(item.id, 1)) {
									/**
									 * Then register the item as a ground item
									 * for our player.
									 */
									ItemHandler.createGroundItem(player, item.id, player.getX(), player.getY(),
											player.heightLevel, 1, player.getId());
								}
							}
						}
					}
				}
				/**
				 * Clear staked items, because there are none, and kill the
				 * task.
				 */
				player.getInstance().stakedItems.clear();
				this.stop();
			}
		});
		
		/**
		 * If the player should receive their winnings automatically, do so.
		 * 
		 * Note: autoGive is set to true upon an unexpected socket closure (i.e.
		 * a disconnection). We want to make sure the player gets their items
		 * ASAP, should such an event occur.
		 */
		if (player.getInstance().autoGive)
			claimDuelRewards(player);
	}
	
	/**
	 * Populates the duel reward interface for the specified player.
	 * 
	 * @param player
	 *            The player to which this interface belongs.
	 */
	public void getDuelRewards(Player player) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrameVarSizeWord(53);
			player.getOutStream().writeWord(6822);
			/**
			 * Populate the interface with the OTHER player's staked items --
			 * because those are the ones that they win :P
			 */
			player.getOutStream().writeWord(player.getInstance().otherStakedItems.toArray().length);

			for (GameItem item : player.getInstance().otherStakedItems) {
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
			}
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();
		}
	}
	
	public void claimDuelRewards(Player player) {
		for (GameItem item : player.getInstance().otherStakedItems) {
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
		removeFromDueling(player);
		removeFromFirstInterface(player);
		removeFromSecondInterface(player);
		player.getInstance().killedDuelOpponent = false;
		resetDuel(player);
		resetDuelItems(player);
	}
	
	/**
	 * This is the method that removes a player's acceptance of a duel if
	 * anything changes (i.e. a rule being enabled/disabled or a staked item
	 * being added/removed).
	 * 
	 * @param player
	 *            The player whose acceptance should be removed.
	 */
	public void removeDuelConsent(Player player) {
		if (player.opponent == null) {
			return;
		}
		
		player.getInstance().acceptedFirst = false;
		player.opponent.getInstance().acceptedFirst = false;
		
		player.opponent.getPA().sendString("", 6684);
		player.getPA().sendString("", 6684);
	}
	
	/**
	 * Handles adding an item into the staking interface (the first dueling
	 * interface).
	 * 
	 * @param itemID
	 *            The ID of the item being added.
	 * @param fromSlot
	 *            The slot from which the item is taken.
	 * @param amount
	 *            The amount of the item to add.
	 * @param player
	 *            The player who is adding the item.
	 * 
	 * @return True if the item was added, false otherwise.
	 */
	public boolean addStakedItem(int itemID, int fromSlot, int amount, Player player) {
		/**
		 * Note:
		 * 
		 * It's very important that this method works correctly and leaves no
		 * possibility for duplication or loss of items.
		 * 
		 * The Duel Arena is a sought-after and integral feature on any economy
		 * server. If the Duel Arena has problems, players very quickly lose
		 * interest in the game, and faith in the developers.
		 * 
		 * Keeping this in mind, we move onto a whole slew of checks...
		 */

		/**
		 * ID check. If the IDs don't match, something's up.
		 */
		if (itemID != player.getInstance().playerItems[fromSlot] - 1) {
			return false;
		}

		/**
		 * ArrayList check. If the player isn't supposed to be using this
		 * interface, something's up.
		 */
		if (!isInFirstInterface(player)) {
			return false;
		}

		if (isInSecondInterface(player)) {
			return false;
		}

		if (isDueling(player)) {
			return false;
		}

		/**
		 * If the item isn't able to be traded, then it isn't able to be staked.
		 */
		if (!player.getTradeHandler().tradeable(itemID)) {
			player.sendMessage("You can't stake this item.");
			return false;
		}

		/**
		 * This is a big one. If the player doesn't actually HAVE the item,
		 * something's up.
		 */
		if (!player.getItems().playerHasItem(itemID, amount))
			return false;

		/**
		 * If the player wants to put in a zero or negative amount of items,
		 * nah. That's not allowed.
		 */
		if (amount <= 0) {
			return false;
		}

		/**
		 * If the player doesn't have an opponent, something's up.
		 */
		if (player.opponent == null) {
			declineDuel(player, false, false);
			return false;
		}

		/**
		 * If the player shouldn't be able to send duel offers, something's up.
		 */
		if (!player.getInstance().canOffer) {
			return false;
		}
		
		/**
		 * If a staked item has been added or removed, a previous acceptance of
		 * duel terms should be redacted.
		 */
		removeDuelConsent(player);
		
		/**
		 * Add the items.
		 */
		if (!ItemLoader.isStackable(itemID)) {
			for (int a = 0; a < amount; a++) {
				if (player.getItems().playerHasItem(itemID, 1)) {
					player.getInstance().stakedItems.add(new GameItem(itemID, 1));
					player.getItems().deleteItem(itemID, player.getItems().getItemSlot(itemID), 1);
				}
			}
			
			/**
			 * And refresh.
			 */
			player.getItems().resetItems(3214);
			player.getItems().resetItems(3322);
			
			player.opponent.getItems().resetItems(3214);
			player.opponent.getItems().resetItems(3322);
			
			refreshDuelScreen(player);
			refreshDuelScreen(player.opponent);
			
			player.getPA().sendString("", 6684);
			player.opponent.getPA().sendString("", 6684);
		}
		
		/**
		 * TODO unneeded? This is already checked above. What is the point of
		 * checking, if the items have already been added?
		 */
		if (!player.getItems().playerHasItem(itemID, amount)) {
			return false;
		}
		
		/**
		 * Remove the items from the player's inventory.
		 */
		if (ItemLoader.isStackable(itemID) || ItemLoader.isNote(itemID)) {
			boolean found = false;
			/**
			 * This is done in an interesting way. We iterate through the
			 * player's items. If the item's ID matches the ID of the item that
			 * was staked, we mark <found> as true. 
			 * 
			 * Then we can re-use the method's own variables when we make the call
			 * to deleteItem(). Pretty cool.
			 */
			for (GameItem item : player.getInstance().stakedItems) {
				if (item.id == itemID) {
					found = true;
					item.amount += amount;
					player.getItems().deleteItem(itemID, fromSlot, amount);
					break;
				}
			}
			
			if (!found) {
				/**
				 * If we've not found the item, delete it first, then add it to
				 * the staking screen as a new <GameItem>.
				 */
				player.getItems().deleteItem(itemID, fromSlot, amount);
				player.getInstance().stakedItems.add(new GameItem(itemID, amount));
			}
		}
		
		/**
		 * And the ubiquitous refreshes.
		 */
		player.getItems().resetItems(3214);
		player.getItems().resetItems(3322);
		
		player.opponent.getItems().resetItems(3214);
		player.opponent.getItems().resetItems(3322);
		
		refreshDuelScreen(player);
		refreshDuelScreen(player.opponent);
		
		player.getPA().sendString("", 6684);
		player.opponent.getPA().sendString("", 6684);
		return true;
	}
	
	/**
	 * Handles removing an item from the staking interface (the first dueling
	 * interface).
	 * 
	 * @param itemID
	 *            The ID of the item being removed.
	 * @param fromSlot
	 *            The slot into which the item is placed.
	 * @param amount
	 *            The amount of the item to remove.
	 * @param player
	 *            The player who is removing the item.
	 * 
	 * @return True if the item was removed., false otherwise.
	 */
	public boolean removeStakedItem(int itemID, int fromSlot, int amount, Player player) {
		/**
		 * If they're not in the interface at which operations on staked items
		 * are allowed, don't allow the removal.
		 */
		if (!isInFirstInterface(player)) {
			return false;
		}
		
		/**
		 * If they're in the second interface, they shouldn't be allowed to take
		 * items out. Both players have already given their consent in regards
		 * to the stake.
		 */
		if (isDueling(player) || isInSecondInterface(player)) {
			return false;
		}
		
		/**
		 * Null check, of course.
		 */
		if (player.opponent == null) {
			declineDuel(player, false, false);
			return false;
		}
		
		/**
		 * Ensure that the future act of a duel rule removing a piece of
		 * equipment won't conflict with immediate act of removing a staked
		 * item, space-wise.
		 */
		if (ItemLoader.isStackable(itemID)) {
			if (player.getItems().freeSlots() - 1 < (player.getInstance().duelSpaceReq)) {
				player.sendMessage("You have too many rules set to remove that item.");
				return false;
			}
		}
		
		/**
		 * Ensure the player is allowed to send duel offers.
		 */
		if (!player.getInstance().canOffer) {
			return false;
		}

		/**
		 * Reset duel consent.
		 */
		removeDuelConsent(player);
		
		/**
		 * Long, complicated, nested for-loop that checks to make sure we have
		 * enough space for the item we're removing.
		 */
		boolean goodSpace = true;
		
		if (!ItemLoader.isStackable(itemID)) {
			
			for (int a = 0; a < amount; a++) {
				
				for (GameItem item : player.getInstance().stakedItems) {
					
					if (item.id == itemID) {
						
						if (!item.stackable) {
							
							if (player.getItems().freeSlots() - 1 < (player.getInstance().duelSpaceReq)) {
								goodSpace = false;
								break;
							}
							
							player.getInstance().stakedItems.remove(item);
							player.getItems().addItem(itemID, 1);
							
						} else {
							if (player.getItems().freeSlots() - 1 < (player.getInstance().duelSpaceReq)) {
								goodSpace = false;
								break;
							}
							
							if (item.amount > amount) {
								item.amount -= amount;
								player.getItems().addItem(itemID, amount);
								
							} else {
								
								if (player.getItems().freeSlots() - 1 < (player.getInstance().duelSpaceReq)) {
									goodSpace = false;
									break;
								}
								
								amount = item.amount;
								player.getInstance().stakedItems.remove(item);
								player.getItems().addItem(itemID, amount);
							}
						}
						break;
					}
					
					/**
					 * Resetting. Always the resetting.
					 */
					player.getItems().resetItems(3214);
					player.getItems().resetItems(3322);
					
					player.opponent.getItems().resetItems(3214);
					player.opponent.getItems().resetItems(3322);
					
					refreshDuelScreen(player);
					refreshDuelScreen(player.opponent);
					
					player.opponent.getPA().sendString("", 6684);
				}
			}
		}
		
		/**
		 * And add them to the inventory.
		 */
		for (GameItem item : player.getInstance().stakedItems) {
			
			if (item.id == itemID) {
				
				if (!item.stackable) {
					//TODO err .. return?
				} else {
					
					if (item.amount > amount) {
						item.amount -= amount;
						player.getItems().addItem(itemID, amount);
						
					} else {
						
						amount = item.amount;
						player.getInstance().stakedItems.remove(item);
						player.getItems().addItem(itemID, amount);
					}
				}
				break;
			}
		}
		
		/**
		 * Resetting.
		 */
		player.getItems().resetItems(3214);
		player.getItems().resetItems(3322);
		
		player.opponent.getItems().resetItems(3214);
		player.opponent.getItems().resetItems(3322);
		
		refreshDuelScreen(player);
		refreshDuelScreen(player.opponent);
		
		player.opponent.getPA().sendString("", 6684);
		
		/**
		 * If they don't have enough space, warn them.
		 */
		if (!goodSpace) {
			player.sendMessage("You have too many rules set to remove that item.");
			return true;
		}
		
		return true;
	}
	
	/**
	 * Handles the actions that must be taken when a player enables or disables
	 * a duel rule.
	 * 
	 * @param i
	 *            The ID of the rule.
	 * @param player
	 *            The player who has enabled the rule.
	 */
	public void toggleDuelRule(int i, Player player) {
		
		/**
		 * Checks.
		 */
		if (player.opponent == null) {
			return;
		}
		
		if (!player.getInstance().canOffer) {
			return;
		}
		
		/**
		 * Resets.
		 */
		player.getPA().sendString("", 6684);
		player.opponent.getPA().sendString("", 6684);
		
		player.getInstance().acceptedFirst = false;
		player.opponent.getInstance().acceptedFirst = false;
		
		removeDuelConsent(player);
		
		/**
		 * Handles computing space requirements. <duelSlot> represents the
		 * equipment slot that is disabled by a duel rule.
		 */
		player.opponent.getInstance().duelSlot = player.getInstance().duelSlot;
		
		if (i >= 11 && player.getInstance().duelSlot > -1) {
			
			if (player.getInstance().playerEquipment[player.getInstance().duelSlot] > 0) {
				if (!player.getInstance().duelRule[i]) {
					player.getInstance().duelSpaceReq++;
				} else {
					player.getInstance().duelSpaceReq--;
				}
			}
			
			if (player.opponent.getInstance().playerEquipment[player.opponent.getInstance().duelSlot] > 0) {
				
				if (!player.opponent.getInstance().duelRule[i]) {
					player.opponent.getInstance().duelSpaceReq++;
					
				} else {
					
					player.opponent.getInstance().duelSpaceReq--;
				}
			}
		}

		/**
		 * If either player doesn't have free space enough to hold the item that
		 * will be removed from their equipment, tell them so and don't
		 * continue.
		 */
		if (i >= 11) {
			if (player.getItems().freeSlots() < (player.getInstance().duelSpaceReq)
					|| player.opponent.getItems().freeSlots() < (player.opponent.getInstance().duelSpaceReq)) {
				
				player.sendMessage("You or your opponent don't have the required space to set this rule.");
				
				if (player.getInstance().playerEquipment[player.getInstance().duelSlot] > 0) {
					player.getInstance().duelSpaceReq--;
				}
				
				if (player.opponent.getInstance().playerEquipment[player.opponent.getInstance().duelSlot] > 0) {
					player.opponent.getInstance().duelSpaceReq--;
				}
				return;
			}
		}
		
		/**
		 * Set the rule.
		 */
		if (!player.getInstance().duelRule[i]) {
			player.getInstance().duelRule[i] = true;
			player.getInstance().duelOption += player.getInstance().DUEL_RULE_ID[i];
		} else {
			player.getInstance().duelRule[i] = false;
			player.getInstance().duelOption -= player.getInstance().DUEL_RULE_ID[i];
		}
		
		/**
		 * Refresh the interface to reflect the rule that was changed.
		 */
		player.getPA().sendFrame87(286, player.getInstance().duelOption);
		player.opponent.getInstance().duelOption = player.getInstance().duelOption;
		player.opponent.getInstance().duelRule[i] = player.getInstance().duelRule[i];
		player.opponent.getPA().sendFrame87(286, player.opponent.getInstance().duelOption);
		
		/**
		 * Handle obstacles/movement and set relevant coordinates.
		 */
		if (player.getInstance().duelRule[RULE_OBSTACLES]) {
			
			if (player.getInstance().duelRule[RULE_MOVEMENT]) {
				player.getInstance().duelTeleX = 3366 + Misc.random(12);
				player.opponent.getInstance().duelTeleX = player.getInstance().duelTeleX - 1;
				
				player.getInstance().duelTeleY = 3246 + Misc.random(6);
				player.opponent.getInstance().duelTeleY = player.getInstance().duelTeleY;
			}
			
		} else {
			
			if (player.getInstance().duelRule[RULE_MOVEMENT]) {
				player.getInstance().duelTeleX = 3335 + Misc.random(12);
				player.opponent.getInstance().duelTeleX = player.getInstance().duelTeleX - 1;
				
				player.getInstance().duelTeleY = 3246 + Misc.random(6);
				player.opponent.getInstance().duelTeleY = player.getInstance().duelTeleY;
			}
		}

	}
	
	public void refreshDuelRules(Player player) {
		for (int i = 0; i < player.getInstance().duelRule.length; i++) {
			player.getInstance().duelRule[i] = false;
		}
		/**
		 * TODO what are these? They're the only things that cause this method
		 * and the one below it to differ.
		 */
		player.getPA().sendFrame87(286, 0);
		player.getInstance().duelOption = 0;
	}
	
	/**
	 * Sets all duel rules to their default value (false/off).
	 * 
	 * @param player
	 *            The player whose duel rules are to be reset.
	 */
	public void resetDuelRules(Player player) {
		for (int i = 0; i < player.getInstance().duelRule.length; i++) {
			player.getInstance().duelRule[i] = false;
		}
	}


	public void refreshDuelScreen(Player player) {
		synchronized (player) {
			if (player.opponent == null) {
				return;
			}
			player.getOutStream().createFrameVarSizeWord(53);
			player.getOutStream().writeWord(6669);
			player.getOutStream().writeWord(player.getInstance().stakedItems.toArray().length);
			int current = 0;
			for (GameItem item : player.getInstance().stakedItems) {
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
			player.getOutStream().writeWord(player.opponent.getInstance().stakedItems.toArray().length);
			current = 0;
			for (GameItem item : player.opponent.getInstance().stakedItems) {
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

	public void getDuelEquipment(int itemId, int amount, int slot, Player player) {
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
	
	/**
	 * Resets all duel-related fields to their default value.
	 * 
	 * @param player
	 *            The player being reset.
	 */
	public void resetDuel(Player player) {
		/**
		 * Send the appropriate right-click option.
		 */
		player.getPA().showOption(3, 0, "Challenge", 3);
		
		/**
		 * Reset the head-icon hint.
		 */
		player.getInstance().headIconHints = 0;
		
		/**
		 * Iterate through all the duel rules and set them false.
		 */
		resetDuelRules(player);
		
		/**
		 * Remove the opponent's head-icon hint.
		 */
		player.getPA().createPlayerHints(10, -1);
		
		/**
		 * The player can now send offers. The player now has an inventory space
		 * requirement of 0 for staking.
		 */
		player.getInstance().canOffer = true;
		player.getInstance().duelSpaceReq = 0;
		
		/**
		 * Request appearance updates.
		 */
		player.getPA().requestUpdates();
		
		/**
		 * Reset combat-related variables.
		 */
		player.getCombat().resetPlayerAttack();
		
		/**
		 * A duel has just concluded -- the player has NOT sent a request to
		 * anybody.
		 */
		player.getInstance().duelRequested = false;
		
		/**
		 * If the player's opponent isn't already null, make it so. The player
		 * no longer has an opponent.
		 */
		if (player.opponent != null) {
			player.opponent.opponent = null;
		}
		
		/**
		 * Remove the player from all relevant ArrayLists.
		 */
		removeFromDueling(player);
		removeFromFirstInterface(player);
		removeFromSecondInterface(player);
		
		player.getInstance().killedDuelOpponent = false;
		player.opponent = null;
	}
	
	/**
	 * Clears the staking interface of items.
	 * 
	 * @param player
	 *            The player whose interface to clear.
	 */
	public void resetDuelItems(Player player) {
		player.getInstance().stakedItems.clear();
		player.getInstance().otherStakedItems.clear();
	}

}
