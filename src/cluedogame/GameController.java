package cluedogame;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import cluedogame.GUI.CluedoFrame;
import cluedogame.cards.Card;
import cluedogame.sqaures.DoorSquare;
import cluedogame.sqaures.RoomSquare;
import cluedogame.sqaures.ShortcutSquare;
import cluedogame.sqaures.Square;

/**
 * A class which controls the gameplay, containing the methods
 * required each time a player takes their turn.
 * @author Sarah Dobie, Chris Read
 *
 */
public class GameController {
	
	private GameOfCluedo game; // the game this controller is associated with
	private CluedoFrame frame; // the frame the game is being played in
//	private boolean gameOver = false; 
	LinkedList<Player> playersInGame = new LinkedList<Player>(); //players still in game
	
	/**
	 * Constructor for class GameController.
	 * @param game The game this controller is associated with
	 * @param frame The frame interfacing the game
	 */
	public GameController(GameOfCluedo game, CluedoFrame frame) {
		this.game = game;
		this.frame = frame;
		playersInGame.addAll(game.getPlayers());
	}
	
	/**
	 * Adds a player to the game.
	 * @param p The player to add
	 */
	public void addPlayer(Player p){
		playersInGame.add(p);
	}
	
	/**
	 * Stars the turn for the next player.
	 */
	public void playTurn(){
		if (/*!gameOver && */playersInGame.size() > 0) {
			// get the next player
			Player player = playersInGame.peek();
			// roll the dice
			game.setCurrentPlayer(player);
			frame.repaintAll();
			game.rollDice();
			frame.showDialog(player.getCharacter()+" rolls "+game.getRoll(), "Dice roll");
			enableButtons(player);
			// put player on end of queue
			playersInGame.add(playersInGame.poll());
		}
	}

	/**
	 * Enables the appropriate buttons in the frame.
	 * @param player The current player
	 */
	public void enableButtons(Player player) {
		Square playerSquare = getPlayerSquare(player);
		// check if player is starting on a shortcut
		if(playerSquare instanceof ShortcutSquare){
			frame.enableShortcutBtn(true);
		} else {
			frame.enableShortcutBtn(false);
		}
		// check if player can suggest or not
		if(playerSquare instanceof RoomSquare ||
				playerSquare instanceof ShortcutSquare){
			frame.enableSuggestBtn(true);
		} else {
			frame.enableSuggestBtn(false);
		}
	}

	/**
	 * Gets the square that a player is currently on.
	 * @param player The player to find the position of
	 * @return The Square that the given player is currently located at.
	 */
	private Square getPlayerSquare(Player player) {
		return game.getBoard().squareAt(player.row(), player.column());
	}
	
	/**
	 * Allows the player to make a suggestion, and checks the suggestion
	 * against any cards the other players have, reporting the matching cards
	 * if there are any.
	 * Can only be used if a player is in a room.
	 */
	public void makeSuggestion() {
		Player player = game.getCurrentPlayer();
		// determine which room the player is in
		String room = null;
		if(getPlayerSquare(player) instanceof RoomSquare){
			RoomSquare square = (RoomSquare)getPlayerSquare(player);
			room = square.getRoom();
		} else {
			ShortcutSquare square = (ShortcutSquare)getPlayerSquare(player);
			room = square.startRoom();
		}
		
		// get the suggestion info from the player
		String[] suggestions = frame.showSuggestionDialog(room);
		String character = suggestions[0];
		String weapon = suggestions[1];
		
		Player suggestedPlayer = null;
		for(Player p : game.getPlayers()){
			if(p.getCharacter().equals(character)){
				suggestedPlayer = p;
				break;
			}
		}
		if(suggestedPlayer != null){
			Board board = game.getBoard();
			for(int r=0; r<Board.ROWS; r++){
				for(int c=0; c<Board.COLS; c++){
					Square sq = board.squareAt(r, c);
					if(sq instanceof RoomSquare){
						RoomSquare roomSq = (RoomSquare)sq;
						if(roomSq.getRoom().equals(room)){
							if(!game.hasPlayerAt(r, c)){
								suggestedPlayer.moveTo(roomSq);
							}
						}
					}
				}
			}
		}
		
		frame.repaintAll();
		
		// iterate over players' hands to find a matching card
		for (Player otherPlayer : game.getPlayers()){
			if (otherPlayer != player){
				for (Card c : otherPlayer.getHand()){
					String cardName = c.getName();
					if ((cardName.equals(character) || cardName.equals(weapon) || cardName.equals(room))
							&& !player.hasSeenCard(c)){
						frame.showDialog(otherPlayer.getCharacter() + " has the card: " + cardName, "Suggestion results");
						player.addCardSeen(c);
						return;
					}
				}
			}
		}
		// if no matches found, diaplay a message
		frame.showDialog("No matching cards were found...", "Suggestion results");
	}

	/**
	 * Allows the player to make an accusation.
	 * First confirms that the player is sure they want to make an accusation.
	 * Checks their accusation against the murder cards, and if they're correct,
	 * end the game. If they're incorrect, they are out of the game.
	 */
	public void makeAccusation() {
		Player player = game.getCurrentPlayer();
		// confirm that the player wants to do this
		int r = JOptionPane.showConfirmDialog(frame, new JLabel("<html>Making an accusation is a serious "
				+ "business. If you get it wrong, you are out of the game! <br />Are you sure you want "
				+ "to make an accusation?</html>"),
				"Make accusation?", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if(r == 1){
			return;
		}
		// Prompt player to select cards
		String[] accusation = frame.showAccusationDialog();
		// make accusation
		if (game.accuse(accusation)){
			// player made a correct accusation and won the game
			frame.showDialog("<html>You are correct! <br />"
					+ "It was "+accusation[0]+" in the "+accusation[2]+
					" with the "+accusation[1]+"!</html>", "Accusation results");
			frame.showDialog("--GAME OVER--", "Game over");
//			gameOver = true;
			frame.enableDiceBtn(false);
		} else {
			// accusation was incorrect, insult player
			frame.showDialog("<html>You were wrong! ...you didn't really think this through...<br />"
					+ player.getCharacter()+" is out of the game!</html>", "Accusation results");
			// remove player from game
			playersInGame.remove(player);
			// check if the game is over
			if(playersInGame.size() == 0){
				frame.showDialog("<html>--GAME OVER-- <br />" //TODO check if they want to play again
						+ "It was "+accusation[0]+" in the "+accusation[2]+
						" with the "+accusation[1]+"!</html>", "Game over");
				frame.enableDiceBtn(false);
				frame.enableShortcutBtn(false);
				frame.enableSuggestBtn(false);
				frame.enableAccuseBtn(false);
			}
		}
	}
	
	/**
	 * Takes the player to the other side of the shortcut they are
	 * standing on.
	 * @param player
	 */
	public void takeShortcut(Player player) {
		try{
			if(game.getRoll() > 0){ // if there are any moves left
				ShortcutSquare shortcut = (ShortcutSquare)getPlayerSquare(player);
				player.setPos(shortcut.toRow(), shortcut.toCol());
				game.useMoves(1);
				frame.repaintAll();
				if(game.getRoll() <= 0){
					frame.enableShortcutBtn(false);
				}
			} else {
				frame.showDialog("Not enough moves!", "Invalid move");
			}
		} catch(ClassCastException e){
			// the player wasn't standing on a shortcut
			return;
		}
	}
	
}
