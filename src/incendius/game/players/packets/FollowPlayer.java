package incendius.game.players.packets;

import incendius.game.players.PacketType;
import incendius.game.players.Player;
import incendius.game.players.PlayerHandler;
import incendius.handlers.Following;

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
		if (c.getInstance().resting) {
			c.getPA().resetRest();
			return;
		}
		c.getInstance().playerIndex = 0;
		c.getInstance().npcIndex = 0;
		c.getInstance().mageFollow = false;
		c.getInstance().usingBow = false;
		c.getInstance().usingRangeWeapon = false;
		c.getInstance().followDistance = 1;
		Following.triggerFollowing(followPlayer, 0, c);
	}
}
