package incendius.game.players.packets;

import incendius.game.players.PacketType;
import incendius.game.players.Player;
import incendius.game.players.content.minigames.DuelArena;
import incendius.game.players.content.skills.cooking.Cooking;
import incendius.game.players.content.skills.crafting.SilverCrafting;
import incendius.game.players.content.skills.dungeoneering.Items;
import incendius.game.players.content.skills.prayer.Prayer;

/**
 * Bank X Items
 **/
public class BankX2 implements PacketType {
	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		int amount = player.getInStream().readDWord();
		if (player.getInstance().teleTimer > 0) {
			return;
		}
		if (amount == 0)
			amount = 1;
		SilverCrafting.makeSilver(player, player.getInstance().xInterfaceId, amount);

		if (player.getInstance().PrayX) {
			Prayer.buryAltarBone(player, player.getInstance().prayerItemID, player.getInstance().altarXCoord,
					player.getInstance().altarYCoord, amount);
			player.getInstance().PrayX = false;
			return;
		}
		if (player.getInstance().bindX) {
			Items.bindStackable(player, amount);
			player.getInstance().bindX = false;
			return;
		}
		switch (player.getInstance().xInterfaceId) {

		case 53150:
			Cooking.handleCookingTick(player, amount);
			break;

		case 5064:
			player.getItems().bankItem(player.getInstance().playerItems[player.getInstance().xRemoveSlot],
					player.getInstance().xRemoveSlot, amount);
			break;

		case 5382:
			player.getItems().fromBank(player.getInstance().xRemoveSlot, amount);
			break;

		case 3322:
			if (!player.getItems().playerHasItem(player.getInstance().xRemoveId, amount))
				return;
			if (!DuelArena.isDueling(player) && !DuelArena.isInFirstInterface(player)
					&& !DuelArena.isInSecondInterface(player)) {
				player.getTradeHandler().tradeItem(player.getInstance().xRemoveId, player.getInstance().xRemoveSlot,
						amount);
			} else {
				player.Dueling.addStakedItem(player.getInstance().xRemoveId, player.getInstance().xRemoveSlot, amount,
						player);
			}
			break;

		case 3415:
			if (!player.getItems().playerHasItem(player.getInstance().xRemoveId, amount))
				return;
			if (!DuelArena.isDueling(player) && !DuelArena.isInFirstInterface(player)
					&& !DuelArena.isInSecondInterface(player)) {
				player.getTradeHandler().fromTrade(player.getInstance().xRemoveId, player.getInstance().xRemoveSlot,
						amount);
			}
			break;

		case 6669:
			if (!player.getItems().playerHasItem(player.getInstance().xRemoveId, amount)) {
				player.sendMessage("return1");// returns here in x not sure were
				// in All
				return;
			}
			player.sendMessage("bankX2");
			player.Dueling.removeStakedItem(player.getInstance().xRemoveId, player.getInstance().xRemoveSlot, amount,
					player);
			break;
		}
	}
}