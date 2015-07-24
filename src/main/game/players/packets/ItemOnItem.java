package main.game.players.packets;

import main.game.items.Item;
import main.game.items.UseItem;
import main.game.players.PacketType;
import main.game.players.Player;
import main.game.players.content.skills.cooking.OneIngredients;
import main.game.players.content.skills.cooking.ThreeIngredients;
import main.game.players.content.skills.cooking.TwoIngredients;
import main.game.players.content.skills.crafting.BasicCraft;
import main.game.players.content.skills.crafting.GemCrafting;
import main.game.players.content.skills.crafting.GemCutting;
import main.game.players.content.skills.crafting.GemData;
import main.game.players.content.skills.crafting.GlassMaking;
import main.game.players.content.skills.crafting.LeatherMakingHandler;
import main.game.players.content.skills.firemaking.Firemaking;
import main.game.players.content.skills.fletching.BoltHandler;
import main.game.players.content.skills.fletching.BowHandler;
import main.game.players.content.skills.fletching.StringingHandler;
import main.game.players.content.skills.herblore.Herblore;
import main.util.Menus;

public class ItemOnItem implements PacketType {

	@Override
	@SuppressWarnings("unused")
	public void processPacket(Player player, int packetType, int packetSize) {
		int itemFirstClickSlot = player.getInStream().readUnsignedWord();
		int itemSecondClickSlot = player.getInStream().readUnsignedWordA();
		int firstItem = player.getVariables().playerItems[itemFirstClickSlot] - 1;
		int secondItem = player.getVariables().playerItems[itemSecondClickSlot] - 1;
		final int useWith = player.getVariables().playerItems[itemFirstClickSlot] - 1;
		final int itemUsed = player.getVariables().playerItems[itemSecondClickSlot] - 1;
		Item firstClickItem = new Item(firstItem, player.getVariables().playerItemsN[itemFirstClickSlot]);
		Item secondClickItem = new Item(secondItem, player.getVariables().playerItemsN[itemSecondClickSlot]);
		if (player.getVariables().teleTimer > 0)
			return;
		if (player.getVariables().resting) {
			player.getPA().resetRest();
		}
		if (Herblore.mixPotion(itemUsed, useWith)) {
			Herblore.setUpUnfinished(player, itemUsed, useWith);
		} else if (Herblore.mixPotionNew(itemUsed, useWith)) {
			Herblore.setUpPotion(player, itemUsed, useWith);
		}
		main.game.players.content.skills.dungeoneering.skills.Herblore.setUpUnfinished(player, itemUsed, useWith);
		main.game.players.content.skills.dungeoneering.skills.Herblore.setUpPotion(player, itemUsed, useWith);
		if (OneIngredients.mixItems(player, firstItem, secondItem, itemFirstClickSlot, itemSecondClickSlot)) {
			return;
		}
		if (TwoIngredients.mixItems(player, firstItem, secondItem, itemFirstClickSlot, itemSecondClickSlot)) {
			return;
		}
		if (ThreeIngredients.mixItems(player, firstItem, secondItem, itemFirstClickSlot, itemSecondClickSlot)) {
			return;
		}
		/* crafting */
		if (GemCutting.handleCutting(player, firstItem, secondItem,
				firstItem != GemCutting.CHISEL ? itemFirstClickSlot : itemSecondClickSlot)) {
			return;
		}
		if (LeatherMakingHandler.handleItemOnItem(player, firstItem, secondItem, itemFirstClickSlot,
				itemSecondClickSlot)) {
			return;
		}
		if (BasicCraft.handleItemOnItem(player, firstItem, secondItem)) {
			return;
		}
		if ((firstItem == GlassMaking.GLASSBLOWING_PIPE && secondItem == GlassMaking.MOLTEN_GLASS)
				|| (secondItem == GlassMaking.GLASSBLOWING_PIPE && firstItem == GlassMaking.MOLTEN_GLASS)) {
			Menus.sendSkillMenu(player, "glassMaking");
			return;
		}
		if ((firstItem == 946 && secondItem == 771) || (firstItem == 771 && secondItem == 946)) {
			Menus.sendSkillMenu(player, "dramenBranch");
			return;
		}
		/*
		 * STRINGING AMULETS
		 */

		for (int i = 0; i < GemData.mendItems.length; i++) {
			if (GemData.mendItems[i][0] == firstItem || GemData.mendItems[i][0] == secondItem) {
				GemCrafting.string(player, i);
				continue;
			}
		}
		UseItem.ItemonItem(player, secondItem, firstItem);
		if ((firstItem == 590 || secondItem == 590) || (firstItem == 17678 || secondItem == 17678)) {
			Firemaking.attemptFire(player, firstItem, secondItem, player.absX, player.absY, false);
		}
		BowHandler.handleInterface(player, useWith, itemUsed);
		StringingHandler.useItemInterface(player, useWith, itemUsed);
		BoltHandler.finishBolt(player, useWith, itemUsed);
	}
}
