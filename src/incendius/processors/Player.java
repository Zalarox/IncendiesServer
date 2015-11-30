package incendius.processors;

import java.awt.EventQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import incendius.game.players.PlayerHandler;

public class Player {

	private ScheduledExecutorService thread = Executors.newSingleThreadScheduledExecutor();

	private int i, a;

	public Player(int i) {
		this.i = i;
	}

	// gonna test if this is a prob with this code.
	public void run() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				thread.scheduleAtFixedRate(new Runnable() {
					@Override
					public void run() {
						/*
						 * if (PlayerHandler.kickAllPlayers) {
						 * PlayerHandler.players[i].disconnected = true; } if
						 * (PlayerHandler.players[i] == null ||
						 * !PlayerHandler.players[i].isActive) {
						 * PlayerHandler.players[i] = null;
						 * PlayerHandler.playerCount--; thread.shutdownNow(); }
						 * com.exile.game.players.Player c =
						 * (com.exile.game.players.Player)
						 * PlayerHandler.players[i]; if (c != null &&
						 * c.disconnected && (System.currentTimeMillis() -
						 * c.getVariables().logoutDelay > 10000 ||
						 * c.getVariables().properLogout ||
						 * PlayerHandler.kickAllPlayers)) { if
						 * (PlayerHandler.players[i].getVariables().inTrade) {
						 * if (c.opponent != null) {
						 * c.opponent.Dueling.declineDuel(c.opponent, true,
						 * false); } } if (c != null && DuelPlayer.contains(c))
						 * { if (c.opponent != null) {
						 * c.opponent.Dueling.duelVictory(c.opponent); } } else
						 * if (c != null && !DuelPlayer.contains(c)) { if
						 * (c.opponent != null) {
						 * c.opponent.Dueling.declineDuel(c.opponent, true,
						 * false); } } com.exile.game.players.Player o =
						 * (com.exile.game.players.Player)
						 * PlayerHandler.players[i]; if (PlayerSave.saveGame(o))
						 * { System.out.println("Game saved for player " +
						 * PlayerHandler.players[i].playerName); } else {
						 * System.out.println("Could not save for " +
						 * PlayerHandler.players[i].playerName); }
						 * PlayerHandler.removePlayer(PlayerHandler.players[i]);
						 * PlayerHandler.players[i] = null; }
						 * 
						 * PlayerHandler.players[i].preProcessing(); while
						 * (PlayerHandler.players[i].processQueuedPackets());
						 * PlayerHandler.players[i].process();
						 * PlayerHandler.players[i].postProcessing();
						 * PlayerHandler.players[i].getNextPlayerMovement();
						 * 
						 * 
						 * if (PlayerHandler.players[i] == null ||
						 * !PlayerHandler.players[i].isActive) {
						 * PlayerHandler.players[i] = null;
						 * PlayerHandler.playerCount--; thread.shutdownNow(); }
						 * if (PlayerHandler.players[i].disconnected &&
						 * (System.currentTimeMillis() -
						 * PlayerHandler.players[i].getVariables().logoutDelay >
						 * 10000 ||
						 * PlayerHandler.players[i].getVariables().properLogout
						 * || PlayerHandler.kickAllPlayers)) { if
						 * (PlayerHandler.players[i].getVariables().inTrade) {
						 * com.exile.game.players.Player o =
						 * (com.exile.game.players.Player)
						 * PlayerHandler.players[PlayerHandler.players[i].
						 * getVariables().tradeWith]; if (o != null) {
						 * o.getTradeHandler().declineTrade(false); } } if (c !=
						 * null && DuelPlayer.contains(c)) { if (c.opponent !=
						 * null) { c.opponent.Dueling.duelVictory(c.opponent); }
						 * } else if (c != null && !DuelPlayer.contains(c)) { if
						 * (c.opponent != null) {
						 * c.opponent.Dueling.declineDuel(c.opponent, true,
						 * false); } }
						 * 
						 * com.exile.game.players.Player o1 =
						 * (com.exile.game.players.Player)
						 * PlayerHandler.players[i]; if
						 * (PlayerSave.saveGame(o1)) { System.out.println(
						 * "Game saved for player " +
						 * PlayerHandler.players[i].playerName); } else {
						 * System.out.println("Could not save for " +
						 * PlayerHandler.players[i].playerName); }
						 * PlayerHandler.removePlayer(PlayerHandler.players[i]);
						 * PlayerHandler.players[i] = null; } else { if
						 * (!PlayerHandler.players[i].getVariables().
						 * initialized) { PlayerHandler.players[i].initialize();
						 * PlayerHandler.players[i].getVariables().initialized =
						 * true; } else { PlayerHandler.players[i].update(); }
						 * 
						 * } try { PlayerHandler.players[i].clearUpdateFlags();
						 * } catch(Exception e) { }
						 */
						PlayerHandler.process(i, thread);
						System.out.println(PlayerHandler.players[i].playerName + "" + a++);
					}
				}, 0, 600, TimeUnit.MILLISECONDS);
			}
		});
	}

}
