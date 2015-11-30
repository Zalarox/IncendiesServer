package incendius.game.players.content.minigames.impl;

import incendius.game.players.Player;

public class Godwars {

	private Player c;

	public Godwars(Player c) {
		this.c = c;
	}

	public static final int[][] gwData = {
			// {kcType, npcIds},
			{ 0, 6203, 6204, 6206, 6210, 6208, 6212, 6214, 6215, 6218, 6219, 6220, 6221 }, // Zamorak
			{ 1, 6222, 6223, 6225, 6227, 6229, 6230, 6231, 6232, 6233, 6234, 6235, 6236, 6237, 6238, 6239, 6240, 6241,
					6242, 6243, 6244, 6245, 6246 }, // Armadyl
			{ 3, 6260, 6261, 6263, 6268, 6265, 6269, 6271, 6275, 6276, 6277, 6278, 6279, 6272, 6274 }, // Bandos
			{ 2, 6247, 6248, 6254, 6250, 6252, 6255, 6256, 6257, 6258, 6247 } // Saradomin
	};

	public static void giveKillcount(Player player, int npc) {
		for (int i = 0; i < gwData.length; i++) {
			for (int i1 = 1; i1 < gwData[i].length; i1++) {
				if (npc == gwData[i][i1]) {
					player.getInstance().gwKills[gwData[i][0]]++;
					interfaceProcess(player);
					break;
				}
			}
		}
	}

	public static void interfaceProcess(Player player) {
		player.getPA().sendString("" + player.getInstance().gwKills[1], 14506);
		player.getPA().sendString("" + player.getInstance().gwKills[3], 14507);
		player.getPA().sendString("" + player.getInstance().gwKills[2], 14508);
		player.getPA().sendString("" + player.getInstance().gwKills[0], 14509);
		player.getPA().walkableInterface(14500);
	}

	public static void doors(Player player, int obj) {
		switch (obj) {
		case 26384:
			if (!player.getItems().playerHasItem(2347, 1)) {
				player.sendMessage("You need a hammer to enter here.");
				return;
			}
			if (player.absX >= player.getInstance().objectX) {
				player.getPA().movePlayer(2850, 5333, 2);
			} else {
				player.getPA().movePlayer(2851, 5333, 2);
			}
			break;

		case 26439:
			if (player.absY == 5332) {
				player.getPA().movePlayer(2885, 5345, 2);
			} else {
				player.getPA().movePlayer(2885, 5332, 2);
			}
			break;

		case 26338:
			if (player.getInstance().playerLevel[2] < 60) {
				player.sendMessage("You must have a strength level of 60 to move this boulder.");
				return;
			}
			if (player.absY < player.getInstance().objectY) {
				player.getPA().movePlayer(2898, 3719, 0);
			} else {
				player.getPA().movePlayer(2898, 3715, 0);
			}
			break;

		case 7272:
			player.getPA().movePlayer(2918, 5273, 0);
			break;

		case 26303:
			if ((player.getInstance().playerEquipment[3] != 9185 || player.getInstance().playerEquipment[3] != 18357)
					|| player.getInstance().playerEquipment[player.getInstance().playerArrows] != 9419) {
				player.sendMessage("You must have a crossbow and a mithril grapple to pass this obstacle.");
				return;
			}
			if (player.absY >= 5279) {
				player.getPA().movePlayer(2871, 5269, 2);
			} else {
				player.getPA().movePlayer(2871, 5279, 2);
			}
			break;

		case 26428:
			if (player.heightLevel != 6) {
				if (player.getInstance().gwKills[0] < 20) {
					player.sendMessage("You must have 20 Zamorak kills before entering.");
					return;
				}
				player.getInstance().gwKills[0] = 0;
				player.getPA().movePlayer(2925, 5331, 6);
				return;
			}
			player.getPA().movePlayer(2925, 5332, 2);
			break;
		case 26427:
			if (player.heightLevel != 4) {
				if (player.getInstance().gwKills[2] < 20) {
					player.sendMessage("You must have 20 Saradomin kills before entering.");
					return;
				}
				player.getInstance().gwKills[2] = 0;
				player.getPA().movePlayer(player.absX - 1, player.absY, 4);
				return;
			}
			player.getPA().movePlayer(player.absX + 1, player.absY, 0);
			break;
		case 26426:
			if (player.heightLevel != 6) {
				if (player.getInstance().gwKills[1] < 20) {
					player.sendMessage("You must have 20 Armadyl kills before entering.");
					return;
				}
				player.getInstance().gwKills[1] = 0;
				player.getPA().movePlayer(2839, 5296, 6);
				return;
			}
			player.getPA().movePlayer(2839, 5295, 2);
			break;
		case 26425:
			if (player.heightLevel != 6) {
				if (player.getInstance().gwKills[3] < 20) {
					player.sendMessage("You must have 20 Bandos kills before entering.");
					return;
				}
				player.getInstance().gwKills[3] = 0;
				player.getPA().movePlayer(2864, 5354, 6);
				return;
			}
			player.getPA().movePlayer(2863, 5354, 2);
			break;
		}
	}

	public static void resetKills(Player player) {
		for (int i = 0; i < player.getInstance().gwKills.length; i++) {
			player.getInstance().gwKills[i] = 0;
		}
		player.sendMessage("The power of all those you slew in the dungeon drains from your body.");
	}

	/**
	 * Player Wearing equipment
	 */
	public boolean wearingArma() {
		for (int i = 0; i < c.getInstance().playerEquipment.length; i++) {
			if (c.getItems().getItemName(c.getInstance().playerEquipment[i]).contains("armadyl")) {
				return true;
			}
		}
		return false;
	}

	public boolean wearingBandos() {
		for (int i = 0; i < c.getInstance().playerEquipment.length; i++) {
			if (c.getItems().getItemName(c.getInstance().playerEquipment[i]).contains("bandos")) {
				return true;
			}
		}
		return false;
	}

	public boolean wearingSara() {
		for (int i = 0; i < c.getInstance().playerEquipment.length; i++) {
			if (c.getItems().getItemName(c.getInstance().playerEquipment[i]).contains("saradomin")) {
				return true;
			}
		}
		return false;
	}

	public boolean wearingZamm() {
		for (int i = 0; i < c.getInstance().playerEquipment.length; i++) {
			if (c.getItems().getItemName(c.getInstance().playerEquipment[i]).contains("zamorak")) {
				return true;
			}
		}
		return false;
	}
}