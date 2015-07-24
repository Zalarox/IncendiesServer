package main.game.players;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import main.Constants;
import main.event.CycleEvent;
import main.game.items.GameItem;
import main.game.npcs.NPC;
import main.game.objects.Objects;
import main.game.players.Boundaries.Area;
import main.game.players.content.QuickCurses;
import main.game.players.content.QuickPrayers;
import main.game.players.content.skills.dungeoneering.Party;

public class PlayerVariables {

	// TODO Move variables over to this class here - PLEASE DO NOT DELETE OR
	// EDIT; OBVIOUSLY NOT DONE - ALEX

	public PlayerVariables() {

	}

	/** Booleans **/
	public boolean PrayX = false, playerIsWoodcutting, CANNOT_BE_ATTACKED = false, loopingBPane, doingAgility,
			agilityEmote = false, logBalance, obstacleNetUp, treeBranchUp, balanceRope, treeBranchDown, obstacleNetOver,
			isStringing, isArrowing, isOnInterface, secondHerb = false, herbloreI = false, hasSavedClan = false,
			goodLootDistance, run1 = false, run2 = false, doingWoodcutting = false, recievedStarter2 = false,
			doneSteelTitanDelay = false, usingSummoningSpecial = false, isSearching, tempBoolean, ableToYell = true,
			busy = false, playerisSmelting = false, killedPlayer = false, killedDuelOpponent = false,
			acceptedFirst = false, acceptedSecond = false, isJumping = false, beenWarned = false, Run1 = false,
			Run2 = false, resting, walkFromFilling = false, fillingWater = false, spinningPlate, isFullBody, isFullHelm,
			isFullMask, settingUpCannon, hasCannon, cannonIsShooting, setUpBase, setUpStand, setUpBarrels, setUpFurnace,
			inDrillEvent, cantTeleport, canLeaveArea, isDonePicking, CursesOn, //
			runOption, staffoflight, quickCurseActive, quickPray, quickCurse, choseQuickPro, quickPrayersOn,
			initialized = false, ruleAgreeButton = false, RebuildNPCList = false, isKicked = false, isDreaming = false,
			playerIsFarming = false, playerIsPraying = false, playerIsSmithing = false, playerIsMining = false,
			playerIsFletching = false, playerIsFishing = false, playerIsHerbloring = false, playerIsFiremaking = false,
			playerIsCrafting = false, playerIsThieving = false, playerIsCooking = false, isSkulled = false,
			friendUpdate = false, newPlayer = false, hasMultiSign = false, saveCharacter = false, mouseButton = false,
			chatEffects = true, acceptAid = false, nextDialogue = false, autocasting = false, usedSpecial = false,
			mageFollow = false, dbowSpec = false, craftingLeather = false, properLogout = false, secDbow = false,
			maxNextHit = false, ssSpec = false, vengOn = false, addStarter = false, accountFlagged = false,
			msbSpec = false, usingBoB, autoGive = false, stopPlayerSkill, inEvent = false, isMining, mining,
			indungboss = false, inBarbDef = false, orb, canChangeAppearance = false, mageAllowed, usingPrayer,
			duelRequested, magicFailed, oldMagicFailed, isMoving, walkingToItem, headingTowardsPlayer, isShopping,
			updateShop, inDuel, tradeAccepted, goodTrade, inTrade, tradeRequested, tradeResetNeeded, tradeConfirmed,
			playerPrayerBook, isRunning2 = true, tradeConfirmed2, canOffer, acceptTrade, acceptedTrade, takeAsNote,
			saveFile = false, usingGlory = false, fishing = false, smeltInterface, patchCleared, antiFirePot = false,
			inPits = false, expLock, bankNotes = false, isBanking = false, isRunning = true, wildernessUpdated = false,
			isDead = false, korasiSpec, hasActivated, closed, doubleHit, usingSpecial, npcDroppingItems,
			usingRangeWeapon, usingBow, usingMagic, castingMagic, playerSkilling[] = new boolean[25],
			killedPheasant[] = new boolean[5], quickCurses[] = new boolean[QuickCurses.MAX_CURSES],
			quickPrayers[] = new boolean[QuickPrayers.MAX_PRAYERS], invSlot[] = new boolean[28],
			equipSlot[] = new boolean[14],
			prayerActive[] = { false, false, false, false, false, false, false, false, false, false, false, false,
					false, false, false, false, false, false, false, false, false, false, false, false, false, false },
			duelRule[] = new boolean[22], curseActive[] = new boolean[20], brotherSpawned[] = new boolean[6],
			brotherKilled[] = new boolean[6], canUseReducingSpell[] = { true, true, true, true, true, true };

