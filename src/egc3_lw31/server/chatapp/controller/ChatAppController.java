package egc3_lw31.server.chatapp.controller;

import java.awt.Component;
import java.awt.EventQueue;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import common.IGroup;
import common.IMember;
import common.IRemoteConnection;
import egc3_lw31.server.chatapp.model.ChatAppModel;
import egc3_lw31.server.chatapp.model.IChatAppModel2ViewAdapter;
import egc3_lw31.server.chatapp.model.IMain2MiniAdapter;
import egc3_lw31.server.chatapp.view.ChatAppGUI;
import egc3_lw31.server.chatapp.view.IChatAppView2ModelAdaptor;
import egc3_lw31.server.chatroom.controller.ChatRoomController;
import egc3_lw31.server.chatroom.model.IMini2MainAdapter;
import egc3_lw31.server.chatroom.view.ChatRoomView;
import egc3_lw31.server.util.ChatRoomWrapper;
import egc3_lw31.server.util.ServerRemoteConnectionWrapper;

/**
 * Controller for ChatApp.
 *
 */
public class ChatAppController {
	/**
	 * Chatapp model.
	 */
	private ChatAppModel chatappmodel;
	
	/**
	 * Chatapp GUI.
	 */
	//@SuppressWarnings("rawtypes")
	private ChatAppGUI<ServerRemoteConnectionWrapper, ChatRoomWrapper> chatappview;
	
	/**
	 * Chatroom controller.
	 */
	//private ChatRoomController miniController;

