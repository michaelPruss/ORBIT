import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * ORBIT- a gravity physics game where the goal is to use the planets' gravity
 * to get to the end goal.
 * 
 * @author Michael Pruss, James Hughes
 * @version April,7, 2015
 */
public class OrbitMain extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	// Set up objects to be used by the game
	private JMenuItem exitOption, mainMenuOption, resetHighScoreOption, controlMenuItem, aboutMenuItem;
	Player ship = new Player(10.0, 700.0, 0.11, 100.0, false);
	LevelCreator level = new LevelCreator();
	Screens screens = new Screens();
	HighScore highScore = new HighScore();

	public OrbitMain() {
		// Set up the frame and the grid
		super("ORBIT");
		setLocation(0, 0);
		setIconImage(Toolkit.getDefaultToolkit().getImage("ORBITtabicon.png"));

		// Set up the window container
		Container contentPane = getContentPane();
		contentPane.add(new GamePanel(), BorderLayout.CENTER);

		// Adds the menu and menu items to the frame
		mainMenuOption = new JMenuItem("Main Menu");
		mainMenuOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mainMenuOption.addActionListener(this);

		resetHighScoreOption = new JMenuItem("Reset High Score");
		resetHighScoreOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		resetHighScoreOption.addActionListener(this);

		exitOption = new JMenuItem("Exit");
		exitOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		exitOption.addActionListener(this);

		// Set up the Help Menu
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');
		controlMenuItem = new JMenuItem("Controls...", 'R');
		controlMenuItem.addActionListener(this);
		helpMenu.add(controlMenuItem);
		aboutMenuItem = new JMenuItem("About...", 'A');
		aboutMenuItem.addActionListener(this);
		helpMenu.add(aboutMenuItem);

		// Add each MenuItem to the Game Menu (with a separator)
		JMenu gameMenu = new JMenu("Game");
		gameMenu.add(mainMenuOption);
		gameMenu.add(resetHighScoreOption);
		gameMenu.add(exitOption);
		JMenuBar mainMenu = new JMenuBar();
		mainMenu.add(gameMenu);
		mainMenu.add(helpMenu);
		// Set the menu bar for this frame to mainMenu
		setJMenuBar(mainMenu);
	}

	/**
	 * Responds to a Menu Event. frame implements ActionListener
	 * 
	 * @param event
	 *            the event that triggered this method
	 */
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == exitOption) // Selected "Exit"
		{
			hide();
			System.exit(0);
		} else if (event.getSource() == mainMenuOption) // Selected "Main Menu"
		{
			screens.setMain(true);
			screens.setGameOver(true);
			screens.setOnGameOverMenu(false);
			screens.setOnLevelSelect(false);
			screens.setOnInstructions(false);
			screens.setOnOptions(false);
			repaint();
		} else if (event.getSource() == resetHighScoreOption) // Selected "Reset
																// High Score"
		{
			if (!(!screens.getMain() && !screens.getOnInstruction() && !screens.getOnLevelSelect()
					&& !screens.getOnOptions())) {
				highScore.resetHighScore();
			}
		} else if (event.getSource() == controlMenuItem) // Selected "Controls"
		{
			JOptionPane.showMessageDialog(this,
					"The goal is to reach the cat." + "\nUse your mouse to aim and fire."
							+ "\nThe farther you click from the space shuttle," + "\nThe faster your ship will go."
							+ "\nchallange yourself with the fastest route and" + "\nprogress through the levels. "
							+ "\n\nGood luck!",
					"Controls", JOptionPane.INFORMATION_MESSAGE);
		} else if (event.getSource() == aboutMenuItem) // Selected "About"
		{
			JOptionPane.showMessageDialog(this, "By: Michael and James", "About ORBIT",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	// Inner class for panel
	private class GamePanel extends JPanel {
		// Final variables that remain constant
		private static final long serialVersionUID = 1L;
		private final double G = 6.7 * Math.pow(10, -11);
		private final static double PI = Math.PI;
		private final static int IMAGE_WIDTH = 8;
		private final static int IMAGE_HEIGHT = 8;
		// Variables that are needed for gravity calculations
		private double forceG;
		private double massShip;
		private double radius;
		private double rotateAngle;
		private double anglePlanet;
		private double acceleration;
		private double velX;
		private double acclY;
		private double acclX;
		private double velY;
		private double initialVelocity;
		private int levelNumb;
		private int rowY;
		private int columnY;
		// Variables to turn on or off music and to play the explosion gif
		private boolean playExplosion;
		private boolean musicOn;
		private boolean soundEffectsOn;
		// variables that deal with timer and record time
		private Timer timer;
		private double time;
		private double timeExplosion;

		// Variables to do with the planet objects
		private int globalIndex;
		private int catIndex;
		private int astroidIndex;
		private Planet[] planetArray;
		private int[] asteroidArray;

		// Image variables
		private Image[] gridImages;
		private Image[] astroidImages;
		private Image shipImage;
		private Image backgroundImage;
		private Image ui;
		private Image arrow;
		private Image gameOverScreen;
		private Image mainMenu;
		private Image levelSelect;
		private Image instructions;
		private Image options;
		private Image explosion;
		private Image muteOption;
		private Image unMuteOption;

		// Audio variables
		private AudioClip catSound;
		private AudioClip explosionSound;
		private AudioClip soundTrack;
		private AudioClip launchSound;

		/**
		 * Constructor for the game panel
		 */
		public GamePanel() {
			// Set up window size
			Dimension size = new Dimension(158 * IMAGE_WIDTH, 90 * IMAGE_HEIGHT);
			this.setPreferredSize(size);

			// Initialize variables
			levelNumb = 1;
			massShip = 74.8427;
			radius = 0.0;
			rotateAngle = PI / 2 - ship.getAngleShip();
			initialVelocity = ship.getVelocity();
			playExplosion = false;
			musicOn = true;
			soundEffectsOn = true;
			// Create new game
			ship.newGame(columnY * (double) IMAGE_WIDTH - 10.0, rowY * (double) IMAGE_HEIGHT, ship.getAngleShip(),
					initialVelocity);
			// Set up asteroids
			astroidImages = new Image[4];
			astroidImages[0] = new ImageIcon("Michaelasteroid1.png").getImage();
			astroidImages[1] = new ImageIcon("Michaelasteroid2.png").getImage();
			astroidImages[2] = new ImageIcon("Michaelasteroid3.png").getImage();
			astroidImages[3] = new ImageIcon("Michaelasteroid4.png").getImage();

			// Set up images that will be used by the grid
			gridImages = new Image[26];
			gridImages[1] = new ImageIcon("Michaelplanet2.png").getImage();// B
																			// Small
			gridImages[2] = new ImageIcon("Michaelplanet4.png").getImage();// C
																			// Small
			gridImages[3] = new ImageIcon("Michaelplanet7.png").getImage();// D
																			// Small
			gridImages[4] = new ImageIcon("Michaelplanet11.png").getImage();// E
																			// Small
			gridImages[5] = new ImageIcon("Michaelplanet1.png").getImage();// F
																			// Medium
			gridImages[6] = new ImageIcon("Michaelplanet6.png").getImage();// G
																			// Medium
			gridImages[7] = new ImageIcon("Michaelplanet8.png").getImage();// H
																			// Medium
			gridImages[8] = new ImageIcon("Michaelplanet10.png").getImage();// I
																			// Medium
			gridImages[9] = new ImageIcon("Michaelplanet3.png").getImage();// J
																			// Large
			gridImages[10] = new ImageIcon("Michaelplanet5.png").getImage();// K
																			// Large
			gridImages[11] = new ImageIcon("Michaelplanet9.png").getImage();// L
																			// Large
			gridImages[12] = new ImageIcon("Michaelplanet12.png").getImage();// M
																				// Large
			gridImages[13] = new ImageIcon("Michaelasteroid1.png").getImage();// N
																				// Asteroid

			gridImages[20] = new ImageIcon("Michaelcathole4.png").getImage();// U
																				// Cats
																				// DEMONIC
			gridImages[21] = new ImageIcon("Michaelcathole3.png").getImage();// V
																				// Cats
																				// Hard
			gridImages[22] = new ImageIcon("Michaelcathole2.png").getImage();// W
																				// Cats
																				// Medium
			gridImages[23] = new ImageIcon("Michaelcathole.png").getImage();// X
																			// Cats
																			// Easy

			// Set up other images
			shipImage = new ImageIcon("MichaelSpaceshuttle.png").getImage();
			backgroundImage = new ImageIcon("Michaelbackground.png").getImage();
			ui = new ImageIcon("Michaelgameuismall.png").getImage();
			arrow = new ImageIcon("Michaelfullarrow.png").getImage();
			gameOverScreen = new ImageIcon("Michaelgameover.png").getImage();
			mainMenu = new ImageIcon("Michaelmainmenu.png").getImage();
			levelSelect = new ImageIcon("Michaellevelselect.png").getImage();
			instructions = new ImageIcon("Michaelintructionsmenu.png").getImage();
			options = new ImageIcon("MichaeloptionsMenu.png").getImage();
			explosion = new ImageIcon("explosion.gif").getImage();
			muteOption = new ImageIcon("MichaelmuteButton.png").getImage();
			unMuteOption = new ImageIcon("MichaelunmuteButton.png").getImage();

			// Create sound clips
			catSound = Applet.newAudioClip(getCompleteURL("cat.wav"));
			explosionSound = Applet.newAudioClip(getCompleteURL("shortexplosion1.wav"));
			soundTrack = Applet.newAudioClip(getCompleteURL("Sound Track3.wav"));
			launchSound = Applet.newAudioClip(getCompleteURL("playerLaunch.wav"));

			soundTrack.loop();
			// Set up mouse and keyboard
			this.setFocusable(true);
			this.addKeyListener(new KeyHandler());
			this.requestFocusInWindow();
			addMouseListener(new MouseHandler());
			addMouseMotionListener(new MouseMotionHandler());
			time = 0.0;
			timeExplosion = 0.0;
			// Set up and start timer (every 15 milliseconds)
			timer = new Timer(15, new TimerEventHandler());
			timer.start();
		}

		/**
		 * Get URL for sound files
		 * 
		 * @param fileName
		 *            The file name
		 * @return URL of sound file
		 */
		public URL getCompleteURL(String fileName) {
			try {
				return new URL("file:" + System.getProperty("user.dir") + "/" + fileName);
			} catch (MalformedURLException e) {
				System.err.println(e.getMessage());
			}
			return null;
		}

		/**
		 * Draws graphics
		 * 
		 * @param g
		 *            Graphics
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			// Draws the level grid when player is on the game screen
			if (!screens.getMain() && !screens.getOnLevelSelect() && !screens.getOnInstruction()
					&& !screens.getOnOptions()) {
				g.drawImage(backgroundImage, 0, 0, this);
				int asteroidCount = 0;
				// Draws level grid
				for (int row = 0; row < level.getLevel().length; row++) {
					for (int column = 0; column < level.getLevel()[row].length; column++) {
						int imageNo = level.getLevel()[row][column];
						if (imageNo == 13) {
							// Draws a random asteroid from 4 different types of
							// asteroids
							g.drawImage(astroidImages[asteroidArray[asteroidCount]], column * IMAGE_WIDTH,
									row * IMAGE_HEIGHT, this);
							asteroidCount++;
							if (asteroidCount >= asteroidArray.length)
								asteroidCount = 0;
						}
						// Draws planets
						else if (imageNo > 0 && imageNo < 13) {
							g.drawImage(gridImages[imageNo], column * IMAGE_WIDTH, row * IMAGE_HEIGHT, this);
						}
						// Draws cats
						else if (imageNo >= 20 && imageNo <= 23) {
							g.drawImage(gridImages[imageNo], column * IMAGE_WIDTH, row * IMAGE_HEIGHT, this);
						}
					}
				}
				// Draws the arrow
				if (!ship.getBeginGame()) {
					// Rotates the image depending where the mouse is
					g2d.rotate(rotateAngle, ship.getX() + 4 / 2, ship.getY() + 4 / 2);
					g2d.drawImage(arrow, -440, -70, this);
					g2d.rotate(-rotateAngle, ship.getX() + 4 / 2, ship.getY() + 4 / 2);
				}

				// Draws the ship
				if (!playExplosion) {
					// Rotates the image depending on the rotation angle
					g2d.rotate(rotateAngle, ship.getX() + 32 / 2, ship.getY() + 48 / 2);
					g2d.drawImage(shipImage, (int) (ship.getX()), (int) (ship.getY()), this);
					g2d.rotate(-rotateAngle, ship.getX() + 32 / 2, ship.getY() + 48 / 2);
				}

				// Draws UI (User Interface)
				g.drawImage(ui, 1065, 660, this);

				g.drawString(String.valueOf(Math.round(ship.getAngleShip() * 180 / PI * 100) / 100.0), 1190, 702);
				g.drawString(String.valueOf(Math.round(initialVelocity * 10) / 10.0), 1110, 702);
				// Draws explosion gif when needed
				if (playExplosion) {
					g.drawImage(explosion, (int) ship.getX() - 58, (int) ship.getY() - 54, this);
				}
			}
			// Draws game over
			if (screens.getGameOver()) {
				g.setFont(new Font("Helvetica LT Std", Font.BOLD, 40));
				g.drawImage(gameOverScreen, 100, -150, this);
				g.drawString(String.valueOf(Math.round(highScore.getHighScore()[levelNumb - 1] * 10) / 10.0), 640, 615);
			}
			// Draws main menu
			if (screens.getMain()) {
				g.drawImage(mainMenu, 0, 0, this);
			}
			// Draws level selection menu
			if (screens.getOnLevelSelect()) {
				g.setFont(new Font("Helvetica LT Std", Font.BOLD, 80));
				g.drawImage(levelSelect, 0, 0, this);
				if (levelNumb < 10) {
					g.drawString("0" + String.valueOf(levelNumb), 330, 450);
				} else if (levelNumb >= 10) {
					g.drawString(String.valueOf(levelNumb), 330, 450);
				}
			}
			// Draws instructions menu
			if (screens.getOnInstruction()) {
				g.drawImage(instructions, 0, 0, this);
			}
			// Draws options menu
			if (screens.getOnOptions()) {
				g.drawImage(options, 0, 0, this);
				if (!musicOn) {
					g.drawImage(muteOption, 682, 253, this);
				} else
					g.drawImage(unMuteOption, 682, 253, this);
				if (!soundEffectsOn) {
					g.drawImage(muteOption, 682, 424, this);
				} else
					g.drawImage(unMuteOption, 682, 424, this);
			}
			// Disposes unwanted objects
			g.dispose();
			g2d.dispose();
		}

		/**
		 * Gets the player position on the grid
		 */
		public void getPlayerPosition() {
			for (int row = 0; row < level.getLevel().length; row++) {
				for (int column = 0; column < level.getLevel()[row].length; column++) {
					if (level.getLevel()[row][column] == 24) {
						rowY = row;
						columnY = column;
					}
				}
			}
		}

		/**
		 * Creates level
		 * 
		 * @param levelNumb
		 *            The level number
		 */
		public void createLevel(int levelNumb) {
			level.createLevel(levelNumb);
			planetArray();
			getPlayerPosition();
		}

		/**
		 * Sets up the planetArray depending on where there are planets in the
		 * level
		 */
		public void planetArray() {
			int arrayLength = 0;

			// Counts how many planets there are in the level
			for (int row = 0; row < level.getLevel().length; row++) {
				for (int column = 0; column < level.getLevel()[row].length; column++) {
					if (level.getLevel()[row][column] > 0 && level.getLevel()[row][column] < 24) {
						arrayLength++;
					}
				}
			}
			// Counts how many asteroids there are in the level
			int totalAsteroids = 0;
			for (int row = 0; row < level.getLevel().length; row++) {
				for (int column = 0; column < level.getLevel()[row].length; column++) {
					if (level.getLevel()[row][column] == 13) {
						totalAsteroids++;
					}
				}
			}

			// Creates an array to keep track of the asteroids
			asteroidArray = new int[totalAsteroids];
			// Adds a random value between 0 to 3 to the array
			for (int count = 0; count < asteroidArray.length; count++) {
				asteroidArray[count] = (int) (Math.floor(Math.random() * (4)));
			}

			int index = 0;
			double centrePlanetX;
			double centrePlanetY;
			double massPlanet = 0.0;
			double radiusPlanet = 0.0;
			boolean goal = false;

			// Creates planet array
			planetArray = new Planet[arrayLength];

			// Goes through the level and adds planets to the planet array
			for (int row = 0; row < level.getLevel().length; row++) {
				for (int column = 0; column < level.getLevel()[row].length; column++) {
					if (level.getLevel()[row][column] > 0 && level.getLevel()[row][column] < 24) {
						// For small planet
						if (level.getLevel()[row][column] >= 1 && level.getLevel()[row][column] <= 4) {
							radiusPlanet = 48.0;
							massPlanet = 7.972 * Math.pow(10, 16);
							goal = false;
						}
						// For medium planet
						else if (level.getLevel()[row][column] >= 5 && level.getLevel()[row][column] <= 8) {
							radiusPlanet = 64.0;
							massPlanet = 3.01 * Math.pow(10, 17);
							goal = false;
						}
						// For large planet
						else if (level.getLevel()[row][column] >= 9 && level.getLevel()[row][column] <= 12) {
							radiusPlanet = 80.0;
							massPlanet = 7.972 * Math.pow(10, 17);
							goal = false;
						}
						// For end goal
						else if (level.getLevel()[row][column] >= 20 && level.getLevel()[row][column] <= 23) {
							radiusPlanet = 36.0;
							massPlanet = 7.972 * Math.pow(10, 15);
							goal = true;
						}
						// For asteroids
						else if (level.getLevel()[row][column] == 13) {
							radiusPlanet = 40.0;
							massPlanet = 7.972 * Math.pow(10, 15);
							goal = false;
						}
						// Calculates the center coordinates of the object
						centrePlanetX = column * (double) IMAGE_WIDTH + radiusPlanet;
						centrePlanetY = row * (double) IMAGE_HEIGHT + radiusPlanet;

						// Adds object to planet array
						planetArray[index] = new Planet(centrePlanetX, centrePlanetY, massPlanet, radiusPlanet, goal);
						index++;

					}
				}
			}

		}

		/**
		 * Pythagorean theorem
		 * 
		 * @param fromX
		 *            initial X
		 * @param fromY
		 *            initial Y
		 * @param toX
		 *            final X
		 * @param toY
		 *            final Y
		 * @return the distance between them
		 */
		public double findDistance(double fromX, double fromY, double toX, double toY) {
			double a = Math.abs(fromX - toX);
			double b = Math.abs(fromY - toY);

			return (a * a) + (b * b);
		}

		/**
		 * Collision detection for player
		 */
		public void collision() {
			// Collision detection for the screen limits
			if (ship.getX() + 16 < level.getColumns() * 0.0
					|| ship.getX() + 16 > level.getColumns() * (double) IMAGE_WIDTH
					|| ship.getY() + 24 < level.getRows() * 0.0
					|| ship.getY() + 24 > level.getRows() * (double) IMAGE_HEIGHT) {
				// Reset level
				ship.newGame(columnY * (double) IMAGE_WIDTH, rowY * (double) IMAGE_HEIGHT, ship.getAngleShip(),
						initialVelocity);
				rotateAngle = PI / 2 - ship.getAngleShip();
				initialVelocity = ship.getVelocity();
				repaint();
			}
			int collisionIndex = 0;
			double hypotenuseAstroid = 1000000000000.0;
			// Find the distance between the ship and the planet/cat
			double hypotenusePlanet = ((planetArray[globalIndex].getPlanetX() - ship.getX())
					* (planetArray[globalIndex].getPlanetX() - ship.getX())
					+ (planetArray[globalIndex].getPlanetY() - ship.getY())
							* (planetArray[globalIndex].getPlanetY() - ship.getY()));
			double hypotenuseCat = ((planetArray[catIndex].getPlanetX() - ship.getX())
					* (planetArray[catIndex].getPlanetX() - ship.getX())
					+ (planetArray[catIndex].getPlanetY() - ship.getY())
							* (planetArray[catIndex].getPlanetY() - ship.getY()));
			// Finds the distance between the ship and an asteroid, if there is
			// one or more in the level
			if (asteroidArray.length > 0) {
				hypotenuseAstroid = ((planetArray[astroidIndex].getPlanetX() - ship.getX())
						* (planetArray[astroidIndex].getPlanetX() - ship.getX())
						+ (planetArray[astroidIndex].getPlanetY() - ship.getY())
								* (planetArray[astroidIndex].getPlanetY() - ship.getY()));
			}
			// Determines if a planet or a cat or an asteroid is closer to the
			// ship
			if (hypotenusePlanet < hypotenuseCat && hypotenusePlanet < hypotenuseAstroid)
				collisionIndex = globalIndex;
			else if (hypotenuseCat < hypotenusePlanet && hypotenuseCat < hypotenuseAstroid) {
				collisionIndex = catIndex;
			} else if (hypotenuseAstroid < hypotenuseCat && hypotenuseAstroid < hypotenusePlanet) {
				collisionIndex = astroidIndex;
			}

			// Rotate circle's center point back
			double unrotatedCircleX = Math.cos(rotateAngle)
					* (planetArray[collisionIndex].getPlanetX() - ship.getX() - 16)
					- Math.sin(rotateAngle) * (planetArray[collisionIndex].getPlanetY() - ship.getY() - 24)
					+ ship.getX() - 16;
			double unrotatedCircleY = Math.sin(rotateAngle)
					* (planetArray[collisionIndex].getPlanetX() - ship.getX() - 16)
					+ Math.cos(rotateAngle) * (planetArray[collisionIndex].getPlanetY() - ship.getY() - 24)
					+ ship.getY() - 24;

			// Closest point in the rectangle to the center of circle rotated
			// backwards(unrotated)
			double closestX, closestY;

			// Find the unrotated closest x point from center of unrotated
			// circle
			if (unrotatedCircleX < ship.getX())
				closestX = ship.getX();
			else if (unrotatedCircleX > ship.getX() - 32)
				closestX = ship.getX() - 32;
			else
				closestX = unrotatedCircleX;

			// Find the unrotated closest y point from center of unrotated
			// circle
			if (unrotatedCircleY < ship.getY())
				closestY = ship.getY();
			else if (unrotatedCircleY > ship.getY() - 48)
				closestY = ship.getY() - 48;
			else
				closestY = unrotatedCircleY;

			// Finds the distance between the ship and closest object
			double distance = findDistance(unrotatedCircleX, unrotatedCircleY, closestX, closestY);
			// If collision is with the end goal
			if (planetArray[collisionIndex].getGoal() && distance < ((planetArray[collisionIndex].getRadius() + 20)
					* (planetArray[collisionIndex].getRadius() + 20))) {
				// Display game over (success)
				if (soundEffectsOn)
					catSound.play();
				highScore.submitHighScore(levelNumb - 1, Math.round(time / 1000.0 * 10) / 10.0);
				screens.setGameOver(true);
				screens.setOnGameOverMenu(true);
				repaint();
			}
			// If collision is with an asteroid
			else if (distance < ((planetArray[collisionIndex].getRadius() + 20)
					* (planetArray[collisionIndex].getRadius() + 20))
					&& planetArray[collisionIndex].getRadius() == 40.0) {
				// Resets level
				if (soundEffectsOn)
					explosionSound.play();
				playExplosion = true;
				repaint();
			}
			// If collision is with a planet
			else if (distance < ((planetArray[collisionIndex].getRadius() + 16)
					* (planetArray[collisionIndex].getRadius() + 16))) {
				// Resets level
				if (soundEffectsOn)
					explosionSound.play();
				playExplosion = true;
				repaint();
			}
		}

		// Inner class to take care of background logic
		private class TimerEventHandler implements ActionListener {

			/**
			 * The following method is called each time a timer event is
			 * generated
			 * 
			 * @param event
			 *            the Timer event
			 */
			public void actionPerformed(ActionEvent event) {
				// When the player collides with a planet or an asteroid
				if (playExplosion) {
					timeExplosion += 15.0;
					// Draws explosion gif and resets the level
					if (timeExplosion / 1000.0 > 1.0) {
						ship.newGame(columnY * (double) IMAGE_WIDTH, rowY * (double) IMAGE_HEIGHT, ship.getAngleShip(),
								initialVelocity);
						rotateAngle = PI / 2 - ship.getAngleShip();
						initialVelocity = ship.getVelocity();
						playExplosion = false;
						timeExplosion = 0.0;
					}
				}

				// When the game begins
				if (ship.getBeginGame() && !screens.getGameOver() && !playExplosion) {
					double hypotenuse = 0.0;
					double smallestHypotenuse = 1000000000000.0;
					double smallestHypotenuseAstroid = 1000000000000.0;
					int closestIndex = 0;

					// Calculates closest object
					for (int count = 0; count < planetArray.length; count++) {
						// Finds the closest planet
						if (planetArray[count].getRadius() >= 48.0 && planetArray[count].getRadius() <= 80.0) {
							hypotenuse = ((planetArray[count].getPlanetX() - ship.getX())
									* (planetArray[count].getPlanetX() - ship.getX())
									+ (planetArray[count].getPlanetY() - ship.getY())
											* (planetArray[count].getPlanetY() - ship.getY()));
							if (hypotenuse < smallestHypotenuse) {
								smallestHypotenuse = hypotenuse;
								closestIndex = count;
							}
						}
						// Finds the cat in the level
						else if (planetArray[count].getGoal())
							catIndex = count;
						// Finds the closest asteroid
						else if (planetArray[count].getRadius() == 40.0) {
							hypotenuse = ((planetArray[count].getPlanetX() - ship.getX())
									* (planetArray[count].getPlanetX() - ship.getX())
									+ (planetArray[count].getPlanetY() - ship.getY())
											* (planetArray[count].getPlanetY() - ship.getY()));
							if (hypotenuse < smallestHypotenuseAstroid) {
								smallestHypotenuseAstroid = hypotenuse;
								astroidIndex = count;
							}
						}
					}
					// Index to be used in gravitational calculations with
					// planets
					globalIndex = closestIndex;

					// Calls collision check
					collision();
					// Finds the angle between the ship and the planet
					anglePlanet = Math.atan((planetArray[globalIndex].getPlanetY() - ship.getY())
							/ (planetArray[globalIndex].getPlanetX() - ship.getX()));

					// Uses CAST rules to determine the proper angle
					if (planetArray[globalIndex].getPlanetY() - ship.getY() < 0
							&& planetArray[globalIndex].getPlanetX() - ship.getX() > 0) {
						anglePlanet = -anglePlanet;
					} else if (planetArray[globalIndex].getPlanetY() - ship.getY() < 0
							&& planetArray[globalIndex].getPlanetX() - ship.getX() < 0) {
						anglePlanet = PI - anglePlanet;
					} else if (planetArray[globalIndex].getPlanetY() - ship.getY() > 0
							&& planetArray[globalIndex].getPlanetX() - ship.getX() < 0) {
						anglePlanet = PI + Math.abs(anglePlanet);
					} else if (planetArray[globalIndex].getPlanetY() - ship.getY() > 0
							&& planetArray[globalIndex].getPlanetX() - ship.getX() > 0) {
						anglePlanet = 2 * PI - anglePlanet;
					}

					// Calculate the distance between the planet and the ship
					radius = Math.sqrt((planetArray[globalIndex].getPlanetX() - ship.getX())
							* (planetArray[globalIndex].getPlanetX() - ship.getX())
							+ (planetArray[globalIndex].getPlanetY() - ship.getY())
									* (planetArray[globalIndex].getPlanetY() - ship.getY()));

					// Calculate the force of gravity between the planet and the
					// ship
					forceG = (G * massShip * planetArray[globalIndex].getMass()) / (radius * radius);
					// Calculates acceleration towards the planet
					acceleration = forceG / massShip;
					acclY = -acceleration * Math.sin(anglePlanet);
					acclX = acceleration * Math.cos(anglePlanet);

					// Acceleration affecting X and Y components of velocity
					velX += (acclX * 0.015);
					velY += (acclY * 0.015);

					// Sets magnitude of velocity vector
					ship.setVelocity(Math.sqrt((velX * velX) + (velY * velY)));

					// Uses CAST rule for the rotation angle
					if (velX > 0) {
						rotateAngle = Math.asin(velY / ship.getVelocity()) + PI / 2;
					} else if (velX < 0) {
						rotateAngle = 3 * PI / 2 - Math.asin(velY / ship.getVelocity());
					}

					time += 15.0;

					// Velocity affects X and Y positions of ship
					ship.setX(ship.getX() + (velX * 0.015));
					ship.setY(ship.getY() + (velY * 0.015));

					// Calls collision
					collision();
					// Repaints screen
					repaint();
				}
			}

		}

		// Inner class for mouse input
		private class MouseHandler extends MouseAdapter {
			/**
			 * Method for mouse pressed
			 */
			public void mousePressed(MouseEvent event) {
				// Find where the mouse is on the screen
				Point mousePoint = event.getPoint();
				// When the screen is on the game and it has not begun
				if (!ship.getBeginGame() && !screens.getGameOver()) {
					// Begins the game
					ship.setBeginGame(true);
					// Initializes both X and Y components of velocity
					velX = ship.getVelocity() * Math.cos(ship.getAngleShip());
					velY = -ship.getVelocity() * Math.sin(ship.getAngleShip());
					if (soundEffectsOn)
						launchSound.play();
					repaint();
				}

				// When the screen is on level select
				if (screens.getOnLevelSelect()) {
					if (mousePoint.x > 501 && mousePoint.x < 641 && mousePoint.y > 288 && mousePoint.y < 422
							&& levelNumb < 30) {
						// Increases level number
						levelNumb++;
						repaint();
					} else if (mousePoint.x > 501 && mousePoint.x < 641 && mousePoint.y > 423 && mousePoint.y < 557
							&& levelNumb > 1) {
						// Decreases level number
						levelNumb--;
						repaint();
					} else if (mousePoint.x > 709 && mousePoint.x < 971 && mousePoint.y > 358 && mousePoint.y < 490) {
						// Starts game with level selected
						screens.setGameOver(false);
						screens.setMain(false);
						screens.setOnLevelSelect(false);
						createLevel(levelNumb);
						ship.newGame(columnY * (double) IMAGE_WIDTH, rowY * (double) IMAGE_HEIGHT, 0.11, 600.0);
						rotateAngle = PI / 2 - ship.getAngleShip();
						initialVelocity = ship.getVelocity();
						time = 0.0;
						repaint();
						return;
					} else if (mousePoint.x > 0 && mousePoint.x < 130 && mousePoint.y > 632 && mousePoint.y < 718) {
						// Goes back to main menu
						screens.setOnLevelSelect(false);
						screens.setMain(true);
						repaint();
						return;
					}
				}
				// When the screen is on main menu
				if (screens.getMain()) {
					time = 0.0;
					if (mousePoint.x > 0 && mousePoint.x < 1263 && mousePoint.y > 228 && mousePoint.y < 350) {
						// Begins on 1st level
						screens.setGameOver(false);
						screens.setMain(false);
						levelNumb = 1;
						createLevel(levelNumb);
						ship.newGame(columnY * (double) IMAGE_WIDTH, rowY * (double) IMAGE_HEIGHT, 0.11, 600.0);
						rotateAngle = PI / 2 - ship.getAngleShip();
						initialVelocity = ship.getVelocity();
						time = 0.0;
						repaint();
						return;
					} else if (mousePoint.x > 0 && mousePoint.x < 1263 && mousePoint.y > 351 && mousePoint.y < 470) {
						// Goes to level select
						screens.setMain(false);
						screens.setOnLevelSelect(true);
						repaint();
						return;
					} else if (mousePoint.x > 0 && mousePoint.x < 1263 && mousePoint.y > 470 && mousePoint.y < 595) {
						// Goes to options
						screens.setMain(false);
						screens.setOnOptions(true);
						repaint();
						return;
					} else if (mousePoint.x > 0 && mousePoint.x < 1263 && mousePoint.y > 595 && mousePoint.y < 720) {
						// Goes to instructions
						screens.setMain(false);
						screens.setOnInstructions(true);
						repaint();
						return;
					}
				}

				// When on game over screen
				if (screens.getOnGameOverMenu()) {
					if (mousePoint.x > 211 && mousePoint.x < 1009 && mousePoint.y > 209 && mousePoint.y < 322) {
						// Retry level
						screens.setGameOver(false);
						ship.newGame(columnY * (double) IMAGE_WIDTH, rowY * (double) IMAGE_HEIGHT, ship.getAngleShip(),
								600.0);
						rotateAngle = PI / 2 - ship.getAngleShip();
						initialVelocity = ship.getVelocity();
						time = 0.0;
						screens.setOnGameOverMenu(false);
						repaint();
						return;
					} else if (mousePoint.x > 212 && mousePoint.x < 1009 && mousePoint.y > 325 && mousePoint.y < 440) {
						// Goes to level select
						screens.setOnLevelSelect(true);
						screens.setOnGameOverMenu(false);
						time = 0.0;
						repaint();
						return;
					} else if (mousePoint.x > 211 && mousePoint.x < 1009 && mousePoint.y > 441 && mousePoint.y < 557) {
						// Goes to main menu
						screens.setMain(true);
						screens.setOnGameOverMenu(false);
						time = 0.0;
						repaint();
						return;
					}

				}

				// When on instructions menu
				if (screens.getOnInstruction()) {
					if (mousePoint.x > 0 && mousePoint.x < 134 && mousePoint.y > 631 && mousePoint.y < 720) {
						// Goes back to main menu
						screens.setOnInstructions(false);
						screens.setMain(true);
						repaint();
						return;
					}
				}

				// When on options menu
				if (screens.getOnOptions()) {
					if (mousePoint.x > 0 && mousePoint.x < 134 && mousePoint.y > 631 && mousePoint.y < 720) {
						// Goes back to main menu
						screens.setOnOptions(false);
						screens.setMain(true);
						repaint();
						return;
					} else if (mousePoint.x > 682 && mousePoint.x < 840 && mousePoint.y > 253 && mousePoint.y < 361) {
						// Mutes/Unmutes music
						musicOn = !musicOn;

						// Loops music if the music is on
						if (musicOn)
							soundTrack.loop();
						else
							soundTrack.stop();
						repaint();
					} else if (mousePoint.x > 682 && mousePoint.x < 840 && mousePoint.y > 424 && mousePoint.y < 553) {
						// Mutes/Unmutes sound effects
						soundEffectsOn = !soundEffectsOn;
						repaint();
					}

				}
			}
		}

		// Inner Class to handle mouse movements
		private class MouseMotionHandler extends MouseMotionAdapter {

			/**
			 * Method to handle mouse movement
			 */
			public void mouseMoved(MouseEvent event) {
				// Finds mouse location on the screen
				Point mousePoint = event.getPoint();
				// Determines initial velocity based on mouse position
				if (!ship.getBeginGame() && !screens.getGameOver()) {
					if (Math.sqrt((mousePoint.x - ship.getX()) * (mousePoint.x - ship.getX())
							+ (mousePoint.y - ship.getY()) * (mousePoint.y - ship.getY())) < 601) {
						ship.setVelocity(Math.sqrt((mousePoint.x - ship.getX()) * (mousePoint.x - ship.getX())
								+ (mousePoint.y - ship.getY()) * (mousePoint.y - ship.getY())));

					} else
						ship.setVelocity(600.0);
					// Finds the angle of the ship depending on mouse position
					if (Math.atan((mousePoint.y - ship.getY()) / (mousePoint.x - ship.getX())) < 0.0) {
						ship.setAngleShip(
								Math.abs((Math.atan((mousePoint.y - ship.getY()) / (mousePoint.x - ship.getX())))));
						initialVelocity = ship.getVelocity();
						rotateAngle = PI / 2 - ship.getAngleShip();
					}
					setCursor(Cursor.getDefaultCursor());
				}

				// Sets mouse curses to hand curses when over an option on game
				// over screen
				if (screens.getOnGameOverMenu()) {
					Rectangle imageRectangle = new Rectangle(211, 209, 798, 113);
					Rectangle imageRectangle2 = new Rectangle(211, 441, 798, 113);
					Rectangle imageRectangle3 = new Rectangle(211, 322, 798, 119);
					if (imageRectangle.contains(mousePoint) || imageRectangle2.contains(mousePoint)
							|| imageRectangle3.contains(mousePoint)) {
						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					} else
						setCursor(Cursor.getDefaultCursor());
				}
				// Sets mouse curses to hand curses when over an option on main
				// menu screen

				if (screens.getMain()) {
					Rectangle imageRectangle = new Rectangle(0, 228, 1263, 492);
					if (imageRectangle.contains(mousePoint)) {
						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					} else
						setCursor(Cursor.getDefaultCursor());
				}
				// Sets mouse curses to hand curses when over an option on level
				// selection screen
				if (screens.getOnLevelSelect()) {
					Rectangle imageRectangle = new Rectangle(501, 288, 140, 134);
					Rectangle imageRectangle2 = new Rectangle(501, 423, 140, 134);
					Rectangle imageRectangle3 = new Rectangle(709, 357, 263, 135);
					Rectangle imageRectangl4 = new Rectangle(0, 632, 130, 86);

					if (imageRectangle.contains(mousePoint) && levelNumb < 30) {
						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					} else if (imageRectangle2.contains(mousePoint) && levelNumb > 1) {
						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					} else if (imageRectangle3.contains(mousePoint)) {
						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					} else if (imageRectangl4.contains(mousePoint)) {
						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					} else
						setCursor(Cursor.getDefaultCursor());
				}
				// Sets mouse curses to hand curses when over an option on
				// instruction screen
				if (screens.getOnInstruction()) {
					Rectangle imageRectangle = new Rectangle(0, 631, 134, 89);
					if (imageRectangle.contains(mousePoint)) {
						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					} else
						setCursor(Cursor.getDefaultCursor());
				}
				// Sets mouse curses to hand curses when over an option on
				// options screen
				if (screens.getOnOptions()) {
					Rectangle imageRectangle = new Rectangle(0, 631, 134, 89);
					Rectangle imageRectangle2 = new Rectangle(682, 252, 157, 110);
					Rectangle imageRectangle3 = new Rectangle(682, 423, 157, 110);
					if (imageRectangle.contains(mousePoint) || imageRectangle2.contains(mousePoint)
							|| imageRectangle3.contains(mousePoint)) {
						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					} else
						setCursor(Cursor.getDefaultCursor());
				}

				// Must redraw the panel contents to show whether mouse is over
				// a button
				repaint();
			}
		}

		// Inner class for key events
		private class KeyHandler extends KeyAdapter {
			/**
			 * Method to handle keypresses
			 */
			public void keyPressed(KeyEvent event) {
				// Increases angle
				if (event.getKeyCode() == KeyEvent.VK_LEFT && !ship.getBeginGame()) {
					if (ship.getAngleShip() > PI / 2)
						ship.setAngleShip(PI / 2);
					else
						ship.setAngleShip(ship.getAngleShip() + 0.01);
				}
				// Decreases angle
				else if (event.getKeyCode() == KeyEvent.VK_RIGHT && !ship.getBeginGame()) {
					if (ship.getAngleShip() < 0.0)
						ship.setAngleShip(0.0);
					else
						ship.setAngleShip(ship.getAngleShip() - 0.005);
				}
				// Increases velocity
				else if (event.getKeyCode() == KeyEvent.VK_UP && !ship.getBeginGame()) {
					if (ship.getVelocity() > 600.0)
						ship.setVelocity(600.0);
					else
						ship.setVelocity(ship.getVelocity() + 2.0);
				}
				// Decreases velocity
				else if (event.getKeyCode() == KeyEvent.VK_DOWN && !ship.getBeginGame()) {
					if (ship.getVelocity() < 0.0)
						ship.setVelocity(0.0);
					else
						ship.setVelocity(ship.getVelocity() - 2.0);
				}
				// Launches ship
				else if (event.getKeyCode() == KeyEvent.VK_SPACE && !ship.getBeginGame()) {
					ship.setBeginGame(true);
					velX = ship.getVelocity() * Math.cos(ship.getAngleShip());
					velY = -ship.getVelocity() * Math.sin(ship.getAngleShip());
					if (soundEffectsOn)
						launchSound.play();
				}
				// Goes back to main menu
				else if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
					screens.setMain(true);
					screens.setGameOver(true);
					screens.setOnGameOverMenu(false);
					screens.setOnLevelSelect(false);
					screens.setOnInstructions(false);
					screens.setOnOptions(false);
					repaint();
				}
				// Gets initial velocity and initial angle
				if (!ship.getBeginGame()) {
					rotateAngle = PI / 2 - ship.getAngleShip();
					initialVelocity = ship.getVelocity();
				}

				// Repaint the screen after the change
				repaint();
			}
		}

	}

	// Sets up the main frame for the Game
	public static void main(String[] args) {
		OrbitMain frame = new OrbitMain();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		// Main method
	}
	// ORBIT main

}
