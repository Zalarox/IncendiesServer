package main.game.npcs.data;

import main.event.CycleEvent;
import main.event.CycleEventContainer;
import main.event.CycleEventHandler;
import main.game.npcs.NPC;
import main.game.npcs.NPCHandler;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.util.Misc;

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
		o.getVariables().killerId = c.playerId;
		o.getVariables().underAttackBy = c.playerId;
		damage = SummoningData.getSummonDamage(n.npcType, false);
		if (c.getVariables().constitution - damage < 0)
			damage = c.getVariables().constitution;
		boolean usingSpecial = false;
		usingSpecial = false;
		if (damage >= SummoningData.getSummonMaxHit(n.npcType)) {
			damage = SummoningData.getSummonMaxHit(n.npcType);
		}
		c.getVariables().damageType = 0;
		if (n.npcType == 7344 && !NPCHandler.goodDistance(n.getX(), n.getY(), c.absX, c.absY, 3))
			c.getVariables().damageType = 1;
		if (!o.getVariables().usingSummoningSpecial) {
			c.getCombat().appendHit(c, damage, 0, c.getVariables().damageType, false, 0);
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
			c.getVariables().steelTitanTarget = c.playerId;
			c.getVariables().steelTitanDelay = 2;
			c.getVariables().doneSteelTitanDelay = true;
			o.getVariables().usingSummoningSpecial = false;
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
			c.getVariables().steelTitanTarget = c.playerId;
			c.getVariables().steelTitanDelay = 2;
			c.getVariables().doneSteelTitanDelay = true;
			o.getVariables().usingSummoningSpecial = false;
			usingSpecial = false;
			createEvent(c);
		}
		if (n.npcType == 7340 && usingSpecial == true) {
			o.getVariables().usingSummoningSpecial = false;
			c.getVariables().geyserTitanTarget = c.playerId;
			c.getVariables().geyserTitanDelay = 2;
			usingSpecial = false;
			createEvent(c);
			// c.getCombat().appendHit(c, dmg, 2, 0, true, 0);
		}
	}

	public void createEvent(final Player c) {
		CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (c.getVariables().steelTitanDelay != 0) {
					c.getVariables().steelTitanDelay--;
				}
				if (c.getVariables().steelTitanDelay == 0 && c.getVariables().doneSteelTitanDelay == true) {
					Player d = (PlayerHandler.players[c.getVariables().steelTitanTarget]);
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
					c.getCombat().appendHit(d, dmg3, c.getVariables().damageType, 0, false, 0);
					c.getCombat().appendHit(d, dmg4, c.getVariables().damageType, 0, false, 0);
					c.getVariables().doneSteelTitanDelay = false;
					container.stop();
				}
				if (c.getVariables().geyserTitanDelay != 1 && c.getVariables().geyserTitanDelay != 0) {
					c.getVariables().geyserTitanDelay--;
				}
				if (c.getVariables().geyserTitanDelay == 1) {
					Player d = (PlayerHandler.players[c.getVariables().geyserTitanTarget]);
					c.gfx0(1375);
					int dmg = SummoningData.getSummonDamage(7344, true);
					dmg = SummoningData.getSummonDamage(7340, true);
					if (dmg >= 300) {
						dmg = 300;
					}
					c.getCombat().appendHit(d, dmg, 2, 0, false, 0);
					c.getVariables().geyserTitanDelay = 0;
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
		if (!o.getVariables().usingSummoningSpecial) {
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
			c.getVariables().steelTitanTarget = c.playerId;
			c.getVariables().steelTitanDelay = 2;
			c.getVariables().doneSteelTitanDelay = true;
			o.getVariables().usingSummoningSpecial = false;
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
			c.getVariables().steelTitanTarget = c.playerId;
			c.getVariables().steelTitanDelay = 2;
			c.getVariables().doneSteelTitanDelay = true;
			o.getVariables().usingSummoningSpecial = false;
			usingSpecial = false;
		}
	}

}