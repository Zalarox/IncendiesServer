package incendius.game.npcs.data;

import incendius.game.npcs.NPC;
import incendius.game.npcs.NPCHandler;
import incendius.game.players.Player;

public class EmoteHandler {

	public static int getDefenceEmote(int i) {
		if (NPCHandler.npcs[i].npcType >= 10328 && NPCHandler.npcs[i].npcType <= 10363) {
			return 424;
		}
		if (NPCHandler.npcs[i].npcType >= 10364 && NPCHandler.npcs[i].npcType <= 10385) {
			return 5567;
		}
		if (NPCHandler.npcs[i].npcType >= 10496 && NPCHandler.npcs[i].npcType <= 10505) {
			return 424;
		}
		if (NPCHandler.npcs[i].npcType >= 10570 && NPCHandler.npcs[i].npcType <= 10603) {
			return 424;
		}
		switch (NPCHandler.npcs[i].npcType) {
		case 10249:
		case 10253:
		case 10257:
		case 12060:
			return 13038;

		case 10247:
		case 10248:
		case 10258:
			return 13051;

		case 10250:
		case 10252:
		case 10254:
		case 10259:
		case 10261:
			return 13034;

		case 10246:
		case 10256:
			return 12307;

		case 10255:
			return 13054;

		case 12051:
		case 10262:
			return 13054;
		default:
			return -1;
		}
	}

