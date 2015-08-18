package main.game.players;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import main.Constants;
import main.GameEngine;

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

public class JailHandler {
	
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
	    
	    boolean isIn(Player c) {
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
	 * Uses the player's location to determine whether or not they are in jail.
	 * 
	 * @return Whether or not the player is in a jail.
	 */
	public static boolean isJailed(Player c) {
		for (Jails j : Jails.values()) {
			if (j.isIn(c)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Sends a player to jail.
	 * 
	 * Since we're cool, we'll give the player a random jail cell.
	 */
	public static boolean jail(Player c) {
		
		if (c.isJailed() && isJailed(c)) {
			return true;
		}
		
		Jails j = Jails.random();
		c.getPA().movePlayer(j.getX(), j.getY(), j.getZ());
		c.sendMessage("You broke a rule, and have been locked up in the " + j.getName() + ".");
		c.setJailed(true);
		
		return (c.isJailed() ? true : false);
		
	}
	
	/**
	 * Returns a player from jail.
	 */
	public static boolean unjail(Player c) {
		
		if (!c.isJailed()) {
			return true;
		}
		
		c.getPA().movePlayer(Constants.RESPAWN_X, Constants.RESPAWN_Y, 0);
		c.sendMessage("Your jail sentance has ended.");
		c.sendMessage("In the future, don't break the rules and you won't end up here.");
		c.setJailed(false);
		
		return (!c.isJailed() ? true : false);
	}
	
	/**
	 * Handles escaped inmates.
	 */
	public static void process() {
		for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
			if (PlayerHandler.getPlayer(i) != null && !PlayerHandler.getPlayer(i).disconnected) {
				Player c = PlayerHandler.getPlayer(i);
				
				if (c.isJailed() && !isJailed(c)) {
					if (jail(c)) {
						GameEngine.sendStaffNotice("the system has returned @red@" + c.getDisplayName() + "@bla@ to jail.");
					} 
				}	
			}
		}
	}
}
