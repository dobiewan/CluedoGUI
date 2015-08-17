package cluedogame;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

import cluedogame.GUI.CluedoFrame;
import cluedogame.cards.*;

/**
 * Represents a game of Cluedo. Stores names of all players,
 * weapons and rooms, as well as the cards in the game, and
 * the murder cards. Organises the distribution of cards.
 * @author Sarah Dobie, Chris Read
 *
 */
public class GameOfCluedo {
	public static final int TOTAL_NUM_CARDS = 21;
	
	// Characters
	public static final String SCARLETT = "Miss Scarlett";
	public static final String MUSTARD = "Colonel Mustard";
	public static final String WHITE = "Mrs White";
	public static final String GREEN = "The Reverend Green";
	public static final String PEACOCK = "Mrs Peacock";
	public static final String PLUM = "Professor Plum";
	
	// Weapons
	public static final String CANDLESTICK = "Candlestick";
	public static final String DAGGER = "Dagger";
	public static final String LEAD_PIPE = "Lead Pipe";
	public static final String REVOLVER = "Revolver";
	public static final String ROPE = "Rope";
	public static final String SPANNER = "Spanner";
	
	// Rooms
	public static final String KITCHEN = "Kitchen";
	public static final String BALL_ROOM = "Ball Room";
	public static final String CONSERVATORY = "Conservatory";
	public static final String BILLIARD_ROOM = "Billiard Room";
	public static final String LIBRARY = "Library";
	public static final String STUDY = "Study";
	public static final String HALL = "Hall";
	public static final String LOUNGE = "Lounge";
	public static final String DINING_ROOM = "Dining Room";
	
	private List<Card> characterCards;
	private List<Card> roomCards;
	private List<Card> weaponCards;
	private Card[] murderCards = new Card[3];
	List<Player> players = new ArrayList<Player>();
	private Player currentPlayer;
	private Board board;
	private int roll;
	private GameController control;
	private boolean isReady = false;
	
	/**
	 * Constructor for class GameOfCluedo
	 */
	public GameOfCluedo(CluedoFrame frame){
		this.board = new Board();
		setupCards();
		setMurderCards();
		control = new GameController(this, frame);
		isReady = false;
	}
	
