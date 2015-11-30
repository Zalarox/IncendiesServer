package incendius.game.npcs.data;

import incendius.event.CycleEvent;
import incendius.event.CycleEventContainer;
import incendius.event.CycleEventHandler;
import incendius.game.npcs.NPC;
import incendius.game.npcs.NPCHandler;
import incendius.game.players.Player;
import incendius.game.players.PlayerHandler;
import incendius.util.Misc;

/*if (10 + Misc.random(c.getCombat().calculateRangeDefence()) > Misc.random(Server.npcHandler.n.attack)) {
	damage = 0;
}
if (10 + Misc.random(c.getCombat().mageDef()) > Misc.random(Server.npcHandler.n.attack)) {
	damage = 0;
	magicFailed = true;
}
if (10 + Misc.random(c.getCombat().calculateMeleeDefence()) > Misc.random(Server.npcHandler.n.attack)) {
	damage = 0;
}	
*/

/**
 * 
 * Horrible code, needs redone
 *
 */
public class SummoningAttacking {

	private Player c;

	public SummoningAttacking(Player c) {
		this.c = c;
	}

	public void attackPlayer(int i) {
		int damage = 0;
		NPC n = NPCHandler.npcs[i];
		if (n == null)
			return;
		Player o = PlayerHandler.players[n.spawnedBy];
		if (o == null)
			return;
		o.getInstance().killerId = c.playerId;
		o.getInstance().underAttackBy = c.playerId;
		damage = SummoningData.getSummonDamage(n.npcType, false);
		if (c.getInstance().lifePoints - damage < 0)
			damage = c.getInstance().lifePoints;
		boolean usingSpecial = false;
		usingSpecial = false;
		if (damage >= SummoningData.getSummonMaxHit(n.npcType)) {
			damage = SummoningData.getSummonMaxHit(n.npcType);
		}
		c.getInstance().damageType = 0;
		if (n.npcType == 7344 && !NPCHandler.goodDistance(n.getX(), n.getY(), c.absX, c.absY, 3))
			c.getInstance().damageType = 1;
		if (!o.getInstance().usingSummoningSpecial) {
			c.getCombat().appendHit(c, damage, 0, c.getInstance().damageType, false, 0);
		} else {
			usingSpecial = true;
		}
		if (n.npcType != 7340 && usingSpecial == true
				&& NPCHandler.goodDistance(n.getX(), n.getY(), c.absX, c.absY, NPCHandler.distanceRequired(i))) {
			int dmg1 = 0;
			int dmg2 = 0;
			dmg1 = SummoningData.getSummonDamage(n.npcType, false);
			dmg2 = SummoningData.getSummonDamage(n.npcType, false);
			if (dmg1 >= 244)
				dmg1 = 244;
			if (dmg2 >= 244)
				dmg2 = 244;
			int random1 = 0;
			random1 = Misc.random(12 + 1);
			if (random1 <= 2)
				dmg1 = 0;
			int random2 = 0;
			random2 = Misc.random(12 + 1);
			if (random2 <= 2)
				dmg2 = 0;
			c.getCombat().appendHit(c, dmg1, 0, 0, true, 0);
			c.getCombat().appendHit(c, dmg2, 0, 0, true, 0);
			c.getInstance().steelTitanTarget = c.playerId;
			c.getInstance().steelTitanDelay = 2;
			c.getInstance().doneSteelTitanDelay = true;
			o.getInstance().usingSummoningSpecial = false;
			usingSpecial = false;
			createEvent(c);
		} else if (n.npcType != 7340 && usingSpecial == true
				&& !NPCHandler.goodDistance(n.getX(), n.getY(), c.absX, c.absY, NPCHandler.distanceRequired(i))) {
			int dmg1 = 0;
			int dmg2 = 0;
			dmg1 = SummoningData.getSummonDamage(n.npcType, false);
			dmg2 = SummoningData.getSummonDamage(n.npcType, false);
			if (dmg1 >= 244)
				dmg1 = 244;
			if (dmg2 >= 244)
				dmg2 = 244;
			int random1 = 0;
			random1 = Misc.random(12 + 1);
			if (random1 <= 2)
				dmg1 = 0;
			int random2 = 0;
			random2 = Misc.random(12 + 1);
			if (random2 <= 2)
				dmg2 = 0;
			c.getCombat().appendHit(c, dmg1, 0, 0, true, 0);
			c.getCombat().appendHit(c, dmg2, 0, 0, true, 0);
			c.getInstance().steelTitanTarget = c.playerId;
			c.getInstance().steelTitanDelay = 2;
			c.getInstance().doneSteelTitanDelay = true;
			o.getInstance().usingSummoningSpecial = false;
			usingSpecial = false;
			createEvent(c);
		}
		if (n.npcType == 7340 && usingSpecial == true) {
			o.getInstance().usingSummoningSpecial = false;
			c.getInstance().geyserTitanTarget = c.playerId;
			c.getInstance().geyserTitanDelay = 2;
			usingSpecial = false;
			createEvent(c);
			// c.getCombat().appendHit(c, dmg, 2, 0, true, 0);
		}
	}

