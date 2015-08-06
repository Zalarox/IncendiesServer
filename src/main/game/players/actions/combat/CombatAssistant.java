package main.game.players.actions.combat;

import java.util.Random;

import main.Constants;
import main.GameEngine;
import main.event.Task;
import main.game.items.degrade.Chaotic;
import main.game.npcs.NPC;
import main.game.npcs.NPCHandler;
import main.game.npcs.data.EmoteHandler;
import main.game.npcs.data.NPCDefinition;
import main.game.npcs.data.SummoningData;
import main.game.players.Boundaries;
import main.game.players.Boundaries.Area;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.game.players.content.minigames.impl.PestControl;
import main.game.players.content.minigames.DuelArena;
import main.game.players.following.PathFinder;
import main.handlers.Following;
import main.handlers.ItemHandler;
import main.handlers.SkillHandler;
import main.util.Misc;
import main.world.TileControl;
import main.world.map.Region;

public class CombatAssistant {

	private final Random random = new Random();
	private final double DEFENCE_MODIFIER = 0.70;
	public double crossbowDamage = 1.0;
	public boolean ignoreDefence;

	private Player c;

	public CombatAssistant(Player Player) {
		this.c = Player;
	}

	public static boolean pathBlocked(Player attacker, NPC victim) {

		double offsetX = Math.abs(attacker.absX - victim.absX);
		double offsetY = Math.abs(attacker.absY - victim.absY);

		int distance = TileControl.calculateDistance(attacker, victim);

		if (distance == 0) {
			return true;
		}

		offsetX = offsetX > 0 ? offsetX / distance : 0;
		offsetY = offsetY > 0 ? offsetY / distance : 0;

		int[][] path = new int[distance][5];

		int curX = attacker.absX;
		int curY = attacker.absY;
		int next = 0;
		int nextMoveX = 0;
		int nextMoveY = 0;

		double currentTileXCount = 0.0;
		double currentTileYCount = 0.0;

		while (distance > 0) {
			distance--;
			nextMoveX = 0;
			nextMoveY = 0;
			if (curX > victim.absX) {
				currentTileXCount += offsetX;
				if (currentTileXCount >= 1.0) {
					nextMoveX--;
					curX--;
					currentTileXCount -= offsetX;
				}
			} else if (curX < victim.absX) {
				currentTileXCount += offsetX;
				if (currentTileXCount >= 1.0) {
					nextMoveX++;
					curX++;
					currentTileXCount -= offsetX;
				}
			}
			if (curY > victim.absY) {
				currentTileYCount += offsetY;
				if (currentTileYCount >= 1.0) {
					nextMoveY--;
					curY--;
					currentTileYCount -= offsetY;
				}
			} else if (curY < victim.absY) {
				currentTileYCount += offsetY;
				if (currentTileYCount >= 1.0) {
					nextMoveY++;
					curY++;
					currentTileYCount -= offsetY;
				}
			}
			path[next][0] = curX;
			path[next][1] = curY;
			path[next][2] = attacker.heightLevel;// getHeightLevel();
			path[next][3] = nextMoveX;
			path[next][4] = nextMoveY;
			next++;
		}
		for (int i = 0; i < path.length; i++) {
			if (!Region./* getSingleton(). */getClipping(path[i][0], path[i][1], path[i][2], path[i][3], path[i][4])) {
				return true;
			}
		}
		return false;
	}

