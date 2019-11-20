package common.message.synchronization;

import java.util.List;
import common.IMember;
import provided.datapacket.IDataPacketData;


/**
 * This is the well-known DataPacketData type that contains the request to update a member's local
 * instance of a group's member list with a list of members in order to ensure a fully connected 
 * P2P graph network.
 * 
 * @author Stephanie Yang
 * @author Makoto Jankovsky
 */
public interface IUpdateMemberListData extends IDataPacketData {
	
	/**
	 * @return a list of IMembers that the receiver should check their member list for.
	 */
	public List<IMember> getUpdateMembers();
	
	/**
	 * @return a single member of interest to be updated
	 */
	public IMember getUpdateMember();

}
