package main.game.players.content.skills.runecrafting;

import main.Constants;
import main.event.CycleEvent;
import main.event.CycleEventContainer;
import main.event.CycleEventHandler;
import main.game.players.Player;
import main.game.players.content.TeleportHandler;
import main.game.players.content.skills.runecrafting.Runecrafting.Runes;

public class RunecraftAltars {

	public static boolean runecraftAltar(Player player, int objectId) {
		switch (objectId) {
		// Runecraft
		case 2482:
			Runecrafting.craftRunes(player, Runes.FIRE);
			return true;
		case 2480:
			Runecrafting.craftRunes(player, Runes.WATER);
			return true;
		case 2478:
			Runecrafting.craftRunes(player, Runes.AIR);
			return true;
		case 2481:
			Runecrafting.craftRunes(player, Runes.EARTH);
			return true;
		case 2479:
			Runecrafting.craftRunes(player, Runes.MIND);
			return true;
		case 2483:
			Runecrafting.craftRunes(player, Runes.BODY);
			return true;
		case 2488:
			Runecrafting.craftRunes(player, Runes.DEATH);
			return true;
		case 2486:
			Runecrafting.craftRunes(player, Runes.NATURE);
			return true;
		case 2487:
			Runecrafting.craftRunes(player, Runes.CHAOS);
			return true;
		case 2485:
			Runecrafting.craftRunes(player, Runes.LAW);
			return true;
		case 2484:
			Runecrafting.craftRunes(player, Runes.COSMIC);
			return true;
		case 7141: // blood rift
			Runecrafting.craftRunes(player, Runes.BLOOD);
			return true;
		// case 2489 :
		case 7138: // soul rift
			Runecrafting.craftRunes(player, Runes.SOUL);
			return true;
		// Abyss
		case 7133:
			player.getPA().startTeleport2(2400, 4835, 0);
			return true;
		case 7132:
			player.getPA().startTeleport2(2142, 4813, 0);
			return true;
		case 7129:
			player.getPA().startTeleport2(2574, 4849, 0);
			return true;
		case 7130:
			player.getPA().startTeleport2(2655, 4830, 0);
			return true;
		case 7131:
			player.getPA().startTeleport2(2523, 4826, 0);
			return true;
		case 7140:
			player.getPA().startTeleport2(2793, 4828, 0);
			return true;
		case 7139:
			player.getPA().startTeleport2(2841, 4829, 0);
			return true;
		case 7137:
			player.getPA().startTeleport2(2726, 4832, 0);
			return true;
		case 7136:
			player.getPA().startTeleport2(2207, 4829, 0);
			return true;
		case 7135:
			player.getPA().startTeleport2(2464, 4818, 0);
			return true;
		case 7134:
			player.getPA().startTeleport2(2281, 4837, 0);
			return true;
		default:
			return false;
		}
	}

	public static boolean clickRuin(Player player, int objectId) {
		if (!Constants.RUNECRAFTING_ENABLED) {
			player.getPA().sendMessage("This skill is currently disabled.");
			return false;
		}
		for (int[] ruin : Runecrafting.RC_DATA) {
			if (objectId == ruin[2]) {
				TeleportHandler.teleport(player, ruin[7], ruin[8], 0, "modern");
				player.getPA().sendMessage("You feel a powerful force take hold of you...");
				return true;
			}
			if (objectId == ruin[6]) {
				TeleportHandler.teleport(player, ruin[3], ruin[4], 0, "modern");
				player.getPA().sendMessage("You step through the portal...");
				return true;
			}
		}
		return false;
	}

	public static boolean useTaliOnRuin(final Player player, final int itemId, int objectId) {
		for (final int[] ruin : Runecrafting.RC_DATA) {
			if (itemId == ruin[0] && objectId == ruin[2]) {
				if (!Constants.RUNECRAFTING_ENABLED) {
					player.getPA().sendMessage("This skill is currently disabled.");
					return false;
				}
				player.getPA().sendMessage(
						"You hold the " + player.getItems().getItemName(ruin[0]) + " towards the mysterious ruins.");
				player.getUpdateFlags().sendAnimation(827);
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						player.getPA().sendMessage("You feel a powerful force take hold of you...");
						TeleportHandler.teleport(player, ruin[7], ruin[8], 0, "modern");
						container.stop();
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
					}
				}, 2);
				return true;
			}
		}
		return false;
	}

}
