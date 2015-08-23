package main.game.players.actions;

import java.util.concurrent.CopyOnWriteArrayList;

import main.Constants;
import main.event.CycleEvent;
import main.event.CycleEventContainer;
import main.event.CycleEventHandler;
import main.game.items.GameItem;
import main.game.items.ItemLoader;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.util.Misc;

public class TradeHandler {

	private Player p;

	public TradeHandler(Player Player) {
		this.p = Player;
	}

	/**
	 * Trading
	 **/

	public CopyOnWriteArrayList<GameItem> offeredItems = new CopyOnWriteArrayList<GameItem>();

	/**
	 * Checks if Both players are able to trade
	 * 
	 * @param p
	 * @param target
	 * @return
	 */
	public boolean ableToTrade(Player target) {
		if (p.getInstance().inTrade) {
			declineTrade(false);
			return false;
		}
		if (p.isBusy(target)) {
			p.sendMessage("Other player is currently busy.");
			return false;
		}
		if (target.getInstance().isBanking) {
			p.sendMessage("Other player is busy at the moment.");
			return false;
		}
		if (p.getInstance().isBanking) {
			p.sendMessage("Close the bank before trading someone.");
			return false;
		}
		if (target.getInstance().isShopping) {
			p.sendMessage("Other player is busy at the moment.");
			return false;
		}
		if (p.getInstance().isShopping) {
			p.sendMessage("Close the shop before trading someone.");
			return false;
		}
		if (System.currentTimeMillis() - p.getInstance().logoutDelay < 10000) {
			p.sendMessage("You cannot trade anyone while in combat!");
			return false;
		}
		if (System.currentTimeMillis() - target.getInstance().logoutDelay < 10000) {
			p.sendMessage("Other player is busy at the moment.");
			return false;
		}
		return true;
	}

