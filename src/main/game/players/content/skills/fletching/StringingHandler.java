package main.game.players.content.skills.fletching;

import main.event.CycleEvent;
import main.event.CycleEventContainer;
import main.event.CycleEventHandler;
import main.game.players.Player;
import main.handlers.SkillHandler;

public class StringingHandler {

	/**
	 * @author Scott | Ochroid
	 */

	/**
	 * Variable Declarations
	 */

	private final static int STRING = 1777;

	/**
	 * Configures All Data For Strining Bows
	 * 
	 * @author Scott
	 */

	private static enum Stringing {

		SHORTBOW(50, 841, 1, 5, false), LONGBOW(48, 839, 10, 10, false), OAK_SHORTBOW(54, 843, 20, 17,
				false), OAK_LONGBOW(56, 845, 25, 25, false), WILLOW_SHORTBOW(60, 849, 35, 33, false), WILLOW_LONGBOW(58,
						847, 40, 42, false), MAPLE_SHORTBOW(64, 853, 50, 50, false), MAPLE_LONGBOW(62, 851, 55, 58,
								false), YEW_SHORTBOW(68, 857, 65, 67, false), YEW_LONGBOW(66, 855, 70, 75,
										false), MAGIC_SHORTBOW(72, 861, 80, 83, false), MAGIC_LONGBOW(70, 859, 85, 92,
												false), BRONZE_CB(9454, 9174, 9, 6, true), IRON_CB(9457, 9177, 39, 22,
														true), STEEL_CB(9459, 9179, 46, 27, true), MITH_CB(9461, 9181,
																54, 32, true), ADDY_CB(9463, 9183, 54, 41,
																		true), RUNE_CB(9465, 9185, 69, 50, true);

		private int bowID, newBow, req, XP;
		private boolean isC1;

		private Stringing(final int bowID, final int newBow, final int req, final int XP, final boolean isC1) {
			this.bowID = bowID;
			this.newBow = newBow;
			this.req = req;
			this.XP = XP;
			this.isC1 = isC1;
		}

		private int getBowID() {
			return bowID;
		}

		private int getNewBow() {
			return newBow;
		}

		private int getReq() {
			return req;
		}

		private int getXP() {
			return XP * 20;
		}

		private static Stringing getID(final int ID) {
			for (Stringing s : Stringing.values()) {
				if (s.getBowID() == ID) {
					return s;
				}
			}
			return null;
		}
	}

	static String getLine(Player c) {
		return ("\\n\\n\\n\\n");
	}

	/**
	 * Handles the actual stringing
	 * 
	 * @param c
	 *            Client
	 */

	public static void useItemInterface(final Player c, final int useWith, final int itemUsed) {
		for (Stringing s : Stringing.values()) {
			if (s == null) {
				return;
			}
			if (useWith == STRING && itemUsed == s.getBowID() || useWith == s.getBowID() && itemUsed == STRING) {
				handleInterface(c, s.getBowID());
			}
		}
		for (ArrowHandler.Arrow a : ArrowHandler.Arrow.values()) {
			if (useWith == a.getFirstItem() && itemUsed == a.getSecondItem()
					|| useWith == a.getSecondItem() && itemUsed == a.getFirstItem()) {
				ArrowHandler.handleInterface1(c, a.getSecondItem());
			}
		}
	}

	private static void handleInterface(final Player c, final int bowID) {
		if (c.getInstance().playerSkilling[c.getInstance().playerFletching] == true) {
			return;
		}
		Stringing s = Stringing.getID(bowID);
		c.getPA().sendFrame164(4429);
		c.getInstance().isOnInterface = true;
		c.getInstance().isStringing = true;
		c.getInstance().stringu = s.getBowID();
		boolean view190 = true;
		c.getPA().sendFrame246(1746, view190 ? 140 : 140, s.getNewBow());
		c.getPA().sendFrame126(getLine(c) + "" + c.getItems().getItemName(s.getNewBow()) + "", 2799);
	}

	public static void stringBow(final Player c, final int item, final int amount) {
		final Stringing s = Stringing.getID(item);
		c.getPA().closeAllWindows();
		if (System.currentTimeMillis() - c.getInstance().lastThieve < 150) {
			return;
		}
		if (c.getInstance().playerSkilling[c.getInstance().playerFletching] == true) {
			return;
		}
		c.getInstance().lastThieve = System.currentTimeMillis();
		if (c.getInstance().playerLevel[c.getInstance().playerFletching] < s.getReq()) {
			c.sendMessage("You must have a fletching level of at least " + s.getReq() + " to string this bow.");
			return;
		}
		if (s.isC1) {
			c.startAnimation(6675);
		} else {
			c.startAnimation(6679);
		}
		c.getInstance().doAmount = amount;
		c.getInstance().isStringing = true;
		c.getInstance().playerSkilling[c.getInstance().playerFletching] = true;
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (c.getInstance().doAmount == 0) {
					container.stop();
					return;
				}
				if (c.getInstance().playerSkilling[c.getInstance().playerFletching] == false) {
					container.stop();
					return;
				}
				if (!c.getItems().playerHasItem(STRING) || !c.getItems().playerHasItem(s.getBowID())) {
					c.sendMessage("You do not have the correct supplies to fletch this.");
					container.stop();
					return;
				}
				if (s.isC1) {
					c.startAnimation(6675);
				} else {
					c.startAnimation(6679);
				}
				c.getItems().deleteItem(s.getBowID(), 1);
				c.getItems().deleteItem(STRING, 1);
				c.getItems().addItem(s.getNewBow(), 1);
				c.getPA().addSkillXP(s.getXP() * SkillHandler.XPRates.FLETCHING.getXPRate(),
						c.getInstance().playerFletching);
				c.getInstance().doAmount--;
			}

			@Override
			public void stop() {
				c.getPA().closeAllWindows();
				c.startAnimation(c.getInstance().playerStandIndex);
				c.getInstance().doAmount = 0;
				c.getInstance().isStringing = false;
				c.getInstance().isOnInterface = false;
				c.getInstance().playerSkilling[c.getInstance().playerFletching] = false;
			}
		}, 2);
	}

}