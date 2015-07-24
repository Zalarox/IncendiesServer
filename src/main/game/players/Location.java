package main.game.players;

/**
 *
 * @author Graham
 */
public class Location {

	/**
	 * The x coordinate.
	 */
	private final int x;

	/**
	 * The y coordinate.
	 */
	private final int y;

	/**
	 * The z coordinate.
	 */
	private final int z;

	/**
	 * Creates a location.
	 *
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 * @param z
	 *            The z coordinate.
	 * @return The location.
	 */
	public static Location create(int x, int y, int z) {
		return new Location(x, y, z);
	}

	/**
	 * Creates a location.
	 *
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 * @param z
	 *            The z coordinate.
	 */
	private Location(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Gets the absolute x coordinate.
	 *
	 * @return The absolute x coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the absolute y coordinate.
	 *
	 * @return The absolute y coordinate.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Gets the z coordinate, or height.
	 *
	 * @return The z coordinate.
	 */
	public int getZ() {
		return z;
	}

	/**
	 * Gets the local x coordinate relative to this region.
	 *
	 * @return The local x coordinate relative to this region.
	 */
	public int getLocalX() {
		return getLocalX(this);
	}

	/**
	 * Gets the local y coordinate relative to this region.
	 *
	 * @return The local y coordinate relative to this region.
	 */
	public int getLocalY() {
		return getLocalY(this);
	}

	/**
	 * Gets the local x coordinate relative to a specific region.
	 *
	 * @param l
	 *            The region the coordinate will be relative to.
	 * @return The local x coordinate.
	 */
	public int getLocalX(Location l) {
		return x - 8 * l.getRegionX();
	}

	/**
	 * Gets the local y coordinate relative to a specific region.
	 *
	 * @param l
	 *            The region the coordinate will be relative to.
	 * @return The local y coordinate.
	 */
	public int getLocalY(Location l) {
		return y - 8 * l.getRegionY();
	}

	/**
	 * Gets the region x coordinate.
	 *
	 * @return The region x coordinate.
	 */
	public int getRegionX() {
		return (x >> 3) - 6;
	}

	/**
	 * Gets the region y coordinate.
	 *
	 * @return The region y coordinate.
	 */
	public int getRegionY() {
		return (y >> 3) - 6;
	}

	/**
	 * Checks if this location is within range of another.
	 *
	 * @param other
	 *            The other location.
	 * @return <code>true</code> if the location is in range, <code>false</code>
	 *         if not.
	 */
	public boolean isWithinDistance(Location other) {
		if (z != other.z) {
			return false;
		}
		int deltaX = other.x - x, deltaY = other.y - y;
		return deltaX <= 14 && deltaX >= -15 && deltaY <= 14 && deltaY >= -15;
	}

	/**
	 * Checks if this location is within interaction range of another.
	 *
	 * @param other
	 *            The other location.
	 * @return <code>true</code> if the location is in range, <code>false</code>
	 *         if not.
	 */
	public boolean isWithinInteractionDistance(Location other) {
		if (z != other.z) {
			return false;
		}
		int deltaX = other.x - x, deltaY = other.y - y;
		return deltaX <= 2 && deltaX >= -3 && deltaY <= 2 && deltaY >= -3;
	}

	/**
	 * Checks if this location is next to another.
	 *
	 * @param other
	 *            The other location.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isNextTo(Location other) {
		if (z != other.z) {
			return false;
		}
		/*
		 * int deltaX = Math.abs(other.x - x), deltaY = Math.abs(other.y - y);
		 * return deltaX <= 1 && deltaY <= 1;
		 */
		return (getX() == other.getX() && getY() != other.getY() || getX() != other.getX() && getY() == other.getY()
				|| getX() == other.getX() && getY() == other.getY());
	}

	/**
	 * Checks if a coordinate is within range of another.
	 *
	 * @return <code>true</code> if the location is in range, <code>false</code>
	 *         if not.
	 */
	public boolean isWithinDistance(int width, int height, Location otherLocation, int otherWidth, int otherHeight,
			int distance) {
		Location myClosestTile = this.closestTileOf(otherLocation, width, height);
		Location theirClosestTile = otherLocation.closestTileOf(this, otherWidth, otherHeight);

		return myClosestTile.distanceToPoint(theirClosestTile) <= distance;
	}

	/**
	 * Checks if a coordinate is within range of another.
	 *
	 * @return <code>true</code> if the location is in range, <code>false</code>
	 *         if not.
	 */
	public boolean isWithinDistance(Location location, int distance) {
		int objectX = location.getX();
		int objectY = location.getY();
		for (int i = 0; i <= distance; i++) {
			for (int j = 0; j <= distance; j++) {
				if ((objectX + i) == x && ((objectY + j) == y || (objectY - j) == y || objectY == y)) {
					return true;
				} else if ((objectX - i) == x && ((objectY + j) == y || (objectY - j) == y || objectY == y)) {
					return true;
				} else if (objectX == x && ((objectY + j) == y || (objectY - j) == y || objectY == y)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets the closest tile of this location from a specific point.
	 */
	public Location closestTileOf(Location from, int width, int height) {
		if (width < 2 && height < 2) {
			return this;
		}
		Location location = null;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Location loc = Location.create(this.x + x, this.y + y, this.z);
				if (location == null || loc.distanceToPoint(from) < location.distanceToPoint(from)) {
					location = loc;
				}
			}
		}
		return location;
	}

	/**
	 * Gets the distance to a location.
	 *
	 * @param other
	 *            The location.
	 * @return The distance from the other location.
	 */
	public int distanceToPoint(Location other) {
		int absX = x;
		int absY = y;
		int pointX = other.getX();
		int pointY = other.getY();
		return (int) Math.sqrt(Math.pow(absX - pointX, 2) + Math.pow(absY - pointY, 2));
	}

	@Override
	public int hashCode() {
		return z << 30 | x << 15 | y;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Location)) {
			return false;
		}
		Location loc = (Location) other;
		return loc.x == x && loc.y == y && loc.z == z;
	}

	@Override
	public String toString() {
		return "[" + x + "," + y + "," + z + "]";
	}

	/**
	 * Creates a new location based on this location.
	 *
	 * @param diffX
	 *            X difference.
	 * @param diffY
	 *            Y difference.
	 * @param diffZ
	 *            Z difference.
	 * @return The new location.
	 */
	public Location transform(int diffX, int diffY, int diffZ) {
		return Location.create(x + diffX, y + diffY, z + diffZ);
	}
}
