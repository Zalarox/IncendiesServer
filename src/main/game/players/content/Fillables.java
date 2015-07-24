package main.game.players.content;

import main.event.CycleEvent;
import main.event.CycleEventContainer;
import main.event.CycleEventHandler;
import main.game.players.Player;

/**
 * Class Fillables Handles Filling water in unfilled item's
 */

public class Fillables {
	private static int EMOTE = 832;
	private static boolean fillingWater = false;

	public static void fillTheItem(final Player c, final int itemId, final int objectId) {
		c.getVariables().walkFromFilling = false;
		if (fillingWater) {
			return;
		}
		fillingWater = true;
		CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!fillingWater)
					container.stop();
				if (c.getVariables().walkFromFilling) {
					container.stop();
				}
				if (Fillables.canFill(itemId, objectId) && c.getItems().playerHasItem(itemId)) {
					c.getItems().deleteItem(itemId, 1);
					c.getItems().addItem(counterpart(itemId), 1);
					c.sendMessage(fillMessage(itemId, objectId));
					c.startAnimation(EMOTE);
					return;
				}
			}

			@Override
			public void stop() {
				fillingWater = false;
			}
		}, 1);
		CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
			@SuppressWarnings("unused")
			@Override
			public void execute(CycleEventContainer container) {
				if (!fillingWater) {
					container.stop();
				}
				if (c.getVariables().walkFromFilling) {
					container.stop();
				}
				if (c.getVariables().walkFromFilling = false && Fillables.canFill(itemId, objectId)
						&& c.getItems().playerHasItem(itemId)) {
					fillTheItem(c, itemId, objectId);
					return;
				}
			}

			@Override
			public void stop() {
			}
		}, 2);
	}

	private static boolean canFill(int id, int oid) {
		return counterpart(id) != -1 && !(getObjectName(oid).equals("Error"));
	}

	private static String fillMessage(int id, int oid) {
		return "You fill the " + getItemName(id) + " from the " + getObjectName(oid) + ".";
	}

	private static int counterpart(int id) {
		switch (id) {
		case 1925: // bucket
			return 1929;
		case 1935: // jug
			return 1937;
		case 229: // vial
			return 227;
		case 1923: // bowl
			return 1921;
		case 1980: // cup
			return 4458;
		case 5331: // watering can
		case 5333:
		case 5334:
		case 5335:
		case 5336:
		case 5337:
		case 5338:
		case 5339:
			return 5340;
		case 1831: // waterskin
		case 1825:
		case 1827:
		case 1829:
			return 1823;
		case 6667:
			return 6668;
		}
		return -1;
	}

	private static String getItemName(int id) {
		switch (id) {
		case 1925:
			return "bucket";
		case 1935:
			return "jug";
		case 229:
			return "vial";
		case 1923:
			return "bowl";
		case 1980:
			return "cup";
		case 5331:
		case 5333:
		case 5334:
		case 5335:
		case 5336:
		case 5337:
		case 5338:
		case 5339:
			return "watering can";
		case 1831:
		case 1825:
		case 1827:
		case 1829:
			return "waterskin";
		case 6667:
			return "fishbowl";
		}
		return "There was a problem with your current action, please report this.";
	}

	private static String getObjectName(int id) {
		switch (id) {
		case 873:
		case 874:
			return "sink";
		case 11661:
			return "waterpump";
		case 879:
		case 11759:
			return "fountain";
		}
		return "Error";
	}
}