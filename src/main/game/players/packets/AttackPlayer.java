package main.game.players.packets;

import main.Constants;
import main.game.players.PacketType;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.game.players.content.minigames.impl.dueling.DuelPlayer;
import main.game.players.content.skills.dungeoneering.Dungeon;
import main.handlers.Following;

/**
 * Attack Player
 **/
public class AttackPlayer implements PacketType {

	public static final int ATTACK_PLAYER = 73, MAGE_PLAYER = 249;

	@SuppressWarnings("static-access")
	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		c.getVariables().playerIndex = 0;
		c.getVariables().npcIndex = 0;
		if (c.getVariables().resting) {
			c.getPA().resetRest();
		}

		switch (packetType) {

		/**
		 * Attack player
		 **/
		case ATTACK_PLAYER:
			c.getVariables().playerIndex = c.getInStream().readSignedWordBigEndian();
			if (c.getVariables().teleTimer > 0) {
				c.getVariables().playerIndex = -1;
				return;
			}
			if (c.getVariables().playerIndex > PlayerHandler.players.length) {
				c.getVariables().playerIndex = -1;
				return;
			}
			if (PlayerHandler.players[c.getVariables().playerIndex] == null) {
				break;
			}
			if (c.party != null) {
				Dungeon.inviteMember(c.getVariables().playerIndex, c);
				c.getVariables().playerIndex = 0;
				return;
			}
			if (c.getVariables().respawnTimer > 0) {
				break;
			}

			if (c.getVariables().autocastId > 0)
				c.getVariables().autocasting = true;

			if (!c.getVariables().autocasting && c.getVariables().spellId > 0) {
				c.getVariables().spellId = 0;
			}
			c.getVariables().mageFollow = false;
			c.getVariables().spellId = 0;
			c.getVariables().usingMagic = false;
			boolean usingBow = false;
			boolean usingOtherRangeWeapons = false;
			boolean usingArrows = false;
			boolean usingCross = c.getVariables().playerEquipment[c.getVariables().playerWeapon] == 9185
					|| c.getVariables().playerEquipment[c.getVariables().playerWeapon] == 18357;
			for (int bowId : c.getVariables().BOWS) {
				if (c.getVariables().playerEquipment[c.getVariables().playerWeapon] == bowId) {
					usingBow = true;
					for (int arrowId : c.getVariables().ARROWS) {
						if (c.getVariables().playerEquipment[c.getVariables().playerArrows] == arrowId) {
							usingArrows = true;
						}
					}
				}
			}
			for (int otherRangeId : c.getVariables().OTHER_RANGE_WEAPONS) {
				if (c.getVariables().playerEquipment[c.getVariables().playerWeapon] == otherRangeId) {
					usingOtherRangeWeapons = true;
				}
			}
			if (DuelPlayer.contains(c)) {
				if (c.getVariables().duelCount > 0) {
					c.sendMessage("The duel hasn't started yet!");
					c.getVariables().playerIndex = 0;
					return;
				}
				if (c.getVariables().duelRule[DuelPlayer.RULE_FUN_WEAPONS]) {
					boolean canUseWeapon = false;
					for (int funWeapon : Constants.FUN_WEAPONS) {
						if (c.getVariables().playerEquipment[c.getVariables().playerWeapon] == funWeapon) {
							canUseWeapon = true;
						}
					}
					if (!canUseWeapon) {
						c.sendMessage("You can only use fun weapons in this duel!");
						return;
					}
				}

				if (c.getVariables().duelRule[DuelPlayer.RULE_RANGED] && (usingBow || usingOtherRangeWeapons)) {
					c.sendMessage("Range has been disabled in this duel!");
					return;
				}
				if (c.getVariables().duelRule[DuelPlayer.RULE_MELEE] && (!usingBow && !usingOtherRangeWeapons)) {
					c.sendMessage("Melee has been disabled in this duel!");
					return;
				}
			}

			if ((usingBow || c.getVariables().autocasting)
					&& c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[c.getVariables().playerIndex].getX(),
							PlayerHandler.players[c.getVariables().playerIndex].getY(), 6)) {
				c.getVariables().usingBow = true;
				c.stopMovement();
			}

