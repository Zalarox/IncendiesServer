package incendius.game.players.packets;

import incendius.Constants;
import incendius.game.players.Boundaries;
import incendius.game.players.PacketType;
import incendius.game.players.Player;
import incendius.game.players.PlayerHandler;
import incendius.game.players.Boundaries.Area;
import incendius.handlers.Following;

/**
 * Trading
 */
public class Trade implements PacketType {

	@SuppressWarnings("unused")
	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int tradeId = c.getInStream().readSignedWordBigEndian();
		if (c.getInstance().teleTimer > 0)
			return;
		if (tradeId > PlayerHandler.players.length) {
			c.getInstance().playerIndex = -1;
			return;
		}
		Following.resetFollow(c);
		if (Boundaries.checkBoundaries(Area.ARENAS, c.getX(), c.getY())) {
			c.sendMessage("You can't trade inside the arena!");
			return;
		}
		if (c.getInstance().playerRights == 2 && !Constants.ADMINISTRATORS_CAN_TRADE_ITEMS) {
			c.sendMessage("Trading as an admin has been disabled.");
			return;
		}
		if (tradeId != c.playerId)
			c.getTradeHandler().requestTrade(tradeId, true);
	}

}
