package main.world;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.Data;
import main.GameEngine;
import main.game.objects.Objects;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.util.Misc;

/**
 * @author Sanity
 */

public class ObjectHandler {

	public static List<Objects> globalObjects = new ArrayList<Objects>();

	public ObjectHandler() {
		loadGlobalObjects(Data.OBJECT_LIST);
	}

	/**
	 * Adds object to list
	 **/
	public void addObject(Objects object) {
		globalObjects.add(object);
	}

	/**
	 * Removes object from list
	 **/
	public void removeObject(Objects object) {
		globalObjects.remove(object);
	}

	/**
	 * Does object exist
	 **/
	public Objects objectExists(int objectX, int objectY, int objectHeight) {
		for (Objects o : globalObjects) {
			if (o.getObjectX() == objectX && o.getObjectY() == objectY && o.getObjectHeight() == objectHeight) {
				return o;
			}
		}
		return null;
	}

	/**
	 * Update objects when entering a new region or logging in
	 **/
	public void updateObjects(Player c) {
		for (Objects o : globalObjects) {
			if (c != null) {
				if (c.heightLevel == o.getObjectHeight() && o.objectTicks == 0) {
					if (c.distanceToPoint(o.getObjectX(), o.getObjectY()) <= 60) {
						c.getPA().object(o.getObjectId(), o.getObjectX(), o.getObjectY(), o.getObjectFace(),
								o.getObjectType());
					}
				}
			}
		}

		if (c.distanceToPoint(2961, 3389) <= 60) {
			c.getPA().object(6552, 2961, 3389, -1, 10);
		}
	}

