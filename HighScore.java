import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Reads and writes high scores to a text document
 * 
 * @author Michael Pruss, James Hughes
 * @version April 7, 2015
 */
public class HighScore {
	private double[] highScore;

	/**
	 * Reads high scores text file and puts all values in a high score array
	 */
	public HighScore() {
		try {
			// Sets up high score array
			highScore = new double[30];
			BufferedReader highScoreFile = new BufferedReader(new FileReader("HighScore.txt"));

			// Reads the text file and puts all the values in the high score
			// array
			for (int row = 0; row < highScore.length; row++) {
				String line = highScoreFile.readLine();
				highScore[row] = Double.parseDouble(line);
			}

			highScoreFile.close();

		} catch (IOException e) {
			System.exit(0);
		}
	}

	/**
	 * Submits a high score to be written to the high score text document
	 * 
	 * @param index
	 *            Index to be written at
	 * @param time
	 *            The high score time
	 */
	public void submitHighScore(int index, double time) {
		try {
			// Sets up a print writer to write to a text document
			PrintWriter pw = new PrintWriter(new FileWriter("HighScore.txt"), false);

			// Rewrites over the text document with the values in the high score
			// array
			for (int i = 0; i < highScore.length; i++) {
				// If the time is less than the time in the text document, write
				// the new high score time
				if (index == i && time < highScore[i]) {
					highScore[i] = time;
				}
				pw.println(String.valueOf(highScore[i]));
			}
			// Flushes the text document once the writing is done
			pw.flush();

			pw.close();
		} catch (IOException e) {
			System.exit(0);
		}
	}

	/**
	 * Resets the high score in the High Score text document
	 */
	public void resetHighScore() {
		try {
			// Sets up a print writer to write to a text document
			PrintWriter pw = new PrintWriter(new FileWriter("HighScore.txt"), false);

			// Rewrites over the text document with reset high score
			for (int i = 0; i < highScore.length; i++) {
				// Resets all of the high scores in the text document
				highScore[i] = 1000000.0;
				pw.println(String.valueOf(highScore[i]));
			}
			// Flushes the text document once the writing is done
			pw.flush();

			pw.close();
		} catch (IOException e) {
			System.exit(0);
		}
	}

	/**
	 * Gets high score array
	 * 
	 * @return high score array
	 */
	public double[] getHighScore() {
		return highScore;
	}

}
