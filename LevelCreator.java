import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Loads level
 * 
 * @author Michael Pruss, James Hughes
 * @version April 7, 2015
 */
public class LevelCreator {
	private int[][] grid;
	private int numOfRows;
	private int numOfColumns;

	/**
	 * Sets initial variable values for the level class.
	 */
	public LevelCreator() {
	}

	/**
	 * Reads the level text document and puts in a 2D array
	 * 
	 * @param levelNo
	 *            The level Number
	 * @return the level grid
	 */
	public int[][] createLevel(int levelNo) {
		try {
			// Begins reading level file to figure out how many rows and columns
			// there are.
			BufferedReader levelNumb = new BufferedReader(new FileReader(
					"level" + levelNo + ".txt"));
			numOfRows = 0;
			numOfColumns = 0;

			// Finds how many rows and columns are in the level file
			String secondLine = levelNumb.readLine();
			while (((levelNumb.readLine()) != null)) {
				numOfRows++;
			}
			for (int count = 0; count < secondLine.length(); count++) {
				numOfColumns++;
			}
			// Closes file
			levelNumb.close();

			// Begins reading level file.
			BufferedReader levelFile = new BufferedReader(new FileReader(
					"level" + levelNo + ".txt"));

			// Setting up the array.
			grid = new int[numOfRows][numOfColumns];

			// Puts the values in the text file to the grid
			for (int row = 0; row < grid.length; row++) {
				String levelLine = levelFile.readLine();
				for (int column = 0; column < grid[row].length; column++) {
					grid[row][column] = (int) (levelLine.charAt(column) - 65);
				}
			}

			levelFile.close();

			return grid;
		} catch (IOException e) {
			System.exit(0);
		}
		return null;
	}

	/**
	 * Gets level grid
	 * 
	 * @return level grid
	 */
	public int[][] getLevel() {
		return grid;
	}

	/**
	 * Gets rows
	 * 
	 * @return number of rows
	 */
	public int getRows() {
		return numOfRows;
	}

	/**
	 * Gets columns
	 * 
	 * @return number of columns
	 */
	public int getColumns() {
		return numOfColumns;
	}

}
