package incendius.game.players.content.skills.summoning;

public class SummoningSpecials {
	/*
	 * public int damage = 0, damage2 = -1, endGfx = -1, timer = 0, drain = -1,
	 * hitType = 0; public boolean doubleHit = false;
	 * 
	 * 
	 * private Player c;
	 * 
	 * public SummoningSpecials(Player Player) { this.c = Player; }
	 * 
	 * public static void castFamiliarSpecial(Player c) { switch
	 * (c.getVariables().familiarID) { case 6817: fruitFall(c); break;
	 * 
	 * case 6841: redSpiderSpec(c); break;
	 * 
	 * case 6796: graniteCrabSpec(c.getVariables().familiarIndex, c); break;
	 * 
	 * case 6837: scorpionSpec(c.getVariables().familiarIndex, c); break;
	 * 
	 * case 6847: ratSpec(c.getVariables().familiarIndex, c); break;
	 * 
	 * case 6871: compostSpec(c.getVariables().familiarIndex, c); break;
	 * 
	 * case 6845: badgerSpec(c.getVariables().familiarIndex, c); break;
	 * 
	 * case 6808: beaverSpec(c.getVariables().familiarIndex, c); break;
	 * 
	 * case 7370: case 7367: case 7351: case 7333:
	 * voidSpec(c.getVariables().familiarIndex, c); break;
	 * 
	 * case 6867: bullAntSpec(c.getVariables().familiarIndex, c); break;
	 * 
	 * case 6851: herbSpec(c.getVariables().familiarIndex, c); break;
	 * 
	 * case 7377: pyreSpec(c.getVariables().familiarIndex, c); break;
	 * 
	 * case 6824: magpieSpec(c.getVariables().familiarIndex, c); break;
	 * 
	 * case 6843: leechSpec(c.getVariables().familiarIndex, c); break;
	 * 
	 * case 6794: terrorSpec(c.getVariables().familiarIndex, c); break;
	 * 
	 * case 6818: abbySpec(c.getVariables().familiarIndex, c); break;
	 * 
	 * case 6991: ibisSpec(c.getVariables().familiarIndex, c); break;
	 * 
	 * case 7363: graahkSpec(c.getVariables().familiarIndex, c); break;
	 * 
	 * case 6820: lurkerSpec(c.getVariables().familiarIndex, c); break;
	 * 
	 * case 6802: cobraSpec(c.getVariables().familiarIndex, c); break;
	 * 
	 * case 6815: tortSpec(c.getVariables().familiarIndex, c); break;
	 * 
	 * case 6813: startBunyipHealing(c); break;
	 * 
	 * case 7345: golemSpec(c.getVariables().familiarIndex, c); break;
	 * 
	 * case 7359: case 7357: case 7355:
	 * threeTitanSpec(c.getVariables().familiarIndex, c); break;
	 * 
	 * case 6811: hydraSpec(c.getVariables().familiarIndex, c); break;
	 * 
	 * case 6822: unicornSpec(c.getVariables().familiarIndex, c); break;
	 * 
	 * case 6869: magicFocus(c.getVariables().familiarIndex, c); break;
	 * 
	 * case 7349: abby2Spec(c.getVariables().familiarIndex, c); break;
	 * 
	 * } if (c.getVariables().summoningSpecialPoints >= 10) { c.getPA()
	 * .sendFrame126("" + c.getVariables().summoningSpecialPoints + "/60",
	 * 18024); } else { c.getPA().sendFrame126(" " +
	 * c.getVariables().summoningSpecialPoints + "/60", 18024); } }
	 * 
	 * private static boolean isNonCombat(final Player c) { switch
	 * (c.getVariables().familiarID) { case 6817: case 6841: case 6869: case
	 * 6796: case 6837: case 6847: case 6871: case 6845: case 6808: case 7370:
	 * case 7367: case 7349: case 7351: case 7333: case 6867: case 6851: case
	 * 7377: case 6824: case 6843: case 6794: case 6818: case 6991: case 7363:
	 * case 6820: case 6811: case 6802: case 6815: case 6813: case 7345: case
	 * 7359: case 7357: case 7355: case 6822: return true; } return false; }
	 * 
	 * public void castFamiliarCombatSpecial(final Player c, final NPC n) {
	 * if(c.getVariables().familiarIndex < 1) { return; } if
	 * (c.getVariables().npcIndex > 0 && !isNonCombat(c)) { if
	 * (c.getVariables().inMulti(c)) {
	 * specialEffectNPC(c.getVariables().familiarIndex,
	 * NPCHandler.npcs[c.getVariables().familiarIndex].npcType, n); if
	 * (c.getVariables().summoningSpecialPoints >= 10) { c.getPA().sendFrame126(
	 * "" + c.getVariables().summoningSpecialPoints + "/60", 18024); } else {
	 * c.getPA().sendFrame126( " " + c.getVariables().summoningSpecialPoints +
	 * "/60", 18024); } } else { c.sendMessage(
	 * "You must be in a multiple attack area to use this special."); } } if
	 * (c.getVariables().playerIndex > 0 && !isNonCombat(c)) { if
	 * (c.getVariables().inMulti(c)) {
	 * specialEffect(c.getVariables().familiarIndex,
	 * NPCHandler.npcs[c.getVariables().familiarIndex].npcType, n); if
	 * (c.getVariables().summoningSpecialPoints >= 10) { c.getPA().sendFrame126(
	 * "" + c.getVariables().summoningSpecialPoints + "/60", 18024); } else {
	 * c.getPA().sendFrame126( " " + c.getVariables().summoningSpecialPoints +
	 * "/60", 18024); } } else { c.sendMessage(
	 * "You must be in a multiple attack area to use this special."); } } }
	 * 
	 * public void specialEffectNPC(final int i, final int npc, final NPC n) {
	 * int famX = NPCHandler.npcs[i].absX; int famY = NPCHandler.npcs[i].absY;
	 * int npcX = NPCHandler.npcs[c.getVariables().npcIndex].absX; int npcY =
	 * NPCHandler.npcs[c.getVariables().npcIndex].absY; int offX = (famX - npcX)
	 * * -1; int offY = (famY - npcY) * -1; endGfx = -1; drain = -1; hitType =
	 * 0; doubleHit = false; damage2 = -1; switch (c.getVariables().familiarID)
	 * { case 7337: // NPC Larupri if (c.getVariables().familiarIndex > 0) {
	 * return; } else { if (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12840, 1)) { c.getItems().deleteItem2(12840,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; c.getPA().createPlayersProjectile(famX, famY, offX, offY,
	 * 50, 70, 1371, 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(5228); n.gfx0(1369); if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } damage =
	 * Misc.random(10); c.specHitTimer = 3; c.getPA().addSkillXP(200,23);
	 * c.getPA().refreshSkill(23); if (damage == 0) { endGfx = 85; } } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 7339: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12833, 1)) { c.getItems().deleteItem2(12833,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; doubleHit = true;
	 * typicalStuff(c); damage = Misc.random(15); damage2 = Misc.random(15);
	 * c.getPA().createPlayersProjectile2(famX + 1, famY + 1, offX, offY, 50,
	 * 70, 1376, 50, 14, c.getVariables().npcIndex + 1, 5, 30, c.heightLevel);
	 * c.getPA().createPlayersProjectile(famX + 1, famY + 1, offX, offY, 50, 70,
	 * 1376, 50, 14, c.getVariables().npcIndex + 1, 20, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(7883); NPCHandler.npcs[i].gfx100(1375);
	 * endGfx = 1377; c.specHitTimer = 4; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 7343: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 12) { if
	 * (c.getItems().playerHasItem(12825, 1)) { c.getItems().deleteItem2(12825,
	 * 1); c.getVariables().summoningSpecialPoints -= 12;
	 * NPCHandler.npcs[i].spec = true; NPCHandler.npcs[i].extraHit = true;
	 * doubleHit = true; typicalStuff(c);
	 * NPCHandler.npcs[i].startAnimation(8190); NPCHandler.npcs[i].gfx100(1449);
	 * c.specHitTimer = 3; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6829: // NPC Spirit Wolf case 6830: if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 3) { if
	 * (c.getItems().playerHasItem(12425, 1)) { c.getItems().deleteItem2(12425,
	 * 1); c.getVariables().summoningSpecialPoints -= 3; typicalStuff(c); if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } hitType = 1;
	 * c.getPA().createPlayersProjectile(famX, famY, offX, offY, 50, 70, 1333,
	 * 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8293); // n.gfx0(1369); damage =
	 * Misc.random(2); c.specHitTimer = 3; c.getPA().addSkillXP(4,23);
	 * c.getPA().refreshSkill(23); } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break; case 6825: // DreadFowl case 6826: if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 3) { if
	 * (c.getItems().playerHasItem(12445, 1)) { c.getItems().deleteItem2(12445,
	 * 1); c.getVariables().summoningSpecialPoints -= 3; typicalStuff(c); if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } hitType = 2;
	 * c.getPA().createPlayersProjectile(famX, famY, offX, offY, 50, 70, 1318,
	 * 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(7810); n.gfx0(1523); damage =
	 * Misc.random(3); c.specHitTimer = 3; c.getPA().addSkillXP(4,23);
	 * c.getPA().refreshSkill(23); if (damage == 0) { endGfx = 85; } } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6806: // Thorny Snail case 6807: if (c.getVariables().familiarIndex
	 * > 0) { return; } else { if (c.getVariables().summoningSpecialPoints >= 3)
	 * { if (c.getItems().playerHasItem(12459, 1)) {
	 * c.getItems().deleteItem2(12459, 1);
	 * c.getVariables().summoningSpecialPoints -= 3; typicalStuff(c); hitType =
	 * 2; if (c.getVariables().playerEquipment[c.getVariables().playerCape] ==
	 * 19893) { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, offX, offY, 50, 70, 1386,
	 * 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8148); n.gfx0(1385); damage =
	 * Misc.random(8); c.specHitTimer = 3; c.getPA().addSkillXP(7,23);
	 * c.getPA().refreshSkill(23); endGfx = 1387; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 7331: // Mosquito case 7332: if (c.getVariables().familiarIndex > 0)
	 * { return; } else { if (c.getVariables().summoningSpecialPoints >= 3) { if
	 * (c.getItems().playerHasItem(12838, 1)) { c.getItems().deleteItem2(12838,
	 * 1); c.getVariables().summoningSpecialPoints -= 3; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } //
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, // poffY, 50, 70,
	 * 1386, 41, 28, c.getVariables().playerIndex + 1, 5, // c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8039); n.gfx0(1441); damage = 2;
	 * c.specHitTimer = 3; c.getPA().addSkillXP(18,23);
	 * c.getPA().refreshSkill(23); } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6831: // Desert Wyrm case 6832: if (c.getVariables().familiarIndex >
	 * 0) { return; } else { if (c.getVariables().summoningSpecialPoints >= 6) {
	 * if (c.getItems().playerHasItem(12460, 1)) {
	 * c.getItems().deleteItem2(12460, 1);
	 * c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c); hitType =
	 * 2; if (c.getVariables().playerEquipment[c.getVariables().playerCape] ==
	 * 19893) { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, offX, offY, 50, 70, 1411,
	 * 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(7795); n.gfx0(1410); damage =
	 * Misc.random(5); c.specHitTimer = 3; c.getPA().addSkillXP(14,23);
	 * c.getPA().refreshSkill(23); endGfx = 1413; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 7361: // TzKih case 7362: if (c.getVariables().familiarIndex > 0) {
	 * return; } else { if (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12839, 1)) { c.getItems().deleteItem2(12839,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } //
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, // poffY, 50, 70,
	 * 1411, 41, 28, c.getVariables().playerIndex + 1, 5, // c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8257); // n.gfx0(1410); damage =
	 * Misc.random(7); c.specHitTimer = 3; c.getPA().addSkillXP(39,23);
	 * c.getPA().refreshSkill(23); endGfx = 1329; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6994: // Kalphite case 6995: if (c.getVariables().familiarIndex > 0)
	 * { return; } else { if (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12446, 1)) { c.getItems().deleteItem2(12446,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, offX, offY, 50, 70, 1349,
	 * 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8517); // n.gfx0(1410); damage =
	 * Misc.random(5); c.specHitTimer = 3; c.getPA().addSkillXP(81,23);
	 * c.getPA().refreshSkill(23); } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 7335: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12841, 1)) { c.getItems().deleteItem2(12841,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * NPCHandler.npcs[i].startAnimation(7871); n.gfx0(1394); damage =
	 * Misc.random(20); c.specHitTimer = 3; c.getPA().addSkillXP(181,23);
	 * c.getPA().refreshSkill(23); endGfx = 1395; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 7353: // chinchompa case 7354: if (c.getVariables().familiarIndex >
	 * 0) { return; } else { if (c.getVariables().summoningSpecialPoints >= 3) {
	 * if (c.getItems().playerHasItem(12834, 1)) {
	 * c.getItems().deleteItem2(12834, 1);
	 * c.getVariables().summoningSpecialPoints -= 3; typicalStuff(c); hitType =
	 * 2; if (c.getVariables().playerEquipment[c.getVariables().playerCape] ==
	 * 19893) { c.getVariables().summoningSpecialPoints += 2; }
	 * NPCHandler.npcs[i].startAnimation(7758); n.gfx0(1364); damage =
	 * Misc.random(12); c.specHitTimer = 2; c.getPA().addSkillXP(104,23);
	 * c.getPA().refreshSkill(23); } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break; case 6835: // vampire bat case 6836: if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 4) { if
	 * (c.getItems().playerHasItem(12447, 1)) { c.getItems().deleteItem2(12447,
	 * 1); c.getVariables().summoningSpecialPoints -= 4; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * NPCHandler.npcs[i].startAnimation(8277); n.gfx0(1467); damage =
	 * Misc.random(12); c.getVariables().playerLevel[3] += Misc.random(2); if
	 * (c.getVariables().playerLevel[3] >
	 * c.getLevelForXP(c.getVariables().playerXP[3]))
	 * c.getVariables().playerLevel[3] =
	 * c.getLevelForXP(c.getVariables().playerXP[3]); c.specHitTimer = 2;
	 * c.getPA().addSkillXP(56,23); c.getPA().refreshSkill(23); } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6853: // bronze minot case 6854: if (c.getVariables().familiarIndex
	 * > 0) { return; } else { if (c.getVariables().summoningSpecialPoints >= 6)
	 * { if (c.getItems().playerHasItem(12461, 1)) {
	 * c.getItems().deleteItem2(12461, 1);
	 * c.getVariables().summoningSpecialPoints -= 6; c.startAnimation(7660);
	 * c.gfx0(1316); NPCHandler.npcs[c.getVariables().familiarIndex].attackTimer
	 * = 4; c.getVariables().familiarIndex += 6; hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, offX, offY, 50, 70, 1497,
	 * 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8026); n.gfx0(1496); damage =
	 * Misc.random(4); c.specHitTimer = 3; c.getPA().addSkillXP(126,23);
	 * c.getPA().refreshSkill(23); if (damage == 0) { endGfx = 85; } } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6833: // evil turnip case 6834: if (c.getVariables().familiarIndex >
	 * 0) { return; } else { if (c.getVariables().summoningSpecialPoints >= 6) {
	 * if (c.getItems().playerHasItem(12448, 1)) {
	 * c.getItems().deleteItem2(12448, 1);
	 * c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c); hitType =
	 * 1; if (c.getVariables().playerEquipment[c.getVariables().playerCape] ==
	 * 19893) { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, offX, offY, 50, 70, 1330,
	 * 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8251); damage = Misc.random(6);
	 * c.getVariables().playerLevel[4] += damage / 2; if
	 * (c.getVariables().playerLevel[4] > c.getPA().getLevelForXP(
	 * c.getVariables().playerXP[4]) + 6) { c.getVariables().playerLevel[4] =
	 * c.getPA().getLevelForXP( c.getVariables().playerXP[4]) + 6; }
	 * c.specHitTimer = 3; c.getPA().addSkillXP(74,23);
	 * c.getPA().refreshSkill(23);
	 * c.getPA().refreshSkill(c.getVariables().playerRanged); if (damage == 0) {
	 * endGfx = 85; } else { endGfx = 1329; } } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6855: // iron minot case 6856: if (c.getVariables().familiarIndex >
	 * 0) { return; } else { if (c.getVariables().summoningSpecialPoints >= 6) {
	 * if (c.getItems().playerHasItem(12462, 1)) {
	 * c.getItems().deleteItem2(12462, 1);
	 * c.getVariables().summoningSpecialPoints -= 6; c.startAnimation(7660);
	 * c.gfx0(1316); NPCHandler.npcs[c.getVariables().familiarIndex].attackTimer
	 * = 4; c.getVariables().familiarIndex += 6; hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, offX, offY, 50, 70, 1497,
	 * 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8026); n.gfx0(1496); damage =
	 * Misc.random(8); c.specHitTimer = 3; c.getPA().addSkillXP(161,23);
	 * c.getPA().refreshSkill(23); if (damage == 0) { endGfx = 85; } } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6800: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12457, 1)) { c.getItems().deleteItem2(12457,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, offX, offY, 50, 70, 1362,
	 * 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(7852); damage = Misc.random(19);
	 * c.specHitTimer = 3; endGfx = 1363; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6992: // spirit Jelly if (c.getVariables().familiarIndex > 0) {
	 * return; } else { if (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12453, 1)) { c.getItems().deleteItem2(12453,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, offX, offY, 50, 70, 1359,
	 * 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8575); damage = Misc.random(12);
	 * c.specHitTimer = 3; c.getPA().addSkillXP(193,23);
	 * c.getPA().refreshSkill(23); endGfx = 1360; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 7347: // talon beast if (c.getVariables().familiarIndex > 0) {
	 * return; } else { if (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12831, 1)) { c.getItems().deleteItem2(12831,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; c.startAnimation(7660);
	 * c.gfx0(1316); NPCHandler.npcs[c.getVariables().familiarIndex].attackTimer
	 * = 3; c.getVariables().familiarIndex += 6; hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, offX, offY, 50, 70, 1520,
	 * 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8043); n.gfx0(1519); damage =
	 * Misc.random(8); damage2 = Misc.random(8); doubleHit = true;
	 * c.specHitTimer = 3; c.getPA().addSkillXP(395,23);
	 * c.getPA().refreshSkill(23); if (damage == 0 || damage2 == 0) { endGfx =
	 * 85; } } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6804: // Spirit Dag if (c.getVariables().familiarIndex > 0) {
	 * return; } else { if (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12456, 1)) { c.getItems().deleteItem2(12456,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, offX, offY, 50, 70, 1426,
	 * 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(7787); // n.gfx0(1410); damage =
	 * Misc.random(18); c.specHitTimer = 3; c.getPA().addSkillXP(281,23);
	 * c.getPA().refreshSkill(23); endGfx = 1428; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 7341: // lava titan if (c.getVariables().familiarIndex > 0) {
	 * return; } else { if (c.getVariables().summoningSpecialPoints >= 4) { if
	 * (c.getItems().playerHasItem(12837, 1)) { c.getItems().deleteItem2(12837,
	 * 1); c.getVariables().summoningSpecialPoints -= 4; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, offX, offY, 50, 70, 1493,
	 * 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(7986); n.gfx0(1492); damage =
	 * Misc.random(20); c.specHitTimer = 3; c.getPA().addSkillXP(241,23);
	 * c.getPA().refreshSkill(23); endGfx = 1494; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 7375: // iron Titan if (c.getVariables().familiarIndex > 0) {
	 * return; } else { if (c.getVariables().summoningSpecialPoints >= 12) { if
	 * (c.getItems().playerHasItem(12828, 1)) { c.getItems().deleteItem2(12828,
	 * 1); c.getVariables().summoningSpecialPoints -= 12;
	 * c.startAnimation(7660); c.gfx0(1316);
	 * NPCHandler.npcs[c.getVariables().familiarIndex].attackTimer = 2;
	 * c.getVariables().familiarIndex += 6; hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * NPCHandler.npcs[i].startAnimation(7837); n.gfx0(1450); damage =
	 * Misc.random(23); damage2 = Misc.random(23); doubleHit = true;
	 * c.specHitTimer = 3; c.getPA().addSkillXP(615,23);
	 * c.getPA().refreshSkill(23); } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6857: // steel minot if (c.getVariables().familiarIndex > 0) {
	 * return; } else { if (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12463, 1)) { c.getItems().deleteItem2(12463,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; c.startAnimation(7660);
	 * c.gfx0(1316); NPCHandler.npcs[c.getVariables().familiarIndex].attackTimer
	 * = 3; c.getVariables().familiarIndex += 6; hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, offX, offY, 50, 70, 1497,
	 * 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8026); n.gfx0(1496); damage =
	 * Misc.random(9); c.specHitTimer = 3; c.getPA().addSkillXP(195,23);
	 * c.getPA().refreshSkill(23); if (damage == 0) { endGfx = 85; } } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6875: // the cocks case 6877: case 6879: case 6881: case 6883: case
	 * 6885: case 6887: if (c.getVariables().familiarIndex > 0) { return; } else
	 * { if (c.getVariables().summoningSpecialPoints >= 3) { if
	 * (c.getItems().playerHasItem(12458, 1)) { c.getItems().deleteItem2(12458,
	 * 1); c.getVariables().summoningSpecialPoints -= 3; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, offX, offY, 50, 70, 1468,
	 * 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(7766); n.gfx0(1467); damage =
	 * Misc.random(10); c.specHitTimer = 3; c.getPA().addSkillXP(33,23);
	 * c.getPA().refreshSkill(23); if (damage == 0) { endGfx = 85; } else {
	 * endGfx = 1469; } } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 7365: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 3) { if
	 * (c.getItems().playerHasItem(12836, 1)) { c.getItems().deleteItem2(12836,
	 * 1); c.getVariables().summoningSpecialPoints -= 3; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * NPCHandler.npcs[i].startAnimation(7914); n.gfx0(1369); damage =
	 * Misc.random(11) * 3; c.specHitTimer = 3; c.getPA().addSkillXP(199,23);
	 * c.getPA().refreshSkill(23); } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6863: // rune minot if (c.getVariables().familiarIndex > 0) {
	 * return; } else { if (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12466, 1)) { c.getItems().deleteItem2(12466,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; c.startAnimation(7660);
	 * c.gfx0(1316); NPCHandler.npcs[c.getVariables().familiarIndex].attackTimer
	 * = 3; c.getVariables().familiarIndex += 6; hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, offX, offY, 50, 70, 1497,
	 * 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8026); n.gfx0(1496); damage =
	 * Misc.random(20); c.specHitTimer = 3; c.getPA().addSkillXP(395,23);
	 * c.getPA().refreshSkill(23); if (damage == 0) { endGfx = 85; } } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6809: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 3) { if
	 * (c.getItems().playerHasItem(12455, 1)) { c.getItems().deleteItem2(12455,
	 * 1); c.getVariables().summoningSpecialPoints -= 3; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, offX, offY, 50, 70, 1479,
	 * 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(7974); n.gfx0(1478); damage =
	 * Misc.random(16); c.specHitTimer = 3; c.getPA().addSkillXP(206,23);
	 * c.getPA().refreshSkill(23); endGfx = 1480; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6865: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12468, 1)) { c.getItems().deleteItem2(12468,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * NPCHandler.npcs[i].startAnimation(7821); n.gfx0(1470); damage =
	 * Misc.random(6); c.specHitTimer = 3; c.getPA().addSkillXP(223,23);
	 * c.getPA().refreshSkill(23); } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6827: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12467, 1)) { c.getItems().deleteItem2(12467,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, offX, offY, 50, 70, 1508,
	 * 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8214); damage = Misc.random(13);
	 * c.specHitTimer = 3; c.getPA().addSkillXP(245,23);
	 * c.getPA().refreshSkill(23); endGfx = 1511; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6859: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12464, 1)) { c.getItems().deleteItem2(12464,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; c.startAnimation(7660);
	 * c.gfx0(1316); NPCHandler.npcs[c.getVariables().familiarIndex].attackTimer
	 * = 4; c.getVariables().familiarIndex += 6; hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, offX, offY, 50, 70, 1497,
	 * 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8026); n.gfx0(1496); damage =
	 * Misc.random(13); c.specHitTimer = 3; c.getPA().addSkillXP(300,23);
	 * c.getPA().refreshSkill(23); if (damage == 0) { endGfx = 85; } } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 7329: // swamp titan if (c.getVariables().familiarIndex > 0) {
	 * return; } else { if (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12832, 1)) { c.getItems().deleteItem2(12832,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, offX, offY, 50, 70, 1462,
	 * 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8223); damage = Misc.random(21);
	 * c.getPA().crabSpecBonus(c.getVariables().playerHerblore, false);
	 * c.getPA().refreshSkill(c.getVariables().playerHerblore); c.specHitTimer =
	 * 3; c.getPA().addSkillXP(241,23); c.getPA().refreshSkill(23); endGfx =
	 * 1460; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6889: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12452, 1)) { c.getItems().deleteItem2(12452,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } //
	 * c.getPA().createPlayersProjectile(famX, famY, offX, // offY, 50, 70,
	 * 1508, 41, 28, c.getVariables().npcIndex + 1, 5, // c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(7705); n.gfx0(1403); damage =
	 * Misc.random(18); c.specHitTimer = 3; c.getPA().addSkillXP(45,23);
	 * c.getPA().refreshSkill(23); endGfx = 1404; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 7372: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 12) { if
	 * (c.getItems().playerHasItem(12830, 1)) { c.getItems().deleteItem2(12830,
	 * 1); c.getVariables().summoningSpecialPoints -= 12; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, offX, offY, 50, 70, 1347,
	 * 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(7995); NPCHandler.npcs[i].gfx0(1346);
	 * damage = Misc.random(7); c.specHitTimer = 3;
	 * c.getPA().addSkillXP(182,23); c.getPA().refreshSkill(23); endGfx = 1348;
	 * } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6839: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12451, 1)) { c.getItems().deleteItem2(12451,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * NPCHandler.npcs[i].gfx100(1405); c.getPA().createPlayersProjectile(famX,
	 * famY, offX, offY, 50, 70, 1406, 41, 28, c.getVariables().npcIndex + 1, 5,
	 * c.heightLevel); NPCHandler.npcs[i].startAnimation(4921); damage =
	 * Misc.random(15); c.specHitTimer = 3; if (damage == 0) { endGfx = 85; }
	 * else { endGfx = 1407; } } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 8575: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 5) { if
	 * (c.getItems().playerHasItem(14622, 1)) { c.getItems().deleteItem2(14622,
	 * 1); c.getVariables().summoningSpecialPoints -= 5; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, offX, offY, 50, 70, 1330,
	 * 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(11093); damage = Misc.random(14);
	 * c.specHitTimer = 3; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6849: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12449, 1)) { c.getItems().deleteItem2(12449,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, offX, offY, 50, 70, 1352,
	 * 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8118); NPCHandler.npcs[i].gfx0(1353);
	 * damage = Misc.random(14); damage2 = Misc.random(14); doubleHit = true;
	 * c.specHitTimer = 3; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6798: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12450, 1)) { c.getItems().deleteItem2(12450,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c); if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } hitType = 2;
	 * NPCHandler.npcs[i].startAnimation(8071); NPCHandler.npcs[i].gfx0(1353);
	 * damage = Misc.random(17); c.specHitTimer = 3; endGfx = 1353; } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6861: // addy minot if (c.getVariables().familiarIndex > 0) {
	 * return; } else { if (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12465, 1)) { c.getItems().deleteItem2(12465,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; c.startAnimation(7660);
	 * c.gfx0(1316); NPCHandler.npcs[c.getVariables().familiarIndex].attackTimer
	 * = 3; c.getVariables().familiarIndex += 6; hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, offX, offY, 50, 70, 1497,
	 * 41, 28, c.getVariables().npcIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8026); n.gfx0(1496); damage =
	 * Misc.random(16); c.specHitTimer = 3; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().addSkillXP(349,23); c.getPA().refreshSkill(23); if (damage ==
	 * 0) { endGfx = 85; } } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * } }
	 * 
	 * public void specialEffect(int i, int npc, NPC n) { int famX =
	 * NPCHandler.npcs[i].absX; int famY = NPCHandler.npcs[i].absY; int pX =
	 * PlayerHandler.players[c.getVariables().playerIndex].absX; int pY =
	 * PlayerHandler.players[c.getVariables().playerIndex].absY; int poffX =
	 * (famX - pX) * -1; int poffY = (famX - pY) * -1; endGfx = -1; drain = -1;
	 * switch (npc) { case 7337: // Larupri if (c.getVariables().familiarIndex >
	 * 0) { return; } else { if (c.getVariables().summoningSpecialPoints >= 6) {
	 * if (c.getItems().playerHasItem(12840, 1)) {
	 * c.getItems().deleteItem2(12840, 1);
	 * c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70, 1371,
	 * 41, 28, c.getVariables().oldPlayerIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(5228); n.gfx0(1369); drain = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } hitType = 2; damage =
	 * Misc.random(10); c.getVariables().specHitTimer = 3;
	 * c.getPA().addSkillXP(200,23); c.getPA().refreshSkill(23); if (damage ==
	 * 0) { endGfx = 85; } } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6829: // Spirit Wolf case 6830: if (c.getVariables().familiarIndex >
	 * 0) { return; } else { if (c.getVariables().summoningSpecialPoints >= 3) {
	 * if (c.getItems().playerHasItem(12425, 1)) {
	 * c.getItems().deleteItem2(12425, 1);
	 * c.getVariables().summoningSpecialPoints -= 3; typicalStuff(c); hitType =
	 * 2; if (c.getVariables().playerEquipment[c.getVariables().playerCape] ==
	 * 19893) { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70, 1333,
	 * 41, 28, c.getVariables().oldPlayerIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8293); // n.gfx0(1369); damage =
	 * Misc.random(2); c.getVariables().specHitTimer = 3;
	 * c.getPA().addSkillXP(4,23); c.getPA().refreshSkill(23); } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break; case 6825: // DreadFowl case 6826: if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 3) { if
	 * (c.getItems().playerHasItem(12445, 1)) { c.getItems().deleteItem2(12445,
	 * 1); c.getVariables().summoningSpecialPoints -= 3; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70, 1318,
	 * 41, 28, c.getVariables().oldPlayerIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(7810); n.gfx0(1523); damage =
	 * Misc.random(3); c.getVariables().specHitTimer = 3;
	 * c.getPA().addSkillXP(4,23); c.getPA().refreshSkill(23); if (damage == 0)
	 * { endGfx = 85; } } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6806: // Thorny Snail case 6807: if (c.getVariables().familiarIndex
	 * > 0) { return; } else { if (c.getVariables().summoningSpecialPoints >= 3)
	 * { if (c.getItems().playerHasItem(12459, 1)) {
	 * c.getItems().deleteItem2(12459, 1);
	 * c.getVariables().summoningSpecialPoints -= 3; typicalStuff(c); hitType =
	 * 2; if (c.getVariables().playerEquipment[c.getVariables().playerCape] ==
	 * 19893) { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70, 1386,
	 * 41, 28, c.getVariables().oldPlayerIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8148); n.gfx0(1385); damage =
	 * Misc.random(8); c.getVariables().specHitTimer = 3;
	 * c.getPA().addSkillXP(7,23); c.getPA().refreshSkill(23); endGfx = 1387; }
	 * else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 7331: // Mosquito case 7332: if (c.getVariables().familiarIndex > 0)
	 * { return; } else { if (c.getVariables().summoningSpecialPoints >= 3) { if
	 * (c.getItems().playerHasItem(12838, 1)) { c.getItems().deleteItem2(12838,
	 * 1); c.getVariables().summoningSpecialPoints -= 3; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } //
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, // poffY, 50, 70,
	 * 1386, 41, 28, c.oldPlayerIndex + 1, 5, // c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8039); n.gfx0(1441); damage = 2;
	 * c.getVariables().specHitTimer = 3; c.getPA().addSkillXP(18,23);
	 * c.getPA().refreshSkill(23); } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6831: // Desert Wyrm case 6832: if (c.getVariables().familiarIndex >
	 * 0) { return; } else { if (c.getVariables().summoningSpecialPoints >= 6) {
	 * if (c.getItems().playerHasItem(12460, 1)) {
	 * c.getItems().deleteItem2(12460, 1);
	 * c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c); hitType =
	 * 2; if (c.getVariables().playerEquipment[c.getVariables().playerCape] ==
	 * 19893) { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70, 1411,
	 * 41, 28, c.getVariables().oldPlayerIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(7795); n.gfx0(1410); damage =
	 * Misc.random(5); c.getVariables().specHitTimer = 3;
	 * c.getPA().addSkillXP(14,23); c.getPA().refreshSkill(23); endGfx = 1413; }
	 * else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 7361: // TzKih case 7362: if (c.getVariables().familiarIndex > 0) {
	 * return; } else { if (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12839, 1)) { c.getItems().deleteItem2(12839,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } //
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, // poffY, 50, 70,
	 * 1411, 41, 28, c.oldPlayerIndex + 1, 5, // c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8257); // n.gfx0(1410); damage =
	 * Misc.random(7); c.getVariables().specHitTimer = 3;
	 * c.getPA().addSkillXP(39,23); c.getPA().refreshSkill(23); endGfx = 1329; }
	 * else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6994: // Kalphite case 6995: if (c.getVariables().familiarIndex > 0)
	 * { return; } else { if (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12446, 1)) { c.getItems().deleteItem2(12446,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70, 1349,
	 * 41, 28, c.getVariables().oldPlayerIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8517); // n.gfx0(1410); damage =
	 * Misc.random(5); c.getVariables().specHitTimer = 3;
	 * c.getPA().addSkillXP(81,23); c.getPA().refreshSkill(23); } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6804: // Spirit Dag if (c.getVariables().familiarIndex > 0) {
	 * return; } else { if (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12456, 1)) { c.getItems().deleteItem2(12456,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; c.getPA().createPlayersProjectile(famX, famY, poffX, poffY,
	 * 50, 70, 1426, 41, 28, c.getVariables().playerIndex + 1, 5,
	 * c.heightLevel); NPCHandler.npcs[i].startAnimation(7787); // n.gfx0(1410);
	 * if (c.getVariables().playerEquipment[c.getVariables().playerCape] ==
	 * 19893) { c.getVariables().summoningSpecialPoints += 2; } damage =
	 * Misc.random(18); c.getVariables().specHitTimer = 3;
	 * c.getPA().addSkillXP(281,23); c.getPA().refreshSkill(23); endGfx = 1428;
	 * } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break; case 7353: // chinchompa case 7354: if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 3) { if
	 * (c.getItems().playerHasItem(12834, 1)) { c.getItems().deleteItem2(12834,
	 * 1); c.getVariables().summoningSpecialPoints -= 3; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } //
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, // poffY, 50, 70,
	 * 1349, 41, 28, c.oldPlayerIndex + 1, 5, // c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(7758); n.gfx0(1364); damage =
	 * Misc.random(12); c.specHitTimer = 3; c.getPA().addSkillXP(104,23);
	 * c.getPA().refreshSkill(23); } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break; case 6835: // vampire bat case 6836: if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 4) { if
	 * (c.getItems().playerHasItem(12447, 1)) { c.getItems().deleteItem2(12447,
	 * 1); c.getVariables().summoningSpecialPoints -= 4; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } //
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, // poffY, 50, 70,
	 * 1497, 41, 28, c.oldPlayerIndex + 1, 5, // c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8277); n.gfx0(1467); damage =
	 * Misc.random(12); c.getVariables().playerLevel[3] += Misc.random(2); if
	 * (c.getVariables().playerLevel[3] > c
	 * .getLevelForXP(c.getVariables().playerLevel[3]))
	 * c.getVariables().playerLevel[3] = c
	 * .getLevelForXP(c.getVariables().playerLevel[3]); c.specHitTimer = 1;
	 * c.getPA().addSkillXP(56,23); c.getPA().refreshSkill(23); } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6853: // bronze minot case 6854: if (c.getVariables().familiarIndex
	 * > 0) { return; } else { if (c.getVariables().summoningSpecialPoints >= 6)
	 * { if (c.getItems().playerHasItem(12461, 1)) {
	 * c.getItems().deleteItem2(12461, 1);
	 * c.getVariables().summoningSpecialPoints -= 6; c.startAnimation(7660); if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } c.gfx0(1316);
	 * NPCHandler.npcs[c.getVariables().familiarIndex].attackTimer = 3;
	 * c.getVariables().familiarIndex += 6; hitType = 2;
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70, 1497,
	 * 41, 28, c.oldPlayerIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8026); n.gfx0(1496); damage =
	 * Misc.random(4); c.specHitTimer = 3; c.getPA().addSkillXP(126,23);
	 * c.getPA().refreshSkill(23); if (damage == 0) { endGfx = 85; } } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 8575: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 5) { if
	 * (c.getItems().playerHasItem(14622, 1)) { c.getItems().deleteItem2(14622,
	 * 1); c.getVariables().summoningSpecialPoints -= 5; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70, 1330,
	 * 41, 28, c.getVariables().playerIndex, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(11093); damage = Misc.random(14);
	 * c.specHitTimer = 3; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6833: // evil turnip case 6834: if (c.getVariables().familiarIndex >
	 * 0) { return; } else { if (c.getVariables().summoningSpecialPoints >= 6) {
	 * if (c.getItems().playerHasItem(12448, 1)) {
	 * c.getItems().deleteItem2(12448, 1);
	 * c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c); hitType =
	 * 2; c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70,
	 * 1330, 41, 28, c.oldPlayerIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8251); damage = Misc.random(6);
	 * c.getVariables().playerLevel[4] += damage / 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } if
	 * (c.getVariables().playerLevel[4] > c.getPA().getLevelForXP(
	 * c.getVariables().playerXP[4]) + 5) { c.getVariables().playerLevel[4] =
	 * c.getPA().getLevelForXP( c.getVariables().playerXP[4]) + 5; }
	 * c.specHitTimer = 3; c.getPA().addSkillXP(74,23);
	 * c.getPA().refreshSkill(23);
	 * c.getPA().refreshSkill(c.getVariables().playerRanged); if (damage == 0) {
	 * endGfx = 85; } else { endGfx = 1329; } } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6889: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12452, 1)) { c.getItems().deleteItem2(12452,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } //
	 * c.getPA().createPlayersProjectile(famX, famY, offX, // offY, 50, 70,
	 * 1508, 41, 28, c.getVariables().npcIndex + 1, 5, // c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(7705); n.gfx0(1403); damage =
	 * Misc.random(18); c.specHitTimer = 3; c.getPA().addSkillXP(45,23);
	 * c.getPA().refreshSkill(23); endGfx = 1404; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6855: // iron minot case 6856: if (c.getVariables().familiarIndex >
	 * 0) { return; } else { if (c.getVariables().summoningSpecialPoints >= 6) {
	 * if (c.getItems().playerHasItem(12462, 1)) {
	 * c.getItems().deleteItem2(12462, 1);
	 * c.getVariables().summoningSpecialPoints -= 6; c.startAnimation(7660);
	 * c.gfx0(1316); NPCHandler.npcs[c.getVariables().familiarIndex].attackTimer
	 * = 3; c.getVariables().familiarIndex += 6; hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70, 1497,
	 * 41, 28, c.oldPlayerIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8026); n.gfx0(1496); damage =
	 * Misc.random(8); c.specHitTimer = 3; c.getPA().addSkillXP(161,23);
	 * c.getPA().refreshSkill(23); if (damage == 0) { endGfx = 85; } } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6992: // spirit Jelly if (c.getVariables().familiarIndex > 0) {
	 * return; } else { if (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12453, 1)) { c.getItems().deleteItem2(12453,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70, 1359,
	 * 41, 28, c.oldPlayerIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8575); damage = Misc.random(8);
	 * c.specHitTimer = 3; c.getPA().addSkillXP(193,23);
	 * c.getPA().refreshSkill(23); endGfx = 1360; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6857: // steel minot if (c.getVariables().familiarIndex > 0) {
	 * return; } else { if (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12463, 1)) { c.getItems().deleteItem2(12463,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; c.startAnimation(7660);
	 * c.gfx0(1316); NPCHandler.npcs[c.getVariables().familiarIndex].attackTimer
	 * = 3; c.getVariables().familiarIndex += 6; hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70, 1497,
	 * 41, 28, c.oldPlayerIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8026); n.gfx0(1496); damage =
	 * Misc.random(9); c.specHitTimer = 3; c.getPA().addSkillXP(195,23);
	 * c.getPA().refreshSkill(23); if (damage == 0) { endGfx = 85; } } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6863: // rune minot if (c.getVariables().familiarIndex > 0) {
	 * return; } else { if (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12466, 1)) { c.getItems().deleteItem2(12466,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; c.startAnimation(7660);
	 * c.gfx0(1316); NPCHandler.npcs[c.getVariables().familiarIndex].attackTimer
	 * = 3; c.getVariables().familiarIndex += 6; hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70, 1497,
	 * 41, 28, c.oldPlayerIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8026); n.gfx0(1496); damage =
	 * Misc.random(20); c.specHitTimer = 3; c.getPA().addSkillXP(395,23);
	 * c.getPA().refreshSkill(23); if (damage == 0) { endGfx = 85; } } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6861: // addy minot if (c.getVariables().familiarIndex > 0) {
	 * return; } else { if (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12465, 1)) { c.getItems().deleteItem2(12465,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; c.startAnimation(7660);
	 * c.gfx0(1316); NPCHandler.npcs[c.getVariables().familiarIndex].attackTimer
	 * = 3; c.getVariables().familiarIndex += 6; hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70, 1497,
	 * 41, 28, c.oldPlayerIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8026); n.gfx0(1496); damage =
	 * Misc.random(16); c.specHitTimer = 3; c.getPA().addSkillXP(349,23);
	 * c.getPA().refreshSkill(23); if (damage == 0) { endGfx = 85; } } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 7339: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12833, 1)) { c.getItems().deleteItem2(12833,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; doubleHit = true;
	 * typicalStuff(c); damage = Misc.random(15); damage2 = Misc.random(15);
	 * c.getPA().createPlayersProjectile2(famX + 1, famY + 1, poffX, poffY, 50,
	 * 70, 1376, 50, 14, c.getVariables().playerIndex + 1, 5, 30,
	 * c.heightLevel); c.getPA().createPlayersProjectile(famX + 1, famY + 1,
	 * poffX, poffY, 50, 70, 1376, 50, 14, c.getVariables().playerIndex + 1, 20,
	 * c.heightLevel); NPCHandler.npcs[i].startAnimation(7883);
	 * NPCHandler.npcs[i].gfx100(1375); endGfx = 1377; c.specHitTimer = 4; }
	 * else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6875: // the cocks case 6877: case 6879: case 6881: case 6883: case
	 * 6885: case 6887: if (c.getVariables().familiarIndex > 0) { return; } else
	 * { if (c.getVariables().summoningSpecialPoints >= 3) { if
	 * (c.getItems().playerHasItem(12458, 1)) { c.getItems().deleteItem2(12458,
	 * 1); c.getVariables().summoningSpecialPoints -= 3; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70, 1468,
	 * 41, 28, c.oldPlayerIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(7766); n.gfx0(1467); damage =
	 * Misc.random(10); c.specHitTimer = 3; c.getPA().addSkillXP(33,23);
	 * c.getPA().refreshSkill(23); if (damage == 0) { endGfx = 85; } else {
	 * endGfx = 1469; } } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 7335: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12841, 1)) { c.getItems().deleteItem2(12841,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * NPCHandler.npcs[i].startAnimation(7871); n.gfx0(1394); damage =
	 * Misc.random(20); c.specHitTimer = 3; c.getPA().addSkillXP(181,23);
	 * c.getPA().refreshSkill(23); endGfx = 1395; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 7365: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 3) { if
	 * (c.getItems().playerHasItem(12836, 1)) { c.getItems().deleteItem2(12836,
	 * 1); c.getVariables().summoningSpecialPoints -= 3; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * NPCHandler.npcs[i].startAnimation(7914); n.gfx0(1369); damage =
	 * Misc.random(11) * 3; c.specHitTimer = 3; c.getPA().addSkillXP(199,23);
	 * c.getPA().refreshSkill(23); } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 7343: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 12) { if
	 * (c.getItems().playerHasItem(12825, 1)) { c.getItems().deleteItem2(12825,
	 * 1); c.getVariables().summoningSpecialPoints -= 12;
	 * NPCHandler.npcs[i].spec = true; NPCHandler.npcs[i].extraHit = true;
	 * doubleHit = true; typicalStuff(c);
	 * NPCHandler.npcs[i].startAnimation(8190); NPCHandler.npcs[i].gfx100(1449);
	 * c.specHitTimer = 3; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6809: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 3) { if
	 * (c.getItems().playerHasItem(12455, 1)) { c.getItems().deleteItem2(12455,
	 * 1); c.getVariables().summoningSpecialPoints -= 3; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70, 1479,
	 * 41, 28, c.oldPlayerIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(7974); n.gfx0(1478); damage =
	 * Misc.random(16); c.specHitTimer = 3; c.getPA().addSkillXP(206,23);
	 * c.getPA().refreshSkill(23); endGfx = 1480; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6865: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12468, 1)) { c.getItems().deleteItem2(12468,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * NPCHandler.npcs[i].startAnimation(7820); n.gfx0(1470); damage =
	 * Misc.random(6); c.specHitTimer = 3; c.getPA().addSkillXP(223,23);
	 * c.getPA().refreshSkill(23); } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6827: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12467, 1)) { c.getItems().deleteItem2(12467,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70, 1508,
	 * 41, 28, c.oldPlayerIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8214); damage = Misc.random(13);
	 * c.specHitTimer = 3; c.getPA().addSkillXP(245,23);
	 * c.getPA().refreshSkill(23); endGfx = 1511; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6859: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12464, 1)) { c.getItems().deleteItem2(12464,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; c.startAnimation(7660);
	 * c.gfx0(1316); NPCHandler.npcs[c.getVariables().familiarIndex].attackTimer
	 * = 4; c.getVariables().familiarIndex += 6; hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70, 1497,
	 * 41, 28, c.oldPlayerIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8026); n.gfx0(1496); damage =
	 * Misc.random(13); c.specHitTimer = 3; c.getPA().addSkillXP(300,23);
	 * c.getPA().refreshSkill(23); if (damage == 0) { endGfx = 85; } } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 7372: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 12) { if
	 * (c.getItems().playerHasItem(12830, 1)) { c.getItems().deleteItem2(12830,
	 * 1); c.getVariables().summoningSpecialPoints -= 12; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70, 1347,
	 * 41, 28, c.oldPlayerIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(7995); NPCHandler.npcs[i].gfx0(1346);
	 * damage = Misc.random(7); c.specHitTimer = 3;
	 * c.getPA().addSkillXP(182,23); c.getPA().refreshSkill(23); endGfx = 1348;
	 * } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6839: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12451, 1)) { c.getItems().deleteItem2(12451,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * NPCHandler.npcs[i].gfx100(1405); c.getPA().createPlayersProjectile(famX,
	 * famY, poffX, poffY, 50, 70, 1406, 41, 28, c.oldPlayerIndex + 1, 5,
	 * c.heightLevel); NPCHandler.npcs[i].startAnimation(4921); damage =
	 * Misc.random(15); c.specHitTimer = 3; if (damage == 0) { endGfx = 85; }
	 * else { endGfx = 1407; } } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6849: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12449, 1)) { c.getItems().deleteItem2(12449,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70, 1352,
	 * 41, 28, c.getVariables().playerIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8118); NPCHandler.npcs[i].gfx0(1353);
	 * damage = Misc.random(14); damage2 = Misc.random(14); doubleHit = true;
	 * c.specHitTimer = 3; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6798: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12450, 1)) { c.getItems().deleteItem2(12450,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * NPCHandler.npcs[i].startAnimation(8071); NPCHandler.npcs[i].gfx0(1353);
	 * damage = Misc.random(17); c.specHitTimer = 3; endGfx = 1353; } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 7341: // lava titan if (c.getVariables().familiarIndex > 0) {
	 * return; } else { if (c.getVariables().summoningSpecialPoints >= 4) { if
	 * (c.getItems().playerHasItem(12837, 1)) { c.getItems().deleteItem2(12837,
	 * 1); c.getVariables().summoningSpecialPoints -= 4; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70, 1493,
	 * 41, 28, c.getVariables().playerIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(7986); n.gfx0(1492); damage =
	 * Misc.random(20); c.specHitTimer = 3; c.getPA().addSkillXP(241,23);
	 * c.getPA().refreshSkill(23); endGfx = 1494; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 7329: // swamp titan if (c.getVariables().familiarIndex > 0) {
	 * return; } else { if (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12832, 1)) { c.getItems().deleteItem2(12832,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70, 1462,
	 * 41, 28, c.getVariables().playerIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8223); damage = Misc.random(21);
	 * c.getPA().crabSpecBonus(c.getVariables().playerHerblore, false);
	 * c.getPA().refreshSkill(c.getVariables().playerHerblore); c.specHitTimer =
	 * 3; c.getPA().addSkillXP(241,23); c.getPA().refreshSkill(23); endGfx =
	 * 1460; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 7347: // talon beast if (c.getVariables().familiarIndex > 0) {
	 * return; } else { if (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12831, 1)) { c.getItems().deleteItem2(12831,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; c.startAnimation(7660);
	 * c.gfx0(1316); NPCHandler.npcs[c.getVariables().familiarIndex].attackTimer
	 * = 3; c.getVariables().familiarIndex += 6; hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70, 1520,
	 * 41, 28, c.getVariables().playerIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8043); n.gfx0(1519); damage =
	 * Misc.random(8); damage2 = Misc.random(8); doubleHit = true;
	 * c.specHitTimer = 3; c.getPA().addSkillXP(395,23);
	 * c.getPA().refreshSkill(23); if (damage == 0 || damage2 == 0) { endGfx =
	 * 85; } } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * case 6800: if (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12457, 1)) { c.getItems().deleteItem2(12457,
	 * 1); c.getVariables().summoningSpecialPoints -= 6; typicalStuff(c);
	 * hitType = 2; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70, 1362,
	 * 41, 28, c.getVariables().playerIndex + 1, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(7852); damage = Misc.random(19);
	 * c.specHitTimer = 3; endGfx = 1363; } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } }
	 * break;
	 * 
	 * } }
	 * 
	 * 
	 * public void initializeHit() { if (c.getVariables().npcIndex > 0) { if
	 * (damage > 0) { if (drain == 0) {
	 * NPCHandler.npcs[c.getVariables().npcIndex].attack -= (damage / 2); } else
	 * if (drain == 1) { NPCHandler.npcs[c.getVariables().npcIndex].defence -=
	 * (damage / 2); } else if (drain == 2) {
	 * NPCHandler.npcs[c.getVariables().npcIndex].maxHit -= (damage / 2); } } if
	 * (damage > NPCHandler.npcs[c.getVariables().npcIndex].HP) { damage =
	 * NPCHandler.npcs[c.getVariables().npcIndex].HP; } if (damage2 >
	 * NPCHandler.npcs[c.getVariables().npcIndex].HP) { damage2 =
	 * NPCHandler.npcs[c.getVariables().npcIndex].HP; }
	 * NPCHandler.npcs[c.getVariables().npcIndex].gfx100(endGfx);
	 * NPCHandler.npcs[c.getVariables().npcIndex].hitDiff2 = damage;
	 * NPCHandler.npcs[c.getVariables().npcIndex].HP -= damage;
	 * NPCHandler.npcs[c.getVariables().npcIndex].hitUpdateRequired2 = true; if
	 * (doubleHit && damage2 != -1) {
	 * NPCHandler.npcs[c.getVariables().npcIndex].hitDiff = damage2;
	 * NPCHandler.npcs[c.getVariables().npcIndex].HP -= damage2;
	 * NPCHandler.npcs[c.getVariables().npcIndex].hitUpdateRequired = true;
	 * doubleHit = false; }
	 * NPCHandler.npcs[c.getVariables().npcIndex].updateRequired = true; } else
	 * if (c.getVariables().playerIndex > 0) { Player o = (Player)
	 * PlayerHandler.getVariables().players[c.getVariables().playerIndex]; if
	 * (hitType == 0 && o.getVariables().prayerActive[14]) { damage = 0; if
	 * (damage2 != 1) damage2 = 0; } if (hitType == 1 &&
	 * o.getVariables().prayerActive[13]) { damage = 0; if (damage2 != 1)
	 * damage2 = 0; } if (hitType == 2 && o.getVariables().prayerActive[12]) {
	 * damage = 0; if (damage2 != 1) damage2 = 0; } if (damage > 0) { if (drain
	 * == 0) { c.sendMessage("You feel inaccurate...");
	 * o.getVariables().playerLevel[0] -= (damage / 2); } else if (drain == 1) {
	 * c.sendMessage("You feel defenceless..."); o.getVariables().playerLevel[1]
	 * -= (damage / 2); } else if (drain == 2) { c.sendMessage(
	 * "You feel weakened..."); o.getVariables().playerLevel[2] -= (damage / 2);
	 * } } if (damage > o.getVariables().playerLevel[3]) { damage =
	 * o.getVariables().playerLevel[3]; } if (damage2 >
	 * o.getVariables().playerLevel[3]) { damage2 =
	 * o.getVariables().playerLevel[3]; } o.gfx100(endGfx); int soak =
	 * o.getCombat().damageSoaked(damage, soakType(c.getVariables().npcIndex));
	 * damage2 -= soak; o.getCombat().appendHit(c, damage, 0,
	 * NPCHandler.npcs[c.getVariables().npcIndex].attackType, false); if
	 * (doubleHit && damage2 != -1) { int soak2 =
	 * o.getCombat().damageSoaked(damage2, soakType(c.getVariables().npcIndex));
	 * damage2 -= soak2; o.getCombat().appendHit(c, damage2, 0,
	 * NPCHandler.npcs[c.getVariables().npcIndex].attackType, false); }
	 * o.getPA().refreshSkill(3); } }
	 * 
	 * public String soakType(int i) { if(NPCHandler.npcs[i].attackType == 0)
	 * return "Melee"; if(NPCHandler.npcs[i].attackType == 1) return "Range";
	 * if(NPCHandler.npcs[i].attackType == 2) { return "Magic"; } return ""; }
	 * 
	 * private static void redSpiderSpec(Player c) { if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12428, 1)) { typicalStuff(c);
	 * c.getItems().deleteItem(12428, c.getItems().getItemSlot(12428), 1);
	 * c.getVariables().summoningSpecialPoints -= 6; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * NPCHandler.npcs[c.getVariables().familiarIndex].startAnimation(8164);
	 * c.getPA().addSkillXP(7, 23); c.getPA().refreshSkill(23); if
	 * (Misc.random(2) == 0) { c.getPA().stillGfx(1342, c.getX() - 1, c.getY(),
	 * c.heightLevel, 3); ItemHandler.createGroundItem(c, 223, c.getX() - 1,
	 * c.getY(), 1, c.getVariables().playerId); } if (Misc.random(2) == 0) {
	 * c.getPA().stillGfx(1342, c.getX() + 1, c.getY(), c.heightLevel, 3);
	 * ItemHandler.createGroundItem(c, 223, c.getX() + 1, c.getY(), 1,
	 * c.getVariables().playerId); } if (Misc.random(2) == 0) {
	 * c.getPA().stillGfx(1342, c.getX(), c.getY() - 1, c.heightLevel, 3);
	 * ItemHandler.createGroundItem(c, 223, c.getX(), c.getY() - 1, 1,
	 * c.getVariables().playerId); } if (Misc.random(2) == 0) {
	 * c.getPA().stillGfx(1342, c.getX() - 1, c.getY() - 1, c.heightLevel, 3);
	 * ItemHandler.createGroundItem(c, 223, c.getX() - 1, c.getY() - 1, 1,
	 * c.getVariables().playerId); } if (Misc.random(2) == 0) {
	 * c.getPA().stillGfx(1342, c.getX() + 1, c.getY() + 1, c.heightLevel, 3);
	 * ItemHandler.createGroundItem(c, 223, c.getX() + 1, c.getY() + 1, 1,
	 * c.getVariables().playerId); } if (Misc.random(2) == 0) {
	 * c.getPA().stillGfx(1342, c.getX() + 1, c.getY() - 1, c.heightLevel, 3);
	 * ItemHandler.createGroundItem(c, 223, c.getX() + 1, c.getY() - 1, 1,
	 * c.getVariables().playerId); } if (Misc.random(2) == 0) {
	 * c.getPA().stillGfx(1342, c.getX() - 1, c.getY() + 1, c.heightLevel, 3);
	 * ItemHandler.createGroundItem(c, 223, c.getX() - 1, c.getY() + 1, 1,
	 * c.getVariables().playerId); } } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * private static void fruitFall(Player c) { if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12423, 1)) { typicalStuff(c);
	 * c.getItems().deleteItem(12423, c.getItems().getItemSlot(12423), 1);
	 * c.getVariables().summoningSpecialPoints -= 6; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * NPCHandler.npcs[c.getVariables().familiarIndex].startAnimation(8320);
	 * c.getPA().addSkillXP(49,23); c.getPA().refreshSkill(23); if
	 * (Misc.random(2) == 0) { c.getPA().stillGfx(1331, c.getX() - 1, c.getY(),
	 * c.heightLevel, 3); ItemHandler.createGroundItem(c, randomDrop(), c.getX()
	 * - 1, c.getY(), 1, c.getVariables().playerId); } if (Misc.random(2) == 0)
	 * { c.getPA().stillGfx(1331, c.getX() + 1, c.getY(), c.heightLevel, 3);
	 * ItemHandler.createGroundItem(c, randomDrop(), c.getX() + 1, c.getY(), 1,
	 * c.getVariables().playerId); } if (Misc.random(2) == 0) {
	 * c.getPA().stillGfx(1331, c.getX(), c.getY() - 1, c.heightLevel, 3);
	 * ItemHandler.createGroundItem(c, randomDrop(), c.getX(), c.getY() - 1, 1,
	 * c.getVariables().playerId); } if (Misc.random(2) == 0) {
	 * c.getPA().stillGfx(1331, c.getX() - 1, c.getY() - 1, c.heightLevel, 3);
	 * ItemHandler.createGroundItem(c, randomDrop(), c.getX() - 1, c.getY() - 1,
	 * 1, c.getVariables().playerId); } if (Misc.random(2) == 0) {
	 * c.getPA().stillGfx(1331, c.getX() + 1, c.getY() + 1, c.heightLevel, 3);
	 * ItemHandler.createGroundItem(c, randomDrop(), c.getX() + 1, c.getY() + 1,
	 * 1, c.getVariables().playerId); } if (Misc.random(2) == 0) {
	 * c.getPA().stillGfx(1331, c.getX() + 1, c.getY() - 1, c.heightLevel, 3);
	 * ItemHandler.createGroundItem(c, randomDrop(), c.getX() + 1, c.getY() - 1,
	 * 1, c.getVariables().playerId); } if (Misc.random(2) == 0) {
	 * c.getPA().stillGfx(1331, c.getX() - 1, c.getY() + 1, c.heightLevel, 3);
	 * ItemHandler.createGroundItem(c, randomDrop(), c.getX() - 1, c.getY() + 1,
	 * 1, c.getVariables().playerId); } } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * static int randomDrop() { return itemsDrop[(int) (Math.random() *
	 * itemsDrop.length)]; }
	 * 
	 * static int randomDrop1() { return itemsDrop1[(int) (Math.random() *
	 * itemsDrop1.length)]; }
	 * 
	 * static int randomDrop2() { return itemsDrop2[(int) (Math.random() *
	 * itemsDrop2.length)]; }
	 * 
	 * static int randomDrop3() { return itemsDrop3[(int) (Math.random() *
	 * itemsDrop3.length)]; }
	 * 
	 * private static int itemsDrop[] = { 5972, 5982, 1963 }; private static int
	 * itemsDrop1[] = { 249, 251, 253, 255, 257, 259, 261, 263, 265, 267, 269 };
	 * private static int itemsDrop2[] = { 1623, 1621, 1619, 1617, 1623, 1621,
	 * 1619, 1623, 1621, 1623, 1631 }; private static int itemsDrop3[] = { 335,
	 * 359, 377 };
	 * 
	 * private static void graniteCrabSpec(int i, Player c) { if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 12) { if
	 * (c.getItems().playerHasItem(12533, 1)) { c.getItems().deleteItem(12533,
	 * c.getItems().getItemSlot(12533), 1); c.getVariables().familiarIndex += 6;
	 * c.getVariables().summoningSpecialPoints -= 12; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } c.startAnimation(7660);
	 * c.gfx0(1300); NPCHandler.npcs[i].startAnimation(8109);
	 * NPCHandler.npcs[i].gfx0(1326); c.getPA().crabSpecBonus(1, false);
	 * c.getPA().refreshSkill(1); c.getPA().addSkillXP(7,23);
	 * c.getPA().refreshSkill(23); } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * private static void scorpionSpec(int i, Player c) { if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12432, 1)) { c.getItems().deleteItem(12432,
	 * c.getItems().getItemSlot(12432), 1); c.getVariables().familiarIndex += 6;
	 * c.getVariables().summoningSpecialPoints -= 6; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } c.startAnimation(7660);
	 * c.gfx0(1300); c.getPA().scorpSpecBonus(0, false);
	 * c.getPA().refreshSkill(0); c.getPA().addSkillXP(7,23);
	 * c.getPA().refreshSkill(23); } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * public static void typicalStuff(Player c) { // typicalStuff(c);
	 * c.startAnimation(7660); c.gfx0(1316);
	 * NPCHandler.npcs[c.getVariables().familiarIndex].attackTimer = 5;
	 * c.getVariables().familiarIndex += 6; }
	 * 
	 * private static void ratSpec(int i, Player c) { int invSlotCount =
	 * c.getItems().freeSlots(); if (c.getVariables().familiarIndex > 0) {
	 * return; } else { if (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (invSlotCount >= 4) { if (c.getItems().playerHasItem(12430, 1)) {
	 * c.getItems().deleteItem(12430, c.getItems().getItemSlot(12430), 1);
	 * c.getItems().addItem(1985, Misc.random(4));
	 * NPCHandler.npcs[i].startAnimation(7907); NPCHandler.npcs[i].gfx0(1384);
	 * typicalStuff(c); c.getVariables().summoningSpecialPoints -= 6; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().addSkillXP(81,23); c.getPA().refreshSkill(23); } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You need to have at least 4 free inventory slots for the familiar to use it's special."
	 * ); } } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * private static void compostSpec(int i, Player c) { int invSlotCount =
	 * c.getItems().freeSlots(); if (c.getVariables().familiarIndex > 0) {
	 * return; } else { if (c.getVariables().summoningSpecialPoints >= 12) { if
	 * (invSlotCount >= 3) { if (c.getItems().playerHasItem(12440, 1)) {
	 * c.getItems().deleteItem(12440, c.getItems().getItemSlot(12440), 1);
	 * c.getItems().addItem(6034, Misc.random(2) + 1);
	 * NPCHandler.npcs[i].startAnimation(7771); NPCHandler.npcs[i].gfx0(1425);
	 * typicalStuff(c); c.getVariables().summoningSpecialPoints -= 12; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().addSkillXP(88,23); c.getPA().refreshSkill(23); } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You need to have at least 3 free inventory slots for the familiar to use it's special."
	 * ); } } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * private static void massiveRefresh(Player c) { c.getPA().refreshSkill(0);
	 * c.getPA().refreshSkill(1); c.getPA().refreshSkill(2);
	 * c.getPA().refreshSkill(4); c.getPA().refreshSkill(6); }
	 * 
	 * private static void badgerBonus(Player c) { c.getPA().scorpSpecBonus(0,
	 * false); c.getPA().scorpSpecBonus(2, false); c.getPA().scorpSpecBonus(4,
	 * false); c.getPA().scorpSpecBonus(6, false); }
	 * 
	 * private static void badgerSpec(int i, Player c) { if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 4) { if
	 * (c.getItems().playerHasItem(12433, 1)) { c.getItems().deleteItem(12433,
	 * c.getItems().getItemSlot(12433), 1); c.getVariables().familiarIndex += 6;
	 * c.getVariables().summoningSpecialPoints -= 4; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } c.startAnimation(7660);
	 * NPCHandler.npcs[i].startAnimation(7930); NPCHandler.npcs[i].gfx0(1397);
	 * c.gfx0(1399); badgerBonus(c); c.getPA().addSkillXP(28,23);
	 * c.getPA().refreshSkill(23); massiveRefresh(c); } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * private static void beaverSpec(int i, Player c) { if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 3) { if
	 * (c.getItems().playerHasItem(12429, 1)) { if
	 * (c.getVariables().playerIsWoodcutting) { c.getItems().deleteItem(12429,
	 * c.getItems().getItemSlot(12429), 1);
	 * c.getVariables().summoningSpecialPoints -= 3; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } typicalStuff(c);
	 * NPCHandler.npcs[i].startAnimation(7722); c.getPA().addSkillXP(28,23);
	 * c.getPA().refreshSkill(23); // Server.npcHandler.npcs[i].gfx0(1397); int
	 * maybe = Misc.random(1); if (maybe == 1) {
	 * Woodcutting.recieveBirdsNest(c); c.sendMessage(
	 * "The beaver found a birds nest for you."); } } else { c.sendMessage(
	 * "You must be woodcutting to use this special."); } } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * private static void voidSpec(int i, Player c) { if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 3) { if
	 * (c.getItems().playerHasItem(12443, 1)) { if (DuelPlayer.contains(c)) {
	 * c.sendMessage("You can't teleport during a duel!"); return; } if
	 * (c.getVariables().inWild(c) && c.wildLevel >
	 * Constants.NO_TELEPORT_WILD_LEVEL) { c.sendMessage(
	 * "You can't teleport above level " + Constants.NO_TELEPORT_WILD_LEVEL +
	 * " in the wilderness."); return; } if (System.currentTimeMillis() -
	 * c.teleBlockDelay < c.teleBlockLength) { c.sendMessage(
	 * "You are teleblocked and can't teleport."); return; }
	 * c.getVariables().familiarIndex += 6; c.getPA().startTeleport(2662, 2650,
	 * 0, "modern"); c.getItems().deleteItem(12443,
	 * c.getItems().getItemSlot(12443), 1);
	 * c.getVariables().summoningSpecialPoints -= 3; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().addSkillXP(28,23); c.getPA().refreshSkill(23); } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * private static void bullAntSpec(int i, Player c) { if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 12) { if
	 * (c.getItems().playerHasItem(12431, 1)) { c.getItems().deleteItem(12431,
	 * c.getItems().getItemSlot(12431), 1);
	 * c.getVariables().summoningSpecialPoints -= 12; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } c.startAnimation(7660);
	 * c.gfx0(1300); NPCHandler.npcs[c.getVariables().familiarIndex].attackTimer
	 * = 5; c.getVariables().familiarIndex += 6; c.runEnergy +=
	 * c.getPA().getLevelForXP(c.getVariables().playerXP[16]) / 2; if
	 * (c.runEnergy > 100) c.runEnergy = 100; c.getPA().sendFrame126(c.runEnergy
	 * + "%", 149); NPCHandler.npcs[i].startAnimation(7895);
	 * c.getPA().addSkillXP(30,23); c.getPA().refreshSkill(23);
	 * NPCHandler.npcs[i].gfx0(1382); } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * private static void herbSpec(int i, Player c) { if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 12) { if
	 * (c.getItems().playerHasItem(12422, 1)) { c.getItems().deleteItem(12422,
	 * c.getItems().getItemSlot(12422), 1);
	 * c.getVariables().summoningSpecialPoints -= 12; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } typicalStuff(c);
	 * NPCHandler.npcs[c.getVariables().familiarIndex].attackTimer = 5;
	 * NPCHandler.npcs[i].startAnimation(8013); c.getPA().addSkillXP(79,23);
	 * c.getPA().refreshSkill(23); if (Misc.random(1) == 0) {
	 * c.getPA().stillGfx(1321, c.getX(), c.getY() - 1, c.heightLevel, 3);
	 * ItemHandler.createGroundItem(c, randomDrop1(), c.getX(), c.getY() - 1, 1,
	 * c.getVariables().playerId); } } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * private static void pyreSpec(int i, Player c) { if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12829, 1)) { if
	 * (c.getItems().playerHasItem(444, 1)) { c.getItems().deleteItem(12829,
	 * c.getItems().getItemSlot(12829), 1); c.getItems().deleteItem(444,
	 * c.getItems().getItemSlot(444), 1); c.getItems().addItem(2357, 1);
	 * c.startAnimation(7660); c.gfx0(1463); c.getVariables().familiarIndex +=
	 * 6; c.getVariables().summoningSpecialPoints -= 6; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * NPCHandler.npcs[i].startAnimation(8082); c.getPA().addSkillXP(75,23);
	 * c.getPA().refreshSkill(23); } else { c.sendMessage(
	 * "You must have gold ore to be able to use this special"); } } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * private static void leechSpec(int i, Player c) { if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12444, 1)) { c.getItems().deleteItem(12444,
	 * c.getItems().getItemSlot(12444), 1); c.startAnimation(7660);
	 * c.gfx0(1416); c.getVariables().familiarIndex += 6;
	 * c.getVariables().summoningSpecialPoints -= 6; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } int adding =
	 * Misc.random(7); c.getVariables().playerLevel[3] += adding; if
	 * (c.getVariables().playerLevel[3] > c.getPA().getLevelForXP(
	 * c.getVariables().playerXP[3])) { c.getVariables().playerLevel[3] =
	 * c.getPA().getLevelForXP( c.getVariables().playerXP[3]); }
	 * NPCHandler.npcs[i].startAnimation(7712); NPCHandler.npcs[i].gfx0(1418);
	 * c.getPA().addSkillXP(75,23); c.getPA().refreshSkill(23);
	 * c.getPA().refreshSkill(3); } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * private static void terrorSpec(int i, Player c) { if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 8) { if
	 * (c.getItems().playerHasItem(12441, 1)) { c.getItems().deleteItem(12441,
	 * c.getItems().getItemSlot(12441), 1); c.startAnimation(7660);
	 * c.gfx0(1300); c.getVariables().familiarIndex += 6;
	 * c.getVariables().summoningSpecialPoints -= 8; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getVariables().playerLevel[16] += 2; if
	 * (c.getVariables().playerLevel[16] > c.getPA().getLevelForXP(
	 * c.getVariables().playerXP[16]) + 2) c.getVariables().playerLevel[16] =
	 * c.getPA().getLevelForXP( c.getVariables().playerXP[16]) + 2; c.runEnergy
	 * += c.getPA().getLevelForXP(c.getVariables().playerXP[16]) / 2; if
	 * (c.runEnergy > 100) c.runEnergy = 100; c.getPA().sendFrame126(c.runEnergy
	 * + "%", 149); NPCHandler.npcs[i].startAnimation(8229);
	 * NPCHandler.npcs[i].gfx0(1521); c.getPA().addSkillXP(75,23);
	 * c.getPA().refreshSkill(23); c.getPA().refreshSkill(16); } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * private static void abbySpec(int i, Player c) { if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 12) { if
	 * (c.getItems().playerHasItem(12454, 1)) { c.getItems().deleteItem(12454,
	 * c.getItems().getItemSlot(12454), 1); typicalStuff(c);
	 * c.getVariables().summoningSpecialPoints -= 12; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * NPCHandler.npcs[i].startAnimation(7672); NPCHandler.npcs[i].gfx0(1422);
	 * c.getPA().addSkillXP(75,23); c.getPA().refreshSkill(23);
	 * c.getDH().sendDialogues(1000, c.npcType); } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * private static void magpieSpec(int i, Player c) { int invSlotCount =
	 * c.getItems().freeSlots(); if (c.getVariables().familiarIndex > 0) {
	 * return; } else { if (c.getVariables().summoningSpecialPoints >= 3) { if
	 * (c.getItems().playerHasItem(12426, 1)) { if (invSlotCount > 1) {
	 * c.getItems().deleteItem(12426, c.getItems().getItemSlot(12426), 1);
	 * c.getItems().addItem(randomDrop2(), 1); c.startAnimation(7660);
	 * c.gfx0(1300); c.getVariables().familiarIndex += 6;
	 * c.getVariables().summoningSpecialPoints -= 3; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * NPCHandler.npcs[i].startAnimation(8009); NPCHandler.npcs[i].gfx0(1336);
	 * c.getPA().addSkillXP(177,23); c.getPA().refreshSkill(23); } else {
	 * c.sendMessage("You must have at least 1 free space in your inventory"); }
	 * } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * private static void ibisSpec(int i, Player c) { if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 12) { if
	 * (c.getItems().playerHasItem(12424, 1)) { typicalStuff(c);
	 * c.getItems().deleteItem(12424, c.getItems().getItemSlot(12424), 1);
	 * c.getVariables().summoningSpecialPoints -= 12; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * c.getPA().addSkillXP(124,23); c.getPA().refreshSkill(23);
	 * NPCHandler.npcs[i].startAnimation(8201); if (Misc.random(2) == 0) {
	 * c.getPA().stillGfx(1337, c.getX() - 1, c.getY(), c.heightLevel, 3);
	 * ItemHandler.createGroundItem(c, randomDrop3(), c.getX() - 1, c.getY(), 1,
	 * c.getVariables().playerId); } if (Misc.random(2) == 0) {
	 * c.getPA().stillGfx(1337, c.getX() + 1, c.getY(), c.heightLevel, 3);
	 * ItemHandler.createGroundItem(c, randomDrop3(), c.getX() + 1, c.getY(), 1,
	 * c.getVariables().playerId); } if (Misc.random(2) == 0) {
	 * c.getPA().stillGfx(1337, c.getX(), c.getY() - 1, c.heightLevel, 3);
	 * ItemHandler.createGroundItem(c, randomDrop3(), c.getX(), c.getY() - 1, 1,
	 * c.getVariables().playerId); } if (Misc.random(2) == 0) {
	 * c.getPA().stillGfx(1337, c.getX() - 1, c.getY() - 1, c.heightLevel, 3);
	 * ItemHandler.createGroundItem(c, randomDrop3(), c.getX() - 1, c.getY() -
	 * 1, 1, c.getVariables().playerId); } if (Misc.random(2) == 0) {
	 * c.getPA().stillGfx(1337, c.getX() + 1, c.getY() + 1, c.heightLevel, 3);
	 * ItemHandler.createGroundItem(c, randomDrop3(), c.getX() + 1, c.getY() +
	 * 1, 1, c.getVariables().playerId); } if (Misc.random(2) == 0) {
	 * c.getPA().stillGfx(1337, c.getX() + 1, c.getY() - 1, c.heightLevel, 3);
	 * ItemHandler.createGroundItem(c, randomDrop3(), c.getX() + 1, c.getY() -
	 * 1, 1, c.getVariables().playerId); } if (Misc.random(2) == 0) {
	 * c.getPA().stillGfx(1337, c.getX() - 1, c.getY() + 1, c.heightLevel, 3);
	 * ItemHandler.createGroundItem(c, randomDrop3(), c.getX() - 1, c.getY() +
	 * 1, 1, c.getVariables().playerId); } } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * private static void graahkSpec(int i, Player c) { if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 3) { if
	 * (c.getItems().playerHasItem(12835, 1)) { c.getItems().deleteItem(12835,
	 * c.getItems().getItemSlot(12835), 1);
	 * NPCHandler.npcs[c.getVariables().familiarIndex].attackTimer = 5;
	 * c.getVariables().familiarIndex += 6;
	 * c.getVariables().summoningSpecialPoints -= 3; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; }
	 * NPCHandler.npcs[i].startAnimation(7910); c.getPA().addSkillXP(196,23);
	 * c.getPA().refreshSkill(23); c.getPA().startTeleport(2818, 3012, 0,
	 * "modern"); } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * private static void lurkerSpec(final int i, final Player c) { if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 3) { if
	 * (c.getItems().playerHasItem(12427, 1)) { c.getItems().deleteItem(12427,
	 * c.getItems().getItemSlot(12427), 1); c.getVariables().familiarIndex += 6;
	 * c.getVariables().summoningSpecialPoints -= 3; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } c.startAnimation(7660);
	 * c.gfx0(1307); NPCHandler.npcs[i].startAnimation(7682);
	 * NPCHandler.npcs[i].gfx0(1339);
	 * c.getPA().crabSpecBonus(c.getVariables().playerAgility, false);
	 * c.getPA().crabSpecBonus(c.getVariables().playerThieving, false);
	 * c.getPA().refreshSkill(c.getVariables().playerAgility);
	 * c.getPA().refreshSkill(c.getVariables().playerThieving);
	 * c.getPA().addSkillXP(134,23); c.getPA().refreshSkill(23); } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * private static void cobraSpec(final int i, final Player c) { int
	 * invSlotCount = c.getItems().freeSlots(); if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 3) { if (invSlotCount >= 4) {
	 * if (c.getItems().playerHasItem(12436, 1)) {
	 * c.getItems().deleteItem(12436, c.getItems().getItemSlot(12436), 1);
	 * c.getItems().addItem(12109, Misc.random(4));
	 * NPCHandler.npcs[i].startAnimation(8159); NPCHandler.npcs[i].gfx0(1388);
	 * c.getVariables().familiarIndex += 6;
	 * c.getVariables().summoningSpecialPoints -= 3; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } c.startAnimation(7660);
	 * c.gfx0(1308); c.getPA().addSkillXP(181,23); c.getPA().refreshSkill(23); }
	 * else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You need to have at least 4 free inventory slots for the familiar to use it's special."
	 * ); } } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * private static void tortSpec(final int i, final Player c) { int famX =
	 * NPCHandler.npcs[i].absX; int famY = NPCHandler.npcs[i].absY; int pX =
	 * PlayerHandler.getVariables().players[c.getVariables().playerId].absX; int
	 * pY =
	 * PlayerHandler.getVariables().players[c.getVariables().playerId].absY; int
	 * poffX = (famX - pX) * -1; int poffY = (famX - pY) * -1; if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 20) { if
	 * (c.getItems().playerHasItem(12439, 1)) { c.getItems().deleteItem(12439,
	 * c.getItems().getItemSlot(12439), 1); c.getVariables().familiarIndex += 6;
	 * c.getVariables().summoningSpecialPoints -= 20; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } c.startAnimation(7660);
	 * c.gfx0(1302);
	 * 
	 * c.getPA().createPlayersProjectile(famX, famY, poffX, poffY, 50, 70, 1415,
	 * 41, 28, c.getVariables().playerId, 5, c.heightLevel);
	 * NPCHandler.npcs[i].startAnimation(8288); NPCHandler.npcs[i].gfx0(1414);
	 * c.getPA().crabSpecBonus(c.getVariables().playerDefence, true);
	 * c.getPA().refreshSkill(c.getVariables().playerDefence);
	 * c.getPA().addSkillXP(234,23); c.getPA().refreshSkill(23); } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * private static void startBunyipHealing(final Player c) { if
	 * (!c.getItems().playerHasItem(12438, 1)) { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); return; } if
	 * (c.getVariables().summoningSpecialPoints < 20) { c.sendMessage(
	 * "You do not have enough special points to cast this special."); return; }
	 * if (c.hasActivated) { c.sendMessage(
	 * "You have already activated this special, you must wait until it deactivates."
	 * ); return; } typicalStuff(c); c.getVariables().summoningSpecialPoints -=
	 * 20; if (c.getVariables().playerEquipment[c.getVariables().playerCape] ==
	 * 19893) { c.getVariables().summoningSpecialPoints += 2; } c.sendMessage(
	 * "The bunyip will slowly heal you for awhile now.");
	 * c.getItems().deleteItem2(12438, 1); c.hasActivated = true;
	 * CycleEventHandler.getInstance().addEvent(c, new CycleEvent() { int time =
	 * 15; int count = 0;
	 * 
	 * @Override public void execute(CycleEventContainer heal) { if
	 * (c.familiarID != 6813) { heal.stop(); } if (count == 25) { heal.stop(); }
	 * if (time > 0) { time--; }
	 * 
	 * if (time == 0) { c.constitution += 20; if(c.constitution >=
	 * c.calculateMaxLifePoints()) { c.constitution =
	 * c.calculateMaxLifePoints(); } c.gfx0(1507); c.getPA().refreshSkill(3);
	 * time = 15; count++;
	 * 
	 * } }
	 * 
	 * @Override public void stop() { c.sendMessage(
	 * "The effect of the bunyip wore off."); c.hasActivated = false; } }, 1); }
	 * 
	 * private static void golemSpec(final int i, final Player c) { if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 12) { if
	 * (c.getItems().playerHasItem(12826, 1)) { c.getItems().deleteItem(12826,
	 * c.getItems().getItemSlot(12826), 1); c.getVariables().familiarIndex += 6;
	 * c.getVariables().summoningSpecialPoints -= 12; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } c.startAnimation(7660);
	 * c.gfx0(1303); NPCHandler.npcs[i].startAnimation(8053);
	 * NPCHandler.npcs[i].gfx0(1465);
	 * c.getPA().crabSpecBonus(c.getVariables().playerStrength, true);
	 * c.getPA().refreshSkill(c.getVariables().playerStrength);
	 * c.getPA().addSkillXP(97,23); c.getPA().refreshSkill(23); } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * private static void threeTitanSpec(final int i, final Player c) { if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 20) { if
	 * (c.getItems().playerHasItem(12824, 1)) { c.getItems().deleteItem(12824,
	 * c.getItems().getItemSlot(12824), 1); c.getVariables().familiarIndex += 6;
	 * switch (c.familiarID) { case 7359:
	 * NPCHandler.npcs[i].startAnimation(7837); NPCHandler.npcs[i].gfx0(1512);
	 * break; case 7357: NPCHandler.npcs[i].startAnimation(7837);
	 * NPCHandler.npcs[i].gfx0(1513); break; case 7355:
	 * NPCHandler.npcs[i].startAnimation(7835); NPCHandler.npcs[i].gfx0(1514);
	 * break; } c.getVariables().summoningSpecialPoints -= 20; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } c.startAnimation(7660);
	 * c.gfx0(1313); c.getVariables().playerLevel[3] += 8; if
	 * (c.getVariables().playerLevel[3] >
	 * (c.getLevelForXP(c.getVariables().playerXP[3]) + 8)) {
	 * c.getVariables().playerLevel[3] =
	 * (c.getLevelForXP(c.getVariables().playerXP[3]) + 8); }
	 * c.getPA().crabSpecBonus(1, true); c.getPA().refreshSkill(3);
	 * c.getPA().refreshSkill(1); c.getPA().addSkillXP(497,23);
	 * c.getPA().refreshSkill(23); } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * private static void hydraSpec(final int i, final Player c) { if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 6) { if
	 * (c.getItems().playerHasItem(12442, 1)) { c.getItems().deleteItem(12442,
	 * c.getItems().getItemSlot(12442), 1); c.getVariables().familiarIndex += 6;
	 * c.getVariables().summoningSpecialPoints -= 6; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } c.startAnimation(7660);
	 * c.gfx0(1308); NPCHandler.npcs[i].startAnimation(7945);
	 * NPCHandler.npcs[i].gfx0(1487);
	 * c.getPA().crabSpecBonus(c.getVariables().playerWoodcutting, true);
	 * c.getPA().crabSpecBonus(c.getVariables().playerFarming, true);
	 * c.getPA().refreshSkill(c.getVariables().playerWoodcutting);
	 * c.getPA().refreshSkill(c.getVariables().playerFarming);
	 * c.getPA().addSkillXP(274,23); c.getPA().refreshSkill(23); } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * private static void unicornSpec(final int i, final Player c) { if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 20) { if
	 * (c.getItems().playerHasItem(12434, 1)) { if
	 * (c.getVariables().playerLevel[3] >
	 * c.getLevelForXP(c.getVariables().playerXP[3])) { c.sendMessage(
	 * "You cannot heal any higher at this moment."); return; }
	 * c.getItems().deleteItem(12434, c.getItems().getItemSlot(12434), 1);
	 * c.getVariables().familiarIndex += 6;
	 * c.getVariables().summoningSpecialPoints -= 20; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } c.startAnimation(7660);
	 * c.gfx0(1298); c.getVariables().playerLevel[3] *= 1.15;
	 * c.getPA().refreshSkill(3); NPCHandler.npcs[i].startAnimation(8267);
	 * NPCHandler.npcs[i].gfx0(1356); c.getPA().addSkillXP(56,23);
	 * c.getPA().refreshSkill(23); } else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * private static void magicFocus(final int i, final Player c) { if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 20) { if
	 * (c.getItems().playerHasItem(12437, 1)) { c.getItems().deleteItem(12437,
	 * c.getItems().getItemSlot(12437), 1); c.getVariables().familiarIndex += 6;
	 * c.getVariables().summoningSpecialPoints -= 20; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } c.startAnimation(7660);
	 * c.gfx0(1296); NPCHandler.npcs[i].startAnimation(8308);
	 * NPCHandler.npcs[i].gfx0(1464); c.getVariables().playerLevel[6] =
	 * c.getLevelForXP(c.getVariables().playerXP[6]) + 7;
	 * c.getPA().refreshSkill(c.getVariables().playerMagic);
	 * c.getPA().addSkillXP(417,23); c.getPA().refreshSkill(23); } else {
	 * c.sendMessage("You do not have the correct scroll for this familiar."); }
	 * } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 * 
	 * private static void abby2Spec(final int i, final Player c) { int
	 * invSlotCount = c.getItems().freeSlots(); if
	 * (c.getVariables().familiarIndex > 0) { return; } else { if
	 * (c.getVariables().summoningSpecialPoints >= 20) { if (invSlotCount >= 4)
	 * { if (c.getItems().playerHasItem(12827, 1)) {
	 * c.getItems().deleteItem(12827, c.getItems().getItemSlot(12827), 1);
	 * c.getItems().addItem(7936, Misc.random(4));
	 * NPCHandler.npcs[i].startAnimation(7698); NPCHandler.npcs[i].gfx0(1457);
	 * c.getVariables().familiarIndex += 6;
	 * c.getVariables().summoningSpecialPoints -= 20; if
	 * (c.getVariables().playerEquipment[c.getVariables().playerCape] == 19893)
	 * { c.getVariables().summoningSpecialPoints += 2; } c.startAnimation(7660);
	 * c.gfx0(1308); c.getPA().addSkillXP(585,23); c.getPA().refreshSkill(23); }
	 * else { c.sendMessage(
	 * "You do not have the correct scroll for this familiar."); } } else {
	 * c.sendMessage(
	 * "You need to have at least 4 free inventory slots for the familiar to use it's special."
	 * ); } } else { c.sendMessage(
	 * "You do not have enough special points to cast this special."); } } }
	 */

}