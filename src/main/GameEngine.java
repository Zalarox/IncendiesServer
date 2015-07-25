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
import main.game.players.Player;
import main.game.players.PlayerHandler;
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
 * Game engine
 * 
 * @author Balla
 */
public class GameEngine {

	private static IoAcceptor acceptor;
	private static ConnectionHandler connectionHandler;
	private static ConnectionThrottleFilter throttleFilter;
	private static Misc.Stopwatch cycleTimer;
	public static boolean shutdownServer = false;
	public static boolean UpdateServer = false;
	private static long minutesCounter;

	/**
	 * Used to identify the server port.
	 */
	public static int serverlistenerPort = 43594;
	private final static int cycleRate = 600;

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

	public static Player[] developer = new Player[Constants.DEVELOPER_AMOUNT];

	/**
	 * Launches the game server.
	 * 
	 * @param args
	 *            the program arguments
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
			while (!shutdownServer) {
				cycle();
				sleep();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * Performs a server cycle.
	 */
	private static void cycle() {
		// Next, perform game processing.
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
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Sleeps for the cycle delay.
	 * 
	 * @throws InterruptedException
	 */
	private static void sleep() throws InterruptedException {
		long sleepTime = cycleRate - cycleTimer.elapsed();

		System.out.println("Cycle rate: " + cycleTimer.elapsed() + " ms, engine load: "
				+ (100 - (Math.abs(sleepTime) / (cycleRate / 100))) + "%");

		if (sleepTime > 0) {
			Thread.sleep(sleepTime);
		} else {
			// The server has reached maximum load, players may now lag.
			System.out.println("[WARNING]: Engine load has reached a critical level!");
		}
		cycleTimer.reset();
	}

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
	 * Initialize Handlers
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

	public static long getMinutesCounter() {
		return minutesCounter;
	}

	private static final TaskScheduler scheduler = new TaskScheduler();

	public static TaskScheduler getScheduler() {
		return scheduler;
	}

}
