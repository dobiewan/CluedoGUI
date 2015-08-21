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
	private Image tokenImg; // the image representing this player
	private Image playerNameImg; // an image of the player's name
	private List<Card> hand; // cards in the player's hand
	private List<Card> cardsSeen; // cards the player has seen
	
	private int cPosition; // the player's current column pos
	private int rPosition; // the player's current row pos
	private boolean inGame;
	
	/**
	 * Constructor for class Player.
	 * @param simpleName The simplified name for the chosen character.
	 * @param number The player's id.
	 */
	public Player(String character, String userName) {
		this.character = character;
		this.userName = userName;
		chooseImage();
		this.hand = new ArrayList<Card>();
		this.cardsSeen = new ArrayList<Card>();
		this.cPosition = startCol(this.character);
		this.rPosition = startRow(this.character);
		this.inGame = true;
	}
	
	/**
	 * Mock constructor used in the A* search.
	 * @param r Row at which to place the player
	 * @param c Column at which to place the player
	 */
	public Player(int r, int c){
		rPosition = r;
		cPosition = c;
	}
	
	/**
	 * Loads the image of this player's token.
	 * @return The image of the player's token.
	 */
	private void chooseImage() {
		try {
			// load player name image
			playerNameImg = ImageIO.read(new File("Images"+File.separator+character+"Name.png"));
			// load player token image
			tokenImg = ImageIO.read(new File("Images"+File.separator+character+".png"));
		} catch (IOException e) {
			System.out.println("Could not read image file: "+e.getMessage());
		}
	}
	
	/**
	 * Returns the name image for drawing at the top of the Dashboard.
	 * @return An image of the player's name
	 */
	public Image getNameImage(int pixel){
		return playerNameImg.getScaledInstance(40*pixel, 15*pixel, Image.SCALE_FAST);
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
	 * returns a list of all card objects the player has seen
	 * @return
	 */
	public List<Card> getCardsSeen(){
		return cardsSeen;
	}
	
	/**
	 * Returns the row, or Y position of the player.
	 * @return The current row of the player
	 */
	public int row(){
		return rPosition;
	}
	
	/**
	 * Returns the column, or X position of the player.
	 * @return The current column of the player
	 */
	public int col(){
		return cPosition;
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
	 * Determines whether the current player is still in the game.
	 * @return True iff the player is still in the game
	 */
	public boolean isInGame() {
		return inGame;
	}

	/**
	 * Determine whether the player should still be in the game.
	 * @param inGame True if the player is in the game, false otherwise
	 */
	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	/**
	 * Determines the row in which the given character starts.
	 * @param name The character for whom to find the start position.
	 * @return the row in which the given character starts.
	 */
	private static int startRow(String name){
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
	 * Determines the column in which the given character starts.
	 * @param name The character for whom to find the start position.
	 * @return the column in which the given character starts.
	 */
	private static int startCol(String name){
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
			// check for other players in the way
			Player otherPlayer = game.getPlayerAt(toSquare.row(), toSquare.col());
			boolean playerInWay = otherPlayer != null && otherPlayer.isInGame();
			return toSquare.isSteppable() && !(toSquare instanceof ShortcutSquare)
					&& !playerInWay;
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
	public void draw(Graphics g, CluedoFrame frame){
		int pixel = frame.getPixelSize();
		int x = frame.convertColToX(cPosition);
		int y = frame.convertRowToY(rPosition);
		g.drawImage(tokenImg.getScaledInstance(9*pixel, 9*pixel, Image.SCALE_FAST), x-(2*pixel), y-(2*pixel), null);
	}
	
	/**
	 * Gets the player's token image
	 * @return The image of the token representing this player.
	 */
	public Image getToken(){
		return tokenImg;
	}
	
}
