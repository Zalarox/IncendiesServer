/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package incendius.game.items;

import incendius.handlers.ItemHandler;

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
