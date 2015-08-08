package main.game.players.packets;

import main.game.players.PacketType;
import main.game.players.Player;

/**
 * Bank X Items
 **/
public class BankX1 implements PacketType {

	public static final int PART1 = 135;
	public static final int PART2 = 208;
	public int XremoveSlot, XinterfaceID, XremoveID, Xamount;

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		if (packetType == 135) {
			c.getInstance().xRemoveSlot = c.getInStream().readSignedWordBigEndian();
			c.getInstance().xInterfaceId = c.getInStream().readUnsignedWordA();
			c.getInstance().xRemoveId = c.getInStream().readSignedWordBigEndian();
		}
		if (c.getInstance().xInterfaceId == 3900) {
			c.getShops().buyItem(c.getInstance().xRemoveId, c.getInstance().xRemoveSlot, 100);// buy
																								// 100
			c.getInstance().xRemoveSlot = 0;
			c.getInstance().xInterfaceId = 0;
			c.getInstance().xRemoveId = 0;
			return;
		}

		if (packetType == PART1) {
			// synchronized(c) {
			c.getOutStream().createFrame(27);
		}
	}

	// }
}