	/** Integers **/
	public int lastGame, altarXCoord, altarYCoord, prayerItemID, timesBuryed, addPlayerSize = 0, questPoints, lastAbsX,
			lastAbsY, faceX, faceY, woodcuttingTree, logID, currentArrow, stringu, barbObstacle = 0, herbAmount,
			doingHerb, newHerb, newXp, lowMemoryVersion = 0, playerRights, timeOutCounter = 0, returnCode = 2,
			damageType = 0, geyserTitanDelay = 0, geyserTitanTarget = 0, steelTitanDelay = 0, steelTitanTarget = 0,
			summoningMonsterId = -1, interfaceIdOpen = 0, event = 0, makeTimes = 0, tzhaarToKill = 0, tzhaarKilled = 0,
			tzKekSpawn = 0, tzKekTimer = 0, caveWave, tzhaarNpcs, waveAmount = 0, totalxp = 0, amountDonated,
			diceTimer = 0, currentSkillTask, oldSkillTask, runEnergy = 200, doAnim, oldItem, oldItem2, gainXp, gainXp2,
			levelReq, levelReq2, newItem, newItem2, objectType, chance, leatherType, tempInteger, skillSpeed, doAmount,
			cannonBalls, cannonBaseX, cannonBaseY, cannonBaseH, rotation, cannonID, playerTitle = 0, FishID,
			bindingNeckCharge, cookingTimer, skillXpToAdd, startingDelay = 360, cookedFishID, burnFishID, succeslvl, DC,
			KC, craftLevelReq, specRestore = 0, xamount, castVengeance = 0, cooksLeft = 0, getPheasent, correctDrill,
			getRefreshment, npcSlot, lastX, lastY, rockCrabKills = 0, xpRecieved = 0, Picked, specRestoreTimer,
			isDonator, maxhp, course2, course1, x1 = -1, y1 = -1, x2 = -1, y2 = -1, speed1 = -1, speed2 = -1,
			direction = -1, ssDelay = -1, ssHeal = 0, leechEndGFX = 0, treeX = 0, treeY = 0, level1 = 0, level2 = 0,
			level3 = 0, clue = 0, casket = 0, leechDelay = 0, leechType, ACCURATE = 0, RAPID = 1, AGGRESSIVE = 1,
			LONGRANGE = 2, BLOCK = 2, DEFENSIVE = 2, CONTROLLED = 3, pkp, rockX, rockY, specAltarTimer, saveDelay,
			playerKilled, pkPoints, DonatorPoints = 0, SlayerPoints = 0, totalPlayerDamageDealt, killedBy,
			lastChatId = 1, privateChat, friendSlot = 0, dialogueId, randomCoffin, newLocation, specEffect, specBarId,
			attackLevelReq, defenceLevelReq, strengthLevelReq, rangeLevelReq, magicLevelReq, prayerLevelReq, followId,
			skullTimer, votingPoints, timesVoted, nextChat = 0, talkingNpc = -1, dialogueAction = 0, autocastId,
			followDistance, followId2, barrageCount = 0, delayedDamage = 0, delayedDamage2 = 0, pcPoints = 0,
			magePoints = 0, desertTreasure = 0, lastArrowUsed = -1, clanId = -1, autoRet = 0, pcDamage = 0,
			xInterfaceId = 0, xRemoveId = 0, xRemoveSlot = 0, waveId, frozenBy = 0, poisonDamage = 0, teleAction = 0,
			magicAltar = 0, bonusAttack = 0, lastNpcAttacked = 0, killCount = 0, destroyItem = 0, barbsToKill = 0,
			barbsKilled = 0, barbWave = 0, npcId2 = 0, foodCombo = 0, barbDamage = 0, barbLeader = 0, barbPoints = 0,
			dungpoint, dungfloor, dungFloor, pDungFloor, dungn, dungtokens, dungtime = 0, teleGrabItem, teleGrabX,
			teleGrabY, duelCount, underAttackBy, underAttackBy2, wildLevel, teleTimer, respawnTimer = 0,
			teleBlockLength, poisonDelay, lastBrother, reduceSpellId, totalTasks, taskDifficulty = -1, prayerId = -1,
			headIcon = -1, bountyIcon = 0, leechEnergyDelay, soulSplitDelay, leechAttackDelay, attackMultiplier,
			rangedMultiplier, leechRangedDelay, leechDefenceDelay, defenceMultiplier, leechMagicDelay, magicMultiplier,
			leechStrengthDelay, strengthMultiplier, leechSpecialDelay, duelTimer, duelTeleX, duelTeleY, duelSlot,
			duelSpaceReq, duelOption, duelingWith, headIconPk = -1, headIconHints, specMaxHitIncrease, freezeDelay,
			freezeTimer = -6, killerId, playerIndex, oldPlayerIndex, lastWeaponUsed, projectileStage,
			crystalBowArrowCount, playerMagicBook, teleGfx, teleEndGfx, teleEndAnimation, teleHeight, teleX, teleY,
			rangeItemUsed, killingNpcIndex, totalDamageDealt, oldNpcIndex, fightMode, attackTimer, npcIndex,
			npcClickIndex, npcType, castingSpellId, oldSpellId, spellId, hitDelay, hitDelay2, dBowHits, saveTimer = 100,
			bowSpecShot, clickNpcType, clickObjectType, objectId, objectX, objectY, objectXOffset, objectYOffset,
			objectDistance, pItemX, pItemY, pItemId, myShopId, tradeStatus, tradeWith, playerTradeWealth, attackAnim,
			combatLevel, apset, actionID, wearItemTimer, wearId, wearSlot, interfaceId, XremoveSlot, XinterfaceID,
			XremoveID, Xamount, specialTeleTimer = -1, specialTeleX, specialTeleY, wcTimer = 0, miningTimer = 0,
			fishTimer = 0, smeltType, smeltAmount, smeltTimer = 0, castleWarsTeam, pitsStatus = 0, CombatLevel = 0,
			soakDamage, soakDamage2 = 0, WillKeepAmt1, WillKeepAmt2, WillKeepAmt3, WillKeepAmt4, WillKeepItem1,
			WillKeepItem2, WillKeepItem3, WillKeepItem4, WillKeepItem1Slot, WillKeepItem2Slot, WillKeepItem3Slot,
			WillKeepItem4Slot, EquipStatus, hitMask, hitIcon, hitMask2, hitIcon2, skillTask, familiarIndex,
			summoningSpecialPoints, timeBetweenSpecials, familiarID, specHitTimer, playerStandIndex = 0x328,
			playerTurnIndex = 0x337, playerWalkIndex = 0x333, playerTurn180Index = 0x334, playerTurn90CWIndex = 0x335,
			playerTurn90CCWIndex = 0x336, playerRunIndex = 0x338, playerHat = 0, playerCape = 1, playerAmulet = 2,
			playerWeapon = 3, playerChest = 4, playerShield = 5, playerLegs = 7, playerHands = 9, playerFeet = 10,
			playerRing = 12, playerArrows = 13, playerAttack = 0, playerDefence = 1, playerStrength = 2,
			playerHitpoints = 3, playerRanged = 4, playerPrayer = 5, playerMagic = 6, playerCooking = 7,
			playerWoodcutting = 8, playerFletching = 9, playerFishing = 10, playerFiremaking = 11, playerCrafting = 12,
			playerSmithing = 13, playerMining = 14, playerHerblore = 15, playerAgility = 16, playerThieving = 17,
			playerSlayer = 18, playerFarming = 19, playerRunecrafting = 20, playerHunter = 21, constitution,
			prayerPoints, playerXP[] = new int[25],
			/* maxConstitution = Player.getLevelForXP(playerXP[3]) * 10, */pouchData[] = { 0, 0, 0, 0 },
			gwKills[] = new int[4], bobItems[] = new int[30], fishingProp[] = new int[11], voidStatus[] = new int[5],
			itemKeptId[] = new int[4], pouches[] = new int[4], playerBonus[] = new int[12],
			playerAppearance[] = new int[13], woodcut[] = new int[3], farm[] = new int[2], playerItems[] = new int[28],
			playerItemsN[] = new int[28], bankItems[] = new int[Constants.BANK_SIZE],
			bankItemsN[] = new int[Constants.BANK_SIZE], playerEquipment[] = new int[14],
			playerEquipmentN[] = new int[14], playerLevel[] = new int[25], playerSkillProp[][] = new int[20][15],
			keepItems[] = new int[4], keepItemsN[] = new int[4], damageTaken[] = new int[Constants.MAX_PLAYERS],
			POUCH_SIZE[] = { 3, 6, 9, 12 },
			BOWS[] = { 18357, 9185, 839, 845, 847, 851, 855, 859, 841, 843, 849, 853, 857, 861, 4212, 4214, 4215, 11235,
					4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223, 6724, 4734, 4934, 4935, 4936, 4937 },
			ARROWS[] = { 882, 884, 886, 888, 890, 892, 4740, 11212, 9140, 9141, 4142, 9143, 9144, 9240, 9241, 9242,
					9243, 9244, 9245 },
			NO_ARROW_DROP[] = { 4212, 4214, 4215, 4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223, 4734, 4934, 4935,
					4936, 4937 },
			OTHER_RANGE_WEAPONS[] = { 863, 864, 865, 866, 867, 868, 869, 806, 807, 808, 809, 810, 811, 825, 826, 827,
					828, 829, 830, 800, 801, 802, 803, 804, 805, 6522 },
			REDUCE_SPELL_TIME[] = { 250000, 250000, 250000, 500000, 500000, 500000 },
			REDUCE_SPELLS[] = { 1153, 1157, 1161, 1542, 1543, 1562 },
			CURSE_DRAIN_RATE[] = { 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
					500, 500, 500 },
			CURSE_LEVEL_REQUIRED[] = { 50, 50, 52, 54, 56, 59, 62, 65, 68, 71, 74, 76, 78, 80, 82, 84, 86, 89, 92, 95 },
			CURSE[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19 },
			CURSE_GLOW[] = { 610, 611, 612, 613, 614, 615, 616, 617, 618, 619, 620, 621, 622, 623, 624, 625, 626, 627,
					628, 629 },
			CURSE_HEAD_ICONS[] = { -1, -1, -1, -1, -1, -1, 12, 10, 11, 9, -1, -1, -1, -1, -1, -1, -1, 16, 17, -1 },
			DUEL_RULE_ID[] = { 1, 2, 16, 32, 64, 128, 256, 512, 1024, 4096, 8192, 16384, 32768, 65536, 131072, 262144,
					524288, 2097152, 8388608, 16777216, 67108864, 134217728 },
			autocastIds[] = { 51133, 32, 51185, 33, 51091, 34, 24018, 35, 51159, 36, 51211, 37, 51111, 38, 51069, 39,
					51146, 40, 51198, 41, 51102, 42, 51058, 43, 51172, 44, 51224, 45, 51122, 46, 51080, 47, 7038, 0,
					7039, 1, 7040, 2, 7041, 3, 7042, 4, 7043, 5, 7044, 6, 7045, 7, 7046, 8, 7047, 9, 7048, 10, 7049, 11,
					7050, 12, 7051, 13, 7052, 14, 7053, 15, 47019, 27, 47020, 25, 47021, 12, 47022, 13, 47023, 14,
					47024, 15 };

