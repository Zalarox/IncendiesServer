package incendius;

public class Constants {

	public static final boolean SERVER_DEBUG_VERBOSE = false; //Enable verbose debug, both to STDOUT and to developers' chat boxes.
	public static final boolean SERVER_LOG_STDOUT = false; //Whether or not to periodically flush STDOUT to disk. (un-implemented)

	public static final String SERVER_NAME = "Incendius";
	public static final String SERVER_MOTD = "";
	
	public static final int SERVER_PORT              = 43594;
	public static final int SERVER_CYCLE_RATE_MS     = 600;
	public static final int SERVER_MASS_SAVE_RATE_MS = 30000;
	
	public static final String SERVER_VERSION_VERBOSE = "developer snapshot";
	public static final String SERVER_VERSION_MAJOR   = "2015-Nov-29";
	public static final String SERVER_VERSION_MINOR   = "";
	
	public static final String SERVER_FORUMS_URL = "";
	
	public static final double SERVER_GLOBAL_XP_MULTIPLIER = 1.0;
	
	public static final int MINIMUM_DELAY_REQUIRED_BETWEEN_CONNECTIONS_MS = 100; //Used for the connection throttle filter.
	public static final int MAXIMUM_CONCURRENT_CONNECTIONS_FROM_SINGLE_IP_ADDRESS = 2; //Used for handling multilogging.

	public static final boolean ADMINISTRATORS_CAN_TRADE_ITEMS = true;
	public static final boolean ADMINISTRATORS_CAN_SELL_ITEMS  = true;
	public static final boolean ADMINISTRATORS_CAN_DROP_ITEMS  = true;

	public static final int ITEM_LIMIT       = 25000; //Amount of items that can exist in the game.
	public static final int ITEM_STACK_LIMIT = Integer.MAX_VALUE; //Maximum instances of an item that can exist in a single stack.
	
	public static final int BANK_SIZE = 352; //Amount of spaces in players' banks.
	public static final int MAX_PLAYERS = 1024; //Maximum amount of concurrent players that the server will accept.
	public static final int MAX_CLANS = 3000; //Maximum amount of clans that can exist.

	public static final int CLIENT_VERSION = 999999; //Unused, guessing it was once meant for server-sided version checks.

	public static final boolean WORLD_LIST_FIX = true; //Used for something involving the PM system, unsure what...

	// Misnomer: This is what items CANNOT be sold.
	public static final int[] ITEM_SELLABLE = { 20072, 18349, 18351, 18353, 18355, 18335, 18357, 3842, 3844, 3840, 8844,
			8845, 8846, 8847, 8848, 8849, 8850, 10551, 6570, 7462, 7461, 7460, 7459, 7458, 7457, 7456, 7455, 7454, 8839,
			8840, 8842, 11663, 11664, 11665, 10499, 9748, 9754, 9751, 9769, 9757, 9760, 9763, 9802, 9808, 9784, 9799,
			9805, 9781, 9796, 9793, 9775, 9772, 9778, 9787, 9811, 9766, 9749, 9755, 9752, 9770, 9758, 9761, 9764, 9803,
			9809, 9785, 9800, 9806, 9782, 9797, 9794, 9776, 9773, 9779, 9788, 9812, 9767, 9747, 9753, 9750, 9768, 9756,
			9759, 9762, 9801, 9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777, 9786, 9810, 9765, 995 };

	// NOTE: Includes items that cannot be stacked!
	public static final int[] UNTRADEABLE_ITEM = { 20072, 15349, 3842, 3844, 3840, 8844, 8845, 8846, 8847, 8848, 8849,
			8850, 10551, 6570, 7462, 7461, 7460, 7459, 7458, 7457, 7456, 7455, 7454, 8839, 8840, 8842, 11663, 11664,
			11665, 10499, 9748, 9754, 9751, 9769, 9757, 9760, 9763, 9802, 9808, 9784, 9799, 9805, 9781, 9796, 9793,
			9775, 9772, 9778, 9787, 9811, 9766, 9749, 9755, 9752, 9770, 9758, 9761, 9764, 9803, 9809, 9785, 9800, 9806,
			9782, 9797, 9794, 9776, 9773, 9779, 9788, 9812, 9767, 9747, 9753, 9750, 9768, 9756, 9759, 9762, 9801, 9807,
			9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777, 9786, 9810, 9765, 15332, 15333, 15334, 15335, 18349,
			18350, 18351, 18352, 18353, 18354, 18355, 18356, 18357, 18358, 18359, 18360, 18361, 18362, 18363, 18364,
			18365, 18366, 18367, 18368, 18369, 18370, 18371, 18372, 18373, 18374 };

