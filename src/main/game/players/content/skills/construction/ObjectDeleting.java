package main.game.players.content.skills.construction;

import main.game.players.Player;

/**
 * 
 * @author TheLife
 *
 */

public class ObjectDeleting {

	public void RemoveAllObjects(Player c) {
		/*
		 * if(c.isInBuildingMode == false) { c.sendMessage(
		 * "You need to be in you're house.(Building mode)"); return; }
		 * if(!c.inConstruction()) { c.sendMessage(
		 * "You need to be in you're house."); return; } if(c.ownsHouse ==
		 * false) { c.sendMessage(
		 * "You need to own a house to remove all of your objects."); return; }
		 * boolean ConstMsg = true; for (int i = 0; i < c.ConstructionID.length;
		 * i++) { if (c.CD.CONSTRUCTION_ID[i] != 0) { c.CD.CONSTRUCTION_ID[i] =
		 * 0; c.CD.CONSTRUCTION_LOCATION_X[i] = 0;
		 * c.CD.CONSTRUCTION_LOCATION_Y[i] = 0; c.CD.SET_PLACE = 0;
		 * //loadObjects(); c.getPA().movePlayer(0, 0, 0);
		 * c.constructionInterfaceTimerLoaded2 = false; c.constructionLoadTimer3
		 * = 3; c.getPA().closeAllWindows(); } }
		 */
	}

	/**
	 * Removes latest object.
	 */

	public void RemoveLatestObject(Player c) {
		/*
		 * if(c.isInBuildingMode == false) { c.sendMessage(
		 * "You need to be in you're house.(Building mode)"); return; }
		 * if(!c.inConstruction()) { c.sendMessage(
		 * "You need to be in you're house."); return; } if(c.ownsHouse ==
		 * false) { c.sendMessage(
		 * "You need to own a house to remove all of your objects."); return; }
		 * if (c.ObjectPlace != 0) { c.ObjectPlace -= 1;
		 * //c.getPA().checkObjectSpawn(-1,
		 * c.ConstructionLocationX[c.ObjectPlace],
		 * c.ConstructionLocationY[c.ObjectPlace], 1, 10);
		 * c.ConstructionID[c.ObjectPlace] = 0;
		 * c.ConstructionLocationX[c.ObjectPlace] = 0;
		 * c.ConstructionLocationY[c.ObjectPlace] = 0; //loadObjects();
		 * c.getPA().movePlayer(0, 0, 0); c.constructionInterfaceTimerLoaded2 =
		 * false; c.constructionLoadTimer3 = 3; c.getPA().closeAllWindows(); }
		 * else { c.ObjectPlace = 0; //c.getPA().checkObjectSpawn(-1,
		 * c.ConstructionLocationX[c.ObjectPlace],
		 * c.ConstructionLocationY[c.ObjectPlace], 1, 10);
		 * c.ConstructionID[c.ObjectPlace] = 0;
		 * c.ConstructionLocationX[c.ObjectPlace] = 0;
		 * c.ConstructionLocationY[c.ObjectPlace] = 0; //loadObjects();
		 * c.getPA().movePlayer(0, 0, 0); c.constructionInterfaceTimerLoaded2 =
		 * false; c.constructionLoadTimer3 = 3; c.getPA().closeAllWindows(); }
		 */
	}

}
