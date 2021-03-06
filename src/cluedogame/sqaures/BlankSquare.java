package cluedogame.sqaures;

/**
 * Represents a square which is essentially outside of the board:
 * the player cannot step on it, and it essentially has no function.
 * @author Sarah Dobie, Chris Read
 *
 */
public class BlankSquare extends Square {

	/**
	 * Constructor for class BlankSquare.
	 */
	public BlankSquare(int row, int col) {
		super(false, row, col);
	}

}
