package cluedogame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import cluedogame.GUI.BoardCanvas;
import cluedogame.GUI.CluedoFrame;
import cluedogame.cards.*;
import cluedogame.sqaures.DoorSquare;
import cluedogame.sqaures.DoorSquare.Dir;
import cluedogame.sqaures.RoomSquare;
import cluedogame.sqaures.ShortcutSquare;
import cluedogame.sqaures.Square;

/**
 * Represents a player in a game of Cluedo.
 * @author Sarah Dobie, Chris Read
 *
 */
public class Player {
	
	private String character; // which character playing as
	private String userName; // the name of the user
	private Image img; // the image representing this player
	private Image playerName;
	private List<Card> hand; // cards in the player's hand
	private List<Card> cardsSeen; // cards the player has seen
	
	private int cPosition; // the player's current column pos
	private int rPosition; // the player's current row pos
	
	/**
	 * Constructor for class Player.
	 * @param simpleName The simplified name for the chosen character.
	 * @param number The player's id.
	 */
	public Player(String character, String userName) {
		this.character = character;
		this.userName = userName;
		this.img = chooseImage();
		this.hand = new ArrayList<Card>();
		this.cardsSeen = new ArrayList<Card>();
		this.cPosition = startCol(this.character);
		this.rPosition = startRow(this.character);
	}
	
	/**
	 * Mock constructor used in the A* search.
	 * @param r
	 * @param c
	 */
	public Player(int r, int c){
		rPosition = r;
		cPosition = c;
	}
	
	private Image chooseImage() {
		try {
			playerName = ImageIO.read(new File("Images"+File.separator+character+"Name.png"))
					.getScaledInstance(200, 75, Image.SCALE_FAST);
			return img = ImageIO.read(new File("Images"+File.separator+character+".png"))
					.getScaledInstance(45, 45, Image.SCALE_FAST);
		} catch (IOException e) {
			System.out.println("Could not read image file: "+e.getMessage());
			return null;
		}
	}
	
	/**
	 * returns the image for drawing at the top of the Dashboard
	 * @return
	 */
	public Image getNameImage(){
		return playerName;
	}

	/**
	 * Gets the cards in the this player's hand.
	 * @return The cards in the player's hand
	 */
	public List<Card> getHand(){
		return hand;
	}
	
	/**
	 * Gets the names of cards in this player's hand.
	 * @return The names of the cards in the player's hand
	 */
	public List<String> getHandStrings(){
		List<String> handStrings = new ArrayList<String>();
		for(Card c : hand){
			handStrings.add(c.getName());
		}
		return handStrings;
	}
	
	/**
	 * gets the names of cards the player has seen
	 * @return the names of cards the player has seen
	 */
	public List<String> getCardsSeenStrings(){
		List<String> cardStrings = new ArrayList<String>();
		for(Card c : cardsSeen){
			cardStrings.add(c.getName());
		}
		return cardStrings;
	}
	
	/**
	 * Returns the column, or X position of the player.
	 * @return The current column of the player
	 */
	public int column(){
		return cPosition;
	}
	
	/**
	 * Returns the row, or Y position of the player.
	 * @return The current row of the player
	 */
	public int row(){
		return rPosition;
	}
	
	/**
	 * Gets the name of the player's character.
	 * @return Player character name
	 */
	public String getCharacter(){
		return character;
	}
	
	/**
	 * Gets the name of the user.
	 * @return The name of the user
	 */
	public String getUserName(){
		return userName;
	}
	
	
	/**
	 * Determines the column in which the given character starts.
	 * @param name The character for whom to find the start position.
	 * @return the column in which the given character starts.
	 */
	public static int startCol(String name){
		switch(name){
		case GameOfCluedo.SCARLETT : return 7;
		case GameOfCluedo.MUSTARD : return 0;
		case GameOfCluedo.WHITE : return 9;
		case GameOfCluedo.GREEN : return 14;
		case GameOfCluedo.PEACOCK : return 23;
		case GameOfCluedo.PLUM : return 23;
		default : return -1;
		}
	}
	
