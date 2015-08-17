package cluedogame.GUI;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class DashboardCanvas extends JPanel {
	
	private CluedoFrame frame;
	
	public DashboardCanvas(CluedoFrame frame){
		this.frame = frame;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	@Override
	public void paint(Graphics g){
		g.setColor(Color.GREEN);
		g.fillRect(0,0,CluedoFrame.DASH_CANVAS_WIDTH,CluedoFrame.DASH_CANVAS_HEIGHT);
	}
}
