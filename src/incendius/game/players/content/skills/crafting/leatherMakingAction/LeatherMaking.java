package incendius.game.players.content.skills.crafting.leatherMakingAction;

import incendius.event.CycleEvent;
import incendius.event.CycleEventContainer;
import incendius.event.CycleEventHandler;
import incendius.game.items.Item;
import incendius.game.players.Player;
import incendius.game.players.content.skills.crafting.LeatherMakingHandler;
import incendius.handlers.Skill;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 30/12/11 Time: 14:01 To change
 * this template use File | Settings | File Templates.
 */
public abstract class LeatherMaking {

	public static final int THREAD = 1734;
	public static final int NEEDLE = 1733;
	public static final int LEATHER_MAKING = 1249;

	protected Player player;

	protected int used;

	protected int used2;

	protected int result;

	protected int amount;

	protected int manualAmount;

	protected int level;

	protected double experience;

	protected LeatherMaking(Player player, int used, int used2, int result, int amount, int manualAmount, int level,
			double experience) {
		this.player = player;
		this.used = used;
		this.used2 = used2;
		this.result = result;
		this.manualAmount = manualAmount;
		this.amount = amount;
		this.level = level;
		this.experience = experience;
	}

	public boolean makeLeatherAction() {
		player.getPA().removeInterfaces();
		if (!player.getInventory().getItemContainer().contains(NEEDLE)) {
			player.getDialogue().sendStatement("You need a needle to do this.");
			return true;
		}
		if (!player.getInventory().getItemContainer().contains(THREAD)) {
			player.getDialogue().sendStatement("You need thread to do this.");
			return true;
		}
		if (!player.getInventory().contains(used, used2)) {
			player.getDialogue().sendStatement("You need " + used2 + " "
					+ new Item(used).getDefinition().getName().toLowerCase() + " to do this.");
			return true;
		}
		if (player.getSkill().getLevel()[Skill.CRAFTING] < level) {
			player.getDialogue().sendStatement("You need a crafting level of " + level + " to make this.");
			return true;
		}

		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {
			int actionAmount = amount != 0 ? amount : manualAmount;

			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task) || actionAmount == 0) {
					player.resetAnimation();
					container.stop();
					return;
				}
				if (!player.getInventory().getItemContainer().contains(LeatherMakingHandler.THREAD)) {
					player.getPA().sendMessage("You have run out of thread!");
					container.stop();
					return;
				}
				if (!player.getInventory().contains(used, used2)) {
					player.getPA().sendMessage(
							"You have run out of " + new Item(used).getDefinition().getName().toLowerCase() + "!");
					container.stop();
					return;
				}
				player.getUpdateFlags().sendAnimation(LEATHER_MAKING);
				player.getPA()
						.sendMessage("You make some " + new Item(result).getDefinition().getName().toLowerCase() + ".");
				player.getInventory().removeItem(new Item(THREAD));
				player.getInventory().removeItem(new Item(used, used2));
				player.getInventory().addItem(new Item(result));
				player.getSkill().addExp(Skill.CRAFTING, experience);
				actionAmount--;
				container.setTick(3);

			}

			@Override
			public void stop() {
				player.resetAnimation();
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 1);
		return true;
	}

}
