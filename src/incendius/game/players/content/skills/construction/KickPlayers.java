package incendius.game.players.content.skills.construction;

import incendius.game.players.Boundaries;
import incendius.game.players.Player;
import incendius.game.players.PlayerHandler;
import incendius.game.players.Boundaries.Area;

public class KickPlayers {

	public void kickFromHouse(Player c, String type) {
		if (Boundaries.checkBoundaries(Area.CONSTRUCTION, c.getX(), c.getY())) {
			return;
		}
		c.CD.KICKED_EVERYONE = false;
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player Player2 = PlayerHandler.players[j];
				if (Boundaries.checkBoundaries(Area.GOD_WARS, Player2.getX(), Player2.getY())
						&& Player2.heightLevel == c.playerId * 4) {
					if (c.CD.KICKED_EVERYONE == false) {
						if (type != "BUILDING_MODE")
							c.sendMessage("You kick everyone from your house.");
						c.CD.KICKED_EVERYONE = true;
					}
					if (Player2.CD.CAN_MAKE_OBJECTS == false) {
						Player2.sendMessage(
								"You have been kicked from " + Player.capitalize(c.playerName) + "'s house.");
						Player2.getPA().movePlayer(2357, 3797, 0);
					}
				}
			}
		}
	}
}
