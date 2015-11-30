package incendius.game.players.actions;

import incendius.Constants;
import incendius.Server;
import incendius.game.npcs.NPC;
import incendius.game.npcs.NPCHandler;
import incendius.game.npcs.transformers.TransformHandler;
import incendius.game.objects.Object;
import incendius.game.players.Player;
import incendius.game.players.actions.combat.CombatPrayer;
import incendius.game.players.content.TeleportHandler;
import incendius.game.players.content.WebsHandler;
import incendius.game.players.content.minigames.impl.FightPits;
import incendius.game.players.content.minigames.impl.Godwars;
import incendius.game.players.content.minigames.impl.PestControl;
import incendius.game.players.content.minigames.impl.barrows.Barrows;
import incendius.game.players.content.pickables.Flax;
import incendius.game.players.content.pickables.Pickables;
import incendius.game.players.content.skills.agility.Agility;
import incendius.game.players.content.skills.crafting.Tanning;
import incendius.game.players.content.skills.dungeoneering.Crate;
import incendius.game.players.content.skills.dungeoneering.Dungeon;
import incendius.game.players.content.skills.dungeoneering.Portal;
import incendius.game.players.content.skills.farming.Farming;
import incendius.game.players.content.skills.fishing.Fishing;
import incendius.game.players.content.skills.mining.Mining;
import incendius.game.players.content.skills.mining.Prospecting;
import incendius.game.players.content.skills.runecrafting.RunecraftAltars;
import incendius.game.players.content.skills.runecrafting.Runecrafting;
import incendius.game.players.content.skills.woodcutting.Woodcutting;
import incendius.util.Menus;
import incendius.util.Misc;

public class ActionHandler {

	private Player c;
	private long lastClick;

	public ActionHandler(Player Player) {
		this.c = Player;
	}

