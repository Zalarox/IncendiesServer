package main.game.players;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

import org.apache.mina.common.IoSession;

import main.Constants;
import main.GameEngine;
import main.action.Action;
import main.event.CycleEvent;
import main.event.CycleEventContainer;
import main.event.CycleEventHandler;
import main.event.Task;
import main.game.items.GameItem;
import main.game.items.ItemAssistant;
import main.game.items.ItemLoader;
import main.game.items.degrade.Chaotic;
import main.game.npcs.NPC;
import main.game.npcs.NPCHandler;
import main.game.npcs.data.SummoningAttacking;
import main.game.npcs.transformers.TransformHandler;
import main.game.objects.Objects;
import main.game.players.Logging;
import main.game.players.Boundaries.Area;
import main.game.players.actions.ActionHandler;
import main.game.players.actions.Bank;
import main.game.players.actions.DialogueHandler;
import main.game.players.actions.Food;
import main.game.players.actions.Potions;
import main.game.players.actions.TradeHandler;
import main.game.players.actions.combat.CombatAssistant;
import main.game.players.actions.combat.CombatPrayer;
import main.game.players.actions.combat.Curses;
import main.game.players.actions.combat.PlayerKilling;
import main.game.players.content.DwarfMultiCannon;
import main.game.players.content.InfernoAdze;
import main.game.players.content.Pouch;
import main.game.players.content.QuickCurses;
import main.game.players.content.QuickPrayers;
import main.game.players.content.SkillInterfaces;
import main.game.players.content.YellChat;
import main.game.players.content.clanchat.load.ClanProcessor;
import main.game.players.content.grandexchange.GrandExchange;
import main.game.players.content.minigames.DuelArena;
import main.game.players.content.minigames.impl.FightPits;
import main.game.players.content.minigames.impl.PestControl;
import main.game.players.content.minigames.impl.barrows.Barrows;
import main.game.players.content.minigames.impl.barrows.BarrowsConstants;
import main.game.players.content.skills.construction.BuildObject;
import main.game.players.content.skills.construction.ConstructionData;
import main.game.players.content.skills.construction.EnterHouse;
import main.game.players.content.skills.construction.FaceObject;
import main.game.players.content.skills.construction.KickPlayers;
import main.game.players.content.skills.construction.ObjectDeleting;
import main.game.players.content.skills.construction.ObjectLoading;
import main.game.players.content.skills.construction.Process;
import main.game.players.content.skills.cooking.Cooking;
import main.game.players.content.skills.dungeoneering.Party;
import main.game.players.content.skills.farming.Allotments;
import main.game.players.content.skills.farming.Bushes;
import main.game.players.content.skills.farming.Compost;
import main.game.players.content.skills.farming.Flowers;
import main.game.players.content.skills.farming.FruitTree;
import main.game.players.content.skills.farming.Herbs;
import main.game.players.content.skills.farming.Hops;
import main.game.players.content.skills.farming.Seedling;
import main.game.players.content.skills.farming.SpecialPlantOne;
import main.game.players.content.skills.farming.SpecialPlantTwo;
import main.game.players.content.skills.farming.ToolLeprechaun;
import main.game.players.content.skills.farming.WoodTrees;
import main.game.players.content.skills.prayer.Prayer;
import main.game.players.content.skills.runecrafting.Tiaras;
import main.game.players.content.skills.slayer.Slayer;
import main.game.players.content.skills.smithing.Smithing;
import main.game.players.content.skills.smithing.SmithingInterface;
import main.game.players.content.skills.summoning.Summoning;
import main.game.players.content.skills.thieving.Thieving;
import main.game.players.content.skills.woodcutting.Woodcutting;
import main.game.players.events.ConstitutionRestoreTask;
import main.game.players.events.SkillRestoreTask;
import main.game.players.events.SpecialRestoreTask;
import main.game.shops.ShopAssistant;
import main.handlers.Following;
import main.handlers.ProcessHandler;
import main.handlers.Skill;
import main.net.HostList;
import main.net.Packet;
import main.net.StaticPacketBuilder;
import main.util.ISAACRandomGen;
import main.util.Misc;
import main.util.Stream;

public class Player {

	public int lastGame;
	public PlayerVariables variables;

	/**
	 * Some Variables used for Bone Burying
	 * 
	 * ^yeah fucking right, this class contains variables for fucking
	 * EVERYTHING, just tossed in here every which way
	 */

	public Player getInstance() {
		return this;
	}

	public int[] chaoticDegrade = new int[Chaotic.brokenIds.length];
	public int[][] boundItems = new int[6][2];
	public boolean bindX = false;
	public int boundItemId;

	public int altarXCoord, altarYCoord, prayerItemID, timesBuried;
	public boolean PrayX = false;

	public ArrayList<Integer> addPlayerList = new ArrayList<Integer>();
	public int addPlayerSize = 0;

	/**
	 * The players current {@link Action}.
	 */
	private Action currentAction;

	/**
	 * Adds an {@link Action} to the queue.
	 */
	public void addAction(Action action) {
		clearAction(true);
		currentAction = action;
		GameEngine.getScheduler().schedule(currentAction);
	}

	/**
	 * Clears the current {@link Action}.
	 * 
	 * @param forceRemove
	 *            Whether to always always remove the action (true) or just only
	 *            if it is unwalkable (false)
	 */
	public void clearAction(boolean forceRemove) {
		if (currentAction != null) {
			if (!currentAction.isWalkable() || forceRemove) {
				currentAction.stop();
				currentAction = null;
			}
		}
	}

	public boolean inMulti() {
		if ((absX >= 3136 && absX <= 3327 && absY >= 3519 && absY <= 3607)
				|| (absX >= 2625 && absX <= 2685 && absY >= 2550 && absY <= 2620) || // Pest
				// Control
				(absX >= 3190 && absX <= 3327 && absY >= 3648 && absY <= 3839)
				|| (absX >= 3334 && absX <= 3355 && absY >= 3208 && absY <= 3217)
				|| (absX >= 3200 && absX <= 3390 && absY >= 3840 && absY <= 3967)
				|| (absX >= 2992 && absX <= 3007 && absY >= 3912 && absY <= 3967)
				|| (absX >= 2946 && absX <= 2959 && absY >= 3816 && absY <= 3831)
				|| (absX >= 3008 && absX <= 3199 && absY >= 3856 && absY <= 3903)
				|| (absX >= 3008 && absX <= 3071 && absY >= 3600 && absY <= 3711)
				|| (absX >= 3072 && absX <= 3327 && absY >= 3608 && absY <= 3647)
				|| (absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619)
				|| (absX >= 2371 && absX <= 2422 && absY >= 5062 && absY <= 5117)
				|| (absX >= 2896 && absX <= 2927 && absY >= 3595 && absY <= 3630)
				|| (absX >= 2892 && absX <= 2932 && absY >= 4435 && absY <= 4464)
				|| (absX >= 2882 && absX <= 3000 && absY >= 4357 && absY <= 4406) // Corp
				// beast
				|| (absX >= 3147 && absX <= 3193 && absY >= 9737 && absY <= 9778)
				|| (absX >= 3100 && absX <= 3300 && absY >= 5300 && absY <= 5600)
				|| (absX >= 2256 && absX <= 2287 && absY >= 4680 && absY <= 4711)
				|| Boundaries.checkBoundaries(Area.GOD_WARS, getX(), getY())) {
			return true;
		}
		return false;
	}

	public boolean inWild() {
		if (absX > 2941 && absX < 3392 && absY > 3524 && absY < 3967
				|| absX > 2941 && absX < 3392 && absY > 9918 && absY < 10366) {
			return true;
		}
		return false;
	}

	public boolean inMageBank() {
		if ((absX > 3086 && absX < 3100 && absY > 3960 && absY < 3953)) {
			return true;
		}
		return false;
	}

	public boolean inDuelArena() {
		if ((absX > 3322 && absX < 3394 && absY > 3195 && absY < 3291)
				|| (absX > 3311 && absX < 3323 && absY > 3223 && absY < 3248)) {
			return true;
		}
		return false;
	}

	/**
	 * Quest bytes, place all of them here.
	 */
	public int questPoints;
	public byte tutorial;
	public byte cookAssistant;
	public byte WGS;
	public long agilityDelay;
	public int lastAbsX, lastAbsY, faceX, faceY;
	public boolean playerIsWoodcutting, CANNOT_BE_ATTACKED = false;
	public int[][] playerSkillProp = new int[20][15];
	public boolean[] playerSkilling = new boolean[25];
	public int[] woodcuttingProp = new int[10];
	public int woodcuttingTree;

	public Party party;
	public Player partner;
	public int currentFloor, prestigeFloor;

	public boolean loopingBPane = false;
	public int logID, currentArrow, stringu;
	public boolean isStringing, isArrowing, isOnInterface;

	public final int fletchAmount(final int button) {
		switch (button) {
		case 34185:
		case 34189:
		case 34170:
		case 34174:
		case 34193:
		case 34217:
		case 34205:
		case 34209:
		case 34213:
			return 1;
		case 34192:
		case 34184:
		case 34188:
		case 34169:
		case 34216:
		case 34173:
		case 34204:
		case 34208:
		case 34212:
			return 5;
		case 34191:
		case 34183:
		case 34187:
		case 34168:
		case 34215:
		case 34172:
		case 34203:
		case 34207:
		case 34211:
			return 10;
		case 34190:
		case 34182:
		case 34186:
		case 34214:
		case 34167:
		case 34171:
		case 34202:
		case 34206:
		case 34210:
			return 27;
		}
		return 0;
	}

	private Task skillRestoreTask = new SkillRestoreTask(this);
	private Task specialRestoreTask = new SpecialRestoreTask(this);
	private Task constitutionRestoreTask = new ConstitutionRestoreTask(this);

	private void initializePlayerTasks() {
		GameEngine.getScheduler().schedule(skillRestoreTask);
		GameEngine.getScheduler().schedule(specialRestoreTask);
		GameEngine.getScheduler().schedule(constitutionRestoreTask);
	}

	private void killPlayerTasks() {
		skillRestoreTask.stop();
		specialRestoreTask.stop();
		constitutionRestoreTask.stop();
	}

	public byte buffer[] = null;
	public Stream inStream = null, outStream = null;
	private IoSession session;
	private ItemAssistant itemAssistant = new ItemAssistant(this);
	private ShopAssistant shopAssistant = new ShopAssistant(this);
	private TradeHandler tradeAndDuel = new TradeHandler(this);
	private PlayerAssistant playerAssistant = new PlayerAssistant(this);
	private CombatAssistant combatAssistant = new CombatAssistant(this);
	private Curses curses = new Curses(this);
	private GrandExchange GE = new GrandExchange(this);
	private Summoning summoning = new Summoning(this);
	private ActionHandler actionHandler = new ActionHandler(this);
	private YellChat yellChat = new YellChat();
	private PlayerKilling playerKilling = new PlayerKilling(this);
	private DialogueHandler dialogueHandler = new DialogueHandler(this);
	private Queue<Packet> queuedPackets = new LinkedList<Packet>();
	private Potions potions = new Potions(this);
	private DwarfMultiCannon cannon = new DwarfMultiCannon(this);
	public ConstructionData CD = new ConstructionData();
	public KickPlayers KP = new KickPlayers();
	public FaceObject FO = new FaceObject();
	public BuildObject BO = new BuildObject();
	public Process process = new Process();
	public ObjectDeleting OD = new ObjectDeleting();
	public EnterHouse EH = new EnterHouse();
	public ObjectLoading OL = new ObjectLoading();
	private Food food = new Food(this);
	private SummoningAttacking sa = new SummoningAttacking(this);
	private Pouch mp = new Pouch(this);
	private Slayer slayer = new Slayer();
	private Smithing smith = new Smithing(this);
	private Prayer prayer = new Prayer();
	private Woodcutting woodcutting = new Woodcutting();
	private InfernoAdze adze = new InfernoAdze();
	private SmithingInterface smithInt = new SmithingInterface(this);
	private Thieving thieving = new Thieving(this);
	public DuelArena Dueling = new DuelArena();
	private SkillInterfaces skillInterfaces = new SkillInterfaces(this);
	private ProcessHandler processHandler = new ProcessHandler();
	private ClanProcessor cc = new ClanProcessor();
	
	private Logging logging = new Logging(this);
	
	public Logging getLogging() {
		return logging;
	}

	
	public int barbObstacle = 0;
	public boolean doingAgility, agilityEmote = false, logBalance, obstacleNetUp, treeBranchUp, balanceRope,
			treeBranchDown, obstacleNetOver;

	public void walk(int EndX, int EndY, int Emote) {
		walkToEmote(Emote);
		getPA().walkTo2(EndX, EndY);
	}

	public void walkToEmote(int id) {
		isRunning2 = false;
		playerWalkIndex = id;
		getPA().requestUpdates(); // this was needed to make the agility work
	}

	public void stopEmote() {
		playerWalkIndex = 0x333;
		agilityEmote = false;
		isRunning2 = true;
		getPA().requestUpdates(); // this was needed to make the agility work
	}

	public int herbAmount, doingHerb, newHerb, newXp;
	public boolean secondHerb = false, herbloreI = false;

	public String savedClan = null, bankText;
	public long lootSharePotential;
	public long lastLootDate;
	public long clanDelay;
	public boolean hasSavedClan = false, goodLootDistance, run1 = false, run2 = false, doingWoodcutting = false,
			recievedStarter2 = false, doneSteelTitanDelay = false, usingSummoningSpecial = false, isSearching,
			tempBoolean, ableToYell = true, busy = false, playerisSmelting = false;
	public int lowMemoryVersion = 0;
	public int timeOutCounter = 0;
	public int returnCode = 2;
	public int damageType = 0;
	public int geyserTitanDelay = 0;
	public int geyserTitanTarget = 0;
	public int steelTitanDelay = 0;
	public int steelTitanTarget = 0;
	public int summoningMonsterId = -1;
	public int interfaceIdOpen = 0;
	public int event = 0;
	public int makeTimes = 0;
	private Future<?> currentTask;
	public GameItem[] bankArray = new GameItem[Constants.BANK_SIZE];

	public Player opponent, copyOfOpponent;
	public int waveAmount = 0;
	public boolean killedPlayer = false;
	public boolean killedDuelOpponent = false;
	public CopyOnWriteArrayList<GameItem> otherStakedItems = new CopyOnWriteArrayList<GameItem>();
	public CopyOnWriteArrayList<GameItem> stakedItems = new CopyOnWriteArrayList<GameItem>();
	public boolean acceptedFirst = false;
	public boolean acceptedSecond = false;
	public int tzhaarToKill = 0, tzhaarKilled = 0, tzKekSpawn = 0, tzKekTimer = 0, caveWave, tzhaarNpcs;
	public int totalxp = 0;
	public int amountDonated;

	/**
	 * Used for tracking a player's name in yell.
	 * 
	 * - Branon McClellan (KeepBotting)
	 */
	public String impersonation = "";

	/**
	 * Used for tracking a player's display name.
	 *
	 * - Branon McClellan (KeepBotting)
	 */
	public String displayName = "";

	public void sendConfig(int x, int y, int z, int i) {
		getOutStream().createFrameVarSize(166);
		getOutStream().writeByte(x);// readUnsignedByte
		getOutStream().writeByte(y);// readUnsignedByte
		getOutStream().writeByte(z);// readUnsignedByte
		getOutStream().writeByte(i);// readUnsignedByte
		getOutStream().writeByte(-1);// readUnsignedByte
		getOutStream().endFrameVarSize();
	}

	public Player(IoSession s, int _playerId) {
		this(_playerId);
		this.session = s;
		outStream = new Stream(new byte[Constants.BUFFER_SIZE]);
		outStream.currentOffset = 0;
		inStream = new Stream(new byte[Constants.BUFFER_SIZE]);
		inStream.currentOffset = 0;
		buffer = new byte[Constants.BUFFER_SIZE];
	}

	public void flushOutStream() {
		if (disconnected || outStream.currentOffset == 0) {
			return;
		}
		StaticPacketBuilder out = new StaticPacketBuilder().setBare(true);
		byte[] temp = new byte[outStream.currentOffset];
		System.arraycopy(outStream.buffer, 0, temp, 0, temp.length);
		out.addBytes(temp);
		session.write(out.toPacket());
		outStream.currentOffset = 0;
	}

	public void addHp(int hp) {
		if (lifePoints < maxLifePoints) {
			lifePoints += hp;
		} else {
			maxhp = 1;
		}
	}
	
