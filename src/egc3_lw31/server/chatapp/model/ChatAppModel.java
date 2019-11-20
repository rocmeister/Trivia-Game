package egc3_lw31.server.chatapp.model;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import javax.swing.JFrame;

import common.IGroup;
import common.IMember;
import common.IRemoteConnection;
import common.message.GroupDataPacket;
import common.message.ILeaveData;
import egc3_lw31.data.LeaveData;
import egc3_lw31.server.util.ChatRoom;
import egc3_lw31.server.util.ServerRemoteConnection;
import provided.discovery.IDiscoveryConnection;
import provided.discovery.IDiscoveryManager;
import provided.discovery.IDiscoveryServer;
import provided.discovery.IEndPointData;
import provided.discovery.impl.view.DiscoveryPanel;
import provided.discovery.impl.view.IDiscoveryPanelAdapter;
import provided.rmiUtils.IRMI_Defs;
import provided.rmiUtils.RMIUtils;


/**
 * Model for the main MVC.
 */
public class ChatAppModel {	
	/* Start of fields definitions */
	
	/* System level fields */
	/**
	 * The model to view adapter
	 */
	private IChatAppModel2ViewAdapter m2vAdpt;
	
	/**
	 * The outputCmd that prints status updates
	 */
	private Consumer<String> outputCmd = new Consumer<String>() {
		@Override
		public void accept(String s) {
			m2vAdpt.displayStatus(s + "\n");
		}
	};
	/**
	 * The RMIUtils object
	 */
	private RMIUtils rmiUtils = new RMIUtils(outputCmd);
	
	/* ChatApp specific fields */
	/**
	 * Stub to this local user
	 */
	private IRemoteConnection localUserStub;
	/**
	 * Stub to a remote user
	 */
	private IRemoteConnection remoteUserStub;
	
	/**
	 * The unique user for this app
	 */
	private IRemoteConnection user;
	
	/**
	 * The local registry
	 */
	private Registry localRegistry;
	
	/* fields for the user */
//	private Set<IMember> localReps = new HashSet<IMember>();
	/**
	 * Set of connected user stubs
	 */
	private Set<IRemoteConnection> connectedUserStubs = new HashSet<IRemoteConnection>();

    /**
     * Roster of players
     */
    private Set<IMember> playerRoster = new HashSet<>();
	
	// Bound ports and names
	/**
	 * Local name of the user
	 */
	public String LOCAL_USER_NAME = IRemoteConnection.BOUND_NAME;
	/**
	 * Remote name of the user
	 */
	public String REMOTE_USER_NAME = IRemoteConnection.BOUND_NAME;
	
	/**
	 * Local port
	 */
	public int LOCAL_RMI_PORT = IRMI_Defs.CLASS_SERVER_PORT_CLIENT; // 2002
	/**
	 * Remote port
	 */
	public int REMOTE_RMI_PORT = IRMI_Defs.CLASS_SERVER_PORT_SERVER; // 2001
	/**
	 * Port of the user
	 */
	public int BOUND_PORT_USER = IRMI_Defs.STUB_PORT_SERVER; //IRemoteConnection.BOUND_PORT; // 2100
	/**
	 * Port of the rep
	 */
	//public final int BOUND_PORT_TEST = IRMI_Defs.STUB_PORT_EXTRA;
	public int BOUND_PORT_REP = IRMI_Defs.STUB_PORT_CLIENT; // 2101
	
	/**
	 * Constructor 
	 * @param m2vAdpt IChatAppModel2ViewAdapter
	 */
	public ChatAppModel(IChatAppModel2ViewAdapter m2vAdpt) {
		this.m2vAdpt = m2vAdpt;
	}
	
	/**
	 *  Discovery Server, initialized to be null.
	 */
	private IDiscoveryServer discSrv = null;
	
	/* Start of method definitions */
	
