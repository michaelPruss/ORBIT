/**
 * Keeps track of the player's attributes
 * 
 * @author Michael Pruss, James Hughes
 * @version April 7, 2015
 */
public class Player {

	private double x;
	private double y;
	private double angleShip;
	private double velocity;
	private boolean beginGame;

	/**
	 * Keeps track of the player's attributes
	 * 
	 * @param x
	 *            The player's x value
	 * @param y
	 *            The player's y value
	 * @param angleShip
	 *            The player's angle
	 * @param velocity
	 *            The player's velocity
	 * @param beginGame
	 *            Whether the game has begun
	 */
	public Player(double x, double y, double angleShip, double velocity,
			boolean beginGame) {
		this.x = x;
		this.y = y;
		this.angleShip = angleShip;
		this.velocity = velocity;
		this.beginGame = beginGame;
	}

	/**
	 * Sets up new game
	 * 
	 * @param x
	 *            The player's X value
	 * @param y
	 *            The player's Y value
	 * @param angleShip
	 *            The angle the ship will be spawned with
	 * @param velocity
	 *            Ship's velocity
	 */
	public void newGame(double x, double y, double angleShip, double velocity) {
		this.x = x;
		this.y = y;
		this.velocity = velocity;
		this.angleShip = angleShip;
		this.beginGame = false;
	}

	/**
	 * Sets X value
	 * 
	 * @param x
	 *            The X value
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Gets the X value
	 * 
	 * @return the X value
	 */
	public double getX() {
		return x;
	}

	/**
	 * Sets Y value
	 * 
	 * @param y
	 *            The Y value
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Gets Y value
	 * 
	 * @return the Y value
	 */
	public double getY() {
		return y;
	}

	/**
	 * Sets angle of the ship
	 * 
	 * @param angleShip
	 *            the angle of the ship
	 */
	public void setAngleShip(double angleShip) {
		this.angleShip = angleShip;
	}

	/**
	 * Gets angle of the ship
	 * 
	 * @return angle of the ship
	 */
	public double getAngleShip() {
		return angleShip;
	}

	/**
	 * Sets velocity
	 * 
	 * @param velocity
	 *            The velocity
	 */
	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	/**
	 * Gets velocity
	 * 
	 * @return ship's velocity
	 */
	public double getVelocity() {
		return velocity;
	}

	/**
	 * Sets whether the game has begun
	 * 
	 * @param beginGame
	 *            Whether the game begun
	 */
	public void setBeginGame(boolean beginGame) {
		this.beginGame = beginGame;
	}

	/**
	 * Gets whether the game has begun
	 * 
	 * @return whether the game has begun
	 */
	public boolean getBeginGame() {
		return beginGame;
	}

}
