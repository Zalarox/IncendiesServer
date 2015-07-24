package main.game.players.packets;

import main.Constants;
import main.event.CycleEvent;
import main.event.CycleEventContainer;
import main.event.CycleEventHandler;
import main.game.npcs.NPCHandler;
import main.game.npcs.data.FamiliarInteraction;
import main.game.players.PacketType;
import main.game.players.Player;
import main.game.players.content.skills.hunter.HunterHandler;
import main.handlers.Following;

/**
 * Click NPC
 */
public class ClickNPC implements PacketType {
	public static final int ATTACK_NPC = 72, MAGE_NPC = 131, FIRST_CLICK = 155, SECOND_CLICK = 17, THIRD_CLICK = 21,
			FOURTH_CLICK = 18;

	@SuppressWarnings("static-access")
	@Override
	public void processPacket(final Player c, int packetType, int packetSize) {
		c.getVariables().npcIndex = 0;
		c.getVariables().npcClickIndex = 0;
		c.getVariables().playerIndex = 0;
		c.getVariables().clickNpcType = 0;
		if (c.getVariables().teleTimer > 0)
			return;
		if (c.getVariables().resting) {
			c.getPA().resetRest();
		}
		c.getPA().resetSkills();
		switch (packetType) {

		/**
		 * Attack npc melee or range
		 **/
		case ATTACK_NPC:
			if (!c.getVariables().mageAllowed) {
				c.getVariables().mageAllowed = true;
				c.sendMessage("I can't reach that.");
				break;
			}
			c.getVariables().npcIndex = c.getInStream().readUnsignedWordA();
			if (c.getVariables().npcIndex > NPCHandler.npcs.length) {
				c.getVariables().npcIndex = -1;
				return;
			}
			if (NPCHandler.npcs[c.getVariables().npcIndex] == null) {
				c.getVariables().npcIndex = 0;
				break;
			}
			if (NPCHandler.npcs[c.getVariables().npcIndex].MaxHP == 0) {
				c.getVariables().npcIndex = 0;
				break;
			}
			if (NPCHandler.npcs[c.getVariables().npcIndex] == null) {
				break;
			}
			if (c.getVariables().autocastId > 0)
				c.getVariables().autocasting = true;
			if (!c.getVariables().autocasting && c.getVariables().spellId > 0) {
				c.getVariables().spellId = 0;
			}
			c.faceUpdate(c.getVariables().npcIndex);
			c.getVariables().usingMagic = false;
			boolean usingBow = false;
			boolean usingOtherRangeWeapons = false;
			boolean usingArrows = false;
			boolean usingCross = c.getVariables().playerEquipment[c.getVariables().playerWeapon] == 9185
					|| c.getVariables().playerEquipment[c.getVariables().playerWeapon] == 18357;
			if (c.getVariables().playerEquipment[c.getVariables().playerWeapon] >= 4214
					&& c.getVariables().playerEquipment[c.getVariables().playerWeapon] <= 4223)
				usingBow = true;
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
			if ((usingBow || c.getVariables().autocasting)
					&& c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[c.getVariables().npcIndex].getX(),
							NPCHandler.npcs[c.getVariables().npcIndex].getY(), 7)) {
				c.stopMovement();
			}

			if (usingOtherRangeWeapons
					&& c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[c.getVariables().npcIndex].getX(),
							NPCHandler.npcs[c.getVariables().npcIndex].getY(), 4)) {
				c.stopMovement();
			}
			if (!usingCross && !usingArrows && usingBow
					&& c.getVariables().playerEquipment[c.getVariables().playerWeapon] < 4212
					&& c.getVariables().playerEquipment[c.getVariables().playerWeapon] > 4223 && !usingCross) {
				c.sendMessage("You have run out of arrows!");
				break;
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

			if (c.getVariables().followId > 0) {
				Following.resetFollow(c);
			}
			if (c.getVariables().attackTimer <= 0) {
				c.getCombat().attackNpc(c.getVariables().npcIndex);
				c.getVariables().attackTimer++;
			}

			break;

		/**
		 * Attack npc with magic
		 **/
		case MAGE_NPC:
			if (!c.getVariables().mageAllowed) {
				c.getVariables().mageAllowed = true;
				c.sendMessage("I can't reach that.");
				break;
			}
			// c.usingSpecial = false;
			// c.getItems().updateSpecialBar();

			c.getVariables().npcIndex = c.getInStream().readSignedWordBigEndianA();
			int castingSpellId = c.getInStream().readSignedWordA();
			c.getVariables().usingMagic = false;
			if (c.getVariables().npcIndex > NPCHandler.npcs.length) {
				c.getVariables().npcIndex = -1;
				return;
			}
			if (NPCHandler.npcs[c.getVariables().npcIndex] == null) {
				break;
			}

			if (NPCHandler.npcs[c.getVariables().npcIndex].MaxHP == 0
					|| NPCHandler.npcs[c.getVariables().npcIndex].npcType == 944
					|| NPCHandler.npcs[c.getVariables().npcIndex].npcType == 1266) {
				c.sendMessage("You can't attack this npc.");
				break;
			}

			for (int i = 0; i < c.getVariables().MAGIC_SPELLS.length; i++) {
				if (castingSpellId == c.getVariables().MAGIC_SPELLS[i][0]) {
					c.getVariables().spellId = i;
					c.getVariables().usingMagic = true;
					break;
				}
			}
			if (castingSpellId == 1171) { // crumble undead
				for (int npc : Constants.UNDEAD_NPCS) {
					if (NPCHandler.npcs[c.getVariables().npcIndex].npcType != npc) {
						c.sendMessage("You can only attack undead monsters with this spell.");
						c.getVariables().usingMagic = false;
						c.stopMovement();
						break;
					}
				}
			}
			/*
			 * if(!c.getCombat().checkMagicReqs(c.spellId)) { c.stopMovement();
			 * break; }
			 */

			if (c.getVariables().autocasting)
				c.getVariables().autocasting = false;

			if (c.getVariables().usingMagic) {
				if (c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[c.getVariables().npcIndex].getX(),
						NPCHandler.npcs[c.getVariables().npcIndex].getY(), 6)) {
					c.stopMovement();
				}
				if (c.getVariables().attackTimer <= 0) {
					c.getCombat().attackNpc(c.getVariables().npcIndex);
					c.getVariables().attackTimer++;
				}
			}

