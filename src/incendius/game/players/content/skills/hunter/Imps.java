package incendius.game.players.content.skills.hunter;

import incendius.game.npcs.NPC;
import incendius.game.players.Player;
import incendius.util.Misc;

/**
 * @author Vegas/Linus/Flux/Jolt/KFC/Tinderbox/Jack Daniels <- Same Person
 *
 **/
public class Imps {

	public static final int[] ImpIds = { 6055, 6055, 6055, 6055, 6055, 6055, 1028, 1028, 1028, 1028, 6056, 6056, 6056,
			6056, 6056, 6056, 1029, 1029, 1029, 6057, 6057, 6057, 6057, 6057, 6057, 1030, 1030, 1030, 6058, 6058, 6058,
			6058, 6058, 1031, 1031, 1031, 1032, 1032, 1032, 1032, 1032, 6059, 6059, 6059, 1033, 1033, 1033, 1033, 6060,
			6060, 6060, 1034, 1034, 1034, 1034, 6061, 6061, 6061, 1035, 1035, 1035, 6062, 6062, 6062, 6063, 6063, 6063,
			6063, 6063, 6063, 7845, 7845, 7846, 7846, 7846, 6064, 6064, 6064, 6064, 6064, 7903, 7906, 7906, 7903 };

	public static void addExp(Player p, int NpcId) {
		if (p.getInventory().freeSlots() == 0)
			return;
		switch (NpcId) {
		case 1028:// baby impling
		case 6055:// Baby impling
			p.getPA().addSkillXP(1000, 22);
			break;
		case 1029:// Young impling
		case 6056:// Young impling
			p.getPA().addSkillXP(1500, 22);
			break;
		case 1030:// Gourmet impling
		case 6057:// Gourmet impling
			p.getPA().addSkillXP(2100, 22);
			break;
		case 1031:// Earth impling
		case 6058:// Earth impling
			p.getPA().addSkillXP(2500, 22);
			break;
		case 1032:// Essence impling
		case 6059:// Essence impling
			p.getPA().addSkillXP(3600, 22);
			break;
		case 1033:// Eclectic impling
		case 6060:// Eclectic impling
			p.getPA().addSkillXP(7503, 22);
			break;
		case 1034:// Nature impling
		case 6061:// Nature impling
			p.getPA().addSkillXP(15500, 22);
			break;
		case 1035:// Magpie impling
		case 6062:// Magpie impling
			p.getPA().addSkillXP(26000, 22);
			break;
		case 6063:// Ninja impling
			p.getPA().addSkillXP(56000, 22);
			break;
		case 7845:// Pirate impling
		case 7846:// Pirate impling
			p.getPA().addSkillXP(61000, 22);
			break;
		case 6064:// Dragon impling
			p.getPA().addSkillXP(85400, 22);
			break;
		case 7903:// Kingly Imp
		case 7906:
			p.getPA().addSkillXP(104000, 22);
			break;

		}
	}

	public static void addItem(Player p, int NpcId) {
		int itemId = -1;
		if (p.getInventory().freeSlots() == 0 && !p.getItems().playerHasItem(11260)) {
			p.sendMessage("You need a free slot.");
			return;
		}
		switch (NpcId) {
		case 1028:// baby impling
		case 6055:// Baby impling
			itemId = 11238;
			break;
		case 1029:// Young impling
		case 6056:// Young impling
			itemId = 11240;
			break;
		case 1030:// Gourmet impling
		case 6057:// Gourmet impling
			itemId = 11242;
			break;
		case 1031:// Earth impling
		case 6058:// Earth impling
			itemId = 11244;
			break;
		case 1032:// Essence impling
		case 6059:// Essence impling
			itemId = 11246;
			break;
		case 1033:// Eclectic impling
		case 6060:// Eclectic impling
			itemId = 11248;
			break;
		case 1034:// Nature impling
		case 6061:// Nature impling
			itemId = 11250;
			break;
		case 1035:// Magpie impling
		case 6062:// Magpie impling
			itemId = 11252;
			break;
		case 6063:// Ninja impling
			itemId = 11254;
			break;
		case 7845:// Pirate impling
		case 7846:// Pirate impling
			itemId = 13337;
			break;
		case 6064:// Dragon impling
			itemId = 11256;
			break;
		case 7903:// Kingly Imp
		case 7906:
			itemId = 15517;
			break;
		}
		if (!p.getItems().playerHasItem(11260) && itemId > 0) {
			HunterLooting.giveLoot(p, itemId, true);
			return;
		}
		if (itemId > 0) {
			p.getItems().deleteItem(11260, 1);
			p.getItems().addItem(itemId, 1);
		}
	}

