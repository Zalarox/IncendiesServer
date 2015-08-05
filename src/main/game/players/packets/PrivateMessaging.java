package main.game.players.packets;

import main.Connection;
import main.Connection.ConnectionType;
import main.Constants;
import main.game.players.PacketType;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.util.Misc;

/**
 * Private messaging, friends etc
 **/
public class PrivateMessaging implements PacketType {

	public final int ADD_FRIEND = 188, SEND_PM = 126, REMOVE_FRIEND = 215, CHANGE_PM_STATUS = 95, REMOVE_IGNORE = 59,
			ADD_IGNORE = 133;

	@SuppressWarnings("unused")
	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		switch (packetType) {

		case ADD_FRIEND:
			c.getPA().closeAllWindows();
			c.getVariables().friendUpdate = true;
			long friendToAdd = c.getInStream().readQWord();
			boolean canAdd = true;

			for (int i1 = 0; i1 < c.getVariables().friends.length; i1++) {
				if (c.getVariables().friends[i1] != 0 && c.getVariables().friends[i1] == friendToAdd) {
					canAdd = false;
					c.sendMessage(friendToAdd + " is already on your friends list.");
				}
			}
			if (canAdd == true) {
				for (int i1 = 0; i1 < c.getVariables().friends.length; i1++) {
					if (c.getVariables().friends[i1] == 0) {
						c.getVariables().friends[i1] = friendToAdd;
						for (int i2 = 1; i2 < Constants.MAX_PLAYERS; i2++) {
							if (PlayerHandler.players[i2] != null && PlayerHandler.players[i2].isActive
									&& Misc.playerNameToInt64(PlayerHandler.players[i2].playerName) == friendToAdd) {
								Player o = PlayerHandler.players[i2];
								if (o != null) {
									if (PlayerHandler.players[i2].getVariables().privateChat == 0
											|| (PlayerHandler.players[i2].getVariables().privateChat == 1
													&& o.getPA().isInPM(Misc.playerNameToInt64(c.playerName)))) {
										c.getPA().loadPM(friendToAdd, 1);
										break;
									}
								}
							}
						}
						break;
					}
				}
			}
			break;

		case SEND_PM:
			long sendMessageToFriendId = c.getInStream().readQWord();
			byte pmchatText[] = new byte[100];
			int pmchatTextSize = (byte) (packetSize - 8);
			c.getInStream().readBytes(pmchatText, pmchatTextSize, 0);
			if (Connection.containsConnection(c.playerName, ConnectionType.forName("MUTE"), false)
					|| Connection.containsConnection(c.connectedFrom, ConnectionType.forName("IPMUTE"), false)
					|| Connection.containsConnection(c.getVariables().identityPunishment,
							ConnectionType.forName("IDENTITY_MUTE"), false))
				break;
			for (int i1 = 0; i1 < c.getVariables().friends.length; i1++) {
				if (c.getVariables().friends[i1] == sendMessageToFriendId) {
					boolean pmSent = false;

					for (int i2 = 1; i2 < Constants.MAX_PLAYERS; i2++) {
						if (PlayerHandler.players[i2] != null && PlayerHandler.players[i2].isActive && Misc
								.playerNameToInt64(PlayerHandler.players[i2].playerName) == sendMessageToFriendId) {
							Player o = PlayerHandler.players[i2];
							if (o != null) {
								if (PlayerHandler.players[i2].getVariables().privateChat == 0
										|| (PlayerHandler.players[i2].getVariables().privateChat == 1
												&& o.getPA().isInPM(Misc.playerNameToInt64(c.playerName)))) {
									o.getPA().sendPM(Misc.playerNameToInt64(c.playerName),
											c.getVariables().playerRights, pmchatText, pmchatTextSize);
									pmSent = true;
				
									c.getLogging().logPM(o.getDisplayName(), Misc.textUnpack(pmchatText, pmchatTextSize));
								}
							}
							break;
						}
					}
					if (!pmSent) {
						c.sendMessage("That player is currently offline.");
						break;
					}
				}
			}
			break;

		case REMOVE_FRIEND:
			c.getPA().closeAllWindows();
			c.getVariables().friendUpdate = true;
			long friendToRemove = c.getInStream().readQWord();

			for (int i1 = 0; i1 < c.getVariables().friends.length; i1++) {
				if (c.getVariables().friends[i1] == friendToRemove) {
					for (int i2 = 1; i2 < Constants.MAX_PLAYERS; i2++) {
						Player o = PlayerHandler.players[i2];
						if (o != null) {
							if (c.getVariables().friends[i1] == Misc
									.playerNameToInt64(PlayerHandler.players[i2].playerName)) {
								o.getPA().updatePM(c.playerId, 0);
								break;
							}
						}
					}
					c.getVariables().friends[i1] = 0;
					break;
				}
			}
			break;

		case REMOVE_IGNORE:
			break;

		case CHANGE_PM_STATUS:
			int tradeAndCompete = c.getInStream().readUnsignedByte();
			c.getVariables().privateChat = c.getInStream().readUnsignedByte();
			int publicChat = c.getInStream().readUnsignedByte();
			for (int i1 = 1; i1 < Constants.MAX_PLAYERS; i1++) {
				if (PlayerHandler.players[i1] != null && PlayerHandler.players[i1].isActive == true) {
					Player o = PlayerHandler.players[i1];
					if (o != null) {
						o.getPA().updatePM(c.playerId, 1);
					}
				}
			}
			break;

		case ADD_IGNORE:

			break;

		}

	}
}