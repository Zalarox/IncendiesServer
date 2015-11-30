package incendius.game.players.packets;

import incendius.game.players.PacketType;
import incendius.game.players.Player;
import incendius.game.players.content.minigames.DuelArena;
import incendius.game.players.content.skills.cooking.FlourRelated;

/**
 * Clicking stuff (interfaces)
 **/
public class CloseInterface implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {

		if (c.getStatedInterface() == "flour")
			FlourRelated.handleButton(c, 53204);
		if (c.getInstance().inTrade) {
			if (!c.getInstance().acceptedTrade) {
				c.getTradeHandler().declineTrade(false);
			}
		}

		if (c.opponent != null) {
			if (DuelArena.isInFirstInterface(c) || DuelArena.isInSecondInterface(c)) {
				c.Dueling.declineDuel(c, true, false);
			}
		}
		if (c.getInstance().killedDuelOpponent) {
			c.Dueling.claimDuelRewards(c);
		}
		c.getPA().closeActivities();
	}

}
