package incendius.game.players.content.skills.dungeoneering;

import incendius.Server;

public class Npc {

	public int x, y, z, c, m, a, d, id;

	public boolean s, dead;

	public Npc(int x, int y, int z, int c, int m, int a, int d, int id) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.c = c;
		this.m = m;
		this.a = a;
		this.d = d;
		this.id = id;
		this.s = false;
		this.dead = false;
	}

	public int getAbsX() {
		return x;
	}

	public int getAbsY() {
		return y;
	}

	public int getHeight() {
		return z;
	}

	public int getId() {
		return id;
	}

	public int getHitPoints() {
		return c;
	}

	public int getMaxHit() {
		return m;
	}

	public int getAttack() {
		return a;
	}

	public int getDefence() {
		return d;
	}

	public boolean spawned() {
		return s;
	}

	public boolean dead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}
	// TODO Get the value of the npc
	/*
	 * public static int getNPCValue(int type, int npcType) { if(type ==
	 * Constants.NPC_DUNGEON_SOUDER) return 1; }
	 */

	public void spawn() {
		if (!spawned() && !dead()) {
			Server.npcHandler.newNPC(getId(), getAbsX(), getAbsY(), getHeight(), 1, getHitPoints(), getMaxHit(),
					getAttack(), getDefence(), 1);
			s = true;
		}
	}

}
