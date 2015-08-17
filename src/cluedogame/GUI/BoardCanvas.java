package cluedogame.GUI;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import cluedogame.sqaures.RoomSquare;
import cluedogame.sqaures.ShortcutSquare;
import cluedogame.sqaures.Square;

/**
 * A canvas which shows the Cluedo playing board.
 * @author Sarah Dobie and Christ Read
 *
 */
public class BoardCanvas extends JPanel implements MouseListener {
	
	private Image boardImage;
	private Image resizedImage;
	private CluedoFrame frame;
	
	/**
	 * Constructor for class BoardCanvas.
	 * @param frame The CluedoFrame containing this canvas.
	 */
	public BoardCanvas(CluedoFrame frame){
		addMouseListener(this);
		this.frame = frame;
		try {
			boardImage = ImageIO.read(new File("board.jpg"));
			resizedImage = boardImage.getScaledInstance(CluedoFrame.BOARD_CANVAS_WIDTH,
					CluedoFrame.BOARD_CANVAS_HEIGHT, Image.SCALE_SMOOTH);
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
				frame.showDialog("Roll the dice first!", "Invalid move");
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
					currentPlayer.moveTo(sq);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					frame.repaint();
				}
				game.useMoves(shortestPath.size());
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
	
}
