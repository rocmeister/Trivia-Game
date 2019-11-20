package egc3_lw31.client.chatroom.model;

import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import javax.swing.ImageIcon;

import common.IGroup;
import common.ICmd2ModelAdapter;
import common.IMember;
import common.IRemoteConnection;
import common.message.AMessageCmd;
import common.message.GroupDataPacket;
import common.message.GroupDataPacketAlgo;
import common.message.IInstallCmdData;
import common.message.IJoinData;
import common.message.ILeaveData;
import common.message.IRequestCmdData;
import common.message.ITxtData;
import common.message.status.IStatusProcessingError;
import common.message.status.IStatusRejectionError;
import common.message.status.IStatusSendingError;
import common.message.synchronization.IUpdateMemberListAddData;
import common.message.synchronization.IUpdateMemberListRemoveData;
import egc3_lw31.data.IImageData;
import egc3_lw31.data.ImageCmd;
import egc3_lw31.data.ImageData;
import egc3_lw31.data.IMapData;
import egc3_lw31.data.Instruct;
import egc3_lw31.data.JoinData;
import egc3_lw31.data.LeaveData;
import egc3_lw31.data.MapData;
import egc3_lw31.data.RequestInstruction;
import egc3_lw31.data.StatusProcessingError;
import egc3_lw31.data.StatusProcessingErrorCmd;
import egc3_lw31.data.StatusRejectionError;
import egc3_lw31.data.StatusRejectionErrorCmd;
import egc3_lw31.data.StatusSendingError;
import egc3_lw31.data.StatusSendingErrorCmd;
import egc3_lw31.data.TextData;
import egc3_lw31.data.TextDataCmd;
import egc3_lw31.data.UpdateMemberListAddCmd;
import egc3_lw31.data.UpdateMemberListRemoveCmd;
import egc3_lw31.client.util.ChatRoom;
import egc3_lw31.client.util.Member;
import provided.datapacket.IDataPacketData;
import provided.datapacket.IDataPacketID;
import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataDictionary;
import provided.mixedData.MixedDataKey;
import provided.rmiUtils.IRMI_Defs;

/**
 * Model of the mini MVC
 */
public class ChatRoomModel {
	/**
	 * Chatroom 
	 */
	private IGroup chatroom;
	/**
	 * Stub of the member
	 */
	private IMember repStub;
	/**
	 * The adapter between the mini MVC and the main MVC
	 */
	private IMini2MainAdapter m2MAdpt;
	/**
	 * The user stub
	 */
	private IRemoteConnection userStub;
	/**
	 * The adapter between the model and the view
	 */
	private IChatRoomModel2ViewAdapter m2vAdpt;
	/**
	 * The visitor used for processing commands
	 */
	private GroupDataPacketAlgo visitor;
	
	/**
	 * Mixed data dictionary for command 2 model adapter
	 */
	private IMixedDataDictionary mdd = new MixedDataDictionary();
	
