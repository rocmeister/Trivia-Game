package egc3_lw31.game.controller;

import java.awt.EventQueue;

import common.ICmd2ModelAdapter;
import common.IGroup;
import common.IMember;
import egc3_lw31.game.model.GameAdapter;
import egc3_lw31.game.model.GameModel;
import egc3_lw31.game.model.IModel2ViewAdapter;
import egc3_lw31.game.view.GameView;
import egc3_lw31.game.view.IGameView2ModelAdapter;
import provided.mixedData.MixedDataKey;

/**
 * @author Rocmeister
 * The GameController
 */
public class GameController {
	/**
	 * Game model.
	 */
	private GameModel gameModel;
	
	/**
	 * Game view.
	 */
	private GameView gameView;
	
	/**
	 * The command to model adapter
	 */
	private ICmd2ModelAdapter c2mAdpt;

	/**
	 * @param fullGroup -- the full roster
	 * @param teamGroup -- the team group
	 * @param mddKey -- the mdd key for retrieving game adapter
	 * @param serverStub --  the stub from the server
	 * @param c2mAdapter --  the c2madapter from the client
	 * @param routeOption -- the route option for Asia or Europe
	 */
	public GameController(IGroup fullGroup, IGroup teamGroup, MixedDataKey<GameAdapter> mddKey, IMember serverStub, 
			ICmd2ModelAdapter c2mAdapter, int routeOption) {
		gameModel = new GameModel(fullGroup, teamGroup, mddKey, serverStub, new IModel2ViewAdapter() {
			@Override
			public void displayPoints(int points) {
				gameView.displayPoints(points);
			}
		});
		
		gameView = new GameView(routeOption, new IGameView2ModelAdapter() {

			@Override
			public void submitScore(int score) {
				gameModel.submitPoints(score);
			}

			@Override
			public void updatePoints(int points) {
				gameModel.updatePoints(points);
			}});
		this.c2mAdpt = c2mAdapter;
	}
	
	/**
	 * Start the system
	 */
	public void start() {
		gameModel.start();
//		gameView.start();
		this.c2mAdpt.addComponentFactory(() -> {
			gameView.start();
			return gameView;
		});
	}
	
	// for testing
	/**
	 * @param args -- args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//GameController controller = new GameController();
					//controller.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
