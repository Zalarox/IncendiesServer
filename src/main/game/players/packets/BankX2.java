package main.game.players.packets;

import main.game.players.PacketType;
import main.game.players.Player;
import main.game.players.content.minigames.impl.dueling.DuelPlayer;
import main.game.players.content.skills.cooking.Cooking;
import main.game.players.content.skills.crafting.SilverCrafting;
import main.game.players.content.skills.dungeoneering.Items;
import main.game.players.content.skills.prayer.Prayer;

/**
 * Bank X Items
 **/
public class BankX2 implements PacketType {
	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		int amount = player.getInStream().readDWord();
		if (player.getVariables().teleTimer > 0) {
			return;
		}
		if (amount == 0)
			amount = 1;
		SilverCrafting.makeSilver(player, player.getVariables().xInterfaceId, amount);

		if (player.getVariables().PrayX) {
			Prayer.buryAltarBone(player, player.getVariables().prayerItemID, player.getVariables().altarXCoord,
					player.getVariables().altarYCoord, amount);
			player.getVariables().PrayX = false;
			return;
		}
		if (player.getVariables().bindX) {
			Items.bindStackable(player, amount);
			player.getVariables().bindX = false;
			return;
		}
		switch (player.getVariables().xInterfaceId) {

		case 53150:
			Cooking.handleCookingTick(player, amount);
			break;

		case 5064:
			player.getItems().bankItem(player.getVariables().playerItems[player.getVariables().xRemoveSlot],
					player.getVariables().xRemoveSlot, amount);
			break;

		case 5382:
			player.getItems().fromBank(player.getVariables().xRemoveSlot, amount);
			break;

		case 3322:
			if (!player.getItems().playerHasItem(player.getVariables().xRemoveId, amount))
				return;
			if (!DuelPlayer.contains(player) && !DuelPlayer.isInFirstScreen(player)
					&& !DuelPlayer.isInSecondScreen(player)) {
				player.getTradeHandler().tradeItem(player.getVariables().xRemoveId, player.getVariables().xRemoveSlot,
						amount);
			} else {
				player.Dueling.stakeItem(player.getVariables().xRemoveId, player.getVariables().xRemoveSlot, amount,
						player);
			}
			break;

		case 3415:
			if (!player.getItems().playerHasItem(player.getVariables().xRemoveId, amount))
				return;
			if (!DuelPlayer.contains(player) && !DuelPlayer.isInFirstScreen(player)
					&& !DuelPlayer.isInSecondScreen(player)) {
				player.getTradeHandler().fromTrade(player.getVariables().xRemoveId, player.getVariables().xRemoveSlot,
						amount);
			}
			break;

		case 6669:
			if (!player.getItems().playerHasItem(player.getVariables().xRemoveId, amount)) {
				player.sendMessage("return1");// returns here in x not sure were
												// in All
				return;
			}
			player.sendMessage("bankX2");
			player.Dueling.fromDuel(player.getVariables().xRemoveId, player.getVariables().xRemoveSlot, amount, player);
			break;
		}
	}
}