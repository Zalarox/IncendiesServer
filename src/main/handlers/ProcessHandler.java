package main.handlers;

import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.game.players.actions.combat.CombatPrayer;

public class ProcessHandler {

	public static void executeProcess(Player c) {
		c.getVariables().diceTimer--;
		c.getVariables().specRestoreTimer--;
		c.curses().handleProcess();
		if (System.currentTimeMillis() - c.getVariables().lastPoison > 20000 && c.getVariables().poisonDamage > 0) {
			int damage = c.getVariables().poisonDamage / 2;
			if (damage > 0) {
				c.getVariables().lastPoison = System.currentTimeMillis();
				c.getCombat().appendHit(c, damage, 2, -1, false);
				c.getVariables().poisonDamage--;
			} else {
				c.getVariables().poisonDamage = -1;
				c.sendMessage("The poison has worn off.");
			}
		}
		if (c.getAwaitingUpdate()) {
			c.getItems().resetItems(3214);
		}
		CombatPrayer.handlePrayerDrain(c);

		if (System.currentTimeMillis() - c.getVariables().singleCombatDelay > 3300) {
			c.getVariables().underAttackBy = 0;
		}
		if (System.currentTimeMillis() - c.getVariables().singleCombatDelay2 > 3300) {
			c.getVariables().underAttackBy2 = 0;
		}
		if (c.getVariables().skullTimer > 0) {
			c.getVariables().skullTimer--;
			if (c.getVariables().skullTimer == 1) {
				c.getVariables().isSkulled = false;
				c.getVariables().attackedPlayers.clear();
				c.getVariables().headIconPk = -1;
				c.getVariables().skullTimer = -1;
				c.getPA().requestUpdates();
			}
		}

		if (c.isDead && c.getVariables().respawnTimer == -6) {
			c.getPA().applyDead();
		}

		if (c.getVariables().respawnTimer == 7) {
			c.getVariables().respawnTimer = -6;
			c.getPA().giveLife();
		} else if (c.getVariables().respawnTimer == 12) {
			c.getVariables().respawnTimer--;
			c.startAnimation(0x900);
			c.getVariables().poisonDamage = -1;
		}

		if (c.getVariables().respawnTimer > -6) {
			c.getVariables().respawnTimer--;
		}
		if (c.getVariables().freezeTimer > -6) {
			c.getVariables().freezeTimer--;
			if (c.getVariables().frozenBy > 0) {
				if (PlayerHandler.players[c.getVariables().frozenBy] == null) {
					c.getVariables().freezeTimer = -1;
					c.getVariables().frozenBy = -1;
				} else if (!c.goodDistance(c.absX, c.absY, PlayerHandler.players[c.getVariables().frozenBy].absX,
						PlayerHandler.players[c.getVariables().frozenBy].absY, 12)) {
					c.getVariables().freezeTimer = -1;
					c.getVariables().frozenBy = -1;
				}
			}
		}
		if (c.getVariables().hitDelay > 0) {
			c.getVariables().hitDelay--;
		}
		c.getSummoning().familiarTick();
		if (c.getVariables().hitDelay == 1) {
			if (c.getVariables().oldNpcIndex > 0) {
				c.getCombat().delayedHit(c.getVariables().oldNpcIndex);
			}
			if (c.getVariables().oldPlayerIndex > 0) {
				c.getCombat().playerDelayedHit(c.getVariables().oldPlayerIndex);
			}
		}

		if (c.getVariables().attackTimer > 0) {
			c.getVariables().attackTimer--;
		}

		if (c.getVariables().attackTimer == 1) {
			if (c.getVariables().npcIndex > 0 && c.getVariables().clickNpcType == 0) {
				c.getCombat().attackNpc(c.getVariables().npcIndex);
			}
			if (c.getVariables().playerIndex > 0) {
				c.getCombat().attackPlayer(c.getVariables().playerIndex);
			}
		} else if (c.getVariables().attackTimer <= 0
				&& (c.getVariables().npcIndex > 0 || c.getVariables().playerIndex > 0)) {
			if (c.getVariables().npcIndex > 0) {
				c.getVariables().attackTimer = 0;
				c.getCombat().attackNpc(c.getVariables().npcIndex);
			} else if (c.getVariables().playerIndex > 0) {
				c.getVariables().attackTimer = 0;
				c.getCombat().attackPlayer(c.getVariables().playerIndex);
			}
		}
	}

}
