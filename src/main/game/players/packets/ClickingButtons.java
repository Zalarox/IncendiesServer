package main.game.players.packets;

import main.Constants;
import main.GameEngine;
import main.event.Task;
import main.game.items.BankSearch;
import main.game.items.GameItem;
import main.game.players.PacketType;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.game.players.actions.combat.CombatPrayer;
import main.game.players.content.ItemsKeptOnDeath;
import main.game.players.content.QuickCurses;
import main.game.players.content.QuickPrayers;
import main.game.players.content.TeleportHandler;
import main.game.players.content.minigames.DuelArena;
import main.game.players.content.minigames.impl.barrows.Barrows;
import main.game.players.content.skills.cooking.Cooking;
import main.game.players.content.skills.cooking.DairyChurn;
import main.game.players.content.skills.cooking.FlourRelated;
import main.game.players.content.skills.crafting.DramenBranch;
import main.game.players.content.skills.crafting.GlassMaking;
import main.game.players.content.skills.crafting.LeatherMakingHandler;
import main.game.players.content.skills.crafting.PotteryMaking;
import main.game.players.content.skills.crafting.SilverCrafting;
import main.game.players.content.skills.crafting.Spinning;
import main.game.players.content.skills.crafting.Tanning;
import main.game.players.content.skills.crafting.Weaving;
import main.game.players.content.skills.dungeoneering.Dungeon;
import main.game.players.content.skills.fletching.BowHandler;
import main.game.players.content.skills.herblore.Herblore;
import main.game.players.content.skills.prayer.Prayer;
import main.game.players.content.skills.smithing.Smelting;
import main.game.players.content.skills.summoning.Creation;
import main.util.Misc;

/**
 * Clicking most buttons
 **/
public class ClickingButtons implements PacketType {