	/** Longs **/
	public long skillTimer, agilityDelay, lootSharePotential, lastLootDate, clanDelay, lastRunRecovery, lastFire,
			spellDelay = 0, lastPlayerMove, lastPoison, lastPoisonSip, poisonImmune, lastSpear, lastProtItem, dfsDelay,
			lastVeng, lastYell, teleGrabDelay, protMageDelay, protMeleeDelay, protRangeDelay, lastAction, lastThieve,
			lastLockPick, alchDelay, duelDelay, teleBlockDelay, godSpellDelay, singleCombatDelay, singleCombatDelay2,
			reduceStat, logoutDelay, buryDelay, foodDelay, potDelay, restoreDelay, lastButton,
			friends[] = new long[200], reduceSpellDelay[] = new long[6], stopPrayerDelay, prayerDelay;

	/** Strings **/
	public String savedClan = null, bankText, familiarName, CookFishName, statedInterface = "", lastKilled, clanName,
			properName = "", donorTag = "Default tag", identityPunishment = "", globalMessage = "", skillReseted,
			leechTypes[] = { "Attack", "Ranged", "Magic", "Defence", "Strength", "energy", "special attack" },
			CURSE_NAME[] = { "Protect Item", "Sap Warrior", "Sap Ranger", "Sap Mage", "Sap Spirit", "Berserker",
					"Deflect Summoning", "Deflect Magic", "Deflect Missiles", "Deflect Melee", "Leech Attack",
					"Leech Ranged", "Leech Magic", "Leech Defence", "Leech Strength", "Leech Energy",
					"Leech Special Attack", "Wrath", "Soul Split", "Turmoil" };