	public void firstClickObject(int objectType, int obX, int obY) {
		if (c.getInstance().inEvent) {
			return;
		}
		if (c.getInstance().agilityEmote)
			return;
		// CastleWarObjects.handleObject(c, objectType, obX, obY);
		c.getInstance().clickObjectType = 0;

		if (c.getInstance().playerRights == 3) {
			c.sendMessage("@blu@Object type: " + objectType);
		}
		Godwars.doors(c, objectType);
		handleDagganothObjects(objectType);
		if (c.stopPlayerPacket) {
			return;
		}
		c.turnPlayerTo(obX, obY);
		if (Agility.agilityObstacle(c, objectType)) {
			c.getInstance().objectDistance = 5;
			Agility.agilityCourse(c, objectType);
		}
		if (Mining.mineOre(c, objectType, obX, obY))
			return;
		if (Farming.harvest(c, obX, obY)) {
			return;
		}
		if (Woodcutting.chopTree(c, objectType, obX, obY)) {
			return;
		}
		if (RunecraftAltars.runecraftAltar(c, objectType) || RunecraftAltars.clickRuin(c, objectType)) {
			return;
		}
		Barrows.handleObjects(c, objectType);
		switch (objectType) {
		case 10093:
			c.getPA().sendChatInterface(15336);
			break;
		case 1742:
			c.getPA().walkTo(2, 0);
			break;
		case 2273:
			c.getPA().movePlayer(2918, 5273, 0);
			break;
		case 28780:
			c.sendMessage("Exit way lol.");
			break;
		case 28823:
		case 28824:
			Crate.search(c, obX, obY);
			break;

		case 28779:
			Portal.handle(c, obX, obY);
			break;
		// Borks portal
		case 29537:
			if (c.objectX == 3115 && c.objectY == 5528) {
				c.getPA().movePlayer(3142, 5545, 0);
			}
			break;
		case 11844:
			if (c.absX > 2935) {
				c.getPA().movePlayer(2935, 3355, c.heightLevel);
			} else {
				c.getPA().movePlayer(2937, 3355, c.heightLevel);
			}
			break;

		/* Dungeons objects and navigating the player through dungeons */

		// Taverly
		case 31814:
			if (c.absY == 9698) {
				c.getPA().movePlayer(2907, 9697, c.heightLevel);
			} else if (c.absY == 9697)
				c.getPA().movePlayer(2907, 9698, c.heightLevel);
			break;

		case 2623:
			if (c.absX == 2923) {
				c.getPA().movePlayer(2924, 9803, c.heightLevel);
			} else if (c.absX == 2924)
				c.getPA().movePlayer(2923, 9803, c.heightLevel);
			break;

		// Slayer tower
		case 10527:
			if (c.absY == 3556) {
				c.getPA().movePlayer(3426, 3555, c.heightLevel);
			} else if (c.absY == 3555) {
				c.getPA().movePlayer(3426, 3556, c.heightLevel);
			}
			break;

		case 10529:
			if (c.absY == 3554) {
				c.getPA().movePlayer(3445, 3555, c.heightLevel);
			} else if (c.absY == 3555) {
				c.getPA().movePlayer(3445, 3554, c.heightLevel);
			}
			break;

		// Relleka Slayer Dungeon
		case 4499: // cave entrance
			c.getPA().movePlayer(2808, 10002, 0);
			break;

		// Edgeville dungeon
		case 29370:
			if (c.absX == 3149) {
				c.getPA().movePlayer(3155, 9906, c.heightLevel);
			} else if (c.absX == 3155) {
				c.getPA().movePlayer(3149, 9906, c.heightLevel);
			}
			break;
		case 29375: // Monkey bars
			if (c.absY == 9963) {
				c.getPA().movePlayer(3120, 9970, c.heightLevel);
			} else if (c.absY == 9970)
				c.getPA().movePlayer(3120, 9963, c.heightLevel);
			break;

		case 29315: // Door
			if (c.absY == 9944) {
				c.getPA().movePlayer(3106, 9945, c.heightLevel);
			} else if (c.absY == 9945)
				c.getPA().movePlayer(3106, 9944, c.heightLevel);
			break;

		case 29316: // Doors
			if (c.absY == 9944) {
				c.getPA().movePlayer(3106, 9945, c.heightLevel);
			} else if (c.absY == 9945)
				c.getPA().movePlayer(3106, 9944, c.heightLevel);
			else if (c.absX == 3145) {
				c.getPA().movePlayer(3146, 9870, c.heightLevel);
			} else if (c.absX == 3146)
				c.getPA().movePlayer(3145, 9870, c.heightLevel);
			break;

		case 29320: // Door
			if (c.absY == 9917) {
				c.getPA().movePlayer(3132, 9918, c.heightLevel);
			} else if (c.absY == 9918)
				c.getPA().movePlayer(3132, 9917, c.heightLevel);
			break;
		/* End of Edgeville Dungeon */

		/* King black dragon objects */
		case 1597: // gate next to lessor demons
			if (c.absX == 3007) {
				c.getPA().movePlayer(3008, 3850, c.heightLevel);
			} else if (c.absX == 3008) {
				c.getPA().movePlayer(3007, 3850, c.heightLevel);
			}
			break;

		case 1816: // lever
			TeleportHandler.teleport(c, 2273, 4680, 0, "modern");
			break;

		case 32015: // ladder
			c.getPA().movePlayer(3017, 3848, 0);
			break;
		/* End of King black dragon objects */

		/* End of Dungeon natigation */

		case 6442: // Donator frost dragons area
			if (c.getInstance().isDonator == 0) {
				c.sendMessage("@red@You must be a Donator to enter this cave!");
				c.sendMessage("@blu@For more information type ::Donate.");
				return;
			} else
				c.getPA().movePlayer(3056, 9555, 0);
			c.sendMessage("Welcome to the Donator only Frost Dragons area.");
			break;

		case 411:
			if (c.getInstance().playerPrayerBook) {
				c.getInstance().playerPrayerBook = false;
				c.startAnimation(645);
				c.sendMessage("You sense a surge of purity flow through your body.");
				c.setSidebarInterface(5, 5608);
				CombatPrayer.resetPrayers(c);
				c.getPA().sendString(":prayer:prayers", -1);
			} else {
				c.getInstance().playerPrayerBook = true;
				c.startAnimation(645);
				c.sendMessage("You sense a surge of power flow through your body!");
				c.setSidebarInterface(5, 22500);
				CombatPrayer.resetPrayers(c);
				c.getPA().sendString(":prayer:curses", -1);
			}
			break;
		case 10091:
			Fishing.fishingNPC(c, 1, 10091);
			break;
		case 6282:
			if (c.getInstance().isDonator == 0) {
				c.sendMessage("@red@You must be a Donator to use this portal!");
				c.sendMessage("@blu@Type ::Donate for more information.");
			} else {
				TeleportHandler.teleport(c, 2392, 9903, 0, "auto");
				c.sendMessage("@red@Welcome to the Donator zone, thank you for donating!");
				return;
			}
			break;
		case 4008:
			if (c.getInstance().specAltarTimer == 0) {
				if (c.getInstance().isDonator != 0) {
					c.startAnimation(645);
					c.getInstance().specAmount = 10;
					c.getInstance();
					c.getItems().addSpecialBar(c.getInstance().playerEquipment[c.playerWeapon]);
					c.sendMessage("@red@Your Special Attack Has Been Fully Restored!");
					c.getInstance().specAltarTimer = 300;
				} else {
					c.sendMessage("@red@You must be a donator to use this altar!");
				}
			} else {
				c.sendMessage("@red@You may only restore your special attack once every 5 minutes!");
			}
			break;
		case 4859:
		case 409:
			if (c.getInstance().playerLevel[5] < c.getPA().getLevelForXP(c.getInstance().playerXP[5])) {
				c.startAnimation(645);
				c.getInstance().playerLevel[5] = c.getPA().getLevelForXP(c.getInstance().playerXP[5]);
				c.sendMessage("You recharge your prayer points.");
				c.getPA().refreshSkill(5);
			} else {
				c.sendMessage("You already have full prayer points.");
			}
			break;
		case 23271:
			c.overWildernessDitch(c.absX, c.absY);
			break;
		case 10596:
			c.getPA().movePlayer(3056, 9555, 0);
			break;
		case 10556:
		case 8929: // Dagganoth entrance
			c.getPA().movePlayer(2442, 10146, 0);
			break;
		case 8966: // Dagganoth stairs
			c.getPA().movePlayer(2523, 3739, 0);
			break;
		case 1765:
			c.getPA().movePlayer(3067, 10256, 0);
			break;
		case 2882:
		case 2883:
			if (c.getInstance().objectX == 3268) {
				if (c.absX < c.getInstance().objectX) {
					c.getPA().walkTo(1, 0);
				} else {
					c.getPA().walkTo(-1, 0);
				}
			}
			break;
		case 272:
			c.getPA().movePlayer(c.absX, c.absY, 1);
			break;

		case 273:
			c.getPA().movePlayer(c.absX, c.absY, 0);
			break;
		case 245:
			c.getPA().movePlayer(c.absX, c.absY + 2, 2);
			break;
		case 246:
			c.getPA().movePlayer(c.absX, c.absY - 2, 1);
			break;
		case 1766:
			c.getPA().movePlayer(3016, 3849, 0);
			break;
		case 6552:
			// c.getPA().switchSpellBook();
			c.getDH().sendDialogues(26, 0);
			break;

		case 1817:
			TeleportHandler.teleport(c, 3067, 10253, 0, "modern");
			break;
		case 1814:
			// ardy lever
			TeleportHandler.teleport(c, 3153, 3923, 0, "modern");
			break;

		case 9356:
			c.getPA().enterCaves();
			break;
		case 1733:
			c.getPA().movePlayer(c.absX, c.absY + 6393, 0);
			break;
		case 117048:
			c.getPA().movePlayer(3087, 3500, 0);
			break;

		case 1734:
			c.getPA().movePlayer(c.absX, c.absY - 6396, 0);
			break;

		case 9357:
			c.getPA().resetTzhaar();
			break;

		case 8959:
			if (c.getX() == 2490 && (c.getY() == 10146 || c.getY() == 10148)) {
				if (c.getPA().checkForPlayer(2490, c.getY() == 10146 ? 10148 : 10146)) {
					new Object(6951, c.getInstance().objectX, c.getInstance().objectY, c.heightLevel, 1, 10, 8959,
							15);
				}
			}
			break;

		case 2112:
			if (c.getInstance().playerLevel[14] <= 84) {
				c.sendMessage("<col=13500416>You need a Mining level of 85+ to enter the Mining Guild.");
				return;
			} else {
				c.getPA().movePlayer(3046, 9756, 0);
			}
			break;

		case 10595:
			c.getPA().movePlayer(3056, 9562, 0);
			c.sendMessage("<col=95>As you enter... You feel a shiver down your spine and an abnormal scream...");
			break;

		case 2213:
		case 4483:
		case 14367:
		case 11758:
		case 3193:
		case 26972:
		case 11402:
		case 24914:
		case 28089:
		case 25808:
		case 27663:
			c.getPA().openUpBank();
			break;

		case 14315:
			if (!PestControl.waitingBoat.containsKey(c)) {
				PestControl.addToWaitRoom(c);
			} else {
				c.getPA().movePlayer(2661, 2639, 0);
			}
			break;

		case 14314:
			if (c.inPcBoat()) {
				if (PestControl.waitingBoat.containsKey(c)) {
					PestControl.leaveWaitingBoat(c);
				} else {
					c.getPA().movePlayer(2657, 2639, 0);
				}
			}
			break;

		case 14235:
		case 14233:
			if (c.getInstance().objectX == 2670) {
				if (c.absX <= 2670) {
					c.absX = 2671;
				} else {
					c.absX = 2670;
				}
			}
			if (c.getInstance().objectX == 2643) {
				if (c.absX >= 2643) {
					c.absX = 2642;
				} else {
					c.absX = 2643;
				}
			}
			if (c.absX <= 2585) {
				c.absY += 1;
			} else {
				c.absY -= 1;
			}
			c.getPA().movePlayer(c.absX, c.absY, 0);
			break;

		case 14829:
		case 14830:
		case 14827:
		case 14828:
		case 14826:
		case 14831:
			Server.objectManager.startObelisk(objectType);
			break;

		case 9369:
			if (c.getY() > 5175) {
				FightPits.addPlayer(c);
			} else {
				FightPits.removePlayer(c, false);
			}
			break;

		case 9368:
			if (c.getY() < 5169) {
				FightPits.removePlayer(c, false);
			}
			break;
		case 4411:
		case 4415:
		case 4417:
		case 4418:
		case 4419:
		case 4420:
		case 4469:
		case 4470:
		case 4911:
		case 4912:
		case 1747:
		case 1757:
			// Server.castleWars.handleObjects(c, objectType, obX, obY);
			break;
		// doors
		case 6749:
			if (obX == 3562 && obY == 9678) {
				c.getPA().object(3562, 9678, 6749, -3, 0);
				c.getPA().object(3562, 9677, 6730, -1, 0);
			} else if (obX == 3558 && obY == 9677) {
				c.getPA().object(3558, 9677, 6749, -1, 0);
				c.getPA().object(3558, 9678, 6730, -3, 0);
			}
			break;
		case 6730:
			if (obX == 3558 && obY == 9677) {
				c.getPA().object(3562, 9678, 6749, -3, 0);
				c.getPA().object(3562, 9677, 6730, -1, 0);
			} else if (obX == 3558 && obY == 9678) {
				c.getPA().object(3558, 9677, 6749, -1, 0);
				c.getPA().object(3558, 9678, 6730, -3, 0);
			}
			break;
		case 6727:
			if (obX == 3551 && obY == 9684) {
				c.sendMessage("You cant open this door..");
			}
			break;
		case 6746:
			if (obX == 3552 && obY == 9684) {
				c.sendMessage("You cant open this door..");
			}
			break;
		case 6748:
			if (obX == 3545 && obY == 9678) {
				c.getPA().object(3545, 9678, 6748, -3, 0);
				c.getPA().object(3545, 9677, 6729, -1, 0);
			} else if (obX == 3541 && obY == 9677) {
				c.getPA().object(3541, 9677, 6748, -1, 0);
				c.getPA().object(3541, 9678, 6729, -3, 0);
			}
			break;
		case 6729:
			if (obX == 3545 && obY == 9677) {
				c.getPA().object(3545, 9678, 6748, -3, 0);
				c.getPA().object(3545, 9677, 6729, -1, 0);
			} else if (obX == 3541 && obY == 9678) {
				c.getPA().object(3541, 9677, 6748, -1, 0);
				c.getPA().object(3541, 9678, 6729, -3, 0);
			}
			break;
		case 6726:
			if (obX == 3534 && obY == 9684) {
				c.getPA().object(3534, 9684, 6726, -4, 0);
				c.getPA().object(3535, 9684, 6745, -2, 0);
			} else if (obX == 3535 && obY == 9688) {
				c.getPA().object(3535, 9688, 6726, -2, 0);
				c.getPA().object(3534, 9688, 6745, -4, 0);
			}
			break;
		case 6745:
			if (obX == 3535 && obY == 9684) {
				c.getPA().object(3534, 9684, 6726, -4, 0);
				c.getPA().object(3535, 9684, 6745, -2, 0);
			} else if (obX == 3534 && obY == 9688) {
				c.getPA().object(3535, 9688, 6726, -2, 0);
				c.getPA().object(3534, 9688, 6745, -4, 0);
			}
			break;
		case 6743:
			if (obX == 3545 && obY == 9695) {
				c.getPA().object(3545, 9694, 6724, -1, 0);
				c.getPA().object(3545, 9695, 6743, -3, 0);
			} else if (obX == 3541 && obY == 9694) {
				c.getPA().object(3541, 9694, 6724, -1, 0);
				c.getPA().object(3541, 9695, 6743, -3, 0);
			}
			break;
		case 6724:
			if (obX == 3545 && obY == 9694) {
				c.getPA().object(3545, 9694, 6724, -1, 0);
				c.getPA().object(3545, 9695, 6743, -3, 0);
			} else if (obX == 3541 && obY == 9695) {
				c.getPA().object(3541, 9694, 6724, -1, 0);
				c.getPA().object(3541, 9695, 6743, -3, 0);
			}
			break;
		// end doors

		// DOORS
		case 1516:
		case 1519:
			if (c.getInstance().objectY == 9698) {
				if (c.absY >= c.getInstance().objectY) {
					c.getPA().walkTo(0, -1);
				} else {
					c.getPA().walkTo(0, 1);
				}
				break;
			}

		case 9319:
			if (c.heightLevel == 0) {
				c.getPA().movePlayer(c.absX, c.absY, 1);
			} else if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX, c.absY, 2);
			}
			break;

