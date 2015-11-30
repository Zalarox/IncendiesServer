package incendius.game.players.content.skills.dungeoneering;

import incendius.event.CycleEvent;
import incendius.event.CycleEventContainer;
import incendius.event.CycleEventHandler;
import incendius.game.players.Player;
import incendius.game.players.PlayerHandler;

/**
 * 
 * @author TheLife/Alex
 *
 */
public class Dungeon {

	/**
	 * TODO: Dialogue when is too low level (You need level x dungeoneering to
	 * start this floor) Dialogue when a player in team isn't high enough (A
	 * member in your party isn't high enough level to start) Check if player is
	 * in the area when accepts Check if player leaves the area so he leaves the
	 * party Send a message to players if the leader leaves, and who the new
	 * leader is make player leave on logout/dc stop teleporting in
	 * dungeoneering, if in a party/ingame Add the following availability:
	 * Melee, magic, ranged, fishing, summoning, herblore, mining, smithing,
	 * prayer, runecrafting, thieving, crafting, fletching, cooking, firemaking,
	 * woodcutting Check how many npcs on the floor, and make them, are you sure
	 * you want to finish? & You still have x npcs left Reset stand stance on
	 * leaving Add level req for armour & weps Fix projectile clipping for npcs!
	 * Make food work Make potions & make em work ******* If you have a partner
	 * in dung and you where the leader, then you leave ****** and start a new
	 * one, you go 2 the same room as him.
	 */

	/**
	 * These following skills are fully finished: - Firemaking
	 */

	public static void setSettings(Player p, char difficulty, int complexity, int level, int totalNpcs) {
		if (p == null)
			return;
		if (p.party == null)
			return;
		p.party.complexity = complexity;
		p.party.difficulty = difficulty;
		p.party.level = level;
		p.party.totalNpcs = totalNpcs;
	}

	public static boolean check(Player p, boolean party) {
		if (p == null)
			return false;
		if (!party && p.party == null)
			return false;
		if (party && p.party != null)
			return false;
		if (!party && p.party.members == null)
			return false;
		if (!party && p.party.floor != null)
			return false;
		return true;
	}

	public static void newParty(Player p) {
		if (!check(p, true)) {
			return;
		}
		p.party = new Party(new Member(p));
		Interface.clearParty(false, p);
		p.setSidebarInterface(14, 26224);
		p.sendMessage("Your party has been formed.");
		p.updateWalkEntities();
	}

	public static void start(Player p) {
		if (!check(p, false)) {
			return;
		}
		for (int i = 0; i < p.party.members.length; i++) {
			if (p.party.members[i] != null && !hasZeroItems(p.party.members[i].p)) {
				p.sendMessage("You, or one of your teammates still has an item in his/her inventory or equipped.");
				return;
			}
		}
		// setSettings(p, 'e', 1, calculateFloorRequirement(p), 1);
		new Floor(p.party.members, p.party.level, p.party.complexity, p.party.difficulty);
	}

	public void handleKilling(Player p) {
		p.party.floor.kills++;
	}

	public void handleDeath(Player p) {
		p.party.floor.deaths++;
	}

	public static int calculateFloorRequirement(Player p) {
		int required = Constants.LOWEST_LEVEL;
		required = (required * p.party.level * (int) Math.PI) / (int) 2.4;
		return required;
	}

	public static void declineInvite(Player p) {
		if (p.partner == null) {
			p.getPA().closeAllWindows();
			return;
		}
		p.partner.sendMessage(p.playerName + " has declined your invite.");
		p.getPA().closeAllWindows();
	}

	public static void acceptInvite(Player p) {
		Party.newMember(p);
		p.getPA().closeAllWindows();
	}

	public static void openShop(Player p) {
		if (p.party == null)
			return;
		if (p.party.floor == null)
			return;
		p.getDH().sendOption3("Open shop one", "Open shop two", "Open shop three");
		p.getInstance().dialogueAction = 15;
	}

	public static void inviteMember(final int id, final Player p) {
		final Player o = PlayerHandler.players[id];
		if (o == null)
			return;
		if (o.partner != null)
			return;
		if (p.party == null) {
			p.sendMessage("You aren't in a party.");
			return;
		}
		if (p.party.floor != null)
			return;
		p.turnPlayerTo(o.absX, o.absY);
		if (p.party.members[0].p != p) {
			p.sendMessage("Only party leaders may invite other members.");
			return;
		}
		if (p.party.members[4] != null) {
			p.sendMessage("You cannot have more than five party members.");
			return;
		}
		if (o.party != null) {
			p.sendMessage("That player is already in a party.");
			return;
		}
		CycleEventHandler.getInstance().addEvent(p, new CycleEvent() {
			int timer = 0;

			@Override
			public void execute(CycleEventContainer container) {
				if (timer >= 25)
					container.stop();
				if (!p.headingTowardsPlayer)
					container.stop();
				if (p != null && o != null && p.goodDistance(o.getX(), o.getY(), p.getX(), p.getY(), 1)
						|| p.copyOfOpponent.goodDistance(p.getX(), p.getY(), o.getX(), o.getY(), 1)) {
					if (p.isBusy(o)) {
						p.sendMessage("Other player is busy at the moment.");
						container.stop();
						return;
					}
					p.sendMessage("Sending party invite...");
					o.partner = p;
					Interface.loadInviteInterface(p, o);
					container.stop();
				}
				timer++;
			}

			@Override
			public void stop() {
				if (p != null)
					p.headingTowardsPlayer = false;
			}
		}, 1);
	}

	public static boolean hasZeroItems(Player p) {
		for (int i = 0; i < p.playerEquipment.length; i++) {
			if (p.playerEquipment[i] != -1 && p.playerEquipment[i] != 15707)
				return false;
		}
		for (int i = 0; i < p.playerItems.length; i++) {
			if (p.playerItems[i] != 0 && p.playerItems[i] != 15708)
				return false;
		}
		return true;
	}

	public static void handleButtons(int button, Player p) {
		switch (button) {
		// Form party
		case 106093:
			newParty(p);
			break;
		// Leave Party
		case 102117:
			if (p.party != null)
				p.party.leave(p);
			break;
		// Reset Prestige
		case 105252:
		case 102120:
			// TODO Reseting prestige
			break;
		// Complexity Interfaces
		case 102114:
		case 106190:
			p.setSidebarInterface(14, 638);
			break;
		case 138013:
		case 138113:
		case 138213:
		case 139057:
		case 139157:
			p.getPA().showInterface(35233);
			break;
		case 137170:
		case 138114:
		case 138214:
		case 139058:
		case 139158:
			p.getPA().showInterface(35333);
			break;
		case 137171:
		case 138015:
		case 138215:
		case 139059:
		case 139159:
			p.getPA().showInterface(35433);
			break;
		case 137172:
		case 138016:
		case 138116:
		case 139060:
		case 139160:
			p.getPA().showInterface(35533);
			break;
		case 137173:
		case 138017:
		case 138117:
		case 138217:
		case 139161:
			p.getPA().showInterface(35633);
			break;
		case 137174:
		case 138018:
		case 138118:
		case 138218:
		case 139062:
			p.getPA().showInterface(35733);
			break;
		// Confirm Complexity Button
		case 137166:
			break;
		// Change Complexity Button
		case 102135:
			if (p.party == null)
				return;
			p.getPA().showInterface(35233 + ((p.party.complexity - (p.party.complexity == 0 ? 0 : 1)) * 100));
			break;
		// Invite interface
		case 157037:
			Dungeon.acceptInvite(p);
			break;
		case 157040:
			p.getPA().removeAllWindows();
			break;
		}
	}

}
