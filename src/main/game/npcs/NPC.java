package main.game.npcs;

import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.util.Misc;
import main.util.Stream;
import main.world.map.Region;

public class NPC {

	public void sendAnimation(int animId) {
		animNumber = animId;
		animUpdateRequired = true;
	}

	public long delay, lastDamageTaken;

	public int npcId, npcSize = 1, npcType, index, absX, absY, heightLevel, viewX, viewY, spawnX, spawnY, makeX, makeY,
			moveX, moveY, direction, walkingType, combatLevel, underAttackBy2, oldIndexNPC, attackType, projectileId,
			endGfx, spawnedBy, owner, summonedFor, hitDelayTimer, HP, MaxHP, hitDiff, animNumber, actionTimer, enemyX,
			enemyY, freezeTimer, attackTimer, killerId = 0, killedBy, oldIndex, underAttackBy = 0, firstAttacker,
			splashDelay = -1, mask80var1 = 0, mask80var2 = 0, teleportDelay = -1, teleX, teleY, teleHeight,
			FocusPointX = -1, FocusPointY = -1, face = 0, hitDiff2 = 0, hitIcon, hitMask, hitIcon2, hitMask2,
			setStyleAmount = 0, totalsetStyleAmount = 0, untransformTimer = 0, transformId = -1, maxHit, attack,
			defence, faceX, faceY, lastX, lastY, columnId = -1, secondsTillTransport, type, id;

	public int[][] splashCoord = new int[5][2];

	public String forcedText, attackStyle = "", npcName;

	public boolean isAttackingAPerson = false, isAttackedByPerson = false, IsUnderAttackNpc = false,
			IsAttackingNPC = false, hitUpdateRequired2 = false, changedAttackStyle = false, applyDead, isDead,
			needRespawn, respawns, walkingHome, underAttack, randomWalk, dirUpdateRequired, animUpdateRequired,
			transformUpdateRequired, hitUpdateRequired, updateRequired, forcedChatRequired, faceToUpdateRequired,
			splash, faceUpdated = false, noDeathEmote, caught = false, canTeleport = false;

	protected boolean mask80update = false;

	public Player projectile = null;

	public boolean spec;

	public boolean extraHit;

	/**
	 * attackType: 0 = melee, 1 = range, 2 = mage
	 */

	public NPC(int _npcId, int _npcType, String npcName) {
		npcId = _npcId;
		npcType = _npcType;
		this.npcName = npcName;
		direction = -1;
		isDead = false;
		applyDead = false;
		actionTimer = 0;
		randomWalk = true;
	}

	public NPC(int _npcType) {
		npcType = _npcType;
	}

	public void setNpcName(String npcName) {
		this.npcName = npcName;
	}

	public String getNpcName() {
		return Misc.optimizeText(npcName.toLowerCase().replaceAll("_", " "));
	}

	public int getCombatLevel() {
		return 1;
		// return NPCHandler.npcCombat[npcType];
	}

