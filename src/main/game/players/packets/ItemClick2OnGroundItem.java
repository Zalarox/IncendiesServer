package main.game.players.packets;

import main.game.players.PacketType;
import main.game.players.Player;
import main.game.players.content.skills.firemaking.Firemaking;
import main.game.players.content.skills.firemaking.LogData.logData;

public class ItemClick2OnGroundItem implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		final int itemX = c.getInStream().readSignedWordBigEndian();
		final int itemY = c.getInStream().readSignedWordBigEndianA();
		final int itemId = c.getInStream().readUnsignedWordA();
		if (c.getInstance().teleTimer > 0)
			return;
		for (final logData l : logData.values()) {
			if (itemId == l.getLogId()) {
				if (c.getItems().playerHasItem(590)) {
					Firemaking.attemptFire(c, 590, itemId, itemX, itemY, true);
				} else {
					Firemaking.attemptFire(c, 17678, itemId, itemX, itemY, true);
				}
			}
		}
	}

}