	/**
	 * Constructor for the Controller, instantiates both the model and view
	 */
	public ChatAppController() {
		chatappmodel = new ChatAppModel(new IChatAppModel2ViewAdapter() {
			@Override
			public void displayStatus(String s) {
				chatappview.displayStatus(s);
			}
			
			@Override
			public void removeRoomFromView(IGroup room) {
				chatappview.removeMiniView(new ChatRoomWrapper(room));
			}
			
			@Override
			public void removeAllRooms() {
				chatappview.removeAllMiniViews();
			}

			@Override
			public void addUser(IRemoteConnection userStub) {
				chatappview.addUser(new ServerRemoteConnectionWrapper(userStub));
				
			}
			
			@Override
			public void addChatroom(IGroup chatroom) {
				// TODO Auto-generated method stub
				chatappview.addRoom(new ChatRoomWrapper(chatroom));
			}

			// This makeMiniMVC method in fact has a lot going on:
			// 1. m2vAdpt instantiates a mini-controller;
			// 2. mini-controller makes mini-model, mini-view, and mini-adapters;
			// 3. mini-controller installs the mini-view into the main-view;
			// 4. finally the m2vAdpt returns the resultant adapter to the mini MVC.
			@Override
			public IMain2MiniAdapter makeMiniMVC(IGroup room, IRemoteConnection userStub) {
				// step 1 and 2
				ChatRoomController miniController;
				miniController = new ChatRoomController(room, userStub, new IMini2MainAdapter() {

					@Override
					public void removeRoomFromMainView(IGroup room) {
						chatappview.removeMiniView(new ChatRoomWrapper(room));
					}

					@Override 
					public void removeRoomFromMainList(IGroup room) {
						chatappview.removeRoomFromList(new ChatRoomWrapper(room));
					}
					
					@Override
					public void addRoom2User(IGroup room) {
						chatappmodel.addRoom2User(room);
						//System.out.println("ChatAppController addRoom2User End");
					}

					@Override
					public void removeRoom4User(IGroup room) {
						chatappmodel.removeRoom4User(room);
					}

					@Override
					public IGroup createTeamRoom(String name, List<IRemoteConnection> members) {
						return chatappmodel.createTeamRoom(name, members);
					}

					@Override
					public void updateRoster(List<IMember> teamList) {
						// TODO Auto-generated method stub
						chatappmodel.storePlayerRoster(teamList);
					}

					@Override
					public Set<IMember> getRoster() {
						return chatappmodel.getPlayerRoster();
					}
				});				
				
				// install the mini-view
				ChatRoomView miniView = miniController.getMiniView();
				chatappview.installMiniView(room.getName(), miniView);
				
				// return the resultant Main2Mini Adapter
				return new IMain2MiniAdapter() {
					
					@Override
					public void appendString(String text) {
						miniView.sendMsg(text);
					}

					@Override
					public IGroup getMiniRoom() {
						return miniController.getMiniModel().getRoom();
					}

					@Override
					public void addComponent(Supplier<Component> compFac) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void addLobbyComponents() {
						miniView.addLobbyComponents();
					}	
				};
			}
			
			// This createLobby method in fact has a lot going on:
			// 1. m2vAdpt instantiates a mini-controller;
			// 2. mini-controller makes mini-model, mini-view, and mini-adapters;
			// 3. mini-controller installs the mini-view into the main-view;
			// 4. finally the m2vAdpt returns the resultant adapter to the mini MVC.
			@Override
			public IMain2MiniAdapter createLobby(IGroup room, IRemoteConnection userStub) {
				// step 1 and 2
				ChatRoomController miniController;
				miniController = new ChatRoomController(room, userStub, new IMini2MainAdapter() {

					@Override
					public void removeRoomFromMainView(IGroup room) {
						chatappview.removeMiniView(new ChatRoomWrapper(room));
					}

					@Override 
					public void removeRoomFromMainList(IGroup room) {
						chatappmodel.removeRoom4User(room);
						chatappview.removeRoomFromList(new ChatRoomWrapper(room));
					}
					
					@Override
					public void addRoom2User(IGroup room) {
						chatappmodel.addRoom2User(room);
						//System.out.println("ChatAppController addRoom2User End");
					}

					@Override
					public void removeRoom4User(IGroup room) {
						chatappmodel.removeRoom4User(room);
					}

					@Override
					public IGroup createTeamRoom(String name, List<IRemoteConnection> members) {
						return chatappmodel.createTeamRoom(name, members);
					}

					@Override
					public void updateRoster(List<IMember> memberList) {
						chatappmodel.storePlayerRoster(memberList);
					}

					@Override
					public Set<IMember> getRoster() {
						return chatappmodel.getPlayerRoster();
					}
				});				
				

				// install the mini-view
				ChatRoomView miniView = miniController.getMiniView();
				
				IMain2MiniAdapter lobbyAdpt = new IMain2MiniAdapter() {
					
					@Override
					public void appendString(String text) {
						miniView.sendMsg(text);
					}

					@Override
					public IGroup getMiniRoom() {
						return miniController.getMiniModel().getRoom();
					}

					@Override
					public void addComponent(Supplier<Component> compFac) {
						// TODO Auto-generated method stub
					}
					
					@Override
					public void addLobbyComponents() {
						miniView.addLobbyComponents();
					}
				};
				lobbyAdpt.addLobbyComponents();
				chatappview.installMiniView(room.getName(), miniView);
				
				// return the resultant Main2Mini Adapter
				return lobbyAdpt;
			}
		});
		
		chatappview = new ChatAppGUI<>(new IChatAppView2ModelAdaptor<ServerRemoteConnectionWrapper, ChatRoomWrapper>() {
//			@Override
//			public void admin() {
//				chatappmodel.admin();
//			}
//
//			@Override
//			public void guest() {
//				chatappmodel.guest();
//			}
			
			@Override
			public void quit() {
				try {
					chatappmodel.stop();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public ServerRemoteConnectionWrapper connectUser(String IP) throws RemoteException {
				return new ServerRemoteConnectionWrapper(chatappmodel.connectTo(IP));
			}

			@Override
			public List<ChatRoomWrapper> getSelectedUserRooms(ServerRemoteConnectionWrapper stub) throws RemoteException {
				List <ChatRoomWrapper> list = new ArrayList<ChatRoomWrapper>();
				stub.getUser().getGroups().forEach((room) -> list.add(new ChatRoomWrapper(room)));
				return list;
			}
			
//			@Override
//			public List<IChatRoom> getMyRooms() throws RemoteException {
//				return chatappmodel.getMyRooms();
//			}
			
			@Override
			public ChatRoomWrapper createRoom(String name)  {
				return new ChatRoomWrapper(chatappmodel.createChatRoom(name));
			}

			@Override
			public void joinRoom(ChatRoomWrapper roomWrapper) throws RemoteException {
				// TODO Auto-generated method stub
				chatappmodel.joinChatRoom(roomWrapper.getRoom());
			}
			
			@Override
			public void inviteUser(ServerRemoteConnectionWrapper stubWrapper, ChatRoomWrapper roomWrapper) {
				chatappmodel.inviteUserToRoom(stubWrapper.getUser(), roomWrapper.getRoom());
			}

			@Override
			public void createLobby() {
				chatappmodel.createLobby();
			}
			
		});
	}
	
	/**
	 * Start the system
	 */
	public void start() {
		chatappmodel.start();
		//chatappmodel.admin();
		chatappview.start();
	}
	
	/**
	 * @param args Inputed arguments for the main method
	 * Main method for the Controller, instantiates and starts the Controller and handles exceptions
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatAppController controller = new ChatAppController();
					controller.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}


