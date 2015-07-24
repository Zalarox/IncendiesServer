package main.game.npcs.data;

import main.util.Misc;

//TODO REDO THIS SHIT.. THIS IS TERRIBLE

public class SummoningData {

	public static boolean isSummonNpc(int id) {
		switch (id) {
		case 6842:
		case 6807:
		case 6830:
		case 7342:
		case 6805:
		case 6812:
		case 7358:
		case 7356:
		case 6801:
		case 6868:
		case 7334:
		case 7352:
		case 7368:
		case 6846:
		case 6836:
		case 6872:
		case 7362:
		case 6838:
		case 6832:
		case 7332:
		case 6797:
		case 7348:
		case 7346:
		case 7338:
		case 6844:
		case 6834:
		case 6888:
		case 6886:
		case 6884:
		case 6882:
		case 6880:
		case 6878:
		case 6876:
		case 7366:
		case 7364:
		case 6993:
		case 6819:
		case 6840:
		case 7373:
		case 6828:
		case 6866:
		case 6810:
		case 6799:
		case 7336:
		case 7330:
		case 6864:
		case 7350:
		case 7344:
		case 7376:
		case 6795:
		case 6870:
		case 6874:
		case 6823:
		case 6816:
		case 7340:
		case 7360:
			return true;
		}
		return false;
	}

	public static int getSummonMaxHit(int npcId) {
		int Maxhit = 0;
		Maxhit = 0;
		switch (npcId) {
		case 6842:
			Maxhit = 30;
			return Maxhit;
		case 6807:
			Maxhit = 40;
			return Maxhit;
		case 6830:
			Maxhit = 40;
			return Maxhit;
		case 7342:
			Maxhit = 140;
			return Maxhit;
		case 6805:
			Maxhit = 140;
			return Maxhit;
		case 6812:
			Maxhit = 157;
			return Maxhit;
		case 7358:
			Maxhit = 150;
			return Maxhit;
		case 7356:
			Maxhit = 152;
			return Maxhit;
		case 6801:
			Maxhit = 150;
			return Maxhit;
		case 6868:
			Maxhit = 90;
			return Maxhit;
		case 7334:
			Maxhit = 50;
			return Maxhit;
		case 7352:
			Maxhit = 60;
			return Maxhit;
		case 7368:
			Maxhit = 60;
			return Maxhit;
		case 6846:
			Maxhit = 50;
			return Maxhit;
		case 6836:
			Maxhit = 30;
			return Maxhit;
		case 6872:
			Maxhit = 50;
			return Maxhit;
		case 7362:
			Maxhit = 50;
			return Maxhit;
		case 6838:
			Maxhit = 60;
			return Maxhit;
		case 6832:
			Maxhit = 40;
			return Maxhit;
		case 7332:
			Maxhit = 42;
			return Maxhit;
		case 6797:
			Maxhit = 40;
			return Maxhit;
		case 7348:
			Maxhit = 80;
			return Maxhit;
		case 7346:
			Maxhit = 140;
			return Maxhit;
		case 7338:
			Maxhit = 107;
			return Maxhit;
		case 6844:
			Maxhit = 90;
			return Maxhit;
		case 6834:
			Maxhit = 70;
			return Maxhit;
		case 6888:
			Maxhit = 70;
			return Maxhit;
		case 6886:
			Maxhit = 70;
			return Maxhit;
		case 6884:
			Maxhit = 70;
			return Maxhit;
		case 6882:
			Maxhit = 70;
			return Maxhit;
		case 6880:
			Maxhit = 70;
			return Maxhit;
		case 6878:
			Maxhit = 70;
			return Maxhit;
		case 6876:
			Maxhit = 70;
			return Maxhit;
		case 7366:
			Maxhit = 107;
			return Maxhit;
		case 7364:
			Maxhit = 100;
			return Maxhit;
		case 6993:
			Maxhit = 100;
			return Maxhit;
		case 6819:
			Maxhit = 100;
			return Maxhit;
		case 6840:
			Maxhit = 130;
			return Maxhit;
		case 7373:
			Maxhit = 134;
			return Maxhit;
		case 6828:
			Maxhit = 121;
			return Maxhit;
		case 6866:
			Maxhit = 112;
			return Maxhit;
		case 6810:
			Maxhit = 160;
			return Maxhit;
		case 6799:
			Maxhit = 145;
			return Maxhit;
		case 7336:
			Maxhit = 140;
			return Maxhit;
		case 7330:
			Maxhit = 160;
			return Maxhit;
		case 6864:
			Maxhit = 160;
			return Maxhit;
		case 7350:
			Maxhit = 229;
			return Maxhit;
		case 7376:
			Maxhit = 230;
			return Maxhit;
		case 7344:
			Maxhit = 244;
			return Maxhit;
		case 6795:
			Maxhit = 76;
			return Maxhit;
		case 6870:
			Maxhit = 220;
			return Maxhit;
		case 6874:
			Maxhit = 180;
			return Maxhit;
		case 6823:
			Maxhit = 80;
			return Maxhit;
		case 6816:
			Maxhit = 100;
			return Maxhit;
		case 7340:
			Maxhit = 215;
			return Maxhit;
		case 7360:
			Maxhit = 152;
			return Maxhit;
		}
		return Maxhit;
	}

