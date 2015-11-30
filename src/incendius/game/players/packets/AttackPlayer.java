package incendius.game.players.packets;

import incendius.Constants;
import incendius.game.players.PacketType;
import incendius.game.players.Player;
import incendius.game.players.PlayerHandler;
import incendius.game.players.content.minigames.DuelArena;
import incendius.game.players.content.skills.dungeoneering.Dungeon;
import incendius.handlers.Following;

/**
 * Attack Player
 * 
 * has duplicate code! check with CombatAssistant.java and fix.
 * 
 **/
public class AttackPlayer implements PacketType {

	public static final int ATTACK_PLAYER = 73, MAGE_PLAYER = 249;

	@SuppressWarnings("static-access")
	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		c.getInstance().playerIndex = 0;
		c.getInstance().npcIndex = 0;
		if (c.getInstance().resting) {
			c.getPA().resetRest();
		}

		switch (packetType) {

		/**
		 * Attack player
		 **/
		case ATTACK_PLAYER:
			c.getInstance().playerIndex = c.getInStream().readSignedWordBigEndian();
			if (c.getInstance().teleTimer > 0) {
				c.getInstance().playerIndex = -1;
				return;
			}
			if (c.getInstance().playerIndex > PlayerHandler.players.length) {
				c.getInstance().playerIndex = -1;
				return;
			}
			if (PlayerHandler.players[c.getInstance().playerIndex] == null) {
				break;
			}
			if (c.party != null) {
				Dungeon.inviteMember(c.getInstance().playerIndex, c);
				c.getInstance().playerIndex = 0;
				return;
			}
			if (c.getInstance().respawnTimer > 0) {
				break;
			}

			if (c.getInstance().autocastId > 0)
				c.getInstance().autocasting = true;

			if (!c.getInstance().autocasting && c.getInstance().spellId > 0) {
				c.getInstance().spellId = 0;
			}
			c.getInstance().mageFollow = false;
			c.getInstance().spellId = 0;
			c.getInstance().usingMagic = false;
			boolean usingBow = false;
			boolean usingOtherRangeWeapons = false;
			boolean usingArrows = false;
			boolean usingCross = c.getInstance().playerEquipment[c.getInstance().playerWeapon] == 9185
					|| c.getInstance().playerEquipment[c.getInstance().playerWeapon] == 18357;
			for (int bowId : c.getInstance().BOWS) {
				if (c.getInstance().playerEquipment[c.getInstance().playerWeapon] == bowId) {
					usingBow = true;
					for (int arrowId : c.getInstance().ARROWS) {
						if (c.getInstance().playerEquipment[c.getInstance().playerArrows] == arrowId) {
							usingArrows = true;
						}
					}
				}
			}
			for (int otherRangeId : c.getInstance().OTHER_RANGE_WEAPONS) {
				if (c.getInstance().playerEquipment[c.getInstance().playerWeapon] == otherRangeId) {
					usingOtherRangeWeapons = true;
				}
			}
			if (DuelArena.isDueling(c)) {
				if (c.getInstance().duelCount > 0) {
					c.sendMessage("The duel hasn't started yet!");
					c.getInstance().playerIndex = 0;
					return;
				}
				if (c.getInstance().duelRule[DuelArena.RULE_FUN_WEAPONS]) {
					boolean canUseWeapon = false;
					for (int funWeapon : Constants.FUN_WEAPONS) {
						if (c.getInstance().playerEquipment[c.getInstance().playerWeapon] == funWeapon) {
							canUseWeapon = true;
						}
					}
					if (!canUseWeapon) {
						c.sendMessage("You can only use fun weapons in this duel!");
						return;
					}
				}

				if (c.getInstance().duelRule[DuelArena.RULE_RANGED] && (usingBow || usingOtherRangeWeapons)) {
					c.sendMessage("Range has been disabled in this duel!");
					return;
				}
				if (c.getInstance().duelRule[DuelArena.RULE_MELEE] && (!usingBow && !usingOtherRangeWeapons)) {
					c.sendMessage("Melee has been disabled in this duel!");
					return;
				}
			}

			if ((usingBow || c.getInstance().autocasting)
					&& c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[c.getInstance().playerIndex].getX(),
							PlayerHandler.players[c.getInstance().playerIndex].getY(), 6)) {
				c.getInstance().usingBow = true;
				c.stopMovement();
			}

