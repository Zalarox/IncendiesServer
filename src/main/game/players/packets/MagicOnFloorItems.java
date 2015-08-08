package main.game.players.packets;

import main.event.CycleEvent;
import main.event.CycleEventContainer;
import main.event.CycleEventHandler;
import main.game.items.ItemLoader;
import main.game.players.PacketType;
import main.game.players.Player;
import main.handlers.ItemHandler;
import main.world.map.Region;

/**
 * Magic on floor items
 **/
@SuppressWarnings("unused")
public class MagicOnFloorItems implements PacketType {

	@Override
	public void processPacket(final Player c, int packetType, int packetSize) {
		final int itemY = c.getInStream().readSignedWordBigEndian();
		int itemId = c.getInStream().readUnsignedWord();
		final int itemX = c.getInStream().readSignedWordBigEndian();
		int spellId = c.getInStream().readUnsignedWordA();
		if (c.getInstance().teleTimer > 0)
			return;
		if (!ItemHandler.itemExists(itemId, itemX, itemY)) {
			c.stopMovement();
			return;
		}
		c.getInstance().usingMagic = true;
		if (!c.getCombat().checkMagicReqs(51)) {
			c.stopMovement();
			return;
		}

		if (c.goodDistance(c.getX(), c.getY(), itemX, itemY, 12)) {
			int offY = (c.getX() - itemX) * -1;
			int offX = (c.getY() - itemY) * -1;
			c.getInstance().teleGrabX = itemX;
			c.getInstance().teleGrabY = itemY;
			c.getInstance().teleGrabItem = itemId;
			c.turnPlayerTo(itemX, itemY);
			c.getInstance().teleGrabDelay = System.currentTimeMillis();
			c.getInstance();
			c.startAnimation(Player.MAGIC_SPELLS[51][2]);
			c.getInstance();
			c.gfx100(Player.MAGIC_SPELLS[51][3]);
			c.getPA().createPlayersStillGfx(144, itemX, itemY, 0, 72);
			c.getInstance();
			c.getPA().createPlayersProjectile(c.getX(), c.getY(), offX, offY, 50, 70, Player.MAGIC_SPELLS[51][4], 50, 10, 0,
					50);
			c.getInstance();
			c.getPA().addSkillXP(Player.MAGIC_SPELLS[51][7], 6);
			c.getPA().refreshSkill(6);
			c.stopMovement();
		}

		/*
		 * Telegrab spell
		 */
		if ((((c.getItems().freeSlots() >= 1) || c.getItems().playerHasItem(itemId, 1))
				&& ItemLoader.isStackable(itemId))
				|| ((c.getItems().freeSlots() > 0) && !ItemLoader.isStackable(itemId))) {
			if (c.goodDistance(c.getX(), c.getY(), itemX, itemY, 12)) {
				c.getInstance().walkingToItem = true;
				int offY = (c.getX() - itemX) * -1;
				int offX = (c.getY() - itemY) * -1;
				c.getInstance().teleGrabX = itemX;
				c.getInstance().teleGrabY = itemY;
				c.getInstance().teleGrabItem = itemId;
				c.turnPlayerTo(itemX, itemY);
				c.getInstance().teleGrabDelay = System.currentTimeMillis();
				c.getInstance();
				c.startAnimation(Player.MAGIC_SPELLS[51][2]);
				c.getInstance();
				c.gfx100(Player.MAGIC_SPELLS[51][3]);
				c.getPA().createPlayersStillGfx(144, itemX, itemY, 0, 72);
				c.getInstance();
				c.getPA().createPlayersProjectile(c.getX(), c.getY(), offX, offY, 50, 70, Player.MAGIC_SPELLS[51][4], 50, 10,
						0, 50);
				c.getInstance();
				c.getPA().addSkillXP(Player.MAGIC_SPELLS[51][7], 6);
				c.getPA().refreshSkill(6);
				c.stopMovement();
				CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (!c.getInstance().walkingToItem)
							container.stop();
						if (System.currentTimeMillis() - c.getInstance().teleGrabDelay > 1550
								&& c.getInstance().usingMagic) {
							if (ItemHandler.itemExists(c.getInstance().teleGrabItem, c.getInstance().teleGrabX,
									c.getInstance().teleGrabY)
									&& c.goodDistance(c.getX(), c.getY(), itemX, itemY, 12)) {
								ItemHandler.removeGroundItem(c, c.getInstance().teleGrabItem,
										c.getInstance().teleGrabX, c.getInstance().teleGrabY, true,
										Region.getRegion(c.getInstance().teleGrabX, c.getInstance().teleGrabY));
								c.getInstance().usingMagic = false;
								container.stop();
							}
						}
					}

					@Override
					public void stop() {
						c.getInstance().walkingToItem = false;
					}
				}, 1);
			}
		} else {
			c.sendMessage("You don't have enough space in your inventory.");
			c.stopMovement();
		}
	}

}
