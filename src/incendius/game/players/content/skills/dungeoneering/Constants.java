package incendius.game.players.content.skills.dungeoneering;

import java.util.ArrayList;

import incendius.game.players.Player;

public class Constants {

	public static final int MAX_FLOORS = 80, MAX_LEVEL = 120, MAX_CRATES = 6, LOWEST_LEVEL = 1, SKILL_ID = 24,
			CURRENCY = 18201, STACKABLE_AMOUNT = 50;

	public static final int ROOM_SMALL = 0, ROOM_MEDIUM = 1, ROOM_LARGE = 2, ROOM_EXTRA_LARGE = 3;

	public static final int ITEM_NOVITE = 0, ITEM_BATHUS = 1, ITEM_MARMAROS = 2, ITEM_KRATONITE = 3, ITEM_FRACITITE = 4,
			ITEM_ZEPHYRIUM = 5, ITEM_ARGONITE = 6, ITEM_KATAGON = 7, ITEM_GOGONITE = 8, ITEM_PROMETHIUM = 9,
			ITEM_PRIMAL = 10;

	public static final int ITEM_DAGGER = 0, ITEM_WARHAMMER = 1, ITEM_RAPIER = 2, ITEM_LONGSWORD = 3,
			ITEM_BATTLEAXE = 4, ITEM_SPEAR = 5, ITEM_MAUL = 6, ITEM_2H_SWORD = 7, ITEM_FULL_HELM = 8,
			ITEM_PLATEBODY = 9, ITEM_CHAINBODY = 10, ITEM_PLATELEGS = 11, ITEM_PLATESKIRT = 12, ITEM_GAUNLETS = 13,
			ITEM_KITESHIELD = 14, ITEM_BOOTS = 15, ITEM_ARROWS = 16, ITEM_ARROWTIPS = 17, ITEM_HATCHET = 18,
			ITEM_PICKAXE = 19, ITEM_ORE = 20, ITEM_BAR = 21;

	public static final int NPC_FORGOTTEN_WARRIOR = 0, NPC_FORGOTTEN_MAGE = 1, NPC_FORGOTTEN_RANGR = 2, NPC_ZOMBIE = 3,
			NPC_DUNGEON_SOUDER = 4;

