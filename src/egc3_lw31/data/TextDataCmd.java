package egc3_lw31.data;

import java.rmi.RemoteException;

import common.message.AMessageCmd;
import common.message.GroupDataPacket;
import common.message.ITxtData;
import provided.datapacket.IDataPacketID;

/**
 * Command for Text Data
 *
 */
public class TextDataCmd extends AMessageCmd<ITxtData>{
	/**
	 * Generated ID
	 */
	private static final long serialVersionUID = 2958039980958378888L;
//	/**
//	 * Adapter from model to view of the chat room
//	 */
//	private IChatRoomModel2ViewAdapter _m2vAdpt;
	
	/**
	 * Constructor 
	 */
//	public TextDataCmd(IChatRoomModel2ViewAdapter m2vAdpt) {
	public TextDataCmd() {
	}
	
//	@Override
//	public Integer apply(IDataPacketID index, DataPacket<IText, IRepresentative> host, Integer... params) {
//		// TODO Auto-generated method stub
//		c2mAdpt.appendString(host.getData().getText());
//		//return host.getData().getText();
//		return 0;
//	}
	
	@Override
	public Void apply(IDataPacketID index, GroupDataPacket<ITxtData> host, Void... params) {
		try {
			this._cmd2ModelAdpt.displayTextMsg(host.getData().getTxt(), host.getSender().getName());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}