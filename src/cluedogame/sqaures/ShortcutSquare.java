package cluedogame.sqaures;

import cluedogame.Board;
import cluedogame.GameOfCluedo;

/**
 * Represents a 'secret passage' on the board.
 * @author Sarah Dobie, Chris Read
 *
 */
public class ShortcutSquare extends Square {
	
	private String startRoom; // the room this shortcut starts in
	private String toRoom; // the room the shortcut goes to
	private int toRow; // the row this shortcut goes to
	private int toCol; // the col this shortcut goes to
	
	/**
	 * Constructor for class ShortcutSquare.
	 * @param toRoom The room the shortcut goes to
	 * @param board The board the shortcut is on
	 */
	public ShortcutSquare(String toRoom, Board board, int row, int col){
		super(true, row, col);
		this.toRoom = toRoom;
		this.startRoom = findStartRoom();
		toRow = findRow(toRoom);
		toCol = findCol(toRoom);
	}
	
	private String findStartRoom() {
		switch(toRoom){
		case GameOfCluedo.KITCHEN : return GameOfCluedo.STUDY;
		case GameOfCluedo.STUDY : return GameOfCluedo.KITCHEN;
		case GameOfCluedo.LOUNGE : return GameOfCluedo.CONSERVATORY;
		case GameOfCluedo.CONSERVATORY : return GameOfCluedo.LOUNGE;
		default : return null;
		}
	}

	/**
	 * Finds the row of the shortcut in the given room
	 * @param room The room with the shortcut
	 * @return The row of the shortcut in the given room,
	 * or -1 if there is none.
	 */
	private static int findRow(String room) {
		switch(room){
		case GameOfCluedo.KITCHEN : return 1;
		case GameOfCluedo.STUDY : return 21;
		case GameOfCluedo.LOUNGE : return 19;
		case GameOfCluedo.CONSERVATORY : return 5;
		default : return -1;
		}
	}

	/**
	 * Finds the column of the shortcut in the given room
	 * @param room The room with the shortcut
	 * @return The column of the shortcut in the given room, or
	 * -1 if there is none.
	 */
	private static int findCol(String room) {
		switch(room){
		case GameOfCluedo.KITCHEN : return 5;
		case GameOfCluedo.STUDY : return 23;
		case GameOfCluedo.LOUNGE : return 0;
		case GameOfCluedo.CONSERVATORY : return 22;
		default : return -1;
		}
	}

	public String startRoom() {
		return startRoom;
	}

	/**
	 * Gets the room this shortcut goes to.
	 * @return The name of the room at the other end of
	 * this shortcut.
	 */
	public String toRoom(){
		return this.toRoom;
	}

	/**
	 * Gets the row this shortcut leads to.
	 * @return The row of the square at the other end of
	 * this shortcut.
	 */
	public int toRow() {
		return toRow;
	}

	/**
	 * Gets the column this shortcut leads to.
	 * @return The column of the square at the other end of
	 * this shortcut.
	 */
	public int toCol() {
		return toCol;
	}

}
