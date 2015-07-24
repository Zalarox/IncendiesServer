package main.game.players.content;

import main.GameEngine;
import main.event.CycleEvent;
import main.event.CycleEventContainer;
import main.event.CycleEventHandler;
import main.game.npcs.NPC;
import main.game.npcs.NPCHandler;
import main.game.objects.Objects;
import main.game.players.Player;
import main.util.Misc;
import main.world.ObjectHandler;

public class DwarfMultiCannon {

	/**
	 * To-Do: Exception when trying to set up a cannon within 3 coords of
	 * another one NPC distance checking Projectiles(Not sure if it works)
	 * 
	 * @author relex lawl / relex
	 */

	private Player player;

	public DwarfMultiCannon(Player player) {
		this.player = player;
	}

	private static final int CANNON_BASE = 7, CANNON_STAND = 8, CANNON_BARRELS = 9, CANNON = 6;
	private static final int CANNONBALL = 2, CANNON_BASE_ID = 6, CANNON_STAND_ID = 8, CANNON_BARRELS_ID = 10,
			CANNON_FURNACE_ID = 12;

	public boolean firstTime = false, stopped = false;

	public void setUpCannon() {
		if (!canSetUpCannon(player.absX, player.absY)) {
			player.sendMessage("You can't do that here!");
			return;
		}
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int time = 4;

			@Override
			public void execute(CycleEventContainer setup) {

				switch (time) {
				case 4:
					if (!player.getItems().playerHasItem(CANNON_BASE_ID))
						setup.stop();
					player.startAnimation(827);
					player.getVariables().cannonBaseX = player.absX;
					player.getVariables().cannonBaseY = player.absY;
					player.getVariables().cannonBaseH = player.heightLevel;
					player.getVariables().hasCannon = true;
					player.getVariables().settingUpCannon = true;
					player.getVariables().setUpBase = true;
					Objects base = new Objects(CANNON_BASE, player.absX, player.absY, 0, 0, 10, 0, true);
					GameEngine.objectHandler.addObject(base);
					GameEngine.objectHandler.placeObject(base);
					player.getVariables().oldCannon = base;
					player.getItems().deleteItem(CANNON_BASE_ID, 1);
					base.belongsTo = player.playerName;
					firstTime = true;
					break;

				case 3:
					if (!player.getItems().playerHasItem(CANNON_STAND_ID)) {
						player.getVariables().settingUpCannon = false;
						setup.stop();
					}
					player.startAnimation(827);
					player.getVariables().setUpStand = true;
					Objects stand = new Objects(CANNON_STAND, player.absX, player.absY, 0, 0, 10, 0, true);
					GameEngine.objectHandler.removeObject(player.getVariables().oldCannon);
					GameEngine.objectHandler.addObject(stand);
					GameEngine.objectHandler.placeObject(stand);
					player.getVariables().oldCannon = stand;
					player.getItems().deleteItem(CANNON_STAND_ID, 1);
					stand.belongsTo = player.playerName;
					firstTime = true;
					break;

				case 2:
					if (!player.getItems().playerHasItem(CANNON_BARRELS_ID)) {
						player.getVariables().settingUpCannon = false;
						setup.stop();
					}
					player.startAnimation(827);
					player.getVariables().setUpBarrels = true;
					Objects barrel = new Objects(CANNON_BARRELS, player.absX, player.absY, 0, 0, 10, 0, true);
					GameEngine.objectHandler.removeObject(player.getVariables().oldCannon);
					GameEngine.objectHandler.addObject(barrel);
					GameEngine.objectHandler.placeObject(barrel);
					player.getVariables().oldCannon = barrel;
					player.getItems().deleteItem(CANNON_BARRELS_ID, 1);
					barrel.belongsTo = player.playerName;
					firstTime = true;
					break;

				case 1:
					if (!player.getItems().playerHasItem(CANNON_FURNACE_ID)) {
						player.getVariables().settingUpCannon = false;
						setup.stop();
					}
					player.startAnimation(827);
					player.getVariables().setUpFurnace = true;
					Objects cannon = new Objects(CANNON, player.absX, player.absY, 0, 0, 10, 0, true);
					GameEngine.objectHandler.removeObject(player.getVariables().oldCannon);
					GameEngine.objectHandler.addObject(cannon);
					GameEngine.objectHandler.placeObject(cannon);
					player.getVariables().oldCannon = cannon;
					player.getItems().deleteItem(CANNON_FURNACE_ID, 1);
					cannon.belongsTo = player.playerName;
					player.getVariables().settingUpCannon = false;
					firstTime = true;
					setup.stop();
					break;

				}
				if (time > 0)
					time--;
			}

			@Override
			public void stop() {

			}
		}, 3);
	}

	public void shootCannon(int x, int y, int h) {
		Objects cannon = null;
		for (main.game.objects.Objects o : ObjectHandler.globalObjects) {
			if (o.objectX == player.getVariables().cannonBaseX && o.objectY == player.getVariables().cannonBaseY
					&& o.objectHeight == player.getVariables().cannonBaseH) {
				cannon = o;
			}
		}
		if (cannon == null) {
			player.sendMessage("This is not your cannon!");
			return;
		}
		if (cannon != null && (cannon.belongsTo != player.playerName || cannon.objectX != x || cannon.objectY != y
				|| cannon.objectHeight != h)) {
			player.sendMessage("This is not your cannon!");
			return;
		}
		if (player.getVariables().cannonIsShooting) {
			if (player.getItems().playerHasItem(CANNONBALL)) {
				int amountOfCannonBalls = player.getItems().getItemAmount(CANNONBALL) > 30 ? 30
						: player.getItems().getItemAmount(CANNONBALL);
				player.getVariables().cannonBalls += amountOfCannonBalls;
				player.sendMessage("You reload the cannon.");
			} else {
				player.sendMessage("Your cannon is already firing!");
				return;
			}
		}
		if (player.getVariables().cannonBalls < 1) {
			int amountOfCannonBalls = player.getItems().getItemAmount(CANNONBALL) > 30 ? 30
					: player.getItems().getItemAmount(CANNONBALL);
			if (amountOfCannonBalls < 1) {
				player.sendMessage("You need ammo to shoot this cannon!");
				return;
			}
			player.getVariables().cannonBalls = amountOfCannonBalls;
			player.sendMessage("You load your cannon with ammo.");
			player.getItems().deleteItem(CANNONBALL, player.getItems().getItemSlot(CANNONBALL), amountOfCannonBalls);
			if (firstTime) {
				startFiringCannon(cannon);
			}
		} else
			startFiringCannon(cannon);
	}

	private void startFiringCannon(final Objects cannon) {
		player.getVariables().cannonIsShooting = true;
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer fire) {
				if (player.getVariables().cannonBalls < 1) {
					player.sendMessage("Your cannon has run out of ammo!");
					player.getVariables().cannonIsShooting = false;
					fire.stop();
				} else {
					player.getVariables().rotation++;
					rotateCannon(cannon);
				}
			}

			@Override
			public void stop() {

			}
		}, (player.getVariables().inMulti() ? 2 : 4));
	}

	private void rotateCannon(Objects cannon) {
		switch (player.getVariables().rotation) {
		case 1: // north
			player.getPA().objectAnim(cannon.objectX, cannon.objectY, 516, 10, -1);
			break;
		case 2: // north-east
			player.getPA().objectAnim(cannon.objectX, cannon.objectY, 517, 10, -1);
			break;
		case 3: // east
			player.getPA().objectAnim(cannon.objectX, cannon.objectY, 518, 10, -1);
			break;
		case 4: // south-east
			player.getPA().objectAnim(cannon.objectX, cannon.objectY, 519, 10, -1);
			break;
		case 5: // south
			player.getPA().objectAnim(cannon.objectX, cannon.objectY, 520, 10, -1);
			break;
		case 6: // south-west
			player.getPA().objectAnim(cannon.objectX, cannon.objectY, 521, 10, -1);
			break;
		case 7: // west
			player.getPA().objectAnim(cannon.objectX, cannon.objectY, 514, 10, -1);
			break;
		case 8: // north-west
			player.getPA().objectAnim(cannon.objectX, cannon.objectY, 515, 10, -1);
			player.getVariables().rotation = 0;
			break;
		}
	}

	public void pickUpCannon(int x, int y, int h) {
		Objects cannon = null;
		for (main.game.objects.Objects o : ObjectHandler.globalObjects) {
			if (o.objectX == player.getVariables().cannonBaseX && o.objectY == player.getVariables().cannonBaseY
					&& o.objectHeight == player.getVariables().cannonBaseH) {
				cannon = o;
			}
		}
		if (cannon != null && (cannon.belongsTo != player.playerName || cannon.objectX != x || cannon.objectY != y
				|| cannon.objectHeight != h)) {
			player.sendMessage("This is not your cannon!");
			return;
		}
		if (cannon != null) {
			stopped = true;
			player.startAnimation(827);
			main.game.objects.Objects empty = new main.game.objects.Objects(100, cannon.objectX, cannon.objectY, 0, 0,
					10, 0, true);
			GameEngine.objectHandler.addObject(empty);
			GameEngine.objectHandler.placeObject(empty);
			GameEngine.objectHandler.removeObject(empty);
			if (player.getVariables().setUpBase) {
				if (player.getItems().freeSlots() > 0)
					player.getItems().addItem(CANNON_BASE_ID, 1);
				else {
					player.getItems().addItemToBank(CANNON_BASE_ID, 1);
					player.sendMessage("You did not have enough inventory space, so this cannon part was banked.");
				}
				player.getVariables().setUpBase = false;
			}
			if (player.getVariables().setUpStand) {
				if (player.getItems().freeSlots() > 0)
					player.getItems().addItem(CANNON_STAND_ID, 1);
				else {
					player.getItems().addItemToBank(CANNON_STAND_ID, 1);
					player.sendMessage("You did not have enough inventory space, so this cannon part was banked.");
				}
				player.getVariables().setUpStand = false;
			}
			if (player.getVariables().setUpBarrels) {
				if (player.getItems().freeSlots() > 0)
					player.getItems().addItem(CANNON_BARRELS_ID, 1);
				else {
					player.getItems().addItemToBank(CANNON_BARRELS_ID, 1);
					player.sendMessage("You did not have enough inventory space, so this cannon part was banked.");
				}
				player.getVariables().setUpBarrels = false;
			}
			if (player.getVariables().setUpFurnace) {
				if (player.getItems().freeSlots() > 0)
					player.getItems().addItem(CANNON_FURNACE_ID, 1);
				else {
					player.getItems().addItemToBank(CANNON_FURNACE_ID, 1);
					player.sendMessage("You did not have enough inventory space, so this cannon part was banked.");
				}
				player.getVariables().setUpFurnace = false;
			}
			if (player.getVariables().cannonBalls > 0) {
				if (player.getItems().freeSlots() > 0)
					player.getItems().addItem(CANNONBALL, player.getVariables().cannonBalls);
				else {
					player.getItems().addItemToBank(CANNONBALL, player.getVariables().cannonBalls);
					player.sendMessage(
							"You did not have enough inventory space, so your cannonballs have been banked.");
				}
				player.getVariables().cannonBalls = 0;
			}
			firstTime = false;
		}
	}

	public static void checkNPCDistance(Player player) {
		NPC n = getNPCWithinDistance(player, player.getVariables().cannonBaseX, player.getVariables().cannonBaseY,
				player.getVariables().cannonBaseH);
		int damage = Misc.random(300);
		if (n != null && player.getVariables().inMulti() && n.inMulti()) {
			startCannonballProjectile(player, player.getVariables().oldCannon, n);
			n.hitIcon = 1;
			n.hitDiff = damage;
			n.HP -= damage;
			n.hitUpdateRequired = true;
			n.killerId = player.playerId;
			n.facePlayer(player.playerId);
			player.getVariables().cannonBalls--;
		} else {// TODO Fixup the attacking in single zones to only attack one
			if (player != null && n != null && (n != null && n.killerId == 0 && player.getVariables().killerId == 0)
					|| (n != null && n.killerId == player.playerId))
				return;
			if (n != null) {
				startCannonballProjectile(player, player.getVariables().oldCannon, n);
				n.hitIcon = 1;
				n.hitDiff = damage;
				n.HP -= damage;
				n.hitUpdateRequired = true;
				n.killerId = player.playerId;
				n.facePlayer(player.playerId);
				player.getVariables().cannonBalls--;
			}
		}
	}

	private static NPC getNPCWithinDistance(Player players, int x, int y, int height) {
		NPC npc = null;
		for (int i = 0; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] != null) {
				npc = NPCHandler.npcs[i];
				int myX = players.getVariables().cannonBaseX;
				int myY = players.getVariables().cannonBaseY;
				int theirX = npc.absX;
				int theirY = npc.absY;
				if (NPCHandler.goodDistance(npc.getX(), npc.getY(), myX, myY, 20) && !npc.isDead
						&& npc.heightLevel == height && !npc.isDead && npc.HP != 0 && npc.npcType != 1266
						&& npc.npcType != 1268) {
					switch (players.getVariables().rotation) {
					case 1: // north
						if (theirY > myY && theirX >= myX - 1 && theirX <= myX + 1)
							return npc;
						break;
					case 2: // north-east
						if (theirX >= myX + 1 && theirY >= myY + 1)
							return npc;
						break;
					case 3: // east
						if (theirX > myX && theirY >= myY - 1 && theirY <= myY + 1)
							return npc;
						break;
					case 4: // south-east
						if (theirY <= myY - 1 && theirX >= myX + 1)
							return npc;
						break;
					case 5: // south
						if (theirY < myY && theirX >= myX - 1 && theirX <= myX + 1)
							return npc;
						break;
					case 6: // south-west
						if (theirX <= myX - 1 && theirY <= myY - 1)
							return npc;
						break;
					case 7: // west
						if (theirX < myX && theirY >= myY - 1 && theirY <= myY + 1)
							return npc;
						break;
					case 8: // north-west
						if (theirX <= myX - 1 && theirY >= myY + 1)
							return npc;
						break;
					}
				}
			}
		}
		return null;
	}

	private static void startCannonballProjectile(Player player, Objects cannon, NPC n) {
		int oX = cannon.objectX + 1;
		int oY = cannon.objectY + 1;
		if (n != null) {
			int offX = ((oX - n.getX()) * -1);
			int offY = ((oY - n.getY()) * -1);
			player.getPA().createPlayersProjectile(oX, oY, offY, offX, 50, 60, 53, 40, 20,
					-player.getVariables().oldNpcIndex + 1, 30);
		}
	}

	public static int distanceToSquare(int x, int y, int tx, int ty) {
		return (int) Math.sqrt((Math.abs(x - tx) + Math.abs(y - ty)));
	}

	private final boolean canSetUpCannon(int x, int y) {
		return inGoodArea(x, y) || player.getVariables().playerLevel[3] > 0 || player.getVariables().hasCannon
				|| !player.getVariables().settingUpCannon;
	}

	private final boolean inGoodArea(int x, int y) {
		boolean inGoodArea = true;
		for (Objects object : ObjectHandler.globalObjects) {
			player.sendMessage(
					"ObjectX: " + object.objectX + " ObjectY: " + object.objectY + " Your X:" + x + " Your Y: " + y);
			if (object.objectX == x && object.objectY == y)
				inGoodArea = false;
			if (object.isCannon && object.objectX + 1 == x && object.objectY == y)
				inGoodArea = false;
			if (object.isCannon && object.objectX + 2 == x && object.objectY == y)
				inGoodArea = false;
			if (object.isCannon && object.objectX == x && object.objectY + 1 == y)
				inGoodArea = false;
			if (object.isCannon && object.objectX + 1 == x && object.objectY + 1 == y)
				inGoodArea = false;
			if (object.isCannon && object.objectX + 2 == x && object.objectY + 1 == y)
				inGoodArea = false;
			if (object.isCannon && object.objectX == x && object.objectY + 2 == y)
				inGoodArea = false;
			if (object.isCannon && object.objectX + 1 == x && object.objectY + 2 == y)
				inGoodArea = false;
			if (object.isCannon && object.objectX + 2 == x && object.objectY + 2 == y)
				inGoodArea = false;
		}
		return inGoodArea;
	}
}