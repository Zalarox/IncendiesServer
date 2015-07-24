package main.game.players.packets;

import main.game.players.Player;
import main.net.Packet;

/**
 * An interface which describes a class that handles packets.
 * 
 * @author Graham Edgecombe
 *
 */
public interface PacketHandler {

	/**
	 * Handles a single packet.
	 * 
	 * @param player
	 *            The player.
	 * @param packet
	 *            The packet.
	 */
	public void handle(Player player, Packet packet);

}
