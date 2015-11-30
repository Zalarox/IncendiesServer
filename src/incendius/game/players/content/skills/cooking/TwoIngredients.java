package incendius.game.players.content.skills.cooking;

import java.util.HashMap;

import incendius.game.players.Player;
import incendius.handlers.SkillHandler;

public class TwoIngredients {
	public static enum MixItemData {
		STEW(2142, 1942, 1921, 1999, 2001, 25, 117, true, 0, 0), STEW1(1942, 2142, 1921, 1997, 2001, 25, 117, true, 0,
				0), PLAIN_PIZZA(1982, 1985, 2283, 2285, 2287, 35, 143, true, 0, 0), TUNE_AND_CORN(361, 5988, 1923, 7086,
						7068, 67, 204, true, 0, 0), TUNE_AND_CORN2(5988, 361, 1923, 7088, 7068, 67, 204, true, 0, 0),

		;

		private int primaryIngredient;
		private int secondIngredient;
		private int recipient;
		private int firstStage;
		private int result;
		private int level;
		private double experience;
		private boolean hasRecipient;
		private int emptyOne;
		private int emptyTwo;

		public static HashMap<Integer, MixItemData> firstItems = new HashMap<Integer, MixItemData>();
		public static HashMap<Integer, MixItemData> firstStages = new HashMap<Integer, MixItemData>();

		public static MixItemData forIdPrimary(int id) {
			return firstItems.get(id);
		}

		public static MixItemData forIdFirstStage(int id) {
			return firstStages.get(id);
		}

		static {
			for (MixItemData data : MixItemData.values()) {
				firstItems.put(data.primaryIngredient, data);
				firstStages.put(data.firstStage, data);
			}
		}

		private MixItemData(int primaryIngredient, int secondIngredient, int recipient, int firstStage, int result,
				int level, double experience, boolean hasRecipient, int emptyOne, int emptyTwo) {
			this.primaryIngredient = primaryIngredient;
			this.secondIngredient = secondIngredient;
			this.recipient = recipient;
			this.firstStage = firstStage;
			this.result = result;
			this.level = level;
			this.experience = experience;
			this.hasRecipient = hasRecipient;
			this.emptyOne = emptyOne;
			this.emptyTwo = emptyTwo;
		}

		public int getPrimaryIngredient() {
			return primaryIngredient;
		}

		public int getSecondIngredient() {
			return secondIngredient;
		}

		public int getFirstStage() {
			return firstStage;
		}

		public int getRecipient() {
			return recipient;
		}

		public int getResult() {
			return result;
		}

		public int getLevel() {
			return level;
		}

		public double getExperience() {
			return experience;
		}

		public boolean hasRecipient() {
			return hasRecipient;
		}

		public int getEmptyOne() {
			return emptyOne;
		}

		public int getEmptyTwo() {
			return emptyTwo;
		}

	}

	public static boolean mixItems(Player player, int itemUsed, int withItem, int itemUsedSlot, int withItemSlot) {
		MixItemData firstIngredient = MixItemData.forIdPrimary(itemUsed) != null ? MixItemData.forIdPrimary(itemUsed)
				: MixItemData.forIdPrimary(withItem);
		MixItemData firstStage = MixItemData.forIdFirstStage(itemUsed) != null ? MixItemData.forIdFirstStage(itemUsed)
				: MixItemData.forIdFirstStage(withItem);
		int ingredient = -1;
		int recipient = -1;
		int result = -1;
		MixItemData rightData = null;

		if (firstIngredient != null
				&& (itemUsed == firstIngredient.getRecipient() || withItem == firstIngredient.getRecipient())) {
			ingredient = firstIngredient.getPrimaryIngredient();
			recipient = firstIngredient.getRecipient();
			result = firstIngredient.getFirstStage();
			rightData = firstIngredient;
		}
		if (firstStage != null && (itemUsed == firstStage.getFirstStage() || withItem == firstStage.getFirstStage())
				&& (itemUsed == firstStage.getSecondIngredient() || withItem == firstStage.getSecondIngredient())) {

			ingredient = firstStage.getSecondIngredient();
			recipient = firstStage.getFirstStage();
			result = firstStage.getResult();
			rightData = firstStage;
		}

		if (rightData == null)
			return false;
		player.getPA().closeAllWindows();

		if (player.getInstance().playerLevel[player.getInstance().playerCooking] < rightData.getLevel()) {
			player.sendMessage("You need a cooking level of " + rightData.getLevel() + " to do this.");
			return true;
		}
		if (rightData.getResult() == 7068 && !player.getItems().contains(946)) {
			player.sendMessage("You need a knife for that.");
			return true;
		}
		if (rightData.hasRecipient())
			player.sendMessage("You put the " + player.getItems().getItemName(ingredient).toLowerCase() + " into the "
					+ player.getItems().getItemName(recipient).toLowerCase() + " and make a "
					+ player.getItems().getItemName(result).toLowerCase() + ".");
		else
			player.sendMessage("You mix the " + player.getItems().getItemName(ingredient).toLowerCase() + " with the "
					+ player.getItems().getItemName(recipient).toLowerCase() + " and make a "
					+ player.getItems().getItemName(result).toLowerCase() + ".");

		player.getItems().deleteItem(itemUsed, 1);
		player.getItems().deleteItem(withItem, 1);
		player.getItems().addItem(result, 1);
		player.getPA().addSkillXP((int) Math.ceil(rightData.getExperience() * SkillHandler.XPRates.COOKING.getXPRate()),
				player.getInstance().playerCooking);
		/* empty recipients */
		if (rightData.getEmptyOne() != 0 && rightData == firstIngredient)
			player.getItems().addItem(rightData.getEmptyOne(), 1);
		if (rightData.getEmptyTwo() != 0 && rightData == firstStage)
			player.getItems().addItem(rightData.getEmptyTwo(), 1);
		return true;
	}
}
