package egc3_lw31.data;

import egc3_lw31.game.model.GameAdapter;
import provided.mixedData.MixedDataKey;

/**
 * @author Rocmeister
 *
 */
public class UpdateScoreData implements IUpdateScoreData {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4634078026219481396L;
	/**
	 * the score 
	 */
	private int score;
	/**
	 * the game adapter key
	 */
	private MixedDataKey<GameAdapter> gameAdapterKey;
	
	/**
	 * @param score -- the score
	 * @param key -- the mixed data key
	 */
	public UpdateScoreData(int score, MixedDataKey<GameAdapter> key) {
		this.score = score;
		this.gameAdapterKey = key;
	}
	
	@Override
	public int getScore() {return score;}
	
	@Override
	public MixedDataKey<GameAdapter> getKey() {return gameAdapterKey;}
	
}
