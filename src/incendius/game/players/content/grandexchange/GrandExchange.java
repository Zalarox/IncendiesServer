package incendius.game.players.content.grandexchange;

import incendius.game.players.Player;

/**
 * 
 * @author Alex(TheLife)
 */

public class GrandExchange {

	/**
	 * Static integers
	 */
	public static int offers = 100000, totalOffers = 0;

	/**
	 * []Integers
	 */
	public int Slots[] = new int[7];
	public int SlotType[] = new int[7];

	/**
	 * Integers
	 */

	public int selectedItemId = 0, selectedAmount = 0, selectedPrice = 0, selectedSlot = 0, itemRecieved = 0,
			itemAmountRecieved = 0, firstItemStacked, secondItemStacked;

	/**
	 * Static booleans
	 */
	public static boolean loading = false;

	/**
	 * Booleans
	 */
	public boolean toHigh = false, recievedMessage = false, stillSearching = false;

	/**
	 * Sellers
	 */

	/**
	 * Buyers
	 */

	/**
	 * Initializing Player c
	 */
	@SuppressWarnings("unused")
	private Player c;

	/**
	 * Setting c
	 */
	public GrandExchange(Player c) {
		this.c = c;
	}

}