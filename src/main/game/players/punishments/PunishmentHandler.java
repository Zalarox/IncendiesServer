package main.game.players.punishments;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import main.Data;
import main.game.players.Player;
import main.util.Misc;

/**
 * Handles punishments.
 * 
 * @author Branon McClellan (KeepBotting)
 *
 */
public class PunishmentHandler {
	
	public static boolean hasLoaded = false;
	
	private static File f;
	private static BufferedReader in;
	private static BufferedWriter out;
	
	public static Collection<String> ipList = new ArrayList<String>();
	
	/**
	 * Some integer constants representing the various punishments.
	 */
	public final static int
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
	 * UUID-based punishment (MAC-ban, MAC-mute) == level 3
	 */
	private final static int
	PUNISHMENT_LEVEL_1 = 1,
	PUNISHMENT_LEVEL_2 = 2,
	PUNISHMENT_LEVEL_3 = 3;
	
	/**
	 * Used to add "cushion" characters to player data.
	 * 
	 * Colons (:) are used throughout this class as "cushion" characters.
	 * 
	 * This prevents the system from exhibiting such undesirable behavior as: 
	 * - equating the banned player "Bob" with the unbanned player "Bob123"
	 * - equating the banned IP "123.456" with the unbanned IP "123.456.789"
	 * 
	 * This allows us to use contains() on our strings without fear of mistaking
	 * one set of data for another.
	 */
	private static String cushion(String s) {
		return (":" + s + ":");
	}
	
	private static String cushion(String s, String ss) {
		return (":" + s + ":" + ss + ":");
	}
	
	/**
	 * An enumerated type containing information about the various types of
	 * punishments.
	 * 
	 * ArrayLists are used because the server loads data into those lists upon
	 * startup, then uses them to track punishments instead of constant I/O on
	 * multiple punishment files.
	 *
	 * @author Branon McClellan (KeepBotting)
	 */
	private enum Punishments {
		/* <name>, <id>, <arraylist>, <data file>, <level> */
		
		BAN    ("ban",  PUNISHMENT_BAN,  new ArrayList<String>(), Data.PUNISHMENT_BAN,  PUNISHMENT_LEVEL_1), 
		MUTE   ("mute", PUNISHMENT_MUTE, new ArrayList<String>(), Data.PUNISHMENT_MUTE, PUNISHMENT_LEVEL_1),
		JAIL   ("jail", PUNISHMENT_JAIL, new ArrayList<String>(), Data.PUNISHMENT_JAIL, PUNISHMENT_LEVEL_1),
		
		IPBAN  ("ipban",  PUNISHMENT_IPBAN,  new ArrayList<String>(), Data.PUNISHMENT_IPBAN,  PUNISHMENT_LEVEL_2), 
		IPMUTE ("ipmute", PUNISHMENT_IPMUTE, new ArrayList<String>(), Data.PUNISHMENT_IPMUTE, PUNISHMENT_LEVEL_2),
		
		MACBAN ("macban",  PUNISHMENT_MACBAN,  new ArrayList<String>(), Data.PUNISHMENT_MACBAN,  PUNISHMENT_LEVEL_3), 
		MACMUTE("macmute", PUNISHMENT_MACMUTE, new ArrayList<String>(), Data.PUNISHMENT_MACMUTE, PUNISHMENT_LEVEL_3);
		;

		private String name;
		private int id;
		private List<String> list;
		private String file;
		private int level;

		private Punishments(String name, int id, List<String> list, String file, int level) {
			this.name  = name;
			this.id    = id;
			this.list  = list;
			this.file  = file;
			this.level = level;
		}
		
		public String getName() {
			return name;
		}
		
		public int getID() {
			return id;
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
		 * - ban
		 * - ipban
		 * - macban
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
			
			for (Punishments pp : Punishments.values()) {
				if (pp.getID() == id) {
					p = pp;
					break;
				}
			}
			return p;
		}
		
		/**
		 * Returns a <Punishments> object based on the given name.
		 * 
		 * @param name
		 *            The name of the <Punishments>.
		 *            
		 * @return The <Punishments>.
		 */
		public static Punishments forName(String name) {	
			Punishments p = null;
			
			for (Punishments pp : Punishments.values()) {
				if (pp.getName().equalsIgnoreCase(name)) {
					p = pp;
					break;
				}
			}
			return p;
		}
		
