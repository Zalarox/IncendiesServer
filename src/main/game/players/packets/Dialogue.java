package main.game.players.packets;

import main.game.players.PacketType;
import main.game.players.Player;

/**
 * Dialogue
 **/
public class Dialogue implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {

		if (c.getInstance().nextChat > 0) {
			c.getDH().sendDialogues(c.getInstance().nextChat, c.getInstance().talkingNpc);
		} else {
			c.getDH().sendDialogues(0, -1);
		}

	}

}
