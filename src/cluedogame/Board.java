package cluedogame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

import cluedogame.sqaures.*;
import cluedogame.sqaures.DoorSquare.Dir;

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
					Square sq = squareTypeFromCode(code, r, c, shortcuts, roomDoors);
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
	private Square squareTypeFromCode(char code, int row, int col, Queue<String> shortcuts,
			Queue<String> roomDoors) {
		Square sq = null;
		switch(code){
		case '/' : sq = new BlankSquare(row, col); break;
		case '_' : sq = new GridSquare(row, col); break;
		case '~' : sq = new ShortcutSquare(shortcuts.poll(), this, row, col); break;
		case 'K' : sq = new RoomSquare(GameOfCluedo.KITCHEN, row, col); break;
		case 'B' : sq = new RoomSquare(GameOfCluedo.BALL_ROOM, row, col); break;
		case 'D' : sq = new RoomSquare(GameOfCluedo.DINING_ROOM, row, col); break;
		case 'C' : sq = new RoomSquare(GameOfCluedo.CONSERVATORY, row, col); break;
		case 'b' : sq = new RoomSquare(GameOfCluedo.BILLIARD_ROOM, row, col); break;
		case 'l' : sq = new RoomSquare(GameOfCluedo.LIBRARY, row, col); break;
		case 'L' : sq = new RoomSquare(GameOfCluedo.LOUNGE, row, col); break;
		case 'H' : sq = new RoomSquare(GameOfCluedo.HALL, row, col); break;
		case 's' : sq = new RoomSquare(GameOfCluedo.STUDY, row, col); break;
		case 'N' : sq = new DoorSquare(roomDoors.poll(), Dir.NORTH, row, col); break;
		case 'E' : sq = new DoorSquare(roomDoors.poll(), Dir.EAST, row, col); break;
		case 'S' : sq = new DoorSquare(roomDoors.poll(), Dir.SOUTH, row, col); break;
		case 'W' : sq = new DoorSquare(roomDoors.poll(), Dir.WEST, row, col); break;
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
		rooms.add(GameOfCluedo.BALL_ROOM);
		rooms.add(GameOfCluedo.BALL_ROOM);
		rooms.add(GameOfCluedo.CONSERVATORY);
		rooms.add(GameOfCluedo.KITCHEN);
		rooms.add(GameOfCluedo.BALL_ROOM);
		rooms.add(GameOfCluedo.BALL_ROOM);
		rooms.add(GameOfCluedo.BILLIARD_ROOM);
		rooms.add(GameOfCluedo.DINING_ROOM);
		rooms.add(GameOfCluedo.LIBRARY);
		rooms.add(GameOfCluedo.BILLIARD_ROOM);
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
	
	/**
	 * Determines the shortest path between two squares, and whether this
	 * path is short enough to be taken within a certain number of moves.
	 * @param start The first square in the path
	 * @param goal The last square in the path
	 * @param moves The number of moves this path must be taken in
	 * @return A List of all the squares in the path, or null if it cannot be
	 * taken within the given number of moves.
	 */
	public List<Square> shortestPath(Square start, Square goal, int moves, GameOfCluedo game){
		if(!goal.isSteppable()){return null;} // goal out of bounds
		// prepare nodes and queue
		setupSearchFlags();
		PriorityQueue<AStarNode> fringe = new PriorityQueue<AStarNode>();
		fringe.offer(new AStarNode(start, null, 0, distance(start,goal)));
		boolean found = false;
		
		// continue polling from fringe until shortest path is found
		while(!fringe.isEmpty()){
			AStarNode n = fringe.poll();
			if(!n.node.isVisited()){
				// update node info
				n.node.setVisited(true);
				n.node.setFrom(n.from);
				n.node.setCost(n.costToHere);
				// check if we have reached the end of the path
				if(n.node == goal){
					found = true;
					break;
				}
				//make list of neighbours
				List<Square> neighbours = setupNeighbours(n.node, goal, game);
				for(Square neigh : neighbours){
					// iterate over valid neighbours
					if(!neigh.isVisited()/* && neigh.isSteppable()*/){
						// add valid neighbours to queue
						double costToNeigh = n.costToHere + 1;
						double estTotal = costToNeigh + distance(neigh, goal);
						fringe.offer(new AStarNode(neigh, n.node, costToNeigh, estTotal));
					}
				}
				
			}
		}
		if(!found){return null;}
		// follow the links in the path to make a list
		return pathToList(start, goal, moves);
	}

	/**
	 * Prepares all Squares on the board for an A* search.
	 */
	private void setupSearchFlags() {
		for(int r=0; r<ROWS; r++){
			for(int c=0; c<COLS; c++){
				Square sq = board[r][c];
				sq.setVisited(false);
				sq.setFrom(null);
			}
		}
	}

	/**
	 * Iterates backwards through shortest path nodes to arrange the path
	 * into a list.
	 * @param goal The final node in the path
	 * @param moves The number of moves the path must fit within
	 * @return A list of the squares in the path excluding the start square,
	 * or null if there aren't enough moves to follow the path.
	 */
	private List<Square> pathToList(Square start, Square goal, int moves) {
		// add all squares to a list in order
		List<Square> shortestPath = new ArrayList<Square>();
		Square sq = goal;
		int movesTaken = 0;
		Square nextSquare = sq.getFrom();
//		shortestPath.add(goal);
//		while(sq.getFrom() != null){
//			shortestPath.add(0,sq.getFrom());
//			if(!(sq instanceof RoomSquare)){ // RoomSquare moves don't count
//				movesTaken++;
//			} else if(fromSquare instanceof DoorSquare){ // unless they are first being entered
//				movesTaken++;
//			}
//			fromSquare = sq;
//			sq = sq.getFrom();
//		}
		while(sq != null && sq != start){
			shortestPath.add(0,sq);
			if(!(sq instanceof RoomSquare)){ // RoomSquare moves don't count
				movesTaken++;
			} else if(nextSquare instanceof DoorSquare){ // unless they are first being entered
				movesTaken++;
			}
			sq = sq.getFrom();
			nextSquare = sq.getFrom();
		}
//		shortestPath.remove(0);
//		System.out.println("Path length: "+shortestPath.size());
		// if the path is too long, set it to null
		if(movesTaken > moves){
			shortestPath = null;
		}
		return shortestPath;
	}

	/**
	 * Calculates the Euclidean distance between two squares.
	 * @param square1 
	 * @param square2
	 * @return The Euclidean distance between the two given squares.
	 */
	private double distance(Square square1, Square square2) {
//		System.out.println(Math.hypot(start.col() - goal.col(), start.row() - goal.row()));
		return Math.hypot(square1.col() - square2.col(), square1.row() - square2.row());
	}
	
	/**
	 * Makes a list of up to four neighbours of a square, that is
	 * the squares above, below, to the left and to the right of the
	 * given square, as well as any available shortcut squares.
	 * @param node The square to find the neighbours of.
	 * @return A list of squares above, below, to the left and to
	 * the right of the given square, as well as any available shortcut squares.
	 */
	private List<Square> setupNeighbours(Square node, Square goal, GameOfCluedo game){
		List<Square> neighbours = new ArrayList<Square>();
		int nodeRow = node.row();
		int nodeCol = node.col();
		int leftCol = nodeCol - 1;
		int rightCol = nodeCol + 1;
		int upRow = nodeRow - 1;
		int downRow = nodeRow + 1;
		Player mock = new Player(node.row(), node.col());
		if(mock.canMoveLeft(this, game)){
			neighbours.add(board[nodeRow][leftCol]);}
		if(mock.canMoveRight(this, game)){
			neighbours.add(board[nodeRow][rightCol]);}
		if(mock.canMoveUp(this, game)){
			neighbours.add(board[upRow][nodeCol]);}
		if(mock.canMoveDown(this, game)){
			neighbours.add(board[downRow][nodeCol]);}
		// check for shortcuts
//		if(goal instanceof ShortcutSquare){
//			ShortcutSquare shortcutSq = (ShortcutSquare)goal;
//			if(node instanceof DoorSquare){
//				DoorSquare roomSq = (DoorSquare)node;
//				if(roomSq.getRoom().equals(shortcutSq.startRoom())){
//					neighbours.add(goal);
//				}
//			}
//		}
		return neighbours;
	}

	/**
	 * Checks if the given row is within bounds.
	 * @param row The row to check
	 * @return True iff the row is within bounds of the board
	 */
	public static boolean validRow(int row){
		return row >= 0 && row < ROWS;
	}

	/**
	 * Checks if the given column is within bounds.
	 * @param col The column to check
	 * @return True iff the column is within bounds of the board
	 */
	public static boolean validCol(int col){
		return col >= 0 && col < COLS;
	}

	/**
	 * A tuple class used in the A* search algorithm.
	 *
	 */
	private class AStarNode implements Comparable<AStarNode> {
		public Square node;
		public Square from;
		public double costToHere;
		public double totalCostToGoal;
		
		public AStarNode(Square node, Square from,
				double costToHere, double totalCostToGoal) {
			this.node = node;
			this.from = from;
			this.costToHere = costToHere;
			this.totalCostToGoal = totalCostToGoal;
		}

		@Override
		public int compareTo(AStarNode o) {
			if(o.totalCostToGoal < totalCostToGoal){ return 1;}
			else if(o.totalCostToGoal == totalCostToGoal){ return 0;}
			else { return -1;}
		}
		
		
		
	}
}
