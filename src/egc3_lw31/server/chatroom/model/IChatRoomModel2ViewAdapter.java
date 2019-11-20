package egc3_lw31.server.chatroom.model;

import java.awt.Component;
import java.util.List;

import common.IGroup;
import common.IRemoteConnection;

/**
 * The adapter from the mini model to view
 */
public interface IChatRoomModel2ViewAdapter {
	/**
	 * Add text to the view
	 * @param t Text to display
	 */
	public void appendString(String t);
	
	/**
	 * Add the GUI component 
	 * @param comp The GUI component to add
	 */
	public void addComponent(Component comp);

	/**
	 * Create a tab for team room
	 * @param name Name of tab for team room
	 * @param teamMbrsConnections Members in new chatroom
	 * @return Chatroom that has been created
	 */
	public IGroup createTeamRoom(String name, List<IRemoteConnection> teamMbrsConnections);
}
