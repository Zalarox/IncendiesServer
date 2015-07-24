package main.game.players.content.minigames.impl;

public class CastleWarObjects {

	/*
	 * public static boolean handleObject(final Player c, final int id, final
	 * int x, final int y) { switch (id) { case 4385: if (!c.zammyTeam()) {
	 * c.sendMessage("You can't fix the enemies catapult."); return true; } if
	 * (!c.getItems().playerHasItem(4051, 1)) { c.sendMessage(
	 * "You need a toolbox to repair this catapult."); return true; }
	 * c.lastAction = System.currentTimeMillis() + 2400; c.startAnimation(894);
	 * c.getItems().deleteItem(4051, 1); c.sendMessage(
	 * "You attempt to repair the catapult...");
	 * Server.getScheduler().schedule(new Task(4) {
	 * 
	 * @Override public void execute() { if (Misc.random(2) == 1) {
	 * c.sendMessage("You successfully repaired the catapult!");
	 * CastleWars.zammyCatapult = true; CastleWars.updateCwObects("catapult",
	 * 2); } else { c.sendMessage("You failed at repairing the catapult."); }
	 * c.getPA().resetAnimation(); this.stop(); } }); return true;
	 * 
	 * case 4386: if (!c.saraTeam()) { c.sendMessage(
	 * "You can't fix the enemies catapult."); return true; } if
	 * (!c.getItems().playerHasItem(4051, 1)) { c.sendMessage(
	 * "You need a toolbox to repair this catapult."); return true; }
	 * c.lastAction = System.currentTimeMillis() + 2400; c.startAnimation(894);
	 * c.getItems().deleteItem(4051, 1); c.sendMessage(
	 * "You attempt to repair the catapult..."); c.playerIsFletching = true;
	 * Server.getScheduler().schedule(new Task(4) {
	 * 
	 * @Override public void execute() { if (Misc.random(2) == 1) {
	 * c.sendMessage("You successfully repaired the catapult!");
	 * CastleWars.saraCatapult = true; CastleWars.updateCwObects("catapult", 1);
	 * } else { c.sendMessage("You failed at repairing the catapult."); }
	 * c.getPA().resetAnimation(); this.stop(); } }); return true;
	 * 
	 * case 4411: // Stepping stones + 1 ladder if (x == 2377 && y == 3088) { if
	 * (c.absX == 2377 && c.absY == 3089) { c.getPA().walkTo(0, -1); } if
	 * (c.absX == 2377 && c.absY == 3087) { c.getPA().walkTo(0, 1); } } if (x ==
	 * 2377 && y == 3087) { if (c.absX == 2377 && c.absY == 3088) {
	 * c.getPA().walkTo(0, -1); } if (c.absX == 2377 && c.absY == 3086) {
	 * c.getPA().walkTo(0, 1); } } if (x == 2377 && y == 3086) { if (c.absX ==
	 * 2377 && c.absY == 3087) { c.getPA().walkTo(0, -1); } if (c.absX == 2377
	 * && c.absY == 3085) { c.getPA().walkTo(0, 1); } } if (x == 2377 && y ==
	 * 3085) { if (c.absX == 2377 && c.absY == 3086) { c.getPA().walkTo(0, -1);
	 * } if (c.absX == 2378 && c.absY == 3085) { c.getPA().walkTo(-1, 0); } } if
	 * (x == 2378 && y == 3085) { if (c.absX == 2377 && c.absY == 3085) {
	 * c.getPA().walkTo(1, 0); } if (c.absX == 2378 && c.absY == 3084) {
	 * c.getPA().walkTo(0, 1); } } if (x == 2378 && y == 3084) { if (c.absX ==
	 * 2378 && c.absY == 3085) { c.getPA().walkTo(0, -1); } if (c.absX == 2378
	 * && c.absY == 3083) { c.getPA().walkTo(0, 1); } } if (x == 2420 && y ==
	 * 3123) { if (c.absX == 2420 && c.absY == 3122) { c.getPA().walkTo(0, 1); }
	 * if (c.absX == 2419 && c.absY == 3123) { c.getPA().walkTo(1, 0); } } if (x
	 * == 2419 && y == 3123) { if (c.absX == 2420 && c.absY == 3123) {
	 * c.getPA().walkTo(-1, 0); } if (c.absX == 2419 && c.absY == 3124) {
	 * c.getPA().walkTo(0, -1); } } if (x == 2419 && y == 3124) { if (c.absX ==
	 * 2419 && c.absY == 3125) { c.getPA().walkTo(0, -1); } if (c.absX == 2419
	 * && c.absY == 3123) { c.getPA().walkTo(0, 1); } } if (x == 2419 && y ==
	 * 3125) { if (c.absX == 2419 && c.absY == 3124) { c.getPA().walkTo(0, 1); }
	 * if (c.absX == 2418 && c.absY == 3125) { c.getPA().walkTo(1, 0); } } if (x
	 * == 2418 && y == 3125) { if (c.absX == 2419 && c.absY == 3125) {
	 * c.getPA().walkTo(-1, 0); } if (c.absX == 2418 && c.absY == 3126) {
	 * c.getPA().walkTo(0, -1); } } if (x == 2421 && y == 3073 && c.heightLevel
	 * == 1) { c.getPA().movePlayer(c.getX(), c.getY(), 0); } return true; case
	 * 4448: // collapse tunnel if (x >= 2390 && x <= 2393 && y >= 9500 && y <=
	 * 9503 && CastleWars.saraRock1 || x >= 2399 && x <= 2402 && y >= 9511 && y
	 * <= 9514 && CastleWars.saraRock2 || x >= 2408 && x <= 2411 && y >= 9502 &&
	 * y <= 9505 && CastleWars.zammyRock1 || x >= 2400 && x <= 2403 && y >= 9493
	 * && y <= 9496 && CastleWars.zammyRock2) { c.sendMessage(
	 * "This passage is already collapsed."); return true; }
	 * c.playerSkilling[Constants.MINING] = true; c.sendMessage(
	 * "You attempt to collapse the passage...");
	 * //c.startAnimation(Mining.getAnimation(c)); final int task = c.getTask();
	 * Server.getScheduler().schedule(new Task(1) { int emote = 15;
	 * 
	 * @Override public void execute() { if (!c.checkTask(task)) { stop();
	 * c.getPA().resetAnimation(); c.getPA().resetPlayerSkillVariables();
	 * return; } if (x >= 2390 && x <= 2393 && y >= 9500 && y <= 9503 &&
	 * CastleWars.saraRock1 || x >= 2399 && x <= 2402 && y >= 9511 && y <= 9514
	 * && CastleWars.saraRock2 || x >= 2408 && x <= 2411 && y >= 9502 && y <=
	 * 9505 && CastleWars.zammyRock1 || x >= 2400 && x <= 2403 && y >= 9493 && y
	 * <= 9496 && CastleWars.zammyRock2) { c.sendMessage(
	 * "This passage is already collapsed."); this.stop();
	 * c.getPA().resetAnimation(); c.getPA().resetPlayerSkillVariables();
	 * return; } if (Misc.random(5) == 1) { if (x >= 2390 && x <= 2393 && y >=
	 * 9500 && y <= 9503) { c.getPA().hitPlayers(2391, 2392, 9501, 9502, 1);
	 * CastleWars.saraRock1 = true; CastleWars.updateCwObects("rock", 1); } if
	 * (x >= 2399 && x <= 2402 && y >= 9511 && y <= 9514) {
	 * c.getPA().hitPlayers(2400, 2401, 9512, 9513, 1); CastleWars.saraRock2 =
	 * true; CastleWars.updateCwObects("rock", 1); } if (x >= 2408 && x <= 2411
	 * && y >= 9502 && y <= 9505) { c.getPA().hitPlayers(2409, 2410, 9503, 9504,
	 * 1); CastleWars.zammyRock1 = true; CastleWars.updateCwObects("rock", 2); }
	 * if (x >= 2400 && x <= 2403 && y >= 9493 && y <= 9496) {
	 * c.getPA().hitPlayers(2401, 2402, 9494, 9495, 1); CastleWars.zammyRock2 =
	 * true; CastleWars.updateCwObects("rock", 2); } c.sendMessage(
	 * "You successfully filled the passage with rocks."); stop();
	 * c.getPA().resetAnimation(); c.getPA().resetPlayerSkillVariables(); } else
	 * { emote--; if (emote < 1) { //c.startAnimation(Mining.getAnimation(c));
	 * emote = 15; } } } }); return true;
	 * 
	 * case 4437: // mine through tunnel
	 * 
	 * if (x == 2391 && y == 9501 && !CastleWars.saraRock1 || x == 2400 && y ==
	 * 9512 && !CastleWars.saraRock2 || x == 2409 && y == 9503 &&
	 * !CastleWars.zammyRock1 || x == 2401 && y == 9494 &&
	 * !CastleWars.zammyRock2) { c.sendMessage(
	 * "This rock has already been mined."); return true; }
	 * c.playerSkilling[Constants.MINING] = true; c.sendMessage(
	 * "You attempt to mine the rock...");
	 * //c.startAnimation(Mining.getAnimation(c)); final int task2 =
	 * c.getTask(); Server.getScheduler().schedule(new Task(1) { int emote = 15;
	 * 
	 * @Override public void execute() { if (!c.checkTask(task2)) { stop();
	 * c.getPA().resetAnimation(); c.getPA().resetPlayerSkillVariables();
	 * return; } if (x == 2391 && y == 9501 && !CastleWars.saraRock1 || x ==
	 * 2400 && y == 9512 && !CastleWars.saraRock2 || x == 2409 && y == 9503 &&
	 * !CastleWars.zammyRock1 || x == 2401 && y == 9494 &&
	 * !CastleWars.zammyRock2) { c.sendMessage(
	 * "This rock has already been mined."); c.getPA().resetAnimation();
	 * c.getPA().resetPlayerSkillVariables(); this.stop(); return; } if
	 * (Misc.random(5) == 1) { if (x == 2391 && y == 9501) {
	 * CastleWars.saraRock1 = false; CastleWars.updateCwObects("rock", 1); } if
	 * (x == 2400 && y == 9512) { CastleWars.saraRock2 = false;
	 * CastleWars.updateCwObects("rock", 1); } if (x == 2409 && y == 9503) {
	 * CastleWars.zammyRock1 = false; CastleWars.updateCwObects("rock", 2); } if
	 * (x == 2401 && y == 9494) { CastleWars.zammyRock2 = false;
	 * CastleWars.updateCwObects("rock", 2); } c.sendMessage(
	 * "You successfully mine through the rocks."); c.getPA().resetAnimation();
	 * c.getPA().resetPlayerSkillVariables(); this.stop(); } else { emote--; if
	 * (emote < 1) { //c.startAnimation(Mining.getAnimation(c)); emote = 15; } }
	 * } }); return true;
	 * 
	 * case 1757: if (x == 2400 && y == 9508) { c.getPA().movePlayer(2400, 3107,
	 * 0); } if (x == 2399 && y == 9499) { c.getPA().movePlayer(2399, 3100, 0);
	 * } if (x == 2430 && y == 9482) { c.getPA().movePlayer(2430, 3081, 0); } if
	 * (x == 2369 && y == 9525) { c.getPA().movePlayer(2369, 3126, 0); } return
	 * true;
	 * 
	 * case 1568: if (x == 2400 && y == 3108) { c.getPA().movePlayer(2400, 9507,
	 * 0); } if (x == 2399 && y == 3099) { c.getPA().movePlayer(2399, 9500, 0);
	 * } return true;
	 * 
	 * case 6280: if (c.playerEquipment[Player.playerWeapon] == 4037 ||
	 * c.playerEquipment[Player.playerWeapon] == 4039) { c.sendMessage(
	 * "You are not allowed in the spawn point with the flag."); return true; }
	 * if (!c.saraTeam()) { c.sendMessage(
	 * "You are not allowed in the other teams spawn point."); return true; }
	 * c.getPA().movePlayer(2429, 3075, 2); return true;
	 * 
	 * case 4471: if (c.playerEquipment[Player.playerWeapon] == 4037 ||
	 * c.playerEquipment[Player.playerWeapon] == 4039) { c.sendMessage(
	 * "You are not allowed in the spawn point with the flag."); return true; }
	 * if (!c.saraTeam()) { c.sendMessage(
	 * "You are not allowed in the other teams spawn point."); return true; }
	 * c.getPA().movePlayer(2429, 3075, 1); return true;
	 * 
	 * case 6281: if (c.playerEquipment[Player.playerWeapon] == 4037 ||
	 * c.playerEquipment[Player.playerWeapon] == 4039) { c.sendMessage(
	 * "You are not allowed in the spawn point with the flag."); return true; }
	 * if (!c.zammyTeam()) { c.sendMessage(
	 * "You are not allowed in the other teams spawn point."); return true; }
	 * c.getPA().movePlayer(2370, 3132, 2); return true;
	 * 
	 * case 4472: if (c.playerEquipment[Player.playerWeapon] == 4037 ||
	 * c.playerEquipment[Player.playerWeapon] == 4039) { c.sendMessage(
	 * "You are not allowed in the spawn point with the flag."); return true; }
	 * if (!c.zammyTeam()) { c.sendMessage(
	 * "You are not allowed in the other teams spawn point."); return true; }
	 * c.getPA().movePlayer(2370, 3132, 1); return true;
	 * 
	 * case 4469: if (!c.saraTeam()) { c.sendMessage(
	 * "You are not allowed in the other teams spawn point."); return true; } if
	 * (c.playerEquipment[Player.playerWeapon] == 4037 ||
	 * c.playerEquipment[Player.playerWeapon] == 4039) { c.sendMessage(
	 * "You are not allowed in the spawn point with the flag."); return true; }
	 * if (x == 2426) { if (c.getY() == 3080) { c.getPA().movePlayer(2426, 3081,
	 * c.heightLevel); } else if (c.getY() == 3081) { c.getPA().movePlayer(2426,
	 * 3080, c.heightLevel); } } else if (x == 2422) { if (c.getX() == 2422) {
	 * c.getPA().movePlayer(2423, 3076, c.heightLevel); } else if (c.getX() ==
	 * 2423) { c.getPA().movePlayer(2422, 3076, c.heightLevel); } } return true;
	 * 
	 * case 4470: if (!c.zammyTeam()) { c.sendMessage(
	 * "You are not allowed in the other teams spawn point."); return true; } if
	 * (c.playerEquipment[Player.playerWeapon] == 4037 ||
	 * c.playerEquipment[Player.playerWeapon] == 4039) { c.sendMessage(
	 * "You are not allowed in the spawn point with the flag."); return true; }
	 * if (x == 2373 && y == 3126) { if (c.getY() == 3126) {
	 * c.getPA().movePlayer(2373, 3127, 1); } else if (c.getY() == 3127) {
	 * c.getPA().movePlayer(2373, 3126, 1); } } else if (x == 2377 && y == 3131)
	 * { if (c.getX() == 2376) { c.getPA().movePlayer(2377, 3131, 1); } else if
	 * (c.getX() == 2377) { c.getPA().movePlayer(2376, 3131, 1); } } return
	 * true;
	 * 
	 * case 4417: if (x == 2428 && y == 3081 && c.heightLevel == 1 && c.absX ==
	 * 2427 && c.absY == 3081) { c.getPA().movePlayer(2430, 3080, 2); } if (x ==
	 * 2425 && y == 3074 && c.heightLevel == 2 && c.absX == 2425 && c.absY ==
	 * 3077) { c.getPA().movePlayer(2426, 3074, 3); /*CollisionMap.setFlag(0,
	 * 2426, 3074, 0); //upstairs CollisionMap.setFlag(0, 2426, 3075, 0);
	 * CollisionMap.setFlag(0, 2426, 3076, 0); CollisionMap.setFlag(0, 2426,
	 * 3073, 0); CollisionMap.setFlag(0, 2429, 3075, 0); CollisionMap.setFlag(0,
	 * 2429, 3073, 0); CollisionMap.setFlag(0, 2429, 3076, 0);
	 */
}
/*
 * if (x == 2419 && y == 3078 && c.heightLevel == 0 && c.absX == 2419 && c.absY
 * == 3077) { c.getPA().movePlayer(2420, 3080, 1); } return true;
 * 
 * case 4415: if (x == 2419 && y == 3080 && c.heightLevel == 1 && c.absX == 2420
 * && c.absY == 3080) { c.getPA().movePlayer(2419, 3077, 0); } if (x == 2430 &&
 * y == 3081 && c.heightLevel == 2 && c.absX == 2430 && c.absY == 3080) {
 * c.getPA().movePlayer(2427, 3081, 1); } if (x == 2425 && y == 3074 &&
 * c.heightLevel == 3 && c.absX == 2426 && c.absY == 3074) {
 * c.getPA().movePlayer(2425, 3077, 2); } if (x == 2374 && y == 3133 &&
 * c.heightLevel == 3 && c.absX == 2373 && c.absY == 3133) {
 * c.getPA().movePlayer(2374, 3130, 2); } if (x == 2369 && y == 3126 &&
 * c.heightLevel == 2 && c.absX == 2369 && c.absY == 3127) {
 * c.getPA().movePlayer(2372, 3126, 1); } if (x == 2380 && y == 3127 &&
 * c.heightLevel == 1 && c.absX == 2379 && c.absY == 3127) {
 * c.getPA().movePlayer(2380, 3130, 0); } return true;
 * 
 * case 4419: if (x == 2417 && y == 3074 && c.heightLevel == 0) { if (c.absX >=
 * 2416 && c.absX <= 2417 && c.absY >= 3074 && c.absY <= 3076) {
 * c.getPA().movePlayer(2417, 3077, 0); } else if (c.absX >= 2417 && c.absX <=
 * 2418 && c.absY >= 3077 && c.absY <= 3078) { c.getPA().movePlayer(2416, 3074,
 * 0); } } return true;
 * 
 * case 4911: if (x == 2421 && y == 3073 && c.heightLevel == 1) {
 * c.getPA().movePlayer(2421, 3074, 0); } if (x == 2378 && y == 3134 &&
 * c.heightLevel == 1) { c.getPA().movePlayer(2378, 3133, 0); } return true;
 * 
 * case 4912: if (x == 2430 && y == 3082 && c.heightLevel == 0) {
 * c.getPA().movePlayer(2430, 9481, 0); } if (x == 2369 && y == 3125 &&
 * c.heightLevel == 0) { c.getPA().movePlayer(2369, 9526, 0); } return true;
 * 
 * case 4418: if (x == 2380 && y == 3127 && c.heightLevel == 0 && c.absX == 2380
 * && c.absY == 3130) { c.getPA().movePlayer(2379, 3127, 1); } if (x == 2369 &&
 * y == 3126 && c.heightLevel == 1 && c.absX == 2372 && c.absY == 3126) {
 * c.getPA().movePlayer(2369, 3127, 2); } if (x == 2374 && y == 3131 &&
 * c.heightLevel == 2 && c.absX == 2374 && c.absY == 3130) {
 * c.getPA().movePlayer(2373, 3133, 3); /*CollisionMap.setFlag(0, 2373, 3133,
 * 0); CollisionMap.setFlag(0, 2373, 3134, 0); CollisionMap.setFlag(0, 2373,
 * 3132, 0); //Upstairs CollisionMap.setFlag(0, 2373, 3131, 0);
 * CollisionMap.setFlag(0, 2370, 3131, 0); CollisionMap.setFlag(0, 2370, 3132,
 * 0); CollisionMap.setFlag(0, 2370, 3134, 0);
 */
