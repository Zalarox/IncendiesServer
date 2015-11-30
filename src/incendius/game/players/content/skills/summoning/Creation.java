package incendius.game.players.content.skills.summoning;

import incendius.event.CycleEvent;
import incendius.event.CycleEventContainer;
import incendius.event.CycleEventHandler;
import incendius.game.players.Player;
import incendius.handlers.SkillHandler;

/**
 * 
 * @author Tringan & Pokemon
 *
 */
public class Creation {

	private final static int GOLD_CHARM = 12158, GREEN_CHARM = 12159, CRIMSON_CHARM = 12160, ABYSSAL_CHARM = 12161,
			TALON_BEAST_CHARM = 12162, BLUE_CHARM = 12163, RAVAGER_CHARM = 12164, SHIFTER_CHARM = 12165,
			SPINNER_CHARM = 12166, TORCHER_CHARM = 12167, OBSIDIAN_CHARM = 12168;

	private static final int SHARD_ID = 18016;

	private static final int POUCH_ID = 12155;

	public enum Data {

		SPIRIT_WOLF(91184, 12047, 12425, new int[] { GOLD_CHARM, 2859 }, 7, 1, 5), SPIRIT_DREADFOWL(91192, 12043, 12445,
				new int[] { GOLD_CHARM, 2138 }, 8, 8,
				9), SPIRIT_SPIDER(91200, 12059, 12428, new int[] { GOLD_CHARM, 6291 }, 8, 8, 10), THORNY_SNAIL(91208,
						12019, 12459, new int[] { GOLD_CHARM, 3363 }, 9, 13, 12), GRANITE_CRAB(91216, 12009, 12533,
								new int[] { GOLD_CHARM, 440 }, 7, 16, 22), SPIRIT_MOSQUITO(91224, 12778, 12838,
										new int[] { GOLD_CHARM, 6319 }, 1, 17, 47), DESERT_WYRM(91232, 12049, 13460,
												new int[] { GREEN_CHARM, 1783 }, 45, 18, 31), SPIRIT_SCORPION(91240,
														12055, 12432, new int[] { CRIMSON_CHARM, 3095 }, 57, 19,
														83), SPIRIT_TZ_KIH(91248, 12808, 12839,
																new int[] { CRIMSON_CHARM, OBSIDIAN_CHARM }, 64, 22,
																97), ALBINO_RAT(92000, 12067, 12430,
																		new int[] { BLUE_CHARM, 2134 }, 75, 23,
																		202), SPIRIT_KALPHITE(92008, 12063, 12446,
																				new int[] { BLUE_CHARM, 3138 }, 51, 25,
																				220), COMPOST_MOUND(92016, 12091, 12440,
																						new int[] { GREEN_CHARM, 6032 },
																						47, 28, 50), GIANT_CHINCHOMPA(
																								92024, 12800, 12834,
																								new int[] { BLUE_CHARM,
																										9976 },
																								84, 29,
																								255), VAMPIRE_BAT(92032,
																										12053, 12447,
																										new int[] {
																												CRIMSON_CHARM,
																												3325 },
																										81, 31,
																										136), HONEY_BADGER(
																												92040,
																												12065,
																												12433,
																												new int[] {
																														CRIMSON_CHARM,
																														12156 },
																												84, 32,
																												141), BEAVER(
																														92048,
																														12021,
																														12429,
																														new int[] {
																																GREEN_CHARM,
																																1519 },
																														72,
																														33,
																														58), VOID_RAVAGER(
																																92056,
																																12818,
																																12443,
																																new int[] {
																																		GREEN_CHARM,
																																		RAVAGER_CHARM },
																																74,
																																34,
																																60), VOID_SHIFTER(
																																		92080,
																																		12814,
																																		12443,
																																		new int[] {
																																				BLUE_CHARM,
																																				SHIFTER_CHARM },
																																		74,
																																		34,
																																		60), VOID_SPINNER(
																																				92064,
																																				12780,
																																				12443,
																																				new int[] {
																																						BLUE_CHARM,
																																						SPINNER_CHARM },
																																				74,
																																				34,
																																				60), VOID_TORCHER(
																																						92072,
																																						12798,
																																						12443,
																																						new int[] {
																																								BLUE_CHARM,
																																								TORCHER_CHARM },
																																						74,
																																						34,
																																						60), BRONZE_MINOTAUR(
																																								92088,
																																								12073,
																																								12461,
																																								new int[] {
																																										BLUE_CHARM,
																																										2349 },
																																								102,
																																								36,
																																								317), BULL_ANT(
																																										92096,
																																										12087,
																																										12431,
																																										new int[] {
																																												GOLD_CHARM,
																																												6010 },
																																										11,
																																										40,
																																										53), MACAW(
																																												92104,
																																												12071,
																																												12422,
																																												new int[] {
																																														GREEN_CHARM,
																																														13572 },
																																												78,
																																												41,
																																												72), EVIL_TURNIP(
																																														92112,
																																														12051,
																																														12442,
																																														new int[] {
																																																CRIMSON_CHARM,
																																																12153 },
																																														104,
																																														42,
																																														185), SPIRIT_COCKATRICE(
																																																92120,
																																																12095,
																																																12458,
																																																new int[] {
																																																		GREEN_CHARM,
																																																		12109 },
																																																88,
																																																43,
																																																(int) 75.2), SPIRIT_GUTHATRICE(
																																																		92128,
																																																		12097,
																																																		12458,
																																																		new int[] {
																																																				GREEN_CHARM,
																																																				12111 },
																																																		88,
																																																		43,
																																																		(int) 75.2), SPIRIT_SARATRICE(
																																																				92136,
																																																				12099,
																																																				12458,
																																																				new int[] {
																																																						GREEN_CHARM,
																																																						12113 },
																																																				88,
																																																				43,
																																																				(int) 75.2), SPIRIT_ZAMATRICE(
																																																						92144,
																																																						12101,
																																																						12458,
																																																						new int[] {
																																																								GREEN_CHARM,
																																																								12115 },
																																																						88,
																																																						43,
																																																						(int) 75.2), SPIRIT_PENGATRICE(
																																																								92152,
																																																								12103,
																																																								12458,
																																																								new int[] {
																																																										GREEN_CHARM,
																																																										12117 },
																																																								88,
																																																								43,
																																																								(int) 75.2), SPIRIT_CORAXATRICE(
																																																										92160,
																																																										12105,
																																																										12458,
																																																										new int[] {
																																																												GREEN_CHARM,
																																																												12119 },
																																																										88,
																																																										43,
																																																										(int) 75.2), SPIRIT_VULATRICE(
																																																												92168,
																																																												12107,
																																																												12458,
																																																												new int[] {
																																																														GREEN_CHARM,
																																																														12121 },
																																																												88,
																																																												43,
																																																												(int) 75.2), IRON_MINOTAUR(
																																																														92176,
																																																														12075,
																																																														12462,
																																																														new int[] {
																																																																BLUE_CHARM,
																																																																2351 },
																																																														125,
																																																														46,
																																																														405), PYRELORD(
																																																																92184,
																																																																12816,
																																																																12829,
																																																																new int[] {
																																																																		CRIMSON_CHARM,
																																																																		590 },
																																																																111,
																																																																46,
																																																																202), MAGPIE(
																																																																		92192,
																																																																		12041,
																																																																		12426,
																																																																		new int[] {
																																																																				GREEN_CHARM,
																																																																				1635 },
																																																																		88,
																																																																		47,
																																																																		83), BLOATED_LEECH(
																																																																				92200,
																																																																				12061,
																																																																				12444,
																																																																				new int[] {
																																																																						CRIMSON_CHARM,
																																																																						2132 },
																																																																				117,
																																																																				49,
																																																																				215), SPIRIT_TERRORBIRD(
																																																																						92208,
																																																																						12007,
																																																																						12441,
																																																																						new int[] {
																																																																								GOLD_CHARM,
																																																																								9978 },
																																																																						12,
																																																																						52,
																																																																						68), ABYSSAL_PARASITE(
																																																																								92216,
																																																																								12035,
																																																																								12454,
																																																																								new int[] {
																																																																										GREEN_CHARM,
																																																																										ABYSSAL_CHARM },
																																																																								106,
																																																																								54,
																																																																								95), SPIRIT_JELLY(
																																																																										92224,
																																																																										12027,
																																																																										12453,
																																																																										new int[] {
																																																																												BLUE_CHARM,
																																																																												1937 },
																																																																										151,
																																																																										55,
																																																																										484), IBIS(
																																																																												92240,
																																																																												12531,
																																																																												12424,
																																																																												new int[] {
																																																																														GREEN_CHARM,
																																																																														311 },
																																																																												109,
																																																																												56,
																																																																												99), STEEL_MINOTAUR(
																																																																														92232,
																																																																														12077,
																																																																														12463,
																																																																														new int[] {
																																																																																BLUE_CHARM,
																																																																																2353 },
																																																																														141,
																																																																														56,
																																																																														493), SPIRIT_GRAAHK(
																																																																																92248,
																																																																																12810,
																																																																																12835,
																																																																																new int[] {
																																																																																		BLUE_CHARM,
																																																																																		10099 },
																																																																																154,
																																																																																57,
																																																																																502), SPIRIT_KYATT(
																																																																																		93000,
																																																																																		12812,
																																																																																		12836,
																																																																																		new int[] {
																																																																																				BLUE_CHARM,
																																																																																				10103 },
																																																																																		153,
																																																																																		57,
																																																																																		502), SPIRIT_LARUPIA(
																																																																																				93008,
																																																																																				12784,
																																																																																				12840,
																																																																																				new int[] {
																																																																																						BLUE_CHARM,
																																																																																						10095 },
																																																																																				155,
																																																																																				57,
																																																																																				502), KHARAMTHULHU_OVERLORD(
																																																																																						93016,
																																																																																						12023,
																																																																																						12455,
																																																																																						new int[] {
																																																																																								BLUE_CHARM,
																																																																																								6667 },
																																																																																						144,
																																																																																						58,
																																																																																						510), SMOKE_DEVIL(
																																																																																								93024,
																																																																																								12085,
																																																																																								12468,
																																																																																								new int[] {
																																																																																										CRIMSON_CHARM,
																																																																																										9736 },
																																																																																								141,
																																																																																								61,
																																																																																								268), ABYSSAL_LURKER(
																																																																																										93032,
																																																																																										12037,
																																																																																										12427,
																																																																																										new int[] {
																																																																																												GREEN_CHARM,
																																																																																												ABYSSAL_CHARM },
																																																																																										119,
																																																																																										62,
																																																																																										110), SPIRIT_COBRA(
																																																																																												93040,
																																																																																												12015,
																																																																																												12436,
																																																																																												new int[] {
																																																																																														CRIMSON_CHARM,
																																																																																														6287 },
																																																																																												116,
																																																																																												63,
																																																																																												269), STRANGER_PLANT(
																																																																																														93048,
																																																																																														12045,
																																																																																														12467,
																																																																																														new int[] {
																																																																																																CRIMSON_CHARM,
																																																																																																8431 },
																																																																																														128,
																																																																																														64,
																																																																																														282), MITHRIL_MINOTAUR(
																																																																																																93056,
																																																																																																12079,
																																																																																																12464,
																																																																																																new int[] {
																																																																																																		BLUE_CHARM,
																																																																																																		2359 },
																																																																																																152,
																																																																																																66,
																																																																																																581), BARKER_TOAD(
																																																																																																		93064,
																																																																																																		12163,
																																																																																																		12452,
																																																																																																		new int[] {
																																																																																																				GOLD_CHARM,
																																																																																																				2150 },
																																																																																																		11,
																																																																																																		66,
																																																																																																		87), WAR_TORTOISE(
																																																																																																				93072,
																																																																																																				23031,
																																																																																																				12439,
																																																																																																				new int[] {
																																																																																																						GOLD_CHARM,
																																																																																																						7939 },
																																																																																																				1,
																																																																																																				67,
																																																																																																				59), BUNYIP(
																																																																																																						93080,
																																																																																																						12029,
																																																																																																						12438,
																																																																																																						new int[] {
																																																																																																								GREEN_CHARM,
																																																																																																								383 },
																																																																																																						110,
																																																																																																						68,
																																																																																																						120), FRUIT_BAT(
																																																																																																								93088,
																																																																																																								12033,
																																																																																																								12423,
																																																																																																								new int[] {
																																																																																																										GREEN_CHARM,
																																																																																																										1963 },
																																																																																																								130,
																																																																																																								69,
																																																																																																								121), RAVENOUS_LOCUST(
																																																																																																										93096,
																																																																																																										12820,
																																																																																																										12830,
																																																																																																										new int[] {
																																																																																																												CRIMSON_CHARM,
																																																																																																												1933 },
																																																																																																										79,
																																																																																																										70,
																																																																																																										132), ARCTIC_BEAR(
																																																																																																												93104,
																																																																																																												12057,
																																																																																																												12451,
																																																																																																												new int[] {
																																																																																																														GOLD_CHARM,
																																																																																																														10117 },
																																																																																																												14,
																																																																																																												71,
																																																																																																												93), PHOENIX(
																																																																																																														93112,
																																																																																																														14623,
																																																																																																														14622,
																																																																																																														new int[] {
																																																																																																																CRIMSON_CHARM,
																																																																																																																14616 },
																																																																																																														165,
																																																																																																														72,
																																																																																																														301), OBSIDIAN_GOLEM(
																																																																																																																93120,
																																																																																																																12792,
																																																																																																																12826,
																																																																																																																new int[] {
																																																																																																																		BLUE_CHARM,
																																																																																																																		OBSIDIAN_CHARM },
																																																																																																																195,
																																																																																																																73,
																																																																																																																642), GRANITE_LOBSTER(
																																																																																																																		93128,
																																																																																																																		12069,
																																																																																																																		12449,
																																																																																																																		new int[] {
																																																																																																																				CRIMSON_CHARM,
																																																																																																																				6979 },
																																																																																																																		166,
																																																																																																																		74,
																																																																																																																		326), PRAYING_MANTIS(
																																																																																																																				93136,
																																																																																																																				12011,
																																																																																																																				12450,
																																																																																																																				new int[] {
																																																																																																																						CRIMSON_CHARM,
																																																																																																																						2460 },
																																																																																																																				168,
																																																																																																																				75,
																																																																																																																				330), FORGE_REGENT(
																																																																																																																						93152,
																																																																																																																						12782,
																																																																																																																						12841,
																																																																																																																						new int[] {
																																																																																																																								GREEN_CHARM,
																																																																																																																								10020 },
																																																																																																																						141,
																																																																																																																						76,
																																																																																																																						134), ADAMANT_MINOTAUR(
																																																																																																																								93144,
																																																																																																																								12081,
																																																																																																																								12465,
																																																																																																																								new int[] {
																																																																																																																										BLUE_CHARM,
																																																																																																																										2361 },
																																																																																																																								144,
																																																																																																																								76,
																																																																																																																								669), TALON_BEAST(
																																																																																																																										93160,
																																																																																																																										12794,
																																																																																																																										12831,
																																																																																																																										new int[] {
																																																																																																																												CRIMSON_CHARM,
																																																																																																																												TALON_BEAST_CHARM },
																																																																																																																										174,
																																																																																																																										77,
																																																																																																																										1015), GIANT_ENT(
																																																																																																																												93168,
																																																																																																																												12013,
																																																																																																																												12457,
																																																																																																																												new int[] {
																																																																																																																														GREEN_CHARM,
																																																																																																																														5933 },
																																																																																																																												124,
																																																																																																																												78,
																																																																																																																												137), FIRE_TITAN(
																																																																																																																														93176,
																																																																																																																														12802,
																																																																																																																														12824,
																																																																																																																														new int[] {
																																																																																																																																BLUE_CHARM,
																																																																																																																																1442 },
																																																																																																																														198,
																																																																																																																														79,
																																																																																																																														695), MOSS_TITAN(
																																																																																																																																93184,
																																																																																																																																12804,
																																																																																																																																12824,
																																																																																																																																new int[] {
																																																																																																																																		BLUE_CHARM,
																																																																																																																																		1440 },
																																																																																																																																202,
																																																																																																																																79,
																																																																																																																																695), ICE_TITAN(
																																																																																																																																		93192,
																																																																																																																																		12806,
																																																																																																																																		12824,
																																																																																																																																		new int[] {
																																																																																																																																				BLUE_CHARM,
																																																																																																																																				1444 },
																																																																																																																																		198,
																																																																																																																																		79,
																																																																																																																																		695), HYDRA(
																																																																																																																																				93200,
																																																																																																																																				12025,
																																																																																																																																				12442,
																																																																																																																																				new int[] {
																																																																																																																																						GREEN_CHARM,
																																																																																																																																						571 },
																																																																																																																																				128,
																																																																																																																																				80,
																																																																																																																																				141), SPIRIT_DAGGANOTH(
																																																																																																																																						93208,
																																																																																																																																						12017,
																																																																																																																																						12456,
																																																																																																																																						new int[] {
																																																																																																																																								CRIMSON_CHARM,
																																																																																																																																								6155 },
																																																																																																																																						1,
																																																																																																																																						83,
																																																																																																																																						365), LAVA_TITAN(
																																																																																																																																								93216,
																																																																																																																																								12788,
																																																																																																																																								12837,
																																																																																																																																								new int[] {
																																																																																																																																										BLUE_CHARM,
																																																																																																																																										OBSIDIAN_CHARM },
																																																																																																																																								219,
																																																																																																																																								83,
																																																																																																																																								730), SWAMP_TITAN(
																																																																																																																																										93224,
																																																																																																																																										12776,
																																																																																																																																										12832,
																																																																																																																																										new int[] {
																																																																																																																																												CRIMSON_CHARM,
																																																																																																																																												10149 },
																																																																																																																																										150,
																																																																																																																																										85,
																																																																																																																																										374), RUNE_MINOTAUR(
																																																																																																																																												93232,
																																																																																																																																												12083,
																																																																																																																																												12466,
																																																																																																																																												new int[] {
																																																																																																																																														BLUE_CHARM,
																																																																																																																																														2363 },
																																																																																																																																												1,
																																																																																																																																												86,
																																																																																																																																												757), UNICORN_STALLION(
																																																																																																																																														93240,
																																																																																																																																														12039,
																																																																																																																																														12434,
																																																																																																																																														new int[] {
																																																																																																																																																GREEN_CHARM,
																																																																																																																																																237 },
																																																																																																																																														140,
																																																																																																																																														88,
																																																																																																																																														154), GEYSER_TITAN(
																																																																																																																																																93248,
																																																																																																																																																12786,
																																																																																																																																																12833,
																																																																																																																																																new int[] {
																																																																																																																																																		BLUE_CHARM,
																																																																																																																																																		1444 },
																																																																																																																																																222,
																																																																																																																																																89,
																																																																																																																																																783), WOLPERTINGER(
																																																																																																																																																		94000,
																																																																																																																																																		12089,
																																																																																																																																																		12437,
																																																																																																																																																		new int[] {
																																																																																																																																																				CRIMSON_CHARM,
																																																																																																																																																				3226 },
																																																																																																																																																		203,
																																																																																																																																																		92,
																																																																																																																																																		405), ABYSSAL_TITAN(
																																																																																																																																																				94008,
																																																																																																																																																				12796,
																																																																																																																																																				12827,
																																																																																																																																																				new int[] {
																																																																																																																																																						GREEN_CHARM,
																																																																																																																																																						ABYSSAL_CHARM },
																																																																																																																																																				113,
																																																																																																																																																				93,
																																																																																																																																																				163), IRON_TITAN(
																																																																																																																																																						94016,
																																																																																																																																																						12822,
																																																																																																																																																						12828,
																																																																																																																																																						new int[] {
																																																																																																																																																								CRIMSON_CHARM,
																																																																																																																																																								1115 },
																																																																																																																																																						198,
																																																																																																																																																						95,
																																																																																																																																																						418), PACK_YAK(
																																																																																																																																																								94024,
																																																																																																																																																								12093,
																																																																																																																																																								12435,
																																																																																																																																																								new int[] {
																																																																																																																																																										CRIMSON_CHARM,
																																																																																																																																																										10818 },
																																																																																																																																																								211,
																																																																																																																																																								96,
																																																																																																																																																								422), STEEL_TITAN(
																																																																																																																																																										94032,
																																																																																																																																																										12790,
																																																																																																																																																										12825,
																																																																																																																																																										new int[] {
																																																																																																																																																												CRIMSON_CHARM,
																																																																																																																																																												1119 },
																																																																																																																																																										178,
																																																																																																																																																										99,
																																																																																																																																																										435);

