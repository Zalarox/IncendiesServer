package incendius;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import incendius.game.players.Player;

/**
 * A class for handling punishments.
 * 
 * @author Thock321, Tringan, Branon McClellan (KeepBotting)
 *
 */
public class Connection {

	/**
	 * An enumerated type, containing the various types of punishment.
	 *
	 */
	public static enum ConnectionType {
		BAN           (new ArrayList<String>()), 
		IPBAN         (new ArrayList<String>()), 
		MUTE          (new ArrayList<String>()), 
		IPMUTE        (new ArrayList<String>()), 
		IDENTITY_BAN  (new ArrayList<String>()), 
		IDENTITY_MUTE (new ArrayList<String>()), 
		
		FIRST_STARTER (new ArrayList<String>()), 
		SECOND_STARTER(new ArrayList<String>());

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
		connectionFile = new File(Data.DATA_DIRECTORY + (type.toString().contains("STARTER") ? "starters" : "/bans/")
				+ type.toString().toLowerCase() + ".txt");
		String toAdd = (type.toString().contains("IP") || type.toString().contains("STARTER"))
				? player.playerName + ":" + player.connectedFrom
				: type.toString().contains("IDENTITY_BAN")
						? player.playerName + ":" + player.getInstance().identityPunishment : player.playerName;
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
		connectionFile = new File(Data.DATA_DIRECTORY + "/bans/" + type.toString().toLowerCase() + ".txt");
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
	 * Checks if a connection of the specified type exists for the specified
	 * player name.
	 * 
	 * @param toRemove
	 *            The string to remove from the file.
	 * @param type
	 *            The type of connection.
	 */
	public static boolean connectionExists(String player, ConnectionType type) {
		connectionFile = new File(Data.DATA_DIRECTORY + "/bans/" + type.toString().toLowerCase() + ".txt");
		boolean success = false;
		try {

			reader = new BufferedReader(new FileReader(connectionFile));
			String line;
			try {
				
				while ((line = reader.readLine()) != null) {
					if (line.toLowerCase().contains(player.toLowerCase())) {
						success = true;
					}
				}
				
			} finally {
				reader.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return success;
	}

	/**
	 * Loads all punishments.
	 */
	public static void loadConnectionData(boolean starter) {
		File ff = new File(Data.DATA_DIRECTORY + (starter ? "starters" : "/bans/"));
		for (File f : ff.listFiles()) {
			ConnectionType type = ConnectionType.forName(f.getName().replace(".txt", ""));
			if (type == null)
				continue;
			try {
				reader = new BufferedReader(new FileReader(f));
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
					Data.DATA_DIRECTORY + (starter ? "starters" : "/bans/") + type.toString().toLowerCase() + ".txt");
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