	public static int getCombatEmote(int i, String type) {
		int attack = -1, dead = 2304;
		NPC n = NPCHandler.npcs[i];
		String name = NPCHandler.getNpcListName(NPCHandler.npcs[i].npcType);
		if (name.toLowerCase().contains("frog")) {
			attack = 1793;
			dead = 1795;
		} else if (name.toLowerCase().contains("cave slime")) {
			attack = 1788;
			dead = 1792;
		} else if (name.toLowerCase().contains("wolf") && !name.toLowerCase().contains("werewolf")) {
			attack = 6581;
			dead = 6576;
		}
		if (NPCHandler.npcs[i].npcType >= 10328 && NPCHandler.npcs[i].npcType <= 10363) {
			attack = 426;
			dead = 2304;
			return (type.equalsIgnoreCase("Attack")) ? attack : dead;
		}
		if (NPCHandler.npcs[i].npcType >= 10364 && NPCHandler.npcs[i].npcType <= 10385) {
			attack = 5568;
			dead = 5569;
			return (type.equalsIgnoreCase("Attack")) ? attack : dead;
		}
		if (NPCHandler.npcs[i].npcType >= 10496 && NPCHandler.npcs[i].npcType <= 10505) {
			attack = 5327;
			dead = 5329;
			return (type.equalsIgnoreCase("Attack")) ? attack : dead;
		}
		if (NPCHandler.npcs[i].npcType >= 10570 && NPCHandler.npcs[i].npcType <= 10603) {
			attack = Player.MAGIC_SPELLS[NPCHandler.npcs[i].id][2];
			dead = 2304;
			return (type.equalsIgnoreCase("Attack")) ? attack : dead;

		}
		switch (NPCHandler.npcs[i].npcType) {
		case 10249:
		case 10253:
		case 10257:
		case 10260:
			attack = 13049;
			dead = 2304;
			return (type.equalsIgnoreCase("Attack")) ? attack : dead;
		case 10247:
		case 10248:
		case 10258:
			attack = 13052;
			dead = 2304;
			return (type.equalsIgnoreCase("Attack")) ? attack : dead;
		case 10250:
		case 10252:
		case 10254:
		case 10259:
		case 10261:
			attack = 13035;
			dead = 2304;
			return (type.equalsIgnoreCase("Attack")) ? attack : dead;
		case 10256:
			attack = 12305;
			dead = 2304;
			return (type.equalsIgnoreCase("Attack")) ? attack : dead;
		case 10255:
			attack = 13055;
			dead = 2304;
			return (type.equalsIgnoreCase("Attack")) ? attack : dead;
		case 12051:
		case 10262:
			attack = 12006;
			dead = 2304;
			return (type.equalsIgnoreCase("Attack")) ? attack : dead;
		case 8133:// corp beast
			if (n.attackType == 2)
				attack = 10053;
			else if (n.attackType == 1)
				attack = 10059;
			else if (n.attackType == 0)
				attack = 10057;
			dead = 10059;
			break;
		case 8349:// tormented demon
			if (NPCHandler.npcs[i].attackType == 2)
				attack = 10917;
			else if (NPCHandler.npcs[i].attackType == 1)
				attack = 10918;
			else if (NPCHandler.npcs[i].attackType == 0)
				attack = 10922;
			dead = 10924;
			break;
		case 6842:
			attack = 5327;
			break;
		case 6807:
			attack = 8145;
			break;
		case 6830:
			attack = 8292;
			break;
		case 7342:
			attack = 7980;
			break;
		case 6805:
			attack = 7786;
			break;
		case 6812:
			attack = 7935;
			break;
		case 7358:
			attack = 7844;
			break;
		case 7356:
			attack = 7834;
			break;
		case 6801:
			attack = 7853;
			break;
		case 6868:
			attack = 7896;
			break;
		case 7334:
			attack = 8172;
			break;
		case 7352:
			attack = 8235;
			break;
		case 7368:
			attack = 8131;
			break;
		case 6846:
			attack = 7928;
			break;
		case 6836:
			attack = 8275;
			break;
		case 6872:
			attack = 7769;
			break;
		case 7362:
			attack = 8257;
			break;
		case 6838:
			attack = 6254;
			break;
		case 6832:
			attack = 7795;
			break;
		case 7332:
			attack = 8032;
			break;
		case 6797:
			attack = 8104;
			break;
		case 7348:
			attack = 5989;
			break;
		case 7346:
			attack = 8050;
			break;
		case 7338:
			attack = 5228;
			break;
		case 6844:
			attack = 7657;
			break;
		case 6834:
			attack = 8248;
			break;
		case 6888:
		case 6886:
		case 6884:
		case 6882:
		case 6880:
		case 6878:
		case 6876:
			attack = 7762;
			break;
		case 7366:
			attack = 5228;
			break;
		case 7364:
			attack = 5228;
			break;
		case 6993:
			attack = 8569;
			break;
		case 6819:
			attack = 7670;
			break;
		case 6840:
			attack = 4925;
			break;
		case 7373:
			attack = 7994;
			break;
		case 6828:
			attack = 8208;
			break;
		case 6866:
			attack = 7816;
			break;
		case 6810:
			attack = 7970;
			break;
		case 6799:
			attack = 8069;
			break;
		case 7336:
			attack = 7863;
			break;
		case 7330:
			attack = 8222;
			break;
		case 6864:
			attack = 8024;
			break;
		case 7350:
			attack = 7693;
			break;
		case 7344:
		case 7376:
			attack = 8183;
			break;
		case 6795:
			attack = 6790;
			break;
		case 6870:
			attack = 8303;
			break;
		case 6874:
			attack = 5782;
			break;
		case 6823:
			attack = 6376;
			break;
		case 6816:
			attack = 8286;
			break;
		case 7340:
			attack = 7879;
			break;
		case 7360:
			attack = 8183;
			break;
		case 50:// drags
			if (n.attackType == 0) {
				attack = 80;
			} else {
				attack = 81;
			}
			break;
		case 10246:// dagger og rapier
		case 10266:
		case 10267:
			attack = 12028;
			dead = 2304;
			break;
		case 10271:// longsword og eitthvad skritid
		case 10280:
			attack = 13039;
			dead = 2304;
			break;
		case 10273:// maul
		case 10278:
			attack = 10505;
			dead = 2304;
			break;
		case 10276:// spjot
			attack = 15072;
			dead = 2304;
			break;
		case 5529: // Yak
			attack = 5782;
			dead = 5784;
			break;
		case 1676: // Experiment
			attack = 1626;
			dead = 1628;
			break;
		case 1265: // Experiment
			attack = 1312;
			dead = 1314;
			break;
		case 1677: // Experiment
			attack = 1616;
			dead = 1618;
			break;
		case 1678: // Experiment
			attack = 1612;
			dead = 1611;
			break;
		case 8324: // Elite Black Knight
			attack = 10854;
			dead = 10856;
			break;
		case 9463:
		case 9465:
		case 9467: // Strykewyrms
			attack = 12791;
			dead = 12793;
			break;
		case 19: // White knight
			attack = 7041;
			break;
		case 7133: // Bork
			attack = 8754;
			dead = 8756;
			break;
		case 1591:
		case 51:
		case 1592:
		case 10604:
		case 10773:
		case 53:
		case 54:
		case 55:
		case 941:
		case 1590: // New dragons
			attack = 13151;
			dead = 12250;
			break;
		case 52:
		case 1589:
		case 3376: // Baby dragons
			attack = 25;
			dead = 28;
			break;
		case 82:
		case 83:
		case 84:
		case 1472: // Demons
			attack = 64;
			dead = 67;
			break;
		case 118:
		case 119:
			attack = 99;
			dead = 102;
			break;
		case 122:
		case 123: // Hobgoblin
			attack = 164;
			dead = 167;
			break;
		case 125:
		case 178: // Warriors
			attack = 451;
			break;
		case 2361:
		case 2362:
		case 1183:// Elf warrior (ranged)
			attack = 426;
			break;
		case 1338:
		case 1340:
		case 1342: // Dagganoths
			attack = 1341;
			dead = 1342;
			break;
		case 1605: // Abberant spectre
			attack = 1507;
			dead = 1508;
			break;
		case 1610:
		case 1611: // Gargoyle
			attack = 1519;
			dead = 1518;
			break;
		case 1612: // Banshee
			attack = 1525;
			dead = 1524;
			break;
		case 1613: // Nechryael
			attack = 1528;
			dead = 1530;
			break;
		case 1615: // Abyssal demon
			attack = 1537;
			dead = 1538;
			break;
		case 1616: // Basilisk
			attack = 1546;
			dead = 1548;
			break;
		case 1618:
		case 1619: // Bloodveld
			attack = 1551;
			dead = 1553;
			break;
		case 1620:
		case 1621: // Cockatrice
			attack = 1562;
			dead = 1563;
			break;
		case 1624: // Dust devil
			attack = 1557;
			dead = 1558;
			break;
		case 1626:
		case 1627:
		case 1628:
		case 1629:
		case 1630:
		case 1631:
		case 1632: // Turoth
			attack = 1595;
			dead = 1597;
			break;
		case 1633:
		case 1634:
		case 1635:
		case 1636: // Elemental fiends
			attack = 1582;
			dead = 1580;
			break;
		case 1653:
		case 1648:
		case 1649:
		case 1650:
		case 1651:
		case 1652:
		case 1654:
		case 1655:
		case 1656:
		case 1657: // Crawling hand
			attack = 9127;
			dead = 9126;
			break;
		case 2031: // Bloodworm
			attack = 2070;
			dead = 2073;
			break;
		case 2783: // Dark beast
			attack = 2731;
			dead = 2733;
			break;
		case 2881: // Daggonoth Supreme
			attack = 2855;
		case 2882: // Daggonoth Prime
			attack = 2854;
		case 2883: // Dagganoth Rex
			attack = 2851;
			dead = 2856;
			break;
		case 3066: // Zombie champion
			attack = 5581;
			dead = 5580;
			break;
		case 3200: // Chaos elemental
			attack = 3147;
			dead = 3147;
			break;
		case 3313: // Tanglefoot
			attack = 3262;
			dead = 3263;
			break;
		case 4397:
		case 4398:
		case 4399: // Catablepon
			attack = 4273;
			dead = 4270;
			break;
		case 4418:
		case 6218: // Gorak
			attack = 4300;
			dead = 4302;
			break;
		case 4463:
		case 4464:
		case 4465: // Vampire juvenate
			attack = 7183;
			break;
		case 4527: // Suqah
			attack = 4387;
			dead = 4389;
			break;
		case 4893: // Giant lobster
			attack = 6261;
			dead = 6267;
			break;
		case 4971: // Baby roc
			attack = 5031;
			dead = 5033;
			break;
		case 4972: // Giant roc
			attack = 5024;
			dead = 5027;
			break;
		case 5174:
		case 5176:
		case 5181:
		case 5184:
		case 5187:
		case 5190:
		case 5193: // Ogre chieftain/ Various ogres
			attack = 359;
			dead = 361;
			break;
		case 5213:
		case 5214:
		case 5215:
		case 5216:
		case 5217:
		case 5218:
		case 5219: // Penance fighter
			attack = 5097;
			dead = 5098;
			break;
		case 90: // Skeleton
			attack = 5485;
			dead = 5491;
			break;
		case 5229:
		case 5230:
		case 5231:
		case 5232:
		case 5233:
		case 5234:
		case 5235:
		case 5236:
		case 5237: // Penance ranger
			attack = 5396;
			dead = 5397;
			break;
		case 5247: // Penance queen
			attack = 5411;
			dead = 5412;
			break;
		case 75: // Zombie
			attack = 5578;
			dead = 5569;
			break;

		case 2627:
			attack = 2621;
			dead = 2620;
			break;
		case 2638:
		case 2630:
			attack = 2625;
			dead = 2627;
			break;
		case 2631:
			if (NPCHandler.npcs[i].attackStyle == "Ranged")
				attack = 2633;
			else
				attack = 2628;
			dead = 2630;
			break;
		case 2741:
			attack = 2637;
			dead = 2638;
			break;
		case 2743:
			if (NPCHandler.npcs[i].attackStyle == "Magic")
				attack = 2646;
			else
				attack = 2644;
			dead = 2647;
			break;

		case 2745: // Jad
			if (NPCHandler.npcs[i].attackStyle == "Magic")
				attack = 9300;
			else if (NPCHandler.npcs[i].attackStyle == "Ranged")
				attack = 9276;
			else if (NPCHandler.npcs[i].attackStyle == "Melee")
				attack = 9277;
			dead = 9279;
			break;
		case 5248: // Penance spawn
			attack = 5092;
			dead = 5093;
			break;
		case 5385:
		case 5387:
		case 5388:
		case 5389:
		case 5411:
		case 5422:// Skeleton (stab)
			attack = 5487;
		case 5386:
		case 5390:
		case 5391:
		case 5392:
		case 5412: // Skeleton (crush)
			attack = 5485;
			dead = 5491;
			break;
		case 5452:
		case 5453:
		case 5454:
		case 5455: // Icelord
			attack = 5725;
			dead = 5726;
			break;
		case 5627:
		case 5628: // Sorebones
			attack = 5647;
			dead = 5649;
			break;
		case 5683:
		case 5691:
		case 5699:
		case 5707:
		case 5715:
		case 5723:
		case 5731:
		case 5739:
		case 5747: // Undead Lumberjack
			attack = 5970;
			dead = 5972;
			break;
		case 5750: // Cave bug
			attack = 6079;
			dead = 6081;
			break;
		case 5906: // A doubt
			attack = 6310;
			dead = 6315;
			break;
		case 5993: // Experiment No 2
			attack = 6513;
			dead = 6512;
			break;
		case 6212:
		case 6213: // Werewolf
			attack = 6536;
			dead = 6537;
			break;
		case 110:
		case 111:
		case 112:
		case 113:
		case 116:
		case 117:
		case 4291:
		case 4292:
		case 6269:
		case 6270: // Cyclops and Giants
			attack = 4652;
			dead = 4653;
			break;
		case 6271:
		case 6272:
		case 6273:
		case 6274: // Ork
			attack = 4320;
			dead = 4321;
			break;
		case 6285:
		case 6293: // Warped terrorbird
			attack = 7108;
			dead = 7109;
			break;
		case 6296:
		case 6297: // Warped tortoise
			attack = 7093;
			dead = 7091;
			break;
		case 9172: // Aquanite
			attack = 12041;
			dead = 12039;
			break;
		case 6645:// Revenant Cyclops
			attack = 7453;
			dead = 7454;
			break;

		case 6998:// Revenant Dragon
			attack = 8589;
			dead = 8593;
			break;

		case 6691:// Revenant Dark Beast
			attack = 7476;
			dead = 7468;
			break;

		case 6647:// Revenant Demon
			attack = 7474;
			dead = 7475;
			break;

		case 6688:// Revenant Hellhound
			attack = 7460;
			dead = 7461;
			break;

		case 6622:// Revenant Pyrefiend
		case 6621:// Revenant Icefiend
			attack = 7481;
			dead = 7484;
			break;

		case 6623:// Revenant Vampire
			attack = 7441;
			dead = 7428;
			break;

		case 6611:// Revenant Knight
			attack = 7441;
			dead = 7442;
			break;

		case 6615:// Revenant Ork
			attack = 7411;
			dead = 7416;
			break;

		case 6606:// Revenant Icefiend
			attack = 7397;
			dead = 7397;
			break;

		case 6605:// Revenant Goblin
			attack = 7449;
			dead = 7448;
			break;

		case 6604:// Revenant Imp
			attack = 7407;
			dead = 7408;
			break;
		}
		if (attack == -1)
			attack = getAttackEmote(i);
		if (dead == 2304)
			dead = getAttackEmote(i);
		NPCDefinition n1 = NPCDefinition.forId(NPCHandler.npcs[i].npcType);
		if (attack == -1 && n1 != null)
			attack = n1.getAttackAnimation();
		if (dead == 2304 && n1 != null)
			dead = n1.getDeathAnimation();
		return (type.equalsIgnoreCase("Attack")) ? attack : dead;
	}

