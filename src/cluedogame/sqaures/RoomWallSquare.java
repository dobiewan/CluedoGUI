package cluedogame.sqaures;

import cluedogame.GameOfCluedo;

/**
 * Represents the wall of a certain room, which players cannot
 * walk through.
 * @author Sarah
 *
 */
public class RoomWallSquare extends Square {

	private String room;
	
	/**
	 * Constructor for class RoomWallSquare.
	 * @param room The room this square is walling
	 */
	public RoomWallSquare(String room){
		super(false);
		this.room = room;
	}
	
	@Override
	public char boardChar() {
		switch(this.room){
		case GameOfCluedo.KITCHEN : return 'K';
		case GameOfCluedo.BALL_ROOM : return 'B';
		case GameOfCluedo.CONSERVATORY : return 'C';
		case GameOfCluedo.BILLIARD_ROOM : return 'P';
		case GameOfCluedo.LIBRARY : return 'L';
		case GameOfCluedo.STUDY : return 'S';
		case GameOfCluedo.HALL : return 'H';
		case GameOfCluedo.LOUNGE : return 'G';
		case GameOfCluedo.DINING_ROOM : return 'D';
		default : return ' ';
		}
	}

}