	/**
	 * Adapter 
	 */
	private ICmd2ModelAdapter c2mAdpt = new ICmd2ModelAdapter() {
		@Override
		public void addComponentFactory(Supplier<Component> compFac) {
			m2vAdpt.addComponent(compFac.get());
		}
		
		public void displayTextMsg(String msg, String identifier) {
			m2vAdpt.appendString(identifier + ": " + msg);
		}
		
		@Override
		public String getLocalMemberName() {
			try {
				return userStub.getName();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public String getLocalGroupName() {
			return chatroom.getName();
		}


		public void sendMessageToMember(IDataPacketData msg, IMember member) {
			try {
				member.receiveData(new GroupDataPacket<IDataPacketData>(msg, repStub));
			} catch (RemoteException e) {
				e.printStackTrace();
			} 
		}
		
		/**
		 * Sends the message to the entire group that this member is in. 
		 * @param msg to be sent
		 */
		public void sendMessageToGroup(IDataPacketData msg) {
			for (IMember member: chatroom.getMembers()) {
				this.sendMessageToMember(msg, member);
			}
		}
		
		@Override
		public <T> T getMDDData(MixedDataKey<T> key) {
			return mdd.get(key);
		}
		
		@Override
		public <T> void putMDDData(MixedDataKey<T> key, T value) {
			mdd.put(key, value);
		}
	};


//	public ChatRoomModel(IGroup room, IGroupModel2ViewAdapter m2vAdpt) {
	/**
	 * @param room -- the room for this mini-model
	 * @param adpt -- the mini to main adapter
	 * @param userStub2 -- the user's stub
	 * @param _m2vAdpt -- the model to view adapter
	 */
	public ChatRoomModel(IGroup room, IMini2MainAdapter adpt, IRemoteConnection userStub2, IChatRoomModel2ViewAdapter _m2vAdpt) {
		//chatroom = room;
		// make a copy of this chatroom with UUID
		chatroom = new ChatRoom(room.getName(), room.getUUID());
		// add all exisiting members to this room
		for (IMember repStub: room.getMembers()) {
			//roomCopy.addRep(repStub);
			chatroom.getMembers().add(repStub);
		}
		m2MAdpt = adpt;
		userStub = userStub2;
		m2vAdpt = _m2vAdpt;
		
		/* START OF MESSAGE HANDLING */
		ConcurrentHashMap<IDataPacketID, List<GroupDataPacket<? extends IDataPacketData>>> cache = new ConcurrentHashMap<>();
		//HashMap<IDataPacketID, List<GroupDataPacket<T>>> cache = new HashMap<>();

		/* default cmd if receive unknown */
		AMessageCmd<? extends IDataPacketData> defaultcmd = new AMessageCmd<>() {
			/**
			 * Serial version UID
			 */
			private static final long serialVersionUID = -2384334100397203447L;

			@Override
			public Void apply(IDataPacketID index, GroupDataPacket<IDataPacketData> host, Void... params) {
				System.out.println("Default CMD fired");
			    // add this to the cached messages
			    if (cache.containsKey(host.getData().getID())) {
					cache.get(host.getData().getID()).add(host);
				} else {
					ArrayList<GroupDataPacket<? extends IDataPacketData>> newList = new ArrayList<>();
					newList.add(host);
					cache.put(host.getData().getID(), newList);
				}
			    
			    // forward an IRequest Cmd
				IRequestCmdData data = new RequestInstruction(index);
			    GroupDataPacket<IRequestCmdData> requestData = new GroupDataPacket<>(data, repStub);
			    try {
					host.getSender().receiveData(requestData);
				} catch (RemoteException e) {
					statusSendingError(host.getSender());
					e.printStackTrace();
				} catch (NullPointerException e) {
					try {
						statusProcessingError(host.getSender(), index);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
			    //cache.get(index).add(host);
			    //cache.put(index, values);
				return null;
			}
		};
		defaultcmd.setCmd2ModelAdpt(this.c2mAdpt);
		this.visitor = new GroupDataPacketAlgo(defaultcmd) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -1105348767997457330L;
		};
		
		/* BEGIN KNOWN MESSAGE TYPE COMMAND INSTALLATION */
		/* if receive a request cmd */
		AMessageCmd<IRequestCmdData> instructCmd = new AMessageCmd<IRequestCmdData> () {
			/**
			 * 
			 */
			private static final long serialVersionUID = 5137945787046283323L;

			@Override
			public Void apply(IDataPacketID index, GroupDataPacket<IRequestCmdData> host, Void... params) {
				System.out.println("INSTRUCT CMD fired");
			    IRequestCmdData request = host.getData();

			    @SuppressWarnings("unchecked")
				AMessageCmd<? extends IDataPacketData> cmd = (AMessageCmd<? extends IDataPacketData>) visitor.getCmd(request.getRequestedCmdType());
			    Instruct data = new Instruct(request.getRequestedCmdType(), cmd);
			    //ImageCmd imgCmd = new ImageCmd(this._cmd2ModelAdpt);
				//imgCmd.setCmd2ModelAdpt(this._cmd2ModelAdpt);
//			    ImageCmd imgCmd = new ImageCmd();
//			    Instruct data = new Instruct(request.getRequestedCmdType(), imgCmd);
			    GroupDataPacket<IInstallCmdData> instructData = new GroupDataPacket<>(data, repStub);
			    try {
				    // check if it's already a well known message 
//				    if (imgCmd == IJoinData.GetID() || imgCmd == ILeaveData.GetID() || imgCmd == ITxtData.GetID()) {
//				    	StatusRejectionError rejection = new StatusRejectionError(host.getSender(), imgCmd);
			    	if (cmd == IJoinData.GetID() || cmd == ILeaveData.GetID() || cmd == ITxtData.GetID()) {
				    	StatusRejectionError rejection = new StatusRejectionError(host.getSender(), cmd);
						GroupDataPacket<IStatusRejectionError> statusErrorDP = new GroupDataPacket<>(rejection, host.getSender());
						host.getSender().receiveData(statusErrorDP);
				    }
					host.getSender().receiveData(instructData);
				} catch (RemoteException e) {
					statusSendingError(host.getSender());
					e.printStackTrace();
				} catch (NullPointerException e) {
					try {
						statusProcessingError(host.getSender(), index);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
			    return null;
			}
		};
		instructCmd.setCmd2ModelAdpt(this.c2mAdpt);
		this.visitor.setCmd(IRequestCmdData.GetID(), instructCmd);

		/* if receive an install cmd */
		AMessageCmd<IInstallCmdData> processCmd = new AMessageCmd<IInstallCmdData> () {
			/**
			 * 
			 */
			private static final long serialVersionUID = 8234500350390642802L;

			@Override
			public Void apply(IDataPacketID index, GroupDataPacket<IInstallCmdData> host, Void... params) {
				System.out.println("PROCESS CMD fired");
			    // install the cmd from the host into visitor
				IInstallCmdData install = host.getData();
			    //AMessageCmd<? extends IDataPacketData> cmd = (AMessageCmd<? extends IDataPacketData>) visitor.getCmd(install.getInstallCmdType());
				AMessageCmd<? extends IDataPacketData> cmd = install.getCmdToInstall();
				cmd.setCmd2ModelAdpt(c2mAdpt);
				//System.out.println(c2mAdpt);
			    visitor.setCmd(install.getInstallCmdType(), cmd);			    
			    
			    // execute all datapackets of the cmd's target type in the cache
			    //List<GroupDataPacket<T>> values = cache.get(install.getInstallCmdType());
			    if (cache.get(install.getInstallCmdType()) != null) {
				    for (GroupDataPacket<? extends IDataPacketData> value : cache.get(install.getInstallCmdType())) {
				      value.execute(visitor);
				    }
			    }
				return null;
			}
		};
		processCmd.setCmd2ModelAdpt(this.c2mAdpt);
		this.visitor.setCmd(IInstallCmdData.GetID(), processCmd);
		
		/* join type cmd */
		//JoinDataCmd addRepCmd = new JoinDataCmd(room, this.m2vAdpt);
		AMessageCmd<IJoinData> addRepCmd = new AMessageCmd<IJoinData>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3888308243229812611L;

			@Override
			public Void apply(IDataPacketID index, GroupDataPacket<IJoinData> host, Void... params) {
				if (!chatroom.getMembers().contains(host.getSender())) chatroom.getMembers().add(host.getSender());
				System.out.println("received an ADDRepMSG!");
				try {
					//System.out.println(host.getSender().getName());
					m2vAdpt.appendString(host.getSender().getName() + " has just joined!");
				} catch (RemoteException e) {
					statusSendingError(host.getSender());
					e.printStackTrace();
				} catch (NullPointerException e) {
					try {
						statusProcessingError(host.getSender(), index);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				} 
				return null;
			}
		};
		addRepCmd.setCmd2ModelAdpt(this.c2mAdpt);
		visitor.setCmd(IJoinData.GetID(), addRepCmd);
		
		/* leave type cmd */
		//LeaveDataCmd removeRepCmd = new LeaveDataCmd(room, this.m2vAdpt);
		AMessageCmd<ILeaveData> removeRepCmd = new AMessageCmd<ILeaveData>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -1465383693939998356L;

			@Override
			public Void apply(IDataPacketID index, GroupDataPacket<ILeaveData> host, Void... params) {
				chatroom.getMembers().remove(host.getSender());
				try {
					m2vAdpt.appendString(host.getSender().getName() + " has just left!");
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
			
		};
		removeRepCmd.setCmd2ModelAdpt(this.c2mAdpt);
		visitor.setCmd(ILeaveData.GetID(), removeRepCmd);
		
		/* status sending error cmd */
		StatusSendingErrorCmd statusSendingErrorCmd = new StatusSendingErrorCmd(room);
		statusSendingErrorCmd.setCmd2ModelAdpt(this.c2mAdpt);
		visitor.setCmd(IStatusSendingError.GetID(), statusSendingErrorCmd);
		
		/* status rejection error cmd */
		StatusRejectionErrorCmd statusRejectionErrorCmd = new StatusRejectionErrorCmd(room);
		statusRejectionErrorCmd.setCmd2ModelAdpt(this.c2mAdpt);
		visitor.setCmd(IStatusRejectionError.GetID(), statusRejectionErrorCmd);
		
		/* status processing error cmd */
		StatusProcessingErrorCmd statusProcessingErrorCmd = new StatusProcessingErrorCmd(room);
		statusProcessingErrorCmd.setCmd2ModelAdpt(this.c2mAdpt);
		visitor.setCmd(IStatusProcessingError.GetID(), statusProcessingErrorCmd);
		
		/* text type cmd */
		TextDataCmd textCmd = new TextDataCmd();
		textCmd.setCmd2ModelAdpt(this.c2mAdpt);
		visitor.setCmd(ITxtData.GetID(), textCmd);
		
		/* image type cmd */
		ImageCmd imgCmd = new ImageCmd();
		imgCmd.setCmd2ModelAdpt(this.c2mAdpt);
		visitor.setCmd(IImageData.GetID(), imgCmd);
		
		/* Sync cmd */
		UpdateMemberListAddCmd updateAddCmd = new UpdateMemberListAddCmd();
		updateAddCmd.setCmd2ModelAdpt(this.c2mAdpt);
		visitor.setCmd(IUpdateMemberListAddData.GetID(), updateAddCmd);
		
		UpdateMemberListRemoveCmd updateRemoveCmd = new UpdateMemberListRemoveCmd();
		updateRemoveCmd.setCmd2ModelAdpt(this.c2mAdpt);
		visitor.setCmd(IUpdateMemberListRemoveData.GetID(), updateRemoveCmd);
		
		
		/* End of Visitor Initialization */
		
		/* END OF MESSAGE HANDLING */
		
		/* START of member stub initialization */
		IMember rep = new Member(room, userStub, c2mAdpt, visitor);
		System.out.println("[ChatRoomModel.sendMsg 1] ChatRoom has: " + chatroom.getMembers().size() + " reps");
		try {
			repStub = (IMember) UnicastRemoteObject.exportObject(rep, IRMI_Defs.STUB_PORT_CLIENT);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		// add this member stub to the chatroom
		chatroom.getMembers().add(repStub);
		// broadcast an IJoinData msg to everyone
		IJoinData data = new JoinData(repStub);
		GroupDataPacket<IJoinData> msg = new GroupDataPacket<>(data, repStub);
		chatroom.sendMsgToAll(msg);
//		// add this member stub to the chatroom
//		chatroom.getMembers().add(repStub);
		// add this chatroom to user
//		try {
//			userStub.getChatRooms().add(chatroom);
//		} catch (RemoteException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		System.out.println("[ChatRoomModel.sendMsg 2] ChatRoom has: " + chatroom.getMembers().size() + " reps");
		//System.out.println("ChatRoomModel Debug 1");
		m2MAdpt.addRoom2User(chatroom);
		//System.out.println("ChatRoomModel End of member creation");
		/* END of member stub creation */
	}
	
	/**
	 * Start model
	 */
	public void start() {}
	
	/**
	 * Send message to everyone in chat room 
	 * @param DP Packet to send 
	 * @throws RemoteException -- remote excpetion when broadcasting a message
	 */
	public void broadcastMessage(GroupDataPacket<? extends IDataPacketData> DP) throws RemoteException {
		chatroom.sendMsgToAll(DP);
		
		//repStub.getRoom().broadcast(DP);
//		IRepresentative sender = D.getSender();
//		try {
//			receiver.receiveMessage(D);
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	/**
	 * Send text to member(s)
	 * @param msg The text to send
	 */
	public void sendText(String msg) {
		// Make a DataPacket for IText
		ITxtData ITextData = new TextData(msg);
		GroupDataPacket<ITxtData> dp = new GroupDataPacket<>(ITextData, repStub);
		try {
			this.broadcastMessage(dp);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Send an image
	 * @param imgPath The file path of the image
	 */
	public void sendImage(String imgPath) {
		// Make an IImageData DataPacket
		//java.net.URL imgURL = getClass().statgetResource(imgPath);
		//java.net.URL imgURL = getClass().getResource("images/Earth.png");		
		//Image img = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/Earth.png"));
		//Image img = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("rice.jpg"));
		//ImageIcon icon = new ImageIcon(imgURL, "An image from team Michelle and Rocky!");
		
		Image img = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/FIFA_Soccer_Ball.png"));
		ImageIcon icon = new ImageIcon(img, "An image from team Michelle and Rocky!");
		//JLabel label = new JLabel(icon);
		//JButton label = new JButton("TETSTSTSTTSTS!");
		//m2vAdpt.addComponent(label);
		IImageData data = new ImageData(icon);
//		IImageData data = new IImageData() {
//
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1415069487638508263L;
//
//			@Override
//			public Component getImageComponent() {
//				// TODO Auto-generated method stub
//				return new JLabel(icon);
//			}
//		};
		GroupDataPacket<IImageData> dp = new GroupDataPacket<>(data, repStub);
		try {
			this.broadcastMessage(dp);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Send an image
	 */
	public void sendMap() {		
		//Image img = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/FIFA_Soccer_Ball.png"));
		//ImageIcon icon = new ImageIcon(img, "An image from team Michelle and Rocky!");
		//JLabel label = new JLabel(icon);
		//JButton label = new JButton("TETSTSTSTTSTS!");
		//m2vAdpt.addComponent(label);
		IMapData data = new MapData();
		GroupDataPacket<IMapData> dp = new GroupDataPacket<>(data, repStub);
		try {
			this.broadcastMessage(dp);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Remove the room from the list of room
	 */
	public void removeRoomfromList() {
		// TODO Auto-generated method stub
		m2MAdpt.removeRoomFromMainList(chatroom);
	}
	
	/**
	 * Leave current room
	 * @throws RemoteException -- exception for leaving the room 
	 */
	public void leaveRoom() throws RemoteException {
		// TODO broadcast an IRemoveRep message to all members in this room, i.e DataPacket<IRemoveRep, IRep>
		chatroom.getMembers().remove(repStub);
		//chatroom.deleteRep(repStub);
		
		// remove this rep locally first
		LeaveData data = new LeaveData(repStub);
		// then tell every rep in the room to remove this user from the chatroom
		GroupDataPacket<ILeaveData> leaveMsg = new GroupDataPacket<>(data, repStub);
		chatroom.sendMsgToAll(leaveMsg);
		
		//((User) user).removeRep(repStub);
		//((RemoteConnection) user).removeRoom(chatroom);
		//userStub.getChatRooms().remove(chatroom);
		m2MAdpt.removeRoom4User(chatroom);
		m2MAdpt.removeRoomFromMainList(chatroom);
		// tell main view to remove this room's view, also remove this room from user's list
		m2MAdpt.removeRoomFromMainView(chatroom);		
	}	
	/**
	 * Gets the room you are in.
	 * @return The chat room
	 */
	public IGroup getRoom() {return chatroom;}
	
	/* Helper methods */
	/**
	 * Helper method for status sending errors.
	 * @param rep The member that needs to be removed.
	 */
	private void statusSendingError(IMember rep) {
		System.err.println("ChatRoomModel Debug: failed status sending error.");

		// remove mem from your local chatroom copy
		chatroom.getMembers().remove(rep);
		
		// send IStatusError to all other reps
		StatusSendingError data = new StatusSendingError(rep);
		GroupDataPacket<IStatusSendingError> statusSendingErrorDP = new GroupDataPacket<>(data, rep);
		chatroom.sendMsgToAll(statusSendingErrorDP);
	}

	/**
	 * Helper method for status processing errors.
	 * @param rep The member that sent the message.
	 * @param id The ID of the faulty message.
	 * @throws RemoteException -- exepction for status error
	 */
	private void statusProcessingError(IMember rep, IDataPacketID id) throws RemoteException {
		System.err.println("ChatRoomModel Debug: failed status processing error.");
		
		// send IStatusProcessingError to sender
		StatusProcessingError data = new StatusProcessingError(rep, id);
		GroupDataPacket<IStatusProcessingError> statusProcessingErrorDP = new GroupDataPacket<>(data, rep);
		rep.receiveData(statusProcessingErrorDP);
	}
}


