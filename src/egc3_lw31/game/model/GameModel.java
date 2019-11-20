package egc3_lw31.game.model;

import java.rmi.RemoteException;

import common.IGroup;
import common.IMember;
import common.message.GroupDataPacket;
import egc3_lw31.data.ISubmitGameScoreData;
import egc3_lw31.data.SubmitGameScoreData;
import provided.mixedData.MixedDataDictionary;
import provided.mixedData.MixedDataKey;

/**
 * @author Rocmeister
 *
 */
public class GameModel {
	
	/**
	 * Group with only players from your team
	 */
	private IGroup teamGroup;
	
	/**
	 * Key for player's MDD
	 */
	private MixedDataKey<GameAdapter> mddKey;
	
	/**
	 * Mixed Data Dictionary specific to player
	 */
	private MixedDataDictionary mdd;
	
	/**
	 * Game adapter for this game instance.
	 */
	private GameAdapter gameAdapter;
	
	/**
	 * Model to view adapter.
	 */
	private IModel2ViewAdapter m2vAdpt;
	
	/**
	 * Counts number of points team has
	 */
	private int points = 0;
	
	/**
	 * Server stub
	 */
	private IMember serverStub;
	
	/**
	 * Constructor for Game Model
	 * @param allTeamsGroup Group with all players
	 * @param playerGroup Group with only players from same team
	 * @param key Key of MDD for this player's MDD
	 * @param stub -- server stub
	 * @param model2ViewAdpt -- game to server adapter
	 */
	public GameModel(IGroup allTeamsGroup, IGroup playerGroup, MixedDataKey<GameAdapter> key, IMember stub, IModel2ViewAdapter model2ViewAdpt) {
		teamGroup = playerGroup;
		mddKey = key;
		m2vAdpt = model2ViewAdpt;
		serverStub = stub;
	}

	/**
	 * Start model
	 */
	public void start() {
		mdd = new MixedDataDictionary();
		gameAdapter = new GameAdapter(this);
		mdd.put(mddKey, gameAdapter);
	}

	/**
	 * Update number of points
	 * @param increment Number of points to increment
	 */
	public void updatePoints(int increment) {
		points = points + increment;
		m2vAdpt.displayPoints(points);
	}

	/**
	 * Submit current number of points
	 * @param score -- Number of points to submit
	 */
	public void submitPoints(int score) {
		ISubmitGameScoreData data = new SubmitGameScoreData(score, teamGroup, mddKey);
		GroupDataPacket<ISubmitGameScoreData> dp = new GroupDataPacket<>(data, teamGroup.getMembers().get(0));
		try {
			serverStub.receiveData(dp);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}	
}