			break;

		case FIRST_CLICK:
			c.getVariables().npcClickIndex = c.inStream.readSignedWordBigEndian();
			if (c.getVariables().npcIndex > NPCHandler.npcs.length) {
				c.getVariables().npcIndex = -1;
				return;
			}
			c.getVariables().npcType = NPCHandler.npcs[c.getVariables().npcClickIndex].npcType;
			if (HunterHandler.tryCatch(c, c.getVariables().npcClickIndex)) {
				return;
			}
			if (c.goodDistance(NPCHandler.npcs[c.getVariables().npcClickIndex].getX(),
					NPCHandler.npcs[c.getVariables().npcClickIndex].getY(), c.getX(), c.getY(), 2)) {
				c.turnPlayerTo(NPCHandler.npcs[c.getVariables().npcClickIndex].getX(),
						NPCHandler.npcs[c.getVariables().npcClickIndex].getY());
				NPCHandler.npcs[c.getVariables().npcClickIndex].facePlayer(c.playerId);
				if (NPCHandler.npcs[c.getVariables().npcClickIndex].summonedFor > 0) {
					if (NPCHandler.npcs[c.getVariables().npcClickIndex].summonedFor == c.getId()) {
						FamiliarInteraction.interactWithFamiliar(c);
						c.resetWalkingQueue();
						return;
					} else {
						c.sendMessage("This is not your familiar!");
						c.resetWalkingQueue();
						return;
					}
				} else {
					int npcId = NPCHandler.npcs[c.getVariables().npcClickIndex].npcType;
					if (npcId != 316 || npcId != 326 || npcId != 334) {
						NPCHandler.npcs[c.getVariables().npcClickIndex].facePlayer(c.playerId);
					}
					c.getActions().firstClickNpc(c.getVariables().npcType);
				}
			} else {
				c.getVariables().clickNpcType = 1;
				CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if ((c.getVariables().clickNpcType == 1)
								&& NPCHandler.npcs[c.getVariables().npcClickIndex] != null) {
							if (c.goodDistance(c.getX(), c.getY(),
									NPCHandler.npcs[c.getVariables().npcClickIndex].getX(),
									NPCHandler.npcs[c.getVariables().npcClickIndex].getY(), 1)) {
								c.turnPlayerTo(NPCHandler.npcs[c.getVariables().npcClickIndex].getX(),
										NPCHandler.npcs[c.getVariables().npcClickIndex].getY());
								NPCHandler.npcs[c.getVariables().npcClickIndex].facePlayer(c.playerId);
								c.getActions().firstClickNpc(c.getVariables().npcType);
								container.stop();
							}
						}
						if (c.getVariables().clickNpcType == 0 || c.getVariables().clickNpcType > 1)
							container.stop();
					}