	public void createEvent(final Player c) {
		CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (c.getInstance().steelTitanDelay != 0) {
					c.getInstance().steelTitanDelay--;
				}
				if (c.getInstance().steelTitanDelay == 0 && c.getInstance().doneSteelTitanDelay == true) {
					Player d = (PlayerHandler.players[c.getInstance().steelTitanTarget]);
					int dmg3 = SummoningData.getSummonDamage(7344, false);
					dmg3 = SummoningData.getSummonDamage(7344, false);
					int dmg4 = SummoningData.getSummonDamage(7344, false);
					dmg4 = SummoningData.getSummonDamage(7344, false);
					if (dmg3 >= 244) {
						dmg3 = 244;
					}
					if (dmg4 >= 244) {
						dmg4 = 244;
					}
					int random1 = 0;
					random1 = Misc.random(12 + 1);
					if (random1 <= 2) {
						dmg3 = 0;
					}
					int random2 = 0;
					random2 = Misc.random(12 + 1);
					if (random2 <= 2) {
						dmg4 = 0;
					}
					c.getCombat().appendHit(d, dmg3, c.getInstance().damageType, 0, false, 0);
					c.getCombat().appendHit(d, dmg4, c.getInstance().damageType, 0, false, 0);
					c.getInstance().doneSteelTitanDelay = false;
					container.stop();
				}
				if (c.getInstance().geyserTitanDelay != 1 && c.getInstance().geyserTitanDelay != 0) {
					c.getInstance().geyserTitanDelay--;
				}
				if (c.getInstance().geyserTitanDelay == 1) {
					Player d = (PlayerHandler.players[c.getInstance().geyserTitanTarget]);
					c.gfx0(1375);
					int dmg = SummoningData.getSummonDamage(7344, true);
					dmg = SummoningData.getSummonDamage(7340, true);
					if (dmg >= 300) {
						dmg = 300;
					}
					c.getCombat().appendHit(d, dmg, 2, 0, false, 0);
					c.getInstance().geyserTitanDelay = 0;
					container.stop();
				}
			}

			@Override
			public void stop() {
			}
		}, 1);
	}

	public void attackNpc(int i, int z) {
		int damage = 0;
		NPC n = NPCHandler.npcs[i];
		NPC d = NPCHandler.npcs[z];
		Player o = PlayerHandler.players[n.spawnedBy];
		if (o == null)
			return;
		if (d == null)
			return;
		d.underAttackBy = o.playerId;
		damage = SummoningData.getSummonDamage(n.npcType, false);
		if (d.HP - damage < 0)
			damage = d.HP;
		boolean usingSpecial = false;
		usingSpecial = false;
		if (damage >= SummoningData.getSummonMaxHit(n.npcType)) {
			damage = SummoningData.getSummonMaxHit(n.npcType);
		}
		if (!o.getInstance().usingSummoningSpecial) {
			o.sendMessage("Should have attacked...");
			c.getCombat().appendHit(d, damage, 0, 0, 1);
		} else {
			usingSpecial = true;
		}
		if (n.npcType != 7340 && usingSpecial == true
				&& NPCHandler.goodDistance(n.getX(), n.getY(), d.absX, d.absY, NPCHandler.distanceRequired(i))) {
			int dmg1 = 0;
			int dmg2 = 0;
			dmg1 = SummoningData.getSummonDamage(n.npcType, false);
			dmg2 = SummoningData.getSummonDamage(n.npcType, false);
			if (dmg1 >= 244)
				dmg1 = 244;
			if (dmg2 >= 244)
				dmg2 = 244;
			int random1 = 0;
			random1 = Misc.random(12 + 1);
			if (random1 <= 2)
				dmg1 = 0;
			int random2 = 0;
			random2 = Misc.random(12 + 1);
			if (random2 <= 2)
				dmg2 = 0;
			c.getCombat().appendHit(d, dmg1, 0, 0, 1);
			c.getCombat().appendHit(d, dmg2, 0, 0, 1);
			c.getInstance().steelTitanTarget = c.playerId;
			c.getInstance().steelTitanDelay = 2;
			c.getInstance().doneSteelTitanDelay = true;
			o.getInstance().usingSummoningSpecial = false;
			usingSpecial = false;
		} else if (n.npcType != 7340 && usingSpecial == true
				&& !NPCHandler.goodDistance(n.getX(), n.getY(), d.absX, d.absY, NPCHandler.distanceRequired(i))) {
			int dmg1 = 0;
			int dmg2 = 0;
			dmg1 = SummoningData.getSummonDamage(n.npcType, false);
			dmg2 = SummoningData.getSummonDamage(n.npcType, false);
			if (dmg1 >= 244)
				dmg1 = 244;
			if (dmg2 >= 244)
				dmg2 = 244;
			int random1 = 0;
			random1 = Misc.random(12 + 1);
			if (random1 <= 2)
				dmg1 = 0;
			int random2 = 0;
			random2 = Misc.random(12 + 1);
			if (random2 <= 2)
				dmg2 = 0;
			c.getCombat().appendHit(d, dmg1, 0, 0, 1);
			c.getCombat().appendHit(d, dmg2, 0, 0, 1);
			c.getInstance().steelTitanTarget = c.playerId;
			c.getInstance().steelTitanDelay = 2;
			c.getInstance().doneSteelTitanDelay = true;
			o.getInstance().usingSummoningSpecial = false;
			usingSpecial = false;
		}
	}

}