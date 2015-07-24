package main.util;

import main.game.players.Player;
import main.game.players.PlayerHandler;

public class ShutDownHook extends Thread {

	@Override
	public void run() {
		System.out.println("Shutdown thread run.");
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c = PlayerHandler.players[j];
				main.game.players.PlayerSave.saveGame(c);
			}
		}
		System.out.println("Shutting down...");
	}

}