	/**
	 * Requests a trade
	 * 
	 * @param id
	 */
	public void requestTrade(int id, boolean face) {
		try {
			final Player o = PlayerHandler.players[id];
			if (o == null || (id == p.playerId) || !ableToTrade(o))
				return;
			if (face)
				p.turnPlayerTo(o.getX(), o.getY());
			if (Constants.goodDistance(p.getX(), p.getY(), o.getX(), o.getY(), 1)) {
				p.getInstance().tradeWith = id;
				if (!p.getInstance().inTrade && o.getInstance().tradeRequested
						&& o.getInstance().tradeWith == p.playerId) {
					p.getTradeHandler().openTrade();
					o.getTradeHandler().openTrade();
				} else if (!p.getInstance().inTrade) {

					p.getInstance().tradeRequested = true;
					p.sendMessage("Sending trade request...");
					
					/**
					 * Support for display names during trade requests.
					 * 
					 *  - Branon McClellan (KeepBotting)
					 */
					o.sendMessage(p.getDisplayName() + ":tradereq:");			
				}
			} else {
				p.getInstance().headingTowardsPlayer = true;
				CycleEventHandler.getInstance().addEvent(p, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (!p.getInstance().headingTowardsPlayer)
							container.stop();
						if (Constants.goodDistance(p.getX(), p.getY(), o.getX(), o.getY(), 1)) {
							if (o != null && p != null)
								requestTrade(o.playerId, false);
							container.stop();
						}
					}

					@Override
					public void stop() {
						if (p != null)
							p.getInstance().headingTowardsPlayer = false;
					}
				}, 1);
			}
		} catch (Exception e) {
			Misc.println("Error requesting trade.");
		}
	}

	/**
	 * Displays the items Wealth
	 * 
	 * @param p
	 */
	public void displayWealthInfo(Player p) {
		Player o = PlayerHandler.players[p.getInstance().tradeWith];
		p.getInstance().playerTradeWealth = 0;
		o.getInstance().playerTradeWealth = 0;
		for (GameItem item : p.getTradeHandler().offeredItems) {
			p.getInstance().playerTradeWealth += (p.getShops().getItemShopValue(item.id) * item.amount);
		}

		for (GameItem item : o.getTradeHandler().offeredItems) {
			o.getInstance().playerTradeWealth += (o.getShops().getItemShopValue(item.id) * item.amount);
		}

		int playerDifference1 = (p.getInstance().playerTradeWealth - o.getInstance().playerTradeWealth);
		int playerDifference2 = (o.getInstance().playerTradeWealth - p.getInstance().playerTradeWealth);

		boolean player1HasMore = (playerDifference1 > playerDifference2);
		boolean equalsSame = (p.getInstance().playerTradeWealth == o.getInstance().playerTradeWealth);

		if (p.getInstance().playerTradeWealth < -1) {
			p.getInstance().playerTradeWealth = 2147483647;
		}
		if (o.getInstance().playerTradeWealth < -1) {
			o.getInstance().playerTradeWealth = 2147483647;
		}

		String playerValue1 = "" + p.getPA().getTotalAmount(p.getInstance().playerTradeWealth) + " ("
				+ Misc.format(p.getInstance().playerTradeWealth) + ")";
		String playerValue2 = "" + p.getPA().getTotalAmount(o.getInstance().playerTradeWealth) + " ("
				+ Misc.format(o.getInstance().playerTradeWealth) + ")";

		if (p.getInstance().playerTradeWealth < -1) {
			playerValue1 = "+" + playerValue1;
		}
		if (o.getInstance().playerTradeWealth < -1) {
			playerValue2 = "+" + playerValue2;
		}
		if (equalsSame) {
			p.getPA().sendFrame126("@yel@Equal Trade", 53504);
			o.getPA().sendFrame126("@yel@Equal Trade", 53504);
		} else if (player1HasMore) {
			p.getPA().sendFrame126("-@red@" + p.getPA().getTotalAmount(playerDifference1) + " ("
					+ Misc.format(playerDifference1) + ")", 53504);
			o.getPA().sendFrame126("+@yel@" + o.getPA().getTotalAmount(playerDifference1) + " ("
					+ Misc.format(playerDifference1) + ")", 53904);
		} else if (!player1HasMore) {
			p.getPA().sendFrame126("+@yel@" + p.getPA().getTotalAmount(playerDifference2) + " ("
					+ Misc.format(playerDifference2) + ")", 53504);
			o.getPA().sendFrame126("-@red@" + o.getPA().getTotalAmount(playerDifference2) + " ("
					+ Misc.format(playerDifference2) + ")", 53504);
		}
		p.getPA().sendFrame126(playerValue1, 53506);
		p.getPA().sendFrame126(playerValue2, 53507);
		o.getPA().sendFrame126(playerValue2, 53506);
		o.getPA().sendFrame126(playerValue1, 53507);
		p.getPA().sendFrame126(Misc.formatName(o.getDisplayName()) + " has\\n " + o.getItems().freeSlots()
				+ " free\\n inventory slots.", 53505);
		o.getPA().sendFrame126(Misc.formatName(p.getDisplayName()) + " has\\n " + p.getItems().freeSlots()
				+ " free\\n inventory slots.", 53505);
	}

	/**
	 * Opens the trade menu
	 */
	public void openTrade() {
		Player o = PlayerHandler.players[p.getInstance().tradeWith];

		if (o == null) {
			return;
		}
		offeredItems.clear();
		o.getTradeHandler().offeredItems.clear();
		p.turnPlayerTo(o.getX(), o.getY());
		p.setBusy(true);
		p.getInstance().inTrade = true;
		p.getInstance().canOffer = true;
		p.getInstance().tradeStatus = 1;
		p.getInstance().tradeRequested = false;
		p.getInstance().tradeConfirmed = false;
		p.getInstance().tradeConfirmed2 = false;
		p.getInstance().tradeAccepted = false;
		p.getInstance().acceptedTrade = false;
		p.getItems().resetItems(3322);
		resetTItems(3415);
		resetOTItems(3416);
		String out = o.playerName;

		if (o.getInstance().playerRights == 1) {
			out = "@cr1@" + out;
		} else if (o.getInstance().playerRights == 2) {
			out = "@cr2@" + out;
		}
		p.getPA().sendString(
				"Trading with: " + o.getDisplayName() + " who has @gre@" + o.getItems().freeSlots() + " free slots", 3417);
		displayWealthInfo(p);
		p.getPA().sendString("", 3431);
		p.getPA().sendString("Are you sure you want to make this trade?", 3535);
		p.getPA().sendFrame248(3323, 3321);
	}

	public void resetTItems(int WriteFrame) {
		// synchronized(p) {
		p.getOutStream().createFrameVarSizeWord(53);
		p.getOutStream().writeWord(WriteFrame);
		int len = offeredItems.toArray().length;
		int current = 0;
		p.getOutStream().writeWord(len);
		for (GameItem item : offeredItems) {
			if (item.amount > 254) {
				p.getOutStream().writeByte(255);
				p.getOutStream().writeDWord_v2(item.amount);
			} else {
				p.getOutStream().writeByte(item.amount);
			}
			p.getOutStream().writeWordBigEndianA(item.id + 1);
			current++;
		}
		if (current < 27) {
			for (int i = current; i < 28; i++) {
				p.getOutStream().writeByte(1);
				p.getOutStream().writeWordBigEndianA(-1);
			}
		}
		p.getOutStream().endFrameVarSizeWord();
		p.flushOutStream();
		// }
	}

	public boolean fromTrade(int itemID, int fromSlot, int amount) {
		Player o = PlayerHandler.players[p.getInstance().tradeWith];
		if (o == null) {
			return false;
		}
		try {
			if (!p.getInstance().inTrade || !p.getInstance().canOffer) {
				declineTrade(false);
				return false;
			}
			if (!p.getItems().playerHasItem(itemID, amount)) // Dupe fix
				return false;

			if (itemID != p.getInstance().playerItems[fromSlot] - 1) { // Dupe
																		// fix
				return false;
			}
			p.getInstance().tradeConfirmed = false;
			o.getInstance().tradeConfirmed = false;
			if (!ItemLoader.isStackable(itemID)) {
				for (int a = 0; a < amount && a < 28; a++) {
					for (GameItem item : offeredItems) {
						if (item.id == itemID) {
							if (!item.stackable) {
								offeredItems.remove(item);
								p.getItems().addItem(itemID, 1);
								o.getPA().sendString("Trading with: " + p.getDisplayName() + " who has @gre@"
										+ p.getItems().freeSlots() + " free slots", 3417);
							} else {
								if (item.amount > amount) {
									item.amount -= amount;
									p.getItems().addItem(itemID, amount);
									o.getPA().sendString("Trading with: " + p.getDisplayName() + " who has @gre@"
											+ p.getItems().freeSlots() + " free slots", 3417);
								} else {
									amount = item.amount;
									offeredItems.remove(item);
									p.getItems().addItem(itemID, amount);
									o.getPA().sendString("Trading with: " + p.getDisplayName() + " who has @gre@"
											+ p.getItems().freeSlots() + " free slots", 3417);
								}
							}
							break;
						}
						o.getPA().sendString("Trading with: " + p.getDisplayName() + " who has @gre@"
								+ p.getItems().freeSlots() + " free slots", 3417);
						p.getInstance().tradeConfirmed = false;
						o.getInstance().tradeConfirmed = false;
						p.getItems().resetItems(3322);
						resetTItems(3415);
						o.getTradeHandler().resetOTItems(3416);
						displayWealthInfo(p);
						p.getPA().sendString("", 3431);
						o.getPA().sendString("", 3431);
					}
				}
			}
			for (GameItem item : offeredItems) {
				if (item.id == itemID) {
					if (!item.stackable) {
					} else {
						if (item.amount > amount) {
							item.amount -= amount;
							p.getItems().addItem(itemID, amount);
							o.getPA().sendString("Trading with: " + p.getDisplayName() + " who has @gre@"
									+ p.getItems().freeSlots() + " free slots", 3417);
						} else {
							amount = item.amount;
							offeredItems.remove(item);
							p.getItems().addItem(itemID, amount);
							o.getPA().sendString("Trading with: " + p.getDisplayName() + " who has @gre@"
									+ p.getItems().freeSlots() + " free slots", 3417);
						}
					}
					break;
				}
			}

			o.getPA().sendString(
					"Trading with: " + p.getDisplayName() + " who has @gre@" + p.getItems().freeSlots() + " free slots",
					3417);
			p.getInstance().tradeConfirmed = false;
			o.getInstance().tradeConfirmed = false;
			p.getItems().resetItems(3322);
			resetTItems(3415);
			o.getTradeHandler().resetOTItems(3416);
			displayWealthInfo(p);
			p.getPA().sendString("", 3431);
			o.getPA().sendString("", 3431);
		} catch (Exception e) {
		}
		return true;
	}

	public boolean tradeable(int itemId) {
		if (itemId == 995)
			return true;
		for (int i : Constants.UNTRADEABLE_ITEM) {
			if (i == itemId) {
				return false;
			}
		}
		return true;
	}

	public boolean tradeItem(int itemID, int fromSlot, int amount) {
		Player o = PlayerHandler.players[p.getInstance().tradeWith];
		if (o == null) {
			return false;
		}
		if (!tradeable(itemID)) {
			p.sendMessage("You can't trade this item.");
			return false;
		}
		p.getInstance().tradeConfirmed = false;
		o.getInstance().tradeConfirmed = false;
		if (!ItemLoader.isStackable(itemID) && !ItemLoader.isNote(itemID)) {
			for (int a = 0; a < amount && a < 28; a++) {
				if (p.getItems().playerHasItem(itemID, 1)) {
					offeredItems.add(new GameItem(itemID, 1));
					p.getItems().deleteItem(itemID, p.getItems().getItemSlot(itemID), 1);
					o.getPA().sendString("Trading with: " + p.getDisplayName() + " who has @gre@" + p.getItems().freeSlots()
							+ " free slots", 3417);
				}
			}
			o.getPA().sendString(
					"Trading with: " + p.getDisplayName() + " who has @gre@" + p.getItems().freeSlots() + " free slots",
					3417);
			p.getItems().resetItems(3322);
			resetTItems(3415);
			o.getTradeHandler().resetOTItems(3416);
			displayWealthInfo(p);
			p.getPA().sendString("", 3431);
			o.getPA().sendString("", 3431);
		}
		if (p.getItems().getItemCount(itemID) < amount) {
			amount = p.getItems().getItemCount(itemID);
			if (amount == 0)
				return false;
		}

		if (!p.getInstance().inTrade || !p.getInstance().canOffer) {
			declineTrade(false);
			return false;
		}
		if (!p.getItems().playerHasItem(itemID, amount))
			return false;

		if (itemID != p.getInstance().playerItems[fromSlot] - 1) {
			return false;
		}

		if (ItemLoader.isStackable(itemID) || ItemLoader.isNote(itemID)) {
			boolean inTrade = false;
			for (GameItem item : offeredItems) {
				if (item.id == itemID) {
					inTrade = true;
					item.amount += amount;
					p.getItems().deleteItem2(itemID, amount);
					o.getPA().sendString("Trading with: " + p.getDisplayName() + " who has @gre@" + p.getItems().freeSlots()
							+ " free slots", 3417);
					break;
				}
			}

			if (!inTrade) {
				offeredItems.add(new GameItem(itemID, amount));
				p.getItems().deleteItem2(itemID, amount);
				o.getPA().sendFrame126(
						"Trading with: " + p.getDisplayName() + " who has @gre@" + p.getItems().freeSlots() + " free slots",
						3417);
			}
		}
		o.getPA().sendString(
				"Trading with: " + p.getDisplayName() + " who has @gre@" + p.getItems().freeSlots() + " free slots", 3417);
		p.getItems().resetItems(3322);
		resetTItems(3415);
		o.getTradeHandler().resetOTItems(3416);
		displayWealthInfo(p);
		p.getPA().sendString("", 3431);
		o.getPA().sendString("", 3431);
		return true;
	}

	/**
	 * Resets a trade
	 */
	public void resetTrade() {
		offeredItems.clear();
		p.getInstance().inTrade = false;
		p.getInstance().tradeWith = 0;
		p.getInstance().canOffer = true;
		p.getInstance().tradeConfirmed = false;
		p.getInstance().tradeConfirmed2 = false;
		p.getInstance().tradeAccepted = false;
		p.getInstance().acceptedTrade = false;
		p.getPA().removeAllWindows();
		p.getInstance().tradeResetNeeded = false;
		p.getPA().sendString("Are you sure you want to make this trade?", 3535);
	}

	public void declineTrade(boolean r) {
		p.getInstance().tradeStatus = 0;
		declineTrade(true, r);
	}

	public void decline(boolean r) {
		p.getInstance().tradeStatus = 0;
		declineTrade(false, r);
	}

	public void declineTrade(boolean tellOther, boolean r) {
		if (p.getInstance().tradeAccepted)
			return;
		if (!r && p != null) {
			p.getPA().removeAllWindows();
		}
		Player o = PlayerHandler.players[p.getInstance().tradeWith];
		if (o == null) {
			return;
		}
		if (p == null) {
			return;
		}
		if (tellOther) {
			if (o != null) {
				o.getTradeHandler().decline(true);
				o.getTradeHandler().p.getPA().removeAllWindows();
			}
		}

		for (GameItem item : offeredItems) {
			if (item.amount < 1) {
				continue;
			}
			if (item.stackable) {
				p.getItems().addItem(item.id, item.amount);
			} else {
				for (int i = 0; i < item.amount; i++) {
					p.getItems().addItem(item.id, 1);
				}
			}
		}
		p.getInstance().canOffer = true;
		p.getInstance().tradeConfirmed = false;
		p.getInstance().tradeConfirmed2 = false;
		p.getInstance().tradeAccepted = false;
		offeredItems.clear();
		p.getInstance().inTrade = false;
		p.getInstance().tradeWith = 0;
		if (o != null && o.getInstance().tradeWith == p.playerId && !p.getInstance().closed) {
			p.sendMessage("Other player has declined the trade.");
			o.getInstance().tradeAccepted = false;
		}
		p.getInstance().closed = false;
	}

	public void resetOTItems(int WriteFrame) {
		Player o = PlayerHandler.players[p.getInstance().tradeWith];
		if (o == null) {
			return;
		}
		p.getOutStream().createFrameVarSizeWord(53);
		p.getOutStream().writeWord(WriteFrame);
		int len = o.getTradeHandler().offeredItems.toArray().length;
		int current = 0;
		p.getOutStream().writeWord(len);
		for (GameItem item : o.getTradeHandler().offeredItems) {
			if (item.amount > 254) {
				p.getOutStream().writeByte(255); // item's stack count. if over
													// 254, write byte 255
				p.getOutStream().writeDWord_v2(item.amount);
			} else {
				p.getOutStream().writeByte(item.amount);
			}
			p.getOutStream().writeWordBigEndianA(item.id + 1); // item id
			current++;
		}
		if (current < 27) {
			for (int i = current; i < 28; i++) {
				p.getOutStream().writeByte(1);
				p.getOutStream().writeWordBigEndianA(-1);
			}
		}
		p.getOutStream().endFrameVarSizeWord();
		p.flushOutStream();
	}

	public void confirmScreen() {
		Player o = PlayerHandler.players[p.getInstance().tradeWith];
		if (o == null) {
			return;
		}
		p.getInstance().canOffer = false;
		p.getItems().resetItems(3214);
		String SendTrade = "Absolutely nothing!";
		String SendAmount = "";
		int Count = 0;
		for (GameItem item : offeredItems) {
			if (item.id > 0) {
				if (item.amount >= 1000 && item.amount < 1000000) {
					SendAmount = "@cya@" + (item.amount / 1000) + "K @whi@(" + Misc.format(item.amount) + ")";
				} else if (item.amount >= 1000000) {
					SendAmount = "@gre@" + (item.amount / 1000000) + " million @whi@(" + Misc.format(item.amount) + ")";
				} else {
					SendAmount = "" + Misc.format(item.amount);
				}

				if (Count == 0) {
					SendTrade = p.getItems().getItemName(item.id);
				} else {
					SendTrade = SendTrade + "\\n" + p.getItems().getItemName(item.id);
				}

				if (item.stackable) {
					SendTrade = SendTrade + " x " + SendAmount;
				}
				Count++;
			}
		}

		p.getPA().sendString(SendTrade, 3557);
		SendTrade = "Absolutely nothing!";
		SendAmount = "";
		Count = 0;

		for (GameItem item : o.getTradeHandler().offeredItems) {
			if (item.id > 0) {
				if (item.amount >= 1000 && item.amount < 1000000) {
					SendAmount = "@cya@" + (item.amount / 1000) + "K @whi@(" + Misc.format(item.amount) + ")";
				} else if (item.amount >= 1000000) {
					SendAmount = "@gre@" + (item.amount / 1000000) + " million @whi@(" + Misc.format(item.amount) + ")";
				} else {
					SendAmount = "" + Misc.format(item.amount);
				}
				if (Count == 0) {
					SendTrade = p.getItems().getItemName(item.id);
				} else {
					SendTrade = SendTrade + "\\n" + p.getItems().getItemName(item.id);
				}
				if (item.stackable) {
					SendTrade = SendTrade + " x " + SendAmount;
				}
				Count++;
			}
		}
		p.getPA().sendString(SendTrade, 3558);
		// TODO: find out what 197 does eee 3213
		p.getPA().sendFrame248(3443, 197);
	}

	@SuppressWarnings("unused")
	public void giveItems() {
		Player o = PlayerHandler.players[p.getInstance().tradeWith];
		
		if (o == null) {
			return;
		}
		
		try {
			for (GameItem item : o.getTradeHandler().offeredItems) {
				if (item.id > 0) {
					p.getInventory().add(item.id, item.amount);
				}
					
			}
			
			for (GameItem item : o.getTradeHandler().offeredItems)
				p.getPA().removeAllWindows();
			
			p.getInstance().tradeResetNeeded = true;
			
			CycleEventHandler.getInstance().addEvent(this, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (p.getInstance().inTrade && p.getInstance().tradeResetNeeded) {
						Player o = PlayerHandler.players[p.getInstance().tradeWith];
						
						if (o != null) {
							
							if (o.getInstance().tradeResetNeeded) {
								p.getTradeHandler().resetTrade();
								o.getTradeHandler().resetTrade();
								container.stop();
								
							} else {
								container.stop();
							}
							
						} else {
							container.stop();
						}
					} else {
						container.stop();
					}
				}

				@Override
				public void stop() {
					p.getInstance().tradeResetNeeded = false;
				}
			}, 1);
		} catch (Exception e) {
		}
	}
}