/*
 * } return true;
 * 
 * case 4420: if (x == 2382 && y == 3131 && c.heightLevel == 0) { if (c.absX ==
 * 2383 && c.absY == 3133) { c.getPA().movePlayer(2382, 3130, 0); } else if
 * (c.absX == 2382 && c.absY == 3130) { c.getPA().movePlayer(2383, 3133, 0); } }
 * return true;
 * 
 * case 4465: // open sara sidedoor if (!c.saraTeam()) { c.lastAction =
 * System.currentTimeMillis() + 1800; c.startAnimation(881); c.sendMessage(
 * "You attempt to picklock the door..."); Server.getScheduler().schedule(new
 * Task(3) {
 * 
 * @Override public void execute() { if (Misc.random(2) == 1) { c.sendMessage(
 * "You are successful and the door opens."); CastleWars.saraSideDoor = true;
 * CastleWars.updateCwObects("door", 1); } else { c.sendMessage("But you fail."
 * ); } this.stop(); } }); } else { CastleWars.saraSideDoor = true;
 * CastleWars.updateCwObects("door", 1); } return true;
 * 
 * case 4466: // close sara sidedoor CastleWars.saraSideDoor = false;
 * CastleWars.updateCwObects("door", 1); return true; case 4467: // open zammy
 * sidedoor if (!c.zammyTeam()) { c.lastAction = System.currentTimeMillis() +
 * 1800; c.startAnimation(881); c.sendMessage(
 * "You attempt to picklock the door..."); Server.getScheduler().schedule(new
 * Task(3) {
 * 
 * @Override public void execute() { if (Misc.random(2) == 1) {
 * CastleWars.zammySideDoor = true; c.sendMessage(
 * "You are successful at picklocking the door.");
 * CastleWars.updateCwObects("door", 2); } else { c.sendMessage("But you fail."
 * ); } this.stop(); } }); } else { CastleWars.zammySideDoor = true;
 * CastleWars.updateCwObects("door", 2); } return true;
 * 
 * case 4468: // close zammy sidedoor CastleWars.zammySideDoor = false;
 * CastleWars.updateCwObects("door", 2); return true;
 * 
 * case 4902: // Sara flag if (CastleWars.SaraFlagTaken ||
 * CastleWars.SflagDropped) { return true; } if (c.saraTeam()) { if
 * (c.playerEquipment[Player.playerWeapon] == 4039) { CastleWars.returnFlag(c,
 * 4039, true); } else { c.sendMessage("You can't steal your team's flag!"); } }
 * else if (c.zammyTeam()) { CastleWars.takeFlag(c, id, x, y, c.heightLevel); }
 * return true;
 * 
 * case 4900: // Sara flag ground object if (!CastleWars.SaraFlagTaken &&
 * !CastleWars.SflagDropped) { return true; } if (c.saraTeam() &&
 * c.inSaraBase()) { CastleWars.returnFlag(c, 4037, false); } else if
 * (c.saraTeam() || c.zammyTeam()) { CastleWars.takeFlag(c, id, x, y,
 * c.heightLevel); } return true;
 * 
 * case 4377: // Sara empty flag if (c.playerEquipment[Player.playerWeapon] ==
 * 4039 && c.saraTeam()) { // CastleWars.returnFlag(c, true); c.sendMessage(
 * "Your flag must be returned in order to score."); } return true;
 * 
 * case 4903: // Zammy flag if (CastleWars.ZammyFlagTaken ||
 * CastleWars.ZflagDropped) { return true; } if (c.zammyTeam()) { if
 * (c.playerEquipment[Player.playerWeapon] == 4037) { CastleWars.returnFlag(c,
 * 4037, true); } else { c.sendMessage("You can't steal your team's flag!"); } }
 * else if (c.saraTeam()) { CastleWars.takeFlag(c, id, x, y, c.heightLevel); }
 * return true;
 * 
 * case 4901: // Zammy flag ground object if (!CastleWars.ZammyFlagTaken &&
 * !CastleWars.ZflagDropped) { return true; } if (c.zammyTeam() &&
 * c.inZammyBase()) { CastleWars.returnFlag(c, 4039, false); } else if
 * (c.saraTeam() || c.zammyTeam()) { CastleWars.takeFlag(c, id, x, y,
 * c.heightLevel); } return true;
 * 
 * case 4378: // Zammy empty flag if (c.playerEquipment[Player.playerWeapon] ==
 * 4037 && c.zammyTeam()) { // CastleWars.returnFlag(c, true); c.sendMessage(
 * "Your flag must be returned in order to score."); return true; }
 * 
 * case 4381: final int[][] sDamageArea = { { 2417, 3103 }, { 2409, 3093 }, {
 * 2402, 3086 }, { 2397, 3079 }, { 2420, 3096 } }; if (!c.zammyTeam()) {
 * c.sendMessage("Your team can't use this catapult!"); return true; } if
 * (System.currentTimeMillis() - CastleWars.ZCatapult < 20000) { c.sendMessage(
 * "The catapult must cool down before firing again."); return true; } if
 * (!c.getItems().playerHasItem(4043, 1)) { c.sendMessage(
 * "You don't have anything to load the catapault with!"); return true; } final
 * int p = Misc.random(4); PlayerAssistant.createStillGfx(287,
 * sDamageArea[p][0], sDamageArea[p][1], 0, 1);
 * c.getPA().hitPlayers(sDamageArea[p][0] - 2, sDamageArea[p][0] + 2,
 * sDamageArea[p][1] - 2, sDamageArea[p][1] + 2, 2);
 * c.getItems().deleteItem(4043, 1); c.sendMessage("You fire the catapult!");
 * CastleWars.ZCatapult = System.currentTimeMillis(); //
 * c.getPA().showInterface(11169); return true;
 * 
 * case 4382: final int[][] zDamageArea = { { 2381, 3104 }, { 2385, 3110 }, {
 * 2394, 3119 }, { 2380, 3111 }, { 2400, 3125 } }; if (!c.saraTeam()) {
 * c.sendMessage("Your team can't use this catapult!"); return true; } if
 * (System.currentTimeMillis() - CastleWars.SCatapult < 20000) { c.sendMessage(
 * "The catapult must cool down before firing again."); return true; } if
 * (!c.getItems().playerHasItem(4043, 1)) { c.sendMessage(
 * "You don't have anything to load the catapault with!"); return true; } final
 * int i = Misc.random(4); PlayerAssistant.createStillGfx(287,
 * zDamageArea[i][0], zDamageArea[i][1], 0, 1);
 * c.getPA().hitPlayers(zDamageArea[i][0] - 2, zDamageArea[i][0] + 2,
 * zDamageArea[i][1] - 2, zDamageArea[i][1] + 2, 20);
 * c.getItems().deleteItem(4043, 1); c.sendMessage("You fire the catapult!");
 * CastleWars.SCatapult = System.currentTimeMillis(); //
 * c.getPA().showInterface(11169); return true;
 * 
 * case 4406: case 4407: case 4389: case 4390: CastleWars.leaveCastleWars(c);
 * return true; case 4387: CastleWars.checkTeam(c, 1); return true; case 4388:
 * CastleWars.checkTeam(c, 2); return true; case 4408: CastleWars.checkTeam(c,
 * 3); return true; case 4458: if (c.inCwSafe()) { CastleWars.takeFromStall(c,
 * 4049); } return true; case 4459: CastleWars.takeFromStall(c, 4051); return
 * true; case 4460: CastleWars.takeFromStall(c, 4043); return true; case 4461:
 * CastleWars.takeFromStall(c, 4053); return true; case 4462:
 * CastleWars.takeFromStall(c, 4047); return true; case 4463:
 * CastleWars.takeFromStall(c, 4045); return true; case 4464:
 * CastleWars.takeFromStall(c, 1265); return true; case 4423: case 4424: if
 * (!c.saraTeam()) { c.sendMessage("Your team cannot open this door!"); return
 * true; } CastleWars.saraMainDoor = true; CastleWars.updateCwObects("door", 1);
 * return true; case 4425: case 4426: CastleWars.saraMainDoor = false;
 * CastleWars.updateCwObects("door", 1); return true; case 4427: case 4428: if
 * (!c.zammyTeam()) { c.sendMessage("Your team cannot open this door!"); return
 * true; } CastleWars.zammyMainDoor = true; CastleWars.updateCwObects("door",
 * 2); return true; case 4429: case 4430: CastleWars.zammyMainDoor = false;
 * CastleWars.updateCwObects("door", 2); return true; default: return false; } }
 * 
 * public static void castleWarsFlags(final Player c, final int team) { if (team
 * == 1) { if (CastleWars.SflagDropped) { c.getPA().object(4900,
 * CastleWars.saraFlagX, CastleWars.saraFlagY, CastleWars.saraFlagZ, -3, 10); }
 * else { c.getPA().object(-1, CastleWars.saraFlagX, CastleWars.saraFlagY,
 * CastleWars.saraFlagZ, -3, 10); } if (CastleWars.SaraFlagTaken ||
 * CastleWars.SflagDropped) { c.getPA().object(4377, 2429, 3074, 3, -3, 10); }
 * else { c.getPA().object(4902, 2429, 3074, 3, -3, 10); } } else if (team == 2)
 * { if (CastleWars.ZflagDropped) { c.getPA().object(4901,
 * CastleWars.zammyFlagX, CastleWars.zammyFlagY, CastleWars.zammyFlagZ, -1, 10);
 * } else { c.getPA().object(-1, CastleWars.zammyFlagX, CastleWars.zammyFlagY,
 * CastleWars.zammyFlagZ, -1, 10); } if (CastleWars.ZammyFlagTaken ||
 * CastleWars.ZflagDropped) { c.getPA().object(4378, 2370, 3133, 3, -1, 10); }
 * else { c.getPA().object(4903, 2370, 3133, 3, -1, 10); } } }
 * 
 * public static void castleWarsRocks(final Player c, final int team) { if
 * (!c.inCwUnderground()) { return; } if (team == 1) { if
 * (!CastleWars.saraRock1) { c.getPA().object(4439, 2391, 9501, 0, 0, 10);
 * /*CollisionMap.setFlag(0, 4439, 2391, 0); CollisionMap.setFlag(0, 4440, 2391,
 * 0); CollisionMap.setFlag(0, 4439, 2392, 0); CollisionMap.setFlag(0, 4440,
 * 2392, 0);
 */
