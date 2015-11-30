package incendius.game.players.packets;

import incendius.game.players.PacketType;
import incendius.game.players.Player;
import incendius.game.players.content.minigames.DuelArena;

/**
 * Move Items
 **/
public class MoveItems implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int somejunk = c.getInStream().readUnsignedWordA(); // junk
		int itemFrom = c.getInStream().readUnsignedWordA();// slot1
		int itemTo = (c.getInStream().readUnsignedWordA() - 128);// slot2
		if (c.getInstance().teleTimer > 0)
			return;
		// c.sendMessage("junk: " + somejunk);
		if (c.getInstance().inTrade) {
			c.getTradeHandler().declineTrade(false);
			return;
		}
		if (c.getInstance().tradeStatus == 1) {
			c.getTradeHandler().declineTrade(false);
			return;
		}
		if (DuelArena.isInFirstInterface(c)) {
			c.Dueling.declineDuel(c, true, false);
			return;
		}
		c.getItems().moveItems(itemFrom, itemTo, somejunk);
	}
}
