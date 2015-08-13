package cluedogame;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import cluedogame.sqaures.*;
import cluedogame.sqaures.RoomEntranceSquare.Dir;

/**
 * A 2D array representation of the Cluedo playing board.
 * @author Sarah Dobie, Chris Read
 *
 */
public class Board {
	public static int ROWS = 25;
	public static int COLS = 24;
	
	private Square[][] board = new Square[ROWS][COLS];
	
	/**
	 * Constructor for class Board.
	 */
	public Board(){
		parse();
	}
	
	/**
	 * Loads a board from a given file.
	 * @param file The file containing the board data.
	 */
	public void parse(){
		try{
			Scanner s = new Scanner(new File("boardFile.txt"));
			// create queues of special squares
			Queue<String> shortcuts = shortcutRooms();
			Queue<String> roomDoors = roomDoors();
			// iterate over each row
			for(int r = 0; r<board.length; r++){
				String line = s.nextLine();
				// parse each column in row r
				for(int c=0; c < board[0].length; c++){
					char code = line.charAt(c); // get the character in the file
					// determine the Square corresponding to the code
					Square sq = squareTypeFromCode(code, shortcuts, roomDoors);
					// add the square to the board
					board[r][c] = sq;
				}
			}
			s.close();	
		} catch(IOException e){
			System.out.println("Error loading file: "+ e.getMessage());
		}
	}

	/**
	 * Creates a new square based on the code read from the file.
	 * @param code A character from the file
	 * @param shortcuts The remaining shortcut rooms
	 * @param roomDoors The remaining room entrances
	 * @return A Square corresponding to the given code.
	 */
	private Square squareTypeFromCode(char code, Queue<String> shortcuts,
			Queue<String> roomDoors) {
		Square sq = null;
		switch(code){
		case '/' : sq = new BlankSquare(); break;
		case '_' : sq = new GridSquare(); break;
		case '~' : sq = new ShortcutSquare(shortcuts.poll(), this); break;
		case 'N' : sq = new RoomEntranceSquare(roomDoors.poll(), Dir.NORTH); break;
		case 'E' : sq = new RoomEntranceSquare(roomDoors.poll(), Dir.EAST); break;
		case 'S' : sq = new RoomEntranceSquare(roomDoors.poll(), Dir.SOUTH); break;
		case 'W' : sq = new RoomEntranceSquare(roomDoors.poll(), Dir.WEST); break;
		}
		return sq;
	}
	
	/**
	 * Creates a queue containing the room at each shortcut
	 * location (ie. the room it goes to), in the order that they will be parsed.
	 * @return A queue containing the end room at each shortcut
	 * location, in the order that they will be parsed.
	 */
	private Queue<String> shortcutRooms(){
		Queue<String> rooms = new LinkedList<String>();
		rooms.add(GameOfCluedo.STUDY);
		rooms.add(GameOfCluedo.LOUNGE);
		rooms.add(GameOfCluedo.CONSERVATORY);
		rooms.add(GameOfCluedo.KITCHEN);
		return rooms;
	}
	
	/**
	 * Creates a queue containing the name of each room in the order
	 * that their door will be parsed.
	 * @return A queue containing the room at each shortcut
	 * location, in the order that they will be parsed.
	 */
	private Queue<String> roomDoors(){
		Queue<String> rooms = new LinkedList<String>();
		rooms.add(GameOfCluedo.CONSERVATORY);
		rooms.add(GameOfCluedo.BALL_ROOM);
		rooms.add(GameOfCluedo.BALL_ROOM);
		rooms.add(GameOfCluedo.KITCHEN);
		rooms.add(GameOfCluedo.BALL_ROOM);
		rooms.add(GameOfCluedo.BALL_ROOM);
		rooms.add(GameOfCluedo.BILLIARD_ROOM);
		rooms.add(GameOfCluedo.DINING_ROOM);
		rooms.add(GameOfCluedo.BILLIARD_ROOM);
		rooms.add(GameOfCluedo.LIBRARY);
		rooms.add(GameOfCluedo.DINING_ROOM);
		rooms.add(GameOfCluedo.LIBRARY);
		rooms.add(GameOfCluedo.HALL);
		rooms.add(GameOfCluedo.HALL);
		rooms.add(GameOfCluedo.LOUNGE);
		rooms.add(GameOfCluedo.HALL);
		rooms.add(GameOfCluedo.STUDY);
		return rooms;
	}
	
	/**
	 * Returns the square at the given position.
	 * @param row The row of the desired square
	 * @param col The column of the desired square
	 * @return The Square at board[row][col]
	 */
	public Square squareAt(int row, int col){
		return board[row][col];
	}
}
