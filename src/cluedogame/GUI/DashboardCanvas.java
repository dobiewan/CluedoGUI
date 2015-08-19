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

public class DashboardCanvas extends JPanel implements MouseListener {
	
	private Image DashBoardImage;
	private Image resizedImage;
	private Image numbers[];
	private CluedoFrame frame;
	private GameOfCluedo game;
	
	public DashboardCanvas(CluedoFrame frame, GameOfCluedo game){
		addMouseListener(this);
		this.frame = frame;
		this.game = game;
		try {
			numbers = new Image[14];
			for (int i = 0; i<=13; i++){
				numbers[i] = ImageIO.read(new File("Images"+File.separator+"Numbers"+File.separator+i+".png"))
						.getScaledInstance(45, 30, Image.SCALE_FAST);
			}
			DashBoardImage = ImageIO.read(new File("Images"+File.separator+"DashBoard.png"));
			resizedImage = DashBoardImage.getScaledInstance(CluedoFrame.DASH_CANVAS_WIDTH,
					CluedoFrame.DASH_CANVAS_HEIGHT, Image.SCALE_FAST);
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
		g.drawImage(resizedImage, 0, 0, null);
		if (!game.isReady()){
			return;
		}
		Player p = game.getCurrentPlayer();
		if (p == null){return;}
		g.setColor(Color.WHITE);
		g.drawImage(p.getNameImage(), 0, 0, null);
//		g.drawString(p.getCharacter() + " ("+p.getUserName()+")", 10, 20);
//		g.drawString(""+game.getRoll(), 20, 40);
		int roll = game.getRoll();
		if (roll > 12){
			g.drawImage(numbers[13], 15, CluedoFrame.BOARD_CANVAS_HEIGHT-45, null);
		} else {
			g.drawImage(numbers[roll], 15, CluedoFrame.BOARD_CANVAS_HEIGHT-45, null);
		}
		
		Image card;
		int x = 35;
		int y = 70;
		int column = 0;
		int row = 0;
		for (Card c : p.getHand()){
			card = c.getImage();
			g.drawImage(card, x+(column*70), y+(row*90), null);
			column++;
			if (column > 1){column = 0; row++;}
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
