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
		case 'N' : sq = new RoomEntranceSquare(roomDoors.poll(), Dir.NORTH, row, col); break;
		case 'E' : sq = new RoomEntranceSquare(roomDoors.poll(), Dir.EAST, row, col); break;
		case 'S' : sq = new RoomEntranceSquare(roomDoors.poll(), Dir.SOUTH, row, col); break;
		case 'W' : sq = new RoomEntranceSquare(roomDoors.poll(), Dir.WEST, row, col); break;
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
	
	public List<Square> shortestPath(Square start, Square goal, int moves){ //TODO moves
		// initalise flags for all square 
		for(int r=0; r<ROWS; r++){
			for(int c=0; c<COLS; c++){
				Square sq = board[r][c];
				sq.setVisited(false);
				sq.setFrom(null);
			}
		}
		PriorityQueue<AStarNode> fringe = new PriorityQueue<AStarNode>();
		fringe.offer(new AStarNode(start, null, 0, distance(start,goal)));
		while(!fringe.isEmpty()){
			AStarNode n = fringe.poll();
			if(!n.node.isVisited()){
				n.node.setVisited(true);
				n.node.setFrom(n.from);
				n.node.setCost(n.costToHere);
				if(n.node == goal){
					break;
				}
				//make list of neighbours
				List<Square> neighbours = new ArrayList<Square>();
				int nodeRow = n.node.row();
				int nodeCol = n.node.col();
				int leftCol = nodeCol - 1;
				int rightCol = nodeCol + 1;
				int upRow = nodeRow - 1;
				int downRow = nodeRow + 1;
				if(validCol(leftCol)){neighbours.add(board[nodeRow][leftCol]);}
				if(validCol(rightCol)){neighbours.add(board[nodeRow][rightCol]);}
				if(validRow(upRow)){neighbours.add(board[upRow][nodeCol]);}
				if(validRow(downRow)){neighbours.add(board[downRow][nodeCol]);}
				for(Square neigh : neighbours){
					if(!neigh.isVisited() && neigh.isSteppable()){
						double costToNeigh = n.costToHere + 1;
						double estTotal = costToNeigh + distance(neigh, goal);
						fringe.offer(new AStarNode(neigh, n.node, costToNeigh, estTotal));
					}
				}
				
			}
		}
		
		
		List<Square> shortestPath = new ArrayList<Square>();
		Square sq = goal;
		shortestPath.add(goal);
		while(sq.getFrom() != null){
			shortestPath.add(0,sq.getFrom());
			sq = sq.getFrom();
		}
		return shortestPath;
	}
	
	private boolean validRow(int row){
		return row >= 0 && row < ROWS;
	}
	
	private boolean validCol(int col){
		return col >= 0 && col < COLS;
	}
	
	private double distance(Square start, Square goal) {
		return Math.hypot(start.col() - goal.col(), start.row() - goal.row());
	}

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
			if(o.totalCostToGoal < totalCostToGoal){ return -1;}
			else if(o.totalCostToGoal == totalCostToGoal){ return 0;}
			else { return 1;}
		}
		
		
		
	}
}
