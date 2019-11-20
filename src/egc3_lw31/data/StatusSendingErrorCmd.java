package egc3_lw31.data;

import java.rmi.RemoteException;

import common.IGroup;
import common.message.AMessageCmd;
import common.message.GroupDataPacket;
import common.message.status.IStatusSendingError;
import provided.datapacket.IDataPacketID;

/**
 * Command for status error.
 */
public class StatusSendingErrorCmd extends AMessageCmd<IStatusSendingError>{
	/**
	 * Generated ID.
	 */
	private static final long serialVersionUID = -4752472140088879738L;
	/**
	 * Chat room
	 */
	IGroup chatroom;
	/**
	 * Adapter from model to view of the chat room
	 */
	//private IChatRoomModel2ViewAdapter _m2vAdpt;
	
	/**
	 * Constructor.
	 * @param room Local chatroom
	 */
	public StatusSendingErrorCmd(IGroup room) {
		//c2Madpt = adpt;
		chatroom =  room;
		//this._m2vAdpt = m2vAdpt;
	}

	@Override
	public Void apply(IDataPacketID index, GroupDataPacket<IStatusSendingError> host, Void... params) {
		chatroom.getMembers().remove(host.getData().getMember());
		try {
//			this._m2vAdpt.appendString(host.getSender().getName() + "has been removed due to a remote error!");
			this._cmd2ModelAdpt.displayTextMsg(host.getSender().getName() + "has been removed due to a remote error!", host.getSender().getName());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}
};
