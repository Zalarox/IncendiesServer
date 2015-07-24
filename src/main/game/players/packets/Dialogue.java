package main.game.players.packets;

import main.game.players.PacketType;
import main.game.players.Player;

/**
 * Dialogue
 **/
public class Dialogue implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {

		if (c.getVariables().nextChat > 0) {
			c.getDH().sendDialogues(c.getVariables().nextChat, c.getVariables().talkingNpc);
		} else {
			c.getDH().sendDialogues(0, -1);
		}

	}

}
