package incendius.processors;

import incendius.world.map.Region;

public class GameProcessor {

	public static void startPlayerProcess(int i) {
		/*
		 * Player playerProcess = new Player(i); playerProcess.run();
		 * PlayerHandler.playerCount++;
		 */
	}

	public static void startItemProcess(Region region) {
		region.itemProcessing = true;
		Item itemProcess = new Item(region);
		itemProcess.run();
	}

}
