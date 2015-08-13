
package cluedogame.GUI;

import javax.swing.*;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import cluedogame.Board;
import cluedogame.GameOfCluedo;
import cluedogame.Player;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * Window containing all GUI components.
 * 
 * @author Sarah Dobie, Chris Read
 */
public class CluedoFrame extends JFrame implements MouseListener {
	
	public static int PREF_BUTTON_SIZE = GroupLayout.DEFAULT_SIZE;
	public static int MAX_BUTTON_SIZE = Short.MAX_VALUE;
	public static int BOARD_CANVAS_WIDTH = 500;
	public static int BOARD_CANVAS_HEIGHT = 500;
	public static int DASH_CANVAS_WIDTH = BOARD_CANVAS_WIDTH - PREF_BUTTON_SIZE;
	public static int DASH_CANVAS_HEIGHT = 100;

	// GUI components
    private BoardCanvas boardCanvas; // the canvas which displays the playing board
    private DashboardCanvas dashboardCanvas; // the canvas which displays the cards and dice
    private Panel btnPanel; // panel containing the buttons
    private JButton rollDiceBtn;
    private JButton suggestBtn;
    private JButton accuseBtn;
    private JMenuBar menuBar; // top menu bar
    private JMenu menuFile; // 'File' button in menu bar
    private JMenuItem fileNewGame;
    private JMenuItem fileExit;
    
    // Game info
    private GameOfCluedo game;
    
    public List<Player> getPlayers(){
    	return game.getPlayers();
    }
    
    /**
     * Constructor for class CluedoFrame
     */
    public CluedoFrame() {
        initialiseUI();
        this.game = new GameOfCluedo();
        selectPlayers();
    }

    /**
     * Initialises all GUI components.
     */
    private void initialiseUI() {
    	setVisible(true);
    	setResizable(false);
        initialiseFields();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initialiseButtons();
        initialisePanel();
        initialiseMenu();
        initialiseFrame();
        pack();
    }

    /**
	 * Initialises all fields (components of the GUI)
	 */
	private void initialiseFields() {
		boardCanvas = new BoardCanvas(this);
	    dashboardCanvas = new DashboardCanvas();
	    btnPanel = new Panel();
	    rollDiceBtn = new JButton();
	    suggestBtn = new JButton();
	    accuseBtn = new JButton();
	    menuBar = new JMenuBar();
	    menuFile = new JMenu();
	    fileNewGame = new JMenuItem();
	    fileExit = new JMenuItem();
	}

