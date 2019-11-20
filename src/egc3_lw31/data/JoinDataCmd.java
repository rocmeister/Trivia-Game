package egc3_lw31.data;

import java.rmi.RemoteException;

import common.IGroup;
import common.message.AMessageCmd;
import common.message.GroupDataPacket;
import common.message.IJoinData;
import provided.datapacket.IDataPacketID;

/**
 * Join command for joining a room.
 */
public class JoinDataCmd extends AMessageCmd<IJoinData>{
	/**
	 * Generated ID
	 */
	private static final long serialVersionUID = 3360188651121025465L;
	//private ICmd2ModelAdapter c2mAdpt;
	/**
	 * Room to join
	 */
	private IGroup room;
	/**
	 * Adapter from model to view of the chat room
	 */
//	private IChatRoomModel2ViewAdapter _m2vAdpt;
	
	/**
	 * Constructor
	 * @param room chatroom
	 */
	public JoinDataCmd(IGroup room) {
		//this.c2mAdpt = adpt;
		this.room = room;
	}
	
//	@Override
//	public Integer apply(IDataPacketID index, DataPacket<IAddRep, IRepresentative> host, Integer... params) {
//		room.addRep(host.getData().getRepToAdd());
//		c2mAdpt.appendString("A new user has joined!\n");
//		return 0;
//	}

	@Override
	public Void apply(IDataPacketID index, GroupDataPacket<IJoinData> host, Void... params) {
		room.getMembers().add(host.getSender());
		try {
			//System.out.println(host.getSender().getName());
			System.out.println("JoinDataCmd: " + host.getSender().getName() + " has just joined!");
			this._cmd2ModelAdpt.displayTextMsg(host.getSender().getName() + " has just joined!", host.getSender().getName());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
