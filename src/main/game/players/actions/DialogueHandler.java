package main.game.players.actions;

import main.game.npcs.NPCHandler;
import main.game.players.Player;
import main.util.Misc;

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
		c.getVariables().talkingNpc = npcId;
		switch (dialogue) {
		case 0:
			c.getVariables().talkingNpc = -1;
			c.getPA().removeAllWindows();
			c.getVariables().nextChat = 0;
			break;
		case 254:
			sendNpcChat4("Hello there " + c.playerName + ".", "I am Sigmund the stall merchant.",
					"I can buy any items from the", "stalls next to me for a set price.", c.getVariables().talkingNpc,
					"Sigmund The Merchent");
			c.getVariables().nextChat = 255;
			break;
		case 321:
			this.sendOption2("How many kills do I have remaining?", "Never mind!");
			c.getVariables().dialogueAction = 121;
			break;
		case 255:
			sendPlayerChat1("Okay thanks a lot Sigmund!", 9850);
			c.getVariables().nextChat = 0;
			break;
		case 170:
			sendNpcChat4("Hello there " + c.playerName + "!", "If you have a skill level 99 in any given skill,",
					"and wish to buy a cape of achievment.", "Then please trade me to purchase a cape.",
					c.getVariables().talkingNpc, "Wise Old Man");
			c.getVariables().nextChat = 171;
			break;
		case 171:
			sendPlayerChat1("Okay thanks a lot Wise Old Man.", 9850);
			c.getVariables().nextChat = 0;
			break;
		case 180:
			sendNpcChat4("Hello there " + c.playerName + ".", "I am the Pest control points, shopkeeper.",
					"I sell various Void Knight items for points which",
					"can be obtained through winning Pest control games.", c.getVariables().talkingNpc, "Void Knight");
			c.getVariables().nextChat = 181;
			break;
		case 181:
			sendPlayerChat1("Okay great, I'm sure I will be back soon!", 9850);
			c.getVariables().nextChat = 0;
			break;
		case 198:
			sendNpcChat3("Hello there " + c.playerName + ".", "I am Thok, the dungeoneering master, I can lead you to ",
					"the dungeoneering lobby, would you like to?", c.getVariables().talkingNpc,
					"Thok, Master of Dungeoneering");
			c.getVariables().nextChat = 199;
			break;
		case 199:
			sendOption2("Yes, please.", "May be later.");
			c.getVariables().dialogueId = 3622;
			break;
		case 208:
			sendNpcChat1("Good luck completing the dungeon!", c.getVariables().talkingNpc,
					"Thok, Master of Dungeoneering");
			break;
		case 204:
			sendNpcChat2("Oh i see you decided to come.", "So what do you want to do?", c.getVariables().talkingNpc,
					"Thok, Master of Dungeoneering");
			c.getVariables().nextChat = 209;
			break;
		case 209:
			sendOption4("Trade", "Start Floor 1(small/medium)", "Start Floor 2(medium/large)", "Cancel");
			c.getVariables().dialogueAction = 3683;
			break;
		case 150:
			sendNpcChat4("Hello there " + c.playerName + "!", "I am the mage of Zamorak and I can teleport",
					"you to a number of Runecrafting altars.", "Which Runecrafting Altar do you want to go to?",
					c.getVariables().talkingNpc, "Mage of Zamorak");
			c.getVariables().nextChat = 151;
			break;
		case 151:
			sendOption5("Air", "Mind", "Water", "Earth", "More");
			c.getVariables().dialogueAction = 10;
			c.getVariables().dialogueId = 151;
			c.getVariables().teleAction = -1;
			break;
		case 152:
			sendOption5("Fire", "Body", "Cosmic", "Astral", "More");
			c.getVariables().dialogueAction = 11;
			c.getVariables().dialogueId = 152;
			c.getVariables().teleAction = -1;
			break;
		case 153:
			sendOption5("Nature", "Law", "Death", "Blood", "Nowhere");
			c.getVariables().dialogueAction = 12;
			c.getVariables().dialogueId = 153;
			c.getVariables().teleAction = -1;
			break;
		case 250:
			sendNpcChat4("Hello there " + c.playerName + "!", "I am the Incendius Guide and I offer advice for",
					"Incendius beginners about all they need to know to begin",
					"there adventure, would you like to see the information I offer?", c.getVariables().talkingNpc,
					"Incendius Guide");
			c.getVariables().nextChat = 251;
			break;
		case 251:
			sendPlayerChat1("Okay great! Yes please show me the information.", 9850);
			c.getVariables().nextChat = 252;
			break;

		case 340:
			break;
		case 111:
			sendNpcChat4("Hello!", "My name is Kuradal and I am a master of the slayer skill.",
					"I can assign you a slayer task suitable to your combat level.", "Would you like a slayer task?",
					c.getVariables().talkingNpc, "Kuradal");
			c.getVariables().nextChat = 112;
			break;
		case 112:
			sendOption2("Yes I would like a slayer task please.",
					"No I would not like a slayer task right now thanks.");
			c.getVariables().dialogueAction = 5;
			break;
		case 113:
			sendNpcChat2("I see I have already assigned you a task to complete.",
					"Would you like me to give you an easier task?", c.getVariables().talkingNpc, "Kuradal");
			c.getVariables().nextChat = 114;
			break;
		case 114:
			sendOption2("Yes I would like an easier task.", "No I would like to keep my task.");
			c.getVariables().dialogueAction = 6;
			break;
		case 320: // Mage of zamorak
			sendNpcChat2("This is no place to talk!", "We are in the middle of the Wilderness..",
					c.getVariables().talkingNpc, "Mage of Zamorak");
			c.getVariables().nextChat = 0;
			break;
		case 4:
			sendPlayerChat1("Do you have any items for sale?", 9850);
			c.getVariables().nextChat = 5;
			break;
		case 5:
			sendNpcChat1("Sure, what type of wares are you interested in seeing?", c.getVariables().talkingNpc,
					"Nastroth");
			c.getVariables().nextChat = 6;
			break;
		case 6:
			sendOption4("Melee Gear", "Magic Gear", "Ranged Gear", "Supplies");
			c.getVariables().dialogueAction = 2;
			c.getVariables().nextChat = 0;
			break;
		case 7:
			sendNpcChat1("Hello " + c.playerName + ", what can I do for you?", c.getVariables().talkingNpc,
					"Strange Old Man");
			c.getVariables().nextChat = 8;
			break;
		case 8:
			sendOption4("I would like to reset my Barrows kills.", "I need my Barrows items fixed.",
					"Do you know where can I get a spade from?", "Never mind.");
			c.getVariables().nextChat = 0;
			c.getVariables().dialogueAction = 50;
			break;
		case 9:
			sendNpcChat1("You can buy a spade from the general store at home.", c.getVariables().talkingNpc,
					"Strange Old Man");
			c.getVariables().nextChat = 10;
			break;
		case 10:
			sendPlayerChat1("Okay great, thanks a lot!", 9850);
			c.nextChat = 0;
			break;
		case 13:
			sendNpcChat1("Sure thing, hand it over.", c.getVariables().talkingNpc, "Strange Old Man");
			c.getVariables().nextChat = 14;
			break;
		case 14:
			c.getPA().fixAllBarrows();
			sendNpcChat1("Here you go, " + c.playerName + ", all fixed!", c.getVariables().talkingNpc,
					"Strange Old Man");
			c.getVariables().nextChat = 0;
			break;
		case 50:
			sendPlayerChat1("Bob, do you know where any Magic trees are located?", 9850);
			c.getVariables().nextChat = 51;
			break;
		case 51:
			sendNpcChat3("Yes, they are located at the Gnome stronghold, west from here.",
					"You will find rare Magic trees there. Would you like me to",
					"transport you to the Gnome Woodcutting area? ", c.getVariables().talkingNpc, "Bob");
			c.getVariables().nextChat = 52;
			break;
		case 52:
			sendOption2("Yes Please!", "Not now thanks.");
			c.getVariables().dialogueAction = 60;
			c.getVariables().nextChat = 0;
			break;
		case 155:
			sendOption2("Yeah I'm fearless", "No way that looks scary");
			c.getVariables().dialogueAction = 982;
			break;
		case 30:
			sendNpcChat3("Hello there " + c.playerName + ".", "If you are interested in training the farming skill,",
					"then please trade with me to buy farming supplies.", c.getVariables().talkingNpc, "Vanessa");
			c.getVariables().nextChat = 31;
			break;
		case 31:
			sendPlayerChat1("Okay great! Thanks Vanessa.", 9850);
			c.getVariables().nextChat = 0;
			break;
		case 200:
			sendPlayerChat1("Nurmof, could you give me a guide on Falador Mine?", 9850);
			c.getVariables().nextChat = 201;
			break;
		case 201:
			sendNpcChat3("Yes of course I can " + c.playerName + ". You can buy a any Pickaxe",
					"you need from me, for a certain price. All the ore you can mine",
					"can be found around this area, and in the Guild located South. ", c.getVariables().talkingNpc,
					"Bob");
			c.getVariables().nextChat = 203;
			break;
		case 203:
			sendPlayerChat1("Okay brilliant, thanks a lot Nurmof!", 9850);
			c.getVariables().nextChat = 0;
			break;
		case 205:
			sendPlayerChat1("Hello there, what is through that gate if I may ask?", 9850);
			c.getVariables().nextChat = 206;
			break;
		case 206:
			sendNpcChat3("Well " + c.playerName + ", through this gate is the Mining Guild.",
					"Any player with a Mining level of 85+ can enter through the",
					"gate to mine the best and more valuable ore on Incendius.", c.getVariables().talkingNpc,
					"Mining Instructor");
			c.getVariables().nextChat = 207;
			break;
		case 207:
			sendPlayerChat1("Okay great, thanks a lot!", 9850);
			c.getVariables().nextChat = 0;
			break;
		case 300:
			sendPlayerChat1("Hello there Zambo, do you have any items for sale?", 9850);
			c.getVariables().nextChat = 311;
			break;
		case 311:
			sendNpcChat4("Indeed I do " + c.playerName + ", I sell a wide range of rare and",
					"powerful items in exchange for Donator Points. You can get",
					"Donator Points by simply typing ::Donate and by following",
					"the instructions on that page or by talking to Raw Envy.", c.getVariables().talkingNpc, "Zambo");
			c.getVariables().nextChat = 312;
			break;

		case 312:
			sendPlayerChat1("Okay thanks a lot Zambo, loving the outfit by the way!", 9850);
			c.getVariables().nextChat = 313;
			break;
		case 313:
			sendNpcChat1("No problem my friend and thanks!", c.getVariables().talkingNpc, "Zambo");
			c.getVariables().nextChat = 0;
			break;

		case 215:
			sendPlayerChat1("Hello there Grum, what wares do you have for sale?", 9850);
			c.getVariables().nextChat = 216;
			break;
		case 216:
			sendNpcChat3("Well " + c.playerName + ", I sell a wide range of rare and",
					"expensive items in exchange for Voting Points. You can",
					"obtain voting Points & a reward by Voting every 24 hours!", c.getVariables().talkingNpc, "Grum");
			c.getVariables().nextChat = 217;
			break;

		case 217:
			sendPlayerChat1("Okay great, I will make sure I use ::vote every 24 hours!", 9850);
			c.getVariables().nextChat = 0;
			break;

		case 225:
			sendNpcChat3("Hello " + c.playerName + ", I am the Make-Over mage. I have",
					"a magical ability which allows me to change the appearance",
					"of players, this is free of charge and costs nothing!", c.getVariables().talkingNpc, "Grum");
			c.getVariables().nextChat = 226;
			break;

		case 226:
			sendPlayerChat1("Okay great, I will make sure I use ::vote every 24 hours!", 9850);
			c.getVariables().nextChat = 0;
			break;

		case 17:
			sendPlayerChat1("Do you have any items for sale?", 9850);
			c.getVariables().nextChat = 18;
			break;
		case 18:
			sendNpcChat3("Yes, I do. I sell a large variety of powerful items in exchange",
					"for player killing points. Feel free to browse through my wares",
					"until you find what you're looking for.", c.getVariables().talkingNpc, "Mandrith");
			c.getVariables().nextChat = 19;
			break;
		case 210:
			sendNpcChat3("Hello there " + c.playerName + ", you look like the kind of person who",
					"would be into the Fishing skill. I sell the finest fishing hardware",
					"in the whole of Incendius, trade me to view my wares.", c.getVariables().talkingNpc, "Harry");
			c.getVariables().nextChat = 211;
			break;
		case 211:
			sendPlayerChat1("Okay thanks for the information Harry.", 9850);
			c.getVariables().nextChat = 0;
			break;
		case 19:
			sendOption4("View the Pk Points shop", "View my current amount of Pk Points",
					"Exchange PvP Artifacts for Pk Points", "Cancel");
			c.getVariables().dialogueAction = 0;
			break;
		case 20:
			sendOption4("Pk Locations", "Minigames", "Monsters", "Cancel");
			c.getVariables().dialogueAction = 55;
			break;
		case 21:
			sendOption4("27 Ports", "East Dragons", "Graveyard (26 Wild)", "Mage Bank");
			c.getVariables().dialogueAction = 1;
			c.getVariables().nextChat = 0;
			break;
		case 22:
			sendOption4("Minigames", "Monsters", "Slayer", "Skilling");
			c.getVariables().dialogueAction = 4;
			c.getVariables().nextChat = 0;
			break;
		case 24:
			sendStatement("You currently have " + c.getVariables().pkp + " PK points.");
			c.getVariables().dialogueAction = 0;
			c.getVariables().nextChat = 0;
			break;
		case 26:
			c.getDH().sendOption4("Normal Magic", "Ancient Magicks", "Lunar Magic", "Go back");
			c.getVariables().nextChat = 0;
			c.getVariables().dialogueAction = 6;
			break;
		case 25:
			sendNpcChat1("You have no statuettes, please come back when you do.", c.getVariables().talkingNpc,
					"Mandrith");
			c.getVariables().nextChat = 0;
			if (c.getItems().playerHasItem(14876, 1)) {
				sendNpcChat1("Hmm.. Ancient Statuette? I'll buy that for 15 Pk Points!", c.getVariables().talkingNpc,
						"Mandrith");
				c.getVariables().nextChat = 81;
			} else if (c.getItems().playerHasItem(14877, 1)) {
				sendNpcChat1("Hmm.. Seren Statuette? I'll buy that for 10 Pk Points!", c.getVariables().talkingNpc,
						"Mandrith");
				c.getVariables().nextChat = 81;
			} else if (c.getItems().playerHasItem(14878, 1)) {
				sendNpcChat1("Hmm.. Armadyl Statuette? I'll buy that for 7 Pk Points!", c.getVariables().talkingNpc,
						"Mandrith");
				c.getVariables().nextChat = 81;// demise
			} else if (c.getItems().playerHasItem(14879, 1)) {
				sendNpcChat1("Hmm.. Zamorak Statuette? I'll buy that for 5 Pk Points!", c.getVariables().talkingNpc,
						"Mandrith");
				c.getVariables().nextChat = 81;
			} else if (c.getItems().playerHasItem(14880, 1)) {
				sendNpcChat1("Hmm.. Saradomin Statuette? I'll buy that for 4 Pk Points!", c.getVariables().talkingNpc,
						"Mandrith");
				c.getVariables().nextChat = 81;
			} else if (c.getItems().playerHasItem(14881, 1)) {
				sendNpcChat1("Hmm.. Bandos Statuette? I'll buy that for 3 Pk Points", c.getVariables().talkingNpc,
						"Mandrith");
				c.getVariables().nextChat = 81;
			}
			break;
		case 81:
			c.sendMessage("You sold your PvP artifact to Mandrith in return for Pk Points.");
			if (c.getItems().playerHasItem(14876, 1)) {
				c.getItems().deleteItem(14876, 1);
				c.getVariables().pkp += 15;
			} else if (c.getItems().playerHasItem(14877, 1)) {
				c.getItems().deleteItem(14877, 1);
				c.getVariables().pkp += 10;
			} else if (c.getItems().playerHasItem(14878, 1)) {
				c.getItems().deleteItem(14878, 1);
				c.getVariables().pkp += 7;
			} else if (c.getItems().playerHasItem(14879, 1)) {
				c.getItems().deleteItem(14879, 1);
				c.getVariables().pkp += 5;
			} else if (c.getItems().playerHasItem(14880, 1)) {
				c.getItems().deleteItem(14880, 1);
				c.getVariables().pkp += 5;
			} else if (c.getItems().playerHasItem(14881, 1)) {
				c.getItems().deleteItem(14881, 1);
				c.getVariables().pkp += 3;
			}
			c.getPA().closeAllWindows();
			break;
		case 77:
			sendNpcChat4("" + c.playerName + " you have Failed.", "You didn't participate enough to take down",
					"the portals. ", "Try Harder next time.", c.getVariables().talkingNpc, "Void Knight");
			break;
		case 78:
			sendNpcChat4("All is Lost!", "You could not take down the portals in time.", " ", "Try Harder next time.",
					c.getVariables().talkingNpc, "Void Knight");
			break;
		case 79:
			sendNpcChat4("Congratulations " + c.playerName + "!", "You took down all the portals whilst keeping",
					"the void knight alive.", "You been awarded, well done.", c.getVariables().talkingNpc,
					"Void Knight");
			break;

		case 401:
			sendNpcChat4("Hello there " + c.playerName + "!", " I can reset any combat skill for you,",
					"but remember, this is irreversable!", "Choose wisely", c.getVariables().talkingNpc, "Town Crier");
			c.getVariables().nextChat = 402;
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
			c.getVariables().dialogueAction = 42;
			break;
		case 403:
			sendOption4("Reset Prayer", "Reset Magic", "Reset Ranged", "Back");
			c.getVariables().dialogueAction = 43;
			break;
		case 404:
			sendNpcChat2("Congratulations!", "Your " + c.getVariables().skillReseted + " has been completely reset!",
					c.getVariables().talkingNpc, "NPC NAME");
			c.getVariables().nextChat = 0;
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
		c.getPA().sendString(Misc.capitalize(c.playerName), 970);
		c.getPA().sendString(s, 971);
		c.getPA().sendFrame185(969);
		c.getPA().sendChatInterface(968);
	}

	public void sendPlayerChat2(String s, String s1) {
		c.getPA().sendFrame200(974, 9850);
		c.getPA().sendString(c.playerName, 975);
		c.getPA().sendString(s, 976);
		c.getPA().sendString(s1, 977);
		c.getPA().sendFrame185(974);
		c.getPA().sendChatInterface(973);
	}

	public void sendPlayerChat3(String s, String s1, String s2) {
		c.getPA().sendFrame200(980, 9850);
		c.getPA().sendString(c.playerName, 981);
		c.getPA().sendString(s, 982);
		c.getPA().sendString(s1, 983);
		c.getPA().sendString(s2, 984);
		c.getPA().sendFrame185(980);
		c.getPA().sendChatInterface(979);
	}

	public void sendPlayerChat4(String s, String s1, String s2, String s3) {
		c.getPA().sendFrame200(987, 9850);
		c.getPA().sendString(c.playerName, 988);
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
