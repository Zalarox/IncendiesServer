package main.net;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * Game protocol encoding class.
 * 
 * @author Graham Edgecombe
 *
 */
public class RS2Encoder implements ProtocolEncoder {

	@SuppressWarnings("unused")
	public void encode(IoSession session, Object in, ProtocolEncoderOutput out) throws Exception {
		Packet p = (Packet) in;

	}

	public void dispose(IoSession session) throws Exception {

	}

	@Override
	public void dispose(org.apache.mina.common.IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void encode(org.apache.mina.common.IoSession arg0, Object arg1, ProtocolEncoderOutput arg2)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