	/** Doubles **/
	public double attackLeechBonus, rangedLeechBonus, magicLeechBonus, defenceLeechBonus, strengthLeechBonus = 1.0,
			attackLeechDefence, rangedLeechDefence, magicLeechDefence, defenceLeechDefence, strengthLeechDefence = 1.0,
			specAmount = 0, specAccuracy = 1, specDamage = 1, prayerPoint = 1.0, soakingBonus[] = new double[3];

	/** Bytes **/
	public byte tutorial, cookAssistant, WGS, poisonMask = 0;

	/** Party(Dungeoneering) **/
	public Party party;

	/** GameItems **/
	public GameItem[] bankArray = new GameItem[Constants.BANK_SIZE];

	/** NPC's **/
	public NPC summoned, ssTargetNPC, leechTargetNPC;

	/** ArrayLists **/
	public CopyOnWriteArrayList<GameItem> otherStakedItems = new CopyOnWriteArrayList<GameItem>();
	public CopyOnWriteArrayList<GameItem> stakedItems = new CopyOnWriteArrayList<GameItem>();
	public ArrayList<String> killedPlayers = new ArrayList<String>();
	public ArrayList<Integer> attackedPlayers = new ArrayList<Integer>();

	/** Objects **/
	public Objects oldCannon;

	/** Cycle events **/
	public CycleEvent skilling, spellSwap;

	public int getPouchData(int i) {
		return pouchData[i];
	}

