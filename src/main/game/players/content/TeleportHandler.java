package main.game.players.content;

import main.Constants;
import main.event.CycleEvent;
import main.event.CycleEventContainer;
import main.event.CycleEventHandler;
import main.game.npcs.NPC;
import main.game.npcs.NPCHandler;
import main.game.players.Boundaries;
import main.game.players.Boundaries.Area;
import main.game.players.Player;
import main.game.players.content.minigames.DuelArena;
import main.game.players.content.minigames.impl.FightPits;
import main.game.players.content.minigames.impl.Godwars;
import main.game.players.content.minigames.impl.PestControl;
import main.handlers.SkillHandler;

/**
 * 
 * @author Tringan
 *
 */
public class TeleportHandler {

	private enum TeleportData {
		MODERN(8939, 1576, 9, 8941, 1577), ANCIENT(9599, 1681, 11, 65535, -1), DUNGEON(13652, 2602, 16, 13654,
				0), TAB(9597, 1680, 8, 11793, -1), GLORY(9603, 1684, 13, 65535, -1);

		private int animation, gfx, teleTimer, teleEndAnimation, teleEndGfx;

		TeleportData(int animation, int gfx, int teleTimer, int teleEndAnimation, int teleEndGfx) {
			this.animation = animation;
			this.gfx = gfx;
			this.teleTimer = teleTimer;
			this.teleEndAnimation = teleEndAnimation;
			this.teleEndGfx = teleEndGfx;
		}

		public int getAnimation() {
			return animation;
		}

		public int getGfx() {
			return gfx;
		}

		public int getTeleTimer() {
			return teleTimer;
		}

		public int getTeleEndAnimation() {
			return teleEndAnimation;
		}

		public int getTeleEndGfx() {
			return teleEndGfx;
		}

		public static TeleportData forId(String type) {
			for (TeleportData d : TeleportData.values()) {
				if (d.toString().toLowerCase().equalsIgnoreCase(type)) {
					return d;
				}
			}
			return null;
		}
	}

	/**
	 * Executes the teleport
	 * 
	 * @param player
	 * @param absX
	 * @param absY
	 * @param height
	 * @param type
	 */
	public static void teleport(Player player, int absX, int absY, int height, String type) {
		resetActions(player);
		if (!ableToTeleport(player))
			return;
		if (!player.isDead && player.getVariables().teleTimer == 0 && player.getVariables().respawnTimer == -6) {
			if (player.getVariables().playerIndex > 0 || player.getVariables().npcIndex > 0) {
				player.getCombat().resetPlayerAttack();
			}
			player.stopMovement();
			player.getPA().removeAllWindows();
			player.getVariables().teleX = absX;
			player.getVariables().teleY = absY;
			player.getVariables().npcIndex = 0;
			player.getVariables().playerIndex = 0;
			player.faceUpdate(0);
			player.getVariables().teleHeight = height;
			player.getVariables().teleEndGfx = 0;
			player.getVariables().teleTimer = 0;
			player.getVariables().teleEndAnimation = 0;
			player.getVariables().teleGfx = 0;
			player.getPA().resetSkills();
			type = type.equalsIgnoreCase("auto") ? player.getVariables().playerMagicBook == 1 ? "ancient" : "modern"
					: type;
			final TeleportData data = TeleportData.forId(type);
			if (data == null) {
				System.out.println("Teleport data is null!");
				return;
			}
			player.startAnimation(data.getAnimation());
			player.getVariables().teleTimer = data.getTeleTimer();
			player.getVariables().gfx0(data.getGfx());
			player.getVariables().teleEndAnimation = data.getTeleEndAnimation();
			player.getVariables().teleEndGfx = data.getTeleEndGfx();
			updateTeleport(player);
		}
	}

	public static void npcTeleport(Player player, int x, int y, int height, int npcId) {
		resetActions(player);
		if (!ableToTeleport(player))
			return;
		if (!player.isDead && player.getVariables().teleTimer == 0) {
			player.stopMovement();
			player.getPA().removeAllWindows();
			player.getVariables().teleEndGfx = 0;
			player.getVariables().teleX = x;
			player.getVariables().teleY = y;
			player.getVariables().npcIndex = 0;
			player.getVariables().playerIndex = 0;
			player.faceUpdate(0);
			player.getVariables().teleHeight = height;
			player.getPA().resetSkills();
			player.startAnimation(1816);
			player.getVariables().teleTimer = 11;
			player.getVariables().teleGfx = 342;
			player.getVariables().teleEndAnimation = 65535;
			NPC n = NPCHandler.npcs[npcId];
			n.gfx0(343);
			n.requestAnimation(1818, 0);
			n.facePlayer(player.playerId);
			updateTeleport(player);
		}
	}

