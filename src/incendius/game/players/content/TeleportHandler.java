package incendius.game.players.content;

import incendius.Constants;
import incendius.event.CycleEvent;
import incendius.event.CycleEventContainer;
import incendius.event.CycleEventHandler;
import incendius.game.npcs.NPC;
import incendius.game.npcs.NPCHandler;
import incendius.game.players.Boundaries;
import incendius.game.players.Player;
import incendius.game.players.Boundaries.Area;
import incendius.game.players.content.minigames.DuelArena;
import incendius.game.players.content.minigames.impl.FightPits;
import incendius.game.players.content.minigames.impl.Godwars;
import incendius.game.players.content.minigames.impl.PestControl;
import incendius.handlers.SkillHandler;

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
		if (!player.isDead && player.getInstance().teleTimer == 0 && player.getInstance().respawnTimer == -6) {
			if (player.getInstance().playerIndex > 0 || player.getInstance().npcIndex > 0) {
				player.getCombat().resetPlayerAttack();
			}
			player.stopMovement();
			player.getPA().removeAllWindows();
			player.getInstance().teleX = absX;
			player.getInstance().teleY = absY;
			player.getInstance().npcIndex = 0;
			player.getInstance().playerIndex = 0;
			player.faceUpdate(0);
			player.getInstance().teleHeight = height;
			player.getInstance().teleEndGfx = 0;
			player.getInstance().teleTimer = 0;
			player.getInstance().teleEndAnimation = 0;
			player.getInstance().teleGfx = 0;
			player.getPA().resetSkills();
			type = type.equalsIgnoreCase("auto") ? player.getInstance().playerMagicBook == 1 ? "ancient" : "modern"
					: type;
			final TeleportData data = TeleportData.forId(type);
			if (data == null) {
				System.out.println("Teleport data is null!");
				return;
			}
			player.startAnimation(data.getAnimation());
			player.getInstance().teleTimer = data.getTeleTimer();
			player.getInstance().gfx0(data.getGfx());
			player.getInstance().teleEndAnimation = data.getTeleEndAnimation();
			player.getInstance().teleEndGfx = data.getTeleEndGfx();
			updateTeleport(player);
		}
	}

	public static void npcTeleport(Player player, int x, int y, int height, int npcId) {
		resetActions(player);
		if (!ableToTeleport(player))
			return;
		if (!player.isDead && player.getInstance().teleTimer == 0) {
			player.stopMovement();
			player.getPA().removeAllWindows();
			player.getInstance().teleEndGfx = 0;
			player.getInstance().teleX = x;
			player.getInstance().teleY = y;
			player.getInstance().npcIndex = 0;
			player.getInstance().playerIndex = 0;
			player.faceUpdate(0);
			player.getInstance().teleHeight = height;
			player.getPA().resetSkills();
			player.startAnimation(1816);
			player.getInstance().teleTimer = 11;
			player.getInstance().teleGfx = 342;
			player.getInstance().teleEndAnimation = 65535;
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
		if (player.isJailed()) {
			player.sendMessage("Rule-breakers lack the privelege of teleportation.");
			return false;
		}
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
		if (player.getInstance().isJumping) {
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
		if (player.getInstance().inWild() && player.getInstance().wildLevel > Constants.NO_TELEPORT_WILD_LEVEL) {
			player.sendMessage(
					"You can't teleport above level " + Constants.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
			return false;
		}
		if (System.currentTimeMillis() - player.getInstance().teleBlockDelay < player.getInstance().teleBlockLength) {
			player.sendMessage("You are teleblocked and can't teleport.");
			return false;
		}
		if (player.getInstance().teleTimer > 0) {
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
			if (player.getInstance().playerIsFishing) {
				player.startAnimation(65535);
				player.getPA().removeAllWindows();
				player.getInstance().playerIsFishing = false;
				for (int i = 0; i < 11; i++) {
					player.getInstance().fishingProp[i] = -1;
				}
			}
		if (player.getInstance().doingWoodcutting) {
			player.getCombat()
					.getPlayerAnimIndex(player.getItems()
							.getItemName(player.getInstance().playerEquipment[player.getInstance().playerWeapon])
							.toLowerCase());
			player.getPA().requestUpdates();
			player.getInstance().doingWoodcutting = false;
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
				if (player.getInstance().teleTimer > 0) {
					player.getInstance().teleTimer--;
					if (!player.isDead) {
						if (player.getInstance().teleTimer == 1 && player.getInstance().newLocation > 0) {
							player.getInstance().teleTimer = 0;
							player.getPA().changeLocation();
							container.stop();
						}
						if (player.getInstance().teleTimer == 5) {
							player.getInstance().teleTimer--;
							processTeleport(player);
						}
						if (player.getInstance().teleTimer == 9 && player.getInstance().teleGfx > 0) {
							player.getInstance().teleTimer--;
							if (player.getInstance().teleGfx != 342) {
								player.gfx100(player.getInstance().teleGfx);
							} else {
								player.gfx0(player.getInstance().teleGfx);
							}
						}
					} else {
						player.getInstance().teleTimer = 0;
						container.stop();
					}
				}
			}

			@Override
			public void stop() {
				player.getInstance().teleTimer = 0;
			}
		}, 1);
	}

	public static void processTeleport(Player player) {
		player.teleportToX = player.getInstance().teleX;
		player.teleportToY = player.getInstance().teleY;
		player.heightLevel = player.getInstance().teleHeight;
		if (player.getInstance().teleEndAnimation > 0) {
			player.startAnimation(player.getInstance().teleEndAnimation);
		}
		if (player.getInstance().teleEndGfx > 0) {
			player.gfx0(player.getInstance().teleEndGfx);
		}
	}
}
