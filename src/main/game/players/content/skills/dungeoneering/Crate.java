package main.game.players.content.skills.dungeoneering;

import main.event.CycleEvent;
import main.event.CycleEventContainer;
import main.event.CycleEventHandler;
import main.game.players.Player;

public class Crate {

	public int item, amount, x, y;
	public boolean stacking;

	public Crate(int item, int amount, int x, int y, boolean stacking) {
		this.item = item;
		this.amount = amount;
		this.x = x;
		this.y = y;
		this.stacking = stacking;
	}

	public int getItem() {
		return item;
	}

	public int getAmount() {
		return amount;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean doesStack() {
		return stacking;
	}

	public static void search(final Player p, final int x, final int y) {
		if (p == null)
			return;
		if (p.party == null)
			return;
		if (p.party.floor == null)
			return;
		if (p.crateId >= 0)
			return;
		p.sendMessage("You search the create..");
		p.found = false;
		p.crateId = -1;
		p.startAnimation(832);
		for (int i = 0; i < p.party.floor.crates.length; i++) {
			if (p.party.floor.crates[i] != null && x == p.party.floor.crates[i].getX()
					&& y == p.party.floor.crates[i].getY()) {
				if (p.party.floor.crates[i].getAmount() > 0) {
					if (!p.party.floor.crates[i].doesStack())
						p.party.floor.crates[i].amount--;
					else
						p.party.floor.crates[i].amount -= Constants.STACKABLE_AMOUNT;
					p.found = true;
					p.crateId = i;
					break;
				}
				p.crateId = i;
				break;
			}
			if (p.crateId == -1)
				p.crateId = 0;
		}
		CycleEventHandler.getSingleton().addEvent(p, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (p.found) {
					p.sendMessage("You find " + (p.party.floor.crates[p.crateId].doesStack() ? "some" : "one") + " "
							+ p.getItems().getItemName(p.party.floor.crates[p.crateId].getItem()) + ".");
					p.getItems().addItem(p.party.floor.crates[p.crateId].getItem(),
							p.party.floor.crates[p.crateId].doesStack() ? 50 : 1);
				} else {
					p.sendMessage("You find nothing.");
				}
				container.stop();
			}

			@Override
			public void stop() {
				p.found = false;
				p.crateId = -1;
			}
		}, 2);
	}

}
