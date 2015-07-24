package main.net;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoFuture;
import org.apache.mina.common.IoFutureListener;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import main.Connection;
import main.Connection.ConnectionType;
import main.Constants;
import main.GameEngine;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.game.players.PlayerSave;
import main.processors.GameProcessor;
import main.util.ISAACRandomGen;
import main.util.Misc;

/**
 * Login protocol decoder.
 * 
 * @author Graham
 * @author Ryan / Lmctruck30 <- login Protocol fixes
 *
 */
public class RS2LoginProtocolDecoder extends CumulativeProtocolDecoder {

	/**
	 * Parses the data in the provided byte buffer and writes it to
	 * <code>out</code> as a <code>Packet</code>.
	 *
	 * @param session
	 *            The IoSession the data was read from
	 * @param in
	 *            The buffer
	 * @param out
	 *            The decoder output stream to which to write the
	 *            <code>Packet</code>
	 * @return Whether enough data was available to create a packet
	 */
	@Override
	public boolean doDecode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) {
		synchronized (session) {
			Object loginStageObj = session.getAttribute("LOGIN_STAGE");
			int loginStage = 0;
			if (loginStageObj != null) {
				loginStage = (Integer) loginStageObj;
			}
			// Logger.log("recv login packet, stage: "+loginStage);
			switch (loginStage) {
			case 0:
				if (2 <= in.remaining()) {
					int protocol = in.get() & 0xff;
					@SuppressWarnings("unused")
					int nameHash = in.get() & 0xff;
					if (protocol == 14) {
						long serverSessionKey = ((long) (java.lang.Math.random() * 99999999D) << 32)
								+ (long) (java.lang.Math.random() * 99999999D);
						StaticPacketBuilder s1Response = new StaticPacketBuilder();
						s1Response.setBare(true).addBytes(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 }).addByte((byte) 0)
								.addLong(serverSessionKey);
						session.setAttribute("SERVER_SESSION_KEY", serverSessionKey);
						session.write(s1Response.toPacket());
						session.setAttribute("LOGIN_STAGE", 1);
					}
					return true;
				} else {
					in.rewind();
					return false;
				}
			case 1:
				@SuppressWarnings("unused")
				int loginType = -1, loginPacketSize = -1, loginEncryptPacketSize = -1;
				if (2 <= in.remaining()) {
					loginType = in.get() & 0xff; // should be 16 or 18
					loginPacketSize = in.get() & 0xff;
					loginEncryptPacketSize = loginPacketSize - (36 + 1 + 1 + 2);
					if (loginPacketSize <= 0 || loginEncryptPacketSize <= 0) {
						System.out.println("Zero or negative login size.");
						session.close();
						return false;
					}
				} else {
					in.rewind();
					return false;
				}
				if (loginPacketSize <= in.remaining()) {
					int magic = in.get() & 0xff;
					int version = in.getUnsignedShort();
					if (magic != 255) {
						// System.out.println("Wrong magic id.");
						session.close();
						return false;
					}
					if (version != 1) {
						// Dont Add Anything
					}
					@SuppressWarnings("unused")
					int lowMem = in.get() & 0xff;
					for (int i = 0; i < 9; i++) {
						in.getInt();
					}
					loginEncryptPacketSize--;
					if (loginEncryptPacketSize != (in.get() & 0xff)) {
						System.out.println("Encrypted size mismatch.");
						session.close();
						return false;
					}
					if ((in.get() & 0xff) != 10) {
						System.out.println("Encrypted id != 10.");
						session.close();
						return false;
					}
					long clientSessionKey = in.getLong();
					long serverSessionKey = in.getLong();
					int uid = in.getInt();
					if (uid != (1945275396 >> 2) * 2) {
						session.close();
						return false;
					}
					String name = readRS2String(in);
					String pass = readRS2String(in);
					boolean registerState = false;// in.get() == 1;
					String identity = readRS2String(in);
					int sessionKey[] = new int[4];
					sessionKey[0] = (int) (clientSessionKey >> 32);
					sessionKey[1] = (int) clientSessionKey;
					sessionKey[2] = (int) (serverSessionKey >> 32);
					sessionKey[3] = (int) serverSessionKey;
					ISAACRandomGen inC = new ISAACRandomGen(sessionKey);
					for (int i = 0; i < 4; i++)
						sessionKey[i] += 50;
					ISAACRandomGen outC = new ISAACRandomGen(sessionKey);
					load(session, identity, registerState, uid, name, pass, inC, outC, version);
					// WorkerThread.load(session, name, pass, inC, outC);
					session.getFilterChain().remove("protocolFilter");
					session.getFilterChain().addLast("protocolFilter",
							new ProtocolCodecFilter(new GameCodecFactory(inC)));
					return true;
				} else {
					in.rewind();
					return false;
				}
			}
		}
		return false;
	}

	@SuppressWarnings("unused")
	private synchronized void load(final IoSession session, final String identity, boolean register, final int uid,
			String name, String pass, final ISAACRandomGen inC, ISAACRandomGen outC, int version) {
		session.setAttribute("opcode", -1);
		session.setAttribute("size", -1);
		int loginDelay = 1;
		int returnCode = 2;
		name = name.trim();
		name = Misc.capitalize(name);
		pass = pass.toLowerCase();

		/* if(!register) { */
		if (!name.matches("[A-Za-z0-9 ]+")) {
			returnCode = 4;
		}

		if (name.length() > 12) {
			returnCode = 8;
		}
		/* } */
		Player cl = new Player(session, -1);
		cl.playerName = name;
		cl.playerName2 = cl.playerName;
		cl.playerPass = pass;
		cl.setInStreamDecryption(inC);
		cl.setOutStreamDecryption(outC);
		cl.outStream.packetEncryption = outC;

		cl.getVariables().saveCharacter = false;

		char first = name.charAt(0);
		cl.getVariables().properName = Character.toUpperCase(first) + name.substring(1, name.length());
		cl.identityPunishment = identity;

		/* if(!register) { */
		if (Connection.containsConnection(cl.playerName, ConnectionType.forName("BAN"), false)
				|| Connection.containsConnection(cl.playerName, ConnectionType.forName("IDENTITY_BAN"), false)) {
			returnCode = 4;
			/* } */

			if (PlayerHandler.isPlayerOn(name)) {
				returnCode = 5;
			}

			if (PlayerHandler.playerCount >= Constants.MAX_PLAYERS) {
				returnCode = 7;
			}
		}
		if (GameEngine.UpdateServer) {
			returnCode = 14;
		}

		if (returnCode == 2) {
			int load = PlayerSave.loadGame(cl, cl.playerName, cl.playerPass);
			if (load == 0) {
				/* if(register) { */
				cl.getVariables().addStarter = true;
				/*
				 * returnCode = 24; } else returnCode = 23;
				 */
			}
			if (load == 3) {
				returnCode = /* register ? 22 : */ 3;
				cl.getVariables().saveFile = false;
			} else {
				for (int i = 0; i < cl.getVariables().playerEquipment.length; i++) {
					if (cl.getVariables().playerEquipment[i] == 0) {
						cl.getVariables().playerEquipment[i] = -1;
						cl.getVariables().playerEquipmentN[i] = 0;
					}
				}
				if (!GameEngine.playerHandler.newPlayerPlayer(cl)/* && !register */) {
					returnCode = 7;
					cl.getVariables().saveFile = false;
				} else {
					cl.getVariables().saveFile = true;
				}
			}
			/* if(!register && returnCode != 23) */
			GameProcessor.startPlayerProcess(cl.playerId);
		}

		cl.packetType = -1;
		cl.packetSize = 0;

		StaticPacketBuilder bldr = new StaticPacketBuilder();
		bldr.setBare(true);
		bldr.addByte((byte) returnCode);
		if (returnCode == 2) {
			cl.getVariables().saveCharacter = true;
			bldr.addByte((byte) cl.getVariables().playerRights);
		} else if (returnCode == 21) {
			bldr.addByte((byte) loginDelay);
		} else {
			bldr.addByte((byte) 0);
		}
		cl.isActive = true;
		bldr.addByte((byte) 0);
		Packet pkt = bldr.toPacket();
		final Player fcl = cl;
		session.setAttachment(cl);
		session.write(pkt).addListener(new IoFutureListener() {
			@Override
			public void operationComplete(IoFuture arg0) {
				session.getFilterChain().remove("protocolFilter");
				session.getFilterChain().addFirst("protocolFilter", new ProtocolCodecFilter(new GameCodecFactory(inC)));
			}
		});
	}

	private synchronized String readRS2String(ByteBuffer in) {
		StringBuilder sb = new StringBuilder();
		byte b;
		while ((b = in.get()) != 10) {
			sb.append((char) b);
		}
		return sb.toString();
	}

	/**
	 * Releases the buffer used by the given session.
	 *
	 * @param session
	 *            The session for which to release the buffer
	 * @throws Exception
	 *             if failed to dispose all resources
	 */
	@Override
	public void dispose(IoSession session) throws Exception {
		super.dispose(session);
	}

}
