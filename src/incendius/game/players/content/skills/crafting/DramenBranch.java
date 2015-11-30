/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package incendius.game.players.content.skills.crafting;

/**
 *
 * @author ArrowzFtw
 */

import java.util.HashMap;

import incendius.event.CycleEvent;
import incendius.event.CycleEventContainer;
import incendius.event.CycleEventHandler;
import incendius.game.items.Item;
import incendius.game.players.Player;
import incendius.handlers.Skill;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 21/12/11 Time: 17:17 To change
 * this template use File | Settings | File Templates.
 */
public class DramenBranch {
	// 3224 5418 5376
	public static final int KNIFE = 946;
	public static final int LOG_CUTTING_ANIM = 1248;

	public static enum DramenItem {
		STAFF(10239, 771, 772, 1, 1, 31, 0), STAFF5(10238, 771, 772, 5, 1, 31, 0), STAFF10(6211, 771, 772, 27, 1, 31,
				0), STAFFX(6212, 771, 772, 0, 1, 31, 0);

		private int buttonId;
		private int used;
		private int result;
		private int amount;
		private int amountNeeded;
		private int level;
		private double experience;

		public static HashMap<Integer, DramenItem> dramenItems = new HashMap<Integer, DramenItem>();

		public static DramenItem forId(int id) {
			return dramenItems.get(id);
		}

		static {
			for (DramenItem data : DramenItem.values()) {
				dramenItems.put(data.buttonId, data);
			}
		}

		private DramenItem(int buttonId, int used, int result, int amount, int amountNeeded, int level,
				double experience) {
			this.buttonId = buttonId;
			this.used = used;
			this.result = result;
			this.amount = amount;
			this.amountNeeded = amountNeeded;
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

		public int getAmountNeeded() {
			return amountNeeded;
		}

		public int getLevel() {
			return level;
		}

		public double getExperience() {
			return experience;
		}

	}

	public static void cutDramen(final Player player, int buttonId, final int amount) {
		final DramenItem dramenItem = DramenItem.forId(buttonId);
		if (dramenItem == null || (dramenItem.getAmount() == 0 && amount == 0))
			return;

		if (player.getStatedInterface() == "dramenBranch") {

			if (player.getInventory().getItemAmount(dramenItem.getUsed()) < dramenItem.getAmountNeeded()) {
				player.getDialogue().sendStatement("You need " + dramenItem.getAmountNeeded() + " "
						+ new Item(dramenItem.getUsed()).getDefinition().getName().toLowerCase() + "s to do this.");
				return;
			}
			if (player.getSkill().getLevel()[Skill.CRAFTING] < dramenItem.getLevel()) {
				player.getDialogue()
						.sendStatement("You need a crafting level of " + dramenItem.getLevel() + " to make this.");
				return;
			}
			player.getPA().removeInterfaces();
			player.getUpdateFlags().sendAnimation(LOG_CUTTING_ANIM);

			final int task = player.getTask();
			player.setSkilling(new CycleEvent() {
				int amnt = dramenItem.getAmount() != 0 ? dramenItem.getAmount() : amount;

				@Override
				public void execute(CycleEventContainer container) {
					if (!player.checkTask(task) || amnt == 0 || player.getInventory()
							.getItemAmount(dramenItem.getUsed()) < dramenItem.getAmountNeeded()) {
						container.stop();
						return;
					}
					player.getUpdateFlags().sendAnimation(LOG_CUTTING_ANIM);
					player.getPA().sendMessage("You cut the branch into a "
							+ new Item(dramenItem.getResult()).getDefinition().getName().toLowerCase() + "s.");
					player.getInventory().removeItem(new Item(dramenItem.getUsed(), dramenItem.getAmountNeeded()));
					player.getInventory().addItem(new Item(dramenItem.getResult()));
					player.getSkill().addExp(Skill.CRAFTING, dramenItem.getExperience());
					amnt--;
				}

				@Override
				public void stop() {
					player.resetAnimation();
				}
			});
			CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 3);

		}

	}
}
