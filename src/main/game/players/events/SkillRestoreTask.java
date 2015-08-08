package main.game.players.events;

import main.event.Task;
import main.game.players.Player;

public class SkillRestoreTask extends Task {

	private Player player;

	public SkillRestoreTask(Player player) {
		super(100);
		this.player = player;
	}

	@Override
	protected void execute() {
		for (int level = 0; level < player.getInstance().playerLevel.length; level++) {
			if (player.getInstance().playerLevel[level] < (player
					.getLevelForXP(player.getInstance().playerXP[level]))) {
				if (level != 5) { // prayer doesn't restore
					player.getInstance().playerLevel[level] += 1;
					player.getPA().setSkillLevel(level, player.getInstance().playerLevel[level],
							player.getInstance().playerXP[level]);
					player.getPA().refreshSkill(level);
				}
			} else if (player.getInstance().playerLevel[level] > (player
					.getLevelForXP(player.getInstance().playerXP[level]))) {
				player.getInstance().playerLevel[level] -= 1;
				player.getPA().setSkillLevel(level, player.getInstance().playerLevel[level],
						player.getInstance().playerXP[level]);
				player.getPA().refreshSkill(level);
			}
		}
	}
}