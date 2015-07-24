package main.game.players.content.skills.cooking;

import main.game.items.Item;
import main.game.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 22/12/11 Time: 21:41 To change
 * this template use File | Settings | File Templates.
 */
public class FlourRelated {
	public static final int POT_OF_FLOUR = 1933;
	public static final int BUCKET_OF_WATER = 1929;
	public static final int BUCKET = 1925;

	public static void handleButton(Player player, int buttonId) {

		if (!player.getInventory().getItemContainer().contains(BUCKET_OF_WATER)
				|| !player.getInventory().getItemContainer().contains(POT_OF_FLOUR))
			return;
		switch (buttonId) {
		case 53202:
			player.getInventory().removeItem(new Item(BUCKET_OF_WATER));
			player.getInventory().removeItem(new Item(POT_OF_FLOUR));
			player.getInventory().addItem(new Item(BUCKET));
			player.getPA().sendMessage("You put the water on the flour and make it into a bread dough");
			player.getInventory().addItem(new Item(2307));
			player.getPA().removeInterfaces();
			break;
		case 53203:
			player.getInventory().removeItem(new Item(BUCKET_OF_WATER));
			player.getInventory().removeItem(new Item(POT_OF_FLOUR));
			player.getInventory().addItem(new Item(BUCKET));
			player.getPA().sendMessage("You put the water on the flour and make it into a pastry dough");
			player.getInventory().addItem(new Item(1953));
			player.getPA().removeInterfaces();
			break;
		case 53204:
			player.getInventory().removeItem(new Item(BUCKET_OF_WATER));
			player.getInventory().removeItem(new Item(POT_OF_FLOUR));
			player.getInventory().addItem(new Item(BUCKET));
			player.getPA().sendMessage("You put the water on the flour and make it into a pizza base");
			player.getInventory().addItem(new Item(2283));

			break;
		}
	}
}
