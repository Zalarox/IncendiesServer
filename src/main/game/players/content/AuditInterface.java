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
		
		optionTab("Account", "Alts", "Bank", "Equipment", "Inventory", "", "", "", "", "",
				"", "", "", "", "");
	}
	
	private void menuLine(final String lines, final int ids, final int lineCounter) {
		c.getPA().sendString(lines, 8760 + lineCounter);
		c.getPA().sendString("", 8720 + lineCounter);
		item[0 + lineCounter] = ids;
		writeInterfaceItem(item);
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

	}
	
	void bankInfo() {
		
	}
	
	void equipInfo() {
		
	}
	
	void invInfo() {
		
	}
	
	void altInfo() {
		
	}
	
	public void showInfo(int menu) {
		switch(menu) {
		case 1: clearMenu(); accountInfo();
			break;
		case 2: clearMenu(); altInfo();
			break;
		case 3: clearMenu(); bankInfo();
			break;
		case 4: clearMenu(); equipInfo();
			break;
		case 5: clearMenu(); invInfo();
			break;
		default: clearMenu();
			break;
		}
	}
	

}