	/**
	 * Determines the row in which the given character starts.
	 * @param name The character for whom to find the start position.
	 * @return the row in which the given character starts.
	 */
	public static int startRow(String name){
		switch(name){
		case GameOfCluedo.SCARLETT : return 24;
		case GameOfCluedo.MUSTARD : return 17;
		case GameOfCluedo.WHITE : return 0;
		case GameOfCluedo.GREEN : return 0;
		case GameOfCluedo.PEACOCK : return 6;
		case GameOfCluedo.PLUM : return 19;
		default : return -1;
		}
	}
	
	/**
	 * Determines whether the player can immediately move to the given square.
	 * @param toSquare The square to move to
	 * @param dir The direction the player is moving in
	 * @param opp The opposite direction to the one the player is moving in
	 * @param board The board of the current game
	 * @param game The current game
	 * @return Return true iff the player can immediately move to square in the given dir.
	 */
	private boolean canMoveTo(Square toSquare, Dir dir, Dir opp, Board board, GameOfCluedo game){
		try{
			Square currentSquare = board.squareAt(rPosition, cPosition);
			// check if entering a room
			if(toSquare instanceof RoomSquare){
				if(currentSquare instanceof DoorSquare){
					DoorSquare doorSq = ((DoorSquare)currentSquare);
					return doorSq.getRoom().equals(((RoomSquare)toSquare).getRoom())
							&& doorSq.getEnterDir() == dir;
				} else if(currentSquare instanceof RoomSquare || currentSquare instanceof ShortcutSquare){
					return true;
				} else {
					return false;
				}
			}
			// check if leaving room
			if(currentSquare instanceof RoomSquare){
				return toSquare instanceof RoomSquare 
						|| toSquare instanceof ShortcutSquare
						|| (toSquare instanceof DoorSquare &&
								((DoorSquare)toSquare).getEnterDir() == opp);
			}
			return toSquare.isSteppable() && !(toSquare instanceof ShortcutSquare)
					&& !game.hasPlayerAt(toSquare.row(), toSquare.col());
		} catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
	}
	
	/**
	 * Returns true if the player can move left.
	 * @param board The board being played on
	 * @return True if the square to the left of the player can be
	 * stepped on; false otherwise.
	 */
	public boolean canMoveLeft(Board board, GameOfCluedo game){
		try{
			int leftRow = rPosition;
			int leftCol = cPosition-1;
			Square leftSquare = board.squareAt(leftRow, leftCol);
			return canMoveTo(leftSquare, Dir.WEST, Dir.EAST, board, game);
		} catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
	}
	
	/**
//	 * Returns true if the player can move right.
//	 * @param board The board being played on
//	 * @return True if the square to the right of the player can be
//	 * stepped on; false otherwise.
//	 */
	public boolean canMoveRight(Board board, GameOfCluedo game){
		try{
			int rightRow = rPosition;
			int rightCol = cPosition+1;
			Square rightSquare = board.squareAt(rightRow, rightCol);
			return canMoveTo(rightSquare, Dir.EAST, Dir.WEST, board, game);
		} catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
	}
	
	/**
	 * Returns true if the player can move up.
	 * @param board The board being played on
	 * @return True if the square to the up of the player can be
	 * stepped on; false otherwise.
	 */
	public boolean canMoveUp(Board board, GameOfCluedo game){
		try{
			int upRow = rPosition-1;
			int upCol = cPosition;
			Square upSquare = board.squareAt(upRow, upCol);
			return canMoveTo(upSquare, Dir.NORTH, Dir.SOUTH, board, game);
		} catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
	}
	
