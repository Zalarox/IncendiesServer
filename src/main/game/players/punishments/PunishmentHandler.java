package main.game.players.punishments;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import main.Data;
import main.game.players.Player;

/**
 * A class for handling punishments.
 * 
 * @author Branon McClellan (KeepBotting)
 *
 */
public class PunishmentHandler {
	
	/**
	 * Some fields used by PunishmentHandler's core.
	 */
	public static Collection<String> loginLimitExceeded = new ArrayList<String>();
	
	private static File f;
	private static BufferedReader in;
	private static BufferedWriter out;
	
	/**
	 * Some integer constants representing the various punishments.
	 */
	private final static int
	PUNISHMENT_BAN     = 0,
	PUNISHMENT_IPBAN   = 1,
	PUNISHMENT_MUTE    = 2,
	PUNISHMENT_IPMUTE  = 3,
	PUNISHMENT_MACBAN  = 4,
	PUNISHMENT_MACMUTE = 5,
	PUNISHMENT_JAIL    = 6;
	
	/**
	 * Some integer constants representing the various levels of the various
	 * punishments.
	 * 
	 * Name-based punishment (ban, mute, jail)   == level 1
	 * Host-based punishment (IP-ban, IP-mute)   == level 2
	 * MAC-based punishment  (MAC-ban, MAC-mute) == level 3
	 */
	private final static int
	PUNISHMENT_LEVEL_1 = 1,
	PUNISHMENT_LEVEL_2 = 2,
	PUNISHMENT_LEVEL_3 = 3;

	/**
	 * An enumerated type containing information about the various types of
	 * punishments.
	 * 
	 * ArrayLists are used because the server loads data into those lists upon
	 * startup, then uses them to track punishments instead of constant I/O on
	 * the actual punishment files.
	 *
	 * @author Branon McClellan (KeepBotting)
	 */
	private enum Punishments {
		/* <name>, <arraylist>, <data file>, <level> */
		
		BAN    ("ban",      new ArrayList<String>(), Data.PUNISHMENT_BAN,  PUNISHMENT_LEVEL_1), 
		MUTE   ("mute",     new ArrayList<String>(), Data.PUNISHMENT_MUTE, PUNISHMENT_LEVEL_1),
		JAIL   ("jail",     new ArrayList<String>(), Data.PUNISHMENT_JAIL, PUNISHMENT_LEVEL_1),
		
		IPBAN  ("IP-ban",   new ArrayList<String>(), Data.PUNISHMENT_IPBAN,  PUNISHMENT_LEVEL_2), 
		IPMUTE ("IP-mute",  new ArrayList<String>(), Data.PUNISHMENT_IPMUTE, PUNISHMENT_LEVEL_2),
		
		MACBAN ("MAC-ban",  new ArrayList<String>(), Data.PUNISHMENT_MACBAN,  PUNISHMENT_LEVEL_3), 
		MACMUTE("MAC-mute", new ArrayList<String>(), Data.PUNISHMENT_MACMUTE, PUNISHMENT_LEVEL_3);
		;

		private String name;
		private List<String> list;
		private String file;
		private int level;

		private Punishments(String name, List<String> list, String file, int level) {
			this.name  = name;
			this.list  = list;
			this.file  = file;
			this.level = level;
		}
		
		public String getName() {
			return name;
		}

		public List<String> getList() {
			return list;
		}
		
		public String getFile() {
			return file;
		}
		
		public int getLevel() {
			return level;
		}
		
		/**
		 * Determines whether or not the punishment is one of the following:
		 * 
		 * - Ban
		 * - IP-ban
		 * - MAC-ban
		 */
		public boolean isBan() {
			return getName().contains("ban");
		}

		/**
		 * Returns a <Punishments> object based on the given ID.
		 * 
		 * @param id
		 *            The ID of the <Punishments>.
		 *            
		 * @return The <Punishments>.
		 */
		public static Punishments forID(int id) {
			Punishments p = null;
			
			switch (id) {
			case PUNISHMENT_BAN:
				p = Punishments.BAN;
				break;
			case PUNISHMENT_MUTE:
				p = Punishments.MUTE;
				break;
			case PUNISHMENT_IPBAN:
				p = Punishments.IPBAN;
				break;
			case PUNISHMENT_IPMUTE:
				p = Punishments.IPMUTE;
				break;
			case PUNISHMENT_MACBAN:
				p = Punishments.MACBAN;
				break;
			case PUNISHMENT_MACMUTE:
				p = Punishments.MACMUTE;
				break;
			case PUNISHMENT_JAIL:
				p = Punishments.JAIL;
				break;
			}
			return p;
		}
		
		public static Punishments forName(String name) {
			Punishments p = null;
			
			switch (name) {
			case "ban":
				p = Punishments.BAN;
				break;
			case "mute":
				p = Punishments.MUTE;
				break;
			case "ipban":
				p = Punishments.IPBAN;
				break;
			case "ipmute":
				p = Punishments.IPMUTE;
				break;
			case "macban":
				p = Punishments.MACBAN;
				break;
			case "macmute":
				p = Punishments.MACMUTE;
				break;
			case "jail":
				p = Punishments.JAIL;
				break;
			}
			return p;
		}
		
		/**
		 * Returns the data relevant to the specific level of punishment.
		 * 
		 * Ban/mute/jail    (level 1) == player name
		 * IP-ban/IP-mute   (level 2) == player name & player IP
		 * MAC-ban/MAC-mute (level 3) == player name & player MAC
		 */
		public static String getData(Player c, Punishments p) {
			String s = null;
			
			switch (p.getLevel()) {
			case PUNISHMENT_LEVEL_1:
				s = c.getName();
				
			case PUNISHMENT_LEVEL_2:
				s = (c.getName() + ":" + c.getIP());
				
			case PUNISHMENT_LEVEL_3:	
				s = (c.getName() + ":" + c.getMAC());
			}
			
		return s;
		}
	}
	
