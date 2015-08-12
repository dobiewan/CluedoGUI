package cluedogame;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import cluedogame.sqaures.*;

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
		File f = new File("boardFile.txt");
		parse(f);
	}
	
	/**
	 * Loads a board from a given file.
	 * @param file The file containing the board data.
	 */
	public void parse(File file){
		try{
			Scanner s = new Scanner(file);
			// create queues of special squares
			Queue<Character> title = titleChars();
//			Queue<String> players = startPlayers();
			Queue<String> shortcuts = shortcutRooms();
			// iterate over each row
			for(int r = 0; r<board.length; r++){
				String line = s.nextLine();
				// parse each column in row r
				for(int c=0; c < board[0].length; c++){
					char code = line.charAt(c); // get the character in the file
					// determine the Square corresponding to the code
					Square sq = squareTypeFromCode(code, title, shortcuts);
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
	 * @param title The remaining letters of 'CLUEDO'
	 * @param shortcuts The remaining shortcut rooms
	 * @return A Square corresponding to the given code.
	 */
	private Square squareTypeFromCode(char code, Queue<Character> title,
			 Queue<String> shortcuts) {
		Square sq = null;
		switch(code){
		case '/' : sq = new BlankSquare(); break;
		case '_' : sq = new GridSquare(); break;
		case '?' : sq = new CharSquare('?'); break;
		case '#' : sq = new CharSquare(title.poll()); break;
		case '~' : sq = new ShortcutSquare(shortcuts.poll(), this); break;
		case '*' : sq = new StarterSquare(); break;
		case 'K' : sq = new RoomWallSquare(GameOfCluedo.KITCHEN); break;
		case 'B' : sq = new RoomWallSquare(GameOfCluedo.BALL_ROOM); break;
		case 'C' : sq = new RoomWallSquare(GameOfCluedo.CONSERVATORY); break;
		case 'P' : sq = new RoomWallSquare(GameOfCluedo.BILLIARD_ROOM); break;
		case 'L' : sq = new RoomWallSquare(GameOfCluedo.LIBRARY); break;
		case 'S' : sq = new RoomWallSquare(GameOfCluedo.STUDY); break;
		case 'H' : sq = new RoomWallSquare(GameOfCluedo.HALL); break;
		case 'G' : sq = new RoomWallSquare(GameOfCluedo.LOUNGE); break;
		case 'D' : sq = new RoomWallSquare(GameOfCluedo.DINING_ROOM); break;
		case 'k' : sq = new RoomSquare(GameOfCluedo.KITCHEN); break;
		case 'b' : sq = new RoomSquare(GameOfCluedo.BALL_ROOM); break;
		case 'c' : sq = new RoomSquare(GameOfCluedo.CONSERVATORY); break;
		case 'p' : sq = new RoomSquare(GameOfCluedo.BILLIARD_ROOM); break;
		case 'l' : sq = new RoomSquare(GameOfCluedo.LIBRARY); break;
		case 's' : sq = new RoomSquare(GameOfCluedo.STUDY); break;
		case 'h' : sq = new RoomSquare(GameOfCluedo.HALL); break;
		case 'g' : sq = new RoomSquare(GameOfCluedo.LOUNGE); break;
		case 'd' : sq = new RoomSquare(GameOfCluedo.DINING_ROOM); break;
		}
		return sq;
	}
	
	/**
	 * Creates a queue containing each letter of the word CLUEDO.
	 * @return A queue containing the letters (in order) C, L,
	 * U, E, D, O
	 */
	private Queue<Character> titleChars(){
		Queue<Character> title = new LinkedList<Character>();
		title.add('C');
		title.add('L');
		title.add('U');
		title.add('E');
		title.add('D');
		title.add('O');
		return title;
	}
	
	/**
	 * Creates a queue containing the room at each shortcut
	 * location, in the order that they will be parsed.
	 * @return A queue containing the room at each shortcut
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
	 * Make a copy of the board.
	 * @return A new 2D array referencing all the same Squares as
	 * the board field.
	 */
	private Square[][] copyBoard(){
		Square[][] copy = new Square[ROWS][COLS];
		for(int r=0; r<ROWS; r++){
			for(int c=0; c<COLS; c++){
				copy[r][c] = board[r][c];
			}
		}
		return copy;
	}
	
	/**
	 * Displays the board on the console.
	 */
	public void draw(List<Player> players){
		// replace squares for player positions
		Square[][] drawBoard = copyBoard();
		for (Player p : players){
			drawBoard[p.row()][p.column()] = new CharSquare(p.ID());
		}
		
		// iterate over every row
		for(int r=0; r<ROWS; r++){
			// don't draw a wall if the next square is blank or a letter
			boolean drawnBlank = false;
			if(drawBoard[r][0] instanceof BlankSquare){
				System.out.print(" ");
			} else {
				System.out.print("|");
			}
			// iterate over every column in r
			for(int c=0; c<COLS; c++){
				Square sq = drawBoard[r][c];
				// don't draw a wall if this square is blank or a letter
				if(sq instanceof BlankSquare || sq instanceof CharSquare){
					if(drawnBlank){
						System.out.print(" ");
					}
					System.out.print(sq.boardChar());
					drawnBlank = true;
					continue;
				}
				// if we've just drawn a blank square, draw a wall
				if(drawnBlank){
					System.out.print("|");
					drawnBlank = false;
				}
				// print this square's symbol
				System.out.print(sq.boardChar());
				// draw a wall
				System.out.print("|");
			}
			// print characters that are on this row
			for (Player p : players){
				if (p.row() == r){
					System.out.print(" <- " + p.getName());
				}
			}
			System.out.println();
		}
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
