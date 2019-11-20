package egc3_lw31.client.chatroom.model;

import common.IGroup;

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
}
