package cluedogame.GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import cluedogame.GameOfCluedo;
import cluedogame.Player;
import cluedogame.cards.Card;

/**
 * A canvas drawn on the left side of the window, displaying the current player's
 * hand, and the number of moves they have left.
 * @author Sarah Dobie and Chris Read
 *
 */
public class DashboardCanvas extends JPanel implements MouseListener {
	
	private Image DashBoardImage;
	private Image resizedImage;
	private Image numbers[];
	private CluedoFrame frame;
	private GameOfCluedo game;
	
	public DashboardCanvas(CluedoFrame frame){
		addMouseListener(this);
		this.frame = frame;
		this.game = frame.getGame();
		try {
			numbers = new Image[14];
			for (int i = 0; i<=13; i++){
				numbers[i] = ImageIO.read(new File("Images"+File.separator+"Numbers"+File.separator+i+".png"));
			}
			DashBoardImage = ImageIO.read(new File("Images"+File.separator+"DashBoard.png"));
			
		} catch (IOException e) {
			System.out.println("Could not read image file: "+e.getMessage());
		}
		repaint();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	@Override
	public void paint(Graphics g){
		int pixel = frame.getPixelSize();
		resizedImage = DashBoardImage.getScaledInstance(frame.DASH_CANVAS_WIDTH,
				frame.DASH_CANVAS_HEIGHT, Image.SCALE_FAST);
		g.drawImage(resizedImage, 0, 0, null);
		if (!game.isReady()){
			return;
		}
		Player p = game.getCurrentPlayer();
		if (p == null){return;}
		g.setColor(Color.WHITE);
		g.drawImage(p.getNameImage(pixel), 0, 0, null);
		int roll = game.getRoll();
		if (roll > 12){
			g.drawImage(numbers[13].getScaledInstance(9*pixel, 6*pixel, Image.SCALE_FAST), 3*pixel, frame.BOARD_CANVAS_HEIGHT-9*pixel, null);
		} else {
			g.drawImage(numbers[roll].getScaledInstance(9*pixel, 6*pixel, Image.SCALE_FAST), 3*pixel, frame.BOARD_CANVAS_HEIGHT-9*pixel, null);
		}
		
		Image card;
		int x = 7*pixel;
		int y = 14*pixel;
		int column = 0;
		int row = 0;
		for (Card c : p.getHand()){
			card = c.getImage().getScaledInstance(12*pixel,16*pixel, Image.SCALE_FAST);
			g.drawImage(card, x+(column*14*pixel), y+(row*18*pixel), null);
			column++;
			if (column > 1){column = 0; row++;}
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent e) {
		int pixel = frame.getPixelSize();
		int x = e.getX();
		int y = e.getY();
		if (x>pixel*13 && x<pixel*27){
			if (y>pixel*106 && y<pixel*113){
				game.toggleCardsSeen();
			}
		}
	}
}
