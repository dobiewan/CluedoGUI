package cluedogame.tests;

import java.util.LinkedList;
import java.util.List;

import org.junit.*;

import cluedogame.Board;
import cluedogame.GameOfCluedo;
import cluedogame.Player;
import cluedogame.cards.Card;
import cluedogame.cards.CharacterCard;
import cluedogame.cards.WeaponCard;
import cluedogame.sqaures.RoomSquare;
import cluedogame.sqaures.ShortcutSquare;
import static org.junit.Assert.*;

public class CluedoTests {

	/**
	 * These three tests check that the game state is set up
	 * correctly, for the players, cards and board state respectively.
	 */
	@Test
	public void testPlayerSetup(){
		GameOfCluedo game = new GameOfCluedo();
		List<Player> players = makePlayers(6);
		game.setPlayers(players);
		assertEquals(players, game.getPlayers());
	}
	
	@Test
	public void testCardDealSetup(){
		GameOfCluedo game = new GameOfCluedo();
		List<Player> players = makePlayers(6);
		game.setPlayers(players);
		game.dealCards();
		players = game.getPlayers();
		for (Player p : players){
			assertFalse(p.getHandStrings().isEmpty());
		}
	}
	
	@Test
	public void testBoardSetup(){
		GameOfCluedo game = new GameOfCluedo();
		Board board = game.getBoard();
		List<Player> players = makePlayers(3);
		game.drawBoard(players);
		assertTrue(board.squareAt(2, 2) instanceof RoomSquare);
	}
	
	/**
	 * These two tests check that correct and 
	 * incorrect accusations resolve correctly.
	 */
	@Test
	public void testFalseAccuse(){
		GameOfCluedo game = new GameOfCluedo();
		List<Player> players = makePlayers(4);
		game.setPlayers(players);
		game.dealCards();
		game.printMurder();
		Card card = players.get(2).getHand().get(0);
		String[] accusation = new String[3];
		assertFalse(card == null);
		if (card instanceof CharacterCard){
			accusation[0] = card.getName();
			accusation[1] = GameOfCluedo.DAGGER;
			accusation[2] = GameOfCluedo.KITCHEN;
		} else if (card instanceof WeaponCard){
			accusation[0] = GameOfCluedo.MUSTARD;
			accusation[1] = card.getName();
			accusation[2] = GameOfCluedo.KITCHEN;
		} else {
			accusation[0] = GameOfCluedo.MUSTARD;
			accusation[1] = GameOfCluedo.DAGGER;
			accusation[2] = card.getName();
		}
		assertFalse(game.accuse(accusation));
	}
	
	@Test
	public void testTrueAccuse(){
		GameOfCluedo game = new GameOfCluedo();
		Card[] murderCards = game.getMurderCards();
		String[] accusation = new String[3];
		for(int i = 0; i < 3; i++){
			accusation[i] = murderCards[i].getName();
		}
		assertTrue(game.accuse(accusation));
	}
	
	/**
	 * Tests whether a player correctly stores a card he/she has seen 
	 * in several situations
	 */
	@Test
	public void testPlayerCardStrings(){
		GameOfCluedo game = new GameOfCluedo();
		List<Player> players = makePlayers(3);
		game.setPlayers(players);
		game.dealCards();
		
		Player p = players.get(1);
		assertFalse(p.getHand().isEmpty());
		for (int i = 0; i < p.getHand().size(); i++){
			assertTrue(p.getHandStrings().get(i).equals(p.getCardsSeenStrings().get(i)));
		}
	}
	
	@Test
	public void testPlayerHasSeenHand(){
		GameOfCluedo game = new GameOfCluedo();
		List<Player> players = makePlayers(3);
		game.setPlayers(players);
		game.dealCards();
		
		Player p = players.get(1);
		assertFalse(p.getHand().isEmpty());
		for (Card c : p.getHand()){
			assertTrue(p.hasSeenCard(c));
		}
	}
	
	@Test
	public void testPlayerSeeNewCard(){
		GameOfCluedo game = new GameOfCluedo();
		List<Player> players = makePlayers(3);
		game.setPlayers(players);
		game.dealCards();
		
		Player p = players.get(1);
		Player p2 = players.get(2);
		Card card = p2.getHand().get(0);
		p.addCardSeen(card);
		assertTrue(p.hasSeenCard(card));
	}
	
	@Test
	public void testPlayerSuggest(){
		GameOfCluedo game = new GameOfCluedo();
		List<Player> players = makePlayers(5);
		game.setPlayers(players);
		game.dealCards();
		
		Player p1 = players.get(1);
		Player p2 = players.get(2);
		Card card = p2.getHand().get(0);
		String cardName = card.getName();
		outer: for (Player p : players){
			for (Card c : p.getHand()){
				if (c.getName().equals(cardName)){
					p1.addCardSeen(c);
					break outer;
				}
			}
		}
		assertTrue(p1.hasSeenCard(card));
	}
	
	/**
	 * This group tests the canMove methods of the Player class,
	 * also tests the failure states
	 */
	@Test
	public void testPlayerCanMoveUp(){
		GameOfCluedo game = new GameOfCluedo();
		List<Player> players = makePlayers(2);
		game.setPlayers(players);
		Board board = game.getBoard();
		Player p = game.getPlayers().get(0);
		p.setPos(24, 7);
		assertTrue(p.canMoveUp(board, game));
	}
	
	@Test
	public void testPlayerCantMoveUp(){
		GameOfCluedo game = new GameOfCluedo();
		List<Player> players = makePlayers(2);
		game.setPlayers(players);
		Board board = game.getBoard();
		Player p = game.getPlayers().get(0);
		p.setPos(0, 0);
		assertFalse(p.canMoveUp(board, game));
	}
	
