package main.game.players.content;

import main.game.players.Player;

public class AuditInterface {
	private Player c;
	private int item[] = new int[40];

	public AuditInterface(Player c) {
		this.c = c;
	}

	private void clearMenu() {
		for (int i = 0; i < 39; i++) {
			item[i] = 0;
		}
		for (int i = 8720; i < 8799; i++) {
			c.getPA().sendString("", i);
		}

		optionTab("Audit Player", "Account", "Account", "Alts", "Bank", "Inventory", "Equipment", "", "", "", "", "", "", "", "");
	}

	private void menuLine(final String lines, final int ids, final int lineCounter) {
		c.getPA().sendString(lines, 8760 + lineCounter);
		c.getPA().sendString("", 8720 + lineCounter);
		if (ids != -1) {
			item[0 + lineCounter] = ids;
			writeInterfaceItem(item);
		}
	}

	private void writeInterfaceItem(int id[]) {
		c.outStream.createFrameVarSizeWord(53);
		c.outStream.writeWord(8847); // 8847
		c.outStream.writeWord(id.length);
		for (int i = 0; i < id.length; i++) {
			c.outStream.writeByte(1);
			if (id[i] > 0) {
				c.outStream.writeWordBigEndianA(id[i] + 1);
			} else {
				c.outStream.writeWordBigEndianA(0);
			}
		}
		c.outStream.endFrameVarSizeWord();
		c.flushOutStream();
	}

	private void optionTab(String title, String currentTab, String op1, String op2, String op3, String op4, String op5,
			String op6, String op7, String op8, String op9, String op10, String op11, String op12, String op13) {
		c.getPA().sendString(title, 8716);
		c.getPA().sendString(currentTab, 8849);
		c.getPA().sendString(op1, 8846);
		c.getPA().sendString(op2, 8823);
		c.getPA().sendString(op3, 8824);
		c.getPA().sendString(op4, 8827);
		c.getPA().sendString(op5, 8837);
		c.getPA().sendString(op6, 8840);
		c.getPA().sendString(op7, 8843);
		c.getPA().sendString(op8, 8859);
		c.getPA().sendString(op9, 8862);
		c.getPA().sendString(op10, 8865);
		c.getPA().sendString(op11, 15303);
		c.getPA().sendString(op12, 15306);
		c.getPA().sendString(op13, 15309);
		c.getPA().showInterface(8714);
	}

	void accountInfo() {
		menuLine("Test information about the player", -1, 0);
		menuLine("Test line 2", -1, 1);
		menuLine("Test line 3", -1, 2);
	}

	void bankInfo() {
		menuLine("Test information about the bank", -1, 0);
		menuLine("Test line 2", -1, 1);
		menuLine("Test line 3", -1, 2);
	}

	void equipInfo() {
		menuLine("Test information about the equip", -1, 0);
		menuLine("Test line 2", -1, 1);
		menuLine("Test line 3", -1, 2);
	}

	void invInfo() {
		menuLine("Test information about the inventory", -1, 0);
		menuLine("Test line 2", -1, 1);
		menuLine("Test line 3", -1, 2);
	}

	void altInfo() {
		menuLine("Test information about the alts", -1, 0);
		menuLine("Test line 2", -1, 1);
		menuLine("Test line 3", -1, 2);
	}

	public void showInfo(int menu) {
		switch (menu) {
		case 1:
			clearMenu();
			accountInfo();
			break;
		case 2:
			clearMenu();
			altInfo();
			break;
		case 3:
			clearMenu();
			bankInfo();
			break;
		case 4:
			clearMenu();
			equipInfo();
			break;
		case 5:
			clearMenu();
			invInfo();
			break;
		default:
			clearMenu();
			break;
		}
	}
	
	public void buttons(int b) {
		switch (b) {
		case 34142: // tab 1
			showInfo(1);
			break;
		case 34119: // tab 2
			showInfo(2);
			break;
		case 34120: // tab 3
			showInfo(3);
			break;
		case 34123: // tab 4
			showInfo(4);
			break;
		case 34133: // tab 5
			showInfo(5);
			break;
		case 34136: // tab 6
			showInfo(6);
			break;
		case 34139: // tab 7
			showInfo(7);
			break;
		case 34155: // tab 8
			showInfo(8);
			break;
		case 34158: // tab 9
			showInfo(9);
			break;
		case 34161: // tab 10
			showInfo(10);
			break;
		case 59199: // tab 11
			showInfo(11);
			break;
		case 59202: // tab 12
			showInfo(12);
			break;
		case 59203: // tab 13
			showInfo(13);
			break;
		}
	}
}
