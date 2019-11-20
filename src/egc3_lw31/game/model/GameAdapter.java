package egc3_lw31.game.model;

/**
 * @author Rocmeister
 *
 */
public class GameAdapter implements IGameAdapter {
	/**
	 * Game model
	 */
	private GameModel model;
	
	/**
	 * @param gameModel -- the game model
	 */
	public GameAdapter(GameModel gameModel) {
		model = gameModel;
	}
	
	@Override
	public void updatePoints(int newScore) {
		model.updatePoints(newScore);
	}
}
