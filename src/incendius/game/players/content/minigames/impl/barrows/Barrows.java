package incendius.game.players.content.minigames.impl.barrows;

import incendius.event.CycleEvent;
import incendius.event.CycleEventContainer;
import incendius.event.CycleEventHandler;
import incendius.game.npcs.NPCHandler;
import incendius.game.players.Player;
import incendius.game.players.PlayerHandler;
import incendius.game.players.actions.DialogueHandler;
import incendius.util.Misc;

/**
 * 
 * @author IQuality and Tringan
 *
 */
public class Barrows {

	/*
	 * You've broken into a crypt! You've found a hidden tunnel, do you want to
	 * enter? Yeah I'm fearless No way that looks scary
	 * 
	 * chest- 3551, 9694
	 */
	public enum barrowData {
		// object,npc,HP,MAXHIT,ATTACK,DEFENCE,X,Y,TYPE,
		AHRIM(6821, 2025, 1050, 20, 175, 175, 3556, 9701, 0), DHAROK(6771, 2026, 1000, 24, 200, 200, 3554, 9716,
				1), GUTHAN(6773, 2027, 1000, 24, 275, 200, 3538, 9702, 2), KARIL(6822, 2028, 1050, 20, 175, 175, 3549,
						9683, 3), TORAG(6772, 2029, 1000, 23, 275, 200, 3568, 9686, 4), VERAC(6823, 2030, 1000, 24, 275,
								200, 3575, 9706, 5);

		private int objectId, npcId, HP, MAX_HIT, ATTACK, DEFENCE, X, Y, type;

		barrowData(int objectId, int npcId, int HP, int MAX_HIT, int ATTACK, int DEFENCE, int X, int Y, int type) {
			this.objectId = objectId;
			this.npcId = npcId;
			this.HP = HP;
			this.MAX_HIT = MAX_HIT;
			this.ATTACK = ATTACK;
			this.DEFENCE = DEFENCE;
			this.X = X;
			this.Y = Y;
			this.type = type;
		}

		public int getObject() {
			return objectId;
		}

		public int getAttack() {
			return ATTACK;
		}

		public int getDefence() {
			return DEFENCE;
		}

		public int getHP() {
			return HP;
		}

		public int getMaxHit() {
			return MAX_HIT;
		}

		public int getNpc() {
			return npcId;
		}

		public int getType() {
			return type;
		}

		public int getX() {
			return X;
		}

		public int getY() {
			return Y;
		}
	}

	/**
	 * Accesses the enum through object id
	 * 
	 * @param objectId
	 * @return
	 */
	private static barrowData getBarrowDataForObject(final int objectId) {
		for (barrowData barrowDatas : barrowData.values())
			if (barrowDatas.getObject() == objectId)
				return barrowDatas;
		return null;
	}

	/**
	 * Accesses the enum through npc id
	 * 
	 * @param objectId
	 * @return
	 */
	private static barrowData getBarrowDataForNpc(final int npcId) {
		for (barrowData barrowDatas : barrowData.values())
			if (barrowDatas.getNpc() == npcId)
				return barrowDatas;
		return null;
	}

	/**
	 * Spawns a npc(Barrows brother)
	 * 
	 * @param p
	 * @param index
	 */
	public static void spawnBrother(Player p, int index) {
		if (p == null)
			return;
		barrowData b = getBarrowDataForObject(index);
		if (p.getInstance().lastBrother == 0)
			p.getInstance().lastBrother = getLastBrother(BarrowsConstants.brotherData);
		if (!spawnedBrother(p, b.getType()) && !killedBrother(p, b.getType())) {
			if (p.getInstance().lastBrother == b.getNpc()) {
				sendTunnelDialogue(p);
				return;
			}
			p.getInstance().brotherSpawned[b.getType()] = true;
			NPCHandler.spawnNpc(p, b.getNpc(), b.getX(), b.getY(), -1, 0, b.getHP(), b.getMaxHit(), b.getAttack(),
					b.getDefence(), true, true);
		} else {
			p.sendMessage("You don't find anything.");
		}
	}

