package main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import cluedogame.*;
import cluedogame.cards.*;
import cluedogame.sqaures.*;

/**
 * The user interface for the Cluedo game.
 * @author Sarah Dobie, Chris Read
 *
 */
public class TextClient {
	static ArrayList<String> characters;
	static ArrayList<String> simpleCharacters;
	static ArrayList<String> weapons;
	static ArrayList<String> rooms;
	static boolean gameOver = false;
	
	/**
	 * Main method to run the game
	 * @param args
	 */
	public static void main(String args[]) {
		GameOfCluedo game = new GameOfCluedo();
		TextHelpers.setup();
//		game.printMurder();
	
		// Print banner
		System.out.println("*** Cluedo Version 1.0 ***");
		System.out.println("By Chris Read and Sarah Dobie, 2015");
	
		// input player info
		int nplayers = TextHelpers.inputNumber("How many players?");
		while(nplayers < 2 || nplayers > 6){
			nplayers = TextHelpers.inputNumber("There must be 2-6 players. Try again: ");
		}
		LinkedList<Player> players = inputPlayers(nplayers); // all players
		game.setPlayers(players);
		LinkedList<Player> playersInGame = (LinkedList<Player>) players.clone(); //players still in game
		game.dealCards();
	
		// begin the game
		Random dice = new Random();
		while (!gameOver && playersInGame.size() > 0) { // loop as long as the game is playing
			Player player = playersInGame.peek();
			System.out.println();
			System.out.println(player.getName() +"'s turn!");
			TextHelpers.waitToContinue();
			int roll = dice.nextInt(10) + 2;
			System.out.println(player.getName() + " rolls " + roll + ".");
			TextHelpers.waitToContinue();
			// display player's options
			playerOptions(player, playersInGame, game, roll);
			// move player to end of queue
			if(playersInGame.contains(player)){
				playersInGame.remove(player);
				playersInGame.add(player);
			}
		}
		// everyone's out and nobody made a correct accusation
		if(playersInGame.size() == 0){
			System.out.println("You didn't find the murderer!");
			System.out.println("Luckily the police are more onto it than you lot!");
			System.out.println();
			System.out.println("Do you want to know who did it and how?");
			TextHelpers.waitToContinue();
			System.out.println();
			game.printMurder();
			System.out.println();
		}
		TextHelpers.gameOver();
	}

	/**
	 * Collect player details from user input
	 */
	private static LinkedList<Player> inputPlayers(int nplayers) {
		// create a list of possible characters
		ArrayList<String> characters = (ArrayList<String>) simpleCharacters.clone();
		
		// list characters to choose from
		System.out.println("The characters you may choose from are:");
		TextHelpers.printList(characters);
	
		LinkedList<Player> players = new LinkedList<Player>();
	
		// ask each player for details
		for (int i = 1; i <= nplayers; i++) {
			String chosenCharacter = TextHelpers.inputString("Player #" + i + " character?");
			// check validity of character
			while (!characters.contains(chosenCharacter) &&
					!characters.contains(TextHelpers.capitalise(chosenCharacter))) {
				System.out.print("That's not a valid character. Please choose one of: ");
				TextHelpers.printList(characters);
				System.out.println();
				// ask again
				chosenCharacter = TextHelpers.inputString("Player #" + i + " character?");
//				chosenCharacter = TextHelpers.simpleToFullName(chosenCharacter);
			}
			
			characters.remove(chosenCharacter); // the chosen character is no longer available
			characters.remove(TextHelpers.capitalise(chosenCharacter));
			chosenCharacter = TextHelpers.simpleToFullName(chosenCharacter);
			players.add(new Player(chosenCharacter, Character.forDigit(i, 10))); // create the Player object
		}
		return players;
	}

