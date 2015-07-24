package main.game.players.content.skills.dungeoneering.npcs;

import main.util.Misc;

public class Dungeon_Spider {

	public int getRandom() {
		int r = Misc.random(9);
		return 10496 + r;
	}

	public int getMaxHit(int id) {
		return 4 + ((10505 - id) / 9);
	}

	public int getAttack(int id) {
		return 30 + (10505 - id);
	}

	public int getDefence(int id) {
		return 35 + (10505 - id);
	}

	public int getConstitution(int id) {
		return 100 + ((10385 - id) * 10);
	}

}
