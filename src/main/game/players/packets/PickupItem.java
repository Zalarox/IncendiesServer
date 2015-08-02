package main.game.players.packets;

import main.event.CycleEvent;
import main.event.CycleEventContainer;
import main.event.CycleEventHandler;
import main.game.players.PacketType;
import main.game.players.Player;
import main.handlers.ItemHandler;
import main.world.map.Region;

/**
 * Pickup Item
 **/
public class PickupItem implements PacketType {

	@Override
	public void processPacket(final Player c, int packetType, int packetSize) {
		c.getVariables().pItemY = c.getInStream().readSignedWordBigEndian();
		c.getVariables().pItemId = c.getInStream().readUnsignedWord();
		c.getVariables().pItemX = c.getInStream().readSignedWordBigEndian();
		
		if (c.getVariables().teleTimer > 0)
			return;
		
		if (Math.abs(c.getX() - c.getVariables().pItemX) > 25 || Math.abs(c.getY() - c.getVariables().pItemY) > 25) {
			c.resetWalkingQueue();
			return;
		}
		
		if (c.getVariables().playerSkilling[c.getVariables().playerFiremaking]) {
			return;
		}
		
		if (c.getVariables().resting) {
			c.getPA().resetRest();
		}
		
		c.getCombat().resetPlayerAttack();
		
		/**
		 * If the player is standing on the item
		 */
		if (c.getX() == c.getVariables().pItemX && c.getY() == c.getVariables().pItemY) {
			ItemHandler.removeGroundItem(c, c.getVariables().pItemId, c.getVariables().pItemX, c.getVariables().pItemY,
					true, Region.getRegion(c.getVariables().pItemX, c.getVariables().pItemY));
			return;
			
		/**
		 * If they're not already on top of it, they've got to walk to it
		 */
		} else {
			c.getVariables().walkingToItem = true;
			CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (!c.getVariables().walkingToItem)
						container.stop();
					if (c.getX() == c.getVariables().pItemX && c.getY() == c.getVariables().pItemY) {
						ItemHandler.removeGroundItem(c, c.getVariables().pItemId, c.getVariables().pItemX,
								c.getVariables().pItemY, true,
								Region.getRegion(c.getVariables().pItemX, c.getVariables().pItemY));
						container.stop();
					}
				}

				@Override
				public void stop() {
					c.getVariables().walkingToItem = false;
				}
			}, 1);
		}
		CycleEventHandler.getInstance().stopEvent(c.getSkilling());
	}
}