	public static final int[] UNDROPPABLE_ITEMS = { 20072, 15349, 3842, 3844, 3840, 8844, 8845, 8846, 8847, 8848, 8849,
			8850, 10551, 6570, 7462, 7461, 7460, 7459, 7458, 7457, 7456, 7455, 7454, 8839, 8840, 8842, 11663, 11664,
			11665, 10499, 9748, 9754, 9751, 9769, 9757, 9760, 9763, 9802, 9808, 9784, 9799, 9805, 9781, 9796, 9793,
			9775, 9772, 9778, 9787, 9811, 9766, 9749, 9755, 9752, 9770, 9758, 9761, 9764, 9803, 9809, 9785, 9800, 9806,
			9782, 9797, 9794, 9776, 9773, 9779, 9788, 9812, 9767, 9747, 9753, 9750, 9768, 9756, 9759, 9762, 9801, 9807,
			9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777, 9786, 9810, 9765 };

	public static final int[] DESTROYABLES = { 10548, 10551, 18349, 18351, 18353, 18355, 18357, 18359 };

	public static final int[] FUN_WEAPONS = { 2460, 2461, 2462, 2463, 2464, 2465, 2466, 2467, 2468, 2469, 2470, 2471,
			2471, 2473, 2474, 2475, 2476, 2477 };

	public static final int START_LOCATION_X = 3080;
	public static final int START_LOCATION_Y = 3504;
	public static final int RESPAWN_X = 3087;
	public static final int RESPAWN_Y = 3490;
	public static final int DUELING_RESPAWN_X = 3362;
	public static final int DUELING_RESPAWN_Y = 3263;
	public static final int RANDOM_DUELING_RESPAWN = 5; // random coordinates

	public static final int NO_TELEPORT_WILD_LEVEL = 20;

	public static final int SKULL_TIMER = 1200; // seconds*2 (what?)

	public static final int TELEBLOCK_DELAY = 20000;

	public static final boolean SINGLE_AND_MULTI_ZONES = true;
	public static final boolean COMBAT_LEVEL_DIFFERENCE = true;

	public static final boolean itemRequirements = true; // Stat requirements

	public static final boolean PRAYER_POINTS_REQUIRED = true;
	public static final boolean PRAYER_LEVEL_REQUIRED = true;
	public static final boolean MAGIC_LEVEL_REQUIRED = true;
	public static final int GOD_SPELL_CHARGE = 300000; // Charge? Timer?
	public static final boolean RUNES_REQUIRED = true; // ??? For any spell?
	public static final boolean CORRECT_ARROWS = true; // ??? What?
	
	public static final boolean CRYSTAL_BOW_DEGRADES = true;
	public static final int ATTACK = 0;
	public static final int DEFENCE = 1;
	public static final int STRENGTH = 2;
	public static final int HITPOINTS = 3;
	public static final int RANGED = 4;
	public static final int PRAYER = 5;
	public static final int MAGIC = 6;
	public static final int COOKING = 7;
	public static final int WOODCUTTING = 8;
	public static final int HANDS = 9;
	public static final int FLETCHING = 9;
	public static final int FISHING = 10;
	public static final int FIREMAKING = 11;
	public static final int CRAFTING = 12;
	public static final int SMITHING = 13;
	public static final int MINING = 14;
	public static final int HERBLORE = 15;
	public static final int AGILITY = 16;
	public static final int THIEVING = 17;
	public static final int SLAYER = 18;
	public static final int FARMING = 19;
	public static final int RUNECRAFTING = 20;

	public static final int attStab = 0, attSlash = 1, attCrush = 2, attMagic = 3, attRange = 4, defStab = 5,
			defSlash = 6, defCrush = 7, defMagic = 8, defRange = 9, str = 10, pray = 11, walkingQueueSize = 50,
			IMMUNE_SPELL_TIME[] = { 250000, 250000, 250000, 500000, 500000, 500000 }, POUCH_SIZE[] = { 3, 6, 9, 12 },
			REDUCE_SPELLS[] = { 1153, 1157, 1161, 1542, 1543, 1562 },
			DUEL_RULE_ID[] = { 1, 2, 16, 32, 64, 128, 256, 512, 1024, 4096, 8192, 16384, 32768, 65536, 131072, 262144,
					524288, 2097152, 8388608, 16777216, 67108864, 134217728 };
	
	// The square grid the NPC cannot move out of (3x3 apparently?)
	public static final int NPC_RANDOM_WALK_DISTANCE = 5; 
	public static final int NPC_FOLLOW_DISTANCE = 10;
	
	public static final int[] UNDEAD_NPCS = { 90, 91, 92, 93, 94, 103, 104, 73, 74, 75, 76, 77 };

	/**
	 * Glory
	 */
	public static final int EDGEVILLE_X = 3087;
	public static final int EDGEVILLE_Y = 3500;
	public static final String EDGEVILLE = "";
	
