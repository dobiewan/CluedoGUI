package cluedogame.sqaures;

public class RoomSquare extends Square {
	
	private String room;

	public RoomSquare(String room, int row, int col) {
		super(true, row, col);
		this.room = room;
	}

	public String getRoom() {
		return room;
	}
	
}
