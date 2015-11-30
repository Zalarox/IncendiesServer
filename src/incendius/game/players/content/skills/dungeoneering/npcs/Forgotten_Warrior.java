package incendius.game.players.content.skills.dungeoneering.npcs;

import incendius.util.Misc;

public class Forgotten_Warrior {

	public int getRandom() {
		int r = Misc.random(16);
		return 10246 + r;
	}

	public int getMaxHit(int id) {
		return 8 + ((10262 - id) / 9);
	}

	public int getAttack(int id) {
		return 45 + (10262 - id);
	}

	public int getDefence(int id) {
		return 30 + (10262 - id);
	}

	public int getConstitution(int id) {
		return 200 + ((10262 - id) * 10);
	}

}
