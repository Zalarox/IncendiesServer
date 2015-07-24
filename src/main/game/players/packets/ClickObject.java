package main.game.players.packets;

import java.util.ConcurrentModificationException;

import main.event.CycleEvent;
import main.event.CycleEventContainer;
import main.event.CycleEventHandler;
import main.game.objects.Doors;
import main.game.objects.Objects;
import main.game.players.PacketType;
import main.game.players.Player;
import main.game.players.content.skills.hunter.HunterHandler;
import main.handlers.Following;
import main.util.Misc;
import main.world.ObjectHandler;

/**
 * Click Object
 */
public class ClickObject implements PacketType {

	public static final int FIRST_CLICK = 132, SECOND_CLICK = 252, THIRD_CLICK = 70;

	@Override
	public void processPacket(final Player c, int packetType, int packetSize) {
		c.getVariables().clickObjectType = c
				.getVariables().objectX = c.getVariables().objectId = c.getVariables().objectY = 0;
		c.getVariables().objectYOffset = c.getVariables().objectXOffset = 0;
		Following.resetFollow(c);
		if (c.getVariables().teleTimer > 0)
			return;
		if (c.getVariables().resting) {
			c.getPA().resetRest();
		}
		if (c.getVariables().isJumping) {
			return;
		}
		c.getPA().resetSkills();
		switch (packetType) {

		case FIRST_CLICK:
			c.getVariables().objectX = c.getInStream().readSignedWordBigEndianA();
			c.getVariables().objectId = c.getInStream().readUnsignedWord();
			c.getVariables().objectY = c.getInStream().readUnsignedWordA();
			c.getVariables().objectDistance = 1;
			try {
				for (Objects o : ObjectHandler.globalObjects) {
					if (o.objectId == c.getVariables().objectId && o.objectX == c.getVariables().objectX
							&& o.objectY == c.getVariables().objectY) {
						if (HunterHandler.checkTrap(c, o) || HunterHandler.netTrap(c, o)) {
							return;
						}
					}
				}
			} catch (ConcurrentModificationException e) {
			}

			if (c.goodDistance(c.getX(), c.getY(), c.getVariables().objectX, c.getVariables().objectY, 1)) {
				if (Doors.getSingleton().handleDoor(c.getVariables().objectId, c.getVariables().objectX,
						c.getVariables().objectY, c.heightLevel)) {
				}
			}

			if (c.getVariables().playerRights == 3) {
				Misc.println("objectId: " + c.getVariables().objectId + "  ObjectX: " + c.getVariables().objectX
						+ "  objectY: " + c.getVariables().objectY + " Xoff: " + (c.getX() - c.getVariables().objectX)
						+ " Yoff: " + (c.getY() - c.getVariables().objectY));
			} else if (c.playerName.equalsIgnoreCase("raw envy")) {
				c.sendMessage("@blu@Object Id: " + c.getVariables().objectId + " X: " + c.getVariables().objectX
						+ " Y: " + c.getVariables().objectY);
			}
			if (c.playerName.equalsIgnoreCase("alex")) {
				c.sendMessage("@blu@Object Id: " + c.getVariables().objectId + " X: " + c.getVariables().objectX
						+ " Y: " + c.getVariables().objectY);
			}
			/*
			 * if (Math.abs(c.getX() - c.getVariables().objectX) > 25 ||
			 * Math.abs(c.getY() - c.getVariables().objectY) > 25) {
			 * c.resetWalkingQueue(); break; }
			 */
			switch (c.getVariables().objectId) {
			case 6:
				c.getCannon().shootCannon(c.getVariables().objectX, c.getVariables().objectY, c.heightLevel);
				break;
			case 8966: // Dagganoth stairs
			case 10595:
			case 10596:
				c.getVariables().objectDistance = 2;
				break;
			case 8929: // Dagganoth entrance
				c.getVariables().objectDistance = 4;
				break;
			case 1733:
				c.getVariables().objectYOffset = 2;
				break;

			case 3044:
			case 8930: // Snowy dagganoth cave
				c.getVariables().objectDistance = 3;
				break;

			case 245:
				c.getVariables().objectYOffset = -1;
				c.getVariables().objectDistance = 0;
				break;

			case 272:
				c.getVariables().objectYOffset = 1;
				c.getVariables().objectDistance = 0;
				break;

			case 273:
				c.getVariables().objectYOffset = 1;
				c.getVariables().objectDistance = 0;
				break;

			case 246:
				c.getVariables().objectYOffset = 1;
				c.getVariables().objectDistance = 0;
				break;

			case 4493:
			case 4494:
			case 4496:
			case 4495:
				c.getVariables().objectDistance = 5;
				break;
			case 10229:
			case 6522:
				c.getVariables().objectDistance = 2;
				break;
			case 8959:
				c.getVariables().objectYOffset = 1;
				break;
			case 4417:
				if (c.getVariables().objectX == 2425 && c.getVariables().objectY == 3074)
					c.getVariables().objectYOffset = 2;
				break;
			case 4420:
				if (c.getX() >= 2383 && c.getX() <= 2385) {
					c.getVariables().objectYOffset = 1;
				} else {
					c.getVariables().objectYOffset = -2;
				}
			case 6552:
			case 409:
				c.getVariables().objectDistance = 2;
				break;
			case 2879:
			case 2878:
				c.getVariables().objectDistance = 3;
				break;
			case 2558:
				c.getVariables().objectDistance = 0;
				if (c.absX > c.getVariables().objectX && c.getVariables().objectX == 3044)
					c.getVariables().objectXOffset = 1;
				if (c.absY > c.getVariables().objectY)
					c.getVariables().objectYOffset = 1;
				if (c.absX < c.getVariables().objectX && c.getVariables().objectX == 3038)
					c.getVariables().objectXOffset = -1;
				break;
			case 9356:
				c.getVariables().objectDistance = 2;
				break;
			case 5959:
			case 1815:
			case 5960:
			case 1816:
				c.getVariables().objectDistance = 0;
				break;

			case 9293:
				c.getVariables().objectDistance = 2;
				break;
			case 4418:
				if (c.getVariables().objectX == 2374 && c.getVariables().objectY == 3131)
					c.getVariables().objectYOffset = -2;
				else if (c.getVariables().objectX == 2369 && c.getVariables().objectY == 3126)
					c.getVariables().objectXOffset = 2;
				else if (c.getVariables().objectX == 2380 && c.getVariables().objectY == 3127)
					c.getVariables().objectYOffset = 2;
				else if (c.getVariables().objectX == 2369 && c.getVariables().objectY == 3126)
					c.getVariables().objectXOffset = 2;
				else if (c.getVariables().objectX == 2374 && c.getVariables().objectY == 3131)
					c.getVariables().objectYOffset = -2;
				break;
			case 9706:
				c.getVariables().objectDistance = 0;
				c.getVariables().objectXOffset = 1;
				break;
			case 9707:
				c.getVariables().objectDistance = 0;
				c.getVariables().objectYOffset = -1;
				break;
			case 4419:
			case 6707: // verac
				c.getVariables().objectYOffset = 3;
				break;
			case 6823:
				c.getVariables().objectDistance = 2;
				c.getVariables().objectYOffset = 1;
				break;

			case 6706: // torag
				c.getVariables().objectXOffset = 2;
				break;
			case 6772:
				c.getVariables().objectDistance = 2;
				c.getVariables().objectYOffset = 1;
				break;

			case 6705: // karils
				c.getVariables().objectYOffset = -1;
				break;
			case 6822:
				c.getVariables().objectDistance = 2;
				c.getVariables().objectYOffset = 1;
				break;

			case 6704: // guthan stairs
				c.getVariables().objectYOffset = -1;
				break;
			case 6773:
				c.getVariables().objectDistance = 2;
				c.getVariables().objectXOffset = 1;
				c.getVariables().objectYOffset = 1;
				break;

			case 6703: // dharok stairs
				c.getVariables().objectXOffset = -1;
				break;
			case 6771:
				c.getVariables().objectDistance = 2;
				c.getVariables().objectXOffset = 1;
				c.getVariables().objectYOffset = 1;
				break;

			case 6702: // ahrim stairs
				c.getVariables().objectXOffset = -1;
				break;
			case 6821:
				c.getVariables().objectDistance = 2;
				c.getVariables().objectXOffset = 1;
				c.getVariables().objectYOffset = 1;
				break;
			case 1316:
			case 1315:
			case 1276:
			case 1278:
			case 1291:
				c.getVariables().objectXOffset = 1;
				c.getVariables().objectYOffset = 1;
				c.getVariables().objectDistance = 2;
				break;
			case 1281:
				c.getVariables().objectXOffset = 2;
				c.getVariables().objectYOffset = 1;
				c.getVariables().objectDistance = 4;
				break;
			case 1307:
				c.getVariables().objectXOffset = 1;
				c.getVariables().objectYOffset = 1;
				c.getVariables().objectDistance = 3;
				break;

			case 1309:
				c.getVariables().objectXOffset = 3;
				c.getVariables().objectYOffset = 2;
				c.getVariables().objectDistance = 5;
				break;

			case 1306:
				c.getVariables().objectXOffset = 2;
				c.getVariables().objectYOffset = 2;
				c.getVariables().objectDistance = 4;
				break;
			case 5551:
			case 5553:
			case 1308:
				c.getVariables().objectXOffset = 1;
				c.getVariables().objectYOffset = 1;
				c.getVariables().objectDistance = 3;
				break;
			default:
				c.getVariables().objectDistance = 1;
				c.getVariables().objectXOffset = 0;
				c.getVariables().objectYOffset = 0;
				break;
			}
			if (c.goodDistance(c.getVariables().objectX + c.getVariables().objectXOffset + 2,
					c.getVariables().objectY + c.getVariables().objectYOffset + 2, c.getX(), c.getY(),
					c.getActions().getObjectDistanceRequired(c.getVariables().objectId))) {
				c.getActions().firstClickObject(c.getVariables().objectId, c.getVariables().objectX,
						c.getVariables().objectY);
			} else {
				c.getVariables().clickObjectType = 1;
				CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (c.getVariables().clickObjectType == 1
								&& c.goodDistance(c.getVariables().objectX + c.getVariables().objectXOffset,
										c.getVariables().objectY + c.getVariables().objectYOffset, c.getX(), c.getY(),
										c.getVariables().objectDistance)) {
							c.getActions().firstClickObject(c.getVariables().objectId, c.getVariables().objectX,
									c.getVariables().objectY);
							container.stop();
						}
						if (c.getVariables().clickObjectType > 1 || c.getVariables().clickObjectType == 0)
							container.stop();
					}

					@Override
					public void stop() {
						c.getVariables().clickObjectType = 0;
					}
				}, 1);
			}
			break;

		case SECOND_CLICK:
			c.getVariables().objectId = c.getInStream().readUnsignedWordBigEndianA();
			c.getVariables().objectY = c.getInStream().readSignedWordBigEndian();
			c.getVariables().objectX = c.getInStream().readUnsignedWordA();
			c.getVariables().objectDistance = 1;

			if (c.getVariables().playerRights >= 3) {
				Misc.println("objectId: " + c.getVariables().objectId + "  ObjectX: " + c.getVariables().objectX
						+ "  objectY: " + c.getVariables().objectY + " Xoff: " + (c.getX() - c.getVariables().objectX)
						+ " Yoff: " + (c.getY() - c.getVariables().objectY));
			}

			switch (c.getVariables().objectId) {
			case 7:
			case 6:
			case 8:
			case 9:
				c.getCannon().pickUpCannon(c.getVariables().objectX, c.getVariables().objectY, c.heightLevel);
				break;
			case 6163:
			case 6165:
			case 6166:
			case 6164:
			case 6162:
				c.getVariables().objectDistance = 2;
				break;
			default:
				c.getVariables().objectDistance = 1;
				c.getVariables().objectXOffset = 0;
				c.getVariables().objectYOffset = 0;
				break;

			}
			if (c.goodDistance(c.getVariables().objectX + c.getVariables().objectXOffset + 2,
					c.getVariables().objectY + c.getVariables().objectYOffset + 2, c.getX(), c.getY(),
					c.getActions().getObjectDistanceRequired(c.getVariables().objectId))) {
				c.getActions().secondClickObject(c.getVariables().objectId, c.getVariables().objectX,
						c.getVariables().objectY);
			} else {
				c.getVariables().clickObjectType = 2;
				CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (c.getVariables().clickObjectType == 2
								&& c.goodDistance(c.getVariables().objectX + c.getVariables().objectXOffset,
										c.getVariables().objectY + c.getVariables().objectYOffset, c.getX(), c.getY(),
										c.getVariables().objectDistance)) {
							c.getActions().secondClickObject(c.getVariables().objectId, c.getVariables().objectX,
									c.getVariables().objectY);
							container.stop();
						}
						if (c.getVariables().clickObjectType < 2 || c.getVariables().clickObjectType > 2)
							container.stop();
					}

					@Override
					public void stop() {
						c.getVariables().clickObjectType = 0;
					}
				}, 1);
			}
			break;

		case THIRD_CLICK:
			c.getVariables().objectX = c.getInStream().readSignedWordBigEndian();
			c.getVariables().objectY = c.getInStream().readUnsignedWord();
			c.getVariables().objectId = c.getInStream().readUnsignedWordBigEndianA();

			if (c.getVariables().playerRights >= 3) {
				Misc.println("objectId: " + c.getVariables().objectId + "  ObjectX: " + c.getVariables().objectX
						+ "  objectY: " + c.getVariables().objectY + " Xoff: " + (c.getX() - c.getVariables().objectX)
						+ " Yoff: " + (c.getY() - c.getVariables().objectY));
			}

			switch (c.getVariables().objectId) {
			default:
				c.getVariables().objectDistance = 1;
				c.getVariables().objectXOffset = 0;
				c.getVariables().objectYOffset = 0;
				break;
			}
			if (c.goodDistance(c.getVariables().objectX + c.getVariables().objectXOffset,
					c.getVariables().objectY + c.getVariables().objectYOffset, c.getX(), c.getY(),
					c.getVariables().objectDistance)) {
				c.getActions().secondClickObject(c.getVariables().objectId, c.getVariables().objectX,
						c.getVariables().objectY);
			} else {
				c.getVariables().clickObjectType = 3;
				CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (c.getVariables().clickObjectType == 3
								&& c.goodDistance(c.getVariables().objectX + c.getVariables().objectXOffset,
										c.getVariables().objectY + c.getVariables().objectYOffset, c.getX(), c.getY(),
										c.getVariables().objectDistance)) {
							c.getActions().thirdClickObject(c.getVariables().objectId, c.getVariables().objectX,
									c.getVariables().objectY);
							container.stop();
						}
						if (c.getVariables().clickObjectType < 3)
							container.stop();
					}

					@Override
					public void stop() {
						c.getVariables().clickObjectType = 0;
					}
				}, 1);
			}
			break;
		}

	}

	public void handleSpecialCase(Player c, int id, int x, int y) {

	}

}