	/**
	 * Sets up the buttons that will be on the button panel
	 */
	private void initialiseButtons() {
		rollDiceBtn.setText("Roll Dice");
	    rollDiceBtn.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	            rollDiceBtnActionPerformed(evt);
	        }
	    });
	
	    suggestBtn.setText("Suggest");
	    suggestBtn.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	        	suggestBtnActionPerformed(evt);
	        }
	    });
	
	    accuseBtn.setText("Accuse");
	    suggestBtn.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	        	accuseBtnActionPerformed(evt);
	        }
	    });
	}

	/**
	 * Sets up the button panel
	 */
	private void initialisePanel() {
		GroupLayout panelLayout = new GroupLayout(btnPanel);
        btnPanel.setLayout(panelLayout);
        
        // Set up the horizontal alignment
        ParallelGroup hzGroup = panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
        hzGroup.addComponent(accuseBtn, PREF_BUTTON_SIZE, PREF_BUTTON_SIZE, MAX_BUTTON_SIZE);
        hzGroup.addComponent(rollDiceBtn, PREF_BUTTON_SIZE, PREF_BUTTON_SIZE, MAX_BUTTON_SIZE);
        hzGroup.addComponent(suggestBtn, /*GroupLayout.Alignment.TRAILING,*/ PREF_BUTTON_SIZE, PREF_BUTTON_SIZE, MAX_BUTTON_SIZE);
        panelLayout.setHorizontalGroup(hzGroup);
        
        // set up the vertical alignment
        ParallelGroup vtGroup = panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
        SequentialGroup sqGroup = panelLayout.createSequentialGroup();
        vtGroup.addGroup(sqGroup);
        sqGroup.addComponent(rollDiceBtn);
        sqGroup.addComponent(accuseBtn);
        sqGroup.addComponent(suggestBtn);
//        sqGroup.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        panelLayout.setVerticalGroup(vtGroup);
	}

	/**
	 * Sets up the menu bar
	 */
	private void initialiseMenu() {
		menuFile.setText("File");
	
		// set up 'New Game' option
	    fileNewGame.setText("New Game");
	    fileNewGame.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	            newGameActionPerformed(evt);
	        }
	    });
	    menuFile.add(fileNewGame);
	
	    // set up 'Exit' option
	    fileExit.setText("Exit");
	    fileExit.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	            exitActionPerformed(evt);
	        }
	    });
	    menuFile.add(fileExit);
	    
	    
	    menuBar.add(menuFile);
	    setJMenuBar(menuBar);
	}

	/**
     * Adds all components to the frame.
     */
	private void initialiseFrame() {
		GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        // set up horizontal alignment
        ParallelGroup hzGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        hzGroup.addComponent(boardCanvas, BOARD_CANVAS_WIDTH, BOARD_CANVAS_WIDTH, BOARD_CANVAS_WIDTH);
        SequentialGroup sqGroupHz = layout.createSequentialGroup();
        hzGroup.addGroup(sqGroupHz);
        sqGroupHz.addComponent(dashboardCanvas, DASH_CANVAS_WIDTH, DASH_CANVAS_WIDTH, DASH_CANVAS_WIDTH);
//        sqGroupHz.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        sqGroupHz.addComponent(btnPanel, PREF_BUTTON_SIZE, PREF_BUTTON_SIZE, PREF_BUTTON_SIZE);
        layout.setHorizontalGroup(hzGroup);
        
        // set up vertical alignment
        ParallelGroup vtGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        SequentialGroup sqGroupVt = layout.createSequentialGroup();
        vtGroup.addGroup(sqGroupVt);
        sqGroupVt.addComponent(boardCanvas, BOARD_CANVAS_HEIGHT, BOARD_CANVAS_HEIGHT, BOARD_CANVAS_HEIGHT);
//        sqGroupVt.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        ParallelGroup dashboardGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        sqGroupVt.addGroup(dashboardGroup);
        dashboardGroup.addComponent(dashboardCanvas, DASH_CANVAS_HEIGHT, DASH_CANVAS_HEIGHT, DASH_CANVAS_HEIGHT);
        dashboardGroup.addComponent(btnPanel, DASH_CANVAS_HEIGHT, DASH_CANVAS_HEIGHT, DASH_CANVAS_HEIGHT);
        layout.setVerticalGroup(vtGroup);
	}

	/**
	 * Runs when the New Game button is pushed.
	 * @param evt
	 */
	private void newGameActionPerformed(ActionEvent evt) {                                           
        // TODO add your handling code here:
    }                                          

	/**
	 * Runs when the Exit button is pushed.
	 * @param evt
	 */
    private void exitActionPerformed(ActionEvent evt) {                                           
    	confirmExit();
    }                    
    
    /**
     * Checks if the user wants to exit, and closes the window if they do.
     */
    private void confirmExit(){
		int r = JOptionPane.showConfirmDialog(this, new JLabel("Do you really want to quit?"),
				"Exit Cluedo?", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if(r == 0){
			System.exit(0);
		}
    }

    /**
	 * Runs when the Roll Dice button is pushed.
	 * @param evt
	 */
    private void rollDiceBtnActionPerformed(ActionEvent evt) {                                         
        // TODO add your handling code here:
    }                                           

    /**
	 * Runs when the Suggest button is pushed.
	 * @param evt
	 */
    private void suggestBtnActionPerformed(ActionEvent evt) {                                         
        // TODO add your handling code here:
    }                                        

    /**
	 * Runs when the Accuse button is pushed.
	 * @param evt
	 */
    private void accuseBtnActionPerformed(ActionEvent evt) {                                         
        // TODO add your handling code here:
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) { //TODO use this for hovering
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}    
	
	/**
	 * Allows users to set up their tokens
	 */
	public void selectPlayers(){
		int numPlayers = inputNumPlayers();
		
		// let each player choose a character
		for(int i=0; i<numPlayers; i++){
			JPanel panel = new JPanel(new GridLayout(0, 1));
			
			// determine which characters are available
			List<String> playerNames = new ArrayList<String>();
			for(Player p : game.getPlayers()){
				playerNames.add(p.getName());
			}
			
			// create buttons
	        ButtonGroup bg = new ButtonGroup();
			JRadioButton greenBtn = new JRadioButton(GameOfCluedo.GREEN);
			JRadioButton mustardBtn = new JRadioButton(GameOfCluedo.MUSTARD);
			JRadioButton peacockBtn = new JRadioButton(GameOfCluedo.PEACOCK); 
			JRadioButton plumBtn = new JRadioButton(GameOfCluedo.PLUM); 
			JRadioButton scarlettBtn = new JRadioButton(GameOfCluedo.SCARLETT); 
	        JRadioButton whiteBtn = new JRadioButton(GameOfCluedo.WHITE); 
	        
	        addAvailableCharacterOptions(panel, playerNames, bg, greenBtn,
					mustardBtn, peacockBtn, plumBtn, scarlettBtn, whiteBtn);
			

			JOptionPane.showMessageDialog(this, panel);
			generatePlayerFromInput(panel, greenBtn, mustardBtn, peacockBtn,
					plumBtn, scarlettBtn, whiteBtn);
			
	        boardCanvas.repaint();
		}
	}

	/**
	 * Determines which button is selected and makes a new player
	 * based on the appropriate button
	 */
	private void generatePlayerFromInput(JPanel panel, JRadioButton greenBtn,
			JRadioButton mustardBtn, JRadioButton peacockBtn,
			JRadioButton plumBtn, JRadioButton scarlettBtn,
			JRadioButton whiteBtn) {
		if(greenBtn.isSelected()){
			game.addPlayer(new Player(GameOfCluedo.GREEN));
		} else if(mustardBtn.isSelected()){
			game.addPlayer(new Player(GameOfCluedo.MUSTARD));
		} else if(peacockBtn.isSelected()){
			game.addPlayer(new Player(GameOfCluedo.PEACOCK));
		} else if(plumBtn.isSelected()){
			game.addPlayer(new Player(GameOfCluedo.PLUM));
		} else if(scarlettBtn.isSelected()){
			game.addPlayer(new Player(GameOfCluedo.SCARLETT));
		} else if(whiteBtn.isSelected()){
			game.addPlayer(new Player(GameOfCluedo.WHITE));
		} else {
			// the player hasn't selected an option
			JOptionPane.showMessageDialog(this, panel);
			generatePlayerFromInput(panel, greenBtn, mustardBtn, peacockBtn,
					plumBtn, scarlettBtn, whiteBtn);
		}
	}

	/**
	 * Gets the number of players from the user.
	 * @return The number of players in the game.
	 * (Between 2 and 6 inclusive.)
	 */
	private int inputNumPlayers() {
		// input number of players
		String numPlayersResponse = JOptionPane.showInputDialog(this, "How many players? (2-6)");
		int numPlayers = 0;
		try{
			numPlayers = Integer.parseInt(numPlayersResponse);
			// check for out of bounds number
			while(numPlayers < 2 || numPlayers > 6){
				numPlayers = Integer.parseInt(JOptionPane.showInputDialog(this, "Invalid numer. Must be 2-6 players."));
			}
		} catch(NumberFormatException e){
			// player entered null or a non-numeric string
			return inputNumPlayers();
		}
		return numPlayers;
	}

	private void addAvailableCharacterOptions(JPanel panel,
			List<String> playerNames, ButtonGroup bg, JRadioButton greenBtn,
			JRadioButton mustardBtn, JRadioButton peacockBtn,
			JRadioButton plumBtn, JRadioButton scarlettBtn,
			JRadioButton whiteBtn) {
		
		if(!playerNames.contains(GameOfCluedo.GREEN)){ 
			bg.add(greenBtn);
			panel.add(greenBtn);
		}
		if(!playerNames.contains(GameOfCluedo.MUSTARD)){
			bg.add(mustardBtn);
			panel.add(mustardBtn);
		}
		if(!playerNames.contains(GameOfCluedo.PEACOCK)){
			bg.add(peacockBtn);
			panel.add(peacockBtn);
		}
		if(!playerNames.contains(GameOfCluedo.PLUM)){
			bg.add(plumBtn);
			panel.add(plumBtn);
		}
		if(!playerNames.contains(GameOfCluedo.SCARLETT)){
			bg.add(scarlettBtn);
			panel.add(scarlettBtn);
		}
		if(!playerNames.contains(GameOfCluedo.WHITE)){
			bg.add(whiteBtn);
			panel.add(whiteBtn);
		}
	}

    /**
     * Main method for CluedoFrame
     */
    public static void main(String args[]) {
    	new CluedoFrame();
    }   
    
    /**
     * Converts a column position to an absolute x position.
     * @param c The column position to convert
     * @return The absolute x position of the left of the column.
     */
    public static int convertColToX(int c){
    	return (int)(squareWidth()*c);
    }

    /**
     * Determines the current width of a board square
     * @return The width of a board square
     */
    public static double squareWidth() {
		double width = (double)BOARD_CANVAS_WIDTH/(double)Board.COLS;
		return width;
	}
    
    /**
     * Converts a row position to an absolute y position.
     * @param r The row position to convert
     * @return The absolute y position of the top of the row.
     */
    public static int convertRowToY(int r){
    	return (int)(squareHeight()*r);
    }

    /**
     * Determines the current height of a board square
     * @return The height of a board square
     */
    public static double squareHeight() {
		double height = (double)BOARD_CANVAS_HEIGHT/(double)Board.ROWS;
		return height;
	}
}