/*
 * } else { c.getPA().object(4437, 2391, 9501, 0, 0, 10);
 * /*CollisionMap.setFlag(0, 4437, 2391, 0); CollisionMap.setFlag(0, 4438, 2391,
 * 0); CollisionMap.setFlag(0, 4437, 2392, 0); CollisionMap.setFlag(0, 4438,
 * 2392, 0);
 */
/*
 * } if (!CastleWars.saraRock2) { c.getPA().object(4439, 2400, 9512, 0, 0, 10);
 * /*CollisionMap.setFlag(0, 4439, 2400, 0); CollisionMap.setFlag(0, 4440, 2401,
 * 0); CollisionMap.setFlag(0, 4439, 2401, 0); CollisionMap.setFlag(0, 4440,
 * 2400, 0);
 */
/*
 * } else { c.getPA().object(4437, 2400, 9512, 0, 0, 10);
 * /*CollisionMap.setFlag(0, 4437, 2400, 0); CollisionMap.setFlag(0, 4438, 2401,
 * 0); CollisionMap.setFlag(0, 4437, 2401, 0); CollisionMap.setFlag(0, 4438,
 * 2400, 0);
 */
/*
 * } } else if (team == 2) { if (!CastleWars.zammyRock1) {
 * c.getPA().object(4439, 2409, 9503, 0, 0, 10); /*CollisionMap.setFlag(0, 4439,
 * 2409, 0); CollisionMap.setFlag(0, 4440, 2410, 0); CollisionMap.setFlag(0,
 * 4439, 2410, 0); CollisionMap.setFlag(0, 4440, 2409, 0);
 */
