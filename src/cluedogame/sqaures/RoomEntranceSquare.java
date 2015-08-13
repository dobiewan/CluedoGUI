package cluedogame.sqaures;

/**
 * Represents a room which a player may enter.
 * @author Sarah Dobie, Chris Read.
 *
 */
public class RoomEntranceSquare extends Square {
	
	public enum Dir{
		NORTH,SOUTH,EAST,WEST;
	}
	
	String room;
	Dir enterDir;
	
	/**
	 * Constructor for class RoomSquare.
	 * @param room The room being represented
	 */
	public RoomEntranceSquare(String room, Dir enterDir){
		super(true);
		this.room = room;
		this.enterDir = enterDir;
	}
	
	public String getRoom(){
		return room;
	}
	
	public Dir getEnterDir(){
		return enterDir;
	}

}
