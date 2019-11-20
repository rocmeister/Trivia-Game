package egc3_lw31.data;

import java.rmi.RemoteException;

import common.IGroup;
import common.message.AMessageCmd;
import common.message.GroupDataPacket;
import common.message.status.IStatusProcessingError;
import provided.datapacket.IDataPacketID;

/**
 * This is the well-known status error message cmd that is sent when something goes wrong during
 * processing of a message by a visitor.
 */
public class StatusProcessingErrorCmd extends AMessageCmd<IStatusProcessingError>{

	/**
	 * Generated ID
	 */
	private static final long serialVersionUID = -134359298980886120L;
	
	/**
	 * Chatroom
	 */
	IGroup chatroom;

	/**
	 * Constructor.
	 * @param room Local chatroom
	 */
	public StatusProcessingErrorCmd(IGroup room) {
		chatroom =  room;
	}
	
	@Override
	public Void apply(IDataPacketID index, GroupDataPacket<IStatusProcessingError> host, Void... params) {
		try {
			this._cmd2ModelAdpt.displayTextMsg(host.getSender().getName() + "cannot process this message: " + index.toString(), host.getSender().getName());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

}
