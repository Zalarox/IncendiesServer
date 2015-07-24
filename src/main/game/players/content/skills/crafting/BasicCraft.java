/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.game.players.content.skills.crafting;

import main.game.items.Item;
import main.game.players.Player;
import main.handlers.Skill;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 22/12/11 Time: 00:29 To change
 * this template use File | Settings | File Templates.
 */
public class BasicCraft {

	public static final int RED_CAPE = 1007;
	public static final int BLACK_CAPE = 1019;
	public static final int BLUE_CAPE = 1021;
	public static final int YELLOW_CAPE = 1023;
	public static final int GREEN_CAPE = 1027;
	public static final int PURPLE_CAPE = 1029;
	public static final int ORANGE_CAPE = 1031;

	public static boolean handleItemOnItem(Player player, int itemUsed, int withItem) {
		/* Battlestaves */

		if ((itemUsed == 569 && withItem == 1387) || (withItem == 569 && itemUsed == 1387)) // fire
		{

			if (player.getSkill().getPlayerLevel(Skill.CRAFTING) < 54) {
				player.getPA().sendMessage("You need a crafting level of 54 to do this.");
				return true;
			}
			player.getPA().sendMessage("You use the orb with a staff and craft a battle staff.");
			player.getInventory().removeItem(new Item(itemUsed));
			player.getInventory().removeItem(new Item(withItem));
			player.getInventory().addItem(new Item(1393));
			player.getSkill().addExp(Skill.CRAFTING, 100);
			return true;
		}
		if ((itemUsed == 571 && withItem == 1383) || (withItem == 571 && itemUsed == 1383)) // water
		{

			if (player.getSkill().getPlayerLevel(Skill.CRAFTING) < 58) {
				player.getDialogue().sendStatement("You need a crafting level of 58 to do this.");
				return true;
			}
			player.getPA().sendMessage("You use the orb with a staff and craft a battle staff.");
			player.getInventory().removeItem(new Item(itemUsed));
			player.getInventory().removeItem(new Item(withItem));
			player.getInventory().addItem(new Item(1395));
			player.getSkill().addExp(Skill.CRAFTING, 112.5);
			return true;
		}
		if ((itemUsed == 573 && withItem == 1381) || (withItem == 573 && itemUsed == 1381)) // air
		{

			if (player.getSkill().getPlayerLevel(Skill.CRAFTING) < 62) {
				player.getDialogue().sendStatement("You need a crafting level of 62 to do this.");
				return true;
			}
			player.getPA().sendMessage("You use the orb with a staff and craft a battle staff.");
			player.getInventory().removeItem(new Item(itemUsed));
			player.getInventory().removeItem(new Item(withItem));
			player.getInventory().addItem(new Item(1397));
			player.getSkill().addExp(Skill.CRAFTING, 125);
			return true;
		}
		if ((itemUsed == 575 && withItem == 1385) || (withItem == 575 && itemUsed == 1385)) // Earth
		{

			if (player.getSkill().getPlayerLevel(Skill.CRAFTING) < 66) {
				player.getDialogue().sendStatement("You need a crafting level of 66 to do this.");
				return true;
			}
			player.getPA().sendMessage("You use the orb with a staff and craft a battle staff.");
			player.getInventory().removeItem(new Item(itemUsed));
			player.getInventory().removeItem(new Item(withItem));
			player.getInventory().addItem(new Item(1399));
			player.getSkill().addExp(Skill.CRAFTING, 137.5);
			return true;
		}
		/* coloring capes */
		if (itemUsed == BLACK_CAPE || withItem == BLACK_CAPE) {

			player.getInventory().removeItem(new Item(itemUsed));
			player.getInventory().removeItem(new Item(withItem));
			player.getSkill().addExp(Skill.CRAFTING, 2);
			if (itemUsed == 1763 || withItem == 1763) {
				player.getPA().sendMessage("You colour the cape into a red cape.");
				player.getInventory().addItem(new Item(RED_CAPE));
				return true;
			} else if (itemUsed == 1765 || withItem == 1765) {
				player.getPA().sendMessage("You colour the cape into a red cape.");
				player.getInventory().addItem(new Item(YELLOW_CAPE));
				return true;
			} else if (itemUsed == 1767 || withItem == 1767) {
				player.getPA().sendMessage("You colour the cape into a red cape.");
				player.getInventory().addItem(new Item(BLUE_CAPE));
				return true;
			} else if (itemUsed == 1769 || withItem == 1769) {
				player.getPA().sendMessage("You colour the cape into a red cape.");
				player.getInventory().addItem(new Item(ORANGE_CAPE));
				return true;
			} else if (itemUsed == 1771 || withItem == 1771) {
				player.getPA().sendMessage("You colour the cape into a red cape.");
				player.getInventory().addItem(new Item(GREEN_CAPE));
				return true;
			} else if (itemUsed == 1773 || withItem == 1773) {
				player.getPA().sendMessage("You colour the cape into a red cape.");
				player.getInventory().addItem(new Item(PURPLE_CAPE));
				return true;
			}
		}
		return false;
	}
}
