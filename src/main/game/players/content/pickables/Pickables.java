package main.game.players.content.pickables;

import main.GameEngine;
import main.event.Task;
import main.game.players.Player;

public class Pickables {

	public static void pickupCabbage(final Player c, final int object, final int obX, final int obY) {
		c.turnPlayerTo(obX, obY);
		if (c.getLastPickable() < 100) {
			return;
		}
		c.startAnimation(827);

		GameEngine.getScheduler().schedule(new Task(2) {
			@Override
			public void execute() {
				GameEngine.objectHandler.createAnObject(c, -1, obX, obY);
				c.getItems().addItem(1965, 1);
				this.stop();
			}
		});
		GameEngine.getScheduler().schedule(new Task(22) {
			@Override
			public void execute() {
				GameEngine.objectHandler.createAnObject(c, object, obX, obY);
				this.stop();
			}
		});
	}

	public static void pickupWheat(final Player c, final int object, final int obX, final int obY) {
		c.turnPlayerTo(obX, obY);
		if (c.getLastPickable() < 100) {
			return;
		}
		c.startAnimation(827);

		GameEngine.getScheduler().schedule(new Task(2) {
			@Override
			public void execute() {
				GameEngine.objectHandler.createAnObject(c, -1, obX, obY);
				c.getItems().addItem(1947, 1);
				this.stop();
			}
		});
		GameEngine.getScheduler().schedule(new Task(22) {
			@Override
			public void execute() {
				GameEngine.objectHandler.createAnObject(c, object, obX, obY);
				this.stop();
			}
		});
	}

	public static void pickupPotato(final Player c, final int object, final int obX, final int obY) {
		c.turnPlayerTo(obX, obY);
		if (c.getLastPickable() < 100) {
			return;
		}
		c.startAnimation(827);

		GameEngine.getScheduler().schedule(new Task(2) {
			@Override
			public void execute() {
				GameEngine.objectHandler.createAnObject(c, -1, obX, obY);
				c.getItems().addItem(1942, 1);
				this.stop();
			}
		});
		GameEngine.getScheduler().schedule(new Task(22) {
			@Override
			public void execute() {
				GameEngine.objectHandler.createAnObject(c, object, obX, obY);
				this.stop();
			}
		});
	}

	public static void pickupOnion(final Player c, final int object, final int obX, final int obY) {
		c.turnPlayerTo(obX, obY);
		if (c.getLastPickable() < 100) {
			return;
		}
		c.startAnimation(827);

		GameEngine.getScheduler().schedule(new Task(2) {
			@Override
			public void execute() {
				GameEngine.objectHandler.createAnObject(c, -1, obX, obY);
				c.getItems().addItem(1957, 1);
				this.stop();
			}
		});
		GameEngine.getScheduler().schedule(new Task(22) {
			@Override
			public void execute() {
				GameEngine.objectHandler.createAnObject(c, object, obX, obY);
				this.stop();
			}
		});
	}

}