package incendius.game.objects;

public class Objects {

	public int objectId, objectX, objectY, objectHeight, objectFace, objectType, objectTicks, xp, item, owner, target;
	public long delay, oDelay;
	public boolean bait, isCannon = false;
	public String belongsTo;

	public Objects(int id, int x, int y, int height, int face, int type, int ticks, boolean isCannon) {
		this.objectId = id;
		this.objectX = x;
		this.objectY = y;
		this.objectHeight = height;
		this.objectFace = face;
		this.objectType = type;
		this.objectTicks = ticks;
		this.isCannon = isCannon;
	}

	public Objects(int objectId, int x, int y, int height, int face, int type) {
		this.objectId = objectId;
		this.objectX = x;
		this.objectY = y;
		this.objectHeight = height;
		this.objectFace = face;
		this.objectType = type;
		this.objectTicks = 0;
		this.isCannon = false;
	}

	public int getObjectId() {
		return this.objectId;
	}

	public int getObjectX() {
		return this.objectX;
	}

	public int getObjectY() {
		return this.objectY;
	}

	public int getObjectHeight() {
		return this.objectHeight;
	}

	public int getObjectFace() {
		return this.objectFace;
	}

	public int getObjectType() {
		return this.objectType;
	}

}