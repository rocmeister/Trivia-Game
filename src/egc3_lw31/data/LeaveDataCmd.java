package egc3_lw31.data;

import java.rmi.RemoteException;

import common.IGroup;
import common.message.AMessageCmd;
import common.message.GroupDataPacket;
import common.message.ILeaveData;
import provided.datapacket.IDataPacketID;

/**
 * @author Rocmeister
 *
 */
public class LeaveDataCmd extends AMessageCmd<ILeaveData> {
	/**
	 * Generated ID
	 */
	private static final long serialVersionUID = -5482197100459678016L;

	/**
	 * Chatroom 
	 */
	IGroup chatroom;
	/**
	 * Adapter from model to view of the chat room
	 */
//	private IChatRoomModel2ViewAdapter _m2vAdpt;
	
	/**
	 * Constructor
	 * @param room Room to leave
	 */
	public LeaveDataCmd(IGroup room) {
		//c2Madpt = adpt;
		chatroom =  room;
	}

//	@Override
//	public Integer apply(IDataPacketID index, DataPacket<IRemoveRep, IRepresentative> host, Integer... params) {
//		// TODO Auto-generated method stub
//		c2Madpt.appendString("A user has left!\n");
//		chatroom.deleteRep(host.getData().getRepToRemove());
//		return 0;
//	}

	@Override
	public Void apply(IDataPacketID index, GroupDataPacket<ILeaveData> host, Void... params) {
		chatroom.getMembers().remove(host.getSender());
		try {
//			this._m2vAdpt.appendString(host.getSender().getName() + "has just left!");
			this._cmd2ModelAdpt.displayTextMsg(host.getSender().getName() + " has just left!", host.getSender().getName());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
