package main;

import java.io.IOException;
import java.net.InetSocketAddress;

import javax.swing.UnsupportedLookAndFeelException;

import org.apache.mina.common.IoAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;

import main.event.CycleEventHandler;
import main.event.TaskScheduler;
import main.game.items.ItemLoader;
import main.game.npcs.NPCHandler;
import main.game.npcs.data.EmoteHandler;
import main.game.npcs.data.NPCDefinition;
import main.game.npcs.data.NPCDrops;
import main.game.players.JailHandler;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.game.players.PlayerSave;
import main.game.players.content.clanchat.ClanChatHandler;
import main.game.players.content.clanchat.load.Clans;
import main.game.players.content.minigames.impl.CastleWars;
import main.game.players.content.minigames.impl.FightCaves;
import main.game.players.content.minigames.impl.PestControl;
import main.game.players.content.skills.hunter.HunterGui;
import main.game.players.content.skills.hunter.HunterNpcs;
import main.handlers.ItemHandler;
import main.net.ConnectionHandler;
import main.net.ConnectionThrottleFilter;
import main.util.Misc;
import main.world.ObjectHandler;
import main.world.ObjectManager;
import main.world.ShopHandler;
import main.world.map.ObjectDef;
import main.world.map.Region;

/**
 * Project Insanity's game engine.
 * 
 * @author Balla
 */
public class GameEngine {

	private static IoAcceptor acceptor;
	private static ConnectionHandler connectionHandler;
	private static ConnectionThrottleFilter throttleFilter;
	private static Misc.Stopwatch cycleTimer;

	/**
	 * Used for the shutdown hook.
	 */
	public static boolean shutdownServer = false;

	/**
	 * Used for the update hook.
	 */
	public static boolean UpdateServer = false;

	/**
	 * Used for various farming skill-related processes.
	 */
	private static long minutesCounter;
	
	public static long getMinutesCounter() {
		return minutesCounter;
	}
	
	/**
	 * Used for timing mass-saves.
	 */
	private static long lastMassSave = System.currentTimeMillis();
	
	public static long getLastMassSave() {
		return System.currentTimeMillis() - lastMassSave;
	}
	
	public static void setLastMassSave(long l) {
		lastMassSave = l;
	}

	/**
	 * Used to identify the server port.
	 */
	public static int serverlistenerPort = 43594;

	/**
	 * Defines the cycle rate. PI cycles at 600 milliseconds, meaning that as
	 * far as this game engine is concerned, one "game tick" == 600ms.
	 */
	private final static int cycleRate = 600;

	/**
	 * Constructors for the core subsystems.
	 */
	public static ItemHandler itemHandler = new ItemHandler();
	public static PlayerHandler playerHandler = new PlayerHandler();
	public static NPCHandler npcHandler = new NPCHandler();
	public static EmoteHandler emoteHandler = new EmoteHandler();
	public static ShopHandler shopHandler = new ShopHandler();
	public static ObjectHandler objectHandler = new ObjectHandler();
	public static ObjectManager objectManager = new ObjectManager();
	public static CastleWars castleWars = new CastleWars();
	public static PestControl pestControl = new PestControl();
	public static NPCDrops npcDrops = new NPCDrops();
	public static ClanChatHandler clanChat = new ClanChatHandler();
	public static Clans pJClans = new Clans();
	public static FightCaves fightCaves = new FightCaves();
	
	private static final TaskScheduler scheduler = new TaskScheduler();

	public static TaskScheduler getScheduler() {
		return scheduler;
	}

	public static Player[] developer = new Player[Constants.DEVELOPER_AMOUNT];

