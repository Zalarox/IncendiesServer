package main.game.players.packets;

import main.GameEngine;
import main.game.players.PacketType;
import main.game.players.Player;
import main.handlers.ItemHandler;

/**
 * Change Regions
 */
public class ChangeRegions implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		GameEngine.objectHandler.updateObjects(c);
		ItemHandler.reloadItems(c);
		GameEngine.objectManager.loadObjects(c);
		c.getPA().castleWarsObjects();
		c.getInstance().rockCrabKills = 0;
		/*
		 * if (c.inCwGame()) CastleWars.updateCwObects(c);/*
		 * 
		 * /*if (Region.REGION_UPDATING_ENABLED) { Region a =
		 * Region.getRegion(c.lastX, c.lastY); Region r =
		 * Region.getRegion(c.absX, c.absY); if (a != null) a.removePlayer(c);
		 * if (r != null) r.addPlayer(c); }
		 */

		c.getInstance().saveFile = true;

		if (c.getInstance().skullTimer > 0) {
			c.getInstance().isSkulled = true;
			c.getInstance().headIconPk = 0;
			c.getPA().requestUpdates();
		}
		if (c.CD.OBJECTS_CAN_LOAD == true) {
			c.OL.loadObjectsOther(c);
		}

	}

}
