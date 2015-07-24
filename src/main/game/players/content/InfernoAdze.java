package main.game.players.content;

import main.Constants;
import main.game.players.Player;
import main.game.players.content.skills.firemaking.LogData.logData;
import main.handlers.SkillHandler;
import main.util.Misc;

/**
 * 
 * @author Tringan/Tringan
 *
 */
public class InfernoAdze {

	/**
	 * Adze item id, mainly for orientation purposes
	 */
	public final static int itemID = 13661;

	/**
	 * Gets the minimum requirements to use it
	 * 
	 * @param p
	 * @return
	 */
	public final boolean getRequirements(Player p) {
		if (!(p.getVariables().playerLevel[p.playerWoodcutting] >= 61
				&& p.getVariables().playerLevel[p.playerFiremaking] >= 92
				&& p.getVariables().playerLevel[p.playerMining] >= 41)) {
			p.sendMessage("You need at least 92 Firemaking, 61 woodcutting and 41 mining to use this axe.");
			return false;
		}
		return true;
	}

	/**
	 * The adze's special effect
	 * 
	 * @param p
	 * @param logID
	 */
	public void cutAndFire(Player p, int logID) {
		int burnChance = Misc.random(2);
		if (burnChance == 2) {
			p.gfx(1776, 150);

			for (logData l : logData.values()) {
				if (logID == l.getLogId()) {
					p.getPA().addSkillXP(l.getXp() * SkillHandler.XPRates.FIREMAKING.getXPRate(), Constants.FIREMAKING);
					p.sendMessage("Your inferno adze burns the logs.");
					break;
				}
			}
		} else
			p.getItems().addItem(logID, 1);
	}
}
