package egc3_lw31.util;

import common.IGroup;

/**
 * Wrapper around the chat room
 */
public class ChatRoomWrapper {
	/**
	 * The room wrapped in
	 */
	private IGroup chatroom;
	
	/**
	 * Constructor
	 * @param room Chat room
	 */
	public ChatRoomWrapper(IGroup room) {chatroom = room;}
	
	/**
	 * Gets the chat room
	 * @return the Chat room
	 */
	public IGroup getRoom() {return chatroom;}
	
	@Override
	public String toString() {
		String name = "unknown user";
		try {
			name = chatroom.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}
}