	@SuppressWarnings("static-access")
	@Override
	public void processPacket(final Player p, int packetType, int packetSize) {
		int buttonId = Misc.hexToInt(p.getInStream().buffer, 0, packetSize);
		if (p.isDead)
			return;
		if (p.getVariables().teleTimer > 0)
			return;
		p.getPA().switchCombatType(buttonId);
		p.getSkillGuide().buttons(buttonId);
		p.curses().curseButtons(buttonId);
		p.getSummoning().handleButtonClick(buttonId);
		Creation.handleButtons(p, buttonId);
		Cooking.handleButtons(p, buttonId);
		FlourRelated.handleButton(p, buttonId);
		DairyChurn.churnItem(p, buttonId);
		PotteryMaking.makePottery(p, buttonId, 0);
		SilverCrafting.makeSilver(p, buttonId, 0);
		Spinning.spin(p, buttonId, 0);
		GlassMaking.makeSilver(p, buttonId, 0);
		LeatherMakingHandler.handleButtons(p, buttonId, 0);
		Tanning.handleButtons(p, buttonId);
		Weaving.weave(p, buttonId, 0);
		DramenBranch.cutDramen(p, buttonId, 0);
		Prayer.handleButtons(p, buttonId);
		Smelting.getBar(p, buttonId);
		Dungeon.handleButtons(buttonId, p);
		if (buttonId > 34100 && buttonId < 34220) {
			BowHandler.handleFletchingButtons(p, buttonId);
		}
		if (p.getVariables().resting) {
			p.getPA().resetRest();
		}
		if (p.getVariables().playerRights == 3) {
			p.sendMessage("@blu@actionbutton: " + buttonId + " Fight mode: " + p.getVariables().fightMode
					+ " Dialogue action: " + p.getVariables().dialogueAction);
			System.out.println("ActionButton: " + buttonId);
		}
		int[] spellIds = { 4128, 4130, 4132, 4134, 4136, 4139, 4142, 4145, 4148, 4151, 4153, 4157, 4159, 4161, 4164,
				4165, 4129, 4133, 4137, 6006, 6007, 6026, 6036, 6046, 6056, 4147, 6003, 47005, 4166, 4167, 4168, 48157,
				50193, 50187, 50101, 50061, 50163, 50211, 50119, 50081, 50151, 50199, 50111, 50071, 50175, 50223, 50129,
				50091 };
		for (int i = 0; i < spellIds.length; i++) {
			if (buttonId == spellIds[i]) {
				p.getVariables().autocasting = (p.getVariables().autocastId != i) ? true : false;
				if (!p.getVariables().autocasting) {
					p.getPA().resetAutocast();
				} else {
					p.getVariables().autocastId = i;
				}
			}
		}

		if (buttonId >= 67050 && buttonId <= 67075) {
			if (p.getVariables().playerPrayerBook == false)
				QuickPrayers.clickPray(p, buttonId);
			else
				QuickCurses.clickCurse(p, buttonId);
		}

		switch (buttonId) {
		case 3182:
			p.getPA().showInterface(40030);
			break;

		case 152:
		case 74214:
			if (p.runEnergy < 1) {
				p.isRunning = false;
				p.getPA().setConfig(173, 0);
				return;
			}
			p.isRunning = !p.isRunning;
			p.isRunning2 = p.isRunning;
			break;

		case 7220:
			p.getPA().rest();
			break;
		case 96026:
		case 96126:
		case 209198:
		case 213174:
			p.getPA().closeAllWindows();
			break;

		/**
		 * Herblore
		 **/

		case 10239:
			if (p.getVariables().secondHerb) {
				Herblore.finishPotion(p, 1);
				return;
			} else if (!p.getVariables().secondHerb) {
				Herblore.finishUnfinished(p, 1);
			}
			break;

		case 10238:
			if (p.getVariables().secondHerb) {
				Herblore.finishPotion(p, 5);
				return;
			} else if (!p.getVariables().secondHerb) {
				Herblore.finishUnfinished(p, 5);
			}
			break;
		case 6212:
			if (p.getVariables().secondHerb) {
				Herblore.finishPotion(p, p.getItems().getItemAmount(p.getVariables().newItem));
			} else {
				Herblore.finishUnfinished(p, p.getItems().getItemAmount(p.getVariables().doingHerb));
			}
			break;
		case 6211:
			if (p.getVariables().secondHerb) {
				Herblore.finishPotion(p, p.getItems().getItemAmount(p.getVariables().newItem));
			} else {
				Herblore.finishUnfinished(p, p.getItems().getItemAmount(p.getVariables().doingHerb));
			}
			break;

		case 23112: // Toggle quick prayers
			if (p.getVariables().quickPray || p.getVariables().quickCurse) {
				QuickCurses.turnOffQuicks(p);
				return;
			}
			QuickCurses.turnOnQuicks(p);
			break;

		case 95185:
		case 95191:
		case 95203:
		case 95206:
		case 95194:
		case 95209:
		case 95188:
		case 95212:
		case 95197:
		case 95215:
		case 95200:
		case 95218:
		case 96078:
		case 96174:
		case 96074:
		case 96082:
		case 96182:
		case 96030:
		case 96130:
		case 96034:
		case 96134:
		case 96038:
		case 96138:
		case 96042:
		case 96142:
		case 96046:
		case 96146:
		case 96050:
		case 96150:
		case 96058:
		case 96158:
		case 96070:
		case 96170:
		case 96062:
		case 96162:
		case 96086:
		case 96186:
		case 96089:
		case 96189:
		case 95223:
		case 95227:
		case 95231:
		case 95235:
		case 95239:
		case 95243:
		case 213230:
		case 209254:
		case 95221:
		case 95225:
		case 95229:
		case 95233:
		case 95237:
		case 95241:
		case 214016:
		case 210040:
			// p.GE().buttonClick(buttonId);
			break;
		case 93196:
			p.getPA().closeAllWindows();
			break;

		case 82012:
			p.getVariables().isSearching = p.getVariables().isSearching == false ? true : false;
			if (p.getVariables().isSearching == true) {
				p.sendMessage("You are now searching for items.");
			} else {
				p.sendMessage("You stop searching for items.");
				BankSearch.clearItems(p);
				p.getItems().resetBank();
			}
			break;
		case 19137: // Select quick prayers
			QuickCurses.selectQuickInterface(p);
			break;

		case 67089: // quick curse confirm
			QuickCurses.clickConfirm(p);
			break;

		case 23113: // select your quick prayers/curses
			QuickCurses.selectQuickInterface(p);
			p.getPA().sendFrame106(5);
			break;
		case 12132:
			p.getPA().openUpBank();
			break;

		case 12133:
			// p.getVariables().playerLevel[3] =
			// p.getLevelForXP(p.getVariables().playerXP[3]);// might need to
			// change the
			// c.calculateMaxLifePoints();
			// to
			// c.getLevelForXP(c.getVariables().playerXP[3]);
			p.getPA().refreshSkill(3);
			break;

		case 150:
			p.getVariables().autoRet = (p.getVariables().autoRet == 0) ? 1 : 0;
			break;

		case 33206: // attack
			p.getSkillGuide().attackComplex(1);
			p.getSkillGuide().selected = 0;
			break;
		case 33209: // strength
			p.getSkillGuide().strengthComplex(1);
			p.getSkillGuide().selected = 1;
			break;
		case 33212: // Defence
			p.getSkillGuide().defenceComplex(1);
			p.getSkillGuide().selected = 2;
			break;
		case 33215: // range
			p.getSkillGuide().rangedComplex(1);
			p.getSkillGuide().selected = 3;
			break;
		case 33218: // prayer
			p.getSkillGuide().prayerComplex(1);
			p.getSkillGuide().selected = 4;
			break;
		case 33221: // mage
			p.getSkillGuide().magicComplex(1);
			p.getSkillGuide().selected = 5;
			break;
		case 33224: // runecrafting
			p.getSkillGuide().runecraftingComplex(1);
			p.getSkillGuide().selected = 6;
			break;
		case 33207: // hp
			p.getSkillGuide().hitpointsComplex(1);
			p.getSkillGuide().selected = 7;
			break;
		case 33210: // agility
			p.getSkillGuide().agilityComplex(1);
			p.getSkillGuide().selected = 8;
			break;
		case 33213: // herblore
			p.getSkillGuide().herbloreComplex(1);
			p.getSkillGuide().selected = 9;
			break;
		case 33216: // theiving
			p.getSkillGuide().thievingComplex(1);
			p.getSkillGuide().selected = 10;
			break;
		case 33219: // crafting
			p.getSkillGuide().craftingComplex(1);
			p.getSkillGuide().selected = 11;
			break;
		case 33222: // fletching
			p.getSkillGuide().fletchingComplex(1);
			p.getSkillGuide().selected = 12;
			break;
		case 47130:// slayer
			p.getSkillGuide().slayerComplex(1);
			p.getSkillGuide().selected = 13;
			break;
		case 33208:// mining
			p.getSkillGuide().miningComplex(1);
			p.getSkillGuide().selected = 14;
			break;
		case 33211: // smithing
			p.getSkillGuide().smithingComplex(1);
			p.getSkillGuide().selected = 15;
			break;
		case 33214: // fishing
			p.getSkillGuide().fishingComplex(1);
			p.getSkillGuide().selected = 16;
			break;
		case 33217: // cooking
			p.getSkillGuide().cookingComplex(1);
			p.getSkillGuide().selected = 17;
			break;
		case 33220: // firemaking
			p.getSkillGuide().firemakingComplex(1);
			p.getSkillGuide().selected = 18;
			break;
		case 33223: // woodcut
			p.getSkillGuide().woodcuttingComplex(1);
			p.getSkillGuide().selected = 19;
			break;
		case 54104: // farming
			p.getSkillGuide().farmingComplex(1);
			p.getSkillGuide().selected = 20;
			break;

		case 34142: // tab 1
			p.getSkillGuide().menuCompilation(1);
			break;

		case 34119: // tab 2
			p.getSkillGuide().menuCompilation(2);
			break;

		case 34120: // tab 3
			p.getSkillGuide().menuCompilation(3);
			break;

		case 34123: // tab 4
			p.getSkillGuide().menuCompilation(4);
			break;

		case 34133: // tab 5
			p.getSkillGuide().menuCompilation(5);
			break;

		case 34136: // tab 6
			p.getSkillGuide().menuCompilation(6);
			break;

		case 34139: // tab 7
			p.getSkillGuide().menuCompilation(7);
			break;

		case 34155: // tab 8
			p.getSkillGuide().menuCompilation(8);
			break;

		case 34158: // tab 9
			p.getSkillGuide().menuCompilation(9);
			break;

		case 34161: // tab 10
			p.getSkillGuide().menuCompilation(10);
			break;

		case 59199: // tab 11
			p.getSkillGuide().menuCompilation(11);
			break;

		case 59202: // tab 12
			p.getSkillGuide().menuCompilation(12);
			break;

		case 59205: // tab 13
			p.getSkillGuide().menuCompilation(13);
			break;

		case 59097: // Equipment screen
			p.getPA().showInterface(15106);
			p.getItems().writeBonus();
			break;

		case 82024: // Deposit equipment
			p.getVariables().constitution -= p.getVariables().calculateRemoved(p.getVariables().playerHat, p);
			p.getVariables().constitution -= p.getVariables().calculateRemoved(p.getVariables().playerLegs, p);
			p.getVariables().constitution -= p.getVariables().calculateRemoved(p.getVariables().playerChest, p);
			for (int i = 0; i < p.getVariables().playerEquipment.length; i++) {
				int itemId = p.getVariables().playerEquipment[i];
				int itemAmount = p.getVariables().playerEquipmentN[i];
				p.getItems().removeItem(itemId, i);
				p.getItems().bankItem(itemId, p.getItems().getItemSlot(itemId), itemAmount);
			}
			p.getVariables().maxConstitution = p.getVariables().calculateMaxLifePoints(p);
			break;

		case 82020: // Deposit Inventory
			for (int i = 0; i < p.getVariables().playerItems.length; i++) {
				p.getItems().bankItem(p.getVariables().playerItems[i], i, p.getVariables().playerItemsN[i]);
			}
			break;

		case 59100: // items kept on death
			boolean protectOn = p.getVariables().prayerActive[10] || p.getVariables().curseActive[0];
			p.getPA().sendString("Items kept on death", 17103);
			ItemsKeptOnDeath.StartBestItemScan(p);
			p.getVariables().EquipStatus = 0;
			for (int k = 0; k < 4; k++)
				p.getPA().sendFrame34a(10494, -1, k, 1);
			for (int k = 0; k < 39; k++)
				p.getPA().sendFrame34a(10600, -1, k, 1);
			if (p.getVariables().WillKeepItem1 > 0)
				p.getPA().sendFrame34a(10494, p.getVariables().WillKeepItem1, 0, p.getVariables().WillKeepAmt1);
			if (p.getVariables().WillKeepItem2 > 0)
				p.getPA().sendFrame34a(10494, p.getVariables().WillKeepItem2, 1, p.getVariables().WillKeepAmt2);
			if (p.getVariables().WillKeepItem3 > 0)
				p.getPA().sendFrame34a(10494, p.getVariables().WillKeepItem3, 2, p.getVariables().WillKeepAmt3);
			if (p.getVariables().WillKeepItem4 > 0 && protectOn)
				p.getPA().sendFrame34a(10494, p.getVariables().WillKeepItem4, 3, 1);
			for (int ITEM = 0; ITEM < 28; ITEM++) {
				if (p.getVariables().playerItems[ITEM] - 1 > 0
						&& !(p.getVariables().playerItems[ITEM] - 1 == p.getVariables().WillKeepItem1
								&& ITEM == p.getVariables().WillKeepItem1Slot)
						&& !(p.getVariables().playerItems[ITEM] - 1 == p.getVariables().WillKeepItem2
								&& ITEM == p.getVariables().WillKeepItem2Slot)
						&& !(p.getVariables().playerItems[ITEM] - 1 == p.getVariables().WillKeepItem3
								&& ITEM == p.getVariables().WillKeepItem3Slot)
						&& !(p.getVariables().playerItems[ITEM] - 1 == p.getVariables().WillKeepItem4
								&& ITEM == p.getVariables().WillKeepItem4Slot)) {
					p.getPA().sendFrame34a(10600, p.getVariables().playerItems[ITEM] - 1, p.getVariables().EquipStatus,
							p.getVariables().playerItemsN[ITEM]);
					p.getVariables().EquipStatus += 1;
				} else if (p.getVariables().playerItems[ITEM] - 1 > 0
						&& (p.getVariables().playerItems[ITEM] - 1 == p.getVariables().WillKeepItem1
								&& ITEM == p.getVariables().WillKeepItem1Slot)
						&& p.getVariables().playerItemsN[ITEM] > p.getVariables().WillKeepAmt1) {
					p.getPA().sendFrame34a(10600, p.getVariables().playerItems[ITEM] - 1, p.getVariables().EquipStatus,
							p.getVariables().playerItemsN[ITEM] - p.getVariables().WillKeepAmt1);
					p.getVariables().EquipStatus += 1;
				} else if (p.getVariables().playerItems[ITEM] - 1 > 0
						&& (p.getVariables().playerItems[ITEM] - 1 == p.getVariables().WillKeepItem2
								&& ITEM == p.getVariables().WillKeepItem2Slot)
						&& p.getVariables().playerItemsN[ITEM] > p.getVariables().WillKeepAmt2) {
					p.getPA().sendFrame34a(10600, p.getVariables().playerItems[ITEM] - 1, p.getVariables().EquipStatus,
							p.getVariables().playerItemsN[ITEM] - p.getVariables().WillKeepAmt2);
					p.getVariables().EquipStatus += 1;
				} else if (p.getVariables().playerItems[ITEM] - 1 > 0
						&& (p.getVariables().playerItems[ITEM] - 1 == p.getVariables().WillKeepItem3
								&& ITEM == p.getVariables().WillKeepItem3Slot)
						&& p.getVariables().playerItemsN[ITEM] > p.getVariables().WillKeepAmt3) {
					p.getPA().sendFrame34a(10600, p.getVariables().playerItems[ITEM] - 1, p.getVariables().EquipStatus,
							p.getVariables().playerItemsN[ITEM] - p.getVariables().WillKeepAmt3);
					p.getVariables().EquipStatus += 1;
				} else if (p.getVariables().playerItems[ITEM] - 1 > 0
						&& (p.getVariables().playerItems[ITEM] - 1 == p.getVariables().WillKeepItem4
								&& ITEM == p.getVariables().WillKeepItem4Slot)
						&& p.getVariables().playerItemsN[ITEM] > 1) {
					p.getPA().sendFrame34a(10600, p.getVariables().playerItems[ITEM] - 1, p.getVariables().EquipStatus,
							p.getVariables().playerItemsN[ITEM] - 1);
					p.getVariables().EquipStatus += 1;
				}
			}
			for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
				if (p.getVariables().playerEquipment[EQUIP] > 0
						&& !(p.getVariables().playerEquipment[EQUIP] == p.getVariables().WillKeepItem1
								&& EQUIP + 28 == p.getVariables().WillKeepItem1Slot)
						&& !(p.getVariables().playerEquipment[EQUIP] == p.getVariables().WillKeepItem2
								&& EQUIP + 28 == p.getVariables().WillKeepItem2Slot)
						&& !(p.getVariables().playerEquipment[EQUIP] == p.getVariables().WillKeepItem3
								&& EQUIP + 28 == p.getVariables().WillKeepItem3Slot)
						&& !(p.getVariables().playerEquipment[EQUIP] == p.getVariables().WillKeepItem4
								&& EQUIP + 28 == p.getVariables().WillKeepItem4Slot)) {
					p.getPA().sendFrame34a(10600, p.getVariables().playerEquipment[EQUIP], p.getVariables().EquipStatus,
							p.getVariables().playerEquipmentN[EQUIP]);
					p.getVariables().EquipStatus += 1;
				} else if (p.getVariables().playerEquipment[EQUIP] > 0
						&& (p.getVariables().playerEquipment[EQUIP] == p.getVariables().WillKeepItem1
								&& EQUIP + 28 == p.getVariables().WillKeepItem1Slot)
						&& p.getVariables().playerEquipmentN[EQUIP] > 1
						&& p.getVariables().playerEquipmentN[EQUIP] - p.getVariables().WillKeepAmt1 > 0) {
					p.getPA().sendFrame34a(10600, p.getVariables().playerEquipment[EQUIP], p.getVariables().EquipStatus,
							p.getVariables().playerEquipmentN[EQUIP] - p.getVariables().WillKeepAmt1);
					p.getVariables().EquipStatus += 1;
				} else if (p.getVariables().playerEquipment[EQUIP] > 0
						&& (p.getVariables().playerEquipment[EQUIP] == p.getVariables().WillKeepItem2
								&& EQUIP + 28 == p.getVariables().WillKeepItem2Slot)
						&& p.getVariables().playerEquipmentN[EQUIP] > 1
						&& p.getVariables().playerEquipmentN[EQUIP] - p.getVariables().WillKeepAmt2 > 0) {
					p.getPA().sendFrame34a(10600, p.getVariables().playerEquipment[EQUIP], p.getVariables().EquipStatus,
							p.getVariables().playerEquipmentN[EQUIP] - p.getVariables().WillKeepAmt2);
					p.getVariables().EquipStatus += 1;
				} else if (p.getVariables().playerEquipment[EQUIP] > 0
						&& (p.getVariables().playerEquipment[EQUIP] == p.getVariables().WillKeepItem3
								&& EQUIP + 28 == p.getVariables().WillKeepItem3Slot)
						&& p.getVariables().playerEquipmentN[EQUIP] > 1
						&& p.getVariables().playerEquipmentN[EQUIP] - p.getVariables().WillKeepAmt3 > 0) {
					p.getPA().sendFrame34a(10600, p.getVariables().playerEquipment[EQUIP], p.getVariables().EquipStatus,
							p.getVariables().playerEquipmentN[EQUIP] - p.getVariables().WillKeepAmt3);
					p.getVariables().EquipStatus += 1;
				} else if (p.getVariables().playerEquipment[EQUIP] > 0
						&& (p.getVariables().playerEquipment[EQUIP] == p.getVariables().WillKeepItem4
								&& EQUIP + 28 == p.getVariables().WillKeepItem4Slot)
						&& p.getVariables().playerEquipmentN[EQUIP] > 1
						&& p.getVariables().playerEquipmentN[EQUIP] - 1 > 0) {
					p.getPA().sendFrame34a(10600, p.getVariables().playerEquipment[EQUIP], p.getVariables().EquipStatus,
							p.getVariables().playerEquipmentN[EQUIP] - 1);
					p.getVariables().EquipStatus += 1;
				}
			}
			ItemsKeptOnDeath.ResetKeepItems(p);
			p.getPA().showInterface(17100);
			break;

		case 9167:// first
			if (p.dialogueAction == 15) {
				if (p.party == null)
					return;
				if (p.party.floor == null)
					return;
				p.getShops().openShop(77);
				p.getVariables().dialogueAction = 0;
				break;
			}
		case 9168:// 2nd
			if (p.dialogueAction == 15) {
				if (p.party == null)
					return;
				if (p.party.floor == null)
					return;
				p.getShops().openShop(78);
				p.getVariables().dialogueAction = 0;
				break;
			}
			break;
		case 9169:// 3rd
			if (p.dialogueAction == 15) {
				if (p.party == null)
					return;
				if (p.party.floor == null)
					return;
				p.getShops().openShop(79);
				p.getVariables().dialogueAction = 0;
				break;
			}
			break;

		case 9178:

			if (p.getVariables().teleAction == 10) {
				TeleportHandler.teleport(p, 2848, 3498, 0, "auto");
				break;
			} else if (p.getVariables().teleAction == 11) {
				// More skilling teleport - Gnome agility course
				TeleportHandler.teleport(p, 2474, 3438, 0, "auto");
				break;
			}
			if (p.getVariables().dialogueAction == 50) {
				Barrows.resetBarrows(p);
				p.getPA().removeAllWindows();
				p.sendMessage("Your Barrows kill count have been reset.");
			}
			if (p.getVariables().usingGlory)
				TeleportHandler.teleport(p, Constants.EDGEVILLE_X, Constants.EDGEVILLE_Y, 0, "glory");
			else if (p.getPA().dialogueAction(0))
				p.getShops().openShop(3);
			else if (p.getPA().dialogueAction(1))
				TeleportHandler.npcTeleport(p, 3370, 3699, 0, 2);
			else if (p.getPA().dialogueAction(4))
				// c.getPA().startTeleport(3565,3308, 0, "modern");
				p.getDH().sendDialogues(27, 3792);
			else if (p.getPA().dialogueAction(2))
				p.getShops().openShop(6);
			else if (p.getPA().dialogueAction(12))
				TeleportHandler.teleport(p, 1, 1, 0, "modern");
			else if (p.getPA().dialogueAction(5))
				TeleportHandler.teleport(p, 2662, 2650, 0, "modern");
			else if (p.getPA().dialogueAction(7))
				TeleportHandler.teleport(p, 3565, 3308, 0, "modern");
			else if (p.getPA().dialogueAction(6)) {
				p.setSidebarInterface(6, 1151);
				p.getItems().sendWeapon(p.getVariables().playerEquipment[p.getVariables().playerWeapon],
						p.getItems().getItemName(p.getVariables().playerEquipment[p.getVariables().playerWeapon]));
				p.getVariables().autocastId = -1;
				p.getPA().resetAutocast();
				p.startAnimation(645);
				p.sendMessage("You switch to the Normal magics book.");
				p.getVariables().playerMagicBook = 0;
				p.getPA().removeAllWindows();
			}
			break;

		case 9179:
			if (p.getVariables().teleAction == 10) {
				// desert
				// p.getPA().spellTeleport(2848, 3848, 0);
				break;
			}
			if (p.getVariables().teleAction == 11) {
				TeleportHandler.teleport(p, 2850, 3336, 0, "auto");
				p.sendMessage("All hunting activites are spread across the island.");
				// p.getDH().sendOption5("Crimson Swift", "Swamp Lizards",
				// "Kebbits", "Gray Chinchompas", "Red Chinchompas");
				// p.getVariables().dialogueAction = 231;
				break;
			}
			if (p.getVariables().dialogueAction == 50) {
				p.getPA().fixAllBarrows();
				p.getPA().removeAllWindows();
				p.sendMessage("Any Barrows items in your inventory have been fixed.");
			}
			/*
			 * if(p.getVariables().dialogueAction == 3683) {
			 * p.getDung().acceptFloorSize(); }
			 */
			if (p.getVariables().usingGlory)
				TeleportHandler.teleport(p, Constants.AL_KHARID_X, Constants.AL_KHARID_Y, 0, "glory");
			else if (p.getPA().dialogueAction(0))
				p.getDH().sendDialogues(24, 8725);
			else if (p.getPA().dialogueAction(1))
				TeleportHandler.npcTeleport(p, 3000, 3620, 0, 2);
			else if (p.getPA().dialogueAction(2))
				p.getShops().openShop(5);
			else if (p.getPA().dialogueAction(4))
				p.getDH().sendDialogues(29, 3792);
			else if (p.getPA().dialogueAction(12))
				TeleportHandler.teleport(p, 2309, 5239, 0, "modern");
			else if (p.getPA().dialogueAction(7))
				TeleportHandler.teleport(p, 2438, 5171, 0, "modern");
			else if (p.getPA().dialogueAction(5))
				TeleportHandler.teleport(p, 3366, 3266, 0, "modern");
			else if (p.getPA().dialogueAction(6)) {
				p.setSidebarInterface(6, 12855);
				p.getItems().sendWeapon(p.getVariables().playerEquipment[p.getVariables().playerWeapon],
						p.getItems().getItemName(p.getVariables().playerEquipment[p.getVariables().playerWeapon]));
				p.getVariables().autocastId = -1;
				p.getPA().resetAutocast();
				p.startAnimation(645);
				p.sendMessage("You switch to the Ancient magics book.");
				p.getVariables().playerMagicBook = 1;
				p.getPA().removeAllWindows();
			}
			break;

		case 9180:
			if (p.getVariables().teleAction == 10) {
				// jungle
				// p.getPA().spellTeleport(2848, 3848, 0);
				break;
			} else if (p.getVariables().teleAction == 11) {
				p.getDH().sendOption5("Seers' Village", "Farming Patches", "Al Kharid Mine", "Back", "More");
				p.getVariables().teleAction = 9;
				break;
			}
			if (p.getVariables().dialogueAction == 50) {
				p.getDH().sendDialogues(9, 2024);
			}
			/*
			 * if(p.getVariables().dialogueAction == 3683) {
			 * p.getDung().acceptFloorSize2(); }
			 */
			if (p.getVariables().usingGlory)
				TeleportHandler.teleport(p, Constants.KARAMJA_X, Constants.KARAMJA_Y, 0, "glory");
			else if (p.getPA().dialogueAction(0))
				p.getDH().sendDialogues(25, 8725);
			else if (p.getPA().dialogueAction(1))
				TeleportHandler.npcTeleport(p, 2980, 3857, 0, 2);
			else if (p.getPA().dialogueAction(2))
				p.getShops().openShop(8);
			else if (p.getPA().dialogueAction(4))
				p.getDH().sendDialogues(30, 3792);
			// c.getPA().startTeleport(2441, 5171, 0, "modern");
			else if (p.getPA().dialogueAction(12))
				TeleportHandler.teleport(p, 1, 1, 0, "modern");// Chaos Tunnels
			else if (p.getPA().dialogueAction(5))
				TeleportHandler.teleport(p, 1, 1, 0, "modern");
			else if (p.getPA().dialogueAction(7))
				p.getPA().movePlayer(2533, 3569, 0);
			else if (p.getPA().dialogueAction(6)) {
				p.setSidebarInterface(6, 29999);
				p.getItems().sendWeapon(p.getVariables().playerEquipment[p.getVariables().playerWeapon],
						p.getItems().getItemName(p.getVariables().playerEquipment[p.getVariables().playerWeapon]));
				p.getVariables().autocastId = -1;
				p.getPA().resetAutocast();
				p.startAnimation(645);
				p.sendMessage("You switch to the Lunar magics book.");
				p.getVariables().playerMagicBook = 2;
				p.getPA().removeAllWindows();
			}
			break;

		case 9181:
			if (p.getVariables().teleAction == 10) {
				p.getDH().sendOption5("King Black Dragon @red@(42 Wild)", "Bork @red@(47 Wild)", "Frost Dragons",
						"Strykewyrms", "(Coming Soon)");
				p.getVariables().teleAction = 7; // More boss teleports
				break;
			}
			if (p.getVariables().dialogueAction == 50) {
				p.getPA().removeAllWindows();
			}
			if (p.getVariables().dialogueAction == 90) {
				p.getShops().openShop(37);
			}
			if (p.getVariables().teleAction == 1) {
				p.getPA().removeAllWindows();
			}
			if (p.getVariables().usingGlory)
				TeleportHandler.teleport(p, Constants.MAGEBANK_X, Constants.MAGEBANK_Y, 0, "glory");
			else if (p.getPA().dialogueAction(0))
				p.getPA().removeAllWindows();
			else if (p.getPA().dialogueAction(1))
				TeleportHandler.npcTeleport(p, 3149, 3660, 0, 2);
			else if (p.getPA().dialogueAction(2))
				p.getShops().openShop(7);
			else if (p.getPA().dialogueAction(4))
				p.getDH().sendDialogues(31, 3792);
			// c.getDH().sendDialogues(23, 3792);
			else if (p.getPA().dialogueAction(12))
				p.getDH().sendDialogues(29, 0);
			else if (p.getPA().dialogueAction(5))
				TeleportHandler.teleport(p, 1, 1, 0, "modern");
			else if (p.getPA().dialogueAction(7))
				p.getDH().sendDialogues(28, 0);
			else if (p.getPA().dialogueAction(6))
				p.getPA().removeAllWindows();
			break;

		case 1093:
		case 1094:
		case 1097:
			if (p.getVariables().autocastId > 0) {
				p.getPA().resetAutocast();
			} else {
				if (p.getVariables().playerMagicBook == 1) {
					if (p.getVariables().playerEquipment[p.getVariables().playerWeapon] == 4675
							|| p.getVariables().playerEquipment[p.getVariables().playerWeapon] == 15486)
						p.setSidebarInterface(0, 1689);
					else
						p.sendMessage("You can't autocast ancients without an ancient staff.");
				} else if (p.getVariables().playerMagicBook == 0) {
					if (p.getVariables().playerEquipment[p.getVariables().playerWeapon] == 4170) {
						p.setSidebarInterface(0, 12050);
					} else {
						p.setSidebarInterface(0, 1829);
					}
				}

			}
			break;

		case 9157:
			if (p.getVariables().dialogueAction == 1) {
				p.getPA().removeAllWindows();
				p.getPA().movePlayer(3551, 9693, 0);
			}
			if (p.getVariables().dialogueAction == 5) {
				if (p.getSlayer().getTask() != null) {
					p.getDH().sendDialogues(113, 9085);
					return;
				} else
					p.getSlayer().assignTask(p, p.getSlayer().getTaskDifficulty(p));
				p.getPA().removeAllWindows();
			}
			if (p.getVariables().dialogueAction == 6) {
				p.getSlayer().resetTask();
				p.getSlayer().assignTask(p, 1);
				p.getPA().removeAllWindows();
			}
			if (p.getVariables().dialogueAction == 982) {
				Barrows.handleTunnel(p);
				p.getPA().removeAllWindows();
			}
			if (p.getVariables().dialogueAction == 121) {
				p.sendMessage(p.getSlayer().getTask() != null
						? "I currently have " + p.getSlayer().getTask().leftToKill() + " "
								+ p.getSlayer().getTask().toFormattedString() + "s left to kill."
						: "You currently have none.");
			}
			if (p.getVariables().dialogueAction == 100) {
				p.getShops().openShop(69);
				p.sendMessage("@red@You currently have " + p.getVariables().DonatorPoints + " Donator points.");
			}
			break;

		case 9158:
			if (p.getVariables().dialogueAction == 982) {
				p.getVariables().dialogueAction = 0;
				p.getPA().removeAllWindows();
			} else if (p.getVariables().dialogueAction == 100) {
				p.getShops().openShop(70);
				p.sendMessage("@red@You currently have " + p.getVariables().DonatorPoints + " Donator points.");
			}
			if (p.getVariables().dialogueAction == 1320) {
				Dungeon.declineInvite(p);
			}
			if (p.getVariables().dialogueAction == 121) {
				p.getPA().removeAllWindows();
			}
			if (p.getVariables().dialogueAction == 5) {
				p.getPA().removeAllWindows();
			}
			if (p.getVariables().dialogueAction == 6) {
				p.getPA().removeAllWindows();
			}
			break;

		/** Specials **/
		case 29188:
			p.getVariables().specBarId = 7636; // the special attack text -
												// sendframe126(S P E
			// C I A L A T T A C K, c.specBarId);
			p.getVariables().usingSpecial = !p.getVariables().usingSpecial;
			p.getItems().updateSpecialBar();
			break;

		case 29163:
			p.getVariables().specBarId = 7611;
			p.getVariables().usingSpecial = !p.getVariables().usingSpecial;
			p.getItems().updateSpecialBar();
			break;

		case 33033:
			p.getVariables().specBarId = 8505;
			p.getVariables().usingSpecial = !p.getVariables().usingSpecial;
			p.getItems().updateSpecialBar();
			break;

		case 29038:
			p.getVariables().specBarId = 7486;
			if (p.getVariables().playerEquipment[p.getVariables().playerWeapon] == 4153)
				p.getCombat().handleGmaulPlayer();
			else
				p.getVariables().usingSpecial = !p.getVariables().usingSpecial;
			p.getItems().updateSpecialBar();
			break;

		case 11192:
			if (p.getVariables().pkPoints > 9 && p.getItems().freeSlots() > 0) {
				p.getItems().addItem(3188, 1);
				p.getVariables().pkPoints -= 10;
			} else if (p.getVariables().pkPoints < 10) {
				p.sendMessage("Not enough PK Points");
			} else if (p.getItems().freeSlots() < 1) {
				p.sendMessage("Not enough space in your inventory.");
			}
			p.getPA().removeAllWindows();
			break;

		case 8041:
			p.getPA().removeAllWindows();
			break;

		case 29063:
			if (p.getCombat().checkSpecAmount(p.getVariables().playerEquipment[p.getVariables().playerWeapon])) {
				p.gfx0(246);
				p.forcedChat("Raarrrrrgggggghhhhhhh!");
				p.startAnimation(1056);
				p.getVariables().playerLevel[2] = p.getLevelForXP(p.getVariables().playerXP[2])
						+ (p.getLevelForXP(p.getVariables().playerXP[2]) * 15 / 100);
				p.getPA().refreshSkill(2);
				p.getItems().updateSpecialBar();
			} else {
				p.sendMessage("You don't have the required special energy to use this attack.");
			}
			break;

		case 48023:
			p.getVariables().specBarId = 12335;
			p.getVariables().usingSpecial = !p.getVariables().usingSpecial;
			p.getItems().updateSpecialBar();
			break;

		case 29138:
			p.getVariables().specBarId = 7586;
			p.getVariables().usingSpecial = !p.getVariables().usingSpecial;
			p.getItems().updateSpecialBar();
			break;

		case 29113:
			p.getVariables().specBarId = 7561;
			p.getVariables().usingSpecial = !p.getVariables().usingSpecial;
			p.getItems().updateSpecialBar();
			break;

		case 29238:
			p.getVariables().specBarId = 7686;
			p.getVariables().usingSpecial = !p.getVariables().usingSpecial;
			p.getItems().updateSpecialBar();
			break;

		case 30108: // Claws
			p.getVariables().specBarId = 7812;
			p.getVariables().usingSpecial = !p.getVariables().usingSpecial;
			p.getItems().updateSpecialBar();
			break;

		/** Dueling **/
		case 26065: // no forfeit
		case 26040:
			if (!DuelArena.isInSecondInterface(p) && DuelArena.isInFirstInterface(p)) {
				p.getVariables().duelSlot = -1;
				p.Dueling.toggleDuelRule(0, p);
			}
			break;

		case 26066: // no movement
		case 26048:
			if (!DuelArena.isInSecondInterface(p) && DuelArena.isInFirstInterface(p)) {
				p.getVariables().duelSlot = -1;
				p.Dueling.toggleDuelRule(1, p);
			}
			break;

		case 26069: // no range
		case 26042:
			if (!DuelArena.isInSecondInterface(p) && DuelArena.isInFirstInterface(p)) {
				p.getVariables().duelSlot = -1;
				p.Dueling.toggleDuelRule(2, p);
			}
			break;

		case 26070: // no melee
		case 26043:
			if (!DuelArena.isInSecondInterface(p) && DuelArena.isInFirstInterface(p)) {
				p.getVariables().duelSlot = -1;
				p.Dueling.toggleDuelRule(3, p);
			}
			break;

		case 26071: // no mage
		case 26041:
			if (!DuelArena.isInSecondInterface(p) && DuelArena.isInFirstInterface(p)) {
				p.getVariables().duelSlot = -1;
				p.Dueling.toggleDuelRule(4, p);
			}
			break;

		case 26072: // no drinks
		case 26045:
			if (!DuelArena.isInSecondInterface(p) && DuelArena.isInFirstInterface(p)) {
				p.getVariables().duelSlot = -1;
				p.Dueling.toggleDuelRule(5, p);
			}
			break;

		case 26073: // no food
		case 26046:
			if (!DuelArena.isInSecondInterface(p) && DuelArena.isInFirstInterface(p)) {
				p.getVariables().duelSlot = -1;
				p.Dueling.toggleDuelRule(6, p);
			}
			break;

		case 26074: // no prayer
		case 26047:
			if (!DuelArena.isInSecondInterface(p) && DuelArena.isInFirstInterface(p)) {
				p.getVariables().duelSlot = -1;
				p.Dueling.toggleDuelRule(7, p);
			}
			break;

		case 26076: // obsticals
		case 26075:
			if (!DuelArena.isInSecondInterface(p) && DuelArena.isInFirstInterface(p)) {
				p.getVariables().duelSlot = -1;
				p.Dueling.toggleDuelRule(8, p);
			}
			break;

		case 2158: // fun weapons
		case 2157:
			if (!DuelArena.isInSecondInterface(p) && DuelArena.isInFirstInterface(p)) {
				p.getVariables().duelSlot = -1;
				p.Dueling.toggleDuelRule(9, p);
			}
			break;

		case 30136: // sp attack
		case 30137:
			if (!DuelArena.isInSecondInterface(p) && DuelArena.isInFirstInterface(p)) {
				p.getVariables().duelSlot = -1;
				p.Dueling.toggleDuelRule(10, p);
			}
			break;

		case 53245: // no helm
			if (!DuelArena.isInSecondInterface(p) && DuelArena.isInFirstInterface(p)) {
				p.getVariables().duelSlot = 0;
				p.Dueling.toggleDuelRule(11, p);
			}
			break;

		case 53246: // no cape
			if (!DuelArena.isInSecondInterface(p) && DuelArena.isInFirstInterface(p)) {
				p.getVariables().duelSlot = 1;
				p.Dueling.toggleDuelRule(12, p);
			}
			break;

		case 53247: // no ammy
			if (!DuelArena.isInSecondInterface(p) && DuelArena.isInFirstInterface(p)) {
				p.getVariables().duelSlot = 2;
				p.Dueling.toggleDuelRule(13, p);
			}
			break;

		case 53249: // no weapon.
			if (!DuelArena.isInSecondInterface(p) && DuelArena.isInFirstInterface(p)) {
				p.getVariables().duelSlot = 3;
				p.Dueling.toggleDuelRule(14, p);
			}
			break;

		case 53250: // no body
			if (!DuelArena.isInSecondInterface(p) && DuelArena.isInFirstInterface(p)) {
				p.getVariables().duelSlot = 4;
				p.Dueling.toggleDuelRule(15, p);
			}
			break;

		case 53251: // no shield
			if (!DuelArena.isInSecondInterface(p) && DuelArena.isInFirstInterface(p)) {
				p.getVariables().duelSlot = 5;
				p.Dueling.toggleDuelRule(16, p);
			}
			break;

		case 53252: // no legs
			if (!DuelArena.isInSecondInterface(p) && DuelArena.isInFirstInterface(p)) {
				p.getVariables().duelSlot = 7;
				p.Dueling.toggleDuelRule(17, p);
			}
			break;

		case 53255: // no gloves
			if (!DuelArena.isInSecondInterface(p) && DuelArena.isInFirstInterface(p)) {
				p.getVariables().duelSlot = 9;
				p.Dueling.toggleDuelRule(18, p);
			}
			break;

		case 53254: // no boots
			if (!DuelArena.isInSecondInterface(p) && DuelArena.isInFirstInterface(p)) {
				p.getVariables().duelSlot = 10;
				p.Dueling.toggleDuelRule(19, p);
			}
			break;

		case 53253: // no rings
			if (!DuelArena.isInSecondInterface(p) && DuelArena.isInFirstInterface(p)) {
				p.getVariables().duelSlot = 12;
				p.Dueling.toggleDuelRule(20, p);
			}
			break;

		case 53248: // no arrows
			if (!DuelArena.isInSecondInterface(p) && DuelArena.isInFirstInterface(p)) {
				p.getVariables().duelSlot = 13;
				p.Dueling.toggleDuelRule(21, p);
			}
			break;

		case 26018:
			if (p.opponent == null) {
				p.Dueling.declineDuel(p, false, false);
				return;
			}

			if (p.getVariables().duelRule[DuelArena.RULE_RANGED] && p.getVariables().duelRule[DuelArena.RULE_MELEE]
					&& p.getVariables().duelRule[DuelArena.RULE_MAGIC]) {
				p.sendMessage("You won't be able to attack the player with the rules you have set.");
				break;
			}
			p.getVariables().acceptedFirst = true;
			if (p.getVariables().acceptedFirst == true) {
				p.getPA().sendString("Waiting for other player...", 6684);
				p.opponent.getPA().sendString("Other player has accepted.", 6684);
			}
			if (p.opponent.getVariables().acceptedFirst == true) {
				p.opponent.getPA().sendString("Waiting for other player...", 6684);
				p.getPA().sendString("Other player has accepted.", 6684);
			}

			if (p.getVariables().acceptedFirst == true && p.opponent.getVariables().acceptedFirst == true) {
				p.getVariables().acceptedSecond = false;
				p.opponent.getVariables().acceptedSecond = false;
				DuelArena.removeFromFirstInterface(p);
				DuelArena.removeFromFirstInterface(p.opponent);
				DuelArena.addToSecondInterface(p.opponent, p);
				p.Dueling.confirmDuel(p);
				p.opponent.Dueling.confirmDuel(p.opponent);
			}
			break;

		case 25120:
			if (DuelArena.isDueling(p)) {
				break;
			}
			if (p.opponent == null) {
				p.Dueling.declineDuel(p, false, false);
				return;
			}
			p.getVariables().acceptedSecond = true;
			if (DuelArena.isInSecondInterface(p) && p.getVariables().acceptedSecond
					&& p.opponent.getVariables().acceptedSecond) {
				p.Dueling.startDuel(p);
				p.opponent.Dueling.startDuel(p.opponent);
				p.opponent.getVariables().duelCount = 4;
				p.getVariables().duelCount = 4;
				GameEngine.getScheduler().schedule(new Task(1) {
					@Override
					public void execute() {
						if (System.currentTimeMillis() - p.getVariables().duelDelay > 800
								&& p.getVariables().duelCount > 0) {
							if (p.getVariables().duelCount != 1) {
								p.forcedChat("" + (--p.getVariables().duelCount));
								p.opponent.forcedChat("" + (--p.opponent.getVariables().duelCount));
								p.getVariables().duelDelay = System.currentTimeMillis();
							} else {
								p.getVariables().damageTaken = new int[Constants.MAX_PLAYERS];
								p.opponent.getVariables().damageTaken = new int[Constants.MAX_PLAYERS];
								p.forcedChat("FIGHT!");
								p.opponent.forcedChat("FIGHT!");
								p.getVariables().duelCount = 0;
								p.opponent.getVariables().duelCount = 0;
							}
						}
						if (p.getVariables().duelCount == 0) {
							this.stop();
						}
					}
				});
				p.getVariables().duelDelay = System.currentTimeMillis();
				p.opponent.getVariables().duelDelay = System.currentTimeMillis();
			} else {
				p.getPA().sendString("Waiting for other player...", 6571);
				p.opponent.getPA().sendString("Other player has accepted", 6571);
			}
			break;

		case 4169: // god spell charge
			p.getVariables().usingMagic = true;
			if (!p.getCombat().checkMagicReqs(48)) {
				break;
			}

			if (System.currentTimeMillis() - p.getVariables().godSpellDelay < Constants.GOD_SPELL_CHARGE) {
				p.sendMessage("You still feel the charge in your body!");
				break;
			}
			p.getVariables().godSpellDelay = System.currentTimeMillis();
			p.sendMessage("You feel charged with a magical power!");
			p.gfx100(p.getVariables().MAGIC_SPELLS[48][3]);
			p.startAnimation(p.getVariables().MAGIC_SPELLS[48][2]);
			p.getVariables().usingMagic = false;
			break;

		case 71046:
			p.getVariables().totalxp = 0;
			p.getPA().sendSkillXP(0, 0);
			break;

		case 9154:
			p.logout();
			break;

		case 85248:
			p.getVariables().takeAsNote = !p.getVariables().takeAsNote;
			break;

		case 82016:
			p.getVariables().takeAsNote = !p.getVariables().takeAsNote;
			break;

		case 4171:
		case 50056:
		case 117048: // Home teleport
		case 75010:
			TeleportHandler.teleport(p, 3087, 3503, 0, "auto");
			break;

		case 50235:
		case 4140:
		case 117112:
			p.getDH().sendOption5("Rock Crabs", "Taverly Dungeon", "Slayer Tower", "Brimhaven Dungeon", "More");
			p.getVariables().teleAction = 1;
			break;

		case 4143:
		case 50245:
		case 117123:
			p.getDH().sendOption5("Barrows", "Duel Arena", "Pest Control", "TzHaar Fight Caves", "TzHaar Fight Pits");
			p.getVariables().teleAction = 2;
			// Minigame Teleports
			break;

		case 50253:
		case 117131:
		case 4146:
			p.getDH().sendOption5("Corporal Beast Lair", "Tormented Demons", "Godwars Dungeon", "Dagannoth kings",
					"More");
			p.getVariables().teleAction = 3;
			// Boss Teleports
			break;

		case 51005:
		case 117154:
		case 4150:
			p.getDH().sendOption5("Edgeville", "27 Portals", "East Dragons", "Mage's Bank", "Nowhere");
			p.getVariables().teleAction = 4;
			// Pk Locations
			break;

		case 51013:
		case 6004:
		case 117162:
			p.getDH().sendOption5("Falador Mine", "Underground Smithing Forge", "Piscatoris Fishing Colony", "Catherby",
					"More");
			p.getVariables().teleAction = 5;
			// Skilling Locations
			break;

		case 72038:
		case 51039:
			TeleportHandler.teleport(p, 2787, 2786, 0, "modern");
			// Ape Atoll
			break;

		case 51023:
		case 6005:
		case 117218:
			p.getDH().sendOption5("Lumbridge", "Varrock", "Camelot", "Falador", "Canifis");
			p.getVariables().teleAction = 6;
			break;

		case 29031:
		case 51031:
		case 117210:
			if (p.getVariables().isDonator >= 1) {
				TeleportHandler.teleport(p, 2392, 9903, 0, "auto");
				p.sendMessage("@red@Welcome to the Donator only zone, thank you for Donating!");
			} else {
				p.sendMessage("@red@You must be a Donator to use this teleport!");
				p.sendMessage("@blu@Type ::Donate for more information.");
				return;
			}
			// Donator zone
			break;

		// First teleporting options
		case 9190:
			if (p.getVariables().teleAction == 1) {
				// Rock crabs
				TeleportHandler.teleport(p, 2676, 3715, 0, "auto");
			} else if (p.getVariables().teleAction == 2) {
				// Barrows
				TeleportHandler.teleport(p, 3565, 3314, 0, "auto");
			} else if (p.getVariables().teleAction == 3) {
				// Corporal beast lair
				TeleportHandler.teleport(p, 2964, 4383, 0, "auto");
				p.sendMessage("@red@The Corporal beast is located West.");
			} else if (p.getVariables().teleAction == 4) {
				// Edgeville
				TeleportHandler.teleport(p, 3087, 3519, 0, "auto");
			} else if (p.getVariables().teleAction == 5) {
				TeleportHandler.teleport(p, 3044, 9779, 0, "auto");
				// Falador Mining
			} else if (p.getVariables().teleAction == 6) {
				// Lumbridge
				TeleportHandler.teleport(p, 3222, 3218, 0, "auto");
			} else if (p.getVariables().teleAction == 7) {
				// King black dragon
				TeleportHandler.teleport(p, 3005, 3849, 0, "auto");
			} else if (p.getVariables().teleAction == 8) {
				// More monster teleport - Edgeville getDung
				TeleportHandler.teleport(p, 3117, 9855, 0, "auto");
			} else if (p.getVariables().teleAction == 9) {
				TeleportHandler.teleport(p, 2726, 3485, 0, "auto");
				break;
			}
			if (p.getVariables().dialogueAction == 10) {
				// Runecrafting tele
				TeleportHandler.teleport(p, 2844, 4832, 0, "auto");
				p.getVariables().dialogueAction = -1;
			} else if (p.getVariables().dialogueAction == 11) {
				// Fire altar teleport
				TeleportHandler.teleport(p, 2583, 4838, 0, "auto");
				p.getVariables().dialogueAction = -1;
			} else if (p.getVariables().dialogueAction == 12) {
				// Nature altar teleport
				TeleportHandler.teleport(p, 2398, 4841, 0, "auto");
				p.getVariables().dialogueAction = -1;
			} else if (p.getVariables().dialogueAction == 231) {
				TeleportHandler.teleport(p, 2698, 3206, 0, "auto");
			}
			break;

		// Second teleporting options
		case 9191:
			if (p.getVariables().teleAction == 1) {
				// Taverly getDung
				TeleportHandler.teleport(p, 2884, 9798, 0, "auto");
			} else if (p.getVariables().teleAction == 2) {
				// Duel Arena
				TeleportHandler.teleport(p, 3366, 3265, 0, "auto");
			} else if (p.getVariables().teleAction == 3) {
				// Tormented demons
				TeleportHandler.teleport(p, 2334, 9810, 0, "auto");
			} else if (p.getVariables().teleAction == 4) {
				// 27 Portal
				TeleportHandler.teleport(p, 3022, 3734, 0, "auto");
			} else if (p.getVariables().teleAction == 5) {
				TeleportHandler.teleport(p, 3079, 9500, 0, "auto");
			} else if (p.getVariables().teleAction == 6) {
				TeleportHandler.teleport(p, 3210, 3424, 0, "auto");
			} else if (p.getVariables().teleAction == 8) {
				// More monster teleport - Experiments
				TeleportHandler.teleport(p, 3554, 9943, 0, "auto");
			} else if (p.getVariables().teleAction == 7) {
				// Bork
				TeleportHandler.teleport(p, 3356, 3895, 0, "auto");
			} else if (p.getVariables().teleAction == 9) {
				TeleportHandler.teleport(p, 3056, 3310, 0, "auto");
				break;
			} else if (p.getVariables().dialogueAction == 10) {
				TeleportHandler.teleport(p, 2788, 4834, 0, "auto");
				p.getVariables().dialogueAction = -1;
			} else if (p.getVariables().dialogueAction == 11) {
				TeleportHandler.teleport(p, 2527, 4833, 0, "auto");
				p.getVariables().dialogueAction = -1;
			} else if (p.getVariables().dialogueAction == 12) {
				TeleportHandler.teleport(p, 2464, 4834, 0, "auto");
				p.getVariables().dialogueAction = -1;
			} else if (p.getVariables().dialogueAction == 231) {
				TeleportHandler.teleport(p, 3537, 3448, 0, "auto");
			}
			break;

		// Third teleport options
		case 9192:
			if (p.getVariables().teleAction == 1) {
				// Slayer tower
				TeleportHandler.teleport(p, 3428, 3537, 0, "auto");
			} else if (p.getVariables().teleAction == 2) {
				// Pest control
				TeleportHandler.teleport(p, 2662, 2649, 0, "auto");
			} else if (p.getVariables().teleAction == 3) {
				// God wars
				TeleportHandler.teleport(p, 2882, 5310, 2, "auto");
				p.sendMessage("Welcome to the dangerous God Wars dungeon!");
			} else if (p.getVariables().teleAction == 4) {
				// East Dragons
				TeleportHandler.teleport(p, 3330, 3669, 0, "auto");
			} else if (p.getVariables().teleAction == 5) {
				TeleportHandler.teleport(p, 2338, 3696, 0, "auto");
				// Piscatoris Fishing Colony
			} else if (p.getVariables().teleAction == 6) {
				TeleportHandler.teleport(p, 2757, 3477, 0, "auto");
			} else if (p.getVariables().teleAction == 8) {
				// More monster teleport - Dag lair
				TeleportHandler.teleport(p, 2524, 3740, 0, "auto");
				p.getPA().sendMessage("Climb down the cave entrance, to enter the Dagannoth lair.");
			} else if (p.getVariables().teleAction == 7) {
				// Frost dragons - Free area
				TeleportHandler.teleport(p, 3033, 9582, 0, "auto");
			} else if (p.getVariables().teleAction == 9) {
				TeleportHandler.teleport(p, 3301, 3307, 0, "auto");
				break;
			} else if (p.getVariables().teleAction == 10) {
				// Jungle stryke

			} else if (p.getVariables().dialogueAction == 10) {
				TeleportHandler.teleport(p, 3482, 4838, 0, "auto");
				p.getVariables().dialogueAction = -1;
			} else if (p.getVariables().dialogueAction == 11) {
				TeleportHandler.teleport(p, 2144, 4831, 0, "auto");
				p.getVariables().dialogueAction = -1;
			} else if (p.getVariables().dialogueAction == 12) {
				TeleportHandler.teleport(p, 2207, 4836, 0, "auto");
				p.getVariables().dialogueAction = -1;
			} else if (p.getVariables().dialogueAction == 231) {
				TeleportHandler.teleport(p, 2786, 3762, 0, "auto");
			}
			break;

		// Fourth teleport options
		case 9193:
			if (p.getVariables().teleAction == 1) {
				// Brimhaven getDung
				TeleportHandler.teleport(p, 22710, 9466, 0, "auto");
			} else if (p.getVariables().teleAction == 2) {
				// Fight Caves
				TeleportHandler.teleport(p, 2441, 5171, 0, "auto");
			} else if (p.getVariables().teleAction == 3) {
				// Dag kings
				TeleportHandler.teleport(p, 1909, 4366, 0, "auto");
			} else if (p.getVariables().teleAction == 4) {
				// Mage's Bank
				TeleportHandler.teleport(p, 2539, 4716, 0, "auto");
			} else if (p.getVariables().teleAction == 5) {
				TeleportHandler.teleport(p, 2804, 3433, 0, "auto");
				// Catherbry
			} else if (p.getVariables().teleAction == 6) {
				TeleportHandler.teleport(p, 2964, 3378, 0, "auto");
				// Falador
			} else if (p.getVariables().teleAction == 7) {
				p.getDH().sendOption4("Ice Strykewyrm", "Desert Strykewyrm", "Jungle Strykewyrm", "Back");
				p.getVariables().teleAction = 10; // More boss teleports
			} else if (p.getVariables().teleAction == 8) {
				// More monster teleport - Relleka slayer getDung
				TeleportHandler.teleport(p, 2795, 3615, 0, "auto");
				p.sendMessage("Go through the cave entrance, to enter the Relleka Slayer Dungeon.");
			} else if (p.getVariables().teleAction == 9) {
				p.getDH().sendOption5("Falador Mine", "Underground Smithing Forge", "Piscatoris Fishing Colony",
						"Catherby", "More");
				p.getVariables().teleAction = 5;
			}
			if (p.getVariables().dialogueAction == 10) {
				TeleportHandler.teleport(p, 2660, 4839, 0, "auto");
				p.getVariables().dialogueAction = -1;
			} else if (p.getVariables().dialogueAction == 11) {
				TeleportHandler.teleport(p, 2157, 3862, 0, "auto");
				// c.getRunecrafting().craftRunes(2489);
				p.getVariables().dialogueAction = -1;
			} else if (p.getVariables().dialogueAction == 12) {
				TeleportHandler.teleport(p, 2640, 4896, 0, "auto");
				// c.getRunecrafting().craftRunes(2489);
				p.getVariables().dialogueAction = -1;
			} else if (p.getVariables().dialogueAction == 231) {
				TeleportHandler.teleport(p, 2534, 2965, 0, "auto");
			}
			break;

		// Fifth teleport options
		case 9194:
			if (p.getVariables().teleAction == 1) {
				p.getDH().sendOption5("Edgeville Dungeon", "Experiments", "Dagannoth Lair", "Relleka Slayer Dungeon",
						"Yaks");
				p.getVariables().teleAction = 8; // More monster teleports
				p.getVariables().dialogueAction = -1;
			} else if (p.getVariables().teleAction == 2) {
				// Fight Pits
				TeleportHandler.teleport(p, 2399, 5178, 0, "auto");
				p.getVariables().dialogueAction = -1;
			} else if (p.getVariables().teleAction == 3) {
				p.getDH().sendOption5("King Black Dragon", "Bork @red@(47 Wild)", "Frost Dragons", "Strykewyrms",
						"(Coming Soon)");
				p.getVariables().teleAction = 7; // More boss teleports
				p.getVariables().dialogueAction = -1;
			} else if (p.getVariables().teleAction == 4) {
				p.getPA().closeAllWindows();
			} else if (p.getVariables().teleAction == 5) {
				// p.getDH().sendOption4("Seers' Village","Farming Patches","Al
				// Kharid Mine","Gnome Agility Course");
				p.getDH().sendOption5("Seers' Village", "Farming Patches", "Al Kharid Mine", "Back", "More");
				p.getVariables().teleAction = 9; // More skilling teleports
				p.getVariables().dialogueAction = -1;
				break;
			} else if (p.getVariables().teleAction == 6) {
				TeleportHandler.teleport(p, 3511, 3479, 0, "auto");
				p.getVariables().dialogueAction = -1;
				// Canifis
			} else if (p.getVariables().teleAction == 8) {
				// More monster teleport - Yaks
				TeleportHandler.teleport(p, 2326, 3800, 0, "auto");
			} else if (p.getVariables().teleAction == 9) {
				p.getDH().sendOption4("Gnome Agility Course", "Hunting Grounds", "Back", "Nowhere");
				p.getVariables().teleAction = 11; // More skilling teleports
				p.getVariables().dialogueAction = -1;
				break;
			}

			if (p.getVariables().dialogueAction == 10 || p.getVariables().dialogueAction == 11) {
				p.getVariables().dialogueId++;
				p.getDH().sendDialogues(p.getVariables().dialogueId, 0);
			} else if (p.getVariables().dialogueAction == 12) {
				p.getPA().closeAllWindows();
			} else if (p.getVariables().dialogueAction == 231) {
				TeleportHandler.teleport(p, 2902, 2981, 0, "auto");
			}
			break;

		/** Prayers **/
		case 97168: // thick skin
			CombatPrayer.activatePrayer(p, 0);
			break;
		case 97170: // burst of str
			CombatPrayer.activatePrayer(p, 1);
			break;
		case 97172: // charity of thought
			CombatPrayer.activatePrayer(p, 2);
			break;
		case 97174: // range
			CombatPrayer.activatePrayer(p, 3);
			break;
		case 97176: // mage
			CombatPrayer.activatePrayer(p, 4);
			break;
		case 97178: // rockskin
			CombatPrayer.activatePrayer(p, 5);
			break;
		case 97180: // super human
			CombatPrayer.activatePrayer(p, 6);
			break;
		case 97182: // improved reflexes
			CombatPrayer.activatePrayer(p, 7);
			break;
		case 97184: // hawk eye
			CombatPrayer.activatePrayer(p, 8);
			break;
		case 97186:
			CombatPrayer.activatePrayer(p, 9);
			break;
		case 97188: // protect Item
			CombatPrayer.activatePrayer(p, 10);
			break;
		case 97190: // 26 range
			CombatPrayer.activatePrayer(p, 11);
			break;
		case 97192: // 27 mage
			CombatPrayer.activatePrayer(p, 12);
			break;
		case 97194: // steel skin
			CombatPrayer.activatePrayer(p, 13);
			break;
		case 97196: // ultimate str
			CombatPrayer.activatePrayer(p, 14);
			break;
		case 97198: // incredible reflex
			CombatPrayer.activatePrayer(p, 15);
			break;
		case 97200: // protect from magic
			CombatPrayer.activatePrayer(p, 16);
			break;
		case 97202: // protect from range
			CombatPrayer.activatePrayer(p, 17);
			break;
		case 97204: // protect from melee
			CombatPrayer.activatePrayer(p, 18);
			break;
		case 97206: // 44 range
			CombatPrayer.activatePrayer(p, 19);
			break;
		case 97208: // 45 mystic
			CombatPrayer.activatePrayer(p, 20);
			break;
		case 97210: // retrui
			CombatPrayer.activatePrayer(p, 21);
			break;
		case 97212: // redem
			CombatPrayer.activatePrayer(p, 22);
			break;
		case 97214: // smite
			CombatPrayer.activatePrayer(p, 23);
			break;
		case 97216: // chiv
			CombatPrayer.activatePrayer(p, 24);
			break;
		case 97218: // piety
			CombatPrayer.activatePrayer(p, 25);
			break;

		case 13092:
			if (System.currentTimeMillis() - p.getVariables().lastButton < 400) {
				p.getVariables().lastButton = System.currentTimeMillis();
				break;
			} else {
				p.getVariables().lastButton = System.currentTimeMillis();
			}
			Player ot = PlayerHandler.players[p.getVariables().tradeWith];
			if (ot == null) {
				p.getTradeHandler().declineTrade(false);
				p.sendMessage("Other player has declined the trade.");
				break;
			}
			if (!p.getVariables().tradeConfirmed)
				ot.getPA().sendString("Other player has accepted.", 3431);
			if (!ot.getVariables().tradeConfirmed)
				p.getPA().sendString("Waiting for other player...", 3431);
			p.getVariables().goodTrade = true;
			ot.getVariables().goodTrade = true;
			for (GameItem item : p.getTradeHandler().offeredItems) {
				if (item.id > 0) {
					if (ot.getItems().freeSlots() < p.getTradeHandler().offeredItems.size()) {
						p.sendMessage(ot.playerName + " only has " + ot.getItems().freeSlots()
								+ " free slots, please remove "
								+ (p.getTradeHandler().offeredItems.size() - ot.getItems().freeSlots()) + " items.");
						ot.sendMessage(p.playerName + " has to remove "
								+ (p.getTradeHandler().offeredItems.size() - ot.getItems().freeSlots())
								+ " items or you could offer them "
								+ (p.getTradeHandler().offeredItems.size() - ot.getItems().freeSlots()) + " items.");
						p.getVariables().goodTrade = false;
						ot.getVariables().goodTrade = false;
						p.getPA().sendString("Not enough inventory space...", 3431);
						ot.getPA().sendString("Not enough inventory space...", 3431);
						break;
					} else {
						p.getPA().sendString("Waiting for other player...", 3431);
						ot.getPA().sendString("Other player has accepted", 3431);
						p.getVariables().goodTrade = true;
						ot.getVariables().goodTrade = true;
					}
				}
			}
			if (p.getVariables().inTrade && !p.getVariables().tradeConfirmed && ot.getVariables().goodTrade
					&& p.getVariables().goodTrade) {
				p.getVariables().tradeConfirmed = true;
				if (ot.getVariables().tradeConfirmed) {
					p.getTradeHandler().confirmScreen();
					ot.getTradeHandler().confirmScreen();
					break;
				}

			}

			break;

		case 13218:
			if (System.currentTimeMillis() - p.getVariables().lastButton < 400) {
				p.getVariables().lastButton = System.currentTimeMillis();
				break;
			} else {
				p.getVariables().lastButton = System.currentTimeMillis();
			}
			p.getVariables().tradeAccepted = true;
			final Player ot1 = PlayerHandler.players[p.getVariables().tradeWith];
			if (ot1 == null) {
				p.getTradeHandler().declineTrade(false);
				p.sendMessage("Other player has declined the trade.");
				break;
			}

			if (p.getVariables().inTrade && p.getVariables().tradeConfirmed && ot1.getVariables().tradeConfirmed
					&& !p.getVariables().tradeConfirmed2 && ot1.getVariables().inTrade) {
				p.getVariables().tradeConfirmed2 = true;
				if (ot1.getVariables().tradeConfirmed2 && p.getVariables().tradeConfirmed2) {
					p.getVariables().acceptedTrade = true;
					ot1.getVariables().acceptedTrade = true;
					p.sendMessage("Accepted trade.");
					ot1.sendMessage("Accepted trade.");
					p.getTradeHandler().giveItems();// after that it goes into
													// this method, where they
													// recieve the items
					ot1.getTradeHandler().giveItems();
					p.inTrade = false;
					ot1.inTrade = false;
					p.getPA().removeAllWindows();
					ot1.getPA().removeAllWindows();
					break;
				}
				ot1.getPA().sendString("Other player has accepted.", 3535);
				if (!ot1.getVariables().tradeConfirmed2)
					p.getPA().sendString("Waiting for other player...", 3535);
			}

			break;
		/* Rules Interface Buttons */
		case 125011: // Click agree
			if (!p.getVariables().ruleAgreeButton) {
				p.getVariables().ruleAgreeButton = true;
				p.getPA().sendFrame36(701, 1);
			} else {
				p.getVariables().ruleAgreeButton = false;
				p.getPA().sendFrame36(701, 0);
			}
			break;
		case 125003:// Accept
			if (p.getVariables().ruleAgreeButton) {
				p.getPA().showInterface(3559);
				p.getVariables().newPlayer = false;
			} else if (!p.getVariables().ruleAgreeButton) {
				p.sendMessage("You need to click on you agree before you can continue on.");
			}
			break;
		case 125006:// Decline
			p.sendMessage("You have chosen to decline, Client will be disconnected from the com.exile.");
			break;
		/* End Rules Interface Buttons */
		/* Player Options */
		case 74176:
			if (!p.getVariables().mouseButton) {
				p.getVariables().mouseButton = true;
				p.getPA().sendFrame36(500, 1);
				p.getPA().sendFrame36(170, 1);
			} else if (p.getVariables().mouseButton) {
				p.getVariables().mouseButton = false;
				p.getPA().sendFrame36(500, 0);
				p.getPA().sendFrame36(170, 0);
			}
			break;
		case 3189:
			if (p.splitChat == false) {
				p.getPA().sendFrame36(502, 1);
				p.getPA().sendFrame36(287, 1);
				p.splitChat = true;
			} else if (p.splitChat == true) {
				p.getPA().sendFrame36(502, 0);
				p.getPA().sendFrame36(287, 0);
				p.splitChat = false;
			}
			break;
		case 74180:
			if (!p.getVariables().chatEffects) {
				p.getVariables().chatEffects = true;
				p.getPA().sendFrame36(501, 1);
				p.getPA().sendFrame36(171, 0);
			} else {
				p.getVariables().chatEffects = false;
				p.getPA().sendFrame36(501, 0);
				p.getPA().sendFrame36(171, 1);
			}
			break;
		case 74188:
			if (!p.getVariables().acceptAid) {
				p.getVariables().acceptAid = true;
				p.getPA().sendFrame36(503, 1);
				p.getPA().sendFrame36(427, 1);
			} else {
				p.getVariables().acceptAid = false;
				p.getPA().sendFrame36(503, 0);
				p.getPA().sendFrame36(427, 0);
			}
			break;
		case 74201:// brightness1
			p.getPA().sendFrame36(505, 1);
			p.getPA().sendFrame36(506, 0);
			p.getPA().sendFrame36(507, 0);
			p.getPA().sendFrame36(508, 0);
			p.getPA().sendFrame36(166, 1);
			break;
		case 74203:// brightness2
			p.getPA().sendFrame36(505, 0);
			p.getPA().sendFrame36(506, 1);
			p.getPA().sendFrame36(507, 0);
			p.getPA().sendFrame36(508, 0);
			p.getPA().sendFrame36(166, 2);
			break;

		case 74204:// brightness3
			p.getPA().sendFrame36(505, 0);
			p.getPA().sendFrame36(506, 0);
			p.getPA().sendFrame36(507, 1);
			p.getPA().sendFrame36(508, 0);
			p.getPA().sendFrame36(166, 3);
			break;

		case 74205:// brightness4
			p.getPA().sendFrame36(505, 0);
			p.getPA().sendFrame36(506, 0);
			p.getPA().sendFrame36(507, 0);
			p.getPA().sendFrame36(508, 1);
			p.getPA().sendFrame36(166, 4);
			break;
		case 74206:// area1
			p.getPA().sendFrame36(509, 1);
			p.getPA().sendFrame36(510, 0);
			p.getPA().sendFrame36(511, 0);
			p.getPA().sendFrame36(512, 0);
			break;
		case 74207:// area2
			p.getPA().sendFrame36(509, 0);
			p.getPA().sendFrame36(510, 1);
			p.getPA().sendFrame36(511, 0);
			p.getPA().sendFrame36(512, 0);
			break;
		case 74208:// area3
			p.getPA().sendFrame36(509, 0);
			p.getPA().sendFrame36(510, 0);
			p.getPA().sendFrame36(511, 1);
			p.getPA().sendFrame36(512, 0);
			break;
		case 74209:// area4
			p.getPA().sendFrame36(509, 0);
			p.getPA().sendFrame36(510, 0);
			p.getPA().sendFrame36(511, 0);
			p.getPA().sendFrame36(512, 1);
			break;
		case 168:
			p.startAnimation(855);
			break;
		case 169:
			p.startAnimation(856);
			break;
		case 162:
			p.startAnimation(857);
			break;
		case 164:
			p.startAnimation(858);
			break;
		case 165:
			p.startAnimation(859);
			break;
		case 161:
			p.startAnimation(860);
			break;
		case 170:
			p.startAnimation(861);
			break;
		case 171:
			p.startAnimation(862);
			break;
		case 163:
			p.startAnimation(863);
			break;
		case 167:
			p.startAnimation(864);
			break;
		case 172:
			p.startAnimation(865);
			break;
		case 166:
			p.startAnimation(866);
			break;
		case 52050:
			p.startAnimation(2105);
			break;
		case 52051:
			p.startAnimation(2106);
			break;
		case 52052:
			p.startAnimation(2107);
			break;
		case 52053:
			p.startAnimation(2108);
			break;
		case 52054:
			p.startAnimation(2109);
			break;
		case 52055:
			p.startAnimation(2110);
			break;
		case 52056:
			p.startAnimation(2111);
			break;
		case 52057:
			p.startAnimation(2112);
			break;
		case 52058:
			p.startAnimation(2113);
			break;
		case 43092:
			p.startAnimation(1374);
			break;
		case 2155:
			p.startAnimation(0x46B);
			break;
		case 25103:
			p.startAnimation(0x46A);
			break;
		case 25106:
			p.startAnimation(0x469);
			break;
		case 2154:
			p.startAnimation(0x468);
			break;
		case 52071:
			p.startAnimation(0x84F);
			break;
		case 52072:
			p.startAnimation(0x850);
			break;
		case 59062:
			p.startAnimation(2836);
			break;
		case 72032:
			p.startAnimation(3544);
			break;
		case 72033:
			p.startAnimation(3543);
			break;
		case 72254:
			p.startAnimation(6111);
			break;

		case 59004:
			p.getPA().removeAllWindows();
			break;

		case 184151:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanEnterChat != 0) {
							GameEngine.clanChat.clans[j].whoCanEnterChat = 0;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 187128:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanEnterChat != 8) {
							GameEngine.clanChat.clans[j].whoCanEnterChat = 8;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 187129:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanEnterChat != 7) {
							GameEngine.clanChat.clans[j].whoCanEnterChat = 7;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 187130:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanEnterChat != 6) {
							GameEngine.clanChat.clans[j].whoCanEnterChat = 6;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 187131:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanEnterChat != 5) {
							GameEngine.clanChat.clans[j].whoCanEnterChat = 5;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 187132:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanEnterChat != 4) {
							GameEngine.clanChat.clans[j].whoCanEnterChat = 4;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 187133:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanEnterChat != 3) {
							GameEngine.clanChat.clans[j].whoCanEnterChat = 3;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 187134:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanEnterChat != 2) {
							GameEngine.clanChat.clans[j].whoCanEnterChat = 2;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 187135:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanEnterChat != 1) {
							GameEngine.clanChat.clans[j].whoCanEnterChat = 1;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 184154:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanTalkOnChat != 0) {
							GameEngine.clanChat.clans[j].whoCanTalkOnChat = 0;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 187138:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanTalkOnChat != 8) {
							GameEngine.clanChat.clans[j].whoCanTalkOnChat = 8;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 187139:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanTalkOnChat != 7) {
							GameEngine.clanChat.clans[j].whoCanTalkOnChat = 7;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 187140:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanTalkOnChat != 6) {
							GameEngine.clanChat.clans[j].whoCanTalkOnChat = 6;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 187141:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanTalkOnChat != 5) {
							GameEngine.clanChat.clans[j].whoCanTalkOnChat = 5;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 187142:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanTalkOnChat != 4) {
							GameEngine.clanChat.clans[j].whoCanTalkOnChat = 4;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 187143:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanTalkOnChat != 3) {
							GameEngine.clanChat.clans[j].whoCanTalkOnChat = 3;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 187144:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanTalkOnChat != 2) {
							GameEngine.clanChat.clans[j].whoCanTalkOnChat = 2;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 187145:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanTalkOnChat != 1) {
							GameEngine.clanChat.clans[j].whoCanTalkOnChat = 1;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 184157:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanKickOnChat != 8) {
							GameEngine.clanChat.clans[j].whoCanKickOnChat = 8;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 187149:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanKickOnChat != 7) {
							GameEngine.clanChat.clans[j].whoCanKickOnChat = 7;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 187150:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanKickOnChat != 6) {
							GameEngine.clanChat.clans[j].whoCanKickOnChat = 6;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 187151:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanKickOnChat != 5) {
							GameEngine.clanChat.clans[j].whoCanKickOnChat = 5;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 187152:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanKickOnChat != 4) {
							GameEngine.clanChat.clans[j].whoCanKickOnChat = 4;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 187153:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanKickOnChat != 3) {
							GameEngine.clanChat.clans[j].whoCanKickOnChat = 3;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 187154:
			for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
				if (GameEngine.clanChat.clans[j] != null) {
					if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(p.playerName)) {
						if (GameEngine.clanChat.clans[j].whoCanKickOnChat != 2) {
							GameEngine.clanChat.clans[j].whoCanKickOnChat = 2;
							p.updateClanChatEditInterface(true);
							GameEngine.clanChat.clans[j].changesMade = true;
						}
					}
				}
			}
			break;

		case 70212:
			p.updateClanChatEditInterface(false);
			break;

		case 193219:
			if (p.getVariables().clanId > -1) {
				GameEngine.clanChat.leaveClan(p.playerId, p.getVariables().clanId, false);
			} else {
				p.sendMessage("You are not in a clan.");
				p.getPA().sendFrame126("Join chat", 18135);
			}
			break;

		case 62137:
			if (p.getVariables().clanId >= 0) {
				p.sendMessage("You are already in a clan.");
				break;
			}
			if (p.getOutStream() != null) {
				p.getOutStream().createFrame(187);
				p.flushOutStream();
			}
			break;

		case 71074:
			GameEngine.clanChat.handleShareButton(p);
			break;

		case 118098: // Vengeance
			if (DuelArena.isDueling(p)) {
				if (p.getVariables().duelRule[DuelArena.RULE_MAGIC]) {
					p.sendMessage("Magic has been disabled in this duel!");
					return;
				}
			}
			if (p.getVariables().castVengeance == 1) {
				p.sendMessage("You have already cast vengeance!");
			}
			p.getPA().vengMe();
			break;

		// case 47130:
		// c.forcedText = "I must slay another " + c.taskAmount + " " +
		// Server.npcHandler.getNpcListName(c.slayerTask) + ".";
		// c.forcedChatUpdateRequired = true;
		// c.updateRequired = true;
		// break;

		case 24017:
			p.getPA().resetAutocast();
			p.getItems().sendWeapon(p.getVariables().playerEquipment[p.getVariables().playerWeapon],
					p.getItems().getItemName(p.getVariables().playerEquipment[p.getVariables().playerWeapon]));
			break;

		case 55095:
			p.getPA().destroyItem(p.getVariables().destroyItem);
		case 55096:
			p.getPA().closeAllWindows();
			break;
		case 154:
			handleSkillCape(p);
			break;
		case 184163:
			p.getPA().closeAllWindows();
			break;
		}
		if (p.getVariables().isAutoButton(buttonId))
			p.getVariables().assignAutocast(buttonId);
	}

	public void handleSkillCape(Player c) {
		if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9747
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9748) {
			c.startAnimation(4959);
			c.gfx0(823);
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9750
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9751) {
			c.startAnimation(4981);
			c.gfx0(828);
		} else {
			c.getVariables();
			c.getVariables();
			if (c.getVariables().playerEquipment[c.playerCape] == 9753
					|| c.getVariables().playerEquipment[c.playerCape] == 9754) {
				c.startAnimation(4961);
				c.gfx0(824);
			} else {
				c.getVariables();
				c.getVariables();
				if (c.getVariables().playerEquipment[c.playerCape] == 9756
						|| c.getVariables().playerEquipment[c.playerCape] == 9757) {
					c.startAnimation(4973);
					c.gfx0(832);
				} else {
					c.getVariables();
					c.getVariables();
					if (c.getVariables().playerEquipment[c.playerCape] == 9759
							|| c.getVariables().playerEquipment[c.playerCape] == 9760) {
						c.startAnimation(4979);
						c.gfx0(829);
					} else {
						c.getVariables();
						c.getVariables();
						if (c.getVariables().playerEquipment[c.playerCape] == 9762
								|| c.getVariables().playerEquipment[c.playerCape] == 9763) {
							c.startAnimation(4939);
							c.gfx0(813);
						} else {
							c.getVariables();
							c.getVariables();
							if (c.getVariables().playerEquipment[c.playerCape] == 9765
									|| c.getVariables().playerEquipment[c.playerCape] == 9766) {
								c.startAnimation(4947);
								c.gfx0(817);
							} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9768
									|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9769) {
								c.startAnimation(4971);
								c.gfx0(833);
							} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9771
									|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9772) {
								c.startAnimation(4977);
								c.gfx0(830);
							} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9774
									|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9775) {
								c.startAnimation(4969);
								c.gfx0(835);
							} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9777
									|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9778) {
								c.startAnimation(4965);
								c.gfx0(826);
							} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9780
									|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9781) {
								c.startAnimation(4949);
								c.gfx0(818);
							} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9783
									|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9784) {
								c.startAnimation(4937);
								c.gfx0(812);
							} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9786
									|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9787) {
								c.startAnimation(4967);
								c.gfx0(827);
							} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9789
									|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9790) {
								c.startAnimation(4953);
								c.gfx0(820);
							} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9792
									|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9793) {
								c.startAnimation(4941);
								c.gfx0(814);
							} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9795
									|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9796) {
								c.startAnimation(4943);
								c.gfx0(815);
							} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9798
									|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9799) {
								c.startAnimation(4951);
								c.gfx0(819);
							} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9801
									|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9802) {
								c.startAnimation(4955);
								c.gfx0(821);
							} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9804
									|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9805) {
								c.startAnimation(4975);
								c.gfx0(831);
							} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9807
									|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9808) {
								c.startAnimation(4957);
								c.gfx0(822);
							} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9810
									|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9811) {
								c.startAnimation(4963);
								c.gfx0(825);
							} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9813) {
								c.startAnimation(4945);
								c.gfx0(816);
							} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19710) {
								c.dungemote();
							} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9948
									|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9949) {
								c.startAnimation(5158);
								c.gfx0(907);
							} else {
								c.sendMessage("You need a skillcape to perform this emote.");
							}
						}
					}
				}
			}
		}
	}

	public static void handleSkillCape2(Player c) {
		if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9747
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9748) {
			c.startAnimation(4959);
			c.gfx0(823);
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9750
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9751) {
			c.startAnimation(4981);
			c.gfx0(828);
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9753
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9754) {
			c.startAnimation(4961);
			c.gfx0(824);
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9756
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9757) {
			c.startAnimation(4973);
			c.gfx0(832);
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9759
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9760) {
			c.startAnimation(4979);
			c.gfx0(829);
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9762
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9763) {
			c.startAnimation(4939);
			c.gfx0(813);
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9765
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9766) {
			c.startAnimation(4947);
			c.gfx0(817);
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9768
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9769) {
			c.startAnimation(4971);
			c.gfx0(833);
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9771
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9772) {
			c.startAnimation(4977);
			c.gfx0(830);
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9774
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9775) {
			c.startAnimation(4969);
			c.gfx0(835);
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9777
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9778) {
			c.startAnimation(4965);
			c.gfx0(826);
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9780
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9781) {
			c.startAnimation(4949);
			c.gfx0(818);
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9783
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9784) {
			c.startAnimation(4937);
			c.gfx0(812);
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9786
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9787) {
			c.startAnimation(4967);
			c.gfx0(827);
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9789
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9790) {
			c.startAnimation(4953);
			c.gfx0(820);
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9792
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9793) {
			c.startAnimation(4941);
			c.gfx0(814);
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9795
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9796) {
			c.startAnimation(4943);
			c.gfx0(815);
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9798
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9799) {
			c.startAnimation(4951);
			c.gfx0(819);
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9801
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9802) {
			c.startAnimation(4955);
			c.gfx0(821);
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9804
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9805) {
			c.startAnimation(4975);
			c.gfx0(831);
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9807
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9808) {
			c.startAnimation(4957);
			c.gfx0(822);
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9810
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9811) {
			c.startAnimation(4963);
			c.gfx0(825);
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9813) {
			c.startAnimation(4945);
			c.gfx0(816);
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19710) {
			c.dungemote();
		} else if (c.getVariables().playerEquipment[c.getVariables().playerCape] == 9948
				|| c.getVariables().playerEquipment[c.getVariables().playerCape] == 9949) {
			c.startAnimation(5158);
			c.gfx0(907);
		} else {
			c.sendMessage("You need a skillcape to perform this emote.");
		}
	}
}
