package incendius.game.players.content.skills.thieving;

import incendius.Server;
import incendius.event.Task;
import incendius.game.players.Player;
import incendius.handlers.SkillHandler;
import incendius.util.Misc;

/**
 * Thieving.java
 *
 * @author Sanity
 *
 **/

public class Thieving {

	private Player c;

	// Structure: {NPC_ID, THIEV_LEVEL, EXP_GIVEN, COIN_GAINED, ???}
	
	public int[][] npcThieving = { { 1, 1, 8, 2000, 1 }, { 18, 25, 26, 5000, 1 }, { 9, 40, 47, 10000, 2 },
			{ 26, 55, 85, 14000, 3 }, { 20, 70, 152, 17500, 4 }, { 21, 80, 273, 20000, 5 } };

	public Thieving(Player c) {
		this.c = c;
	}

	public void stealFromNPC(int id) {
		if (System.currentTimeMillis() - c.getInstance().lastThieve < 2000)
			return;
		for (int j = 0; j < npcThieving.length; j++) {
			if (npcThieving[j][0] == id) {
				if (c.getInstance().playerLevel[c.getInstance().playerThieving] >= npcThieving[j][1]) {
					if (Misc.random(c.getInstance().playerLevel[c.getInstance().playerThieving] + 2
							- npcThieving[j][1]) != 1) {
						c.getPA().addSkillXP(npcThieving[j][2] * SkillHandler.XPRates.THIEVING.getXPRate(),
								c.getInstance().playerThieving);
						c.getItems().addItem(995, npcThieving[j][3]);
						c.startAnimation(881);
						c.getInstance().lastThieve = System.currentTimeMillis();
						c.sendMessage("You manage to steal some money.");
						break;
					} else {
						c.setHitDiff(npcThieving[j][4]);
						c.setHitUpdateRequired(true);
						c.getInstance().lifePoints -= npcThieving[j][4] * 10;
						c.getInstance().lastThieve = System.currentTimeMillis() + 2000;
						break;
					}
				} else {
					c.sendMessage("You need a thieving level of " + npcThieving[j][1] + " to pickpocket this NPC.");
				}
			}
		}
	}

	public void stealFromStall(int id, int xp, int level, final int i, final int x, final int y) {
		c.turnPlayerTo(x, y);
		if (System.currentTimeMillis() - c.getInstance().lastThieve < 2500)
			return;
		if (c.getInstance().playerLevel[c.getInstance().playerThieving] >= level) {
			int quantity = (int) (Math.random() * 2 + 1);
			
			if (c.getItems().addItem(id, quantity)) {
				c.startAnimation(832);
				c.getPA().addSkillXP(xp * SkillHandler.XPRates.THIEVING.getXPRate(), c.getInstance().playerThieving);
				c.getInstance().lastThieve = System.currentTimeMillis();
				c.sendMessage("You steal a " + incendius.game.items.ItemLoader.getItemName(id) + ".");
				c.getPA().checkObjectSpawn(634, x, y, 1, 10);
				Server.getScheduler().schedule(new Task(6) {
					@Override
					public void execute() {
						c.getPA().checkObjectSpawn(i, x, y, 1, 10);
						this.stop();
					}
				});
			}
		} else {
			c.sendMessage("You must have a thieving level of " + level + " to steal from this stall.");
		}
	}

}
