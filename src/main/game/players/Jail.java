package main.game.players;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A class tracking methods used on players who are
 * in "jail," a game mechanic defining player accounts
 * who are not banned, but cannot interact with the game
 * in most ways.
 * 
 * Jailed players are restricted to the following in-game activities:
 * - Logging in
 * - Joining & talking in the "help" clan channel
 * 
 * @author Branon McClellan (KeepBotting)
 *
 */

public class Jail {

	static Player c;

	public Jail(Player c) {
		this.c = c;
	}
	
	private static final Random r = new Random();
	
	/**
	 * An enumerated type containing information about the 
	 * various jail cells.
	 * 
	 * This makes adding or removing jail cells VERY easy, 
	 * since jails are defined centrally in this enum and
	 * nowhere else.
	 * 
	 * @author Branon McClellan (KeepBotting)
	 */
	private enum Jails {
		/* <jail name>, <x-coord>, <y-coord>, <z-coord> */
		
	    VARROCK_LEFT    ("Varrock pillory",      3230, 3407, 0),
		VARROCK_CENTER  ("Varrock pillory",      3228, 3407, 0),
		VARROCK_RIGHT   ("Varrock pillory",      3226, 3407, 0),
		SHANTAY_PASS    ("Shantay Pass jail",    3299, 3123, 0),
		PORT_SARIM_1    ("Port Sarim jail",      3013, 3195, 0),
		PORT_SARIM_2    ("Port Sarim jail",      3013, 3192, 0),
		PORT_SARIM_3    ("Port Sarim jail",      3013, 3189, 0),
		PORT_SARIM_4    ("Port Sarim jail",      3018, 3188, 0),
		PORT_SARIM_5    ("Port Sarim jail",      3018, 3181, 0),
		PORT_SARIM_6    ("Port Sarim jail",      3014, 3181, 0),
		DRAYNOR_VILLAGE ("Draynor village jail", 3123, 3243, 0);

		private String name;
		private int x;
		private int y;
		private int z;
		
	    Jails(String name, int x, int y, int z) {
	        this.name = name;
	        this.x    = x;
	        this.y    = y;
	        this.z    = z;
	    }
	    
		String getName() {
	    	return this.name;
	    }
	    
	    int getX() {
	    	return this.x;
	    }
	    
	    int getY() {
	    	return this.y;
	    }
	    
	    int getZ() {
	    	return this.z;
	    }
	    
	    boolean isIn() {
	    	return (c.getX() == this.getX() 
	    		 && c.getY() == this.getY() 
	    		 && c.getZ() == this.getZ());
	    }

	    /**
	     * A unmodifiable list of the values in this enumerated type.
	     */
		private static final List<Jails> jails = Collections.unmodifiableList(Arrays.asList(values()));

		/**
		 * Pulls a random jail from Jails.
		 * 
		 * @return The randomized jail.
		 */
		public static Jails random() {
			return jails.get(r.nextInt(jails.size()));
		}
	    
	    }
	
	/**
	 * Determining if a player is jailed works off a location basis.
	 * 
	 * No non-jailed player will ever enter this area, and players
	 * who become jailed are always sent straight to this area.
	 * 
	 * Thus it is safe to assume that players existing here are in jail.
	 * This prevents us having to add an <isJailed> token to the character file.
	 * 
	 * @return Whether or not the player is jailed.
	 */
	public boolean isJailed() {
		for (Jails j : Jails.values()) {
			if (j.isIn()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Sends a player to jail. Since jail-checking works on the location
	 * basis, the only thing needed to jail a player is a simple method that
	 * teleports them to the appropriate spot.
	 * 
	 * Since we're cool, we'll give the player a random jail cell.
	 */
	public void jail() {
		if (c.getJail().isJailed()) {
			return;
		}
		
		Jails j = Jails.random();
		c.getPA().movePlayer(j.getX(), j.getY(), j.getZ());
		c.sendMessage("You broke a rule, and have been locked up in the " + j.getName() + ".");
	}

}
