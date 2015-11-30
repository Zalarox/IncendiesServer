package incendius.game.players.content.minigames.impl.barrows;

import incendius.game.players.Player;

/**
 * 
 * @author IQuality and Tringan
 *
 */
public class BarrowsConstants {

	/**
	 * The brothers data(npc ids)
	 */
	public final static int[] brotherData = { 2025, 2026, 2027, 2028, 2029, 2030 };

	/**
	 * Every location needed
	 * 
	 * @param p
	 * @return
	 */

	public final static boolean inAhrimsMount(Player p) {
		return p.absX >= 3562 && p.absX <= 3569 && p.absY >= 3285 && p.absY <= 3294;
	}

	public final static boolean inBarrows(Player p) {
		if (p.absX >= 3524 && p.absX <= 3590 && p.absY >= 9665 && p.absY <= 9725)
			return true;
		return false;
	}

	public final static boolean inDhMount(Player p) {
		return p.absX >= 3570 && p.absX <= 3580 && p.absY >= 3293 && p.absY <= 3300;
	}

	public final static boolean inGuthansMount(Player p) {
		return p.absX >= 3574 && p.absX <= 3581 && p.absY >= 3278 && p.absY <= 3287;
	}

	public final static boolean inKarilsMount(Player p) {
		return p.absX >= 3562 && p.absX <= 3569 && p.absY >= 3273 && p.absY <= 3280;
	}

	public final static boolean inMount(Player p) {
		if (inVeracsMount(p) || inToragsMount(p) || inAhrimsMount(p) || inDhMount(p) || inGuthansMount(p)
				|| inKarilsMount(p))
			return true;
		return false;
	}

	public final static boolean inToragsMount(Player p) {
		return p.absX >= 3550 && p.absX <= 3557 && p.absY >= 3279 && p.absY <= 3287;
	}

	public final static boolean inVeracsMount(Player p) {
		return p.absX >= 3552 && p.absX <= 3560 && p.absY >= 3294 && p.absY <= 3301;
	}

	/**
	 * The chance of getting an item
	 */
	public final static int FIRST_BARROW_CHANCE = 7;

	/**
	 * The chance of getting a second item
	 */
	public final static int SECOND_BARROW_CHANCE = 14;

	/**
	 * Items obtained regularly
	 */
	public final static int[][] REGULAR_ITEMS = { { 558, 1795 }, { 562, 773 }, { 560, 391 }, { 565, 164 },
			{ 995, 4162 }, { 4740, 188 } };

	/**
	 * The rare items
	 */
	public static int[] BARROW_ITEMS = { 985, 987, 1149, 4708, 4710, 4712, 4714, 4716, 4718, 4720, 4722, 4724, 4726,
			4728, 4730, 4732, 4734, 4836, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759 };

	/**
	 * The seconds between each rock falls
	 */
	public final static int ROCK_TIMER = 20;

	/**
	 * If it requires to move to the chest with doors or not
	 */
	public final static boolean DOORS_REQUIRED = false;
}
