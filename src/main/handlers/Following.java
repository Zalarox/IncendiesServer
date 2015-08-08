package main.handlers;

import main.event.CycleEvent;
import main.event.CycleEventContainer;
import main.event.CycleEventHandler;
import main.game.npcs.NPC;
import main.game.npcs.NPCHandler;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.game.players.following.PathFinder;
import main.world.map.Region;

public class Following {

	public static void playerWalk(int x, int y, Player p) {
		PathFinder.getPathFinder().findRoute(p, x, y, true, 1, 1);
	}

	public static boolean canFollow(Player p) {
		if (PlayerHandler.players[p.followId] == null || PlayerHandler.players[p.followId].isDead) {
			resetFollow(p);
			return false;
		}
		if (p.freezeTimer > 0) {
			return false;
		}
		if (p.isDead || p.lifePoints <= 0)
			return false;
		return true;
	}

	public static boolean needsReset(Player p, Player p2, NPC n, int type) {
		if (type == 0 && (p2 == null || p2.isDead))
			return true;
		if (type == 0 && !p.goodDistance(p2.getX(), p2.getY(), p.getX(), p.getY(), 25))
			return true;
		if (type == 1 && (n == null || n.isDead))
			return true;
		if (type == 1 && !p.goodDistance(n.getX(), n.getY(), p.getX(), p.getY(), 25))
			return true;
		return false;
	}

	public static void player(Player p) {
		Player p2 = PlayerHandler.players[p.followId];
		if (needsReset(p, p2, null, 0)) {
			resetFollow(p);
			return;
		}
		if (!canFollow(p)) {
			return;
		}
		int otherX = p2.getX(), otherY = p2.getY();
		boolean sameSpot = (p.absX == otherX && p.absY == otherY),
				hallyDistance = p.goodDistance(otherX, otherY, p.getX(), p.getY(), 2),
				rangeWeaponDistance = p.goodDistance(otherX, otherY, p.getX(), p.getY(), 4),
				bowDistance = p.goodDistance(otherX, otherY, p.getX(), p.getY(), 6),
				mageDistance = p.goodDistance(otherX, otherY, p.getX(), p.getY(), 7),
				castingMagic = (p.usingMagic || p.autocasting || p.spellId > 0 || p.getInstance().autocastId > 0)
						&& mageDistance && p.mageFollow,
				playerRanging = (p.usingRangeWeapon) && rangeWeaponDistance,
				playerBowOrCross = (p.usingBow) && bowDistance;

		p.faceUpdate(p.followId + 32768);
		if (sameSpot) {
			stepAway(p);
			p.faceUpdate(p.followId + 32768);
			return;
		}

		if (p.playerIndex > 0 && !p.usingSpecial && p.inWild()) {
			if (p.usingSpecial && (playerRanging || playerBowOrCross)) {
				p.stopMovement();
				return;
			}
			if (castingMagic || playerRanging || playerBowOrCross) {
				p.stopMovement();
				return;
			}
			if (p.getCombat().usingHally() && hallyDistance) {
				p.stopMovement();
				return;
			}
		}
		walk(otherX, otherY, p);
		p.faceUpdate(p.followId + 32768);
	}

	/**
	 * if (p.goodDistance(otherX, otherY, p.getX(), p.getY(), 1)) { if (otherX
	 * != p.getX() && otherY != p.getY()) { stopDiagonal(otherX, otherY);
	 * return; } }
	 * 
	 * @param p
	 */
	public static void npc(Player p) {
		if (needsReset(p, null, NPCHandler.npcs[p.followId2], 1)) {
			resetFollow(p);
			return;
		}
		if (!canFollow(p)) {
			return;
		}
		int otherX = NPCHandler.npcs[p.followId2].getX(), otherY = NPCHandler.npcs[p.followId2].getY();
		boolean sameSpot = (p.absX == otherX && p.absY == otherY),
				hallyDistance = p.goodDistance(otherX, otherY, p.getX(), p.getY(), 2),
				rangeWeaponDistance = p.goodDistance(otherX, otherY, p.getX(), p.getY(), 4),
				bowDistance = p.goodDistance(otherX, otherY, p.getX(), p.getY(), 6),
				mageDistance = p.goodDistance(otherX, otherY, p.getX(), p.getY(), 7),
				castingMagic = (p.usingMagic || p.autocasting || p.spellId > 0 || p.getInstance().autocastId > 0)
						&& mageDistance && p.mageFollow,
				playerRanging = (p.usingRangeWeapon) && rangeWeaponDistance,
				playerBowOrCross = (p.usingBow) && bowDistance;

		p.faceUpdate(p.getInstance().followId2);
		if (sameSpot) {
			stepAway(p);
			p.faceUpdate(p.getInstance().followId2);
			return;
		}

		if (p.npcIndex > 0 && !p.usingSpecial && p.inWild()) {
			if (p.usingSpecial && (playerRanging || playerBowOrCross)) {
				p.stopMovement();
				return;
			}
			if (castingMagic || playerRanging || playerBowOrCross) {
				p.stopMovement();
				return;
			}
			if (p.getCombat().usingHally() && hallyDistance) {
				p.stopMovement();
				return;
			}
		}
		p.faceUpdate(p.getInstance().followId2);
		walk(otherX, otherY, p);
		p.faceUpdate(p.getInstance().followId2);
	}

