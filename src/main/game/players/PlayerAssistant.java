package main.game.players;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import main.Connection;
import main.Connection.ConnectionType;
import main.Constants;
import main.GameEngine;
import main.event.CycleEvent;
import main.event.CycleEventContainer;
import main.event.CycleEventHandler;
import main.event.Task;
import main.game.items.GameItem;
import main.game.items.Item;
import main.game.items.ItemLoader;
import main.game.npcs.NPC;
import main.game.npcs.NPCHandler;
import main.game.players.Boundaries.Area;
import main.game.players.actions.combat.CombatPrayer;
import main.game.players.content.Enchanting;
import main.game.players.content.minigames.DuelArena;
import main.game.players.content.minigames.impl.FightPits;
import main.game.players.content.minigames.impl.Godwars;
import main.game.players.content.minigames.impl.PestControl;
import main.handlers.Following;
import main.handlers.SkillHandler;
import main.util.Misc;
import main.world.map.Region;

public class PlayerAssistant {

	private Player c;

	public PlayerAssistant(Player Player) {
		this.c = Player;
	}
	
	/**
	 * Gets a player's display name, if they have one.
	 * 
	 * @return The player's display name, if applicable.
	 */
	public String getDisplayName() {
		if ((c.displayName == "")) {
			return c.playerName;
		} else {
			return c.displayName;
		}
	}
	
	/**
	 * Checks if a display name is in use.
	 * 
	 * @param name
	 *            The display name to check.
	 *            
	 * @return True if the display name is in use. False if it is not.
	 */
	@SuppressWarnings("resource")
	public boolean isDisplayNameTaken(String name) {
		try {
			File list = new File("./data/displaynames.txt");
			FileReader read = new FileReader(list);
			BufferedReader reader = new BufferedReader(read);
			String line = null;

			while ((line = reader.readLine()) != null) {
				if (line.equalsIgnoreCase(name)) {
					return true;
				}
			}

			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * Checks if a real name is in use.
	 * 
	 * @param name
	 *            The real name to check.
	 *            
	 * @return True if the real name is in use, false if not.
	 */
	public boolean isNameTaken(String name) {
		try {
			File names = new File("./data/characters/" + name + ".txt");

			if (names.exists()) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * Creates a display name.
	 * 
	 * @param name
	 *            The display name to create.
	 */
	public void createDisplayName(String name) {
		BufferedWriter names = null;
		
		try {
			names = new BufferedWriter(new FileWriter("./data/displaynames.txt", true));
			names.write(name);
			names.newLine();
			names.flush();
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
			
		} finally {
			
			if (names != null) {
				try {
					names.close();
					
				} catch (IOException ioe2) {
					ioe2.printStackTrace();
				}
			}
		}
	}

	public void createArrow(final int x, final int y, final int height, final int pos) {
		if (c != null) {
			c.getOutStream().createFrame(254); // The packet ID
			c.getOutStream().writeByte(pos); // Position on Square(2 = middle, 3
			// = west, 4 = east, 5 = south,
			// 6 = north)
			c.getOutStream().writeWord(x); // X-Coord of Object
			c.getOutStream().writeWord(y); // Y-Coord of Object
			c.getOutStream().writeByte(height); // Height off Ground
		}
	}

	public static void createStillGfx(final int id, final int x, final int y, final int height, final int time) {
		for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
			final Player p = PlayerHandler.players[i];
			if (p != null) {
				final Player person = p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							person.getPA().stillGfx(id, x, y, height, time);
						}
					}
				}
			}
		}
	}