	public int getDonarPointbonus(int point) {
		int bonus = 1;
		switch (isDonator) {
		case 1:
			bonus = (point < 11) ? 2 : (point / 4);
			break;
		case 2:
			bonus = (point < 11) ? 4 : (point / 3);
			break;
		case 3:
			bonus = (point < 3) ? 2 : point / 2;
			break;
		}
		return bonus;
	}

	/**
	 * 
	 * TODO add to fletch class...
	 */
	public final int fletchAmount(final int button) {
		switch (button) {
		case 34185:
		case 34189:
		case 34170:
		case 34174:
		case 34193:
		case 34217:
		case 34205:
		case 34209:
		case 34213:
			return 1;
		case 34192:
		case 34184:
		case 34188:
		case 34169:
		case 34216:
		case 34173:
		case 34204:
		case 34208:
		case 34212:
			return 5;
		case 34191:
		case 34183:
		case 34187:
		case 34168:
		case 34215:
		case 34172:
		case 34203:
		case 34207:
		case 34211:
			return 10;
		case 34190:
		case 34182:
		case 34186:
		case 34214:
		case 34167:
		case 34171:
		case 34202:
		case 34206:
		case 34210:
			return 27;
		}
		return 0;
	}

	public boolean isThereNexArmourEquipped(int slot) {
		if (slot == playerHat) {
			if (playerEquipment[slot] == 20135 || playerEquipment[slot] == 20147 || playerEquipment[slot] == 20159) {
				return true;
			}
		} else if (slot == playerChest) {
			if (playerEquipment[slot] == 20163 || playerEquipment[slot] == 20151 || playerEquipment[slot] == 20139) {
				return true;
			}
		} else if (slot == playerLegs) {
			if (playerEquipment[slot] == 20167 || playerEquipment[slot] == 20155 || playerEquipment[slot] == 20143) {
				return true;
			}
		}
		return false;
	}

	public int calculateMaxLifePoints(Player p) {
		int lifePoints = p.getLevelForXP(playerXP[3]) * 10;
		if (isThereNexArmourEquipped(playerHat)) {
			lifePoints += 66;
		}
		if (isThereNexArmourEquipped(playerChest)) {
			lifePoints += 200;
		}
		if (isThereNexArmourEquipped(playerLegs)) {
			lifePoints += 134;
		}
		return lifePoints;
	}

	public int calculateLP(int slot1, int slot2, int health1, int health2, Player p) {
		int lifePoints = p.getLevelForXP(playerXP[3]) * 10;
		if (isThereNexArmourEquipped(slot1)) {
			lifePoints += health1;
		}
		if (isThereNexArmourEquipped(slot2)) {
			lifePoints += health2;
		}
		return lifePoints;
	}

	public int calculateRemoved(int slot, Player p) {
		int lifePoints = p.getLevelForXP(playerXP[3]) * 10;
		int removeLP = 0;// The normal hp
		int maximumHealth = 0;
		if (isThereNexArmourEquipped(slot)) {
			if (slot == playerHat) {
				removeLP += 66;
				maximumHealth = calculateLP(playerChest, playerLegs, 200, 134, p);
			} else if (slot == playerChest) {
				removeLP += 200;
				maximumHealth = calculateLP(playerHat, playerLegs, 66, 134, p);
			} else if (slot == playerLegs) {
				removeLP += 134;
				maximumHealth = calculateLP(playerHat, playerChest, 66, 200, p);
			}
			if (maximumHealth < lifePoints) {
				removeLP = 0;
			}
			if (constitution - removeLP < lifePoints) {
				int l = constitution - lifePoints;
				removeLP = l;
			}
			if (maximumHealth > constitution) {
				removeLP = 0;
			}
		}
		return removeLP;
	}

