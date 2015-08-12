package cluedogame.sqaures;

/**
 * Represents a room which a player may enter.
 * @author Sarah Dobie, Chris Read.
 *
 */
public class RoomSquare extends Square {
	
	String room;
	
	/**
	 * Constructor for class RoomSquare.
	 * @param room The room being represented
	 */
	public RoomSquare(String room){
		super(true);
		this.room = room;
	}
	
//	/**
//	 * Finds the shortcut square for the given room.
//	 * @param room The room which may contain a shortcut
//	 * @param board The board we are playing on
//	 * @return The ShortcutSquare in the given room, or null if there
//	 * is none.
//	 */
//	public static ShortcutSquare findShortcut(String room, Board board){
//		switch(room){
//		case GameOfCluedo.KITCHEN : return (ShortcutSquare)board.squareAt(2, 4);
//		case GameOfCluedo.STUDY : return (ShortcutSquare)board.squareAt(21, 22);
//		case GameOfCluedo.LOUNGE : return (ShortcutSquare)board.squareAt(20, 1);
//		case GameOfCluedo.CONSERVATORY : return (ShortcutSquare)board.squareAt(4, 22);
//		default : return null;
//		}
//	}
	
	public String getRoom(){
		return room;
	}
	
	@Override
	public char boardChar() {
		return '_';
	}

}