	/**
	 * Provides the player with all possible actions they may take.
	 * @param player The player whose turn it is.
	 * @param players All players
	 * @param playersInGame The players still in the game
	 * @param game The current game of Cluedo
	 * @param roll The number rolled by the dice.
	 */
	private static void playerOptions(Player player,
			List<Player> playersInGame, GameOfCluedo game, int roll) {
		System.out.println();
		Board board = game.getBoard();
		boolean endTurn = false;
		boolean suggested = false;
		
		// go until it's no longer the player's turn
		outer: for(int i=roll; i>0 && !endTurn; i--){
			List<String> options = new ArrayList<String>(); // stores options available
			Square sq = board.squareAt(player.row(), player.column());
			game.drawBoard(playersInGame);
			
			System.out.println();
			System.out.println(i +" turns remaining");
			System.out.println("Options:");
			
			// check the directions the player can move in
			movementOptions(board, player, options, game);
			
			// option to look at player hand
			System.out.println("H : View hand");
			options.add("H");
			
			// option to look at all cards player has seen
			System.out.println("C : View all cards seen");
			options.add("C");
			
			// option to make accusation
			System.out.println("A : Make an accusation");
			options.add("A");
			
			// check if the currrent square is a room
			if(sq instanceof RoomSquare){
				// player can make a suggestion if in a room
				System.out.println("M : Make an accusatory suggestion");
				options.add("M");
			}
			
			// check for a shortcut
			if(sq instanceof ShortcutSquare){
				ShortcutSquare shortcut = (ShortcutSquare)sq;
				System.out.println("S : Take a shortcut to "+ shortcut.toRoom());
				options.add("S");
			}
			
			System.out.println();
			
			// receive user input
			String choice = TextHelpers.inputString("What will "+player.getName()+" do?");
			// check input is valid
			while(!options.contains(choice) && !options.contains(choice.toUpperCase())){
				choice = TextHelpers.inputString("Invalid input. Please try again.");
			}
			
			System.out.println();
			
			switch(choice){
			case ("L") : case("l") : player.moveLeft(); break;
			case ("R") : case("r") : player.moveRight(); break;
			case ("U") : case("u") : player.moveUp(); break;
			case ("D") : case("d") : player.moveDown(); break;
			case ("H") : case("h") : viewHand(player); i++; break;
			case ("C") : case("c") : viewCardsSeen(player); i++; break;
			case ("A") : case("a") : makeAccusation(player, playersInGame, game); endTurn = true; break;
			case ("M") : case("m") : makeSuggestion(player, game); suggested = true; endTurn = true; break;
			case ("S") : case("s") : takeShortcut(player, (ShortcutSquare)sq); break;
			}
		}
		
		if(!gameOver){
			endOfTurnOptions(player, playersInGame, game, suggested);
			System.out.println();
		}
	}

	/**
	 * Moves the player to the other end of the shortcut
	 * @param player The player to move
	 * @param shortcut The shortcut they are taking
	 */
	private static void takeShortcut(Player player, ShortcutSquare shortcut) {
		player.setPos(shortcut.toRow(), shortcut.toCol());
	}

	/**
	 * Check which directions the player can move in, and prints out
	 * the available options.
	 * @param board The board being played on
	 * @param playerRow The player's current row position
	 * @param playerCol The player's current column position
	 * @param options The list of options currently available to the player
	 */
	private static void movementOptions(Board board, Player player,
			List<String> options, GameOfCluedo game) {
		// left
		if(player.canMoveLeft(board, game)){
			System.out.println("L : Move left");
			options.add("L");
		}
		// right
		if(player.canMoveRight(board, game)){
			System.out.println("R : Move right");
			options.add("R");
		}
		// up
		if(player.canMoveUp(board, game)){
			System.out.println("U : Move up");
			options.add("U");
		}
		// down
		if(player.canMoveDown(board, game)){
			System.out.println("D : Move down");
			options.add("D");
		}
	}

	

