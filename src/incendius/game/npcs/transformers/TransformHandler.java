package incendius.game.npcs.transformers;

import incendius.game.npcs.NPC;
import incendius.game.npcs.NPCHandler;
import incendius.game.players.Player;

public class TransformHandler {

	public static void transform(final Player c) {
	}

	public static boolean Strykewyrm(final Player c) {
		final NPC n = NPCHandler.npcs[c.getInstance().npcClickIndex];
		if (n == null) {
			return false;
		}
		if (n.npcType != 9462 && n.npcType != 9464 && n.npcType != 9466) {
			return false;
		}
		return true;
	}

}
