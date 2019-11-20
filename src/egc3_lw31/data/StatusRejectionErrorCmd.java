package egc3_lw31.data;

import java.rmi.RemoteException;

import common.IGroup;
import common.message.AMessageCmd;
import common.message.GroupDataPacket;
import common.message.status.IStatusRejectionError;
import provided.datapacket.IDataPacketID;

/**
 * Command for if you receive a request to process a well known type.
 */
public class StatusRejectionErrorCmd extends AMessageCmd<IStatusRejectionError> {

	/**
	 * Generated ID
	 */
	private static final long serialVersionUID = -3639818792909512380L;
	
	/**
	 * Chat room
	 */
	IGroup chatroom;
	
	/**
	 * Constructor.
	 * @param room Local chatroom
	 */
	public StatusRejectionErrorCmd(IGroup room) {
		chatroom =  room;
	}
	
	@Override
	public Void apply(IDataPacketID index, GroupDataPacket<IStatusRejectionError> host, Void... params) {
		try {
//			this._m2vAdpt.appendString(host.getSender().getName() + "has sent when a request message for a well known message!");
			this._cmd2ModelAdpt.displayTextMsg(host.getSender().getName() + "has sent when a request message for a well known message!", host.getSender().getName());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

}
