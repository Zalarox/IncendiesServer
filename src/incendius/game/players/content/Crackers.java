package incendius.game.players.content;

import incendius.game.players.Player;
import incendius.game.players.PlayerHandler;
import incendius.util.Misc;

/*Need to do the itemonplayer class first*/
public class Crackers {

	public static void handleCrackers(Player c, int itemId, int playerId) {
		Player usedOn = PlayerHandler.players[playerId];
		if (!c.getItems().playerHasItem(itemId))
			return;

		if (usedOn.getItems().freeSlots() < 1) {
			c.sendMessage("The other player doesn't have enough inventory space!");
			return;
		}

		c.turnPlayerTo(usedOn.absX, usedOn.absY);
		c.sendMessage("You pull a Christmas cracker...");
		usedOn.sendMessage("You pull a Christmas cracker...");
		c.gfx0(176);
		c.startAnimation(451);
		c.getItems().deleteItem(itemId, 1);

		if (Misc.random(1) == 0) {
			c.getItems().addItem(getRandomPhat(), 1);
			c.sendMessage("You got the prize!");
			c.forcedChat("Hey! I got the cracker!");
			usedOn.sendMessage("You didn't get the prize.");
		} else {
			usedOn.getItems().addItem(getRandomPhat(), 1);
			usedOn.sendMessage("You got the prize!");
			usedOn.forcedChat("Hey! I got the cracker!");
			c.sendMessage("You didn't get the prize.");
		}
	}

	private static int getRandomPhat() {
		int[] phats = { 1038, 1040, 1042, 1044, 1048 };
		return phats[(int) Math.floor(Math.random() * phats.length)];
	}

}
