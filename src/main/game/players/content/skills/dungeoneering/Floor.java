package main.game.players.content.skills.dungeoneering;

import java.util.ArrayList;

import main.game.npcs.NPCHandler;
import main.util.Misc;

/**
 * 
 * @author Thelife/Alex
 *
 */
public class Floor {

	public Member[] members;
	public boolean[] lockedPortal = new boolean[Portal.objectCoords.length],
			npcViaRoomSpawned = new boolean[Constants.ROOM_SIZE.length], crateHasItems;
	public int keyToPortal[] = new int[Portal.objectCoords.length];
	public int level, complexity, x, y, heightLevel, tokens, deaths, damage, kills, roomId;
	public char difficulty;
	public AccessedPortal[] accessedPortals = new AccessedPortal[Portal.objectCoords.length];
	public ArrayList<AccessedPortal> floorAccessability = new ArrayList<AccessedPortal>();
	public Crate[] crates = new Crate[Constants.MAX_CRATES];

	public Floor(Member[] members, int level, int complexity, char difficulty) {
		this.members = members;
		this.level = level;
		this.complexity = complexity;
		this.difficulty = difficulty;
		startup();
	}

	private void startup() {
		this.heightLevel = getAnOpenHeightLevel();
		members[0].p.sendMessage("" + this.heightLevel);
		this.roomId = Misc.random(Constants.START_LOCATIONS.length - 1);
		this.crateHasItems = new boolean[Constants.CRATE_ROOM[this.roomId].length];
		int distance = difficulty == 'e' ? 20 : (difficulty == 'm' ? 40 : (difficulty == 'h' ? 90 : 250));
		getFloorItems();
		floorAccessability.clear();
		for (int i = 0; i < Portal.objectCoords.length; i++) {
			if (members[0].p.goodDistance(Portal.objectCoords[i][0], Portal.objectCoords[i][1],
					Constants.START_LOCATIONS[this.roomId][0], Constants.START_LOCATIONS[this.roomId][1], distance)) {
				accessedPortals[i] = new AccessedPortal(Portal.teleportCoords[i][0], Portal.teleportCoords[i][1]);
				floorAccessability.add(accessedPortals[i]);
			}
		}
		members[0].p.getInstance().party.setFloor(this);
		for (int i = 0; i < Constants.MAIN_NPC_LOCATIONS[this.roomId].length; i++) {
			new Npc(Constants.MAIN_NPC_LOCATIONS[this.roomId][i][1], Constants.MAIN_NPC_LOCATIONS[this.roomId][i][2],
					this.heightLevel, 0, 0, 0, 0, Constants.MAIN_NPC_LOCATIONS[this.roomId][i][0]).spawn();
		}
		for (int i = 0; i < this.members.length; i++) {
			if (members[i] != null) {
				members[i].p.getPA().movePlayer(Constants.START_LOCATIONS[this.roomId][0] + Misc.random(1),
						Constants.START_LOCATIONS[this.roomId][1] + Misc.random(1), this.heightLevel);
				members[i].p.sendMessage("");
				members[i].p.sendMessage("- Welcome to the Chaos tunnels -");
				members[i].p.sendMessage("Floor @blu@1 @bla@Complexity @blu@1");
				members[i].p.sendMessage(
						"Dungeon Size: @blu@Small");/**
													 * TODO calculate small or
													 * big really big etc
													 **/
				members[i].p.sendMessage("- Most credits go to Alex/Thelife -");
				members[i].p.sendMessage("");
			}
		}
	}

