package cluedogame.sqaures;

/**
 * Represents a basic, 'empty' square on the board which a player
 * may step on. Has no function other than to be walked on.
 * @author Sarah Dobie, Chris Read
 *
 */
public class HallwaySquare extends Square {
	
	/**
	 * Constructor for class HallWaySquare.
	 */
	public HallwaySquare(int row, int col){
		super(true, row, col);
	}

}
