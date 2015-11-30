package incendius.game.players.actions;

import incendius.game.npcs.NPCHandler;
import incendius.game.players.Player;
import incendius.util.Misc;

public class DialogueHandler {

	private Player c;

	public DialogueHandler(Player player) {
		this.c = player;
	}

	/**
	 * Handles all talking
	 * 
	 * @param dialogue
	 *            The dialogue you want to use
	 * @param npcId
	 *            The npc id that the chat will focus on during the chat
	 */
	public void sendDialogues(int dialogue, int npcId) {
		c.getInstance().talkingNpc = npcId;
		switch (dialogue) {
		case 0:
			c.getInstance().talkingNpc = -1;
			c.getPA().removeAllWindows();
			c.getInstance().nextChat = 0;
			break;
		case 254:
			sendNpcChat4("Hello there " + c.getDisplayName() + ".", "I am Sigmund the stall merchant.",
					"I can buy any items from the", "stalls next to me for a set price.", c.getInstance().talkingNpc,
					"Sigmund The Merchent");
			c.getInstance().nextChat = 255;
			break;
		case 321:
			this.sendOption2("How many kills do I have remaining?", "Nevermind");
			c.getInstance().dialogueAction = 121;
			break;
		case 255:
			sendPlayerChat1("I'll consider it.", 9850);
			c.getInstance().nextChat = 0;
			break;
		case 170:
			sendNpcChat4("Hello " + c.getDisplayName() + "!", "Get 99 in any skill and",
					"I will allow you to buy my skillcapes.", "Trade me to buy them.",
					c.getInstance().talkingNpc, "Wise Old Man");
			c.getInstance().nextChat = 171;
			break;
		case 171:
			sendPlayerChat1("Got it, thanks.", 9850);
			c.getInstance().nextChat = 0;
			break;
		case 180:
			sendNpcChat4("Hello traveller.", "I am the Pest Control shopkeeper.",
					"I sell Void Knight items for points which you",
					"can obtain by playing Pest control games.", c.getInstance().talkingNpc, "Void Knight");
			c.getInstance().nextChat = 181;
			break;
		case 181:
			sendPlayerChat1("Alright, I'll play some if I want to buy that.", 9850);
			c.getInstance().nextChat = 0;
			break;
		case 198:
			sendNpcChat3("Halt, " + c.getDisplayName() + "!", "I am THOK, the dungeoneering master! Come with me to ",
					"the dungeoneering lobby!", c.getInstance().talkingNpc,
					"Thok, Master of Dungeoneering");
			c.getInstance().nextChat = 199;
			break;
		case 199:
			sendOption2("Yes, sir!", "I'll... just go now.");
			c.getInstance().dialogueId = 3622;
			break;
		case 208:
			sendNpcChat1("THOK WISHES YOU LUCK!", c.getInstance().talkingNpc,
					"Thok, Master of Dungeoneering");
			break;
		case 204:
			sendNpcChat2("THOK WELCOMES YOU.", "What do you wish for?", c.getInstance().talkingNpc,
					"Thok, Master of Dungeoneering");
			c.getInstance().nextChat = 209;
			break;
		case 209:
			sendOption4("Trade...", "Start Floor 1(small/medium)", "Start Floor 2(medium/large)", "Cancel");
			c.getInstance().dialogueAction = 3683;
			break;
		case 150:
			sendNpcChat4("Ssssup " + c.getDisplayName() + "...", "I am the mage of Zamorak and I can teleport",
					"you to a number of Runecrafting altars.", "Which one will you go to today?",
					c.getInstance().talkingNpc, "Mage of Zamorak");
			c.getInstance().nextChat = 151;
			break;
		case 151:
			sendOption5("Air Altar", "Mind Altar", "Water Altar", "Earth Altar", "Something else...");
			c.getInstance().dialogueAction = 10;
			c.getInstance().dialogueId = 151;
			c.getInstance().teleAction = -1;
			break;
		case 152:
			sendOption5("Fire Altar", "Body Altar", "Cosmic Altar", "Astral Altar", "Something else...");
			c.getInstance().dialogueAction = 11;
			c.getInstance().dialogueId = 152;
			c.getInstance().teleAction = -1;
			break;
		case 153:
			sendOption5("Nature Altar", "Law Altar", "Death Altar", "Blood Altar", "I think I'll stay here.");
			c.getInstance().dialogueAction = 12;
			c.getInstance().dialogueId = 153;
			c.getInstance().teleAction = -1;
			break;
		case 250:
			sendNpcChat4("Greetings " + c.getDisplayName() + "!", "I am the Incendius Guide and I offer advice for",
					"Incendius beginners. Check out the forums for more!",
					"So... would you like to see the information I offer?", c.getInstance().talkingNpc,
					"Incendius Guide");
			c.getInstance().nextChat = 251;
			break;
		case 251:
			sendPlayerChat1("Sure, I guess.", 9850);
			c.getInstance().nextChat = 252;
			break;

		case 340:
			break;
		case 111:
			sendNpcChat4("Hello adventurer!", "My name is Kuradal and I am a master of the slayer!",
					"I can assign you a slayer task suitable to your combat level.", "Would you like one?",
					c.getInstance().talkingNpc, "Kuradal");
			c.getInstance().nextChat = 112;
			break;
		case 112:
			sendOption2("Yeah, give me some stuff to kill.",
					"No slayer for me, thanks.");
			c.getInstance().dialogueAction = 5;
			break;
		case 113:
			sendNpcChat2("Hmm... It seems I have already assigned you a task to complete.",
					"Would you like me to make it easier on you?", c.getInstance().talkingNpc, "Kuradal");
			c.getInstance().nextChat = 114;
			break;
		case 114:
			sendOption2("Yeah, something easier would be nice.", "No I can deal with this.");
			c.getInstance().dialogueAction = 6;
			break;
		case 320: // Mage of zamorak
			sendNpcChat2("Dude like, wasssssup with you!", "People can kill you here and stuff...",
					c.getInstance().talkingNpc, "Mage of Zamorak");
			c.getInstance().nextChat = 0;
			break;
		case 4:
			sendPlayerChat1("Do you have any items for sale?", 9850);
			c.getInstance().nextChat = 5;
			break;
		case 5:
			sendNpcChat1("Sure, what are you interested in buying?", c.getInstance().talkingNpc,
					"Nastroth");
			c.getInstance().nextChat = 6;
			break;
		case 6:
			sendOption4("Show me Melee Gear", "Show me Magic Gear", "Show me Ranged Gear", "Some Supplies, perhaps");
			c.getInstance().dialogueAction = 2;
			c.getInstance().nextChat = 0;
			break;
		case 7:
			sendNpcChat1("Hi " + c.getDisplayName() + ", what can I do for you today?", c.getInstance().talkingNpc,
					"Strange Old Man");
			c.getInstance().nextChat = 8;
			break;
		case 8:
			sendOption4("Could you reset my Barrows kills?", "I need my Barrows items fixed.",
					"Where do I find myself a spade?", "Nothing, I'm good.");
			c.getInstance().nextChat = 0;
			c.getInstance().dialogueAction = 50;
			break;
		case 9:
			sendNpcChat1("You can buy a spade from the General Store at home.", c.getInstance().talkingNpc,
					"Strange Old Man");
			c.getInstance().nextChat = 10;
			break;
		case 10:
			sendPlayerChat1("Okay great, thanks a lot!", 9850);
			c.nextChat = 0;
			break;
		case 13:
			sendNpcChat1("Sure thing, hand the items over.", c.getInstance().talkingNpc, "Strange Old Man");
			c.getInstance().nextChat = 14;
			break;
		case 14:
			c.getPA().fixAllBarrows();
			sendNpcChat1("Here you go, " + c.getDisplayName() + ", all fixed!", c.getInstance().talkingNpc,
					"Strange Old Man");
			c.getInstance().nextChat = 0;
			break;
		case 50:
			sendPlayerChat1("Bob, do you know where any Magic trees are located?", 9850);
			c.getInstance().nextChat = 51;
			break;
		case 51:
			sendNpcChat3("Yes, they are located at the Gnome stronghold, west from here.",
					"You will find rare Magic trees there. Would you like me to",
					"transport you to the Gnome Woodcutting area? ", c.getInstance().talkingNpc, "Bob");
			c.getInstance().nextChat = 52;
			break;
		case 52:
			sendOption2("Yeah, teleport me.", "Not now, thanks.");
			c.getInstance().dialogueAction = 60;
			c.getInstance().nextChat = 0;
			break;
		case 155:
			sendOption2("Yeah, I'm fearless!", "No way, that looks scary.");
			c.getInstance().dialogueAction = 982;
			break;
		case 30:
			sendNpcChat3("Hello " + c.getDisplayName() + ".", "If you are interested in farming",
					"then you can trade me to buy farming supplies.", c.getInstance().talkingNpc, "Vanessa");
			c.getInstance().nextChat = 31;
			break;
		case 31:
			sendPlayerChat1("Sure thing, Venessa.", 9850);
			c.getInstance().nextChat = 0;
			break;
		case 200:
			sendPlayerChat1("Nurmof, could you give me a guide on Falador Mine?", 9850);
			c.getInstance().nextChat = 201;
			break;
		case 201:
			sendNpcChat3("Yes of course I can. You can buy any Pickaxe",
					"you need from me, for a certain price. All the ore you can mine",
					"can be found around this area, and in the Guild located South. ", c.getInstance().talkingNpc,
					"Bob");
			c.getInstance().nextChat = 203;
			break;
		case 203:
			sendPlayerChat1("Okay brilliant, thanks a lot Nurmof!", 9850);
			c.getInstance().nextChat = 0;
			break;
		case 205:
			sendPlayerChat1("Hello there, what's through that gate?", 9850);
			c.getInstance().nextChat = 206;
			break;
		case 206:
			sendNpcChat3("Well, through this gate is the Mining Guild.",
					"Any player with a Mining level of 85+ can enter through the",
					"gate to mine the best and more valuable ore on Incendius.", c.getInstance().talkingNpc,
					"Mining Instructor");
			c.getInstance().nextChat = 207;
			break;
		case 207:
			sendPlayerChat1("Ah I see, thanks a lot!", 9850);
			c.getInstance().nextChat = 0;
			break;
		case 300:
			sendPlayerChat1("Hello there Zambo, do you have any items for sale?", 9850);
			c.getInstance().nextChat = 311;
			break;
		case 311:
			sendNpcChat4("Of course! I sell a wide range of rare and",
					"powerful items in exchange for Donator Points. You can get",
					"Donator Points by simply typing ::Donate and by following",
					"the instructions on that page or by talking to the staff.", c.getInstance().talkingNpc, "Zambo");
			c.getInstance().nextChat = 312;
			break;

		case 312:
			sendPlayerChat1("Okay thanks!", 9850);
			c.getInstance().nextChat = 313;
			break;
		case 313:
			sendNpcChat1("Don't forget to vote for free benefits!", c.getInstance().talkingNpc, "Zambo");
			c.getInstance().nextChat = 0;
			break;

		case 215:
			sendPlayerChat1("Hello there Grum, what wares do you have for sale?", 9850);
			c.getInstance().nextChat = 216;
			break;
		case 216:
			sendNpcChat3("I'll sell you a variety of unique items",
					"in exchange for Voting Points. You can obtain them",
					"by voting once per 24 hours!", c.getInstance().talkingNpc, "Grum");
			c.getInstance().nextChat = 217;
			break;

		case 217:
			sendPlayerChat1("Okay great, I will make sure I use ::vote every 24 hours!", 9850);
			c.getInstance().nextChat = 0;
			break;

		case 225:
			sendNpcChat3("Hello " + c.getDisplayName() + ", I am the Make-Over mage. I have",
					"a magical ability which allows me to change the appearance",
					"of players, this is free of charge and costs nothing!", c.getInstance().talkingNpc, "Grum");
			c.getInstance().nextChat = 226;
			break;

		case 226:
			sendPlayerChat1("Okay great, I will make sure I use ::vote every 24 hours!", 9850);
			c.getInstance().nextChat = 0;
			break;

		case 17:
			sendPlayerChat1("Do you have any items for sale?", 9850);
			c.getInstance().nextChat = 18;
			break;
		case 18:
			sendNpcChat3("Yes, I do. I sell a large variety of powerful items in exchange",
					"for PK points. Feel free to browse through my wares",
					"until you find what you're looking for.", c.getInstance().talkingNpc, "Mandrith");
			c.getInstance().nextChat = 19;
			break;
		case 210:
			sendNpcChat3("Hello there " + c.getDisplayName() + ", you look like the kind of person who",
					"would be into the Fishing skill. I sell the finest fishing hardware",
					"in the whole of Incendius, trade me to view my wares.", c.getInstance().talkingNpc, "Harry");
			c.getInstance().nextChat = 211;
			break;
		case 211:
			sendPlayerChat1("Okay thanks for the information Harry.", 9850);
			c.getInstance().nextChat = 0;
			break;
		case 19:
			sendOption4("View PK Points shop", "My current PK Points",
					"Exchange PvP Artifacts for PK Points", "Cancel");
			c.getInstance().dialogueAction = 0;
			break;
		case 20:
			sendOption4("PK Locations", "Minigames", "Monsters", "Cancel");
			c.getInstance().dialogueAction = 55;
			break;
		case 21:
			sendOption4("27 Ports", "East Dragons", "Graveyard (26 Wild)", "Mage Bank");
			c.getInstance().dialogueAction = 1;
			c.getInstance().nextChat = 0;
			break;
		case 22:
			sendOption4("Minigames", "Monsters", "Slayer", "Skilling");
			c.getInstance().dialogueAction = 4;
			c.getInstance().nextChat = 0;
			break;
		case 24:
			sendStatement("You currently have " + c.getInstance().pkp + " PK points.");
			c.getInstance().dialogueAction = 0;
			c.getInstance().nextChat = 0;
			break;
		case 26:
			c.getDH().sendOption4("Normal Magic", "Ancient Magicks", "Lunar Magic", "Go back");
			c.getInstance().nextChat = 0;
			c.getInstance().dialogueAction = 6;
			break;
		case 25:
			sendNpcChat1("You have no statuettes, please come back when you do.", c.getInstance().talkingNpc,
					"Mandrith");
			c.getInstance().nextChat = 0;
			if (c.getItems().playerHasItem(14876, 1)) {
				sendNpcChat1("Hmm.. Ancient Statuette? I'll buy that for 15 Pk Points!", c.getInstance().talkingNpc,
						"Mandrith");
				c.getInstance().nextChat = 81;
			} else if (c.getItems().playerHasItem(14877, 1)) {
				sendNpcChat1("Hmm.. Seren Statuette? I'll buy that for 10 Pk Points!", c.getInstance().talkingNpc,
						"Mandrith");
				c.getInstance().nextChat = 81;
			} else if (c.getItems().playerHasItem(14878, 1)) {
				sendNpcChat1("Hmm.. Armadyl Statuette? I'll buy that for 7 Pk Points!", c.getInstance().talkingNpc,
						"Mandrith");
				c.getInstance().nextChat = 81;// demise
			} else if (c.getItems().playerHasItem(14879, 1)) {
				sendNpcChat1("Hmm.. Zamorak Statuette? I'll buy that for 5 Pk Points!", c.getInstance().talkingNpc,
						"Mandrith");
				c.getInstance().nextChat = 81;
			} else if (c.getItems().playerHasItem(14880, 1)) {
				sendNpcChat1("Hmm.. Saradomin Statuette? I'll buy that for 4 Pk Points!", c.getInstance().talkingNpc,
						"Mandrith");
				c.getInstance().nextChat = 81;
			} else if (c.getItems().playerHasItem(14881, 1)) {
				sendNpcChat1("Hmm.. Bandos Statuette? I'll buy that for 3 Pk Points", c.getInstance().talkingNpc,
						"Mandrith");
				c.getInstance().nextChat = 81;
			}
			break;
		case 81:
			c.sendMessage("You sold your PvP artifact to Mandrith in return for Pk Points.");
			if (c.getItems().playerHasItem(14876, 1)) {
				c.getItems().deleteItem(14876, 1);
				c.getInstance().pkp += 15;
			} else if (c.getItems().playerHasItem(14877, 1)) {
				c.getItems().deleteItem(14877, 1);
				c.getInstance().pkp += 10;
			} else if (c.getItems().playerHasItem(14878, 1)) {
				c.getItems().deleteItem(14878, 1);
				c.getInstance().pkp += 7;
			} else if (c.getItems().playerHasItem(14879, 1)) {
				c.getItems().deleteItem(14879, 1);
				c.getInstance().pkp += 5;
			} else if (c.getItems().playerHasItem(14880, 1)) {
				c.getItems().deleteItem(14880, 1);
				c.getInstance().pkp += 5;
			} else if (c.getItems().playerHasItem(14881, 1)) {
				c.getItems().deleteItem(14881, 1);
				c.getInstance().pkp += 3;
			}
			c.getPA().closeAllWindows();
			break;
		case 77:
			sendNpcChat4("" + c.getDisplayName() + " you have failed.", "You didn't participate enough to take down",
					"the portals. ", "Try harder next time.", c.getInstance().talkingNpc, "Void Knight");
			break;
		case 78:
			sendNpcChat4("All is lost!", "You could not take down the portals in time.", " ", "Try harder next time.",
					c.getInstance().talkingNpc, "Void Knight");
			break;
		case 79:
			sendNpcChat4("Congratulations " + c.getDisplayName() + "!", "You took down all the portals whilst keeping",
					"the void knight alive.", "You been awarded, well done.", c.getInstance().talkingNpc,
					"Void Knight");
			break;

		case 401:
			sendNpcChat4("Hello there " + c.getDisplayName() + "!", " I can reset any combat skill for you,",
					"but remember, this is irreversable!", "Choose wisely", c.getInstance().talkingNpc, "Town Crier");
			c.getInstance().nextChat = 402;
			break;
		/**
		 * 
		 * Start of reset skills, lets add a security type thing of 4 numbers
		 * and they shall type that in evertime they want to reset. NOTICE, THE
		 * BUTTON CLICKING HAS NOT BE IMPLENTED NOR THE NPC TO DO THIS PLEASE
		 * ALSO CHANGE THE NAME OF THE NPC - GL
		 */
		case 402:
			sendOption5("Reset Constitution", "Reset Attack", "Reset Defence", "Reset Strength", "Next");
			c.getInstance().dialogueAction = 42;
			break;
		case 403:
			sendOption4("Reset Prayer", "Reset Magic", "Reset Ranged", "Back");
			c.getInstance().dialogueAction = 43;
			break;
		case 404:
			sendNpcChat2("Congratulations!", "Your " + c.getInstance().skillReseted + " has been completely reset!",
					c.getInstance().talkingNpc, "NPC NAME");
			c.getInstance().nextChat = 0;
		}
	}