	/**
	 * Options available to the player if they have used up all the moves
	 * in their dice roll.
	 * @param player The player whose turn it is
	 * @param playersInGame The players still in the game
	 * @param game The game being played
	 */
	private static void endOfTurnOptions(Player player,
			List<Player> playersInGame, GameOfCluedo game, Boolean suggested) {
		if(playersInGame.contains(player)){
			while(true){
				game.drawBoard(playersInGame);
				Square sq = game.getBoard().squareAt(player.row(), player.column());
				System.out.println();
				System.out.println("0 turns remaining");
				List<String> options = new ArrayList<String>(); // stores options available
				
				// option to look at player hand
				System.out.println("H : View hand");
				options.add("H");
				
				// option to look at all cards player has seen
				System.out.println("C : View all cards seen");
				options.add("C");
				
				// option to make a suggestion if player is in a room
				if (sq instanceof RoomSquare && !suggested){
					System.out.println("M : Make an accusatory suggestion");
					options.add("M");
				}
				
				// option to make accusation
				System.out.println("A : Make an accusation");
				options.add("A");
		
				System.out.println("E : End turn");
				options.add("E");
				
				String choice = TextHelpers.inputString("What will "+player.getName()+" do?");
				// check input is valid
				while(!options.contains(choice) && !options.contains(choice.toUpperCase())){
					choice = TextHelpers.inputString("Invalid input. Please try again.");
				}
				
				switch(choice){
				case ("H") : case("h") : viewHand(player); break;
				case ("C") : case("c") : viewCardsSeen(player); break;
				case ("M") : case("m") : makeSuggestion(player, game); return;
				case ("A") : case("a") : makeAccusation(player, playersInGame, game); return;
				case ("E") : case("e") : return;
				}
				
				System.out.println();
			}
		}
	}

	/**
	 * Displays all the cards in the player's hand.
	 * @param player The player whose turn it is.
	 */
	private static void viewHand(Player player) {
		System.out.println();
		TextHelpers.printList(player.getHandStrings());
		TextHelpers.waitToContinue();
	}

	/**
	 * Displays all cards the player has seen.
	 * @param player The player whose turn it is.
	 */
	private static void viewCardsSeen(Player player) {
		System.out.println();
		TextHelpers.printList(player.getCardsSeenStrings());
		TextHelpers.waitToContinue();
	}

	/**
	 * Prompts the player to enter a character, weapon and room and
	 * checks whether the named cards are in any player's hands.
	 * If so, adds them to the seen cards of the players
	 * @param player The current player
	 * @param players All players
	 * @param game The game being played
	 */
	private static void makeSuggestion(Player player, GameOfCluedo game) {
		System.out.println();
		RoomSquare square = (RoomSquare)game.getBoard().squareAt(player.row(), player.column());
		String room = square.getRoom();
		System.out.println("You're in the " + room);
		System.out.println("Suggest a character and weapon:");
		System.out.println();
		
		String character = TextHelpers.selectCharacter();
		System.out.println();
		
		String weapon = TextHelpers.selectWeapon();
		System.out.println();
		
		
		// iterate over players' hands to find a matching card
		for (Player otherPlayer : game.getPlayers()){
			if (otherPlayer != player){
				for (Card c : otherPlayer.getHand()){
					String cardName = c.getName();
					if ((cardName.equals(character) || cardName.equals(weapon) || cardName.equals(room))
							&& !player.hasSeenCard(c)){
						System.out.println(otherPlayer.getName() + " has the card: " + cardName);
						player.addCardSeen(c);
						TextHelpers.waitToContinue();
						return;
					}
				}
			}
		}
		System.out.println();
		System.out.println("No matching cards were found...");
		System.out.println();
	}

	/**
	 * Prompts the player to choose a character, weapon and room
	 * and checks them against the murderer, murder weapon and murder location.
	 * If successful, the player wins the game.
	 * @param player The player making the accusation
	 * @param game The game being played
	 */
	private static void makeAccusation(Player player, List<Player> playersInGame,
			GameOfCluedo game) {
		// Print ominous message
		System.out.println();
		System.out.println("This is serious business... One of us could be the murderer!");
		System.out.println();
		// Prompt player to select cards
		String[] accusation = new String[3];
		accusation[0] = TextHelpers.selectCharacter();
		System.out.println();
		accusation[1] = TextHelpers.selectWeapon();
		System.out.println();
		accusation[2] = TextHelpers.selectRoom();
		System.out.println();
		// make accusation
		if (game.accuse(accusation)){
			// player made a correct accusation and won the game
			System.out.println("You are correct!");
			TextHelpers.waitToContinue();
			System.out.println();
			game.printMurder();
			System.out.println();
			gameOver = true;
		} else {
			// accusation was incorrect, insult player
			System.out.println("You were wrong!\n ...you didn't really think this through...");
			playersInGame.remove(player);
			System.out.println();
			System.out.println(player.getName() +" is out of the game!");
			System.out.println();
		}
	}
	
	
}
