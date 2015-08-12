package cluedogame.GUI;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BoardCanvas extends JPanel {
	
	private Image boardImage;
	
	public BoardCanvas(){
		try {
			boardImage = ImageIO.read(new File("board.jpg"));
		} catch (IOException e) {
			System.out.println("Could not read image file");
		}
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	@Override
	public void paint(Graphics g){
		Image resizedImage = boardImage.getScaledInstance(CluedoFrame.BOARD_CANVAS_WIDTH,
				CluedoFrame.BOARD_CANVAS_HEIGHT, Image.SCALE_SMOOTH);
		g.drawImage(resizedImage, 0, 0, null);
	}
	
}
