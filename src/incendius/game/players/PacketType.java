package incendius.game.players;

public interface PacketType {
	public void processPacket(Player c, int packetType, int packetSize);
}
