package main.game.players.content.skills.slayer;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import main.game.npcs.NPC;
import main.game.npcs.NPCHandler;
import main.game.players.Player;
import main.handlers.SkillHandler;
import main.util.Misc;

/**
 *
 * @author Harlan and Tringan/Rafael
 *
 **/

public class Slayer {

	/**
	 * Represents the tasks a player can get
	 * 
	 * @args npc name ( difficulty, reqs )
	 * @author Harlan edited a tiny bit by Tringan
	 *
	 */
	public enum Assignment {
		YAK(1, 1), GOBLIN(1, 1), ROCK_CRAB(1, 1), AL_KHARID_WARRIOR(1, 1), CHAOS_DRUID(1, 1), GHOST(1, 1), GIANT_BAT(1,
				1), HILL_GIANT(1, 1), CHAOS_DWARF(1, 1), CRAWLING_HAND(1, 5), BANSHEE(1, 15), MOSS_GIANT(2,
						1), ICE_WARRIOR(2, 1), BABY_BLUE_DRAGON(2, 1), INFERNAL_MAGE(2, 45), BLOODVELD(2,
								50), GREEN_DRAGON(2, 1), LESSER_DEMON(2, 1), DAGANNOTH(2, 1), DUST_DEVIL(3,
										65), GARGOYLE(3, 75), NECHRYAEL(3, 80), ABYSSAL_DEMON(3, 85), DARK_BEAST(3,
												90), BLUE_DRAGON(3, 1), BLACK_DEMON(3, 1), HELL_HOUND(3, 1);
		// ICE_STRYKEWYRM(4, 93),
		// DESERT_STRYKEWYRM(4, 77), TODO: Add spawns and ways to get to
		// Strykewyrm's
		// JUNGLE_STRYKEWYRM(4, 73);

		private Assignment(int req, int difficulty) {
			this.req = req;
			this.difficulty = difficulty;
		}

		public String toFormattedString() {
			return Misc.optimizeText(toString().toLowerCase().replaceAll("_", " "));
		}

		public int leftToKill() {
			return amountToKill;
		}

		public int getReq() {
			return req;
		}

		public int getDifficulty() {
			return difficulty;
		}

		private int amountToKill;
		private int req;
		private int difficulty;
	}

	/**
	 * Getter for currentTask
	 * 
	 * @return
	 */
	public Assignment getTask() {
		return currentTask;
	}

	/**
	 * Finds the player a random task out of the tasks they can perform
	 * 
	 * @return chosen task
	 */
	public Assignment findRandomTask(Player p, final int difficulty) {
		List<Assignment> possibleTasks = new LinkedList<Assignment>();
		if (Assignment.values() == null) {
			System.out.println("values r null");
			return null;
		}
		for (Assignment t : Assignment.values()) {
			if (canDoTask(p, t)) {
				t.difficulty = difficulty;
				switch (t.difficulty) {
				case 1:
					possibleTasks.add(t);
					break;
				case 2:
					possibleTasks.add(t);
					break;
				case 3:
					possibleTasks.add(t);
					break;
				case 4:
					possibleTasks.add(t);
					break;
				}
			}
		}
		return possibleTasks.get(r.nextInt(possibleTasks.size()));
	}

	/**
	 * Checks weather our player can do a task if it is given to us
	 * 
	 * @param task
	 *            to check
	 * @return
	 */
	public boolean canDoTask(Player p, Assignment t) {
		return p.getVariables().playerLevel[p.getVariables().playerSlayer] >= t.req;
	}

	public int getTaskDifficulty(Player p) {
		int d = 1;
		if (p.getVariables().combatLevel > 45 && p.getVariables().combatLevel < 75)
			d = 2;
		if (p.getVariables().combatLevel > 75 && p.getVariables().combatLevel < 100)
			d = 3;
		if (p.getVariables().combatLevel > 100)
			d = 4;
		return d;
	}

