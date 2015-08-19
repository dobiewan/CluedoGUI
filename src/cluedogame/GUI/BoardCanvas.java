package cluedogame.GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cluedogame.Board;
import cluedogame.GameOfCluedo;
import cluedogame.Player;
import cluedogame.sqaures.DoorSquare;
import cluedogame.sqaures.RoomSquare;
import cluedogame.sqaures.ShortcutSquare;
import cluedogame.sqaures.Square;

/**
 * A canvas which shows the Cluedo playing board.
 * @author Sarah Dobie and Christ Read
 *
 */
public class BoardCanvas extends JPanel implements MouseListener, MouseMotionListener {
	
	public static int TOOLTIP_WIDTH = 150;
	public static int TOOLTIP_HEIGHT = 40;
	
	private Image boardImage;
	private Image resizedImage;
	private Image moveImage;
	private CluedoFrame frame;
	private List<Square> path;
	private String toolTipLine1;
	private String toolTipLine2;
	private int toolTipX;
	private int toolTipY;
	private boolean toolTipRight;
	
	/**
	 * Constructor for class BoardCanvas.
	 * @param frame The CluedoFrame containing this canvas.
	 */
	public BoardCanvas(CluedoFrame frame){
		addMouseListener(this);
		addMouseMotionListener(this);
		this.frame = frame;
		try {
			boardImage = ImageIO.read(new File("Images"+File.separator+"board.png"));
			resizedImage = boardImage.getScaledInstance(CluedoFrame.BOARD_CANVAS_WIDTH,
					CluedoFrame.BOARD_CANVAS_HEIGHT, Image.SCALE_FAST);
			moveImage = ImageIO.read(new File("Images"+File.separator+"Move.png"))
					.getScaledInstance(25,25, Image.SCALE_FAST);
		} catch (IOException e) {
			System.out.println("Could not read image file: "+e.getMessage());
		}
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	@Override
	public void paint(Graphics g){
//		Image resizedImage = boardImage.getScaledInstance(CluedoFrame.BOARD_CANVAS_WIDTH,
//				CluedoFrame.BOARD_CANVAS_HEIGHT, Image.SCALE_SMOOTH);
		// draw board
		g.drawImage(resizedImage, 0, 0, null);
		// draw players
		for(Player p : frame.getPlayers()){
			p.draw(g);
		}
		// draw shortest path
		if(path != null){
			for(Square sq: path){
				int x = CluedoFrame.convertColToX(sq.col());
				int y = CluedoFrame.convertRowToY(sq.row());
				g.drawImage(moveImage, x, y, null);
			}
		}
		// draw tooltip
		if(toolTipLine1 != null){
			showToolTip(toolTipLine1, toolTipLine2, toolTipX, toolTipY, g);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {
		GameOfCluedo game = frame.getGame();
		int row = CluedoFrame.convertYToRow(e.getY());
		int col = CluedoFrame.convertXToCol(e.getX());
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
	public void movePlayer(GameOfCluedo game, int goalRow, int goalCol) {
		if(Board.validRow(goalRow) && Board.validCol(goalCol)){
			Board board = game.getBoard();
			// check if there is a current player
			Player currentPlayer = game.getCurrentPlayer();
			if(currentPlayer == null){
				frame.showDialog("Click 'Next Turn' first!", "Invalid move");
				return;
			}
			// determine shortest path
			Square start = board.squareAt(currentPlayer.row(), currentPlayer.column());
			Square goal = board.squareAt(goalRow, goalCol);
			List<Square> shortestPath = board.shortestPath(start, goal,
					game.getRoll(), game);
			// check for invalid path
			if(shortestPath == null){
				if(!goal.isSteppable()){
					frame.showDialog("Can't move there!", "Invalid move");
				} else {
					frame.showDialog("Not enough moves!", "Invalid move");
				}
			} else {
				// move the player
				for(Square sq : shortestPath){
					Square fromSquare = board.squareAt(currentPlayer.row(), currentPlayer.column());
					currentPlayer.moveTo(sq);
					// use up player moves
					if(!(sq instanceof RoomSquare)){
						game.useMoves(1);
					} else if(fromSquare instanceof DoorSquare){
						game.useMoves(1);
					}
					frame.repaintAll();
//					try {
//						Thread.sleep(100);
//					} catch (InterruptedException e) {e.printStackTrace();}
				}
//				game.useMoves(shortestPath.size());
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
		
	private void showToolTip(String line1, String line2, int x, int y, Graphics g){ //TODO
		g.setColor(Color.WHITE);
		int line1Width = g.getFontMetrics().stringWidth(line1);
		int line2Width = g.getFontMetrics().stringWidth(line2);
		int maxWidth = Math.max(line1Width, line2Width);
		if(x > CluedoFrame.BOARD_CANVAS_WIDTH - maxWidth){
			g.fillRect(x-maxWidth, y+5, maxWidth+10, TOOLTIP_HEIGHT);
			g.setColor(Color.BLACK);
			g.drawRect(x-maxWidth, y+5, maxWidth+10, TOOLTIP_HEIGHT);
			g.drawString(line1, x+5-maxWidth, y+20);
			g.drawString(line2, x+5-maxWidth, y+40);
		} else if(y > CluedoFrame.BOARD_CANVAS_HEIGHT - TOOLTIP_HEIGHT){
			g.fillRect(x, y+5-TOOLTIP_HEIGHT, maxWidth+10, TOOLTIP_HEIGHT);
			g.setColor(Color.BLACK);
			g.drawRect(x, y+5-TOOLTIP_HEIGHT, maxWidth+10, TOOLTIP_HEIGHT);
			g.drawString(line1, x+5, y+20-TOOLTIP_HEIGHT);
			g.drawString(line2, x+5, y+40-TOOLTIP_HEIGHT);
		} else {
			g.fillRect(x, y+5, maxWidth+10, TOOLTIP_HEIGHT);
			g.setColor(Color.BLACK);
			g.drawRect(x, y+5, maxWidth+10, TOOLTIP_HEIGHT);
			g.drawString(line1, x+5, y+20);
			g.drawString(line2, x+5, y+40);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// determine where the mouse is on the board
		int row = CluedoFrame.convertYToRow(e.getY());
		int col = CluedoFrame.convertXToCol(e.getX());
		if(Board.validRow(row) && Board.validCol(col)){
			GameOfCluedo game = frame.getGame();
			if(game != null){
				displayShortestPath(row, col, game);
				// check if hovering over player
				if(game.hasPlayerAt(row, col)){
					Player p = game.getPlayerAt(row, col);
					toolTipLine1 = p.getCharacter();
					toolTipLine2 = p.getUserName();
					toolTipX = e.getX();
					toolTipY = e.getY();
				} else {
					setToolTipToNull();
				}
			}
		}
	}

	private void setToolTipToNull() {
		toolTipLine1 = null;
		toolTipLine2 = null;
		toolTipX = 0;
		toolTipY = 0;
	}

	public void displayShortestPath(int row, int col, GameOfCluedo game) {
		Player player = frame.getGame().getCurrentPlayer();
		// if there is a current player, get their location
		if(player != null){
			Board board = game.getBoard();
			Square playerPos = board.squareAt(player.row(), player.column());
			Square mousePos = board.squareAt(row, col);
			// find the path between player and mouse
			List<Square> shortestPath = board.shortestPath(playerPos,
					mousePos, game.getRoll(), game);
			this.path = shortestPath;
			repaint();
		}
	}

	public void enableDaveMode() {
		// TODO Auto-generated method stub
		
	}
	
}
