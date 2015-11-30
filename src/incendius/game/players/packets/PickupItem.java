package incendius.game.players.packets;

import incendius.event.CycleEvent;
import incendius.event.CycleEventContainer;
import incendius.event.CycleEventHandler;
import incendius.game.players.PacketType;
import incendius.game.players.Player;
import incendius.handlers.ItemHandler;
import incendius.world.map.Region;

/**
 * Pickup Item
 **/
public class PickupItem implements PacketType {

	@Override
	public void processPacket(final Player c, int packetType, int packetSize) {
		c.getInstance().pItemY = c.getInStream().readSignedWordBigEndian();
		c.getInstance().pItemId = c.getInStream().readUnsignedWord();
		c.getInstance().pItemX = c.getInStream().readSignedWordBigEndian();
		
		if (c.getInstance().teleTimer > 0)
			return;
		
		if (Math.abs(c.getX() - c.getInstance().pItemX) > 25 || Math.abs(c.getY() - c.getInstance().pItemY) > 25) {
			c.resetWalkingQueue();
			return;
		}
		
		if (c.getInstance().playerSkilling[c.getInstance().playerFiremaking]) {
			return;
		}
		
		if (c.getInstance().resting) {
			c.getPA().resetRest();
		}
		
		c.getCombat().resetPlayerAttack();
		
		/**
		 * If the player is standing on the item
		 */
		if (c.getX() == c.getInstance().pItemX && c.getY() == c.getInstance().pItemY) {
			ItemHandler.removeGroundItem(c, c.getInstance().pItemId, c.getInstance().pItemX, c.getInstance().pItemY,
					true, Region.getRegion(c.getInstance().pItemX, c.getInstance().pItemY));
			return;
			
		/**
		 * If they're not already on top of it, they've got to walk to it
		 */
		} else {
			c.getInstance().walkingToItem = true;
			CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (!c.getInstance().walkingToItem)
						container.stop();
					if (c.getX() == c.getInstance().pItemX && c.getY() == c.getInstance().pItemY) {
						ItemHandler.removeGroundItem(c, c.getInstance().pItemId, c.getInstance().pItemX,
								c.getInstance().pItemY, true,
								Region.getRegion(c.getInstance().pItemX, c.getInstance().pItemY));
						container.stop();
					}
				}

				@Override
				public void stop() {
					c.getInstance().walkingToItem = false;
				}
			}, 1);
		}
		CycleEventHandler.getInstance().stopEvent(c.getSkilling());
	}
}