	public void setMaxLP(int lp) {
		maxLifePoints = lp;
	}
	
	public void setLP(int lp) {
		lifePoints = lp;
	}
	
	public int getLP() {
		return lifePoints;
	}

	public int getDonarPointbonus(int point) {
		int bonus = 1;
		switch (isDonator) {
		case 1:
			bonus = (point < 11) ? 2 : (point / 4);
			break;
		case 2:
			bonus = (point < 11) ? 4 : (point / 3);
			break;
		case 3:
			bonus = (point < 3) ? 2 : point / 2;
			break;
		}
		return bonus;
	}

	public void jadSpawn() {
		c.sendMessage("Temporary unvailable.");
		/*
		 * getDH().sendDialogues(33, 2618);
		 * CycleEventHandler.getInstance().addEvent(this, new CycleEvent() {
		 * 
		 * @Override public void execute(CycleEventContainer container) {
		 * Server.fightCaves .spawnNextWave((Player)
		 * PlayerHandler.players[playerId]); container.stop(); }
		 * 
		 * @Override public void stop() { } }, 6000);
		 */
	}

	public void declinePlayerTrades() {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c2 = PlayerHandler.players[j];
				if (c2.inTrade) {

					c2.getTradeHandler().declineTrade(false);
				}
			}
		}
	}

	public void sendClan(String name, String message, String clan, int rights) {
		outStream.createFrameVarSizeWord(217);
		outStream.writeString(name);
		outStream.writeString(message);
		outStream.writeString(clan);
		outStream.writeWord(rights);
		outStream.endFrameVarSize();
	}

	public static String capitalize(String s) {

		for (int i = 0; i < s.length(); i++) {

			if (i == 0) {
				s = String.format("%s%s", Character.toUpperCase(s.charAt(0)), s.substring(1));
			}
			if (!Character.isLetterOrDigit(s.charAt(i))) {
				if (i + 1 < s.length()) {
					s = String.format("%s%s%s", s.subSequence(0, i + 1), Character.toUpperCase(s.charAt(i + 1)),

							s.substring(i + 2));
				}
			}

		}

		return s;

	}

	public boolean isJumping = false, beenWarned = false;

	public void overWildernessDitch(final int x, int y) {
		if (isJumping) {
			return;
		}
		if (y == 3523) {
			isJumping = true;
			turnPlayerTo(x, 3519);
			startAnimation(6132);
			GameEngine.getScheduler().schedule(new Task(true) {

				int timer = 0;

				@Override
				public void execute() {
					if (timer == 1) {
						getPA().movePlayer(x, 3522, 0);
					}
					if (timer == 2) {
						getPA().movePlayer(x, 3521, 0);
					}
					if (timer == 3) {
						getPA().movePlayer(x, 3520, 0);
						turnPlayerTo(x, 3522);
					}
					if (timer == 4) {
						isJumping = false;
						this.stop();
					}
					timer++;
				}
			});
		}
		if (y == 3520) {
			if (!beenWarned) {
				getPA().showInterface(1908);
				beenWarned = true;
				return;
			}
			isJumping = true;
			turnPlayerTo(x, 3524);
			startAnimation(6132);
			GameEngine.getScheduler().schedule(new Task(true) {

				int timer = 0;

				@Override
				public void execute() {
					if (timer == 1) {
						getPA().movePlayer(x, 3521, 0);
					}
					if (timer == 2) {
						getPA().movePlayer(x, 3522, 0);
					}
					if (timer == 3) {
						getPA().movePlayer(x, 3523, 0);
						turnPlayerTo(x, 3522);
					}
					if (timer == 4) {
						isJumping = false;
						this.stop();
					}
					timer++;
				}
			});
		}
	}

	public boolean Run1 = false;
	public boolean Run2 = false;

	public void setAnimation(int i) {
		Run1 = isRunning;
		isRunning = false;
		Run2 = isRunning2;
		isRunning2 = false;
		playerWalkIndex = i;
		getPA().requestUpdates();
	}

	public void resetIndexes() {
		isRunning = Run1;
		isRunning2 = Run2;
		getCombat().getPlayerAnimIndex(getItems().getItemName(playerEquipment[playerWeapon]).toLowerCase());
		getPA().requestUpdates();
	}

	public void destruct() {
		System.out.println("[DEREGISTERED]: " + this.getDisplayName());
		getLogging().logSession(false);
		
		/**
		 * Mark 'em as disconnected.
		 */
		disconnected = true;

		/**
		 * Remove the player from Fight Pits.
		 */
		if (FightPits.getState(this) != null) {
			FightPits.removePlayer(this, true);
		}

		/**
		 * Remove the player from their Dungeoneering party.
		 */
		if (party != null) {
			party.leave(this);
		}

		/**
		 * If this block runs, it means the player was in combat when they
		 * logged off -- via socket closure (commonly known as an x-log).
		 * 
		 */
		if (underAttackBy > 0 || underAttackBy2 > 0) {
			saveCharacter = true;
			PlayerSave.saveGame(this);
			autoGive = true;
			return;
		}

		/**
		 * Leave their clan chat, if they're in one.
		 */
		if (clanId >= 0) {
			GameEngine.clanChat.leaveClan(playerId, clanId, true);
		}

		/**
		 * Pick up their cannon, if they've placed one.
		 */
		getCannon().pickUpCannon(cannonBaseX, cannonBaseY, cannonBaseH);

		/**
		 * Get them out of the Pest Control boat, if they're in there.
		 */
		if (PestControl.isInPcBoat(this)) {
			PestControl.removePlayerGame(this);
			getPA().movePlayer(2440, 3089, 0);
		}

		/**
		 * Kill ongoing player tasks.
		 */
		killPlayerTasks();

		if (playerRights == 3) {
			for (int i = 0; i < GameEngine.developer.length; i++) {
				if (GameEngine.developer[i] == this) {
					GameEngine.developer[i] = null;
					break;
				}
			}
		}

		/**
		 * Get their summoned creature out of the way, if they have one.
		 */
		if (getSummoning().summonedFamiliar != null && summoned != null) {
			summoned.npcTeleport(0, 0, 0);
		}

		/**
		 * Decline duels or trades, make sure we mark the character for saving.
		 */
		if (disconnected == true) {
			Dueling.declineDuel(this, true, true);
			getTradeHandler().declineTrade(false);
			saveCharacter = true;
		}

		/**
		 * May as well save again, just to be sure.
		 */
		PlayerSave.saveGame(this);

		/**
		 * ???
		 */
		if (session == null) {
			return;
		}

		/**
		 * Stop cycled events for this player.
		 */
		CycleEventHandler.getSingleton().stopEvents(this);
		
		/**
		 * No longer impersonated.
		 */
		impersonation = "";

		HostList.getHostList().remove(session);
		session.close();
		session = null;
		inStream = null;
		outStream = null;
		isActive = false;
		buffer = null;
		playerListSize = 0;
		for (int i = 0; i < maxPlayerListSize; i++) {
			playerList[i] = null;
		}
		/*
		 * if (inCwGame()) { CastleWars.dropFlag(this); CastleWars.offerSara =
		 * saraTeam() ? true : CastleWars.offerSara; CastleWars.offerZammy =
		 * zammyTeam() ? true : CastleWars.offerZammy; }
		 */
		setPos(-1, -1);
		mapRegionX = mapRegionY = -1;
		currentX = currentY = 0;
		resetWalkingQueue();
	}

	public boolean inArea(Area area) {
		return (Boundaries.checkBoundaries(area, absX, absY));
	}

	public static String capitalized(String s) {
		if (s.length() > 1) {
			s.toLowerCase();
			char c = s.charAt(0);
			c = Character.toUpperCase(c);
			return new String(c + s.substring(1, s.length()));
		} else {
			return s.toUpperCase();
		}
	}

	public void dungemote() {
		GameEngine.getScheduler().schedule(new Task(1) {

			int dungtime = 16;

			@Override
			public void execute() {
				if (dungtime == 16) {
					gfx0(2442);
					startAnimation(13190);
				}
				if (dungtime == 15) {
					npcId2 = 11228;
					isNpc = true;
					updateRequired = true;
					appearanceUpdateRequired = true;
					startAnimation(13192);
				}
				if (dungtime == 10) {
					npcId2 = 11227;
					isNpc = true;
					updateRequired = true;
					appearanceUpdateRequired = true;
					startAnimation(13193);
				}
				if (dungtime == 6) {
					gfx0(2442);
				}
				if (dungtime == 5) {
					npcId2 = 11229;
					updateRequired = true;
					appearanceUpdateRequired = true;
					startAnimation(13194);
				}
				if (dungtime == 0) {
					npcId2 = -1;
					updateRequired = true;
					appearanceUpdateRequired = true;
				}
				if (dungtime <= 0) {
					this.stop();
					return;
				}
				if (dungtime >= 0) {
					dungtime--;
				}
			}
		});
	}

	public void addToHp(int toAdd) {
		if (lifePoints + toAdd >= maxLifePoints) {
			toAdd = maxLifePoints - maxLifePoints;
		}
		lifePoints += toAdd;
	}

	public void removeFromPrayer(int toRemove) {
		if (toRemove > playerLevel[5]) {
			toRemove = playerLevel[5];
		}
		playerLevel[5] -= toRemove;
		getPA().refreshSkill(5);
	}

	public void sendMessage(String s) {
		if (getOutStream() != null) {
			outStream.createFrameVarSize(253);
			outStream.writeString(s);
			outStream.endFrameVarSize();
		}
	}
	
	public void sendMessage(String s[]) {
		if (getOutStream() != null) {
			outStream.createFrameVarSize(253);
			
			for (int i = 0; i < s.length; i++) {
				outStream.writeString(s[i]);
			}
			
			outStream.endFrameVarSize();
		}
	}

	public void setSidebarInterface(int menuId, int form) {
		if (getOutStream() != null) {
			outStream.createFrame(71);
			outStream.writeWord(form);
			outStream.writeByteA(menuId);
		}
	}

	public void questTab() {
		getPA().sendFrame126("Incendius", 19155);
		getPA().sendFrame126("-- PVP --", 19161);
		getPA().sendFrame126("", 19162);
		getPA().sendFrame126("Kills:" + KC, 19163);
		getPA().sendFrame126("Deaths:" + DC, 663);
		getPA().sendFrame126("", 16026);
		getPA().sendFrame126("PK Points: " + pkp, 16027);
		getPA().sendFrame126("", 16028);
		getPA().sendFrame126("Vote Points: " + votingPoints, 16029);
		getPA().sendFrame126("Slayer Points: " + SlayerPoints, 16030);

		for (int i = 16031; i < 16126; i++)
			getPA().sendFrame126("", i);
	}
	
	public void initialize() {
		variables = new PlayerVariables();
		outStream.createFrame(249);
		outStream.writeByteA(1); // 1 for members, zero for free
		outStream.writeWordBigEndianA(playerId);
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (j == playerId) {
				continue;
			}
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].playerName.equalsIgnoreCase(playerName)) {
					disconnected = true;
				}
			}
		}
		for (int p = 0; p < CombatPrayer.PRAYER.length; p++) { // reset prayer
			// glows
			prayerActive[p] = false;
			getPA().sendFrame36(CombatPrayer.PRAYER_GLOW[p], 0);
		}
		sendMessage(playerPrayerBook ? ":prayer:curses" : ":prayer:prayers");
		if (BarrowsConstants.inBarrows(this))
			getPA().sendFrame99(2);
		if (splitChat == false) {
			getPA().sendFrame36(502, 0);
			getPA().sendFrame36(287, 0);
		}
		if (splitChat == true) {
			getPA().sendFrame36(502, 1);
			getPA().sendFrame36(287, 1);
		}
		getPA().setConfig(173, isRunning ? 0 : 1);
		questTab();
		isFullHelm = ItemLoader.isFullHelm(playerEquipment[playerHat]);
		isFullMask = ItemLoader.isFullMask(playerEquipment[playerHat]);
		isFullBody = ItemLoader.isFullBody(playerEquipment[playerChest]);
		getPA().sendString("" + lifePoints, 19001);
		getPA().sendFrame126("" + playerLevel[23], 4030);
		getPA().sendFrame126("" + playerLevel[5], 34555);
		getPA().sendFrame126("" + getPA().getLevelForXP(playerXP[5]), 34556);
		maxLifePoints = maxLP();
		getPA().sendString("Join chat", 18135);
		// getActionSender().sendCrashFrame();
		getPA().handleWeaponStyle();
		getPA().handleLoginText();
		accountFlagged = getPA().checkForFlags();
		getPA().sendFrame36(166, 4); // Brightness
		getPA().sendFrame36(108, 0);// resets autocast button
		getPA().sendFrame36(172, 1);
		getPA().sendFrame107(); // reset screen
		getPA().setChatOptions(0, 0, 0); // reset private messaging options
		setSidebarInterface(0, 2423); // attack style
		setSidebarInterface(1, 23900); // achievments
		setSidebarInterface(2, 3917); // skill tab
		setSidebarInterface(3, 3213);
		setSidebarInterface(4, 1644);// equiptment tab
		getPA().PrayerTab();
		setSidebarInterface(6, getPA().spellBook[playerMagicBook]);
		setSidebarInterface(7, 18128); // Clan chat
		setSidebarInterface(8, 5065); // Friends list
		setSidebarInterface(9, 5715); // Ignore list
		setSidebarInterface(10, 2449); // Log out
		setSidebarInterface(11, 904); // wrench tab
		setSidebarInterface(12, 147); // emotes tab
		setSidebarInterface(13, -1); // music tab 6299 for lowdetail. 962 for
		// highdetail
		setSidebarInterface(14, 638); // Quest Tab
		// setSidebarInterface(15, 17011); // Summoning tab
		setSidebarInterface(16, -1); // Notes Tab
		correctCoordinates();
		getPA().sendSkillXP(0, 0); // Sends the current XP of the counter
		getPA().resetAutocast();
		CombatPrayer.resetPrayers(this);
		if (specAmount > 10) {
			specAmount = 10;
		}
		sendMessage("Welcome to " + Constants.SERVER_NAME + ", " + Constants.SERVER_VERSION_VERBOSE + " "
				+ Constants.SERVER_VERSION_MAJOR + "." + Constants.SERVER_VERSION_MINOR);
		sendMessage(Constants.SERVER_MOTD);
		getPA().showOption(4, 0, "Follow", 4);
		getPA().showOption(5, 0, "Trade With", 3);
		getItems().resetItems(3214);
		getItems().sendWeapon(playerEquipment[playerWeapon], getItems().getItemName(playerEquipment[playerWeapon]));
		getItems().resetBonus();
		getItems().getBonus();
		getItems().writeBonus();
		getItems().setEquipment(playerEquipment[playerHat], 1, playerHat);
		getItems().setEquipment(playerEquipment[playerCape], 1, playerCape);
		getItems().setEquipment(playerEquipment[playerAmulet], 1, playerAmulet);
		getItems().setEquipment(playerEquipment[playerArrows], playerEquipmentN[playerArrows], playerArrows);
		getItems().setEquipment(playerEquipment[playerChest], 1, playerChest);
		getItems().setEquipment(playerEquipment[playerShield], 1, playerShield);
		getItems().setEquipment(playerEquipment[playerLegs], 1, playerLegs);
		getItems().setEquipment(playerEquipment[playerHands], 1, playerHands);
		getItems().setEquipment(playerEquipment[playerFeet], 1, playerFeet);
		getItems().setEquipment(playerEquipment[playerRing], 1, playerRing);
		getItems().setEquipment(playerEquipment[playerWeapon], playerEquipmentN[playerWeapon], playerWeapon);
		getCombat().getPlayerAnimIndex(getItems().getItemName(playerEquipment[playerWeapon]).toLowerCase());
		getPA().logIntoPM();
		getPA().updateEnergy();
		Tiaras.handleTiara(this, getEquipment().getID(Constants.HAT));
		getItems().addSpecialBar(playerEquipment[playerWeapon]);
		saveTimer = 100;
		saveCharacter = true;
		
		Misc.println("[REGISTERED]: " + getDisplayName() + "");
		getLogging().logSession(true);

		/**
		 * If they don't have a display name, ensure their playerName is shown.
		 */
		if (displayName.equalsIgnoreCase("") || displayName == null) {
			displayName = playerName;
		}

		if (addStarter) {
			getPA().addStarter();
			lifePoints = 100;
		}
		initializePlayerTasks();
		update();
		// GE().sendUpdate(playerName);
		getPA().clearClanChat();
		if (savedClan != null)
			GameEngine.clanChat.handleClanChatJoin(this, savedClan);
		if (getSummoning().summonedFamiliar != null) {
			c.getPA().sendString(c.familiarName, 17017);
			c.setSidebarInterface(15, 17011);
			c.getPA().sendString(Integer.toString(c.specRestoreTimer), 17021);
			c.getPA().sendFrame75(getSummoning().summonedFamiliar.npcId, 17027);
		}
		if (playerRights == 3) {
			for (int i = 0; i < GameEngine.developer.length; i++) {
				if (GameEngine.developer[i] == null) {
					GameEngine.developer[i] = this;
					break;
				}
			}
		}
		if (Boundaries.checkBoundaries(Area.ARENAS, getX(), getY()))
			c.getPA().movePlayer(DuelArena.LOSER_X_COORD, DuelArena.LOSER_Y_COORD, 0);
		Following.resetFollow(this);
		getPA().sendFrame36(172, autoRet);
		getPA().sendFrame36(173, isRunning2 ? 1 : 0);
		if (castVengeance == 1) {
			vengOn = true;
		}
		for (int i = 0; i < 25; i++) {
			getPA().setSkillLevel(i, playerLevel[i], playerXP[i]);
			getPA().refreshSkill(i);
		}
		getPA().registerEvents();
	}

	public void update() {
		handler.updatePlayer(this, outStream);
		handler.updateNPC(this, outStream);
		flushOutStream();
	}

	public void logout() {
		if (party != null && !sentWarning) {
			sendMessage("You will leave your party & floor if you logout.");
			sentWarning = true;
			return;
		}
		if (System.currentTimeMillis() - logoutDelay > 10000) {
			if (party != null)
				party.leave(this);
			if (Boundaries.checkBoundaries(Area.PEST_CONTROL, getX(), getY())) {
				PestControl.removePlayerGame(this);
				getPA().movePlayer(2440, 3089, 0);
			}
			if (clanId >= 0)
				GameEngine.clanChat.leaveClan(playerId, clanId, true);
			if (getSummoning().summonedFamiliar != null && summoned != null) {
				summoned.npcTeleport(0, 0, 0);
			}
			outStream.createFrame(109);
			properLogout = true;
			CycleEventHandler.getSingleton().stopEvents(this);
		} else {
			sendMessage("You must wait a few seconds after combat to log out.");
		}
	}

	public int packetSize = 0, packetType = -1;
	public int diceTimer = 0;
	public boolean sentWarning = false;

	public void process() {
		ProcessHandler.executeProcess(this);
	}

	public void updateWalkEntities() {
		if (c.hasOverloadBoost && c.inWild()) {
			c.getPotions().resetOverload();
		}
		c.getPA().resetSkills();
		getPA().sendString("" + lifePoints, 19001);
		if (!inWild()) {
			getPA().sendString("Combat Level: " + calculateCombatLevel(), 19000);
		} else if (getLevelForXP(playerXP[23]) != 1) {
			int s = calculateCombatLevel() - calculateCombatLevelWOSumm();
			getPA().sendString("Combat Level: " + calculateCombatLevelWOSumm() + " + " + s, 19000);
		} else {
			getPA().sendString("Combat Level: " + calculateCombatLevelWOSumm() + "", 19000);
		}
		if (inWild()) {
			int modY = absY > 6400 ? absY - 6400 : absY;
			wildLevel = (((modY - 3520) / 8) + 1);
			getPA().walkableInterface(197);
			if (Constants.SINGLE_AND_MULTI_ZONES) {
				if (inMulti()) {
					getPA().sendString("@yel@Level: " + wildLevel, 199);
				} else {
					getPA().sendString("@yel@Level: " + wildLevel, 199);
				}
			} else {
				getPA().multiWay(-1);
				getPA().sendString("@yel@Level: " + wildLevel, 199);
			}
			getPA().showOption(3, 0, "Attack", 1);
		} else if (inDuelArena()) {
			getPA().walkableInterface(201);
			if (DuelArena.isDueling(this)) {
				getPA().showOption(3, 0, "Attack", 1);
			} else {
				getPA().showOption(3, 0, "Challenge", 1);
			}
		} else if (Boundaries.checkBoundaries(Area.GOD_WARS, getX(), getY())) {
			c.getPA().sendString("" + c.gwKills[1], 14506);
			c.getPA().sendString("" + c.gwKills[3], 14507);
			c.getPA().sendString("" + c.gwKills[2], 14508);
			c.getPA().sendString("" + c.gwKills[0], 14509);
			c.getPA().walkableInterface(14500);
		} else if (BarrowsConstants.inBarrows(this)) {
			getPA().sendFrame126("Kill Count: " + Barrows.getBarrowKillCount(brotherKilled), 4536);
			getPA().walkableInterface(4535);
		} else if (inCwGame || inPits) {
			getPA().showOption(3, 0, "Attack", 1);
		} else if (getPA().inPitsWait()) {
			getPA().showOption(3, 0, "Null", 1);
		} else if (!inCwWait) {
			getPA().sendFrame99(0);
			getPA().walkableInterface(-1);
			getPA().showOption(3, 0, "Null", 1);
		}
		if (inPcBoat()) {
			getPA().walkableInterface(21119);
		}
		if (Boundaries.checkBoundaries(Area.PEST_CONTROL, getX(), getY())) {
			getPA().walkableInterface(21100);
		}
		if (!hasMultiSign && inMulti()) {
			hasMultiSign = true;
			getPA().multiWay(1);
		}

		if (hasMultiSign && !inMulti()) {
			hasMultiSign = false;
			getPA().multiWay(-1);
		}
		if (party != null && party.floor == null)
			c.getPA().showOption(3, 0, "Invite", 1);
		if (c.getInstance().teleBlockLength > 0 && !c.getInstance().inWild()) {
			c.getInstance().teleBlockLength = 0;
		}
	}

	public boolean inPcBoat() {
		return absX >= 2660 && absX <= 2663 && absY >= 2638 && absY <= 2643;
	}
	
	int calculateMaxLP() {
		int calculatedLP = getLevelForXP(playerXP[3]) * 10;
		
		if(equipment.hasNexArmor(Equipment.EQUIPMENT_HEAD)) {
			calculatedLP += 66;
		}
		
		if(equipment.hasNexArmor(Equipment.EQUIPMENT_CHEST)) {
			calculatedLP += 200;
		}
		
		if(equipment.hasNexArmor(Equipment.EQUIPMENT_LEGS)) {
			calculatedLP += 134;
		}
		
		if(getLP() > calculatedLP) {
			setLP(calculatedLP);
		}
		
		return calculatedLP;
	}
	
	public int maxLP() {
		setMaxLP(calculateMaxLP()); // TODO refactor this method to getMaxLP when all possibilities set.
		return maxLifePoints;
	}

	public void setCurrentTask(Future<?> task) {
		currentTask = task;
	}

	public Future<?> getCurrentTask() {
		return currentTask;
	}

	public Stream getInStream() {
		return inStream;
	}

	public int getPacketType() {
		return packetType;
	}

	public int getPacketSize() {
		return packetSize;
	}

	public Stream getOutStream() {
		return outStream;
	}

	public SummoningAttacking getSA() {
		return sa;
	}

	public ItemAssistant getItems() {
		return itemAssistant;
	}

	public PlayerAssistant getPA() {
		return playerAssistant;
	}

	public DialogueHandler getDH() {
		return dialogueHandler;
	}

	public ShopAssistant getShops() {
		return shopAssistant;
	}

	public TradeHandler getTradeHandler() {
		return tradeAndDuel;
	}

	public CombatAssistant getCombat() {
		return combatAssistant;
	}

	public Curses curses() {
		return curses;
	}

	public Summoning getSummoning() {
		return summoning;
	}

	public ActionHandler getActions() {
		return actionHandler;
	}

	public YellChat getYell() {
		return yellChat;
	}

	public PlayerKilling getKill() {
		return playerKilling;
	}

	public ClanProcessor getCC() {
		return cc;
	}

	public IoSession getSession() {
		return session;
	}

	public Potions getPotions() {
		return potions;
	}

	public Food getFood() {
		return food;
	}

	public InfernoAdze getAdze() {
		return adze;
	}

	/**
	 * Skill Constructors
	 */
	public Slayer getSlayer() {
		return slayer;
	}

	public GrandExchange GE() {
		return GE;
	}

	public Pouch MoneyPouch() {
		return mp;
	}

	public Woodcutting getWoodcutting() {
		return woodcutting;
	}

	public Cooking chef() {
		return cooking;
	}

	// public Hunter getHunter() {
	// return hunter;
	// }

	public Smithing getSmithing() {
		return smith;
	}

	public Thieving getThieving() {
		return thieving;
	}

	public SmithingInterface getSmithingInt() {
		return smithInt;
	}

	public SkillInterfaces getSkillGuide() {
		return skillInterfaces;
	}

	public Prayer getPrayer() {
		return prayer;
	}

	public ProcessHandler ProcessHandler() {
		return processHandler;
	}

	/**
	 * End of Skill Constructors
	 */

	public void updateClanChatEditInterface(boolean update) {
		int cc = 0;
		for (int j = 0; j < GameEngine.clanChat.clans.length; j++) {
			if (GameEngine.clanChat.clans[j] != null) {
				if (GameEngine.clanChat.clans[j].owner.equalsIgnoreCase(playerName)) {
					getPA().sendFrame126("", 47997);
					getPA().sendFrame126("" + GameEngine.clanChat.clans[j].Recruits, 47997);
					getPA().sendFrame126("" + GameEngine.clanChat.clans[j].Corporals, 47996);
					getPA().sendFrame126("" + GameEngine.clanChat.clans[j].Sergeants, 47995);
					getPA().sendFrame126("" + GameEngine.clanChat.clans[j].Lieutenants, 47994);
					getPA().sendFrame126("" + GameEngine.clanChat.clans[j].Captains, 47993);
					getPA().sendFrame126("" + GameEngine.clanChat.clans[j].Generals, 47992);

					cc = j;
				}
			}
		}
		getPA().sendFrame126(GameEngine.clanChat.clans[cc].name, 47814);
		getPA().sendFrame126("[UPDATE]", 47998);
		getPA().showInterface(40172);
		if (GameEngine.clanChat.clans[cc].whoCanEnterChat == 0)
			getPA().sendFrame126("Anyone", 47815);
		if (GameEngine.clanChat.clans[cc].whoCanEnterChat == 1)
			getPA().sendFrame126("Any friends", 47815);
		if (GameEngine.clanChat.clans[cc].whoCanEnterChat == 2)
			getPA().sendFrame126("Recruit+", 47815);
		if (GameEngine.clanChat.clans[cc].whoCanEnterChat == 3)
			getPA().sendFrame126("Corporal+", 47815);
		if (GameEngine.clanChat.clans[cc].whoCanEnterChat == 4)
			getPA().sendFrame126("Sergeant+", 47815);
		if (GameEngine.clanChat.clans[cc].whoCanEnterChat == 5)
			getPA().sendFrame126("Lieutenant+", 47815);
		if (GameEngine.clanChat.clans[cc].whoCanEnterChat == 6)
			getPA().sendFrame126("Captain+", 47815);
		if (GameEngine.clanChat.clans[cc].whoCanEnterChat == 7)
			getPA().sendFrame126("General+", 47815);
		if (GameEngine.clanChat.clans[cc].whoCanEnterChat == 8)
			getPA().sendFrame126("Only me", 47815);

		if (GameEngine.clanChat.clans[cc].whoCanTalkOnChat == 0)
			getPA().sendFrame126("Anyone", 47816);
		if (GameEngine.clanChat.clans[cc].whoCanTalkOnChat == 1)
			getPA().sendFrame126("Any friends", 47816);
		if (GameEngine.clanChat.clans[cc].whoCanTalkOnChat == 2)
			getPA().sendFrame126("Recruit+", 47816);
		if (GameEngine.clanChat.clans[cc].whoCanTalkOnChat == 3)
			getPA().sendFrame126("Corporal+", 47816);
		if (GameEngine.clanChat.clans[cc].whoCanTalkOnChat == 4)
			getPA().sendFrame126("Sergeant+", 47816);
		if (GameEngine.clanChat.clans[cc].whoCanTalkOnChat == 5)
			getPA().sendFrame126("Lieutenant+", 47816);
		if (GameEngine.clanChat.clans[cc].whoCanTalkOnChat == 6)
			getPA().sendFrame126("Captain+", 47816);
		if (GameEngine.clanChat.clans[cc].whoCanTalkOnChat == 7)
			getPA().sendFrame126("General+", 47816);
		if (GameEngine.clanChat.clans[cc].whoCanTalkOnChat == 8)
			getPA().sendFrame126("Only me", 47816);

		if (GameEngine.clanChat.clans[cc].whoCanKickOnChat == 2)
			getPA().sendFrame126("Recruit+", 47817);
		if (GameEngine.clanChat.clans[cc].whoCanKickOnChat == 3)
			getPA().sendFrame126("Corporal+", 47817);
		if (GameEngine.clanChat.clans[cc].whoCanKickOnChat == 4)
			getPA().sendFrame126("Sergeant+", 47817);
		if (GameEngine.clanChat.clans[cc].whoCanKickOnChat == 5)
			getPA().sendFrame126("Lieutenant+", 47817);
		if (GameEngine.clanChat.clans[cc].whoCanKickOnChat == 6)
			getPA().sendFrame126("Captain+", 47817);
		if (GameEngine.clanChat.clans[cc].whoCanKickOnChat == 7)
			getPA().sendFrame126("General+", 47817);
		if (GameEngine.clanChat.clans[cc].whoCanKickOnChat == 8)
			getPA().sendFrame126("Only me", 47817);
		if (update) {
			if (GameEngine.clanChat.clans[cc].changesMade != true)
				sendMessage("Changes will take effect on your clan chat in the next 60 seconds.");
			GameEngine.clanChat.clans[cc].changesMade = true;
		}
	}

	public void queueMessage(Packet arg1) {
		synchronized (queuedPackets) {
			queuedPackets.add(arg1);
		}
	}

	public synchronized boolean processQueuedPackets() {
		Packet p = null;
		synchronized (queuedPackets) {
			p = queuedPackets.poll();
		}
		if (p == null) {
			return false;
		}
		inStream.currentOffset = 0;
		packetType = p.getId();
		packetSize = p.getLength();
		inStream.buffer = p.getData();
		if (packetType > 0) {
			PacketHandler.processPacket(this, packetType, packetSize);
		}
		timeOutCounter = 0;
		return true;
	}

	public boolean processPacket(Packet p) {
		if (p == null) {
			return false;
		}
		inStream.currentOffset = 0;
		packetType = p.getId();
		packetSize = p.getLength();
		inStream.buffer = p.getData();
		if (packetType > 0) {
			PacketHandler.processPacket(this, packetType, packetSize);
		}
		timeOutCounter = 0;
		return true;
	}

	public void correctCoordinates() {
		if (inPcGame()) {
			getPA().movePlayer(2657, 2639, 0);
		}
		if (Boundaries.checkBoundaries(Area.FIGHT_CAVES, getX(), getY())) {
			getPA().movePlayer(absX, absY, playerId * 4);
			sendMessage("Your wave will start in 10 seconds.");
			CycleEventHandler.getInstance().addEvent(this, new CycleEvent() {

				@Override
				public void execute(CycleEventContainer container) {
					GameEngine.fightCaves.spawnNextWave(PlayerHandler.players[playerId]);
					container.stop();
				}

				@Override
				public void stop() {
				}
			}, 10);

		}

	}

	private Skill skill = new Skill(this);

	public Skill getSkill() {
		return skill;
	}

	private Inventory inventory = new Inventory(this);

	public Inventory getInventory() {
		return inventory;
	}

	public DialogueHandler getDialogue() {
		return dialogueHandler;
	}

	public void setSkillTask(int skillTask) {
		this.skillTask = skillTask;
	}

	public int getTask() {
		skillTask++;
		if (skillTask > Integer.MAX_VALUE - 2) {
			skillTask = 0;
		}
		return skillTask;
	}

	public String statedInterface = "";

	public String getStatedInterface() {
		return statedInterface;
	}

	private int tempInteger;

	public void setTempInteger(int i) {
		tempInteger = i;
	}

	public int getTempInteger() {
		return tempInteger;
	}

	private int currentSkillTask;
	private int oldSkillTask;
	public int fireX, fireY, fireHeight, fireId;

	public void setNewSkillTask() {
		currentSkillTask++;
		oldSkillTask = currentSkillTask;
		if (oldSkillTask > Integer.MAX_VALUE - 2 || currentSkillTask > Integer.MAX_VALUE - 2) {
			oldSkillTask = 0;
			currentSkillTask = 0;
		}
	}

	public void setCurrentSkillTask() {
		currentSkillTask++;
	}

	public boolean checkNewSkillTask() {
		return oldSkillTask == currentSkillTask;
	}

	public void setStatedInterface(String string) {
		this.statedInterface = string;
	}

	private Cooking cooking = new Cooking(this);

	public Cooking getCooking() {
		return cooking;
	}

	public void sendAnimation(int id) {
		startAnimation(id);
	}

	public void resetAnimation() {
		getPA().resetAnimation();
	}

	public Player getUpdateFlags() {
		return this;
	}

	public void setStopPacket(boolean b) {
	}

	public void sendAnimation(int i, int i2) {
		startAnimation(i);
	}

	public Bank bank = new Bank(this);

	public Bank getBank() {
		return bank;
	}

	private Compost compost = new Compost(this);
	private Allotments allotment = new Allotments(this);
	private Flowers flower = new Flowers(this);
	private Herbs herb = new Herbs(this);
	private Hops hops = new Hops(this);
	private Bushes bushes = new Bushes(this);
	private Seedling seedling = new Seedling(this);
	private WoodTrees trees = new WoodTrees(this);
	private FruitTree fruitTrees = new FruitTree((this));
	private SpecialPlantOne specialPlantOne = new SpecialPlantOne(this);
	private SpecialPlantTwo specialPlantTwo = new SpecialPlantTwo(this);
	private ToolLeprechaun toolLeprechaun = new ToolLeprechaun(this);

	public Compost getCompost() {
		return compost;
	}

	public Allotments getAllotment() {
		return allotment;
	}

	public Flowers getFlowers() {
		return flower;
	}

	public Herbs getHerbs() {
		return herb;
	}

	public Hops getHops() {
		return hops;
	}

	public Bushes getBushes() {
		return bushes;
	}

	public Seedling getSeedling() {
		return seedling;
	}

	public WoodTrees getTrees() {
		return trees;
	}

	public FruitTree getFruitTrees() {
		return fruitTrees;
	}

	public SpecialPlantOne getSpecialPlantOne() {
		return specialPlantOne;
	}

	public SpecialPlantTwo getSpecialPlantTwo() {
		return specialPlantTwo;
	}

	public ToolLeprechaun getFarmingTools() {
		return toolLeprechaun;
	}

	public DwarfMultiCannon getCannon() {
		return cannon;
	}

	private int bindingNeckCharge;

	public int getBindingNeckCharge() {
		return bindingNeckCharge;
	}

	public void setBindingNeckCharge(int set) {
		bindingNeckCharge = set;
	}

	public Equipment equipment = new Equipment(this);

	public Equipment getEquipment() {
		return equipment;
	}
	
	private boolean jailed = false;
	
	public boolean isJailed() {
		return jailed;
	}
	
	public void setJailed(boolean b) {
		jailed = b;
	}

	public boolean resting;
	public int runEnergy = 200;// 200 so it lasts twice as long, otherwise it
	// gets depleted too fast
	public long lastRunRecovery;

	public boolean isRunning() {
		return isNewWalkCmdIsRunning() || (isRunning2 && isMoving);
	}

	private int pouchData[] = { 0, 0, 0, 0 };

	public int getPouchData(int i) {
		return pouchData[i];
	}

	public void setPouchData(int i, int amount) {
		pouchData[i] = amount;
	}

	public long getLastPickable() {
		return System.currentTimeMillis() - spellDelay;
	}

	public String familiarName;
	public boolean walkFromFilling = false;
	public boolean fillingWater = false;
	public boolean spinningPlate;
	public boolean isFullBody = false;
	public boolean isFullHelm = false;
	public boolean isFullMask = false;
	public ArrayList<String> killedPlayers = new ArrayList<String>();
	public ArrayList<Integer> attackedPlayers = new ArrayList<Integer>();
	public int doAnim, oldItem, oldItem2, gainXp, gainXp2, levelReq, levelReq2, newItem, newItem2, objectType, chance,
	leatherType, skillSpeed, doAmount;
	public boolean settingUpCannon, hasCannon, cannonIsShooting, setUpBase, setUpStand, setUpBarrels, setUpFurnace;
	public int cannonBalls, cannonBaseX, cannonBaseY, cannonBaseH, rotation, cannonID;
	public Objects oldCannon;
	public int playerTitle = 0;
	public int FishID, cookingTimer, skillXpToAdd;
	public int startingDelay = 360;
	public int[] gwKills = new int[4];
	public int cookedFishID;
	public int burnFishID;
	public int succeslvl;
	public int DC, KC;
	public int craftLevelReq;
	public int specRestore = 0;
	public int xamount;
	private long spellDelay = 0;
	public String CookFishName;
	public int castVengeance = 0;
	public int cooksLeft = 0;
	public boolean splitChat;
	public int getPheasent;
	public boolean[] killedPheasant = new boolean[5];
	public boolean inDrillEvent;
	public boolean cantTeleport;
	public boolean canLeaveArea;
	public int correctDrill;
	public int getRefreshment;
	public int npcSlot;
	public int lastX;
	public int lastY;
	public int rockCrabKills = 0;
	public int xpRecieved = 0;
	/* Starter Interface */
	public int Picked, specRestoreTimer, isDonator, maxhp;
	public boolean isDonePicking, CursesOn, stopPlayerPacket, runOption, forceMovementUpdateRequired, staffoflight;
	public int course2, course1, x1 = -1, y1 = -1, x2 = -1, y2 = -1, speed1 = -1, speed2 = -1, direction = -1;
	/* Quick Prayers */
	public boolean quickCurseActive, quickPray, quickCurse, choseQuickPro, quickPrayersOn;
	public boolean[] quickCurses = new boolean[QuickCurses.MAX_CURSES];
	public boolean[] quickPrayers = new boolean[QuickPrayers.MAX_PRAYERS];

	public void setForceMovement(final int x, final int y, final int speed1, final int speed2, final int direction,
			final int endAnim, final int time, final int height, final boolean stopPacket) {
		final int endX = getX() + x;
		final int endY = getY() + y;
		this.x1 = currentX;
		this.y1 = currentY;
		this.x2 = currentX + x;
		this.y2 = currentY + y;
		this.speed1 = speed1;
		this.speed2 = speed2;
		this.direction = direction;
		updateRequired = true;
		forceMovementUpdateRequired = true;
		if (stopPacket) {
			stopPlayerPacket = true;
		}
		final Player c = this;
		CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				if (endAnim > 0) {
					c.startAnimation(endAnim);
				}
				container.stop();
			}

			@Override
			public void stop() {
			}
		}, time - 1);
		CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				updateRequired = true;
				forceMovementUpdateRequired = false;
				if (stopPacket) {
					stopPlayerPacket = false;
				}
				c.getPA().movePlayer(endX, endY, height);
				c.getCombat().getPlayerAnimIndex2();
				container.stop();
			}

			@Override
			public void stop() {
			}
		}, time);
	}

	private long lastFire;

	/**
	 * @return the lastFire
	 */
	public long getLastFire() {
		return lastFire;
	}

	/**
	 * @param lastFire
	 *            the lastFire to set
	 */
	public void setLastFire(long lastFire) {
		this.lastFire = lastFire;
	}

	public boolean initialized = false, disconnected = false, ruleAgreeButton = false, RebuildNPCList = false,
			isActive = false, isKicked = false, isDreaming = false, playerIsFarming = false, playerIsPraying = false,
			playerIsSmithing = false, playerIsMining = false, playerIsFletching = false, playerIsFishing = false,
			playerIsHerbloring = false, playerIsFiremaking = false, playerIsCrafting = false, playerIsThieving = false,
			playerIsCooking = false, isSkulled = false, friendUpdate = false, newPlayer = false, hasMultiSign = false,
			saveCharacter = false, mouseButton = false, chatEffects = true, acceptAid = false, nextDialogue = false,
			autocasting = false, usedSpecial = false, mageFollow = false, dbowSpec = false, craftingLeather = false,
			properLogout = false, secDbow = false, maxNextHit = false, ssSpec = false, vengOn = false,
			addStarter = false, accountFlagged = false, msbSpec = false, summonedCanTeleport = false;
	public NPC summoned, ssTargetNPC, leechTargetNPC;
	public Player ssTarget, leechTarget;
	public int ssDelay = -1, ssHeal = 0, leechEndGFX = 0, treeX = 0, treeY = 0, level1 = 0, level2 = 0, level3 = 0,
			clue = 0, casket = 0;
	public double attackLeechBonus, rangedLeechBonus, magicLeechBonus, defenceLeechBonus, strengthLeechBonus = 1.0;
	public double attackLeechDefence, rangedLeechDefence, magicLeechDefence, defenceLeechDefence,
	strengthLeechDefence = 1.0;
	public int leechDelay = 0;
	public int leechType;
	public String[] leechTypes = { "Attack", "Ranged", "Magic", "Defence", "Strength", "energy", "special attack" };
	/* Attacking styles */
	public final int ACCURATE = 0, RAPID = 1, AGGRESSIVE = 1, LONGRANGE = 2, BLOCK = 2, DEFENSIVE = 2, CONTROLLED = 3;

	public boolean combatType(int type) {
		return fightMode == type;
	}

	public int[] bobItems = new int[30];
	public boolean usingBoB, autoGive = false;

	public boolean isSkilling() {
		return isDreaming || playerIsPraying || playerIsSmithing || playerIsCooking || playerIsMining
				|| playerIsFletching || playerIsWoodcutting || playerIsFishing || playerIsHerbloring
				|| playerIsFiremaking || playerIsCrafting || playerIsThieving || playerIsFarming;
	}

	/*
	 * Pk points and other related variables
	 */
	public int pkp;
	public String lastKilled;
	/* New Skilling Variables */
	public int[] fishingProp = new int[11];
	public boolean stopPlayerSkill;
	public boolean inEvent = false;
	public boolean isMining;
	public boolean mining;
	public int rockX, rockY;
	public int specAltarTimer, saveDelay, playerKilled, pkPoints, DonatorPoints = 0, SlayerPoints = 0,
			totalPlayerDamageDealt, killedBy, lastChatId = 1, privateChat, friendSlot = 0, dialogueId, randomCoffin,
			newLocation, specEffect, specBarId, attackLevelReq, defenceLevelReq, strengthLevelReq, rangeLevelReq,
			magicLevelReq, prayerLevelReq, followId, skullTimer, votingPoints, timesVoted, nextChat = 0,
			talkingNpc = -1, dialogueAction = 0, autocastId, followDistance, followId2, barrageCount = 0,
			delayedDamage = 0, delayedDamage2 = 0, pcPoints = 0, magePoints = 0, desertTreasure = 0, lastArrowUsed = -1,
			clanId = -1, autoRet = 0, pcDamage = 0, xInterfaceId = 0, xRemoveId = 0, xRemoveSlot = 0, waveId,
			frozenBy = 0, poisonDamage = 0, teleAction = 0, magicAltar = 0, bonusAttack = 0, lastNpcAttacked = 0,
			killCount = 0, destroyItem = 0, barbsToKill = 0, barbsKilled = 0, barbWave = 0, npcId2 = 0, foodCombo = 0,
			barbDamage = 0, barbLeader = 0, barbPoints = 0, dungpoint, dungfloor, dungFloor, pDungFloor, dungn,
			dungtokens;
	public boolean indungboss = false;
	public int dungtime = 0;
	public boolean inBarbDef = false;
	public String clanName, properName = "", donorTag = "Default tag";
	public boolean orb;
	public int[] voidStatus = new int[5];
	public int[] itemKeptId = new int[4];
	public int[] pouches = new int[4];
	public final int[] POUCH_SIZE = { 3, 6, 9, 12 };
	public boolean[] invSlot = new boolean[28], equipSlot = new boolean[14];
	public long friends[] = new long[200];
	public double specAmount = 0;
	public double specAccuracy = 1;
	public double specDamage = 1;
	public double prayerPoint = 1.0;
	public int teleGrabItem, teleGrabX, teleGrabY, duelCount, underAttackBy, underAttackBy2, wildLevel, teleTimer,
	respawnTimer = 0, teleBlockLength, poisonDelay;
	public long lastPlayerMove, lastPoison, lastPoisonSip, poisonImmune, lastSpear, lastProtItem, dfsDelay, lastVeng,
	lastYell, teleGrabDelay, protMageDelay, protMeleeDelay, protRangeDelay, lastAction, lastThieve,
	lastLockPick, alchDelay, duelDelay, teleBlockDelay, godSpellDelay, singleCombatDelay, singleCombatDelay2,
	reduceStat, logoutDelay, buryDelay, foodDelay, potDelay, restoreDelay, lastButton;
	public boolean canChangeAppearance = false;
	public boolean mageAllowed;
	public byte poisonMask = 0;
	public final int[] BOWS = { 18357, 9185, 839, 845, 847, 851, 855, 859, 841, 843, 849, 853, 857, 861, 4212, 4214,
			4215, 11235, 4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223, 6724, 4734, 4934, 4935, 4936, 4937 };
	public final int[] ARROWS = { 882, 884, 886, 888, 890, 892, 4740, 11212, 9140, 9141, 4142, 9143, 9144, 9240, 9241,
			9242, 9243, 9244, 9245 };
	public final int[] NO_ARROW_DROP = { 4212, 4214, 4215, 4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223, 4734, 4934,
			4935, 4936, 4937 };
	public final int[] OTHER_RANGE_WEAPONS = { 863, 864, 865, 866, 867, 868, 869, 806, 807, 808, 809, 810, 811, 825,
			826, 827, 828, 829, 830, 800, 801, 802, 803, 804, 805, 6522 };
	public final static int[][] MAGIC_SPELLS = {
			// example {magicId, level req, animation, startGFX, projectile Id,
			// endGFX, maxhit, exp gained, rune 1, rune 1 amount, rune 2, rune 2
			// amount, rune 3, rune 3 amount, rune 4, rune 4 amount}

			// Modern Spells
			{ 1152, 1, 10546, 457, 458, 463, 2, 5, 556, 1, 558, 1, 0, 0, 0, 0 }, // wind
			// strike
			{ 1154, 5, 10542, 2701, 2703, 2708, 4, 7, 555, 1, 556, 1, 558, 1, 0, 0 }, // water
			// strike
			{ 1156, 9, 14209, 2713, 2718, 2723, 6, 9, 557, 2, 556, 1, 558, 1, 0, 0 }, // earth
			// strike
			{ 1158, 13, 2791, 2728, 2729, 2737, 8, 11, 554, 3, 556, 2, 558, 1, 0, 0 }, // fire
			// strike
			{ 1160, 17, 10546, 457, 459, 464, 9, 13, 556, 2, 562, 1, 0, 0, 0, 0 }, // wind
			// bolt
			{ 1163, 23, 10542, 2701, 2704, 2709, 10, 16, 556, 2, 555, 2, 562, 1, 0, 0 }, // water
			// bolt
			{ 1166, 29, 14209, 2714, 2719, 2724, 11, 20, 556, 2, 557, 3, 562, 1, 0, 0 }, // earth
			// bolt
			{ 1169, 35, 2791, 2728, 2730, 2738, 12, 22, 556, 3, 554, 4, 562, 1, 0, 0 }, // fire
			// bolt
			{ 1172, 41, 10546, 457, 460, 1863, 13, 25, 556, 3, 560, 1, 0, 0, 0, 0 }, // wind
			// blast
			{ 1175, 47, 10542, 2701, 2705, 2706, 14, 28, 556, 3, 555, 3, 560, 1, 0, 0 }, // water
			// blast
			{ 1177, 53, 14209, 2715, 2720, 2425, 15, 31, 556, 3, 557, 4, 560, 1, 0, 0 }, // earth
			// blast
			{ 1181, 59, 2791, 2728, 2731, 2739, 16, 35, 556, 4, 554, 5, 560, 1, 0, 0 }, // fire
			// blast
			{ 1183, 62, 10546, 457, 461, 2699, 17, 36, 556, 5, 565, 1, 0, 0, 0, 0 }, // wind
			// wave
			{ 1185, 65, 10542, 2701, 2706, 2711, 18, 37, 556, 5, 555, 7, 565, 1, 0, 0 }, // water
			// wave
			{ 1188, 70, 14209, 2716, 2721, 2726, 19, 40, 556, 5, 557, 7, 565, 1, 0, 0 }, // earth
			// wave
			{ 1189, 75, 2791, 2728, 2733, 2740, 20, 42, 556, 5, 554, 7, 565, 1, 0, 0 }, // fire
			// wave
			{ 1153, 3, 716, 102, 103, 104, 0, 13, 555, 3, 557, 2, 559, 1, 0, 0 }, // confuse
			{ 1157, 11, 716, 105, 106, 107, 0, 20, 555, 3, 557, 2, 559, 1, 0, 0 }, // weaken
			{ 1161, 19, 716, 108, 109, 110, 0, 29, 555, 2, 557, 3, 559, 1, 0, 0 }, // curse
			{ 1542, 66, 729, 167, 168, 169, 0, 76, 557, 5, 555, 5, 566, 1, 0, 0 }, // vulnerability
			{ 1543, 73, 729, 170, 171, 172, 0, 83, 557, 8, 555, 8, 566, 1, 0, 0 }, // enfeeble
			{ 1562, 80, 729, 173, 174, 107, 0, 90, 557, 12, 555, 12, 556, 1, 0, 0 }, // stun
			{ 1572, 20, 710, 177, 178, 181, 0, 30, 557, 3, 555, 3, 561, 2, 0, 0 }, // bind
			{ 1582, 50, 710, 177, 178, 180, 2, 60, 557, 4, 555, 4, 561, 3, 0, 0 }, // snare
			{ 1592, 79, 710, 177, 178, 179, 4, 90, 557, 5, 555, 5, 561, 4, 0, 0 }, // entangle
			{ 1171, 39, 724, 145, 146, 147, 15, 25, 556, 2, 557, 2, 562, 1, 0, 0 }, // crumble
			// undead
			{ 1539, 50, 708, 87, 88, 89, 25, 42, 554, 5, 560, 1, 0, 0, 0, 0 }, // iban
			// blast
			{ 12037, 50, 1576, 327, 328, 329, 19, 30, 560, 1, 558, 4, 0, 0, 0, 0 }, // magic
			// dart
			{ 1190, 60, 811, 0, 0, 76, 20, 60, 554, 2, 565, 2, 556, 4, 0, 0 }, // sara
			// strike
			{ 1191, 60, 811, 0, 0, 77, 20, 60, 554, 1, 565, 2, 556, 4, 0, 0 }, // cause
			// of
			// guthix
			{ 1192, 60, 811, 0, 0, 78, 20, 60, 554, 4, 565, 2, 556, 1, 0, 0 }, // flames
			// of
			// zammy
			{ 12445, 85, 1819, 0, 344, 345, 0, 65, 563, 1, 562, 1, 560, 1, 0, 0 }, // teleblock

			// Ancient Spells
			{ 12939, 50, 1978, 0, 384, 385, 13, 30, 560, 2, 562, 2, 554, 1, 556, 1 }, // smoke
			// rush
			{ 12987, 52, 1978, 0, 378, 379, 14, 31, 560, 2, 562, 2, 566, 1, 556, 1 }, // shadow
			// rush
			{ 12901, 56, 1978, 0, 0, 373, 15, 33, 560, 2, 562, 2, 565, 1, 0, 0 }, // blood
			// rush
			{ 12861, 58, 1978, 0, 360, 361, 16, 34, 560, 2, 562, 2, 555, 2, 0, 0 }, // ice
			// rush
			{ 12963, 62, 1979, 0, 0, 389, 19, 36, 560, 2, 562, 4, 556, 2, 554, 2 }, // smoke
			// burst
			{ 13011, 64, 1979, 0, 0, 382, 20, 37, 560, 2, 562, 4, 556, 2, 566, 2 }, // shadow
			// burst
			{ 12919, 68, 1979, 0, 0, 376, 21, 39, 560, 2, 562, 4, 565, 2, 0, 0 }, // blood
			// burst
			{ 12881, 70, 1979, 0, 0, 363, 22, 40, 560, 2, 562, 4, 555, 4, 0, 0 }, // ice
			// burst
			{ 12951, 74, 1978, 0, 386, 387, 23, 42, 560, 2, 554, 2, 565, 2, 556, 2 }, // smoke
			// blitz
			{ 12999, 76, 1978, 0, 380, 381, 24, 43, 560, 2, 565, 2, 556, 2, 566, 2 }, // shadow
			// blitz
			{ 12911, 80, 1978, 0, 374, 375, 25, 45, 560, 2, 565, 4, 0, 0, 0, 0 }, // blood
			// blitz
			{ 12871, 82, 1978, 366, 0, 367, 26, 46, 560, 2, 565, 2, 555, 3, 0, 0 }, // ice
			// blitz
			{ 12975, 86, 1979, 0, 0, 391, 27, 48, 560, 4, 565, 2, 556, 4, 554, 4 }, // smoke
			// barrage
			{ 13023, 88, 1979, 0, 0, 383, 28, 49, 560, 4, 565, 2, 556, 4, 566, 3 }, // shadow
			// barrage
			{ 12929, 92, 1979, 0, 0, 377, 29, 51, 560, 4, 565, 4, 566, 1, 0, 0 }, // blood
			// barrage
			{ 12891, 94, 1979, 0, 0, 369, 30, 52, 560, 4, 565, 2, 555, 6, 0, 0 }, // ice
			// barrage

			{ -1, 80, 811, 301, 0, 0, 0, 0, 554, 3, 565, 3, 556, 3, 0, 0 }, // charge
			{ -1, 21, 712, 112, 0, 0, 0, 10, 554, 3, 561, 1, 0, 0, 0, 0 }, // low
			// alch
			{ -1, 55, 713, 113, 0, 0, 0, 20, 554, 5, 561, 1, 0, 0, 0, 0 }, // high
			// alch
			{ -1, 33, 728, 142, 143, 144, 0, 35, 556, 1, 563, 1, 0, 0, 0, 0 } // telegrab

	};

	public boolean isAutoButton(int button) {
		for (int j = 0; j < autocastIds.length; j += 2) {
			if (autocastIds[j] == button) {
				return true;
			}
		}
		return false;
	}

	public int magicHitBonus() {
		if (playerEquipment[playerWeapon] == 18355) {
			return 8;
		} else if (playerEquipment[playerWeapon] == 6914) {
			return 3;
		} else if (playerEquipment[playerWeapon] == 15486) {
			return 3;
		} else {
			return 5;
		}
	}

	public int totalMagicBonus() {
		if (playerEquipment[playerAmulet] == 18335) {
			return 4 + magicHitBonus();
		} else {
			return magicHitBonus();
		}
	}

	public int[] autocastIds = { 51133, 32, 51185, 33, 51091, 34, 24018, 35, 51159, 36, 51211, 37, 51111, 38, 51069, 39,
			51146, 40, 51198, 41, 51102, 42, 51058, 43, 51172, 44, 51224, 45, 51122, 46, 51080, 47, 7038, 0, 7039, 1,
			7040, 2, 7041, 3, 7042, 4, 7043, 5, 7044, 6, 7045, 7, 7046, 8, 7047, 9, 7048, 10, 7049, 11, 7050, 12, 7051,
			13, 7052, 14, 7053, 15, 47019, 27, 47020, 25, 47021, 12, 47022, 13, 47023, 14, 47024, 15 };

	// public String spellName = "Select Spell";
	public void assignAutocast(int button) {
		for (int j = 0; j < autocastIds.length; j++) {
			if (autocastIds[j] == button) {
				Player c = PlayerHandler.players[this.playerId];
				autocasting = true;
				autocastId = autocastIds[j + 1];
				c.getPA().sendFrame36(108, 1);
				c.setSidebarInterface(0, 328);
				// spellName = getSpellName(autocastId);
				// spellName = spellName;
				// c.getActionSender().sendFrame126(spellName, 354);
				c = null;
				break;
			}
		}
	}

	public String getSpellName(int id) {
		switch (id) {
		case 0:
			return "Air Strike";
		case 1:
			return "Water Strike";
		case 2:
			return "Earth Strike";
		case 3:
			return "Fire Strike";
		case 4:
			return "Air Bolt";
		case 5:
			return "Water Bolt";
		case 6:
			return "Earth Bolt";
		case 7:
			return "Fire Bolt";
		case 8:
			return "Air Blast";
		case 9:
			return "Water Blast";
		case 10:
			return "Earth Blast";
		case 11:
			return "Fire Blast";
		case 12:
			return "Air Wave";
		case 13:
			return "Water Wave";
		case 14:
			return "Earth Wave";
		case 15:
			return "Fire Wave";
		case 32:
			return "Shadow Rush";
		case 33:
			return "Smoke Rush";
		case 34:
			return "Blood Rush";
		case 35:
			return "Ice Rush";
		case 36:
			return "Shadow Burst";
		case 37:
			return "Smoke Burst";
		case 38:
			return "Blood Burst";
		case 39:
			return "Ice Burst";
		case 40:
			return "Shadow Blitz";
		case 41:
			return "Smoke Blitz";
		case 42:
			return "Blood Blitz";
		case 43:
			return "Ice Blitz";
		case 44:
			return "Shadow Barrage";
		case 45:
			return "Smoke Barrage";
		case 46:
			return "Blood Barrage";
		case 47:
			return "Ice Barrage";
		default:
			return "Select Spell";
		}
	}

	public boolean fullStatius() {
		return playerEquipment[playerChest] == 13884 && playerEquipment[playerLegs] == 13890
				&& playerEquipment[playerHat] == 13896;
	}

	public boolean fullVoidRange() {
		return playerEquipment[playerHat] == 11664 && playerEquipment[playerLegs] == 8840
				&& playerEquipment[playerChest] == 8839 && playerEquipment[playerHands] == 8842;
	}

	public boolean fullVoidMage() {
		return playerEquipment[playerHat] == 11663 && playerEquipment[playerLegs] == 8840
				&& playerEquipment[playerChest] == 8839 && playerEquipment[playerHands] == 8842;
	}

	public boolean fullVoidMelee() {
		return playerEquipment[playerHat] == 11665 && playerEquipment[playerLegs] == 8840
				&& playerEquipment[playerChest] == 8839 && playerEquipment[playerHands] == 8842;
	}

	/*
	 * Barrows
	 */
	public boolean[] brotherSpawned = new boolean[6];
	public boolean[] brotherKilled = new boolean[6];
	public int lastBrother;
	public int reduceSpellId;
	public final int[] REDUCE_SPELL_TIME = { 250000, 250000, 250000, 500000, 500000, 500000 }; // how
	// long
	// does
	// the
	// other
	// player
	// stay
	// immune
	// to
	// the
	// spell
	public long[] reduceSpellDelay = new long[6];
	public final int[] REDUCE_SPELLS = { 1153, 1157, 1161, 1542, 1543, 1562 };
	public boolean[] canUseReducingSpell = { true, true, true, true, true, true };
	public int totalTasks, taskDifficulty = -1;
	public int prayerId = -1;
	public int headIcon = -1;
	public int bountyIcon = 0;
	public long stopPrayerDelay, prayerDelay;
	public boolean usingPrayer;

	public boolean[] prayerActive = { false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false, false, false, false, false, false, false };
	/* Curse Prayers */
	public int leechEnergyDelay, soulSplitDelay, leechAttackDelay, attackMultiplier, rangedMultiplier, leechRangedDelay,
	leechDefenceDelay, defenceMultiplier, leechMagicDelay, magicMultiplier, leechStrengthDelay,
	strengthMultiplier, leechSpecialDelay;
	public final int[] CURSE_DRAIN_RATE = { 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
			500, 500, 500, 500, 500 };
	public final int[] CURSE_LEVEL_REQUIRED = { 50, 50, 52, 54, 56, 59, 62, 65, 68, 71, 74, 76, 78, 80, 82, 84, 86, 89,
			92, 95 };
	public final int[] CURSE = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19 };
	public final String[] CURSE_NAME = { "Protect Item", "Sap Warrior", "Sap Ranger", "Sap Mage", "Sap Spirit",
			"Berserker", "Deflect Summoning", "Deflect Magic", "Deflect Missiles", "Deflect Melee", "Leech Attack",
			"Leech Ranged", "Leech Magic", "Leech Defence", "Leech Strength", "Leech Energy", "Leech Special Attack",
			"Wrath", "Soul Split", "Turmoil" };
	public final int[] CURSE_GLOW = { 610, 611, 612, 613, 614, 615, 616, 617, 618, 619, 620, 621, 622, 623, 624, 625,
			626, 627, 628, 629 };
	public final int[] CURSE_HEAD_ICONS = { -1, -1, -1, -1, -1, -1, 12, 10, 11, 9, -1, -1, -1, -1, -1, -1, -1, 16, 17,
			-1 };
	public boolean[] curseActive = new boolean[20];
	/* End of curse prayers */
	public int duelTimer, duelTeleX, duelTeleY, duelSlot, duelSpaceReq, duelOption, duelingWith;
	public int headIconPk = -1, headIconHints;
	public boolean duelRequested;
	public boolean[] duelRule = new boolean[22];
	public final int[] DUEL_RULE_ID = { 1, 2, 16, 32, 64, 128, 256, 512, 1024, 4096, 8192, 16384, 32768, 65536, 131072,
			262144, 524288, 2097152, 8388608, 16777216, 67108864, 134217728 };
	public boolean doubleHit, usingSpecial, npcDroppingItems, usingRangeWeapon, usingBow, usingMagic, castingMagic;
	public int specMaxHitIncrease, freezeDelay, freezeTimer = -6, killerId, playerIndex, oldPlayerIndex, lastWeaponUsed,
			projectileStage, crystalBowArrowCount, playerMagicBook, teleGfx, teleEndGfx, teleEndAnimation, teleHeight,
			teleX, teleY, rangeItemUsed, killingNpcIndex, totalDamageDealt, oldNpcIndex, fightMode, attackTimer,
			npcIndex, npcClickIndex, npcType, castingSpellId, oldSpellId, spellId, hitDelay, hitDelay2, dBowHits,
			formerWeapon;
	public int saveTimer = 100;
	public boolean magicFailed, oldMagicFailed;
	public int bowSpecShot, clickNpcType, clickObjectType, objectId, objectX, objectY, objectXOffset, objectYOffset,
	objectDistance;
	public int pItemX, pItemY, pItemId;
	public boolean isMoving, walkingToItem, headingTowardsPlayer = false;
	public boolean isShopping, updateShop;
	public int myShopId;
	public int tradeStatus, tradeWith, playerTradeWealth;;
	public boolean forcedChatUpdateRequired, inDuel, tradeAccepted, goodTrade, inTrade, tradeRequested,
	tradeResetNeeded, tradeConfirmed, tradeConfirmed2, canOffer, acceptTrade, acceptedTrade;
	public int attackAnim, animationRequest = -1, animationWaitCycles;
	public int[] playerBonus = new int[12];
	public double[] soakingBonus = new double[3];
	public boolean playerPrayerBook;
	public boolean isRunning2 = true;
	public boolean takeAsNote;
	public int combatLevel;
	public boolean saveFile = false;
	public int playerAppearance[] = new int[13];
	public int apset;
	public int actionID;
	public int wearItemTimer, wearId, wearSlot, interfaceId;
	public int XremoveSlot, XinterfaceID, XremoveID, Xamount;
	public int specialTeleTimer = -1, specialTeleX, specialTeleY;
	public boolean usingGlory = false;
	public int[] woodcut = new int[3];
	public int wcTimer = 0;
	public int miningTimer = 0;
	public boolean fishing = false;
	public int fishTimer = 0;
	public int smeltType; // 1 = bronze, 2 = iron, 3 = steel, 4 = gold, 5 =
	// mith, 6 = addy, 7 = rune
	public int smeltAmount;
	public int smeltTimer = 0;
	public boolean smeltInterface;
	public boolean patchCleared;
	public int[] farm = new int[2];
	public boolean antiFirePot = false;
	/**
	 * Castle Wars
	 */
	public int castleWarsTeam;
	public boolean inCwGame;
	public boolean inCwWait;
	/**
	 * Fight Pits
	 */
	public boolean inPits = false;
	public int pitsStatus = 0;

	public String connectedFrom = "";
	public String identityPunishment = "";
	public String globalMessage = "";

	public int playerId = -1;
	public String playerName = null;
	public String playerName2 = null;
	public String playerPass = null;
	public int playerRights;
	public boolean expLock;
	public PlayerHandler handler = null;
	public int playerItems[] = new int[28];
	public int playerItemsN[] = new int[28];
	public int bankItems[] = new int[Constants.BANK_SIZE];
	public int bankItemsN[] = new int[Constants.BANK_SIZE];
	public boolean bankNotes = false;
	public boolean isBanking = false;
	public int playerStandIndex = 0x328;
	public int playerTurnIndex = 0x337;
	public int playerWalkIndex = 0x333;
	public int playerTurn180Index = 0x334;
	public int playerTurn90CWIndex = 0x335;
	public int playerTurn90CCWIndex = 0x336;
	public int playerRunIndex = 0x338;
	public int playerHat = 0;
	public int playerCape = 1;
	public int playerAmulet = 2;
	public int playerWeapon = 3;
	public int playerChest = 4;
	public int playerShield = 5;
	public int playerLegs = 7;
	public int playerHands = 9;
	public int playerFeet = 10;
	public int playerRing = 12;
	public int playerArrows = 13;
	public int playerAttack = 0;
	public int playerDefence = 1;
	public int playerStrength = 2;
	public int playerHitpoints = 3;
	public int playerRanged = 4;
	public int playerPrayer = 5;
	public int playerMagic = 6;
	public int playerCooking = 7;
	public int playerWoodcutting = 8;
	public int playerFletching = 9;
	public int playerFishing = 10;
	public int playerFiremaking = 11;
	public int playerCrafting = 12;
	public int playerSmithing = 13;
	public int playerMining = 14;
	public int playerHerblore = 15;
	public int playerAgility = 16;
	public int playerThieving = 17;
	public int playerSlayer = 18;
	public int playerFarming = 19;
	public int playerRunecrafting = 20;
	public int playerHunter = 21;
	public int[] playerEquipment = new int[14];
	public int[] playerEquipmentN = new int[14];
	public int[] playerLevel = new int[25];
	public int[] playerXP = new int[25];

	public int getSkillLevel(int skillId) {
		return getLevelForXP(playerXP[skillId]);
	}

	public void updateshop(int i) {
		Player p = PlayerHandler.players[playerId];
		p.getShops().resetShop(i);
	}

	public void println_debug(String str) {
		System.out.println("[player-" + playerId + "]: " + str);
	}

	public void println(String str) {
		System.out.println("[player-" + playerId + "]: " + str);
	}

	public Player(int _playerId) {
		playerId = _playerId;
		playerRights = 0;
		for (int i = 0; i < playerItems.length; i++) {
			playerItems[i] = 0;
		}
		for (int i = 0; i < playerItemsN.length; i++) {
			playerItemsN[i] = 0;
		}

		for (int i = 0; i < playerLevel.length; i++) {
			if (i == 3) {
				playerLevel[i] = 10;
			} else {
				playerLevel[i] = 1;
			}
		}

		for (int i = 0; i < playerXP.length; i++) {
			if (i == 3) {
				playerXP[i] = 1300;
			} else {
				playerXP[i] = 0;
			}
		}
		for (int i = 0; i < Constants.BANK_SIZE; i++) {
			bankItems[i] = 0;
		}

		for (int i = 0; i < Constants.BANK_SIZE; i++) {
			bankItemsN[i] = 0;
		}

		playerAppearance[0] = 0; // gender
		playerAppearance[1] = 3; // head
		playerAppearance[2] = 18;// Torso
		playerAppearance[3] = 26; // arms
		playerAppearance[4] = 33; // hands
		playerAppearance[5] = 36; // legs
		playerAppearance[6] = 42; // feet
		playerAppearance[7] = 10; // beard
		playerAppearance[8] = 3; // hair colour
		playerAppearance[9] = 1; // torso colour
		playerAppearance[10] = 2; // legs colour
		playerAppearance[11] = 0; // feet colour
		playerAppearance[12] = 0; // skin colour

		apset = 0;
		actionID = 0;

		playerEquipment[playerHat] = -1;
		playerEquipment[playerCape] = -1;
		playerEquipment[playerAmulet] = -1;
		playerEquipment[playerChest] = -1;
		playerEquipment[playerShield] = -1;
		playerEquipment[playerLegs] = -1;
		playerEquipment[playerHands] = -1;
		playerEquipment[playerFeet] = -1;
		playerEquipment[playerRing] = -1;
		playerEquipment[playerArrows] = -1;
		playerEquipment[playerWeapon] = -1;

		heightLevel = 0;

		teleportToX = Constants.START_LOCATION_X;
		teleportToY = Constants.START_LOCATION_Y;

		setPos(-1, -1);
		mapRegionX = mapRegionY = -1;
		currentX = currentY = 0;
		// playerName = Misc.capitalize(playerName);
		resetWalkingQueue();
	}

	public void setPos(int x, int y) {
		lastX = absX;
		lastY = absY;
		absX = x;
		absY = y;
	}

	public static final int maxPlayerListSize = Constants.MAX_PLAYERS;
	public Player playerList[] = new Player[maxPlayerListSize];
	public int playerListSize = 0;
	public byte playerInListBitmap[] = new byte[(Constants.MAX_PLAYERS + 7) >> 3];
	public static final int maxNPCListSize = NPCHandler.maxNPCs;
	public NPC npcList[] = new NPC[maxNPCListSize];
	public int npcListSize = 0;
	public byte npcInListBitmap[] = new byte[(NPCHandler.maxNPCs + 7) >> 3];

	public boolean withinDistance(Player otherPlr) {
		if (heightLevel != otherPlr.heightLevel) {
			return false;
		}
		int deltaX = otherPlr.absX - absX, deltaY = otherPlr.absY - absY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public boolean withinDistance(NPC npc) {
		if (heightLevel != npc.heightLevel) {
			return false;
		}
		if (npc.needRespawn == true) {
			return false;
		}
		int deltaX = npc.absX - absX, deltaY = npc.absY - absY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public int distanceToPoint(int pointX, int pointY) {
		return (int) Math.sqrt(Math.pow(absX - pointX, 2) + Math.pow(absY - pointY, 2));
	}

	public int mapRegionX, mapRegionY;
	public int absX, absY;
	public int currentX, currentY;
	public int heightLevel;
	public int playerSE = 0x328;
	public int playerSEW = 0x333;
	public int playerSER = 0x334;
	public boolean updateRequired = true;
	public final int walkingQueueSize = 50;
	public int walkingQueueX[] = new int[walkingQueueSize], walkingQueueY[] = new int[walkingQueueSize];
	public int wQueueReadPtr = 0;
	public int wQueueWritePtr = 0;
	public boolean isRunning = true;
	public int teleportToX = -1, teleportToY = -1;

	public void resetWalkingQueue() {
		wQueueReadPtr = wQueueWritePtr = 0;

		for (int i = 0; i < walkingQueueSize; i++) {
			walkingQueueX[i] = currentX;
			walkingQueueY[i] = currentY;
		}
	}

	public void addToWalkingQueue(int x, int y) {
		// if (VirtualWorld.I(heightLevel, absX, absY, x, y, 0)) {
		int next = (wQueueWritePtr + 1) % walkingQueueSize;
		if (next == wQueueWritePtr) {
			return;
		}
		walkingQueueX[wQueueWritePtr] = x;
		walkingQueueY[wQueueWritePtr] = y;
		wQueueWritePtr = next;
		// }
	}

	public boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		return ((objectX - playerX <= distance && objectX - playerX >= -distance)
				&& (objectY - playerY <= distance && objectY - playerY >= -distance));
	}

	public int getNextWalkingDirection() {
		Player p = this;
		TransformHandler.transform(p);
		if (wQueueReadPtr == wQueueWritePtr) {
			return -1;
		}
		int dir;
		do {
			dir = Misc.direction(currentX, currentY, walkingQueueX[wQueueReadPtr], walkingQueueY[wQueueReadPtr]);
			if (dir == -1) {
				wQueueReadPtr = (wQueueReadPtr + 1) % walkingQueueSize;
			} else if ((dir & 1) != 0) {
				resetWalkingQueue();
				return -1;
			}
		} while ((dir == -1) && (wQueueReadPtr != wQueueWritePtr));
		if (dir == -1) {
			return -1;
		}
		dir >>= 1;
		// TODO DONT REMOVE - ADD WHEN WE LOAD 525 MAPS
		/*
		 * if (!Region.canMove(this.absX, this.absY, this.absX +
		 * Misc.directionDeltaX[dir], this.absY + Misc.directionDeltaY[dir],
		 * this.heightLevel % 4, 1, 1)) { } else {
		 */
		currentX += Misc.directionDeltaX[dir];
		currentY += Misc.directionDeltaY[dir];
		setPos(absX += Misc.directionDeltaX[dir], absY += Misc.directionDeltaY[dir]);
		/* } */

		if (isRunning()) {
			if (runEnergy > 0) {
				runEnergy--;
				getPA().sendFrame126(runEnergy + "%", 149);
			} else {
				isRunning2 = false;
				getPA().setConfig(173, 0);
			}
		}
		if (!wildernessUpdated && inWild()) {
			updateRequired = true;
			setAppearanceUpdateRequired(true);
			wildernessUpdated = true;
		}
		if (wildernessUpdated && !inWild()) {
			updateRequired = true;
			setAppearanceUpdateRequired(true);
			wildernessUpdated = false;
		}
		if (!Boundaries.checkBoundaries(Area.ROCK_CRABS, getX(), getY()) && rockCrabKills >= 35) {
			rockCrabKills = 0;
		}
		updateWalkEntities();
		return dir;
	}

	public boolean wildernessUpdated = false;
	public boolean didTeleport = false;
	public boolean mapRegionDidChange = false;
	public int dir1 = -1, dir2 = -1;
	public boolean createItems = false;
	public int poimiX = 0, poimiY = 0;

	public void getNextPlayerMovement() {
		mapRegionDidChange = false;
		didTeleport = false;
		dir1 = dir2 = -1;

		if (teleportToX != -1 && teleportToY != -1) {
			mapRegionDidChange = true;
			if (mapRegionX != -1 && mapRegionY != -1) {
				int relX = teleportToX - mapRegionX * 8, relY = teleportToY - mapRegionY * 8;
				if (relX >= 2 * 8 && relX < 11 * 8 && relY >= 2 * 8 && relY < 11 * 8) {
					mapRegionDidChange = false;
				}
			}
			if (mapRegionDidChange) {
				mapRegionX = (teleportToX >> 3) - 6;
				mapRegionY = (teleportToY >> 3) - 6;
			}
			currentX = teleportToX - 8 * mapRegionX;
			currentY = teleportToY - 8 * mapRegionY;
			setPos(teleportToX, teleportToY);
			resetWalkingQueue();

			teleportToX = teleportToY = -1;
			didTeleport = true;
			updateVisiblePlayers();
			updateWalkEntities();
		} else {
			dir1 = getNextWalkingDirection();
			if (dir1 == -1) {
				return;
			}
			if (isRunning) {
				dir2 = getNextWalkingDirection();
			}
			int deltaX = 0, deltaY = 0;
			if (currentX < 2 * 8) {
				deltaX = 4 * 8;
				mapRegionX -= 4;
				mapRegionDidChange = true;
			} else if (currentX >= 11 * 8) {
				deltaX = -4 * 8;
				mapRegionX += 4;
				mapRegionDidChange = true;
			}
			if (currentY < 2 * 8) {
				deltaY = 4 * 8;
				mapRegionY -= 4;
				mapRegionDidChange = true;
			} else if (currentY >= 11 * 8) {
				deltaY = -4 * 8;
				mapRegionY += 4;
				mapRegionDidChange = true;
			}
			if (dir1 != -1 || dir2 != -1) {
				updateVisiblePlayers(); // they/you could come in or out of
				// their/your area
			}
			if (mapRegionDidChange/*
			 * && VirtualWorld.I(heightLevel, currentX,
			 * currentY, currentX + deltaX, currentY +
			 * deltaY, 0)
			 */) {
				currentX += deltaX;
				currentY += deltaY;
				for (int i = 0; i < walkingQueueSize; i++) {
					walkingQueueX[i] += deltaX;
					walkingQueueY[i] += deltaY;
				}
			}
			// CoordAssistant.processCoords(this);

		}
	}

	public void updateVisiblePlayers() {
		for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] == null || !PlayerHandler.players[i].isActive
					|| PlayerHandler.players[i] == this)
				continue;

			int id = PlayerHandler.players[i].playerId;

			if ((playerInListBitmap[id >> 3] & (1 << (id & 7))) != 0) {
				continue;
			}

			if (!withinDistance(PlayerHandler.players[i]) || addPlayerList.contains(id)) {
				continue;
			}

			addPlayerList.add(id);
			addPlayerSize++;

			PlayerHandler.players[i].addPlayerList.add(playerId);
			PlayerHandler.players[i].addPlayerSize++;
		}
	}

	public void updateThisPlayerMovement(Stream str) {

		if (mapRegionDidChange) {
			str.createFrame(73);
			str.writeWordA(mapRegionX + 6);
			str.writeWord(mapRegionY + 6);
		}

		if (didTeleport) {
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			str.writeBits(1, 1);
			str.writeBits(2, 3);
			str.writeBits(2, heightLevel);
			str.writeBits(1, 1);
			str.writeBits(1, (updateRequired) ? 1 : 0);
			str.writeBits(7, currentY);
			str.writeBits(7, currentX);
			return;
		}

		if (dir1 == -1) {
			// don't have to update the character position, because we're just
			// standing
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			isMoving = false;
			if (updateRequired) {
				// tell client there's an update block appended at the end
				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else {
				str.writeBits(1, 0);
			}
			if (DirectionCount < 50) {
				DirectionCount++;
			}
		} else {
			DirectionCount = 0;
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			str.writeBits(1, 1);

			if (dir2 == -1) {
				isMoving = true;
				str.writeBits(2, 1);
				str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
				if (updateRequired) {
					str.writeBits(1, 1);
				} else {
					str.writeBits(1, 0);
				}
			} else {
				isMoving = true;
				str.writeBits(2, 2);
				str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
				str.writeBits(3, Misc.xlateDirectionToClient[dir2]);
				if (updateRequired) {
					str.writeBits(1, 1);
				} else {
					str.writeBits(1, 0);
				}
			}
		}
	}

	public void updatePlayerMovement(Stream str) {
		if (dir1 == -1) {
			if (updateRequired || isChatTextUpdateRequired()) {

				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else {
				str.writeBits(1, 0);
			}
		} else if (dir2 == -1) {

			str.writeBits(1, 1);
			str.writeBits(2, 1);
			str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
			str.writeBits(1, (updateRequired || isChatTextUpdateRequired()) ? 1 : 0);
		} else {

			str.writeBits(1, 1);
			str.writeBits(2, 2);
			str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
			str.writeBits(3, Misc.xlateDirectionToClient[dir2]);
			str.writeBits(1, (updateRequired || isChatTextUpdateRequired()) ? 1 : 0);
		}
	}

	public byte cachedPropertiesBitmap[] = new byte[(Constants.MAX_PLAYERS + 7) >> 3];

	public void addNewNPC(NPC npc, Stream str, Stream updateBlock) {
		int id = npc.npcId;
		npcInListBitmap[id >> 3] |= 1 << (id & 7);
		npcList[npcListSize++] = npc;

		str.writeBits(14, id);

		int z = npc.absY - absY;
		if (z < 0) {
			z += 32;
		}
		str.writeBits(5, z);
		z = npc.absX - absX;
		if (z < 0) {
			z += 32;
		}
		str.writeBits(5, z);

		str.writeBits(1, 0);
		str.writeBits(18, npc.npcType);

		boolean savedUpdateRequired = npc.updateRequired;
		npc.updateRequired = true;
		npc.appendNPCUpdateBlock(updateBlock, this);
		npc.updateRequired = savedUpdateRequired;
		str.writeBits(1, 1);
		npc.faceUpdated = false;
	}

	public void addNewPlayer(Player plr, Stream str, Stream updateBlock) {

		if (playerListSize >= 255) {
			return;
		}
		int id = plr.playerId;
		playerInListBitmap[id >> 3] |= 1 << (id & 7);
		playerList[playerListSize++] = plr;
		str.writeBits(11, id);
		str.writeBits(1, 1);
		boolean savedFlag = plr.isAppearanceUpdateRequired();
		boolean savedUpdateRequired = plr.updateRequired;
		plr.setAppearanceUpdateRequired(true);
		plr.updateRequired = true;
		plr.appendPlayerUpdateBlock(updateBlock);
		plr.setAppearanceUpdateRequired(savedFlag);
		plr.updateRequired = savedUpdateRequired;
		str.writeBits(1, 1);
		int z = plr.absY - absY;
		if (z < 0) {
			z += 32;
		}
		str.writeBits(5, z);
		z = plr.absX - absX;
		if (z < 0) {
			z += 32;
		}
		str.writeBits(5, z);
	}

	public void updateFace(final Player p) {
		CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
			int timer = 1;

			@Override
			public void execute(CycleEventContainer container) {
				if (timer == 0) {
					faceCurrentDirection(p);
					container.stop();
				}
				timer--;

			}

			@Override
			public void stop() {
			}
		}, 1);
	}

	public static enum FaceDirection {
		NORTH, NORTH_EAST, NORTH_WEST, EAST, SOUTH, SOUTH_EAST, SOUTH_WEST, WEST;
	}

	public void getLatestFacePoint(Player p) {
		switch (getFaceDirection(p)) {
		case NORTH:
			p.faceX = 0;
			p.faceY = 1;
			break;
		case NORTH_EAST:
			p.faceX = 1;
			p.faceY = 1;
			break;
		case NORTH_WEST:
			p.faceX = -1;
			p.faceY = 1;
			break;
		case EAST:
			p.faceX = 1;
			p.faceY = 0;
			break;
		case SOUTH:
			p.faceX = 0;
			p.faceY = -1;
			break;
		case SOUTH_EAST:
			p.faceX = 1;
			p.faceY = -1;
			break;
		case SOUTH_WEST:
			p.faceX = -1;
			p.faceY = -1;
			break;
		case WEST:
			p.faceX = -1;
			p.faceY = 0;
			break;
		}
	}

	public void faceCurrentDirection(Player p) {
		p.getLatestFacePoint(p);
		p.faceX = p.faceX;
		p.faceY = p.faceY;
		p.turnPlayerTo(p.getX() + p.faceX, p.getY() + p.faceY);
	}

	public FaceDirection getFaceDirection(Player p) {
		if (p.getLastX() < p.getX()) {
			if (p.getLastY() == p.getY())
				return FaceDirection.EAST;
			else if (p.getLastY() < p.getY())
				return FaceDirection.NORTH_EAST;
			else
				return FaceDirection.SOUTH_EAST;
		} else if (p.getLastX() == p.getX()) {
			if (p.getLastY() < p.getY())
				return FaceDirection.NORTH;
			else
				return FaceDirection.SOUTH;
		} else if (p.getLastX() > p.getX()) {
			if (p.getLastY() == p.getY())
				return FaceDirection.WEST;
			else if (p.getLastY() < p.getY())
				return FaceDirection.NORTH_WEST;
			else
				return FaceDirection.SOUTH_WEST;
		}
		return FaceDirection.NORTH;
	}

	public int DirectionCount = 0;
	public boolean appearanceUpdateRequired = true;
	public boolean appearanceUpdateRequired2 = true;
	// public int npcId2 = 0;
	public boolean isNpc;
	protected int hitDiff2;
	private int hitDiff = 0;
	protected boolean hitUpdateRequired2;
	private boolean hitUpdateRequired = false;
	public boolean isDead = false;
	protected static Stream playerProps;

	static {
		playerProps = new Stream(new byte[100]);
	}

	protected void appendPlayerAppearance(Stream str) {
		maxLifePoints = getLevelForXP(playerXP[3]) * 10;

		playerProps.currentOffset = 0;
		playerProps.writeByte(playerAppearance[0]);
		playerProps.writeByte(headIcon);// not easy as im not sure what to
		// wrutei
		playerProps.writeByte(headIconPk);
		if (npcId2 <= 0) {
			if (playerEquipment[playerHat] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerHat]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerCape] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerCape]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerAmulet] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerAmulet]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerWeapon] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerWeapon]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerChest] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerChest]);
			} else {
				playerProps.writeWord(0x100 + playerAppearance[2]);
			}

			if (playerEquipment[playerShield] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerShield]);
			} else {
				playerProps.writeByte(0);
			}
			if (!isFullBody) {
				playerProps.writeWord(0x100 + playerAppearance[3]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerLegs] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerLegs]);
			} else {
				playerProps.writeWord(0x100 + playerAppearance[5]);
			}
			if (!isFullHelm && !isFullMask) {
				playerProps.writeWord(0x100 + playerAppearance[1]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerHands] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerHands]);
			} else {
				playerProps.writeWord(0x100 + playerAppearance[4]);
			}

			if (playerEquipment[playerFeet] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerFeet]);
			} else {
				playerProps.writeWord(0x100 + playerAppearance[6]);
			}
			if (playerAppearance[0] != 1 && !isFullMask) {
				playerProps.writeWord(0x100 + playerAppearance[7]);
			} else {
				playerProps.writeByte(0);
			}
		} else {
			playerProps.writeWord(-1);
			playerProps.writeWord(npcId2);
		}

		playerProps.writeByte(playerAppearance[8]);
		playerProps.writeByte(playerAppearance[9]);
		playerProps.writeByte(playerAppearance[10]);
		playerProps.writeByte(playerAppearance[11]);
		playerProps.writeByte(playerAppearance[12]);
		playerProps.writeWord(playerStandIndex); // standAnimIndex
		playerProps.writeWord(playerTurnIndex); // standTurnAnimIndex
		playerProps.writeWord(playerWalkIndex); // walkAnimIndex
		playerProps.writeWord(playerTurn180Index); // turn180AnimIndex
		playerProps.writeWord(playerTurn90CWIndex); // turn90CWAnimIndex
		playerProps.writeWord(playerTurn90CCWIndex); // turn90CCWAnimIndex
		playerProps.writeWord(playerRunIndex); // runAnimIndex
		playerProps.writeWord(lifePoints);
		playerProps.writeWord(maxLP());

		/**
		 * Handling for display names.
		 * 
		 * - Branon McClellan (KeepBotting)
		 */
		playerProps.writeQWord(Misc.playerNameToInt64(displayName == "" ? playerName : displayName));

		CombatLevel = calculateCombatLevel();
		playerProps.writeByte(CombatLevel); // combat level
		if (inWild()) {
			playerProps.writeWord(getLevelForXP(playerXP[23]));
		} else {
			playerProps.writeWord(1);
		}
		playerProps.writeWord(playerTitle);
		str.writeByteC(playerProps.currentOffset);
		str.writeBytes(playerProps.buffer, playerProps.currentOffset, 0);

	}

	public int CombatLevel = 0;

	public int calculateCombatLevel() {
		int mag = (int) ((getLevelForXP(playerXP[6])) * 1.5);
		int ran = (int) ((getLevelForXP(playerXP[4])) * 1.5);
		int attstr = (int) ((double) (getLevelForXP(playerXP[0])) + (double) (getLevelForXP(playerXP[2])));

		if (ran > attstr) {
			combatLevel = (int) (((getLevelForXP(playerXP[1])) * 0.25) + ((getLevelForXP(playerXP[3])) * 0.25)
					+ ((getLevelForXP(playerXP[5])) * 0.125) + ((getLevelForXP(playerXP[4])) * 0.4875));
			// + ((getLevelForXP(playerXP[23])) * 0.125));
		} else if (mag > attstr) {
			combatLevel = (int) (((getLevelForXP(playerXP[1])) * 0.25) + ((getLevelForXP(playerXP[3])) * 0.25)
					+ ((getLevelForXP(playerXP[5])) * 0.125) + ((getLevelForXP(playerXP[6])) * 0.4875));
			// + ((getLevelForXP(playerXP[23])) * 0.125));
		} else {
			combatLevel = (int) (((getLevelForXP(playerXP[1])) * 0.25) + ((getLevelForXP(playerXP[3])) * 0.25)
					+ ((getLevelForXP(playerXP[5])) * 0.125) + ((getLevelForXP(playerXP[0])) * 0.325)
					+ ((getLevelForXP(playerXP[2])) * 0.325));
			// + ((getLevelForXP(playerXP[23])) * 0.125));
		}
		CombatLevel = 0;
		if (ran > attstr) {
			CombatLevel = (int) (((getLevelForXP(playerXP[1])) * 0.25) + ((getLevelForXP(playerXP[3])) * 0.25)
					+ ((getLevelForXP(playerXP[5])) * 0.125) + ((getLevelForXP(playerXP[4])) * 0.4875)
					+ ((getLevelForXP(playerXP[23])) * 0.125));
		} else if (mag > attstr) {
			CombatLevel = (int) (((getLevelForXP(playerXP[1])) * 0.25) + ((getLevelForXP(playerXP[3])) * 0.25)
					+ ((getLevelForXP(playerXP[5])) * 0.125) + ((getLevelForXP(playerXP[6])) * 0.4875)
					+ ((getLevelForXP(playerXP[23])) * 0.125));
		} else {
			CombatLevel = (int) (((getLevelForXP(playerXP[1])) * 0.25) + ((getLevelForXP(playerXP[3])) * 0.25)
					+ ((getLevelForXP(playerXP[5])) * 0.125) + ((getLevelForXP(playerXP[0])) * 0.325)
					+ ((getLevelForXP(playerXP[2])) * 0.325) + ((getLevelForXP(playerXP[23])) * 0.125));
		}
		return CombatLevel;
	}

	public int calculateCombatLevelWOSumm() {
		combatLevel = 0;
		int mag = (int) ((getLevelForXP(playerXP[6])) * 1.5);
		int ran = (int) ((getLevelForXP(playerXP[4])) * 1.5);
		int attstr = (int) ((double) (getLevelForXP(playerXP[0])) + (double) (getLevelForXP(playerXP[2])));

		if (ran > attstr) {
			combatLevel = (int) (((getLevelForXP(playerXP[1])) * 0.25) + ((getLevelForXP(playerXP[3])) * 0.25)
					+ ((getLevelForXP(playerXP[5])) * 0.125) + ((getLevelForXP(playerXP[4])) * 0.4875));
		} else if (mag > attstr) {
			combatLevel = (int) (((getLevelForXP(playerXP[1])) * 0.25) + ((getLevelForXP(playerXP[3])) * 0.25)
					+ ((getLevelForXP(playerXP[5])) * 0.125) + ((getLevelForXP(playerXP[6])) * 0.4875));
		} else {
			combatLevel = (int) (((getLevelForXP(playerXP[1])) * 0.25) + ((getLevelForXP(playerXP[3])) * 0.25)
					+ ((getLevelForXP(playerXP[5])) * 0.125) + ((getLevelForXP(playerXP[0])) * 0.325)
					+ ((getLevelForXP(playerXP[2])) * 0.325));
		}
		return combatLevel;
	}

	public void setAwaitingUpdate(boolean update) {
		awaitingUpdate = update;
	}

	public boolean getAwaitingUpdate() {
		return awaitingUpdate;
	}

	private boolean awaitingUpdate = false;

	public int getLevelForXP(int exp) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp) {
				return lvl;
			}
		}
		return 99;
	}

	private boolean chatTextUpdateRequired = false;
	private byte chatText[] = new byte[4096];
	private byte chatTextSize = 0;
	private int chatTextColor = 0;
	private int chatTextEffects = 0;

	protected void appendPlayerChatText(Stream str) {

		str.writeWordBigEndian(((getChatTextColor() & 0xFF) << 8) + (getChatTextEffects() & 0xFF));
		str.writeByte(playerRights);
		str.writeByteC(getChatTextSize());
		str.writeBytes_reverse(getChatText(), getChatTextSize(), 0);

	}

	public void forcedChat(String text) {
		forcedText = text;
		forcedChatUpdateRequired = true;
		updateRequired = true;
		setAppearanceUpdateRequired(true);
	}

	public String forcedText = "null";

	public void appendForcedChat(Stream str) {

		str.writeString(forcedText);

	}

	/**
	 * Graphics
	 **/
	public int mask100var1 = 0;
	public int mask100var2 = 0;
	protected boolean mask100update = false;

	public void appendMask100Update(Stream str) {

		str.writeWordBigEndian(mask100var1);
		str.writeDWord(mask100var2);

	}

	public void gfx100(int gfx) {
		mask100var1 = gfx;
		mask100var2 = 6553600;
		mask100update = true;
		updateRequired = true;
	}

	public void gfx50(int gfx) {
		mask100var1 = gfx;
		mask100var2 = 655360;
		mask100update = true;
		updateRequired = true;
	}

	public void gfx0(int gfx) {
		mask100var1 = gfx;
		mask100var2 = 65536;
		mask100update = true;
		updateRequired = true;
	}

	public void gfx(int gfx, int height) {
		mask100var1 = gfx;
		mask100var2 = 65536 * height;
		mask100update = true;
		updateRequired = true;
	}

	public boolean wearing2h() {
		Player c = this;
		String s = getItems().getItemName(c.playerEquipment[c.playerWeapon]);
		if (s.contains("2h")) {
			return true;
		} else if (s.contains("godsword")) {
			return true;
		}
		return false;
	}

	public boolean wearingSharpWeapon() {
		Player c = this;
		String s = getItems().getItemName(c.playerEquipment[c.playerWeapon]);
		if (s.contains("2h") || s.contains("sword") || s.contains("dagger") || s.contains("light")
				|| s.contains("rapier") || s.contains("scimitar") || s.contains("halberd") || s.contains("spear")
				|| s.contains("axe") || s.contains("excalibur") || s.contains("claws") || s.contains("whip")) {
			return true;
		}
		return false;
	}

	/**
	 * Animations
	 **/
	public void startAnimation(int animId) {
		animationRequest = animId;
		animationWaitCycles = 0;
		updateRequired = true;
	}

	public void startAnimation(int animId, int time) {
		animationRequest = animId;
		animationWaitCycles = time;
		updateRequired = true;
	}

	public void appendAnimationRequest(Stream str) {

		str.writeWordBigEndian((animationRequest == -1) ? 65535 : animationRequest);
		str.writeByteC(animationWaitCycles);

	}

	/**
	 * Face Update
	 **/
	protected boolean faceUpdateRequired = false;
	public int face = -1;
	public int FocusPointX = -1, FocusPointY = -1;

	public void faceUpdate(int index) {
		face = index;
		faceUpdateRequired = true;
		updateRequired = true;
	}

	public void appendFaceUpdate(Stream str) {

		str.writeWordBigEndian(face);

	}

	public void turnPlayerTo(int pointX, int pointY) {
		FocusPointX = 2 * pointX + 1;
		FocusPointY = 2 * pointY + 1;
		updateRequired = true;
	}

	private void appendSetFocusDestination(Stream str) {

		str.writeWordBigEndianA(FocusPointX);
		str.writeWordBigEndian(FocusPointY);

	}

	/**
	 * Hit Update
	 **/
	public int soakDamage, soakDamage2 = 0;

	protected void appendHitUpdate(Stream str) {

		str.writeWordA(getHitDiff());
		str.writeByte(hitMask);
		if (lifePoints <= 0) {
			lifePoints = 0;
			isDead = true;
		}
		str.writeByte(hitIcon);
		str.writeWordA(soakDamage);
		str.writeWordA(lifePoints);
		str.writeWordA(maxLP());

	}

	protected void appendHitUpdate2(Stream str) {

		str.writeWordA(hitDiff2);
		str.writeByte(hitMask2);
		if (lifePoints <= 0) {
			lifePoints = 0;
			isDead = true;
		}
		str.writeByte(hitIcon2);
		str.writeWordA(soakDamage2);
		str.writeWordA(lifePoints);
		str.writeWordA(maxLP());

	}

	public int maxLifePoints = getLevelForXP(playerXP[3]) * 10;
	public int lifePoints;
	public int prayerPoints;
	public boolean korasiSpec;

	public void appendPlayerUpdateBlock(Stream str) {
		maxLifePoints = getLevelForXP(playerXP[3]) * 10;

		if (!updateRequired && !isChatTextUpdateRequired()) {
			return; // nothing required
		}
		int updateMask = 0;
		if (mask100update) {
			updateMask |= 0x100;
		}
		if (animationRequest != -1) {
			updateMask |= 8;
		}
		if (forcedChatUpdateRequired) {
			updateMask |= 4;
		}
		if (isChatTextUpdateRequired()) {
			updateMask |= 0x80;
		}
		if (isAppearanceUpdateRequired()) {
			updateMask |= 0x10;
		}
		if (faceUpdateRequired) {
			updateMask |= 1;
		}
		if (FocusPointX != -1) {
			updateMask |= 2;
		}
		if (isHitUpdateRequired()) {
			updateMask |= 0x20;
		}

		if (hitUpdateRequired2) {
			updateMask |= 0x200;
		}

		if (updateMask >= 0x100) {
			updateMask |= 0x40;
			str.writeByte(updateMask & 0xFF);
			str.writeByte(updateMask >> 8);
		} else {
			str.writeByte(updateMask);
		}

		// now writing the various update blocks itself - note that their order
		// crucial
		if (mask100update) {
			appendMask100Update(str);
		}
		if (animationRequest != -1) {
			appendAnimationRequest(str);
		}
		if (forcedChatUpdateRequired) {
			appendForcedChat(str);
		}
		if (isChatTextUpdateRequired()) {
			appendPlayerChatText(str);
		}
		if (faceUpdateRequired) {
			appendFaceUpdate(str);
		}
		if (isAppearanceUpdateRequired()) {
			appendPlayerAppearance(str);
		}
		if (FocusPointX != -1) {
			appendSetFocusDestination(str);
		}
		if (isHitUpdateRequired()) {
			appendHitUpdate(str);
		}
		if (hitUpdateRequired2) {
			appendHitUpdate2(str);
		}
	}

	public void clearUpdateFlags() {
		updateRequired = false;
		setChatTextUpdateRequired(false);
		setAppearanceUpdateRequired(false);
		setHitUpdateRequired(false);
		hitUpdateRequired2 = false;
		forcedChatUpdateRequired = false;
		mask100update = false;
		animationRequest = -1;
		FocusPointX = -1;
		FocusPointY = -1;
		poisonMask = -1;
		faceUpdateRequired = false;
		face = 65535;
	}

	public void stopMovement() {
		if (teleportToX <= 0 && teleportToY <= 0) {
			teleportToX = absX;
			teleportToY = absY;
		}
		newWalkCmdSteps = 0;
		getNewWalkCmdX()[0] = getNewWalkCmdY()[0] = travelBackX[0] = travelBackY[0] = 0;
		getNextPlayerMovement();
	}

	private int newWalkCmdX[] = new int[walkingQueueSize];
	private int newWalkCmdY[] = new int[walkingQueueSize];
	public int newWalkCmdSteps = 0;
	private boolean newWalkCmdIsRunning = false;
	protected int travelBackX[] = new int[walkingQueueSize];
	protected int travelBackY[] = new int[walkingQueueSize];
	protected int numTravelBackSteps = 0;

	public void preProcessing() {
		newWalkCmdSteps = 0;
	}

	public void postProcessing() {
		if (newWalkCmdSteps > 0) {
			int firstX = getNewWalkCmdX()[0], firstY = getNewWalkCmdY()[0];

			int lastDir = 0;
			boolean found = false;
			numTravelBackSteps = 0;
			int ptr = wQueueReadPtr;
			int dir = Misc.direction(currentX, currentY, firstX, firstY);
			if (dir != -1 && (dir & 1) != 0) {
				do {
					lastDir = dir;
					if (--ptr < 0) {
						ptr = walkingQueueSize - 1;
					}

					travelBackX[numTravelBackSteps] = walkingQueueX[ptr];
					travelBackY[numTravelBackSteps++] = walkingQueueY[ptr];
					dir = Misc.direction(walkingQueueX[ptr], walkingQueueY[ptr], firstX, firstY);
					if (lastDir != dir) {
						found = true;
						break;
					}

				} while (ptr != wQueueWritePtr);
			} else {
				found = true;
			}

			if (!found) {
				println_debug("Fatal: couldn't find connection vertex! Dropping packet.");
			} else {
				wQueueWritePtr = wQueueReadPtr;

				addToWalkingQueue(currentX, currentY);

				if (dir != -1 && (dir & 1) != 0) {

					for (int i = 0; i < numTravelBackSteps - 1; i++) {
						addToWalkingQueue(travelBackX[i], travelBackY[i]);
					}
					int wayPointX2 = travelBackX[numTravelBackSteps - 1],
							wayPointY2 = travelBackY[numTravelBackSteps - 1];
					int wayPointX1, wayPointY1;
					if (numTravelBackSteps == 1) {
						wayPointX1 = currentX;
						wayPointY1 = currentY;
					} else {
						wayPointX1 = travelBackX[numTravelBackSteps - 2];
						wayPointY1 = travelBackY[numTravelBackSteps - 2];
					}

					dir = Misc.direction(wayPointX1, wayPointY1, wayPointX2, wayPointY2);
					if (dir == -1 || (dir & 1) != 0) {
						println_debug("Fatal: The walking queue is corrupt! wp1=(" + wayPointX1 + ", " + wayPointY1
								+ "), " + "wp2=(" + wayPointX2 + ", " + wayPointY2 + ")");
					} else {
						dir >>= 1;
					found = false;
					int x = wayPointX1, y = wayPointY1;
					while (x != wayPointX2 || y != wayPointY2) {
						x += Misc.directionDeltaX[dir];
						y += Misc.directionDeltaY[dir];
						if ((Misc.direction(x, y, firstX, firstY) & 1) == 0) {
							found = true;
							break;
						}
					}
					if (!found) {
						println_debug("Fatal: Internal error: unable to determine connection vertex!" + "  wp1=("
								+ wayPointX1 + ", " + wayPointY1 + "), wp2=(" + wayPointX2 + ", " + wayPointY2
								+ "), " + "first=(" + firstX + ", " + firstY + ")");
					} else {
						addToWalkingQueue(wayPointX1, wayPointY1);
					}
					}
				} else {
					for (int i = 0; i < numTravelBackSteps; i++) {
						addToWalkingQueue(travelBackX[i], travelBackY[i]);
					}
				}

				for (int i = 0; i < newWalkCmdSteps; i++) {
					addToWalkingQueue(getNewWalkCmdX()[i], getNewWalkCmdY()[i]);
				}

			}

			isRunning = isNewWalkCmdIsRunning() || isRunning2;
		}
	}
	
	public int getId() {
		return playerId;
	}
	
	public String getIP() {
		return connectedFrom;
	}
	
	public String getMAC() {
		return identityPunishment;
	}
	
	public void disconnect() {
		disconnected = true;
	}
	
	public String getName() {
		return playerName;
	}

	public String getDisplayName() {
		return (displayName == "" ? playerName : displayName);
	}
	
	public boolean hasDisplayName(String s) {
		return (getDisplayName().equalsIgnoreCase(s) ? true : false);
	}
	
	/**
	 * TODO transition playerRights and everything dealing with player rights to
	 * its own class, main.game.players.Rights
	 * 
	 * Eventually we'll phase out the majority of fields, the Player class is
	 * just a dumping ground for PI's shit and I'd like to see a lot of this
	 * stuff in its own classes which are instanced here.
	 */
	
	public static int
	RIGHTS_PLAYER         = 0,
	RIGHTS_MODERATOR      = 1,
	RIGHTS_ADIMINISTRATOR = 2,
	RIGHTS_DEVELOPER      = 3;
	
	public void setRights(int i) {
		getInstance().playerRights = i;
	}
	
	public int getRights() {
		return getInstance().playerRights;
	}
	
	public boolean hasRights(int i) {
		return (getInstance().playerRights == i ? true : false);
	}
	
	public int getX() {
		return absX;
	}

	public int getY() {
		return absY;
	}
	
	public int getZ() {
		return heightLevel;
	}
	
	public int getLastX() {
		return lastAbsX;
	}

	public int getLastY() {
		return lastAbsY;
	}	
	
	public int getLocalX() {
		return getX() - 8 * getMapRegionX();
	}

	public int getLocalY() {
		return getY() - 8 * getMapRegionY();
	}	
	
	public int getMapRegionX() {
		return mapRegionX;
	}
	
	public int getMapRegionY() {
		return mapRegionY;
	}

	public boolean inPcGame() {
		return absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619;
	}

	public void setHitDiff(int hitDiff) {
		this.hitDiff = hitDiff;
	}

	public void setHitDiff2(int hitDiff2) {
		this.hitDiff2 = hitDiff2;
	}

	public int getHitDiff() {
		return hitDiff;
	}

	public void setHitUpdateRequired(boolean hitUpdateRequired) {
		this.hitUpdateRequired = hitUpdateRequired;
	}

	public void setHitUpdateRequired2(boolean hitUpdateRequired2) {
		this.hitUpdateRequired2 = hitUpdateRequired2;
	}

	public boolean isHitUpdateRequired() {
		return hitUpdateRequired;
	}

	public boolean getHitUpdateRequired() {
		return hitUpdateRequired;
	}

	public boolean getHitUpdateRequired2() {
		return hitUpdateRequired2;
	}

	public void setAppearanceUpdateRequired(boolean appearanceUpdateRequired) {
		this.appearanceUpdateRequired = appearanceUpdateRequired;
	}

	public boolean isAppearanceUpdateRequired() {
		return appearanceUpdateRequired;
	}

	public void setAppearanceUpdateRequired2(boolean appearanceUpdateRequired2) {
		this.appearanceUpdateRequired2 = appearanceUpdateRequired2;
	}

	public boolean isAppearanceUpdateRequired2() {
		return appearanceUpdateRequired2;
	}

	public void setChatTextEffects(int chatTextEffects) {
		this.chatTextEffects = chatTextEffects;
	}

	public int getChatTextEffects() {
		return chatTextEffects;
	}

	public void setChatTextSize(byte chatTextSize) {
		this.chatTextSize = chatTextSize;
	}

	public byte getChatTextSize() {
		return chatTextSize;
	}

	public void setChatTextUpdateRequired(boolean chatTextUpdateRequired) {
		this.chatTextUpdateRequired = chatTextUpdateRequired;
	}

	public boolean isChatTextUpdateRequired() {
		return chatTextUpdateRequired;
	}

	public void setChatText(byte chatText[]) {
		this.chatText = chatText;
	}

	public byte[] getChatText() {
		return chatText;
	}

	public void setChatTextColor(int chatTextColor) {
		this.chatTextColor = chatTextColor;
	}

	public int getChatTextColor() {
		return chatTextColor;
	}

	public void setNewWalkCmdX(int newWalkCmdX[]) {
		this.newWalkCmdX = newWalkCmdX;
	}

	public int[] getNewWalkCmdX() {
		return newWalkCmdX;
	}

	public void setNewWalkCmdY(int newWalkCmdY[]) {
		this.newWalkCmdY = newWalkCmdY;
	}

	public int[] getNewWalkCmdY() {
		return newWalkCmdY;
	}

	public void setNewWalkCmdIsRunning(boolean newWalkCmdIsRunning) {
		this.newWalkCmdIsRunning = newWalkCmdIsRunning;
	}

	public boolean isNewWalkCmdIsRunning() {
		return newWalkCmdIsRunning;
	}

	public void setInStreamDecryption(ISAACRandomGen inStreamDecryption) {
	}

	public void setOutStreamDecryption(ISAACRandomGen outStreamDecryption) {
	}

	public boolean samePlayer() {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (j == playerId) {
				continue;
			}
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].playerName.equalsIgnoreCase(playerName)) {
					disconnected = true;
					return true;
				}
			}
		}
		return false;
	}

	public void putInCombat(int attacker) {
		underAttackBy = attacker;
		logoutDelay = System.currentTimeMillis();
		singleCombatDelay = System.currentTimeMillis();
	}

	public int[] keepItems = new int[4];
	public int[] keepItemsN = new int[4];
	public int WillKeepAmt1, WillKeepAmt2, WillKeepAmt3, WillKeepAmt4, WillKeepItem1, WillKeepItem2, WillKeepItem3,
	WillKeepItem4, WillKeepItem1Slot, WillKeepItem2Slot, WillKeepItem3Slot, WillKeepItem4Slot, EquipStatus;

	private Player c = this;

	public void dealDamage(int damage) {
		if (lifePoints - damage < 0) {
			damage = lifePoints;
		}
		lifePoints -= damage;
		equpimentEffect(damage);
		int difference = lifePoints - damage;
		if (difference <= maxLifePoints / 10 && difference > 0)
			appendRedemption();
		if (playerEquipment[playerShield] == 13740 && damage > 0) { // Divine
			int prayerDamage = (int) (damage * .15);
			if (playerLevel[5] >= prayerDamage) {
				playerLevel[5] -= prayerDamage;
				damage = (int) (damage * .70);
			} else {
				double mod = (playerLevel[5] / prayerDamage);
				mod = mod * .30;
				damage = (int) (damage * mod);
				playerLevel[5] = 0;
			}
			this.getPA().refreshSkill(5);
		}
	}

	public void appendRedemption() {
		Player c = PlayerHandler.players[this.playerId];
		if (prayerActive[22]) {
			lifePoints += (int) (maxLifePoints * .25);
			playerLevel[5] = 0;
			c.getPA().refreshSkill(3);
			c.getPA().refreshSkill(5);
			gfx0(436);
			CombatPrayer.resetPrayers(c);
		}
	}

	public int[] damageTaken = new int[Constants.MAX_PLAYERS];
	public int hitMask, hitIcon, hitMask2, hitIcon2;

	public void handleHitMask(int damage, int mask, int icon, int soak, boolean maxHit) {
		if (!hitUpdateRequired) {
			hitDiff = damage;
			hitMask = maxHit ? 1 : mask;
			hitIcon = icon;
			soakDamage = soak;
			hitUpdateRequired = true;
		} else if (!hitUpdateRequired2) {
			hitDiff2 = damage;
			hitMask2 = maxHit ? 1 : mask;
			hitIcon2 = icon;
			soakDamage2 = soak;
			hitUpdateRequired2 = true;
		}
		updateRequired = true;
	}

	public CycleEvent skilling, spellSwap;
	public int skillTask;

	public boolean closed = false;

	public String skillReseted;

	public long skillTimer;

	public int familiarIndex;

	public int summoningSpecialPoints;

	public int timeBetweenSpecials;

	public int familiarID;

	public int specHitTimer;

	public boolean hasActivated;
	public boolean found;
	public int crateId = -1;

	public boolean hasOverloadBoost;
	public long lastSkillingAction;

	public void stopEvent(CycleEvent event) {
		if (event != null) {
			event.stop();
			event = null;
		}
	}

	public boolean checkTask(int task) {
		return task == skillTask;
	}

	public void stopSkilling() {
		if (skilling != null) {
			skilling.stop();
			skilling = null;
		}
	}

	public void setSkilling(CycleEvent event) {
		this.skilling = event;
	}

	public CycleEvent getSkilling() {
		return skilling;
	}

	private void equpimentEffect(int damage) {

		if (playerEquipment[playerRing] == 2570) {
			if (damage != lifePoints) {
				int hpLimit = (int) Math.ceil(maxLifePoints * 0.1);
				int futureDamage = lifePoints - damage;
				if (futureDamage <= hpLimit) {
					if (c.wildLevel <= 20) {
						c.getItems().deleteEquipment(10, playerRing);
						c.getPA().startTeleport2(Constants.LUMBY_X, Constants.LUMBY_Y, 0);
						c.sendMessage("Your ring of life saves you and is destoryed in the process.");
					}
				}
			}
		}

		if (playerEquipment[playerAmulet] == 11090) {
			if (damage != lifePoints) {
				int hpLimit = (int) Math.ceil(maxLifePoints * 0.2);
				int futureDamage = lifePoints - damage;
				if (futureDamage <= hpLimit) {
					if (!c.duelRule[DuelArena.RULE_FOOD]) {
						int toHeal = (int) Math.ceil(maxLifePoints * 0.3);
						lifePoints += toHeal;
						c.getItems().deleteEquipment(10, playerAmulet);
						c.sendMessage("Your necklace heals you and is destroyed in the process.");
					}
				}
			}
		}
	}

	public Player getClient(String name) {
		name = name.toLowerCase();
		for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
			if (validClient(i)) {
				Player client = getClient(i);
				if (client.playerName.toLowerCase().equalsIgnoreCase(name)) {
					return client;
				}
			}
		}
		return null;
	}

	public boolean isBusy(Player p) {
		return p.busy;
	}

	public void setBusy(Boolean busy) {
		this.busy = busy;
	}

	public Player getClient(int id) {
		return PlayerHandler.players[id];
	}

	public boolean validClient(int id) {
		if (id < 0 || id > Constants.MAX_PLAYERS) {
			return false;
		}
		return validClient(getClient(id));
	}

	public boolean validClient(String name) {
		return validClient(getClient(name));
	}

	public boolean validClient(Player client) {
		return (client != null && !client.disconnected);
	}
}