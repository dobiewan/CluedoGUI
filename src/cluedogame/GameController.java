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
 * run each time a player takes their turn.
 * @author Sarah Dobie, Chris Read
 *
 */
public class GameController {
	
	private GameOfCluedo game; // the game this controller is associated with
	private CluedoFrame frame; // the frame the game is being played in
	private LinkedList<Player> playersInGame = new LinkedList<Player>(); //players still in game
	
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
		if (playersInGame.size() > 0) {
			// get the next player
			Player player = playersInGame.peek();
			// roll the dice
			game.setCurrentPlayer(player);
			frame.repaintAll();
			game.rollDice();
			frame.showDialog(frame.makeDave(player.getCharacter())+" rolls "+game.getRoll(), "Dice roll");
			enableButtons(player);
			// put player on end of queue
			playersInGame.add(playersInGame.poll());
		}
	}

	/**
	 * Enables the appropriate buttons in the frame.
	 * @param player The current player
	 */
	private void enableButtons(Player player) {
		Square playerSquare = getPlayerSquare(player);
		frame.enableAccuseBtn(true);
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
		return game.getBoard().squareAt(player.row(), player.col());
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
		String[] suggestions = frame.getDialogHandler().showSuggestionDialog(frame.makeDave(room));
		String character = suggestions[0];
		String weapon = suggestions[1];
		
		// get the player object from the string
		Player suggestedPlayer = null;
		for(Player p : game.getPlayers()){
			if(p.getCharacter().equals(character)){
				suggestedPlayer = p;
				break;
			}
		}
		// move the suggested player to the room
		if(suggestedPlayer != null){
			Board board = game.getBoard();
			// find the room on the board
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
						frame.showDialog(frame.makeDave(otherPlayer.getCharacter())
								+ " has the card: " + frame.makeDave(cardName), "Suggestion results");
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
		if(r == 1){ // if the player selected no, return
			return;
		}
		
		// Prompt player to select cards
		String[] accusation = frame.getDialogHandler().showAccusationDialog();
		// make accusation
		if (game.accuse(accusation)){
			// player made a correct accusation and won the game
			frame.showDialog("<html>You are correct! <br />"
					+ "It was "+frame.makeDave(accusation[0])+" in the "+frame.makeDave(accusation[2])+
					" with the "+frame.makeDave(accusation[1])+"!</html>", "Accusation results");
			if(frame.isDave()){
				frame.showDialog("--DAVE OVER--", "Game over");
			} else {
				frame.showDialog("--GAME OVER--", "Game over");
			}
			frame.disableAllButtons();
		} else {
			// accusation was incorrect, insult player
			frame.showDialog("<html>You were wrong! You didn't really think this through...<br />"
					+ frame.makeDave(player.getCharacter())+" is out of the game!</html>", "Accusation results");
			// remove player from game
			playersInGame.remove(player);
			player.setInGame(false);
			game.endTurn();
			// check if the game is over
			if(playersInGame.size() == 0){
				if(frame.isDave()){
					frame.showDialog("<html>--DAVE OVER-- <br />"
							+ "It was "+frame.makeDave(accusation[0])+" in the "+frame.makeDave(accusation[2])+
							" with the "+frame.makeDave(accusation[1])+"!</html>", "Game over");
				} else {
					frame.showDialog("<html>--GAME OVER-- <br />"
							+ "It was "+frame.makeDave(accusation[0])+" in the "+frame.makeDave(accusation[2])+
							" with the "+frame.makeDave(accusation[1])+"!</html>", "Game over");
				}
				frame.disableAllButtons();
			} else {
				// still players remaining, move to next player
				playTurn();
			}
		}
	}
	
	/**
	 * Takes the player to the other side of the shortcut they are
	 * standing on.
	 * @param player The player to move
	 */
	public void takeShortcut(Player player) {
		try{
			if(game.getRoll() > 0){ // if there are any moves left
				ShortcutSquare shortcut = (ShortcutSquare)getPlayerSquare(player);
				player.setPos(shortcut.toRow(), shortcut.toCol());
				game.useMoves(1); // use up a move
				frame.repaintAll();
				if(game.getRoll() <= 0){ // disable button if no moves left
					frame.enableShortcutBtn(false);
				}
			} else { // no moves left
				frame.showDialog("Not enough moves!", "Invalid move");
			}
		} catch(ClassCastException e){
			// the player wasn't standing on a shortcut
			return;
		}
	}
	
}