		private int buttonId, pouchId, scrollId, shardsRequired, levelRequired, receivedExp;
		private int[] requiredItems;

		private Data(int buttonId, int pouchId, int scrollId, int[] requiredItems, int shardsRequired,
				int levelRequired, int receivedExp) {
			this.buttonId = buttonId;
			this.pouchId = pouchId;
			this.scrollId = scrollId;
			this.requiredItems = requiredItems;
			this.shardsRequired = shardsRequired;
			this.levelRequired = levelRequired;
			this.receivedExp = receivedExp;
		}

		public static Data forId(int buttonId) {
			for (Data d : Data.values()) {
				if (d.getButtonId() == buttonId) {
					return d;
				}
			}
			return null;
		}

		public int getButtonId() {
			return buttonId;
		}

		public int getPouchId() {
			return pouchId;
		}

		public int getScrollId() {
			return scrollId;
		}

		public int[] getRequiredItems() {
			return requiredItems;
		}

		public int getShardsRequired() {
			return shardsRequired;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public int getReceivedExp() {
			return receivedExp;
		}
	}

	private static boolean isAble(final Data data, final Player player, final boolean scroll) {
		if (data == null)
			return false;
		if (player.playerLevel[24] >= data.getLevelRequired()) {
			if (scroll) {
				if (player.getItems().playerHasItem(data.getPouchId())) {
					return true;
				}
			} else {
				if (player.getItems().playerHasItem(POUCH_ID)) {
					if (player.getItems().playerHasItem(SHARD_ID, data.getShardsRequired())) {
						if (player.getItems().playerHasItem(data.getRequiredItems()[0])) {
							if (player.getItems().playerHasItem(data.getRequiredItems()[1])) {
								return true;
							} else {
								player.sendMessage(
										"You need " + player.getItems().getItemName(data.getRequiredItems()[1])
												+ " to create this pouch.");
								return false;
							}
						} else {
							player.sendMessage("You need a " + player.getItems().getItemName(data.getRequiredItems()[0])
									+ "to make this pouch.");
							return false;
						}
					} else {
						player.sendMessage(
								"You need at least " + data.getShardsRequired() + " shards to create this pouch.");
						return false;
					}
				} else {
					player.sendMessage("You dont have a pouch to create this item.");
					return false;
				}
			}
		} else {
			player.sendMessage("You need a summoning level of " + data.getLevelRequired() + " to create this pouch");
			return false;
		}
		return false;
	}

