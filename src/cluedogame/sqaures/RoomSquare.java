package cluedogame.sqaures;

public class RoomSquare extends Square {
	
	private String room;

	/**
	 * Constructor for class RoomSquare
	 * @param room The room this square is in
	 */
	public RoomSquare(String room, int row, int col) {
		super(true, row, col);
		this.room = room;
	}

	/**
	 * Returns the room which this square is in.
	 * @return The room which this square is in
	 */
	public String getRoom() {
		return room;
	}
	
}
