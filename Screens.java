/**
 * keeps track which screen is selected
 * 
 * @author Michael Pruss, James Hughes
 * @version April 7, 2015
 */
public class Screens {
	private boolean onMain;
	private boolean gameOver;
	private boolean onGameOverMenu;
	private boolean onLevelSelect;
	private boolean onInstructions;
	private boolean onOptions;

	/**
	 * Initilizes all screens
	 */
	public Screens() {
		onMain = true;
		gameOver = true;
		setOnGameOverMenu(false);
		onLevelSelect = false;
		onInstructions = false;
		onOptions = false;
	}

	/**
	 * Gets whether the player is on the main screen
	 * 
	 * @return whether the player is on the main screen
	 */
	public boolean getMain() {
		return onMain;
	}

	/**
	 * Gets whether the player is on the gameover screen
	 * 
	 * @return Gets whether the player is on the gameover screen
	 */
	public boolean getGameOver() {
		return gameOver;
	}

	/**
	 * Gets whether the player is on the level selection screen
	 * 
	 * @return whether the player is on the level selection screen
	 */
	public boolean getOnLevelSelect() {
		return onLevelSelect;
	}

	/**
	 * Gets whether the player is on the instruction screen
	 * 
	 * @return whether the player is on the instruction screen
	 */
	public boolean getOnInstruction() {
		return onInstructions;
	}

	/**
	 * Gets whether the player is on the instruction screen
	 * 
	 * @return whether the player is on the instruction screen
	 */
	public boolean getOnOptions() {
		return onOptions;
	}

	/**
	 * Sets whether the player is on the main screen
	 * 
	 * @param main
	 *            whether the player is on the main screen
	 */
	public void setMain(boolean main) {
		this.onMain = main;
	}

	/**
	 * Sets whether the player is on the gameover screen
	 * 
	 * @param gameOver
	 *            whether the player is on the gameover screen
	 */
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	/**
	 * Sets whether the player is on the level selection screen
	 * 
	 * @param levelSelect
	 *            whether the player is on the level selection screen
	 */
	public void setOnLevelSelect(boolean levelSelect) {
		this.onLevelSelect = levelSelect;
	}

	/**
	 * Sets whether the player is on the instruction screen
	 * 
	 * @param instructions
	 *            whether the player is on the instruction screen
	 */
	public void setOnInstructions(boolean instructions) {
		this.onInstructions = instructions;
	}

	/**
	 * Sets whether the player is on the instruction screen
	 * 
	 * @param options
	 *            whether the player is on the instruction screen
	 */
	public void setOnOptions(boolean options) {
		this.onOptions = options;
	}

	/**
	 * Checks if the player is on the game over menu
	 * 
	 * @return if the player is on the game over menu
	 */
	public boolean getOnGameOverMenu() {
		return onGameOverMenu;
	}

	/**
	 * Sets whether the player is on game over menu
	 * 
	 * @param onGameOverMenu
	 *            whether the player is on game over menu
	 */
	public void setOnGameOverMenu(boolean onGameOverMenu) {
		this.onGameOverMenu = onGameOverMenu;
	}

}
