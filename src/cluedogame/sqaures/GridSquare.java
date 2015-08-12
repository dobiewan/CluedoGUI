package cluedogame.sqaures;

/**
 * Represents a basic, 'empty' square on the board which a player
 * may step on. Has no function other than to be 'walked' on.
 * @author Sarah Dobie, Chris Read
 *
 */
public class GridSquare extends Square {
	
	/**
	 * Constructor for class GridSquare.
	 */
	public GridSquare(){
		super(true);
	}

	@Override
	public char boardChar() {
		return '_';
	}

}
