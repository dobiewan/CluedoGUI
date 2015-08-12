package cluedogame.sqaures;

/**
 * A square on which players may start the game.
 * @author Sarah Dobie, Chris Read
 *
 */
public class StarterSquare extends Square {
	
	/**
	 * Constructor for class Square.
	 */
	public StarterSquare(){
		super(true);
	}

	@Override
	public char boardChar() {
		return '*';
	}

}
