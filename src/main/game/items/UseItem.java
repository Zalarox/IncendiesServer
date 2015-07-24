package main.game.items;

import main.game.players.Player;
import main.game.players.content.CrystalChest;
import main.game.players.content.skills.crafting.GlassMaking;
import main.game.players.content.skills.crafting.PotteryMaking;
import main.game.players.content.skills.farming.Farming;
import main.game.players.content.skills.prayer.Prayer;
import main.game.players.content.skills.runecrafting.MixingRunes;
import main.game.players.content.skills.runecrafting.RunecraftAltars;
import main.game.players.content.skills.runecrafting.Tiaras;
import main.handlers.SkillHandler;
import main.util.Menus;
import main.util.Misc;

/**
 * 
 * @author Ryan / Lmctruck30
 *
 */
public class UseItem {

	public static void ItemonObject(Player c, int objectID, int objectX, int objectY, int itemId) {

		if (!c.getItems().playerHasItem(itemId, 1)) {

			return;
		}

		// ObjectDef def = ObjectDef.forId(objectID);

		// farming
		if (Farming.prepareCrop(c, itemId, objectID, objectX, objectY)) {
			return;
		}

		if (RunecraftAltars.useTaliOnRuin(c, itemId, objectID)) {

			return;
		}

		if (MixingRunes.combineRunes(c, itemId, objectID)) {

			return;
		}

		if (Tiaras.bindTiara(c, itemId, objectID)) {

			return;
		}
		// TODO
		/*
		 * if(def != null) { if (def.name.toLowerCase().contains("furn")) { if
		 * (itemId == GlassMaking.BUCKET_OF_SAND) {
		 * GlassMaking.makeMoltenGlass(c); } else if (itemId ==
		 * GemCrafting.GOLD_BAR) { GemCrafting.openInterface(c); } else if
		 * (itemId == SilverCrafting.SILVER_BAR) { Menus.sendSkillMenu(c,
		 * "silverCrafting"); } } }
		 */

		if (c.getCooking().handleInterface(itemId, objectID, objectX, objectY)) {
			return;
		}
		if (c.getCooking().handleFillingObjectWater(itemId, objectID)) {
			c.getInventory().update();
			return;
		}
		switch (itemId) {
		case 4045: // explosive potion
			if (!c.getItems().playerHasItem(4045, 2)) {
				c.sendMessage("You need 2 explosive potions to blow this up!");
				return;
			}
			switch (objectID) {
			case 4437: // explode rocks
				if (!c.goodDistance(objectX, objectY, c.getX(), c.getY(), 2)) {
					return;
				}
				/*
				 * if (objectX == 2391 && objectY == 9501 &&
				 * !CastleWars.saraRock1 || objectX == 2400 && objectY == 9512
				 * && !CastleWars.saraRock2 || objectX == 2409 && objectY ==
				 * 9503 && !CastleWars.zammyRock1 || objectX == 2401 && objectY
				 * == 9494 && !CastleWars.zammyRock2) { c.sendMessage(
				 * "This rock has already been removed."); break; } if (objectX
				 * == 2391 && objectY == 9501) {
				 * PlayerAssistant.createStillGfx(157, 2391, 9501, 0, 1);
				 * CastleWars.saraRock1 = false;
				 * CastleWars.updateCwObects("rock", 1); } if (objectX == 2400
				 * && objectY == 9512) { PlayerAssistant.createStillGfx(157,
				 * 2400, 9512, 0, 1); CastleWars.saraRock2 = false;
				 * CastleWars.updateCwObects("rock", 1); } if (objectX == 2409
				 * && objectY == 9503) { PlayerAssistant.createStillGfx(157,
				 * 2409, 9503, 0, 1); CastleWars.zammyRock1 = false;
				 * CastleWars.updateCwObects("rock", 2); } if (objectX == 2401
				 * && objectY == 9494) { PlayerAssistant.createStillGfx(157,
				 * 2401, 9494, 0, 1); CastleWars.zammyRock2 = false;
				 * CastleWars.updateCwObects("rock", 2); } c.sendMessage(
				 * "You explode the rocks with the explosive potion.");
				 * c.getItems().deleteItem(4045, 2); break; case 4448: //
				 * explode-collapse tunnel if (!c.goodDistance(objectX, objectY,
				 * c.getX(), c.getY(), 2)) { return; } if (objectX >= 2390 &&
				 * objectX <= 2393 && objectY >= 9500 && objectY <= 9503 &&
				 * CastleWars.saraRock1 || objectX >= 2399 && objectX <= 2402 &&
				 * objectY >= 9511 && objectY <= 9514 && CastleWars.saraRock2 ||
				 * objectX >= 2408 && objectX <= 2411 && objectY >= 9502 &&
				 * objectY <= 9505 && CastleWars.zammyRock1 || objectX >= 2400
				 * && objectX <= 2403 && objectY >= 9493 && objectY <= 9496 &&
				 * CastleWars.zammyRock2) { c.sendMessage(
				 * "This passage is already collapsed."); return; } if (objectX
				 * >= 2390 && objectX <= 2393 && objectY >= 9500 && objectY <=
				 * 9503) { c.getPA().hitPlayers(2391, 2392, 9501, 9502, 1);
				 * PlayerAssistant.createStillGfx(157, 2391, 9501, 0, 1);
				 * CastleWars.saraRock1 = true;
				 * CastleWars.updateCwObects("rock", 1); } if (objectX >= 2399
				 * && objectX <= 2402 && objectY >= 9511 && objectY <= 9514) {
				 * c.getPA().hitPlayers(2400, 2401, 9512, 9513, 1);
				 * PlayerAssistant.createStillGfx(157, 2400, 9512, 0, 1);
				 * CastleWars.saraRock2 = true;
				 * CastleWars.updateCwObects("rock", 1); } if (objectX >= 2408
				 * && objectX <= 2411 && objectY >= 9502 && objectY <= 9505) {
				 * c.getPA().hitPlayers(2409, 2410, 9503, 9504, 1);
				 * PlayerAssistant.createStillGfx(157, 2409, 9503, 0, 1);
				 * CastleWars.zammyRock1 = true;
				 * CastleWars.updateCwObects("rock", 2); } if (objectX >= 2400
				 * && objectX <= 2403 && objectY >= 9493 && c.objectY <= 9496) {
				 * c.getPA().hitPlayers(2401, 2402, 9494, 9495, 1);
				 * PlayerAssistant.createStillGfx(157, 2401, 9494, 0, 1);
				 * CastleWars.zammyRock2 = true;
				 * CastleWars.updateCwObects("rock", 2); } c.sendMessage(
				 * "You collapase the tunnel with the explosive potion.");
				 * c.getItems().deleteItem(4045, 2); break; case 4381: // zammy
				 * catapult if (!c.goodDistance(objectX, objectY, c.getX(),
				 * c.getY(), 3)) { return; } if (c.zammyTeam()) { c.sendMessage(
				 * "You can't destroy your own catapult!"); return; }
				 * PlayerAssistant.createStillGfx(287, objectX, objectY, 0, 1);
				 * c.sendMessage("You have destroyed the enemies catapult!");
				 * CastleWars.zammyCatapult = false;
				 * CastleWars.updateCwObects("catapult", 2);
				 * c.getItems().deleteItem(4045, 2); break; case 4382: // sara
				 * catapult if (!c.goodDistance(objectX, objectY, c.getX(),
				 * c.getY(), 3)) { return; } if (c.saraTeam()) { c.sendMessage(
				 * "You can't destroy your own catapult!"); return; }
				 * PlayerAssistant.createStillGfx(287, objectX, objectY, 0, 1);
				 * c.sendMessage("You have destroyed the enemies catapult!");
				 * CastleWars.saraCatapult = false;
				 * CastleWars.updateCwObects("catapult", 1);
				 * c.getItems().deleteItem(4045, 2); break;
				 */
			}
		}
		switch (objectID) {
		case 172:
			if (itemId == CrystalChest.KEY) {
				CrystalChest.searchChest(c, objectID, objectX, objectY);
			}
			break;
		case 2645:// pile of sand
			if (itemId == GlassMaking.BUCKET) {
				GlassMaking.fillWithSand(c);
			}
			break;
		case 2642:// pottery unfire
			if (itemId == PotteryMaking.SOFT_CLAY) {
				Menus.sendSkillMenu(c, "potteryUnfired");
			}
			break;
		case 2783:
			c.getSmithingInt().showSmithInterface(itemId);
			break;

		case 409:
			Prayer.sendPrayerInterface(c, itemId, objectX, objectY);
			break;
		default:
			if (c.getVariables().playerRights == 3) {
				Misc.println("Player At Object id: " + objectID + " with Item id: " + itemId);
			}
			break;
		}

	}

