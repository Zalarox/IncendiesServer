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
		for (int level = 0; level < player.getVariables().playerLevel.length; level++) {
			if (player.getVariables().playerLevel[level] < (player
					.getLevelForXP(player.getVariables().playerXP[level]))) {
				if (level != 5) { // prayer doesn't restore
					player.getVariables().playerLevel[level] += 1;
					player.getPA().setSkillLevel(level, player.getVariables().playerLevel[level],
							player.getVariables().playerXP[level]);
					player.getPA().refreshSkill(level);
				}
			} else if (player.getVariables().playerLevel[level] > (player
					.getLevelForXP(player.getVariables().playerXP[level]))) {
				player.getVariables().playerLevel[level] -= 1;
				player.getPA().setSkillLevel(level, player.getVariables().playerLevel[level],
						player.getVariables().playerXP[level]);
				player.getPA().refreshSkill(level);
			}
		}
	}
}