	/**
	 * Creates the object for anyone who is within 60 squares of the object
	 **/
	public void placeObject(Objects o) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				removeAllObjects(o);
				globalObjects.add(o);
				Player person = p;
				if (person != null) {
					if (person.heightLevel == o.getObjectHeight() && o.objectTicks == 0) {
						if (person.distanceToPoint(o.getObjectX(), o.getObjectY()) <= 60) {
							person.getPA().object(o.getObjectId(), o.getObjectX(), o.getObjectY(), o.getObjectFace(),
									o.getObjectType());
						}
					}
				}
			}
		}
	}

	public void removeAllObjects(Objects o) {
		for (Objects s : globalObjects) {
			if (s.getObjectX() == o.objectX && s.getObjectY() == o.objectY
					&& s.getObjectHeight() == o.getObjectHeight()) {
				globalObjects.remove(s);
				break;
			}
		}
	}

	public void createAnObject(Player c, int id, int x, int y) {
		Objects OBJECT = new Objects(id, x, y, 0, 0, 10, 0, false);
		if (id == -1) {
			removeObject(OBJECT);
		} else {
			addObject(OBJECT);
		}
		main.GameEngine.objectHandler.placeObject(OBJECT);
	}

	public void createAnObject(Player c, int id, int x, int y, int face) {
		Objects OBJECT = new Objects(id, x, y, 0, face, 10, 0, false);
		if (id == -1) {
			removeObject(OBJECT);
		} else {
			addObject(OBJECT);
		}
		placeObject(OBJECT);
	}

	public void process() {
		for (int j = 0; j < globalObjects.size(); j++) {
			if (globalObjects.get(j) != null) {
				Objects o = globalObjects.get(j);
				if (o.objectTicks > 0) {
					o.objectTicks--;
				}
				if (o.objectTicks == 1) {
					Objects deleteObject = objectExists(o.getObjectX(), o.getObjectY(), o.getObjectHeight());
					removeObject(deleteObject);
					o.objectTicks = 0;
					placeObject(o);
					removeObject(o);
					if (isObelisk(o.objectId)) {
						int index = getObeliskIndex(o.objectId);
						if (activated[index]) {
							activated[index] = false;
							teleportObelisk(index);
						}
					}
				}
			}

		}
	}

	@SuppressWarnings("unused")
	public boolean loadGlobalObjects(String fileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		int ReadMode = 0;
		BufferedReader objectFile = null;
		try {
			objectFile = new BufferedReader(new FileReader("./" + fileName));
		} catch (FileNotFoundException fileex) {
			Misc.println(fileName + ": file not found.");
			return false;
		}
		try {
			line = objectFile.readLine();
		} catch (IOException ioexception) {
			Misc.println(fileName + ": error loading file.");
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("object")) {
					Objects object = new Objects(Integer.parseInt(token3[0]), Integer.parseInt(token3[1]),
							Integer.parseInt(token3[2]), Integer.parseInt(token3[3]), Integer.parseInt(token3[4]),
							Integer.parseInt(token3[5]), 0, false);
					addObject(object);
				}
			} else {
				if (line.equals("[ENDOFOBJECTLIST]")) {
					try {
						objectFile.close();
					} catch (IOException ioexception) {
					}
					return true;
				}
			}
			try {
				line = objectFile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			objectFile.close();
		} catch (IOException ioexception) {
		}
		return false;
	}

	public final int IN_USE_ID = 14825;

	public boolean isObelisk(int id) {
		for (int j = 0; j < obeliskIds.length; j++) {
			if (obeliskIds[j] == id)
				return true;
		}
		return false;
	}

	public int[] obeliskIds = { 14829, 14830, 111235, 14828, 14826, 14831 };
	public int[][] obeliskCoords = { { 3154, 3618 }, { 3225, 3665 }, { 3033, 3730 }, { 3104, 3792 }, { 2978, 3864 },
			{ 3305, 3914 } };
	public boolean[] activated = { false, false, false, false, false, false };

	public void startObelisk(int obeliskId) {
		int index = getObeliskIndex(obeliskId);
		if (index >= 0) {
			if (!activated[index]) {
				activated[index] = true;
				Objects obby1 = new Objects(14825, obeliskCoords[index][0], obeliskCoords[index][1], 0, -1, 10, 0,
						false);
				Objects obby2 = new Objects(14825, obeliskCoords[index][0] + 4, obeliskCoords[index][1], 0, -1, 10, 0,
						false);
				Objects obby3 = new Objects(14825, obeliskCoords[index][0], obeliskCoords[index][1] + 4, 0, -1, 10, 0,
						false);
				Objects obby4 = new Objects(14825, obeliskCoords[index][0] + 4, obeliskCoords[index][1] + 4, 0, -1, 10,
						0, false);
				addObject(obby1);
				addObject(obby2);
				addObject(obby3);
				addObject(obby4);
				GameEngine.objectHandler.placeObject(obby1);
				GameEngine.objectHandler.placeObject(obby2);
				GameEngine.objectHandler.placeObject(obby3);
				GameEngine.objectHandler.placeObject(obby4);
				Objects obby5 = new Objects(obeliskIds[index], obeliskCoords[index][0], obeliskCoords[index][1], 0, -1,
						10, 10, false);
				Objects obby6 = new Objects(obeliskIds[index], obeliskCoords[index][0] + 4, obeliskCoords[index][1], 0,
						-1, 10, 10, false);
				Objects obby7 = new Objects(obeliskIds[index], obeliskCoords[index][0], obeliskCoords[index][1] + 4, 0,
						-1, 10, 10, false);
				Objects obby8 = new Objects(obeliskIds[index], obeliskCoords[index][0] + 4, obeliskCoords[index][1] + 4,
						0, -1, 10, 10, false);
				addObject(obby5);
				addObject(obby6);
				addObject(obby7);
				addObject(obby8);
			}
		}
	}

	public int getObeliskIndex(int id) {
		for (int j = 0; j < obeliskIds.length; j++) {
			if (obeliskIds[j] == id)
				return j;
		}
		return -1;
	}

	public void teleportObelisk(int port) {
		int random = Misc.random(5);
		while (random == port) {
			random = Misc.random(5);
		}
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c = PlayerHandler.players[j];
				if (c.goodDistance(c.getX(), c.getY(), obeliskCoords[port][0] + 2, obeliskCoords[port][1] + 2, 1)) {
					c.getPA().startTeleport2(obeliskCoords[random][0] + 2, obeliskCoords[random][1] + 2, 0);
				}
			}
		}
	}
}
