package common;

import java.rmi.Remote;
import java.util.List;
import java.rmi.RemoteException;

/**
 * Defines the RMI Remote Server Object that allows connections
 * at the ChatApp level (outside of individual rooms).
 *
 */
public interface IRemoteConnection extends Remote {
	
	/**
	 * The name the IStaffImplRemoteConnection object will be bound to in the RMI Registry
	 */
	static final String BOUND_NAME = "RemoteConnection";
	
	/**
	 * Used to connect to another chat app. When you call this method on a foreign IStaffImplRemoteConnection stub
	 * you give the foreign chatapp associated with this stub your IStaffImplRemoteConnection stub (so that they 
	 * know how to communicate with you). 
	 * 
	 * @param remoteConnectionStub - stub of IStaffImplRemoteConnection from the connector
	 * @throws RemoteException if an error occurs with remote connections.
	 */
	public void connect(IRemoteConnection remoteConnectionStub) throws RemoteException;
	
	/**
	 * Gets a list of IGroup ID's that the Remote ChatApp is actively in. 
	 * 
	 * It's okay to store these IGroups after receiving them (in a drop list or otherwise).
	 * 
	 * Notice that these IGroups may become stale (their memberlists are not updated to 
	 * reflect changes that occur after they are received). Make sure to utilize
	 * member synchronization protocols provided using IUpdateMemberListData.
	 * 
	 * @return A list of serialized IGroups.
	 * @throws RemoteException if an error occurs with remote connections. 
	 */
	public List<IGroup> getGroups() throws RemoteException;
	

	/**
	 * This is used to send an already connected peer an invite to their group. The
	 * inviter provides a group to the invitee 
	 * @param group A serialized IGroup to join.
	 * @throws RemoteException if an error occurs with remote connections. 
	 */
	public void receiveInvite(IGroup group) throws RemoteException;
	
	/**
	 * Get the name of this user.
	 * @return The name of this user, as defined locally.
	 * @throws RemoteException if an error occurs with remote connections.
	 */
	public String getName() throws RemoteException;
	
	/**
	 * Tells the remote system that holds this IStaffImplRemoteConnection object that the given IStaffImplRemoteConnection stub 
	 * is no longer valid and should be removed from their system.
	 * 
	 * The application quitting process must entail telling all known IStaffImplRemoteConnection stubs that one is quitting.
	 * 
	 * You must leave all groups (send a message with ILeaveData) before using this method.
	 * 
	 * @param remoteConnectionStub stub to be removed
	 * @throws RemoteException if an error occurs with remote connections.
	 */
	public void quit(IRemoteConnection remoteConnectionStub) throws RemoteException;

}