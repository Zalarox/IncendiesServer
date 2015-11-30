package incendius.game.players.content.skills.prayer;

import incendius.event.CycleEvent;
import incendius.event.CycleEventContainer;
import incendius.event.CycleEventHandler;
import incendius.game.players.Player;
import incendius.handlers.SkillHandler;

/**
 * 
 * @author Tringan
 * 
 */

public class Prayer extends SkillHandler {

	public enum PrayerData {
		BONES(526, 5), BURNT_BONES(528, 5), BAT_BONES(530, 5), PLAYER_BONES(2530, 5), WOLF_BONES(2859,
				5), MONKEY_BONES(3179, 5), MONKEY_BONES_2(3180, 5), MONKEY_BONES_3(3181, 5), MONKEY_BONES_4(3182,
						5), MONKEY_BONES_5(3183, 5), MONKEY_BONES_6(3184, 5), MONKEY_BONES_7(3185, 5), MONKEY_BONES_8(
								3186, 5), MONKEY_BONES_9(3187, 5), BIG_BONES(532, 15), BABY_DRAGON_BONES(534,
										30), DRAGON_BONES(536, 72), SHAIKAHAN_BONES(3123, 25), JOGRE_BONES(3125,
												23), BURNT_JOGRE_BONES(3127, 25), ZOGRE_BONES(4812, 82), FAYGR_BONES(
														4830, 84), RAURG_BONES(4832, 96), OURG_BONES(4834,
																140), DAGANNOTH_BONES(6729, 125), WYVERN_BONES(6812,
																		50), FROST_DRAGON_BONES(18830, 175);

		/**
		 * Constructor for the prayer data
		 * 
		 * @param itemID
		 * @param expGained
		 * @param itemName
		 */
		private PrayerData(final int itemID, final int expGained) {
			this.itemID = itemID;
			this.expGained = expGained;
		}

		/**
		 * Gets the itemID(bones/ashes)
		 * 
		 * @return
		 */
		public int getItemID() {
			return itemID;
		}

		/**
		 * Gets the amount of exp gained per bone/ash
		 * 
		 * @return
		 */
		public int getExpGained() {
			return expGained;
		}

		private int itemID, expGained;

		/**
		 * Gets the ID from the prayer data
		 * 
		 * @param ID
		 * @return
		 */
		public static PrayerData getID(final int ID) {
			for (PrayerData p : values()) {
				if (p.getItemID() == ID) {
					return p;
				}
			}
			return null;
		}

	}

	/**
	 * Used to bury a bone/ash without the altar
	 * 
	 * @param c
	 * @param itemID
	 * @param slot
	 */
	public static void buryBone(final Player c, final int itemID, final int slot) {
		final PrayerData p = PrayerData.getID(itemID);
		if (System.currentTimeMillis() - c.getInstance().buryDelay > 1500) {
			// if (itemID == p.getItemID()/* &&
			// c.getItems().playerHasItem(p.getItemID())*/) {
			c.getInstance().playerSkilling[5] = true;
			c.getItems().deleteItem(itemID, slot, 1);
			c.getPA().addSkillXP(p.getExpGained() * XPRates.PRAYER.getXPRate(), 5);
			c.startAnimation(827);
			c.sendMessage("You bury the bones.");
			c.getInstance().buryDelay = System.currentTimeMillis();
			// }
		}
	}

	/**
	 * Used to bury a bone/ash in the altar(double xp)(Uses Cycle event)
	 * 
	 * @param c
	 * @param itemID
	 * @param altarX
	 * @param altarY
	 */
	public static void buryAltarBone(final Player c, final int itemID, final int altarX, final int altarY,
			final int timesToBury) {
		final PrayerData p = PrayerData.getID(itemID);
		if (p == null)
			return;
		if (itemID == p.getItemID() && c.getItems().playerHasItem(itemID) && c.absX >= altarX - 1
				&& c.absX <= altarX + 2 && c.absY >= altarY - 1 && c.absY <= altarY + 1) {
			c.getInstance().playerSkilling[5] = true;
			placeBonesOnAltar(c, itemID, altarX, altarY, p);
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (!c.getItems().playerHasItem(itemID) || !(c.getInstance().playerSkilling[5])
							|| !(c.absX >= altarX - 1 && c.absX <= altarX + 2 && c.absY >= altarY - 1
									&& c.absY <= altarY + 1))
						container.stop();
					if (c.getInstance().timesBuried == timesToBury)
						container.stop();
					if (c.getInstance().playerSkilling[5])
						placeBonesOnAltar(c, itemID, altarX, altarY, p);
				}

				@Override
				public void stop() {
					c.getInstance().playerSkilling[5] = false;
					c.startAnimation(65535);
					c.getInstance().timesBuried = 0;
				}
			}, 4);
		}
	}

	/**
	 * Places the bones in the altar
	 * 
	 * @param c
	 * @param itemID
	 * @param altarX
	 * @param altarY
	 * @param p
	 */
	public static void placeBonesOnAltar(final Player c, final int itemID, final int altarX, final int altarY,
			PrayerData p) {
		c.turnPlayerTo(altarX, altarY);
		c.getItems().deleteItem(itemID, 1);
		c.getPA().addSkillXP(p.getExpGained() * 2 * XPRates.PRAYER.getXPRate(), 5);
		c.startAnimation(896);
		c.getPA().createPlayersStillGfx(624, altarX, altarY, 0, 0);
		c.sendMessage("The gods are pleased with your offerings.");
		c.getInstance().timesBuried += 1;
		// c.sendMessage("Debug - You buried : "+c.timesBuryed);
	}

	/**
	 * Sends the Chatarea prayer interface
	 * 
	 * @param item
	 * @param c
	 */
	public static void sendPrayerInterface(final Player c, final int item, final int altarX, final int altarY) {
		PrayerData p = PrayerData.getID(item);
		if (item == p.getItemID()) {
			if (!c.getItems().playerHasItem(item))
				return;
			if (c.getItems().playerHasItem(item, 1) && !c.getItems().playerHasItem(item, 2)) {
				buryAltarBone(c, item, altarX, altarY, 1);
				return;
			}
			c.getInstance().prayerItemID = item;
			c.getInstance().altarXCoord = altarX;
			c.getInstance().altarYCoord = altarY;
			c.getPA().sendFrame164(4429);
			c.getPA().sendFrame126("How many would you like to offer?", 2800);
			c.getPA().sendFrame246(1746, 150, p.getItemID());
			c.getPA().sendFrame126(c.getItems().getItemName(p.getItemID()), 2799);
		}
	}

	/**
	 * Executes the button actions in the Chatarea interface
	 * 
	 * @param c
	 * @param buttonID
	 */
	public static void handleButtons(final Player c, int buttonID) {
		if (c.getInstance().playerSkilling[5])
			return;
		if (buttonID == 10239 || buttonID == 10238 || buttonID == 6211)
			c.getPA().removeAllWindows();
		switch (buttonID) {
		case 10239:
			buryAltarBone(c, c.getInstance().prayerItemID, c.getInstance().altarXCoord, c.getInstance().altarYCoord,
					1);
			break;
		case 10238:
			buryAltarBone(c, c.getInstance().prayerItemID, c.getInstance().altarXCoord, c.getInstance().altarYCoord,
					5);
			break;
		case 6211:
			buryAltarBone(c, c.getInstance().prayerItemID, c.getInstance().altarXCoord, c.getInstance().altarYCoord,
					-1);
			break;
		case 6212:
			c.getInstance().PrayX = true;
			c.outStream.createFrame(27);
			break;
		}
	}

}