		/**
		 * Returns player information relevant to the specific level of punishment.
		 * 
		 * Ban/mute/jail    (level 1) == player name
		 * IP-ban/IP-mute   (level 2) == player name & player IP
		 * MAC-ban/MAC-mute (level 3) == player name & player MAC
		 */
		public static String getData(Player c, Punishments p) {
			String s = null;
			
			switch (p.getLevel()) {
			case PUNISHMENT_LEVEL_1:
				s = cushion(c.getName());
				break;
			case PUNISHMENT_LEVEL_2:
				s = cushion(c.getName(), c.getIP());
				break;
			case PUNISHMENT_LEVEL_3:	
				s = cushion(c.getName(), c.getMAC());
				break;
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
			Punishments p = Punishments.forName(Misc.trimFileExtension(file.getName()));
			String line;
			
			if (p == null)
				continue;
			
			try {
				/**
				 * Initialize the BufferedReader using the current file.
				 */
				in = new BufferedReader(new FileReader(file));
				
				try {
					/**
					 * Add all non-null lines to the relevant list.
					 */
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
		hasLoaded = true;
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
		
		if (!hasLoaded) {
			load();
		}
		
		if (isPunished(c, id)) {
			return;
		}
		
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
		 * Add the relevant data to the relevant list. This allows us to
		 * register new punishments instantly, on the fly, without having to
		 * re-read the file.
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
			}
			
		} catch (IOException ioe) {
			System.out.println("[WARNING]: An error occured while writing punishment data!");
			ioe.printStackTrace();
			return;
		}
		
		if (p.isBan()) {
			c.disconnect();
		}
	}
	
	/**
	 * Removes the specified punishment for the specified player name. Takes a
	 * string instead of a Player because oftentimes, players who are punished
	 * cannot log in, and their Player objects will never exist. Thus we have
	 * nothing to pull a name from.
	 * 
	 * @param name
	 *            The name of the player who has served their punishment.
	 * @param id
	 *            The ID of the punishment to remove.
	 */
	public static boolean quash(String name, int id) {
		boolean b = false;
		
		/**
		 * This method will eventually wipe & re-write the entire file relevant
		 * to the type of punishment that it is removing. Very bad idea to do
		 * this if we haven't actually loaded the punishments yet.
		 */
		if (!hasLoaded) {
			load();
		}
		
		Punishments p = Punishments.forID(id);
		f = new File(p.getFile());
		
		System.out.println("Removing a " + p.getName() + " for " + Misc.formatName(name) + ".");
		
		/**
		 * Loop through and remove the data from the list.
		 * 
		 * This method accepts a player name as a string, rather than accepting
		 * a Player object and pulling the name from it later.
		 * 
		 * This is done because the method needs to be able to work with offline
		 * as well as online players. Because of this, we're only going to
		 * search for the name.
		 */
		for (int i = 0; i < p.getList().size(); i++) {
			/**
			 * If the name is found, remove that index from the list.
			 * Essentially, this will clear any punishments of the specified
			 * level that are registered under the specified name.
			 */
			if (p.getList().get(i).contains(cushion(name))) {
				p.getList().remove(i);
				b = true;
				break;
			}
		}

		try {
			
			/**
			 * Initialize and immediately close a new PrintWriter on the
			 * relevant file. This effectively clears all data from the file.
			 */
			PrintWriter pw = new PrintWriter(f);
			pw.close();
			
			/**
			 * Then open a new BufferedWriter and point it to the file we just
			 * cleared. We're going to write the list back to the file.
			 * 
			 * The list no longer contains the punishment that we removed, so
			 * the punishment has effectively been quashed for the current
			 * instance of the server ONLY.
			 * 
			 * To ensure that the punishment is not re-loaded the next time the
			 * server starts, we need to make sure it no longer exists in the
			 * file as well.
			 */
			out = new BufferedWriter(new FileWriter(f));

			try {
				
				/**
				 * Loop through the list, add the data back one at a time.
				 */
				for (int i = 0; i < p.getList().size(); i++) {
					out.write(p.getList().get(i));
					out.newLine();
				}
				
			} finally {
				out.close();
			}

		} catch (IOException ioe) {
			System.out.println("[WARNING]: An error occured while writing punishment data!");
			ioe.printStackTrace();
			return false;
		}
		return b;
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
		data.add(cushion(c.getName()));

		switch (p.getLevel()) {
		/**
		 * If we're attempting to determine if a level-1 punishment exists, we
		 * needn't search for anything else. Do nothing.
		 */
		case PUNISHMENT_LEVEL_1:
			break;
			/**
			 * However, for a level-2 punishment, we must take into account the IP
			 * address as well. If either the name or the IP exists, the player is
			 * considered to be punished.
			 */
		case PUNISHMENT_LEVEL_2:
			data.add(cushion(c.getIP()));
			break;
			/**
			 * Similarly, for a level-3 punishment, the player MAC must also be
			 * checked.
			 */
		case PUNISHMENT_LEVEL_3:
			data.add(cushion(c.getMAC()));
			break;
		}

		/**
		 * Iterate through the punishment list.
		 */
		for (int i = 0; i < p.getList().size(); i++) {
			/**
			 * Iterate through each check that we have determined is necessary.
			 */
			for (int j = 0; j < data.size(); j++) {
				/**
				 * If the entry in the list contains any of the data that we're
				 * checking for, then this player should be considered punished.
				 * 
				 * This works because it covers all of the following possible
				 * scenarios:
				 * 
				 * Player <A> attempts to log in and is banned. The system finds
				 * Player <A>'s name in the ban file and denies login.
				 * 
				 * Player <B> attempts to log in and is IP-banned. The system
				 * finds Player <B>'s IP (under ANY name) in the IP-ban file and
				 * denies login.
				 * 
				 * Player <C> attempts to log in and is MAC-banned. The system
				 * finds Player <C>'s MAC (under ANY name) in the MAC-ban file
				 * and denies login.
				 * 
				 * These three scenarios work the same way for both mutes and
				 * bans.
				 * 
				 * TODO determine if the lowercasing is actually needed.
				 */
				if (p.getList().get(i).toLowerCase().contains(data.get(j).toLowerCase())) {
					b = true;
					break;
				}
			}
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
