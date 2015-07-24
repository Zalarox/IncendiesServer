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
			c.getVariables().xRemoveSlot = c.getInStream().readSignedWordBigEndian();
			c.getVariables().xInterfaceId = c.getInStream().readUnsignedWordA();
			c.getVariables().xRemoveId = c.getInStream().readSignedWordBigEndian();
		}
		if (c.getVariables().xInterfaceId == 3900) {
			c.getShops().buyItem(c.getVariables().xRemoveId, c.getVariables().xRemoveSlot, 100);// buy
																								// 100
			c.getVariables().xRemoveSlot = 0;
			c.getVariables().xInterfaceId = 0;
			c.getVariables().xRemoveId = 0;
			return;
		}

		if (packetType == PART1) {
			// synchronized(c) {
			c.getOutStream().createFrame(27);
		}
	}

	// }
}
