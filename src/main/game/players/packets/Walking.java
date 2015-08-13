package main.game.players.packets;

import main.game.players.PacketType;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.game.players.content.minigames.DuelArena;
import main.handlers.Following;
import main.handlers.SkillHandler;

/**
 * Walking packet
 **/
public class Walking implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		if (c.getJail().isJailed()) {
			return;
		}
		
		if (c.getInstance().resting) {
			c.getPA().resetRest();
			return;
		}
		
		if (c.getInstance().doingAgility) {
			return;
		}
		
		c.sentWarning = false;
		c.getPA().resetSkills();
		
		if (!DuelArena.isDueling(c))
			c.getPA().closeActivities();
		
		if (DuelArena.isInFirstInterface(c) || DuelArena.isInSecondInterface(c)) {
			if (c.opponent != null)
				c.opponent.Dueling.declineDuel(c.opponent, true, true);
			c.Dueling.declineDuel(c, true, true);
			c.getPA().closeAllWindows();
		}
		
		c.faceUpdate(0);
		// c.playerIndex = 0;
		c.getInstance().npcIndex = 0;
		// c.followId = 0;
		c.getInstance().followId2 = 0;

		if (packetType == 248 || packetType == 164) {
			c.getInstance().clickObjectType = 0;
			c.getInstance().clickNpcType = 0;
			c.faceUpdate(0);
			c.getInstance().npcIndex = 0;
			c.getInstance().playerIndex = 0;
			if (c.getInstance().followId > 0 || c.getInstance().followId2 > 0)
				Following.resetFollow(c);
		}
		
		c.getPA().removeAllWindows();
		
		if (c.getInstance().duelRule[DuelArena.RULE_MOVEMENT] && DuelArena.isDueling(c)) {
			if (PlayerHandler.players[c.getInstance().duelingWith] != null) {
				if (!c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[c.getInstance().duelingWith].getX(),
						PlayerHandler.players[c.getInstance().duelingWith].getY(), 1)
						|| c.getInstance().attackTimer == 0) {
					c.sendMessage("Movement has been disabled in this duel!");
				}
			}
			c.getInstance().playerIndex = 0;
			return;
		}
		
		if (c.getInstance().playerSkilling[c.getInstance().playerFiremaking]) {
			return;
		}
		
		for (boolean ps : c.getInstance().playerSkilling) {
			if (ps)
				ps = false;
		}
		
		if (c.getInstance().playerIsWoodcutting)
			c.getInstance().playerIsWoodcutting = false;
		
		if (c.getInstance().teleTimer >= 1) {
			c.getInstance().playerIndex = 0;
			return;
		}
		
		if (c.getInstance().freezeTimer > 0) {
			if (PlayerHandler.players[c.getInstance().playerIndex] != null) {
				if (c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[c.getInstance().playerIndex].getX(),
						PlayerHandler.players[c.getInstance().playerIndex].getY(), 1) && packetType != 98) {
					c.getInstance().playerIndex = 0;
					return;
				}
			}
			
			if (packetType != 98) {
				c.sendMessage("A magical force stops you from moving.");
				c.getInstance().playerIndex = 0;
			}
			
			return;
		}

		if (System.currentTimeMillis() - c.getInstance().lastSpear < 4000) {
			c.sendMessage("You have been stunned.");
			c.getInstance().playerIndex = 0;
			return;
		}
		
		if (SkillHandler.playerIsBusy(c))
			if (c.getInstance().playerIsFishing) {
				c.startAnimation(65535);
				c.getPA().removeAllWindows();
				c.getInstance().playerIsFishing = false;
				for (int i = 0; i < 11; i++) {
					c.getInstance().fishingProp[i] = -1;
				}
			}

		if (c.getInstance().stopPlayerSkill) {
			c.getInstance().stopPlayerSkill = false;
			// CycleEventHandler.getInstance().stopEvents(c);
		}

		if (packetType == 98) {
			c.getInstance().mageAllowed = true;
		}

		if (DuelArena.isDueling(c)) {
			if (c.getInstance().killedDuelOpponent) {
				c.Dueling.claimDuelRewards(c);
				return;
			}
		}

		if (c.getInstance().respawnTimer > 3) {
			return;
		}
		
		if (c.getInstance().inTrade) {
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
		
		c.getInstance().interfaceIdOpen = 0;
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
