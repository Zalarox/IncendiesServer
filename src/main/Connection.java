package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import main.game.players.Player;

/**
 * A class for handling punishments and connections.
 * 
 * @author Thock321
 * @credits Tringan
 *
 */
public class Connection {

	/**
	 * An enum containing the types of punishments.
	 * 
	 * @author Thock321
	 *
	 */
	public static enum ConnectionType {
		BAN(new ArrayList<String>()), IPBAN(new ArrayList<String>()), MUTE(new ArrayList<String>()), IPMUTE(
				new ArrayList<String>()), IDENTITY_BAN((new ArrayList<String>())), IDENTITY_MUTE(
						(new ArrayList<String>())), FIRST_STARTER(new ArrayList<String>()), SECOND_STARTER(
								new ArrayList<String>());

		/**
		 * The list that contains punishment data.
		 */
		private List<String> list;

		/**
		 * Creates a new punishment type.
		 * 
		 * @param list
		 *            A new list to contain punishment data.
		 */
		private ConnectionType(List<String> list) {
			this.list = list;
		}

		/**
		 * Gets the list containing punishment data.
		 * 
		 * @return The list.
		 */
		public List<String> getList() {
			return list;
		}

		/**
		 * Gets a punishment type for it's name.
		 * 
		 * @param name
		 *            The name.
		 * @return The punishment type.
		 */
		public static ConnectionType forName(String name) {
			for (ConnectionType pt : ConnectionType.values()) {
				if (name.equalsIgnoreCase(pt.toString()))
					return pt;
			}
			return null;
		}
	}

	public static Collection<String> loginLimitExceeded = new ArrayList<String>();
	private static String FilesFolder = "./Data/";
	private static File _FilesFolder = new File(FilesFolder);
	private static File connectionFile;
	private static BufferedReader reader;
	private static BufferedWriter writer;

	/**
	 * Adds a connection type based on player.
	 * 
	 * @param player
	 *            The player.
	 * @param type
	 *            The type of connection
	 */
	public static void addConnection(Player player, ConnectionType type) {
		connectionFile = new File(FilesFolder + (type.toString().contains("STARTER") ? "starters" : "/bans/")
				+ type.toString().toLowerCase() + ".txt");
		String toAdd = (type.toString().contains("IP") || type.toString().contains("STARTER"))
				? player.playerName + ":" + player.connectedFrom
				: type.toString().contains("IDENTITY_BAN")
						? player.playerName + ":" + player.getVariables().identityPunishment : player.playerName;
		type.getList().add(toAdd);
		try {
			writer = new BufferedWriter(new FileWriter(connectionFile));
			try {
				writer.newLine();
				writer.write(toAdd);
			} finally {
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes a connection.
	 * 
	 * @param toRemove
	 *            The string to remove from the file.
	 * @param type
	 *            The type of connection.
	 */
	public static void removeConnection(String toRemove, ConnectionType type) {
		connectionFile = new File(FilesFolder + "/bans/" + type.toString().toLowerCase() + ".txt");
		type.getList().remove(toRemove);
		try {
			writer = new BufferedWriter(new FileWriter(connectionFile));
			reader = new BufferedReader(new FileReader(connectionFile));
			String line;
			try {
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (!line.equalsIgnoreCase(toRemove)) {
						writer.write(line);
					}
				}
			} finally {
				writer.close();
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads all punishments.
	 */
	public static void loadConnectionData(boolean starter) {
		_FilesFolder = new File(FilesFolder + (starter ? "starters" : "/bans/"));
		for (File file : _FilesFolder.listFiles()) {
			ConnectionType type = ConnectionType.forName(file.getName().replace(".txt", ""));
			if (type == null)
				continue;
			try {
				reader = new BufferedReader(new FileReader(file));
				String line;
				try {
					while ((line = reader.readLine()) != null) {
						line = line.trim();
						type.getList().add(line);
					}
				} finally {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Checks if a player is registered for a certain connetion type.
	 * 
	 * @param playerName
	 *            The player.
	 * @param type
	 *            The connetion type.
	 * @return <code>true</code> if the player is punished for the type,
	 *         otherwise <code>false</code>.
	 */
	public static boolean containsConnection(String playerName, ConnectionType type, boolean starter) {
		try {
			connectionFile = new File(
					FilesFolder + (starter ? "starters" : "/bans/") + type.toString().toLowerCase() + ".txt");
			reader = new BufferedReader(new FileReader(connectionFile));
			try {
				String line;
				while ((line = reader.readLine()) != null) {
					if (line.toLowerCase().contains(playerName.toLowerCase()))
						return true;
				}
			} finally {
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Checks if an ip has reached its login limit.
	 * 
	 * @param IP
	 *            The ip.
	 * @return <code>true</code> if the ip has reached its login limit,
	 *         otherwise <code>false</code>.
	 */
	public static boolean checkLoginList(String IP) {
		loginLimitExceeded.add(IP);
		int num = 0;
		for (String ips : loginLimitExceeded) {
			if (IP.equals(ips)) {
				num++;
			}
		}
		return num > 5;
	}
}