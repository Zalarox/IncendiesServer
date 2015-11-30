package incendius.game.players.packets;

import incendius.game.players.Boundaries;
import incendius.game.players.PacketType;
import incendius.game.players.Player;
import incendius.game.players.PlayerHandler;
import incendius.game.players.Boundaries.Area;
import incendius.game.players.content.minigames.DuelArena;

/**
 * Challenge Player
 **/
public class ChallengePlayer implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		switch (packetType) {
		case 128:
			int answerPlayer = c.getInStream().readUnsignedWord();
			if (c.getInstance().teleTimer > 0) {
				return;
			}
			if (answerPlayer > PlayerHandler.players.length) {
				return;
			}
			if (PlayerHandler.players[answerPlayer] == null) {
				return;
			}

			if (Boundaries.checkBoundaries(Area.ARENAS, c.getX(), c.getY()) || DuelArena.isDueling(c)) {
				c.sendMessage("You can't challenge inside the arena!");
				return;
			}
			c.Dueling.declineDuel(c, true, false);
			Player o = PlayerHandler.players[answerPlayer];
			if (o != null)
				c.Dueling.requestDuel(o, c, true);
			break;
		}
	}
}
