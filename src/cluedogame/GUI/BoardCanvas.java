package cluedogame.GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import cluedogame.Board;
import cluedogame.GameOfCluedo;
import cluedogame.Player;
import cluedogame.cards.Card;
import cluedogame.sqaures.DoorSquare;
import cluedogame.sqaures.RoomSquare;
import cluedogame.sqaures.ShortcutSquare;
import cluedogame.sqaures.Square;

/**
 * A canvas which shows the Cluedo playing board.
 * @author Sarah Dobie, Chris Read
 *
 */
public class BoardCanvas extends JPanel implements MouseListener, MouseMotionListener, ActionListener {
	
	public static final int TOOLTIP_HEIGHT = 40; // the height of tooltip windows
	
	private CluedoFrame frame; // the frame containing this canvas
	private GameOfCluedo game; // the game represented on the board
	private Image boardImage; // original board image
	private List<Square> possiblePath; // the path to draw when mouse has moved
	private Queue<Square> movingPlayerQueue = new LinkedList<Square>(); // path for current player to follow
	private Timer timer = new Timer(100, this); // a thread used for animating player movement
	private Player currentPlayer; // the player whose turn it currently is
	private boolean playerMoving = false; // true if a player is currently moving
	
	// image fields
	private Image resizedBoardImage; // resized board image
	private Image cardsSeenImage; // cards seen window image
	private Image cardsSeenResized;
	private Image moveImage; // image used to draw player path
	private Image moveImageResized;
	
	// tooltip fields
	private String toolTipLine1; // the first line of the tooltip
	private String toolTipLine2; // the second line of the tooltip
	private int toolTipX; // the x position of the tooltip
	private int toolTipY; // the y position of the tooltip
	
	
	/**
	 * Constructor for class BoardCanvas.
	 * @param frame The CluedoFrame containing this canvas.
	 */
	public BoardCanvas(CluedoFrame frame){
		addMouseListener(this);
		addMouseMotionListener(this);
		this.frame = frame;
		this.game = frame.getGame();
		this.timer.start();
		loadImages();
	}