		case 9320:
			if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX, c.absY, 0);
			} else if (c.heightLevel == 2) {
				c.getPA().movePlayer(c.absX, c.absY, 1);
			}
			break;

		case 4496:
		case 4494:
			if (c.heightLevel == 2) {
				c.getPA().movePlayer(c.absX - 5, c.absY, 1);
			} else if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 0);
			}
			break;

		case 4493:
			if (c.heightLevel == 0) {
				c.getPA().movePlayer(c.absX - 5, c.absY, 1);
			} else if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 2);
			}
			break;

		case 4495:
			if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 2);
			}
			break;

		case 5126:
			if (c.absY == 3554) {
				c.getPA().walkTo(0, 1);
			} else {
				c.getPA().walkTo(0, -1);
			}
			break;

		case 1755:
			if (c.getInstance().objectX == 2884 && c.getInstance().objectY == 9797) {
				c.getPA().movePlayer(c.absX, c.absY - 6400, 0);
			}
			break;
		case 1759:
			if (c.getInstance().objectX == 2884 && c.getInstance().objectY == 3397) {
				c.getPA().movePlayer(c.absX, c.absY + 6400, 0);
			}
			break;
		/*
		 * case 3203: //dueling forfeit if (c.getVariables().duelCount > 0) {
		 * c.sendMessage("You may not forfeit yet."); break; } Client o =
		 * (Client) PlayerHandler.players[c.getVariables().duelingWith]; if(o ==
		 * null) { c.getVariables().getTradeAndDuel().resetDuel();
		 * c.getPA().movePlayer(Config.DUELING_RESPAWN_X+(Misc.random(Config.
		 * RANDOM_DUELING_RESPAWN)),
		 * Config.DUELING_RESPAWN_Y+(Misc.random(Config.RANDOM_DUELING_RESPAWN))
		 * , 0); break; } if(c.getVariables().duelRule[0]) { c.sendMessage(
		 * "Forfeiting the duel has been disabled!"); break; } if(o != null) {
		 * o.getPA().movePlayer(Config.DUELING_RESPAWN_X+(Misc.random(Config.
		 * RANDOM_DUELING_RESPAWN)),
		 * Config.DUELING_RESPAWN_Y+(Misc.random(Config.RANDOM_DUELING_RESPAWN))
		 * , 0);
		 * c.getPA().movePlayer(Config.DUELING_RESPAWN_X+(Misc.random(Config.
		 * RANDOM_DUELING_RESPAWN)),
		 * Config.DUELING_RESPAWN_Y+(Misc.random(Config.RANDOM_DUELING_RESPAWN))
		 * , 0); o.duelStatus = 6; o.getTradeAndDuel().duelVictory();
		 * c.getVariables().getTradeAndDuel().resetDuel();
		 * c.getVariables().getTradeAndDuel().resetDuelItems(); o.sendMessage(
		 * "The other player has forfeited the duel!"); c.sendMessage(
		 * "You forfeit the duel!"); break; }
		 * 
		 * break;
		 */

		case 2640:
			if (c.getInstance().playerLevel[5] < c.getPA().getLevelForXP(c.getInstance().playerXP[5])) {
				c.startAnimation(645);
				c.getInstance().playerLevel[5] = c.getPA().getLevelForXP(c.getInstance().playerXP[5]);
				c.sendMessage("You recharge your prayer points.");
				c.getPA().refreshSkill(5);
			} else {
				c.sendMessage("You already have full prayer points.");
			}
			break;
		case 2873:
			if (!c.getItems().ownsCape()) {
				c.startAnimation(645);
				c.sendMessage("Saradomin blesses you with a cape.");
				c.getItems().addItem(2412, 1);
			}
			break;
		case 2875:
			if (!c.getItems().ownsCape()) {
				c.startAnimation(645);
				c.sendMessage("Guthix blesses you with a cape.");
				c.getItems().addItem(2413, 1);
			}
			break;
		case 2874:
			if (!c.getItems().ownsCape()) {
				c.startAnimation(645);
				c.sendMessage("Zamorak blesses you with a cape.");
				c.getItems().addItem(2414, 1);
			}
			break;
		case 2879:
			c.getPA().movePlayer(2538, 4716, 0);
			break;
		case 2878:
			c.getPA().movePlayer(2509, 4689, 0);
			break;
		case 5960:
			c.turnPlayerTo(obX, obY);
			c.getPA().startTeleport2(3090, 3956, 0);
			break;

		case 1815:
			c.getPA().startTeleport2(Constants.EDGEVILLE_X, Constants.EDGEVILLE_Y, 0);
			break;

		case 9706:
			c.getPA().startTeleport2(3105, 3951, 0);
			break;
		case 9707:
			c.getPA().startTeleport2(3105, 3956, 0);
			break;

		case 5959:
			c.turnPlayerTo(obX, obY);
			c.getPA().startTeleport2(2539, 4712, 0);
			break;

		case 2558:
			c.sendMessage("This door is locked.");
			break;

		case 9294:
			if (c.absX < c.getInstance().objectX) {
				c.getPA().movePlayer(c.getInstance().objectX + 1, c.absY, 0);
			} else if (c.absX > c.getInstance().objectX) {
				c.getPA().movePlayer(c.getInstance().objectX - 1, c.absY, 0);
			}
			break;

		case 9293:
			if (c.absX < c.getInstance().objectX) {
				c.getPA().movePlayer(2892, 9799, 0);
			} else {
				c.getPA().movePlayer(2886, 9799, 0);
			}
			break;

		case 3044:
			c.getSmithing().sendSmelting();
			break;

		case 733:
			WebsHandler.handleWebs(c, objectType, obX, obY, 0, 1);
			break;

		case 1568:
			// new Object(734, c.getVariables().objectX,
			// c.getVariables().objectY, c.heightLevel, 1, 10, 733, 50);
			c.startAnimation(451);
			c.getPA().movePlayer(3097, 9868, 0);
			break;

		default:
			if (c.getInstance().playerRights == 3 || c.getInstance().playerRights == 8)
				System.out.println("objectClick1_" + objectType + "_" + obX + "_" + obY);
			break;

		}
	}

	private boolean obj(int obX, int obY) {
		return c.getInstance().objectX == obX && c.getInstance().objectY == obY;
	}

	public final int getObjectDistanceRequired(int objectID) {
		switch (objectID) {
		case 2561:
		case 2562:
		case 2563:
		case 2564:
		case 2565:
			return 2;// Thieve stalls
		case 23271:
			return 2;
		case 411:
			return 2;
		default:
			return 1;
		}
	}

	public void secondClickObject(int objectType, int obX, int obY) {
		c.getInstance().clickObjectType = 0;
		// c.sendMessage("Object type: " + objectType);
		/*
		 * if (!Region.objectExists(objectType, obX, obY, c.heightLevel)) {
		 * c.sendMessage("This object does not exist"); return; }
		 */
		if (Farming.inspectObject(c, obX, obY)) {
			return;
		}
		Prospecting.prospectRock(c, objectType);
		switch (objectType) {
		case 10177: // Dagganoth ladder 1st level
			if (obj(2546, 10143)) {
				c.getPA().movePlayer(1798, 4407, 3);
			}
			break;
		case 28089:
			// c.getVariables().GE().openGrandExchange(true);
			c.getPA().sendMessage("Grand Exchange coming soon!");
			break;

		case 2646:
			if (System.currentTimeMillis() - lastClick > 1800) {
				Flax.pickFlax(c, obX, obY);
				lastClick = System.currentTimeMillis();
			}
			break;

		case 1161:
			Pickables.pickupCabbage(c, objectType, obX, obY);
			break;

		case 313:
		case 5583:
		case 5584:
		case 5585:
		case 15508:
			Pickables.pickupWheat(c, objectType, obX, obY);
			break;

		case 312:
			Pickables.pickupPotato(c, objectType, obX, obY);
			break;

		case 3366:
			Pickables.pickupOnion(c, objectType, obX, obY);
			break;

		case 8717:
			Menus.sendSkillMenu(c, "weaving");
			break;
		case 11666:
		case 3044:
			c.getSmithing().sendSmelting();
			break;
		case 2213:
		case 14367:
		case 11758:
		case 3193:
		case 26972:
		case 11402:
		case 24914:
		case 25808:
			c.getPA().openUpBank();
			break;
		case 2561: // Cake stall
			c.getThieving().stealFromStall(1897, 50, 1, objectType, obX, obY);
			break;
		case 2562: // Donator topaz stall
			if (c.getInstance().isDonator == 0) {
				c.sendMessage("@red@You must be a Donator to thieve from this stall!");
				c.sendMessage("@blu@Please type ::Donate for more information.");
				return;
			} else
				c.getThieving().stealFromStall(1613, 300, 1, objectType, obX, obY);
			break;
		case 2563: // Fur stall
			c.getThieving().stealFromStall(6814, 250, 50, objectType, obX, obY);
			break;
		case 2564:
			c.getThieving().stealFromStall(7650, 100, 75, objectType, obX, obY);
			break;
		case 2565: // Silver necklace stall
			c.getThieving().stealFromStall(1796, 300, 85, objectType, obX, obY);
			break;
		case 599:
			c.getPA().showInterface(3559);
			c.getInstance().canChangeAppearance = true;
			break;
		case 2558:
			if (System.currentTimeMillis() - c.getInstance().lastLockPick < 3000 || c.getInstance().freezeTimer > 0) {
				break;
			}
			if (c.getItems().playerHasItem(1523, 1)) {
				c.getInstance().lastLockPick = System.currentTimeMillis();
				if (Misc.random(10) <= 3) {
					c.sendMessage("You fail to pick the lock.");
					break;
				}
				if (c.getInstance().objectX == 3044 && c.getInstance().objectY == 3956) {
					if (c.absX == 3045) {
						c.getPA().walkTo2(-1, 0);
					} else if (c.absX == 3044) {
						c.getPA().walkTo2(1, 0);
					}

				} else if (c.getInstance().objectX == 3038 && c.getInstance().objectY == 3956) {
					if (c.absX == 3037) {
						c.getPA().walkTo2(1, 0);
					} else if (c.absX == 3038) {
						c.getPA().walkTo2(-1, 0);
					}
				} else if (c.getInstance().objectX == 3041 && c.getInstance().objectY == 3959) {
					if (c.absY == 3960) {
						c.getPA().walkTo2(0, -1);
					} else if (c.absY == 3959) {
						c.getPA().walkTo2(0, 1);
					}
				}
			} else {
				c.sendMessage("I need a lockpick to pick this lock.");
			}
			break;
		default:
			if (c.getInstance().playerRights == 3 || c.getInstance().playerRights == 8)
				System.out.println("objectClick2_" + objectType + "_" + obX + "_" + obY);
			break;
		}
	}

	public void thirdClickObject(int objectType, int obX, int obY) {
		c.getInstance().clickObjectType = 0;
		c.sendMessage("Object type: " + objectType);
		switch (objectType) {
		case 10177: // Dagganoth ladder 1st level
			c.getPA().movePlayer(1798, 4407, 3);
			break;
		default:
			if (c.getInstance().playerRights == 3 || c.getInstance().playerRights == 8)
				System.out.println("objectClick3_" + objectType + "_" + obX + "_" + obY);
			break;
		}
	}

	public void firstClickNpc(int npcType) {
		if (TransformHandler.Strykewyrm(c)) {
			c.getInstance().clickNpcType = 0;
			c.getInstance().npcClickIndex = 0;
			return;
		}
		Fishing.fishingNPC(c, 1, npcType);
		if (c.getInstance().teleTimer > 0) {
			return;
		}
		// if (c.getVariables().getHunter().hasReqs(npcType)) {
		// c.getVariables().getHunter().Checking(npcType);
		// }
		switch (npcType) {
		case 5113:
			c.getShops().openShop(76);
			break;
		case 1699:
			c.getShops().openShop(77);
			break;
		case 6742:
			c.getDH().sendDialogues(270, 6742);
			break;
		case 1282:
			c.getDH().sendDialogues(254, npcType);
			break;
		case 9713:
			c.sendMessage("Not done");
			break;

		case 2458:
			c.getDH().sendDialogues(1103, npcType);
			break;
		case 2305:
			c.getDH().sendDialogues(30, npcType);
			break;
		case 2790:
			c.getDH().sendDialogues(1105, npcType);
			break;
		case 2566:
			c.getDH().sendDialogues(170, npcType);
			break;
		case 1598:
		case 1596:
			c.getDH().sendDialogues(37, npcType);
			break;
		case 2824:
		case 804:
			Tanning.tanningInterface(c);
			break;
		case 519:
			c.getDH().sendDialogues(50, npcType);
			// Bobs axes
			break;
		case 9085:
			// if (c.getVariables().slayerTask <= 0) {
			c.getDH().sendDialogues(111, npcType);
			// } else {
			// c.getDH().sendDialogues(113, npcType);
			// }
			break;// Kuradal
		case 594:
			c.getDH().sendDialogues(200, npcType);
			// Nurmof
			break;
		case 945:
			c.getDH().sendDialogues(250, npcType);
			// Project-Exile Guide
			break;
		case 2259:
			c.getDH().sendDialogues(320, npcType);
			break;
		case 948:
			c.getDH().sendDialogues(205, npcType);
			// Mining instructor
			break;
		case 568:
			c.getDH().sendDialogues(300, npcType);
			break;
		case 576:
			c.getDH().sendDialogues(210, npcType);
			break;
		case 556:
			c.getDH().sendDialogues(215, npcType);
			break;
		case 6540:
			c.getDH().sendDialogues(4, npcType);
			break;
		case 3791:
			c.getDH().sendDialogues(22, npcType);
			break;
		case 872:
			c.getDH().sendDialogues(20, npcType);
			break;
		case 5029:
			c.getDH().sendDialogues(1, npcType);
			break;
		case 706:
			c.getDH().sendDialogues(9, npcType);
			break;
		case 2258:
			c.getDH().sendDialogues(150, npcType);
			break;
		case 2024:
			c.getDH().sendDialogues(8, npcType);
			break;
		case 8725:
			c.getDH().sendDialogues(17, npcType);
			break;
		case 1152:
			c.getDH().sendDialogues(16, npcType);
			break;
		case 542:
			c.getShops().openShop(10);
			break;
		case 534:
			c.getShops().openShop(11);
			break;
		case 587:
			c.getShops().openShop(75);
			break;
		case 554:
			c.getShops().openShop(68);
			break;
		case 494:
		case 902:
			c.getPA().openUpBank();
			break;

		case 3788: // void knight
			c.getDH().sendDialogues(180, npcType);
			break;

		case 6532:
		case 6535:
		case 6533:
		case 6534:
		case 6531:
		case 6528:
		case 6529:
		case 6530:
			c.getPA().openUpBank();
			break;

		case 522:
		case 523:
			c.getShops().openShop(1);
			break;
		case 557:
			c.getShops().openShop(21);
			break;
		case 599:
			c.getPA().showInterface(3559);
			c.getInstance().canChangeAppearance = true;
			break;

		case 541:
			c.getShops().openShop(5);
			break;

		case 461:
			c.getShops().openShop(2);
			break;

		case 683:
			c.getShops().openShop(100);
			break;

		case 549:
			c.getShops().openShop(4);
			break;

		case 2538:
			c.getShops().openShop(6);
			break;
		case 518:
			c.getShops().openShop(3);
			break;
		case 517:
			c.getShops().openShop(94);
			break;
		case 6891:
			c.getShops().openShop(30);
			break;
		case 904:
			c.sendMessage("You have " + c.getInstance().magePoints + " points.");
			break;
		default:
			if (c.getInstance().playerRights == 3 || c.getInstance().playerRights == 8)
				System.out.println("npcClick1_" + npcType);
			if (c.getInstance().playerRights == 3) {
				Misc.println("First Click Npc : " + npcType);
			}
			break;
		}
	}

	public void secondClickNpc(int npcType) {
		c.getInstance().clickNpcType = 0;
		c.getInstance().npcClickIndex = 0;
		Fishing.fishingNPC(c, 2, npcType);
		switch (npcType) {
		case 11226:
			Dungeon.openShop(c);
			break;
		case 1699:
			c.getShops().openShop(77);
			break;
		case 5030:
			c.getDH().sendDialogues(1164, c.getInstance().npcType);
			break;
		case 9713:
			c.getDH().sendDialogues(204, npcType);
			break;
		case 587:
			c.getShops().openShop(75);
			break;
		case 2566:
			c.getShops().openSkillCape();
			break;
		case 3788:
			c.getShops().openShop(72);
			c.sendMessage("You currently have " + c.getInstance().pcPoints + " pest control points.");
			break;
		case 9085:
			c.getDH().sendDialogues(112, c.getInstance().npcType);
			break;
		case 6528:
		case 6529:
		case 6531:
		case 6530:
			c.getPA().sendMessage("Grand Exchange coming soon!");
			// c.getVariables().GE().openGrandExchange(true);
			break;
		case 556:
			c.getShops().openShop(13);
			c.sendMessage("@red@You currently have " + c.getInstance().votingPoints + " Voting Points.");
			c.sendMessage("@red@To obtain more Voting Points, type ::Vote and follow the simple steps.");
			break;
		case 2305:
			c.getShops().openShop(15);
			break;
		case 576:
			c.getShops().openShop(19);
			break;
		case 300:
		case 844:
		case 462:
			NPC n = NPCHandler.npcs[c.getInstance().npcClickIndex];
			if (n == null) {
				return;
			}
			Runecrafting.teleportRunecraft(c, n);
			break;
		case 2259:
			c.getShops().openShop(20);
			break;
		case 945:
			c.getShops().openShop(72);
			break;
		case 568:
			c.getDH().sendDialogues(350, c.getInstance().npcType);
			break;
		case 594:
			c.getShops().openShop(16);
			break;

		case 5029:
			c.getShops().openShop(2);
			break;
		case 554:
			c.getShops().openShop(68);
			break;
		case 534:
			c.getShops().openShop(11);
			break;
		case 542:
			c.getShops().openShop(10);
			break;
		case 1282:
			c.getShops().openShop(73);
			break;
		case 494:
		case 902:
			c.getPA().openUpBank();
			break;
		case 904:
			c.getShops().openShop(17);
			break;
		case 522:
		case 523:
			c.getShops().openShop(1);
			break;
		case 541:
			c.getShops().openShop(5);
			break;

		case 461:
			c.getShops().openShop(2);
			break;

		case 683:
			c.getShops().openShop(3);
			break;

		case 2538:
			c.getShops().openShop(6);
			break;

		case 519:
			c.getShops().openShop(9);
			break;
		case 3789:
			c.getShops().openShop(18);
			break;
		case 6534:
		case 6533:
		case 6535:
		case 6532:
			c.getPA().openUpBank();
			break;

		case 1:
		case 2:
		case 9:
		case 18:
		case 20:
		case 26:
		case 21:
			c.getThieving().stealFromNPC(npcType);
			break;
		default:
			if (c.getInstance().playerRights == 3 || c.getInstance().playerRights == 8)
				System.out.println("npcClick2_" + npcType);
			if (c.getInstance().playerRights == 3) {
				Misc.println("Second Click Npc : " + npcType);
			}
			break;

		}
	}

	public void thirdClickNpc(int npcType) {
		c.getInstance().clickNpcType = 0;
		c.getInstance().npcClickIndex = 0;
		switch (npcType) {
		case 9085:
			c.getShops().openShop(74);
			break;
		case 5029:
			c.getDH().sendDialogues(1164, c.getInstance().npcType);
			break;
		case 553:
			NPC n = NPCHandler.npcs[c.getInstance().npcClickIndex];
			if (n == null) {
				return;
			}
			Runecrafting.teleportRunecraft(c, n);
			break;
		/*
		 * case 5029: c.getShops().openShop(71); break;
		 */
		default:
			if (c.getInstance().playerRights == 3 || c.getInstance().playerRights == 8)
				System.out.println("npcClick3_" + npcType);
			if (c.getInstance().playerRights == 3) {
				Misc.println("Third Click NPC : " + npcType);
			}
			break;

		}
	}

	public void fourthClickNpc(int npcType) {
		c.getInstance().clickNpcType = 0;
		c.getInstance().npcClickIndex = 0;
		switch (npcType) {

		case 9085:
			c.getShops().openShop(71);
			c.sendMessage("@blu@You currently have " + c.getInstance().SlayerPoints + " Slayer points.");
			break;

		default:
			if (c.getInstance().playerRights == 3 || c.getInstance().playerRights == 8)
				System.out.println("npcClick4_" + npcType);
			if (c.getInstance().playerRights == 3)
				System.out.println("Fourth Click NPC : " + npcType);
			break;

		}
	}

	private void handleDagganothObjects(int objectId) {
		switch (objectId) {
		case 8930:
			c.getPA().movePlayer(1975, 4409, 3);
			break;

		case 10177: // Dagganoth ladder 1st level
			c.getPA().movePlayer(1798, 4407, 3);
			break;

		case 10193:
			c.getPA().movePlayer(2545, 10143, 0);
			break;

		case 10194:
			c.getPA().movePlayer(2544, 3741, 0);
			break;

		case 10195:
			c.getPA().movePlayer(1809, 4405, 2);
			break;

		case 10196:
			c.getPA().movePlayer(1807, 4405, 3);
			break;

		case 10197:
			c.getPA().movePlayer(1823, 4404, 2);
			break;

		case 10198:
			c.getPA().movePlayer(1825, 4404, 3);
			break;

		case 10199:
			c.getPA().movePlayer(1834, 4388, 2);
			break;

		case 10200:
			c.getPA().movePlayer(1834, 4390, 3);
			break;

		case 10201:
			c.getPA().movePlayer(1811, 4394, 1);
			break;

		case 10202:
			c.getPA().movePlayer(1812, 4394, 2);
			break;

		case 10203:
			c.getPA().movePlayer(1799, 4386, 2);
			break;

		case 10204:
			c.getPA().movePlayer(1799, 4388, 1);
			break;

		case 10205:
			c.getPA().movePlayer(1796, 4382, 1);
			break;

		case 10206:
			c.getPA().movePlayer(1796, 4382, 2);
			break;

		case 10207:
			c.getPA().movePlayer(1800, 4369, 2);
			break;

		case 10208:
			c.getPA().movePlayer(1802, 4370, 1);
			break;

		case 10209:
			c.getPA().movePlayer(1827, 4362, 1);
			break;

		case 10210:
			c.getPA().movePlayer(1825, 4362, 2);
			break;

		case 10211:
			c.getPA().movePlayer(1863, 4373, 2);
			break;

		case 10212:
			c.getPA().movePlayer(1863, 4371, 1);
			break;

		case 10213:
			c.getPA().movePlayer(1864, 4389, 1);
			break;

		case 10214:
			c.getPA().movePlayer(1864, 4387, 2);
			break;

		case 10215:
			c.getPA().movePlayer(1890, 4407, 0);
			break;

		case 10216:
			c.getPA().movePlayer(1890, 4406, 1);
			break;

		case 10217:
			c.getPA().movePlayer(1957, 4373, 1);
			break;

		case 10218:
			c.getPA().movePlayer(1957, 4371, 0);
			break;

		case 10219:
			c.getPA().movePlayer(1824, 4379, 3);
			break;

		case 10220:
			c.getPA().movePlayer(1824, 4381, 2);
			break;

		case 10221:
			c.getPA().movePlayer(1838, 4375, 2);
			break;

		case 10222:
			c.getPA().movePlayer(1838, 4377, 3);
			break;

		case 10223:
			c.getPA().movePlayer(1850, 4386, 1);
			break;

		case 10224:
			c.getPA().movePlayer(1850, 4387, 2);
			break;

		case 10225:
			c.getPA().movePlayer(1932, 4378, 1);
			break;

		case 10226:
			c.getPA().movePlayer(1932, 4380, 2);
			break;

		case 10227:
			if (obj(1961, 4392)) {
				c.getPA().movePlayer(1961, 4392, 2);
			} else {
				c.getPA().movePlayer(1932, 4377, 1);
			}
			break;

		case 10228:
			c.getPA().movePlayer(1961, 4393, 3);
			break;

		case 10229:
			c.getPA().movePlayer(1912, 4367, 0);
			break;

		case 10230:
			c.getPA().movePlayer(2899, 4449, 0);
			break;
		}
	}
}