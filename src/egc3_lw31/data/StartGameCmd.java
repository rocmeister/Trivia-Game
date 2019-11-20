package egc3_lw31.data;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JPanel;

import common.IGroup;
import common.IMember;
import common.message.AMessageCmd;
import common.message.GroupDataPacket;
import egc3_lw31.game.controller.GameController;
import egc3_lw31.game.model.GameAdapter;
import provided.datapacket.IDataPacketID;
import provided.mixedData.MixedDataKey;

/**
 * @author Rocmeister
 *
 */
public class StartGameCmd extends AMessageCmd<IStartGameData>{

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = -576087956761808266L;
	
	/**
	 * A convenient map
	 */
	private Map<String, Integer> route2Number = new HashMap<>();  

	/**
	 * Constructor
	 */	
	public StartGameCmd() {init();}
	
	/**
	 * Returns an IStrategyFac that can instantiate the strategy specified by
	 * classname. Returns a factory for a beeping error strategy if classname is null. 
	 * The toString() of the returned factory is the classname.
	 * @param fullGroup -- the full roster group
	 * @param teamGroup -- the team group
	 * @param serverStub -- the server's member stub
	 * @param mddKey -- the mdd key
	 * @param routeOption -- Asia or Europe
	 * 
	 * @return A factory to make that strategy
	 */
	public IGameFac makeGameFac(IGroup fullGroup, IGroup teamGroup, IMember serverStub, MixedDataKey<GameAdapter> mddKey, 
			int routeOption) {
	    return new IGameFac() {
	        /**
	         * Instantiate a game with the corresponding full group, team group, and mdd key
	         * @return An IGameFactory instance
	         */
	        public GameController make() {
	        	GameController gameController = new GameController(fullGroup, teamGroup, mddKey, serverStub,
	        			_cmd2ModelAdpt, routeOption);
	        	gameController.start();
	        	return gameController;
	        }
	    };
	}

	@Override
	public Void apply(IDataPacketID index, GroupDataPacket<IStartGameData> host, Void... params) {		
		this._cmd2ModelAdpt.addComponentFactory(new Supplier<Component> () {
			@Override
			public Component get() {
				IStartGameData data = host.getData();
				IGroup fullGroup = data.getFullGroup();
				IGroup teamGroup = data.getTeamGroup();
				IMember serverStub = data.getServerStub();
				MixedDataKey<GameAdapter> mddKey = data.getMDDKey();
				JPanel startGamePnl = new JPanel();
				int playerIndex = data.getPlayerIndex();
				
				/* Create start route option buttons */
				JButton startBtn0 = new JButton("Passport to Europe");
				startBtn0.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						makeGameFac(fullGroup, teamGroup, serverStub, mddKey, route2Number.get("Europe")).make();
						startGamePnl.setVisible(false);
					}
				});
				
				JButton startBtn1 = new JButton("All About Asia");
				startBtn1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						makeGameFac(fullGroup, teamGroup, serverStub, mddKey, route2Number.get("Asia")).make();
						startGamePnl.setVisible(false);
					}
				});
				/* End of route option buttons */
				
				//startBtn.setToolTipText("Start the game.");
				if (playerIndex == 1) {
					startGamePnl.add(startBtn1);
				} else {
					startGamePnl.add(startBtn0); // add the 0-th set of options by default
				}
				startGamePnl.setBounds(150, 150, 800, 488);
				startGamePnl.setVisible(true);

				return startGamePnl;
			}
		});
		return null;
	}
	
	/**
	 * helper to do	some initialization works
	 */
	private void init() {
		route2Number.put("Africa", 0);
		route2Number.put("Americas", 1);
		route2Number.put("Asia", 2);
		route2Number.put("Europe", 3);
	}
}
