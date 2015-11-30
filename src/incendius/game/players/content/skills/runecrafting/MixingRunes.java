package incendius.game.players.content.skills.runecrafting;

import incendius.Constants;
import incendius.game.items.Item;
import incendius.game.players.Player;
import incendius.handlers.Skill;
import incendius.util.Misc;

public class MixingRunes {

	public final static int BindingNeck = 5521;

	public enum MixingRunesData { // tali needed, altar id, elemental runes
									// needed
		MIST_RUNES_AIR(1438, 556, 2480, 4695, 6, 8), MIST_RUNES_WATER(1444, 555, 2478, 4695, 6, 8), DUST_RUNES_AIR(1438,
				556, 2481, 4696, 10, 8.3), DUST_RUNES_EARTH(1440, 557, 2478, 4696, 10, 8.3), MUD_RUNES_WATER(1444, 555,
						2481, 4698, 13, 9.3), MUD_RUNES_EARTH(1440, 557, 2480, 4698, 13, 9.3), SMOKE_RUNES_AIR(1438,
								556, 2482, 4697, 15, 9.5), SMOKE_RUNES_FIRE(1442, 554, 2478, 4697, 15,
										9.5), STEAM_RUNES_WATER(1444, 555, 2482, 4694, 19, 10), STEAM_RUNES_FIRE(1442,
												554, 2480, 4694, 19, 10), LAVA_RUNES_EARTH(1440, 557, 2482, 4699, 23,
														10.5), LAVA_RUNES_FIRE(1442, 554, 2481, 4699, 23, 10.5),;
		private int talismanId;
		private int elementalRuneId;
		private int altarId;
		private int combinedRune;
		private int levelRequired;
		private double experienceReceived;

		public static MixingRunesData forId(int itemUsed, int objectId) {
			for (MixingRunesData mixingRunesData : MixingRunesData.values()) {
				if (itemUsed == mixingRunesData.talismanId && objectId == mixingRunesData.altarId) {
					return mixingRunesData;
				}
			}
			return null;
		}

		MixingRunesData(int talismanId, int elementalRuneId, int altarId, int combinedRune, int levelRequired,
				double experienceReceived) {
			this.talismanId = talismanId;
			this.elementalRuneId = elementalRuneId;
			this.altarId = altarId;
			this.combinedRune = combinedRune;
			this.levelRequired = levelRequired;
			this.experienceReceived = experienceReceived;
		}

		public int getTalismanId() {
			return talismanId;
		}

		public int getElementalRuneId() {
			return elementalRuneId;
		}

		public int getCombinedRune() {
			return combinedRune;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public double getExperienceReceived() {
			return experienceReceived;
		}
	}

	public static boolean combineRunes(Player player, int itemUsed, int objectId) {
		MixingRunesData mixingRunesData = MixingRunesData.forId(itemUsed, objectId);
		if (mixingRunesData == null) {
			return false;
		}
		if (!Constants.RUNECRAFTING_ENABLED) {
			player.getPA().sendMessage("This skill is currently disabled.");
			return false;
		}
		if (!player.getInventory().playerHasItem(mixingRunesData.getTalismanId())) {
			player.getPA().sendMessage("You need a "
					+ player.getItems().getItemName(mixingRunesData.getTalismanId()).toLowerCase() + " to do this.");
			return true;
		}
		if (!player.getInventory().playerHasItem(mixingRunesData.getElementalRuneId())
				|| !player.getInventory().playerHasItem(Runecrafting.PureEss)) {
			player.getPA().sendMessage("You need pure essences to do this.");
			return true;
		}
		if (player.getSkill().getClientLevel(Skill.RUNECRAFTING) < mixingRunesData.getLevelRequired()) {
			player.getPA().sendMessage(
					"You need a runecrafting level of " + mixingRunesData.getLevelRequired() + " to do this.");
			return true;
		}
		if (player.getEquipment().getID(Constants.WEAPON) == BindingNeck) {
			player.setBindingNeckCharge(player.getBindingNeckCharge() - 1);
		}
		if (player.getBindingNeckCharge() <= 0) {
			player.setBindingNeckCharge(15);
			player.getEquipment().setEquipment(BindingNeck, Constants.AMULET);

			player.getPA().sendMessage("Your binding necklace crumble into dust.");
		}
		player.getInventory().removeItem(new Item(mixingRunesData.getTalismanId(), 1));
		player.getPA().sendMessage("You attempt to bind the Runes.");
		player.getUpdateFlags().sendAnimation(791);
		while (player.getInventory().playerHasItem(mixingRunesData.getElementalRuneId())
				&& player.getInventory().playerHasItem(Runecrafting.PureEss)) {
			player.getInventory().removeItem(new Item(mixingRunesData.getElementalRuneId(), 1));
			player.getInventory().removeItem(new Item(Runecrafting.PureEss, 1));
			if (Misc.random(1) == 0 || player.getEquipment().getID(Constants.WEAPON) == BindingNeck) {
				player.getInventory().addItem(new Item(mixingRunesData.getCombinedRune(), 1));
				player.getSkill().addExp(Skill.RUNECRAFTING, mixingRunesData.getExperienceReceived());
			}
		}
		return true;
	}
}
