
package cluedogame.GUI;

import javax.swing.*;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import cluedogame.Board;
import cluedogame.GameOfCluedo;
import cluedogame.Player;
import cluedogame.sqaures.Square;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Window containing all GUI components for Cluedo.
 * 
 * @author Sarah Dobie, Chris Read
 */
public class CluedoFrame extends JFrame {
	
	public static int PREF_BUTTON_SIZE = GroupLayout.DEFAULT_SIZE;
	public static int MAX_BUTTON_SIZE = Short.MAX_VALUE;
	public static int BOARD_CANVAS_WIDTH = 500;
	public static int BOARD_CANVAS_HEIGHT = 500;
	public static int DASH_CANVAS_WIDTH = 200;
	public static int DASH_CANVAS_HEIGHT = BOARD_CANVAS_HEIGHT;

	// GUI components
    private BoardCanvas boardCanvas; // the canvas which displays the playing board
    private DashboardCanvas dashboardCanvas; // the canvas which displays the cards and dice
    private Panel btnPanel; // panel containing the buttons
    private JButton rollDiceBtn;
    private JButton takeShortcutBtn;
    private JButton suggestBtn;
    private JButton accuseBtn;
    private JMenuBar menuBar; // top menu bar
    private JMenu menuFile; // 'File' button in menu bar
    private JMenuItem fileNewGame;
    private JMenuItem fileExit;
    
    // Game info
    private GameOfCluedo game;
    
    /**
     * Constructor for class CluedoFrame
     */
    public CluedoFrame() {
        initialiseUI();
        this.game = new GameOfCluedo(this);
        selectPlayers();
        game.dealCards();
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
        initialiseButtonPanel();
        initialiseMenu();
        initialiseFrame();
        pack();
    }

