package incendius.game.players.content.skills.dungeoneering;

import incendius.game.players.Player;

/**
 * 
 * @author TheLife/Alex
 *
 */
public class Portal {

	public static final int[][] objectCoords = { { 3254, 5451 }, { 3250, 5448 }, { 3241, 5445 }, { 3233, 5445 },
			{ 3259, 5446 }, { 3265, 5491 }, { 3260, 5491 }, { 3266, 5446 }, { 3241, 5469 }, { 3233, 5470 },
			{ 3235, 5457 }, { 3229, 5454 }, { 3280, 5460 }, { 3273, 5460 }, { 3283, 5448 }, { 3287, 5448 },
			{ 3244, 5495 }, { 3239, 5498 }, { 3232, 5501 }, { 3238, 5507 }, { 3218, 5497 }, { 3222, 5488 },
			{ 3218, 5478 }, { 3215, 5475 }, { 3224, 5479 }, { 3222, 5474 }, { 3208, 5471 }, { 3210, 5477 },
			{ 3214, 5456 }, { 3212, 5452 }, { 3204, 5445 }, { 3197, 5448 }, { 3189, 5444 }, { 3187, 5460 },
			{ 3192, 5472 }, { 3186, 5472 }, { 3185, 5478 }, { 3191, 5482 }, { 3171, 5473 }, { 3167, 5471 },
			{ 3171, 5478 }, { 3167, 5478 }, { 3168, 5456 }, { 3178, 5460 }, { 3191, 5495 }, { 3194, 5490 },
			{ 3141, 5480 }, { 3142, 5489 }, { 3142, 5462 }, { 3154, 5462 }, { 3143, 5443 }, { 3155, 5449 },
			{ 3307, 5496 }, { 3317, 5496 }, { 3318, 5481 }, { 3322, 5480 }, { 3299, 5484 }, { 3303, 5477 },
			{ 3286, 5470 }, { 3285, 5474 }, { 3290, 5463 }, { 3302, 5469 }, { 3296, 5455 }, { 3299, 5450 },
			{ 3280, 5501 }, { 3285, 5508 }, { 3300, 5514 }, { 3297, 5510 }, { 3289, 5533 }, { 3288, 5536 },
			{ 3285, 5527 }, { 3282, 5531 }, { 3325, 5518 }, { 3323, 5531 }, { 3299, 5533 }, { 3297, 5536 },
			{ 3321, 5554 }, { 3315, 5552 }, { 3291, 5555 }, { 3285, 5556 }, { 3266, 5552 }, { 3262, 5552 },
			{ 3256, 5561 }, { 3253, 5561 }, { 3249, 5546 }, { 3252, 5543 }, { 3261, 5536 }, { 3268, 5534 },
			{ 3243, 5526 }, { 3241, 5529 }, { 3230, 5547 }, { 3226, 5553 }, { 3206, 5553 }, { 3204, 5546 },
			{ 3211, 5533 }, { 3214, 5533 }, { 3208, 5527 }, { 3211, 5523 }, { 3201, 5531 }, { 3197, 5529 },
			{ 3202, 5515 }, { 3196, 5512 }, { 3190, 5515 }, { 3190, 5519 }, { 3185, 5518 }, { 3181, 5517 },
			{ 3187, 5531 }, { 3182, 5530 }, { 3169, 5510 }, { 3159, 5501 }, { 3165, 5515 }, { 3173, 5530 },
			{ 3156, 5523 }, { 3152, 5520 }, { 3148, 5533 }, { 3153, 5537 }, { 3143, 5535 }, { 3147, 5541 },
			{ 3168, 5541 }, { 3171, 5542 }, { 3190, 5549 }, { 3190, 5554 }, { 3180, 5557 }, { 3174, 5558 },
			{ 3162, 5557 }, { 3158, 5561 }, { 3166, 5553 }, { 3162, 5545 }, { 3142, 5545 }, { 3326, 5469 }, };

