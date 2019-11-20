package egc3_lw31.server.chatroom.model;

import java.util.List;
import java.util.Set;

import common.IGroup;
import common.IMember;
import common.IRemoteConnection;

/**
 * The adapter from the mini to main adapter
 */
public interface IMini2MainAdapter {
	/**
	 * Remove the room from the main view
	 * @param room The room to remove
	 */
	public void removeRoomFromMainView(IGroup room);
	
	/**
	 * Add the room to the user
	 * @param room The room to add
	 */
	public void addRoom2User(IGroup room);
	
	/**
	 * Remove the room for the user
	 * @param room The room to remove
	 */
	public void removeRoom4User(IGroup room);

	/**
	 * Remove the room from the user's list of rooms
	 * @param room The room to remove
	 */
	public void removeRoomFromMainList(IGroup room);

	/**
	 * Creates a new chatroom for each team
	 * @param name Name of new chatroom
	 * @param members IRemoteConnections of members in new chatroom
	 * @return New chatroom created 
	 */
	public IGroup createTeamRoom(String name, List<IRemoteConnection> members);
	
	/**
	 * Update roster of players
	 * @param memberList List of members in game
	 */
	public void updateRoster(List<IMember> memberList);
	
	/**
	 * Gets the roster of players
	 * @return Set of players
	 */
	public Set<IMember> getRoster();
}
