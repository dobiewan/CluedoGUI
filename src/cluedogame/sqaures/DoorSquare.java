package cluedogame.sqaures;

/**
 * Represents a room which a player may enter.
 * @author Sarah Dobie, Chris Read.
 *
 */
public class DoorSquare extends Square {
	
	public enum Dir{
		NORTH,SOUTH,EAST,WEST;
	}
	
	private String room;
	private Dir enterDir; // the direction which the player can enter going in
	
	/**
	 * Constructor for class DoorSquare.
	 * @param room The room being represented
	 * @param enterDir The direction in which the door can be entered.
	 */
	public DoorSquare(String room, Dir enterDir, int row, int col){
		super(true, row, col);
		this.room = room;
		this.enterDir = enterDir;
	}
	
	/**
	 * Gets the room this door opens to.
	 * @return The name of the room this door opens to.
	 */
	public String getRoom(){
		return room;
	}
	
	/**
	 * Gets the direction through which the door can be entered.
	 * @return The direction through which this door can be entered
	 */
	public Dir getEnterDir(){
		return enterDir;
	}

}
