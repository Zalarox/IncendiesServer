package main.game.players.packets;

import main.GameEngine;
import main.game.players.PacketType;
import main.game.players.Player;
import main.game.players.content.ArmourSets;
import main.game.players.content.minigames.impl.barrows.Barrows;
import main.game.players.content.minigames.impl.barrows.BarrowsConstants;
import main.game.players.content.skills.crafting.GemCrafting;
import main.game.players.content.skills.crafting.GemData;
import main.game.players.content.skills.herblore.Herblore;
import main.game.players.content.skills.hunter.HunterHandler;
import main.game.players.content.skills.hunter.HunterLooting;
import main.game.players.content.skills.prayer.Prayer;
import main.game.players.content.skills.runecrafting.Pouches;
import main.game.players.content.skills.woodcutting.Woodcutting;
import main.handlers.Following;
import main.util.Misc;

/**
 * Clicking an item, bury bone, eat food etc
 **/
public class ClickItem implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int frame = c.getInStream().readSignedWordBigEndianA(); // use to be
																// readSignedWordBigEndianA();
		int itemSlot = c.getInStream().readSignedWordA(); // use to be
															// readUnsignedWordA();
		int itemId = c.getInStream().readSignedWordBigEndian(); // us to be
																// unsigned.
		if (c.getVariables().teleTimer > 0)
			return;
		if (c.getVariables().resting) {
			c.getPA().resetRest();
		}
		Following.resetFollow(c);
		if (itemSlot > c.getVariables().playerItems.length) {
			return;
		}
		if (itemId != c.getVariables().playerItems[itemSlot] - 1 || HunterHandler.layTrap(c, itemId)) {
			return;
		}
		if (HunterLooting.giveLoot(c, itemId, false)) {
			return;
		}
		c.getPA().resetSkills();
		if (Prayer.PrayerData.getID(itemId) != null) {
			Prayer.buryBone(c, itemId, itemSlot);
		}
		if (Herblore.isHerb(itemId)) {
			Herblore.cleanTheHerb(c, itemId);
		}
		main.game.players.content.skills.dungeoneering.skills.Herblore.clean(c, itemId);
		switch (frame) {
		case 4233: // make 1 ring crafting
			GemCrafting.startCrafter(c, GemData.getGemSlot()[itemSlot], 1, 0);
			break;
		case 4239: // make 1 neckalce crafting
			GemCrafting.startCrafter(c, GemData.getGemSlot()[itemSlot], 1, 1);
			break;
		case 4245: // make 1 amulet crafting
			GemCrafting.startCrafter(c, GemData.getGemSlot()[itemSlot], 1, 2);
			break;
		}

		if (itemId >= 5509 && itemId <= 5514) {
			Pouches.fillEssencePouch(c, itemId);
			return;
		}

		if (ArmourSets.isSet(itemId))
			ArmourSets.handleSet(c, itemId);

		if (itemId >= 5509 && itemId <= 5514) {
			int pouch = -1;
			int a = itemId;
			if (a == 5509)
				pouch = 0;
			if (a == 5510)
				pouch = 1;
			if (a == 5512)
				pouch = 2;
			if (a == 5514)
				pouch = 3;
			c.getPA().fillPouch(pouch);
			return;
		}
		if (itemId == 4155) { // Slayer gem
			c.getDH().sendDialogues(321, 0);
		}
		if (c.getFood().isFood(itemId))
			c.getFood().eat(itemId, itemSlot);
		if (c.getPotions().isPotion(itemId))
			c.getPotions().handlePotion(itemId, itemSlot);
		switch (itemId) {
		case 6:
			c.getCannon().setUpCannon();
			break;

		case 952:
			c.startAnimation(830);
			if (BarrowsConstants.inMount(c))
				Barrows.digMount(c);
			else
				c.sendMessage("Nothing interesting happens");
			break;

		/*
		 * case 4053: for (int i = 0; i < NPCHandler.maxNPCs; i++) if
		 * (NPCHandler.npcs[i] != null) if (NPCHandler.npcs[i].npcType == 1532
		 * || NPCHandler.npcs[i].npcType == 1533 || NPCHandler.npcs[i].npcType
		 * == 1534 || NPCHandler.npcs[i].npcType == 1535) if
		 * (NPCHandler.npcs[i].absX == c.absX && NPCHandler.npcs[i].absY ==
		 * c.absY && NPCHandler.npcs[i].heightLevel == c.heightLevel) {
		 * c.sendMessage("You can't setup a barricade here."); return; } if
		 * (c.inCwSafe()) { c.sendMessage("You can't setup a barricade here.");
		 * return; } if ((c.saraTeam() && CastleWars.saraBarricades > 9) ||
		 * (c.zammyTeam() && CastleWars.zammyBarricades > 9)) { c.sendMessage(
		 * "Your team already has 10 barricades setup."); return; }
		 * c.getItems().deleteItem(4053, itemSlot, 1); if (c.saraTeam()) {
		 * Server.npcHandler.spawnNpc(1532, c.absX, c.absY, c.heightLevel, 0,
		 * 50, 0, 0, 50); CastleWars.saraBarricades++; } if (c.zammyTeam()) {
		 * Server.npcHandler.spawnNpc(1534, c.absX, c.absY, c.heightLevel, 0,
		 * 50, 0, 0, 50); CastleWars.zammyBarricades++; } break;
		 */

		case 5070:
		case 5071:
		case 5072:
		case 5073:
		case 5074:
			Woodcutting.handleNest(c, itemId);
			break;

		case 15098:
		case 15088:
			if (c.getVariables().diceTimer < 1) {
				if (c.getVariables().clanId < 0) {
					c.sendMessage("@red@You must be in a clan chat to use a dice.");
					return;
				} else {
					int random = 2 + Misc.random(98);
					GameEngine.clanChat
							.messageClan(
									"<col=255>" + c.playerName + "</col> <col=255>has rolled a</col> <col=16711680>"
											+ random + "</col> <col=255>using the percentile dice.</col>",
									c.getVariables().clanId);
					c.startAnimation(11900);
					c.gfx0(2075);
					c.getVariables().diceTimer = 10;
				}
			}
			break;
		case 15707:
			main.game.players.content.skills.dungeoneering.Items.teleport(c, true);
			break;
		case 19650:
			main.game.players.content.skills.dungeoneering.Items.handleToolKit(c);
			break;
		}
	}

}
