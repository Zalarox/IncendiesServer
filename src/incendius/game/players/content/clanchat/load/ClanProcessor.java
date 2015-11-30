package incendius.game.players.content.clanchat.load;

import incendius.Server;
import incendius.game.players.Player;

/**
 * Chat
 **/
public class ClanProcessor {

	private void writeRank(int j, Player c, String textSent, int rankId) {
		String[] ranks = { "null", "Recruits", "Corporals", "Sergeants", "Lieutenants", "Captains", "Generals" };
		Server.clanChat.clans[j].writeRank(c, textSent, ranks[rankId], rankId);
	}

	public void process(Player c, String textSent, int rank) {
		if (rank >= 0 && rank <= 6) {
			for (int j = 0; j < Server.clanChat.clans.length; j++) {
				if (Server.clanChat.clans[j] != null) {
					if (Server.clanChat.clans[j].owner.equalsIgnoreCase(c.playerName)) {
						Server.clanChat.clans[j].removeRecruit(textSent, c.playerName);
						Server.clanChat.clans[j].removeCorporal(textSent, c.playerName);
						Server.clanChat.clans[j].removeSergeant(textSent, c.playerName);
						Server.clanChat.clans[j].removeLieutenant(textSent, c.playerName);
						Server.clanChat.clans[j].removeCaptain(textSent, c.playerName);
						Server.clanChat.clans[j].removeGeneral(textSent, c.playerName);
						if (rank != 0)
							writeRank(j, c, textSent, rank);
						c.updateClanChatEditInterface(true);
						Server.clanChat.clans[j].changesMade = true;
					}
				}
			}
		}
		if (rank == 7) {
			for (int j = 0; j < Server.clanChat.clans.length; j++) {
				if (Server.clanChat.clans[j] != null) {
					if (Server.clanChat.clans[j].owner.equalsIgnoreCase(c.playerName)) {
						Server.clanChat.clans[j].writeRank(c, textSent, "Friends", 7);
						if (Server.clanChat.clans[j].changesMade != true)
							c.sendMessage("Changes will take effect on your clan chat in the next 60 seconds.");
					}
					Server.clanChat.clans[j].changesMade = true;
				}
			}
		}
		if (rank == 8) {
			for (int j = 0; j < Server.clanChat.clans.length; j++) {
				if (Server.clanChat.clans[j] != null) {
					if (Server.clanChat.clans[j].owner.equalsIgnoreCase(c.playerName)) {
						Server.clanChat.clans[j].removeFriend(textSent, c.playerName);
						Server.clanChat.clans[j].removeRecruit(textSent, c.playerName);
						Server.clanChat.clans[j].removeCorporal(textSent, c.playerName);
						Server.clanChat.clans[j].removeSergeant(textSent, c.playerName);
						Server.clanChat.clans[j].removeLieutenant(textSent, c.playerName);
						Server.clanChat.clans[j].removeCaptain(textSent, c.playerName);
						Server.clanChat.clans[j].removeGeneral(textSent, c.playerName);
						if (Server.clanChat.clans[j].changesMade != true)
							c.sendMessage("Changes will take effect on your clan chat in the next 60 seconds.");
					}
					Server.clanChat.clans[j].changesMade = true;
				}
			}
		}
	}
}