	/**
	 * Returns true if the player can move down.
	 * @param board The board being played on
	 * @return True if the square to the down of the player can be
	 * stepped on; false otherwise.
	 */
	public boolean canMoveDown(Board board, GameOfCluedo game){
		try{
			int downRow = rPosition+1;
			int downCol = cPosition;
			Square downSquare = board.squareAt(downRow, downCol);
			return canMoveTo(downSquare, Dir.SOUTH, Dir.NORTH, board, game);
		} catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
	}
	
//	/**
//	 * Returns true if the player can move left.
//	 * @param board The board being played on
//	 * @return True if the square to the left of the player can be
//	 * stepped on; false otherwise.
//	 */
//	public boolean canMoveLeft(Board board, GameOfCluedo game){
//		try{
//			int leftRow = rPosition;
//			int leftCol = cPosition-1;
//			Square currentSquare = board.squareAt(rPosition, cPosition);
//			Square leftSquare = board.squareAt(leftRow, leftCol);
//			// check if entering a room
//			if(leftSquare instanceof RoomSquare){
//				if(currentSquare instanceof DoorSquare){
//					DoorSquare doorSq = ((DoorSquare)currentSquare);
//					return doorSq.getRoom().equals(((RoomSquare)leftSquare).getRoom())
//							&& doorSq.getEnterDir() == Dir.WEST;
//				} else if(currentSquare instanceof RoomSquare || currentSquare instanceof ShortcutSquare){
//					return true;
//				} else {
//					return false;
//				}
//			}
//			// check if leaving room
//			if(currentSquare instanceof RoomSquare){
//				return leftSquare instanceof RoomSquare || leftSquare instanceof ShortcutSquare
//						|| (leftSquare instanceof DoorSquare && ((DoorSquare)leftSquare).getEnterDir() == Dir.EAST);
//			}
//			return leftSquare.isSteppable() && !(leftSquare instanceof ShortcutSquare)
//					&& !game.hasPlayerAt(leftRow, leftCol);
//		} catch(ArrayIndexOutOfBoundsException e){
//			return false;
//		}
//	}
//
//	/**
//	 * Returns true if the player can move right.
//	 * @param board The board being played on
//	 * @return True if the square to the right of the player can be
//	 * stepped on; false otherwise.
//	 */
//	public boolean canMoveRight(Board board, GameOfCluedo game){
//		try {
//			int rightRow = rPosition;
//			int rightCol = cPosition+1;
//			Square currentSquare = board.squareAt(rPosition, cPosition);
//			Square rightSquare = board.squareAt(rightRow, rightCol);
//			// check if entering a room
//			if(rightSquare instanceof RoomSquare){
//				if(currentSquare instanceof DoorSquare){
//					DoorSquare doorSq = ((DoorSquare)currentSquare);
//					return doorSq.getRoom().equals(((RoomSquare)rightSquare).getRoom())
//							&& doorSq.getEnterDir() == Dir.EAST;
//				} else if(currentSquare instanceof RoomSquare || currentSquare instanceof ShortcutSquare){
//					return true;
//				} else {
//					return false;
//				}
//			}
//			// check if leaving room
//			if(currentSquare instanceof RoomSquare){
//				return rightSquare instanceof RoomSquare || rightSquare instanceof ShortcutSquare
//						|| (rightSquare instanceof DoorSquare && ((DoorSquare)rightSquare).getEnterDir() == Dir.WEST);
//			}
//			return rightSquare.isSteppable() && !(rightSquare instanceof ShortcutSquare) && !game.hasPlayerAt(rightRow, rightCol);
//		} catch(ArrayIndexOutOfBoundsException e){
//			return false;
//		}
//	}
//	
//	/**
//	 * Returns true if the player can move up.
//	 * @param board The board being played on
//	 * @return True if the square to the up of the player can be
//	 * stepped on; false otherwise.
//	 */
//	public boolean canMoveUp(Board board, GameOfCluedo game){
//		try {
//			int upRow = rPosition-1;
//			int upCol = cPosition;
//			Square currentSquare = board.squareAt(rPosition, cPosition);
//			Square upSquare = board.squareAt(upRow, upCol);
//			// check if entering a room
//			if(upSquare instanceof RoomSquare){
//				if(currentSquare instanceof DoorSquare){
//					DoorSquare doorSq = ((DoorSquare)currentSquare);
//					return doorSq.getRoom().equals(((RoomSquare)upSquare).getRoom())
//							&& doorSq.getEnterDir() == Dir.NORTH;
//				} else if(currentSquare instanceof RoomSquare || currentSquare instanceof ShortcutSquare){
//					return true;
//				} else {
//					return false;
//				}
//			}
//			// check if leaving room
//			if(currentSquare instanceof RoomSquare){
//				return upSquare instanceof RoomSquare || upSquare instanceof ShortcutSquare
//						|| (upSquare instanceof DoorSquare && ((DoorSquare)upSquare).getEnterDir() == Dir.SOUTH);
//			}
//			return upSquare.isSteppable() && !(upSquare instanceof ShortcutSquare) && !game.hasPlayerAt(upRow, upCol);
//		} catch(ArrayIndexOutOfBoundsException e){
//			return false;
//		}
//	}
//	
//	/**
//	 * Returns true if the player can move down.
//	 * @param board The board being played on
//	 * @return True if the square to the down of the player can be
//	 * stepped on; false otherwise.
//	 */
//	public boolean canMoveDown(Board board, GameOfCluedo game){
//		try {
//			int downRow = rPosition+1;
//			int downCol = cPosition;
//			Square currentSquare = board.squareAt(rPosition, cPosition);
//			Square downSquare = board.squareAt(downRow, downCol);
//			// check if entering a room
//			if(downSquare instanceof RoomSquare){
//				if(currentSquare instanceof DoorSquare){
//					DoorSquare doorSq = ((DoorSquare)currentSquare);
//					return doorSq.getRoom().equals(((RoomSquare)downSquare).getRoom())
//							&& doorSq.getEnterDir() == Dir.SOUTH;
//				} else if(currentSquare instanceof RoomSquare || currentSquare instanceof ShortcutSquare){
//					return true;
//				} else {
//					return false;
//				}
//			}
//			// check if leaving room
//			if(currentSquare instanceof RoomSquare){
//				return downSquare instanceof RoomSquare || downSquare instanceof ShortcutSquare
//						|| (downSquare instanceof DoorSquare && ((DoorSquare)downSquare).getEnterDir() == Dir.NORTH);
//			}
//			return downSquare.isSteppable() && !(downSquare instanceof ShortcutSquare) && !game.hasPlayerAt(downRow, downCol);
//		} catch(ArrayIndexOutOfBoundsException e){
//			return false;
//		}
//	}	

