package egc3_lw31.data;

import common.message.AMessageCmd;
import common.message.GroupDataPacket;
import common.message.synchronization.IUpdateMemberListRemoveData;
import provided.datapacket.IDataPacketID;

/**
 * @author Rocmeister
 *
 */
public class UpdateMemberListRemoveCmd extends AMessageCmd<IUpdateMemberListRemoveData> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7443606780710626466L;

	@Override
	public Void apply(IDataPacketID index, GroupDataPacket<IUpdateMemberListRemoveData> host, Void... params) {
		// TODO Auto-generated method stub
		return null;
	}

}