	public boolean inMulti(Player p) {
		if ((p.absX >= 3136 && p.absX <= 3327 && p.absY >= 3519 && p.absY <= 3607)
				|| (p.absX >= 2625 && p.absX <= 2685 && p.absY >= 2550 && p.absY <= 2620) || // Pest
																								// Control
				(p.absX >= 3190 && p.absX <= 3327 && p.absY >= 3648 && p.absY <= 3839)
				|| (p.absX >= 3334 && p.absX <= 3355 && p.absY >= 3208 && p.absY <= 3217)
				|| (p.absX >= 3200 && p.absX <= 3390 && p.absY >= 3840 && p.absY <= 3967)
				|| (p.absX >= 2992 && p.absX <= 3007 && p.absY >= 3912 && p.absY <= 3967)
				|| (p.absX >= 2946 && p.absX <= 2959 && p.absY >= 3816 && p.absY <= 3831)
				|| (p.absX >= 3008 && p.absX <= 3199 && p.absY >= 3856 && p.absY <= 3903)
				|| (p.absX >= 3008 && p.absX <= 3071 && p.absY >= 3600 && p.absY <= 3711)
				|| (p.absX >= 3072 && p.absX <= 3327 && p.absY >= 3608 && p.absY <= 3647)
				|| (p.absX >= 2624 && p.absX <= 2690 && p.absY >= 2550 && p.absY <= 2619)
				|| (p.absX >= 2371 && p.absX <= 2422 && p.absY >= 5062 && p.absY <= 5117)
				|| (p.absX >= 2896 && p.absX <= 2927 && p.absY >= 3595 && p.absY <= 3630)
				|| (p.absX >= 2892 && p.absX <= 2932 && p.absY >= 4435 && p.absY <= 4464)
				|| (p.absX >= 2882 && p.absX <= 3000 && p.absY >= 4357 && p.absY <= 4406) // Corp
																							// beast
				|| (p.absX >= 3147 && p.absX <= 3193 && p.absY >= 9737 && p.absY <= 9778)
				|| (p.absX >= 2256 && p.absX <= 2287 && p.absY >= 4680 && p.absY <= 4711)
				|| Boundaries.checkBoundaries(Area.GOD_WARS, p.getX(), p.getY())) {
			return true;
		}
		return false;
	}

	public boolean inWild(Player p) {
		if (p.absX > 2941 && p.absX < 3392 && p.absY > 3524 && p.absY < 3967
				|| p.absX > 2941 && p.absX < 3392 && p.absY > 9918 && p.absY < 10366) {
			return true;
		}
		return false;
	}

	public boolean inDuelArena(Player p) {
		if ((p.absX > 3322 && p.absX < 3394 && p.absY > 3195 && p.absY < 3291)
				|| (p.absX > 3311 && p.absX < 3323 && p.absY > 3223 && p.absY < 3248)) {
			return true;
		}
		return false;
	}

	public boolean isAutoButton(int button) {
		for (int j = 0; j < autocastIds.length; j += 2) {
			if (autocastIds[j] == button) {
				return true;
			}
		}
		return false;
	}

	public void assignAutocast(int button, Player p) {
		for (int j = 0; j < autocastIds.length; j++) {
			if (autocastIds[j] == button) {
				Player c = PlayerHandler.players[p.playerId];
				autocasting = true;
				autocastId = autocastIds[j + 1];
				c.getPA().sendFrame36(108, 1);
				c.setSidebarInterface(0, 328);
				// spellName = getSpellName(autocastId);
				// spellName = spellName;
				// c.getActionSender().sendFrame126(spellName, 354);
				c = null;
				break;
			}
		}
	}

	/** TODO **/
	public boolean fullStatius() {
		return playerEquipment[playerChest] == 13884 && playerEquipment[playerLegs] == 13890
				&& playerEquipment[playerHat] == 13896;
	}

	public boolean fullVoidRange() {
		return playerEquipment[playerHat] == 11664 && playerEquipment[playerLegs] == 8840
				&& playerEquipment[playerChest] == 8839 && playerEquipment[playerHands] == 8842;
	}

	public boolean fullVoidMage() {
		return playerEquipment[playerHat] == 11663 && playerEquipment[playerLegs] == 8840
				&& playerEquipment[playerChest] == 8839 && playerEquipment[playerHands] == 8842;
	}

	public boolean fullVoidMelee() {
		return playerEquipment[playerHat] == 11665 && playerEquipment[playerLegs] == 8840
				&& playerEquipment[playerChest] == 8839 && playerEquipment[playerHands] == 8842;
	}

	public void ResetKeepItems() {
		WillKeepAmt1 = -1;
		WillKeepItem1 = -1;
		WillKeepAmt2 = -1;
		WillKeepItem2 = -1;
		WillKeepAmt3 = -1;
		WillKeepItem3 = -1;
		WillKeepAmt4 = -1;
		WillKeepItem4 = -1;
	}

	/**
	 * Magic
	 */
	// TODO Move to a better class
	public int magicHitBonus() {
		if (playerEquipment[playerWeapon] == 18355) {
			return 8;
		} else if (playerEquipment[playerWeapon] == 6914) {
			return 3;
		} else if (playerEquipment[playerWeapon] == 15486) {
			return 3;
		} else {
			return 5;
		}
	}

	public int totalMagicBonus() {
		if (playerEquipment[playerAmulet] == 18335) {
			return 4 + magicHitBonus();
		} else {
			return magicHitBonus();
		}
	}

