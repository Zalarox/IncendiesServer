package main.game.players.content.skills.dungeoneering;

import main.game.players.Player;

public class Interface {

	public static int[] strings = { 26235, 26236, 26237, 26238, 26239 };

	public void update(Player p) {
		p.setSidebarInterface(14, 26224);
		// p.getPA().sendFrame126(p.playerName, 26235);
		clearParty(false, p);
		loadStatistics(p, p.getInstance().party.getFloor());
	}

	public void loadStatistics(Player c, Floor f) {
		if (f == null) {
			c.getPA().sendFrame126("0", 26240);
			c.getPA().sendFrame126("0", 26241);
		}
		for (int i = 0; i < strings.length; i++)
			c.getPA().sendFrame126("" + f.level, strings[i]);
	}

	// id: 40224
	public static void loadInviteInterface(Player p, Player invited) {
		invited.getPA().showInterface(40224);
		for (int i = 0; i < p.party.members.length; i++) {
			if (p.party.members[i] != null) {
				invited.getPA().sendFrame126(p.party.members[i].p.playerName, 40235 + i);
				invited.getPA().sendFrame126(p.party.members[i].p.getSkillLevel(23) + "", 40242 + i);
				invited.getPA().sendFrame126(p.party.members[i].p.CombatLevel + "", 40247 + i);
				invited.getPA().sendFrame126(p.party.members[i].p.getPA().getHighestLevel(false) + "", 40252 + i);
				invited.getPA().sendFrame126(p.party.members[i].p.getPA().getTotalLevel() + "", 40257 + i);
			}
		}
	}

	public static void clearParty(boolean clear, Player p) {
		if (clear) {
			for (int i = 0; i < strings.length; i++) {
				p.getPA().sendFrame126("", strings[i] + 1000);
			}
			p.setSidebarInterface(14, 27224);
			p.party = null;
		} else if (p.getInstance().party != null) {
			for (int i = 0; i < strings.length; i++) {
				p.getPA().sendFrame126((p.getInstance().party.members[i] == null ? " "
						: p.getInstance().party.members[i].p.playerName), strings[i]);
			}
		}
	}

}