	public static void sendNpcChat(Player c, String s, String s2, int ChatNpc, String name) {
		c.getPA().sendFrame200(4888, 9847);
		c.getPA().sendString(name, 4889);
		c.getPA().sendString(s, 4890);
		c.getPA().sendString(s2, 4891);
		c.getPA().sendFrame75(ChatNpc, 4888);
		c.getPA().sendChatInterface(4887);
	}

	public void talk(int face, String line1, String line2, String line3, String line4, int npcID) {
		c.getPA().sendFrame200(4901, face);
		c.getPA().sendString(NPCHandler.getNpcListName(npcID).replaceAll("_", " "), 4902);
		c.getPA().sendString("" + line1, 4903);
		c.getPA().sendString("" + line2, 4904);
		c.getPA().sendString("" + line3, 4905);
		c.getPA().sendString("" + line4, 4906);
		c.getPA().sendString("Click here to continue", 4907);
		c.getPA().sendFrame75(npcID, 4901);
		c.getPA().sendChatInterface(4900);
	}
	/*
	 * Information Box
	 */

	public void sendStartInfo(String text, String text1, String text2, String text3, String title) {
		c.getPA().sendString(title, 6180);
		c.getPA().sendString(text, 6181);
		c.getPA().sendString(text1, 6182);
		c.getPA().sendString(text2, 6183);
		c.getPA().sendString(text3, 6184);
		c.getPA().sendChatInterface(6179);
	}