	/**
	 * Appends the Barrows death
	 * 
	 * @param i
	 */
	public static void appendBarrowsDeath(int i) {
		Player p = PlayerHandler.players[NPCHandler.npcs[i].killedBy];
		if (p != null) {
			for (int id = 0; id < BarrowsConstants.brotherData.length; id++) {
				if (NPCHandler.npcs[i].npcType == BarrowsConstants.brotherData[id]) {
					p.getInstance().brotherKilled[id] = true;
				}
			}
		}
	}

	/**
	 * Selects a random Regular item
	 * 
	 * @return
	 */
	public static int getRandomItem() {
		return BarrowsConstants.REGULAR_ITEMS[(int) (Math.random() * BarrowsConstants.REGULAR_ITEMS.length)][0];
	}

	/**
	 * Gives a reward when opening the chest
	 * 
	 * @param p
	 * @param randomItem
	 * @param secondItem
	 */
	public static void appendChest(Player p, int randomItem, int secondItem) {
		if (p.getInstance().lastBrother == 0 || getBarrowKillCount(p.getInstance().brotherKilled) < 5)
			return;
		barrowData b = getBarrowDataForNpc(p.getInstance().lastBrother);
		if (!killedBrother(p, b.getType())) {
			NPCHandler.spawnNpc(p, b.getNpc(), 3552, 9694, 0, 0, b.getHP(), b.getMaxHit(), b.getAttack(),
					b.getDefence(), true, true);
			p.getInstance().brotherSpawned[b.getType()] = true;
			return;
		}
		p.getPA().sendFrame99(0);
		p.getPA().sendFrame35(Misc.random(5), Misc.random(5), Misc.random(15), Misc.random(15));// sends
																								// earthquake
		appendRocks(p);
		if (Misc.random(BarrowsConstants.FIRST_BARROW_CHANCE) == 0)
			p.getItems().addItem(
					BarrowsConstants.BARROW_ITEMS[(int) (Math.random() * BarrowsConstants.BARROW_ITEMS.length)], 1);
		if (Misc.random(BarrowsConstants.SECOND_BARROW_CHANCE) == 0)
			p.getItems().addItem(
					BarrowsConstants.BARROW_ITEMS[(int) (Math.random() * BarrowsConstants.BARROW_ITEMS.length)], 1);
		for (int i = 0; i < BarrowsConstants.REGULAR_ITEMS.length; i++) {
			if (BarrowsConstants.REGULAR_ITEMS[i][0] == randomItem)
				p.getItems().addItem(randomItem, Misc.random(BarrowsConstants.REGULAR_ITEMS[i][1]));
			if (BarrowsConstants.REGULAR_ITEMS[i][0] == secondItem)
				p.getItems().addItem(secondItem, Misc.random(BarrowsConstants.REGULAR_ITEMS[i][1]));
		}
		resetBarrows(p);
	}

	/**
	 * Resets the earthquake
	 * 
	 * @param p
	 */
	public static void resetEarthquake(Player p) {
		p.getPA().sendFrame107();
	}

	/**
	 * Appends rock falling
	 * 
	 * @param p
	 */
	private static void appendRocks(final Player p) {
		CycleEventHandler.getSingleton().addEvent(p, new CycleEvent() {
			int timer = BarrowsConstants.ROCK_TIMER;

			@Override
			public void execute(CycleEventContainer container) {
				timer--;
				if (timer == 0) {
					int randomHit = 20 + Misc.random(50);
					p.getPA().createPlayersProjectile(p.absX, p.absY, p.absX, p.absY, 60, 60, 60, 43, 31,
							-p.playerId - 1, 0);
					if (p.getInstance().lifePoints - randomHit < 0)
						randomHit = p.getInstance().lifePoints;
					p.handleHitMask(randomHit, 0, -1, 0, false);
					p.dealDamage(randomHit);
					timer = BarrowsConstants.ROCK_TIMER;
				}
				if (!BarrowsConstants.inBarrows(p)) {
					container.stop();
				}
			}

			@Override
			public void stop() {
				resetEarthquake(p);
			}
		}, 1);
	}