	public static void ItemonItem(Player c, int itemUsed, int useWith) {
		if (itemUsed == 145 && useWith == 261 || itemUsed == 261 && useWith == 145) {
			if (c.getItems().playerHasItem(145, 1) && c.getItems().playerHasItem(261, 1)) {
				if (c.playerLevel[c.playerHerblore] >= 88) {
					c.getItems().deleteItem(145, 1);
					c.getItems().deleteItem(261, 1);
					c.getItems().addItem(15309, 1);
					c.sendMessage("You make a Extreme Attack (3).");
					c.getPA().addSkillXP(220 * SkillHandler.XPRates.HERBLORE.getXPRate(), c.playerHerblore);
				} else {
					c.sendMessage("You need a herblore level of 88 to make that potion.");
				}
			} else {
				c.sendMessage("You need super attack (3) and a Clean avantoe to make that.");
			}
		}

		if (itemUsed == 267 && useWith == 157 || itemUsed == 157 && useWith == 267) {
			if (c.getItems().playerHasItem(157, 1) && c.getItems().playerHasItem(267, 1)) {
				if (c.playerLevel[c.playerHerblore] >= 89) {
					c.getItems().deleteItem(267, 1);
					c.getItems().deleteItem(157, 1);
					c.getItems().addItem(15313, 1);
					c.sendMessage("You make a Extreme Strength (3).");
					c.getPA().addSkillXP(230 * SkillHandler.XPRates.HERBLORE.getXPRate(), c.playerHerblore);
				} else {
					c.sendMessage("You need a herblore level of 89 to make that potion.");
				}
			} else {
				c.sendMessage("You need super strength (3) and a Clean Dwarf Weed.");
			}
		}

		if (itemUsed == 3026 && useWith == 3000 || itemUsed == 3000 && useWith == 3026) {
			if (c.getItems().playerHasItem(3026, 1) && c.getItems().playerHasItem(3000, 1)) {
				if (c.playerLevel[c.playerHerblore] >= 85) {
					c.getItems().deleteItem(3026, c.getItems().getItemSlot(3026), 1);
					c.getItems().deleteItem(3000, c.getItems().getItemSlot(3000), 1);
					c.getItems().addItem(15301, 1);
					c.sendMessage("You make a Recover Special (3).");
					c.getPA().addSkillXP(200 * SkillHandler.XPRates.HERBLORE.getXPRate(), c.playerHerblore);
				} else {
					c.sendMessage("You need a herblore level of 85 to make that potion.");
				}
			} else {
				c.sendMessage("You need super restore (3) and a Clean Snap dragon.");
			}
		}

		if (itemUsed == 3042 && useWith == 3000 || itemUsed == 3000 && useWith == 3042) {
			if (c.getItems().playerHasItem(3042, 1) && c.getItems().playerHasItem(3000, 1)) {
				if (c.playerLevel[c.playerHerblore] >= 93) {
					c.getItems().deleteItem(3042, 1);
					c.getItems().deleteItem(3000, 1);
					c.getItems().addItem(15321, 1);
					c.sendMessage("You make a Extreme Mage (3).");
					c.getPA().addSkillXP(320 * SkillHandler.XPRates.HERBLORE.getXPRate(), c.playerHerblore);
				} else {
					c.sendMessage("You need a herblore level of 93 to make that potion.");
				}
			} else {
				c.sendMessage("You need a Magic Potion (3) and a Clean Snap dragon.");
			}
		}

		if (itemUsed == 139 && useWith == 3000 || itemUsed == 3000 && useWith == 139) {
			if (c.getItems().playerHasItem(139, 1) && c.getItems().playerHasItem(3000, 1)) {
				if (c.playerLevel[c.playerHerblore] >= 92) {
					c.getItems().deleteItem(139, 1);
					c.getItems().deleteItem(3000, 1);
					c.getItems().addItem(15329, 1);
					c.sendMessage("You make a Super Prayer (3).");
					c.getPA().addSkillXP(330 * SkillHandler.XPRates.HERBLORE.getXPRate(), c.playerHerblore);
				} else {
					c.sendMessage("You need a herblore level of 92 to make that potion.");
				}
			} else {
				c.sendMessage("You need a Prayer Potion (3) and a Clean Snap dragon.");
			}
		}

		if (itemUsed == 169 && useWith == 3000 || itemUsed == 3000 && useWith == 169) {
			if (c.getItems().playerHasItem(169, 1) && c.getItems().playerHasItem(3000, 1)) {
				if (c.playerLevel[c.playerHerblore] >= 91) {
					c.getItems().deleteItem(169, 1);
					c.getItems().deleteItem(3000, 1);
					c.getItems().addItem(15325, 1);
					c.sendMessage("You make a Extreme Range (3).");
					c.getPA().addSkillXP(310 * SkillHandler.XPRates.HERBLORE.getXPRate(), c.playerHerblore);
				} else {
					c.sendMessage("You need a herblore level of 91 to make that potion.");
				}
			} else {
				c.sendMessage("You need a ranging potion (3) and a Clean Snap dragon.");
			}
		}

		if (itemUsed == 2481 && useWith == 163 || itemUsed == 163 && useWith == 2481) {
			if (c.getItems().playerHasItem(2481, 1) && c.getItems().playerHasItem(163, 1)) {
				if (c.playerLevel[c.playerHerblore] >= 90) {
					c.getItems().deleteItem(2481, 1);
					c.getItems().deleteItem(163, 1);
					c.getItems().addItem(15317, 1);
					c.sendMessage("You make a Extreme Defence (3).");
					c.getPA().addSkillXP(240 * SkillHandler.XPRates.HERBLORE.getXPRate(), c.playerHerblore);
				} else {
					c.sendMessage("You need a herblore level of 90 to make that potion.");
				}
			} else {
				c.sendMessage("You need super Defence (3) and a Clean lantadyme.");
			}
		}

		if (itemUsed == 269 && useWith == 15309 || itemUsed == 269 && useWith == 15313
				|| itemUsed == 269 && useWith == 15317 || itemUsed == 269 && useWith == 15321
				|| itemUsed == 269 && useWith == 15325) {
			if (c.getItems().playerHasItem(15309, 1) && c.getItems().playerHasItem(15313, 1)
					&& c.getItems().playerHasItem(15317, 1) && c.getItems().playerHasItem(15321, 1)
					&& c.getItems().playerHasItem(15325, 1)) {
				if (c.playerLevel[c.playerHerblore] >= 96) {
					c.getItems().deleteItem(269, 1);
					c.getItems().deleteItem(15309, 1);
					c.getItems().deleteItem(15313, 1);
					c.getItems().deleteItem(15317, 1);
					c.getItems().deleteItem(15321, 1);
					c.getItems().deleteItem(15325, 1);
					c.getItems().addItem(15332, 1);
					c.sendMessage("You make a Overload Potion (4).");
					c.getPA().addSkillXP(400 * SkillHandler.XPRates.HERBLORE.getXPRate(), c.playerHerblore);
				} else {
					c.sendMessage("You need a herblore level of 96 to make that potion.");
				}
			} else {
				c.sendMessage("You need all extreme potions to make a Overload & A Clean Torstol.");
			}
		}
		if (itemUsed == CrystalChest.toothHalf() && useWith == CrystalChest.loopHalf()
				|| itemUsed == CrystalChest.loopHalf() && useWith == CrystalChest.toothHalf()) {
			CrystalChest.makeKey(c);
		}
		/* Cooking */
		if ((itemUsed == 1929 && useWith == 1933) || (itemUsed == 1933 && useWith == 1929)) {

			c.setStatedInterface("flour");
			c.getPA().sendString("Bread dough", 13770);
			c.getPA().sendString("Pastry dough", 13771);
			c.getPA().sendString("Pizza base", 13772);
			c.getPA().sendChatInterface(13768);
		}

		if (itemUsed >= 11710 && itemUsed <= 11714 && useWith >= 11710 && useWith <= 11714) {
			if (c.getItems().hasAllShards()) {
				c.getItems().makeBlade();
			}
		}
		if (c.getItems().isHilt(itemUsed) || c.getItems().isHilt(useWith)) {
			int hilt = c.getItems().isHilt(itemUsed) ? itemUsed : useWith;
			int blade = c.getItems().isHilt(itemUsed) ? useWith : itemUsed;
			if (blade == 11690) {
				c.getItems().makeGodsword(hilt);
			}
		}
		if ((itemUsed == 1540 && useWith == 11286) || (itemUsed == 11286 && useWith == 1540)) {
			if (c.getVariables().playerLevel[c.getVariables().playerSmithing] >= 95) {
				c.getItems().deleteItem(1540, c.getItems().getItemSlot(1540), 1);
				c.getItems().deleteItem(11286, c.getItems().getItemSlot(11286), 1);
				c.getItems().addItem(11284, 1);
				c.sendMessage("You combine the two materials to create a dragonfire shield.");
				c.getPA().addSkillXP(500 * SkillHandler.XPRates.SMITHING.getXPRate(), c.getVariables().playerSmithing);
			} else {
				c.sendMessage("You need a smithing level of 95 to create a dragonfire shield.");
			}
		}
		if (itemUsed == 2368 && useWith == 2366 || itemUsed == 2366 && useWith == 2368) {
			c.getItems().deleteItem(2368, c.getItems().getItemSlot(2368), 1);
			c.getItems().deleteItem(2366, c.getItems().getItemSlot(2366), 1);
			c.getItems().addItem(1187, 1);
		}

		switch (itemUsed) {

		case 1755:
			switch (useWith) {
			case 1603:
				if (c.getVariables().playerLevel[9] < 63) {
					c.sendMessage("You need a fletching level of 63 to cut this.");
					return;
				}
				c.getItems().deleteItem2(useWith, 1);
				c.getItems().addItem(9191, 12);
				c.startAnimation(887);
				c.getPA().addSkillXP(6 * c.getPA().getExp(), 9);
				c.getPA().refreshSkill(9);
				break;
			case 1601:
				if (c.getVariables().playerLevel[9] < 65) {
					c.sendMessage("You need a fletching level of 65 to cut this.");
					return;
				}
				c.getItems().deleteItem2(useWith, 1);
				c.getItems().addItem(9192, 12);
				c.startAnimation(886);
				c.getPA().addSkillXP(7 * c.getPA().getExp(), 9);
				c.getPA().refreshSkill(9);
				break;
			case 1615:
				if (c.getVariables().playerLevel[9] < 71) {
					c.sendMessage("You need a fletching level of 71 to cut this.");
					return;
				}
				c.getItems().deleteItem2(useWith, 1);
				c.getItems().addItem(9193, 12);
				c.startAnimation(885);
				c.getPA().addSkillXP(8 * c.getPA().getExp(), 9);
				c.getPA().refreshSkill(9);
				break;
			case 6573:
				if (c.getVariables().playerLevel[9] < 73) {
					c.sendMessage("You need a fletching level of 73 to cut this.");
					return;
				}
				c.getItems().deleteItem2(useWith, 1);
				c.getItems().addItem(9194, 24);
				c.startAnimation(2717);
				c.getPA().addSkillXP(9 * c.getPA().getExp(), 9);
				c.getPA().refreshSkill(9);
				break;
			}
			break;

		case 9143:
			switch (useWith) {
			case 9191:
				if (c.getVariables().playerLevel[9] < 63) {
					c.sendMessage("You need a fletching level of 63 to attach these.");
					return;
				}
				if (!c.getItems().playerHasItem(itemUsed, 12) || !c.getItems().playerHasItem(useWith, 12)) {
					c.sendMessage("You need 12 bolts and bolt tips to attach these.");
					return;
				}
				c.getItems().deleteItem2(useWith, 12);
				c.getItems().deleteItem2(itemUsed, 12);
				c.getItems().addItem(9242, 12);
				c.getPA().addSkillXP(6 * c.getPA().getExp(), 9);
				c.getPA().refreshSkill(9);
				break;
			case 9192:
				if (c.getVariables().playerLevel[9] < 65) {
					c.sendMessage("You need a fletching level of 65 to attach these.");
					return;
				}
				if (!c.getItems().playerHasItem(itemUsed, 12) || !c.getItems().playerHasItem(useWith, 12)) {
					c.sendMessage("You need 12 bolts and bolt tips to attach these.");
					return;
				}
				c.getItems().deleteItem2(useWith, 12);
				c.getItems().deleteItem2(itemUsed, 12);
				c.getItems().addItem(9243, 12);
				c.getPA().addSkillXP(7 * c.getPA().getExp(), 9);
				c.getPA().refreshSkill(9);
				break;
			}
			break;

		case 9144:
			switch (useWith) {
			case 9193:
				if (c.getVariables().playerLevel[9] < 71) {
					c.sendMessage("You need a fletching level of 71 to attach these.");
					return;
				}
				if (!c.getItems().playerHasItem(itemUsed, 12) || !c.getItems().playerHasItem(useWith, 12)) {
					c.sendMessage("You need 12 bolts and bolt tips to attach these.");
					return;
				}
				c.getItems().deleteItem2(useWith, 12);
				c.getItems().deleteItem2(itemUsed, 12);
				c.getItems().addItem(9244, 12);
				c.getPA().addSkillXP(8 * c.getPA().getExp(), 9);
				c.getPA().refreshSkill(9);
				break;
			case 9194:
				if (c.getVariables().playerLevel[9] < 73) {
					c.sendMessage("You need a fletching level of 73 to attach these.");
					return;
				}
				if (!c.getItems().playerHasItem(itemUsed, 12) || !c.getItems().playerHasItem(useWith, 12)) {
					c.sendMessage("You need 12 bolts and bolt tips to attach these.");
					return;
				}
				c.getItems().deleteItem2(useWith, 12);
				c.getItems().deleteItem2(itemUsed, 12);
				c.getItems().addItem(9245, 12);
				c.getPA().addSkillXP(9 * c.getPA().getExp(), 9);
				c.getPA().refreshSkill(9);
				break;
			}
			break;

		default:
			if (c.getVariables().playerRights == 3) {
				Misc.println("Player used Item id: " + itemUsed + " with Item id: " + useWith);
			}
			break;
		}
	}

	public static void ItemonNpc(Player c, int itemId, int npcId, int slot) {
		switch (itemId) {

		default:
			if (c.getVariables().playerRights == 3) {
				Misc.println("Player used Item id: " + itemId + " with Npc id: " + npcId + " With Slot : " + slot);
			}
			break;
		}

	}
}
