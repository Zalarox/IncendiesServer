package incendius.game.players.content.pickables;

import incendius.event.CycleEvent;
import incendius.event.CycleEventContainer;
import incendius.event.CycleEventHandler;
import incendius.game.players.Player;
import incendius.util.Misc;

public class Flax {

	public static void pickFlax(final Player c, final int x, final int y) {
		c.turnPlayerTo(x, y);
		if (c.getItems().freeSlots() != 0) {
			c.getItems().addItem(1779, 1);
			c.startAnimation(827);
			c.sendMessage("You pick some flax.");
			if (Misc.random(3) == 1) {
				c.getPA().object(-1, x, y, 0, 10);

				CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
					int timer = 1;

					@Override
					public void execute(CycleEventContainer container) {

						if (timer == 0) {
							container.stop();
						}
						timer--;

					}

					@Override
					public void stop() {
						c.getPA().object(2646, x, y, 0, 10);
					}
				}, 300);
			}
		} else {
			c.sendMessage("Not enough space in your inventory.");
			return;
		}

	}
}
