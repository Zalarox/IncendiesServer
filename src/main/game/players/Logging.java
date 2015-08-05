package main.game.players;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import main.Data;
import main.game.players.packets.ClickingButtons;

/**
 * A class containing methods for logging various portions of a player's game.
 * 
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
	 * @param drop
	 *            True if we're logging a dropped item. False if we're logging a
	 *            picked-up item.
	 */
	public void logDrop(int id, int amount, int x, int y, boolean drop) {
		try {

			DateFormat df = new SimpleDateFormat("'On' MM/dd/yyyy 'at' HH:mm:ss");
			Date d = new Date();
			BufferedWriter log = new BufferedWriter(new FileWriter(Data.DROP_LOG, true));

			try {

				if (drop) {
					log.write(df.format(d) + ", " + c.getDisplayName() + " dropped   " + amount + "x "
							+ c.getItems().getItemName(id) + " at (" + x + ", " + y + ")");
				} else {
					log.write(df.format(d) + ", " + c.getDisplayName() + " picked up " + amount + "x "
							+ c.getItems().getItemName(id) + " at (" + x + ", " + y + ")");
				}

			} finally {
				log.newLine();
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
	 *            The name of a player trading.
	 * @param player2
	 *            The name of a player trading.
	 */
	public void logTrade(String player1, String player2) {
		try {

			DateFormat df = new SimpleDateFormat("'On' MM/dd/yyyy 'at' HH:mm:ss");
			Date d = new Date();
			BufferedWriter log = new BufferedWriter(new FileWriter(Data.TRADE_LOG, true));

			try {
				log.write(df.format(d) + ", " + player1 + " and " + player2 + " traded with each other.");

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

	private long sessionStart;
	private long sessionLength;

	/**
	 * Logs a player's game session start, end, and length.
	 *
	 * @param logIn
	 *            Whether we're logging a player who is logging in, or a player
	 *            who is logging out.
	 */
	public void logSession(boolean logIn) {
		try {

			DateFormat df = new SimpleDateFormat("'On' MM/dd/yyyy 'at' HH:mm:ss");
			Date d = new Date();
			BufferedWriter log = new BufferedWriter(new FileWriter(Data.SESSION_LOG, true));

			try {

				if (logIn) {
					log.write(df.format(d) + ", " + c.getDisplayName() + " initated a game session.");
					c.getLogging().sessionStart = System.currentTimeMillis();
				} else {
					c.getLogging().sessionLength = System.currentTimeMillis() - c.getLogging().sessionStart;

					String sessionLengthFinal = String.format("%02d:%02d:%02d",
							TimeUnit.MILLISECONDS.toHours(c.getLogging().sessionLength),
							TimeUnit.MILLISECONDS.toMinutes(c.getLogging().sessionLength) - TimeUnit.HOURS
							.toMinutes(TimeUnit.MILLISECONDS.toHours(c.getLogging().sessionLength)),
							TimeUnit.MILLISECONDS.toSeconds(c.getLogging().sessionLength) - TimeUnit.MINUTES
							.toSeconds(TimeUnit.MILLISECONDS.toMinutes(c.getLogging().sessionLength)));

					log.write(df.format(d) + ", " + c.getDisplayName() + " ended their game session (length: "
							+ sessionLengthFinal + ").");
				}

			} finally {
				log.newLine();
				log.close();
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Logging chat is interesting for two reasons:
	 * 
	 * 1) Players talk a lot
	 * 
	 * 2) Players never shut the fuck up
	 * 
	 * If the server is logging all player chat at all times, the size of the
	 * chatlog can quickly become unacceptably large. This will waste disk space
	 * & also make opening/reading the chatlog a horrifying ordeal. That's a
	 * TODO -- implement a system to purge older logs once the chatlog file
	 * reaches a specified number of lines (5,000 seems like a good baseline).
	 * 
	 * In addition, the logging methods generally create a new BufferedWriter
	 * every time they are called. This is okay for operations like trading, or
	 * dueling which take time to execute, but chat is an action that is
	 * completed instantaneously, and can be repeated equally instantaneously.
	 * 
	 * This means that if a player decides to spam very quickly by using the
	 * technique of pressing a letter key, then instantly pressing <Enter>, the
	 * server will be forced to open & close the BufferedWriter a large amount
	 * of times in very quick succession, wasting cycle time.
	 * 
	 * This is not good -- during my test run, it was possible for a single
	 * player to push the server to 143% engine load (947ms cycle time) by
	 * spamming using the aforementioned technique. Manually, no autotyper.
	 * 
	 * The found solution was as follows: aggregate players' sent chat messages
	 * into an ArrayList, then write the ArrayList to the log file only when it
	 * reaches a specified size (100 lines). The cycle time needed to perform
	 * either of those operations is minimal.
	 */

	private static ArrayList<String> chatLog = new ArrayList<String>();

	/**
	 * Logs a player's chat messages.
	 *
	 * @param text
	 *            The text being logged.
	 */

	public void logChat(String text) {
		try {

			DateFormat df = new SimpleDateFormat("'On' MM/dd/yyyy 'at' HH:mm:ss");
			Date d = new Date();

			chatLog.add(df.format(d) + ", " + c.getDisplayName() + " said: " + text + "");

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			writeChatLog();
		}
	}

	/**
	 * Writes the logged chat messages to the logfile.
	 */
	private void writeChatLog() {
		if (chatLog.size() < 100) {
			return;
		}

		try {

			BufferedWriter log = new BufferedWriter(new FileWriter(Data.CHAT_LOG, true));

			try {

				for (int i = 0; i < chatLog.size(); i++) {
					log.write(chatLog.get(i));
					log.newLine();
				}

			} finally {
				log.close();
				chatLog.clear();
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Logs a private message (unethicality ftw?)
	 * 
	 * @param to
	 *            The player who was receiving the private message.
	 * @param message
	 *            The private message.
	 */
	public void logPM(String to, String message) {
		try {

			DateFormat df = new SimpleDateFormat("'On' MM/dd/yyyy 'at' HH:mm:ss");
			Date d = new Date();
			BufferedWriter log = new BufferedWriter(new FileWriter(Data.PM_LOG, true));

			try {
				
					log.write(df.format(d) + ", [From: " + c.getDisplayName() + "] [To: " + to + "] [Message: " + message + "]");
					
			} finally {
				log.newLine();
				log.close();
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
