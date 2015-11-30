package incendius.game.players.content.skills.construction;

import incendius.game.players.Player;

/**
 * 
 * @author Thelife
 *
 */

public class FaceObject {

	public void ObjectFacingLeft(Player c) {
		c.CD.WALKING_WAY = true;
		for (int i = 0; i < c.CD.CONSTRUCTION_ID.length; i++) {
			if (c.CD.CONSTRUCTION_FACE_ID[c.CD.FACE_ID] == 0 && c.CD.CONSTRUCTION_ID[c.CD.FACE_ID] != 0
					&& c.CD.WALKING_WAY == true) {
				c.sendMessage("Can't go any lower.");
				c.CD.WALKING_WAY = false;
			}
			if (c.CD.CONSTRUCTION_FACE_ID[c.CD.FACE_ID] == 1 && c.CD.CONSTRUCTION_ID[c.CD.FACE_ID] != 0
					&& c.CD.WALKING_WAY == true) {
				c.CD.CONSTRUCTION_FACE_ID[c.CD.FACE_ID] = 0;
				c.CD.WALKING_WAY = false;
			}
			if (c.CD.CONSTRUCTION_FACE_ID[c.CD.FACE_ID] == 2 && c.CD.CONSTRUCTION_ID[c.CD.FACE_ID] != 0
					&& c.CD.WALKING_WAY == true) {
				c.CD.CONSTRUCTION_FACE_ID[c.CD.FACE_ID] = 1;
				c.CD.WALKING_WAY = false;
			}
			if (c.CD.CONSTRUCTION_FACE_ID[c.CD.FACE_ID] == 3 && c.CD.CONSTRUCTION_ID[c.CD.FACE_ID] != 0
					&& c.CD.WALKING_WAY == true) {
				c.CD.CONSTRUCTION_FACE_ID[c.CD.FACE_ID] = 2;
				c.CD.WALKING_WAY = false;
			}
			if (c.CD.CONSTRUCTION_FACE_ID[c.CD.FACE_ID] == 4 && c.CD.CONSTRUCTION_ID[c.CD.FACE_ID] != 0
					&& c.CD.WALKING_WAY == true) {
				c.CD.CONSTRUCTION_FACE_ID[c.CD.FACE_ID] = 3;
				c.CD.WALKING_WAY = false;
			}
		}
		c.OL.loadObjects(c);
	}

	/**
	 * Object facing right.
	 */

	public void ObjectFacingRight(Player c) {
		c.CD.WALKING_WAY = true;
		for (int i = 0; i < c.CD.CONSTRUCTION_ID.length; i++) {
			if (c.CD.CONSTRUCTION_FACE_ID[c.CD.FACE_ID] == 4 && c.CD.CONSTRUCTION_ID[c.CD.FACE_ID] != 0
					&& c.CD.WALKING_WAY == true) {
				c.sendMessage("Can't go any higher.");
				c.CD.WALKING_WAY = false;
			}
			if (c.CD.CONSTRUCTION_FACE_ID[c.CD.FACE_ID] == 3 && c.CD.CONSTRUCTION_ID[c.CD.FACE_ID] != 0
					&& c.CD.WALKING_WAY == true) {
				c.CD.CONSTRUCTION_FACE_ID[c.CD.FACE_ID] = 4;
				c.CD.WALKING_WAY = false;
			}
			if (c.CD.CONSTRUCTION_FACE_ID[c.CD.FACE_ID] == 2 && c.CD.CONSTRUCTION_ID[c.CD.FACE_ID] != 0
					&& c.CD.WALKING_WAY == true) {
				c.CD.CONSTRUCTION_FACE_ID[c.CD.FACE_ID] = 3;
				c.CD.WALKING_WAY = false;
			}
			if (c.CD.CONSTRUCTION_FACE_ID[c.CD.FACE_ID] == 1 && c.CD.CONSTRUCTION_ID[c.CD.FACE_ID] != 0
					&& c.CD.WALKING_WAY == true) {
				c.CD.CONSTRUCTION_FACE_ID[c.CD.FACE_ID] = 2;
				c.CD.WALKING_WAY = false;
			}
			if (c.CD.CONSTRUCTION_FACE_ID[c.CD.FACE_ID] == 0 && c.CD.CONSTRUCTION_ID[c.CD.FACE_ID] != 0
					&& c.CD.WALKING_WAY == true) {
				c.CD.CONSTRUCTION_FACE_ID[c.CD.FACE_ID] = 1;
				c.CD.WALKING_WAY = false;
			}
		}
		c.OL.loadObjects(c);
	}

}