	public static final int AL_KHARID_X = 2916;
	public static final int AL_KHARID_Y = 3176;
	public static final String AL_KHARID = "";
	
	public static final int KARAMJA_X = 3293;
	public static final int KARAMJA_Y = 3183;
	public static final String KARAMJA = "";
	
	public static final int MAGEBANK_X = 3084;
	public static final int MAGEBANK_Y = 3248;
	public static final String MAGEBANK = "";

	/**
	 * Teleport Spells
	 **/
	// modern
	public static final int VARROCK_X = 3210;
	public static final int VARROCK_Y = 3424;
	public static final String VARROCK = "";
	
	public static final int LUMBY_X = 3222;
	public static final int LUMBY_Y = 3218;
	public static final String LUMBY = "";
	
	public static final int FALADOR_X = 2964;
	public static final int FALADOR_Y = 3378;
	public static final String FALADOR = "";
	
	public static final int CAMELOT_X = 2757;
	public static final int CAMELOT_Y = 3477;
	public static final String CAMELOT = "";
	
	public static final int ARDOUGNE_X = 2662;
	public static final int ARDOUGNE_Y = 3305;
	public static final String ARDOUGNE = "";
	
	public static final int WATCHTOWER_X = 3054;
	public static final int WATCHTOWER_Y = 3508;
	public static final String WATCHTOWER = "";
	
	public static final int TROLLHEIM_X = 3243;
	public static final int TROLLHEIM_Y = 3513;
	public static final String TROLLHEIM = "";

	// ancient

	public static final int PADDEWWA_X = 3098;
	public static final int PADDEWWA_Y = 9884;

	public static final int SENNTISTEN_X = 3322;
	public static final int SENNTISTEN_Y = 3336;

	public static final int KHARYRLL_X = 3492;
	public static final int KHARYRLL_Y = 3471;

	public static final int LASSAR_X = 3006;
	public static final int LASSAR_Y = 3471;

	public static final int DAREEYAK_X = 3161;
	public static final int DAREEYAK_Y = 3671;

	public static final int CARRALLANGAR_X = 3156;
	public static final int CARRALLANGAR_Y = 3666;

	public static final int ANNAKARL_X = 3288;
	public static final int ANNAKARL_Y = 3886;

	public static final int GHORROCK_X = 2977;
	public static final int GHORROCK_Y = 3873;

	public static final int TIMEOUT = 20;
	public static final int BUFFER_SIZE = 10025;

	/**
	 * Slayer Variables
	 */
	public static final int[][] SLAYER_TASKS = { 
			{ 1, 87, 90, 4, 5 }, // low tasks
			{ 6, 7, 8, 9, 10 }, // med tasks
			{ 11, 12, 13, 14, 15 }, // high tasks
			{ 1, 1, 15, 20, 25 }, // low requirements
			{ 30, 35, 40, 45, 50 }, // med requirements
			{ 60, 75, 80, 85, 90 } }; // high requirements

	public static final int HAT = 0;
	public static final int CAPE = 1;
	public static final int AMULET = 2;
	public static final int WEAPON = 3;
	public static final int CHEST = 4;
	public static final int SHIELD = 5;
	public static final int LEGS = 7;
	public static final int FEET = 10;
	public static final int RING = 12;
	public static final int ARROWS = 13;

	public static boolean FARMING_ENABLED = true; // Doesn't work, apparently
	public static boolean RUNECRAFTING_ENABLED = true;
	public static boolean HERBLORE_ENABLED = true;

	public static final int PACKET_SIZES[] = { 0, 0, 0, 1, -1, 0, 0, 0, 0, 0, // 0
			0, 0, 0, 0, 8, 0, 6, 2, 2, 0, // 10
			0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20`
			0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
			2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
			0, 0, 0, 12, 0, 0, 0, 8, 8, 12, // 50
			8, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
			6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
			0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
			0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
			0, 13, 0, -1, 0, 0, 0, 0, 0, 0, // 100
			0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
			1, 0, 6, 0, 0, 0, -1, 0, 2, 6, // 120
			0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
			0, 0, 0, 0, 0, 6, 0, 0, 0, 0, // 140
			0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
			0, 0, 0, 0, -1, -1, 0, 0, 0, 0, // 160
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
			0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
			0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
			2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
			4, 0, 0, 0, 7, 8, 0, 0, 10, 0, // 210
			0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
			1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230
			0, 4, 0, 0, 0, 0, -1, 0, -1, 4, // 240
			0, 0, 6, 6, 0, 0, 0 // 250
	};

	public static final boolean MYSQL_ACTIVE = true;

	public static boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		return ((objectX - playerX <= distance && objectX - playerX >= -distance)
				&& (objectY - playerY <= distance && objectY - playerY >= -distance));
	}
}