	/**
	 * Moves the player character left one space on the game board.
	 */
	public void moveLeft() {
		this.cPosition -= 1;
		
	}

	/**
	 * Moves the player character right one space on the game board.
	 */
	public void moveRight() {
		this.cPosition += 1;
		
	}

	/**
	 * Moves the player character up one space on the game board.
	 */
	public void moveUp() {
		this.rPosition -= 1;
		
	}

	/**
	 * Moves the player character down one space on the game board.
	 */
	public void moveDown() {
		this.rPosition += 1;
		
	}
	
	/**
	 * Sets the player's position to the given row and col (if
	 * within bounds).
	 * @param row The player's new row
	 * @param col The player's new column
	 */
	public void setPos(int row, int col){
		if(row >= 0 && row < Board.ROWS
				&& col >= 0 && col < Board.COLS){
			this.rPosition = row;
			this.cPosition = col;
		}
	}
	
	/**
	 * Moves the player onto the given square.
	 * @param sq The square to move the player to.
	 */
	public void moveTo(Square sq){
		rPosition = sq.row();
		cPosition = sq.col();
	}

	/**
	 * Adds a card to the player's hand
	 * @param c Card to add
	 */
	public void addCard(Card c){
		hand.add(c);
		cardsSeen.add(c);
	}
	
	/**
	 * Adds a card to the list of card the player has seen.
	 * @param c Card to add
	 */
	public void addCardSeen(Card c){
		cardsSeen.add(c);
	}
	
	/**
	 * Returns true if the player has seen the given card.
	 * @param c The card to look for
	 * @return True if the player has seen the card; false otherwise.
	 */
	public boolean hasSeenCard(Card c){
		return cardsSeen.contains(c);
	}
	
	/**
	 * Draws the player
	 * @param g The Graphics object to draw on.
	 */
	public void draw(Graphics g){
		int x = CluedoFrame.convertColToX(cPosition);
		int y = CluedoFrame.convertRowToY(rPosition);
//		Image resizedImage = img.getScaledInstance((int)CluedoFrame.squareWidth(),
//				(int)CluedoFrame.squareHeight(), Image.SCALE_SMOOTH);
		g.drawImage(img, x-10, y-10, null);
	}
	
}
