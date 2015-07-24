package main.world;

import java.util.ArrayList;
import java.util.LinkedList;

import main.GameEngine;
import main.game.npcs.NPCHandler;
import main.game.objects.Object;
import main.game.objects.Objects;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.handlers.ItemHandler;
import main.util.Misc;

/**
 * @author Sanity
 */

public class ObjectManager {

	public static ArrayList<Point3D> objectMap = new ArrayList<Point3D>();
	public static ArrayList<Object> objects = new ArrayList<Object>();
	private static ArrayList<Object> toRemove = new ArrayList<Object>();

	public void process() {
		for (final Object o : objects) {
			if (o.tick > 0) {
				o.tick--;
			} else {
				updateObject(o);
				toRemove.add(o);
			}
		}
		for (final Object o : toRemove) {
			if (o.objectId == 2732) {
				for (final Player player : PlayerHandler.players) {
					if (player != null) {
						final Player c = player;
						ItemHandler.createGroundItem(c, 592, o.objectX, o.objectY, o.height, 1, c.playerId);
					}
				}
			}
			if (isObelisk(o.newId)) {
				final int index = getObeliskIndex(o.newId);
				if (activated[index]) {
					activated[index] = false;
					teleportObelisk(index);
				}
			}
			objects.remove(o);
		}
		toRemove.clear();
	}/*
		 * public void process() { for (Object o : objects) { if (o.tick > 0) {
		 * o.tick--; } else { updateObject(o); toRemove.add(o); } } for (Object
		 * o : toRemove) { if (isObelisk(o.newId)) { int index =
		 * getObeliskIndex(o.newId); if (activated[index]) { activated[index] =
		 * false; teleportObelisk(index); } } objects.remove(o); }
		 * toRemove.clear(); }
		 */

