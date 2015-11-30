package incendius.game.players.content.minigames.impl;

public class CastleWars {

	/*
	 * public static int saradominScore, zamorakScore, waitTimer, gameTimer,
	 * SaraMainGateHP, ZammyMainGateHP, saraBarricades, zammyBarricades,
	 * saraFlagX, saraFlagY, saraFlagZ, zammyFlagX, zammyFlagY, zammyFlagZ,
	 * saraFlagHolder, zammyFlagHolder, game = Misc .random(100);
	 * 
	 * public static boolean ZammyFlagTaken, zammySideDoor, zammyRock1,
	 * zammyRock2, zammyCatapult, ZflagDropped, SaraFlagTaken, saraSideDoor,
	 * saraRock1, saraRock2, saraCatapult, SflagDropped, saraMainDoor,
	 * zammyMainDoor, offerSara, offerZammy, gameStarted;
	 * 
	 * public static long ZCatapult, SCatapult;
	 */

	/**
	 * Wait for game start
	 */
	/*
	 * public static void startNewWait() { waitTimer = 100; resetVariables();
	 * Server.getScheduler().schedule(new Task(2) {
	 * 
	 * @Override public void execute() { if (waitTimer >= 0) { waitTimer --; }
	 * if (waitTimer == 0) { if (inZammyWaitCount() < 1 || inSaraWaitCount() <
	 * 1) { waitTimer = 60; } else { this.stop(); } } }
	 * 
	 * @Override public void stop() { startGame(); waitTimer = -1; } }); }
	 */
	/**
	 * Start the game
	 */
	/*
	 * public static void startNewGame() { gameTimer = 2000; resetVariables();
	 * gameStarted = true; Server.getScheduler().schedule(new Task(1) {
	 * 
	 * @Override public void execute() { if (gameTimer >= 0) { gameTimer --; }
	 * if (gameTimer == 0) { this.stop(); } }
	 * 
	 * @Override public void stop() { endGame(); gameTimer = -1; } }); }
	 * 
	 * public static void checkTeam(final Player c, final int team) { if
	 * (c.playerEquipment[Constants.HAT] > 0 ||
	 * c.playerEquipment[Constants.CAPE] > 0) { c.sendMessage(
	 * "You may not wear capes or helmets inside of castle wars."); return; }
	 * switch (team) { case 1: if (inSaraWaitCount() > inZammyWaitCount()) {
	 * c.sendMessage("This team is currently full."); } else { joinWait(c, 1); }
	 * break; case 2: if (inZammyWaitCount() > inSaraWaitCount()) {
	 * c.sendMessage("This team is currently full."); } else { joinWait(c, 2); }
	 * break; case 3: if (inZammyWaitCount() > inSaraWaitCount()) {
	 * c.sendMessage("You have been added to the Saradomin team."); joinWait(c,
	 * 1); } else if (inSaraWaitCount() > inZammyWaitCount()) { c.sendMessage(
	 * "You have been added to the Zamorak team."); joinWait(c, 2); } else if
	 * (inZammyWaitCount() == inSaraWaitCount()) { if (Misc.random(1) == 1) {
	 * c.sendMessage("You have been added to the Saradomin team."); joinWait(c,
	 * 1); } else { c.sendMessage("You have been added to the Zamorak team.");
	 * joinWait(c, 2); } } break; } }
	 * 
	 * public static void joinWait(final Player c, final int team) { switch
	 * (team) { case 1: c.getPA().movePlayer(2377 + Misc.random(10), 9485 +
	 * Misc.random(5), 0); c.getItems().wearItem(4041, 1, Constants.CAPE);
	 * c.getItems().wearItem(4513, 1, Constants.HAT); break; case 2:
	 * c.getPA().movePlayer(2419 + Misc.random(5), 9519 + Misc.random(7), 0);
	 * c.getItems().wearItem(4042, 1, Constants.CAPE);
	 * c.getItems().wearItem(4515, 1, Constants.HAT); break; } }
	 * 
	 * public static void offerPlace() { for (int d = 0; d <
	 * Constants.MAX_PLAYERS; d++) { if (PlayerHandler.players[d] != null &&
	 * PlayerHandler.players[d].isActive) { final Player c =
	 * PlayerHandler.players[d]; if (c.inSaraWait() && offerSara) {
	 * c.getDH().sendOption2("Take open Saradomin spot", "Don't take the spot");
	 * c.dialogueAction = 1234; } if (c.inZammyWait() && offerZammy) {
	 * c.getDH().sendOption2("Take open Saradomin spot", "Don't take the spot");
	 * c.dialogueAction = 1234; } } } }
	 * 
	 * public static void leaveCastleWars(final Player c) { if ((c.saraTeam() &&
	 * inSaraTeamCount() < 2 || c.zammyTeam() && inZammyTeamCount() < 2) &&
	 * gameTimer > 0) { endGame(); return; } offerSara = c.saraTeam() ? true :
	 * false; offerZammy = c.zammyTeam() ? true : false; createArrow(c, 0, 0);
	 * c.getPA().createArrow(0, 0, 0, 0); c.getItems().wearItem(-1, 0,
	 * Constants.CAPE); c.getItems().wearItem(-1, 0, Constants.HAT); if
	 * (c.playerEquipment[Player.playerWeapon] == 4037 ||
	 * c.playerEquipment[Player.playerWeapon] == 4039) {
	 * c.getItems().wearItem(-1, 0, Player.playerWeapon); } final int[]
	 * deleteItems = { 4049, 4051, 4043, 4053, 4047, 4045, 1265 }; for (final
	 * int delete : deleteItems) { c.getItems().deleteItem(delete,
	 * Integer.MAX_VALUE); } c.getPA().movePlayer(Constants.CASTLE_WARS_X +
	 * Misc.random(4), Constants.CASTLE_WARS_Y + Misc.random(4), 0);
	 * c.getPA().requestUpdates(); c.getPA().sendFrame126("", 6570);
	 * c.getPA().walkableInterface(-1); }
	 * 
	 * public static void startGame() { startNewGame(); for (int d = 0; d <
	 * Constants.MAX_PLAYERS; d++) { if (PlayerHandler.players[d] != null &&
	 * PlayerHandler.players[d].isActive) { final Player c =
	 * PlayerHandler.players[d]; c.lastGame = game; if (c.inSaraWait()) {
	 * c.getPA().walkableInterface(-1); c.getPA().movePlayer(2424 +
	 * Misc.random(5), 3075 + Misc.random(4), 1); } if (c.inZammyWait()) {
	 * c.getPA().walkableInterface(-1); c.getPA().movePlayer(2370 +
	 * Misc.random(5), 3128 + Misc.random(4), 1); } } } }
	 * 
	 * public static void endGame() { gameTimer = 0; gameStarted = false; for
	 * (int d = 0; d < Constants.MAX_PLAYERS; d++) { if
	 * (PlayerHandler.players[d] != null && PlayerHandler.players[d].isActive) {
	 * final Player c = PlayerHandler.players[d]; c.sendMessage(
	 * "[@red@Castle Wars@bla@] @blu@The game has ended, a new one starts in 2 minutes!"
	 * ); if (c.inCwGame()) { c.getItems().addItem( 4067, saradominScore >
	 * zamorakScore ? c.saraTeam() ? 20 : 0 : saradominScore < zamorakScore ? c
	 * .saraTeam() ? 0 : 20 : 10); leaveCastleWars(c); } } } startNewWait();
	 * game++; }
	 * 
	 * public static int inZammyWaitCount() { int count = 0; for (int i = 0; i <
	 * PlayerHandler.players.length; i++) { if (PlayerHandler.players[i] != null
	 * && !PlayerHandler.players[i].disconnected) { if
	 * (PlayerHandler.players[i].inZammyWait()) { count++; } } } return count; }
	 * 
	 * public static int inSaraWaitCount() { int count = 0; for (int i = 0; i <
	 * PlayerHandler.players.length; i++) { if (PlayerHandler.players[i] != null
	 * && !PlayerHandler.players[i].disconnected) { if
	 * (PlayerHandler.players[i].inSaraWait()) { count++; } } } return count; }
	 * 
	 * public static int inCastleWarsCount() { int count = 0; for (int i = 0; i
	 * < PlayerHandler.players.length; i++) { if (PlayerHandler.players[i] !=
	 * null && !PlayerHandler.players[i].disconnected) { if
	 * (PlayerHandler.players[i].inCwGame()) { count++; } } } return count; }
	 * 
	 * public static int inZammyTeamCount() { int count = 0; for (int i = 0; i <
	 * PlayerHandler.players.length; i++) { if (PlayerHandler.players[i] != null
	 * && !PlayerHandler.players[i].disconnected) { if
	 * (PlayerHandler.players[i].inCwGame() &&
	 * PlayerHandler.players[i].zammyTeam()) { count++; } } } return count; }
	 * 
	 * public static int inSaraTeamCount() { int count = 0; for (int i = 0; i <
	 * PlayerHandler.players.length; i++) { if (PlayerHandler.players[i] != null
	 * && !PlayerHandler.players[i].disconnected) { if
	 * (PlayerHandler.players[i].inCwGame() &&
	 * PlayerHandler.players[i].saraTeam()) { count++; } } } return count; }
	 * 
	 * public static void dropFlag(final Player c) { if
	 * (c.playerEquipment[Player.playerWeapon] == 4037) {
	 * c.getItems().wearItem(-1, 0, Player.playerWeapon); saraFlagX = c.absX;
	 * saraFlagY = c.absY; saraFlagZ = c.heightLevel; SflagDropped = true;
	 * SaraFlagTaken = false; saraFlagHolder = -1; updateCwObects("flag", 1); }
	 * if (c.playerEquipment[Player.playerWeapon] == 4039) {
	 * c.getItems().wearItem(-1, 0, Player.playerWeapon); zammyFlagX = c.absX;
	 * zammyFlagY = c.absY; zammyFlagZ = c.heightLevel; ZflagDropped = true;
	 * ZammyFlagTaken = false; zammyFlagHolder = -1; updateCwObects("flag", 2);
	 * } updateHintForEveryone(); }
	 * 
	 * public static void takeFlag(final Player c, final int id, final int x,
	 * final int y, final int z) { if (c.inCwSafe()) { c.sendMessage(
	 * "You must be out of the safe zone to take the flag."); return; } if
	 * (c.playerEquipment[Player.playerWeapon] == 4037 ||
	 * c.playerEquipment[Player.playerWeapon] == 4039) { c.sendMessage(
	 * "You must drop your flag before picking up another one."); return; } int
	 * space = 0; space += c.playerEquipment[Player.playerWeapon] > 0 ? 1 : 0;
	 * space += c.playerEquipment[Player.playerShield] > 0 ? 1 : 0; if
	 * (c.getItems().freeSlots() < space) { c.sendMessage(
	 * "You don't have enough inventory space to do that."); return; } if (id ==
	 * 4900 || id == 4902) { SaraFlagTaken = true; if (id == 4900) {
	 * SflagDropped = false; }
	 * c.getItems().addItem(c.playerEquipment[Player.playerWeapon],
	 * c.playerEquipmentN[Player.playerWeapon]);
	 * c.getItems().addItem(c.playerEquipment[Player.playerShield], 1);
	 * saraFlagHolder = c.playerId; c.getItems().wearItem(-1, 0, 5);
	 * c.getItems().wearItem(4037, 1, 3); updateCwObects("flag", 1); } if (id ==
	 * 4901 || id == 4903) { ZammyFlagTaken = true; if (id == 4901) {
	 * ZflagDropped = false; }
	 * c.getItems().addItem(c.playerEquipment[Player.playerWeapon],
	 * c.playerEquipmentN[Player.playerWeapon]);
	 * c.getItems().addItem(c.playerEquipment[Player.playerShield], 1);
	 * zammyFlagHolder = c.playerId; c.getItems().wearItem(-1, 0, 5);
	 * c.getItems().wearItem(4039, 1, 3); updateCwObects("flag", 2); }
	 * updateHintForEveryone(); }
	 * 
	 * public static void returnFlag(final Player c, final int flag, final
	 * boolean score) { if (flag == 4037) { if (score) { zamorakScore += 1;
	 * c.sendMessage("You score a point for Zamorak!");
	 * c.getItems().wearItem(-1, 0, 3); } SaraFlagTaken = false; SflagDropped =
	 * false; saraFlagHolder = -1; updateCwObects("flag", 1); } if (flag ==
	 * 4039) { if (score) { saradominScore += 1; c.sendMessage(
	 * "You score a point for Saradomin!"); c.getItems().wearItem(-1, 0, 3); }
	 * ZammyFlagTaken = false; ZflagDropped = false; zammyFlagHolder = -1;
	 * updateCwObects("flag", 2); } updateHintForEveryone(); }
	 * 
	 * public static void resetVariables() { // Saradomin variables
	 * saradominScore = 0; SaraMainGateHP = 100; saraBarricades = 0; saraFlagX =
	 * 0; saraFlagY = 0; saraFlagZ = 0; SaraFlagTaken = false; SflagDropped =
	 * false; saraMainDoor = false; saraSideDoor = false; saraRock1 = true;
	 * saraRock2 = true; saraCatapult = true; SCatapult = 0; saraFlagHolder =
	 * -1; // Zamorak variables zamorakScore = 0; ZammyMainGateHP = 100;
	 * zammyBarricades = 0; zammyFlagX = 0; zammyFlagY = 0; zammyFlagZ = 0;
	 * ZammyFlagTaken = false; ZflagDropped = false; zammyMainDoor = false;
	 * zammySideDoor = false; zammyRock1 = true; zammyRock2 = true;
	 * zammyCatapult = true; ZCatapult = 0; zammyFlagHolder = -1; // Barricades
	 * for (int j = 0; j < NPCHandler.npcs.length; j++) { if (NPCHandler.npcs[j]
	 * != null && !NPCHandler.npcs[j].isDead) { if (NPCHandler.npcs[j].npcType
	 * >= 1532 && NPCHandler.npcs[j].npcType <= 1535) { NPCHandler.npcs[j].absX
	 * = 0; NPCHandler.npcs[j].absY = 0; NPCHandler.npcs[j] = null; } } } }
	 * 
	 * public static void createArrow(Player c, final int type, final int id) {
	 * if (c.getOutStream() == null) { return; }
	 * c.getOutStream().createFrame(254); // The packet ID
	 * c.getOutStream().writeByte(type); // 1=NPC, 10=Player
	 * c.getOutStream().writeWord(id); // NPC/Player ID
	 * c.getOutStream().write3Byte(0); // Junk return; }
	 * 
	 * public static void updateHintForEveryone() { for (int d = 0; d <
	 * Constants.MAX_PLAYERS; d++) { if (PlayerHandler.players[d] != null &&
	 * PlayerHandler.players[d].isActive) { final Player t =
	 * PlayerHandler.players[d]; if (t.inCwGame()) { createArrow(t, 0, 0);
	 * t.getPA().createArrow(0, 0, 0, 0); if (saraFlagHolder < 0 &&
	 * zammyFlagHolder < 0 && !SflagDropped && !ZflagDropped) { return; } if
	 * (saraFlagHolder != t.playerId && SaraFlagTaken && t.saraTeam()) {
	 * createArrow(t, 10, saraFlagHolder); } if (zammyFlagHolder != t.playerId
	 * && ZammyFlagTaken && t.zammyTeam()) { createArrow(t, 10,
	 * zammyFlagHolder); } if (SflagDropped && t.saraTeam()) {
	 * t.getPA().createArrow(saraFlagX, saraFlagY, 200, 2); } if (ZflagDropped
	 * && t.zammyTeam()) { t.getPA().createArrow(zammyFlagX, zammyFlagY, 200,
	 * 2); } } } } }
	 * 
	 * public static void updateCwObects(final Player c) { if (c.inCwGame()) {
	 * CastleWarObjects.castleWarsFlags(c, 1);
	 * CastleWarObjects.castleWarsFlags(c, 2);
	 * CastleWarObjects.castleWarsRocks(c, 1);
	 * CastleWarObjects.castleWarsRocks(c, 2);
	 * CastleWarObjects.castleWarsCatapults(c, 1);
	 * CastleWarObjects.castleWarsCatapults(c, 2);
	 * CastleWarObjects.castleWarsDoors(c, 1);
	 * CastleWarObjects.castleWarsDoors(c, 2); } }
	 * 
	 * public static void updateCwObects(final String type, final int team) {
	 * for (int d = 0; d < Constants.MAX_PLAYERS; d++) { if
	 * (PlayerHandler.players[d] != null && PlayerHandler.players[d].isActive) {
	 * final Player t = PlayerHandler.players[d]; if (t.inCwGame()) { if (type
	 * == "flag") { CastleWarObjects.castleWarsFlags(t, team); } if (type ==
	 * "rock") { CastleWarObjects.castleWarsRocks(t, team); } if (type ==
	 * "catapult") { CastleWarObjects.castleWarsCatapults(t, team); } if (type
	 * == "door") { CastleWarObjects.castleWarsDoors(t, team); } } } } }
	 * 
	 * public static void takeFromStall(final Player c, final int item) { if
	 * (c.getItems().addItem(item, 1)) { c.startAnimation(832); c.getItems();
	 * c.sendMessage("You take a " + c.getItems().getItemName(item) + "."); } }
	 * 
	 * public static void updateCastleWarsScreen(final Player c) { if
	 * (c.inSaraWait() || c.inZammyWait()) { if (c.interfaceId != 6673) {
	 * c.getPA().walkableInterface(6673); } c.getPA().sendFrame126(waitTimer > 0
	 * ? "Next Game Begins In: " + (waitTimer < 100 ? "" + (int) (waitTimer *
	 * 0.6) + " secs" : "" + (int) (waitTimer / 60) + " min") :
	 * "@gre@Game in Progress - Time Left: " + (gameTimer > 100 ? (int)
	 * (gameTimer / 60) + " mins" : (int) (gameTimer * 0.6) + " sec"), 6570);
	 * c.getPA().sendFrame126("", 6572); c.getPA().sendFrame126("", 6664); }
	 * else if (c.inCwGame()) { if (c.interfaceId != 11344) {
	 * c.getPA().walkableInterface(11344); } c.getPA().sendFrame126(gameTimer >
	 * 100 ? (int) (gameTimer / 100) + " Mins" : (int) (gameTimer * 0.6) +
	 * " Sec", 11353); int config = ZammyMainGateHP; if (c.zammyTeam() ?
	 * zammySideDoor : saraSideDoor) { config += 128; } if (!saraRock1) { config
	 * += 256; } if (!saraRock2) { config += 512; } if (c.zammyTeam() ?
	 * !zammyCatapult : !saraCatapult) { config += 1024; } if (c.zammyTeam() ?
	 * ZflagDropped : SflagDropped) { config += 2097152 * 2; } else if
	 * (c.zammyTeam() ? ZammyFlagTaken : SaraFlagTaken) { config += 2097152 * 1;
	 * } config += 16777216 * (c.zammyTeam() ? zamorakScore : saradominScore);
	 * c.getPA().sendFrame87(c.zammyTeam() ? 377 : 378, config);
	 * 
	 * config = SaraMainGateHP; if (c.zammyTeam() ? zammySideDoor :
	 * saraSideDoor) { config += 128; } if (!zammyRock1) { config += 256; } if
	 * (!zammyRock2) { config += 512; } if (!saraCatapult) { config += 1024; }
	 * if (c.zammyTeam() ? SflagDropped : ZflagDropped) { config += 2097152 * 2;
	 * } else if (c.zammyTeam() ? SaraFlagTaken : ZammyFlagTaken) { config +=
	 * 2097152 * 1; } config += 16777216 * (c.zammyTeam() ? saradominScore :
	 * zamorakScore); c.getPA().sendFrame87(c.zammyTeam() ? 378 : 377, config);
	 * } }
	 */
}