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
	private Card[] murderCards = new Card[3]; // the cards which are the solution to the murder
	private List<Player> players = new ArrayList<Player>(); // all the players in the game
	private Player currentPlayer; // the player whose turn it is
	private Board board;
	private int roll; // the number of moves remaining for the current player
	private GameController control;
	private boolean isReady = false; // whether or not the game has finished setting up
	private boolean infiniteMovement; // true if player is allowed infinite moves
	
	private int pixelSize = 5; // size of the pixels of the art
	private boolean cardsSeen = false; // whether the cardsseen window is being displayed

	
	/**
	 * Constructor for class GameOfCluedo
	 * @param frame The CluedoFrame displaying this game.
	 */
	public GameOfCluedo(CluedoFrame frame){
		this.board = new Board();
		setupCards();
		setMurderCards();
		control = new GameController(this, frame);
		isReady = false;
	}
	
	/**
	 * Generates a list of all cards in the game, and
	 * shuffles the order.
	 * @return A shuffled List of all game cards.
	 */
	private void setupCards(){
		
		// add character cards
		List<Card> cCards = new ArrayList<Card>();
		try {	
		cCards.add(new CharacterCard(SCARLETT, ImageIO.read(new File("Images"+File.separator+"ScarlettCard.png")), 0));
		cCards.add(new CharacterCard(MUSTARD, ImageIO.read(new File("Images"+File.separator+"MustardCard.png")), 1));
		cCards.add(new CharacterCard(WHITE, ImageIO.read(new File("Images"+File.separator+"WhiteCard.png")), 5));
		cCards.add(new CharacterCard(GREEN, ImageIO.read(new File("Images"+File.separator+"GreenCard.png")), 2));
		cCards.add(new CharacterCard(PEACOCK, ImageIO.read(new File("Images"+File.separator+"PeacockCard.png")), 4));
		cCards.add(new CharacterCard(PLUM, ImageIO.read(new File("Images"+File.separator+"PlumCard.png")), 3));
		Collections.shuffle(cCards);
		this.characterCards = cCards;
		
		// add room cards
		List<Card> rCards = new ArrayList<Card>();
		rCards.add(new RoomCard("Conservatory", ImageIO.read(new File("Images"+File.separator+"ConservatoryCard.png")), 23));
		rCards.add(new RoomCard("Billiard Room", ImageIO.read(new File("Images"+File.separator+"BilliardsCard.png")), 24));
		rCards.add(new RoomCard("Library", ImageIO.read(new File("Images"+File.separator+"LibraryCard.png")), 25));
		rCards.add(new RoomCard("Study", ImageIO.read(new File("Images"+File.separator+"StudyCard.png")), 26));
		rCards.add(new RoomCard("Hall", ImageIO.read(new File("Images"+File.separator+"HallCard.png")), 27));
		rCards.add(new RoomCard("Lounge", ImageIO.read(new File("Images"+File.separator+"LoungeCard.png")), 28));
		rCards.add(new RoomCard("Dining Room", ImageIO.read(new File("Images"+File.separator+"DiningCard.png")), 20));
		rCards.add(new RoomCard("Kitchen", ImageIO.read(new File("Images"+File.separator+"KitchenCard.png")), 21));
		rCards.add(new RoomCard("Ball Room", ImageIO.read(new File("Images"+File.separator+"BallroomCard.png")), 22));
		Collections.shuffle(rCards);
		this.roomCards = rCards;
		
		// add weapon cards
		List<Card> wCards = new ArrayList<Card>();
		wCards.add(new WeaponCard("Candlestick", ImageIO.read(new File("Images"+File.separator+"CandlestickCard.png")), 12));
		wCards.add(new WeaponCard("Dagger", ImageIO.read(new File("Images"+File.separator+"DaggerCard.png")), 10));
		wCards.add(new WeaponCard("Lead Pipe", ImageIO.read(new File("Images"+File.separator+"PipeCard.png")), 13));
		wCards.add(new WeaponCard("Revolver", ImageIO.read(new File("Images"+File.separator+"RevolverCard.png")), 11));
		wCards.add(new WeaponCard("Rope", ImageIO.read(new File("Images"+File.separator+"RopeCard.png")), 15));
		wCards.add(new WeaponCard("Spanner", ImageIO.read(new File("Images"+File.separator+"SpannerCard.png")), 14));
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
		if(infiniteMovement){
			roll = Integer.MAX_VALUE;
		} else {
			roll = new Random().nextInt(10) + 2;
		}
	}
	
	/**
	 * Sets the roll to be (essentially) infinite.
	 */
	public void setRollToInfinite(){
		roll = Integer.MAX_VALUE;
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
	 */
	public void playTurn() {
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
	 * Testing method used when checking for a correct accusation.
	 * @return The three murder cards
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
	 * @param player The player to add
	 */
	public void addPlayer(Player player){
		this.players.add(player);
		this.control.addPlayer(player);
	}
	
	/**
	 * Returns the player whose turn it is.
	 * @return The Player whose turn it is at the moment.
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * Tells the game whose turn it is.
	 * @param currentPlayer The Player whose turn it is.
	 */
	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
	/**
	 * Checks if the game is set up.
	 * @return True iff the game has finished being set up
	 */
	public boolean isReady(){
		return isReady;
	}
	
	/**
	 * Manually tells the game that it must have finished setting up, or not.
	 * @param ready True if the game is ready, false otherwise.
	 */
	public void setReady(boolean ready){
		isReady = ready;
	}
	
	/**
	 * Enables/disables infinite player movement.
	 * @param infiniteMovement True to enable, false to disable
	 */
	public void setInfiniteMovement(boolean infiniteMovement) {
		this.infiniteMovement = infiniteMovement;
	}

	/**
	 * Determines whether there is a player at the given position.
	 * @param row The row of the position
	 * @param col The column of the position
	 * @return True if and only if there is a player at the given position.
	 */
	public boolean hasPlayerAt(int row, int col){
		for(Player p : players){
			if(p.row() == row && p.col() == col){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the player at the given row and column.
	 * @param row The row at which to look for a player
	 * @param col The column at which to look for a player
	 * @return The Player at the given row and column, or null if there
	 * is none.
	 */
	public Player getPlayerAt(int row, int col){
		for(Player p : players){
			if(p.row() == row && p.col() == col){
				return p;
			}
		}
		return null;
	}
	
	/**
	 * Gets the pixel drawing size.
	 * @return The current pixel size
	 */
	public int getPixelSize(){
		return pixelSize;
	}
	
	/**
	 * Sets the pixel size.
	 * @param size The new pixel size
	 */
	public void setPixelSixe(int size){
		pixelSize = size;
	}
	
	/**
	 * Determines whether the cards seen window is currently
	 * displaying.
	 * @return True iff the cards seen window is on display.
	 */
	public boolean cardsSeenWindow(){
		return cardsSeen;
	}
	
	/**
	 * Show/hide cards seen window.
	 * @param isOpen True to show, false to hide
	 */
	public void setCardsSeenWindow(boolean isOpen){
		cardsSeen = isOpen;
	}
	
	/**
	 * Reverses the current status of the cards seen window.
	 */
	public void toggleCardsSeen(){
		cardsSeen = !cardsSeen;
	}
	
}