	public static void sendStatement(Player c, String s) { // 1 line click here
		// to continue chat
		// box interface
		c.getPA().sendString(s, 357);
		c.getPA().sendString("Click here to continue", 358);
		c.getPA().sendChatInterface(356);
	}

	public static void sendNpcChat(Player c, String s, int ChatNpc, String name) {
		c.getPA().sendFrame200(4883, 9847);
		c.getPA().sendString(name, 4884);
		c.getPA().sendString(s, 4885);
		c.getPA().sendFrame75(ChatNpc, 4883);
		c.getPA().sendChatInterface(4882);
	}

	/*
	 * Options
	 */

	public void sendOption2(String s, String s1) {
		c.getPA().sendString("Select an Option", 2460);
		c.getPA().sendString(s, 2461);
		c.getPA().sendString(s1, 2462);
		c.getPA().sendChatInterface(2459);
	}

	public void sendOption3(String s, String s1, String s2) {
		c.getPA().sendString("Select an Option", 2470);
		c.getPA().sendString(s, 2471);
		c.getPA().sendString(s1, 2472);
		c.getPA().sendString(s2, 2473);
		c.getPA().sendChatInterface(2469);
	}

	public void sendOption4(String s, String s1, String s2, String s3) {
		c.getPA().sendString("Select an Option", 2481);
		c.getPA().sendString(s, 2482);
		c.getPA().sendString(s1, 2483);
		c.getPA().sendString(s2, 2484);
		c.getPA().sendString(s3, 2485);
		c.getPA().sendChatInterface(2480);
	}

