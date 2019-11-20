package common.message.synchronization;

import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * This is the well-known DataPacketData type that contains a list of member stubs and is meant to update 
 * a member's local instance of a group's member list by removing these members from their list, if present.
 * 
 * Sending: When receiving a leaving message, send this message attached with the joining member
 * to everyone in the IGroup's member list to make sure they also have removed this
 * member.
 * 
 * Receiving: Remove members in the IGroup member list if contained in the UpdateMembers list.
 * 
 * @author Mak Jankovksy
 * @author Stephatine Yang
 */
public interface IUpdateMemberListRemoveData extends IUpdateMemberListData {
	
	/**
	 * @return A unique IDataPacketID based on the class. The specific type is immaterial.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IUpdateMemberListRemoveData.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return GetID();
	}

}
