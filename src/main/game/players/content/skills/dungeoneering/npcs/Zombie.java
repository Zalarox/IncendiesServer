package main.game.players.content.skills.dungeoneering.npcs;

import main.util.Misc;

public class Zombie {

	public int getRandom() {
		int r = Misc.random(21);
		return 10364 + r;
	}

	public int getMaxHit(int id) {
		return 8 + ((10385 - id) / 9);
	}

	public int getAttack(int id) {
		return 45 + (10385 - id);
	}

	public int getDefence(int id) {
		return 30 + (10385 - id);
	}

	public int getConstitution(int id) {
		return 200 + ((10385 - id) * 10);
	}

}