	public void sendOption5(String s, String s1, String s2, String s3, String s4) {
		c.getPA().sendString("Select an Option", 2493);
		c.getPA().sendString(s, 2494);
		c.getPA().sendString(s1, 2495);
		c.getPA().sendString(s2, 2496);
		c.getPA().sendString(s3, 2497);
		c.getPA().sendString(s4, 2498);
		c.getPA().sendChatInterface(2492);
	}

	/*
	 * Statements
	 */

	public void sendStatement(String s) { // 1 line click here to continue chat
											// box interface
		c.getPA().sendString(s, 357);
		c.getPA().sendString("Click here to continue", 358);
		c.getPA().sendChatInterface(356);
	}

	public void sendStatement(String line1, String line2) {
		c.getPA().sendString(line1, 360);
		c.getPA().sendString(line2, 361);
		c.getPA().sendChatInterface(359);
	}

	public void sendStatement(String line1, String line2, String line3) {
		c.getPA().sendString(line1, 364);
		c.getPA().sendString(line2, 365);
		c.getPA().sendString(line3, 366);
		c.getPA().sendChatInterface(363);
	}

	public void sendStatement(String line1, String line2, String line3, String line4) {
		c.getPA().sendString(line1, 369);
		c.getPA().sendString(line2, 370);
		c.getPA().sendString(line3, 371);
		c.getPA().sendString(line4, 372);
		c.getPA().sendChatInterface(368);
	}