	/**
	 * Digs the mount
	 * 
	 * @param p
	 */
	public static void digMount(Player p) {
		if (BarrowsConstants.inVeracsMount(p))
			p.getPA().movePlayer(3578, 9706, -1);
		else if (BarrowsConstants.inToragsMount(p))
			p.getPA().movePlayer(3568, 9683, -1);
		else if (BarrowsConstants.inAhrimsMount(p))
			p.getPA().movePlayer(3557, 9703, -1);
		else if (BarrowsConstants.inDhMount(p))
			p.getPA().movePlayer(3556, 9718, -1);
		else if (BarrowsConstants.inGuthansMount(p))
			p.getPA().movePlayer(3534, 9704, -1);
		else if (BarrowsConstants.inKarilsMount(p))
			p.getPA().movePlayer(3546, 9684, -1);
		p.sendMessage("You've broken into a crypt!");
		p.getPA().sendFrame99(2);
	}

	/**
	 * Gets the current kill count
	 * 
	 * @param index
	 * @return
	 */
	public static int getBarrowKillCount(boolean[] index) {
		int killCount = 0;
		for (int i = 0; i < index.length; i++)
			if (index[i])
				killCount++;
		return killCount;
	}

	/**
	 * Selects the last Barrow's brother to kill
	 * 
	 * @param input
	 * @return
	 */
	private static int getLastBrother(int[] input) {
		return input[(int) (Math.random() * input.length)];
	}

	/**
	 * Check if a certain brother is dead
	 * 
	 * @param p
	 * @param index
	 * @return
	 */
	private static boolean killedBrother(Player p, int index) {
		return p.getInstance().brotherKilled[index];
	}

	/**
	 * Checks if a brother was spawned
	 * 
	 * @param p
	 * @param index
	 * @return
	 */
	private static boolean spawnedBrother(Player p, int index) {
		return p.getInstance().brotherSpawned[index];
	}

	/**
	 * Resets the barrows
	 * 
	 * @param p
	 */
	public static void resetBarrows(Player p) {
		for (int index = 0; index < 6; index++) {
			p.getInstance().brotherSpawned[index] = false;
			p.getInstance().brotherKilled[index] = false;
		}
		p.getInstance().lastBrother = 0;
		System.out.println("Reseted barrows for player: " + p.playerName);
	}

	/**
	 * Accesses the stairCase enum
	 * 
	 * @param id
	 * @return
	 */
	private static StairCases getStairCaseForID(int id) {
		for (StairCases index : StairCases.values())
			if (index.getID() == id)
				return index;
		return null;
	}

	/**
	 * Handles the stairs
	 * 
	 * @param p
	 * @param index
	 */
	public static void handleStaircases(Player p, int index) {
		StairCases s = getStairCaseForID(index);
		p.getInstance().brotherSpawned[s.getType()] = false;
		p.getPA().movePlayer(s.getX() + Misc.random(2), s.getY() + Misc.random(2), 0);
		p.getPA().sendFrame99(0);
	}

	/**
	 * Handles the tunnel
	 * 
	 * @param p
	 */
	public static void handleTunnel(Player p) {
		if (!BarrowsConstants.DOORS_REQUIRED) {
			p.getPA().movePlayer(3551, 9690, 0);
		}
	}

	/**
	 * Sends a dialogue in order to enter the tunnel
	 * 
	 * @param p
	 */
	static void sendTunnelDialogue(Player p) {
		DialogueHandler.sendStatement(p, "You've found a hidden tunnel, do you want to enter?");
		p.getInstance().nextChat = 155;
	}

	/**
	 * Handles Barrows related objects
	 * 
	 * @param p
	 * @param objectId
	 */
	public static void handleObjects(Player p, int objectId) {
		switch (objectId) {
		case 982:
			handleTunnel(p);
			p.getPA().closeAllWindows();
			break;
		case 10284:
			Barrows.appendChest(p, getRandomItem(), getRandomItem());
			break;
		case 6771:
		case 6821:
		case 6822:
		case 6773:
		case 6772:
		case 6823:
			spawnBrother(p, objectId);
			break;
		case 6707: // verac
		case 6706: // torag
		case 6705: // karil stairs
		case 6704: // guthan stair
		case 6703: // dharok stairs
		case 6702: // ahrim stairs
			Barrows.handleStaircases(p, objectId);
			break;
		}
	}

}