	public void updateNPCMovement(Stream str) {
		if (direction == -1) {
			if (updateRequired) {
				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else {
				str.writeBits(1, 0);
			}
		} else {
			str.writeBits(1, 1);
			str.writeBits(2, 1);
			str.writeBits(3, Misc.xlateDirectionToClient[direction]);
			if (updateRequired) {
				str.writeBits(1, 1);
			} else {
				str.writeBits(1, 0);
			}
		}
	}

	/**
	 * Text update
	 **/
	public void forceChat(String text) {
		forcedText = text;
		forcedChatRequired = true;
		updateRequired = true;
	}

	/**
	 * Graphics
	 **/

	public void appendMask80Update(Stream str) {
		str.writeWord(mask80var1);
		str.writeDWord(mask80var2);
	}

	public void gfx100(int gfx) {
		mask80var1 = gfx;
		mask80var2 = 6553600;
		mask80update = true;
		updateRequired = true;
	}

	public void npcTeleport(int x, int y, int h) {
		if (!canTeleport)
			return;
		needRespawn = true;
		teleX = x;
		teleY = y;
		teleHeight = h;
		updateRequired = true;
		teleportDelay = 0;
	}

	public void gfx0(int gfx) {
		mask80var1 = gfx;
		mask80var2 = 65536;
		mask80update = true;
		updateRequired = true;
	}

	public void appendTransformUpdate(Stream str) {
		str.writeWordBigEndianA(transformId);
	}

	public void requestTransform(int id) {
		transformId = id;
		transformUpdateRequired = true;
		updateRequired = true;
	}

	public void appendAnimUpdate(Stream str) {
		str.writeWordBigEndian(animNumber);
		str.writeByte(1);
	}

	public void requestAnimation(int animId, int i) {
		animNumber = animId;
		animUpdateRequired = true;
		updateRequired = true;
	}

	/**
	 *
	 * Face
	 *
	 **/

	private void appendSetFocusDestination(Stream str) {
		str.writeWordBigEndian(FocusPointX);
		str.writeWordBigEndian(FocusPointY);
	}

	public void turnNpc(int i, int j) {
		FocusPointX = 2 * i + 1;
		FocusPointY = 2 * j + 1;
		updateRequired = true;

	}

	public void turnNpcTo(int pointX, int pointY) {
		FocusPointX = 2 * pointX + 1;
		FocusPointY = 2 * pointY + 1;
		updateRequired = true;
	}

	public void faceNPC(NPC n) {
		if (n == null) {
			return;
		}
		face = n.index;
		dirUpdateRequired = true;
		updateRequired = true;
	}

	public void appendFaceEntity(Stream str) {
		str.writeWord(face);
	}

	public void facePlayer(int player) {
		face = player + 32768;
		dirUpdateRequired = true;
		updateRequired = true;
		Player p = PlayerHandler.players[player];
		if (p != null) {
			lastX = p.getX();
			lastY = p.getY();
		}
	}

	public void appendFaceToUpdate(Stream str) {
		str.writeWordBigEndian(viewX);
		str.writeWordBigEndian(viewY);
	}

	public void appendNPCUpdateBlock(Stream str, Player c) {
		if (!updateRequired) {
			return;
		}
		int updateMask = 0;
		if (animUpdateRequired) {
			updateMask |= 0x10;
		}
		if (hitUpdateRequired2) {
			updateMask |= 8;
		}
		if (mask80update) {
			updateMask |= 0x80;
		}
		if (dirUpdateRequired) {
			updateMask |= 0x20;
		}
		if (forcedChatRequired) {
			updateMask |= 1;
		}
		if (hitUpdateRequired) {
			updateMask |= 0x40;
		}
		if (FocusPointX != -1) {
			updateMask |= 4;
		}
		if (transformUpdateRequired) {
			updateMask |= 2;
		}

		str.writeByte(updateMask);

		if (animUpdateRequired) {
			appendAnimUpdate(str);
		}
		if (hitUpdateRequired2) {
			appendHitUpdate2(str, c);
		}
		if (mask80update) {
			appendMask80Update(str);
		}
		if (dirUpdateRequired) {
			appendFaceEntity(str);
		}
		if (transformUpdateRequired) {
			appendTransformUpdate(str);
		}
		if (forcedChatRequired) {
			str.writeString(forcedText);
		}
		if (hitUpdateRequired) {
			appendHitUpdate(str, c);
		}
		if (FocusPointX != -1) {
			appendSetFocusDestination(str);
		}

	}

	public void startAnimation(int animId) {
		animNumber = animId;
		animUpdateRequired = true;
		updateRequired = true;
	}

	public void clearUpdateFlags() {
		updateRequired = false;
		forcedChatRequired = false;
		hitUpdateRequired = false;
		hitUpdateRequired2 = false;
		animUpdateRequired = false;
		transformUpdateRequired = false;
		dirUpdateRequired = false;
		mask80update = false;
		forcedText = null;
		moveX = 0;
		moveY = 0;
		direction = -1;
		FocusPointX = -1;
		FocusPointY = -1;
	}

	public static enum FaceDirection {
		NORTH, NORTH_EAST, NORTH_WEST, EAST, SOUTH, SOUTH_EAST, SOUTH_WEST, WEST;
	}

	/**
	 * nordur x = 0, y = 1 sudur = x = 0, y = -1 austur x = 1, y = 0 vestur x =
	 * -1, y = 0
	 */
	public void getLatestFacePoint() {
		switch (getFaceDirection()) {
		case NORTH:
			faceX = 0;
			faceY = 1;
			break;
		case NORTH_EAST:
			faceX = 1;
			faceY = 1;
			break;
		case NORTH_WEST:
			faceX = -1;
			faceY = 1;
			break;
		case EAST:
			faceX = 1;
			faceY = 0;
			break;
		case SOUTH:
			faceX = 0;
			faceY = -1;
			break;
		case SOUTH_EAST:
			faceX = 1;
			faceY = -1;
			break;
		case SOUTH_WEST:
			faceX = -1;
			faceY = -1;
			break;
		case WEST:
			faceX = -1;
			faceY = 0;
			break;
		}
	}

	public void faceCurrentDirection() {
		if (!faceUpdated) {
			getLatestFacePoint();
			faceX = -faceX;
			faceY = -faceY;
			turnNpc(getX() + faceX, getY() + faceY);
			faceUpdated = true;
		}
	}

	public FaceDirection getFaceDirection() {
		if (getLastX() < getX()) {
			if (getLastY() == getY())
				return FaceDirection.EAST;
			else if (getLastY() < getY())
				return FaceDirection.NORTH_EAST;
			else
				return FaceDirection.SOUTH_EAST;
		} else if (getLastX() == getX()) {
			if (getLastY() < getY())
				return FaceDirection.NORTH;
			else
				return FaceDirection.SOUTH;
		} else if (getLastX() > getX()) {
			if (getLastY() == getY())
				return FaceDirection.WEST;
			else if (getLastY() < getY())
				return FaceDirection.NORTH_WEST;
			else
				return FaceDirection.SOUTH_WEST;
		}
		return FaceDirection.NORTH;
	}

	/*
	 * public int getNextWalkingDirection() { int dir; dir =
	 * Misc.direction(absX, absY, (absX + moveX), (absY + moveY)); if (dir ==
	 * -1) { return -1; } dir >>= 1;
	 */

	/*
	 * if (!Region.canMove(absX, absY, absX + moveX, absY + moveY + 1,
	 * heightLevel % 4, moveX, moveY)) { moveX = 0; moveY = 0; return -1; } absX
	 * += moveX; absY += moveY; /*if (lastRegion != Region.getRegion(absX, absY)
	 * && Region.REGION_UPDATING_ENABLED) { lastRegion.removeNpc(this);
	 * lastRegion = Region.getRegion(absX, absY); lastRegion.addNpc(this); }
	 */
	/*
	 * return dir; }
	 */

	public int getNextWalkingDirection() {
		int dir;
		dir = Misc.direction(absX, absY, (absX + moveX), (absY + moveY));
		if (dir == -1)
			return -1;
		dir >>= 1;
		absX += moveX;
		absY += moveY;
		return dir;
	}

	public void getNextNPCMovement(int i) {
		direction = -1;
		if (moveX != 0 || moveY != 0) {
			int step[] = Region.getNextStep(absX, absY, absX + moveX, absY + moveY, heightLevel % 4, getNpcSize(),
					getNpcSize());
			moveX = step[0] - absX;
			moveY = step[1] - absY;
			if (NPCHandler.npcs[i].freezeTimer <= 0)
				direction = getNextWalkingDirection();
		}
	}

	public int getNpcSize() {
		return NPCHandler.npcSizes.length > npcType ? NPCHandler.npcSizes[npcType] : 1;
	}

	public void appendHitUpdate(Stream str, Player c) {
		if (HP <= 0) {
			isDead = true;
		}
		str.writeWordA(hitDiff);
		str.writeByteS(hitMask);
		str.writeByte(hitIcon);
		str.writeWordA(HP);
		str.writeWordA(MaxHP);
	}

	public void appendHitUpdate2(Stream str, Player c) {
		if (HP <= 0) {
			isDead = true;
		}
		str.writeWordA(hitDiff2);
		str.writeByteC(hitMask2);
		str.writeByte(hitIcon2);
		str.writeWordA(HP);
		str.writeWordA(MaxHP);
	}

	public void handleHitMask(int damage) {
		if (!hitUpdateRequired) {
			hitUpdateRequired = true;
			hitDiff = damage;
		} else if (!hitUpdateRequired2) {
			hitUpdateRequired2 = true;
			hitDiff2 = damage;
		}
		updateRequired = true;
	}

	public int getX() {
		return absX;
	}

	public int getY() {
		return absY;
	}

	public int getLastX() {
		return lastX;
	}

	public int getLastY() {
		return lastY;
	}

	public boolean inMulti() {
		if ((absX >= 3136 && absX <= 3327 && absY >= 3519 && absY <= 3607)
				|| (absX >= 2625 && absX <= 2685 && absY >= 2550 && absY <= 2620) || // Pest
																						// Control
				(absX >= 3190 && absX <= 3327 && absY >= 3648 && absY <= 3839)
				|| (absX >= 3334 && absX <= 3355 && absY >= 3208 && absY <= 3217)
				|| (absX >= 3200 && absX <= 3390 && absY >= 3840 && absY <= 3967)
				|| (absX >= 2992 && absX <= 3007 && absY >= 3912 && absY <= 3967)
				|| (absX >= 2946 && absX <= 2959 && absY >= 3816 && absY <= 3831)
				|| (absX >= 3008 && absX <= 3199 && absY >= 3856 && absY <= 3903)
				|| (absX >= 3008 && absX <= 3071 && absY >= 3600 && absY <= 3711)
				|| (absX >= 3072 && absX <= 3327 && absY >= 3608 && absY <= 3647)
				|| (absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619)
				|| (absX >= 2371 && absX <= 2422 && absY >= 5062 && absY <= 5117)
				|| (absX >= 2896 && absX <= 2927 && absY >= 3595 && absY <= 3630)
				|| (absX >= 2892 && absX <= 2932 && absY >= 4435 && absY <= 4464)
				|| (absX >= 2882 && absX <= 3000 && absY >= 4357 && absY <= 4406) // Corp
																					// beast
				|| (absX >= 3100 && absX <= 3300 && absY >= 5300 && absY <= 5600)
				|| (coordsCheck(3147, 3193, 9737, 9778))
				|| (absX >= 2256 && absX <= 2287 && absY >= 4680 && absY <= 4711) || ingodWars()) {
			return true;
		}
		return false;
	}

	public boolean ingodWars() {
		if (absX >= 2815 && absX <= 2942 && absY >= 5253 && absY <= 5377) {
			return true;
		}
		return false;
	}

	public boolean inBarbDef() {
		return (coordsCheck(3147, 3193, 9737, 9778));
	}

	public boolean coordsCheck(int X1, int X2, int Y1, int Y2) {
		return absX >= X1 && absX <= X2 && absY >= Y1 && absY <= Y2;
	}

	public boolean inWild() {
		if (absX > 2941 && absX < 3392 && absY > 3518 && absY < 3966
				|| absX > 2941 && absX < 3392 && absY > 9918 && absY < 10366) {
			return true;
		}
		return false;
	}

	public boolean inChamber(int chamber) {
		if (chamber == 0) {// zammy

		} else if (chamber == 1) {// bandos

		} else if (chamber == 2) {// arma

		} else if (chamber == 3) {// sara

		}
		return false;
	}

	public String barbRandom(Player c, int type) {
		switch (type) {
		case 0:
			return "KILL THAT MONGREL!";
		case 1:
			return "YOU'RE MINE, " + c.playerName.toUpperCase() + "!";
		case 2:
			return "YOU REALLY THINK YOU CAN BEAT ME?";
		case 3:
			return "IS THAT ALL YOU'VE GOT WEAKLING?";
		case 4:
			return "FAILURE IS NOT AN OPTION MY MINIONS! ATTACK!";
		}
		return "";
	}
}
