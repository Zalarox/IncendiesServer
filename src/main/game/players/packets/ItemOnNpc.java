package main.game.players.packets;

import main.game.items.UseItem;
import main.game.npcs.NPCHandler;
import main.game.players.PacketType;
import main.game.players.Player;

public class ItemOnNpc implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int itemId = c.getInStream().readSignedWordA();
		int i = c.getInStream().readSignedWordA();
		int slot = c.getInStream().readSignedWordBigEndian();
		int npcId = NPCHandler.npcs[i].npcType;
		if (c.getInstance().teleTimer > 0)
			return;
		if (c.getInstance().resting) {
			c.getPA().resetRest();
		}
		UseItem.ItemonNpc(c, itemId, npcId, slot);
	}
}