	public void sendStatement(String line1, String line2, String line3, String line4, String line5) {
		c.getPA().sendString(line1, 375);
		c.getPA().sendString(line2, 376);
		c.getPA().sendString(line3, 377);
		c.getPA().sendString(line4, 378);
		c.getPA().sendString(line5, 379);
		c.getPA().sendChatInterface(374);
	}

	/*
	 * Npc chat head
	 */

	public void sendNpcChat1(String s, int ChatNpc, String name) {
		c.getPA().sendFrame200(4883, 9850);
		c.getPA().sendString(name, 4884);
		c.getPA().sendString(s, 4885);
		c.getPA().sendFrame75(ChatNpc, 4883);
		c.getPA().sendChatInterface(4882);
	}

	public void sendNpcChat2(String s, String s1, int ChatNpc, String name) {
		c.getPA().sendFrame200(4888, 9850);
		c.getPA().sendString(name, 4889);
		c.getPA().sendString(s, 4890);
		c.getPA().sendString(s1, 4891);
		c.getPA().sendFrame75(ChatNpc, 4888);
		c.getPA().sendChatInterface(4887);
	}

	public void sendNpcChat3(String s, String s1, String s2, int ChatNpc, String name) {
		c.getPA().sendFrame200(4894, 9850);
		c.getPA().sendString(name, 4895);
		c.getPA().sendString(s, 4896);
		c.getPA().sendString(s1, 4897);
		c.getPA().sendString(s2, 4898);
		c.getPA().sendFrame75(ChatNpc, 4894);
		c.getPA().sendChatInterface(4893);
	}

