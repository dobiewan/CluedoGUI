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
	
	private Image boardImage;
	private Image resizedImage;
	private Image moveImage;
	private CluedoFrame frame;
	private List<Square> path;
	
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
		g.drawImage(resizedImage, 0, 0, null);
		for(Player p : frame.getPlayers()){
			p.draw(g);
		}
		if(path != null){
//			g.setColor(Color.green);
//			for(Square sq: path){
//				int x = CluedoFrame.convertColToX(sq.col());
//				int y = CluedoFrame.convertRowToY(sq.row());
//				g.fillRect(x,y,(int)CluedoFrame.squareWidth(),(int)CluedoFrame.squareHeight());
//			}
			for(Square sq: path){
				int x = CluedoFrame.convertColToX(sq.col());
				int y = CluedoFrame.convertRowToY(sq.row());
				g.drawImage(moveImage, x, y, null);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

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
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// determine where the mouse is on the board
		int row = CluedoFrame.convertYToRow(e.getY());
		int col = CluedoFrame.convertXToCol(e.getX());
		if(Board.validRow(row) && Board.validCol(col)){
			GameOfCluedo game = frame.getGame();
			if(game != null){
				Player player = frame.getGame().getCurrentPlayer();
				// if ther is a current player, get their location
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
		}
	}

	public void enableDaveMode() {
		// TODO Auto-generated method stub
		
	}
	
}
