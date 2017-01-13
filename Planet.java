/**
 * Keeps track of each of the planets
 * 
 * @author Michael Pruss, James Hughes
 * @version April 7, 2015
 */
public class Planet {

	private double planetX;
	private double planetY;
	private double mass;
	private double radius;
	private boolean goal;

	/**
	 * Sets up the attributes of the planets
	 * 
	 * @param planetX
	 *            Planet's center X coordinate
	 * @param planetY
	 *            Planet's center Y coordinate
	 * @param mass
	 *            Planet's mass
	 * @param radius
	 *            Planet's radius relative to itself
	 * @param goal
	 *            The final goal of the level
	 */
	public Planet(double planetX, double planetY, double mass, double radius,
			boolean goal) {
		this.planetX = planetX;
		this.planetY = planetY;
		this.mass = mass;
		this.radius = radius;
		this.goal = goal;
	}

	/**
	 * Returns Planet's center X value
	 * 
	 * @return center X value
	 */
	public double getPlanetX() {
		return planetX;
	}

	/**
	 * Returns Planet's center Y value
	 * 
	 * @return center Y value
	 */
	public double getPlanetY() {
		return planetY;
	}

	/**
	 * Returns Planet's mass
	 * 
	 * @return Planet's mass
	 */
	public double getMass() {
		return mass;
	}

	/**
	 * Returns the Planet's radius
	 * 
	 * @return Planet's radius
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Returns the end goal
	 * 
	 * @return Final goal
	 */
	public boolean getGoal() {
		return goal;
	}

}
