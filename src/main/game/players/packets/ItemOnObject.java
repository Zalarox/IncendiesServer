package main.game.players.packets;

import main.game.items.UseItem;
import main.game.objects.Objects;
import main.game.players.PacketType;
import main.game.players.Player;
import main.game.players.content.Fillables;
import main.game.players.content.skills.hunter.HunterHandler;
import main.handlers.Following;
import main.world.ObjectHandler;

public class ItemOnObject implements PacketType {

	@SuppressWarnings("unused")
	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		/*
		 * a = ? b = ?
		 */

		int a = c.getInStream().readUnsignedWord();
		int objectId = c.getInStream().readSignedWordBigEndian();
		int objectY = c.getInStream().readSignedWordBigEndianA();
		int b = c.getInStream().readUnsignedWord();
		int objectX = c.getInStream().readSignedWordBigEndianA();
		int itemId = c.getInStream().readUnsignedWord();
		if (c.getInstance().teleTimer > 0)
			return;
		Fillables.fillTheItem(c, itemId, objectId);
		if (c.getInstance().resting) {
			c.getPA().resetRest();
		}
		Following.resetFollow(c);
		c.turnPlayerTo(objectX, objectY);
		UseItem.ItemonObject(c, objectId, objectX, objectY, itemId);
		for (Objects o : ObjectHandler.globalObjects) {
			if (o.objectX == objectX && o.objectY == objectY) {
				if (o != null)
					HunterHandler.bait(c, itemId, o);
				break;
			}
		}
	}

}
