package egc3_lw31.data;

import egc3_lw31.game.controller.GameController;

/**
 * An interface that defines a factory that instantiates 
 * a specific game
 */
public interface IGameFac { 
  /**
   * Instantiate the specific IUpdateStrategy for which this factory is defined.
   * @return An IUpdateStrategy instance.
   */
  public GameController make();
}