			if (usingOtherRangeWeapons
					&& c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[c.getVariables().playerIndex].getX(),
							PlayerHandler.players[c.getVariables().playerIndex].getY(), 3)) {
				c.getVariables().usingRangeWeapon = true;
				c.stopMovement();
			}
			if (!usingBow)
				c.getVariables().usingBow = false;
			if (!usingOtherRangeWeapons)
				c.getVariables().usingRangeWeapon = false;

			if (!usingCross && !usingArrows && usingBow
					&& c.getVariables().playerEquipment[c.getVariables().playerWeapon] < 4212
					&& c.getVariables().playerEquipment[c.getVariables().playerWeapon] > 4223) {
				c.sendMessage("You have run out of arrows!");
				return;
			}
			if (c.getCombat().correctBowAndArrows() < c.getVariables().playerEquipment[c.getVariables().playerArrows]
					&& Constants.CORRECT_ARROWS && usingBow && !c.getCombat().usingCrystalBow() && !usingCross) {
				c.sendMessage("You can't use "
						+ c.getItems().getItemName(c.getVariables().playerEquipment[c.getVariables().playerArrows])
								.toLowerCase()
						+ " with a "
						+ c.getItems().getItemName(c.getVariables().playerEquipment[c.getVariables().playerWeapon])
								.toLowerCase()
						+ ".");
				c.stopMovement();
				c.getCombat().resetPlayerAttack();
				return;
			}
			if (usingCross && !c.getCombat().properBolts()) {
				c.sendMessage("You must use bolts with a crossbow.");
				c.stopMovement();
				c.getCombat().resetPlayerAttack();
				return;
			}
			if (c.getCombat().checkReqs()) {
				if (!c.getVariables().usingMagic && !usingBow && !usingOtherRangeWeapons) {
					Following.triggerFollowing(c.getVariables().playerIndex, 0, c);
					c.getVariables().followDistance = 1;
				}
				if (c.getVariables().attackTimer <= 0) {
					// c.sendMessage("Tried to attack...");
					// c.getCombat().attackPlayer(c.playerIndex);
					// c.attackTimer++;
				}
			}
			break;

		/**
		 * Attack player with magic
		 **/
		case MAGE_PLAYER:
			if (!c.getVariables().mageAllowed) {
				c.getVariables().mageAllowed = true;
				break;
			}
			c.getPA().walkTo(0, 0);
			// c.usingSpecial = false;
			// c.getItems().updateSpecialBar();

			c.getVariables().playerIndex = c.getInStream().readSignedWordA();
			if (c.getVariables().teleTimer > 0) {
				c.getVariables().playerIndex = -1;
				return;
			}
			if (c.getVariables().playerIndex > PlayerHandler.players.length) {
				c.getVariables().playerIndex = -1;
				return;
			}
			int castingSpellId = c.getInStream().readSignedWordBigEndian();
			c.getVariables().usingMagic = false;
			if (PlayerHandler.players[c.getVariables().playerIndex] == null) {
				break;
			}
			if (c.getVariables().respawnTimer > 0) {
				break;
			}

			for (int i = 0; i < c.getVariables().MAGIC_SPELLS.length; i++) {
				if (castingSpellId == c.getVariables().MAGIC_SPELLS[i][0]) {
					c.getVariables().spellId = i;
					c.getVariables().usingMagic = true;
					break;
				}
			}

			if (c.getVariables().autocasting)
				c.getVariables().autocasting = false;

			if (!c.getCombat().checkReqs()) {
				break;
			}
			if (DuelPlayer.contains(c)) {
				if (c.getVariables().duelCount > 0) {
					c.sendMessage("The duel hasn't started yet!");
					c.getVariables().playerIndex = 0;
					return;
				}
				if (c.getVariables().duelRule[DuelPlayer.RULE_MAGIC]) {
					c.sendMessage("Magic has been disabled in this duel!");
					return;
				}
			}

			for (int r = 0; r < c.getVariables().REDUCE_SPELLS.length; r++) { // reducing
																				// spells,
																				// confuse
																				// etc
				if (PlayerHandler.players[c.getVariables().playerIndex].getVariables().REDUCE_SPELLS[r] == c
						.getVariables().MAGIC_SPELLS[c.getVariables().spellId][0]) {
					if ((System.currentTimeMillis() - PlayerHandler.players[c.getVariables().playerIndex]
							.getVariables().reduceSpellDelay[r]) < PlayerHandler.players[c.getVariables().playerIndex]
									.getVariables().REDUCE_SPELL_TIME[r]) {
						c.sendMessage("That player is currently immune to this spell.");
						c.getVariables().usingMagic = false;
						c.stopMovement();
						c.getCombat().resetPlayerAttack();
					}
					break;
				}
			}

			if (System.currentTimeMillis()
					- PlayerHandler.players[c.getVariables().playerIndex]
							.getVariables().teleBlockDelay < PlayerHandler.players[c.getVariables().playerIndex]
									.getVariables().teleBlockLength
					&& c.getVariables().MAGIC_SPELLS[c.getVariables().spellId][0] == 12445) {
				c.sendMessage("That player is already affected by this spell.");
				c.getVariables().usingMagic = false;
				c.stopMovement();
				c.getCombat().resetPlayerAttack();
			}

			/*
			 * if(!c.getCombat().checkMagicReqs(c.spellId)) { c.stopMovement();
			 * c.getCombat().resetPlayerAttack(); break; }
			 */

			if (c.getVariables().usingMagic) {
				if (c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[c.getVariables().playerIndex].getX(),
						PlayerHandler.players[c.getVariables().playerIndex].getY(), 7)) {
					c.stopMovement();
				}
				if (c.getCombat().checkReqs()) {
					Following.triggerFollowing(c.playerIndex, 0, c);
					c.getVariables().mageFollow = true;
					if (c.getVariables().attackTimer <= 0) {
						// c.getCombat().attackPlayer(c.playerIndex);
						// c.attackTimer++;
					}
				}
			}
			break;

		}

	}

}