	/**
	 * Launches the game server.
	 * 
	 * @param args
	 *            the program arguments
	 *            
	 * @throws IOException
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public static void main(final String[] args) throws IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		System.out.println("[1/7] Starting the server...");
		loadServerData();
		bind();
		cycleTimer = new Misc.Stopwatch();
		System.out.println("[7/7] The server has loaded and is accepting connections.");
		try {
			/**
			 * Here it is -- the massive loop that PI is so famous for. Using a
			 * loop to manage some events in a game isn't a bad thing -- unless
			 * EVERYTHING is handled by the same loop.
			 */
			while (!shutdownServer) {
				cycle();
				save();
				sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Performs a server cycle. This is where the magic happens. Each of the
	 * core subsystems will perform their process() for each cycle. At the end
	 * of the cycle, we'll sleep for however much time we have remaining.
	 */
	private static void cycle() {
		try {
			
			playerHandler.process();
			npcHandler.process();
			shopHandler.process();
			objectManager.process();
			pestControl.process();
			pJClans.process();
			HunterNpcs.process();
			CycleEventHandler.getSingleton().process();
			objectHandler.process();
			JailHandler.process();
			
		} catch (Exception e) {
			System.out.println("A fatal error has occured during the game engine's cycling process. The server will be halted immediately.");
			shutdownServer = true;
			e.printStackTrace();
		}
	}
	
	/**
	 * Mass-saving is done here, after the cycle ends. Saving is done once every
	 * thirty seconds.
	 * 
	 * TODO:
	 * 
	 * Mass-saves are a horrible way to do things, it's lazy, inefficient and
	 * puts undue stress on the server, eating up more cycle time than is
	 * necessary.
	 * 
	 * The best saving scheme I can think of is a randomized schedule generated
	 * on a per-player basis. When a player logs in, he or she is given a save
	 * timer of anywhere from 10 to 30 seconds.
	 * 
	 * Every time the server cycles, it checks the players' save timers against
	 * the last time it saved that player. If it's been long enough, the server
	 * will save them again. This spreads out and staggers player saving over
	 * several cycles instead of trying to shove an entire mass-save into a
	 * 600ms window.
	 * 
	 * Scheduled saving MUST be implemented before the server goes live. hades5
	 * couldn't get above ~80-100 players without lagging, this is why.
	 */
	private static void save() {
		if (getLastMassSave() > Constants.SAVE_TIMER) {
			System.out.println("Mass save for everybody.");
			for (int i = 0; i < PlayerHandler.getPlayerCount() + 1; i++) {
				if (PlayerHandler.isValid(i)) {
					PlayerSave.saveGame(PlayerHandler.getPlayer(i));
				}
			}
			setLastMassSave(System.currentTimeMillis());
		}

	}

	/**
	 * Sleeps for the remainder of the cycle. The method above performed the
	 * processing for the current cycle. The method below sleeps for the amount
	 * of time left over in the cycle.
	 * 
	 * Imagine, for a moment, that the server needed 100ms to perform all of its
	 * operations. This means that 100ms of the cycle is spent processing, 500ms
	 * is spent sleeping.
	 * 
	 * That's all well and good. But what happens if the server needs 500ms for
	 * processing, and spends 100ms sleeping? Still, we've managed to get all of
	 * our processing done in time. We're good.
	 * 
	 * But you can see where I'm going with this.
	 * 
	 * Now imagine that there are 300 players on the server, all actively
	 * performing tasks in-game: fighting, skilling, changing regions, clicking
	 * action buttons, etc. What happens if the server takes over 600ms to
	 * process all of these players?
	 * 
	 * Enter the big problem with PI. If a cycle takes more than 600ms to
	 * complete, the server begins a phase which I arbitrarily refer to as
	 * "over-cycling." This means that the game engine is taking MORE than a
	 * single game tick to process a single game tick's worth of events.
	 * 
	 * This causes all sorts of issues. Global server lag is the most obvious
	 * and the most disrupting to gameplay.
	 * 
	 * The server will also begin to ignore events submitted after the
	 * over-cycling begins. For example: if the engine is over-cycling by 50ms
	 * (taking 650ms to complete a cycle), then the FIRST 50ms of the NEXT game
	 * tick will be ignored, since that time is being used to play catch-up with
	 * the LAST tick's processing.
	 * 
	 * @throws InterruptedException
	 */
	private static void sleep() throws InterruptedException {
		/**
		 * The time we sleep is set to the server's cycle rate minus the time it
		 * took to complete the cycle.
		 */
		long sleepTime = cycleRate - cycleTimer.elapsed();
		long engineLoad = (100 - (Math.abs(sleepTime) / (cycleRate / 100)));
		
		if(engineLoad > 60) {
			System.out.println("Cycle rate: " + cycleTimer.elapsed() + " ms, engine load: " + engineLoad + "%");
		}
		
		if (sleepTime > 0) {
			Thread.sleep(sleepTime);
			/**
			 * If sleepTime is equal to or less than zero, it means the server
			 * is cycling at 600ms, or over-cycling at more than 600ms -- an
			 * engine load of 100% or greater.
			 */
		} else {
			System.out.println("[WARNING]: Server is over-cycling by " + Math.abs(sleepTime) + "ms, engine load: "
					+ (100 + Math.abs(engineLoad)) + "%");
		}
		cycleTimer.reset();
	}

	/**
	 * Binds the server to the port.
	 * 
	 * @throws IOException
	 */
	public static void bind() throws IOException {
		acceptor = new SocketAcceptor();
		connectionHandler = new ConnectionHandler();
		SocketAcceptorConfig sac = new SocketAcceptorConfig();
		sac.getSessionConfig().setTcpNoDelay(false);
		sac.setReuseAddress(true);
		sac.setBacklog(100);

		throttleFilter = new ConnectionThrottleFilter(Constants.CONNECTION_DELAY);
		sac.getFilterChain().addFirst("throttleFilter", throttleFilter);
		acceptor.bind(new InetSocketAddress(serverlistenerPort), connectionHandler, sac);
	}

	/**
	 * Initialize core subsystems.
	 * 
	 * @throws IOException
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	static void loadServerData() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException, IOException {
		Connection.loadConnectionData(false);
		Connection.loadConnectionData(true);
		try {
			NPCDefinition.init();
		} catch (Exception e) {
		}
		ObjectDef.loadConfig();
		System.out.println("[2/7] Loaded object configuration data.");
		Region.load();
		System.out.println("[3/7] Loaded region data.");
		pJClans.initialize();
		pJClans.loadOptions();
		System.out.println("[4/7] Loaded clan data.");
		HunterGui.showGUI = false;
		npcHandler.loadNpcs();
		System.out.println("[5/7] Loaded NPC data.");
		new HunterGui();
		ItemLoader.load();
		System.out.println("[6/7] Loaded item data.");
		Connection.loadConnectionData(false);
		Connection.loadConnectionData(true);
	}
	
	public static void sendStaffNotice(String message) {
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			if (PlayerHandler.getPlayer(i) != null) {
				Player p = PlayerHandler.getPlayer(i);
				
				if (p.getRights() >= Player.RIGHTS_MODERATOR) {
					p.sendMessage("@blu@Staff Notice: @bla@" + message);
				}
			}
		}
	}
}
