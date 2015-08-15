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
import cluedogame.sqaures.Square;

public class BoardCanvas extends JPanel implements MouseListener {
	
	private Image boardImage;
	private Image resizedImage;
	private CluedoFrame frame;
	
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
		if(Board.validRow(row) && Board.validCol(col)){
			Board board = game.getBoard();
			// check if the current player is valid
			Player current = game.getCurrentPlayer();
			if(current == null){
				frame.showDialog("Roll the dice first!", "Invalid move");
				return;
			}
			// determine shortest path
			Square start = board.squareAt(current.row(), current.column());
			Square goal = board.squareAt(row, col);
			List<Square> shortestPath = board.shortestPath(start, goal, game.getRoll());
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
					current.moveTo(sq);
					repaint();
				}
				game.useMoves(shortestPath.size());
				// check if player is in room or not
				if(goal instanceof RoomSquare){
					frame.enableSuggestBtn(true);
				} else {
					frame.enableSuggestBtn(false);
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
