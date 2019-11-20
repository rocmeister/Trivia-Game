package common;

import java.awt.Component;
import java.util.function.Supplier;

import provided.datapacket.IDataPacketData;
import provided.mixedData.MixedDataKey;

/**
 * This is the adapter that must be set by the well-known InstallNewCommandCmd to enable some incoming
 * ICmdData to communicate with the local system of the receiver. It is up to the user of this interface
 * to define how much access to the "local system" the command has.
 * 
 * @author David Senter
 * @author Joel Webb
 */
public interface ICmd2ModelAdapter {
	/**
	 * This method ensures that the new command has a way to display its output on the local
	 * system. 
	 * 
	 * @param compFac - The component factory that commands passes to the local system, to be
	 *      			displayed however the local system chooses.
	 */
	public void addComponentFactory(Supplier<Component> compFac);
	
	
	/**
	 * This method ensures that the new command has a way to display a text message on the local UI.
	 * Output is expected to be on the same component as the text from an ITxtData message would appear.
	 * 
	 * @param msg Text message to display
	 * @param identifier Indicates what process is generating the text (e.g. sender name)
	 */
	public void displayTextMsg(String msg, String identifier);
	
	/**
	 * Gets the name of the local IMember.
	 * Returns the same value that the IMember.getName() returns for the local IMember RMI server object.
	 * @return The username of this IMember.
	 */
	public String getLocalMemberName();
	
	/**
	 * Retrieves the name of the current room/group that the IMember is in.
	 * Returns the same value that the IGroup.getName() returns for that group object.
	 * @return The group name of this IMember's associated group.
	 */
	public String getLocalGroupName();

	/**
	 * Sends the message to the specified member. This method must be non-blocking, i.e. the 
	 * sending of the message must take place on a different thread than the calling thread.
	 * @param msg to be sent
	 * @param member to be sent to
	 * 
	 */
	public void sendMessageToMember(IDataPacketData msg, IMember member);
	
	/**
	 * Sends the message to the entire group that this member is in. 
	 * @param msg to be sent
	 */
	public void sendMessageToGroup(IDataPacketData msg);
	
	/**
	 * Gets the value from the mixed data dictionary. If the key doesn't exist in the MDD, then 
	 * this method should return null. We mandate that there is one MDD per application.
	 * 
	 * Contains method-level generics to ensure consistent typing for the method itself.
	 * @param <T> Type parameter for the key.
	 * @param key 	the class of the data.
	 * @return  The value indexed by the key in the mixed data dictionary. Return null if the key doesn't exist
	 */
	public <T> T getMDDData(MixedDataKey<T> key);
	
	/**
	 * Puts a data into the mixed data dictionary. We mandate that there is one MDD per application.
	 * 
	 * Contains method-level generics to ensure consistent typing for the method itself.
	 * @param <T> Type parameter for the key.
	 * @param key	The type of the data
	 * @param value	The datapacket.
	 */
	public <T> void putMDDData(MixedDataKey<T> key, T value);
	
	
}
