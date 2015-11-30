package incendius.game.players.content.skills.hunter;

import incendius.game.players.Player;
import incendius.util.Misc;

/**
 * @author Vegas/Linus/Flux/Jolt/KFC/Tinderbox/Jack Daniels <- Same Person
 *
 **/

public class HunterLooting {

	private static final int[][] BabyLoots = { { 1438, 1 }, { 319, 1 }, { 1759, 1 }, { 1777, 1 }, { 1985, 1 },
			{ 1755, 1 }, { 1779, 1 }, { 2347, 1 }, { 1743, 1 }, { 863, 1 }, { 379, 1 }, { 7170, 1 }, { 1733, 1 },
			{ 1607, 1 }, { 401, 1 }, { 2355, 1 }, { 1761, 1 }, { 2007, 1 }, { 1734, 1 }, };

	private static final int[][] YoungLoots = { { 1353, 1 }, { 855, 1 }, { 1097, 1 }, { 1157, 1 }, { 1539, 5 },
			{ 8778, 1 }, { 2293, 1 }, { 1784, 4 }, { 133, 1 }, { 453, 1 }, { 1523, 1 }, { 7936, 1 }, { 2359, 1 },
			{ 5970, 1 }, { 1777, 1 }, { 361, 1 }, { 7178, 1 }, { 231, 1 }, { 1901, 1 }, { 1761, 1 }, };

	private static final int[][] GourmetLoots = { { 365, 1 }, { 361, 1 }, { 2011, 1 }, { 2327, 1 }, { 1897, 1 },
			{ 2293, 1 }, { 5004, 1 }, { 1883, 1 }, { 247, 1 }, { 380, 4 }, { 386, 3 }, { 7170, 1 }, { 5755, 1 },
			{ 7178, 1 }, { 7288, 1 }, { 7179, 6 }, { 374, 3 }, { 10136, 1 }, { 5406, 1 }, };

	private static final int[][] EarthLoots = { { 1440, 1 }, { 1442, 1 }, { 5535, 1 }, { 557, 32 }, { 6033, 6 },
			{ 6035, 2 }, { 1784, 4 }, { 447, 1 }, { 237, 1 }, { 2353, 1 }, { 5311, 2 }, { 5294, 2 }, { 5104, 2 },
			{ 454, 1 }, { 444, 1 }, { 1603, 1 }, { 1606, 2 }, { 1622, 2 }, { 1273, 1 }, };

	private static final int[][] EssenceLoots = { { 562, 4 }, { 555, 13 }, { 558, 25 }, { 556, 25 }, { 559, 28 },
			{ 554, 50 }, { 1448, 1 }, { 7937, 20 }, { 1439, 20 }, { 564, 4 }, { 4695, 4 }, { 4696, 4 }, { 4698, 4 },
			{ 4694, 4 }, { 4699, 4 }, { 4697, 4 }, { 565, 7 }, { 566, 11 }, { 563, 13 }, { 561, 13 }, { 560, 13 },
			{ 1442, 1 }, };

	private static final int[][] EclecticLoots = { { 1391, 1 }, { 1273, 1 }, { 2493, 1 }, { 1199, 1 }, { 10083, 1 },
			{ 1213, 1 }, { 5970, 1 }, { 231, 1 }, { 4527, 1 }, { 444, 1 }, { 2358, 5 }, { 450, 10 }, { 556, 30 },
			{ 7937, 20 }, { 237, 1 }, { 1601, 1 }, { 5760, 2 }, { 7208, 1 }, { 8779, 4 }, { 5321, 3 }, };

	private static final int[][] NatureLoots = { { 5100, 1 }, { 5104, 1 }, { 5281, 1 }, { 5294, 1 }, { 5295, 1 },
			{ 5297, 1 }, { 5299, 1 }, { 5298, 5 }, { 5303, 1 }, { 5304, 1 }, { 5313, 1 }, { 5286, 1 }, { 5285, 1 },
			{ 3000, 1 }, { 220, 2 }, { 5974, 1 }, { 6016, 1 }, { 1513, 1 }, { 254, 4 }, { 270, 4 }, };

	private static final int[][] PirateLoots = { { 2358, 14 }, { 7110, 1 }, { 7122, 1 }, { 7128, 1 }, { 7134, 1 },
			{ 7116, 1 }, { 7126, 1 }, { 7132, 1 }, { 7138, 1 }, { 8949, 1 }, { 7114, 1 }, { 8951, 1 }, { 13355, 1 },
			{ 8977, 1 }, { 8972, 1 }, { 8974, 1 }, { 8938, 1 }, { 8936, 1 }, { 8981, 1 }, { 8976, 1 }, { 1353, 1 },
			{ 1925, 1 }, { 1923, 1 }, };

	private static final int[][] NinjaLoots = { { 6328, 1 }, { 3385, 1 }, { 3391, 1 }, { 4097, 1 }, { 4095, 1 },
			{ 1113, 1 }, { 3101, 1 }, { 1333, 1 }, { 1347, 1 }, { 1215, 1 }, { 6313, 1 }, { 892, 70 }, { 811, 70 },
			{ 868, 40 }, { 805, 50 }, { 9342, 2 }, { 9194, 4 }, { 1748, 10 }, { 139, 1 }, { 5938, 4 }, { 6156, 3 },
			{ 2364, 4 }, };

