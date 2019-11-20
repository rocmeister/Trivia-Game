package egc3_lw31.data;


import common.IGroup;
import provided.mixedData.MixedDataKey;

/**
 * Data for submitting score
 *
 */
@SuppressWarnings("rawtypes")
public class SubmitGameScoreData implements ISubmitGameScoreData {
	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = -3589562336618660867L;

	/**
	 * Group with all members
	 */
	private int score;
	
	/**
	 * Group with only team members
	 */
	private IGroup teamGroup;
	
	/**
	 * Key for mdd
	 */
	private MixedDataKey mddKey;	

	/**
	 * Constructor for start game data
	 * @param points Number of points a team is submitting
	 * @param teamRoom Group for team
	 * @param key -- the mdd key
	 */
	public SubmitGameScoreData(int points, IGroup teamRoom, MixedDataKey key) {
		this.score = points;
		this.teamGroup = teamRoom;
		this.mddKey = key;
	}

	@Override
	public IGroup getTeamGroup() {
		return teamGroup;
	}

	@Override
	public int getScore() {
		return score;
	}
	
	@Override
	public MixedDataKey getMDDKey() {
		return mddKey;
	}
}