			if (usingOtherRangeWeapons
					&& c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[c.getInstance().playerIndex].getX(),
							PlayerHandler.players[c.getInstance().playerIndex].getY(), 3)) {
				c.getInstance().usingRangeWeapon = true;
				c.stopMovement();
			}
			if (!usingBow)
				c.getInstance().usingBow = false;
			if (!usingOtherRangeWeapons)
				c.getInstance().usingRangeWeapon = false;

			if (!usingCross && !usingArrows && usingBow
					&& c.getInstance().playerEquipment[c.getInstance().playerWeapon] < 4212
					&& c.getInstance().playerEquipment[c.getInstance().playerWeapon] > 4223) {
				c.sendMessage("You have run out of arrows!");
				return;
			}
			if (c.getCombat().correctBowAndArrows() < c.getInstance().playerEquipment[c.getInstance().playerArrows]
					&& Constants.CORRECT_ARROWS && usingBow && !c.getCombat().usingCrystalBow() && !usingCross) {
				c.sendMessage("You can't use "
						+ c.getItems().getItemName(c.getInstance().playerEquipment[c.getInstance().playerArrows])
								.toLowerCase()
						+ " with a "
						+ c.getItems().getItemName(c.getInstance().playerEquipment[c.getInstance().playerWeapon])
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
				if (!c.getInstance().usingMagic && !usingBow && !usingOtherRangeWeapons) {
					Following.triggerFollowing(c.getInstance().playerIndex, 0, c);
					c.getInstance().followDistance = 1;
				}
				if (c.getInstance().attackTimer <= 0) {
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
			if (!c.getInstance().mageAllowed) {
				c.getInstance().mageAllowed = true;
				break;
			}
			c.getPA().walkTo(0, 0);
			// c.usingSpecial = false;
			// c.getItems().updateSpecialBar();

			c.getInstance().playerIndex = c.getInStream().readSignedWordA();
			if (c.getInstance().teleTimer > 0) {
				c.getInstance().playerIndex = -1;
				return;
			}
			if (c.getInstance().playerIndex > PlayerHandler.players.length) {
				c.getInstance().playerIndex = -1;
				return;
			}
			int castingSpellId = c.getInStream().readSignedWordBigEndian();
			c.getInstance().usingMagic = false;
			if (PlayerHandler.players[c.getInstance().playerIndex] == null) {
				break;
			}
			if (c.getInstance().respawnTimer > 0) {
				break;
			}

			for (int i = 0; i < c.getInstance().MAGIC_SPELLS.length; i++) {
				if (castingSpellId == c.getInstance().MAGIC_SPELLS[i][0]) {
					c.getInstance().spellId = i;
					c.getInstance().usingMagic = true;
					break;
				}
			}

			if (c.getInstance().autocasting)
				c.getInstance().autocasting = false;

			if (!c.getCombat().checkReqs()) {
				break;
			}
			if (DuelArena.isDueling(c)) {
				if (c.getInstance().duelCount > 0) {
					c.sendMessage("The duel hasn't started yet!");
					c.getInstance().playerIndex = 0;
					return;
				}
				if (c.getInstance().duelRule[DuelArena.RULE_MAGIC]) {
					c.sendMessage("Magic has been disabled in this duel!");
					return;
				}
			}

			for (int r = 0; r < c.getInstance().REDUCE_SPELLS.length; r++) { // reducing
																				// spells,
																				// confuse
																				// etc
				if (PlayerHandler.players[c.getInstance().playerIndex].getInstance().REDUCE_SPELLS[r] == c
						.getInstance().MAGIC_SPELLS[c.getInstance().spellId][0]) {
					if ((System.currentTimeMillis() - PlayerHandler.players[c.getInstance().playerIndex]
							.getInstance().reduceSpellDelay[r]) < PlayerHandler.players[c.getInstance().playerIndex]
									.getInstance().REDUCE_SPELL_TIME[r]) {
						c.sendMessage("That player is currently immune to this spell.");
						c.getInstance().usingMagic = false;
						c.stopMovement();
						c.getCombat().resetPlayerAttack();
					}
					break;
				}
			}

			if (System.currentTimeMillis()
					- PlayerHandler.players[c.getInstance().playerIndex]
							.getInstance().teleBlockDelay < PlayerHandler.players[c.getInstance().playerIndex]
									.getInstance().teleBlockLength
					&& c.getInstance().MAGIC_SPELLS[c.getInstance().spellId][0] == 12445) {
				c.sendMessage("That player is already affected by this spell.");
				c.getInstance().usingMagic = false;
				c.stopMovement();
				c.getCombat().resetPlayerAttack();
			}

			/*
			 * if(!c.getCombat().checkMagicReqs(c.spellId)) { c.stopMovement();
			 * c.getCombat().resetPlayerAttack(); break; }
			 */

			if (c.getInstance().usingMagic) {
				if (c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[c.getInstance().playerIndex].getX(),
						PlayerHandler.players[c.getInstance().playerIndex].getY(), 7)) {
					c.stopMovement();
				}
				if (c.getCombat().checkReqs()) {
					Following.triggerFollowing(c.playerIndex, 0, c);
					c.getInstance().mageFollow = true;
					if (c.getInstance().attackTimer <= 0) {
						// c.getCombat().attackPlayer(c.playerIndex);
						// c.attackTimer++;
					}
				}
			}
			break;

		}

	}

}
