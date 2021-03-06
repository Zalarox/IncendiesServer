/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package incendius.game.players.content.skills.crafting;

import java.util.HashMap;

import incendius.event.CycleEvent;
import incendius.event.CycleEventContainer;
import incendius.event.CycleEventHandler;
import incendius.game.items.Item;
import incendius.game.players.Player;
import incendius.handlers.Skill;

/**
 *
 * @author ArrowzFtw
 */

public class Spinning {
	private static final int SPINNING_ANIMATION = 896;

	public static enum SpinningWheel {
		WOOL(34185, 1737, 1759, 1, 1, 2.5), WOOL5(34184, 1737, 1759, 5, 1, 2.5), WOOL10(34183, 1737, 1759, 10, 1,
				2.5), WOOLX(34182, 1737, 1759, 0, 1, 2.5), FLAX(34189, 1779, 1777, 1, 10, 15), FLAX5(34188, 1779, 1777,
						5, 10, 15), FLAX10(34187, 1779, 1777, 10, 10, 15), FLAXX(34186, 1779, 1777, 0, 10,
								15), ROOT(34193, 6051, 6038, 1, 19, 30), ROOT5(34192, 6051, 6038, 5, 19,
										30), ROOT10(34191, 6051, 6038, 10, 19, 30), ROOTX(34190, 6051, 6038, 0, 19, 30);

		private int buttonId;
		private int used;
		private int result;
		private int amount;
		private int level;
		private double experience;

		public static HashMap<Integer, SpinningWheel> spinningItems = new HashMap<Integer, SpinningWheel>();

		public static SpinningWheel forId(int id) {
			return spinningItems.get(id);
		}

		static {
			for (SpinningWheel data : SpinningWheel.values()) {
				spinningItems.put(data.buttonId, data);
			}
		}

		private SpinningWheel(int buttonId, int used, int result, int amount, int level, double experience) {
			this.buttonId = buttonId;
			this.used = used;
			this.result = result;
			this.amount = amount;
			this.level = level;
			this.experience = experience;
		}

		public int getButtonId() {
			return buttonId;
		}

		public int getUsed() {
			return used;
		}

		public int getResult() {
			return result;
		}

		public int getAmount() {
			return amount;
		}

		public int getLevel() {
			return level;
		}

		public double getExperience() {
			return experience;
		}

	}

	public static void spin(final Player player, int buttonId, final int amount) {
		final SpinningWheel spinningWheel = SpinningWheel.forId(buttonId);
		if (spinningWheel == null || (spinningWheel.getAmount() == 0 && amount == 0))
			return;
		if (player.getStatedInterface() == "spinning") {

			if (!player.getInventory().getItemContainer().contains(spinningWheel.getUsed())) {
				player.getDialogue().sendStatement("You need "
						+ new Item(spinningWheel.getUsed()).getDefinition().getName().toLowerCase() + " to do this.");
				return;
			}
			if (player.getSkill().getLevel()[Skill.CRAFTING] < spinningWheel.getLevel()) {
				player.getDialogue()
						.sendStatement("You need a crafting level of " + spinningWheel.getLevel() + " to make this.");
				return;
			}
			player.getPA().removeInterfaces();
			player.sendAnimation(SPINNING_ANIMATION);

			final int task = player.getTask();
			player.setSkilling(new CycleEvent() {
				int amnt = spinningWheel.getAmount() != 0 ? spinningWheel.getAmount() : amount;

				@Override
				public void execute(CycleEventContainer container) {
					if (!player.checkTask(task) || amnt == 0
							|| !player.getInventory().getItemContainer().contains(spinningWheel.getUsed())) {
						container.stop();
						return;
					}
					player.sendAnimation(SPINNING_ANIMATION);
					player.getPA()
							.sendMessage("You make the " + new Item(spinningWheel.getUsed()).getDefinition().getName()
									+ " into a " + new Item(spinningWheel.getResult()).getDefinition().getName() + ".");
					player.getInventory().removeItem(new Item(spinningWheel.getUsed()));
					player.getInventory().addItem(new Item(spinningWheel.getResult()));
					player.getSkill().addExp(Skill.CRAFTING, spinningWheel.getExperience());
					amnt--;

				}

				@Override
				public void stop() {
					player.getPA().resetAnimation();
				}
			});
			CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 3);

		}

	}

}