	public void getFloorItems() {
		if (level <= 5) {
			for (int i = 0; i < this.members.length; i++) {
				if (members[i] != null) {
					for (int i2 = 0; i2 < 6; i2++) {
						if (members[i].p.boundItems[i2][0] > 0) {
							members[i].p.getItems().addItem(members[i].p.boundItems[i2][0],
									members[i].p.boundItems[i2][1]);
						}
					}
					/** TODO Add runes here & food & mage gear & range **/
					Items.addItem(members[i].p, Constants.ITEM_RAPIER, Constants.ITEM_NOVITE, 1);
					Items.addItem(members[i].p, Constants.ITEM_2H_SWORD, Constants.ITEM_NOVITE, 1);
					Items.addItem(members[i].p, Constants.ITEM_KITESHIELD, Constants.ITEM_NOVITE, 1);
					Items.addItem(members[i].p, Constants.ITEM_FULL_HELM, Constants.ITEM_NOVITE, 1);
					Items.addItem(members[i].p, Constants.ITEM_PLATEBODY, Constants.ITEM_NOVITE, 1);
					Items.addItem(members[i].p, Constants.ITEM_PLATELEGS, Constants.ITEM_NOVITE, 1);
					Items.addItem(members[i].p, Constants.ITEM_BOOTS, Constants.ITEM_NOVITE, 1);
					Items.addItem(members[i].p, Constants.ITEM_GAUNLETS, Constants.ITEM_NOVITE, 1);
					Items.addItem(members[i].p, Constants.ITEM_ARROWS, Constants.ITEM_NOVITE, 100);
					for (int i3 = 0; i3 < 4; i3++) {
						members[i].p.getItems().addItem(Constants.FOOD[0], 1);
					}
					members[i].p.getItems().addItem(Constants.CURRENCY, 7501 + Misc.random(2499));
				}
			}
			return;
		}
		int memberAmount = 0;
		for (int i = 0; i < this.members.length; i++) {
			if (members[i] != null) {
				memberAmount++;
			}
		}
		for (int i = 0; i < this.members.length; i++) {
			if (members[i] != null) {
				for (int i2 = 0; i2 < 6; i2++) {
					if (members[i].p.boundItems[i2][0] > 0) {
						members[i].p.getItems().addItem(members[i].p.boundItems[i2][0], members[i].p.boundItems[i2][1]);
					}
				}
			}
		}
		int[] requirements = { 1, 10, 20, 30, 40, 50, 60, 70, 80, 90, 99 };
		int requirement = 0;
		for (int i = 0; i < requirements.length; i++) {
			if (members[0].p.playerLevel[Constants.SKILL_ID] < requirements[i]) {
				requirement = i - 1;
				break;
			}
		}
		boolean crateTaken[] = new boolean[Constants.CRATE_ROOM[this.roomId].length];
		int crateIds[] = new int[Constants.MAX_CRATES];
		for (int i = 0; i < crateIds.length; i++) {
			int random = Misc.random(Constants.CRATE_ROOM[this.roomId].length - 1);
			if (crateTaken[random] && i > 0) {
				i--;
			} else {
				crateTaken[random] = true;
				int i2 = getCrateAmount(i, memberAmount);
				crates[i] = new Crate(getCrateItem(i, requirement), i2, Constants.CRATE_ROOM[this.roomId][random][0],
						Constants.CRATE_ROOM[this.roomId][random][1], i2 >= 50 ? true : false);
			}

		}
	}

	public int getAnOpenHeightLevel() {
		for (int i = 1; i <= 2000; i++) {
			if (!Constants.openHeightLevels.contains(i)) {
				Constants.openHeightLevels.add(i);
				return i * 4;
			}
		}
		return (Misc.random(80000) + 2000) * 4;
	}

	public int getCrateItem(int i, int tier) {
		switch (i) {
		case 0:
			return Constants.FOOD[Misc.random(Constants.FOOD.length - 1)];
		case 1:
			return Items.getItem(Misc.random(Constants.ITEM_2H_SWORD), Misc.random(tier));
		case 2:
			return Constants.FOOD[Misc.random(Constants.FOOD.length - 1)];
		case 3:
			return Items.getItem(Misc.random(4) + 8, Misc.random(tier));
		case 4:
			return Constants.FOOD[Misc.random(Constants.FOOD.length - 1)];
		case 5:
			return Items.getItem(Constants.ITEM_ARROWS, Misc.random(tier));
		case 6: // BOWS HERE & STAFFS
		case 7:
			return 1;// TODO
		default:
			return Constants.FOOD[Misc.random(Constants.FOOD.length - 1)];
		}
	}

	public int getCrateAmount(int i, int memberAmount) {
		switch (i) {
		case 0:
			return (Misc.random(2) + 1) * memberAmount;
		case 1:
			return memberAmount;
		case 2:
			return (Misc.random(2) + 1) * memberAmount;
		case 3:
			return memberAmount;
		case 4:
			return (Misc.random(2) + 1) * memberAmount;
		case 5:
			return Constants.STACKABLE_AMOUNT * memberAmount;
		case 6: // BOWS HERE & STAFFS
		case 7:
			return memberAmount;// TODO
		default:
			return (Misc.random(2) + 1) * memberAmount;
		}
	}

	public void clearFloor() {
		for (int i = 0; i < NPCHandler.npcs.length; i++) {
			if (NPCHandler.npcs[i] != null && getHeight() == NPCHandler.npcs[i].heightLevel
					&& NPCHandler.npcs[i].type == 1) {
				NPCHandler.npcs[i] = null;
			}
		}
		/*
		 * for(GroundItem i : ItemHandler.items) { if(i.getItemX() >= 3100 &&
		 * i.getItemX() <= 3300 && i.getItemY() >= 5300 && i.getItemY() <= 5600
		 * && i.heightLevel == heightLevel) { i.remove = true; } }
		 */
	}

	public int getLevel() {
		return level;
	}

	public int getComplexity() {
		return complexity;
	}

	public char getDifficulty() {
		return difficulty;
	}

	public int getStartX() {
		return x;
	}

	public int getStartY() {
		return y;
	}

	public int getHeight() {
		return heightLevel;
	}

	public int getDeaths() {
		return deaths;
	}

	public int getKills() {
		return kills;
	}

	public int getDamage() {
		return damage;
	}

	public int getTokens() {
		return tokens/* Do a formula for recieved tokens in the end here */;
	}

	public void addDamage(int damage) {
		this.damage += damage;
	}

}
