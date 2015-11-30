package incendius.game.players.content.minigames.impl.barrows;

/**
 * 
 * @author IQuality
 *
 */
public enum StairCases {
	// id,x,y
	OBJECT_0(6703, 3574, 3298, 0), OBJECT_1(6704, 3578, 3284, 3), OBJECT_2(6705, 3565, 3276, 2), OBJECT_3(6702, 3565,
			3289, 1), OBJECT_4(6707, 3557, 3298, 5), OBJECT_5(6706, 3553, 3283, 4);

	private int id, x, y, type;

	StairCases(int id, int x, int y, int type) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.type = type;
	}

	public int getID() {
		return id;
	}

	public int getType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
