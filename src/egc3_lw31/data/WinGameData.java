package egc3_lw31.data;

/**
 * Data for winning a game
 */
public class WinGameData implements IWinGameData {
	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = 4863545375588528643L;
	/**
	 * Stub of the member to add
	 */
	private String winningTeams;
	
	/**
	 * Constructor
	 * @param winners Winning teams
	 */
	public WinGameData(String winners) {
		this.winningTeams = winners;
	}

	/**
	 * Gets the winning teams
	 * @return The winning teams
	 */
	@Override
	public String getWinner() {
		return winningTeams;
	}
}

