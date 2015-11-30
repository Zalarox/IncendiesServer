package incendius.game.players.packets;

import incendius.game.items.Item;
import incendius.game.items.UseItem;
import incendius.game.players.PacketType;
import incendius.game.players.Player;
import incendius.game.players.content.skills.cooking.OneIngredients;
import incendius.game.players.content.skills.cooking.ThreeIngredients;
import incendius.game.players.content.skills.cooking.TwoIngredients;
import incendius.game.players.content.skills.crafting.BasicCraft;
import incendius.game.players.content.skills.crafting.GemCrafting;
import incendius.game.players.content.skills.crafting.GemCutting;
import incendius.game.players.content.skills.crafting.GemData;
import incendius.game.players.content.skills.crafting.GlassMaking;
import incendius.game.players.content.skills.crafting.LeatherMakingHandler;
import incendius.game.players.content.skills.firemaking.Firemaking;
import incendius.game.players.content.skills.fletching.BoltHandler;
import incendius.game.players.content.skills.fletching.BowHandler;
import incendius.game.players.content.skills.fletching.StringingHandler;
import incendius.game.players.content.skills.herblore.Herblore;
import incendius.util.Menus;

public class ItemOnItem implements PacketType {

	@Override
	@SuppressWarnings("unused")
	public void processPacket(Player player, int packetType, int packetSize) {
		int itemFirstClickSlot = player.getInStream().readUnsignedWord();
		int itemSecondClickSlot = player.getInStream().readUnsignedWordA();
		int firstItem = player.getInstance().playerItems[itemFirstClickSlot] - 1;
		int secondItem = player.getInstance().playerItems[itemSecondClickSlot] - 1;
		final int useWith = player.getInstance().playerItems[itemFirstClickSlot] - 1;
		final int itemUsed = player.getInstance().playerItems[itemSecondClickSlot] - 1;
		Item firstClickItem = new Item(firstItem, player.getInstance().playerItemsN[itemFirstClickSlot]);
		Item secondClickItem = new Item(secondItem, player.getInstance().playerItemsN[itemSecondClickSlot]);
		if (player.getInstance().teleTimer > 0)
			return;
		if (player.getInstance().resting) {
			player.getPA().resetRest();
		}
		if (Herblore.mixPotion(itemUsed, useWith)) {
			Herblore.setUpUnfinished(player, itemUsed, useWith);
		} else if (Herblore.mixPotionNew(itemUsed, useWith)) {
			Herblore.setUpPotion(player, itemUsed, useWith);
		}
		incendius.game.players.content.skills.dungeoneering.skills.Herblore.setUpUnfinished(player, itemUsed, useWith);
		incendius.game.players.content.skills.dungeoneering.skills.Herblore.setUpPotion(player, itemUsed, useWith);
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
