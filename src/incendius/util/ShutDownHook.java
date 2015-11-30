package incendius.util;

import incendius.game.players.Player;
import incendius.game.players.PlayerHandler;

public class ShutDownHook extends Thread {

	@Override
	public void run() {
		System.out.println("Shutdown thread run.");
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c = PlayerHandler.players[j];
				incendius.game.players.PlayerSave.saveGame(c);
			}
		}
		System.out.println("Shutting down...");
	}

}