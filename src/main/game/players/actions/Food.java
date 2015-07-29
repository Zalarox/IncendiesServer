package main.game.players.actions;

import java.util.HashMap;

import main.event.CycleEvent;
import main.event.CycleEventContainer;
import main.event.CycleEventHandler;
import main.game.players.Player;
import main.game.players.content.minigames.DuelArena;
import main.util.Misc;

public class Food {

	public static enum FoodToEat {
	    MANTA(391, 22, "Manta Ray"), 
	    SHARK(385, 20, "Shark"), 
	    LOBSTER(379, 12, "Lobster"), 
	    TROUT(333, 7, "Trout"), 
	    SALMON(329, 9, "Salmon"), 
	    SWORDFISH(373, 14, "Swordfish"), 
	    TUNA(361, 10, "Tuna"), 
	    MONKFISH(7946, 16, "Monkfish"), 
	    SEA_TURTLE(397, 21, "Sea Turtle"), 
	    CAKE(1891, 4, "Cake"), 
	    BASS(365, 13, "Bass"), 
	    COD(339, 7, "Cod"), 
	    POTATO(1942, 1, "Potato"), 
	    BAKED_POTATO(6701, 4, "Baked Potato"), 
	    POTATO_WITH_CHEESE(6705, 16, "Potato with Cheese"), 
	    EGG_POTATO(7056, 16, "Egg Potato"), 
	    CHILLI_POTATO(7054, 14, "Chilli Potato"), 
	    MUSHROOM_POTATO(7058, 20, "Mushroom Potato"), 
	    TUNA_POTATO(7060, 22, "Tuna Potato"), 
	    SHRIMPS(315, 3, "Shrimps"), 
	    HERRING(347, 5, "Herring"), 
	    SARDINE(325, 4, "Sardine"), 
	    CHOCOLATE_CAKE(1897, 5, "Chocolate Cake"), 
	    ANCHOVIES(319, 1, "Anchovies"), 
	    PLAIN_PIZZA(2289, 7, "Plain Pizza"), 
	    MEAT_PIZZA(2293, 8, "Meat Pizza"), 
	    ANCHOVY_PIZZA(2297, 9, "Anchovy Pizza"), 
	    PINEAPPLE_PIZZA(2301, 11, "Pineapple Pizza"), 
	    BREAD(2309, 5, "Bread"), APPLE_PIE(2323, 7, "Apple Pie"), 
	    REDBERRY_PIE(2325, 5, "Redberry Pie"), 
	    MEAT_PIE(2327, 6, "Meat Pie"), 
	    PIKE(351, 8, "Pike"), 
	    POTATO_WITH_BUTTER(6703, 14, "Potato with Butter"), 
	    BANANA(1963, 2, "Banana"), 
	    PEACH(6883, 8, "Peach"), 
	    ORANGE(2108, 2, "Orange"), 
	    PINEAPPLE_RINGS(2118, 2, "Pineapple Rings"), 
	    PINEAPPLE_CHUNKS(2116, 2, "Pineapple Chunks"), 
	    PURPLE_SWEETS(10476, 9, "Purple Sweets"), 
	    ROCKTAIL(15272, 23, "Rocktail"), 
	    HELM_CRAB(18159, 2, "Heim crab"), 
	    RED_EYE(18161, 50, "Red-eye"), 
	    DUSK_EEL(18163, 7, "Dusk eel"), 
	    GIANT_FLATFISH(18165, 10, "Giant flatfish"), 
	    SHORT_FINNED_EEL(18167, 12, "Short-finned eel"), 
	    WEB_SNIPPER(18169, 15, "Web snipper"), 
	    BOULDABASS(18171, 17, "Bouldabass"), 
	    SALVE_EEL(18173, 20, "Salve eel"), 
	    BLUE_CRAB(18175, 22, "Blue crab"), 
	    CAVE_MORAY(18177, 25, "Cave moray");

		public static FoodToEat forId(int id) {
			return food.get(id);
		}

		private int id;
		private int heal;

		private String name;

		public static HashMap<Integer, FoodToEat> food = new HashMap<Integer, FoodToEat>();

		static {
			for (FoodToEat f : FoodToEat.values())
				food.put(f.getId(), f);
		}

		private FoodToEat(int id, int heal, String name) {
			this.id = id;
			this.heal = heal;
			this.name = name;
		}

		public int getHeal() {
			return heal;
		}

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}

	private Player c;

	public Food(Player c) {
		this.c = c;
	}

	public void eat(int id, int slot) {
		if (c.isDead || c.getVariables().constitution <= 0) {
			return;
		}
		
		/**
		 * Can't eat in duels if the relevant rule is toggled.
		 * 
		 * - KeepBotting
		 */
		if (c.getVariables().duelRule[DuelArena.RULE_FOOD]) {
			c.sendMessage("Food has been disabled in this duel!");
			return;
		}
		
		if (System.currentTimeMillis() - c.getVariables().foodDelay >= 1600 && c.getVariables().constitution > 0) {
			c.getVariables().attackTimer += 2;
			c.startAnimation(829);
			c.getItems().deleteItem(id, slot, 1);
			FoodToEat f = FoodToEat.food.get(id);
			if (c.getVariables().constitution < c.getVariables().calculateMaxLifePoints(c)) {
				c.getVariables().constitution += f.getHeal() * 10;
				if (c.getVariables().constitution > c.getVariables().calculateMaxLifePoints(c))
					c.getVariables().constitution = c.getVariables().calculateMaxLifePoints(c);
			}
			c.getPA().refreshSkill(3);
			c.sendMessage("You eat the " + f.getName().toLowerCase() + ".");
			if (id != 10476) {
				c.getVariables().foodDelay = System.currentTimeMillis();
				CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						c.sendMessage("It restores some health.");
						container.stop();
					}

					@Override
					public void stop() {

					}
				}, 2);
			} else if (f.getId() == 15272 && !c.isDead) {
				boolean heal = (c.getVariables().constitution < c.getPA()
						.getLevelForXP(c.getVariables().playerXP[3] + 10));
				if (heal) {
					c.getVariables().constitution += (23 + (Misc.random(10)));
				}
				c.getItems().deleteItem(id, slot, 1);
				c.startAnimation(829);
			}
			c.getVariables().foodDelay = System.currentTimeMillis();
		}
	}

	public boolean isFood(int id) {
		return FoodToEat.food.containsKey(id);
	}
}