	/**
	 * Checks if a player can teleport
	 * 
	 * @param player
	 * @return
	 */
	public static boolean ableToTeleport(Player player) {
		if (Boundaries.checkBoundaries(Area.GOD_WARS, player.getX(), player.getY())) {
			Godwars.resetKills(player);
		}
		if (player.party != null && player.party.floor != null) {
			player.sendMessage("You can't teleport out of a dungeon!");
			return false;
		}
		if (FightPits.getState(player) != null) {
			player.sendMessage("You can't teleport from a Fight pits Game!");
			return false;
		}
		if (player.getVariables().isJumping) {
			return false;
		}
		if (PestControl.isInGame(player)) {
			player.sendMessage("You can't teleport from a Pest Control Game!");
			return false;
		}
		if (DuelArena.isDueling(player)) {
			player.sendMessage("You can't teleport during a duel!");
			return false;
		}
		if (player.getVariables().inWild() && player.getVariables().wildLevel > Constants.NO_TELEPORT_WILD_LEVEL) {
			player.sendMessage(
					"You can't teleport above level " + Constants.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
			return false;
		}
		if (System.currentTimeMillis() - player.getVariables().teleBlockDelay < player.getVariables().teleBlockLength) {
			player.sendMessage("You are teleblocked and can't teleport.");
			return false;
		}
		if (player.getVariables().teleTimer > 0) {
			return false;
		}
		return true;
	}

	/**
	 * Resets the player Actions
	 * 
	 * @param player
	 */
	public static void resetActions(Player player) {
		if (SkillHandler.playerIsBusy(player))
			if (player.getVariables().playerIsFishing) {
				player.startAnimation(65535);
				player.getPA().removeAllWindows();
				player.getVariables().playerIsFishing = false;
				for (int i = 0; i < 11; i++) {
					player.getVariables().fishingProp[i] = -1;
				}
			}
		if (player.getVariables().doingWoodcutting) {
			player.getCombat()
					.getPlayerAnimIndex(player.getItems()
							.getItemName(player.getVariables().playerEquipment[player.getVariables().playerWeapon])
							.toLowerCase());
			player.getPA().requestUpdates();
			player.getVariables().doingWoodcutting = false;
		}
	}

	/**
	 * Updates the Teleport
	 * 
	 * @param player
	 */
	public static void updateTeleport(final Player player) {
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (player.getVariables().teleTimer > 0) {
					player.getVariables().teleTimer--;
					if (!player.isDead) {
						if (player.getVariables().teleTimer == 1 && player.getVariables().newLocation > 0) {
							player.getVariables().teleTimer = 0;
							player.getPA().changeLocation();
							container.stop();
						}
						if (player.getVariables().teleTimer == 5) {
							player.getVariables().teleTimer--;
							processTeleport(player);
						}
						if (player.getVariables().teleTimer == 9 && player.getVariables().teleGfx > 0) {
							player.getVariables().teleTimer--;
							if (player.getVariables().teleGfx != 342) {
								player.gfx100(player.getVariables().teleGfx);
							} else {
								player.gfx0(player.getVariables().teleGfx);
							}
						}
					} else {
						player.getVariables().teleTimer = 0;
						container.stop();
					}
				}
			}

			@Override
			public void stop() {
				player.getVariables().teleTimer = 0;
			}
		}, 1);
	}

	public static void processTeleport(Player player) {
		player.teleportToX = player.getVariables().teleX;
		player.teleportToY = player.getVariables().teleY;
		player.heightLevel = player.getVariables().teleHeight;
		if (player.getVariables().teleEndAnimation > 0) {
			player.startAnimation(player.getVariables().teleEndAnimation);
		}
		if (player.getVariables().teleEndGfx > 0) {
			player.gfx0(player.getVariables().teleEndGfx);
		}
	}
}
