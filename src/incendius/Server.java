package incendius;

import java.io.IOException;
import java.net.InetSocketAddress;

import javax.swing.UnsupportedLookAndFeelException;

import org.apache.mina.common.IoAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;

import incendius.event.CycleEventHandler;
import incendius.event.TaskScheduler;
import incendius.game.items.ItemLoader;
import incendius.game.npcs.NPCHandler;
import incendius.game.npcs.data.EmoteHandler;
import incendius.game.npcs.data.NPCDefinition;
import incendius.game.npcs.data.NPCDrops;
import incendius.game.players.JailHandler;
import incendius.game.players.Player;
import incendius.game.players.PlayerHandler;
import incendius.game.players.PlayerSave;
import incendius.game.players.content.clanchat.ClanChatHandler;
import incendius.game.players.content.clanchat.load.Clans;
import incendius.game.players.content.minigames.impl.CastleWars;
import incendius.game.players.content.minigames.impl.FightCaves;
import incendius.game.players.content.minigames.impl.PestControl;
import incendius.game.players.content.skills.hunter.HunterNpcs;
import incendius.game.players.punishments.PunishmentHandler;
import incendius.handlers.ItemHandler;
import incendius.net.ConnectionHandler;
import incendius.net.ConnectionThrottleFilter;
import incendius.util.Misc;
import incendius.world.ObjectHandler;
import incendius.world.ObjectManager;
import incendius.world.ShopHandler;
import incendius.world.map.ObjectDef;
import incendius.world.map.Region;

/**
 * Project Insanity's game engine.
 * 
 * @author Balla
 */
public class Server {

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
	
	private static long getLastMassSave() {
		return System.currentTimeMillis() - lastMassSave;
	}
	
	public static void setLastMassSave() {
		lastMassSave = System.currentTimeMillis();
	}
	
	/**
	 * Used for debugging load times.
	 */
	private static long loadTime = System.currentTimeMillis();
	
	public static void resetLoadTime() {
		System.out.println(System.currentTimeMillis() - loadTime + "ms");
		loadTime = System.currentTimeMillis();
	}

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
		System.out.println("Launching Project Insanity...");
		System.out.println();
		
		load();
		bind();
		
		cycleTimer = new Misc.Stopwatch();
		
		System.out.println();
		System.out.println("The server is listening on port " + Constants.SERVER_PORT + ".");
		
		try {
			/**
			 * Here it is: the massive loop that PI is so famous for. Using a
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
	 * Initialize core subsystems.
	 * 
	 * @throws IOException
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	static void load() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException, IOException {
		
		try {
			
		Connection.loadConnectionData(false);
		Connection.loadConnectionData(true);
		
		try {
			NPCDefinition.init();
		} catch (Exception e) {
		}
		
		System.out.print("Loading object data... ");
		ObjectDef.loadConfig();
		resetLoadTime();
		
		System.out.print("Loading region data... ");
		Region.load();
		resetLoadTime();
		
		System.out.print("Loading clan data... ");
		pJClans.initialize();
		pJClans.loadOptions();
		resetLoadTime();
		
		System.out.print("Loading NPC data... ");
		npcHandler.loadNpcs();
		resetLoadTime();
		
		System.out.print("Loading item data... ");
		ItemLoader.load();
		resetLoadTime();
		
		System.out.print("Loading punishment data... ");
		Connection.loadConnectionData(false);
		Connection.loadConnectionData(true);
		PunishmentHandler.load();
		resetLoadTime();
		
		} catch (Exception e) {
			System.out.println("A fatal error has occured during the server's startup process.");
			e.printStackTrace();
			System.exit(1);
		}
		
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

		throttleFilter = new ConnectionThrottleFilter(Constants.MINIMUM_DELAY_REQUIRED_BETWEEN_CONNECTIONS_MS);
		sac.getFilterChain().addFirst("throttleFilter", throttleFilter);
		acceptor.bind(new InetSocketAddress(Constants.SERVER_PORT), connectionHandler, sac);
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
			System.out.println(
					"A fatal error has occured during the game engine's cycling process. The server will be halted immediately.");
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
		if (getLastMassSave() > Constants.SERVER_MASS_SAVE_RATE_MS) {
			for (int i = 0; i < PlayerHandler.getPlayerCount() + 1; i++) {
				if (PlayerHandler.isValid(i)) {
					PlayerSave.saveGame(PlayerHandler.getPlayer(i));
				}
			}
			setLastMassSave();
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
		long sleepTime = Constants.SERVER_CYCLE_RATE_MS - cycleTimer.elapsed();
		long engineLoad = (100 - (Math.abs(sleepTime) / (Constants.SERVER_CYCLE_RATE_MS / 100)));
		
		if(engineLoad > 60) {
			System.out.println("WARNING: Cycle rate " + cycleTimer.elapsed() + " ms, engine load " + engineLoad + "%");
		}
		
		if (sleepTime > 0) {
			Thread.sleep(sleepTime);
			/**
			 * If sleepTime is equal to or less than zero, it means the server
			 * is cycling at 600ms, or over-cycling at more than 600ms -- an
			 * engine load of 100% or greater.
			 */
		} else {
			System.out.println("Can't keep up! Running " + Math.abs(sleepTime) + "ms behind, skipping "
					+ (Math.abs(sleepTime) / Constants.SERVER_CYCLE_RATE_MS) + " cycle(s)");
		}
		cycleTimer.reset();
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
	
	public static void sendDeveloperNotice(String message) {
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			if (PlayerHandler.getPlayer(i) != null) {
				Player p = PlayerHandler.getPlayer(i);
				
				if (p.getRights() >= Player.RIGHTS_DEVELOPER) {
					p.sendMessage("[Debug]: " + message);
				}
			}
		}
	}
}
