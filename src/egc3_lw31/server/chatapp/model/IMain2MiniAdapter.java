package egc3_lw31.server.chatapp.model;

import java.awt.Component;
import java.util.function.Supplier;

import common.IGroup;

/**
 * Adapter from the main to the mini MVC
 */
public interface IMain2MiniAdapter {
	
	/**
	 * Gets the chat room
	 * @return chatroom
	 */
	public IGroup getMiniRoom();
	
	/**
	 * Adds text to the mini view
	 * @param text The text to display
	 */
	public void appendString(String text);
	
	//public void addComponent(JComponent component);

	/**
	 * Adds a GUI component 
	 * @param compFac The component factory supplier
	 */
	public void addComponent(Supplier<Component> compFac);

	/**
	 * Add divide teams and send game functionality
	 */
	void addLobbyComponents();	
}