	/**
	 * Start up the ChatAppModel
	 */
	@SuppressWarnings("rawtypes")
	public void start() {
		try {
			// Start as a SERVER & CLIENT
			// Step 1. Start the RMI system
			rmiUtils.startRMI(LOCAL_RMI_PORT);
			
			// Step 2. Use the IRMIUtils to get the LOCAL Registry.
			localRegistry = rmiUtils.getLocalRegistry();
			
			// Step 3. Instantiate the IUser RMI Object.
			user = new ServerRemoteConnection("The Silk Road Server", rmiUtils.getLocalAddress(), m2vAdpt);
			
			// Step 4. Create a stub of this ChatApp's IUser Object to be
			// used when connecting to a remote IUser.
			localUserStub = (IRemoteConnection) UnicastRemoteObject.exportObject(user, BOUND_PORT_USER);
//			userServerRemoteConn = ((ServerRemoteConnection) user);
//			userServerRemoteConn.setUserStub(localUserStub);
			
			((ServerRemoteConnection) user).setUserStub(localUserStub);
			
			// Step 5. Bind the IUser stub to the local Registry
			localRegistry.rebind(LOCAL_USER_NAME, localUserStub);
			//outputCmd.accept("ChatAppModel successfully started! Your IP address is: " + user.getIP());
			outputCmd.accept("ChatAppModel successfully started!");
			
			//START DISCOVERY SERVER

			
			@SuppressWarnings("unchecked")
			DiscoveryPanel discoveryPanel = new DiscoveryPanel(new IDiscoveryPanelAdapter<IEndPointData>() {

				@Override
				public void connectToDiscoveryServer(String category, boolean watchOnly, Consumer endPtsUpdateFn) {
					IDiscoveryManager discMgr;
					try {
						discMgr = IDiscoveryConnection.getDiscoveryManager(rmiUtils);
												
						if (watchOnly) {
							discSrv = discMgr.connectAs(category);
						} else { 
							discSrv = discMgr.register("The Silk Road Game", category, IRemoteConnection.BOUND_NAME);
						}	
						Consumer<Iterable<IEndPointData>> endPtDisplayFn = endPtsUpdateFn;
						discSrv.watch(endPtDisplayFn);	
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}							
				}

				@Override
				public void connectToEndPoint(IEndPointData selectedValue) {
					// Get IP Address
					String ipAddress = selectedValue.getAddress();
					IRemoteConnection remoteStub = connectTo(ipAddress);
					m2vAdpt.addUser(remoteStub);
//					RemoteAPIStubFactory<IRemoteConnection> APIStubFac = new RemoteAPIStubFactory<>(rmiUtils);
//					//Get endpoint
//					try {
//						IRemoteConnection remoteStub = APIStubFac.get((IEndPointData) selectedValue);
////						remoteStub.connect(localUserStub);
////						localUserStub.connect(remoteStub);
//						connectedUserStubs.add(remoteStub);
//					} catch (NotBoundException | RemoteException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}
				
			});
			
			//Initialize discovery server and have it appear as a new window
			discoveryPanel.start();
			JFrame discoverySvrPnl = new JFrame();
			discoverySvrPnl.getContentPane().add(discoveryPanel);
			discoverySvrPnl.setBounds(150, 150, 800, 488);
			discoverySvrPnl.setVisible(true);
		} catch (Exception e) {
			String s = "ChatAppModel Starting Error: " + e.toString();
			errMsg(s, e);
		}
	}
	// Regular Admin Start
//	public void admin() {
//		LOCAL_RMI_PORT = 2002;
//		REMOTE_RMI_PORT = 2001;
//	    BOUND_PORT_USER = 2100;
//	    BOUND_PORT_REP = 2102;
//	    LOCAL_USER_NAME = "Admin";
//	    REMOTE_USER_NAME = "Guest";    // change to "USER" if tested with another implementation
//	    outputCmd.accept("[ChatappModel.setUserMode()] Admin Mode.");
//	    try {
//			// Start as a SERVER & CLIENT
//			// Step 1. Start the RMI system
//			rmiUtils.startRMI(LOCAL_RMI_PORT);
//			
//			// Step 2. Use the IRMIUtils to get the LOCAL Registry.
//			localRegistry = rmiUtils.getLocalRegistry();
//			
//			// Step 3. Instantiate the IUser RMI Object.
//			user = new RemoteConnection("Admin", rmiUtils.getLocalAddress(), m2vAdpt);
//			
//			// Step 4. Create a stub of this ChatApp's IUser Object to be
//			// used when connecting to a remote IUser.
//			localUserStub = (IRemoteConnection) UnicastRemoteObject.exportObject(user, BOUND_PORT_USER);
//			
//			// Step 5. Bind the IUser stub to the local Registry
//			localRegistry.rebind(LOCAL_USER_NAME, localUserStub);
//			//outputCmd.accept("ChatAppModel successfully started! Your IP address is: " + user.getIP());
//			
//		} catch (Exception e) {
//			String s = "ChatAppModel Starting Error: " + e.toString();
//			errMsg(s, e);
//		}
//	}
//	
//	// Test Guest Start
//	public void guest() {
//		LOCAL_RMI_PORT = 2004;
//		REMOTE_RMI_PORT = 2003;
//	    BOUND_PORT_USER = 2101;
//	    BOUND_PORT_REP = 2103;
//	    LOCAL_USER_NAME = "Guest";
//	    REMOTE_USER_NAME = "Admin";
//	    outputCmd.accept("[ChatappModel.setTestMode()] Guest Mode.");
//	    try {
//			// Start as a SERVER & CLIENT
//			// Step 1. Start the RMI system
//			rmiUtils.startRMI(LOCAL_RMI_PORT);
//			
//			// Step 2. Use the IRMIUtils to get the LOCAL Registry.
//			localRegistry = rmiUtils.getLocalRegistry();
//			
//			// Step 3. Instantiate the IUser RMI Object.
//			user = new RemoteConnection("Admin", rmiUtils.getLocalAddress(), m2vAdpt);
//			
//			// Step 4. Create a stub of this ChatApp's IUser Object to be
//			// used when connecting to a remote IUser.
//			localUserStub = (IRemoteConnection) UnicastRemoteObject.exportObject(user, BOUND_PORT_USER);
//			((RemoteConnection) user).setUserStub(localUserStub);
//			
//			// Step 5. Bind the IUser stub to the local Registry
//			localRegistry.rebind(LOCAL_USER_NAME, localUserStub);
//			//outputCmd.accept("ChatAppModel successfully started! Your IP address is: " + user.getIP());
//			
//		} catch (Exception e) {
//			String s = "ChatAppModel Starting Error: " + e.toString();
//			errMsg(s, e);
//		}
//	}
	
	/**
	 * @throws RemoteException -- exception when quitting the app
	 */
	public void stop() throws RemoteException {
		for (IGroup room : user.getGroups()) {
			for (IMember member : room.getMembers()) {
				if (member.getRemoteConnection().equals(user)) {
					room.getMembers().remove(member);
					ILeaveData data = new LeaveData(member);
					GroupDataPacket<ILeaveData> leaveMsg = new GroupDataPacket<>(data, member);
					room.sendMsgToAll(leaveMsg);
					//m2vAdpt.removeRoomFromView(room);
				}
			}
		}
		m2vAdpt.removeAllRooms();

		try {
			localRegistry.unbind(LOCAL_USER_NAME);
			rmiUtils.stopRMI();
			discSrv.disconnect();
		} catch (Exception e) {
			String s = "ChatAppModel Stopping Error: " + e.toString();
			errMsg(s, e);
		}
		System.exit(0);
	}
	
	/**
	 * connecting to a remoteUser using its IP address, add the set of chatrooms to the dropdown list
	 * @param remoteUser The remote user
	 * @return The remote user stub
	 */
	public IRemoteConnection connectTo(String remoteUser) {
		try {
			// Connect to the remoteUser
			Registry remoteRegistry = rmiUtils.getRemoteRegistry(remoteUser);
			remoteUserStub = (IRemoteConnection) remoteRegistry.lookup(REMOTE_USER_NAME);
			connectedUserStubs.add(remoteUserStub);
			outputCmd.accept("Successfully connected to " + remoteUser + "!");
			
			// Auto-connect back
			remoteUserStub.connect(localUserStub);
			outputCmd.accept(remoteUser + "successfully connected back!");
			
			return remoteUserStub;
		} catch (Exception e) {
			String s = "ChatAppModel Connection Error: " + e.toString();
			errMsg(s, e);
			return null;
		}
	}
	
	/**
	 * Create a chatroom
	 * @param roomName The name of the room
	 * @return A chat room
	 */
	public IGroup createChatRoom(String roomName) {
		IGroup chatroom = new ChatRoom(roomName);
		/* Mini Model Part */
		IMain2MiniAdapter m2mAdpt = m2vAdpt.makeMiniMVC(chatroom, localUserStub);
		//ChatRoomHelper helper = new ChatRoomHelper(chatroom);
		
		return m2mAdpt.getMiniRoom();
	}
	
	/**
	 * Create a new IMember for the user to join the chatroom		
	 * add this member's stub to the chatroom locally
	 * update this chatroom for every member in this room
	 * broadcast the add rep msg to other reps
	 * @param chatroom The chat room
	 * @throws RemoteException -- exception when joining a remove
	 */
	public void joinChatRoom(IGroup chatroom) throws RemoteException{
		// make a copy of this chatroom with UUID
		IGroup roomCopy = new ChatRoom(chatroom.getName(), chatroom.getUUID());
		// add all exisiting members to this room
		for (IMember repStub: chatroom.getMembers()) {
			//roomCopy.addRep(repStub);
			roomCopy.getMembers().add(repStub);
		}
		
		for (IGroup room : user.getGroups()) {
			if (chatroom.getUUID() == room.getUUID()) {
				outputCmd.accept("You are already in" + room.getName() + "!");
				return;
			}
		}
		// tell everybody to add you
		IMain2MiniAdapter m2mAdpt = m2vAdpt.makeMiniMVC(roomCopy, localUserStub);
		m2vAdpt.addChatroom(m2mAdpt.getMiniRoom());
		
		//ChatRoomHelper helper = new ChatRoomHelper(chatroom);
		//return m2mAdpt.getMiniRoom();
		//ChatRoomHelper helper = new ChatRoomHelper(roomCopy);

//		IAddRep data = new IAddRep() {
//			private static final long serialVersionUID = -63482753576045163L;
//
//			@Override
//			public IRepresentative getRepToAdd() {
//				// TODO Auto-generated method stub
//				return helper.repStub;
//			}
//		};
//		IAddRep data = new AddRep(helper.repStub);
//		DataPacket<IAddRep, IRepresentative> msg = new DataPacket<>(data, helper.repStub);
//		// use chatroom to broadcast so you can skip yourself
//		//chatroom.broadcast(msg);
//		//roomCopy.broadcast(msg);
//		helper.room.broadcast(msg);
	}
	
	/**
	 * Add the room for the user
	 * @param chatroom The chatroom to add
	 */
	public void addRoom2User(IGroup chatroom) {
		try {
			//System.out.println("ChatAppModel addRoom2User Start");
			user.getGroups().add(chatroom);
			//System.out.println("ChatAppModel addRoom2User End");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Remove the room from the user
	 * @param chatroom The room to remove
	 */
	public void removeRoom4User(IGroup chatroom) {
		try {
			user.getGroups().remove(chatroom);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			for (IGroup group: user.getGroups()) {
				System.out.println("<ChatAppMode> Rooms after removed room: " + group.getName());
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Add user to users you're connected to
	 * @param userStub The user to add
	 */
	public void addUser(IRemoteConnection userStub) {
		connectedUserStubs.add(userStub);
	}
	
	/**
	 * Invite an user to your room
	 * @param stub The user to invite
	 * @param room Your room
	 */
	public void inviteUserToRoom(IRemoteConnection stub, IGroup room) {
		try {
			stub.receiveInvite(room);
		} catch (RemoteException e) {
			e.printStackTrace();
		}		
	}
	
	/* Helper functions */	
	/**
	 * @param s -- message
	 * @param e -- error
	 */
	private void errMsg(String s, Exception e) {
		System.err.println(s);
		outputCmd.accept(s);
		e.printStackTrace();
	}

	/**
	 * Create new team for game
	 * @param name Name of chatroom for team
	 * @param members IRemoteConnections for members
	 * @return chatroom that was created
	 */
	public IGroup createTeamRoom(String name, List<IRemoteConnection> members) {
		IGroup teamRoom = createChatRoom(name);
		for (IRemoteConnection mbr : members) {
			if (!mbr.equals(localUserStub)) { 
				inviteUserToRoom(mbr, teamRoom);
			}
		}
		m2vAdpt.addChatroom(teamRoom);
		return teamRoom;
	}

	/**
	 * Create lobby chatroom
	 */
	public void createLobby() {
		IGroup chatroom = new ChatRoom("Lobby");
		/* Mini Model Part */
		//IMain2MiniAdapter m2mAdpt = m2vAdpt.createLobby(chatroom, localUserStub);
		m2vAdpt.createLobby(chatroom, localUserStub);
		try {
			for (IGroup group: user.getGroups()) {
				System.out.println("<ChatAppMode> Group name: " + group.getName());
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
//		try {
//			user.getGroups().add(chatroom);
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
		m2vAdpt.addChatroom(chatroom);
	}
	
	/**
	 * Stores the player roster
	 * @param teamList List of players to be stored
	 */
	public void storePlayerRoster(List<IMember> teamList) {
		teamList.forEach(m -> this.playerRoster.add(m));
	}
	
	/**
	 * Gets roster of players
	 * @return Roster of players
	 */
	public Set<IMember> getPlayerRoster() {return this.playerRoster;}
}