	public static int getAttackEmote(int i) {
		switch (NPCHandler.npcs[i].npcType) {
		case 1831:// Cave Slime
			return 1793;
		case 907:// Kolodion
		case 910:// Kolodion
		case 2497:// Tribesman
			return 729;
		case 10100:// Bulwark Beast
			return 13001;
		case 3835:// Kalphite Queen
			if (NPCHandler.npcs[i].attackType == 0)
				return 6241;
			else
				return 6240;

		case 3495:// Kalphite Queen 2
			if (NPCHandler.npcs[i].attackType == 0)
				return 2075;
			else
				return 1979;
		case 6261:// Sergeant Strongstack
		case 6263:// Sergeant Steelwill
		case 6265:// Sergeant Grimspike
			return 6154;
		case 6222:// Kree'arra
			return 3505;
		case 6225:// Flockleader Geerin
			return 6953;
		case 6223:// Wingman Skree
			return 6952;
		case 6227:// Flight Kilisa
			return 6954;
		case 6247:// Commander Zilyana
			return 6964;
		case 6248:// Starlight
			return 6376;
		case 6250:// Growler
			return 7018;
		case 6252:// Bree
			return 7009;
		case 8281:// Ballance Elemental
			return 10680;
		case 8282:// Ballance Elemental
			return 10669;
		case 8283:// Ballance Elemental
			return 10681;
		case 8597:// Avatar Of Creation
		case 9437:// Decaying Avatar
			return 11202;
		case 8596:// Avatar Of Destruction
			return 11197;
		case 3497:// Gelatinnoth Mother
		case 3498:// Gelatinnoth Mother
		case 3499:// Gelatinnoth Mother
		case 3500:// Gelatinnoth Mother
		case 3501:// Gelatinnoth Mother
		case 3502:// Gelatinnoth Mother
			return 1341;
		case 10126:// Unholy Cursebearer
			return 13169;
		case 8528:// Nomad
			return 12697;

		case 10775:// Frost Dragon
			return 13155;

		case 1158:// Kalphite Queen
			return 6231;

		case 5666:// Barrelchest
			if (NPCHandler.npcs[i].attackType == 0)
				return 5894;
			else
				return 5895;

		case 3847:// Sea Troll Queen
			return 3992;

		case 3340:// Giant Mole
			if (NPCHandler.npcs[i].attackType == 7)
				return 3311;
			else if (NPCHandler.npcs[i].attackType == 0) // melee
				return 3312;

		case 7354:// Giant Chinchompa
			return 7755;
		case 7355:// Fire Titan
			return 7834;
		case 7359:// Ice Titan
			return 8183;
		case 7374:// Ravenous Locust
			return 7994;
		case 6803:// Spirit Cobra
			return 8159;
		case 6814:// Bunyip
			return 7741;
		case 6826:// Dreadfowl
			return 5387;
		case 6856:// Iron Minotaur
			return 4921;
		case 6858:// Steel Minotaur
			return 5327;
		case 6860:// Mithril Minotaur
		case 6862:// Adamant Minotaur
			return 7656;
		case 2746:// Yt-Hurkot
			return 2637;
		case 2607:// Tzhaar-Xil
			return 2611;
		case 7369:// Void Shifter
			return 8130;
		case 7371:// Void Ravager
			return 8093;
		case 2028:// Karil
			return 2075;

		case 2025:// Ahrim
			return 729;

		case 2026:// Dharok
			return 2067;

		case 2027:// Guthan
			return 2080;

		case 2029:// Torag
			return 0x814;

		case 2030:// Verac
			return 2062;

		case 5228:// Penance Runner
			return 5228;

		// Training & Misc
		case 1640:// Jelly
			return 8575;

		case 8321:// Elite Dark Mage
			return 10516;

		case 1250:// Fiyr Shade
			return 1284;

		case 10815:// New Red Dragon
		case 10607:// New Green Dragon
		case 10224:// New Black Dragon
			return 13151;

		case 8777:// Chaos Dwarf Hancannoeer
			return 12141;

		case 7797:// Kurask Overlord
			return 9439;

		case 6753:// Mummy
			return 5554;

		case 5250:// Scarab Mage
			return 7621;

		case 7808:// Mummy Warrior
			return 5554;

		case 7135:// Ork Legion
			return 8760;

		case 2892:// Spinolyp
		case 2894:// Spinolyp
			return 2868;

		case 2037:// Skeleton
			return 5485;

		case 2457:// Wallaski
			return 2365;

		case 6219:// Spiritual Warrior
		case 6255:// Spiritual Warrior
			return 451;

		case 13:// Wizard
			return 711;

		case 103:// Ghost
		case 655:// Tree Spirit
			return 123;
		case 1643:// Infernal Mage
			return 7183;
		case 5363:// Mithril Dragon
			return 80;

		case 124:// Earth Warrior
			return 390;

		case 803:// Monk
			return 422;

		case 58:// Shadow Spider
		case 59:// Giant Spider
		case 60:// Giant Spider
		case 61:// Spider
		case 62:// Jungle Spider
		case 63:// Deadly Red Spider
		case 64:// Ice Spider
		case 134:// Poison Spider
			return 143;

		case 105:// Bear
		case 106:// Bear
			return 41;

		case 412:// Bat
		case 78:// Giant Bat
			return 30;

		case 2033:// Giant Rat
			return 138;
		case 102:// Goblin
		case 100:// Goblin
		case 101:// Goblin
			return 6184;
		case 81:// Cow
			return 0x03B;
		case 21:// Hero
			return 451;
		case 41:// Chicken
			return 55;
		case 9:// Guard
		case 32:// Guard
		case 20:// Paladin
			return 451;
		case 2452:// Giant Rock Crab
			return 1312;

		case 2889:// Rock Lobster
			return 2859;

		case 1267:// Rock Crab
			return 1312;

		case 1153:// Kalphite Worker
		case 1154:// Kalphite Soldier
		case 1155:// Kalphite Guardian
		case 1156:// Kalphite Worker
		case 1157:// Kalphite Guardian
			return 1184;
		default:
			return -1;
		}
	}