	public final int[][] MAGIC_SPELLS = {
			// example {magicId, level req, animation, startGFX, projectile Id,
			// endGFX, maxhit, exp gained, rune 1, rune 1 amount, rune 2, rune 2
			// amount, rune 3, rune 3 amount, rune 4, rune 4 amount}
			{ 1152, 1, 10546, 457, 458, 463, 2, 5, 556, 1, 558, 1, 0, 0, 0, 0 }, // wind
			{ 1154, 5, 10542, 2701, 2703, 2708, 4, 7, 555, 1, 556, 1, 558, 1, 0, 0 }, // water
			{ 1156, 9, 14209, 2713, 2718, 2723, 6, 9, 557, 2, 556, 1, 558, 1, 0, 0 }, // earth
			{ 1158, 13, 2791, 2728, 2729, 2737, 8, 11, 554, 3, 556, 2, 558, 1, 0, 0 }, // fire
			{ 1160, 17, 10546, 457, 459, 464, 9, 13, 556, 2, 562, 1, 0, 0, 0, 0 }, // wind
			{ 1163, 23, 10542, 2701, 2704, 2709, 10, 16, 556, 2, 555, 2, 562, 1, 0, 0 }, // water
																							// bolt
			{ 1166, 29, 14209, 2714, 2719, 2724, 11, 20, 556, 2, 557, 3, 562, 1, 0, 0 }, // earth
																							// bolt
			{ 1169, 35, 2791, 2728, 2730, 2738, 12, 22, 556, 3, 554, 4, 562, 1, 0, 0 }, // fire
																						// bolt
			{ 1172, 41, 10546, 457, 460, 1863, 13, 25, 556, 3, 560, 1, 0, 0, 0, 0 }, // wind
			{ 1175, 47, 10542, 2701, 2705, 2706, 14, 28, 556, 3, 555, 3, 560, 1, 0, 0 }, // water
																							// blast
			{ 1177, 53, 14209, 2715, 2720, 2425, 15, 31, 556, 3, 557, 4, 560, 1, 0, 0 }, // earth
																							// blast
			{ 1181, 59, 2791, 2728, 2731, 2739, 16, 35, 556, 4, 554, 5, 560, 1, 0, 0 }, // fire
																						// blast
			{ 1183, 62, 10546, 457, 461, 2699, 17, 36, 556, 5, 565, 1, 0, 0, 0, 0 }, // wind
			{ 1185, 65, 10542, 2701, 2706, 2711, 18, 37, 556, 5, 555, 7, 565, 1, 0, 0 }, // water
																							// wave
			{ 1188, 70, 14209, 2716, 2721, 2726, 19, 40, 556, 5, 557, 7, 565, 1, 0, 0 }, // earth
																							// wave
			{ 1189, 75, 2791, 2728, 2733, 2740, 20, 42, 556, 5, 554, 7, 565, 1, 0, 0 }, // fire
																						// wave
			{ 1153, 3, 716, 102, 103, 104, 0, 13, 555, 3, 557, 2, 559, 1, 0, 0 }, // confuse
			{ 1157, 11, 716, 105, 106, 107, 0, 20, 555, 3, 557, 2, 559, 1, 0, 0 }, // weaken
			{ 1161, 19, 716, 108, 109, 110, 0, 29, 555, 2, 557, 3, 559, 1, 0, 0 }, // curse
			{ 1542, 66, 729, 167, 168, 169, 0, 76, 557, 5, 555, 5, 566, 1, 0, 0 }, // vulnerability
			{ 1543, 73, 729, 170, 171, 172, 0, 83, 557, 8, 555, 8, 566, 1, 0, 0 }, // enfeeble
			{ 1562, 80, 729, 173, 174, 107, 0, 90, 557, 12, 555, 12, 556, 1, 0, 0 }, // stun
			{ 1572, 20, 710, 177, 178, 181, 0, 30, 557, 3, 555, 3, 561, 2, 0, 0 }, // bind
			{ 1582, 50, 710, 177, 178, 180, 2, 60, 557, 4, 555, 4, 561, 3, 0, 0 }, // snare
			{ 1592, 79, 710, 177, 178, 179, 4, 90, 557, 5, 555, 5, 561, 4, 0, 0 }, // entangle
			{ 1171, 39, 724, 145, 146, 147, 15, 25, 556, 2, 557, 2, 562, 1, 0, 0 }, // crumble
																					// undead
			{ 1539, 50, 708, 87, 88, 89, 25, 42, 554, 5, 560, 1, 0, 0, 0, 0 }, // iban
			{ 12037, 50, 1576, 327, 328, 329, 19, 30, 560, 1, 558, 4, 0, 0, 0, 0 }, // magic
																					// dart
			{ 1190, 60, 811, 0, 0, 76, 20, 60, 554, 2, 565, 2, 556, 4, 0, 0 }, // sara
			{ 1191, 60, 811, 0, 0, 77, 20, 60, 554, 1, 565, 2, 556, 4, 0, 0 }, // cause
			{ 1192, 60, 811, 0, 0, 78, 20, 60, 554, 4, 565, 2, 556, 1, 0, 0 }, // flames
			{ 12445, 85, 1819, 0, 344, 345, 0, 65, 563, 1, 562, 1, 560, 1, 0, 0 }, // teleblock
			{ 12939, 50, 1978, 0, 384, 385, 13, 30, 560, 2, 562, 2, 554, 1, 556, 1 }, // smoke
																						// rush
			{ 12987, 52, 1978, 0, 378, 379, 14, 31, 560, 2, 562, 2, 566, 1, 556, 1 }, // shadow
																						// rush
			{ 12901, 56, 1978, 0, 0, 373, 15, 33, 560, 2, 562, 2, 565, 1, 0, 0 }, // blood
			{ 12861, 58, 1978, 0, 360, 361, 16, 34, 560, 2, 562, 2, 555, 2, 0, 0 }, // ice
																					// rush
			{ 12963, 62, 1979, 0, 0, 389, 19, 36, 560, 2, 562, 4, 556, 2, 554, 2 }, // smoke
																					// burst
			{ 13011, 64, 1979, 0, 0, 382, 20, 37, 560, 2, 562, 4, 556, 2, 566, 2 }, // shadow
																					// burst
			{ 12919, 68, 1979, 0, 0, 376, 21, 39, 560, 2, 562, 4, 565, 2, 0, 0 }, // blood
			{ 12881, 70, 1979, 0, 0, 363, 22, 40, 560, 2, 562, 4, 555, 4, 0, 0 }, // ice
			{ 12951, 74, 1978, 0, 386, 387, 23, 42, 560, 2, 554, 2, 565, 2, 556, 2 }, // smoke
																						// blitz
			{ 12999, 76, 1978, 0, 380, 381, 24, 43, 560, 2, 565, 2, 556, 2, 566, 2 }, // shadow
																						// blitz
			{ 12911, 80, 1978, 0, 374, 375, 25, 45, 560, 2, 565, 4, 0, 0, 0, 0 }, // blood
			{ 12871, 82, 1978, 366, 0, 367, 26, 46, 560, 2, 565, 2, 555, 3, 0, 0 }, // ice
																					// blitz
			{ 12975, 86, 1979, 0, 0, 391, 27, 48, 560, 4, 565, 2, 556, 4, 554, 4 }, // smoke
																					// barrage
			{ 13023, 88, 1979, 0, 0, 383, 28, 49, 560, 4, 565, 2, 556, 4, 566, 3 }, // shadow
																					// barrage
			{ 12929, 92, 1979, 0, 0, 377, 29, 51, 560, 4, 565, 4, 566, 1, 0, 0 }, // blood
			{ 12891, 94, 1979, 0, 0, 369, 30, 52, 560, 4, 565, 2, 555, 6, 0, 0 }, // ice
			{ -1, 80, 811, 301, 0, 0, 0, 0, 554, 3, 565, 3, 556, 3, 0, 0 }, // charge
			{ -1, 21, 712, 112, 0, 0, 0, 10, 554, 3, 561, 1, 0, 0, 0, 0 }, // low
			{ -1, 55, 713, 113, 0, 0, 0, 20, 554, 5, 561, 1, 0, 0, 0, 0 }, // high
			{ -1, 33, 728, 142, 143, 144, 0, 35, 556, 1, 563, 1, 0, 0, 0, 0 } // telegrab
	};