	public void createPlayersStillGfx(int id, int x, int y, int notUsed, int notUsed2, int height) {
		// synchronized(c) {
		for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Player person = p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							person.getPA().stillGfx(id, x, y, height, 0);
						}
					}
				}
			}
		}
	}

	public void castleWarsObjects() {
		/*
		 * object(-1, 2373, 3119, -3, 10); object(-1, 2372, 3119, -3, 10);
		 */
	}

	public void hitPlayers(int x1, int x2, int y1, int y2, int hp) {
		for (Player p : PlayerHandler.players) {
			if (p == null || !p.isActive)
				continue;
			final Player t = p;
			if (t.absX >= x1 && t.absX <= x2 && t.absY >= y1 && t.absY <= y2) {
				int hit = t.getVariables().playerLevel[c.playerHitpoints] / hp;
				t.setHitDiff2(hit);
				t.setHitUpdateRequired2(true);
				t.getVariables().playerLevel[c.playerHitpoints] -= hit;
				t.getPA().refreshSkill(c.playerHitpoints);
				t.updateRequired = true;
			}
		}
	}

	/**
	 * Player resting
	 */
	public void rest() {
		/**
		 * Player is already reseting
		 */
		if (c.getVariables().resting) {
			return;
		}
		/**
		 * Cant rest in combat
		 */
		if (c.getVariables().underAttackBy > 0 || c.getVariables().underAttackBy2 > 0) {
			c.sendMessage("You can't rest in combat!");
			return;
		}
		/**
		 * The start of resting
		 */
		c.startAnimation(11786);
		c.stopMovement();
		c.getVariables().npcIndex = 0;
		c.getVariables().npcClickIndex = 0;
		c.getVariables().playerIndex = 0;
		c.getVariables().clickNpcType = 0;
		Following.resetFollow(c);
		CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				/**
				 * Full energy
				 */
				if (c.getVariables().runEnergy >= 200) {
					c.getVariables().runEnergy = 200;
				} else {
					c.getVariables().runEnergy += Misc.random(4);
					c.startAnimation(11787);
				}
				updateEnergy();
				if (!c.getVariables().resting) {
					container.stop();
				}
			}

			@Override
			public void stop() {

			}
		}, 2);
	}

	/**
	 * Resets resting
	 */
	public void resetRest() {
		c.startAnimation(11788);
		sendFrame126("resting", 0);
		c.getVariables().resting = false;
	}

	public int CraftInt, Dcolor, FletchInt;

	/**
	 * Temporary event running
	 */
	public void registerEvents() {
		CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (c != null || c.disconnected) {

					/**
					 * Running
					 */
					if (c.getVariables().runEnergy < 200) {
						if (System.currentTimeMillis() > getRunRestore() + c.getVariables().lastRunRecovery) {
							c.getVariables().runEnergy++;
							c.getVariables().lastRunRecovery = System.currentTimeMillis();
							updateEnergy();
						}
					}
				} else {
					container.stop();
				}
			}

			@Override
			public void stop() {

			}
		}, 1);
	}

	/**
	 * Gets the run restore
	 * 
	 * @param c
	 *            the player
	 * @return the run restore
	 */
	public double getRunRestore() {
		return 2260 - (c.getVariables().playerLevel[16] * 10);
	}

	/**
	 * Resets all skills if the player moves or teleports
	 */
	public void resetSkills() {
		c.getVariables().playerIsFishing = false;
		CycleEventHandler.getInstance().stopEvent(c.getSkilling());
		c.getVariables().doingWoodcutting = false;
		for (int i = 0; i < c.getVariables().playerSkilling.length; i++) {
			c.getVariables().playerSkilling[i] = false;
		}
	}

	public void setConfig(int id, int state) {
		c.getOutStream().createFrame(36);
		c.getOutStream().writeWordBigEndian(id);
		c.getOutStream().writeByte(state);
	}

	/**
	 * MulitCombat icon
	 * 
	 * @param i1
	 *            0 = off 1 = on
	 */
	public void multiWay(int i1) {
		c.outStream.createFrame(61);
		c.outStream.writeByte(i1);
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	/**
	 * Sends the XPCounter information
	 * 
	 * @param skillID
	 * @param xp
	 */
	public void sendSkillXP(int skillID, int xp) {
		if (c.getOutStream() != null && c != null) {
			c.getVariables().totalxp += xp;
			c.outStream.createFrame(124);
			c.outStream.writeByte(skillID);
			c.outStream.writeDWord(xp);
			c.outStream.writeDWord(c.getVariables().totalxp);
			c.flushOutStream();
		}
	}

	/**
	 * Draws a black pane in the gameScreen mainly used for quests
	 * 
	 * @param opacity
	 */
	public void drawBlackPane(int opacity, boolean active) {
		if (c.getOutStream() != null && c != null) {
			c.outStream.createFrame(178);
			c.outStream.writeDWord(opacity);
			c.getOutStream().writeByte(active ? 1 : 0);
			c.flushOutStream();
		}
	}

	/**
	 * Activates/deactivates the summoning orb and sets the familiar special
	 * attack
	 * 
	 * @param active
	 * @param special
	 */
	public void sendSummOrbDetails(boolean active, String special) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSize(172);
			c.getOutStream().writeByte(active ? 1 : 0);
			if (active)
				c.getOutStream().writeString(special);
			c.getOutStream().endFrameVarSize();
		}
	}

	public void sendConsole(String toSend) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSize(198);
			c.getOutStream().writeString(toSend);
			c.getOutStream().endFrameVarSize();
		}
	}

	/**
	 * Sends the run energy to the client
	 */
	public void updateEnergy() {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(110);
			c.getOutStream().writeByte(c.getVariables().runEnergy / 2);
		}
		sendFrame126(c.getVariables().runEnergy / 2 + "%", 149);
	}

	public void createObjectAnim(int X, int Y, int animationID, int tileObjectType, int orientation) {
		if (c == null) {
			return;
		}
		try {
			c.outStream.createFrame(85);
			c.outStream.writeByteC(Y - c.mapRegionY * 8);
			c.outStream.writeByteC(X - c.mapRegionX * 8);
			int x = 0;
			int y = 0;
			c.outStream.createFrame(160);
			c.outStream.writeByteS(((x & 7) << 4) + (y & 7));// tiles away -
			// could just
			// send 0
			c.outStream.writeByteS((tileObjectType << 2) + (orientation & 3));
			c.outStream.writeWordA(animationID);// animation id
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void objectAnim(int X, int Y, int animationID, int tileObjectType, int orientation) {
		for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				Player person = PlayerHandler.players[i];
				if (person != null) {
					Player cc = person;
					if (cc.distanceToPoint(X, Y) <= 25) {
						createObjectAnim(X, Y, animationID, tileObjectType, orientation);
					}
				}
			}
		}
	}

	public void object(int objectId, int objectX, int objectY, int objectH, int face, int objectType) {
		if (c.heightLevel != objectH) {
			return;
		}
		if (!Constants.goodDistance(objectX, objectY, c.absX, c.absY, 60)) {
			return;
		}
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(objectY - c.getMapRegionY() * 8);
			c.getOutStream().writeByteC(objectX - c.getMapRegionX() * 8);
			c.getOutStream().createFrame(101);
			c.getOutStream().writeByteC((objectType << 2) + (face & 3));
			c.getOutStream().writeByte(0);
			if (objectId != -1) { // removing
				c.getOutStream().createFrame(151);
				c.getOutStream().writeByteS(0);
				c.getOutStream().writeWordBigEndian(objectId);
				c.getOutStream().writeByteS((objectType << 2) + (face & 3));
			}
			c.flushOutStream();
		}
	}

	public void sendObject(int objectId, int objectX, int objectY, int objectH, int face, int objectType) {
		if (c.heightLevel != objectH) {
			return;
		}
		if (!Constants.goodDistance(objectX, objectY, c.absX, c.absY, 60)) {
			return;
		}
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(objectY - c.getMapRegionY() * 8);
			c.getOutStream().writeByteC(objectX - c.getMapRegionX() * 8);
			c.getOutStream().createFrame(101);
			c.getOutStream().writeByteC((objectType << 2) + (face & 3));
			c.getOutStream().writeByte(0);
			if (objectId != -1) { // removing
				c.getOutStream().createFrame(151);
				c.getOutStream().writeByteS(0);
				c.getOutStream().writeWordBigEndian(objectId);
				c.getOutStream().writeByteS((objectType << 2) + (face & 3));
			}
			c.flushOutStream();
		}
	}

	/**
	 * Objects, add and remove
	 **/
	public void object(int objectId, int objectX, int objectY, int objectType) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(objectY - c.getMapRegionY() * 8);
			c.getOutStream().writeByteC(objectX - c.getMapRegionX() * 8);
			c.getOutStream().createFrame(101);
			c.getOutStream().writeByteC((objectType << 2) + (0 & 3));
			c.getOutStream().writeByte(0);
			if (objectId != -1) { // removing
				c.getOutStream().createFrame(151);
				c.getOutStream().writeByteS(0);
				c.getOutStream().writeWordBigEndian(objectId);
				c.getOutStream().writeByteS((objectType << 2) + (0 & 3));
			}
			c.flushOutStream();
		}
	}

	public void walkThruDoor(final int id1, final int id2, final int x1, final int y1, final int x2, final int y2,
			final int h) {
		object(-1, x1, y1, h, 0, 0);
		object(-1, x2, y2, h, 0, 0);
		object(id1, x1, y1 + 1, h, 2, 0);
		object(id2, x2, y2 + 1, h, 4, 0);
		// walkTo(0, c.absY > walkY ? -1 : 1);
		CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				object(id2, x1, y1, h, 1, 0);
				object(id1, x2, y2, h, 1, 0);
				object(-1, x1, y1 + 1, h, 0, 0);
				object(-1, x2, y2 + 1, h, 0, 0);
				container.stop();
			}

			@Override
			public void stop() {
			}
		}, 2);
	}

	public void walkThruDoor(final int id1, final int x1, final int y1, final int h) {
		object(-1, x1, y1, h, 0, 0);
		object(id1, x1, y1 + 1, h, 4, 0);
		CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				object(id1, x1, y1, h, 1, 0);
				object(-1, x1, y1 + 1, h, 0, 0);
				container.stop();
			}

			@Override
			public void stop() {
			}
		}, 2);
	}

	public void resetPlayerSkillVariables() {
		if (c.isSkilling()) {
			c.getVariables().isDreaming = false;
			c.getVariables().playerIsPraying = false;
			c.getVariables().playerIsSmithing = false;
			c.getVariables().playerIsCooking = false;
			c.getVariables().playerIsMining = false;
			c.getVariables().playerIsFletching = false;
			c.getVariables().playerIsWoodcutting = false;
			c.getVariables().playerIsFishing = false;
			c.getVariables().playerIsHerbloring = false;
			c.getVariables().playerIsFiremaking = false;
			c.getVariables().playerIsCrafting = false;
			c.getVariables().playerIsThieving = false;
			c.getVariables().playerIsFarming = false;
			c.getVariables().doAnim = -1;
			c.getVariables().oldItem = -1;
			c.getVariables().oldItem2 = -1;
			c.getVariables().gainXp = -1;
			c.getVariables().gainXp2 = -1;
			c.getVariables().levelReq = -1;
			c.getVariables().levelReq2 = -1;
			c.getVariables().newItem = -1;
			c.getVariables().newItem2 = -1;
			c.getVariables().objectType = -1;
			c.getVariables().chance = -1;
			c.getVariables().leatherType = -1;
			c.getVariables().skillSpeed = 1;
			removeAllWindows();
		}
	}

	public void clearClanChat() {
		c.getVariables().clanId = -1;
		sendString("Talking in: ", 18139);
		sendString("Owner: ", 18140);
		for (int j = 18144; j < 18244; j++) {
			sendString("[REG]", j);
		}
	}

	public void resetAutocast() {
		c.getVariables().autocastId = 0;
		c.getVariables().autocasting = false;
		sendFrame36(108, 0);
		c.sendMessage(":resetautocast:");
	}

	public String getTotalAmount(int j) {
		if (j >= 10000 && j < 10000000) {
			return j / 1000 + "K";
		} else if (j >= 10000000 && j <= 2147483647) {
			return j / 1000000 + "M";
		} else {
			return "" + j + " gp";
		}
	}

	public void removeAllItems() {
		for (int i = 0; i < c.getVariables().playerItems.length; i++) {
			c.getVariables().playerItems[i] = 0;
		}
		for (int i = 0; i < c.getVariables().playerItemsN.length; i++) {
			c.getVariables().playerItemsN[i] = 0;
		}
		c.getItems().resetItems(3214);
	}

	public void sendSkillInterface(int id[]) {
		c.outStream.createFrameVarSizeWord(53);
		c.outStream.writeWord(8847); // 8847
		c.outStream.writeWord(id.length);
		for (int i = 0; i < id.length; i++) {
			c.outStream.writeByte(1);
			if (id[i] > 0) {
				c.outStream.writeWordBigEndianA(id[i] + 1);
			} else {
				c.outStream.writeWordBigEndianA(0);
			}
		}
		c.outStream.endFrameVarSizeWord();
		c.flushOutStream();
	}

	public void sendString(String s, int id) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(126);
			c.getOutStream().writeString(s);
			c.getOutStream().writeWordA(id);
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public void sendLink(String s) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(187);
			c.getOutStream().writeString(s);
		}
	}

	public void setSkillLevel(int skillNum, int currentLevel, int XP) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(134);
			c.getOutStream().writeByte(skillNum);
			c.getOutStream().writeDWord_v1(XP);
			c.getOutStream().writeByte(currentLevel);
			c.flushOutStream();
		}
	}

	public void openGuide(String title, String guide) {
		for (int i = 8145; i < 8195; i++) {
			sendFrame126("", i);
		}
		showInterface(8134);
		sendFrame126("@dre@" + title, 8144);
		sendFrame126("@blu@" + guide, 8147);
		c.flushOutStream();
	}

	public void sendFrame106(int sideIcon) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(106);
			c.getOutStream().writeByteC(sideIcon);
			c.flushOutStream();
			requestUpdates();
		}
	}

	public void sendFrame164(int Frame) {
		// synchronized (c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(164);
			c.getOutStream().writeWordBigEndian_dup(Frame);
			c.flushOutStream();
		}
		// }
	}

	public void sendFrame107() {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(107);
			c.flushOutStream();
		}
	}

	public void sendFrame36(int id, int state) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(36);
			c.getOutStream().writeWordBigEndian(id);
			c.getOutStream().writeByte(state);
			c.flushOutStream();
		}
	}

	public void sendFrame87(int id, int state) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(87);
			c.getOutStream().writeWordBigEndian(id);
			c.getOutStream().writeDWord_v1(state);
			c.flushOutStream();
		}
	}

	public void sendFrame185(int Frame) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(185);
			c.getOutStream().writeWordBigEndianA(Frame);
		}
	}

	public void showInterface(int interfaceid) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(97);
			c.getOutStream().writeWord(interfaceid);
			c.flushOutStream();
			c.getVariables().interfaceIdOpen = interfaceid;
		}
	}

	public void sendFrame248(int MainFrame, int SubFrame) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(248);
			c.getOutStream().writeWordA(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.flushOutStream();
		}
	}

	public void sendFrame246(int MainFrame, int SubFrame, int SubFrame2) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(246);
			c.getOutStream().writeWordBigEndian(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.getOutStream().writeWord(SubFrame2);
			c.flushOutStream();
		}
	}

	public void sendFrame171(int MainFrame, int SubFrame) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(171);
			c.getOutStream().writeByte(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.flushOutStream();
		}
	}

	public void sendFrame200(int MainFrame, int SubFrame) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(200);
			c.getOutStream().writeWord(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.flushOutStream();
		}
	}

	public void sendFrame70(int i, int o, int id) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(70);
			c.getOutStream().writeWord(i);
			c.getOutStream().writeWordBigEndian(o);
			c.getOutStream().writeWordBigEndian(id);
			c.flushOutStream();
		}
	}

	public void sendFrame75(int MainFrame, int SubFrame) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(75);
			c.getOutStream().writeWordBigEndianA(MainFrame);
			c.getOutStream().writeWordBigEndianA(SubFrame);
			c.flushOutStream();
		}
	}

	public void sendChatInterface(int Frame) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(164);
			c.getOutStream().writeWordBigEndian_dup(Frame);
			c.flushOutStream();
		}
	}

	public void setPrivateMessaging(int i) { // friends and ignore list status
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(221);
			c.getOutStream().writeByte(i);
			c.flushOutStream();
		}
	}

	public void setChatOptions(int publicChat, int privateChat, int tradeBlock) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(206);
			c.getOutStream().writeByte(publicChat);
			c.getOutStream().writeByte(privateChat);
			c.getOutStream().writeByte(tradeBlock);
			c.flushOutStream();
		}
	}

	public void sendPM(long name, int rights, byte[] chatmessage, int messagesize) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSize(196);
			c.getOutStream().writeQWord(name);
			c.getOutStream().writeDWord(c.getVariables().lastChatId++);
			c.getOutStream().writeByte(rights);
			c.getOutStream().writeBytes(chatmessage, messagesize, 0);
			c.getOutStream().endFrameVarSize();
			c.flushOutStream();
			// String chatmessagegot = Misc.textUnpack(chatmessage,
			// messagesize);
			// String target = Misc.longToPlayerName(name);
		}
	}

	public void createPlayerHints(int type, int id) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(254);
			c.getOutStream().writeByte(type);
			c.getOutStream().writeWord(id);
			c.getOutStream().write3Byte(0);
			c.flushOutStream();
		}
	}

	public void createObjectHints(int x, int y, int height, int pos) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(254);
			c.getOutStream().writeByte(pos);
			c.getOutStream().writeWord(x);
			c.getOutStream().writeWord(y);
			c.getOutStream().writeByte(height);
			c.flushOutStream();
		}
	}

	public void loadPM(long playerName, int world) {
		if (c.getOutStream() != null && c != null) {
			if (world != 0) {
				world += 9;
			} else if (!Constants.WORLD_LIST_FIX) {
				world += 1;
			}
			c.getOutStream().createFrame(50);
			c.getOutStream().writeQWord(playerName);
			c.getOutStream().writeByte(world);
			c.flushOutStream();
		}
	}

	public void removeAllWindows() {
		if (c.getOutStream() != null && c != null) {
			if (c.getVariables().killedDuelOpponent) {
				c.Dueling.claimDuelRewards(c);
			}
			c.getOutStream().createFrame(219);
			c.flushOutStream();
			c.getVariables().interfaceIdOpen = 0;
			c.getVariables().statedInterface = "NONE";
			c.getVariables().isShopping = false;
			c.getVariables().isBanking = false;
			c.getVariables().isSearching = false;
			c.partner = null;
			c.setBusy(false);
		}
	}

	public void closeActivities() {
		c.setBusy(false);
		c.Dueling.declineDuel(c, true, true);
		c.getTradeHandler().declineTrade(true);
	}

	public void closeAllWindows() {
		if (c.getOutStream() != null && c != null) {
			if (c.getVariables().killedDuelOpponent) {
				c.Dueling.claimDuelRewards(c);
			}
			c.getOutStream().createFrame(219);
			c.flushOutStream();
			c.getVariables().interfaceIdOpen = 0;
			c.getVariables().statedInterface = "NONE";
			c.getVariables().isShopping = false;
			c.getVariables().isBanking = false;
			c.getVariables().isSearching = false;
			c.partner = null;
			c.setBusy(false);
			c.getVariables().closed = true;
			c.getTradeHandler().declineTrade(true);
		}
	}

	public void sendFrame34(int id, int slot, int column, int amount) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(34); // init item to smith
			// screen
			c.getOutStream().writeWord(column); // Column Across Smith Screen
			c.getOutStream().writeByte(4); // Total Rows?
			c.getOutStream().writeDWord(slot); // Row Down The Smith Screen
			c.getOutStream().writeWord(id + 1); // item
			c.getOutStream().writeByte(amount); // how many there are?
			c.getOutStream().endFrameVarSizeWord();
		}
	}

	public void sendUpdateItem(Item item, int slot, int column) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(34); // init item to smith
			// screen
			c.getOutStream().writeWord(column); // Column Across Smith Screen
			c.getOutStream().writeByte(4); // Total Rows?
			c.getOutStream().writeDWord(slot); // Row Down The Smith Screen
			c.getOutStream().writeWord(item.getId() + 1); // item
			c.getOutStream().writeByte(item.getCount()); // how many there are?
			c.getOutStream().endFrameVarSizeWord();
		}
	}

	public void sendUpdateItem(int slot, int column, Item item) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(34); // init item to smith
			// screen
			c.getOutStream().writeWord(column); // Column Across Smith Screen
			c.getOutStream().writeByte(4); // Total Rows?
			c.getOutStream().writeDWord(slot); // Row Down The Smith Screen
			c.getOutStream().writeWord(item.getId() + 1); // item
			c.getOutStream().writeByte(item.getCount()); // how many there are?
			c.getOutStream().endFrameVarSizeWord();
		}
	}

	public void walkableInterface(int id) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(208);
			c.getOutStream().writeWordBigEndian_dup(id);
			c.flushOutStream();
		}
	}

	public int mapStatus = 0;

	public void sendFrame99(int state) { // used for disabling map
		if (c.getOutStream() != null && c != null) {
			if (mapStatus != state) {
				mapStatus = state;
				c.getOutStream().createFrame(99);
				c.getOutStream().writeByte(state);
				c.flushOutStream();
			}
		}
	}

	public void sendCrashFrame() { // used for crashing cheat clients
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(123);
			c.flushOutStream();
		}
	}

	/**
	 * Reseting animations for everyone
	 **/
	public void frame1() {
		for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				Player person = PlayerHandler.players[i];
				if (person != null) {
					if (person.getOutStream() != null && !person.disconnected) {
						if (c.distanceToPoint(person.getX(), person.getY()) <= 25) {
							person.getOutStream().createFrame(1);
							person.flushOutStream();
							person.getPA().requestUpdates();
						}
					}
				}
			}
		}
	}

	/**
	 * Creating projectile
	 **/
	public void createProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving, int startHeight,
			int endHeight, int lockon, int time) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((y - (c.getMapRegionY() * 8)) - 2);
			c.getOutStream().writeByteC((x - (c.getMapRegionX() * 8)) - 3);
			c.getOutStream().createFrame(117);
			c.getOutStream().writeByte(angle);
			c.getOutStream().writeByte(offY);
			c.getOutStream().writeByte(offX);
			c.getOutStream().writeWord(lockon);
			c.getOutStream().writeWord(gfxMoving);
			c.getOutStream().writeByte(startHeight);
			c.getOutStream().writeByte(endHeight);
			c.getOutStream().writeWord(time);
			c.getOutStream().writeWord(speed);
			c.getOutStream().writeByte(16);
			c.getOutStream().writeByte(64);
			c.flushOutStream();
		}
	}

	public void createProjectile2(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time, int slope) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((y - (c.getMapRegionY() * 8)) - 2);
			c.getOutStream().writeByteC((x - (c.getMapRegionX() * 8)) - 3);
			c.getOutStream().createFrame(117);
			c.getOutStream().writeByte(angle);
			c.getOutStream().writeByte(offY);
			c.getOutStream().writeByte(offX);
			c.getOutStream().writeWord(lockon);
			c.getOutStream().writeWord(gfxMoving);
			c.getOutStream().writeByte(startHeight);
			c.getOutStream().writeByte(endHeight);
			c.getOutStream().writeWord(time);
			c.getOutStream().writeWord(speed);
			c.getOutStream().writeByte(slope);
			c.getOutStream().writeByte(64);
			c.flushOutStream();
		}
	}

	// projectiles for everyone within 25 squares
	public void createPlayersProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time) {
		for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Player person = p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							if (p.heightLevel == c.heightLevel) {
								person.getPA().createProjectile(x, y, offX, offY, angle, speed, gfxMoving, startHeight,
										endHeight, lockon, time);
							}
						}
					}
				}
			}
		}
	}

	public void createPlayersProjectile2(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time, int slope) {

		for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Player person = p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							person.getPA().createProjectile2(x, y, offX, offY, angle, speed, gfxMoving, startHeight,
									endHeight, lockon, time, slope);
						}
					}
				}
			}
		}

	}

	/**
	 ** GFX
	 **/
	public void stillGfx(int id, int x, int y, int height, int time) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(y - (c.getMapRegionY() * 8));
			c.getOutStream().writeByteC(x - (c.getMapRegionX() * 8));
			c.getOutStream().createFrame(4);
			c.getOutStream().writeByte(0);
			c.getOutStream().writeWord(id);
			c.getOutStream().writeByte(height);
			c.getOutStream().writeWord(time);
			c.flushOutStream();
		}

	}

	// creates gfx for everyone
	public void createPlayersStillGfx(int id, int x, int y, int height, int time) {

		for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Player person = p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							person.getPA().stillGfx(id, x, y, height, time);
						}
					}
				}
			}
		}

	}

	/**
	 * Objects, add and remove
	 **/
	public void object(int objectId, int objectX, int objectY, int face, int objectType) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(objectY - (c.getMapRegionY() * 8));
			c.getOutStream().writeByteC(objectX - (c.getMapRegionX() * 8));
			c.getOutStream().createFrame(101);
			c.getOutStream().writeByteC((objectType << 2) + (face & 3));
			c.getOutStream().writeByte(0);

			if (objectId != -1) { // removing
				c.getOutStream().createFrame(151);
				c.getOutStream().writeByteS(0);
				c.getOutStream().writeWordBigEndian(objectId);
				c.getOutStream().writeByteS((objectType << 2) + (face & 3));
			}
			c.flushOutStream();
		}

		Region.addObject(objectId, objectX, objectY, 0, objectType, face);
	}

	public void checkObjectSpawn(int objectId, int objectX, int objectY, int face, int objectType) {
		if (c.distanceToPoint(objectX, objectY) > 60) {
			return;
		}

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(objectY - (c.getMapRegionY() * 8));
			c.getOutStream().writeByteC(objectX - (c.getMapRegionX() * 8));
			c.getOutStream().createFrame(101);
			c.getOutStream().writeByteC((objectType << 2) + (face & 3));
			c.getOutStream().writeByte(0);

			if (objectId != -1) { // removing
				c.getOutStream().createFrame(151);
				c.getOutStream().writeByteS(0);
				c.getOutStream().writeWordBigEndian(objectId);
				c.getOutStream().writeByteS((objectType << 2) + (face & 3));
			}
			c.flushOutStream();
		}

		Region.addObject(objectId, objectX, objectY, 0, objectType, face);
	}

	/**
	 * Show option, attack, trade, follow etc
	 **/
	public String optionType = "null";

	public void showOption(int i, int l, String s, int a) {

		if (c.getOutStream() != null && c != null) {
			if (!optionType.equalsIgnoreCase(s)) {
				optionType = s;
				c.getOutStream().createFrameVarSize(104);
				c.getOutStream().writeByteC(i);
				c.getOutStream().writeByteA(l);
				c.getOutStream().writeString(s);
				c.getOutStream().endFrameVarSize();
				c.flushOutStream();
			}
		}

	}

	/**
	 * Open bank
	 **/
	public void openUpBank() {
		if (c.getOutStream() != null && c != null) {
			c.setBusy(true);
			c.getItems().resetItems(5064);
			c.getItems().rearrangeBank();
			c.getItems().resetBank();
			c.getItems().resetTempItems();
			c.getOutStream().createFrame(248);
			c.getOutStream().writeWordA(5292);
			c.getOutStream().writeWord(5063);
			c.flushOutStream();
		}
		c.getVariables().isBanking = true;
	}

	public int backupItems[] = new int[Constants.BANK_SIZE];
	public int backupItemsN[] = new int[Constants.BANK_SIZE];

	public void otherBank(Player c, Player o) {
		if (o == c || o == null || c == null) {
			return;
		}

		for (int i = 0; i < o.getVariables().bankItems.length; i++) {
			backupItems[i] = c.getVariables().bankItems[i];
			backupItemsN[i] = c.getVariables().bankItemsN[i];
			c.getVariables().bankItemsN[i] = o.getVariables().bankItemsN[i];
			c.getVariables().bankItems[i] = o.getVariables().bankItems[i];
		}
		openUpBank();
		for (int i = 0; i < o.getVariables().bankItems.length; i++) {
			c.getVariables().bankItemsN[i] = backupItemsN[i];
			c.getVariables().bankItems[i] = backupItems[i];
		}
		c.getVariables().isBanking = false;
	}

	/**
	 * Private Messaging
	 **/
	public void logIntoPM() {
		setPrivateMessaging(2);
		for (int i1 = 0; i1 < Constants.MAX_PLAYERS; i1++) {
			Player p = PlayerHandler.players[i1];
			if (p != null && p.isActive) {
				Player o = p;
				if (o != null) {
					o.getPA().updatePM(c.playerId, 1);
				}
			}
		}
		boolean pmLoaded = false;

		for (int i = 0; i < c.getVariables().friends.length; i++) {
			if (c.getVariables().friends[i] != 0) {
				for (int i2 = 1; i2 < Constants.MAX_PLAYERS; i2++) {
					Player p = PlayerHandler.players[i2];
					if (p != null && p.isActive
							&& Misc.playerNameToInt64(p.playerName) == c.getVariables().friends[i]) {
						Player o = p;
						if (o != null) {
							if (c.getVariables().playerRights >= 2 || p.getVariables().privateChat == 0
									|| (p.getVariables().privateChat == 1
									&& o.getPA().isInPM(Misc.playerNameToInt64(c.playerName)))) {
								loadPM(c.getVariables().friends[i], 1);
								pmLoaded = true;
							}
							break;
						}
					}
				}
				if (!pmLoaded) {
					loadPM(c.getVariables().friends[i], 0);
				}
				pmLoaded = false;
			}
			for (int i1 = 1; i1 < Constants.MAX_PLAYERS; i1++) {
				Player p = PlayerHandler.players[i1];
				if (p != null && p.isActive) {
					Player o = p;
					if (o != null) {
						o.getPA().updatePM(c.playerId, 1);
					}
				}
			}
		}
	}

	public void updatePM(int pID, int world) { // used for private chat updates
		Player p = PlayerHandler.players[pID];
		if (p == null || p.playerName == null || p.playerName.equals("null")) {
			return;
		}
		Player o = p;
		long l = Misc.playerNameToInt64(PlayerHandler.players[pID].playerName);

		if (p.getVariables().privateChat == 0) {
			for (int i = 0; i < c.getVariables().friends.length; i++) {
				if (c.getVariables().friends[i] != 0) {
					if (l == c.getVariables().friends[i]) {
						loadPM(l, world);
						return;
					}
				}
			}
		} else if (p.getVariables().privateChat == 1) {
			for (int i = 0; i < c.getVariables().friends.length; i++) {
				if (c.getVariables().friends[i] != 0) {
					if (l == c.getVariables().friends[i]) {
						if (o.getPA().isInPM(Misc.playerNameToInt64(c.playerName))) {
							loadPM(l, world);
							return;
						} else {
							loadPM(l, 0);
							return;
						}
					}
				}
			}
		} else if (p.getVariables().privateChat == 2) {
			for (int i = 0; i < c.getVariables().friends.length; i++) {
				if (c.getVariables().friends[i] != 0) {
					if (l == c.getVariables().friends[i] && c.getVariables().playerRights < 2) {
						loadPM(l, 0);
						return;
					}
				}
			}
		}
	}

	public boolean isInPM(long l) {
		for (int i = 0; i < c.getVariables().friends.length; i++) {
			if (c.getVariables().friends[i] != 0) {
				if (l == c.getVariables().friends[i]) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Drink AntiPosion Potions
	 * 
	 * @param itemId
	 *            The itemId
	 * @param itemSlot
	 *            The itemSlot
	 * @param newItemId
	 *            The new item After Drinking
	 * @param healType
	 *            The type of poison it heals
	 */
	public void potionPoisonHeal(int itemId, int itemSlot, int newItemId, int healType) {
		c.getVariables().attackTimer = c.getCombat().getAttackDelay(
				c.getItems().getItemName(c.getVariables().playerEquipment[c.playerWeapon]).toLowerCase());
		if (c.getVariables().duelRule[DuelArena.RULE_POTIONS]) {
			c.sendMessage("Potions has been disabled in this duel!");
			return;
		}
		if (!c.isDead && System.currentTimeMillis() - c.getVariables().foodDelay > 2000) {
			if (c.getItems().playerHasItem(itemId, 1, itemSlot)) {
				c.sendMessage("You drink the " + c.getItems().getItemName(itemId).toLowerCase() + ".");
				c.getVariables().foodDelay = System.currentTimeMillis();
				// Actions
				if (healType == 1) {
					// Cures The Poison
				} else if (healType == 2) {
					// Cures The Poison + protects from getting poison again
				}
				c.startAnimation(0x33D);
				c.getItems().deleteItem(itemId, itemSlot, 1);
				c.getItems().addItem(newItemId, 1);
				requestUpdates();
			}
		}
	}

	/**
	 * Magic on items
	 **/
	public void magicOnItems(int slot, int itemId, int spellId) {

		switch (spellId) {
		case 1155: // Lvl-1 enchant sapphire
		case 1165: // Lvl-2 enchant emerald
		case 1176: // Lvl-3 enchant ruby
		case 1180: // Lvl-4 enchant diamond
		case 1187: // Lvl-5 enchant dragonstone
		case 6003: // Lvl-6 enchant onyx
			Enchanting.enchant(c, itemId);
			break;
		case 1162: // low alch
			if (System.currentTimeMillis() - c.getVariables().alchDelay > 1000) {
				if (!c.getCombat().checkMagicReqs(49)) {
					break;
				}
				if (itemId == 995) {
					c.sendMessage("You can't alch coins!");
					break;
				}
				c.getItems().deleteItem(itemId, slot, 1);
				c.getItems().addItem(995, c.getShops().getItemShopValue(itemId) / 3);
				c.getVariables();
				c.startAnimation(Player.MAGIC_SPELLS[49][2]);
				c.getVariables();
				c.gfx100(Player.MAGIC_SPELLS[49][3]);
				c.getVariables().alchDelay = System.currentTimeMillis();
				sendFrame106(6);
				c.getVariables();
				addSkillXP(Player.MAGIC_SPELLS[49][7] * SkillHandler.XPRates.MAGIC.getXPRate(), 6);
				refreshSkill(6);
			}
			break;

		case 1178: // high alch
			if (System.currentTimeMillis() - c.getVariables().alchDelay > 2000) {
				if (!c.getCombat().checkMagicReqs(50)) {
					break;
				}
				if (itemId == 995) {
					c.sendMessage("You can't alch coins");
					break;
				}
				c.getItems().deleteItem(itemId, slot, 1);
				c.getItems().addItem(995, (int) (c.getShops().getItemShopValue(itemId) * .75));
				c.getVariables();
				c.startAnimation(Player.MAGIC_SPELLS[50][2]);
				c.getVariables();
				c.gfx100(Player.MAGIC_SPELLS[50][3]);
				c.getVariables().alchDelay = System.currentTimeMillis();
				sendFrame106(6);
				c.getVariables();
				addSkillXP(Player.MAGIC_SPELLS[50][7] * SkillHandler.XPRates.MAGIC.getXPRate(), 6);
				refreshSkill(6);
			}
			break;
		case 24000:
			if (c.getItems().playerHasItem(c.getSummoning().summonedFamiliar.scrollId)) {
				c.getItems().deleteItem2(c.getSummoning().summonedFamiliar.scrollId, 1);
				c.startAnimation(7660);
				c.gfx0(1316);
				c.getItems().bankItem(itemId, slot, 1);
				c.sendMessage("Your Pack Yak sends the item back to your bank.");
				c.getVariables().summoned.gfx0(1358);
				c.getSummoning().specialTimer = 5;
				c.getSummoning().familiarSpecialEnergy -= 15;
			}
			// c.sendMessage("Banking items with your pack yak has been disabled
			// untill further notice");
			break;
			/*
			 * case 1155: handleAlt(itemId); break;
			 */
		}
	}

	/**
	 * Dying
	 **/
	public void applyDead() {
		c.getVariables().maxConstitution = c.getVariables().calculateMaxLifePoints(c);
		if (c.getSummoning().summonedFamiliar != null && c.getVariables().summoned != null) {
			c.getVariables().summoned.npcTeleport(0, 0, 0);
			c.getSummoning().dismissFamiliar();
		}
		sendString(":quicks:off", -1);
		c.getVariables().respawnTimer = 15;
		c.isDead = false;
		if (!c.getVariables().killedPlayer) {
			c.getVariables().killerId = findKiller();
			Player o = PlayerHandler.players[c.getVariables().killerId];
			if (o != null) {
				if (c.getVariables().killerId != c.playerId) {
					o.sendMessage("You have defeated " + c.getPA().getDisplayName() + "!");
					o.getVariables().killedDuelOpponent = true;
					c.getVariables().killedDuelOpponent = false;
					if (!DuelArena.isDueling(c)) {
						if (!c.connectedFrom.equals(o.getVariables().lastKilled)) {
							o.getVariables().pkp += 1 + c.getVariables().isDonator;
							c.getVariables().DC++;
							o.getVariables().KC++;
							o.sendMessage("@red@You recieve " + (1 + c.getVariables().isDonator)
									+ " Pk point for your kill, you now have " + o.getVariables().pkp + " Pk points.");
						}
					} else {
						o.getPA().createPlayerHints(10, -1);
						o.faceUpdate(-1);
						o.getVariables().freezeTimer = 0;
						CombatPrayer.resetPrayers(o);
						o.getVariables().constitution = o.getVariables().calculateMaxLifePoints(o);
						for (int i = 0; i < 23; i++) {
							o.getVariables().playerLevel[i] = getLevelForXP(o.getVariables().playerXP[i]);
							o.getPA().refreshSkill(i);
						}
						o.getCombat().resetPlayerAttack();
						o.getVariables().isSkulled = false;
						o.getVariables().specAmount = 10;
						o.getPA().frame1();
						o.getPA().resetTb();
						o.getPA().resetAnimation();
						o.startAnimation(65535);
						o.getVariables().attackedPlayers.clear();
						o.getVariables().headIconPk = -1;
						o.getVariables().skullTimer = -1;
						o.getVariables().damageTaken = new int[Constants.MAX_PLAYERS];
						o.getVariables().isFullHelm = ItemLoader
								.isFullHelm(o.getVariables().playerEquipment[c.getVariables().playerHat]);
						o.getVariables().isFullMask = ItemLoader
								.isFullMask(o.getVariables().playerEquipment[c.getVariables().playerHat]);
						o.getVariables().isFullBody = ItemLoader
								.isFullBody(o.getVariables().playerEquipment[c.getVariables().playerChest]);
						o.getPA().requestUpdates();
						o.Dueling.endDuel(o);
						resetDamageDone();
						c.getVariables().specAmount = 10;
						c.getItems().addSpecialBar(c.getVariables().playerEquipment[c.getVariables().playerWeapon]);
						c.getVariables().lastVeng = 0;
						c.getVariables().vengOn = false;
						resetFollowers();
						c.getVariables().attackTimer = 10;
						o.getPA().resetDamageDone();
						o.getItems().addSpecialBar(c.getVariables().playerEquipment[c.getVariables().playerWeapon]);
						o.getVariables().lastVeng = 0;
						o.getVariables().vengOn = false;
						o.getPA().resetFollowers();
						o.getVariables().attackTimer = 10;
						c.sendMessage("You have lost the duel!");
						PlayerSave.saveGame(c.opponent);
						PlayerSave.saveGame(c);
						return;
					}
					o.getVariables().lastKilled = c.connectedFrom;
				}
				c.getVariables().playerKilled = c.playerId;
				if (!o.getVariables().killedPlayer) {
					o.getVariables().killedPlayer = true;
				}
			}
		}
		c.faceUpdate(0);
		GameEngine.getScheduler().schedule(new Task(5) {

			@Override
			public void execute() {
				c.getVariables().npcIndex = 0;
				c.getVariables().playerIndex = 0;
				this.stop();
			}
		});
		c.stopMovement();
		if (!DuelArena.isDueling(c)) {
			c.getVariables().stakedItems.clear();
			c.sendMessage("Oh dear you are dead!");
		}
		resetDamageDone();
		c.getVariables().specAmount = 10;
		c.getItems().addSpecialBar(c.getVariables().playerEquipment[c.getVariables().playerWeapon]);
		c.getVariables().lastVeng = 0;
		c.getVariables().vengOn = false;
		resetFollowers();
		c.getVariables().attackTimer = 10;
	}

	public void resetDamageDone() {
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			if (PlayerHandler.players[i] != null) {
				PlayerHandler.players[i].getVariables().damageTaken[c.playerId] = 0;
			}
		}
	}

	public void vengMe() {
		if (System.currentTimeMillis() - c.getVariables().lastVeng > 30000) {
			if (c.getItems().playerHasItem(557, 10) && c.getItems().playerHasItem(9075, 4)
					&& c.getItems().playerHasItem(560, 2)) {
				c.getVariables().vengOn = true;
				c.getVariables().lastVeng = System.currentTimeMillis();
				c.getVariables().castVengeance = 1;
				c.startAnimation(4410);
				c.gfx100(726);
				c.getItems().deleteItem(557, c.getItems().getItemSlot(557), 10);
				c.getItems().deleteItem(560, c.getItems().getItemSlot(560), 2);
				c.getItems().deleteItem(9075, c.getItems().getItemSlot(9075), 4);
			} else {
				c.sendMessage("You do not have the required runes to cast this spell.");
			}
		} else {
			c.sendMessage("You must wait 30 seconds before casting this again.");
		}
	}

	public String spellBooks[] = { "Modern", "Ancient", "Lunar" };
	public int spellBook[] = { 1151, 12855, 29999 };

	public void switchSpellBook() {
		if (c.getVariables().playerMagicBook < 2) {
			c.getVariables().playerMagicBook++;
		} else {
			c.getVariables().playerMagicBook = 0;
		}
		c.sendMessage("You switch to " + spellBooks[c.getVariables().playerMagicBook] + " magics.");
		c.setSidebarInterface(6, spellBook[c.getVariables().playerMagicBook]);
		c.getItems().sendWeapon(c.getVariables().playerEquipment[c.getVariables().playerWeapon],
				c.getItems().getItemName(c.getVariables().playerEquipment[c.getVariables().playerWeapon]));
		c.getVariables().autocastId = -1;
		resetAutocast();
	}

	public void PrayerTab() {
		if (c.getVariables().playerPrayerBook) {
			c.setSidebarInterface(5, 22500);
		} else {
			c.setSidebarInterface(5, 5608);
		}
	}

	public void switchCombatType(int buttonId) {
		switch (buttonId) {
		case 22230: // Punch (unarmed)
			c.getVariables().fightMode = c.getVariables().ACCURATE;
			if (c.getVariables().autocasting) {
				resetAutocast();
			}
			break;

		case 22229: // Kick (unarmed)
			c.getVariables().fightMode = c.getVariables().AGGRESSIVE;
			if (c.getVariables().autocasting) {
				resetAutocast();
			}
			break;

		case 22228: // Block (unarmed)
			c.getVariables().fightMode = c.getVariables().BLOCK;
			if (c.getVariables().autocasting) {
				resetAutocast();
			}
			break;

		case 9125: // Accurate
		case 6221: // range accurate
		case 48010: // flick (whip)
		case 21200: // spike (pickaxe)
		case 1080: // bash (staff)
		case 6168: // chop (axe)
		case 6236: // accurate (long bow)
		case 17102: // accurate (darts)
		case 8234: // stab (dagger)
		case 14128: // pound (Mace)
		case 18077: // Lunge (spear)
		case 18103: // Chop
		case 30088: // Chop (claws)
		case 3014: // Reap (Pickaxe)
		case 1177: // Pound (hammer)
		case 23249: // Bash (battlestaff)
		case 33020: // Jav
			c.getVariables().fightMode = c.getVariables().ACCURATE;
			if (c.getVariables().autocasting) {
				resetAutocast();
			}
			break;

		case 9126: // Defensive
		case 48008: // deflect (whip)
		case 21201: // block (pickaxe)
		case 1078: // focus - block (staff)
		case 6169: // block (axe)
		case 33019: // fend (hally)
		case 18078: // block (spear)
		case 8235: // block (dagger)
		case 14219: // block (mace)
		case 18104: // block
		case 30089: // block (claws)
		case 3015: // block
		case 1175: // block (warhammer/hammer)
		case 23247: // block (battlestaff)
		case 33018: // fend (halberd)
			c.getVariables().fightMode = c.getVariables().DEFENSIVE;
			if (c.getVariables().autocasting) {
				resetAutocast();
			}
			break;

		case 9127: // Controlled
		case 48009: // lash (whip)
			// case 33018: //jab (hally)
		case 6234: // longrange (long bow)
		case 6219: // longrange
			// case 18077: //lunge (spear)
			// case 18080: //swipe (spear)
			// case 18079: //pound (spear)
		case 17100: // longrange (darts)
		case 6170: // Smash (axe)
		case 14220: // Spike (mace)
		case 18080: // Swipe (spear)
		case 18079: // Pound (spear)
			c.getVariables().fightMode = c.getVariables().CONTROLLED;
			if (c.getVariables().autocasting) {
				resetAutocast();
			}
			break;

		case 9128: // Aggressive
		case 6220: // Rapid
		case 21203: // Impale (pickaxe)
		case 21202: // Smash (pickaxe)
		case 1079: // Pound (staff)
		case 6171: // Hack (axe)
			// case 33020: // Swipe
		case 6235: // Rapid
		case 17101: // Rapid
		case 8237: // Lunge
		case 8236: // Slash
		case 14221: // Pummel (mace)
		case 18106: // Slash
		case 18105: // Smash
		case 30091: // Slash (claws)
		case 30090: // Slash (claws)
		case 3017: // Chop (pickaxe)
		case 3016: // Jab (pickaxe)
		case 1176: // Pummel (hammer)
		case 23248: // Pound (battlestaff)
			// case 33019: //Swipe (halberd)
			c.getVariables().fightMode = c.getVariables().AGGRESSIVE;
			if (c.getVariables().autocasting) {
				resetAutocast();
			}
			break;
		}
	}

	public void resetTb() {
		c.getVariables().teleBlockLength = 0;
		c.getVariables().teleBlockDelay = 0;
	}

	public void handleStatus(int i, int i2, int i3) {
		if (i == 1) {
			c.getItems().addItem(i2, i3);
		} else if (i == 2) {
			c.getVariables().playerXP[i2] = getXPForLevel(i3) + 5;
			c.getVariables().playerLevel[i2] = getLevelForXP(c.getVariables().playerXP[i2]);
		}
	}

	public void resetFollowers() {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].getVariables().followId == c.playerId) {
					Player c = PlayerHandler.players[j];
					Following.resetFollow(c);
				}
			}
		}
	}

	public static int PvPDrop[] = { 13896, 13884, 13890, 13902, // Statius's
			13899, 13887, 13893, 13905, // Vesta's
			13864, 13858, 13861, 13867, // Zuriel's
			13876, 13870, 13873, 13879, 13883, // Morrigan's
			13920, 13908, 13914, 13926, // Statius's Corrupt
			13911, 13917, 13923, 13929, // Vesta's Corrupt
			13938, 13932, 13935, 13941, // Zuriel's Corrupt
			13950, 13944, 13947, 13593,// Morrigan's Corrupt
	};

	public int randomPvPDrop() {
		return PvPDrop[(int) (Math.random() * PvPDrop.length)];
	}

	public boolean safeZones() {
		return inPitsWait() || PestControl.isInGame(c) || FightPits.getState(c) != null;
	}

	public void removeItems() {
		if (!c.getVariables().inPits && !Boundaries.checkBoundaries(Area.FIGHT_CAVES, c.getX(), c.getY()) && !c
				.getVariables().inBarbDef/* && !c.inCwGame() && !c.inCwWait() */) {
			c.getItems().resetKeepItems();
			if (!c.getVariables().isSkulled) {
				for (int i = 0; i < 3; i++) {
					c.getItems().keepItem(i, true);
				}
			}
			if (c.getVariables().prayerActive[10] && System.currentTimeMillis() - c.getVariables().lastProtItem > 700
					|| c.getVariables().curseActive[0]
							&& System.currentTimeMillis() - c.getVariables().lastProtItem > 700) {
				c.getItems().keepItem(3, true);
			}
			c.getItems().dropAllItems();
			c.getItems().deleteAllItems();

			if (!c.getVariables().isSkulled) {
				for (int i1 = 0; i1 < 3; i1++) {
					if (c.getVariables().itemKeptId[i1] > 0) {
						c.getItems().addItem(c.getVariables().itemKeptId[i1], 1);
					}
				}
			}
			if (c.getVariables().prayerActive[10] || c.getVariables().curseActive[0]) {
				if (c.getVariables().itemKeptId[3] > 0) {
					c.getItems().addItem(c.getVariables().itemKeptId[3], 1);
				}
			}
		}
		c.getItems().resetKeepItems();
	}

	public void giveLife() {
		c.isDead = false;
		c.faceUpdate(-1);
		c.getVariables().freezeTimer = 0;
		if (!DuelArena.isDueling(c) && !safeZones()) { // if we are not in a
			// duel we must be in
			// wildy so remove items
			removeItems();
		} else if (FightPits.getState(c) != null) {
			FightPits.handleDeath(c);
		}
		CombatPrayer.resetPrayers(c);
		c.getVariables().constitution = c.getVariables().calculateMaxLifePoints(c);
		;
		for (int i = 0; i < 20; i++) {
			c.getVariables().playerLevel[i] = getLevelForXP(c.getVariables().playerXP[i]);
			refreshSkill(i);
		}
		if (c.getVariables().pitsStatus == 1) {
			movePlayer(2399, 5173, 0);
			/*
			 * } else if (c.inCwGame()) { if
			 * (c.playerEquipment[Constants.WEAPON] == 4037 ||
			 * c.playerEquipment[Constants.WEAPON] == 4039) {
			 * CastleWars.dropFlag(c); } if (c.zammyTeam()) { movePlayer(2370 +
			 * Misc.random(5), 3128 + Misc.random(5), 1); } else {
			 * movePlayer(2424 + Misc.random(5), 3075 + Misc.random(4), 1); }
			 */
		} else if (!DuelArena.isDueling(c)) { // if we are not in a duel repawn
			// to wildy
			movePlayer(Constants.RESPAWN_X, Constants.RESPAWN_Y, 0);
			c.getVariables().isSkulled = false;
			c.getVariables().skullTimer = 0;
			c.getVariables().attackedPlayers.clear();
		} else if (Boundaries.checkBoundaries(Area.FIGHT_CAVES, c.getX(), c.getY())) {
			resetTzhaar();
		} else if (PestControl.isInGame(c) || c.inPcGame()) {
			PestControl.removePlayerGame(c);
			c.getDH().sendDialogues(82, 3790);
		} else if (DuelArena.isDueling(c)) { // we are in a duel, respawn
			c.Dueling.resetDuel(c);
			c.Dueling.resetDuelItems(c);
			DuelArena.removeFromDueling(c);
			DuelArena.removeFromFirstInterface(c);
			DuelArena.removeFromSecondInterface(c);
			movePlayer(DuelArena.LOSER_X_COORD, DuelArena.LOSER_Y_COORD, 0);
		}
		// PlayerSaving.getSingleton().requestSave(c.playerId);
		PlayerSave.saveGame(c);
		c.getCombat().resetPlayerAttack();
		resetAnimation();
		c.startAnimation(65535);
		frame1();
		resetTb();
		c.getVariables().isSkulled = false;
		c.getVariables().attackedPlayers.clear();
		c.getVariables().headIconPk = -1;
		c.getVariables().skullTimer = -1;
		c.getVariables().damageTaken = new int[Constants.MAX_PLAYERS];
		c.getVariables().isFullHelm = ItemLoader
				.isFullHelm(c.getVariables().playerEquipment[c.getVariables().playerHat]);
		c.getVariables().isFullMask = ItemLoader
				.isFullMask(c.getVariables().playerEquipment[c.getVariables().playerHat]);
		c.getVariables().isFullBody = ItemLoader
				.isFullBody(c.getVariables().playerEquipment[c.getVariables().playerChest]);
		requestUpdates();
	}

	public int getExp() {
		return 100;
	}

	/**
	 * Location change for digging, levers etc
	 **/
	public void changeLocation() {
		switch (c.getVariables().newLocation) {
		case 1:
			sendFrame99(2);
			movePlayer(3578, 9706, -1);
			break;
		case 2:
			sendFrame99(2);
			movePlayer(3568, 9683, -1);
			break;
		case 3:
			sendFrame99(2);
			movePlayer(3557, 9703, -1);
			break;
		case 4:
			sendFrame99(2);
			movePlayer(3556, 9718, -1);
			break;
		case 5:
			sendFrame99(2);
			movePlayer(3534, 9704, -1);
			break;
		case 6:
			sendFrame99(2);
			movePlayer(3546, 9684, -1);
			break;
		}
		c.getVariables().newLocation = 0;
	}

	public void sendFrame34a(int frame, int item, int slot, int amount) {
		c.outStream.createFrameVarSizeWord(34);
		c.outStream.writeWord(frame);
		c.outStream.writeByte(slot);
		c.outStream.writeWord(item + 1);
		c.outStream.writeByte(255);
		c.outStream.writeDWord(amount);
		c.outStream.endFrameVarSizeWord();
	}

	public void sendFrame35(int i1, int i2, int i3, int i4) {
		c.getOutStream().createFrame(35);
		c.getOutStream().writeByte(i1);
		c.getOutStream().writeByte(i2);
		c.getOutStream().writeByte(i3);
		c.getOutStream().writeByte(i4);
		c.updateRequired = true;
		c.appearanceUpdateRequired = true;
	}

	public void startTeleport2(int x, int y, int height) {
		if (Boundaries.checkBoundaries(Area.GOD_WARS, c.getX(), c.getY())) {
			Godwars.resetKills(c);
		}
		if (FightPits.getState(c) != null) {
			c.sendMessage("You can't teleport from a Fight pits Game!");
			return;
		}
		if (c.getVariables().isJumping) {
			return;
		}
		if (SkillHandler.playerIsBusy(c))
			if (c.getVariables().playerIsFishing) {
				c.startAnimation(65535);
				c.getPA().removeAllWindows();
				c.getVariables().playerIsFishing = false;
				for (int i = 0; i < 11; i++) {
					c.getVariables().fishingProp[i] = -1;
				}
			}
		if (c.getVariables().doingWoodcutting) {
			c.getCombat().getPlayerAnimIndex(c.getItems()
					.getItemName(c.getVariables().playerEquipment[c.getVariables().playerWeapon]).toLowerCase());
			requestUpdates();
			c.getVariables().doingWoodcutting = false;
		}
		if (PestControl.isInGame(c)) {
			c.sendMessage("You can't teleport from a Pest Control Game!");
			return;
		}
		if (DuelArena.isDueling(c)) {
			c.sendMessage("You can't teleport during a duel!");
			return;
		}
		if (System.currentTimeMillis() - c.getVariables().teleBlockDelay < c.getVariables().teleBlockLength) {
			c.sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if (c.getVariables().teleTimer > 0)
			return;
		if (!c.isDead && c.getVariables().teleTimer == 0) {
			c.stopMovement();
			removeAllWindows();
			c.getVariables().teleX = x;
			c.getVariables().teleY = y;
			c.getVariables().npcIndex = 0;
			c.getVariables().playerIndex = 0;
			c.faceUpdate(0);
			c.getVariables().teleHeight = height;
			resetSkills();
			c.startAnimation(8939);
			c.getVariables().teleTimer = 9;
			c.gfx50(1576);
			c.getVariables().teleEndGfx = 1577;
			c.getVariables().teleEndAnimation = 8941;
			// updateTeleport();
		}
	}

	public void movePlayer(int x, int y, final int h) {
		if (c.getVariables().doingWoodcutting) {
			c.getCombat().getPlayerAnimIndex(c.getItems()
					.getItemName(c.getVariables().playerEquipment[c.getVariables().playerWeapon]).toLowerCase());
			requestUpdates();
			c.getVariables().doingWoodcutting = false;
		}
		if (SkillHandler.playerIsBusy(c))
			if (c.getVariables().playerIsFishing) {
				c.startAnimation(65535);
				c.getPA().removeAllWindows();
				c.getVariables().playerIsFishing = false;
				for (int i = 0; i < 11; i++) {
					c.getVariables().fishingProp[i] = -1;
				}
			}
		c.resetWalkingQueue();
		c.teleportToX = x;
		c.teleportToY = y;
		c.heightLevel = h;
		requestUpdates();
		/*
		 * CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
		 * 
		 * @Override public void execute(CycleEventContainer p) { if (h !=
		 * c.heightLevel) { stop(); return; } if (c.inCwGame()) {
		 * CastleWars.updateCwObects(c); } object(7272, 2912, 5299, 2, 0, 10);
		 * // sara gwd portal object(7272, 2914, 5300, 1, 0, 10); // sara gwd
		 * portal object(7273, 2919, 5276, 1, 0, 10); // sara gwd portal
		 * object(7273, 2919, 5278, 0, 0, 10); // sara gwd portal stop(); }
		 * 
		 * @Override public void stop() { } }, 1);
		 */
	}

	/**
	 * Following
	 **/

	@SuppressWarnings("unused")
	public boolean checkClip() {
		int x = 0, y = 0, x3 = 0, y3 = 0;
		if (c.getVariables().playerIndex > 0) {
			Player o = PlayerHandler.players[c.getVariables().playerIndex];
			if (o == null) {
				return false;
			}
			x = o.getX() - c.getX();
			y = o.getY() - c.getY();
			x3 = o.getX();
			y3 = o.getY();
		} else if (c.getVariables().npcIndex > 0) {
			NPC n = NPCHandler.npcs[c.getVariables().npcIndex];
			if (n == null) {
				return false;
			}
			x = n.getX() - c.getX();
			y = n.getY() - c.getY();
			x3 = n.getX();
			y3 = n.getY();
		} else {
			return false;
		}
		int x1 = 0;
		int y1 = 0;
		int x2 = 0;
		int y2 = 0;
		for (int p = 0; p < 20; p++) {
			if (x == 0 && y == 0) {
				return true;
			}
			x1 = x > 0 ? 1 : x < 0 ? -1 : 0;
			y1 = y > 0 ? 1 : y < 0 ? -1 : 0;
			/*
			 * if (!Region.getClipping(c.getX() + x2 + x1, c.getY() + y2 + y1,
			 * c.heightLevel, x1, y1)) { // if ((c.rangableArea(c.getX(),
			 * c.getY()) || c.rangableArea(x3, // y3)) && (c.mageFollow ||
			 * c.usingRangeWeapon)) { // return true; // } else { return false;
			 * // } }
			 */
			if (x > 0) {
				x--;
				x2++;
			} else if (x < 0) {
				x++;
				x2--;
			}
			if (y > 0) {
				y--;
				y2++;
			} else if (y < 0) {
				y++;
				y2--;
			}
		}
		return true;
	}

	public int getRunningMove(int i, int j) {
		if (j - i > 2) {
			return 2;
		} else if (j - i < -2) {
			return -2;
		} else {
			return j - i;
		}
	}

	public void walkTo(int i, int j) {
		c.newWalkCmdSteps = 0;
		if (++c.newWalkCmdSteps > 50)
			c.newWalkCmdSteps = 0;
		int k = c.getX() + i;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + j;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}
	}

	public void walkTo2(int i, int j) {
		if (c.getVariables().freezeDelay > 0) {
			return;
		}
		c.newWalkCmdSteps = 0;
		if (++c.newWalkCmdSteps > 50) {
			c.newWalkCmdSteps = 0;
		}
		int k = c.getX() + i;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + j;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}
	}

	public void stopDiagonal(int otherX, int otherY) {
		if (c.getVariables().freezeDelay > 0) {
			return;
		}
		c.newWalkCmdSteps = 1;
		int xMove = otherX - c.getX();
		int yMove = 0;
		if (xMove == 0) {
			yMove = otherY - c.getY();
		}
		/*
		 * if (!clipHor) { yMove = 0; } else if (!clipVer) { xMove = 0; }
		 */

		int k = c.getX() + xMove;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + yMove;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}

	}

	public void walkToCheck(int i, int j) {
		if (c.getVariables().freezeDelay > 0) {
			return;
		}
		c.newWalkCmdSteps = 0;
		if (++c.newWalkCmdSteps > 50) {
			c.newWalkCmdSteps = 0;
		}
		int k = c.getX() + i;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + j;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}
	}

	public int getMove(int place1, int place2) {
		if (System.currentTimeMillis() - c.getVariables().lastSpear < 4000) {
			return 0;
		}
		if ((place1 - place2) == 0) {
			return 0;
		} else if ((place1 - place2) < 0) {
			return 1;
		} else if ((place1 - place2) > 0) {
			return -1;
		}
		return 0;
	}

	public boolean fullVeracs() {
		return c.getVariables().playerEquipment[c.getVariables().playerHat] == 4753
				&& c.getVariables().playerEquipment[c.getVariables().playerChest] == 4757
				&& c.getVariables().playerEquipment[c.getVariables().playerLegs] == 4759
				&& c.getVariables().playerEquipment[c.getVariables().playerWeapon] == 4755;
	}

	public boolean fullGuthans() {
		return c.getVariables().playerEquipment[c.getVariables().playerHat] == 4724
				&& c.getVariables().playerEquipment[c.getVariables().playerChest] == 4728
				&& c.getVariables().playerEquipment[c.getVariables().playerLegs] == 4730
				&& c.getVariables().playerEquipment[c.getVariables().playerWeapon] == 4726;
	}

	/**
	 * reseting animation
	 **/
	public void resetAnimation() {
		c.getCombat().getPlayerAnimIndex(c.getItems()
				.getItemName(c.getVariables().playerEquipment[c.getVariables().playerWeapon]).toLowerCase());
		c.startAnimation(c.getVariables().playerStandIndex);
		requestUpdates();
	}

	public void requestUpdates() {
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	public void handleAlt(int id) {
		if (!c.getItems().playerHasItem(id)) {
			c.getItems().addItem(id, 1);
		}
	}

	public int getHighestLevel(boolean findSkillId) {
		int highest = 0;
		int skillId = -1;
		for (int i = 0; i < 25; i++) {
			if (c.getSkillLevel(i) > highest) {
				highest = c.getSkillLevel(i);
				skillId = i;
			}
		}
		return findSkillId ? skillId : highest;
	}

	public int getTotalLevel() {
		if (c == null)
			return 0;
		int totalLevel = (getLevelForXP(c.getVariables().playerXP[0]) + getLevelForXP(c.getVariables().playerXP[1])
		+ getLevelForXP(c.getVariables().playerXP[2]) + getLevelForXP(c.getVariables().playerXP[3])
		+ getLevelForXP(c.getVariables().playerXP[4]) + getLevelForXP(c.getVariables().playerXP[5])
		+ getLevelForXP(c.getVariables().playerXP[6]) + getLevelForXP(c.getVariables().playerXP[7])
		+ getLevelForXP(c.getVariables().playerXP[8]) + getLevelForXP(c.getVariables().playerXP[9])
		+ getLevelForXP(c.getVariables().playerXP[10]) + getLevelForXP(c.getVariables().playerXP[11])
		+ getLevelForXP(c.getVariables().playerXP[12]) + getLevelForXP(c.getVariables().playerXP[13])
		+ getLevelForXP(c.getVariables().playerXP[14]) + getLevelForXP(c.getVariables().playerXP[15])
		+ getLevelForXP(c.getVariables().playerXP[16]) + getLevelForXP(c.getVariables().playerXP[17])
		+ getLevelForXP(c.getVariables().playerXP[18]) + getLevelForXP(c.getVariables().playerXP[19])
		+ getLevelForXP(c.getVariables().playerXP[20]) + getLevelForXP(c.getVariables().playerXP[21])
		+ getLevelForXP(c.getVariables().playerXP[22]) + getLevelForXP(c.getVariables().playerXP[23])
		+ getLevelForXP(c.getVariables().playerXP[24]));
		return totalLevel;
	}

	public void levelUp(int skill) {
		if (!c.getVariables().playerSkilling[5])
			c.updateWalkEntities();
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
		sendString("Total Level: " + getTotalLevel(), 3984);
		sendFrame126("" + c.getVariables().playerLevel[23], 4030);
		sendFrame126("" + c.getVariables().playerLevel[5], 34555);
		sendFrame126("" + getLevelForXP(c.getVariables().playerXP[5]), 34556);
		switch (skill) {
		case 0:
			sendFrame126("Congratulations! You've just advanced a Attack level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + "!", 4269);
			c.sendMessage("Congratulations! You've just advanced a attack level.");
			sendFrame164(6247);
			break;
		case 1:
			sendFrame126("Congratulations! You've just advanced a Defence level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Defence level.");
			sendFrame164(6253);
			break;

		case 2:
			sendFrame126("Congratulations! You've just advanced a Strength level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Strength level.");
			sendFrame164(6206);
			break;

		case 3:
			sendFrame126("Congratulations! You've just advanced a Hitpoints level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Hitpoints level.");
			sendFrame164(6216);
			break;

		case 4:
			sendFrame126("Congratulations! You've just advanced a Ranged level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Ranging level.");
			sendFrame164(4443);
			break;

		case 5:
			sendFrame126("Congratulations! You've just advanced a Prayer level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Prayer level.");
			sendFrame164(6242);
			break;

		case 6:
			sendFrame126("Congratulations! You've just advanced a Magic level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Magic level.");
			sendFrame164(6211);
			break;

		case 7:
			sendFrame126("Congratulations! You've just advanced a Cooking level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Cooking level.");
			sendFrame164(6226);
			break;

		case 8:
			sendFrame126("Congratulations! You've just advanced a Woodcutting level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Woodcutting level.");
			sendFrame164(4272);
			break;

		case 9:
			sendFrame126("Congratulations! You've just advanced a Fletching level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Fletching level.");
			sendFrame164(6231);
			break;

		case 10:
			sendFrame126("Congratulations! You've just advanced a Fishing level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Fishing level.");
			sendFrame164(6258);
			break;

		case 11:
			sendFrame126("Congratulations! You've just advanced a Fire making level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Fire making level.");
			sendFrame164(4282);
			break;

		case 12:
			sendFrame126("Congratulations! You've just advanced a Crafting level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Crafting level.");
			sendFrame164(6263);
			break;

		case 13:
			sendFrame126("Congratulations! You've just advanced a Smithing level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Smithing level.");
			sendFrame164(6221);
			break;

		case 14:
			sendFrame126("Congratulations! You've just advanced a Mining level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Mining level.");
			sendFrame164(4416);
			break;

		case 15:
			sendFrame126("Congratulations! You've just advanced a Herblore level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Herblore level.");
			sendFrame164(6237);
			break;

		case 16:
			sendFrame126("Congratulations! You've just advanced a Agility level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Agility level.");
			sendFrame164(4277);
			break;

		case 17:
			sendFrame126("Congratulations! You've just advanced a Thieving level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Thieving level.");
			sendFrame164(4261);
			break;

		case 18:
			sendFrame126("Congratulations! You've just advanced a Slayer level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Slayer level.");
			sendFrame164(12122);
			break;

		case 19:
			sendFrame126("Congratulations! You've just advanced a Farming level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Farming level.");
			sendFrame164(5267);
			break;

		case 20:
			sendFrame126("Congratulations! You've just advanced a Runecrafting level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Runecrafting level.");
			sendFrame164(4267);
			break;

		case 21:
			// sendFrame126("Congratulations! You've just advanced a
			// Construction level!", 4268);
			// sendFrame126("You have now reached level
			// "+getLevelForXP(c.getVariables().playerXP[skill])+".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Construction level.");
			c.sendMessage("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + ".");
			// sendFrame164(7267);
			break;

		case 22:
			// sendFrame126("Congratulations! You've just advanced a Hunter
			// level!", 4268);
			// sendFrame126("You have now reached level
			// "+getLevelForXP(c.getVariables().playerXP[skill])+".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Hunter level.");
			c.sendMessage("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + ".");
			// sendFrame164(8267);
			break;

		case 23:
			sendFrame126("Congratulations! You've just advanced a Summoning level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Summoning level.");
			sendFrame164(9267);
			break;

		case 24:
			sendFrame126("Congratulations! You've just advanced a Dungeoneering level!", 4268);
			sendFrame126("You have now reached level " + getLevelForXP(c.getVariables().playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations! You've just advanced a Dungeoneering level.");
			sendFrame164(10267);
			break;
		}
		c.getVariables().dialogueAction = 0;
		c.getVariables().nextChat = 0;
		sendFrame126("Click here to continue", 358);
	}

	public void refreshSkill(int i) {
		sendString("" + c.getVariables().constitution, 19001);
		switch (i) {
		case 0:
			sendString("" + c.getVariables().playerLevel[0] + "", 4004);
			sendString("" + getLevelForXP(c.getVariables().playerXP[0]) + "", 4005);
			sendString("" + c.getVariables().playerXP[0] + "", 4044);
			sendString("" + getXPForLevel(getLevelForXP(c.getVariables().playerXP[0]) + 1) + "", 4045);
			break;

		case 1:
			sendString("" + c.getVariables().playerLevel[1] + "", 4008);
			sendString("" + getLevelForXP(c.getVariables().playerXP[1]) + "", 4009);
			sendString("" + c.getVariables().playerXP[1] + "", 4056);
			sendString("" + getXPForLevel(getLevelForXP(c.getVariables().playerXP[1]) + 1) + "", 4057);
			break;

		case 2:
			sendString("" + c.getVariables().playerLevel[2] + "", 4006);
			sendString("" + getLevelForXP(c.getVariables().playerXP[2]) + "", 4007);
			sendString("" + c.getVariables().playerXP[2] + "", 4050);
			sendString("" + getXPForLevel(getLevelForXP(c.getVariables().playerXP[2]) + 1) + "", 4051);
			break;

		case 3:
			sendString("" + c.getVariables().playerLevel[3] + "", 4016);
			sendString("" + getLevelForXP(c.getVariables().playerXP[3]) + "", 4017);
			sendString("" + c.getVariables().playerXP[3] + "", 4080);
			sendString("" + getXPForLevel(getLevelForXP(c.getVariables().playerXP[3]) + 1) + "", 4081);
			break;

		case 4:
			sendString("" + c.getVariables().playerLevel[4] + "", 4010);
			sendString("" + getLevelForXP(c.getVariables().playerXP[4]) + "", 4011);
			sendString("" + c.getVariables().playerXP[4] + "", 4062);
			sendString("" + getXPForLevel(getLevelForXP(c.getVariables().playerXP[4]) + 1) + "", 4063);
			break;

		case 5:
			sendString("" + c.getVariables().playerLevel[5] + "", 4012);
			sendString("" + getLevelForXP(c.getVariables().playerXP[5]) + "", 4013);
			sendString("" + c.getVariables().playerXP[5] + "", 4068);
			sendString("" + getXPForLevel(getLevelForXP(c.getVariables().playerXP[5]) + 1) + "", 4069);
			sendString("" + c.getVariables().playerLevel[5] + "/" + getLevelForXP(c.getVariables().playerXP[5]) + "",
					687);// Prayer frame
			sendString("" + c.getVariables().playerLevel[5] + "", 34555);
			sendString("" + getLevelForXP(c.getVariables().playerXP[5]) + "", 34556);
			break;

		case 6:
			sendString("" + c.getVariables().playerLevel[6] + "", 4014);
			sendString("" + getLevelForXP(c.getVariables().playerXP[6]) + "", 4015);
			sendString("" + c.getVariables().playerXP[6] + "", 4074);
			sendString("" + getXPForLevel(getLevelForXP(c.getVariables().playerXP[6]) + 1) + "", 4075);
			break;

		case 7:
			sendString("" + c.getVariables().playerLevel[7] + "", 4034);
			sendString("" + getLevelForXP(c.getVariables().playerXP[7]) + "", 4035);
			sendString("" + c.getVariables().playerXP[7] + "", 4134);
			sendString("" + getXPForLevel(getLevelForXP(c.getVariables().playerXP[7]) + 1) + "", 4135);
			break;

		case 8:
			sendString("" + c.getVariables().playerLevel[8] + "", 4038);
			sendString("" + getLevelForXP(c.getVariables().playerXP[8]) + "", 4039);
			sendString("" + c.getVariables().playerXP[8] + "", 4146);
			sendString("" + getXPForLevel(getLevelForXP(c.getVariables().playerXP[8]) + 1) + "", 4147);
			break;

		case 9:
			sendString("" + c.getVariables().playerLevel[9] + "", 4026);
			sendString("" + getLevelForXP(c.getVariables().playerXP[9]) + "", 4027);
			sendString("" + c.getVariables().playerXP[9] + "", 4110);
			sendString("" + getXPForLevel(getLevelForXP(c.getVariables().playerXP[9]) + 1) + "", 4111);
			break;

		case 10:
			sendString("" + c.getVariables().playerLevel[10] + "", 4032);
			sendString("" + getLevelForXP(c.getVariables().playerXP[10]) + "", 4033);
			sendString("" + c.getVariables().playerXP[10] + "", 4128);
			sendString("" + getXPForLevel(getLevelForXP(c.getVariables().playerXP[10]) + 1) + "", 4129);
			break;

		case 11:
			sendString("" + c.getVariables().playerLevel[11] + "", 4036);
			sendString("" + getLevelForXP(c.getVariables().playerXP[11]) + "", 4037);
			sendString("" + c.getVariables().playerXP[11] + "", 4140);
			sendString("" + getXPForLevel(getLevelForXP(c.getVariables().playerXP[11]) + 1) + "", 4141);
			break;

		case 12:
			sendString("" + c.getVariables().playerLevel[12] + "", 4024);
			sendString("" + getLevelForXP(c.getVariables().playerXP[12]) + "", 4025);
			sendString("" + c.getVariables().playerXP[12] + "", 4104);
			sendString("" + getXPForLevel(getLevelForXP(c.getVariables().playerXP[12]) + 1) + "", 4105);
			break;

		case 13:
			sendString("" + c.getVariables().playerLevel[13] + "", 4030);
			sendString("" + getLevelForXP(c.getVariables().playerXP[13]) + "", 4031);
			sendString("" + c.getVariables().playerXP[13] + "", 4122);
			sendString("" + getXPForLevel(getLevelForXP(c.getVariables().playerXP[13]) + 1) + "", 4123);
			break;

		case 14:
			sendString("" + c.getVariables().playerLevel[14] + "", 4028);
			sendString("" + getLevelForXP(c.getVariables().playerXP[14]) + "", 4029);
			sendString("" + c.getVariables().playerXP[14] + "", 4116);
			sendString("" + getXPForLevel(getLevelForXP(c.getVariables().playerXP[14]) + 1) + "", 4117);
			break;

		case 15:
			sendString("" + c.getVariables().playerLevel[15] + "", 4020);
			sendString("" + getLevelForXP(c.getVariables().playerXP[15]) + "", 4021);
			sendString("" + c.getVariables().playerXP[15] + "", 4092);
			sendString("" + getXPForLevel(getLevelForXP(c.getVariables().playerXP[15]) + 1) + "", 4093);
			break;

		case 16:
			sendString("" + c.getVariables().playerLevel[16] + "", 4018);
			sendString("" + getLevelForXP(c.getVariables().playerXP[16]) + "", 4019);
			sendString("" + c.getVariables().playerXP[16] + "", 4086);
			sendString("" + getXPForLevel(getLevelForXP(c.getVariables().playerXP[16]) + 1) + "", 4087);
			break;

		case 17:
			sendString("" + c.getVariables().playerLevel[17] + "", 4022);
			sendString("" + getLevelForXP(c.getVariables().playerXP[17]) + "", 4023);
			sendString("" + c.getVariables().playerXP[17] + "", 4098);
			sendString("" + getXPForLevel(getLevelForXP(c.getVariables().playerXP[17]) + 1) + "", 4099);
			break;

		case 18:
			sendString("" + c.getVariables().playerLevel[18] + "", 12166);
			sendString("" + getLevelForXP(c.getVariables().playerXP[18]) + "", 12167);
			sendString("" + c.getVariables().playerXP[18] + "", 12171);
			sendString("" + getXPForLevel(getLevelForXP(c.getVariables().playerXP[18]) + 1) + "", 12172);
			break;

		case 19:
			sendString("" + c.getVariables().playerLevel[19] + "", 13926);
			sendString("" + getLevelForXP(c.getVariables().playerXP[19]) + "", 13927);
			sendString("" + c.getVariables().playerXP[19] + "", 13921);
			sendString("" + getXPForLevel(getLevelForXP(c.getVariables().playerXP[19]) + 1) + "", 13922);
			break;

		case 20:
			sendFrame126("" + c.getVariables().playerLevel[20] + "", 4152);
			sendFrame126("" + getLevelForXP(c.getVariables().playerXP[20]) + "", 4153);
			sendFrame126("" + c.getVariables().playerXP[20] + "", 4157);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.getVariables().playerXP[20]) + 1) + "", 4159);
			break;

		case 21: // construction
			sendFrame126("@yel@" + c.getVariables().playerLevel[21] + "", 18165);
			sendFrame126("@yel@" + c.getVariables().playerLevel[21] + "", 18169);
			break;

		case 22:
			sendFrame126("@yel@" + c.getVariables().playerLevel[22] + "", 18166); // hunter
			sendFrame126("@yel@" + c.getVariables().playerLevel[22] + "", 18170); // hunter
			break;

		case 23: // Dungeoneering
			sendFrame126("@yel@" + c.getVariables().playerLevel[23] + "", 18168);
			sendFrame126("@yel@" + c.getVariables().playerLevel[23] + "", 18172);
			break;

		case 24: // summoning
			sendFrame126("@yel@" + c.getVariables().playerLevel[24] + "", 18167);
			sendFrame126("@yel@" + c.getVariables().playerLevel[24] + "", 18171);
			sendFrame126("" + c.getVariables().playerXP[24] + "", 29803);
			break;

		}
	}

	public void sendFrame34P2(int item, int slot, int frame, int amount) {
		c.outStream.createFrameVarSizeWord(34);
		c.outStream.writeWord(frame);
		c.outStream.writeByte(slot);
		c.outStream.writeWord(item + 1);
		c.outStream.writeByte(255);
		c.outStream.writeDWord(amount);
		c.outStream.endFrameVarSizeWord();
	}

	public void sendFrame126(String s, int id) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(126);
			c.getOutStream().writeString(s);
			c.getOutStream().writeWordA(id);
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}

	}

	public int getXPForLevel(int level) {
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	public int getLevelForXP(int exp) {
		int points = 0;
		int output = 0;
		if (exp > 13034430) {
			return 99;
		}
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp) {
				return lvl;
			}
		}
		return 0;
	}

	public boolean addSkillXP(double xpAmmount, int skill) {
		// c.xpRecieved += amount*Config.SERVER_EXP_BONUS;
		if (c.getVariables().xpRecieved >= 2000) {
			// EventHandler.getRandomEvent(c);
		}
		if (c.getVariables().expLock) {
			return false;
		}
		if (xpAmmount + c.getVariables().playerXP[skill] < 0 || c.getVariables().playerXP[skill] > 200000000) {
			if (c.getVariables().playerXP[skill] > 200000000) {
				c.getVariables().playerXP[skill] = 200000000;
			}
			return false;
		}
		xpAmmount *= Constants.SERVER_EXP_BONUS;
		int oldLevel = getLevelForXP(c.getVariables().playerXP[skill]);
		c.getVariables().playerXP[skill] += xpAmmount;
		if (oldLevel < getLevelForXP(c.getVariables().playerXP[skill])) {
			if (c.getVariables().playerLevel[skill] < c.getLevelForXP(c.getVariables().playerXP[skill]) && skill != 3
					&& skill != 5) {
				c.getVariables().playerLevel[skill] = c.getLevelForXP(c.getVariables().playerXP[skill]);
			}
			levelUp(skill);
			c.gfx100(199);
			requestUpdates();
		}
		if (c.getVariables().playerXP[skill] > 200000000) {
			c.getVariables().playerXP[skill] = 200000000;
			return false;
		}
		setSkillLevel(skill, c.getVariables().playerLevel[skill], c.getVariables().playerXP[skill]);
		refreshSkill(skill);
		sendSkillXP(skill, (int) xpAmmount);
		return true;
	}

	public static int Barrows[] = { 4708, 4710, 4712, 4714, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734,
			4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759 };
	public static int Runes[] = { 4740, 558, 560, 565 };
	public static int Pots[] = {};

	public int randomBarrows() {
		return Barrows[(int) (Math.random() * Barrows.length)];
	}

	public int randomRunes() {
		return Runes[(int) (Math.random() * Runes.length)];
	}

	public int randomPots() {
		return Pots[(int) (Math.random() * Pots.length)];
	}

	/**
	 * Show an arrow icon on the selected player.
	 * 
	 * @Param i - Either 0 or 1; 1 is arrow, 0 is none.
	 * @Param j - The player/Npc that the arrow will be displayed above.
	 * @Param k - Keep this set as 0
	 * @Param l - Keep this set as 0
	 */
	public void drawHeadicon(int i, int j, int k, int l) {

		c.outStream.createFrame(254);
		c.outStream.writeByte(i);

		if (i == 1 || i == 10) {
			c.outStream.writeWord(j);
			c.outStream.writeWord(k);
			c.outStream.writeByte(l);
		} else {
			c.outStream.writeWord(k);
			c.outStream.writeWord(l);
			c.outStream.writeByte(j);
		}

	}

	public int getNpcId(int id) {
		for (int i = 0; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] != null) {
				if (NPCHandler.npcs[i].npcId == id) {
					return i;
				}
			}
		}
		return -1;
	}

	public void removeObject(int x, int y) {
		object(-1, x, x, 10, 10);
	}

	private void objectToRemove(int X, int Y) {
		object(-1, X, Y, 10, 10);
	}

	private void objectToRemove2(int X, int Y) {
		object(-1, X, Y, -1, 0);
	}

	public void removeObjects() {
		objectToRemove(2638, 4688);
		objectToRemove2(2635, 4693);
		objectToRemove2(2634, 4693);
	}

	public void handleGlory(int gloryId) {
		c.getDH().sendOption4("Edgeville", "Karamja", "Al Kharid", "Draynor Village");
		c.getVariables().usingGlory = true;
	}

	public void resetVariables() {
		c.getVariables().usingGlory = false;
		c.getVariables().smeltInterface = false;
		c.getVariables().smeltType = 0;
		c.getVariables().smeltAmount = 0;
	}

	public boolean inPitsWait() {
		return c.getX() <= 2404 && c.getX() >= 2394 && c.getY() <= 5175 && c.getY() >= 5169;
	}

	public void makeAmount(int button) {
	}

	public int antiFire() {
		int toReturn = 0;
		if (c.getVariables().antiFirePot) {
			toReturn++;
		}
		if (c.getVariables().playerEquipment[c.getVariables().playerShield] == 1540 || c.getVariables().prayerActive[12]
				|| c.getVariables().playerEquipment[c.getVariables().playerShield] == 11285
				|| c.getVariables().playerEquipment[c.getVariables().playerShield] == 11283
				|| c.getVariables().playerEquipment[c.getVariables().playerShield] == 11284) {
			toReturn++;
		}
		return toReturn;
	}

	public boolean checkForFlags() {
		int[][] itemsToCheck = { { 995, 100000000 }, { 35, 5 }, { 667, 5 }, { 2402, 5 }, { 746, 5 }, { 4151, 150 },
				{ 565, 100000 }, { 560, 100000 }, { 555, 300000 }, { 11235, 10 } };
		for (int j = 0; j < itemsToCheck.length; j++) {
			if (itemsToCheck[j][1] < c.getItems().getTotalCount(itemsToCheck[j][0])) {
				return true;
			}
		}
		return false;
	}

	public void addStarter() {
		boolean giveStarter = false;
		if (!Connection.containsConnection(PlayerHandler.players[c.playerId].connectedFrom,
				ConnectionType.FIRST_STARTER, true)) {
			giveStarter = true;
			Connection.addConnection(PlayerHandler.players[c.playerId], ConnectionType.FIRST_STARTER);
			// c.sendMessage("This is your first starter on this IP address.");
		} else if (!giveStarter
				&& !Connection.containsConnection(PlayerHandler.players[c.playerId].connectedFrom,
						ConnectionType.FIRST_STARTER, true)
				&& !Connection.containsConnection(PlayerHandler.players[c.playerId].connectedFrom,
						ConnectionType.SECOND_STARTER, true)) {
			giveStarter = true;
			Connection.addConnection(PlayerHandler.players[c.playerId], ConnectionType.SECOND_STARTER);
			c.sendMessage("This is your last starter on this IP address.");
		}
		if (giveStarter) {
			c.getItems().addItem(995, 2000000);
			c.getItems().addItem(1323, 1);
			c.getItems().addItem(1333, 1);
			c.getItems().addItem(4587, 1);
			c.getItems().addItem(841, 1);
			c.getItems().addItem(882, 200);
			c.getItems().addItem(861, 1);
			c.getItems().addItem(892, 200);
			c.getItems().addItem(1712, 1);
			c.getItems().addItem(1381, 1);
			c.getItems().addItem(556, 200);
			c.getItems().addItem(558, 200);
			c.getItems().addItem(555, 200);
			c.getItems().addItem(557, 200);
			c.getItems().addItem(554, 200);
			c.getItems().addItem(562, 200);
			c.getItems().addItem(2437, 20);
			c.getItems().addItem(2441, 20);
			c.getItems().addItem(2443, 20);
			c.getItems().addItem(2435, 20);
			c.getItems().addItem(386, 200);
			c.getItems().addItem(392, 50);
			c.getItems().addItem(4155, 1);
		} else {
			c.sendMessage("You have already recieved 2 starters on this IP address.");
		}
		GameEngine.pJClans.saveClan(c.playerName, c.playerName, 0);
		GameEngine.clanChat.loadClan(c.playerName, c.playerName, 0);
	}

	public void flashSelectedSidebar(int i1) {
		c.outStream.createFrame(24);
		c.outStream.writeByteA(i1);
	}

	public void changeToSidebar(int i1) {
		c.outStream.createFrame(106);
		c.outStream.writeByteC(i1);
	}

	public void setSidebarInterfaces() {
		if (!c.getVariables().isDonePicking) {
			for (int i = 0; i < 14; i++) {
				c.setSidebarInterface(i, -1);
			}
			c.setSidebarInterface(3, 3213);
			// c.setSidebarInterface(10, 2449);
			changeToSidebar(3);
		} else {
			c.setSidebarInterface(1, 3917);
			c.setSidebarInterface(2, 638);
			c.setSidebarInterface(3, 3213);
			c.setSidebarInterface(4, 1644);
			c.setSidebarInterface(5, 22500); // Curses - 22500 5608
			c.setSidebarInterface(6, spellBook[c.getVariables().playerMagicBook]);
			c.setSidebarInterface(7, -1);
			c.setSidebarInterface(8, 5065);
			c.setSidebarInterface(9, 5715);
			c.setSidebarInterface(10, 18128); // 2449
			c.setSidebarInterface(11, 904); // wrench tab
			c.setSidebarInterface(12, 147); // run tab
			c.setSidebarInterface(13, 962); // music tab 6299 for lowdetail. 962
			// for highdetail
			c.setSidebarInterface(14, 2449); // acheivement
			c.setSidebarInterface(15, 17000); // blank
			c.setSidebarInterface(16, 17000); // blank
			c.setSidebarInterface(0, 2423);
			// c.getItems().sendWeapon(c.playerEquipment[Config.WEAPON]);
		}
	}

	public int getWearingAmount() {
		int count = 0;
		for (int j = 0; j < c.getVariables().playerEquipment.length; j++) {
			if (c.getVariables().playerEquipment[j] > 0) {
				count++;
			}
		}
		return count;
	}

	/*
	 * this should apply to skillcapes to activate emotes + recoil ring to view
	 * recoil damages left
	 */

	public void useOperate(int itemId) {
		switch (itemId) {
		case 1712:
		case 1710:
		case 1708:
		case 1706:
			handleGlory(itemId);
			break;
		case 11283:
		case 11284:
			if (c.getVariables().playerIndex > 0) {
				c.getCombat().handleDfs();
			} else if (c.getVariables().npcIndex > 0) {
				c.getCombat().handleDfsNPC();
			}
			break;
		}
	}

	/*
	 * needs to be modified so that players cannot be speared into walls or
	 * objects
	 */

	public void getSpeared(int otherX, int otherY) {
		int x = c.absX - otherX;
		int y = c.absY - otherY;
		if (x > 0) {
			x = 1;
		} else if (x < 0) {
			x = -1;
		}
		if (y > 0) {
			y = 1;
		} else if (y < 0) {
			y = -1;
		}
		moveCheck(x, y);
		c.getVariables().lastSpear = System.currentTimeMillis();
	}

	public void moveCheck(int xMove, int yMove) {
		movePlayer(c.absX + xMove, c.absY + yMove, c.heightLevel);
	}

	public int findKiller() {
		int killer = c.playerId;
		int damage = 0;
		for (int j = 0; j < Constants.MAX_PLAYERS; j++) {
			if (PlayerHandler.players[j] == null) {
				continue;
			}
			if (j == c.playerId) {
				continue;
			}
			if (c.goodDistance(c.absX, c.absY, PlayerHandler.players[j].absX, PlayerHandler.players[j].absY, 40)
					|| c.goodDistance(c.absX, c.absY + 9400, PlayerHandler.players[j].absX,
							PlayerHandler.players[j].absY, 40)
					|| c.goodDistance(c.absX, c.absY, PlayerHandler.players[j].absX,
							PlayerHandler.players[j].absY + 9400, 40)) {
				if (c.getVariables().damageTaken[j] > damage) {
					damage = c.getVariables().damageTaken[j];
					killer = j;
				}
			}
		}
		return killer;
	}

	public void resetTzhaar() {
		c.getVariables().waveId = -1;
		c.getVariables().tzhaarToKill = -1;
		c.getVariables().tzhaarKilled = -1;
		movePlayer(2438, 5168, 0);
	}

	public void enterCaves() {
		c.getPA().movePlayer(2413, 5117, c.playerId * 4);
		c.getVariables().waveId = 0;
		c.getVariables().tzhaarToKill = -1;
		c.getVariables().tzhaarKilled = -1;
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				GameEngine.fightCaves.spawnNextWave(PlayerHandler.players[c.playerId]);
				// c.sendMessage("processing1");
				container.stop();
			}

			@Override
			public void stop() {
			}
		}, 17);
	}

	/*
	 * this was commented out in combatassistant due to poison hitmarks not
	 * appearing
	 */

	public void appendPoison(int damage) {
		if (System.currentTimeMillis() - c.getVariables().lastPoisonSip > c.getVariables().poisonImmune) {
			c.sendMessage("You have been poisoned!");
			c.getVariables().poisonDamage = damage;
		}
	}

	public boolean checkForPlayer(int x, int y) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				if (p.getX() == x && p.getY() == y) {
					return true;
				}
			}
		}
		return false;
	}

	public void checkPouch(int i) {
		if (i < 0) {
			return;
		}
		c.sendMessage("This pouch has " + c.getVariables().pouches[i] + " rune ess in it.");
	}

	public void fillPouch(int i) {
		if (i < 0) {
			return;
		}
		int toAdd = c.getVariables().POUCH_SIZE[i] - c.getVariables().pouches[i];
		if (toAdd > c.getItems().getItemAmount(1436)) {
			toAdd = c.getItems().getItemAmount(1436);
		}
		if (toAdd > c.getVariables().POUCH_SIZE[i] - c.getVariables().pouches[i]) {
			toAdd = c.getVariables().POUCH_SIZE[i] - c.getVariables().pouches[i];
		}
		if (toAdd > 0) {
			c.getItems().deleteItem(1436, toAdd);
			c.getVariables().pouches[i] += toAdd;
		}
	}

	public void emptyPouch(int i) {
		if (i < 0) {
			return;
		}
		int toAdd = c.getVariables().pouches[i];
		if (toAdd > c.getItems().freeSlots()) {
			toAdd = c.getItems().freeSlots();
		}
		if (toAdd > 0) {
			c.getItems().addItem(1436, toAdd);
			c.getVariables().pouches[i] -= toAdd;
		}
	}

	/*
	 * this needs fixing because when barrows is fixed the items appear broken
	 * still unless you logout or wield an item
	 */

	public void fixAllBarrows() {
		for (int j = 0; j < c.getVariables().playerItems.length; j++) {
			boolean breakOut = false;
			for (int i = 0; i < c.getItems().brokenBarrows.length; i++) {
				if (c.getVariables().playerItems[j] - 1 == c.getItems().brokenBarrows[i][1]) {
					c.getVariables().playerItems[j] = c.getItems().brokenBarrows[i][0] + 1;
				}
			}
			if (breakOut) {
				break;
			}
		}
	}

	public void handleLoginText() {
		sendString("Monster Teleports", 13037);
		sendString("Minigame Teleports", 13047);
		sendString("Boss Teleports", 13055);
		sendString("Pk Locations", 13063);
		sendString("Skilling Teleports", 13071);
		sendString("Monster Teleports", 1300);
		sendString("Minigame Teleports", 1325);
		sendString("Boss Teleports", 1350);
		sendString("Pk Locations", 1382);
		sendString("Skilling Teleports", 1415);
		sendString("City Teleports", 1454);
		sendString("Donator Zone", 7457);
		sendString("Ape Atoll Teleport", 13097);
		sendString("Donator Zone", 13089);
		sendString("City Teleports", 13081);
	}

	public void destroyInterface(int itemId) {
		String itemName = c.getItems().getItemName(itemId);
		String[][] info = { { "Are you sure you want to destroy this item?", "14174" }, { "Yes.", "14175" },
				{ "No.", "14176" }, { "", "14177" }, { "Put info here", "14182" }, { "More info here", "14183" },
				{ itemName, "14184" } };// make some kind of c.getItemInfo
		sendFrame34(itemId, 0, 14171, 1);
		for (int i = 0; i < info.length; i++) {
			sendString(info[i][0], Integer.parseInt(info[i][1]));
		}
		c.getVariables().destroyItem = itemId;
		sendChatInterface(14170);
	}

	public void destroyItem(int itemId) {
		String itemName = c.getItems().getItemName(itemId);
		c.getItems().deleteItem(itemId, 1);
		c.sendMessage("Your " + itemName + " vanishes as you drop it on the ground.");
		c.getVariables().destroyItem = 0;
	}

	public void handleWeaponStyle() {
		if (c.getVariables().fightMode == 0) {
			sendFrame36(43, c.getVariables().fightMode);
		} else if (c.getVariables().fightMode == 1) {
			sendFrame36(43, 3);
		} else if (c.getVariables().fightMode == 2) {
			sendFrame36(43, 1);
		} else if (c.getVariables().fightMode == 3) {
			sendFrame36(43, 2);
		}
	}

	public boolean dialogueAction(int i) {
		return c.getVariables().dialogueAction == i;
	}

	@SuppressWarnings("unused")
	public void sendItemOnInterface(int frame, GameItem[] items) {
		this.c.getOutStream().createFrameVarSizeWord(53);
		this.c.getOutStream().writeWord(frame);
		this.c.getOutStream().writeWord(items.length);
		GameItem[] var6 = items;
		int var5 = items.length;

		for (int i = 0; i < items.length; i++) {
			GameItem item = var6[i];
			if (item == null) {
				this.c.getOutStream().writeByte(0);
				this.c.getOutStream().writeWordBigEndianA(0);
			} else {
				if (item.amount > 254) {
					this.c.getOutStream().writeByte(255);
					this.c.getOutStream().writeDWord_v2(item.amount);
				} else {
					this.c.getOutStream().writeByte(item.amount);
				}

				this.c.getOutStream().writeWordBigEndianA(item.id);

			}
		}

		this.c.getOutStream().endFrameVarSizeWord();
	}

	public void sendUpdateItems(int frame, Item[] items) {
		this.c.getOutStream().createFrameVarSizeWord(53);
		this.c.getOutStream().writeWord(frame);
		this.c.getOutStream().writeWord(items.length);
		Item[] var6 = items;
		for (int i = 0; i < items.length; i++) {
			Item item = var6[i];
			if (item == null) {
				this.c.getOutStream().writeByte(0);
				this.c.getOutStream().writeWordBigEndianA(0);
			} else {
				if (item.getCount() > 254) {
					this.c.getOutStream().writeByte(255);
					this.c.getOutStream().writeDWord_v2(item.getCount());
				} else {
					this.c.getOutStream().writeByte(item.getCount());
				}

				this.c.getOutStream().writeWordBigEndianA(item.getId());

			}
		}

		this.c.getOutStream().endFrameVarSizeWord();
	}

	public void removeInterfaces() {
		this.closeAllWindows();
	}

	public void sendMessage(String s) {
		c.sendMessage(s);
	}

	public void sendConfig(int id, int value) {
		if (value < 128) {
			sendFrame36(id, value);
		} else {
			sendFrame87(id, value);
		}
	}

	public void sendSidebarInterface(int menuId, int form) {
		c.setSidebarInterface(menuId, form);
	}

	public void sendInterface(int id) {
		showInterface(id);
	}

	public boolean isFilledVial(final int item) {
		switch (item) {
		case 6685:
		case 6687:
		case 6689:
		case 6691:
		case 2436:
		case 145:
		case 147:
		case 149:
		case 2440:
		case 157:
		case 159:
		case 161:
		case 2444:
		case 169:
		case 171:
		case 173:
		case 2432:
		case 133:
		case 135:
		case 137:
		case 113:
		case 115:
		case 117:
		case 119:
		case 2428:
		case 121:
		case 123:
		case 125:
		case 2442:
		case 163:
		case 165:
		case 167:
		case 3024:
		case 3026:
		case 3028:
		case 3030:
		case 12140:
		case 12142:
		case 12144:
		case 12146:
		case 10925:
		case 10927:
		case 10929:
		case 10931:
		case 2434:
		case 139:
		case 141:
		case 143:
		case 2446:
		case 175:
		case 177:
		case 179:
		case 2448:
		case 181:
		case 183:
		case 185:
		case 15312:
		case 15313:
		case 15314:
		case 15315:
		case 15308:
		case 15309:
		case 15310:
		case 15311:
		case 15316:
		case 15317:
		case 15318:
		case 15319:
		case 15324:
		case 15325:
		case 15326:
		case 15327:
		case 15320:
		case 15321:
		case 15322:
		case 15323:
		case 15328:
		case 15329:
		case 15330:
		case 15331:
		case 15300:
		case 15301:
		case 15302:
		case 15303:
		case 15332:
		case 15333:
		case 15334:
		case 15335:
			return true;
		}
		return false;
	}

	public void createPlayersProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time, int h) {
		for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				if (c.heightLevel != h)
					continue;
				Player person = p;
				if (person != null) {
					if (c.heightLevel != h)
						continue;
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							person.getPA().createProjectile(x, y, offX, offY, angle, speed, gfxMoving, startHeight,
									endHeight, lockon, time);
						}
					}
				}
			}
		}
	}

	public void createPlayersProjectile2(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time, int slope, int h) {
		for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				if (c.heightLevel != h)
					continue;
				Player person = p;
				if (person != null) {
					if (c.heightLevel != h)
						continue;
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							person.getPA().createProjectile2(x, y, offX, offY, angle, speed, gfxMoving, startHeight,
									endHeight, lockon, time, slope);
						}
					}
				}
			}
		}
	}

	public void crabSpecBonus(int stat, boolean sup) {
		boostStat1(stat, sup);
	}

	public void scorpSpecBonus(int stat, boolean sup) {
		boostStat(stat, sup);
	}

	public int getBoostStat(int skill, boolean sup) {
		int increaseBy = 0;
		if (sup) {
			increaseBy = (int) (c.getLevelForXP(c.getVariables().playerXP[skill]) * .20);
		} else {
			increaseBy = (int) (c.getLevelForXP(c.getVariables().playerXP[skill]) * .06);
		}
		if (c.getVariables().playerLevel[skill] + increaseBy > c.getLevelForXP(c.getVariables().playerXP[skill])
				+ increaseBy) {
			return c.getLevelForXP(c.getVariables().playerXP[skill]) + increaseBy - c.getVariables().playerLevel[skill];
		}
		return increaseBy;
	}

	public int getBoostStat1(int skill, boolean sup) {
		int increaseBy = 0;
		if (sup) {
			increaseBy = (int) (c.getLevelForXP(c.getVariables().playerXP[skill]) * .10);
		} else {
			increaseBy = (int) (c.getLevelForXP(c.getVariables().playerXP[skill]) * .06);
		}
		if (c.getVariables().playerLevel[skill] + increaseBy > c.getLevelForXP(c.getVariables().playerXP[skill])
				+ increaseBy) {
			return c.getLevelForXP(c.getVariables().playerXP[skill]) + increaseBy - c.getVariables().playerLevel[skill];
		}
		return increaseBy;
	}

	public void boostStat(int skillID, boolean sup) {
		c.getVariables().playerLevel[skillID] += getBoostStat(skillID, sup);
		c.getPA().refreshSkill(skillID);
	}

	public void boostStat1(int skillID, boolean sup) {
		c.getVariables().playerLevel[skillID] += getBoostStat1(skillID, sup);
		c.getPA().refreshSkill(skillID);
	}
}
