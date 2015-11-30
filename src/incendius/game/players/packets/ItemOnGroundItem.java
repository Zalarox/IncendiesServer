package incendius.game.players.packets;

import incendius.game.players.PacketType;
import incendius.game.players.Player;
import incendius.game.players.content.skills.firemaking.Firemaking;
import incendius.util.Misc;

public class ItemOnGroundItem implements PacketType {

	@Override
	@SuppressWarnings("unused")
	public void processPacket(Player c, int packetType, int packetSize) {
		int a1 = c.getInStream().readSignedWordBigEndian();
		int itemUsed = c.getInStream().readSignedWordA();
		int groundItem = c.getInStream().readUnsignedWord();
		int gItemY = c.getInStream().readSignedWordA();
		int itemUsedSlot = c.getInStream().readSignedWordBigEndianA();
		int gItemX = c.getInStream().readUnsignedWord();
		if (c.getInstance().teleTimer > 0)
			return;
		switch (itemUsed) {
		case 590:
		case 17678:
			Firemaking.attemptFire(c, itemUsed, groundItem, gItemX, gItemY, true);
			break;
		default:
			if (c.getInstance().playerRights == 3)
				Misc.println("ItemUsed " + itemUsed + " on Ground Item " + groundItem);
			break;
		}
	}

}
