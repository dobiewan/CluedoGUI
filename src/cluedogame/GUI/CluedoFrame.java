
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
public class CluedoFrame extends JFrame implements KeyListener, MouseMotionListener {
	
	public static final int PREF_BUTTON_SIZE = GroupLayout.DEFAULT_SIZE;
	public static final int MAX_BUTTON_SIZE = Short.MAX_VALUE;
	public static final int BUTTON_HEIGHT = 49;
	public static final int MIN_BOARD_CANVAS_WIDTH = 120;
	public static final int MIN_BOARD_CANVAS_HEIGHT = 125;
	public static final int MIN_DASH_CANVAS_WIDTH = 40;
	public static final int MIN_DASH_CANVAS_HEIGHT = MIN_BOARD_CANVAS_HEIGHT;
	public int BOARD_CANVAS_WIDTH = 600;
	public int BOARD_CANVAS_HEIGHT = 625;
	public int DASH_CANVAS_WIDTH = 200;
	public int DASH_CANVAS_HEIGHT = BOARD_CANVAS_HEIGHT;

	// GUI components
    private BoardCanvas boardCanvas; // the canvas which displays the playing board
    private DashboardCanvas dashboardCanvas; // the canvas which displays the cards and dice
    private Panel btnPanel; // panel containing the buttons
    private JButton nextTurnBtn;
    private JButton takeShortcutBtn;
    private JButton suggestBtn;
    private JButton accuseBtn;
    private JMenuBar menuBar; // top menu bar
    private JMenu menuFile; // 'File' button in menu bar
    private JMenuItem fileNewGame;
    private JMenuItem fileExit;
    private JMenu menuGame;
    private JMenuItem gameNextTurn;
    private JMenuItem gameTakeShortcut;
    private JMenuItem gameSuggest;
    private JMenuItem gameAccuse;
    private JMenuItem gameDaveMode;
    
	private int pixelSize = 5; // size of the pixels of the art
    
    // Game info
    private GameOfCluedo game;
    private DialogHandler dialogHandler;
    
    // buttons pressed
    private List<Integer> keysPressed;
    
    /**
     * Constructor for class CluedoFrame
     */
    public CluedoFrame() {
    	this.addComponentListener(new CompAdapter(this));
    	addMouseMotionListener(this);
    	this.game = new GameOfCluedo(this);
    	this.dialogHandler = new DialogHandler(this);
        initialiseUI();
        dialogHandler.selectPlayers();
        game.dealCards();
        game.setReady(true);
    	keysPressed = new ArrayList<Integer>();
    	addKeyListeners();
    	requestFocusInWindow();
    }
    
    ////////////////////////////////////////////////////////
    //                 GUI SETUP METHODS                  //
    ////////////////////////////////////////////////////////

    /**
     * Adds this as a KeyListener to all relevant components.
     */
	private void addKeyListeners() {
		addKeyListener(this);
    	boardCanvas.addKeyListener(this);
    	dashboardCanvas.addKeyListener(this);
    	btnPanel.addKeyListener(this);
    	nextTurnBtn.addKeyListener(this);
    	takeShortcutBtn.addKeyListener(this);
    	suggestBtn.addKeyListener(this);
    	accuseBtn.addKeyListener(this);
    	menuBar.addKeyListener(this);
    	menuFile.addKeyListener(this);
    	fileNewGame.addKeyListener(this);
    	gameDaveMode.addKeyListener(this);
    	fileExit.addKeyListener(this);
	}

    /**
     * Initialises all GUI components.
     */
    private void initialiseUI() {
    	setVisible(true);
    	setResizable(true);
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
	    nextTurnBtn = new JButton();
	    takeShortcutBtn = new JButton();
	    suggestBtn = new JButton();
	    accuseBtn = new JButton();
	    menuBar = new JMenuBar();
	    menuFile = new JMenu();
	    fileNewGame = new JMenuItem();
	    gameDaveMode = new JMenuItem();
	    fileExit = new JMenuItem();
	    menuGame = new JMenu();
	    gameNextTurn = new JMenuItem();
	    gameTakeShortcut = new JMenuItem();
	    gameSuggest = new JMenuItem();
	    gameAccuse = new JMenuItem();
	}

