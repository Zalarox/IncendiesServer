/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.game.items;

import main.handlers.ItemHandler;

/**
 *
 * @author ArrowzFtw
 * 
 */
public class ItemDefinition {
	public static ItemList forId(int id) {
		return ItemHandler.ItemList[id];
	}
}