					@Override
					public void stop() {
						c.getVariables().clickNpcType = 0;
					}
				}, 1);
			}
			break;

		case SECOND_CLICK:
			c.getVariables().npcClickIndex = c.inStream.readUnsignedWordBigEndianA();
			if (c.getVariables().npcIndex > NPCHandler.npcs.length) {
				c.getVariables().npcIndex = -1;
				return;
			}
			c.getVariables().npcType = NPCHandler.npcs[c.getVariables().npcClickIndex].npcType;
			if (c.goodDistance(NPCHandler.npcs[c.getVariables().npcClickIndex].getX(),
					NPCHandler.npcs[c.getVariables().npcClickIndex].getY(), c.getX(), c.getY(), 2)) {
				c.turnPlayerTo(NPCHandler.npcs[c.getVariables().npcClickIndex].getX(),
						NPCHandler.npcs[c.getVariables().npcClickIndex].getY());
				NPCHandler.npcs[c.getVariables().npcClickIndex].facePlayer(c.playerId);
				if (NPCHandler.npcs[c.getVariables().npcClickIndex].summonedFor > 0) {
					if (NPCHandler.npcs[c.getVariables().npcClickIndex].summonedFor == c.getId()) {
						c.getSummoning().openBoB();
						c.resetWalkingQueue();
						return;
					} else if (NPCHandler.npcs[c.getVariables().npcClickIndex].summonedFor != -1) {
						c.sendMessage("This is not your familiar!");
						c.resetWalkingQueue();
						return;
					}
				} else {
					int npcId = NPCHandler.npcs[c.getVariables().npcClickIndex].npcType;
					if (npcId != 316 || npcId != 326 || npcId != 334) {
						NPCHandler.npcs[c.getVariables().npcClickIndex].facePlayer(c.playerId);
					}
					c.getActions().secondClickNpc(c.getVariables().npcType);
				}
			} else {
				c.getVariables().clickNpcType = 2;
				CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if ((c.getVariables().clickNpcType == 2)
								&& NPCHandler.npcs[c.getVariables().npcClickIndex] != null) {
							if (c.goodDistance(c.getX(), c.getY(),
									NPCHandler.npcs[c.getVariables().npcClickIndex].getX(),
									NPCHandler.npcs[c.getVariables().npcClickIndex].getY(), 1)) {
								c.turnPlayerTo(NPCHandler.npcs[c.getVariables().npcClickIndex].getX(),
										NPCHandler.npcs[c.getVariables().npcClickIndex].getY());
								NPCHandler.npcs[c.getVariables().npcClickIndex].facePlayer(c.playerId);
								c.getActions().secondClickNpc(c.getVariables().npcType);
								container.stop();
							}
						}
						if (c.getVariables().clickNpcType < 2 || c.getVariables().clickNpcType > 2)
							container.stop();
					}

					@Override
					public void stop() {
						c.getVariables().clickNpcType = 0;
					}
				}, 1);
			}
			break;

		case THIRD_CLICK:
			c.getVariables().npcClickIndex = c.inStream.readSignedWord();
			if (c.getVariables().npcIndex > NPCHandler.npcs.length) {
				c.getVariables().npcIndex = -1;
				return;
			}
			c.getVariables().npcType = NPCHandler.npcs[c.getVariables().npcClickIndex].npcType;
			if (c.goodDistance(NPCHandler.npcs[c.getVariables().npcClickIndex].getX(),
					NPCHandler.npcs[c.getVariables().npcClickIndex].getY(), c.getX(), c.getY(), 2)) {
				c.turnPlayerTo(NPCHandler.npcs[c.getVariables().npcClickIndex].getX(),
						NPCHandler.npcs[c.getVariables().npcClickIndex].getY());
				NPCHandler.npcs[c.getVariables().npcClickIndex].facePlayer(c.playerId);
				if (NPCHandler.npcs[c.getVariables().npcClickIndex].summonedFor > 0) {
					if (NPCHandler.npcs[c.getVariables().npcClickIndex].summonedFor == c.getId()) {
						c.getSummoning().openBoB();
						c.resetWalkingQueue();
						return;
					} else if (NPCHandler.npcs[c.getVariables().npcClickIndex].summonedFor != -1) {
						c.sendMessage("This is not your familiar!");
						c.resetWalkingQueue();
						return;
					}
				} else {
					int npcId = NPCHandler.npcs[c.getVariables().npcClickIndex].npcType;
					if (npcId != 316 || npcId != 326 || npcId != 334) {
						NPCHandler.npcs[c.getVariables().npcClickIndex].facePlayer(c.playerId);
					}
					c.getActions().thirdClickNpc(c.getVariables().npcType);
				}
			} else {
				c.getVariables().clickNpcType = 3;
				CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if ((c.getVariables().clickNpcType == 3)
								&& NPCHandler.npcs[c.getVariables().npcClickIndex] != null) {
							if (c.goodDistance(c.getX(), c.getY(),
									NPCHandler.npcs[c.getVariables().npcClickIndex].getX(),
									NPCHandler.npcs[c.getVariables().npcClickIndex].getY(), 1)) {
								c.turnPlayerTo(NPCHandler.npcs[c.getVariables().npcClickIndex].getX(),
										NPCHandler.npcs[c.getVariables().npcClickIndex].getY());
								NPCHandler.npcs[c.getVariables().npcClickIndex].facePlayer(c.playerId);
								c.getActions().thirdClickNpc(c.getVariables().npcType);
								container.stop();
							}
						}
						if (c.getVariables().clickNpcType < 3 || c.getVariables().clickNpcType > 3)
							container.stop();
					}

					@Override
					public void stop() {
						c.getVariables().clickNpcType = 0;
					}
				}, 1);
			}
			break;

		case FOURTH_CLICK:
			c.getVariables().npcClickIndex = c.inStream.readSignedWordBigEndian();
			if (c.getVariables().npcIndex > NPCHandler.npcs.length) {
				c.getVariables().npcIndex = -1;
				return;
			}
			c.getVariables().npcType = NPCHandler.npcs[c.getVariables().npcClickIndex].npcType;
			if (c.goodDistance(NPCHandler.npcs[c.getVariables().npcClickIndex].getX(),
					NPCHandler.npcs[c.getVariables().npcClickIndex].getY(), c.getX(), c.getY(), 2)) {
				c.turnPlayerTo(NPCHandler.npcs[c.getVariables().npcClickIndex].getX(),
						NPCHandler.npcs[c.getVariables().npcClickIndex].getY());
				NPCHandler.npcs[c.getVariables().npcClickIndex].facePlayer(c.playerId);
				int npcId = NPCHandler.npcs[c.getVariables().npcClickIndex].npcType;
				if (npcId != 316 || npcId != 326 || npcId != 334) {
					NPCHandler.npcs[c.getVariables().npcClickIndex].facePlayer(c.playerId);
				}
				c.getActions().fourthClickNpc(c.getVariables().npcType);
			} else {
				c.getVariables().clickNpcType = 4;
				CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if ((c.getVariables().clickNpcType == 4)
								&& NPCHandler.npcs[c.getVariables().npcClickIndex] != null) {
							if (c.goodDistance(c.getX(), c.getY(),
									NPCHandler.npcs[c.getVariables().npcClickIndex].getX(),
									NPCHandler.npcs[c.getVariables().npcClickIndex].getY(), 1)) {
								c.turnPlayerTo(NPCHandler.npcs[c.getVariables().npcClickIndex].getX(),
										NPCHandler.npcs[c.getVariables().npcClickIndex].getY());
								NPCHandler.npcs[c.getVariables().npcClickIndex].facePlayer(c.playerId);
								c.getActions().fourthClickNpc(c.getVariables().npcType);
								container.stop();
							}
						}
						if (c.getVariables().clickNpcType < 4)
							container.stop();
					}

					@Override
					public void stop() {
						c.getVariables().clickNpcType = 0;
					}
				}, 1);
			}
			break;
		}

	}
}