	private void sendNpcChat4(String s, String s1, String s2, String s3, int ChatNpc, String name) {
		c.getPA().sendFrame200(4901, 9850);
		c.getPA().sendString(name, 4902);
		c.getPA().sendString(s, 4903);
		c.getPA().sendString(s1, 4904);
		c.getPA().sendString(s2, 4905);
		c.getPA().sendString(s3, 4906);
		c.getPA().sendFrame75(ChatNpc, 4901);
		c.getPA().sendChatInterface(4900);
	}

	/*
	 * Player chat head
	 */

	private void sendPlayerChat1(String s, int emoteid) {
		c.getPA().sendFrame200(969, emoteid);
		c.getPA().sendString(Misc.capitalize(c.getDisplayName()), 970);
		c.getPA().sendString(s, 971);
		c.getPA().sendFrame185(969);
		c.getPA().sendChatInterface(968);
	}

	public void sendPlayerChat2(String s, String s1) {
		c.getPA().sendFrame200(974, 9850);
		c.getPA().sendString(c.getDisplayName(), 975);
		c.getPA().sendString(s, 976);
		c.getPA().sendString(s1, 977);
		c.getPA().sendFrame185(974);
		c.getPA().sendChatInterface(973);
	}

	public void sendPlayerChat3(String s, String s1, String s2) {
		c.getPA().sendFrame200(980, 9850);
		c.getPA().sendString(c.getDisplayName(), 981);
		c.getPA().sendString(s, 982);
		c.getPA().sendString(s1, 983);
		c.getPA().sendString(s2, 984);
		c.getPA().sendFrame185(980);
		c.getPA().sendChatInterface(979);
	}

	public void sendPlayerChat4(String s, String s1, String s2, String s3) {
		c.getPA().sendFrame200(987, 9850);
		c.getPA().sendString(c.getDisplayName(), 988);
		c.getPA().sendString(s, 989);
		c.getPA().sendString(s1, 990);
		c.getPA().sendString(s2, 991);
		c.getPA().sendString(s3, 992);
		c.getPA().sendFrame185(987);
		c.getPA().sendChatInterface(986);
	}

	/**
	 * Statements.
	 */
	public void sendStatement(String[] lines) {
		switch (lines.length) {
		case 1:
			sendStatement(lines[0]);
			break;
		case 2:
			sendStatement(lines[0], lines[1]);
			break;
		case 3:
			sendStatement(lines[0], lines[1], lines[2]);
			break;
		case 4:
			sendStatement(lines[0], lines[1], lines[2], lines[3]);
			break;
		case 5:
			sendStatement(lines[0], lines[1], lines[2], lines[3], lines[4]);
			break;
		}
	}
}
