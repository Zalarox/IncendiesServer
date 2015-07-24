package main.game.players.content.skills.thieving;

import main.GameEngine;
import main.event.Task;
import main.game.players.Player;
import main.handlers.SkillHandler;
import main.util.Misc;

/**
 * Thieving.java
 *
 * @author Sanity
 *
 **/

public class Thieving {

	private Player c;

	public Thieving(Player c) {
		this.c = c;
	}

	public void stealFromNPC(int id) {
		if (System.currentTimeMillis() - c.getVariables().lastThieve < 2000)
			return;
		for (int j = 0; j < npcThieving.length; j++) {
			if (npcThieving[j][0] == id) {
				if (c.getVariables().playerLevel[c.getVariables().playerThieving] >= npcThieving[j][1]) {
					if (Misc.random(c.getVariables().playerLevel[c.getVariables().playerThieving] + 2
							- npcThieving[j][1]) != 1) {
						c.getPA().addSkillXP(npcThieving[j][2] * SkillHandler.XPRates.THIEVING.getXPRate(),
								c.getVariables().playerThieving);
						c.getItems().addItem(995, npcThieving[j][3]);
						c.startAnimation(881);
						c.getVariables().lastThieve = System.currentTimeMillis();
						c.sendMessage("You thieve some money...");
						break;
					} else {
						c.setHitDiff(npcThieving[j][4]);
						c.setHitUpdateRequired(true);
						c.getVariables().constitution -= npcThieving[j][4] * 10;
						c.getVariables().lastThieve = System.currentTimeMillis() + 2000;
						c.sendMessage("You fail to thieve the NPC.");
						break;
					}
				} else {
					c.sendMessage("You need a thieving level of " + npcThieving[j][1] + " to thieve from this NPC.");
				}
			}
		}
	}

	public void stealFromStall(int id, int xp, int level, final int i, final int x, final int y) {
		c.turnPlayerTo(x, y);
		if (System.currentTimeMillis() - c.getVariables().lastThieve < 2500)
			return;
		if (c.getVariables().playerLevel[c.getVariables().playerThieving] >= level) {
			if (c.getItems().addItem(id, 1)) {
				c.startAnimation(832);
				c.getPA().addSkillXP(xp * SkillHandler.XPRates.THIEVING.getXPRate(), c.getVariables().playerThieving);
				c.getVariables().lastThieve = System.currentTimeMillis();
				c.sendMessage("You steal a " + main.game.items.ItemLoader.getItemName(id) + ".");
				c.getPA().checkObjectSpawn(634, x, y, 1, 10);
				GameEngine.getScheduler().schedule(new Task(6) {
					@Override
					public void execute() {
						c.getPA().checkObjectSpawn(i, x, y, 1, 10);
						this.stop();
					}
				});
			}
		} else {
			c.sendMessage("You must have a thieving level of " + level + " to thieve from this stall.");
		}
	}

	// npc, level, exp, coin amount
	public int[][] npcThieving = { { 1, 1, 8, 200, 1 }, { 18, 25, 26, 500, 1 }, { 9, 40, 47, 1000, 2 },
			{ 26, 55, 85, 1400, 3 }, { 20, 70, 152, 1750, 4 }, { 21, 80, 273, 2000, 5 } };

}