	/**
	 * Loads image field files.
	 */
	public void loadImages() {
		try {
			boardImage = ImageIO.read(new File("Images"+File.separator+"board.png"));
			cardsSeenImage = ImageIO.read(new File("Images"+File.separator+"CardsSeen.png"));
			moveImage = ImageIO.read(new File("Images"+File.separator+"Move.png"));
		} catch (IOException e) {
			System.out.println("Could not read image file: "+e.getMessage());
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	@Override
	public void paint(Graphics g){
		int pixel = frame.getPixelSize();
		// resize images
		resizedBoardImage = boardImage.getScaledInstance(frame.BOARD_CANVAS_WIDTH,
				frame.BOARD_CANVAS_HEIGHT, Image.SCALE_FAST);
		cardsSeenResized = cardsSeenImage.getScaledInstance(frame.BOARD_CANVAS_WIDTH,
				frame.BOARD_CANVAS_HEIGHT, Image.SCALE_FAST);
		moveImageResized = moveImage.getScaledInstance(pixel*5,pixel*5, Image.SCALE_FAST);
		// draw board
		g.drawImage(resizedBoardImage, 0, 0, null);
		// draw shortest path
		if(possiblePath != null){
			for(Square sq: possiblePath){
				int x = frame.convertColToX(sq.col());
				int y = frame.convertRowToY(sq.row());
				g.drawImage(moveImageResized, x, y, null);
			}
		}
		// draw players
		for(Player p : frame.getPlayers()){
			p.draw(g, frame);
		}
		// draw tooltip
		if(toolTipLine1 != null){
			showToolTip(toolTipLine1, toolTipLine2, toolTipX, toolTipY, g);
		}
		
		// draw cards seen window
		if (game.cardsSeenWindow()){
			drawCardsSeen(g, pixel);
		}
	}

	/**
	 * Draws the cards seen window.
	 * @param g The graphics object on which to draw the window
	 * @param pixel The current pixel size
	 */
	private void drawCardsSeen(Graphics g, int pixel) {
		if(currentPlayer == null){ // check the game is in play
			return;
		}
		// draw the background
		g.drawImage(cardsSeenResized, 0, 0, null);
		Image icon = currentPlayer.getToken().getScaledInstance(9*pixel, 9*pixel, Image.SCALE_FAST);
		List<Card> seen = currentPlayer.getCardsSeen();
		// draws the player token next to all seen cards
		for (Card c : seen){
			int id = c.getID();
			if (id < 10){
				g.drawImage(icon, (pixel*3)-(pixel*2), (pixel*5)+(pixel*8*id)-(pixel*2), null);
			} else if (id < 20){
				g.drawImage(icon, (pixel*3)-(pixel*2), (pixel*57)+(pixel*8*(id-10))-(pixel*2), null);
			} else {
				g.drawImage(icon, (pixel*67)-(pixel*2), (pixel*5)+(pixel*8*(id-20))-(pixel*2), null);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {
		// hide the cards seen window if it's showing
		if (game.cardsSeenWindow()){
			game.setCardsSeenWindow(false);
			return;
		}
		// try to move the player to the clicked square
		GameOfCluedo game = frame.getGame();
		int row = frame.convertYToRow(e.getY());
		int col = frame.convertXToCol(e.getX());
//		System.out.println("Mouse released at ("+e.getX()+","+e.getY()+")");
//		System.out.println("Mouse released at ("+row+","+col+")");
		movePlayer(game, row, col);
	}

	/**
	 * Determines the shortest path between the player and the square they clicked on.
	 * If this path is valid, the player is moved down that path.
	 * If the path is invalid, a message is displayed.
	 * @param game The current GameOfCluedo being played
	 * @param goalRow The row that the mouse was clicked in
	 * @param goalCol The column that the mouse was clicked in
	 */
	private void movePlayer(GameOfCluedo game, int goalRow, int goalCol) {
		if(playerMoving){ // wait until any other players stop moving
			return;
		}
		if(Board.validRow(goalRow) && Board.validCol(goalCol)){
			Board board = game.getBoard();
			// check if there is a current player
			currentPlayer = game.getCurrentPlayer();
			if(currentPlayer == null){
				frame.showDialog("Click 'Next Turn' first!", "Invalid move");
				return;
			}
			// determine shortest path
			Square start = board.squareAt(currentPlayer.row(), currentPlayer.col());
			Square goal = board.squareAt(goalRow, goalCol);
			List<Square> shortestPath = board.shortestPath(start, goal,
					game.getRoll(), game);
			possiblePath = null;
			// check for invalid path
			if(shortestPath == null){
				if(!goal.isSteppable()){
					frame.showDialog("Can't move there!", "Invalid move");
				} else {
					frame.showDialog("Not enough moves!", "Invalid move");
				}
			} else {
				// move the player
				this.movingPlayerQueue.addAll(shortestPath);
				timer.restart();
					
				// check if player is in room or not
				if(goal instanceof RoomSquare || goal instanceof ShortcutSquare){
					frame.enableSuggestBtn(true);
				} else {
					frame.enableSuggestBtn(false);
				}
				// check if player is on a shortcut or not
				if(goal instanceof ShortcutSquare && game.getRoll() > 0){
					frame.enableShortcutBtn(true);
				} else {
					frame.enableShortcutBtn(false);
				}
				
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {}
		
	private void showToolTip(String line1, String line2, int x, int y, Graphics g){
		// determine which of line1 and line2 is shorter in pixels
		int line1Width = g.getFontMetrics().stringWidth(line1);
		int line2Width = g.getFontMetrics().stringWidth(line2);
		int maxWidth = Math.max(line1Width, line2Width);
		// set up variables for drawing
		int boxX = x;
		int boxY = y+5;
		int boxWidth = maxWidth+10;
		int lineX = x+5;
		int line1Y = y+20;
		int line2Y = y+40;
		// check if box is too far to right to draw
		if(x > frame.BOARD_CANVAS_WIDTH - maxWidth){ // mouse is too far to the right
			boxX -= maxWidth;
			lineX -= maxWidth;
		}
		// check if box is too close to the bottom to draw
		if(y > frame.BOARD_CANVAS_HEIGHT - TOOLTIP_HEIGHT){ // mouse too far down
			boxY -= TOOLTIP_HEIGHT;
			line1Y -= TOOLTIP_HEIGHT;
			line2Y -= TOOLTIP_HEIGHT;
		}
		// draw the tooltip
		g.setColor(Color.WHITE);
		g.fillRect(boxX, boxY, boxWidth, TOOLTIP_HEIGHT);
		g.setColor(Color.BLACK);
		g.drawRect(boxX, boxY, boxWidth, TOOLTIP_HEIGHT);
		g.drawString(line1, lineX, line1Y);
		g.drawString(line2, lineX, line2Y);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// determine where the mouse is on the board
		int row = frame.convertYToRow(e.getY());
		int col = frame.convertXToCol(e.getX());
		if(Board.validRow(row) && Board.validCol(col)){
			GameOfCluedo game = frame.getGame();
			if(game != null){
				displayShortestPath(row, col);
				// check if hovering over player
				if(game.hasPlayerAt(row, col)){
					Player p = game.getPlayerAt(row, col);
					// set up tooltip
					toolTipLine1 = p.getCharacter();
					toolTipLine2 = p.getUserName();
					toolTipX = e.getX();
					toolTipY = e.getY();
				} else {
					setToolTipToNull();
				}
				repaint();
			}
		}
	}

	/**
	 * Sets all tooltip fields to null to indicate that no tooltip
	 * should be shown.
	 */
	private void setToolTipToNull() {
		toolTipLine1 = null;
		toolTipLine2 = null;
		toolTipX = 0;
		toolTipY = 0;
	}

	/**
	 * Draw the shortest path between the current player and the mouse on
	 * the board.
	 * @param startRow The player's row
	 * @param startCol The player's column
	 */
	private void displayShortestPath(int startRow, int startCol) {
		if(playerMoving){ // don't display a new path if a player is still moving
			return;
		}
		GameOfCluedo game = frame.getGame();
		currentPlayer = game.getCurrentPlayer();
		// if there is a current player, get their location
		if(currentPlayer != null){
			Board board = game.getBoard();
			Square playerPos = board.squareAt(currentPlayer.row(), currentPlayer.col());
			Square mousePos = board.squareAt(startRow, startCol);
			// find the path between player and mouse
			List<Square> shortestPath = board.shortestPath(playerPos,
					mousePos, game.getRoll(), game);
			this.possiblePath = shortestPath;
			repaint();
		}
	}

	public void enableDaveMode() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) { // called every 250 milliseconds
		if(!movingPlayerQueue.isEmpty()){ // a player should be moving
			playerMoving = true;
			GameOfCluedo game = frame.getGame();
			Board board = game.getBoard();
			Square fromSquare = board.squareAt(currentPlayer.row(), currentPlayer.col());
			Square sq = movingPlayerQueue.poll();
			// move the player to the next square
			currentPlayer.moveTo(sq);
			// use up player moves
			if(!(sq instanceof RoomSquare)){
				game.useMoves(1);
			} else if(fromSquare instanceof DoorSquare){
				game.useMoves(1);
			}
		} else {
			playerMoving = false;
		}
		frame.resize();
		frame.repaintAll();
	}
	
}
