package incendius.game.players.packets;

import incendius.game.items.UseItem;
import incendius.game.npcs.NPCHandler;
import incendius.game.players.PacketType;
import incendius.game.players.Player;

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