	/**
	 * Assigns the final task
	 */
	public void assignTask(Player p, final int difficulty) {
		p.getVariables().taskDifficulty = difficulty;
		while (currentTask == null)
			currentTask = findRandomTask(p, difficulty);
		currentTask.amountToKill = Misc.random(difficulty * 30) + 25;
		if (currentTask.amountToKill < 2) {
			currentTask.amountToKill = 3;
		}
		p.sendMessage("You have been assigned to kill " + currentTask.amountToKill + " "
				+ currentTask.toFormattedString() + "s.");
	}

	/**
	 * Checks if a player is able to slay
	 * 
	 * @param p
	 * @param npc
	 * @return
	 */
	public boolean ableToSlay(Player p, NPC n) {
		for (Assignment t : Assignment.values()) {
			if (p.getVariables().playerLevel[p.getVariables().playerSlayer] < t.getReq()
					&& n.getNpcName().contains(t.toFormattedString().toLowerCase())) {
				p.sendMessage("You need a slayer level of " + t.getReq() + " to harm this NPC.");
				return false;
			}
		}
		return true;
	}

	public void resetTask() {
		currentTask = null;
	}

	/**
	 * Checks if the npc that the player is killing is his task or not
	 * 
	 * @param n
	 * @return
	 */
	public boolean isSlayerTask(NPC n) {
		if (n != null && currentTask != null)
			return n.getNpcName().toLowerCase().contains(currentTask.toFormattedString().toLowerCase());
		return false;
	}

	/**
	 * Sets a task @Assignment
	 * 
	 * @param t
	 * @param amount
	 */
	public void setTask(Assignment t, int amount) {
		currentTask = t;
		currentTask.amountToKill = amount;
	}

	/**
	 * Sets a task @String
	 * 
	 * @param npc
	 * @param amount
	 */
	public void setTask(String npc, int amount) {
		currentTask = Assignment.valueOf(npc);
		currentTask.amountToKill = amount;
	}

	/**
	 * Decreases the kill amount, basically gives progress to the task
	 * 
	 * @param p
	 */
	public void decreaseKillAmount(Player p) {
		currentTask.amountToKill--;
		if (currentTask.amountToKill <= 0) {
			resetTask();
			p.getVariables().totalTasks += 1;
			p.getVariables().SlayerPoints += getReceivedPoints(p, p.getVariables().taskDifficulty)
					+ p.getVariables().getDonarPointbonus(getReceivedPoints(p, p.getVariables().taskDifficulty));
			p.sendMessage("You've completed " + p.getVariables().totalTasks
					+ " task in a row and received " + (getReceivedPoints(p, p.getVariables().taskDifficulty) + p
							.getVariables().getDonarPointbonus(getReceivedPoints(p, p.getVariables().taskDifficulty)))
					+ " points.");
			p.getVariables().taskDifficulty = 0;
		}
	}

	/**
	 * Gets the received points
	 * 
	 * @param p
	 * @param difficulty
	 * @return
	 */
	private int getReceivedPoints(Player p, final int difficulty) {
		int points = (difficulty == 1 ? 2 : difficulty == 2 ? 10 : difficulty == 3 ? 20 : 35);
		int bonus = 0;
		for (int Rows = 0; Rows < p.getVariables().totalTasks + 50; Rows++) {
			bonus = (p.getVariables().totalTasks == (10 * Rows) ? 5
					: p.getVariables().totalTasks == (50 * Rows) ? 15 : 1);
		}
		return points + bonus;
	}

	/**
	 * Appends a slayer task kill
	 **/
	public void appendSlayerExperience(Player p, int npc, NPC n) {
		if (p != null && isSlayerTask(n)) {
			p.getPA().addSkillXP(NPCHandler.npcs[npc].MaxHP * SkillHandler.XPRates.SLAYER.getXPRate(), 18);
			decreaseKillAmount(p);
		}
	}

	public Assignment currentTask;
	static Random r = new Random();

	// int points = 10 + ((tasks % 10 == 0) ? ((tasks % 50 == 0) ? 140 : 40) :
	// 0);
}