	@Test
	public void testPlayerCanMoveDown(){
		GameOfCluedo game = new GameOfCluedo();
		List<Player> players = makePlayers(2);
		game.setPlayers(players);
		Board board = game.getBoard();
		Player p = game.getPlayers().get(0);
		p.setPos(23, 7);
		assertTrue(p.canMoveDown(board, game));
	}
	
	@Test
	public void testPlayerCantMoveDown(){
		GameOfCluedo game = new GameOfCluedo();
		List<Player> players = makePlayers(2);
		game.setPlayers(players);
		Board board = game.getBoard();
		Player p = game.getPlayers().get(0);
		p.setPos(24, 1);
		assertFalse(p.canMoveDown(board, game));
	}
	
	@Test
	public void testPlayerCanMoveLeft(){
		GameOfCluedo game = new GameOfCluedo();
		List<Player> players = makePlayers(2);
		game.setPlayers(players);
		Board board = game.getBoard();
		Player p = game.getPlayers().get(0);
		p.setPos(22, 8);
		assertTrue(p.canMoveLeft(board, game));
	}
	
	@Test
	public void testPlayerCantMoveLeft(){
		GameOfCluedo game = new GameOfCluedo();
		List<Player> players = makePlayers(2);
		game.setPlayers(players);
		Board board = game.getBoard();
		Player p = game.getPlayers().get(0);
		p.setPos(0, 0);
		assertFalse(p.canMoveLeft(board, game));
	}
	
	@Test
	public void testPlayerCanMoveRight(){
		GameOfCluedo game = new GameOfCluedo();
		List<Player> players = makePlayers(2);
		game.setPlayers(players);
		Board board = game.getBoard();
		Player p = game.getPlayers().get(0);
		p.setPos(22, 7);
		assertTrue(p.canMoveRight(board, game));
	}
	
	@Test
	public void testPlayerCantMoveRight(){
		GameOfCluedo game = new GameOfCluedo();
		List<Player> players = makePlayers(2);
		game.setPlayers(players);
		Board board = game.getBoard();
		Player p = game.getPlayers().get(0);
		p.setPos(2, 23);
		assertFalse(p.canMoveRight(board, game));
	}

	
	/**
	 * This next group of tests works on the Player.move methods,
	 * checking that the destinations for each move are as expected.
	 */
	@Test
	public void testMoveUp(){
		GameOfCluedo game = new GameOfCluedo();
		List<Player> players = makePlayers(2);
		game.setPlayers(players);
		Player p = game.getPlayers().get(0);
		p.setPos(23, 7);
		p.moveUp();
		assertTrue(p.column() == 7);
		assertTrue(p.row() == 22);
	}
	
	@Test
	public void testMoveDown(){
		GameOfCluedo game = new GameOfCluedo();
		List<Player> players = makePlayers(2);
		game.setPlayers(players);
		Player p = game.getPlayers().get(0);
		p.setPos(23, 7);
		p.moveDown();
		assertTrue(p.column() == 7);
		assertTrue(p.row() == 24);
	}
	
	@Test
	public void testMoveLeft(){
		GameOfCluedo game = new GameOfCluedo();
		List<Player> players = makePlayers(2);
		game.setPlayers(players);
		Player p = game.getPlayers().get(0);
		p.setPos(23, 8);
		p.moveLeft();
		assertTrue(p.column() == 7);
		assertTrue(p.row() == 23);
	}
	
	@Test
	public void testMoveRight(){
		GameOfCluedo game = new GameOfCluedo();
		List<Player> players = makePlayers(2);
		game.setPlayers(players);
		Player p = game.getPlayers().get(0);
		p.setPos(23, 7);
		p.moveRight();
		assertTrue(p.column() == 8);
		assertTrue(p.row() == 23);
	}
	
	/**
	 * These next two methods check that shortcuts are correctly initialised,
	 * and that they respond correctly when an invalid shortcut is set up.
	 */
	@Test
	public void testShortcutCreationFail(){
		GameOfCluedo game = new GameOfCluedo();
		Board board = game.getBoard();
		ShortcutSquare shortcut = new ShortcutSquare("Ball Room", board);
		assertTrue(shortcut.toRoom().equals("Ball Room"));
		assertTrue(shortcut.toCol() == -1);
		assertTrue(shortcut.toRow() == -1);
	}
	
	@Test
	public void testShortcutCreation(){
		GameOfCluedo game = new GameOfCluedo();
		Board board = game.getBoard();
		ShortcutSquare shortcut = new ShortcutSquare("Study", board);
		assertTrue(shortcut.toRoom().equals("Study"));
		assertTrue(shortcut.toCol() == 22);
		assertTrue(shortcut.toRow() == 21);
	}
	
	/**
	 * Creates an List of the specified number of players
	 * @param Number of players to create - defaults to two.
	 * @return The List of players
	 */
	public List<Player> makePlayers(int count){
		List<Player> players = new LinkedList<Player>();
		switch(count){
		case 6:
			players.add(new Player(GameOfCluedo.PLUM, '5'));
		case 5:
			players.add(new Player(GameOfCluedo.PEACOCK, '4'));
		case 4:
			players.add(new Player(GameOfCluedo.GREEN, '3'));
		case 3:
			players.add(new Player(GameOfCluedo.WHITE, '2'));
		default:
			players.add(new Player(GameOfCluedo.MUSTARD, '1'));
			players.add(new Player(GameOfCluedo.SCARLETT, '0'));
		}
		return players;
	}
}