	public static final int[][] teleportCoords = { { 3250, 5448 }, { 3254, 5451 }, { 3233, 5445 }, { 3241, 5445 },
			{ 3265, 5491 }, { 3259, 5446 }, { 3266, 5446 }, { 3260, 5491 }, { 3233, 5470 }, { 3241, 5469 },
			{ 3229, 5454 }, { 3235, 5457 }, { 3273, 5460 }, { 3280, 5460 }, { 3287, 5448 }, { 3283, 5448 },
			{ 3239, 5498 }, { 3244, 5495 }, { 3238, 5507 }, { 3232, 5501 }, { 3222, 5488 }, { 3218, 5497 },
			{ 3215, 5475 }, { 3218, 5478 }, { 3222, 5474 }, { 3224, 5479 }, { 3210, 5477 }, { 3208, 5471 },
			{ 3212, 5452 }, { 3214, 5456 }, { 3197, 5448 }, { 3204, 5445 }, { 3187, 5460 }, { 3189, 5444 },
			{ 3186, 5472 }, { 3192, 5472 }, { 3191, 5482 }, { 3185, 5478 }, { 3167, 5471 }, { 3171, 5473 },
			{ 3167, 5478 }, { 3171, 5478 }, { 3178, 5460 }, { 3168, 5456 }, { 3194, 5490 }, { 3191, 5495 },
			{ 3142, 5489 }, { 3141, 5480 }, { 3154, 5462 }, { 3142, 5462 }, { 3155, 5449 }, { 3143, 5443 },
			{ 3317, 5496 }, { 3307, 5496 }, { 3322, 5480 }, { 3318, 5481 }, { 3303, 5477 }, { 3299, 5484 },
			{ 3285, 5474 }, { 3286, 5470 }, { 3302, 5469 }, { 3290, 5463 }, { 3299, 5450 }, { 3296, 5455 },
			{ 3285, 5508 }, { 3280, 5501 }, { 3297, 5510 }, { 3300, 5514 }, { 3288, 5536 }, { 3289, 5533 },
			{ 3282, 5531 }, { 3285, 5527 }, { 3323, 5531 }, { 3325, 5518 }, { 3297, 5536 }, { 3299, 5533 },
			{ 3315, 5552 }, { 3321, 5554 }, { 3285, 5556 }, { 3291, 5555 }, { 3262, 5552 }, { 3266, 5552 },
			{ 3253, 5561 }, { 3256, 5561 }, { 3252, 5543 }, { 3249, 5546 }, { 3268, 5534 }, { 3261, 5536 },
			{ 3241, 5529 }, { 3243, 5526 }, { 3226, 5553 }, { 3230, 5547 }, { 3204, 5546 }, { 3206, 5553 },
			{ 3214, 5533 }, { 3211, 5533 }, { 3211, 5523 }, { 3208, 5527 }, { 3197, 5529 }, { 3201, 5531 },
			{ 3196, 5512 }, { 3202, 5515 }, { 3190, 5519 }, { 3190, 5515 }, { 3181, 5517 }, { 3185, 5518 },
			{ 3182, 5530 }, { 3187, 5531 }, { 3159, 5501 }, { 3169, 5510 }, { 3173, 5530 }, { 3165, 5515 },
			{ 3152, 5520 }, { 3156, 5523 }, { 3153, 5537 }, { 3148, 5533 }, { 3147, 5541 }, { 3143, 5535 },
			{ 3171, 5542 }, { 3168, 5541 }, { 3190, 5554 }, { 3190, 5549 }, { 3174, 5558 }, { 3180, 5557 },
			{ 3158, 5561 }, { 3162, 5557 }, { 3162, 5545 }, { 3166, 5553 }, { 3115, 5528 }, { 3115, 5528 }, };

	public static void handle(Player p, int objectX, int objectY) {
		if (p == null)
			return;
		if (p.party == null)
			return;
		if (p.party.floor == null)
			return;
		for (int i = 0; i < objectCoords.length; i++) {
			if (objectX == objectCoords[i][0] && objectY == objectCoords[i][1]) {
				// if(p.party.floor.floorAccessability.contains((p.party.floor.accessedPortals[i])))
				// {
				p.getPA().movePlayer(teleportCoords[i][0], teleportCoords[i][1], p.heightLevel);
				p.sendMessage("You enter the portal.");
				/*
				 * if(!p.party.floor.npcViaRoomSpawned[Constants.ROOM_IDS[i]]) {
				 * int r = Misc.random(1)+3; for(int i2 = 0; i2 < r; i2++) {
				 * for(int i3 = 0; i3 < teleportCoords.length; i3++) {
				 * if(teleportCoords[i3][0] == teleportCoords[i][0] &&
				 * teleportCoords[i3][1] == teleportCoords[i][1]) { int id =
				 * Forgotten_Mage.getRandom(); new
				 * Npc(Constants.NPC_LOCATION[Constants.ROOM_IDS[i3]][i2][0],
				 * Constants.NPC_LOCATION[Constants.ROOM_IDS[i3]][i2][1],
				 * p.party.floor.heightLevel,
				 * Forgotten_Mage.getConstitution(id),
				 * Forgotten_Mage.getMaxHit(id), Forgotten_Mage.getAttack(id),
				 * Forgotten_Mage.getDefence(id),
				 * Forgotten_Mage.getRandom()).spawn();
				 * //p.sendMessage(Constants.NPC_LOCATION[Constants.ROOM_IDS[i3]
				 * ][i2][0]+", "
				 * +Constants.NPC_LOCATION[Constants.ROOM_IDS[i3]][i2][1]); } }
				 * } p.party.floor.npcViaRoomSpawned[Constants.ROOM_IDS[i]] =
				 * true; }
				 */
				/*
				 * } else { p.sendMessage("This portal cannot be entered."); }
				 */
				return;
			}
		}
	}

}