	public static int getSummonHealth(int npcId) {
		int Health = 0;
		Health = 0;
		switch (npcId) {
		case 6842:
			Health = 180;
			return Health;
		case 6807:
			Health = 280;
			return Health;
		case 6830:
			Health = 150;
			return Health;
		case 7342:
			Health = 5280;
			return Health;
		case 6805:
			Health = 5280;
			return Health;
		case 6812:
			Health = 4900;
			return Health;
		case 7358:
			Health = 4760;
			return Health;
		case 7356:
			Health = 4760;
			return Health;
		case 6801:
			Health = 4760;
			return Health;
		case 6868:
			Health = 1540;
			return Health;
		case 7334:
			Health = 5900;
			return Health;
		case 7352:
			Health = 1210;
			return Health;
		case 7368:
			Health = 1210;
			return Health;
		case 6846:
			Health = 1100;
			return Health;
		case 6836:
			Health = 1050;
			return Health;
		case 6872:
			Health = 950;
			return Health;
		case 7362:
			Health = 630;
			return Health;
		case 6838:
			Health = 670;
			return Health;
		case 6832:
			Health = 470;
			return Health;
		case 7332:
			Health = 430;
			return Health;
		case 6797:
			Health = 390;
			return Health;
		case 7348:
			Health = 4540;
			return Health;
		case 7346:
			Health = 4060;
			return Health;
		case 7338:
			Health = 2680;
			return Health;
		case 6844:
			Health = 2110;
			return Health;
		case 6834:
			Health = 1620;
			return Health;
		case 6888:
			Health = 1730;
			return Health;
		case 6886:
			Health = 1730;
			return Health;
		case 6884:
			Health = 1730;
			return Health;
		case 6882:
			Health = 1730;
			return Health;
		case 6880:
			Health = 1730;
			return Health;
		case 6878:
			Health = 1730;
			return Health;
		case 6876:
			Health = 1730;
			return Health;
		case 7366:
			Health = 2680;
			return Health;
		case 7364:
			Health = 2680;
			return Health;
		case 6993:
			Health = 255;
			return Health;
		case 6819:
			Health = 2480;
			return Health;
		case 6840:
			Health = 3810;
			return Health;
		case 7373:
			Health = 3750;
			return Health;
		case 6828:
			Health = 3220;
			return Health;
		case 6866:
			Health = 3000;
			return Health;
		case 6810:
			Health = 2760;
			return Health;
		case 6799:
			Health = 4280;
			return Health;
		case 7336:
			Health = 4410;
			return Health;
		case 7330:
			Health = 5560;
			return Health;
		case 6864:
			Health = 5700;
			return Health;
		case 7350:
			Health = 6670;
			return Health;
		case 7376:
			Health = 6940;
			return Health;
		case 7344:
			Health = 7540;
			return Health;
		case 6795:
			Health = 2330;
			return Health;
		case 6870:
			Health = 6510;
			return Health;
		case 6874:
			Health = 7100;
			return Health;
		case 6823:
			Health = 1000;
			return Health;
		case 6816:
			Health = 3480;
			return Health;
		case 7340:
			Health = 6200;
			return Health;
		case 7360:
			Health = 4760;
			return Health;
		}
		return Health;
	}

	public static int getSummonAttack(int npcId) {
		int Attack = 0;
		Attack = 0;
		switch (npcId) {
		case 6842:
		case 6807:
		case 6830:
		case 7342:
		case 6805:
		case 6812:
		case 7358:
		case 7356:
		case 6801:
		case 6868:
		case 7334:
		case 7352:
		case 7368:
		case 6846:
		case 6836:
		case 6872:
		case 7362:
		case 6838:
		case 6832:
		case 7332:
		case 6797:
		case 7348:
		case 7346:
		case 7338:
		case 6844:
		case 6834:
		case 6888:
		case 6886:
		case 6884:
		case 6882:
		case 6880:
		case 6878:
		case 6876:
		case 7366:
		case 7364:
		case 6993:
		case 6819:
		case 6840:
		case 7373:
		case 6828:
		case 6866:
		case 6810:
		case 6799:
		case 7336:
		case 7330:
		case 6864:
		case 7350:
		case 7344:
		case 7376:
		case 6795:
		case 6870:
		case 6874:
		case 6823:
		case 6816:
		case 7340:
		case 7360:
			Attack = getSummonMaxHit(npcId) - 10;
			return Attack;
		}
		return Attack;
	}

	public static int getSummonDamage(int npcId, boolean moreDamage) {
		int damage = 0;
		damage = 0;
		int addon = 0;
		addon = getSummonAttack(npcId);
		if (moreDamage)
			addon *= 0.15;
		else
			addon *= 0.05;
		switch (npcId) {
		case 7344:
		case 6795:
		case 6870:
		case 6874:
		case 6823:
		case 6816:
		case 7340:
		case 7360:
		case 6842:
		case 6807:
		case 6830:
		case 7342:
		case 6805:
		case 6812:
		case 7358:
		case 7356:
		case 6801:
		case 6868:
		case 7334:
		case 7352:
		case 7368:
		case 6846:
		case 6836:
		case 6872:
		case 7362:
		case 6838:
		case 6832:
		case 7332:
		case 6797:
		case 7348:
		case 7346:
		case 7338:
		case 6844:
		case 6834:
		case 6888:
		case 6886:
		case 6884:
		case 6882:
		case 6880:
		case 6878:
		case 6876:
		case 7366:
		case 7364:
		case 6993:
		case 6819:
		case 6840:
		case 7373:
		case 6828:
		case 6866:
		case 6810:
		case 6799:
		case 7336:
		case 7330:
		case 6864:
		case 7350:
		case 7376:
			damage = Misc.random(getSummonMaxHit(npcId));
			damage += addon;
			return damage;
		}
		return damage;
	}

}
