package main.game.players.content.skills.firemaking;

import main.GameEngine;
import main.event.CycleEvent;
import main.event.CycleEventContainer;
import main.event.CycleEventHandler;
import main.game.objects.Object;
import main.game.players.Player;
import main.game.players.content.skills.firemaking.LogData.logData;
import main.handlers.ItemHandler;
import main.handlers.SkillHandler;
import main.util.Misc;
import main.world.map.Region;

public class Firemaking extends SkillHandler {

	public static boolean Area(Player c, final int x1, final int x2, final int y1, final int y2) {
		return (c.absX >= x1 && c.absX <= x2 && c.absY >= y1 && c.absY <= y2);
	}

	public static boolean inBank(Player c) {
		return Area(c, 3090, 3099, 3487, 3500) || Area(c, 3089, 3090, 3492, 3498) || Area(c, 3248, 3258, 3413, 3428)
				|| Area(c, 3179, 3191, 3432, 3448) || Area(c, 2944, 2948, 3365, 3374) || Area(c, 2942, 2948, 3367, 3374)
				|| Area(c, 2944, 2950, 3365, 3370) || Area(c, 3008, 3019, 3352, 3359) || Area(c, 3017, 3022, 3352, 3357)
				|| Area(c, 3203, 3213, 3200, 3237) || Area(c, 3212, 3215, 3200, 3235) || Area(c, 3215, 3220, 3202, 3235)
				|| Area(c, 3220, 3227, 3202, 3229) || Area(c, 3227, 3230, 3208, 3226) || Area(c, 3226, 3228, 3230, 3211)
				|| Area(c, 3227, 3229, 3208, 3226);
	}

	public static void attemptFire(final Player c, final int itemUsed, final int usedWith, final int x, final int y,
			final boolean groundObject) {
		if (!c.getItems().playerHasItem(590) && !c.getItems().playerHasItem(17678)) {
			c.sendMessage("You need a tinderbox to light a fire.");
			return;
		}
		if (c.playerIsFiremaking)
			return;
		for (final logData l : logData.values()) {
			final int logId = (usedWith == 590 || usedWith == 17678) ? itemUsed : usedWith;
			if (logId == l.getLogId()) {
				if (c.playerLevel[11] < l.getLevel()) {
					c.sendMessage("You need a firemaking level of " + l.getLevel() + " to light "
							+ c.getItems().getItemName(logId));
					return;
				}
				if (inBank(c)) {
					c.sendMessage("You cannot light a fire in a bank.");
					return;
				}
				if (GameEngine.objectManager.objectExists(c.absX, c.absY)) {
					c.sendMessage("You cannot light a fire here.");
					return;
				}
				final boolean notInstant = (System.currentTimeMillis() - c.lastSkillingAction) > 2500;
				int cycle = 2;
				if (notInstant) {
					c.sendMessage("You attempt to light a fire.");
					if (groundObject == false) {
						c.getItems().deleteItem(logId, c.getItems().getItemSlot(logId), 1);
						ItemHandler.createGroundItem(c, logId, c.absX, c.absY, c.heightLevel, 1, c.playerId);
						c.startAnimation(733);
					}
					cycle = 3 + Misc.random(6);
				} else {
					if (groundObject == false) {
						c.getItems().deleteItem(logId, c.getItems().getItemSlot(logId), 1);
						ItemHandler.createGroundItem(c, logId, c.absX, c.absY, c.heightLevel, 1, c.playerId);
					}
				}
				final boolean walk;
				if (Region.getClipping((x - 1), y, c.heightLevel, -1, 0)) {
					walk = true;
				} else {
					walk = false;
				}
				c.getPA().walkTo(walk == true ? -1 : 1, 0);
				c.playerIsFiremaking = true;
				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (c.playerIsFiremaking
								&& ItemHandler.removeGroundItem(c, logId, x, c.absY, false, Region.getRegion(x, y))) {
							new Object(2732, x, y, c.heightLevel, 0, 10, -1, 60 + Misc.random(30));
							c.sendMessage("The fire catches and the log beings to burn.");
							CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
								@Override
								public void execute(CycleEventContainer container) {
									c.turnPlayerTo(walk == true ? (x + 1) : (x - 1), y);
									container.stop();
								}

								@Override
								public void stop() {
									c.turnPlayerTo(walk == true ? (x + 1) : (x - 1), y);
								}
							}, 2);
							c.getPA().addSkillXP((int) (l.getXp()), 11);
							c.turnPlayerTo(walk == true ? (x + 1) : (x - 1), y);
							container.stop();
						} else {
							container.stop();
							return;
						}
					}

					@Override
					public void stop() {
						c.playerIsFiremaking = false;
						c.startAnimation(65535);
						c.lastSkillingAction = System.currentTimeMillis();
					}
				}, cycle);
			}
		}
	}
}