	public static boolean pathBlocked(Player attacker, Player victim) {

		double offsetX = Math.abs(attacker.absX - victim.absX);
		double offsetY = Math.abs(attacker.absY - victim.absY);

		int distance = TileControl.calculateDistance(attacker, victim);

		if (distance == 0) {
			return true;
		}

		offsetX = offsetX > 0 ? offsetX / distance : 0;
		offsetY = offsetY > 0 ? offsetY / distance : 0;

		int[][] path = new int[distance][5];

		int curX = attacker.absX;
		int curY = attacker.absY;
		int next = 0;
		int nextMoveX = 0;
		int nextMoveY = 0;

		double currentTileXCount = 0.0;
		double currentTileYCount = 0.0;

		while (distance > 0) {
			distance--;
			nextMoveX = 0;
			nextMoveY = 0;
			if (curX > victim.absX) {
				currentTileXCount += offsetX;
				if (currentTileXCount >= 1.0) {
					nextMoveX--;
					curX--;
					currentTileXCount -= offsetX;
				}
			} else if (curX < victim.absX) {
				currentTileXCount += offsetX;
				if (currentTileXCount >= 1.0) {
					nextMoveX++;
					curX++;
					currentTileXCount -= offsetX;
				}
			}
			if (curY > victim.absY) {
				currentTileYCount += offsetY;
				if (currentTileYCount >= 1.0) {
					nextMoveY--;
					curY--;
					currentTileYCount -= offsetY;
				}
			} else if (curY < victim.absY) {
				currentTileYCount += offsetY;
				if (currentTileYCount >= 1.0) {
					nextMoveY++;
					curY++;
					currentTileYCount -= offsetY;
				}
			}
			path[next][0] = curX;
			path[next][1] = curY;
			path[next][2] = attacker.heightLevel;// getHeightLevel();
			path[next][3] = nextMoveX;
			path[next][4] = nextMoveY;
			next++;
		}
		for (int i = 0; i < path.length; i++) {
			if (!Region./* getSingleton(). */getClipping(path[i][0], path[i][1], path[i][2], path[i][3], path[i][4])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Attack Npcs
	 */
	public void attackNpc(int i) {
		if (NPCHandler.npcs[i] != null) {
			if (NPCHandler.npcs[i].isDead || NPCHandler.npcs[i].MaxHP <= 0) {
				c.usingMagic = false;
				c.faceUpdate(0);
				c.npcIndex = 0;
				return;
			}
			if (c.respawnTimer > 0) {
				c.npcIndex = 0;
				return;
			}
			if (NPCHandler.npcs[i].underAttackBy > 0 && NPCHandler.npcs[i].underAttackBy != c.playerId
					&& !NPCHandler.npcs[i].inMulti()/* && !c.inCwGame() */) {
				c.npcIndex = 0;
				c.sendMessage("This monster is already in combat.");
				return;
			}
			if ((c.underAttackBy > 0 || c.underAttackBy2 > 0) && c.underAttackBy2 != i && !c.getVariables().inMulti()) {
				resetPlayerAttack();
				c.sendMessage("I am already under attack.");
				return;
			}
			if (!c.getSlayer().ableToSlay(c, NPCHandler.npcs[i])) {
				resetPlayerAttack();
				return;
			}
			if ((NPCHandler.npcs[i].spawnedBy == c.playerId) && SummoningData.isSummonNpc(NPCHandler.npcs[i].npcType)) {
				resetPlayerAttack();
				c.sendMessage("You cannot attack your summoning monster.");
				return;
			}

			if (SummoningData.isSummonNpc(NPCHandler.npcs[i].npcType)) {
				Player OtherPlayer = PlayerHandler.players[NPCHandler.npcs[i].spawnedBy];
				if (!c.getVariables().inWild() || !OtherPlayer.getVariables().inWild()) {
					resetPlayerAttack();
					c.sendMessage("This monster is not in the wilderness.");
					return;
				}
			}

			if (SummoningData.isSummonNpc(NPCHandler.npcs[i].npcType)) {
				Player OtherPlayer2 = PlayerHandler.players[NPCHandler.npcs[i].spawnedBy];
				if (!c.getVariables().inMulti() || !OtherPlayer2.getVariables().inMulti()) {
					resetPlayerAttack();
					c.sendMessage("This monster is not in multi.");
					return;
				}
			}

			if ((NPCHandler.npcs[i].spawnedBy != c.playerId) && (NPCHandler.npcs[i].spawnedBy > 0)
					&& !SummoningData.isSummonNpc(
							NPCHandler.npcs[i].npcType)/* && !c.inCwGame() */) {
				resetPlayerAttack();
				c.sendMessage("This monster was not spawned for you.");
				return;
			}

			Following.triggerFollowing(i, 1, c);
			if (c.attackTimer <= 0) {
				boolean usingBow = false;
				boolean usingArrows = false;
				boolean usingOtherRangeWeapons = false;
				boolean usingCross = c.playerEquipment[c.playerWeapon] == 9185
						|| c.playerEquipment[c.playerWeapon] == 18357;
				c.bonusAttack = 0;
				c.rangeItemUsed = 0;
				c.projectileStage = 0;
				if (c.autocasting) {
					c.spellId = c.autocastId;
					c.usingMagic = true;
				}
				if (c.spellId > 0) {
					c.usingMagic = true;
				}
				c.attackTimer = getAttackDelay(
						c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
				c.formerWeapon = c.playerEquipment[c.playerWeapon];
				c.specAccuracy = 1.0;
				c.specDamage = 1.0;
				if (usingBow || c.usingMagic || usingOtherRangeWeapons) {
					c.mageFollow = true;
				} else {
					c.mageFollow = false;
				}
				if (!c.usingMagic) {
					for (int bowId : c.BOWS) {
						if (c.playerEquipment[c.playerWeapon] == bowId) {
							usingBow = true;
							for (int arrowId : c.ARROWS) {
								if (c.playerEquipment[c.playerArrows] == arrowId) {
									usingArrows = true;
								}
							}
						}
					}

					for (int otherRangeId : c.OTHER_RANGE_WEAPONS) {
						if (c.playerEquipment[c.playerWeapon] == otherRangeId) {
							usingOtherRangeWeapons = true;
						}
					}
				}
				if (armaNpc(i) && !usingCross && !usingBow && !c.usingMagic && !usingCrystalBow()
						&& !usingOtherRangeWeapons) {
					c.sendMessage("You cannot reach that npc!");
					resetPlayerAttack();
					return;
				}
				if ((!c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(), 2)
						&& (usingHally() && !usingOtherRangeWeapons && !usingBow && !c.usingMagic))
						|| (!c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(), 4)
								&& (usingOtherRangeWeapons && !usingBow && !c.usingMagic))
						|| (!c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(), 1)
								&& (!usingOtherRangeWeapons && !usingHally() && !usingBow && !c.usingMagic))
						|| ((!c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(),
								8) && (usingBow || c.usingMagic)))) {
					c.attackTimer = 2;
					return;
				}

				if (!usingCross && !usingArrows && usingBow
						&& (c.playerEquipment[c.playerWeapon] < 4212 || c.playerEquipment[c.playerWeapon] > 4223)) {
					c.sendMessage("You have run out of arrows!");
					c.stopMovement();
					c.npcIndex = 0;
					return;
				}
				if (correctBowAndArrows() < c.playerEquipment[c.playerArrows] && Constants.CORRECT_ARROWS && usingBow
						&& !usingCrystalBow() && !usingCross) {
					c.sendMessage("You can't use "
							+ c.getItems().getItemName(c.playerEquipment[c.playerArrows]).toLowerCase() + " with a "
							+ c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase() + ".");
					c.stopMovement();
					c.npcIndex = 0;
					return;
				}

				if (usingCross && !properBolts()) {
					c.sendMessage("You must use bolts with a crossbow.");
					c.stopMovement();
					resetPlayerAttack();
					return;
				}

				if (usingBow || c.usingMagic || usingOtherRangeWeapons
						|| (c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(), 2)
								&& usingHally())) {
					c.stopMovement();
				}

				if (!checkMagicReqs(c.spellId)) {
					c.stopMovement();
					c.npcIndex = 0;
					return;
				}
				int pX = c.getX();
				int pY = c.getY();
				int nX = NPCHandler.npcs[i].getX();
				int nY = NPCHandler.npcs[i].getY();
				if (Region.blockedShot(c.getX(), c.getY(), c.heightLevel, NPCHandler.npcs[i].getX(),
						NPCHandler.npcs[i].getY())) {
					if ((c.usingBow || c.usingMagic || usingOtherRangeWeapons || c.autocasting))
						PathFinder.getPathFinder().findRoute(c, NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(),
								true, 8, 8);
					c.attackTimer = 0;
					return;
				}
				if (pathBlocked(c, NPCHandler.npcs[i])) {
					if ((c.usingBow || c.usingMagic || usingOtherRangeWeapons || c.autocasting))
						PathFinder.getPathFinder().findRoute(c, NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(),
								true, 8, 8);
					if (!c.usingBow && !c.usingMagic && !usingOtherRangeWeapons && !c.autocasting)
						PathFinder.getPathFinder().findRoute(c, NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(),
								true, 1, 1);
					c.attackTimer = 0;
					return;
				}
				c.faceUpdate(i);
				NPCHandler.npcs[i].underAttackBy = c.playerId;
				NPCHandler.npcs[i].lastDamageTaken = System.currentTimeMillis();
				if (c.usingSpecial && !c.usingMagic) {
					if (checkSpecAmount(c.playerEquipment[c.playerWeapon])) {
						c.lastWeaponUsed = c.playerEquipment[c.playerWeapon];
						c.lastArrowUsed = c.playerEquipment[c.playerArrows];
						activateSpecial(c.playerEquipment[c.playerWeapon], i);
						return;
					} else {
						c.sendMessage("You don't have the required special energy to use this attack.");
						c.usingSpecial = false;
						c.getItems().updateSpecialBar();
						c.npcIndex = 0;
						return;
					}
				}
				if (usingBow || c.usingMagic || usingOtherRangeWeapons) {
					c.mageFollow = true;
				} else {
					c.mageFollow = false;
				}
				c.specMaxHitIncrease = 0;
				if (!c.usingMagic) {
					int anim = getWepAnim(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					c.startAnimation(anim);
					if (anim == 423)
						c.attackTimer++;
				} else {
					c.startAnimation(Player.MAGIC_SPELLS[c.spellId][2]);
				}
				NPCDefinition n1 = NPCDefinition.forId(NPCHandler.npcs[i].npcType);
				if (n1 != null)
					NPCHandler.npcs[i].startAnimation(n1.getDefenceAnimation());
				c.lastWeaponUsed = c.playerEquipment[c.playerWeapon];
				c.lastArrowUsed = c.playerEquipment[c.playerArrows];
				if (!usingBow && !c.usingMagic && !usingOtherRangeWeapons) { // melee
					// hit
					// delay
					c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					c.projectileStage = 0;
					c.oldNpcIndex = i;
				}

				if (usingBow && !usingOtherRangeWeapons && !c.usingMagic || usingCross) { // range
																							// hit
																							// delay
					if (usingCross)
						c.usingBow = true;
					if (c.fightMode == c.RAPID)
						c.attackTimer--;
					c.lastArrowUsed = c.playerEquipment[c.playerArrows];
					c.lastWeaponUsed = c.playerEquipment[c.playerWeapon];
					c.gfx100(getRangeStartGFX());
					c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					c.projectileStage = 1;
					c.oldNpcIndex = i;
					if (c.playerEquipment[c.playerWeapon] >= 4212 && c.playerEquipment[c.playerWeapon] <= 4223) {
						c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
						c.crystalBowArrowCount++;
						c.lastArrowUsed = 0;
					} else {
						c.rangeItemUsed = c.playerEquipment[c.playerArrows];
						c.getItems().deleteArrow();
					}
					fireProjectileNpc();
				}

				if (usingOtherRangeWeapons && !c.usingMagic && !usingBow) { // knives,
					// darts,
					// etc
					// hit
					// delay
					c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
					c.getItems().deleteEquipment();
					c.gfx100(getRangeStartGFX());
					c.lastArrowUsed = 0;
					c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					c.projectileStage = 1;
					c.oldNpcIndex = i;
					if (c.fightMode == c.RAPID)
						c.attackTimer--;
					fireProjectileNpc();
				}

				if (c.usingMagic) { // magic hit delay
					int offX = (pY - nY) * -1;
					int offY = (pX - nX) * -1;
					c.castingMagic = true;
					c.projectileStage = 2;
					if (Player.MAGIC_SPELLS[c.spellId][3] > 0) {
						if (getStartGfxHeight() == 100) {
							c.gfx100(Player.MAGIC_SPELLS[c.spellId][3]);
						} else {
							c.gfx0(Player.MAGIC_SPELLS[c.spellId][3]);
						}
					}
					if (Player.MAGIC_SPELLS[c.spellId][4] > 0) {
						if (!NPCHandler.goodDistance(pX, pY, nX, nY, NPCHandler.npcs[i].getNpcSize() + 1)) {
							c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 78, Player.MAGIC_SPELLS[c.spellId][4],
									getStartHeight(), getEndHeight(), i + 1, 60);
						}
					}
					c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					c.oldNpcIndex = i;
					c.oldSpellId = c.spellId;
					c.spellId = 0;
					if (!c.autocasting)
						c.npcIndex = 0;
				}
				if (Misc.random(15) == 0) {
					npcleechAttack(0);
				}
				if (Misc.random(15) == 0) {
					npcleechDefence(0);
				}
				if (Misc.random(15) == 0) {
					npcleechStrength(0);
				}
				if (Misc.random(20) == 0) {
					npcleechSpecial(0);
				}
				if (Misc.random(15) == 0) {
					npcleechRanged(0);
				}
				if (Misc.random(15) == 0) {
					npcleechMagic(0);
				}

				if (usingBow && Constants.CRYSTAL_BOW_DEGRADES) { // crystal bow
					// degrading
					if (c.playerEquipment[c.playerWeapon] == 4212) { // new
						// crystal
						// bow
						// becomes
						// full
						// bow
						// on
						// the
						// first
						// shot
						c.getItems().wearItem(4214, 1, 3);
					}

					if (c.crystalBowArrowCount >= 250) {
						switch (c.playerEquipment[c.playerWeapon]) {

						case 4223: // 1/10 bow
							c.getItems().wearItem(-1, 1, 3);
							c.sendMessage("Your crystal bow has fully degraded.");
							if (!c.getItems().addItem(4207, 1)) {
								ItemHandler.createGroundItem(c, 4207, c.getX(), c.getY(), c.heightLevel, 1, c.getId());
							}
							c.crystalBowArrowCount = 0;
							break;

						default:
							c.getItems().wearItem(++c.playerEquipment[c.playerWeapon], 1, 3);
							c.sendMessage("Your crystal bow degrades.");
							c.crystalBowArrowCount = 0;
							break;

						}
					}
				}
				if (c.leechDelay == 0)
					c.leechDelay = 7;
			}
		}
	}

	public void delayedHit(int i) { // npc hit delay
		if (NPCHandler.npcs[i] != null) {
			if (NPCHandler.npcs[i].isDead) {
				c.npcIndex = 0;
				c.dBowHits = 0;
				c.dbowSpec = false;
				return;
			}
			if (!NPCHandler.npcs[i].IsAttackingNPC)
				NPCHandler.npcs[i].facePlayer(c.playerId);
			if (NPCHandler.npcs[i].underAttackBy > 0 && NPCHandler.getsPulled(i)) {
				NPCHandler.npcs[i].killerId = c.playerId;
			} else if (NPCHandler.npcs[i].underAttackBy < 0 && !NPCHandler.getsPulled(i)) {
				NPCHandler.npcs[i].killerId = c.playerId;
			}
			c.lastNpcAttacked = i;
			if (c.projectileStage == 0) { // melee hit damage
				applyNpcMeleeDamage(i, 1);
				if (c.doubleHit) {
					applyNpcMeleeDamage(i, 2);
				}
			}
			NPCHandler.npcs[i].startAnimation(EmoteHandler.getDefenceEmote(i));
			if (!c.castingMagic && c.projectileStage > 0) { // range hit damage
				int damage = Misc.random(rangeMaxHit());
				int damage2 = -1;
				if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1)
					damage2 = Misc.random(rangeMaxHit());
				boolean ignoreDef = false;
				if (Misc.random(5) == 1 && c.lastArrowUsed == 9243) {
					ignoreDef = true;
					NPCHandler.npcs[i].gfx0(758);
				}
				if (c.playerEquipment[3] == 9185) {
					if (Misc.random(10) == 1) {
						if (damage > 0) {
							EnchantedBolts.crossbowSpecial(c, i, damage);
							damage *= crossbowDamage;
						}
					}
				}

				if (Misc.random(NPCHandler.npcs[i].defence) > Misc.random(10 + calculateRangeAttack()) && !ignoreDef) {
					damage = 0;
				} else if (NPCHandler.npcs[i].npcType == 2881 || NPCHandler.npcs[i].npcType == 2883 && !ignoreDef) {
					damage = 0;
				}

				if (c.bowSpecShot == 1) {
					if (Misc.random(NPCHandler.npcs[i].defence) > Misc.random(10 + calculateRangeAttack()))
						damage2 = 0;
				}
				if (c.dbowSpec) {
					NPCHandler.npcs[i].gfx100(1100);
					c.dBowHits++;
					if (damage < 8)
						damage = 8;
					if (c.dBowHits == 2) {
						c.dbowSpec = false;
						c.dBowHits = 0;
					}
				}

				if (NPCHandler.npcs[i].HP - damage < 0) {
					damage = NPCHandler.npcs[i].HP;
				}
				if (NPCHandler.npcs[i].HP - damage <= 0 && damage2 > 0) {
					damage2 = 0;
				}
				if (damage > 0 && PestControl.isInGame(c)) {
					if (NPCHandler.npcs[i].npcType == 6142) {
						c.pcDamage += damage;
						PestControl.portalHealth[0] -= damage;
					}
					if (NPCHandler.npcs[i].npcType == 6143) {
						c.pcDamage += damage;
						PestControl.portalHealth[1] -= damage;
					}
					if (NPCHandler.npcs[i].npcType == 6144) {
						c.pcDamage += damage;
						PestControl.portalHealth[2] -= damage;
					}
					if (NPCHandler.npcs[i].npcType == 6145) {
						c.pcDamage += damage;
						PestControl.portalHealth[3] -= damage;
					}
				}
				boolean dropArrows = true;

				for (int noArrowId : c.NO_ARROW_DROP) {
					if (c.lastWeaponUsed == noArrowId) {
						dropArrows = false;
						break;
					}
				}
				if (dropArrows) {
					c.getItems().dropArrowNpc();
				}
				NPCHandler.npcs[i].underAttack = true;
				appendHit(NPCHandler.npcs[i], damage, 0, 1, 1);
				addCombatXP(1, damage);
				if (damage2 > -1) {
					appendHit(NPCHandler.npcs[i], damage2, 0, 1, 2);
					c.totalDamageDealt += damage2;
					addCombatXP(1, damage2);
				}
				if (c.inBarbDef) {
					c.barbDamage += damage;
					if (damage2 > 0)
						c.barbDamage += damage2;
				}
				if (c.killingNpcIndex != c.oldNpcIndex) {
					c.totalDamageDealt = 0;
				}
				c.killingNpcIndex = c.oldNpcIndex;
				c.totalDamageDealt += damage;
				c.curses().soulSplit(i, damage);
				if (c.vengOn) {
					appendVengeanceNpc(i, damage);
				}
			} else if (c.projectileStage > 0) { // magic hit damage
				int damage = Misc.random(finalMagicDamage(c));
				if (godSpells()) {
					if (System.currentTimeMillis() - c.godSpellDelay < Constants.GOD_SPELL_CHARGE) {
						damage += Misc.random(10);
					}
				}
				boolean magicFailed = false;
				int bonusAttack = getBonusAttack(i);
				if (Misc.random(NPCHandler.npcs[i].defence) > 10 + Misc.random(mageAtk()) + bonusAttack) {
					damage = 0;
					magicFailed = true;
				} else if (NPCHandler.npcs[i].npcType == 2881 || NPCHandler.npcs[i].npcType == 2882) {
					damage = 0;
					magicFailed = true;
				}

				if (NPCHandler.npcs[i].inMulti() && multis()) {
					c.barrageCount = 0;
					for (int j = 0; j < NPCHandler.npcs.length; j++) {
						if (NPCHandler.npcs[j] != null) {
							if (c.barrageCount >= 9)
								break;
							int nX = NPCHandler.npcs[j].getX(), nY = NPCHandler.npcs[j].getY(),
									pX = NPCHandler.npcs[i].getX(), pY = NPCHandler.npcs[i].getY();
							if ((nX - pX == -1 || nX - pX == 0 || nX - pX == 1)
									&& (nY - pY == -1 || nY - pY == 0 || nY - pY == 1) && i != j)
								appendMultiBarrageNPC(j, c.magicFailed);
						}
					}
				}

				if (NPCHandler.npcs[i].HP - damage < 0) {
					damage = NPCHandler.npcs[i].HP;
				}
				c.getPA().refreshSkill(3);
				c.getPA().refreshSkill(6);
				if (damage > 0 && PestControl.isInGame(c)) {
					if (NPCHandler.npcs[i].npcType == 6142) {
						c.pcDamage += damage;
						PestControl.portalHealth[0] -= damage;
					}
					if (NPCHandler.npcs[i].npcType == 6143) {
						c.pcDamage += damage;
						PestControl.portalHealth[1] -= damage;
					}
					if (NPCHandler.npcs[i].npcType == 6144) {
						c.pcDamage += damage;
						PestControl.portalHealth[2] -= damage;
					}
					if (NPCHandler.npcs[i].npcType == 6145) {
						c.pcDamage += damage;
						PestControl.portalHealth[3] -= damage;
					}
				}
				if (getEndGfxHeight() == 100 && !magicFailed) { // end GFX
					NPCHandler.npcs[i].gfx100(Player.MAGIC_SPELLS[c.oldSpellId][5]);
				} else if (!magicFailed) {
					NPCHandler.npcs[i].gfx0(Player.MAGIC_SPELLS[c.oldSpellId][5]);
				}

				if (magicFailed) {
					NPCHandler.npcs[i].gfx100(85);
				}
				if (!magicFailed) {
					int freezeDelay = getFreezeTime();// freeze
					if (freezeDelay > 0 && NPCHandler.npcs[i].freezeTimer == 0) {
						NPCHandler.npcs[i].freezeTimer = freezeDelay;
					}
					switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
					case 12901:
					case 12919: // blood spells
					case 12911:
					case 12929:
						int heal = Misc.random(damage / 2);
						if (c.constitution + heal >= c.maxConstitution) {
							c.constitution = c.maxConstitution;
						} else {
							c.constitution += heal;
						}
						c.getPA().refreshSkill(3);
						break;
					}

				}
				NPCHandler.npcs[i].underAttack = true;
				if (Player.MAGIC_SPELLS[c.oldSpellId][6] != 0) {
					appendHit(NPCHandler.npcs[i], damage, 0, 2, 1);
					addCombatXP(2, damage);
					c.totalDamageDealt += damage;
					if (c.inBarbDef)
						c.barbDamage += damage;
				}
				c.curses().soulSplit(i, damage);
				c.killingNpcIndex = c.oldNpcIndex;
				NPCHandler.npcs[i].updateRequired = true;
				c.usingMagic = false;
				c.castingMagic = false;
				c.oldSpellId = 0;
				if (c.vengOn) {
					appendVengeanceNpc(i, damage);
				}
			}
		}

		if (c.bowSpecShot <= 0) {
			c.oldNpcIndex = 0;
			c.projectileStage = 0;
			c.doubleHit = false;
			c.lastWeaponUsed = 0;
			c.bowSpecShot = 0;
		}
		if (c.bowSpecShot >= 2) {
			c.bowSpecShot = 0;
		}
		if (c.bowSpecShot == 1) {
			fireProjectileNpc();
			c.hitDelay = 2;
			c.bowSpecShot = 0;
		}
	}

	public void applyNpcMeleeDamage(final int i, int damageMask) {
		int damage = Misc.random(calculateMeleeMaxHit());
		boolean fullVeracsEffect = c.getPA().fullVeracs() && Misc.random(3) == 1;
		if (NPCHandler.npcs[i].HP - damage < 0) {
			damage = NPCHandler.npcs[i].HP;
		}

		if (!fullVeracsEffect) {
			if (Misc.random(NPCHandler.npcs[i].defence) > 10 + Misc.random(calculateMeleeAttack())) {
				damage = 0;
			} else if (NPCHandler.npcs[i].npcType == 2882 || NPCHandler.npcs[i].npcType == 2883) {
				damage = 0;
			}
		}
		boolean guthansEffect = false;
		if (c.getPA().fullGuthans()) {
			if (Misc.random(3) == 1) {
				guthansEffect = true;
			}
		}
		if (damage > 0 && PestControl.isInGame(c)) {
			if (NPCHandler.npcs[i].npcType == 6142) {
				c.pcDamage += damage;
				PestControl.portalHealth[0] -= damage;
			}
			if (NPCHandler.npcs[i].npcType == 6143) {
				c.pcDamage += damage;
				PestControl.portalHealth[1] -= damage;
			}
			if (NPCHandler.npcs[i].npcType == 6144) {
				c.pcDamage += damage;
				PestControl.portalHealth[2] -= damage;
			}
			if (NPCHandler.npcs[i].npcType == 6145) {
				c.pcDamage += damage;
				PestControl.portalHealth[3] -= damage;
			}
		}
		if (damage > 0 && guthansEffect) {
			c.constitution += damage;
			if (c.constitution > c.maxConstitution)
				c.constitution = c.maxConstitution;
			c.getPA().refreshSkill(3);
			NPCHandler.npcs[i].gfx0(398);
		}
		NPCHandler.npcs[i].underAttack = true;
		c.killingNpcIndex = c.npcIndex;
		c.lastNpcAttacked = i;
		switch (c.specEffect) {
		case 4:
			if (damage > 0) {
				if (c.constitution + damage > c.maxConstitution)
					if (c.constitution > c.maxConstitution)
						;
					else
						c.constitution = c.maxConstitution;
				else
					c.constitution += damage;
			}
			break;
		case 9:
			damage = korasiDamage(null);
			NPCHandler.npcs[i].gfx0(1730);
			if (NPCHandler.npcs[i].HP - damage < 0) {
				damage = NPCHandler.npcs[i].HP;
			}
			break;
		case 10:
			int hit1 = damage;
			int hit2, hit3, hit4;
			if (hit1 > 0)
				hit2 = hit1 / 2;
			else
				hit2 = Misc.random(NPCHandler.npcs[i].defence) > 10 + Misc.random(calculateMeleeAttack())
						? Misc.random(calculateMeleeMaxHit()) : 0;
			if (hit2 > 0)
				hit3 = hit2 / 2;
			else
				hit3 = Misc.random(NPCHandler.npcs[i].defence) > 10 + Misc.random(calculateMeleeAttack())
						? Misc.random(calculateMeleeMaxHit()) : 0;
			if (hit3 > 0)
				hit4 = hit3 + 1;
			else
				hit4 = Misc.random(NPCHandler.npcs[i].defence) > 10 + Misc.random(calculateMeleeAttack())
						? Misc.random(calculateMeleeMaxHit()) : 1;
			final int h3 = hit3;
			final int h4 = hit4;
			appendHit(NPCHandler.npcs[i], hit2, 0, 0, damageMask == 1 ? 2 : 1);
			GameEngine.getScheduler().schedule(new Task(1) {

				@Override
				protected void execute() {
					c.getCombat().appendHit(NPCHandler.npcs[i], h3, 0, 0, 1);
					c.getCombat().appendHit(NPCHandler.npcs[i], h4, 0, 0, 2);
					stop();
				}

			});
			break;

		}
		appendHit(NPCHandler.npcs[i], damage, 0, c.korasiSpec ? 2 : 0, damageMask);
		c.korasiSpec = false;
		addCombatXP(0, damage);
		c.specEffect = 0;
		c.totalDamageDealt += damage;
		if (c.inBarbDef)
			c.barbDamage += damage;
		c.curses().soulSplit(i, damage);
		if (c.vengOn) {
			appendVengeanceNpc(i, damage);
		}
	}

	public void fireProjectileNpc() {
		if (c.oldNpcIndex > 0) {
			if (NPCHandler.npcs[c.oldNpcIndex] != null) {
				c.projectileStage = 2;
				int pX = c.getX();
				int pY = c.getY();
				int nX = NPCHandler.npcs[c.oldNpcIndex].getX();
				int nY = NPCHandler.npcs[c.oldNpcIndex].getY();
				int offX = (pY - nY) * -1;
				int offY = (pX - nX) * -1;
				c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, getProjectileSpeed(), getRangeProjectileGFX(),
						43, 31, c.oldNpcIndex + 1, getStartDelay());
				if (usingDbow())
					c.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, getProjectileSpeed() - 20,
							getRangeProjectileGFX(), 43, 31, c.oldNpcIndex + 1, getStartDelay(), 29);
			}
		}
	}

	/**
	 * Attack Players - Similiar to attacking Npcs
	 **/

	public void attackPlayer(int i) {
		Player c2 = PlayerHandler.players[i]; // the player we are
		// attacking

		if (PlayerHandler.players[i] != null) {

			if (PlayerHandler.players[i].isDead) {
				resetPlayerAttack();
				return;
			}

			if (c.respawnTimer > 0 || PlayerHandler.players[i].respawnTimer > 0) {
				resetPlayerAttack();
				return;
			}

			if (!c.getCombat().checkReqs()) {
				return;
			}

			boolean sameSpot = c.absX == PlayerHandler.players[i].getX() && c.absY == PlayerHandler.players[i].getY();
			if (!c.goodDistance(PlayerHandler.players[i].getX(), PlayerHandler.players[i].getY(), c.getX(), c.getY(),
					25) && !sameSpot) {
				resetPlayerAttack();
				return;
			}

			if (PlayerHandler.players[i].respawnTimer > 0) {
				PlayerHandler.players[i].playerIndex = 0;
				resetPlayerAttack();
				return;
			}

			if (PlayerHandler.players[i].heightLevel != c.heightLevel) {
				resetPlayerAttack();
				return;
			}
			Following.triggerFollowing(i, 0, c);
			if (c.attackTimer <= 0) {
				c.usingBow = false;
				c.specEffect = 0;
				c.usingRangeWeapon = false;
				c.rangeItemUsed = 0;
				boolean usingBow = false;
				boolean usingArrows = false;
				boolean usingOtherRangeWeapons = false;
				boolean usingCross = c.playerEquipment[c.playerWeapon] == 9185
						|| c.playerEquipment[c.playerWeapon] == 18357;
				c.projectileStage = 0;

				if (c.absX == PlayerHandler.players[i].absX && c.absY == PlayerHandler.players[i].absY) {
					if (c.freezeTimer > 0) {
						resetPlayerAttack();
						return;
					}
					Following.triggerFollowing(i, 0, c);
					c.attackTimer = 0;
					return;
				}
				if (!c.usingMagic) {
					for (int bowId : c.BOWS) {
						if (c.playerEquipment[c.playerWeapon] == bowId) {
							usingBow = true;
							for (int arrowId : c.ARROWS) {
								if (c.playerEquipment[c.playerArrows] == arrowId) {
									usingArrows = true;
								}
							}
						}
					}

					for (int otherRangeId : c.OTHER_RANGE_WEAPONS) {
						if (c.playerEquipment[c.playerWeapon] == otherRangeId) {
							usingOtherRangeWeapons = true;
						}
					}
				}
				/**
				 * Checks if the player is not on the same X coord and Y coord
				 * and not using any range or mage
				 */
				if (c.getX() != c2.getX() && c.getY() != c2.getY() && !usingOtherRangeWeapons && !usingHally()
						&& !usingBow && !c.usingMagic) {
					c.faceUpdate(i + 32768); // face the player
					c.getPA().stopDiagonal(c2.getX(), c2.getY());// move to a
					// correct
					// spot
					return;
				}
				if (c.autocasting) {
					c.spellId = c.autocastId;
					c.usingMagic = true;
				}
				if (c.spellId > 0) {
					c.usingMagic = true;
				}
				c.attackTimer = getAttackDelay(
						c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
				c.formerWeapon = c.playerEquipment[c.playerWeapon];

				if (c.duelRule[9]) {
					boolean canUseWeapon = false;
					for (int funWeapon : Constants.FUN_WEAPONS) {
						if (c.playerEquipment[c.playerWeapon] == funWeapon) {
							canUseWeapon = true;
						}
					}
					if (!canUseWeapon) {
						c.sendMessage("You can only use fun weapons in this duel!");
						resetPlayerAttack();
						return;
					}
				}
				// c.sendMessage("Made it here3.");
				if (c.duelRule[DuelArena.RULE_RANGED] && (usingBow || usingOtherRangeWeapons)) {
					c.sendMessage("Range has been disabled in this duel!");
					return;
				}
				if (c.duelRule[DuelArena.RULE_MELEE] && (!usingBow && !usingOtherRangeWeapons && !c.usingMagic)) {
					c.sendMessage("Melee has been disabled in this duel!");
					return;
				}

				if (c.duelRule[DuelArena.RULE_MAGIC] && c.usingMagic) {
					c.sendMessage("Magic has been disabled in this duel!");
					resetPlayerAttack();
					return;
				}

				if ((!c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[i].getX(),
						PlayerHandler.players[i].getY(), 4) && (usingOtherRangeWeapons && !usingBow && !c.usingMagic))
						|| (!c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[i].getX(),
								PlayerHandler.players[i].getY(), 2)
								&& (!usingOtherRangeWeapons && usingHally() && !usingBow && !c.usingMagic))
						|| (!c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[i].getX(),
								PlayerHandler.players[i].getY(), getRequiredDistance())
								&& (!usingOtherRangeWeapons && !usingHally() && !usingBow && !c.usingMagic))
						|| (!c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[i].getX(),
								PlayerHandler.players[i].getY(), 10) && (usingBow || c.usingMagic))) {
					// c.sendMessage("Setting attack timer to 1");
					c.attackTimer = 1;
					if (!usingBow && !c.usingMagic && !usingOtherRangeWeapons && c.freezeTimer > 0)
						resetPlayerAttack();
					return;
				}

				if (!usingCross && !usingArrows && usingBow
						&& (c.playerEquipment[c.playerWeapon] < 4212 || c.playerEquipment[c.playerWeapon] > 4223)
						&& !c.usingMagic) {
					c.sendMessage("You have run out of arrows!");
					c.stopMovement();
					resetPlayerAttack();
					return;
				}
				if (correctBowAndArrows() < c.playerEquipment[c.playerArrows] && Constants.CORRECT_ARROWS && usingBow
						&& !usingCrystalBow() && !usingCross && !c.usingMagic) {
					c.sendMessage("You can't use "
							+ c.getItems().getItemName(c.playerEquipment[c.playerArrows]).toLowerCase() + " with a "
							+ c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase() + ".");
					c.stopMovement();
					resetPlayerAttack();
					return;
				}
				if (usingCross && !properBolts() && !c.usingMagic) {
					c.sendMessage("You must use bolts with a crossbow.");
					c.stopMovement();
					resetPlayerAttack();
					return;
				}

				if (usingBow || c.usingMagic || usingOtherRangeWeapons || usingHally()) {
					c.stopMovement();
				}

				if (!checkMagicReqs(c.spellId)) {
					c.stopMovement();
					resetPlayerAttack();
					return;
				}
				if (Region.blockedShot(c.getX(), c.getY(), c.heightLevel, NPCHandler.npcs[i].getX(),
						NPCHandler.npcs[i].getY())) {
					if ((c.usingBow || c.usingMagic || usingOtherRangeWeapons || c.autocasting))
						PathFinder.getPathFinder().findRoute(c, NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(),
								true, 8, 8);
					c.attackTimer = 0;
					return;
				}
				if (pathBlocked(c, c2)) {
					if ((c.usingBow || c.usingMagic || usingOtherRangeWeapons || c.autocasting))
						PathFinder.getPathFinder().findRoute(c, c2.absX, c2.absY, true, 8, 8);
					if (!c.usingBow && !c.usingMagic && !usingOtherRangeWeapons && !c.autocasting)
						PathFinder.getPathFinder().findRoute(c, c2.absX, c2.absY, true, 1, 1);
					c.attackTimer = 0;
					return;
				}
				c.faceUpdate(i + 32768);

				if (!DuelArena.isDueling(c)) {
					if (!c.attackedPlayers.contains(c.playerIndex)
							&& !PlayerHandler.players[c.playerIndex].attackedPlayers.contains(c.playerId)) {
						c.attackedPlayers.add(c.playerIndex);
						c.isSkulled = true;
						c.skullTimer = Constants.SKULL_TIMER;
						c.headIconPk = 0;
						c.getPA().requestUpdates();
					}
				}
				c.specAccuracy = 1.0;
				c.specDamage = 1.0;
				c.delayedDamage = c.delayedDamage2 = 0;
				if (c.usingSpecial && !c.usingMagic) {
					if (c.duelRule[DuelArena.RULE_SPECIAL_ATTACK] && DuelArena.isDueling(c)) {
						c.sendMessage("Special attacks have been disabled during this duel!");
						c.usingSpecial = false;
						c.getItems().updateSpecialBar();
						resetPlayerAttack();
						return;
					}
					if (checkSpecAmount(c.playerEquipment[c.playerWeapon])) {
						c.lastArrowUsed = c.playerEquipment[c.playerArrows];
						activateSpecial(c.playerEquipment[c.playerWeapon], i);
						Following.triggerFollowing(c.playerIndex, 0, c);
						return;
					} else {
						c.sendMessage("You don't have the required special energy to use this attack.");
						c.usingSpecial = false;
						c.getItems().updateSpecialBar();
						c.playerIndex = 0;
						return;
					}
				}
				if (usingBow || c.usingMagic || usingOtherRangeWeapons) {
					c.mageFollow = true;
				} else {
					c.mageFollow = false;
				}
				if (!c.usingMagic) {
					int anim = getWepAnim(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					c.startAnimation(anim);
					if (anim == 423)
						c.attackTimer++;
					c.mageFollow = false;
				} else {
					c.startAnimation(Player.MAGIC_SPELLS[c.spellId][2]);
					c.mageFollow = true;
					Following.triggerFollowing(c.playerIndex, 0, c);
				}
				PlayerHandler.players[i].underAttackBy = c.playerId;
				PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
				PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
				PlayerHandler.players[i].killerId = c.playerId;
				c.lastArrowUsed = 0;
				c.rangeItemUsed = 0;
				if (!usingBow && !c.usingMagic && !usingOtherRangeWeapons) { // melee
					// hit
					// delay
					Following.triggerFollowing(PlayerHandler.players[c.playerIndex].playerId, 0, c);
					c.getItems();
					c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					c.delayedDamage = Misc.random(calculateMeleeMaxHit());
					c.projectileStage = 0;
					c.oldPlayerIndex = i;
				}

				if (usingBow && !usingOtherRangeWeapons && !c.usingMagic || usingCross) { // range
																							// hit
																							// delay
					if (c.playerEquipment[c.playerWeapon] >= 4212 && c.playerEquipment[c.playerWeapon] <= 4223) {
						c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
						c.crystalBowArrowCount++;
					} else {
						c.rangeItemUsed = c.playerEquipment[c.playerArrows];
						c.getItems().deleteArrow();
					}
					if (c.fightMode == c.RAPID)
						c.attackTimer--;
					if (usingCross)
						c.usingBow = true;
					c.usingBow = true;
					Following.triggerFollowing(PlayerHandler.players[c.playerIndex].playerId, 0, c);
					c.lastWeaponUsed = c.playerEquipment[c.playerWeapon];
					c.lastArrowUsed = c.playerEquipment[c.playerArrows];
					c.gfx100(getRangeStartGFX());
					c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					c.projectileStage = 1;
					c.oldPlayerIndex = i;
					fireProjectilePlayer();
				}

				if (usingOtherRangeWeapons) { // knives, darts, etc hit delay
					c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
					c.getItems().deleteEquipment();
					c.usingRangeWeapon = true;
					Following.triggerFollowing(PlayerHandler.players[c.playerIndex].playerId, 0, c);
					c.gfx100(getRangeStartGFX());
					if (c.fightMode == c.RAPID)
						c.attackTimer--;
					c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					c.projectileStage = 1;
					c.oldPlayerIndex = i;
					fireProjectilePlayer();
				}

				if (c.usingMagic) { // magic hit delay
					int pX = c.getX();
					int pY = c.getY();
					int nX = PlayerHandler.players[i].getX();
					int nY = PlayerHandler.players[i].getY();
					int offX = (pY - nY) * -1;
					int offY = (pX - nX) * -1;
					c.castingMagic = true;
					c.projectileStage = 2;
					if (Player.MAGIC_SPELLS[c.spellId][3] > 0) {
						if (getStartGfxHeight() == 100) {
							c.gfx100(Player.MAGIC_SPELLS[c.spellId][3]);
						} else {
							c.gfx0(Player.MAGIC_SPELLS[c.spellId][3]);
						}
					}
					if (Player.MAGIC_SPELLS[c.spellId][4] > 0) {
						if (!c.goodDistance(pX, pY, nX, nY, 2))
							c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 78, Player.MAGIC_SPELLS[c.spellId][4],
									getStartHeight(), getEndHeight(), -i - 1, getStartDelay());
					}
					if (c.autocastId > 0) {
						Following.triggerFollowing(c.playerIndex, 0, c);
						c.followDistance = 5;
					}
					c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					c.oldPlayerIndex = i;
					c.oldSpellId = c.spellId;
					c.spellId = 0;
					Player o = PlayerHandler.players[i];
					if (Player.MAGIC_SPELLS[c.oldSpellId][0] == 12891 && o.isMoving) {
						c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 85, 368, 25, 25, -i - 1,
								getStartDelay());
					}
					c.magicFailed = canHitMage(o);
					int freezeDelay = getFreezeTime();// freeze time
					if (freezeDelay > 0 && PlayerHandler.players[i].freezeTimer <= -3 && !c.magicFailed) {
						PlayerHandler.players[i].freezeTimer = freezeDelay;
						o.resetWalkingQueue();
						o.getCombat().resetPlayerAttack();
						o.sendMessage("You have been frozen!");
						o.orb = false;
						o.frozenBy = c.playerId;
					}
					if (!c.autocasting && c.spellId <= 0)
						c.playerIndex = 0;
				}
				applyLeeches(i);

				if (usingBow && Constants.CRYSTAL_BOW_DEGRADES) { // crystal bow
					// degrading
					if (c.playerEquipment[c.playerWeapon] == 4212) { // new
						// crystal
						// bow
						// becomes
						// full
						// bow
						// on
						// the
						// first
						// shot
						c.getItems().wearItem(4214, 1, 3);
					}

					if (c.crystalBowArrowCount >= 250) {
						switch (c.playerEquipment[c.playerWeapon]) {
						case 4223: // 1/10 bow
							c.getItems().wearItem(-1, 1, 3);
							c.sendMessage("Your crystal bow has fully degraded.");
							if (!c.getItems().addItem(4207, 1)) {
								ItemHandler.createGroundItem(c, 4207, c.getX(), c.getY(), c.heightLevel, 1, c.getId());
							}
							c.crystalBowArrowCount = 0;
							break;

						default:
							c.getItems().wearItem(++c.playerEquipment[c.playerWeapon], 1, 3);
							c.sendMessage("Your crystal bow degrades.");
							c.crystalBowArrowCount = 0;
							break;
						}
					}
				}
				if (c.leechDelay == 0)
					c.leechDelay = 7;
			}
		}
	}

	public boolean usingCrystalBow() {
		return c.playerEquipment[c.playerWeapon] >= 4212 && c.playerEquipment[c.playerWeapon] <= 4223;
	}

	public void appendHit(Player c2, int damage, int mask, int icon, boolean playerHitting) {
		int soak = 0;
		boolean maxHit = false;
		if (c2.getVariables().teleTimer > 0)
			return;
		if (c2.getVariables().CANNOT_BE_ATTACKED) {
			return;
		}
		if (DuelArena.isDueling(c2) && c2.killedDuelOpponent) {
			return;
		}
		if (playerHitting) {
			switch (icon) {
			case 0:
				soak = c2.getCombat().damageSoaked(damage, "Melee");
				maxHit = damage >= calculateMeleeMaxHit() - 20;
				break;
			case 1:
				soak = c2.getCombat().damageSoaked(damage, "Ranged");
				maxHit = damage >= rangeMaxHit() - 20;
				break;
			case 2:
				soak = c2.getCombat().damageSoaked(damage, "Magic");
				maxHit = damage == finalMagicDamage(c);
				break;
			}
		}
		damage -= soak;
		if (damage > c2.constitution)
			damage = c2.constitution;
		c2.handleHitMask(damage, mask, icon, soak, maxHit);
		c2.dealDamage(damage);
		c2.getPA().refreshSkill(3);
	}

	public void appendHit(NPC n, int damage, int mask, int icon, int damageMask) {
		n.HP -= damage;
		boolean maxHit = false;
		if (c.getVariables().teleTimer > 0)
			return;
		switch (icon) {
		case 0:
			maxHit = damage >= calculateMeleeMaxHit() - 20;
			break;
		case 1:
			maxHit = damage >= rangeMaxHit() - 20;
			break;
		case 2:
			maxHit = damage == finalMagicDamage(c);
			break;
		}
		if (maxHit)
			mask = 1;
		switch (damageMask) {
		case 1:
			n.hitDiff = damage;
			n.hitUpdateRequired = true;
			n.updateRequired = true;
			n.hitIcon = icon;
			n.hitMask = mask;
			break;
		case 2:
			n.hitDiff2 = damage;
			n.hitUpdateRequired2 = true;
			n.updateRequired = true;
			c.doubleHit = false;
			n.hitIcon2 = icon;
			n.hitMask2 = mask;
			break;
		}
	}

	public void appendHit(Player c2, int damage, int mask, int icon, boolean playerHitting, int soak) {
		boolean maxHit = false;
		if (c2.getVariables().teleTimer > 0)
			return;
		if (c2.getVariables().CANNOT_BE_ATTACKED) {
			return;
		}
		if (DuelArena.isDueling(c2) && c2.killedDuelOpponent) {
			return;
		}
		if (playerHitting) {
			switch (icon) {
			case 0:
				maxHit = damage >= calculateMeleeMaxHit() - 20;
				break;
			case 1:
				maxHit = damage >= rangeMaxHit() - 20;
				break;
			case 2:
				maxHit = damage == finalMagicDamage(c);
				break;
			}
		}
		if (damage > c2.constitution)
			damage = c2.constitution;
		if (c.korasiSpec)
			maxHit = true;
		c2.handleHitMask(damage, mask, icon, soak, maxHit);
		c2.dealDamage(damage);
		c2.getPA().refreshSkill(3);
	}

	public void appendVengeance(int otherPlayer, int damage) {
		if (damage <= 0)
			return;
		Player o = PlayerHandler.players[otherPlayer];
		// Client o2 = (Client) PlayerHandler.players[otherPlayer];
		o.castVengeance = 0;
		o.forcedText = "Taste Vengeance!";
		o.forcedChatUpdateRequired = true;
		o.updateRequired = true;
		o.vengOn = false;
		if ((o.constitution - damage) > 0) {
			damage = (int) (damage * 0.75);
			if (damage > c.constitution)
				damage = c.constitution;
			appendHit(c, damage, 0, -1, true);
			o.setHitUpdateRequired(true);
			c.setHitUpdateRequired2(true);
			c.constitution -= damage;
		}
		c.updateRequired = true;
	}

	public int VengeanceDamage = 0;

	public void appendVengeanceNpc(final int i, int damage) {
		if (damage <= 0)
			return;
		NPC o = NPCHandler.npcs[i];
		c.forcedText = "Taste Vengeance!";
		c.forcedChatUpdateRequired = true;
		c.updateRequired = true;
		c.vengOn = false;
		c.castVengeance = 0;
		if ((o.HP - damage) > 0) {
			damage = (int) (damage * 0.75);
			VengeanceDamage = damage;
			GameEngine.getScheduler().schedule(new Task(1) {
				@Override
				public void execute() {
					appendHit(NPCHandler.npcs[i], VengeanceDamage, 0, 2, 1);
					this.stop();
				}
			});
		}
		c.updateRequired = true;
	}

	public void playerDelayedHit(int i) {
		if (PlayerHandler.players[i] != null) {
			if (PlayerHandler.players[i].isDead || c.isDead || c.constitution <= 0) {
				c.playerIndex = 0;
				return;
			}
			if (PlayerHandler.players[i].respawnTimer > 0) {
				c.faceUpdate(0);
				c.playerIndex = 0;
				return;
			}
			Player o = PlayerHandler.players[i];
			o.getPA().removeAllWindows();
			if (o.playerIndex <= 0 && o.npcIndex <= 0) {
				if (o.autoRet == 1) {
					o.playerIndex = c.playerId;
				}
			}
			if (o.attackTimer <= 3 || o.attackTimer == 0 && o.playerIndex == 0 && !c.castingMagic) { // block
																										// animation
				o.startAnimation(o.getCombat().getBlockEmote());
			}
			if (o.inTrade) {
				o.getTradeHandler().declineTrade(false);
			}
			if (c.projectileStage == 0) { // melee hit damage
				applyPlayerMeleeDamage(i, 1);
				if (c.doubleHit) {
					applyPlayerMeleeDamage(i, 2);
				}
			}

			if (!c.castingMagic && c.projectileStage > 0) { // range hit damage
				int damage = Misc.random(rangeMaxHit());
				int damage2 = -1;
				if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1) {
					damage2 = Misc.random(rangeMaxHit());
				}
				if (c.playerEquipment[3] == 9185) {
					if (Misc.random(10) == 1) {
						if (damage > 0) {
							EnchantedBolts.crossbowSpecial(c, i, damage);
							damage *= crossbowDamage;
						}
					}
				}
				if (Misc.random(10 + o.getCombat().calculateRangeDefence()) > Misc.random(10 + calculateRangeAttack())
						&& !ignoreDefence) {
					damage = 0;
				}

				if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1) {
					if (Misc.random(10 + o.getCombat().calculateRangeDefence()) > Misc
							.random(10 + calculateRangeAttack()))
						damage2 = 0;
				}

				if (c.dbowSpec) {
					o.gfx100(1100);
					if (damage < 8)
						damage = 8;
					if (damage2 < 8)
						damage2 = 8;
					c.dbowSpec = false;
				}
				if (o.curseActive[c.curses().DEFLECT_MISSILES]
						&& System.currentTimeMillis() - o.protRangeDelay > 1500) {
					damage = damage * 40 / 100;
					o.curses().deflect(c, damage, 1);
					if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1) {
						damage2 = damage2 * 40 / 100;
						o.curses().deflect(c, damage2, 1);
					}
				}
				if (o.prayerActive[17] && System.currentTimeMillis() - o.protRangeDelay > 1500) { // if
					// prayer
					// active
					// reduce
					// damage
					// by
					// half
					damage = damage * 60 / 100;
					if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1)
						damage2 = damage2 * 60 / 100;
				}
				if (PlayerHandler.players[i].constitution - damage < 0) {
					damage = PlayerHandler.players[i].constitution;
				}
				if (PlayerHandler.players[i].constitution - damage - damage2 < 0) {
					damage2 = PlayerHandler.players[i].constitution - damage;
				}
				if (damage < 0)
					damage = 0;
				if (damage2 < 0 && damage2 != -1)
					damage2 = 0;
				if (o.vengOn) {
					appendVengeance(i, damage);
					appendVengeance(i, damage2);
				}
				if (damage > 0)
					applyRecoil(damage, i);
				if (damage2 > 0)
					applyRecoil(damage2, i);
				boolean dropArrows = true;
				for (int noArrowId : c.NO_ARROW_DROP) {
					if (c.lastWeaponUsed == noArrowId) {
						dropArrows = false;
						break;
					}
				}
				if (dropArrows) {
					c.getItems().dropArrowPlayer();
				}
				PlayerHandler.players[i].underAttackBy = c.playerId;
				PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
				PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
				PlayerHandler.players[i].killerId = c.playerId;
				PlayerHandler.players[i].damageTaken[c.playerId] += damage;
				c.killedBy = PlayerHandler.players[i].playerId;
				int soak = o.getCombat().damageSoaked(damage, "Range");
				damage -= soak;
				appendHit(o, damage, 0, 1, true, soak);
				addCombatXP(1, damage);
				if (damage2 != -1) {
					int soak2 = o.getCombat().damageSoaked(damage2, "Melee");
					damage2 -= soak2;
					PlayerHandler.players[i].damageTaken[c.playerId] += damage2;
					appendHit(o, damage2, 0, 1, true, soak2);
					addCombatXP(0, damage2);
				}
				PlayerHandler.players[i].updateRequired = true;
				applySmite(i, damage);
				if (damage2 != -1) {
					applySmite(i, damage2);
					c.curses().soulSplit(i, damage2);
				}
				c.curses().soulSplit(i, damage);

			} else if (c.projectileStage > 0) { // magic hit damage
				int damage = Misc.random(finalMagicDamage(c));
				if (godSpells()) {
					if (System.currentTimeMillis() - c.godSpellDelay < Constants.GOD_SPELL_CHARGE) {
						damage += 10;
					}
				}
				if (c.magicFailed)
					damage = 0;
				if (o.curseActive[c.curses().DEFLECT_MAGIC] && System.currentTimeMillis() - o.protMageDelay > 1500) {
					damage = damage * 40 / 100;
					o.curses().deflect(c, 0, 0);
				}
				if (o.prayerActive[16] && System.currentTimeMillis() - o.protMageDelay > 1500) { // if
					// prayer
					// active
					// reduce
					// damage
					// by
					// half
					damage = damage * 60 / 100;
				}
				if (PlayerHandler.players[i].constitution - damage < 0) {
					damage = PlayerHandler.players[i].constitution;
				}
				if (o.vengOn)
					appendVengeance(i, damage);
				if (damage > 0)
					applyRecoil(damage, i);
				c.getPA().refreshSkill(3);
				c.getPA().refreshSkill(6);

				int endGFX = Player.MAGIC_SPELLS[c.oldSpellId][5];
				if (endGFX == 369 && o.orb) {
					endGFX = 1677;
				}
				o.orb = true;
				if (getEndGfxHeight() == 100 && !c.magicFailed) { // end GFX
					PlayerHandler.players[i].gfx100(Player.MAGIC_SPELLS[c.oldSpellId][5]);
				} else if (!c.magicFailed) {
					if (endGFX == 1677)
						PlayerHandler.players[i].gfx(1677, 50);
					else
						PlayerHandler.players[i].gfx0(Player.MAGIC_SPELLS[c.oldSpellId][5]);
				} else if (c.magicFailed) {
					PlayerHandler.players[i].gfx100(85);
				}

				if (!c.magicFailed) {
					if (System.currentTimeMillis() - PlayerHandler.players[i].reduceStat > 35000) {
						PlayerHandler.players[i].reduceStat = System.currentTimeMillis();
						switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
						case 12987:
						case 13011:
						case 12999:
						case 13023:
							PlayerHandler.players[i].playerLevel[0] -= ((o.getPA()
									.getLevelForXP(PlayerHandler.players[i].playerXP[0]) * 10) / 100);
							break;
						}
					}

					switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
					case 12445: // teleblock
						if (System.currentTimeMillis() - o.teleBlockDelay > o.teleBlockLength) {
							o.teleBlockDelay = System.currentTimeMillis();
							o.sendMessage("You have been teleblocked.");
							if (o.prayerActive[16] && System.currentTimeMillis() - o.protMageDelay > 1500
									|| o.curseActive[c.curses().DEFLECT_MAGIC]
											&& System.currentTimeMillis() - o.protMageDelay > 1500)
								o.teleBlockLength = 150000;
							else
								o.teleBlockLength = 300000;
						}
						break;

					case 12901:
					case 12919: // blood spells
					case 12911:
					case 12929:
						int heal = damage / 4;
						if (c.constitution + heal > c.maxConstitution) {
							c.constitution = c.maxConstitution;
						} else {
							c.constitution += heal;
						}
						break;

					case 1153:
						PlayerHandler.players[i].playerLevel[0] -= ((o.getPA()
								.getLevelForXP(PlayerHandler.players[i].playerXP[0]) * 5) / 100);
						o.sendMessage("Your attack level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(0);
						break;

					case 1157:
						PlayerHandler.players[i].playerLevel[2] -= ((o.getPA()
								.getLevelForXP(PlayerHandler.players[i].playerXP[2]) * 5) / 100);
						o.sendMessage("Your strength level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(2);
						break;

					case 1161:
						PlayerHandler.players[i].playerLevel[1] -= ((o.getPA()
								.getLevelForXP(PlayerHandler.players[i].playerXP[1]) * 5) / 100);
						o.sendMessage("Your defence level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(1);
						break;

					case 1542:
						PlayerHandler.players[i].playerLevel[1] -= ((o.getPA()
								.getLevelForXP(PlayerHandler.players[i].playerXP[1]) * 10) / 100);
						o.sendMessage("Your defence level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(1);
						break;

					case 1543:
						PlayerHandler.players[i].playerLevel[2] -= ((o.getPA()
								.getLevelForXP(PlayerHandler.players[i].playerXP[2]) * 10) / 100);
						o.sendMessage("Your strength level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(2);
						break;

					case 1562:
						PlayerHandler.players[i].playerLevel[0] -= ((o.getPA()
								.getLevelForXP(PlayerHandler.players[i].playerXP[0]) * 10) / 100);
						o.sendMessage("Your attack level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(0);
						break;
					}
				}

				PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
				PlayerHandler.players[i].underAttackBy = c.playerId;
				PlayerHandler.players[i].killerId = c.playerId;
				PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
				if (Player.MAGIC_SPELLS[c.oldSpellId][6] != 0) {
					PlayerHandler.players[i].damageTaken[c.playerId] += damage;
					c.totalPlayerDamageDealt += damage;
					if (!c.magicFailed) {
						addCombatXP(2, damage);
						int soak = o.getCombat().damageSoaked(damage, "Magic");
						damage -= soak;
						appendHit(o, damage, 0, 2, true, soak);
					} else
						addCombatXP(2, 0);
				}
				applySmite(i, damage);
				c.curses().soulSplit(i, damage);
				c.killedBy = PlayerHandler.players[i].playerId;
				PlayerHandler.players[i].updateRequired = true;
				c.usingMagic = false;
				c.castingMagic = false;
				if (o.getVariables().inMulti() && multis()) {
					c.barrageCount = 0;
					for (int j = 0; j < PlayerHandler.players.length; j++) {
						if (PlayerHandler.players[j] != null) {
							if (j == o.playerId)
								continue;
							if (c.barrageCount >= 9)
								break;
							if (o.goodDistance(o.getX(), o.getY(), PlayerHandler.players[j].getX(),
									PlayerHandler.players[j].getY(), 1))
								appendMultiBarrage(j, c.magicFailed);
						}
					}
				}
				c.getPA().refreshSkill(3);
				c.getPA().refreshSkill(6);
				c.oldSpellId = 0;
			}
		}
		c.getPA().requestUpdates();
		if (c.bowSpecShot <= 0) {
			c.oldPlayerIndex = 0;
			c.projectileStage = 0;
			c.lastWeaponUsed = 0;
			c.doubleHit = false;
			c.bowSpecShot = 0;
		}
		if (c.bowSpecShot != 0) {
			c.bowSpecShot = 0;
		}
	}

	public boolean multis() {
		switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
		case 12891:
		case 12881:
		case 13011:
		case 13023:
		case 12919: // blood spells
		case 12929:
		case 12963:
		case 12975:
			return true;
		}
		return false;

	}

	public void appendMultiBarrage(int playerId, boolean splashed) {
		if (PlayerHandler.players[playerId] != null) {
			Player c2 = PlayerHandler.players[playerId];
			if (c2.isDead || c2.respawnTimer > 0)
				return;
			if (checkMultiBarrageReqs(playerId)) {
				c.barrageCount++;
				if (Misc.random(mageAtk()) > Misc.random(mageDef()) && !c.magicFailed) {
					int spellGFX = Player.MAGIC_SPELLS[c.spellId][5];
					if (spellGFX == 369 && c2.orb) { // ORB
						spellGFX = 1677;
					}
					c2.orb = true;
					if (getEndGfxHeight() == 100) { // end GFX
						c2.gfx100(Player.MAGIC_SPELLS[c.oldSpellId][5]);
					} else {
						if (spellGFX == 1677)
							c2.gfx0(spellGFX);
						else
							c2.gfx0(Player.MAGIC_SPELLS[c.oldSpellId][5]);
					}
					int damage = Misc.random(finalMagicDamage(c));
					if (c2.prayerActive[12]) {
						damage *= (int) (.60);
					}
					if (c2.constitution - damage < 0) {
						damage = c2.constitution;
					}
					int soak = c2.getCombat().damageSoaked(damage, "Melee");
					damage -= soak;
					appendHit(c2, damage, 0, 2, true, soak);
					addCombatXP(2, damage);
					PlayerHandler.players[playerId].damageTaken[c.playerId] += damage;
					c.totalPlayerDamageDealt += damage;
					multiSpellEffect(playerId, damage);
				} else {
					c2.gfx100(85);
				}
			}
		}
	}

	public void appendMultiBarrageNPC(int npcId, boolean splashed) {
		if (NPCHandler.npcs[npcId] != null) {
			NPC n = NPCHandler.npcs[npcId];
			if (n.isDead || n.HP <= 0)
				return;
			if (checkMultiBarrageReqsNPC(npcId)) {
				c.barrageCount++;
				if (Misc.random(NPCHandler.npcs[npcId].defence) < (10 + Misc.random(mageAtk())) && !c.magicFailed) {
					if (getEndGfxHeight() == 100)
						n.gfx100(Player.MAGIC_SPELLS[c.oldSpellId][5]);
					else
						n.gfx0(Player.MAGIC_SPELLS[c.oldSpellId][5]);
					int damage = Misc.random(finalMagicDamage(c));
					if (n.HP - damage < 0) {
						damage = n.HP;
					}
					appendHit(n, damage, 0, 2, 1);
					addCombatXP(2, damage);
					n.underAttackBy = c.playerId;
					n.underAttack = true;
					if (c.inBarbDef)
						c.barbDamage += damage;
					c.totalDamageDealt += damage;
					multiSpellEffectNPC(npcId, damage);
				} else
					n.gfx100(85);
			}
		}
	}

	public void multiSpellEffect(int playerId, int damage) {
		switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
		case 13011:
		case 13023:
			if (System.currentTimeMillis() - PlayerHandler.players[playerId].reduceStat > 35000) {
				PlayerHandler.players[playerId].reduceStat = System.currentTimeMillis();
				PlayerHandler.players[playerId].playerLevel[0] -= ((PlayerHandler.players[playerId]
						.getLevelForXP(PlayerHandler.players[playerId].playerXP[0]) * 10) / 100);
			}
			break;
		case 12919: // blood spells
		case 12929:
			int heal = damage / 4;
			if (c.constitution + heal >= c.maxConstitution) {
				c.constitution = c.maxConstitution;
			} else {
				c.constitution += heal;
			}
			break;
		case 12891:
		case 12881:
			if (PlayerHandler.players[playerId].freezeTimer < -4) {
				PlayerHandler.players[playerId].freezeTimer = getFreezeTime();
				PlayerHandler.players[playerId].stopMovement();
			}
			break;
		}
	}

	public void multiSpellEffectNPC(int npcId, int damage) {
		switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
		case 12919: // blood spells
		case 12929:
			int heal = damage / 4;
			if (c.constitution + heal >= c.maxConstitution)
				c.constitution = c.maxConstitution;
			else
				c.constitution += heal;
			break;
		case 12891:
		case 12881:
			if (NPCHandler.npcs[npcId].freezeTimer == 0) {
				NPCHandler.npcs[npcId].freezeTimer = getFreezeTime();
			}
			break;
		}
	}

	public void applyPlayerMeleeDamage(final int i, int damageMask) {
		Player o = PlayerHandler.players[i];
		if (o == null) {
			return;
		}
		int damage = Misc.random(calculateMeleeMaxHit());
		int damage2 = damage;
		damage = calculateDefenceDamageReduction(i, damage2);
		if (c.playerEquipment[c.playerWeapon] == 5698 && o.poisonDamage <= 0 && Misc.random(3) == 1)
			o.getPA().appendPoison(i);
		boolean veracsEffect = false;
		boolean guthansEffect = false;
		if (c.getPA().fullVeracs()) {
			if (Misc.random(4) == 1) {
				veracsEffect = true;
			}
		}
		if (c.getPA().fullGuthans()) {
			if (Misc.random(4) == 1) {
				guthansEffect = true;
			}
		}
		if (damageMask != 1) {
			damage = c.delayedDamage2;
			c.delayedDamage = 0;
		}
		if (o.curseActive[c.curses().DEFLECT_MELEE] && System.currentTimeMillis() - o.protMeleeDelay > 1500
				&& !veracsEffect) {
			damage = damage * 40 / 100;
			o.curses().deflect(c, damage, 0);
		}
		if (o.prayerActive[18] && System.currentTimeMillis() - o.protMeleeDelay > 1500 && !veracsEffect) { // if
																											// prayer
																											// active
																											// reduce
																											// damage
																											// by
																											// 40%
			damage = damage * 60 / 100;
		}
		if (damage > 0 && guthansEffect) {
			c.constitution += damage;
			if (c.constitution > c.maxConstitution)
				c.constitution = c.maxConstitution;
			o.gfx0(398);
		}
		if (c.ssSpec && damageMask == 2) {
			damage = 5 + Misc.random(11);
			c.ssSpec = false;
		}
		if (PlayerHandler.players[i].constitution - damage < 0) {
			damage = PlayerHandler.players[i].constitution;
		}
		if (o.vengOn && damage > 0)
			appendVengeance(i, damage);
		if (damage > 0)
			applyRecoil(damage, i);
		switch (c.specEffect) {
		case 1: // dragon scimmy special
			if (damage > 0) {
				if (o.prayerActive[16] || o.prayerActive[17] || o.prayerActive[18]) {
					o.headIcon = -1;
					o.getPA().sendFrame36(CombatPrayer.PRAYER_GLOW[16], 0);
					o.getPA().sendFrame36(CombatPrayer.PRAYER_GLOW[17], 0);
					o.getPA().sendFrame36(CombatPrayer.PRAYER_GLOW[18], 0);
				}
				if (o.curseActive[o.curses().DEFLECT_MELEE] || o.curseActive[o.curses().DEFLECT_MAGIC]
						|| o.curseActive[o.curses().DEFLECT_MISSILES]) {
					o.headIcon = -1;
					o.curses().deactivate(o.curses().DEFLECT_MAGIC);
					o.curses().deactivate(o.curses().DEFLECT_MISSILES);
					o.curses().deactivate(o.curses().DEFLECT_MELEE);
				}
				o.sendMessage("You have been injured!");
				o.stopPrayerDelay = System.currentTimeMillis();
				o.prayerActive[16] = false;
				o.prayerActive[17] = false;
				o.prayerActive[18] = false;
				o.getPA().requestUpdates();
			}
			break;
		case 2:
			if (damage > 0) {
				if (o.freezeTimer <= 0)
					o.freezeTimer = 30;
				o.gfx0(369);
				o.sendMessage("You have been frozen.");
				o.frozenBy = c.playerId;
				o.stopMovement();
				c.sendMessage("You freeze your enemy.");
			}
			break;
		case 3:
			if (damage > 0) {
				o.playerLevel[1] -= damage;
				o.sendMessage("You feel weak.");
				if (o.playerLevel[1] < 1)
					o.playerLevel[1] = 1;
				o.getPA().refreshSkill(1);
			}
			break;
		case 4:
			if (damage > 0) {
				if (c.constitution + damage > c.maxConstitution)
					if (c.constitution > c.maxConstitution)
						;
					else
						c.constitution = c.maxConstitution;
				else
					c.constitution += damage;
				c.getPA().refreshSkill(3);
			}
			break;
		case 9:
			damage = korasiDamage(o);
			if (PlayerHandler.players[i].constitution - damage < 0) {
				damage = PlayerHandler.players[i].constitution;
			}
			o.gfx0(1730);
			break;
		case 10:
			int hit1 = damage;
			int hit2 = hit1 > 0 ? hit1 / 2
					: meleeHitSuccess(calculateMeleeAttack(), o.getCombat().calculateMeleeDefence())
							? Misc.random(calculateMeleeMaxHit()) : 0;
			final int hit3 = hit2 > 0 ? hit2 / 2
					: meleeHitSuccess(calculateMeleeAttack(), o.getCombat().calculateMeleeDefence())
							? Misc.random(calculateMeleeMaxHit()) : 0;
			final int hit4 = hit3 > 0 ? hit3 + 1
					: meleeHitSuccess(calculateMeleeAttack(), o.getCombat().calculateMeleeDefence())
							? Misc.random(calculateMeleeMaxHit()) : 1;
			int soak = o.getCombat().damageSoaked(hit2, "Melee");
			hit2 -= soak;
			appendHit(o, hit2, 0, 0, true, soak);
			GameEngine.getScheduler().schedule(new Task(1) {

				@Override
				protected void execute() {
					c.getCombat().appendHit(PlayerHandler.players[i], hit3, 0, 0, true);
					c.getCombat().appendHit(PlayerHandler.players[i], hit4, 0, 0, true);
					this.stop();
				}

			});
			break;
		}
		c.specEffect = 0;
		PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
		PlayerHandler.players[i].underAttackBy = c.playerId;
		PlayerHandler.players[i].killerId = c.playerId;
		PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
		if (c.killedBy != PlayerHandler.players[i].playerId)
			c.totalPlayerDamageDealt = 0;
		c.killedBy = PlayerHandler.players[i].playerId;
		applySmite(i, damage);
		c.curses().soulSplit(i, damage);
		int soak = o.getCombat().damageSoaked(damage, c.korasiSpec ? "Magic" : "Melee");
		damage -= soak;
		appendHit(o, damage, 0, c.korasiSpec ? 2 : 0, true, soak);
		addCombatXP(0, damage);
		switch (damageMask) {
		case 1:
			PlayerHandler.players[i].damageTaken[c.playerId] += damage;
			c.totalPlayerDamageDealt += damage;
			break;

		case 2:
			PlayerHandler.players[i].damageTaken[c.playerId] += damage;
			c.totalPlayerDamageDealt += damage;
			c.doubleHit = false;
			break;
		}
		c.korasiSpec = false;
	}

	/**
	 * @author Chris
	 * @param defence
	 */

	public boolean calculateBlockedHit(int defence) {
		if (defence > 450 && Misc.random(5) == 1)
			return true;
		if (defence > 400 && Misc.random(5) == 1)
			return true;
		if (defence > 350 && Misc.random(6) == 1)
			return true;
		if (defence > 300 && Misc.random(6) == 1)
			return true;
		if (Misc.random(6) == 1 && defence > 150)
			return true;
		if (defence > 10 && Misc.random(7) == 1)
			return true;
		return false;
	}

	/**
	 * @author Chris & Thelife
	 * @param i
	 *            (Returns player index for meleeDefence())
	 * @param damage
	 * @return
	 */
	public int calculateDefenceDamageReduction(int i, int damage) {
		Player o = PlayerHandler.players[i];
		int defence = o.getCombat().calculateMeleeDefence();
		if (calculateBlockedHit(defence))
			return 0;
		if (defence > 450)
			return damage *= .635;
		if (defence > 400)
			return damage *= .655;
		if (defence > 350)
			return damage *= .715;
		if (defence > 300)
			return damage *= .775;
		if (defence > 250)
			return damage *= .835;
		if (defence > 200)
			return damage *= .85;
		if (defence > 150)
			return damage *= .9125;
		if (defence > 100)
			return damage *= .975;
		if (defence > 10)
			return damage *= .99;
		return damage;
	}

	public void applySmite(int index, int damage) {
		if (!c.prayerActive[23])
			return;
		if (damage <= 0)
			return;
		if (PlayerHandler.players[index] != null) {
			Player c2 = PlayerHandler.players[index];
			c2.playerLevel[5] -= damage / 40;
			if (c2.playerLevel[5] <= 0) {
				c2.playerLevel[5] = 0;
				CombatPrayer.resetPrayers(c2);
			}
			c2.getPA().refreshSkill(5);
		}

	}

	public void applyLeeches(int index) {
		if (Misc.random(20) == 0) {
			leechAttack(index);
		}
		if (Misc.random(20) == 0) {
			leechDefence(index);
		}
		if (Misc.random(20) == 0) {
			leechStrength(index);
		}
		if (Misc.random(20) == 0) {
			leechSpecial(index);
		}
		if (Misc.random(20) == 0) {
			leechRanged(index);
		}
		if (Misc.random(20) == 0) {
			leechMagic(index);
		}
		if (Misc.random(20) == 0) {
			leechEnergy(index);
		}
	}

	public void applynpcLeeches(int index) {
		if (Misc.random(20) == 0) {
			npcleechAttack(index);
		}
		if (Misc.random(20) == 0) {
			npcleechDefence(index);
		}
		if (Misc.random(20) == 0) {
			npcleechStrength(index);
		}
		if (Misc.random(20) == 0) {
			npcleechSpecial(index);
		}
		if (Misc.random(20) == 0) {
			npcleechRanged(index);
		}
		if (Misc.random(20) == 0) {
			npcleechMagic(index);
		}
		if (Misc.random(20) == 0) {
			npcleechEnergy(index);
		}
	}

	public void leechAttack(int index) {
		if (!c.curseActive[10])
			return;
		if (PlayerHandler.players[index] != null) {
			final Player c2 = PlayerHandler.players[index];
			final int pX = c.getX();
			final int pY = c.getY();
			final int oX = c2.getX();
			final int oY = c2.getY();
			int offX = (pY - oY) * -1;
			int offY = (pX - oX) * -1;
			c.sendMessage("You leech your opponent's attack.");
			c.gfx0(2234);
			c.startAnimation(12575);
			c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2231, 43, 31, -c.oldPlayerIndex - 1, 1);
			c.leechAttackDelay = 2;
			GameEngine.getScheduler().schedule(new Task(1) {
				@Override
				public void execute() {
					if (c.leechAttackDelay > 0) {
						c.leechAttackDelay--;
					}
					if (c.leechAttackDelay == 1) {
						c2.gfx0(2232);
						if (c.attackMultiplier < 1.10) {
							c.attackMultiplier += 0.01;
						}
						if (c2.attackMultiplier > 0.80) {
							c2.attackMultiplier -= 0.01;
						}
					}
					if (c.leechAttackDelay == 0) {
						this.stop();
					}
				}
			});
		}
	}

	public void leechRanged(int index) {
		if (!c.curseActive[11])
			return;
		if (PlayerHandler.players[index] != null) {
			final Player c2 = PlayerHandler.players[index];
			final int pX = c.getX();
			final int pY = c.getY();
			final int oX = c2.getX();
			final int oY = c2.getY();
			int offX = (pY - oY) * -1;
			int offY = (pX - oX) * -1;
			c.sendMessage("You leech your opponent's range.");
			c.gfx0(2235);
			c.startAnimation(12575);
			c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2236, 43, 31, -c.oldPlayerIndex - 1, 0);
			c.leechRangedDelay = 2;
			GameEngine.getScheduler().schedule(new Task(1) {
				@Override
				public void execute() {
					if (c.leechRangedDelay > 0) {
						c.leechRangedDelay--;
					}
					if (c.leechRangedDelay == 1) {
						c2.gfx0(2238);
						if (c.rangedMultiplier < 1.10) {
							c.rangedMultiplier += 0.01;
						}
						if (c2.rangedMultiplier > 0.80) {
							c2.rangedMultiplier -= 0.01;
						}
					}
					if (c.leechRangedDelay == 0) {
						this.stop();
					}
				}
			});
		}
	}

	public void leechMagic(int index) {
		if (!c.curseActive[12])
			return;
		if (PlayerHandler.players[index] != null) {
			final Player c2 = PlayerHandler.players[index];
			final int pX = c.getX();
			final int pY = c.getY();
			final int oX = c2.getX();
			final int oY = c2.getY();
			int offX = (pY - oY) * -1;
			int offY = (pX - oX) * -1;
			c.sendMessage("You leech your opponent's magic.");
			c.gfx0(2239);
			c.startAnimation(12575);
			c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2240, 43, 31, -c.oldPlayerIndex - 1, 2);
			c.leechMagicDelay = 2;
			GameEngine.getScheduler().schedule(new Task(1) {
				@Override
				public void execute() {
					if (c.leechMagicDelay > 0) {
						c.leechMagicDelay--;
					}
					if (c.leechMagicDelay == 1) {
						c2.gfx0(2242);
						if (c.magicMultiplier < 1.10) {
							c.magicMultiplier += 0.01;
						}
						if (c2.magicMultiplier > 0.80) {
							c2.magicMultiplier -= 0.01;
						}
					}
					if (c.leechMagicDelay == 0) {
						this.stop();
					}
				}
			});
		}
	}

	public void leechDefence(int index) {
		if (!c.curseActive[13])
			return;
		if (PlayerHandler.players[index] != null) {
			final Player c2 = PlayerHandler.players[index];
			final int pX = c.getX();
			final int pY = c.getY();
			final int oX = c2.getX();
			final int oY = c2.getY();
			int offX = (pY - oY) * -1;
			int offY = (pX - oX) * -1;
			c.sendMessage("You leech your opponent's defence.");
			c.gfx0(2243);
			c.startAnimation(12575);
			c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2244, 43, 31, -c.oldPlayerIndex - 1, 3);
			c.leechDefenceDelay = 2;
			GameEngine.getScheduler().schedule(new Task(1) {
				@Override
				public void execute() {
					if (c.leechDefenceDelay > 0) {
						c.leechDefenceDelay--;
					}
					if (c.leechDefenceDelay == 1) {
						c2.gfx0(2246);
						if (c.defenceMultiplier < 1.10) {
							c.defenceMultiplier += 0.01;
						}
						if (c2.defenceMultiplier > 0.80) {
							c2.defenceMultiplier -= 0.01;
						}
					}
					if (c.leechDefenceDelay == 0) {
						this.stop();
					}
				}
			});
		}
	}

	public void leechStrength(int index) {
		if (!c.curseActive[14])
			return;
		if (PlayerHandler.players[index] != null) {
			final Player c2 = PlayerHandler.players[index];
			final int pX = c.getX();
			final int pY = c.getY();
			final int oX = c2.getX();
			final int oY = c2.getY();
			int offX = (pY - oY) * -1;
			int offY = (pX - oX) * -1;
			c.sendMessage("You leech your opponent's strength.");
			c.gfx0(2247);
			c.startAnimation(12575);
			c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2248, 43, 31, -c.oldPlayerIndex - 1, 4);
			c.leechStrengthDelay = 2;
			GameEngine.getScheduler().schedule(new Task(1) {
				@Override
				public void execute() {
					if (c.leechStrengthDelay > 0) {
						c.leechStrengthDelay--;
					}
					if (c.leechStrengthDelay == 1) {
						c2.gfx0(2250);
						if (c.strengthMultiplier < 1.10) {
							c.strengthMultiplier += 0.01;
						}
						if (c2.strengthMultiplier > 0.80) {
							c2.strengthMultiplier -= 0.01;
						}
					}
					if (c.leechStrengthDelay == 0) {
						this.stop();
					}
				}
			});
		}
	}

	public void leechEnergy(int index) {
		if (!c.curseActive[15])
			return;
		if (PlayerHandler.players[index] != null) {
			final Player c2 = PlayerHandler.players[index];
			final int pX = c.getX();
			final int pY = c.getY();
			final int oX = c2.getX();
			final int oY = c2.getY();
			int offX = (pY - oY) * -1;
			int offY = (pX - oX) * -1;
			c.sendMessage("You leech your opponent's run energy.");
			c.gfx0(2234);
			c.startAnimation(12575);
			c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2252, 43, 31, -c.oldPlayerIndex - 1, 5);
			c.leechEnergyDelay = 2;
			GameEngine.getScheduler().schedule(new Task(1) {
				@Override
				public void execute() {
					if (c.leechEnergyDelay > 0) {
						c.leechEnergyDelay--;
					}
					if (c.leechEnergyDelay == 1) {
						c2.gfx0(2254);
					}
					if (c.leechEnergyDelay == 0) {
						this.stop();
					}
				}
			});
		}
	}

	public void leechSpecial(int index) {
		if (!c.curseActive[16])
			return;
		if (PlayerHandler.players[index] != null) {
			final Player c2 = PlayerHandler.players[index];
			final int pX = c.getX();
			final int pY = c.getY();
			final int oX = c2.getX();
			final int oY = c2.getY();
			int offX = (pY - oY) * -1;
			int offY = (pX - oX) * -1;
			c.sendMessage("You leech your opponent's special attack.");
			c.gfx0(2253);
			c.startAnimation(12575);
			c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2256, 43, 31, -c.oldPlayerIndex - 1, 6);
			c.leechSpecialDelay = 2;
			GameEngine.getScheduler().schedule(new Task(1) {
				@Override
				public void execute() {
					if (c.leechSpecialDelay > 0) {
						c.leechSpecialDelay--;
					}
					if (c.leechSpecialDelay == 1) {
						c2.gfx0(2258);
						if (c.specAmount >= 10)
							return;
						if (c2.specAmount <= 0)
							return;
						c.specAmount += 1;
						c2.specAmount -= 1;
						c2.sendMessage("Your special attack has been drained.");
					}
					if (c.leechSpecialDelay == 0) {
						this.stop();
					}
				}
			});
		}
	}

	public void npcleechAttack(int index) {
		if (!c.curseActive[10])
			return;
		if (c.oldNpcIndex > 0) {
			if (NPCHandler.npcs[c.oldNpcIndex] != null) {
				final int pX = c.getX();
				final int pY = c.getY();
				final int nX = NPCHandler.npcs[c.oldNpcIndex].getX();
				final int nY = NPCHandler.npcs[c.oldNpcIndex].getY();
				final int offX = (pY - nY) * -1;
				final int offY = (pX - nX) * -1;
				c.sendMessage("You leech your opponent's attack.");
				c.gfx0(2234);
				c.startAnimation(12575);
				c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2231, 43, 31, -c.oldNpcIndex - 1, 1);
				c.leechAttackDelay = 2;
				GameEngine.getScheduler().schedule(new Task(1) {
					@Override
					public void execute() {
						if (c.leechAttackDelay > 0) {
							c.leechAttackDelay--;
						}
						if (c.leechAttackDelay == 1) {
							NPCHandler.npcs[c.oldNpcIndex].gfx0(2232);
							if (c.attackMultiplier < 1.10) {
								c.attackMultiplier += 0.01;
							}
						}
						if (c.leechAttackDelay == 0) {
							this.stop();
						}
					}
				});
			}
		}
	}

	public void npcleechRanged(int index) {
		if (!c.curseActive[11])
			return;
		if (c.oldNpcIndex > 0) {
			if (NPCHandler.npcs[c.oldNpcIndex] != null) {
				final int pX = c.getX();
				final int pY = c.getY();
				final int nX = NPCHandler.npcs[c.oldNpcIndex].getX();
				final int nY = NPCHandler.npcs[c.oldNpcIndex].getY();
				final int offX = (pY - nY) * -1;
				final int offY = (pX - nX) * -1;
				c.sendMessage("You leech your opponent's range.");
				c.gfx0(2235);
				c.startAnimation(12575);
				c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2236, 43, 31, -c.oldNpcIndex - 1, 0);
				c.leechRangedDelay = 2;
				GameEngine.getScheduler().schedule(new Task(1) {
					@Override
					public void execute() {
						if (c.leechRangedDelay > 0) {
							c.leechRangedDelay--;
						}
						if (c.leechRangedDelay == 1) {
							NPCHandler.npcs[c.oldNpcIndex].gfx0(2238);
							if (c.rangedMultiplier < 1.10) {
								c.rangedMultiplier += 0.01;
							}
						}
						if (c.leechRangedDelay == 0) {
							this.stop();
						}
					}
				});
			}
		}
	}

	public void npcleechMagic(int index) {
		if (!c.curseActive[12])
			return;
		if (c.oldNpcIndex > 0) {
			if (NPCHandler.npcs[c.oldNpcIndex] != null) {
				final int pX = c.getX();
				final int pY = c.getY();
				final int nX = NPCHandler.npcs[c.oldNpcIndex].getX();
				final int nY = NPCHandler.npcs[c.oldNpcIndex].getY();
				final int offX = (pY - nY) * -1;
				final int offY = (pX - nX) * -1;
				c.sendMessage("You leech your opponent's magic.");
				c.gfx0(2239);
				c.startAnimation(12575);
				c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2240, 43, 31, -c.oldNpcIndex - 1, 2);
				c.leechMagicDelay = 2;
				GameEngine.getScheduler().schedule(new Task(1) {
					@Override
					public void execute() {
						if (c.leechMagicDelay > 0) {
							c.leechMagicDelay--;
						}
						if (c.leechMagicDelay == 1) {
							NPCHandler.npcs[c.oldNpcIndex].gfx0(2242);
							if (c.magicMultiplier < 1.10) {
								c.magicMultiplier += 0.01;
							}
						}
						if (c.leechMagicDelay == 0) {
							this.stop();
						}
					}
				});
			}
		}
	}

	public void npcleechDefence(int index) {
		if (!c.curseActive[13])
			return;
		if (c.oldNpcIndex > 0) {
			if (NPCHandler.npcs[c.oldNpcIndex] != null) {
				final int pX = c.getX();
				final int pY = c.getY();
				final int nX = NPCHandler.npcs[c.oldNpcIndex].getX();
				final int nY = NPCHandler.npcs[c.oldNpcIndex].getY();
				final int offX = (pY - nY) * -1;
				final int offY = (pX - nX) * -1;
				c.sendMessage("You leech your opponent's defence.");
				c.gfx0(2243);
				c.startAnimation(12575);
				c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2244, 43, 31, -c.oldNpcIndex - 1, 3);
				c.leechDefenceDelay = 2;
				GameEngine.getScheduler().schedule(new Task(1) {
					@Override
					public void execute() {
						if (c.leechDefenceDelay > 0) {
							c.leechDefenceDelay--;
						}
						if (c.leechDefenceDelay == 1) {
							NPCHandler.npcs[c.oldNpcIndex].gfx0(2246);
							if (c.defenceMultiplier < 1.10) {
								c.defenceMultiplier += 0.01;
							}
						}
						if (c.leechDefenceDelay == 0) {
							this.stop();
						}
					}
				});
			}
		}
	}

	public void npcleechStrength(int index) {
		if (!c.curseActive[14])
			return;
		if (c.oldNpcIndex > 0) {
			if (NPCHandler.npcs[c.oldNpcIndex] != null) {
				final int pX = c.getX();
				final int pY = c.getY();
				final int nX = NPCHandler.npcs[c.oldNpcIndex].getX();
				final int nY = NPCHandler.npcs[c.oldNpcIndex].getY();
				final int offX = (pY - nY) * -1;
				final int offY = (pX - nX) * -1;
				c.sendMessage("You leech your opponent's strength.");
				c.gfx0(2247);
				c.startAnimation(12575);
				c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2248, 43, 31, -c.oldNpcIndex - 1, 4);
				c.leechStrengthDelay = 2;
				GameEngine.getScheduler().schedule(new Task(1) {
					@Override
					public void execute() {
						if (c.leechStrengthDelay > 0) {
							c.leechStrengthDelay--;
						}
						if (c.leechStrengthDelay == 1) {
							NPCHandler.npcs[c.oldNpcIndex].gfx0(2250);
							if (c.strengthMultiplier < 1.10) {
								c.strengthMultiplier += 0.01;
							}
						}
						if (c.leechStrengthDelay == 0) {
							this.stop();
						}
					}
				});
			}
		}
	}

	public void npcleechEnergy(int index) {
		if (!c.curseActive[15])
			return;
		if (c.oldNpcIndex > 0) {
			if (NPCHandler.npcs[c.oldNpcIndex] != null) {
				final int pX = c.getX();
				final int pY = c.getY();
				final int nX = NPCHandler.npcs[c.oldNpcIndex].getX();
				final int nY = NPCHandler.npcs[c.oldNpcIndex].getY();
				final int offX = (pY - nY) * -1;
				final int offY = (pX - nX) * -1;
				c.sendMessage("You leech your opponent's run energy.");
				c.gfx0(2251);
				c.startAnimation(12575);
				c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2252, 43, 31, -c.oldNpcIndex - 1, 5);
				c.leechEnergyDelay = 2;
				GameEngine.getScheduler().schedule(new Task(1) {
					@Override
					public void execute() {
						if (c.leechEnergyDelay > 0) {
							c.leechEnergyDelay--;
						}
						if (c.leechEnergyDelay == 1) {
							NPCHandler.npcs[c.oldNpcIndex].gfx0(2254);
						}
						if (c.leechEnergyDelay == 0) {
							this.stop();
						}
					}
				});
			}
		}
	}

	public void npcleechSpecial(int index) {
		if (!c.curseActive[16])
			return;
		if (c.oldNpcIndex > 0) {
			if (NPCHandler.npcs[c.oldNpcIndex] != null) {
				final int pX = c.getX();
				final int pY = c.getY();
				final int nX = NPCHandler.npcs[c.oldNpcIndex].getX();
				final int nY = NPCHandler.npcs[c.oldNpcIndex].getY();
				final int offX = (pY - nY) * -1;
				final int offY = (pX - nX) * -1;
				c.sendMessage("You leech your opponent's special attack.");
				c.gfx0(2255);
				c.startAnimation(12575);
				c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2256, 43, 31, -c.oldNpcIndex - 1, 6);
				c.leechSpecialDelay = 2;
				GameEngine.getScheduler().schedule(new Task(1) {
					@Override
					public void execute() {
						if (c.leechSpecialDelay > 0) {
							c.leechSpecialDelay--;
						}
						if (c.leechSpecialDelay == 1) {
							NPCHandler.npcs[c.oldNpcIndex].gfx0(2258);
							if (c.specAmount >= 10)
								return;
							c.specAmount += 1;
						}
						if (c.leechSpecialDelay == 0) {
							this.stop();
						}
					}
				});
			}
		}
	}

	public void fireProjectilePlayer() {
		if (c.oldPlayerIndex > 0) {
			if (PlayerHandler.players[c.oldPlayerIndex] != null) {
				c.projectileStage = 2;
				int pX = c.getX();
				int pY = c.getY();
				int oX = PlayerHandler.players[c.oldPlayerIndex].getX();
				int oY = PlayerHandler.players[c.oldPlayerIndex].getY();
				int offX = (pY - oY) * -1;
				int offY = (pX - oX) * -1;
				if (!c.msbSpec)
					c.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, getProjectileSpeed(),
							getRangeProjectileGFX(), 43, 31, -c.oldPlayerIndex - 1, getStartDelay(), 16);
				else if (c.msbSpec) {
					c.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, getProjectileSpeed(),
							getRangeProjectileGFX(), 43, 31, -c.oldPlayerIndex - 1, getStartDelay(), 10);
					c.msbSpec = false;
				}
				if (usingDbow())
					c.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, getProjectileSpeed(),
							getRangeProjectileGFX(), 60, 31, -c.oldPlayerIndex - 1, getStartDelay(), 30);
			}
		}
	}

	public boolean usingDbow() {
		return c.playerEquipment[c.playerWeapon] == 11235;
	}

	/**
	 * Specials
	 **/

	public void activateSpecial(int weapon, int i) {
		if (c.npcIndex > 0 && NPCHandler.npcs[i] == null) {
			return;
		}
		if (c.playerIndex > 0 && PlayerHandler.players[i] == null) {
			return;
		}
		c.doubleHit = false;
		c.specEffect = 0;
		c.projectileStage = 0;
		c.specMaxHitIncrease = 2;
		if (c.npcIndex > 0) {
			c.oldNpcIndex = i;
		} else if (c.playerIndex > 0) {
			c.oldPlayerIndex = i;
			PlayerHandler.players[i].underAttackBy = c.playerId;
			PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
			PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
			PlayerHandler.players[i].killerId = c.playerId;
		}
		switch (weapon) {

		case 1305: // dragon long
			c.gfx100(248);
			c.startAnimation(1058);
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			c.specAccuracy = 1.10;
			c.specDamage = 1.20;
			break;

		case 1215: // dragon daggers
		case 1231:
		case 5680:
		case 5698:
			c.gfx100(252);
			c.startAnimation(1062);
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			c.doubleHit = true;
			c.specAccuracy = 1.10;
			c.specDamage = 1.08;
			break;

		case 11730:
			final Player o1 = PlayerHandler.players[c.playerIndex];
			c.startAnimation(7072);
			c.gfx100(1224);
			if (o1 != null)
				o1.gfx100(1194);
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			c.doubleHit = true;
			// c.korasiSpec = true;
			c.ssSpec = true;
			c.specAccuracy = 1.30;
			break;

		case 4151:
		case 15445:
		case 15444:
		case 15443:
		case 15442:
		case 15441:
			if (NPCHandler.npcs[i] != null) {
				NPCHandler.npcs[i].gfx0(2108);
			}
			c.specAccuracy = 1.10;
			c.startAnimation(11956);
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			break;

		case 14484:
			c.startAnimation(10961);
			c.gfx0(1950);
			c.specDamage = -50;
			c.specAccuracy = 1.26;
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			c.specEffect = 10;
			break;

		case 13905: // Vesta spear
			c.startAnimation(10499);
			c.gfx0(1835);
			c.specAccuracy = 4.25;
			// c.specEffect = 6;
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			break;

		case 13899: // Vesta LongSword
			c.startAnimation(10502);
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase() + 1);
			c.specDamage = 1.20;
			c.specAccuracy = 4.8;
			break;
		case 13883: // Morrigan Throwing Axe
			c.usingRangeWeapon = true;
			c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
			c.getItems().deleteArrow();
			c.lastWeaponUsed = weapon;
			c.startAnimation(10504);
			c.gfx0(1838);
			c.hitDelay = 3;
			c.specAccuracy = 1.90;
			c.specDamage = 1.20;
			c.projectileStage = 1;
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			if (c.fightMode == 2)
				c.attackTimer--;
			if (c.playerIndex > 0)
				fireProjectilePlayer();
			else if (c.npcIndex > 0)
				fireProjectileNpc();
			break;
		case 13879: // Morrigan Javeline
			c.usingRangeWeapon = true;
			c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
			c.getItems().deleteArrow();
			c.lastWeaponUsed = weapon;
			c.startAnimation(10501);
			c.gfx0(1836);
			c.specAccuracy = 2.00;
			c.specDamage = 1.30;
			c.hitDelay = 3;
			c.projectileStage = 1;
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			if (c.fightMode == 2)
				c.attackTimer--;
			if (c.playerIndex > 0)
				fireProjectilePlayer();
			else if (c.npcIndex > 0)
				fireProjectileNpc();
			break;

		case 13902: // Statius
			if (c.fullStatius()) {
				c.startAnimation(10505);
				c.gfx0(1840);
				c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase() + 1);
				c.specDamage = 1.12;
				c.specAccuracy = 1.15;
			} else {
				c.startAnimation(10505);
				c.gfx0(1840);
				c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase() + 1);
				c.specDamage = 1.00;
				c.specAccuracy = 1.25;
			}
			break;
		case 19784:
		case 19780:
			c.startAnimation(14788);
			c.gfx(1729, 0);
			c.korasiSpec = true;
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase() + 1);
			c.specEffect = 9;
			c.specAccuracy = 1.4;
			break;

		case 11694: // Armadyl godsword
			c.startAnimation(7074);
			c.specDamage = 1.35;
			c.specAccuracy = 1.4;
			c.gfx0(1222);
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			break;

		case 11700: // Zamorak godsword
			c.startAnimation(7070);
			c.gfx0(1221);
			c.specAccuracy = 1.10;
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			c.specEffect = 2;
			break;

		case 11696: // Bandos godsword
			c.startAnimation(7073);
			c.gfx0(1223);
			c.specDamage = 1.10;
			c.specAccuracy = 1.5;
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			c.specEffect = 3;
			break;

		case 11698: // Saradomin godsword
			c.startAnimation(7071);
			c.gfx0(1220);
			c.specAccuracy = 1.25;
			c.specEffect = 4;
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			break;

		case 1249:
			c.startAnimation(405);
			c.gfx100(253);
			if (c.playerIndex > 0) {
				Player o = PlayerHandler.players[i];
				o.getPA().getSpeared(c.absX, c.absY);
			}
			break;

		case 3204: // d hally
			try {
				c.gfx100(282);
				c.startAnimation(1203);
				c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
				if (NPCHandler.npcs[i] != null && c.npcIndex > 0) {
					if (!c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(), 1)) {
						c.doubleHit = true;
					}
				}
				if (PlayerHandler.players[i] != null && c.playerIndex > 0) {
					if (!c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[i].getX(),
							PlayerHandler.players[i].getY(), 1)) {
						c.doubleHit = true;
						c.delayedDamage2 = Misc.random(calculateMeleeMaxHit());
					}
				}
			} catch (Exception e) {
				System.out.println("SPECIAL ATTACK CRASHES SERVER, ERROR:");
				e.printStackTrace();
			}
			break;

		case 4153: // gmaul
			c.startAnimation(1667);
			c.gfx100(337);
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()) + 1;
			c.specDamage = 1.15;
			c.specAccuracy = 2.00;
			break;

		case 4587: // dscimmy
			c.gfx100(347);
			c.specEffect = 1;
			c.startAnimation(1872);
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			break;

		case 1434: // mace
			c.startAnimation(1060);
			c.gfx100(251);
			c.specMaxHitIncrease = 3;
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()) + 1;
			c.specDamage = 1.35;
			c.specAccuracy = 1.15;
			break;

		case 859: // magic long
			c.usingBow = true;
			c.bowSpecShot = 3;
			c.rangeItemUsed = c.playerEquipment[c.playerArrows];
			c.getItems().deleteArrow();
			c.lastWeaponUsed = weapon;
			c.startAnimation(426);
			c.gfx100(250);
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			c.projectileStage = 1;
			if (c.fightMode == c.RAPID)
				c.attackTimer--;
			break;

		case 861: // magic short
			c.usingBow = true;
			c.bowSpecShot = 1;
			c.rangeItemUsed = c.playerEquipment[c.playerArrows];
			c.getItems().deleteArrow();
			c.lastWeaponUsed = weapon;
			c.startAnimation(1074);
			c.hitDelay = 3;
			c.projectileStage = 1;
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			if (c.fightMode == c.RAPID)
				c.attackTimer--;
			if (c.playerIndex > 0)
				fireProjectilePlayer();
			else if (c.npcIndex > 0)
				fireProjectileNpc();
			break;

		case 11235: // dark bow
			c.usingBow = true;
			c.dbowSpec = true;
			c.rangeItemUsed = c.playerEquipment[c.playerArrows];
			c.getItems().deleteArrow();
			c.getItems().deleteArrow();
			c.lastWeaponUsed = weapon;
			c.startAnimation(426);
			c.projectileStage = 1;
			c.gfx100(getRangeStartGFX());
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			c.hitDelay2 = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()) + 1;
			if (c.fightMode == c.RAPID)
				c.attackTimer--;
			if (c.playerIndex > 0)
				fireProjectilePlayer();
			else if (c.npcIndex > 0)
				fireProjectileNpc();
			c.specAccuracy = 1.75;
			c.specDamage = 1.65;
			break;
		}
		c.delayedDamage = Misc.random(calculateMeleeMaxHit());
		c.delayedDamage2 = Misc.random(calculateMeleeMaxHit());
		c.usingSpecial = false;
		c.getItems().updateSpecialBar();
	}

	public boolean checkSpecAmount(int weapon) {
		if (c.playerEquipment[c.playerRing] == 19669) {
			c.specAmount += c.specAmount * 0.1;
		}
		switch (weapon) {
		case 19784:
		case 19780:
			if (c.specAmount >= 6) {
				c.specAmount -= 6;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		case 1249:
		case 1215:
		case 1231:
		case 5680:
		case 5698:
		case 13899:
		case 1305:
		case 1434:
			if (c.specAmount >= 2.5) {
				c.specAmount -= 2.5;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		case 4151:
		case 15445:
		case 15444:
		case 15443:
		case 15442:
		case 15441:
		case 11694:
		case 11698:
		case 4153:
		case 14484:
		case 13902:
		case 13883: // morrigan throwing axe
		case 13879: // Morrigan Javeline
			if (c.specAmount >= 5) {
				c.specAmount -= 5;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		case 3204:
			if (c.specAmount >= 3) {
				c.specAmount -= 3;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		case 1377:
		case 11696:
		case 11730:
		case 13905:
		case 15486:
			if (c.specAmount >= 10) {
				c.specAmount -= 10;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		case 4587:
		case 859:
		case 861:
		case 11235:
		case 11700:
			if (c.specAmount >= 5.5) {
				c.specAmount -= 5.5;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		default:
			return true; // incase u want to test a weapon
		}
	}

	public void resetPlayerAttack() {
		c.usingMagic = false;
		c.npcIndex = 0;
		c.faceUpdate(0);
		c.playerIndex = 0;
		Following.resetFollow(c);
		// c.sendMessage("Reset attack.");
	}

	public int getCombatDifference(int combat1, int combat2) {
		if (combat1 > combat2) {
			return (combat1 - combat2);
		}
		if (combat2 > combat1) {
			return (combat2 - combat1);
		}
		return 0;
	}

	/**
	 * Get killer id
	 **/

	public int getKillerId(int playerId) {
		int oldDamage = 0;
		int killerId = 0;
		for (int i = 1; i < Constants.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				if (PlayerHandler.players[i].killedBy == playerId) {
					if (PlayerHandler.players[i].withinDistance(PlayerHandler.players[playerId])) {
						if (PlayerHandler.players[i].totalPlayerDamageDealt > oldDamage) {
							oldDamage = PlayerHandler.players[i].totalPlayerDamageDealt;
							killerId = i;
						}
					}
					PlayerHandler.players[i].totalPlayerDamageDealt = 0;
					PlayerHandler.players[i].killedBy = 0;
				}
			}
		}
		return killerId;
	}

	/**
	 * Adding combat XP
	 **/
	public void addCombatXP(int attackType, int damage) {
		// Attack type is based on the kind of damage that is being done
		// (Ranged, mage, melee)
		Chaotic.degrade(c, getAttackDelay(c.getItems().getItemName(c.formerWeapon).toLowerCase()));
		c.getPA().addSkillXP(damage * SkillHandler.XPRates.MELEE.getXPRate() / 3
				- (damage * SkillHandler.XPRates.MELEE.getXPRate() / 4), 3);
		switch (attackType) {
		case 0: // Melee
			switch (c.fightMode) {
			case 0: // Accurate
				c.getPA().addSkillXP(damage * SkillHandler.XPRates.MELEE.getXPRate() / 3, 0);
				break;
			case 1: // Aggressive
				c.getPA().addSkillXP(damage * SkillHandler.XPRates.MELEE.getXPRate() / 3, 2);
				break;
			case 2: // Block
				c.getPA().addSkillXP(damage * SkillHandler.XPRates.MELEE.getXPRate() / 3, 1);
				break;
			case 3: // Controlled
				for (int i = 0; i < 3; i++)
					c.getPA().addSkillXP(damage * SkillHandler.XPRates.MELEE.getXPRate() / 6, i);// 1.3
				break;
			}
			break;
		case 1: // Ranged
			switch (c.fightMode) {
			case 0: // Accurate
			case 1: // Rapid
				c.getPA().addSkillXP(damage * SkillHandler.XPRates.RANGE.getXPRate() / 4, 4);
				break;
			case 3: // Block
				c.getPA().addSkillXP(damage * SkillHandler.XPRates.MELEE.getXPRate() / 2, 1);
				c.getPA().addSkillXP(damage * SkillHandler.XPRates.RANGE.getXPRate() / 2, 4);
				break;
			}
			break;
		case 2: // Magic
			int magicXP = (SkillHandler.XPRates.MAGIC.getXPRate() * damage) + Player.MAGIC_SPELLS[c.oldSpellId][7];
			c.getPA().addSkillXP(magicXP, 6);
			break;
		}
	}

	/**
	 * Wildy and duel info
	 **/

	public boolean checkReqs() {
		if (PlayerHandler.players[c.playerIndex] == null) {
			return false;
		}
		if (c.playerIndex == c.playerId)
			return false;
		if (c.inPits && PlayerHandler.players[c.playerIndex].inPits)
			return true;
		/*
		 * if (c.inCwSafe() || PlayerHandler.players[c.playerIndex].inCwSafe())
		 * { return false; }
		 */
		if (PlayerHandler.players[c.playerIndex].getVariables().inDuelArena() && !DuelArena.isDueling(c)
				&& !c.usingMagic) {
			if (Boundaries.checkBoundaries(Area.ARENAS, c.getX(), c.getY()) || DuelArena.isDueling(c)) {
				c.sendMessage("You can't challenge inside the arena!");
				return false;
			}
			Player o = PlayerHandler.players[c.playerIndex];
			c.Dueling.requestDuel(o, c, true);
			return false;
		}
		Player o = PlayerHandler.players[c.playerIndex];
		if (o != null && DuelArena.isDueling(o) && DuelArena.isDueling(c)) {
			if (o.opponent == c) {
				return true;
			} else {
				c.sendMessage("This isn't your opponent!");
				return false;
			}
		}
		if (!PlayerHandler.players[c.playerIndex].getVariables().inWild()) {
			c.sendMessage("That player is not in the wilderness.");
			c.stopMovement();
			c.getCombat().resetPlayerAttack();
			return false;
		}
		if (!c.getVariables().inWild()) {
			c.sendMessage("You are not in the wilderness.");
			c.stopMovement();
			c.getCombat().resetPlayerAttack();
			return false;
		}
		if (Constants.COMBAT_LEVEL_DIFFERENCE) {
			int combatDif1 = c.getCombat().getCombatDifference(c.calculateCombatLevelWOSumm(),
					PlayerHandler.players[c.playerIndex].calculateCombatLevelWOSumm());
			if (combatDif1 > c.wildLevel || combatDif1 > PlayerHandler.players[c.playerIndex].wildLevel) {
				c.sendMessage("Your combat level difference is too great to attack that player here.");
				c.stopMovement();
				c.getCombat().resetPlayerAttack();
				return false;
			}
		}

		if (Constants.SINGLE_AND_MULTI_ZONES) {
			if (!PlayerHandler.players[c.playerIndex].getVariables().inMulti()) { // single
				// combat
				// zones
				if (PlayerHandler.players[c.playerIndex].underAttackBy != c.playerId
						&& PlayerHandler.players[c.playerIndex].underAttackBy != 0) {
					c.sendMessage("That player is already in combat.");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return false;
				}
				if (PlayerHandler.players[c.playerIndex].playerId != c.underAttackBy && c.underAttackBy != 0
						|| c.underAttackBy2 > 0) {
					c.sendMessage("You are already in combat.");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return false;
				}
			}
		}
		return true;
	}

	public boolean checkMultiBarrageReqs(int i) {
		if (PlayerHandler.players[i] == null) {
			return false;
		}
		if (i == c.playerId)
			return false;
		if (c.inPits && PlayerHandler.players[i].inPits)
			return true;
		if (!PlayerHandler.players[i].getVariables().inWild()) {
			return false;
		}
		/*
		 * if (PlayerHandler.players[i].inCwSafe()) { return false; }
		 */
		if (Constants.COMBAT_LEVEL_DIFFERENCE) {
			int combatDif1 = c.getCombat().getCombatDifference(c.calculateCombatLevelWOSumm(),
					PlayerHandler.players[i].calculateCombatLevelWOSumm());
			if (combatDif1 > c.wildLevel || combatDif1 > PlayerHandler.players[i].wildLevel) {
				c.sendMessage("Your combat level difference is too great to attack that player here.");
				return false;
			}
		}

		if (Constants.SINGLE_AND_MULTI_ZONES) {
			if (!PlayerHandler.players[i].getVariables().inMulti()) { // single
																		// combat
																		// zones
				if (PlayerHandler.players[i].underAttackBy != c.playerId
						&& PlayerHandler.players[i].underAttackBy != 0) {
					return false;
				}
				if (PlayerHandler.players[i].playerId != c.underAttackBy && c.underAttackBy != 0) {
					c.sendMessage("You are already in combat.");
					return false;
				}
			}
		}
		return true;
	}

	public boolean checkMultiBarrageReqsNPC(int i) {
		if (NPCHandler.npcs[i] == null)
			return false;
		return true;
	}

	/**
	 * Weapon stand, walk, run, etc emotes
	 **/

	public void getPlayerAnimIndex(String weaponName) {
		c.playerStandIndex = 0x328;
		c.playerTurnIndex = 0x337;
		c.playerWalkIndex = 0x333;
		c.playerTurn180Index = 0x334;
		c.playerTurn90CWIndex = 0x335;
		c.playerTurn90CCWIndex = 0x336;
		c.playerRunIndex = 0x338;

		if (weaponName.contains("ahrim")) {
			c.playerStandIndex = 809;
			c.playerWalkIndex = 1146;
			c.playerRunIndex = 1210;
			return;
		}

		if (weaponName.contains("staff") || weaponName.contains("halberd") || weaponName.contains("guthan")
				|| weaponName.contains("rapier") || weaponName.contains("wand")) {
			weaponInfo(12010, 1146, 1210);
			c.playerTurnIndex = 1205;
			c.playerTurn180Index = 1206;
			c.playerTurn90CWIndex = 1207;
			c.playerTurn90CCWIndex = 1208;
			return;
		}

		if (weaponName.contains("dharok")) {
			c.playerStandIndex = 0x811;
			c.playerWalkIndex = 0x67F;
			c.playerRunIndex = 12001;
			return;
		}

		if (weaponName.contains("Verac's")) {
			weaponInfo(2061, 1830, 824);
			return;
		}

		if (weaponName.contains("karil")) {
			c.playerStandIndex = 2074;
			c.playerWalkIndex = 2076;
			c.playerRunIndex = 2077;
			return;
		}

		if (weaponName.contains("2h sword") || weaponName.contains("godsword") || weaponName.contains("saradomin sw")) {
			weaponInfo(10266, 7046, 7039);
			c.playerTurnIndex = 7040;
			c.playerTurn180Index = 7045;
			c.playerTurn90CWIndex = 7043;
			c.playerTurn90CCWIndex = 7044;
			return;
		}

		if (weaponName.contains("sword") || weaponName.contains("scimitar")) {
			weaponInfo(15069, 15073, 15070);
			c.playerTurnIndex = 15075;
			c.playerTurn180Index = 15075;
			c.playerTurn90CWIndex = 15076;
			c.playerTurn90CCWIndex = 15077;
			return;
		}

		if (weaponName.contains("bow")) {
			c.playerStandIndex = 808;
			c.playerWalkIndex = 819;
			c.playerRunIndex = 824;
			return;
		}

		switch (c.playerEquipment[c.playerWeapon]) {
		case 4151:
		case 15445:
		case 15444:
		case 15443:
		case 15442:
		case 15441:
			weaponInfo(11973, 11975, 11976);
			break;
		case 15241:
			c.playerStandIndex = 12155;
			c.playerWalkIndex = 12154;
			c.playerRunIndex = 12154;
			break;
		case 18355:
			c.playerStandIndex = 808;
			break;
		case 6528:
			c.playerStandIndex = 0x811;
			c.playerWalkIndex = 2064;
			c.playerRunIndex = 1664;
			break;
		case 18353:
		case 4153:
			c.playerStandIndex = 1662;
			c.playerWalkIndex = 1663;
			c.playerRunIndex = 1664;
			break;
		case 1305:
			c.playerStandIndex = 809;
			break;
		case 19784:
			c.playerStandIndex = 809;
			break;
		case 11716:
			c.playerRunIndex = 12016;
			c.playerWalkIndex = 12012;
			c.playerStandIndex = 12010;
			break;
		}
	}

	public void getPlayerAnimIndex2() {
		String weaponName = c.getItems().getItemName2(c.playerEquipment[Constants.WEAPON]).toLowerCase();
		c.playerStandIndex = 0x328;
		c.playerTurnIndex = 0x337;
		c.playerWalkIndex = 0x333;
		c.playerTurn180Index = 0x334;
		c.playerTurn90CWIndex = 0x335;
		c.playerTurn90CCWIndex = 0x336;
		c.playerRunIndex = 0x338;

		if (weaponName.contains("ahrim")) {
			c.playerStandIndex = 809;
			c.playerWalkIndex = 1146;
			c.playerRunIndex = 1210;
			return;
		}

		if (weaponName.contains("staff") || weaponName.contains("halberd") || weaponName.contains("guthan")
				|| weaponName.contains("rapier") || weaponName.contains("wand")) {
			weaponInfo(12010, 1146, 1210);
			c.playerTurnIndex = 1205;
			c.playerTurn180Index = 1206;
			c.playerTurn90CWIndex = 1207;
			c.playerTurn90CCWIndex = 1208;
			return;
		}

		if (weaponName.contains("dharok")) {
			c.playerStandIndex = 0x811;
			c.playerWalkIndex = 0x67F;
			c.playerRunIndex = 12001;
			return;
		}

		if (weaponName.contains("Verac's")) {
			weaponInfo(2061, 1830, 824);
			return;
		}

		if (weaponName.contains("karil")) {
			c.playerStandIndex = 2074;
			c.playerWalkIndex = 2076;
			c.playerRunIndex = 2077;
			return;
		}

		if (weaponName.contains("2h sword") || weaponName.contains("godsword") || weaponName.contains("saradomin sw")) {
			weaponInfo(10266, 7046, 7039);
			c.playerTurnIndex = 7040;
			c.playerTurn180Index = 7045;
			c.playerTurn90CWIndex = 7043;
			c.playerTurn90CCWIndex = 7044;
			return;
		}

		if (weaponName.contains("sword") || weaponName.contains("scimitar")) {
			weaponInfo(15069, 15073, 15070);
			c.playerTurnIndex = 15075;
			c.playerTurn180Index = 15075;
			c.playerTurn90CWIndex = 15076;
			c.playerTurn90CCWIndex = 15077;
			return;
		}

		if (weaponName.contains("bow")) {
			c.playerStandIndex = 808;
			c.playerWalkIndex = 819;
			c.playerRunIndex = 824;
			return;
		}

		switch (c.playerEquipment[c.playerWeapon]) {
		case 4151:
		case 15445:
		case 15444:
		case 15443:
		case 15442:
		case 15441:
			weaponInfo(11973, 11975, 11976);
			break;
		case 15241:
			c.playerStandIndex = 12155;
			c.playerWalkIndex = 12154;
			c.playerRunIndex = 12154;
			break;
		case 18355:
			c.playerStandIndex = 808;
			break;
		case 6528:
			c.playerStandIndex = 0x811;
			c.playerWalkIndex = 2064;
			c.playerRunIndex = 1664;
			break;
		case 18353:
		case 4153:
			c.playerStandIndex = 1662;
			c.playerWalkIndex = 1663;
			c.playerRunIndex = 1664;
			break;
		case 1305:
			c.playerStandIndex = 809;
			break;
		case 19784:
			c.playerStandIndex = 809;
			break;
		case 11716:
			c.playerRunIndex = 12016;
			c.playerWalkIndex = 12012;
			c.playerStandIndex = 12010;
			break;
		}
	}

	public void weaponInfo(int s, int w, int r) {
		c.playerStandIndex = s;
		c.playerWalkIndex = w;
		c.playerRunIndex = r;
	}

	/**
	 * Weapon emotes
	 **/

	public int getWepAnim(String weaponName) {
		if (c.playerEquipment[c.playerWeapon] <= 0) {
			if (c.combatType(c.ACCURATE) || c.combatType(c.BLOCK))
				return 422;
			if (c.combatType(c.AGGRESSIVE))
				return 423;
		}
		if (weaponName.contains("knife") || weaponName.contains("dart") || weaponName.contains("javelin")
				|| weaponName.contains("thrownaxe")) {
			return 806;
		}
		if (weaponName.contains("halberd")) {
			return 440;
		}
		if (weaponName.startsWith("dragon dagger")) {
			return 402;
		}
		if (weaponName.endsWith("dagger")) {
			return 412;
		}
		if (weaponName.contains("2h sword") || weaponName.contains("godsword")
				|| weaponName.contains("aradomin sword")) {
			if (c.combatType(c.AGGRESSIVE) || c.combatType(c.ACCURATE))
				return 7041;
			if (c.combatType(c.CONTROLLED))
				return 7048;
			if (c.combatType(c.DEFENSIVE))
				return 7049;
		}
		if (weaponName.contains("sword") && !weaponName.contains("korasi"))
			return 390;
		if (weaponName.contains("karil"))
			return 2075;
		if (weaponName.contains("bow") && !weaponName.contains("crossbow"))
			return 426;
		if (weaponName.contains("crossbow"))
			return 4230;
		if (weaponName.contains("battleaxe"))
			return 395;
		if (weaponName.contains("scimitar") || weaponName.contains("longsword") || weaponName.contains("korasi")) {
			switch (c.fightMode) {
			case 0:
				return 15071;
			case 1:
				return 15071;
			case 2:
				return 15071;
			case 3:
				return 15072;
			}
		}
		if (weaponName.contains("pickaxe")) {
			return 13035;
		}
		if (weaponName.contains("rapier")) {
			switch (c.fightMode) {
			case 0:
				return 12028;
			case 1:
				return 12028;
			case 2:
				return 12028;
			case 3:
				return 12028;
			}
		}

		switch (c.playerEquipment[c.playerWeapon]) { // if you don't want to use
		// strings
		case 6522:
			return 2614;
		case 4153: // Granite maul
			return 1665;
		case 4726: // Guthans spear
			return 2080;
		case 4747: // Torags hammers
			return 0x814;
		case 13905:
			return 13041;
		case 4718: // Dharok's greataxe
			if (c.combatType(c.AGGRESSIVE))
				return 2066;
			return 2067;
		case 4710: // Ahrim's staff
			return 406;
		case 14484:
			return 393;// claws
		case 11716:
			return 12006;// zamorakian spear
		case 4755: // Verac's flail
			return 2062;
		case 4734: // Karil's crossbow
			return 2075;
		case 13902: // Karil's crossbow
			return 13035;
		case 4151:
		case 15445:
		case 15444:
		case 15443:
		case 15442:
		case 15441:
			return 1658;
		case 15241:
			return 12152;
		case 6528: // Obby maul
		case 18353:
			return 2661;
		default:
			return 451;
		}
	}

	/**
	 * Block emotes
	 */
	public int getBlockEmote() {
		c.getItems();
		String shield = c.getItems().getItemName(c.playerEquipment[c.playerShield]).toLowerCase();
		c.getItems();
		String weapon = c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase();
		if (shield.contains("defender"))
			return 4177;
		if (shield.contains("book") && (weapon.contains("wand")))
			return 420;
		if (shield.contains("shield"))
			return 1156;
		if (weapon.contains("rapier") || weapon.contains("scimitar") || weapon.contains("korasi"))
			return 15074;
		if (weapon.contains("staff"))
			return 404;
		switch (c.playerEquipment[c.playerWeapon]) {
		case 1291:
		case 1293:
		case 1295:
		case 1297:
		case 1299:
		case 1301:
		case 1303:
		case 1305:
		case 6607:
		case 13474:
		case 13899:
		case 13901:
		case 13923:
		case 13925:
		case 13982:
		case 13984:
		case 16024:
		case 16025:
		case 16026:
		case 16027:
		case 16028:
		case 16029:
		case 16030:
		case 16031:
		case 16032:
		case 16033:
		case 16034:
		case 16383:
		case 16385:
		case 16387:
		case 16389:
		case 16391:
		case 16393:
		case 16395:
		case 16397:
		case 16399:
		case 16401:
		case 16403:
		case 16961:
		case 16963:
		case 18351:
		case 18352:
		case 18367:
		case 18368:
		case 1321:
		case 1323:
		case 1325:
		case 1327:
		case 1329:
		case 1331:
		case 1333:
		case 4587:
		case 6611:
		case 13979:
		case 13981:
		case 14097:
		case 14287:
		case 14289:
		case 14291:
		case 14293:
		case 14295:
		case 746:
		case 747:
		case 1203:
		case 1205:
		case 1207:
		case 1209:
		case 1211:
		case 1213:
		case 1215:
		case 1217:
		case 1219:
		case 1221:
		case 1223:
		case 1225:
		case 1227:
		case 1229:
		case 1231:
		case 1233:
		case 1235:
		case 1813:
		case 5668:
		case 5670:
		case 5672:
		case 5674:
		case 5676:
		case 5678:
		case 5680:
		case 5682:
		case 5684:
		case 5686:
		case 5688:
		case 5690:
		case 5692:
		case 5694:
		case 5696:
		case 5698:
		case 5700:
		case 5702:
		case 6591:
		case 6593:
		case 6595:
		case 6597:
		case 8872:
		case 8873:
		case 8875:
		case 8877:
		case 8879:
		case 13976:
		case 13978:
		case 14297:
		case 14299:
		case 14301:
		case 14303:
		case 14305:
		case 15826:
		case 15848:
		case 15849:
		case 15850:
		case 15851:
		case 15853:
		case 15854:
		case 15855:
		case 15856:
		case 15857:
		case 15858:
		case 15859:
		case 15860:
		case 15861:
		case 15862:
		case 15863:
		case 15864:
		case 15865:
		case 15866:
		case 15867:
		case 15868:
		case 15869:
		case 15870:
		case 15871:
		case 15872:
		case 15873:
		case 15874:
		case 15875:
		case 15876:
		case 15877:
		case 15879:
		case 15880:
		case 15881:
		case 15882:
		case 15883:
		case 15884:
		case 15885:
		case 15886:
		case 15887:
		case 15888:
		case 15889:
		case 15890:
		case 15891:
		case 16757:
		case 16759:
		case 16761:
		case 16763:
		case 16765:
		case 16767:
		case 16769:
		case 16771:
		case 16773:
		case 16775:
		case 16777:
		case 16779:
		case 16781:
		case 16783:
		case 16785:
		case 16787:
		case 16789:
		case 16791:
		case 16793:
		case 16795:
		case 16797:
		case 16799:
		case 16801:
		case 16803:
		case 16805:
		case 16807:
		case 16809:
		case 16811:
		case 16813:
		case 16815:
		case 16817:
		case 16819:
		case 16821:
		case 16823:
		case 16825:
		case 16827:
		case 16829:
		case 16831:
		case 16833:
		case 16835:
		case 16837:
		case 16839:
		case 16841:
		case 16843:
		case 17275:
		case 17277:
		case 667:
		case 1277:
		case 1279:
		case 1281:
		case 1283:
		case 1285:
		case 1287:
		case 1289:
		case 19780:
		case 16035:
		case 16036:
		case 16037:
		case 16038:
		case 16039:
		case 16040:
		case 16041:
		case 16042:
		case 16043:
		case 16044:
		case 16045:
		case 16935:
		case 16937:
		case 16939:
		case 16941:
		case 16943:
		case 16945:
		case 16947:
		case 16949:
		case 16951:
		case 16953:
		case 16955:
		case 16957:
		case 16959:
		case 18349:
		case 18350:
		case 18365:
		case 18366:
			return 12030;

		case 1171:
		case 1173:
		case 1175:
		case 1177:
		case 1179:
		case 1181:
		case 1183:
		case 1185:
		case 1187:
		case 1189:
		case 1191:
		case 1193:
		case 1195:
		case 1197:
		case 1199:
		case 1201:
		case 1540:
		case 2589:
		case 2597:
		case 2603:
		case 2611:
		case 2621:
		case 2629:
		case 2659:
		case 2675:
		case 2890:
		case 3122:
		case 3488:
		case 3758:
		case 4156:
		case 4224:
		case 4226:
		case 4227:
		case 4228:
		case 4229:
		case 4230:
		case 4231:
		case 4232:
		case 4233:
		case 4234:
		case 4235:
		case 4507:
		case 4512:
		case 6215:
		case 6217:
		case 6219:
		case 6221:
		case 6223:
		case 6225:
		case 6227:
		case 6229:
		case 6231:
		case 6233:
		case 6235:
		case 6237:
		case 6239:
		case 6241:
		case 6243:
		case 6245:
		case 6247:
		case 6249:
		case 6251:
		case 6253:
		case 6255:
		case 6257:
		case 6259:
		case 6261:
		case 6263:
		case 6265:
		case 6267:
		case 6269:
		case 6271:
		case 6273:
		case 6275:
		case 6277:
		case 6279:
		case 6631:
		case 6633:
		case 6894:
		case 7332:
		case 7334:
		case 7336:
		case 7338:
		case 7340:
		case 7342:
		case 7344:
		case 7346:
		case 7348:
		case 7350:
		case 7352:
		case 7354:
		case 7356:
		case 7358:
		case 7360:
		case 7676:
		case 9731:
		case 10352:
		case 10665:
		case 10667:
		case 10669:
		case 10671:
		case 10673:
		case 10675:
		case 10677:
		case 10679:
		case 10827:
		case 11283:
		case 11284:
		case 12908:
		case 12910:
		case 12912:
		case 12914:
		case 12916:
		case 12918:
		case 12920:
		case 12922:
		case 12924:
		case 12926:
		case 12928:
		case 12930:
		case 12932:
		case 12934:
		case 13506:
		case 13734:
		case 13736:
		case 13738:
		case 13740:
		case 13742:
		case 13744:
		case 13964:
		case 13966:
		case 14578:
		case 14579:
		case 15808:
		case 15809:
		case 15810:
		case 15811:
		case 15812:
		case 15813:
		case 15814:
		case 15815:
		case 15816:
		case 15817:
		case 15818:
		case 16079:
		case 16933:
		case 16934:
		case 16971:
		case 16972:
		case 17341:
		case 17342:
		case 17343:
		case 17344:
		case 17345:
		case 17346:
		case 17347:
		case 17348:
		case 17349:
		case 17351:
		case 17353:
		case 17355:
		case 17357:
		case 17359:
		case 17361:
		case 17405:
		case 18359:
		case 18360:
		case 18361:
		case 18362:
		case 18363:
		case 18364:
		case 18582:
		case 18584:
		case 18691:
		case 18709:
		case 18747:
		case 19340:
		case 19345:
		case 19352:
		case 19410:
		case 19426:
		case 19427:
		case 19440:
		case 19441:
		case 19442:
		case 19749:
			return 1156;

		case 4151:
		case 13444:
		case 14661:
		case 15441:
		case 15442:
		case 15443:
		case 15444:
		case 21369:
		case 21371:
		case 21372:
		case 21373:
		case 21374:
		case 21375:
		case 23691:
			return 11974;

		case 8844:
		case 8845:
		case 8846:
		case 8847:
		case 8848:
		case 8849:
		case 8850:
		case 15455:
		case 15456:
		case 15457:
		case 15458:
		case 15459:
		case 15825:
		case 17273:
		case 20072:
			return 4177;

		case 3095:
		case 3096:
		case 3097:
		case 3098:
		case 3099:
		case 3100:
		case 3101:
		case 6587:
		case 14484:
			return 397;

		case 1379:
		case 1381:
		case 1383:
		case 1385:
		case 1387:
		case 1389:
		case 1391:
		case 1393:
		case 1395:
		case 1397:
		case 1399:
		case 1401:
		case 1403:
		case 1405:
		case 1407:
		case 1409:
		case 2415:
		case 2416:
		case 2417:
		case 3053:
		case 3054:
		case 3055:
		case 3056:
		case 4170:
		case 4675:
		case 4710:
		case 4862:
		case 4863:
		case 4864:
		case 4865:
		case 4866:
		case 4867:
		case 6562:
		case 6603:
		case 6727:
		case 9084:
		case 9091:
		case 9092:
		case 9093:
		case 11736:
		case 11738:
		case 11739:
		case 11953:
		case 13406:
		case 13629:
		case 13630:
		case 13631:
		case 13632:
		case 13633:
		case 13634:
		case 13635:
		case 13636:
		case 13637:
		case 13638:
		case 13639:
		case 13640:
		case 13641:
		case 13642:
		case 6908:
		case 6910:
		case 6912:
		case 6914:
			return 415;
		case 4153:
		case 6528:
			return 1666;
		case 1307:
		case 1309:
		case 1311:
		case 1313:
		case 1315:
		case 1317:
		case 1319:
		case 6609:
		case 7158:
		case 7407:
		case 16127:
		case 16128:
		case 16129:
		case 16130:
		case 16131:
		case 16132:
		case 16133:
		case 16134:
		case 16135:
		case 16136:
		case 16137:
		case 16889:
		case 16891:
		case 16893:
		case 16895:
		case 16897:
		case 16899:
		case 16901:
		case 16903:
		case 16905:
		case 16907:
		case 16909:
		case 16973:
		case 18369:
		case 20874:
		case 11694:
		case 11696:
		case 11698:
		case 11700:
		case 11730:
			return 13051;

		case 18355:
		case 15486:
		case 3190:
		case 3192:
		case 3194:
		case 3196:
		case 3198:
		case 3200:
		case 3202:
		case 3204:
		case 6599:
			return 12806;

		case 18353:
		case 18354:
			return 13054;

		case 15241:
			return 12156;

		case 4718:
			return 12004;

		case 10887:
			return 5866;

		case 4755:
			return 2063;
		case 11716:
			return 12008;
		case 15445:
			return 11974;
		default:
			return 424;
		}
	}

	/**
	 * Weapon and magic attack speed!
	 **/

	public int getAttackDelay(String s) {
		int get = 4; // default
		String[][] getDelay = { { "dart", "3" }, { "knife", "3" }, { "Blisterwood stake", "3" }, { "Shortbow", "4" },
				{ "Karils crossbow", "4" }, { " Toktz-xil-ul", "4" }, { " Dagger", "4" }, { "Bronze sword", "4" },
				{ "Iron sword", "4" }, { "Steel sword", "4" }, { "Black sword", "4" }, { "Mithril sword", "4" },
				{ "Adamant sword", "4" }, { "Rune sword", "4" }, { "Scimitar", "4" }, { "Abyssal whip", "4" },
				{ "claws", "4" }, { "Zamorakian spear", "4" }, { "Saradomin sword", "4" }, { "Toktz-xil-ak", "4" },
				{ "Toktz-xil-ek", "4" }, { "Saradomin staff", "4" }, { "Zamorak staff", "4" }, { "Guthix staff", "4" },
				{ "Slayer's staff", "4" }, { "Ancient staff", "4" }, { "Gravite rapier", "4" },
				{ "Chaotic rapier", "4" }, { "Armadyl battlestaff", "4" }, { "Longsword", "5" }, { "mace", "5" },
				{ "axe", "5" }, { "pickaxe", "5" }, { "Tzhaar-ket-em", "5" }, { "Torags hammers", "5" },
				{ "Guthans warspear", "5" }, { "Veracs flail", "5" }, { "Staff", "5" }, { "Staff of air", "5" },
				{ "Staff of water", "5" }, { "Staff of earth", "5" }, { "Staff of fire", "5" }, { "Magic staff", "5" },
				{ "Mystic fire staff", "5" }, { "Mystic fire staff", "5" }, { "Mystic water staff", "5" },
				{ "Mystic water staff", "5" }, { "Mystic air staff", "5" }, { "Mystic air staff", "5" },
				{ "Mystic earth staff", "5" }, { "Mystic earth staff", "5" }, { "Battlestaff", "5" },
				{ "Iban's staff", "5" }, { "Staff of light", "5" }, { "Salamander", "5" },
				{ "Maple longbow (sighted)", "5" }, { "Magic longbow (sighted)", "5" }, { "thrownaxe", "5" },
				{ "Comp ogre bow", "5" }, { "New crystal bow", "5" }, { "Crystal bow", "5" }, { "Seercull", "5" },
				{ "Chaotic longsword", "5" }, { "Gravite longsword", "5" }, { "Battleaxe", "6" }, { "warhammer", "6" },
				{ "godsword", "6" }, { "Barrelchest anchor", "6" }, { "Ahrims staff", "6" }, { "Toktz-mej-tal", "6" },
				{ "Gravite 2h sword", "6" }, { "Chaotic maul", "6" }, { "Longbow", "6" }, { "Zamorak bow", "6" },
				{ "Saradomin bow", "6" }, { "Guthix bow", "6" }, { "javelin", "6" }, { "Dorgeshuun c'bow", "6" },
				{ "Crossbow", "6" }, { "Zaryte bow", "6" }, { "Phoenix crossbow", "6" }, { "Sagaie", "6" },
				{ "Bolas", "6" }, { "Auspicious katana", "6" }, { "2h sword", "7" }, { "halberd", "7" },
				{ "Granite maul", "7" }, { "Balmung", "7" }, { "Tzhaar-ket-om", "7" }, { "Ivandis flail", "7" },
				{ "Hand cannon", "7" }, { "Dharoks greataxe", "7" }, { "Ogre bow", "8" }, { "Dark bow", "9" },
				{ "Dreadnip", "10" }, { "Swagger stick", "10" } };
		for (int i = 0; i < getDelay.length; i++) {
			if (s.contains(getDelay[i][0].toLowerCase().replaceAll("_", " "))) {
				get = Integer.parseInt(getDelay[i][1]);
			}
		}
		if (c.usingMagic) {
			switch (Player.MAGIC_SPELLS[c.spellId][0]) {
			case 12871: // ice blitz
			case 13023: // shadow barrage
			case 12891: // ice barrage
				get = 5;

			default:
				get = 6;
			}
		}
		return get;
	}

	/**
	 * How long it takes to hit your enemy
	 **/
	public int getHitDelay(String weaponName) {
		if (c.usingMagic) {
			switch (Player.MAGIC_SPELLS[c.spellId][0]) {
			case 12891:
				return 4;
			case 12871:
				return 6;
			default:
				return 4;
			}
		} else {

			if (weaponName.contains("knife") || weaponName.contains("dart") || weaponName.contains("javelin")
					|| weaponName.contains("thrownaxe")) {
				return 3;
			}
			if (weaponName.contains("cross") || weaponName.contains("c'bow")) {
				return 4;
			}
			if (weaponName.contains("bow") && !c.dbowSpec) {
				return 4;
			} else if (c.dbowSpec) {
				return 4;
			}

			switch (c.playerEquipment[c.playerWeapon]) {
			case 6522: // Toktz-xil-ul
				return 3;

			default:
				return 2;
			}
		}
	}

	public int getRequiredDistance() {
		if (c.followId > 0 && c.freezeTimer <= 0 && !c.isMoving)
			return 2;
		else if (c.followId > 0 && c.freezeTimer <= 0 && c.isMoving) {
			return 3;
		} else {
			return 1;
		}
	}

	public boolean usingHally() {
		switch (c.playerEquipment[c.playerWeapon]) {
		case 3190:
		case 3192:
		case 3194:
		case 3196:
		case 3198:
		case 3200:
		case 3202:
		case 3204:
			return true;

		default:
			return false;
		}
	}

	/**
	 * Melee
	 **/

	boolean meleeHitSuccess(int a, int d) {
		a = Misc.random(a);
		d = Misc.random(d);
		c.sendMessage("Attack: " + a + " Defence: " + d);
		return a > d;
	}

	public int calculateMeleeAttack() {
		int attackLevel = c.playerLevel[0];
		if (c.prayerActive[2])
			attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.05;
		else if (c.prayerActive[7])
			attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.1;
		else if (c.prayerActive[15])
			attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.15;
		else if (c.prayerActive[24])
			attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.15;
		else if (c.prayerActive[25])
			attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.2;
		else if (c.curseActive[c.curses().TURMOIL])
			attackLevel = (int) (attackLevel * c.curses().getTurmoilMultiplier("Attack"));
		if (c.fullVoidMelee())
			attackLevel += c.getLevelForXP(c.playerXP[c.playerAttack]) * 0.1;
		attackLevel *= c.specAccuracy;
		int bestMeleeAttack = bestMeleeAtk();
		int i = c.playerBonus[bestMeleeAttack];
		i += c.bonusAttack;
		if (c.playerEquipment[c.playerAmulet] == 11128 && c.playerEquipment[c.playerWeapon] == 6528) {
			i *= 1.30;
		}
		if (c.combatType(c.ACCURATE)) {
			i += 3;
		}
		return (int) (attackLevel + (attackLevel * 0.15) + (i + i * 0.05));
	}

	public int bestMeleeAtk() {
		if (c.playerBonus[0] > c.playerBonus[1] && c.playerBonus[0] > c.playerBonus[2])
			return 0;
		if (c.playerBonus[1] > c.playerBonus[0] && c.playerBonus[1] > c.playerBonus[2])
			return 1;
		return c.playerBonus[2] <= c.playerBonus[1] || c.playerBonus[2] <= c.playerBonus[0] ? 0 : 2;
	}

	public int calculateMeleeMaxHit() {
		double maxHit = 0;
		int strBonus = c.playerBonus[10];
		if (c.combatType(c.AGGRESSIVE)) {
			strBonus += 3;
		}
		int strength = c.playerLevel[2];
		int lvlForXP = c.getLevelForXP(c.playerXP[2]);
		if (c.prayerActive[1])
			strength += (int) (lvlForXP * .05);
		else if (c.prayerActive[6])
			strength += (int) (lvlForXP * .10);
		else if (c.prayerActive[14])
			strength += (int) (lvlForXP * .15);
		else if (c.prayerActive[24])
			strength += (int) (lvlForXP * .18);
		else if (c.prayerActive[25])
			strength += (int) (lvlForXP * .23);
		else if (c.curseActive[c.curses().TURMOIL])
			strength = (int) (strength * c.curses().getTurmoilMultiplier("Strength"));
		if (c.playerEquipment[c.playerHat] == 2526 && c.playerEquipment[c.playerChest] == 2520
				&& c.playerEquipment[c.playerLegs] == 2522) {
			maxHit += (maxHit * 10 / 100);
		}
		maxHit += 1.05D + strBonus * strength * 0.00175D;
		maxHit += strength * 0.11D;
		if (c.playerEquipment[c.playerWeapon] == 4718 && c.playerEquipment[c.playerHat] == 4716
				&& c.playerEquipment[c.playerChest] == 4720 && c.playerEquipment[c.playerLegs] == 4722) {
			maxHit += (c.maxConstitution - c.constitution) / 20;// multiplied by
			// 10 due to
			// constitution
		}
		if (c.specDamage > 1)
			maxHit = (int) (maxHit * c.specDamage);
		if (maxHit < 0)
			maxHit = 1;
		if (c.fullVoidMelee())
			maxHit = (int) (maxHit * 1.10);
		if (c.playerEquipment[c.playerAmulet] == 11128 && c.playerEquipment[c.playerWeapon] == 6528) {
			maxHit *= 1.20;
		}
		return (int) (Math.floor(maxHit) * 10);
	}

	public int calculateMeleeDefence() {
		int defenceLevel = c.playerLevel[1];
		int i = c.playerBonus[bestMeleeDef()];
		if (c.combatType(c.BLOCK)) {
			i += 3;
		}
		if (c.prayerActive[0])
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.05;
		else if (c.prayerActive[5])
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.1;
		else if (c.prayerActive[13])
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.15;
		else if (c.prayerActive[24])
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.2;
		else if (c.prayerActive[25])
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.25;
		else if (c.curseActive[c.curses().TURMOIL])
			defenceLevel = (int) (defenceLevel * c.curses().getTurmoilMultiplier("Defence"));
		return (int) (defenceLevel + (defenceLevel * 0.15) + (i + i * 0.05));
	}

	public int bestMeleeDef() {
		if (c.playerBonus[5] > c.playerBonus[6] && c.playerBonus[5] > c.playerBonus[7])
			return 5;
		if (c.playerBonus[6] > c.playerBonus[5] && c.playerBonus[6] > c.playerBonus[7])
			return 6;
		return c.playerBonus[7] <= c.playerBonus[5] || c.playerBonus[7] <= c.playerBonus[6] ? 5 : 7;
	}

	public double getMeleeAttack() {
		double level = c.playerLevel[0];
		double levelMultiplier = 1.00;
		if (c.prayerActive[2])
			levelMultiplier *= 1.05;
		else if (c.prayerActive[7])
			levelMultiplier *= 1.1;
		else if (c.prayerActive[15])
			levelMultiplier *= 1.15;
		else if (c.prayerActive[24])
			levelMultiplier *= 1.15;
		else if (c.prayerActive[25])
			levelMultiplier *= 1.20;
		if (c.fullVoidMelee())
			levelMultiplier *= 1.1;
		double effectiveAttack = (int) (level * levelMultiplier);
		double maxAttack = effectiveAttack * getMeleeAccuracyBonus();
		maxAttack *= c.specAccuracy;
		if (maxAttack < 1) {
			maxAttack = 1;
		}
		return maxAttack;
	}

	public int getMeleeAccuracyBonus() {
		if (c.combatType(c.ACCURATE))
			return c.playerBonus[0];
		else if (c.combatType(c.AGGRESSIVE))
			return c.playerBonus[1];
		else if (c.combatType(c.CONTROLLED))
			return c.playerBonus[2];
		return 0;
	}

	public double getMeleeDefence(Player o) {
		double level = c.playerLevel[1];
		double levelMultiplier = 1.00;
		if (c.prayerActive[2])
			levelMultiplier *= 1.05;
		else if (c.prayerActive[7])
			levelMultiplier *= 1.1;
		else if (c.prayerActive[15])
			levelMultiplier *= 1.15;
		else if (c.prayerActive[24])
			levelMultiplier *= 1.20;
		else if (c.prayerActive[25])
			levelMultiplier *= 1.25;
		double effectiveDefence = (int) (level * levelMultiplier);
		double maxDefence = effectiveDefence * getMeleeDefenceBonus(o);
		if (maxDefence < 1) {
			maxDefence = 1;
		}
		return maxDefence * 1.20;
	}

	public int getMeleeDefenceBonus(Player o) {
		if (c.combatType(c.ACCURATE))
			return o.playerBonus[5];
		else if (c.combatType(c.AGGRESSIVE))
			return o.playerBonus[6];
		else if (c.combatType(c.CONTROLLED))
			return o.playerBonus[7];
		return 0;
	}

	/**
	 * Range
	 **/

	public int calculateRangeAttack() {
		int attackLevel = c.playerLevel[4];
		attackLevel *= c.specAccuracy;
		if (c.fullVoidRange())
			attackLevel += c.getLevelForXP(c.playerXP[c.playerRanged]) * 0.1;
		if (c.prayerActive[3])
			attackLevel *= 1.05;
		else if (c.prayerActive[11])
			attackLevel *= 1.10;
		else if (c.prayerActive[19])
			attackLevel *= 1.15;
		// dbow spec
		if (c.fullVoidRange() && c.specAccuracy > 1.15) {
			attackLevel *= 1.75;
		}
		return (int) (attackLevel + (c.playerBonus[4] * 1.95));
	}

	public int calculateRangeDefence() {
		int defenceLevel = c.playerLevel[1];
		if (c.prayerActive[0]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.05;
		} else if (c.prayerActive[5]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.1;
		} else if (c.prayerActive[13]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.15;
		} else if (c.prayerActive[24]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.2;
		} else if (c.prayerActive[25]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.25;
		}
		return defenceLevel + c.playerBonus[9] + (c.playerBonus[9] / 2);
	}

	public boolean usingBolts() {
		return c.playerEquipment[c.playerArrows] >= 9130 && c.playerEquipment[c.playerArrows] <= 9145
				|| c.playerEquipment[c.playerArrows] >= 9230 && c.playerEquipment[c.playerArrows] <= 9245;
	}

	public int rangeMaxHit() {
		int rangeLevel = c.playerLevel[4];
		double modifier = 1.0;
		double wtf = c.specDamage;
		int itemUsed = c.usingBow ? c.lastArrowUsed : c.lastWeaponUsed;
		if (c.prayerActive[3])
			modifier += 0.05;
		else if (c.prayerActive[11])
			modifier += 0.10;
		else if (c.prayerActive[19])
			modifier += 0.15;
		if (c.fullVoidRange())
			modifier += 0.20;
		double c = modifier * rangeLevel;
		int rangeStr = getRangeStr(itemUsed);
		double max = (c + 8) * (rangeStr + 64) / 640;
		if (wtf != 1)
			max *= wtf;
		if (max < 1)
			max = 1;
		return (int) (max * 10);
	}

	public int getRangeStr(int i) {
		if (i == 4214)
			return 70;
		switch (i) {
		case 15241:
		case 15243:
			return 150;
		// bronze to rune bolts
		case 877:
			return 10;
		case 9140:
			return 46;
		case 9141:
			return 64;
		case 9142:
		case 9241:
		case 9240:
			return 82;
		case 9143:
		case 9243:
		case 9242:
			return 100;
		case 9144:
		case 9244:
		case 9245:
			return 115;
		// bronze to dragon arrows
		case 882:
			return 7;
		case 884:
			return 10;
		case 886:
			return 16;
		case 888:
			return 22;
		case 890:
			return 31;
		case 892:
		case 4740:
			return 49;
		case 11212:
			return 60;
		// knifes
		case 864:
			return 3;
		case 863:
			return 4;
		case 865:
			return 7;
		case 866:
			return 10;
		case 867:
			return 14;
		case 868:
			return 24;
		}
		return 0;
	}

	public boolean properBolts() {
		return c.playerEquipment[c.playerArrows] >= 9140 && c.playerEquipment[c.playerArrows] <= 9144
				|| c.playerEquipment[c.playerArrows] >= 9240 && c.playerEquipment[c.playerArrows] <= 9244;
	}

	public int correctBowAndArrows() {
		if (usingBolts())
			return -1;
		switch (c.playerEquipment[c.playerWeapon]) {

		case 839:
		case 841:
			return 882;

		case 843:
		case 845:
			return 884;

		case 847:
		case 849:
			return 886;

		case 851:
		case 853:
			return 890;

		case 855:
		case 857:
			return 890;

		case 859:
		case 861:
			return 892;

		case 4734:
		case 4935:
		case 4936:
		case 4937:
			return 4740;

		case 15241:
			return 15243;

		case 11235:
			return 11212;
		}
		return -1;
	}

	public int getRangeStartGFX() {
		switch (c.rangeItemUsed) {

		case 863:
			return 220;
		case 864:
			return 219;
		case 865:
			return 221;
		case 866: // knives
			return 223;
		case 867:
			return 224;
		case 868:
			return 225;
		case 869:
			return 222;

		case 806:
			return 232;
		case 807:
			return 233;
		case 808:
			return 234;
		case 809: // darts
			return 235;
		case 810:
			return 236;
		case 811:
			return 237;

		case 825:
			return 206;
		case 826:
			return 207;
		case 827: // javelin
			return 208;
		case 828:
			return 209;
		case 829:
			return 210;
		case 830:
			return 211;

		case 800:
			return 42;
		case 801:
			return 43;
		case 802:
			return 44; // axes
		case 803:
			return 45;
		case 804:
			return 46;
		case 805:
			return 48;

		case 882:
			return 19;

		case 884:
			return 18;

		case 886:
			return 20;

		case 888:
			return 21;

		case 890:
			return 22;

		case 892:
			return 24;

		case 11212:
			return 26;

		case 4212:
		case 4214:
		case 4215:
		case 4216:
		case 4217:
		case 4218:
		case 4219:
		case 4220:
		case 4221:
		case 4222:
		case 4223:
			return 250;

		}
		return -1;
	}

	public int getRangeProjectileGFX() {
		if (c.dbowSpec) {
			return 1099;
		}
		if (c.bowSpecShot > 0) {
			switch (c.rangeItemUsed) {
			default:
				return 249;
			}
		}
		boolean usingCross = c.playerEquipment[c.playerWeapon] == 9185 || c.playerEquipment[c.playerWeapon] == 18357;
		if (usingCross)
			return 27;
		switch (c.rangeItemUsed) {
		case 15243:
			return 2143;
		case 863:
			return 213;
		case 864:
			return 212;
		case 865:
			return 214;
		case 866: // knives
			return 216;
		case 867:
			return 217;
		case 868:
			return 218;
		case 869:
			return 215;

		case 806:
			return 226;
		case 807:
			return 227;
		case 808:
			return 228;
		case 809: // darts
			return 229;
		case 810:
			return 230;
		case 811:
			return 231;

		case 825:
			return 200;
		case 826:
			return 201;
		case 827: // javelin
			return 202;
		case 828:
			return 203;
		case 829:
			return 204;
		case 830:
			return 205;

		case 6522: // Toktz-xil-ul
			return 442;

		case 800:
			return 36;
		case 801:
			return 35;
		case 802:
			return 37; // axes
		case 803:
			return 38;
		case 804:
			return 39;
		case 805:
			return 40;

		case 882:
			return 10;

		case 884:
			return 9;

		case 886:
			return 11;

		case 888:
			return 12;

		case 890:
			return 13;

		case 892:
			return 15;

		case 11212:
			return 17;

		case 4740: // bolt rack
			return 27;

		case 4212:
		case 4214:
		case 4215:
		case 4216:
		case 4217:
		case 4218:
		case 4219:
		case 4220:
		case 4221:
		case 4222:
		case 4223:
			return 249;

		}
		return -1;
	}

	public int getProjectileSpeed() {
		if (c.dbowSpec)
			return 100;
		if (c.playerEquipment[c.playerWeapon] == 15241)
			return 50;
		return 70;
	}

	public int getProjectileShowDelay() {
		switch (c.playerEquipment[c.playerWeapon]) {
		case 863:
		case 864:
		case 865:
		case 866: // knives
		case 867:
		case 868:
		case 869:

		case 806:
		case 807:
		case 808:
		case 809: // darts
		case 810:
		case 811:

		case 825:
		case 826:
		case 827: // javelin
		case 828:
		case 829:
		case 830:

		case 800:
		case 801:
		case 802:
		case 803: // axes
		case 804:
		case 805:

		case 4734:
		case 9185:
		case 4935:
		case 4936:
		case 4937:
			return 15;

		default:
			return 0;
		}
	}

	/**
	 * MAGIC
	 **/

	public boolean canHitMage(Player o) {
		double hitSucceed = DEFENCE_MODIFIER * (mageAtkTest() / o.getCombat().mageDefTest());
		if (hitSucceed > 1.0)
			hitSucceed = 1;
		return hitSucceed < random.nextDouble();
	}

	public double mageDefTest() {
		double defenceBonus = c.playerBonus[8] == 0 ? 1 : c.playerBonus[8];
		if (defenceBonus < 1)
			defenceBonus = 1;
		double defenceCalc = defenceBonus * c.playerLevel[1];
		if (c.prayerActive[0])
			defenceCalc *= 1.05;
		else if (c.prayerActive[3])
			defenceCalc *= 1.10;
		else if (c.prayerActive[9])
			defenceCalc *= 1.15;
		else if (c.prayerActive[18])
			defenceCalc *= 1.20;
		else if (c.prayerActive[19])
			defenceCalc *= 1.25;
		return defenceCalc;
	}

	public double mageAtkTest() {
		double attackBonus = c.playerBonus[3] == 0 ? 1 : c.playerBonus[3];
		if (attackBonus < 1)
			attackBonus = 1;
		double attackCalc = attackBonus * c.playerLevel[6]; // +1 as its
		// exclusive
		if (c.fullVoidMage())
			attackCalc += 1.10;
		if (c.prayerActive[4])
			attackCalc *= 1.05;
		else if (c.prayerActive[12])
			attackCalc *= 1.10;
		else if (c.prayerActive[20])
			attackCalc *= 1.15;
		return attackCalc;
	}

	public int mageAtk() {
		int attackLevel = c.playerLevel[6];
		if (c.fullVoidMage())
			attackLevel += c.getLevelForXP(c.playerXP[6]) * 0.2;
		if (c.prayerActive[4])
			attackLevel *= 1.05;
		else if (c.prayerActive[12])
			attackLevel *= 1.10;
		else if (c.prayerActive[20])
			attackLevel *= 1.15;
		return attackLevel + (c.playerBonus[3] * 2);
	}

	public int mageDef() {
		int defenceLevel = c.playerLevel[1] / 2 + c.playerLevel[6] / 2;
		if (c.prayerActive[0]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.05;
		} else if (c.prayerActive[3]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.1;
		} else if (c.prayerActive[9]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.15;
		} else if (c.prayerActive[18]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.2;
		} else if (c.prayerActive[19]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.25;
		}
		return defenceLevel + c.playerBonus[8] + (c.playerBonus[8] / 3);
	}

	public boolean wearingStaff(int runeId) {
		int wep = c.playerEquipment[c.playerWeapon];
		switch (runeId) {
		case 554:
			if (wep == 1387)
				return true;
			break;
		case 555:
			if (wep == 1383)
				return true;
			break;
		case 556:
			if (wep == 1381)
				return true;
			break;
		case 557:
			if (wep == 1385)
				return true;
			break;
		}
		return false;
	}

	public boolean checkMagicReqs(int spell) {
		if (c.usingMagic && Constants.RUNES_REQUIRED) { // check for runes
			if ((!c.getItems().playerHasItem(Player.MAGIC_SPELLS[spell][8], Player.MAGIC_SPELLS[spell][9])
					&& !wearingStaff(Player.MAGIC_SPELLS[spell][8]))
					|| (!c.getItems().playerHasItem(Player.MAGIC_SPELLS[spell][10], Player.MAGIC_SPELLS[spell][11])
							&& !wearingStaff(Player.MAGIC_SPELLS[spell][10]))
					|| (!c.getItems().playerHasItem(Player.MAGIC_SPELLS[spell][12], Player.MAGIC_SPELLS[spell][13])
							&& !wearingStaff(Player.MAGIC_SPELLS[spell][12]))
					|| (!c.getItems().playerHasItem(Player.MAGIC_SPELLS[spell][14], Player.MAGIC_SPELLS[spell][15])
							&& !wearingStaff(Player.MAGIC_SPELLS[spell][14]))) {
				c.sendMessage("You don't have the required runes to cast this spell.");
				return false;
			}
		}

		if (c.usingMagic && c.playerIndex > 0) {
			if (PlayerHandler.players[c.playerIndex] != null) {
				for (int r = 0; r < c.REDUCE_SPELLS.length; r++) { // reducing
					// spells,
					// confuse
					// etc
					if (PlayerHandler.players[c.playerIndex].REDUCE_SPELLS[r] == Player.MAGIC_SPELLS[spell][0]) {
						c.reduceSpellId = r;
						if ((System.currentTimeMillis()
								- PlayerHandler.players[c.playerIndex].reduceSpellDelay[c.reduceSpellId]) > PlayerHandler.players[c.playerIndex].REDUCE_SPELL_TIME[c.reduceSpellId]) {
							PlayerHandler.players[c.playerIndex].canUseReducingSpell[c.reduceSpellId] = true;
						} else {
							PlayerHandler.players[c.playerIndex].canUseReducingSpell[c.reduceSpellId] = false;
						}
						break;
					}
				}
				if (!PlayerHandler.players[c.playerIndex].canUseReducingSpell[c.reduceSpellId]) {
					c.sendMessage("That player is currently immune to this spell.");
					c.usingMagic = false;
					c.stopMovement();
					resetPlayerAttack();
					return false;
				}
			}
		}

		int staffRequired = getStaffNeeded();
		if (c.usingMagic && staffRequired > 0 && Constants.RUNES_REQUIRED) { // staff
			// required
			if (c.playerEquipment[c.playerWeapon] != staffRequired) {
				c.sendMessage(
						"You need a " + c.getItems().getItemName(staffRequired).toLowerCase() + " to cast this spell.");
				return false;
			}
		}

		if (c.usingMagic && Constants.MAGIC_LEVEL_REQUIRED) { // check magic
			// level
			if (c.playerLevel[6] < Player.MAGIC_SPELLS[spell][1]) {
				c.sendMessage("You need to have a magic level of " + Player.MAGIC_SPELLS[spell][1] + " to cast this spell.");
				return false;
			}
		}
		if (c.usingMagic && Constants.RUNES_REQUIRED) {
			if (Player.MAGIC_SPELLS[spell][8] > 0) { // deleting runes
				if (!wearingStaff(Player.MAGIC_SPELLS[spell][8]))
					c.getItems().deleteItem(Player.MAGIC_SPELLS[spell][8],
							c.getItems().getItemSlot(Player.MAGIC_SPELLS[spell][8]), Player.MAGIC_SPELLS[spell][9]);
			}
			if (Player.MAGIC_SPELLS[spell][10] > 0) {
				if (!wearingStaff(Player.MAGIC_SPELLS[spell][10]))
					c.getItems().deleteItem(Player.MAGIC_SPELLS[spell][10],
							c.getItems().getItemSlot(Player.MAGIC_SPELLS[spell][10]), Player.MAGIC_SPELLS[spell][11]);
			}
			if (Player.MAGIC_SPELLS[spell][12] > 0) {
				if (!wearingStaff(Player.MAGIC_SPELLS[spell][12]))
					c.getItems().deleteItem(Player.MAGIC_SPELLS[spell][12],
							c.getItems().getItemSlot(Player.MAGIC_SPELLS[spell][12]), Player.MAGIC_SPELLS[spell][13]);
			}
			if (Player.MAGIC_SPELLS[spell][14] > 0) {
				if (!wearingStaff(Player.MAGIC_SPELLS[spell][14]))
					c.getItems().deleteItem(Player.MAGIC_SPELLS[spell][14],
							c.getItems().getItemSlot(Player.MAGIC_SPELLS[spell][14]), Player.MAGIC_SPELLS[spell][15]);
			}
		}
		return true;
	}

	public int getFreezeTime() {
		switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
		case 1572:
		case 12861: // ice rush
			return 10;

		case 1582:
		case 12881: // ice burst
			return 17;

		case 1592:
		case 12871: // ice blitz
			return 25;

		case 12891: // ice barrage
			return 33;

		default:
			return 0;
		}
	}

	public void freezePlayer(int i) {
	}

	public int getStartHeight() {
		switch (Player.MAGIC_SPELLS[c.spellId][0]) {
		case 1562: // stun
			return 25;

		case 12939:// smoke rush
			return 35;

		case 12987: // shadow rush
			return 38;

		case 12861: // ice rush
			return 15;

		case 12951: // smoke blitz
			return 38;

		case 12999: // shadow blitz
			return 25;

		case 12911: // blood blitz
			return 25;

		default:
			return 43;
		}
	}

	public int getEndHeight() {
		switch (Player.MAGIC_SPELLS[c.spellId][0]) {
		case 1562: // stun
			return 10;

		case 12939: // smoke rush
			return 20;

		case 12987: // shadow rush
			return 28;

		case 12861: // ice rush
			return 10;

		case 12951: // smoke blitz
			return 28;

		case 12999: // shadow blitz
			return 15;

		case 12911:
			return 10;// blood blitz

		default:
			return 31;
		}
	}

	public int getStartDelay() {
		switch (c.playerEquipment[c.playerWeapon]) {
		case 15241:
			return 0;
		}
		switch (Player.MAGIC_SPELLS[c.spellId][0]) {
		case 1539:
			return 60;
		default:
			return 53;
		}
	}

	public int getStaffNeeded() {
		switch (Player.MAGIC_SPELLS[c.spellId][0]) {
		case 1539:
			return 1409;

		case 12037:
			return 4170;

		case 1190:
			return 2415;

		case 1191:
			return 2416;

		case 1192:
			return 2417;

		default:
			return 0;
		}
	}

	public boolean godSpells() {
		switch (Player.MAGIC_SPELLS[c.spellId][0]) {
		case 1190:
			return true;

		case 1191:
			return true;

		case 1192:
			return true;

		default:
			return false;
		}
	}

	public int getEndGfxHeight() {
		switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
		case 12987:
		case 12901:
		case 12861:
		case 12445:
		case 1192:
		case 13011:
		case 12919:
		case 12881:
		case 12999:
		case 12911:
		case 12871:
		case 13023:
		case 12929:
		case 12891:
			return 0;

		default:
			return 100;
		}
	}

	public int getStartGfxHeight() {
		switch (Player.MAGIC_SPELLS[c.spellId][0]) {
		case 12871:
		case 12891:
			return 0;

		default:
			return 0;
		}
	}

	public void handleDfs() {
	}

	public void handleDfsNPC() {
	}

	public void applyRecoil(int damage, int i) {
		if (damage > 0 && PlayerHandler.players[i].playerEquipment[c.playerRing] == 2550) {
			int recDamage = damage / 10 + 1;
			appendHit(c, recDamage, 0, 3, false);
		}
	}

	public int getBonusAttack(int i) {
		switch (NPCHandler.npcs[i].npcType) {
		case 2883:
			return Misc.random(50) + 30;
		case 2026:
		case 2027:
		case 2029:
		case 2030:
			return Misc.random(50) + 30;
		}
		return 0;
	}

	@SuppressWarnings("unused")
	public void handleGmaulPlayer() {
		if (c.playerIndex > 0) {
			Player o = PlayerHandler.players[c.playerIndex];
			if (c.goodDistance(c.getX(), c.getY(), o.getX(), o.getY(), getRequiredDistance())) {
				if (checkReqs()) {
					if (checkSpecAmount(4153)) {
						int damage = 0;
						if (meleeHitSuccess(calculateMeleeAttack(), o.getCombat().calculateMeleeDefence()))
							damage = Misc.random(calculateMeleeMaxHit());
						if (o.prayerActive[18] && System.currentTimeMillis() - o.protMeleeDelay > 1500)
							damage *= .6;
						if (o.curseActive[o.curses().DEFLECT_MELEE]
								&& System.currentTimeMillis() - o.protMeleeDelay > 1500)
							damage *= .4;
						c.startAnimation(1667);
						c.gfx100(337);
					}
				}
			}
		}
	}

	public boolean armaNpc(int i) {
		int[] armadyl = { 6229, 6230, 6231, 6232, 6233, 6234, 6235, 6236, 6237, 6238, 6239, 6240, 6241, 6242, 6243,
				6244, 6245, 6246 };
		for (int g : armadyl) {
			if (g == NPCHandler.npcs[i].npcType) {
				return true;
			}
		}
		switch (NPCHandler.npcs[i].npcType) {
		case 2558:
		case 2559:
		case 2560:
		case 2561:
			return true;
		}
		return false;
	}

	public int korasiDamage(Player o) {
		double hitMultiplier = random.nextDouble() + 0.5;
		int damage = (int) (calculateMeleeMaxHit() * hitMultiplier);
		if (o != null && o.curseActive[c.curses().DEFLECT_MAGIC] && System.currentTimeMillis() - o.protMageDelay > 1500)
			damage = damage * 40 / 100;
		if (o != null && o.prayerActive[16] && System.currentTimeMillis() - o.protMageDelay > 1500)
			damage = damage * 60 / 100;
		return damage;
	}

	public double soakPercentage(String type) {
		double total = 0;
		if (type.equals("Melee")) {
			total = c.soakingBonus[0];
		} else if (type.equals("Ranged")) {
			total = c.soakingBonus[2];
		} else if (type.equals("Magic")) {
			total = c.soakingBonus[1];
		}
		return total;
	}

	public int damageSoaked(int damage, String type) {
		if (damage <= 200)
			return 0;
		return (int) ((damage - 200) * soakPercentage(type));
	}

	public static int finalMagicDamage(Player c) {
		double damage = Player.MAGIC_SPELLS[c.oldSpellId][6] * 10;
		double damageMultiplier = 1;
		int level = c.playerLevel[c.playerMagic];
		if (level > c.getLevelForXP(c.playerXP[6]) && c.getLevelForXP(c.playerXP[6]) >= 95)
			damageMultiplier += .03 * ((level > 104 ? 104 : level) - 99);
		else
			damageMultiplier = 1;
		switch (c.playerEquipment[c.playerWeapon]) {
		case 18371: // Gravite Staff
			damageMultiplier += .05;
			break;
		case 4675: // Ancient Staff
		case 4710: // Ahrim's Staff
		case 4862: // Ahrim's Staff
		case 4864: // Ahrim's Staff
		case 4865: // Ahrim's Staff
		case 6914: // Master Wand
		case 8841: // Void Knight Mace
		case 13867: // Zuriel's Staff
		case 13869: // Zuriel's Staff (Deg)
			damageMultiplier += .10;
			break;
		case 15486: // Staff of Light
			damageMultiplier += .15;
			break;
		case 18355: // Chaotic Staff
			damageMultiplier += .20;
			break;
		}
		switch (c.playerEquipment[c.playerAmulet]) {
		case 18333: // Arcane Pulse
			damageMultiplier += .05;
			break;
		case 18334:// Arcane Blast
			damageMultiplier += .10;
			break;
		case 18335:// Arcane Stream
			damageMultiplier += .15;
			break;
		}
		damage *= damageMultiplier;
		return (int) damage;
	}

}