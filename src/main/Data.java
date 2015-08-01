package main;

/**
 * A utility class that tracks fields related to the server's data files.
 * 
 * @author KeepBotting
 *
 */

public class Data {
	
	/**
	 * The root directory, containing all of the server's data files.
	 */
	public static final String DATA_DIRECTORY       = "./data/";
	
	/**
	 * The character directory, containing players' save game files.
	 */
	public static final String CHARACTER_DIRECTORY  = DATA_DIRECTORY + "characters/";
	
	/**
	 * The map directory, containing the server's mapdata in .gz format.
	 * 
	 * TODO transfer mapdata to this directory instead of MAP_CONFIG_DIRECTORY
	 */
	public static final String MAP_DIRECTORY        = DATA_DIRECTORY + "map/";
	
	/**
	 * The config directory, containing the server's configuration files.
	 */
	public static final String CONFIG_DIRECTORY     = DATA_DIRECTORY + "config/";
	
	/*
	 * The configuration sub-directories.
	 */
	public static final String NPC_CONFIG_DIRECTORY       = CONFIG_DIRECTORY + "npc/";
	public static final String ITEM_CONFIG_DIRECTORY      = CONFIG_DIRECTORY + "item/";
	public static final String DOOR_CONFIG_DIRECTORY      = CONFIG_DIRECTORY + "door/";
	public static final String PLAYER_CONFIG_DIRECTORY    = CONFIG_DIRECTORY + "player/";
	public static final String OBJECT_CONFIG_DIRECTORY    = CONFIG_DIRECTORY + "object/";
	public static final String MAP_CONFIG_DIRECTORY       = CONFIG_DIRECTORY + "map/";
	public static final String SHOP_CONFIG_DIRECTORY      = CONFIG_DIRECTORY + "shop/";
	
	public static final String CLAN_DATA_CONFIG_DIRECTORY = CONFIG_DIRECTORY + "clan/";
	public static final String CLAN_CHAT_CONFIG_DIRECTORY = CLAN_DATA_CONFIG_DIRECTORY + "clan-chat/";
	
	/*
	 * NPC configuration files.
	 */
	public static final String NPC_LIST        = NPC_CONFIG_DIRECTORY + "npc-list.cfg";
	public static final String NPC_SPAWN       = NPC_CONFIG_DIRECTORY + "npc-spawn.cfg";
	public static final String NPC_DROPS       = NPC_CONFIG_DIRECTORY + "npc-drops.tsm";
	public static final String NPC_CONSTANTS   = NPC_CONFIG_DIRECTORY + "npc-constants.tsm";
	public static final String NPC_SIZES       = NPC_CONFIG_DIRECTORY + "npc-sizes.txt";
	
	/*
	 * Item configuration files.
	 */
	public static final String ITEM_LIST       = ITEM_CONFIG_DIRECTORY + "item-list.cfg";
	public static final String ITEM_PRICES     = ITEM_CONFIG_DIRECTORY + "item-prices.txt";
	public static final String ITEM_STACKABLE  = ITEM_CONFIG_DIRECTORY + "item-stackable.dat";
	public static final String ITEM_NOTABLE    = ITEM_CONFIG_DIRECTORY + "item-notable.dat";
	public static final String ITEM_EQUIPPABLE = ITEM_CONFIG_DIRECTORY + "item-equippable.dat"; 
	
	/*
	 * Door configuration files.
	 */
	public static final String DOOR_SINGLE     = DOOR_CONFIG_DIRECTORY + "door-single.txt";
	public static final String DOOR_DOUBLE     = DOOR_CONFIG_DIRECTORY + "door-double.txt";
	
	/*
	 * Player configuration files.
	 */
	public static final String PLAYER_DISPLAY_NAMES = PLAYER_CONFIG_DIRECTORY + "display-names.txt";
	
	/*
	 * Object configuration files.
	 */
	public static final String OBJECT_LOC_DATA      = OBJECT_CONFIG_DIRECTORY + "object-loc.dat";
	public static final String OBJECT_LOC_INDEX     = OBJECT_CONFIG_DIRECTORY + "object-loc.idx";
	public static final String OBJECT_LIST          = OBJECT_CONFIG_DIRECTORY + "object-list.cfg";
	
	/*
	 * Map configuration files.
	 */
	public static final String MAP_INDEX            = MAP_CONFIG_DIRECTORY + "map-index";
	
	/*
	 * Shop configuration files.
	 */
	public static final String SHOP_LIST            = SHOP_CONFIG_DIRECTORY + "shop-list.cfg";
	
	/**
	 * The log directory, containing all of the server's event logs.
	 */
	public static final String LOG_DIRECTORY = DATA_DIRECTORY + "log/";
	
	/*
	 * Individual log files.
	 */
	public static final String COMMAND_LOG = LOG_DIRECTORY + "cmd.log";
	public static final String TRADE_LOG   = LOG_DIRECTORY + "trade.log";
	public static final String DROP_LOG    = LOG_DIRECTORY + "drop.log";
	public static final String CHAT_LOG    = LOG_DIRECTORY + "chat.log";
	public static final String DUEL_LOG    = LOG_DIRECTORY + "duel.log";
	

	
}