	public String getSpellName(int id) {
		switch (id) {
		case 0:
			return "Air Strike";
		case 1:
			return "Water Strike";
		case 2:
			return "Earth Strike";
		case 3:
			return "Fire Strike";
		case 4:
			return "Air Bolt";
		case 5:
			return "Water Bolt";
		case 6:
			return "Earth Bolt";
		case 7:
			return "Fire Bolt";
		case 8:
			return "Air Blast";
		case 9:
			return "Water Blast";
		case 10:
			return "Earth Blast";
		case 11:
			return "Fire Blast";
		case 12:
			return "Air Wave";
		case 13:
			return "Water Wave";
		case 14:
			return "Earth Wave";
		case 15:
			return "Fire Wave";
		case 32:
			return "Shadow Rush";
		case 33:
			return "Smoke Rush";
		case 34:
			return "Blood Rush";
		case 35:
			return "Ice Rush";
		case 36:
			return "Shadow Burst";
		case 37:
			return "Smoke Burst";
		case 38:
			return "Blood Burst";
		case 39:
			return "Ice Burst";
		case 40:
			return "Shadow Blitz";
		case 41:
			return "Smoke Blitz";
		case 42:
			return "Blood Blitz";
		case 43:
			return "Ice Blitz";
		case 44:
			return "Shadow Barrage";
		case 45:
			return "Smoke Barrage";
		case 46:
			return "Blood Barrage";
		case 47:
			return "Ice Barrage";
		default:
			return "Select Spell";
		}
	}

}
