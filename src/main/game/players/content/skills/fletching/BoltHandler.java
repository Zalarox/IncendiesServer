package main.game.players.content.skills.fletching;

import main.game.players.Player;
import main.handlers.SkillHandler;

public class BoltHandler {

	/**
	 * @author Ochroid | Scott
	 */

	private static int[][] boltArray = { { 9375, 877, 10 }, { 9377, 9140, 15 }, { 9378, 9141, 20 }, { 9379, 9142, 30 },
			{ 9380, 9143, 40 }, { 9381, 9144, 50 } };

	public static void finishBolt(final Player c, final int useWith, final int itemUsed) {
		for (int i = 0; i < boltArray.length; i++) {
			if (useWith == 314 && itemUsed == boltArray[i][0] || useWith == boltArray[i][0] && itemUsed == 314) {
				if (!c.getItems().playerHasItem(useWith, 15) && !c.getItems().playerHasItem(itemUsed, 15)) {
					c.sendMessage("You must have 15 of each supply to do this!");
					return;
				}
				c.getItems().deleteItem2(314, 15);
				c.getItems().deleteItem2(boltArray[i][0], 15);
				c.getItems().addItem(boltArray[i][1], 15);
				c.getPA().addSkillXP(boltArray[i][2] * SkillHandler.XPRates.FLETCHING.getXPRate(),
						c.getInstance().playerFletching);
				c.sendMessage("You create 15 " + c.getItems().getItemName(boltArray[i][1]) + ".");
			}
		}
	}

}
