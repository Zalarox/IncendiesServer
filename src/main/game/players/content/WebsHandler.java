package main.game.players.content;

import main.GameEngine;
import main.event.Task;
import main.game.players.Player;
import main.util.Misc;

public class WebsHandler {

	/**
	 * Web respawn timer, 60000 = 60 seconds.
	 */
	private static int WEB_RESPAWN = 60000;

	private static boolean canSlashWeb(String item) {
		return item.contains("rapier") || item.contains("dagger") || item.contains("scimitar") || item.contains("sword")
				|| item.contains("whip") || item.contains("axe") || item.contains("claws");
	}

	public static void handleWebs(final Player p, final int objectID, final int x, final int y, final int h,
			final int face) {
		if (!canSlashWeb(p.getItems().getItemName(p.getVariables().playerEquipment[3]))) {
			p.sendMessage("You need a sharp weapon to slash the web!");
			return;
		}
		/*
		 * if (!p.inMageBank()) { p.sendMessage(
		 * "@red@You can only slash a web in mage bank!"); return; } //TODO: fix
		 * the coords for inMageBank
		 */
		if (Misc.random(1) == 0) {
			p.startAnimation(p.getCombat().getWepAnim(p.getItems().getItemName(p.getVariables().playerEquipment[3])));
			p.sendMessage("You failed to slash the web!");
			return;
		}
		p.startAnimation(p.getCombat().getWepAnim(p.getItems().getItemName(p.getVariables().playerEquipment[3])));
		p.sendMessage("You slash the web apart!");
		p.getPA().checkObjectSpawn(-1, x, y, face, 0);
		GameEngine.getScheduler().schedule(new Task(WEB_RESPAWN) {
			@Override
			public void execute() {
				p.getPA().checkObjectSpawn(733, x, y, face, 0);
				this.stop();
			}
		});
	}

}
