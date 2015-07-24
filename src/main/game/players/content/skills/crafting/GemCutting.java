/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.game.players.content.skills.crafting;

/**
 *
 * @author ArrowzFtw
 */

import java.util.HashMap;

import main.event.CycleEvent;
import main.event.CycleEventContainer;
import main.event.CycleEventHandler;
import main.game.players.Player;
import main.handlers.Skill;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 21/12/11 Time: 13:53 To change
 * this template use File | Settings | File Templates.
 */
public class GemCutting {
	public static int CHISEL = 1755;

	public static boolean handleCutting(final Player player, int selectedItemId, int usedItemId, final int slot) {
		final int itemId = selectedItemId != CHISEL ? selectedItemId : usedItemId;
		if (selectedItemId != CHISEL && usedItemId != CHISEL) {
			return false;
		}
		final gemData gem = gemData.forId(itemId);
		if (gem != null) {

			if (!player.getInventory().getItemContainer().contains(CHISEL)) {
				return true;
			} else if (player.getSkill().getLevel()[Skill.CRAFTING] < gem.getLevel()) {
				player.getDialogue()
						.sendStatement("You need a crafting level of " + gem.getLevel() + " to cut this gem.");// wrong
				return true;
			}
			player.getUpdateFlags().sendAnimation(gem.getAnimation());
			final int task = player.getTask();
			player.setSkilling(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (!player.checkTask(task)) {
						container.stop();
						return;
					}
					player.getItems().deleteItem(itemId, 1);
					player.getItems().addItem(gem.getGemId(), 1);
					player.getSkill().addExp(Skill.CRAFTING, gem.getExperience());
					container.stop();
				}

				@Override
				public void stop() {
				}
			});
			CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 2);
			return true;
		}
		return false;
	}

	public static enum gemData {// uncut, gem, level, anim, experience
		SAPPHIRE(1623, 1607, 1, 888, 2500), EMERALD(1621, 1605, 30, 889, 3250), RUBY(1619, 1603, 40, 887,
				4250), DIAMOND(1617, 1601, 50, 886, 5000), DRAGONSTONE(1631, 1615, 60, 885, 6500), ONYX(6571, 6585, 70,
						885, 10000);

		private short uncutId;
		private short gemId;
		private byte level;
		private short animId;
		private double experience;

		public static HashMap<Integer, gemData> craftinggems = new HashMap<Integer, gemData>();

		public static gemData forId(int id) {
			return craftinggems.get(id);
		}

		static {
			for (gemData c : gemData.values()) {
				craftinggems.put(c.getId(), c);
			}
		}

		private gemData(int uncutId, int gemId, int level, int animId, double experience) {
			this.uncutId = (short) uncutId;
			this.gemId = (short) gemId;
			this.level = (byte) level;
			this.animId = (short) animId;
			this.experience = experience;
		}

		public int getId() {
			return uncutId;
		}

		public int getGemId() {
			return gemId;
		}

		public int getLevel() {
			return level;
		}

		public int getAnimation() {
			return animId;
		}

		public double getExperience() {
			return experience;
		}
	}
}
