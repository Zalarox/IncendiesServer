package incendius.game.players.content.skills.dungeoneering.npcs;

import incendius.util.Misc;

public class Forgotten_Ranger {

	public int getRandom() {
		int r = Misc.random(52);
		return 10328 + r;
	}

	public int getMaxHit(int id) {
		return 8 + ((10363 - id) / 9);
	}

	public int getAttack(int id) {
		return 45 + (10363 - id);
	}

	public int getDefence(int id) {
		return 30 + (10363 - id);
	}

	public int getConstitution(int id) {
		return 200 + ((10363 - id) * 5);
	}

}