	public static void handleCraft(final Data data, final Player player, final int buttonId, final boolean scroll) {
		if (data == null)
			return;
		if (!isAble(data, player, scroll))
			return;
		player.getPA().removeAllWindows();
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!isAble(data, player, scroll))
					container.stop();
				if (isAble(data, player, scroll)) {
					player.playerSkilling[24] = true;
					player.getItems().deleteItem(POUCH_ID, 1);
					player.getItems().deleteItem(SHARD_ID, data.getShardsRequired());
					player.getItems().deleteItem(data.getRequiredItems()[0], 1);
					player.getItems().deleteItem(data.getRequiredItems()[1], 1);
					player.getPA().addSkillXP(data.getReceivedExp() * SkillHandler.XPRates.SUMMONING.getXPRate(), 24);
					player.getItems().addItem(data.getPouchId(), 1);
					player.startAnimation(725);
				}
			}

			@Override
			public void stop() {
				player.startAnimation(65535);
				player.playerSkilling[24] = false;
			}
		}, 3);
	}

	public static void handleButtons(Player player, int buttonId) {
		final Data data = Data.forId(buttonId);
		if (data == null)
			return;
		handleCraft(data, player, buttonId, false);
	}
}
// //PRAYING_MANTIS1(93136, 12011, 12160, 168, 2462, 75, 330),
// //PRAYING_MANTIS2(93136, 12011, 12160, 168, 2464, 75, 330),
// //PRAYING_MANTIS3(93136, 12011, 12160, 168, 2466, 75, 330),
// //PRAYING_MANTIS4(93136, 12011, 12160, 168, 2468, 75, 330),
// //PRAYING_MANTIS5(93136, 12011, 12160, 168, 2470, 75, 330), Might cause a
// dupe
// //PRAYING_MANTIS6(93136, 12011, 12160, 168, 2472, 75, 330),
// //PRAYING_MANTIS7(93136, 12011, 12160, 168, 2474, 75, 330),
// //PRAYING_MANTIS8(93136, 12011, 12160, 168, 2476, 75, 330),
