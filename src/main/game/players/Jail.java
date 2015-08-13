package main.game.players;

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

	Player c;

	public Jail(Player c) {
		this.c = c;
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
		if(c.getX() == 3230 && c.getY() == 3407 //Varrock - left cage 
		|| c.getX() == 3228 && c.getY() == 3407 //Varrock - center cage
		|| c.getX() == 3226 && c.getY() == 3407 //Varrock - right cage
		) { 
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Sends a player to jail. Since jail-checking works on the location
	 * basis, the only thing needed to jail a player is a simple method that
	 * teleports them to the appropriate spot.
	 * 
	 * Since we're cool, we'll give the player a random jail cell. More possibilities
	 * could be added in the future, for now it's just one of the three Varrock cages.
	 * 
	 */
	public void jail() {
		
	}

}
