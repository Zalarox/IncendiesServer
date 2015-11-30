package incendius.handlers;

import incendius.game.players.Player;
import incendius.game.players.PlayerHandler;
import incendius.game.players.actions.combat.CombatPrayer;

public class ProcessHandler {

	public static void executeProcess(Player c) {
		c.getInstance().diceTimer--;
		c.getInstance().specRestoreTimer--;
		c.curses().handleProcess();
		if (System.currentTimeMillis() - c.getInstance().lastPoison > 20000 && c.getInstance().poisonDamage > 0) {
			int damage = c.getInstance().poisonDamage / 2;
			if (damage > 0) {
				c.getInstance().lastPoison = System.currentTimeMillis();
				c.getCombat().appendHit(c, damage, 2, -1, false);
				c.getInstance().poisonDamage--;
			} else {
				c.getInstance().poisonDamage = -1;
				c.sendMessage("The poison has worn off.");
			}
		}
		if (c.getAwaitingUpdate()) {
			c.getItems().resetItems(3214);
		}
		CombatPrayer.handlePrayerDrain(c);

		if (System.currentTimeMillis() - c.getInstance().singleCombatDelay > 3300) {
			c.getInstance().underAttackBy = 0;
		}
		if (System.currentTimeMillis() - c.getInstance().singleCombatDelay2 > 3300) {
			c.getInstance().underAttackBy2 = 0;
		}
		if (c.getInstance().skullTimer > 0) {
			c.getInstance().skullTimer--;
			if (c.getInstance().skullTimer == 1) {
				c.getInstance().isSkulled = false;
				c.getInstance().attackedPlayers.clear();
				c.getInstance().headIconPk = -1;
				c.getInstance().skullTimer = -1;
				c.getPA().requestUpdates();
			}
		}

		if (c.isDead && c.getInstance().respawnTimer == -6) {
			c.getPA().applyDead();
		}

		if (c.getInstance().respawnTimer == 7) {
			c.getInstance().respawnTimer = -6;
			c.getPA().giveLife();
		} else if (c.getInstance().respawnTimer == 12) {
			c.getInstance().respawnTimer--;
			c.startAnimation(0x900);
			c.getInstance().poisonDamage = -1;
		}

		if (c.getInstance().respawnTimer > -6) {
			c.getInstance().respawnTimer--;
		}
		if (c.getInstance().freezeTimer > -6) {
			c.getInstance().freezeTimer--;
			if (c.getInstance().frozenBy > 0) {
				if (PlayerHandler.players[c.getInstance().frozenBy] == null) {
					c.getInstance().freezeTimer = -1;
					c.getInstance().frozenBy = -1;
				} else if (!c.goodDistance(c.absX, c.absY, PlayerHandler.players[c.getInstance().frozenBy].absX,
						PlayerHandler.players[c.getInstance().frozenBy].absY, 12)) {
					c.getInstance().freezeTimer = -1;
					c.getInstance().frozenBy = -1;
				}
			}
		}
		if (c.getInstance().hitDelay > 0) {
			c.getInstance().hitDelay--;
		}
		c.getSummoning().familiarTick();
		if (c.getInstance().hitDelay == 1) {
			if (c.getInstance().oldNpcIndex > 0) {
				c.getCombat().delayedHit(c.getInstance().oldNpcIndex);
			}
			if (c.getInstance().oldPlayerIndex > 0) {
				c.getCombat().playerDelayedHit(c.getInstance().oldPlayerIndex);
			}
		}

		if (c.getInstance().attackTimer > 0) {
			c.getInstance().attackTimer--;
		}

		if (c.getInstance().attackTimer == 1) {
			if (c.getInstance().npcIndex > 0 && c.getInstance().clickNpcType == 0) {
				c.getCombat().attackNpc(c.getInstance().npcIndex);
			}
			if (c.getInstance().playerIndex > 0) {
				c.getCombat().attackPlayer(c.getInstance().playerIndex);
			}
		} else if (c.getInstance().attackTimer <= 0
				&& (c.getInstance().npcIndex > 0 || c.getInstance().playerIndex > 0)) {
			if (c.getInstance().npcIndex > 0) {
				c.getInstance().attackTimer = 0;
				c.getCombat().attackNpc(c.getInstance().npcIndex);
			} else if (c.getInstance().playerIndex > 0) {
				c.getInstance().attackTimer = 0;
				c.getCombat().attackPlayer(c.getInstance().playerIndex);
			}
		}
	}

}