	/**
	 * Generate a list of all cards in the game, and
	 * shuffles the order.
	 * @return A shuffled List of all game cards.
	 */
	private void setupCards(){
		
		// add character cards
		List<Card> cCards = new ArrayList<Card>();
		try {	
		cCards.add(new CharacterCard(SCARLETT, ImageIO.read(new File("Images/ScarlettCard.png"))));
		cCards.add(new CharacterCard(MUSTARD, ImageIO.read(new File("Images/MustardCard.png"))));
		cCards.add(new CharacterCard(WHITE, ImageIO.read(new File("Images/WhiteCard.png"))));
		cCards.add(new CharacterCard(GREEN, ImageIO.read(new File("Images/GreenCard.png"))));
		cCards.add(new CharacterCard(PEACOCK, ImageIO.read(new File("Images/PeacockCard.png"))));
		cCards.add(new CharacterCard(PLUM, ImageIO.read(new File("Images/PlumCard.png"))));
		Collections.shuffle(cCards);
		this.characterCards = cCards;
		
		// add room cards
		List<Card> rCards = new ArrayList<Card>();
		rCards.add(new RoomCard("Conservatory", ImageIO.read(new File("Images/RoomCard.png"))));
		rCards.add(new RoomCard("Billiard Room", ImageIO.read(new File("Images/RoomCard.png"))));
		rCards.add(new RoomCard("Library", ImageIO.read(new File("Images/RoomCard.png"))));
		rCards.add(new RoomCard("Study", ImageIO.read(new File("Images/RoomCard.png"))));
		rCards.add(new RoomCard("Hall", ImageIO.read(new File("Images/RoomCard.png"))));
		rCards.add(new RoomCard("Lounge", ImageIO.read(new File("Images/RoomCard.png"))));
		rCards.add(new RoomCard("Dining Room", ImageIO.read(new File("Images/RoomCard.png"))));
		rCards.add(new RoomCard("Kitchen", ImageIO.read(new File("Images/RoomCard.png"))));
		rCards.add(new RoomCard("Ball Room", ImageIO.read(new File("Images/RoomCard.png"))));
		Collections.shuffle(rCards);
		this.roomCards = rCards;
		
		// add weapon cards
		List<Card> wCards = new ArrayList<Card>();
		wCards.add(new WeaponCard("Candlestick", ImageIO.read(new File("Images/WeaponCard.png"))));
		wCards.add(new WeaponCard("Dagger", ImageIO.read(new File("Images/WeaponCard.png"))));
		wCards.add(new WeaponCard("Lead Pipe", ImageIO.read(new File("Images/WeaponCard.png"))));
		wCards.add(new WeaponCard("Revolver", ImageIO.read(new File("Images/WeaponCard.png"))));
		wCards.add(new WeaponCard("Rope", ImageIO.read(new File("Images/WeaponCard.png"))));
		wCards.add(new WeaponCard("Spanner", ImageIO.read(new File("Images/WeaponCard.png"))));
		Collections.shuffle(wCards);
		this.weaponCards = wCards;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Picks a random card from each card group (characters, rooms,
	 * weapons) and adds it to the array of murder cards.
	 */
	private void setMurderCards(){
		// choose a character card
		int randomIndex = (int) (Math.random()*(characterCards.size()-1));
		this.murderCards[0] = this.characterCards.remove(randomIndex);
		// choose a room card
		randomIndex = (int) (Math.random()*(weaponCards.size()-1));
		this.murderCards[1] = this.weaponCards.remove(randomIndex);
		// choose a weapon card
		randomIndex = (int) (Math.random()*(roomCards.size()-1));
		this.murderCards[2] = this.roomCards.remove(randomIndex);
//		System.out.println(murderCards[0].getName()+","+murderCards[1].getName()+","+murderCards[2].getName());
	}
	
	/**
	 * Evenly deals out each type of card to every player,
	 * until there are no cards left.
	 * @param players The players in the game.
	 */
	public void dealCards() {
		Queue<Player> dealTo = new LinkedList<Player>();
		dealTo.addAll(players);
		// deal character cards
		while(!characterCards.isEmpty()){
			Player p = dealTo.poll();
			p.addCard(characterCards.remove(0));
			dealTo.add(p); // put player on end of queue
		}
		// deal weapon cards
		while(!weaponCards.isEmpty()){
			Player p = dealTo.poll();
			p.addCard(weaponCards.remove(0));
			dealTo.add(p); // put player on end of queue
		}
		// deal room cards
		while(!roomCards.isEmpty()){
			Player p = dealTo.poll();
			p.addCard(roomCards.remove(0));
			dealTo.add(p); // put player on end of queue
		}
	}

	/**
	 * Rolls two dice.
	 */
	public void rollDice(){
		roll = new Random().nextInt(10) + 2;
	}

	/**
	 * Gets the number of moves remaining in the turn.
	 * @return The number of moves remaining in the turn (0-12 inclusive).
	 */
	public int getRoll() {
		return roll;
	}

	/**
	 * Uses up the given number of moves.
	 * @param movesUsed The moves to use up.
	 */
	public void useMoves(int movesUsed){
		roll -= movesUsed;
	}

	/**
	 * Takes away any remaining moves, so the turn is over.
	 */
	public void endTurn() {
		roll = 0;
	}

	/**
	 * Start the next turn.
	 * @param frame The frame the game is being played in
	 */
	public void playTurn(CluedoFrame frame) {
		control.playTurn();
	}

	/**
	 * Allow the player to make a suggestion.
	 */
	public void makeSuggestion() {
		control.makeSuggestion();
	}

	/**
	 * Allow the player to make an accusation.
	 */
	public void makeAccusation() {
		control.makeAccusation();
		
	}

	/**
	 * Checks an accusation against the murder cards.
	 * @param accusation String array containing the character, weapon and room in that order.
	 * @return True if only if all three cards are correct
	 */
	public boolean accuse(String[] accusation){
		for (int i = 0; i < murderCards.length; i++){
			if (!accusation[i].equals(murderCards[i].getName())){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Allows the player to take a shortcut.
	 */
	public void takeShortcut() {
		control.takeShortcut(currentPlayer);
	}

	/**
	 * Testing method used when checking a correct accusation
	 * @return the murder cards
	 */
	public Card[] getMurderCards(){
		return murderCards;
	}
	
	/**
	 * Gets this game's board.
	 * @return The board being used by the game.
	 */
	public Board getBoard(){
		return board;
	}

	/**
	 * Adds all players to game.
	 * @param players The complete list of players in the game.
	 */
	public void setPlayers(List<Player> players){
		this.players = players;
	}

	/**
	 * Gets all players in the game.
	 * @return A list of all players in the game.
	 */
	public List<Player> getPlayers(){
		return this.players;
	}

	/**
	 * Adds a player to the game.
	 */
	public void addPlayer(Player p){
		this.players.add(p);
		this.control.addPlayer(p);
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
	/**
	 * checks if the game is set up, returns true if it has been
	 * @return
	 */
	public boolean isReady(){
		return isReady;
	}
	
	public void isReady(boolean ready){
		isReady = ready;
	}
	
	/**
	 * Determines whether there is a player at the given position.
	 * @param row The row of the position
	 * @param col The column of the position
	 * @return True if and only if there is a player at the given position.
	 */
	public boolean hasPlayerAt(int row, int col){
		for(Player p : players){
			if(p.row() == row && p.column() == col){
				return true;
			}
		}
		return false;
	}
	
}
