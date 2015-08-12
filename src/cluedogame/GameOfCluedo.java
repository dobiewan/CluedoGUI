package cluedogame;

import java.util.*;

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
	List<Player> players;
	private Board board;
	
	/**
	 * Constructor for class GameOfCluedo
	 */
	public GameOfCluedo(){
		this.board = new Board();
		setupCards();
		setMurderCards();
	}
	
	/**
	 * Generate a list of all cards in the game, and
	 * shuffles the order.
	 * @return A shuffled List of all game cards.
	 */
	private void setupCards(){
		
		// add character cards
		List<Card> cCards = new ArrayList<Card>();
		cCards.add(new CharacterCard(SCARLETT));
		cCards.add(new CharacterCard(MUSTARD));
		cCards.add(new CharacterCard(WHITE));
		cCards.add(new CharacterCard(GREEN));
		cCards.add(new CharacterCard(PEACOCK));
		cCards.add(new CharacterCard(PLUM));
		Collections.shuffle(cCards);
		this.characterCards = cCards;
		
		// add room cards
		List<Card> rCards = new ArrayList<Card>();
		rCards.add(new RoomCard("Conservatory"));
		rCards.add(new RoomCard("Billiard Room"));
		rCards.add(new RoomCard("Library"));
		rCards.add(new RoomCard("Study"));
		rCards.add(new RoomCard("Hall"));
		rCards.add(new RoomCard("Lounge"));
		rCards.add(new RoomCard("Dining Room"));
		rCards.add(new RoomCard("Kitchen"));
		rCards.add(new RoomCard("Ball Room"));
		Collections.shuffle(rCards);
		this.roomCards = rCards;
		
		// add weapon cards
		List<Card> wCards = new ArrayList<Card>();
		wCards.add(new WeaponCard("Candlestick"));
		wCards.add(new WeaponCard("Dagger"));
		wCards.add(new WeaponCard("Lead Pipe"));
		wCards.add(new WeaponCard("Revolver"));
		wCards.add(new WeaponCard("Rope"));
		wCards.add(new WeaponCard("Spanner"));
		Collections.shuffle(wCards);
		this.weaponCards = wCards;
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
	 * Testing method used when checking a correct accusation
	 * @return the murder cards
	 */
	public Card[] getMurderCards(){
		return murderCards;
	}
	
	/**
	 * Prints out the murderer, their weapon and room.
	 */
	public void printMurder(){
		System.out.println(murderCards[0].getName() +
				" used the " + murderCards[1].getName() +
				" in the " + murderCards[2].getName() + "!");
	}
	
	/**
	 * Displays the board on the console.
	 */
	public void drawBoard(List<Player> players) {
		board.draw(players);
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
