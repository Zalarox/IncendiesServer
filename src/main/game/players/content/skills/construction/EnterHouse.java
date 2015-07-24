package main.game.players.content.skills.construction;

import main.game.players.Player;

/**
 * 
 * @author TheLife
 *
 */

public class EnterHouse {

	public void EnterTheHouse(Player c, final String Type) {
		if (c.CD.OWNS_HOUSE == false) {
			c.sendMessage("You don't own a house here, try buying it first.");
			c.getPA().removeAllWindows();
			return;
		}
		if (c.getVariables().inWild()) {
			c.getPA().removeAllWindows();
			return;
		}
		c.getPA().removeAllWindows();
		for (int i = 0; i < c.CD.CONSTRUCTION_ID.length; i++) {
			c.CD.OBJECTS_CAN_LOAD = true;
			c.CD.CAN_MAKE_OBJECTS = Type == "BUILDING_MODE" ? true : false;
			c.getPA().movePlayer(2907, 4575, c.playerId * 4);
			c.CD.LOAD_TIMER = 5;
			c.CD.CAN_GO_HOUSES = false;
			c.CD.UPDATER = c.playerName;
			// c.getPA().openInventoryInterface(16010);
			c.CD.INTERFACE_TIMER_V1 = 7;
			c.CD.INTERFACE_LOADED_V1 = false;
			c.CD.CONSTRUCTION_ID_OTHERS[i] = c.CD.CONSTRUCTION_ID[i];
			c.CD.CONSTRUCTION_FACE_OTHERS[i] = c.CD.CONSTRUCTION_FACE_ID[i];
			c.CD.CONSTRUCTION_LOCATION_X_OTHERS[i] = c.CD.CONSTRUCTION_LOCATION_X[i];
			c.CD.CONSTRUCTION_LOCATION_Y_OTHERS[i] = c.CD.CONSTRUCTION_LOCATION_Y[i];
			c.CD.IN_BUILDING_MODE = Type == "BUILDING_MODE" ? true : false;
			c.CD.IN_FRIENDS_HOUSE = false;
			if (Type == "BUILDING_MODE")
				c.KP.kickFromHouse(c, "BUILDING_MODE");
		}
	}

	public EnterHouse() {
		// TODO Auto-generated constructor stub
	}

}