	public int getDeathEmote(int npcId) {
		switch (npcId) {
		case 1831:// Cave Slime
			return 1792;
		case 907:// Kolodion
		case 910:// Kolodion
		case 2497:// Tribesman
			return 714;
		case 10100:// Bulwark Beast
			return 13005;
		case 8596:// Avatar Of Destruction
			return 11199;
		case 10126:// Unholy Cursebearer
			return 13171;
		case 7480:// Tumeken's Shadow
			return 11629;
		case 1250:// Fiyr Shade
			return 1285;
		case 1158:// Kalphite Queen
			return 6228;
		case 2889:// Rock Lobster
			return 2862;
		case 2457:// Wallaski
			return 2367;
		case 8281:// Ballance Elemental
		case 8282:// Ballance Elemental
		case 8283:// Ballance Elemental
			return 10679;
		case 3497:// Gelatinoth Mother
		case 3498:// Gelatinoth Mother
		case 3499:// Gelatinoth Mother
		case 3500:// Gelatinoth Mother
		case 3501:// Gelatinoth Mother
		case 3502:// Gelatinoth Mother
			return 1342;
		case 8777:// Handcannonneer
			return 12181;
		case 5250:// Scarab Mage
			return 7616;
		case 7808:// Mummy Warrior
			return 5555;
		case 6753:// Mummy
			return 5555;
		case 7797:// Kurask Overlord
			return 9440;
		case 10815:// New Red Dragon
		case 10607:// New Green Dragon
		case 10224:// New Black Dragon
			return 13153;
		case 8528:// Nomad
			return 12694;
		case 8597:// Avatar Of Creation
		case 9437:// Decaying Avatar
			return 11204;
		case 1160:// Kalphite Queen
			return 6233;
		case 10775:// Frost Dragon
			return 13153;
		case 7135:// Ork Legion
			return 8761;
		case 3340:// Giant Mole
			return 3310;
		case 8321:// Elite Dark Mage
			return 2304;
		case 5666:// Barrelchest
			return 5898;
		case 6247:// Commander Zilyana
			return 6965;
		case 6248:// Starlight
			return 6377;
		case 6250:// Growler
			return 7016;
		case 6252:// Bree
			return 7011;
		case 6261:// Seargent Strongstack
		case 6263:// Seargent Steelwill
		case 6265:// Seargent Grimspike
			return 6156;
		case 6260:// General Graardor
			return 7062;
		case 2892:// Spinolyp
		case 2894:// Spinolyp
			return 2865;
		case 6222:// Kree'ara
			return 3503;
		case 6223:// Wingman Skree
		case 6225:// Flockleader Geerin
		case 6227:// Flight Kilisa
			return 6956;
		case 2607:// Tzhaar-Xil
			return 2607;
		case 2738:// Tz-Kek
			return 2627;
		case 2746:// Yt-Hurkot
			return 2638;
		case 2035:// Rat
			return 146;
		case 2033:// Rat
			return 141;
		case 102:// Goblin
		case 100:// Goblin
		case 101:// Goblin
			return 6182;
		case 81:// Cow
			return 0x03E;
		case 41:// Chicken
		case 751:// Berserk Barbarian Spirit
			return 302;
		case 105:// Grizly Bear
		case 106:// Black Bear
			return 44;
		case 412:// Bat
		case 78:// Giant Bat
			return 36;
		case 58:// Shadow Spider
		case 59:// Giant Spider
		case 60:// Giant Spider
		case 61:// Spider
		case 62:// Jungle Spider
		case 63:// Deadly Red Spider
		case 64:// Ice Spider
		case 134:// Poison Spider
			return 146;
		case 1153:// Kalphite Worker
		case 1154:// Kalphite Soldier
		case 1155:// Kalphite Guardian
		case 1156:// Kalphite Worker
		case 1157:// Kalphite Guardian
			return 1190;
		case 103:// Ghost
		case 104:// Ghost
			return 123;
		default:
			return 2304;
		}
	}
}