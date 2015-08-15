package cluedogame;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import cluedogame.GUI.CluedoFrame;
import cluedogame.cards.Card;
import cluedogame.sqaures.RoomSquare;

public class GameController {
	
	private GameOfCluedo game;
	private CluedoFrame frame;
	private boolean gameOver = false;
	LinkedList<Player> playersInGame = new LinkedList<Player>(); //players still in game
	
	public GameController(GameOfCluedo game, CluedoFrame frame) {
		this.game = game;
		this.frame = frame;
		playersInGame.addAll(game.getPlayers());
	}
	
	public void addPlayer(Player p){
		playersInGame.add(p);
	}
	
	public void playTurn(){
		if (!gameOver && playersInGame.size() > 0) {
			Player player = playersInGame.peek();
			game.setCurrentPlayer(player);
			game.rollDice();
			frame.showDialog(player.getName()+" rolls a "+game.getRoll(), "Dice roll");
			// check if player can suggest or not
			if(game.getBoard().squareAt(player.row(), player.column()) instanceof RoomSquare){
				frame.enableSuggestBtn(true);
			} else {
				frame.enableSuggestBtn(false);
			}
			// put player on end of queue
			playersInGame.add(playersInGame.poll());
		}
	}
	
	
	public void makeSuggestion() {
		Player player = game.getCurrentPlayer();
		RoomSquare square = (RoomSquare)game.getBoard().squareAt(player.row(), player.column());
		String room = square.getRoom();
		String[] suggestions = frame.showSuggestionDialog(room);
		String character = suggestions[0];
		String weapon = suggestions[1];
		
		// iterate over players' hands to find a matching card
		for (Player otherPlayer : game.getPlayers()){
			if (otherPlayer != player){
				for (Card c : otherPlayer.getHand()){
					String cardName = c.getName();
					if ((cardName.equals(character) || cardName.equals(weapon) || cardName.equals(room))
							&& !player.hasSeenCard(c)){
						frame.showDialog(otherPlayer.getName() + " has the card: " + cardName, "Suggestion results");
						player.addCardSeen(c);
						return;
					}
				}
			}
		}
		frame.showDialog("No matching cards were found...", "Suggestion results");
	}

	
	public void makeAccusation() {
		Player player = game.getCurrentPlayer();
		// confirm that the player wants to do this
		int r = JOptionPane.showConfirmDialog(frame, new JLabel("Making an accusation is a serious "
				+ "business.\n If you get it wrong, you are out of the game! \nAre you sure you want "
				+ "to make an accusation?"),
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
			frame.showDialog("You are correct! \n"
					+ "It was "+accusation[0]+" in the "+accusation[2]+
					" with the "+accusation[1]+"!", "Accusation results");
			frame.showDialog("--GAME OVER--", "Game over");
			gameOver = true;
			frame.enableDiceBtn(false);
		} else {
			// accusation was incorrect, insult player
			frame.showDialog("You were wrong!\n ...you didn't really think this through...\n"
					+ player.getName()+" is out of the game!", "Accusation results");
			playersInGame.remove(player);
			if(playersInGame.size() == 0){
				frame.showDialog("--GAME OVER-- \n" //TODO check if they want to play again
						+ "It was "+accusation[0]+" in the "+accusation[2]+
						" with the "+accusation[1]+"!", "Game over");
			}
			frame.enableDiceBtn(false);
		}
	}
	
}
