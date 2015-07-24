package main.game.players.packets;

import main.game.players.PacketType;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.game.players.content.minigames.impl.dueling.DuelPlayer;
import main.handlers.Following;
import main.handlers.SkillHandler;

/**
 * Walking packet
 **/
public class Walking implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		if (c.getVariables().resting) {
			c.getPA().resetRest();
			return;
		}
		if (c.getVariables().doingAgility) {
			return;
		}
		c.sentWarning = false;
		c.getPA().resetSkills();
		if (!DuelPlayer.contains(c))
			c.getPA().closeActivities();
		if (DuelPlayer.isInFirstScreen(c) || DuelPlayer.isInSecondScreen(c)) {
			if (c.opponent != null)
				c.opponent.Dueling.declineDuel(c.opponent, true, true);
			c.Dueling.declineDuel(c, true, true);
			c.getPA().closeAllWindows();
		}
		c.faceUpdate(0);
		// c.playerIndex = 0;
		c.getVariables().npcIndex = 0;
		// c.followId = 0;
		c.getVariables().followId2 = 0;

		if (packetType == 248 || packetType == 164) {
			c.getVariables().clickObjectType = 0;
			c.getVariables().clickNpcType = 0;
			c.faceUpdate(0);
			c.getVariables().npcIndex = 0;
			c.getVariables().playerIndex = 0;
			if (c.getVariables().followId > 0 || c.getVariables().followId2 > 0)
				Following.resetFollow(c);
		}
		c.getPA().removeAllWindows();
		if (c.getVariables().duelRule[DuelPlayer.RULE_WALKING] && DuelPlayer.contains(c)) {
			if (PlayerHandler.players[c.getVariables().duelingWith] != null) {
				if (!c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[c.getVariables().duelingWith].getX(),
						PlayerHandler.players[c.getVariables().duelingWith].getY(), 1)
						|| c.getVariables().attackTimer == 0) {
					c.sendMessage("Walking has been disabled in this duel!");
				}
			}
			c.getVariables().playerIndex = 0;
			return;
		}
		if (c.getVariables().playerSkilling[c.getVariables().playerFiremaking]) {
			return;
		}
		for (boolean ps : c.getVariables().playerSkilling) {
			if (ps)
				ps = false;
		}
		if (c.getVariables().playerIsWoodcutting)
			c.getVariables().playerIsWoodcutting = false;
		if (c.getVariables().teleTimer >= 1) {
			c.getVariables().playerIndex = 0;
			return;
		}
		if (c.getVariables().freezeTimer > 0) {
			if (PlayerHandler.players[c.getVariables().playerIndex] != null) {
				if (c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[c.getVariables().playerIndex].getX(),
						PlayerHandler.players[c.getVariables().playerIndex].getY(), 1) && packetType != 98) {
					c.getVariables().playerIndex = 0;
					return;
				}
			}
			if (packetType != 98) {
				c.sendMessage("A magical force stops you from moving.");
				c.getVariables().playerIndex = 0;
			}
			return;
		}

		if (System.currentTimeMillis() - c.getVariables().lastSpear < 4000) {
			c.sendMessage("You have been stunned.");
			c.getVariables().playerIndex = 0;
			return;
		}
		if (SkillHandler.playerIsBusy(c))
			if (c.getVariables().playerIsFishing) {
				c.startAnimation(65535);
				c.getPA().removeAllWindows();
				c.getVariables().playerIsFishing = false;
				for (int i = 0; i < 11; i++) {
					c.getVariables().fishingProp[i] = -1;
				}
			}

		if (c.getVariables().stopPlayerSkill) {
			c.getVariables().stopPlayerSkill = false;
			// CycleEventHandler.getInstance().stopEvents(c);
		}

		if (packetType == 98) {
			c.getVariables().mageAllowed = true;
		}

		if (DuelPlayer.contains(c)) {
			if (c.getVariables().killedDuelOpponent) {
				c.Dueling.claimStakedItems(c);
				return;
			}
		}

		if (c.getVariables().respawnTimer > 3) {
			return;
		}
		if (c.getVariables().inTrade) {
			c.getTradeHandler().declineTrade(false);
			return;
		}
		if (packetType == 248) {
			packetSize -= 14;
		}
		c.newWalkCmdSteps = (packetSize - 5) / 2;
		if (++c.newWalkCmdSteps > c.walkingQueueSize) {
			c.newWalkCmdSteps = 0;
			return;
		}
		c.getVariables().interfaceIdOpen = 0;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		final int firstStepX = c.getInStream().readSignedWordBigEndianA() - c.getMapRegionX() * 8;
		for (int i = 1; i < c.newWalkCmdSteps; i++) {
			c.getNewWalkCmdX()[i] = c.getInStream().readSignedByte();
			c.getNewWalkCmdY()[i] = c.getInStream().readSignedByte();
		}

		final int firstStepY = c.getInStream().readSignedWordBigEndian() - c.getMapRegionY() * 8;
		c.setNewWalkCmdIsRunning(c.getInStream().readSignedByteC() == 1);
		for (int i1 = 0; i1 < c.newWalkCmdSteps; i1++) {
			c.getNewWalkCmdX()[i1] += firstStepX;
			c.getNewWalkCmdY()[i1] += firstStepY;
		}
		if ((c.absX - (c.mapRegionX * 8)) - firstStepX != 0 || (c.absY - (c.mapRegionY * 8)) - firstStepY != 0)
			c.startAnimation(65535);
		c.updateWalkEntities();
	}

}