    /**
	 * Initialises all fields (components of the GUI)
	 */
	private void initialiseFields() {
		boardCanvas = new BoardCanvas(this);
	    dashboardCanvas = new DashboardCanvas(this);
	    btnPanel = new Panel();
	    rollDiceBtn = new JButton();
	    takeShortcutBtn = new JButton();
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
		// Roll Dice button
		rollDiceBtn.setText("Roll Dice");
	    rollDiceBtn.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	            rollDiceBtnActionPerformed(evt);
	        }
	    });
	    
	    // Take Shortcut button
	    takeShortcutBtn.setText("Take Shortcut");
	    takeShortcutBtn.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	        	shortcutBtnActionPerformed(evt);
	        }
	    });
	    enableShortcutBtn(false);
	
	    // Suggest button
	    suggestBtn.setText("Suggest");
	    suggestBtn.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	        	suggestBtnActionPerformed(evt);
	        }
	    });
	    enableSuggestBtn(false);
	
	    // Accuse button
	    accuseBtn.setText("Accuse");
	    accuseBtn.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	        	accuseBtnActionPerformed(evt);
	        }
	    });
	    enableAccuseBtn(false);
	}

	/**
	 * Sets up the button panel
	 */
	private void initialiseButtonPanel() {
		GroupLayout panelLayout = new GroupLayout(btnPanel);
        btnPanel.setLayout(panelLayout);
        
        // Set up the horizontal alignment
        ParallelGroup hzGroup = panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
        SequentialGroup hzSqGroup = panelLayout.createSequentialGroup();
        hzSqGroup.addComponent(rollDiceBtn, PREF_BUTTON_SIZE, PREF_BUTTON_SIZE, MAX_BUTTON_SIZE);
        hzSqGroup.addComponent(takeShortcutBtn, PREF_BUTTON_SIZE, PREF_BUTTON_SIZE, MAX_BUTTON_SIZE);
        hzSqGroup.addComponent(suggestBtn, PREF_BUTTON_SIZE, PREF_BUTTON_SIZE, MAX_BUTTON_SIZE);
        hzSqGroup.addComponent(accuseBtn, PREF_BUTTON_SIZE, PREF_BUTTON_SIZE, MAX_BUTTON_SIZE);

        hzGroup.addGroup(hzSqGroup);
        panelLayout.setHorizontalGroup(hzGroup);
        
        // set up the vertical alignment
        ParallelGroup vtGroup = panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
//        SequentialGroup sqGroup = panelLayout.createSequentialGroup();
//        vtGroup.addGroup(sqGroup);
        vtGroup.addComponent(rollDiceBtn);
        vtGroup.addComponent(takeShortcutBtn);
        vtGroup.addComponent(accuseBtn);
        vtGroup.addComponent(suggestBtn);
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
	    
	    // add File to menu bar
	    menuBar.add(menuFile);
	    setJMenuBar(menuBar);
	}

	/**
     * Adds all GUI components to the frame.
     */
	private void initialiseFrame() {
		GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        // set up horizontal alignment
        ParallelGroup hzGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        SequentialGroup sqGroupHz = layout.createSequentialGroup();
        
        sqGroupHz.addComponent(dashboardCanvas, DASH_CANVAS_WIDTH, DASH_CANVAS_WIDTH, DASH_CANVAS_WIDTH);
        sqGroupHz.addComponent(boardCanvas, BOARD_CANVAS_WIDTH, BOARD_CANVAS_WIDTH, BOARD_CANVAS_WIDTH);
        hzGroup.addGroup(sqGroupHz);
        
//        sqGroupHz.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        hzGroup.addComponent(btnPanel, PREF_BUTTON_SIZE, PREF_BUTTON_SIZE, PREF_BUTTON_SIZE);
        layout.setHorizontalGroup(hzGroup);
        
        // set up vertical alignment
//        ParallelGroup vtGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        SequentialGroup sqGroupVt = layout.createSequentialGroup();
//        vtGroup.addGroup(sqGroupVt);
        
        ParallelGroup dashboardGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        dashboardGroup.addComponent(dashboardCanvas, DASH_CANVAS_HEIGHT, DASH_CANVAS_HEIGHT, DASH_CANVAS_HEIGHT);
        sqGroupVt.addComponent(boardCanvas, BOARD_CANVAS_HEIGHT, BOARD_CANVAS_HEIGHT, BOARD_CANVAS_HEIGHT);
        sqGroupVt.addComponent(btnPanel, PREF_BUTTON_SIZE, PREF_BUTTON_SIZE, MAX_BUTTON_SIZE);
        dashboardGroup.addGroup(sqGroupVt);
        
//        sqGroupVt.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        layout.setVerticalGroup(dashboardGroup);
	}

	/**
	 * Runs when the New Game button is pushed.
	 * @param evt
	 */
	private void newGameActionPerformed(ActionEvent evt) {                                           
        confirmNewGame();
    }    
	
	/**
     * Checks if the user wants to start a new game, and restarts the game if they do.
     */
    private void confirmNewGame(){
		int r = JOptionPane.showConfirmDialog(this, new JLabel("Do you really want to abandon this game?"),
				"Start New Game?", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if(r == 0){
	        initialiseFields();
	        initialiseButtons();
	        initialiseButtonPanel();
	        initialiseMenu();
	        initialiseFrame();
	        pack();
	        this.game = new GameOfCluedo(this);
	        selectPlayers();
		}
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
    	enableAccuseBtn(true);
        game.playTurn(this);
    }                                            

    /**
	 * Runs when the Take Shortcut button is pushed.
	 * @param evt
	 */
    private void shortcutBtnActionPerformed(ActionEvent evt) {   
        game.takeShortcut();
    }                                               

    /**
	 * Runs when the Suggest button is pushed.
	 * @param evt
	 */
    private void suggestBtnActionPerformed(ActionEvent evt) {                                         
        game.makeSuggestion();
    }                                        

    /**
	 * Runs when the Accuse button is pushed.
	 * @param evt
	 */
    private void accuseBtnActionPerformed(ActionEvent evt) {                                         
        game.makeAccusation();
    }         
	
	/**
	 * Allows users to set up their characters
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
			
			JOptionPane.showMessageDialog(this, panel, "Character select", JOptionPane.QUESTION_MESSAGE);
			generatePlayerFromInput(panel, greenBtn, mustardBtn, peacockBtn,
					plumBtn, scarlettBtn, whiteBtn);
			
	        boardCanvas.repaint();
		}
	}

	/**
		 * Gets the number of players from the user.
		 * @return The number of players in the game. (Between 2 and 6 inclusive.)
		 */
		private int inputNumPlayers() {
			// set up dialog box
			String[] options = {"OK"};
			JPanel panel = new JPanel(new GridLayout(0, 1));
			JLabel lbl = new JLabel("How many players? (2-6)");
			JTextField txt = new JTextField(10);
			panel.add(lbl);
			panel.add(txt);
			
			// show dialog box
			JOptionPane.showOptionDialog(this, panel, "# Players",
					JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options , options[0]);
			String numPlayersResponse = txt.getText();
	//		String numPlayersResponse = JOptionPane.showInputDialog(this, "How many players? (2-6)");
			
			// determine input validity
			int numPlayers = 0;
			try{
				numPlayers = Integer.parseInt(numPlayersResponse);
				// check for out of bounds number
				while(numPlayers < 2 || numPlayers > 6){
	//				numPlayers = Integer.parseInt(JOptionPane.showInputDialog(this, "Invalid number. Must be 2-6 players."));
					panel.remove(lbl);
					txt.setText("");
					lbl = new JLabel("Invalid number. Must be 2-6 players.");
					panel.add(lbl);
					JOptionPane.showOptionDialog(this, panel, "# Players",
							JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options , options[0]);
					numPlayersResponse = txt.getText();
					numPlayers = Integer.parseInt(numPlayersResponse);
				}
			} catch(NumberFormatException e){
				// player entered nothing or a non-numeric string
				return inputNumPlayers();
			}
			return numPlayers;
		}

	/**
	 * Adds the remaining available character options to the character select
	 * panel.
	 */
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
	 * Determines which button is selected and makes a new player
	 * based on the appropriate button.
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
     * Allows the player to select a character and room for a suggestion.
     * @param room The room the player is in.
     * @return A String array of length 2, where the item at index 0
     * is the suggested character, and the item at index 1 is the 
     * suggested weapon.
     */
    public String[] showSuggestionDialog(String room){
    	enableSuggestBtn(false);
    	game.endTurn();
    	String characterSuggestion = showCharacterSuggestions(room);
    	String weaponSuggestion = showWeaponSuggestions(room);
    	return new String[]{characterSuggestion, weaponSuggestion};
    }

    /**
     * Shows a dialog box with radio buttons to allow the user to select
     * a weapon for the purpose of making a suggestion.
     * @param room The room the player is currently in
     * @return The name of the selected weapon
     */
	private String showWeaponSuggestions(String room) {
		ButtonGroup weaponButtons = new ButtonGroup();
		List<JRadioButton> btns = setupWeaponButtons();
        
        JPanel weaponPanel = new JPanel(new GridLayout(0, 1));
		JLabel lbl = new JLabel("You are in the "+room+"."
				+ "\n Select the weapon that you suspect.");
		weaponPanel.add(lbl);
        
        for(JRadioButton b : btns){
        	weaponButtons.add(b);
        	weaponPanel.add(b);
        }
		
        JOptionPane.showMessageDialog(this, weaponPanel);
        
//        ButtonModel model = weaponButtons.getSelection();
//        return model.getActionCommand();
        for(JRadioButton b : btns){
        	if(b.isSelected()){
        		return b.getText();
        	}
        }
        return null;
	}

	/**
     * Shows a dialog box with radio buttons to allow the user to select
     * a character for the purpose of making a suggestion.
     * @param room The room the player is currently in
     * @return The name of the selected character
     */
	private String showCharacterSuggestions(String room) {
		ButtonGroup characterButtons = new ButtonGroup();
		List<JRadioButton> btns = setupCharacterButtons();
		
        // set up dialog box
		JPanel characterPanel = new JPanel(new GridLayout(0, 1));
		JLabel lbl = new JLabel("You are in the "+room+"."
				+ "\n Select the character that you suspect.");
		characterPanel.add(lbl);
		
        // add the buttons to the panel
        for(JRadioButton b : btns){
        	characterButtons.add(b);
        	characterPanel.add(b);
        }
        
		// show dialog
        JOptionPane.showMessageDialog(this, characterPanel);
        // decide which button has been selected
        for(JRadioButton b : btns){
        	if(b.isSelected()){
        		return b.getText();
        	}
        }
        return null;
	}

	/**
     * Allows the player to select a character, weapon and room for an accusation.
     * @param room The room the player is in.
     * @return A String array of length 3, where the item at index 0
     * is the selected character, the item at index 1 is the 
     * selected weapon, and the item at index 2 is the selected room.
     */
	public String[] showAccusationDialog() {
		enableAccuseBtn(false);
		return new String[]{showCharacterAccusations(),
				showWeaponAccusations(),showRoomAccusations()};
	}
	
	/**
     * Shows a dialog box with radio buttons to allow the user to select
     * a weapon for the purpose of making an accusation.
     * @return The name of the selected weapon
     */
	private String showWeaponAccusations() {
		ButtonGroup weaponButtons = new ButtonGroup();
		List<JRadioButton> btns = setupWeaponButtons();
        
		// set up the dialog box
        JPanel weaponPanel = new JPanel(new GridLayout(0, 1));
		JLabel lbl = new JLabel("Select the weapon that you suspect.");
		weaponPanel.add(lbl);
        // add the buttons to the panel
        for(JRadioButton b : btns){
        	weaponButtons.add(b);
        	weaponPanel.add(b);
        }
        
		// display the dialog box
        JOptionPane.showMessageDialog(this, weaponPanel);
        // decide which button was selected
        for(JRadioButton b : btns){
        	if(b.isSelected()){
        		return b.getText();
        	}
        }
        return null;
	}
	
	/**
     * Shows a dialog box with radio buttons to allow the user to select
     * a character for the purpose of making an accusation.
     * @return The name of the selected character
     */
	private String showCharacterAccusations() {
		ButtonGroup characterButtons = new ButtonGroup();
		List<JRadioButton> btns = setupCharacterButtons();
        
		// set up dialog box
		JPanel characterPanel = new JPanel(new GridLayout(0, 1));
		JLabel lbl = new JLabel("Select the character that you suspect.");
		characterPanel.add(lbl);
        // add buttons to panel
        for(JRadioButton b : btns){
        	characterButtons.add(b);
        	characterPanel.add(b);
        }
		
        // show dialog box
        JOptionPane.showMessageDialog(this, characterPanel);
        // decide which button was selected
        for(JRadioButton b : btns){
        	if(b.isSelected()){
        		return b.getText();
        	}
        }
        return null;
	}
	
	/**
     * Shows a dialog box with radio buttons to allow the user to select
     * a room for the purpose of making an accusation.
     * @return The name of the selected room
     */
	private String showRoomAccusations() {
		ButtonGroup characterButtons = new ButtonGroup();
		List<JRadioButton> btns = setupRoomButtons();
        
		// set up the dialog box
		JPanel roomPanel = new JPanel(new GridLayout(0, 1));
		JLabel lbl = new JLabel("Select the room that you suspect.");
		roomPanel.add(lbl);
        // add the buttons to the panel
        for(JRadioButton b : btns){
        	characterButtons.add(b);
        	roomPanel.add(b);
        }
		
        // display dialog box
        JOptionPane.showMessageDialog(this, roomPanel);
        // decide which button was selected
        for(JRadioButton b : btns){
        	if(b.isSelected()){
        		return b.getText();
        	}
        }
        return null;
	}
	
	/**
	 * Creates a list of radio buttons, each of which is labeled with one of
	 * the six possible characters. The first button is selected.
	 * @return A List of radio buttons representing each character.
	 */
	private List<JRadioButton> setupCharacterButtons() {
		List<JRadioButton> btns = new ArrayList<JRadioButton>();
		// select the first button
		JRadioButton greenBtn = new JRadioButton(GameOfCluedo.GREEN);
		greenBtn.setSelected(true);
		// add buttons to list
		btns.add(greenBtn);
		btns.add(new JRadioButton(GameOfCluedo.MUSTARD));
		btns.add(new JRadioButton(GameOfCluedo.PEACOCK)); 
		btns.add(new JRadioButton(GameOfCluedo.PLUM)); 
		btns.add(new JRadioButton(GameOfCluedo.SCARLETT)); 
		btns.add(new JRadioButton(GameOfCluedo.WHITE)); 
		return btns;
	}

	/**
	 * Creates a list of radio buttons, each of which is labeled with one of
	 * the six possible weapons. The first button is selected.
	 * @return A List of radio buttons representing each weapon.
	 */
	private List<JRadioButton> setupWeaponButtons() {
	    List<JRadioButton> btns = new ArrayList<JRadioButton>();
	    // select top button
	    JRadioButton candlestickBtn = new JRadioButton(GameOfCluedo.CANDLESTICK);
	    candlestickBtn.setSelected(true);
	    // add buttons to list
		btns.add(candlestickBtn);
		btns.add(new JRadioButton(GameOfCluedo.DAGGER));
		btns.add(new JRadioButton(GameOfCluedo.LEAD_PIPE)); 
		btns.add(new JRadioButton(GameOfCluedo.REVOLVER)); 
		btns.add(new JRadioButton(GameOfCluedo.ROPE)); 
		btns.add(new JRadioButton(GameOfCluedo.SPANNER));
		return btns;
	}

	/**
	 * Creates a list of radio buttons, each of which is labeled with one of
	 * the nine possible rooms. The first button is selected.
	 * @return A List of radio buttons representing each character.
	 */
	private List<JRadioButton> setupRoomButtons() {
        List<JRadioButton> btns = new ArrayList<JRadioButton>();
        // select the top button
		JRadioButton kitchenBtn = new JRadioButton(GameOfCluedo.KITCHEN);
        kitchenBtn.setSelected(true);
        // add buttons to list
        btns.add(kitchenBtn);
        btns.add(new JRadioButton(GameOfCluedo.BALL_ROOM));
        btns.add(new JRadioButton(GameOfCluedo.CONSERVATORY)); 
        btns.add(new JRadioButton(GameOfCluedo.BILLIARD_ROOM)); 
        btns.add(new JRadioButton(GameOfCluedo.LIBRARY)); 
        btns.add(new JRadioButton(GameOfCluedo.STUDY)); 
        btns.add(new JRadioButton(GameOfCluedo.HALL)); 
        btns.add(new JRadioButton(GameOfCluedo.LOUNGE)); 
        btns.add(new JRadioButton(GameOfCluedo.DINING_ROOM)); 
		return btns;
	}

	/**
	 * Shows a basic dialog window with an OK button.
	 * @param message The text to be displayed in the dialog
	 * @param title The title of the dialog box
	 */
	public void showDialog(String message, String title){
		JOptionPane.showConfirmDialog(this, new JLabel(message),
				title, JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Enable or disable the Suggest button.
	 * When a button is disabled, it appears greyed out and cannot
	 * be pressed by the user.
	 * @param canSuggest True to enable the button, false to disable.
	 */
	public void enableSuggestBtn(boolean canSuggest) {
		suggestBtn.setEnabled(canSuggest);
	}

	/**
	 * Enable or disable the Accuse button.
	 * When a button is disabled, it appears greyed out and cannot
	 * be pressed by the user.
	 * @param canAccuse True to enable the button, false to disable.
	 */
	public void enableAccuseBtn(boolean canAccuse) {
		accuseBtn.setEnabled(canAccuse);
	}

	/**
	 * Enable or disable the Take Shortcut button.
	 * When a button is disabled, it appears greyed out and cannot
	 * be pressed by the user.
	 * @param canTakeShortcut True to enable the button, false to disable.
	 */
	public void enableShortcutBtn(boolean canTakeShortcut) {
		takeShortcutBtn.setEnabled(canTakeShortcut);
	}

	/**
	 * Enable or disable the Roll Dice button.
	 * When a button is disabled, it appears greyed out and cannot
	 * be pressed by the user.
	 * @param canRoll True to enable the button, false to disable.
	 */
	public void enableDiceBtn(boolean canRoll) {
		rollDiceBtn.setEnabled(canRoll);
	}

	/**
	 * Returns the current GameOfCluedo being played.
	 * @return The current GameOfCluedo
	 */
	public GameOfCluedo getGame() {
		return game;
	}

	public List<Player> getPlayers(){
		return game.getPlayers();
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
     * Converts an x position to a column position.
     * @param x The x position to convert
     * @return The column containing the x position
     */
    public static int convertXToCol(int x){
    	return (int)((double)x/squareWidth());
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
     * Converts an y position to a row position.
     * @param y The y position to convert
     * @return The row containing the y position
     */
    public static int convertYToRow(int y){
    	return (int)((double)y/squareHeight());
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
