package main.game.players.packets;

import main.game.players.PacketType;
import main.game.players.Player;
import main.game.players.content.minigames.impl.dueling.DuelPlayer;
import main.game.players.content.skills.cooking.FlourRelated;

/**
 * Clicking stuff (interfaces)
 **/
public class CloseInterface implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {

		if (c.getStatedInterface() == "flour")
			FlourRelated.handleButton(c, 53204);
		if (c.getVariables().inTrade) {
			if (!c.getVariables().acceptedTrade) {
				c.getTradeHandler().declineTrade(false);
			}
		}

		if (c.opponent != null) {
			if (DuelPlayer.isInFirstScreen(c) || DuelPlayer.isInSecondScreen(c)) {
				c.Dueling.declineDuel(c, true, false);
			}
		}
		if (c.getVariables().killedDuelOpponent) {
			c.Dueling.claimStakedItems(c);
		}
		c.getPA().closeActivities();
	}

}
