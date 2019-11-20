package common.message.synchronization;

import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * This is the well-known DataPacketData type that contains a list of member stubs,
 * meant to update a member's IGroup's member list by adding these members if not
 * already present.
 * 
 * Sending: When receiving a join message, send this message attached with the joining member
 * to everyone in the IGroup's member list to make sure they also have this
 * member. Next, send this message attached with your member list to the joining
 * member in order to make sure they have all your connections as well.
 * 
 * Receiving: Add members in the IGroup member list if not yet present and is 
 * contained in the UpdateMembers list.
 * 
 * 
 * @author Mak Jankovksy
 * @author Stephatine Yang
 */
public interface IUpdateMemberListAddData extends IUpdateMemberListData {
	
	/**
	 * @return A unique IDataPacketID based on the class. The specific type is immaterial.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IUpdateMemberListAddData.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return GetID();
	}

}
