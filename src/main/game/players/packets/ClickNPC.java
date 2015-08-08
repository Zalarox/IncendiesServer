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
		c.getInstance().npcIndex = 0;
		c.getInstance().npcClickIndex = 0;
		c.getInstance().playerIndex = 0;
		c.getInstance().clickNpcType = 0;
		if (c.getInstance().teleTimer > 0)
			return;
		if (c.getInstance().resting) {
			c.getPA().resetRest();
		}
		c.getPA().resetSkills();
		switch (packetType) {

		/**
		 * Attack npc melee or range
		 **/
		case ATTACK_NPC:
			if (!c.getInstance().mageAllowed) {
				c.getInstance().mageAllowed = true;
				c.sendMessage("I can't reach that.");
				break;
			}
			c.getInstance().npcIndex = c.getInStream().readUnsignedWordA();
			if (c.getInstance().npcIndex > NPCHandler.npcs.length) {
				c.getInstance().npcIndex = -1;
				return;
			}
			if (NPCHandler.npcs[c.getInstance().npcIndex] == null) {
				c.getInstance().npcIndex = 0;
				break;
			}
			if (NPCHandler.npcs[c.getInstance().npcIndex].MaxHP == 0) {
				c.getInstance().npcIndex = 0;
				break;
			}
			if (NPCHandler.npcs[c.getInstance().npcIndex] == null) {
				break;
			}
			if (c.getInstance().autocastId > 0)
				c.getInstance().autocasting = true;
			if (!c.getInstance().autocasting && c.getInstance().spellId > 0) {
				c.getInstance().spellId = 0;
			}
			c.faceUpdate(c.getInstance().npcIndex);
			c.getInstance().usingMagic = false;
			boolean usingBow = false;
			boolean usingOtherRangeWeapons = false;
			boolean usingArrows = false;
			boolean usingCross = c.getInstance().playerEquipment[c.getInstance().playerWeapon] == 9185
					|| c.getInstance().playerEquipment[c.getInstance().playerWeapon] == 18357;
			if (c.getInstance().playerEquipment[c.getInstance().playerWeapon] >= 4214
					&& c.getInstance().playerEquipment[c.getInstance().playerWeapon] <= 4223)
				usingBow = true;
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
			if ((usingBow || c.getInstance().autocasting)
					&& c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[c.getInstance().npcIndex].getX(),
							NPCHandler.npcs[c.getInstance().npcIndex].getY(), 7)) {
				c.stopMovement();
			}

			if (usingOtherRangeWeapons
					&& c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[c.getInstance().npcIndex].getX(),
							NPCHandler.npcs[c.getInstance().npcIndex].getY(), 4)) {
				c.stopMovement();
			}
			if (!usingCross && !usingArrows && usingBow
					&& c.getInstance().playerEquipment[c.getInstance().playerWeapon] < 4212
					&& c.getInstance().playerEquipment[c.getInstance().playerWeapon] > 4223 && !usingCross) {
				c.sendMessage("You have run out of arrows!");
				break;
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

			if (c.getInstance().followId > 0) {
				Following.resetFollow(c);
			}
			if (c.getInstance().attackTimer <= 0) {
				c.getCombat().attackNpc(c.getInstance().npcIndex);
				c.getInstance().attackTimer++;
			}

			break;

		/**
		 * Attack npc with magic
		 **/
		case MAGE_NPC:
			if (!c.getInstance().mageAllowed) {
				c.getInstance().mageAllowed = true;
				c.sendMessage("I can't reach that.");
				break;
			}
			// c.usingSpecial = false;
			// c.getItems().updateSpecialBar();

			c.getInstance().npcIndex = c.getInStream().readSignedWordBigEndianA();
			int castingSpellId = c.getInStream().readSignedWordA();
			c.getInstance().usingMagic = false;
			if (c.getInstance().npcIndex > NPCHandler.npcs.length) {
				c.getInstance().npcIndex = -1;
				return;
			}
			if (NPCHandler.npcs[c.getInstance().npcIndex] == null) {
				break;
			}

			if (NPCHandler.npcs[c.getInstance().npcIndex].MaxHP == 0
					|| NPCHandler.npcs[c.getInstance().npcIndex].npcType == 944
					|| NPCHandler.npcs[c.getInstance().npcIndex].npcType == 1266) {
				c.sendMessage("You can't attack this npc.");
				break;
			}

			for (int i = 0; i < c.getInstance().MAGIC_SPELLS.length; i++) {
				if (castingSpellId == c.getInstance().MAGIC_SPELLS[i][0]) {
					c.getInstance().spellId = i;
					c.getInstance().usingMagic = true;
					break;
				}
			}
			if (castingSpellId == 1171) { // crumble undead
				for (int npc : Constants.UNDEAD_NPCS) {
					if (NPCHandler.npcs[c.getInstance().npcIndex].npcType != npc) {
						c.sendMessage("You can only attack undead monsters with this spell.");
						c.getInstance().usingMagic = false;
						c.stopMovement();
						break;
					}
				}
			}
			/*
			 * if(!c.getCombat().checkMagicReqs(c.spellId)) { c.stopMovement();
			 * break; }
			 */

			if (c.getInstance().autocasting)
				c.getInstance().autocasting = false;

			if (c.getInstance().usingMagic) {
				if (c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[c.getInstance().npcIndex].getX(),
						NPCHandler.npcs[c.getInstance().npcIndex].getY(), 6)) {
					c.stopMovement();
				}
				if (c.getInstance().attackTimer <= 0) {
					c.getCombat().attackNpc(c.getInstance().npcIndex);
					c.getInstance().attackTimer++;
				}
			}

			break;

		case FIRST_CLICK:
			c.getInstance().npcClickIndex = c.inStream.readSignedWordBigEndian();
			if (c.getInstance().npcIndex > NPCHandler.npcs.length) {
				c.getInstance().npcIndex = -1;
				return;
			}
			c.getInstance().npcType = NPCHandler.npcs[c.getInstance().npcClickIndex].npcType;
			if (HunterHandler.tryCatch(c, c.getInstance().npcClickIndex)) {
				return;
			}
			if (c.goodDistance(NPCHandler.npcs[c.getInstance().npcClickIndex].getX(),
					NPCHandler.npcs[c.getInstance().npcClickIndex].getY(), c.getX(), c.getY(), 2)) {
				c.turnPlayerTo(NPCHandler.npcs[c.getInstance().npcClickIndex].getX(),
						NPCHandler.npcs[c.getInstance().npcClickIndex].getY());
				NPCHandler.npcs[c.getInstance().npcClickIndex].facePlayer(c.playerId);
				if (NPCHandler.npcs[c.getInstance().npcClickIndex].summonedFor > 0) {
					if (NPCHandler.npcs[c.getInstance().npcClickIndex].summonedFor == c.getId()) {
						FamiliarInteraction.interactWithFamiliar(c);
						c.resetWalkingQueue();
						return;
					} else {
						c.sendMessage("This is not your familiar!");
						c.resetWalkingQueue();
						return;
					}
				} else {
					int npcId = NPCHandler.npcs[c.getInstance().npcClickIndex].npcType;
					if (npcId != 316 || npcId != 326 || npcId != 334) {
						NPCHandler.npcs[c.getInstance().npcClickIndex].facePlayer(c.playerId);
					}
					c.getActions().firstClickNpc(c.getInstance().npcType);
				}
			} else {
				c.getInstance().clickNpcType = 1;
				CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if ((c.getInstance().clickNpcType == 1)
								&& NPCHandler.npcs[c.getInstance().npcClickIndex] != null) {
							if (c.goodDistance(c.getX(), c.getY(),
									NPCHandler.npcs[c.getInstance().npcClickIndex].getX(),
									NPCHandler.npcs[c.getInstance().npcClickIndex].getY(), 1)) {
								c.turnPlayerTo(NPCHandler.npcs[c.getInstance().npcClickIndex].getX(),
										NPCHandler.npcs[c.getInstance().npcClickIndex].getY());
								NPCHandler.npcs[c.getInstance().npcClickIndex].facePlayer(c.playerId);
								c.getActions().firstClickNpc(c.getInstance().npcType);
								container.stop();
							}
						}
						if (c.getInstance().clickNpcType == 0 || c.getInstance().clickNpcType > 1)
							container.stop();
					}

					@Override
					public void stop() {
						c.getInstance().clickNpcType = 0;
					}
				}, 1);
			}
			break;

		case SECOND_CLICK:
			c.getInstance().npcClickIndex = c.inStream.readUnsignedWordBigEndianA();
			if (c.getInstance().npcIndex > NPCHandler.npcs.length) {
				c.getInstance().npcIndex = -1;
				return;
			}
			c.getInstance().npcType = NPCHandler.npcs[c.getInstance().npcClickIndex].npcType;
			if (c.goodDistance(NPCHandler.npcs[c.getInstance().npcClickIndex].getX(),
					NPCHandler.npcs[c.getInstance().npcClickIndex].getY(), c.getX(), c.getY(), 2)) {
				c.turnPlayerTo(NPCHandler.npcs[c.getInstance().npcClickIndex].getX(),
						NPCHandler.npcs[c.getInstance().npcClickIndex].getY());
				NPCHandler.npcs[c.getInstance().npcClickIndex].facePlayer(c.playerId);
				if (NPCHandler.npcs[c.getInstance().npcClickIndex].summonedFor > 0) {
					if (NPCHandler.npcs[c.getInstance().npcClickIndex].summonedFor == c.getId()) {
						c.getSummoning().openBoB();
						c.resetWalkingQueue();
						return;
					} else if (NPCHandler.npcs[c.getInstance().npcClickIndex].summonedFor != -1) {
						c.sendMessage("This is not your familiar!");
						c.resetWalkingQueue();
						return;
					}
				} else {
					int npcId = NPCHandler.npcs[c.getInstance().npcClickIndex].npcType;
					if (npcId != 316 || npcId != 326 || npcId != 334) {
						NPCHandler.npcs[c.getInstance().npcClickIndex].facePlayer(c.playerId);
					}
					c.getActions().secondClickNpc(c.getInstance().npcType);
				}
			} else {
				c.getInstance().clickNpcType = 2;
				CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if ((c.getInstance().clickNpcType == 2)
								&& NPCHandler.npcs[c.getInstance().npcClickIndex] != null) {
							if (c.goodDistance(c.getX(), c.getY(),
									NPCHandler.npcs[c.getInstance().npcClickIndex].getX(),
									NPCHandler.npcs[c.getInstance().npcClickIndex].getY(), 1)) {
								c.turnPlayerTo(NPCHandler.npcs[c.getInstance().npcClickIndex].getX(),
										NPCHandler.npcs[c.getInstance().npcClickIndex].getY());
								NPCHandler.npcs[c.getInstance().npcClickIndex].facePlayer(c.playerId);
								c.getActions().secondClickNpc(c.getInstance().npcType);
								container.stop();
							}
						}
						if (c.getInstance().clickNpcType < 2 || c.getInstance().clickNpcType > 2)
							container.stop();
					}

					@Override
					public void stop() {
						c.getInstance().clickNpcType = 0;
					}
				}, 1);
			}
			break;

		case THIRD_CLICK:
			c.getInstance().npcClickIndex = c.inStream.readSignedWord();
			if (c.getInstance().npcIndex > NPCHandler.npcs.length) {
				c.getInstance().npcIndex = -1;
				return;
			}
			c.getInstance().npcType = NPCHandler.npcs[c.getInstance().npcClickIndex].npcType;
			if (c.goodDistance(NPCHandler.npcs[c.getInstance().npcClickIndex].getX(),
					NPCHandler.npcs[c.getInstance().npcClickIndex].getY(), c.getX(), c.getY(), 2)) {
				c.turnPlayerTo(NPCHandler.npcs[c.getInstance().npcClickIndex].getX(),
						NPCHandler.npcs[c.getInstance().npcClickIndex].getY());
				NPCHandler.npcs[c.getInstance().npcClickIndex].facePlayer(c.playerId);
				if (NPCHandler.npcs[c.getInstance().npcClickIndex].summonedFor > 0) {
					if (NPCHandler.npcs[c.getInstance().npcClickIndex].summonedFor == c.getId()) {
						c.getSummoning().openBoB();
						c.resetWalkingQueue();
						return;
					} else if (NPCHandler.npcs[c.getInstance().npcClickIndex].summonedFor != -1) {
						c.sendMessage("This is not your familiar!");
						c.resetWalkingQueue();
						return;
					}
				} else {
					int npcId = NPCHandler.npcs[c.getInstance().npcClickIndex].npcType;
					if (npcId != 316 || npcId != 326 || npcId != 334) {
						NPCHandler.npcs[c.getInstance().npcClickIndex].facePlayer(c.playerId);
					}
					c.getActions().thirdClickNpc(c.getInstance().npcType);
				}
			} else {
				c.getInstance().clickNpcType = 3;
				CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if ((c.getInstance().clickNpcType == 3)
								&& NPCHandler.npcs[c.getInstance().npcClickIndex] != null) {
							if (c.goodDistance(c.getX(), c.getY(),
									NPCHandler.npcs[c.getInstance().npcClickIndex].getX(),
									NPCHandler.npcs[c.getInstance().npcClickIndex].getY(), 1)) {
								c.turnPlayerTo(NPCHandler.npcs[c.getInstance().npcClickIndex].getX(),
										NPCHandler.npcs[c.getInstance().npcClickIndex].getY());
								NPCHandler.npcs[c.getInstance().npcClickIndex].facePlayer(c.playerId);
								c.getActions().thirdClickNpc(c.getInstance().npcType);
								container.stop();
							}
						}
						if (c.getInstance().clickNpcType < 3 || c.getInstance().clickNpcType > 3)
							container.stop();
					}

					@Override
					public void stop() {
						c.getInstance().clickNpcType = 0;
					}
				}, 1);
			}
			break;

		case FOURTH_CLICK:
			c.getInstance().npcClickIndex = c.inStream.readSignedWordBigEndian();
			if (c.getInstance().npcIndex > NPCHandler.npcs.length) {
				c.getInstance().npcIndex = -1;
				return;
			}
			c.getInstance().npcType = NPCHandler.npcs[c.getInstance().npcClickIndex].npcType;
			if (c.goodDistance(NPCHandler.npcs[c.getInstance().npcClickIndex].getX(),
					NPCHandler.npcs[c.getInstance().npcClickIndex].getY(), c.getX(), c.getY(), 2)) {
				c.turnPlayerTo(NPCHandler.npcs[c.getInstance().npcClickIndex].getX(),
						NPCHandler.npcs[c.getInstance().npcClickIndex].getY());
				NPCHandler.npcs[c.getInstance().npcClickIndex].facePlayer(c.playerId);
				int npcId = NPCHandler.npcs[c.getInstance().npcClickIndex].npcType;
				if (npcId != 316 || npcId != 326 || npcId != 334) {
					NPCHandler.npcs[c.getInstance().npcClickIndex].facePlayer(c.playerId);
				}
				c.getActions().fourthClickNpc(c.getInstance().npcType);
			} else {
				c.getInstance().clickNpcType = 4;
				CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if ((c.getInstance().clickNpcType == 4)
								&& NPCHandler.npcs[c.getInstance().npcClickIndex] != null) {
							if (c.goodDistance(c.getX(), c.getY(),
									NPCHandler.npcs[c.getInstance().npcClickIndex].getX(),
									NPCHandler.npcs[c.getInstance().npcClickIndex].getY(), 1)) {
								c.turnPlayerTo(NPCHandler.npcs[c.getInstance().npcClickIndex].getX(),
										NPCHandler.npcs[c.getInstance().npcClickIndex].getY());
								NPCHandler.npcs[c.getInstance().npcClickIndex].facePlayer(c.playerId);
								c.getActions().fourthClickNpc(c.getInstance().npcType);
								container.stop();
							}
						}
						if (c.getInstance().clickNpcType < 4)
							container.stop();
					}

					@Override
					public void stop() {
						c.getInstance().clickNpcType = 0;
					}
				}, 1);
			}
			break;
		}

	}
}