	public static void triggerFollowing(int id, int type, Player p) {
		if (type == 0) {
			p.followId = id;
			p.followId2 = 0;
		} else {
			p.followId2 = id;
			p.followId = 0;
		}
		updateFollowing(p);
	}

	public static void updateFollowing(final Player p) {
		CycleEventHandler.getSingleton().addEvent(p, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (p.getInstance().followId > 0)
					player(p);
				else if (p.getInstance().followId2 > 0)
					npc(p);
				else
					container.stop();
			}

			@Override
			public void stop() {
				p.getInstance().followId = 0;
				p.getInstance().followId2 = 0;
			}
		}, 1);
	}

	public static void resetFollow(Player c) {
		c.getInstance().followId = 0;
		c.getInstance().followId2 = 0;
		c.getInstance().mageFollow = false;
		c.outStream.createFrame(174);
		c.outStream.writeWord(0);
		c.outStream.writeByte(0);
		c.outStream.writeWord(1);
	}

	public static void walk(int otherX, int otherY, Player p) {
		if (p.isRunning2) {
			if (otherY > p.getY() && otherX == p.getX()) {
				playerWalk(otherX, otherY - 1, p);
			} else if (otherY < p.getY() && otherX == p.getX()) {
				playerWalk(otherX, otherY + 1, p);
			} else if (otherX > p.getX() && otherY == p.getY()) {
				playerWalk(otherX - 1, otherY, p);
			} else if (otherX < p.getX() && otherY == p.getY()) {
				playerWalk(otherX + 1, otherY, p);
			} else if (otherX < p.getX() && otherY < p.getY()) {
				playerWalk(otherX + 1, otherY + 1, p);
			} else if (otherX > p.getX() && otherY > p.getY()) {
				playerWalk(otherX - 1, otherY - 1, p);
			} else if (otherX < p.getX() && otherY > p.getY()) {
				playerWalk(otherX + 1, otherY - 1, p);
			} else if (otherX > p.getX() && otherY < p.getY()) {
				playerWalk(otherX + 1, otherY - 1, p);
			}
		} else {
			if (otherY > p.getY() && otherX == p.getX()) {
				playerWalk(otherX, otherY - 1, p);
			} else if (otherY < p.getY() && otherX == p.getX()) {
				playerWalk(otherX, otherY + 1, p);
			} else if (otherX > p.getX() && otherY == p.getY()) {
				playerWalk(otherX - 1, otherY, p);
			} else if (otherX < p.getX() && otherY == p.getY()) {
				playerWalk(otherX + 1, otherY, p);
			} else if (otherX < p.getX() && otherY < p.getY()) {
				playerWalk(otherX + 1, otherY + 1, p);
			} else if (otherX > p.getX() && otherY > p.getY()) {
				playerWalk(otherX - 1, otherY - 1, p);
			} else if (otherX < p.getX() && otherY > p.getY()) {
				playerWalk(otherX + 1, otherY - 1, p);
			} else if (otherX > p.getX() && otherY < p.getY()) {
				playerWalk(otherX - 1, otherY + 1, p);
			}
		}
	}

	public static void stepAway(Player p) {
		if (Region.getClipping(p.getX() - 1, p.getY(), p.heightLevel, -1, 0)) {
			p.getPA().walkTo(-1, 0);
			return;
		} else if (Region.getClipping(p.getX() + 1, p.getY(), p.heightLevel, 1, 0)) {
			p.getPA().walkTo(1, 0);
			return;
		} else if (Region.getClipping(p.getX(), p.getY() - 1, p.heightLevel, 0, -1)) {
			p.getPA().walkTo(0, -1);
			return;
		} else if (Region.getClipping(p.getX(), p.getY() + 1, p.heightLevel, 0, 1)) {
			p.getPA().walkTo(0, 1);
			return;
		}
	}

}
