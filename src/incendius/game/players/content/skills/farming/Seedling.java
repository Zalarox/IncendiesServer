/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package incendius.game.players.content.skills.farming;

import java.util.HashMap;
import java.util.Map;

import incendius.Constants;
import incendius.game.items.Item;
import incendius.game.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 18/03/12 Time: 11:00 To change
 * this template use File | Settings | File Templates.
 */
public class Seedling {

	private Player player;

	public Seedling(Player player) {
		this.player = player;
	}

	public enum SeedlingData {
		OAK(5312, 5358, 5364, 5370), WILLOW(5313, 5359, 5365, 5371), MAPLE(5314, 5360, 5366, 5372), YEW(5315, 5361,
				5367, 5373), MAGIC(5316, 5362, 5368, 5374), SPIRIT(5317, 5363, 5369, 5375), APPLE(5283, 5480, 5488,
						5496), BANANA(5284, 5481, 5489, 5497), ORANGE(5285, 5482, 5490, 5498), CURRY(5286, 5483, 5491,
								5499), PINEAPPLE(5287, 5484, 5492, 5500), PAPAYA(5288, 5485, 5493, 5501), PALM(5289,
										5486, 5494, 5502), CALQUAT(5290, 5487, 5495, 5503);

		private int seedId;
		private int unwateredSeedlingId;
		private int wateredSeedlingId;
		private int saplingId;

		private static Map<Integer, SeedlingData> seeds = new HashMap<Integer, SeedlingData>();
		private static Map<Integer, SeedlingData> unwatered = new HashMap<Integer, SeedlingData>();
		private static Map<Integer, SeedlingData> watered = new HashMap<Integer, SeedlingData>();

		static {
			for (SeedlingData data : SeedlingData.values()) {
				seeds.put(data.seedId, data);
				unwatered.put(data.unwateredSeedlingId, data);
				watered.put(data.wateredSeedlingId, data);
			}
		}

		SeedlingData(int seedId, int unwateredSeedlingId, int wateredSeedlingId, int saplingId) {
			this.seedId = seedId;
			this.unwateredSeedlingId = unwateredSeedlingId;
			this.wateredSeedlingId = wateredSeedlingId;
			this.saplingId = saplingId;
		}

		public static SeedlingData getSeed(int seedId) {
			return seeds.get(seedId);
		}

		public static SeedlingData getUnwatered(int seedId) {
			return unwatered.get(seedId);
		}

		public static SeedlingData getWatered(int seedId) {
			return watered.get(seedId);
		}

		public int getSeedId() {
			return seedId;
		}

		public int getUnwateredSeedlingId() {
			return unwateredSeedlingId;
		}

		public int getWateredSeedlingId() {
			return wateredSeedlingId;
		}

		public int getSaplingId() {
			return saplingId;
		}
	}

	public void makeSaplingInInv(int itemId) {
		SeedlingData seedlingData = SeedlingData.getWatered(itemId);
		if (seedlingData == null)
			return;
		player.getInventory().removeItem(new Item(itemId));
		player.getInventory().addItem(new Item(seedlingData.getSaplingId()));
	}

	public void makeSaplingInBank(int itemId) {
		SeedlingData seedlingData = SeedlingData.getWatered(itemId);
		if (seedlingData == null)
			return;
		player.getBank().remove(new Item(itemId));
		player.getBank().add(new Item(seedlingData.getSaplingId()));
	}

	public boolean waterSeedling(int itemUsed, int usedWith, int itemUsedSlot, int usedWithSlot) {
		SeedlingData seedlingData = SeedlingData.getUnwatered(itemUsed);
		if (seedlingData == null)
			seedlingData = SeedlingData.getUnwatered(usedWith);
		if (seedlingData == null || (!new Item(itemUsed).getDefinition().getName().toLowerCase().contains("watering")
				&& !new Item(usedWith).getDefinition().getName().toLowerCase().contains("watering")))
			return false;

		if (itemUsed >= 5333 && itemUsed <= 5340)
			player.getInventory().set(itemUsedSlot, new Item(itemUsed == 5333 ? itemUsed - 2 : itemUsed - 1));
		if (usedWith >= 5333 && usedWith <= 5340)
			player.getInventory().set(usedWithSlot, new Item(usedWith == 5333 ? usedWith - 2 : usedWith - 1));

		player.getPA().sendMessage(
				"You water the " + new Item(seedlingData.getSeedId()).getDefinition().getName().toLowerCase() + ".");
		player.getInventory().removeItem(new Item(seedlingData.getUnwateredSeedlingId()));
		player.getInventory().addItem(new Item(seedlingData.getWateredSeedlingId()));
		return true;

	}

	public boolean placeSeedInPot(int itemUsed, int usedWith, int itemUsedSlot, int usedWithSlot) {
		SeedlingData seedlingData = SeedlingData.getSeed(itemUsed);
		if (seedlingData == null)
			seedlingData = SeedlingData.getUnwatered(usedWith);
		if (seedlingData == null || (itemUsed != 5354 && usedWith != 5354))
			return false;
		player.getInventory().removeItem(new Item(seedlingData.getSeedId()));
		player.getInventory().set(itemUsed == 5354 ? itemUsedSlot : usedWithSlot,
				new Item(seedlingData.getUnwateredSeedlingId()));
		player.getPA().sendMessage("You sow some maple tree seeds in the plantpots.");
		player.getPA().sendMessage("They need watering before they will grow.");
		return true;
	}

	public boolean fillPotWithSoil(int itemId, int objectX, int objectY) {
		if (itemId != 5350)
			return false;
		if (!Constants.FARMING_ENABLED) {
			player.getPA().sendMessage("This skill is currently disabled.");
			return true;
		}

		/*
		 * TODO DISABLED
		 * 
		 */
		/*
		 * if (!(player.getAllotment().checkIfRaked(objectX, objectY) ||
		 * player.getBushes().checkIfRaked(objectX, objectY) ||
		 * player.getFlowers().checkIfRaked(objectX, objectY) ||
		 * player.getFruitTrees().checkIfRaked(objectX, objectY) ||
		 * player.getHerbs().checkIfRaked(objectX, objectY) ||
		 * player.getHops().checkIfRaked(objectX, objectY) ||
		 * player.getTrees().checkIfRaked(objectX, objectY) ||
		 * player.getSpecialPlantOne().checkIfRaked(objectX, objectY) ||
		 * player.getSpecialPlantTwo().checkIfRaked(objectX, objectY))) {
		 * player.getActionSender().sendMessage(
		 * "You can only fill your pot on raked patches."); return true; }
		 */

		if (!player.getInventory().getItemContainer().contains(FarmingConstants.TROWEL)) {
			player.getPA().sendMessage("You need a gardening trowel to fill this pot with soil.");
			return true;
		}
		player.getInventory().removeItem(new Item(itemId));
		player.getUpdateFlags().sendAnimation(FarmingConstants.FILLING_POT_ANIM);
		player.getPA().sendMessage("You fill the empty plant pot with soil.");
		player.getInventory().addItem(new Item(5354));
		return true;
	}
}
