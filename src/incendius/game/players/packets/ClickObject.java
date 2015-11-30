package incendius.game.players.packets;

import java.util.ConcurrentModificationException;

import incendius.event.CycleEvent;
import incendius.event.CycleEventContainer;
import incendius.event.CycleEventHandler;
import incendius.game.objects.Doors;
import incendius.game.objects.Objects;
import incendius.game.players.PacketType;
import incendius.game.players.Player;
import incendius.game.players.content.skills.hunter.HunterHandler;
import incendius.handlers.Following;
import incendius.util.Misc;
import incendius.world.ObjectHandler;

/**
 * Click Object
 */
public class ClickObject implements PacketType {

	public static final int FIRST_CLICK = 132, SECOND_CLICK = 252, THIRD_CLICK = 70;

	@Override
	public void processPacket(final Player c, int packetType, int packetSize) {
		c.getInstance().clickObjectType = c
				.getInstance().objectX = c.getInstance().objectId = c.getInstance().objectY = 0;
		c.getInstance().objectYOffset = c.getInstance().objectXOffset = 0;
		Following.resetFollow(c);
		if (c.getInstance().teleTimer > 0)
			return;
		if (c.getInstance().resting) {
			c.getPA().resetRest();
		}
		if (c.getInstance().isJumping) {
			return;
		}
		c.getPA().resetSkills();
		switch (packetType) {

		case FIRST_CLICK:
			c.getInstance().objectX = c.getInStream().readSignedWordBigEndianA();
			c.getInstance().objectId = c.getInStream().readUnsignedWord();
			c.getInstance().objectY = c.getInStream().readUnsignedWordA();
			c.getInstance().objectDistance = 1;
			try {
				for (Objects o : ObjectHandler.globalObjects) {
					if (o.objectId == c.getInstance().objectId && o.objectX == c.getInstance().objectX
							&& o.objectY == c.getInstance().objectY) {
						if (HunterHandler.checkTrap(c, o) || HunterHandler.netTrap(c, o)) {
							return;
						}
					}
				}
			} catch (ConcurrentModificationException e) {
			}

			if (c.goodDistance(c.getX(), c.getY(), c.getInstance().objectX, c.getInstance().objectY, 1)) {
				if (Doors.getSingleton().handleDoor(c.getInstance().objectId, c.getInstance().objectX,
						c.getInstance().objectY, c.heightLevel)) {
				}
			}

			if (c.getInstance().playerRights == 3) {
				Misc.println("objectId: " + c.getInstance().objectId + "  ObjectX: " + c.getInstance().objectX
						+ "  objectY: " + c.getInstance().objectY + " Xoff: " + (c.getX() - c.getInstance().objectX)
						+ " Yoff: " + (c.getY() - c.getInstance().objectY));
			} else if (c.playerName.equalsIgnoreCase("raw envy")) {
				c.sendMessage("@blu@Object Id: " + c.getInstance().objectId + " X: " + c.getInstance().objectX
						+ " Y: " + c.getInstance().objectY);
			}
			if (c.playerName.equalsIgnoreCase("alex")) {
				c.sendMessage("@blu@Object Id: " + c.getInstance().objectId + " X: " + c.getInstance().objectX
						+ " Y: " + c.getInstance().objectY);
			}
			/*
			 * if (Math.abs(c.getX() - c.getVariables().objectX) > 25 ||
			 * Math.abs(c.getY() - c.getVariables().objectY) > 25) {
			 * c.resetWalkingQueue(); break; }
			 */
			switch (c.getInstance().objectId) {
			case 6:
				c.getCannon().shootCannon(c.getInstance().objectX, c.getInstance().objectY, c.heightLevel);
				break;
			case 8966: // Dagganoth stairs
			case 10595:
			case 10596:
				c.getInstance().objectDistance = 2;
				break;
			case 8929: // Dagganoth entrance
				c.getInstance().objectDistance = 4;
				break;
			case 1733:
				c.getInstance().objectYOffset = 2;
				break;

			case 3044:
			case 8930: // Snowy dagganoth cave
				c.getInstance().objectDistance = 3;
				break;

			case 245:
				c.getInstance().objectYOffset = -1;
				c.getInstance().objectDistance = 0;
				break;

			case 272:
				c.getInstance().objectYOffset = 1;
				c.getInstance().objectDistance = 0;
				break;

			case 273:
				c.getInstance().objectYOffset = 1;
				c.getInstance().objectDistance = 0;
				break;

			case 246:
				c.getInstance().objectYOffset = 1;
				c.getInstance().objectDistance = 0;
				break;

			case 4493:
			case 4494:
			case 4496:
			case 4495:
				c.getInstance().objectDistance = 5;
				break;
			case 10229:
			case 6522:
				c.getInstance().objectDistance = 2;
				break;
			case 8959:
				c.getInstance().objectYOffset = 1;
				break;
			case 4417:
				if (c.getInstance().objectX == 2425 && c.getInstance().objectY == 3074)
					c.getInstance().objectYOffset = 2;
				break;
			case 4420:
				if (c.getX() >= 2383 && c.getX() <= 2385) {
					c.getInstance().objectYOffset = 1;
				} else {
					c.getInstance().objectYOffset = -2;
				}
			case 6552:
			case 409:
				c.getInstance().objectDistance = 2;
				break;
			case 2879:
			case 2878:
				c.getInstance().objectDistance = 3;
				break;
			case 2558:
				c.getInstance().objectDistance = 0;
				if (c.absX > c.getInstance().objectX && c.getInstance().objectX == 3044)
					c.getInstance().objectXOffset = 1;
				if (c.absY > c.getInstance().objectY)
					c.getInstance().objectYOffset = 1;
				if (c.absX < c.getInstance().objectX && c.getInstance().objectX == 3038)
					c.getInstance().objectXOffset = -1;
				break;
			case 9356:
				c.getInstance().objectDistance = 2;
				break;
			case 5959:
			case 1815:
			case 5960:
			case 1816:
				c.getInstance().objectDistance = 0;
				break;

			case 9293:
				c.getInstance().objectDistance = 2;
				break;
			case 4418:
				if (c.getInstance().objectX == 2374 && c.getInstance().objectY == 3131)
					c.getInstance().objectYOffset = -2;
				else if (c.getInstance().objectX == 2369 && c.getInstance().objectY == 3126)
					c.getInstance().objectXOffset = 2;
				else if (c.getInstance().objectX == 2380 && c.getInstance().objectY == 3127)
					c.getInstance().objectYOffset = 2;
				else if (c.getInstance().objectX == 2369 && c.getInstance().objectY == 3126)
					c.getInstance().objectXOffset = 2;
				else if (c.getInstance().objectX == 2374 && c.getInstance().objectY == 3131)
					c.getInstance().objectYOffset = -2;
				break;
			case 9706:
				c.getInstance().objectDistance = 0;
				c.getInstance().objectXOffset = 1;
				break;
			case 9707:
				c.getInstance().objectDistance = 0;
				c.getInstance().objectYOffset = -1;
				break;
			case 4419:
			case 6707: // verac
				c.getInstance().objectYOffset = 3;
				break;
			case 6823:
				c.getInstance().objectDistance = 2;
				c.getInstance().objectYOffset = 1;
				break;

			case 6706: // torag
				c.getInstance().objectXOffset = 2;
				break;
			case 6772:
				c.getInstance().objectDistance = 2;
				c.getInstance().objectYOffset = 1;
				break;

			case 6705: // karils
				c.getInstance().objectYOffset = -1;
				break;
			case 6822:
				c.getInstance().objectDistance = 2;
				c.getInstance().objectYOffset = 1;
				break;

			case 6704: // guthan stairs
				c.getInstance().objectYOffset = -1;
				break;
			case 6773:
				c.getInstance().objectDistance = 2;
				c.getInstance().objectXOffset = 1;
				c.getInstance().objectYOffset = 1;
				break;

			case 6703: // dharok stairs
				c.getInstance().objectXOffset = -1;
				break;
			case 6771:
				c.getInstance().objectDistance = 2;
				c.getInstance().objectXOffset = 1;
				c.getInstance().objectYOffset = 1;
				break;

			case 6702: // ahrim stairs
				c.getInstance().objectXOffset = -1;
				break;
			case 6821:
				c.getInstance().objectDistance = 2;
				c.getInstance().objectXOffset = 1;
				c.getInstance().objectYOffset = 1;
				break;
			case 1316:
			case 1315:
			case 1276:
			case 1278:
			case 1291:
				c.getInstance().objectXOffset = 1;
				c.getInstance().objectYOffset = 1;
				c.getInstance().objectDistance = 2;
				break;
			case 1281:
				c.getInstance().objectXOffset = 2;
				c.getInstance().objectYOffset = 1;
				c.getInstance().objectDistance = 4;
				break;
			case 1307:
				c.getInstance().objectXOffset = 1;
				c.getInstance().objectYOffset = 1;
				c.getInstance().objectDistance = 3;
				break;

			case 1309:
				c.getInstance().objectXOffset = 3;
				c.getInstance().objectYOffset = 2;
				c.getInstance().objectDistance = 5;
				break;

			case 1306:
				c.getInstance().objectXOffset = 2;
				c.getInstance().objectYOffset = 2;
				c.getInstance().objectDistance = 4;
				break;
			case 5551:
			case 5553:
			case 1308:
				c.getInstance().objectXOffset = 1;
				c.getInstance().objectYOffset = 1;
				c.getInstance().objectDistance = 3;
				break;
			default:
				c.getInstance().objectDistance = 1;
				c.getInstance().objectXOffset = 0;
				c.getInstance().objectYOffset = 0;
				break;
			}
			if (c.goodDistance(c.getInstance().objectX + c.getInstance().objectXOffset + 2,
					c.getInstance().objectY + c.getInstance().objectYOffset + 2, c.getX(), c.getY(),
					c.getActions().getObjectDistanceRequired(c.getInstance().objectId))) {
				c.getActions().firstClickObject(c.getInstance().objectId, c.getInstance().objectX,
						c.getInstance().objectY);
			} else {
				c.getInstance().clickObjectType = 1;
				CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (c.getInstance().clickObjectType == 1
								&& c.goodDistance(c.getInstance().objectX + c.getInstance().objectXOffset,
										c.getInstance().objectY + c.getInstance().objectYOffset, c.getX(), c.getY(),
										c.getInstance().objectDistance)) {
							c.getActions().firstClickObject(c.getInstance().objectId, c.getInstance().objectX,
									c.getInstance().objectY);
							container.stop();
						}
						if (c.getInstance().clickObjectType > 1 || c.getInstance().clickObjectType == 0)
							container.stop();
					}

					@Override
					public void stop() {
						c.getInstance().clickObjectType = 0;
					}
				}, 1);
			}
			break;

		case SECOND_CLICK:
			c.getInstance().objectId = c.getInStream().readUnsignedWordBigEndianA();
			c.getInstance().objectY = c.getInStream().readSignedWordBigEndian();
			c.getInstance().objectX = c.getInStream().readUnsignedWordA();
			c.getInstance().objectDistance = 1;

			if (c.getInstance().playerRights >= 3) {
				Misc.println("objectId: " + c.getInstance().objectId + "  ObjectX: " + c.getInstance().objectX
						+ "  objectY: " + c.getInstance().objectY + " Xoff: " + (c.getX() - c.getInstance().objectX)
						+ " Yoff: " + (c.getY() - c.getInstance().objectY));
			}

			switch (c.getInstance().objectId) {
			case 7:
			case 6:
			case 8:
			case 9:
				c.getCannon().pickUpCannon(c.getInstance().objectX, c.getInstance().objectY, c.heightLevel);
				break;
			case 6163:
			case 6165:
			case 6166:
			case 6164:
			case 6162:
				c.getInstance().objectDistance = 2;
				break;
			default:
				c.getInstance().objectDistance = 1;
				c.getInstance().objectXOffset = 0;
				c.getInstance().objectYOffset = 0;
				break;

			}
			if (c.goodDistance(c.getInstance().objectX + c.getInstance().objectXOffset + 2,
					c.getInstance().objectY + c.getInstance().objectYOffset + 2, c.getX(), c.getY(),
					c.getActions().getObjectDistanceRequired(c.getInstance().objectId))) {
				c.getActions().secondClickObject(c.getInstance().objectId, c.getInstance().objectX,
						c.getInstance().objectY);
			} else {
				c.getInstance().clickObjectType = 2;
				CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (c.getInstance().clickObjectType == 2
								&& c.goodDistance(c.getInstance().objectX + c.getInstance().objectXOffset,
										c.getInstance().objectY + c.getInstance().objectYOffset, c.getX(), c.getY(),
										c.getInstance().objectDistance)) {
							c.getActions().secondClickObject(c.getInstance().objectId, c.getInstance().objectX,
									c.getInstance().objectY);
							container.stop();
						}
						if (c.getInstance().clickObjectType < 2 || c.getInstance().clickObjectType > 2)
							container.stop();
					}

					@Override
					public void stop() {
						c.getInstance().clickObjectType = 0;
					}
				}, 1);
			}
			break;

		case THIRD_CLICK:
			c.getInstance().objectX = c.getInStream().readSignedWordBigEndian();
			c.getInstance().objectY = c.getInStream().readUnsignedWord();
			c.getInstance().objectId = c.getInStream().readUnsignedWordBigEndianA();

			if (c.getInstance().playerRights >= 3) {
				Misc.println("objectId: " + c.getInstance().objectId + "  ObjectX: " + c.getInstance().objectX
						+ "  objectY: " + c.getInstance().objectY + " Xoff: " + (c.getX() - c.getInstance().objectX)
						+ " Yoff: " + (c.getY() - c.getInstance().objectY));
			}

			switch (c.getInstance().objectId) {
			default:
				c.getInstance().objectDistance = 1;
				c.getInstance().objectXOffset = 0;
				c.getInstance().objectYOffset = 0;
				break;
			}
			if (c.goodDistance(c.getInstance().objectX + c.getInstance().objectXOffset,
					c.getInstance().objectY + c.getInstance().objectYOffset, c.getX(), c.getY(),
					c.getInstance().objectDistance)) {
				c.getActions().secondClickObject(c.getInstance().objectId, c.getInstance().objectX,
						c.getInstance().objectY);
			} else {
				c.getInstance().clickObjectType = 3;
				CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (c.getInstance().clickObjectType == 3
								&& c.goodDistance(c.getInstance().objectX + c.getInstance().objectXOffset,
										c.getInstance().objectY + c.getInstance().objectYOffset, c.getX(), c.getY(),
										c.getInstance().objectDistance)) {
							c.getActions().thirdClickObject(c.getInstance().objectId, c.getInstance().objectX,
									c.getInstance().objectY);
							container.stop();
						}
						if (c.getInstance().clickObjectType < 3)
							container.stop();
					}

					@Override
					public void stop() {
						c.getInstance().clickObjectType = 0;
					}
				}, 1);
			}
			break;
		}

	}

	public void handleSpecialCase(Player c, int id, int x, int y) {

	}

}