/*
 * } else { c.getPA().object(4437, 2409, 9503, 0, 0, 10);
 * /*CollisionMap.setFlag(0, 4437, 2409, 0); CollisionMap.setFlag(0, 4438, 2410,
 * 0); CollisionMap.setFlag(0, 4437, 2410, 0); CollisionMap.setFlag(0, 4438,
 * 2409, 0);
 */
/*
 * } if (!CastleWars.zammyRock2) { c.getPA().object(4439, 2401, 9494, 0, 0, 10);
 * /*CollisionMap.setFlag(1, 4439, 2401, 0); CollisionMap.setFlag(1, 4440, 2401,
 * 0); CollisionMap.setFlag(1, 4439, 2402, 0); CollisionMap.setFlag(1, 4440,
 * 2402, 0);
 */
/*
 * } else { c.getPA().object(4437, 2401, 9494, 0, 0, 10);
 * /*CollisionMap.setFlag(1, 4437, 2401, 0); CollisionMap.setFlag(1, 4438, 2401,
 * 0); CollisionMap.setFlag(1, 4437, 2402, 0); CollisionMap.setFlag(1, 4438,
 * 2402, 0);
 */
/*
 * } } }
 * 
 * public static void castleWarsCatapults(final Player c, final int team) { if
 * (c.inCwUnderground()) { return; } if (team == 1) { if
 * (!CastleWars.saraCatapult) { c.getPA().object(4386, 2413, 3088, 0, 0, 10); }
 * else { c.getPA().object(4382, 2413, 3088, 0, 0, 10); } } else if (team == 2)
 * { if (!CastleWars.zammyCatapult) { c.getPA().object(4385, 2384, 3117, 0, -2,
 * 10); } else { c.getPA().object(4381, 2384, 3117, 0, -2, 10); } } }
 * 
 * public static void castleWarsDoors(final Player c, final int team) { if
 * (c.inCwUnderground()) { return; } if (team == 1) { // 1 = north, 2 = east, 3
 * = south, 4/0 = west if (CastleWars.saraSideDoor) { c.getPA().object(4466,
 * 2414, 3073, 0, 1, 0); c.getPA().object(-1, 2415, 3073, 0, 0, 0); } else {
 * c.getPA().object(4465, 2415, 3073, 0, 0, 0); c.getPA().object(-1, 2414, 3073,
 * 0); } if (CastleWars.saraMainDoor) { c.getPA().object(4425, 2426, 3087, 0, 0,
 * 0); c.getPA().object(4426, 2427, 3087, 0, 2, 0); c.getPA().object(-1, 2426,
 * 3088, 0, 3, 0); c.getPA().object(-1, 2427, 3088, 0, 3, 0); } else {
 * c.getPA().object(4423, 2426, 3088, 0, 3, 0); c.getPA().object(4424, 2427,
 * 3088, 0, 3, 0); c.getPA().object(-1, 2426, 3087, 0); c.getPA().object(-1,
 * 2427, 3087, 0); } } else if (team == 2) { if (CastleWars.zammySideDoor) {
 * c.getPA().object(4468, 2385, 3134, 0, 3, 0); c.getPA().object(-1, 2384, 3134,
 * 0, 2, 0); } else { c.getPA().object(4467, 2384, 3134, 0, 2, 0);
 * c.getPA().object(-1, 2385, 3134, 0); } if (CastleWars.zammyMainDoor) {
 * c.getPA().object(4429, 2373, 3120, 0, 2, 0); c.getPA().object(4430, 2372,
 * 3120, 0, 0, 0); c.getPA().object(-1, 2373, 3119, 0, 1, 0);
 * c.getPA().object(-1, 2372, 3119, 0, 2, 0); } else { c.getPA().object(4427,
 * 2373, 3119, 0, 1, 0); c.getPA().object(4428, 2372, 3119, 0, 1, 0);
 * c.getPA().object(-1, 2373, 3120, 0); c.getPA().object(-1, 2372, 3120, 0); } }
 * }
 * 
 * public void reloadCastleWarsObjects() { for (int d = 0; d <
 * Constants.MAX_PLAYERS; d++) { if (PlayerHandler.players[d] != null &&
 * PlayerHandler.players[d].isActive) { final Player p =
 * PlayerHandler.players[d]; if (p.inCwGame()) { CastleWars.updateCwObects(p); }
 * } } }
 */
