package incendius.game.players.content.skills.construction;

import incendius.game.players.Player;

/**
 * 
 * @author TheLife
 *
 */

public class ObjectLoading {

	public void loadObjects(Player c) {
		for (int i = 0; i < c.CD.CONSTRUCTION_ID.length; i++) {
			if (c.CD.CONSTRUCTION_ID[i] != 0) {
				c.getPA().checkObjectSpawn(c.CD.CONSTRUCTION_ID[i], c.CD.CONSTRUCTION_LOCATION_X[i],
						c.CD.CONSTRUCTION_LOCATION_Y[i], c.CD.CONSTRUCTION_FACE_ID[i], 10);
			}
		}
	}

	public void loadObjectsOther(Player c) {
		for (int i = 0; i < c.CD.CONSTRUCTION_ID_OTHERS.length; i++) {
			if (c.CD.CONSTRUCTION_ID_OTHERS[i] != 0) {
				c.getPA().checkObjectSpawn(c.CD.CONSTRUCTION_ID_OTHERS[i], c.CD.CONSTRUCTION_LOCATION_X_OTHERS[i],
						c.CD.CONSTRUCTION_LOCATION_Y_OTHERS[i], c.CD.CONSTRUCTION_FACE_OTHERS[i], 10);
			}
		}
	}

}
