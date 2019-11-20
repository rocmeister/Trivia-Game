package egc3_lw31.data;

import common.message.AMessageCmd;
import common.message.GroupDataPacket;
import common.message.synchronization.IUpdateMemberListAddData;
import provided.datapacket.IDataPacketID;

/**
 * @author Rocmeister
 *
 */
public class UpdateMemberListAddCmd extends AMessageCmd<IUpdateMemberListAddData> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7334842905502775437L;

	@Override
	public Void apply(IDataPacketID index, GroupDataPacket<IUpdateMemberListAddData> host, Void... params) {
		// TODO Auto-generated method stub
		return null;
	}

}