	public static int getReq(int NpcId) {
		switch (NpcId) {
		case 1029:// Young impling
		case 6056:// Young impling
			return 22;
		case 1030:// Gourmet impling
		case 6057:// Gourmet impling
			return 28;
		case 1031:// Earth impling
		case 6058:// Earth impling
			return 36;
		case 1032:// Essence impling
		case 6059:// Essence impling
			return 42;
		case 1033:// Eclectic impling
		case 6060:// Eclectic impling
			return 50;
		case 1034:// Nature impling
		case 6061:// Nature impling
			return 58;
		case 1035:// Magpie impling
		case 6062:// Magpie impling
			return 65;
		case 6063:// Ninja impling
			return 74;
		case 7845:// Pirate impling
		case 7846:// Pirate impling
			return 76;
		case 6064:// Dragon impling
			return 83;
		case 7903:// Kingly Imp
		case 7906:
			return 91;
		}
		return 0;
	}

	public static boolean catchImp(final Player p, final NPC n) {
		switch (n.npcType) {
		case 1028:// baby impling
		case 6055:// Baby impling

		case 1029:// Young impling
		case 6056:// Young impling

		case 1030:// Gourmet impling
		case 6057:// Gourmet impling

		case 1031:// Earth impling
		case 6058:// Earth impling

		case 1032:// Essence impling
		case 6059:// Essence impling

		case 1033:// Eclectic impling
		case 6060:// Eclectic impling

		case 1034:// Nature impling
		case 6061:// Nature impling

		case 1035:// Magpie impling
		case 6062:// Magpie impling

		case 6063:// Ninja impling

		case 7845:// Pirate impling
		case 7846:// Pirate impling

		case 6064:// Dragon impling

		case 7903:// Kingly Imp
		case 7906:
			if (n.caught)
				return true;
			if (System.currentTimeMillis() - p.getInstance().skillTimer < 1500) {
				return true;
			}
			p.getInstance().skillTimer = System.currentTimeMillis();
			if (!p.goodDistance(p.getX(), p.getY(), n.getX(), n.getY(), 2))
				return true;
			if ((p.getInstance().playerEquipment[p.getInstance().playerWeapon] != 11259
					&& p.getInstance().playerEquipment[p.getInstance().playerWeapon] != 10010)) {
				p.sendMessage("You need a net to catch imps!");
				return true;
			}
			if (p.getInstance().playerLevel[22] < getReq(n.npcType)) {
				p.sendMessage("You need a hunter level of " + getReq(n.npcType) + " to catch this impling.");
				return true;
			}
			p.turnPlayerTo(n.absX, n.absY);
			p.startAnimation(5209);
			if (Misc.random(10) >= ((p.getInstance().playerLevel[22] - 10) / 10) + 1) {
				p.sendMessage("You fail catching it.");
				return true;
			}
			if (!n.caught) {
				n.caught = true;
				p.turnPlayerTo(n.absX, n.absY);
				p.sendMessage("You catch the impling!");
				addItem(p, n.npcType);
				addExp(p, n.npcType);
				HunterNpcs.removeImp(n);
				return true;
			}
		}
		return false;
	}

}