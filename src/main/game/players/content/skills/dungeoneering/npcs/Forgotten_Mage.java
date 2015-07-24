package main.game.players.content.skills.dungeoneering.npcs;

import main.util.Misc;

public class Forgotten_Mage {

	public static int getRandom() {
		int r = Misc.random(33);
		return 10570 + r;
	}

	public static int getMaxHit(int id) {
		return 16 + ((10603 - id) / 9);
	}

	public static int getAttack(int id) {
		return 45 + (10603 - id);
	}

	public static int getDefence(int id) {
		return 30 + (10603 - id);
	}

	public static int getConstitution(int id) {
		return 200 + ((10603 - id) * 7);
	}

}
