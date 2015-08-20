package cluedogame.sqaures;

/**
 * Represents a square on the Cluedo playing board.
 * @author Sarah Dobie, Chris Read
 *
 */
public abstract class Square {
	
	protected boolean steppable; // true if a player can step on the square
	protected int row;
	protected int col;
	
	// A* search flags
	private Square from;
	private double cost;
	private boolean visited;
	
	/**
	 * Constructor for class Square.
	 * @param steppable True if a player can walk on this square
	 * @param row The row on the board containing this square
	 * @param col The column on the board containing this square
	 */
	public Square(boolean steppable, int row, int col){
		this.steppable = steppable;
		this.row = row;
		this.col = col;
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
	 * Gets the row of this square.
	 * @return The row this square belongs to.
	 */
	public int row() {
		return row;
	}

	/**
	 * Gets the column of this square.
	 * @return The column this square belongs to.
	 */
	public int col() {
		return col;
	}

	// A* search getters/setters
	
	public Square getFrom() {
		return from;
	}

	public void setFrom(Square from) {
		this.from = from;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	
}
