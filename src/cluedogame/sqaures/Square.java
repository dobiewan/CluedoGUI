package cluedogame.sqaures;

/**
 * Represents a square on the Cluedo playing board.
 * @author Sarah Dobie, Chris Read
 *
 */
public abstract class Square {
	
	protected boolean steppable; // true if a player can step on the square
	
	/**
	 * Constructor for class Square.
	 * @param steppable True if a player can walk on this square.
	 */
	public Square(boolean steppable){
		this.steppable = steppable;
	}
	
	/**
	 * Whether or not this square can be walked on.
	 * @return True if players may walk on this square;
	 * false otherwise.
	 */
	public boolean isSteppable(){
		return steppable;
	}
	
	/**
	 * Returns the char that will represent this square on
	 * the board.
	 * @return A char representation of this Square.
	 */
	public abstract char boardChar();
}