	/**
	 * Sets up the buttons that will be on the button panel
	 */
	private void initialiseButtons() {
		// Roll Dice button
		nextTurnBtn.setText("Next Turn");
	    nextTurnBtn.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	            nextTurnBtnActionPerformed(evt);
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
        hzSqGroup.addComponent(nextTurnBtn, PREF_BUTTON_SIZE, PREF_BUTTON_SIZE, MAX_BUTTON_SIZE);
        hzSqGroup.addComponent(takeShortcutBtn, PREF_BUTTON_SIZE, PREF_BUTTON_SIZE, MAX_BUTTON_SIZE);
        hzSqGroup.addComponent(suggestBtn, PREF_BUTTON_SIZE, PREF_BUTTON_SIZE, MAX_BUTTON_SIZE);
        hzSqGroup.addComponent(accuseBtn, PREF_BUTTON_SIZE, PREF_BUTTON_SIZE, MAX_BUTTON_SIZE);

        hzGroup.addGroup(hzSqGroup);
        panelLayout.setHorizontalGroup(hzGroup);
        
        // set up the vertical alignment
        ParallelGroup vtGroup = panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
        vtGroup.addComponent(nextTurnBtn);
        vtGroup.addComponent(takeShortcutBtn);
        vtGroup.addComponent(accuseBtn);
        vtGroup.addComponent(suggestBtn);
        panelLayout.setVerticalGroup(vtGroup);
	}

	/**
	 * Sets up the menu bar
	 */
	private void initialiseMenu() {
	    setJMenuBar(menuBar);
		initialiseFileMenu();
		initialiseGameMenu();
	}

	/**
	 * Sets up the file menu
	 */
	public void initialiseFileMenu() {
		menuFile.setText("File");
	
		// set up 'New Game' option
	    fileNewGame.setText("New Game (Ctrl + Shift + N)");
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
	}

