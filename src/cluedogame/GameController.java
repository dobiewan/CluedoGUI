package cluedogame;

import java.util.LinkedList;
import java.util.List;

import main.TextHelpers;
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
		frame.showSuggestionDialog(room);
		
//		// iterate over players' hands to find a matching card
//		for (Player otherPlayer : game.getPlayers()){
//			if (otherPlayer != player){
//				for (Card c : otherPlayer.getHand()){
//					String cardName = c.getName();
//					if ((cardName.equals(character) || cardName.equals(weapon) || cardName.equals(room))
//							&& !player.hasSeenCard(c)){
//						System.out.println(otherPlayer.getName() + " has the card: " + cardName);
//						player.addCardSeen(c);
//						TextHelpers.waitToContinue();
//						return;
//					}
//				}
//			}
//		}
//		System.out.println();
//		System.out.println("No matching cards were found...");
//		System.out.println();
	}

	
//	private void makeAccusation(Player player, List<Player> playersInGame,
//			GameOfCluedo game) {
//		// Print ominous message
//		System.out.println();
//		System.out.println("This is serious business... One of us could be the murderer!");
//		System.out.println();
//		// Prompt player to select cards
//		String[] accusation = new String[3];
//		accusation[0] = TextHelpers.selectCharacter();
//		System.out.println();
//		accusation[1] = TextHelpers.selectWeapon();
//		System.out.println();
//		accusation[2] = TextHelpers.selectRoom();
//		System.out.println();
//		// make accusation
//		if (game.accuse(accusation)){
//			// player made a correct accusation and won the game
//			System.out.println("You are correct!");
//			TextHelpers.waitToContinue();
//			System.out.println();
//			game.printMurder();
//			System.out.println();
//			gameOver = true;
//		} else {
//			// accusation was incorrect, insult player
//			System.out.println("You were wrong!\n ...you didn't really think this through...");
//			playersInGame.remove(player);
//			System.out.println();
//			System.out.println(player.getName() +" is out of the game!");
//			System.out.println();
//		}
//	}
	
}