	public static void load() {
		
		/**
		 * Get the directory of all the punishment files.
		 */
		File directory = new File(Data.PUNISHMENT_DIRECTORY);
		
		/**
		 * Iterate through the files inside the directory and return a
		 * <Punishments> object based on the files' names.
		 */
		for (File file : directory.listFiles()) {
			Punishments p = Punishments.forName(file.getName().replace(".txt", ""));
			String line;
			
			if (p == null)
				continue;
			
			try {
				
				in = new BufferedReader(new FileReader(file));
				
				try {
					
					while ((line = in.readLine()) != null) {
						line = line.trim();
						p.getList().add(line);
					}
					
				} finally {
					in.close();
				}
				
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	/**
	 * Registers the specified punishment for the specified player.
	 * 
	 * @param c
	 *            The player who is being punished.
	 * @param id
	 *            The ID of the punishment to apply.
	 */
	public static void punish(Player c, int id) {
		/**
		 * Determine the relevant punishment.
		 */
		Punishments p = Punishments.forID(id);
		
		System.out.println("Registering a " + p.getName() + " for " + c.getDisplayName() + ".");
		
		/**
		 * Get the relevant file.
		 */
		f = new File(p.getFile());
		
		/**
		 * Retrieve the relevant data.
		 */
		String data = Punishments.getData(c, p);

		/**
		 * Add the relevant data to the relevant list. This allows us to load
		 * new punishments on the fly.
		 */
		p.getList().add(data);
		
		/**
		 * Write the relevant data to the relevant file.
		 */
		try {
			out = new BufferedWriter(new FileWriter(f));
			
			try {
				out.write(data);
			} finally {
				out.newLine();
				out.close();
				System.out.print("Registered a " + p.getName() + " for " + c.getDisplayName() + ".");
			}
			
		} catch (IOException e) {
			System.out.println("Error writing the data for " + c.getDisplayName() + "'s " + p.getName() + ".");
			e.printStackTrace();
			return;
		}
		
		if (p.isBan()) {
			c.disconnect();
			System.out.println(" -- player kicked.");
		}	
	}
	
	/**
	 * Determines if the specified punishment has been applied to the specified player.
	 * 
	 * Level 1 checks player name only.
	 * Level 2 checks player name and IP.
	 * Level 3 checks player name and MAC.
	 * 
	 * @return Whether or not the player should be considered punished.
	 */
	private static boolean isPunished(Player c, int id) {
		Punishments p = Punishments.forID(id);
		ArrayList<String> data = new ArrayList<String>();
		boolean b = false;
		
		/**
		 * The player's name is always needed. Add it no matter what.
		 */
		data.add(c.getName());
		
		switch (p.getLevel()) {
		/**
		 * If we're attempting to determine if a level-1 punishment exists, we
		 * needn't search for anything else. Do nothing.
		 */
		case PUNISHMENT_LEVEL_1:
			break;
		/**
		 * However, for a level-2 punishment, we must take into account the
		 * IP address as well. If either the name or the IP exists, the player is
		 * considered to be punished.
		 */
		case PUNISHMENT_LEVEL_2:
			data.add(c.getIP());
			break;
		/**
		 * Similarly, for a level-3 punishment, the player MAC must also be checked.
		 */
		case PUNISHMENT_LEVEL_3:
			data.add(c.getMAC());
			break;
		}

		try {
			
			/**
			 * Get the file.
			 */
			f = new File(p.getFile());

			try {
				/**
				 * Iterate through the punishment list.
				 */
				for (int i = 0; i < p.getList().size(); i++) {
					/**
					 * Iterate through each check that we have determined is
					 * necessary.
					 */
					for (int j = 0; j < data.size(); j++) {
						/**
						 * If the entry in the list contains any of the data
						 * that we're checking for, then this player should be
						 * considered punished.
						 * 
						 * This works because of the following scenarios:
						 * 
						 * Player <A> attempts to log in and is banned
						 * (level-1). The system reads through the ban file and
						 * checks every entry for Player <A>'s name. If it is
						 * found, Player <A> is not allowed to log in.
						 * 
						 * Player <B> attempts to log in and is IP-banned
						 * (level-2). The system reads through the IP-ban file
						 * and checks every entry for Player <B>'s IP. If it is
						 * found (even under a different player's name), Player
						 * <B> is not allowed to log in.
						 * 
						 * Player <C> attempts to log in and is MAC-banned
						 * (level-3). The system reads through the MAC-ban file
						 * and checks every entry for Player <C>'s MAC. If it is
						 * found (even under a different player's name), Player
						 * <C> is not allowed to log in.
						 * 
						 * These three scenarios work the same way for level 1,
						 * 2, and 3 mutes as well as bans.
						 */
						if (p.getList().get(i).toLowerCase().contains(data.get(j).toLowerCase())) {
							b = true;
							break;
						}
					}
				}

			} finally {
				in.close();
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		return b;
	}
	
	public static boolean isBanned(Player c) {
		return (isPunished(c, PUNISHMENT_BAN) 
			 || isPunished(c, PUNISHMENT_IPBAN) 
			 || isPunished(c, PUNISHMENT_MACBAN));
	}
	
	public static boolean isMuted(Player c) {
		return (isPunished(c, PUNISHMENT_MUTE) 
			 || isPunished(c, PUNISHMENT_IPMUTE)
			 || isPunished(c, PUNISHMENT_MACMUTE));
	}

}
