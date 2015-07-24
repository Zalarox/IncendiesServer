package main.game.players.packets;

import main.game.players.PacketType;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.handlers.Following;

/**
 * Follow Player
 **/
public class FollowPlayer implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int followPlayer = c.getInStream().readUnsignedWordBigEndian();
		if (followPlayer > PlayerHandler.players.length) {
			return;
		}
		if (PlayerHandler.players[followPlayer] == null) {
			return;
		}
		if (c.getVariables().resting) {
			c.getPA().resetRest();
			return;
		}
		c.getVariables().playerIndex = 0;
		c.getVariables().npcIndex = 0;
		c.getVariables().mageFollow = false;
		c.getVariables().usingBow = false;
		c.getVariables().usingRangeWeapon = false;
		c.getVariables().followDistance = 1;
		Following.triggerFollowing(followPlayer, 0, c);
	}
}
