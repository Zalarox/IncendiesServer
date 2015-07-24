/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.game.players.content.skills.crafting;

import java.util.HashMap;

import main.event.CycleEvent;
import main.event.CycleEventContainer;
import main.event.CycleEventHandler;
import main.game.items.Item;
import main.game.players.Player;
import main.handlers.Skill;
import main.util.Misc;

/**
 *
 * @author ArrowzFtw
 */

/**
 * Created by IntelliJ IDEA. User: vayken Date: 21/12/11 Time: 16:05 To change
 * this template use File | Settings | File Templates.
 */
public class SilverCrafting {
	public static final int SILVER_BAR = 2355;
	private static final int SILVER_ANIMATION = 899;

	public static enum SilverCraft {
		UNBLESSED(34205, 2355, 1716, 1, 16, 50), UNBLESSED5(34204, 2355, 1716, 5, 16, 50), UNBLESSED10(34203, 2355,
				1716, 10, 16, 50), UNBLESSEDX(34202, 2355, 1716, 0, 16, 50), UNHOLY(34209, 2355, 1724, 1, 17,
						50), UNHOLY5(34208, 2355, 1724, 5, 17, 50), UNHOLY10(34207, 2355, 1724, 10, 17, 50), UNHOLYX(
								34206, 2355, 1724, 0, 17, 50), SICKLE(34213, 2355, 2961, 1, 18, 50), SICKLE5(34212,
										2355, 2961, 5, 18, 50), SICKLE10(34211, 2355, 2961, 10, 18, 50), SICKLEX(34210,
												2355, 2961, 0, 18, 50), TIARA(34217, 2355, 5525, 1, 23, 52.5), TIARA5(
														34216, 2355, 5525, 5, 23, 52.5), TIARA10(34215, 2355, 5525, 10,
																23, 52.5), TIARAX(34214, 2355, 5525, 0, 23, 52.5);

		private int buttonId;
		private int used;
		private int result;
		private int amount;
		private int level;
		private double experience;

		public static HashMap<Integer, SilverCraft> silverItems = new HashMap<Integer, SilverCraft>();

		public static SilverCraft forId(int id) {
			return silverItems.get(id);
		}

		static {
			for (SilverCraft data : SilverCraft.values()) {
				silverItems.put(data.buttonId, data);
			}
		}

		private SilverCraft(int buttonId, int used, int result, int amount, int level, double experience) {
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

	public static void makeSilver(final Player c, int buttonId, final int amount) {
		final SilverCraft silverCraft = SilverCraft.forId(buttonId);
		if (silverCraft == null || (silverCraft.getAmount() == 0 && amount == 0))
			return;
		if (silverCraft.getUsed() == SILVER_BAR && c.getStatedInterface() == "silverCrafting") {

			if (!c.getInventory().getItemContainer().contains(SILVER_BAR)) {
				c.getDialogue().sendStatement("You need a silver bar to do this.");
				return;
			}
			if (c.getSkill().getLevel()[Skill.CRAFTING] < silverCraft.getLevel()) {
				c.getDialogue()
						.sendStatement("You need a crafting level of " + silverCraft.getLevel() + " to make this.");
				return;
			}
			c.sendAnimation(SILVER_ANIMATION);
			c.getPA().removeInterfaces();

			final int task = c.getTask();
			c.setSkilling(new CycleEvent() {
				int amnt = silverCraft.getAmount() != 0 ? silverCraft.getAmount() : amount;

				@Override
				public void execute(CycleEventContainer container) {
					if (!c.checkTask(task) || amnt == 0 || !c.getInventory().getItemContainer().contains(SILVER_BAR)) {
						container.stop();
						return;
					}
					container.setTick(3);
					c.getUpdateFlags().sendAnimation(SILVER_ANIMATION);
					c.getPA().sendMessage("You make the silver bar into "
							+ Misc.determineAorAn(
									new Item(silverCraft.getResult()).getDefinition().getName().toLowerCase())
							+ " " + new Item(silverCraft.getResult()).getDefinition().getName().toLowerCase() + ".");
					c.getInventory().removeItem(new Item(SILVER_BAR));
					c.getInventory().addItem(new Item(silverCraft.getResult()));
					c.getSkill().addExp(Skill.CRAFTING, silverCraft.getExperience());
					amnt--;

				}

				@Override
				public void stop() {
					c.resetAnimation();
				}
			});
			CycleEventHandler.getInstance().addEvent(c, c.getSkilling(), 1);

		}
	}

}