	public static final int START_LOCATIONS[][] = { { 3149, 5524 }, { 3142, 5494 } },
			MAIN_NPC_LOCATIONS[][][] = { { { 11226, 3142, 5515 } }, { { 11226, 3157, 5496 } } },
			OBJECT[][][] = { { { 17010, 3146, 5513, 0, 10 }, { 22721, 3140, 5526, 0, 10 } },
					{ { 17010, 3144, 5492, 0, 10 }, { 22721, 3157, 5491, 0, 10 } } },
			/** TODO add objects also **/
			CRATE_ROOM[][][] = {
					{ { 3144, 5511 }, { 3140, 5517 }, { 3141, 5518 }, { 3148, 5520 }, { 3142, 5523 }, { 3142, 5523 },
							{ 3141, 5523 }, { 3138, 5527 }, { 3138, 5528 }, { 3138, 5530 } },
					{ { 3139, 5491 }, { 3142, 5499 }, { 3143, 5499 }, { 3162, 5497 }, { 3161, 5493 }, { 3152, 5492 },
							{ 3149, 5487 }, { 3148, 5487 },
							{ 3148, 5487 } } }, /** Add sub crates also **/
			FOOD[] = { 18159, 18161, 18163, 18165, 18167, 18169, 18171, 18173, 18175, 18177 },
			ROOM_SIZE[] = { 0, 2, 1, 1, 2, 2, 1, 3, 0, 1, 0, 0, 2, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 2, 0, 1, 2, 1,
					1, 1, 1, 2, 2, 0, 1, 2, 1, 1, 1, 0, 0, 1, 1, 2, 0, 2, 1, 0, 0, 3 },
			ROOM_IDS[] = { 0, 1, 2, 3, 4, 5, 6, 7, 7, 1, 8, 3, 9, 6, 10, 6, 11, 7, 12, 12, 13, 14, 11, 15, 13, 8, 16,
					15, 2, 15, 17, 2, 18, 17, 19, 20, 19, 21, 22, 19, 18, 21, 16, 20, 23, 24, 25, 26, 27, 28, 26, 29,
					26, 9, 29, 30, 29, 30, 10, 31, 27, 32, 31, 33, 31, 34, 31, 35, 32, 36, 35, 36, 35, 33, 36, 37, 33,
					38, 37, 39, 38, 34, 39, 12, 39, 40, 12, 41, 40, 12, 41, 42, 41, 43, 41, 44, 42, 43, 44, 45, 44, 45,
					43, 23, 45, 46, 45, 47, 46, 48, 47, 49, 47, 50, 48, 51, 50, 52, 51, 49, 52, 49, 52, 53, 53 },
			NPC_LOCATION[][][] = { { { 3237, 5454 }, { 3244, 5449 }, { 3245, 5445 }, { 3239, 5451 } },
					{ { 3255, 5455 }, { 3256, 5470 }, { 3243, 5465 }, { 3258, 5451 } },
					{ { 3227, 5444 }, { 3207, 5444 }, { 3215, 5450 }, { 3215, 5442 } },
					{ { 3245, 5447 }, { 3240, 5450 }, { 3237, 5455 }, { 3248, 5448 } },
					{ { 3270, 5489 }, { 3288, 5496 }, { 3303, 5495 }, { 3280, 5489 } },
					{ { 3255, 5455 }, { 3256, 5470 }, { 3243, 5462 }, { 3252, 5455 } },
					{ { 3283, 5458 }, { 3280, 5452 }, { 3274, 5453 }, { 3269, 5446 } },
					{ { 3244, 5486 }, { 3252, 5493 }, { 3243, 5489 }, { 3251, 5488 } },
					{ { 3222, 5463 }, { 3222, 5467 }, { 3226, 5458 }, { 3222, 5472 } },
					{ { 3273, 5474 }, { 3278, 5475 }, { 3271, 5462 }, { 3280, 5475 } },
					{ { 3291, 5453 }, { 3292, 5456 }, { 3290, 5448 }, { 3290, 5490 } },
					{ { 3230, 5496 }, { 3234, 5498 }, { 3222, 5497 }, { 3232, 5494 } },
					{ { 3238, 5511 }, { 3235, 5526 }, { 3225, 5540 }, { 3222, 5523 } },
					{ { 3218, 5481 }, { 3224, 5484 }, { 3218, 5486 }, { 3220, 5484 } },
					{ { 3214, 5472 }, { 3208, 5469 }, { 3209, 5461 }, { 3212, 5458 } },
					{ { 3203, 5485 }, { 3207, 5489 }, { 3201, 5489 }, { 3204, 5494 } },
					{ { 3192, 5467 }, { 3195, 5461 }, { 3195, 5453 }, { 3193, 5446 } },
					{ { 3176, 5455 }, { 3178, 5450 }, { 3184, 5448 }, { 3187, 5455 } },
					{ { 3181, 5469 }, { 3182, 5479 }, { 3177, 5481 }, { 3174, 5477 } },
					{ { 3170, 5496 }, { 3169, 5492 }, { 3180, 5491 }, { 3185, 5493 } },
					{ { 3168, 5460 }, { 3166, 5463 }, { 3168, 5465 }, { 3167, 5468 } },
					{ { 3146, 5466 }, { 3146, 5474 }, { 3146, 5479 }, { 3160, 5484 } },
					{ { 3140, 5494 }, { 3144, 5492 }, { 3151, 5489 }, { 3159, 5498 } },
					{ { 3145, 5447 }, { 3148, 5451 }, { 3151, 5453 }, { 3152, 5456 } },
					{ { 3158, 5451 }, { 3161, 5450 }, { 3166, 5449 }, { 3167, 5446 } },
					{ { 3305, 5484 }, { 3311, 5481 }, { 3313, 5485 }, { 3299, 5484 } },
					{ { 3303, 5494 }, { 3293, 5496 }, { 3285, 5496 }, { 3270, 5489 } },
					{ { 3318, 5470 }, { 3322, 5471 }, { 3321, 5474 }, { 3323, 5477 } },
					{ { 3296, 5476 }, { 3286, 5483 }, { 3291, 5468 }, { 3287, 5466 } },
					{ { 3309, 5459 }, { 3304, 5451 }, { 3316, 5448 }, { 3316, 5454 } },
					{ { 3285, 5512 }, { 3297, 5519 }, { 3289, 5526 }, { 3291, 5529 } },
					{ { 3323, 5509 }, { 3314, 5509 }, { 3307, 5510 }, { 3301, 5508 } },
					{ { 3289, 5544 }, { 3281, 5557 }, { 3271, 5549 }, { 3276, 5546 } },
					{ { 3274, 5512 }, { 3277, 5518 }, { 3271, 5531 }, { 3278, 5535 } },
					{ { 3312, 5520 }, { 3300, 5530 }, { 3309, 5535 }, { 3321, 5548 } },
					{ { 3315, 5561 }, { 3298, 5562 }, { 3301, 5548 }, { 3299, 5541 } },
					{ { 3260, 5560 }, { 3258, 5557 }, { 3260, 5554 }, { 3257, 5551 } },
					{ { 3234, 5563 }, { 3238, 5556 }, { 3246, 5558 }, { 3243, 5548 } },
					{ { 3257, 5540 }, { 3257, 5530 }, { 3255, 5514 }, { 3247, 5519 } },
					{ { 3222, 5556 }, { 3210, 5552 }, { 3209, 5562 }, { 3218, 5547 } },
					{ { 3204, 5541 }, { 3205, 5535 }, { 3208, 5531 }, { 3205, 5529 } },
					{ { 3205, 5512 }, { 3207, 5516 }, { 3209, 5519 }, { 3215, 5518 } },
					{ { 3190, 5532 }, { 3194, 5524 }, { 3190, 5522 }, { 3189, 5525 } },
					{ { 3186, 5514 }, { 3188, 5511 }, { 3191, 5512 }, { 3193, 5510 } },
					{ { 3167, 5513 }, { 3172, 5512 }, { 3178, 5520 }, { 3179, 5528 } },
					{ { 3169, 5527 }, { 3160, 5521 }, { 3158, 5530 }, { 3164, 5531 } },
					{ { 3140, 5531 }, { 3141, 5526 }, { 3148, 5514 }, { 3143, 5516 } },
					{ { 3165, 5538 }, { 3161, 5539 }, { 3158, 5538 }, { 3155, 5539 } },
					{ { 3146, 5559 }, { 3153, 5559 }, { 3157, 5549 }, { 3147, 5545 } },
					{ { 3192, 5545 }, { 3184, 5542 }, { 3178, 5543 }, { 3174, 5541 } },
					{ { 3183, 5559 }, { 3187, 5556 }, { 3192, 5557 }, { 3192, 5562 } },
					{ { 3165, 5556 }, { 3169, 5557 }, { 3171, 5559 }, { 3172, 5556 } },
					{ { 3107, 5537 }, { 3101, 5532 }, { 3094, 5541 }, { 3091, 5533 } } },
			BOUND_ITEMS[][] = { { 15753, 16207 }, { 16273, 15936 }, { 16339, 16262 }, { 16383, 16024 },
					{ 16405, 16174 }, { 16647, 16196 }, { 16669, 15925 }, { 16691, 15914 }, { 16713, 16080 },
					{ 16889, 16127 }, { 16935, 16035 }, { 17019, 16116 }, { 17239, 16013 }, { 17341, 15808 },
					{ 16757, 15848 }, { 15755, 16208 }, { 16275, 15937 }, { 16341, 16263 }, { 16385, 16025 },
					{ 16407, 16175 }, { 16649, 16197 }, { 16671, 15926 }, { 16693, 15915 }, { 16715, 16081 },
					{ 16891, 16128 }, { 16937, 16036 }, { 17021, 16117 }, { 17241, 16014 }, { 17343, 15809 },
					{ 15757, 16209 }, { 16277, 15938 }, { 16343, 16264 }, { 16387, 16026 }, { 16409, 16176 },
					{ 16651, 16198 }, { 16673, 15927 }, { 16695, 15916 }, { 16717, 16082 }, { 16893, 16129 },
					{ 16939, 16037 }, { 17023, 16118 }, { 17243, 16015 }, { 17345, 15810 }, { 15759, 16210 },
					{ 16279, 15939 }, { 16345, 16265 }, { 16389, 16027 }, { 16411, 16177 }, { 16653, 16199 },
					{ 16675, 15928 }, { 16697, 15917 }, { 16719, 16083 }, { 16895, 16130 }, { 16941, 16038 },
					{ 17025, 16119 }, { 17245, 16016 }, { 17347, 15811 }, { 15761, 16211 }, { 16281, 15940 },
					{ 16347, 16266 }, { 16391, 16028 }, { 16413, 16178 }, { 16655, 16200 }, { 16677, 15929 },
					{ 16699, 15918 }, { 16721, 16084 }, { 16897, 16131 }, { 16943, 16039 }, { 17027, 16120 },
					{ 17247, 16017 }, { 17349, 15812 }, { 15763, 16212 }, { 16283, 15941 }, { 16349, 16267 },
					{ 16393, 16029 }, { 16415, 16179 }, { 16657, 16201 }, { 16679, 15930 }, { 16701, 15919 },
					{ 16723, 16085 }, { 16899, 16132 }, { 16945, 16040 }, { 17029, 16121 }, { 17249, 16018 },
					{ 17351, 15813 }, { 15765, 16213 }, { 16285, 15942 }, { 16351, 16268 }, { 16395, 16030 },
					{ 16417, 16180 }, { 16659, 16202 }, { 16681, 15931 }, { 16703, 15920 }, { 16725, 16086 },
					{ 16901, 16133 }, { 16947, 16041 }, { 17031, 16122 }, { 17251, 16019 }, { 17353, 15814 },
					{ 15767, 16214 }, { 16287, 15943 }, { 16353, 16269 }, { 16397, 16031 }, { 16419, 16181 },
					{ 16661, 16203 }, { 16683, 15932 }, { 16705, 15921 }, { 16727, 16087 }, { 16903, 16134 },
					{ 16949, 16042 }, { 17033, 16123 }, { 17253, 16020 }, { 17355, 15815 }, { 15769, 16215 },
					{ 16289, 15944 }, { 16355, 16270 }, { 16399, 16032 }, { 16421, 16182 }, { 16663, 16204 },
					{ 16685, 15933 }, { 16707, 15922 }, { 16729, 16088 }, { 16905, 16135 }, { 16951, 16043 },
					{ 17035, 16124 }, { 17255, 16021 }, { 17357, 15816 }, { 15771, 16216 }, { 16291, 15945 },
					{ 16357, 16271 }, { 16401, 16033 }, { 16423, 16183 }, { 16665, 16205 }, { 16687, 15934 },
					{ 16709, 15923 }, { 16731, 16089 }, { 16907, 16136 }, { 16953, 16044 }, { 17037, 16125 },
					{ 17257, 16022 }, { 17359, 15817 }, { 15773, 16217 }, { 16293, 15946 }, { 16359, 16272 },
					{ 16403, 16034 }, { 16425, 16184 }, { 16667, 16206 }, { 16689, 15935 }, { 16711, 15924 },
					{ 16733, 16090 }, { 16909, 16137 }, { 16955, 16045 }, { 17039, 16126 }, { 17259, 16023 },
					{ 17361, 15818 }, { 17792, 16103 } };

	public static ArrayList<Integer> openHeightLevels = new ArrayList<Integer>();

	public static final boolean inDungeoneeringLobby(Player p) {
		return p.absX >= 3036 && p.absX <= 3067 && p.absY >= 4958 && p.absY <= 4993;
	}
}