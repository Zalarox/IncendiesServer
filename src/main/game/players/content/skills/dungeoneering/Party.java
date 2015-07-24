package main.game.players.content.skills.dungeoneering;

import main.game.players.Player;
import main.util.Misc;

/**
 * 
 * @author Thelife/Alex
 *
 */
public class Party {

	public Member[] members = new Member[5];

	public Floor floor;

	public int level, complexity, totalNpcs;

	public char difficulty;

	public Party(Member member) {
		this.members[0] = member;
	}

	public void setFloor(Floor floor) {
		this.floor = floor;
	}

	public Floor getFloor() {
		return floor;
	}

	public static void newMember(Player p) {
		if (p.partner == null)
			return;
		Party party = p.partner.party;
		if (party == null)
			return;
		if (party.floor != null) {
			p.sendMessage("The party has already started a floor.");
			return;
		}
		if (party.members[4] != null) {
			p.sendMessage("The party is already full.");
			return;
		}
		for (int i = 0; i < party.members.length; i++) {
			if (party.members[i] == null) {
				party.members[i] = new Member(p);
				p.party = party;
				p.sendMessage("You have joined the party!");
				Interface.clearParty(false, p);
				p.setSidebarInterface(14, 26224);
				break;
			}
		}
		for (int i = 0; i < party.members.length; i++) {
			if (party.members[i] != null && party.members[i].p != p) {
				party.members[i].p.sendMessage(p.playerName + " has joined the party!");
				Interface.clearParty(false, party.members[i].p);
				break;
			}
		}
	}

	public void leave(Player p) {
		// TODO Are you sure you would like to quit?
		if (p == null)
			return;
		if (p.party == null)
			return;
		if (p.party.floor != null) {
			p.getItems().dropAllItems();
			p.getItems().deleteAllItems();
			p.getItems().sendWeapon(p.getVariables().playerEquipment[p.playerWeapon],
					p.getItems().getItemName(p.getVariables().playerEquipment[p.playerWeapon]));
			p.getItems().resetBonus();
			p.getItems().getBonus();
			p.getItems().writeBonus();
			p.getCombat().getPlayerAnimIndex(
					p.getItems().getItemName(p.getVariables().playerEquipment[p.playerWeapon]).toLowerCase());
			p.getPA().requestUpdates();
			if (p.party.members[1] == null) {
				p.party.floor.clearFloor();
				Constants.openHeightLevels.remove(p.party.floor.heightLevel / 4 - 1);
			}
			p.getPA().movePlayer(3056 + Misc.random(1) - Misc.random(1), 4974 + Misc.random(1) - Misc.random(2), 1);
		}
		for (int i = 0; i < members.length; i++) {
			if (members[i] != null && p == members[i].p) {
				members[i] = null;
				for (int i2 = i; i2 < members.length; i2++) {
					if (members[i2] != null) {
						members[i2 - 1] = members[i2];
						members[i2] = null;
					}
				}
				for (int i3 = 0; i3 < members.length; i3++) {
					if (members[i3] != null) {
						members[i3].p.sendMessage(p.playerName + " has left the party!");
						Interface.clearParty(false, members[i3].p);
					}
				}
				p.updateWalkEntities();
				Interface.clearParty(true, p);
				p.sendMessage("You left the party.");
				return;
			}
		}
	}

}