	private static final int[][] MagpieLoots = { { 2364, 2 }, { 1682, 3 }, { 1732, 3 }, { 2569, 3 }, { 1602, 4 },
			{ 2571, 4 }, { 1748, 10 }, { 4097, 1 }, { 4095, 1 }, { 1215, 1 }, { 987, 1 }, { 985, 1 }, { 5541, 1 },
			{ 5287, 1 }, { 5300, 1 }, { 1682, 1 }, { 1185, 1 }, { 1347, 1 }, { 993, 1 }, { 3391, 1 }, };

	private static final int[][] DragonLoots = { { 4089, 1 }, { 4091, 1 }, { 4093, 1 }, { 4095, 1 }, { 4097, 1 },
			{ 1705, 3 }, { 1703, 3 }, { 11230, 350 }, { 11232, 350 }, { 11212, 500 }, { 11237, 350 }, { 11230, 350 },
			{ 537, 188 }, { 535, 300 }, { 5547, 1 }, { 1456, 1 }, { 1305, 1 }, { 535, 300 }, { 7219, 30 }, { 1616, 3 },
			{ 5300, 6 }, { 9244, 14 }, { 9341, 18 }, };
	private static final int[][] KinglyLoots = {
			// **{ 15503, 1 }, Missing shirt!**/
			{ 15509, 1 }, { 15505, 1 }, { 15507, 1 }, { 15511, 1 }, { 11230, 319 }, { 1306, 2 }, { 11212, 144 },
			{ 9193, 70 }, { 1618, 34 }, { 11212, 144 }, { 1705, 3 }, { 1632, 15 }, { 1616, 6 }, { 9342, 64 },
			{ 9194, 60 }, { 2364, 10 }, { 990, 2 }, { 1250, 1 }, { 2366, 1 }, { 7158, 1 }, { 1703, 3 }, { 6571, 1 }, };

	public static boolean giveLoot(Player p, int itemId, boolean give) {
		int id = 0;
		int amount = 0;
		int index = 0;
		switch (itemId) {
		case 11238:
			index = Misc.random(BabyLoots.length - 1);
			id = BabyLoots[index][0];
			amount = BabyLoots[index][1];
			break;
		case 11240:
			index = Misc.random(YoungLoots.length - 1);
			id = YoungLoots[index][0];
			amount = YoungLoots[index][1];
			break;
		case 11242:
			index = Misc.random(GourmetLoots.length - 1);
			id = GourmetLoots[index][0];
			amount = GourmetLoots[index][1];
			break;
		case 11244:
			index = Misc.random(EarthLoots.length - 1);
			id = EarthLoots[index][0];
			amount = EarthLoots[index][1];
			break;
		case 11246:
			index = Misc.random(EssenceLoots.length - 1);
			id = EssenceLoots[index][0];
			amount = EssenceLoots[index][1];
			break;
		case 11248:
			index = Misc.random(EclecticLoots.length - 1);
			id = EclecticLoots[index][0];
			amount = EclecticLoots[index][1];
			break;
		case 11250:
			index = Misc.random(NatureLoots.length - 1);
			id = NatureLoots[index][0];
			amount = NatureLoots[index][1];
			break;
		case 11252:
			index = Misc.random(MagpieLoots.length - 1);
			id = MagpieLoots[index][0];
			amount = MagpieLoots[index][1];
			break;
		case 13337:
			index = Misc.random(PirateLoots.length - 1);
			id = PirateLoots[index][0];
			amount = PirateLoots[index][1];
			break;
		case 11254:
			index = Misc.random(NinjaLoots.length - 1);
			id = NinjaLoots[index][0];
			amount = NinjaLoots[index][1];
			break;
		case 11256:
			index = Misc.random(DragonLoots.length - 1);
			id = DragonLoots[index][0];
			amount = DragonLoots[index][1];
			break;
		case 15517:
			index = Misc.random(KinglyLoots.length - 1);
			id = KinglyLoots[index][0];
			amount = KinglyLoots[index][1];
			break;
		default:
			return false;
		}
		if (amount >= 2) {
			amount = Misc.random(amount) + 1;
		}
		int i = Misc.random(20);
		if (p.getInventory().freeSlots() < 1 && i <= 13) {
			p.sendMessage("You need a free slot!");
			return false;
		}
		if (give) {
			p.getItems().deleteItem(11260, 1);
			p.getItems().addItem(id, amount);
			p.sendMessage("You get the reward instantly as you don't have an impling jar.");
			return true;
		}
		if (p.getItems().playerHasItem(itemId) && !give) {
			p.getItems().deleteItem(itemId, 1);
			p.getItems().addItem(id, amount);
			if (i > 13)
				p.sendMessage("The jar shatters as you open it.");
			else
				p.getItems().addItem(11260, 1);
			return true;
		}
		return false;
	}

}