	/**
	 * Sets up the Game menu.
	 */
	public void initialiseGameMenu() {
		menuGame.setText("Game");
	
		// set up 'Next Turn' option
	    gameNextTurn.setText("Next turn (Ctrl + N)");
	    gameNextTurn.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	            nextTurnBtnActionPerformed(evt);
	        }
	    });
	    menuGame.add(gameNextTurn);
	
		// set up 'Take Shortcut' option
	    gameTakeShortcut.setText("Take Shortcut (Ctrl + T)");
	    gameTakeShortcut.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	            shortcutBtnActionPerformed(evt);
	        }
	    });
	    menuGame.add(gameTakeShortcut);
	    
	    // set up 'Suggest' option
	    gameSuggest.setText("Make a suggestion (Ctrl + S)");
	    gameSuggest.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	            suggestBtnActionPerformed(evt);
	        }
	    });
	    menuGame.add(gameSuggest);
	    
	    // set up 'Accuse' option
	    gameAccuse.setText("Make an accusation (Ctrl + A)");
	    gameAccuse.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	            accuseBtnActionPerformed(evt);
	        }
	    });
	    menuGame.add(gameAccuse);
	    
	    // set up 'Dave Mode' option
	    gameDaveMode.setText("Dave Mode (Ctrl + D)");
	    gameDaveMode.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	            daveModeActionPerformed(evt);
	        }
	    });
	    menuGame.add(gameDaveMode);
	    
	    // set up 'Infinite Movement' option
	    gameDaveMode.setText("Infinite Moves (Ctrl + I)");
	    gameDaveMode.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	            infiniteMovesActionPerformed(evt);
	        }
	    });
	    menuGame.add(gameDaveMode);
	    
	    // add Game to menu bar
	    menuBar.add(menuGame);
	}

	//Infinite moves menu option
	protected void infiniteMovesActionPerformed(ActionEvent evt) {
		game.setInfiniteMovement(true);
		game.setRollToInfinite();
		repaintAll();
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
        
        hzGroup.addComponent(btnPanel, PREF_BUTTON_SIZE, PREF_BUTTON_SIZE, PREF_BUTTON_SIZE);
        layout.setHorizontalGroup(hzGroup);
        
        // set up vertical alignment
        SequentialGroup sqGroupVt = layout.createSequentialGroup();
        
        ParallelGroup dashboardGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        dashboardGroup.addComponent(dashboardCanvas, DASH_CANVAS_HEIGHT, DASH_CANVAS_HEIGHT, DASH_CANVAS_HEIGHT);
        sqGroupVt.addComponent(boardCanvas, BOARD_CANVAS_HEIGHT, BOARD_CANVAS_HEIGHT, BOARD_CANVAS_HEIGHT);
        sqGroupVt.addComponent(btnPanel, PREF_BUTTON_SIZE, PREF_BUTTON_SIZE, MAX_BUTTON_SIZE);
        dashboardGroup.addGroup(sqGroupVt);
        
        layout.setVerticalGroup(dashboardGroup);
	}
	
	
    ////////////////////////////////////////////////////////
    //              BUTTON RESPONSE METHODS               //
    ////////////////////////////////////////////////////////

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
			dispose();
			new CluedoFrame();
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
	 * Runs when the Next Turn button is pushed.
	 * @param evt
	 */
	private void nextTurnBtnActionPerformed(ActionEvent evt) { 
		enableAccuseBtn(true);
	    game.playTurn();
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
	 * Runs when the Dave Mode button is pushed.
	 * @param evt
	 */
	private void daveModeActionPerformed(ActionEvent evt) {                                           
	    boardCanvas.enableDaveMode();
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		keysPressed.add(e.getKeyCode());
		boolean foundCtrl = false;
		boolean foundShift = false;
		// see if ctrl or shift are held down
		for(int i : keysPressed){
			if(i == KeyEvent.VK_CONTROL){
				foundCtrl = true;
			} else if(i == KeyEvent.VK_SHIFT){
				foundShift = true;
			}
		}
		// ctrl + [ ] shortcuts
		if(foundCtrl){
			for(int i : keysPressed){
				if(i == KeyEvent.VK_N && foundShift){ // new game
					confirmNewGame();
					keysPressed.clear();
					break;
				} else if(i == KeyEvent.VK_D){ // dave mode
					boardCanvas.enableDaveMode();
					keysPressed.clear();
					break;
				} else if(i == KeyEvent.VK_I){ // infinite moves
					game.setInfiniteMovement(true);
					game.setRollToInfinite();
					repaintAll();
					keysPressed.clear();
					break;
				} else if(i == KeyEvent.VK_N && nextTurnBtn.isEnabled()){ // next turn
					nextTurnBtnActionPerformed(null);
					keysPressed.clear();
					break;
				} else if(i == KeyEvent.VK_T && takeShortcutBtn.isEnabled()){ // take shortcut
					shortcutBtnActionPerformed(null);
					keysPressed.clear();
					break;
				} else if(i == KeyEvent.VK_S && suggestBtn.isEnabled()){ // suggest
					suggestBtnActionPerformed(null);
					keysPressed.clear();
					break;
				} else if(i == KeyEvent.VK_A && accuseBtn.isEnabled()){ // accuse
					accuseBtnActionPerformed(null);
					keysPressed.clear();
					break;
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// remove the key from the list of keys held down
		for(int i=0; i<keysPressed.size(); i++){
			int keyCode = keysPressed.get(i);
			if(keyCode == e.getKeyCode()){
				keysPressed.remove(i);
			}
		}
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
	 * Enable or disable the Next Turn button.
	 * When a button is disabled, it appears greyed out and cannot
	 * be pressed by the user.
	 * @param canRoll True to enable the button, false to disable.
	 */
	public void enableNextTurnBtn(boolean canRoll) {
		nextTurnBtn.setEnabled(canRoll);
		gameNextTurn.setEnabled(canRoll);
	}

	/**
	 * Enable or disable the Take Shortcut button.
	 * When a button is disabled, it appears greyed out and cannot
	 * be pressed by the user.
	 * @param canTakeShortcut True to enable the button, false to disable.
	 */
	public void enableShortcutBtn(boolean canTakeShortcut) {
		takeShortcutBtn.setEnabled(canTakeShortcut);
		gameTakeShortcut.setEnabled(canTakeShortcut);
	}

	/**
	 * Enable or disable the Suggest button.
	 * When a button is disabled, it appears greyed out and cannot
	 * be pressed by the user.
	 * @param canSuggest True to enable the button, false to disable.
	 */
	public void enableSuggestBtn(boolean canSuggest) {
		suggestBtn.setEnabled(canSuggest);
		gameSuggest.setEnabled(canSuggest);
	}

	/**
	 * Enable or disable the Accuse button.
	 * When a button is disabled, it appears greyed out and cannot
	 * be pressed by the user.
	 * @param canAccuse True to enable the button, false to disable.
	 */
	public void enableAccuseBtn(boolean canAccuse) {
		accuseBtn.setEnabled(canAccuse);
		gameAccuse.setEnabled(canAccuse);
	}
	
	/**
	 * Disables all JButtons and buttons in the game menu (except Dave Move).
	 */
	public void disableAllButtons(){
		enableNextTurnBtn(false);
		enableShortcutBtn(false);
		enableSuggestBtn(false);
		enableAccuseBtn(false);
	}

	

	////////////////////////////////////////////////////////
    //                INFORMATION METHODS                 //
    ////////////////////////////////////////////////////////

	/**
	 * Returns the current GameOfCluedo being played.
	 * @return The current GameOfCluedo
	 */
	public GameOfCluedo getGame() {
		return game;
	}

	/**
	 * Gets all players in the game.
	 * @return A List of all players involved in the game
	 */
	public List<Player> getPlayers(){
		return game.getPlayers();
	}

	/**
	 * Gets the dialog handler for this frame.
	 * @return The DialogHandler associated with this frame
	 */
	public DialogHandler getDialogHandler(){
		return dialogHandler;
	}

	/**
	 * Converts a column position to an absolute x position.
	 * @param c The column position to convert
	 * @return The absolute x position of the left of the column.
	 */
	public int convertColToX(int c){
		return (int)(squareWidth()*c);
	}

	/**
	 * Converts an x position to a column position.
	 * @param x The x position to convert
	 * @return The column containing the x position
	 */
	public int convertXToCol(int x){
		return (int)((double)x/squareWidth());
	}

	/**
	 * Determines the current width of a board square
	 * @return The width of a board square
	 */
	public double squareWidth() {
		double width = (double)BOARD_CANVAS_WIDTH/(double)Board.COLS;
		return width;
	}

	/**
	 * Converts a row position to an absolute y position.
	 * @param r The row position to convert
	 * @return The absolute y position of the top of the row.
	 */
	public int convertRowToY(int r){
		return (int)(squareHeight()*r);
	}

	/**
	 * Converts an y position to a row position.
	 * @param y The y position to convert
	 * @return The row containing the y position
	 */
	public int convertYToRow(int y){
		return (int)((double)y/squareHeight());
	}

	/**
	 * Determines the current height of a board square
	 * @return The height of a board square
	 */
	public double squareHeight() {
		double height = (double)BOARD_CANVAS_HEIGHT/(double)Board.ROWS;
		return height;
	}

	/**
	 * Repaints the board and dashboard.
	 */
	public void repaintAll(){
		boardCanvas.repaint();
		dashboardCanvas.repaint();
	}

	/**
     * Main method for CluedoFrame
     */
    public static void main(String args[]) {
    	EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CluedoFrame();
            }
        });
    }

	@Override
	public void mouseDragged(MouseEvent e) {}
	
	public void resize(){
		Rectangle bounds = getBounds();
		int x = bounds.width;
		int y = bounds.height;
		// aspect ratio = 800x625 (5px)
		//(32x25) (160x125)
		double xSize = x/160;
		double ySize = y/125;
		if(xSize < 1 || ySize < 1){
			pixelSize = 1;
			return;
		}
		double minSize = Math.min(xSize, ySize);
		int newPixelSize = (int)minSize;
		pixelSize = newPixelSize;
		BOARD_CANVAS_WIDTH = 120*pixelSize;
		BOARD_CANVAS_HEIGHT = 125*pixelSize;
		DASH_CANVAS_WIDTH = 40*pixelSize;
		DASH_CANVAS_HEIGHT = BOARD_CANVAS_HEIGHT;
		Insets insets = getInsets();
		int newFrameWidth = BOARD_CANVAS_WIDTH + DASH_CANVAS_WIDTH + insets.left + insets.right;
		int newFrameHeight = (int) (BOARD_CANVAS_HEIGHT + BUTTON_HEIGHT + insets.top + insets.bottom);
		setSize(new Dimension(newFrameWidth, newFrameHeight));
		initialiseFrame();
	}

	@Override
	public void mouseMoved(MouseEvent e) {}
	
	/**
	 * Gets the pixel drawing size.
	 * @return The current pixel size
	 */
	public int getPixelSize(){
		return pixelSize;
	}
	
	/**
	 * Sets the pixel size.
	 * @param size The new pixel size
	 */
	public void setPixelSize(int size){
		pixelSize = size;
	}
}

/*
 * class for ComponentAdapter to allow resizing
 * 
 */
class CompAdapter extends ComponentAdapter {
	private CluedoFrame frame;
	
	public CompAdapter(CluedoFrame frame){
		this.frame = frame;
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
        frame.resize();
    }
}
