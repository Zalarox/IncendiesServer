package main.game.players;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import main.Data;
import main.game.players.packets.ClickingButtons;


/**
 * A class containing methods for logging various portions of a player's game.
 * @author Branon McClellan (KeepBotting)
 *
 */

public class Logging {
	
	private Player c;

	public Logging(Player Player) {
		this.c = Player;
	}
	
	/**
	 * Used for logging dropped or picked-up items.
	 * 
	 * @param id
	 *            The ID of the dropped item.
	 * @param amount
	 *            The amount of the dropped item.
	 * @param x
	 *            Where the item was dropped (X).
	 * @param y
	 *            Where the item was dropped (Y).
	 */
	public void logDrop(int id, int amount, int x, int y, boolean drop) {
		try {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy 'at' HH:mm:ss");
			Date d = new Date();
			BufferedWriter log = new BufferedWriter(new FileWriter(Data.DROP_LOG, true));
			try {
				log.newLine();
				
				if (drop) {
				log.write("On " + df.format(d) + ", " + c.getDisplayName() + " dropped " + amount + "x "
						+ c.getItems().getItemName(id) + " at (" + x + ", " + y + ")");
				} else {
				log.write("On " + df.format(d) + ", " + c.getDisplayName() + " picked up " + amount + "x "
						+ c.getItems().getItemName(id) + " at (" + x + ", " + y + ")");
				}
			} finally {
				log.close();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Used for logging accepted trades.
	 * 
	 * @param player1
	 *            One of the players trading.
	 * @param player2
	 *            The other player trading.
	 */
	public void logTrade(String player1, String player2) {
		try {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy 'at' HH:mm:ss");
			Date d = new Date();
			BufferedWriter log = new BufferedWriter(new FileWriter(Data.TRADE_LOG, true));
			try {
				log.write("On " + df.format(d) + ", " + player1 + " and " + player2 + " traded with each other.");

				log.newLine();
				log.newLine();

				log.write(player1 + "'s offer was: ");
				for (int i = 0; i < ClickingButtons.ids1.size(); i++) {
					log.write(ClickingButtons.amounts1.get(i) + "x "
							+ c.getItems().getItemName(ClickingButtons.ids1.get(i)) + ", ");
				}

				log.newLine();
				log.newLine();

				log.write(player2 + "'s offer was: ");
				for (int i = 0; i < ClickingButtons.ids2.size(); i++) {
					log.write(ClickingButtons.amounts2.get(i) + "x "
							+ c.getItems().getItemName(ClickingButtons.ids2.get(i)) + ", ");

				}

				log.newLine();
			} finally {
				log.newLine();
				log.newLine();
				log.newLine();
				log.close();

				ClickingButtons.ids1.clear();
				ClickingButtons.ids2.clear();
				ClickingButtons.amounts1.clear();
				ClickingButtons.amounts2.clear();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
