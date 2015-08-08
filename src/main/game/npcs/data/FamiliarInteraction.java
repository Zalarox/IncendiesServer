package main.game.npcs.data;

import main.game.players.Player;
import main.util.Misc;

public class FamiliarInteraction {

	protected enum Familiar { // info from 634 npc list/item.cfg
		// NAME(pouchID, levelReq, time(minutes), pointsAmountNeeded, xp,
		// combatLevel, lifePoints, scrollID, npcID, attack, defence,
		// scrollName);
		SPIRIT_WOLF(12047, 1, 6, 1, 0.1, 26, 150, 12425, 6829, 10, 10, "Howl"), DREADFOWL(12043, 4, 4, 1, 0.1, 26, 160,
				12445, 6825, 15, 10, "Dreadfowl Strike"), SPIRIT_SPIDER(12059, 10, 15, 2, 0.2, 25, 180, 12428, 6841, 10,
						15, "Egg Spawn"), THORNY_SNAIL(12019, 13, 16, 2, 0.2, 26, 280, 12459, 6806, 20, 10,
								"Slime Spray"), GRANITE_CRAB(12009, 16, 18, 2, 0.2, 26, 160, 12533, 6796, 15, 25,
										"Stony Shell"), SPIRIT_MOSQUITO(12778, 17, 12, 2, 0.5, 32, 430, 12838, 7331, 25,
												20, "Pester"), DESERT_WYRM(12049, 18, 19, 1, 0.4, 31, 470, 12460, 6831,
														30, 20, "Electric Lash"), SPIRIT_SCORPION(12055, 19, 17, 2, 0.9,
																51, 670, 12432, 6837, 30, 30,
																"Venom Shot"), SPIRIT_TZKIH(12808, 22, 18, 3, 1.1, 36,
																		630, 12839, 7361, 35, 20,
																		"Fireball Assault"), ALBINO_RAT(12067, 23, 22,
																				3, 2.3, 37, 680, 12430, 6847, 30, 30,
																				"Cheese Feast"), SPIRIT_KALPHITE(12063,
																						25, 22, 3, 2.5, 39, 770, 12446,
																						6994, 30, 35,
																						"Sandstorm"), COMPOST_MOUND(
																								12091, 28, 24, 6, 0.6,
																								37, 930, 12440, 6871,
																								45, 45,
																								"Generate Compost"), GIANT_CHINCHOMPA(
																										12800, 29, 31,
																										1, 2.9, 42, 970,
																										12834, 7353, 20,
																										40, "Explode"),

		SPIRIT_TERRORBIRD(12007, 52, 36, 6, 0.8, 62, 2330, 12441, 6794, 75, 200, "Tireless run"),

		PACK_YAK(12093, 96, 58, 10, 4.8, 135, 7100, 12435, 6873, 200, 350, "Winter Storage");

		private int npcID;

		protected final int getNPCID() {
			return npcID;
		}

		protected final String getFamiliarName() {
			return Misc.optimizeText(toString().toLowerCase().replaceAll("_", " "));
		}

		private Familiar(int pouchID, int levelReq, int time, int summonPointsNeeded, double xp, int combatLevel,
				int lifePoints, int scrollID, int npcID, int attackLevel, int defenceLevel, String specialAttackName) {
			this.npcID = npcID;
		}
	}

	public static void interactWithFamiliar(Player p) {
		String[] chat = { "Null" };
		String name = "Null";
		for (Familiar f : Familiar.values()) {
			if (f.getNPCID() == p.getSummoning().summonedFamiliar.npcId + 1)
				name = f.getFamiliarName();
		}
		int l = p.getSummoning().summonedFamiliar.npcId - 1;
		switch (l) {
		case 6830: // Spirit wolf
			chat = new String[] { "Woof!", "Woof, woof?", "Ummm...Wewf?", "Hi...I mean...Woof!" };
			break;

		case 6826: // Dreadfowl
			chat = new String[] { "Bawk!", "Bawk?!", "*Chirp*", "BAWK!!" };
			break;

		case 6842: // Spirit spider
			chat = new String[] { "*Cricri*", "*Crunch*", "Please stop looking at me." };
			break;

		case 6807: // Thorny snail-NOT
			chat = new String[] { "...", "???" };
			break;

		case 6797: // Granite crab-NOT
			chat = new String[] { "*Cruuunch, crunch*", "*Tssk!*", "I bet I can eat you whole." };
			break;

		case 7332: // Spirit mosquito
			chat = new String[] { "*Buzz*", "*Buzzzzzz!*", "I'm Buzzzy!" };
			break;

		case 6832: // Desert wyrm
			chat = new String[] { "*Ssssss*", "*Thhhhhssss*", "What are you *Ssss* looking at?" };
			break;

		case 6838: // Spirit scorpion
			chat = new String[] { "*Tssk, tssk*", "*Ku, ku*" };
			break;

		case 7362: // Spirit tz-kih
			chat = new String[] { "*Ssssss..." };
			break;

		case 6848: // Albino rat
			chat = new String[] { "Got any cheese?", "Your face is very *cheesy*, hahahaha", "*Nibble, nibble*" };
			break;

		case 6995: // Spirit kalphite
			chat = new String[] { "Take me to my queen, now!", "I demand to talk to your leader.",
					"When my queen finds out about this world..." };
			break;

		case 6872: // Compost mound
			chat = new String[] { "*Brrragghh*", "*Hmmf*, *Hmmmmf*", "Gah!" };
			break;

		case 7354: // Giant chinchompa
			chat = new String[] { "*Sniffle, sniffle*", "I have an....*exploding*, personality! Mwuahaha.",
					"KABOOM!...Gotcha!" };
			break;

		case 6795: // Spirit terrorbird
			chat = new String[] { "Macaw!", "Caw, caw!", "Don't you get *tired* of using my ability all the time?" };
			break;

		case 6873: // Pack yak
			chat = new String[] { "Barruw", "Barrooo!", "Barroo" };
			break;
		}
		if (chat.equals("Null") || name.equals("Null") || chat == null || name == null)
			return;
		p.getInstance().nextChat = -1;
		sendFamiliarChat(p, chat, name);
	}

	private static void sendFamiliarChat(Player p, String[] message, String familiarName) {
		p.getDH().sendNpcChat3("", message[(int) (Math.random() * message.length)], "",
				p.getSummoning().summonedFamiliar.npcId, familiarName);
	}
}