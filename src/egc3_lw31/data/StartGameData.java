package egc3_lw31.data;

import common.IGroup;
import common.IMember;
import egc3_lw31.game.model.GameAdapter;
import provided.mixedData.MixedDataKey;

/**
 * @author Rocmeister
 *
 */
public class StartGameData implements IStartGameData {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5261454640069037937L;

	/**
	 * Group with all members
	 */
	private IGroup fullGroup;
	
	/**
	 * Group with only team members
	 */
	private IGroup teamGroup;
	
	/**
	 * Key for each player's MDD for other players to access this player's game adapter
	 */
	private MixedDataKey<GameAdapter> mddKey;
	
	/**
	 * IMember stub of the server
	 */
	private IMember serverStub;
	
	/**
	 * Player's order index
	 */
	private int playerIndex;

	/**
	 * Constructor for start game data
	 * @param allMbrGroup Group with all the players of the game
	 * @param teamMbrGroup Group with only the players on the same team
	 * @param key MDD key that is the same across the board
	 * @param repStub IMember of the server
	 * @param playerIndex player's order index
	 */
	public StartGameData(IGroup allMbrGroup, IGroup teamMbrGroup, MixedDataKey<GameAdapter> key, IMember repStub, 
			int playerIndex) {
		this.fullGroup = allMbrGroup;
		this.teamGroup = teamMbrGroup;
		this.mddKey = key;
		this.serverStub = repStub;
		this.playerIndex = playerIndex;
	}

	/**
	 * Getter for full group with all players
	 */
	@Override
	public IGroup getFullGroup() {
		return fullGroup;
	}
	
	/**
	 * Getter for team group with all players in team
	 */
	@Override
	public IGroup getTeamGroup() {
		return teamGroup;
	}
	
	/**
	 * Getter for MDD key
	 */
	@Override
	public MixedDataKey<GameAdapter> getMDDKey() {
		return mddKey;
	}
	
	/**
	 * Getter for server stub
	 */
	@Override
	public IMember getServerStub() {
		return serverStub;
	}

	@Override
	public int getPlayerIndex() {
		return this.playerIndex;
	}
}
