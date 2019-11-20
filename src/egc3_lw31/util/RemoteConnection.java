package egc3_lw31.util;

import java.util.List;
import java.rmi.RemoteException;
import java.util.ArrayList;
//import java.util.UUID;

import common.IRemoteConnection;
import egc3_lw31.client.chatapp.model.IChatAppModel2ViewAdapter;
import egc3_lw31.client.chatapp.model.IMain2MiniAdapter;
import common.IGroup;
import common.IMember;

/**
 * Remote Connection
 */
public class RemoteConnection implements IRemoteConnection {
	/**
	 * The user name
	 */
	private String name;

	//private Set<IMember> reps;
	/**
	 * The set of chatrooms this user is currently in
	 */
	private List<IGroup> rooms;
	/**
	 * The Model to View adapter
	 */
	private IChatAppModel2ViewAdapter m2vAdpt;
	/**
	 * The stub to this user
	 */
	private IRemoteConnection userStub;
	
	/**
	 * Constructor
	 * @param name Name of the user
	 * @param ip IP address of the host
	 * @param _m2vAdpt Model2ViewAdapter
	 */
	public RemoteConnection(String name, String ip, IChatAppModel2ViewAdapter _m2vAdpt) {
		this.name = name + " @ " + ip;
//		this.ID = UUID.randomUUID();
		this.rooms = new ArrayList<>();
//		this.IPAddress = ip;
		this.m2vAdpt = _m2vAdpt;
//		this.reps = new HashSet<>();
	}

	@Override
	public List<IGroup> getGroups() {
		System.out.println("RemoteConnection room size: " + rooms.size());
		return rooms;
	}

//	@Override
//	public void connectTo (IUser userStub) throws RemoteException {
//		// TODO get the other user's list of chatrooms and 
//		// update them to the view using the adaptor
//		//rooms.addAll(userStub.getChatRooms());
//	}

//	@Override
//	public UUID getUserID() {
//		return ID;
//	}
//
//	@Override
//	public UUID getUUID() throws RemoteException {
//		// TODO Auto-generated method stub
//		return ID;
//	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void connect(IRemoteConnection remoteConnectionStub) throws RemoteException {
		// TODO Auto-generated method stub
		m2vAdpt.addUser(remoteConnectionStub);
	}

	@Override
	public void receiveInvite(IGroup chatRoom) throws RemoteException {
		// make a copy of this chatroom with UUID		
		if (!rooms.contains(chatRoom)) {
			IGroup roomCopy = new ChatRoom(chatRoom.getName(), chatRoom.getUUID());
			// add all exisiting members to this room
			for (IMember repStub: chatRoom.getMembers()) {
				//roomCopy.addRep(repStub);
				roomCopy.getMembers().add(repStub);
			}
			// tell everybody to add you
			IMain2MiniAdapter m2mAdpt = m2vAdpt.makeMiniMVC(roomCopy, userStub);
			m2vAdpt.addChatroom(m2mAdpt.getMiniRoom());
		}
		
//		for (IGroup room : user.getChatRooms()) {
//			if (chatroom.getUUID() == room.getUUID()) {
//				outputCmd.accept("You are already in" + room.getName() + "!");
//				return;
//			}
//		}
	}

	@Override
	public void quit(IRemoteConnection remoteConnectionStub) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String toString() {
		return name;
	}
//	@Override
//	public String getIP() {
//		return IPAddress;
//	}
//	
//	@Override
//	public void addRoom(IGroup room) throws RemoteException{
//		rooms.add(room);
//	}

//	@Override
//	public Set<IMember> getRepresentatives() throws RemoteException {
//		return reps;
//	}
	
	/**
	 * @param userStub -- the stub
	 */
	public void setUserStub(IRemoteConnection userStub) {
		this.userStub = userStub;
	}
	
//	/**
//	 * Removes the member from the list of all members.
//	 * @param rep The member to remove
//	 * @throws RemoteException -- a remote exception for removing a member
//	 */
//	public void removeRep(IMember rep) throws RemoteException {
//		reps.remove(rep);
//	}
//	
//	/**
//	 * Removes the room from the list of all rooms
//	 * @param room The room to remove
//	 * @throws RemoteException --  a remote exception for removing a room
//	 */
//	public void removeRoom(IGroup room) throws RemoteException {
//		rooms.remove(room);
//	}
}
