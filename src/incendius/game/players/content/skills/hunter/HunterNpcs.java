package incendius.game.players.content.skills.hunter;

import incendius.game.npcs.NPC;
import incendius.game.npcs.NPCHandler;
import incendius.util.Misc;
import incendius.world.map.Region;

/**
 * @author Vegas/Linus/Flux/Jolt/KFC/Tinderbox/Jack Daniels <- Same Person
 *
 **/

public class HunterNpcs {

	static int kinglyImpling = 0, dragonImpling = 0, pirateImpling = 0, ninjaImpling = 0, magpieImpling = 0,
			natureImpling = 0, eclecticImpling = 0, essenceImpling = 0, earthImpling = 0, gourmetImpling = 0,
			youngImpling = 0, babyImpling = 0, impSpawned = 0;

	public static void process() {
		if (kinglyImpling < 5)
			spawnNewImp(true, (Misc.random(20) >= 10 ? 7903 : 7906));
		if (dragonImpling < 10)
			spawnNewImp(true, 6064);
		if (pirateImpling < 11)
			spawnNewImp(true, (Misc.random(20) >= 10 ? 7846 : 7845));
		if (ninjaImpling < 12)
			spawnNewImp(true, 6063);
		if (magpieImpling < 14)
			spawnNewImp(true, (Misc.random(20) >= 10 ? 1035 : 6062));
		if (natureImpling < 15)
			spawnNewImp(true, (Misc.random(20) >= 10 ? 1034 : 6061));
		HunterNpcs.spawnNewImp(false, -1);
	}

	public static int getRandomImp() {
		int max = Imps.ImpIds.length - 1;
		int r1 = Misc.random(max);
		int r2 = Misc.random(max);
		int min = Math.min(r1, r2);
		return Imps.ImpIds[min];
	}

	public static void hunterStartup() {
		for (int i = 0; i < 1005; i++) {
			spawnNewImp(false, -1);
		}
	}

	public static void spawnNewImp(boolean custom, int implingId) {
		if (impSpawned > 280 && !custom)
			return;
		int x = 2450 + Misc.random(1100);
		int y = 3150 + Misc.random(450);
		for (int i = 0; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] != null) {
				if (NPCHandler.goodDistance(x, y, NPCHandler.npcs[i].absX, NPCHandler.npcs[i].absY, 10)) {
					return;
				}
			}
		}
		if (!((Region.getClipping(x, y, 0) & 0x1280180) == 0)) {
			return;
		}
		int slot = -1;
		for (int a = 1; a < NPCHandler.maxNPCs; a++) {
			if (NPCHandler.npcs[a] == null) {
				slot = a;
				break;
			}
		}
		if (slot == -1) {
			return;
		}
		int npcId = -1;
		if (custom)
			npcId = implingId;
		else
			npcId = getRandomImp();
		NPC newNPC = new NPC(slot, npcId, NPCHandler.getNpcListName(npcId));
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = 0;
		newNPC.walkingType = 1;
		newNPC.HP = 1;
		newNPC.MaxHP = 1;
		newNPC.maxHit = 1;
		newNPC.attack = 1;
		newNPC.defence = 1;
		NPCHandler.npcs[slot] = newNPC;
		impSpawned++;
		HunterGui.newImpling(implingName(newNPC.npcType, 1), false, newNPC.absX, newNPC.absY, newNPC);
	}

	public static void removeImp(NPC imp) {
		HunterGui.setCaught(imp.columnId, imp.npcType);
		imp.isDead = true;
		imp.noDeathEmote = true;
		imp.updateRequired = true;
		impSpawned--;
	}

	public static String implingName(int npcId, int i) {
		switch (npcId) {
		case 1028:
		case 6055:
			babyImpling += i;
			return "Baby Impling";
		case 1029:
		case 6056:
			youngImpling += i;
			return "Young Impling";
		case 1030:
		case 6057:
			gourmetImpling += i;
			return "Gourmet Impling";
		case 1031:
		case 6058:
			earthImpling += i;
			return "Earth Impling";
		case 1032:
		case 6059:
			essenceImpling += i;
			return "Essence Impling";
		case 1033:
		case 6060:
			eclecticImpling += i;
			return "Eclectic Impling";
		case 1034:
		case 6061:
			natureImpling += i;
			return "Nature Impling";
		case 1035:
		case 6062:
			magpieImpling += i;
			return "Magpie Impling";
		case 6063:
			ninjaImpling += i;
			return "Ninja Impling";
		case 7845:
		case 7846:
			pirateImpling += i;
			return "Pirate Impling";
		case 6064:
			dragonImpling += i;
			return "Dragon Impling";
		case 7903:
		case 7906:
			kinglyImpling += i;
			return "Kingly Impling";
		}
		return "";
	}
}