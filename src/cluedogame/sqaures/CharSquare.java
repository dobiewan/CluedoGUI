package cluedogame.sqaures;

/**
 * Represents a decorative square on the board which
 * will be displayed as the letter that is passed in to
 * the constructor.
 * Also used to represent the position of a player.
 * @author Sarah Dobie, Chris Read
 *
 */
public class CharSquare extends Square {

	private char charToDraw; // the letter to be drawn
	
	/**
	 * Constructor for class CharSquare.
	 * @param charToDraw The character to represent this Square
	 */
	public CharSquare(char charToDraw){
		super(false);
		this.charToDraw = charToDraw;
	}
	
	@Override
	public char boardChar() {
		return charToDraw;
	}

}
