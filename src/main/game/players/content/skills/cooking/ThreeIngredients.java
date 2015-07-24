package main.game.players.content.skills.cooking;

import java.util.HashMap;

import main.game.players.Player;
import main.handlers.Skill;

public class ThreeIngredients {

	public static enum MixItemData {
		MUD_PIE(6032, 2953, 434, 2315, 7164, 7166, 7168, 29, 128, true, 3727, 3727, 0), GARDEN_PIE(1982, 1957, 1965,
				2315, 7172, 7174, 7176, 34, 138, true, 0, 0, 0), FISH_PIE(339, 333, 1942, 2315, 7182, 7184, 7186, 47,
						164, true, 0, 0,
						0), ADMIRAL_PIE(329, 361, 1942, 2315, 7192, 7194, 7196, 70, 210, true, 0, 0, 0), WILD_PIE(2136,
								2876, 3226, 2315, 7202, 7204, 7206, 85, 240, true, 0, 0,
								0), SUMMER_PIE(5504, 5982, 1955, 2315, 7212, 7214, 7216, 95, 260, true, 0, 0, 0);

		private int primaryIngredient;

		private int secondaryIngredient;
		private int thirdIngredient;
		private int recipient;
		private int firstStage;
		private int secondStage;
		private int result;
		private int level;
		private double experience;
		private boolean hasRecipient;
		private int emptyOne;
		private int emptyTwo;
		private int emptyThree;

		public static HashMap<Integer, MixItemData> firstItems = new HashMap<Integer, MixItemData>();
		public static HashMap<Integer, MixItemData> firstStages = new HashMap<Integer, MixItemData>();
		public static HashMap<Integer, MixItemData> secondStages = new HashMap<Integer, MixItemData>();

		public static MixItemData forIdPrimary(int id) {
			return firstItems.get(id);
		}

		public static MixItemData forIdFirstStage(int id) {
			return firstStages.get(id);
		}

		public static MixItemData forIdSecondStage(int id) {
			return secondStages.get(id);
		}

		static {
			for (MixItemData data : MixItemData.values()) {
				firstItems.put(data.primaryIngredient, data);
				firstStages.put(data.firstStage, data);
				secondStages.put(data.secondStage, data);
			}
		}

		private MixItemData(int primaryIngredient, int secondaryIngredient, int thirdIngredient, int recipient,
				int firstStage, int SecondStage, int result, int level, double experience, boolean hasRecipient,
				int emptyOne, int emptyTwo, int emptyThree) {
			this.primaryIngredient = primaryIngredient;
			this.secondaryIngredient = secondaryIngredient;
			this.thirdIngredient = thirdIngredient;
			this.recipient = recipient;
			this.firstStage = firstStage;
			this.secondStage = SecondStage;
			this.result = result;
			this.level = level;
			this.experience = experience;
			this.hasRecipient = hasRecipient;
			this.emptyOne = emptyOne;
			this.emptyTwo = emptyTwo;
			this.emptyThree = emptyThree;
		}

		public int getPrimaryIngredient() {
			return primaryIngredient;
		}

		public int getSecondaryIngredient() {
			return secondaryIngredient;
		}

		public int getThirdIngredient() {
			return thirdIngredient;
		}

		public int getFirstStage() {
			return firstStage;
		}

		public int getSecondStage() {
			return secondStage;
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

		public int getEmptyThree() {
			return emptyThree;
		}

	}

	public static boolean mixItems(Player c, int itemUsed, int withItem, int itemUsedSlot, int withItemSlot) {
		MixItemData firstIngredient = MixItemData.forIdPrimary(itemUsed) != null ? MixItemData.forIdPrimary(itemUsed)
				: MixItemData.forIdPrimary(withItem);
		MixItemData firstStage = MixItemData.forIdFirstStage(itemUsed) != null ? MixItemData.forIdFirstStage(itemUsed)
				: MixItemData.forIdFirstStage(withItem);
		MixItemData secondStage = MixItemData.forIdSecondStage(itemUsed) != null
				? MixItemData.forIdSecondStage(itemUsed) : MixItemData.forIdSecondStage(withItem);
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
				&& (itemUsed == firstStage.getSecondaryIngredient()
						|| withItem == firstStage.getSecondaryIngredient())) {

			ingredient = firstStage.getSecondaryIngredient();
			recipient = firstStage.getFirstStage();
			result = firstStage.getSecondStage();
			rightData = firstStage;
		}
		if (secondStage != null
				&& (itemUsed == secondStage.getSecondStage() || withItem == secondStage.getSecondStage())
				&& (itemUsed == secondStage.getThirdIngredient() || withItem == secondStage.getThirdIngredient())) {

			ingredient = secondStage.getThirdIngredient();
			recipient = secondStage.getSecondStage();
			result = secondStage.getResult();
			rightData = secondStage;
		}
		if (rightData == null)
			return false;
		c.getPA().closeAllWindows();

		if (c.getSkill().getLevel()[Skill.COOKING] < rightData.getLevel()) {
			c.sendMessage("You need a cooking level of " + rightData.getLevel() + " to do this.");
			return true;
		}
		if (rightData.hasRecipient())
			c.sendMessage("You put the " + c.getItems().getItemName(ingredient).toLowerCase() + " into the "
					+ c.getItems().getItemName(recipient).toLowerCase() + " and make a "
					+ c.getItems().getItemName(result).toLowerCase() + ".");
		else
			c.sendMessage("You mix the " + c.getItems().getItemName(ingredient).toLowerCase() + " with the "
					+ c.getItems().getItemName(recipient).toLowerCase() + " and make a "
					+ c.getItems().getItemName(result).toLowerCase() + ".");

		c.getItems().deleteItem(itemUsed, 1);
		c.getItems().deleteItem(withItem, 1);
		c.getItems().addItem(result, 1);
		c.getSkill().addExp(Skill.COOKING, rightData.getExperience() / 5);
		/* empty recipients */
		if (rightData.getEmptyOne() != 0 && rightData == firstIngredient)
			c.getItems().addItem(rightData.getEmptyOne(), 1);
		if (rightData.getEmptyTwo() != 0 && rightData == firstStage)
			c.getItems().addItem(rightData.getEmptyTwo(), 1);
		if (rightData.getEmptyThree() != 0 && rightData == secondStage)
			c.getItems().addItem(rightData.getEmptyThree(), 1);
		return true;
	}
}
