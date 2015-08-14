package cluedogame.GUI;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import cluedogame.Player;

public class BoardCanvas extends JPanel {
	
	private Image boardImage;
	private Image resizedImage;
	private CluedoFrame frame;
	
	public BoardCanvas(CluedoFrame frame){
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
	
}