	public void removeObject(int x, int y) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c = PlayerHandler.players[j];
				c.getPA().object(-1, x, y, 0, 10);
			}
		}
	}

	public void updateObject(Object o) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c = PlayerHandler.players[j];
				c.getPA().object(o.newId, o.objectX, o.objectY, o.face, o.type);
			}
		}
	}

	public void placeObject(Object o) {
		for (int j = 0; j < PlayerHandler.getPlayers().length; j++) {
			if (PlayerHandler.getPlayers()[j] != null) {
				Player c = PlayerHandler.getPlayers()[j];
				if (c.distanceToPoint(o.objectX, o.objectY) <= 60)
					c.getPA().object(o.objectId, o.objectX, o.objectY, o.face, o.type);
			}
		}
	}

	public Object getObject(int x, int y, int height) {
		for (Object o : objects) {
			if (o.objectX == x && o.objectY == y && o.height == height)
				return o;
		}
		return null;
	}

	public void loadObjects(Player c) {
		if (c == null)
			return;
		for (Object o : objects) {
			if (loadForPlayer(o, c))
				c.getPA().object(o.objectId, o.objectX, o.objectY, o.face, o.type);
		}
		loadCustomSpawns(c);

	}
	

	public void loadCustomSpawns(Player c) {
		// hunter
		c.getPA().checkObjectSpawn(19652, 3537, 3451, 1, 10);
		c.getPA().checkObjectSpawn(19652, 3538, 3446, 0, 10);
		c.getPA().checkObjectSpawn(19652, 3532, 3446, 0, 10); // <---
		c.getPA().checkObjectSpawn(19652, 3553, 3446, 0, 10); // Swamp Lizards
		c.getPA().checkObjectSpawn(19652, 3554, 3453, 0, 10); // Swamp Lizards
		c.getPA().checkObjectSpawn(19652, 3555, 3451, 0, 10); // Swamp Lizards

		c.getPA().checkObjectSpawn(19205, 2787, 3766, 0, 10); // Rock
		c.getPA().checkObjectSpawn(19205, 2782, 3763, 0, 10); // Rock
		c.getPA().checkObjectSpawn(19205, 2787, 3759, 0, 10); //
		c.getPA().checkObjectSpawn(19205, 2781, 3767, 0, 10); // Rock
		// Donator zone
		c.getPA().checkObjectSpawn(4008, 2395, 9902, 1, 10);
		c.getPA().checkObjectSpawn(26972, 2391, 9907, 2, 10);
		c.getPA().checkObjectSpawn(26972, 2392, 9907, 2, 10);
		c.getPA().checkObjectSpawn(26972, 2393, 9907, 2, 10);
		c.getPA().checkObjectSpawn(26972, 2394, 9907, 2, 10);
		c.getPA().checkObjectSpawn(26972, 2390, 9907, 2, 10);
		c.getPA().checkObjectSpawn(2562, 2395, 9893, 3, 10);
		c.getPA().checkObjectSpawn(6442, 2384, 9898, 3, 10);
		/* End of Donator Zone */
		c.getPA().checkObjectSpawn(-1, 3103, 9909, 0, 0);
		c.getPA().checkObjectSpawn(-1, 3101, 9910, 0, 0);
		c.getPA().checkObjectSpawn(-1, 2543, 10143, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2541, 10141, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2545, 10145, 0, 10);
		/* Skilling teleport objects */
		c.getPA().checkObjectSpawn(26972, 3046, 9778, 1, 10);// Mining area bank
		c.getPA().checkObjectSpawn(26972, 3081, 9502, 0, 10);
		c.getPA().checkObjectSpawn(26972, 3080, 9502, 0, 10);// Banks in
																// smithing area
		c.getPA().checkObjectSpawn(26972, 2330, 3691, 0, 10);
		c.getPA().checkObjectSpawn(26972, 2329, 3691, 0, 10);
		c.getPA().checkObjectSpawn(26972, 2328, 3691, 0, 10);// Banks at fishing
																// colony
		c.getPA().checkObjectSpawn(1306, 2403, 3434, 0, 10);
		c.getPA().checkObjectSpawn(1306, 2399, 3431, 0, 10);
		c.getPA().checkObjectSpawn(1306, 2394, 3427, 0, 10);
		c.getPA().checkObjectSpawn(1306, 2394, 3435, 0, 10);
		c.getPA().checkObjectSpawn(1306, 2401, 3443, 0, 10);
		c.getPA().checkObjectSpawn(1306, 2393, 3439, 0, 10);
		c.getPA().checkObjectSpawn(1306, 2405, 3439, 0, 10);// Magic trees at
															// the gnome wcing
															// area
		c.getPA().checkObjectSpawn(26972, 2389, 3427, 1, 10);
		c.getPA().checkObjectSpawn(26972, 2389, 3428, 1, 10);// Banks at gnome
																// wcing area
		c.getPA().checkObjectSpawn(14859, 3044, 9748, 0, 10);// runite ore
		c.getPA().checkObjectSpawn(14859, 3045, 9748, 0, 10);// runite ore
		c.getPA().checkObjectSpawn(14859, 3045, 9747, 0, 10);// runite ore
		c.getPA().checkObjectSpawn(14859, 3044, 9747, 0, 10);// runite ore
		c.getPA().checkObjectSpawn(14859, 3038, 9744, 0, 10);// runite ore
		c.getPA().checkObjectSpawn(14859, 3038, 9745, 0, 10);// runite ore
		c.getPA().checkObjectSpawn(14859, 3042, 9751, 0, 10);// runite ore
		c.getPA().checkObjectSpawn(14859, 3052, 9747, 0, 10);// runite ore
		c.getPA().checkObjectSpawn(14859, 3054, 9744, 0, 10);// runite ore
		c.getPA().checkObjectSpawn(14859, 3047, 9740, 0, 10);// runite ore
		c.getPA().checkObjectSpawn(14859, 3044, 9735, 0, 10);// runite ore
		c.getPA().checkObjectSpawn(14859, 3034, 9737, 0, 10);// runite ore
		c.getPA().checkObjectSpawn(2728, 2330, 3686, 3, 10);// Cooking range
		/* End of skilling objects */
		// Edgeville Home
		c.getPA().checkObjectSpawn(6552, 3096, 3506, 2, 10);// Magic altar
		c.getPA().checkObjectSpawn(409, 3092, 3506, 0, 10);// Prayer altar
		c.getPA().checkObjectSpawn(411, 3094, 3500, 2, 10);// Curses altar
		c.getPA().checkObjectSpawn(2561, 3083, 3488, 1, 10);// Stall 1
		c.getPA().checkObjectSpawn(2563, 3083, 3492, 1, 10);// Stall 2
		c.getPA().checkObjectSpawn(2565, 3083, 3496, 1, 10);// Stall 3
		c.getPA().checkObjectSpawn(6282, 3084, 3483, 2, 10);// Donator zone
															// portal
		c.getPA().checkObjectSpawn(2273, 2912, 5299, 0, 10);
		// Barrows chest
		// c.getActionSender().checkObjectSpawn(10284, 3564, 3289, 0, 10);
		for (int i = 0; i < 5; i++)
			c.getPA().checkObjectSpawn(1814, 3090, 3475, 0, i);
		reloadWorldObjects(c);
		if (c.party != null && c.party.floor != null) {
			for (int i = 0; i < main.game.players.content.skills.dungeoneering.Constants.OBJECT[c.party.floor.roomId].length; i++) {
				c.getPA().checkObjectSpawn(
						main.game.players.content.skills.dungeoneering.Constants.OBJECT[c.party.floor.roomId][i][0],
						main.game.players.content.skills.dungeoneering.Constants.OBJECT[c.party.floor.roomId][i][1],
						main.game.players.content.skills.dungeoneering.Constants.OBJECT[c.party.floor.roomId][i][2],
						main.game.players.content.skills.dungeoneering.Constants.OBJECT[c.party.floor.roomId][i][3],
						main.game.players.content.skills.dungeoneering.Constants.OBJECT[c.party.floor.roomId][i][4]);
			}
		}
	}

	public void reloadWorldObjects(Player player) {
		LinkedList<Objects> objectsToPlace = new LinkedList<Objects>();
		for (Objects object : ObjectHandler.globalObjects) {
			if (object != null
					&& NPCHandler.goodDistance(object.objectX, object.objectY, player.absX, player.absY, 36)) {
				objectsToPlace.add(object);
			}
		}
		for (Objects object : objectsToPlace) {
			GameEngine.objectHandler.placeObject(object);
		}
	}

	public final int IN_USE_ID = 14825;

	public boolean isObelisk(int id) {
		for (int j = 0; j < obeliskIds.length; j++) {
			if (obeliskIds[j] == id)
				return true;
		}
		return false;
	}

	public int[] obeliskIds = { 14829, 14830, 14827, 14828, 14826, 14831 };
	public int[][] obeliskCoords = { { 3154, 3618 }, { 3225, 3665 }, { 3033, 3730 }, { 3104, 3792 }, { 2978, 3864 },
			{ 3305, 3914 } };
	public boolean[] activated = { false, false, false, false, false, false };

	public void startObelisk(int obeliskId) {
		int index = getObeliskIndex(obeliskId);
		if (index >= 0) {
			if (!activated[index]) {
				activated[index] = true;
				addObject(
						new Object(14825, obeliskCoords[index][0], obeliskCoords[index][1], 0, -1, 10, obeliskId, 16));
				addObject(new Object(14825, obeliskCoords[index][0] + 4, obeliskCoords[index][1], 0, -1, 10, obeliskId,
						16));
				addObject(new Object(14825, obeliskCoords[index][0], obeliskCoords[index][1] + 4, 0, -1, 10, obeliskId,
						16));
				addObject(new Object(14825, obeliskCoords[index][0] + 4, obeliskCoords[index][1] + 4, 0, -1, 10,
						obeliskId, 16));
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
				int xOffset = c.absX - obeliskCoords[port][0];
				int yOffset = c.absY - obeliskCoords[port][1];
				if (c.goodDistance(c.getX(), c.getY(), obeliskCoords[port][0] + 2, obeliskCoords[port][1] + 2, 1)) {
					c.getPA().startTeleport2(obeliskCoords[random][0] + xOffset, obeliskCoords[random][1] + yOffset, 0);
				}
			}
		}
	}

	public boolean loadForPlayer(Object o, Player c) {
		if (o == null || c == null)
			return false;
		return c.distanceToPoint(o.objectX, o.objectY) <= 60 && c.heightLevel == o.height;
	}

	public void addObject(Object o) {
		if (getObject(o.objectX, o.objectY, o.height) == null) {
			objects.add(o);
			placeObject(o);
		}
	}

	public static class Point3D {

		private int x;
		private int y;
		private int z;

		public Point3D(int x, int y, int z) {
			this.x = (x);
			this.y = (y);
			this.z = (z);
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getZ() {
			return z;
		}
	}

	public boolean objectExists(final int x, final int y) {
		for (Object o : objects) {
			if (o.objectX == x && o.objectY == y) {
				return true;
			}
		}
		return false;
	}
}