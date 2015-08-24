package cluedogame.GUI;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import cluedogame.GameOfCluedo;
import cluedogame.Player;

/**
 * A helper for the CluedoFrame which handles any extended use of
 * dialog boxes, especially input of players, suggestions and accusations.
 * @author Sarah Dobie, Chris Read
 *
 */
public class DialogHandler {
	
	private CluedoFrame frame;
	private GameOfCluedo game;
	
	public DialogHandler(CluedoFrame frame){
		this.frame = frame;
		this.game = frame.getGame();
	}

	////////////////////////////////////////////////////////
	//                 GAME SETUP METHODS                 //
	////////////////////////////////////////////////////////
	
	/**
	 * Allows users to set up their characters
	 */
	void selectPlayers(){
		frame.repaintAll();
		int numPlayers = inputNumPlayers();
		
		// let each player choose a character
		for(int i=0; i<numPlayers; i++){
			JPanel panel = new JPanel(new GridLayout(0, 1));
			
			// determine which characters are available
			List<String> playerNames = new ArrayList<String>();
			for(Player p : game.getPlayers()){
				playerNames.add(p.getCharacter());
			}
			
			// create buttons
	        ButtonGroup bg = new ButtonGroup();
			JRadioButton greenBtn = new JRadioButton(GameOfCluedo.GREEN);
			JRadioButton mustardBtn = new JRadioButton(GameOfCluedo.MUSTARD);
			JRadioButton peacockBtn = new JRadioButton(GameOfCluedo.PEACOCK); 
			JRadioButton plumBtn = new JRadioButton(GameOfCluedo.PLUM); 
			JRadioButton scarlettBtn = new JRadioButton(GameOfCluedo.SCARLETT); 
	        JRadioButton whiteBtn = new JRadioButton(GameOfCluedo.WHITE); 
	        
	        String playerName = getPlayerName();
	        
	        panel.add(new JLabel("Who will "+playerName+" play as?"));
	        addAvailableCharacterOptions(panel, playerNames, bg, greenBtn,
					mustardBtn, peacockBtn, plumBtn, scarlettBtn, whiteBtn);
			JOptionPane.showMessageDialog(frame, panel, "Character select", JOptionPane.QUESTION_MESSAGE);
			generatePlayerFromInput(panel, playerName, greenBtn, mustardBtn, peacockBtn,
					plumBtn, scarlettBtn, whiteBtn);
			
	        frame.repaintAll();
		}
	}

	private String getPlayerName() {
		// set up dialog box
		String[] options = {"OK"};
		JPanel namePanel = new JPanel(new GridLayout(0, 1));
		JLabel lbl = new JLabel("Next player, what is your name?");
		JTextField txt = new JTextField(10);
		namePanel.add(lbl);
		namePanel.add(txt);
		
		// show dialog box
		JOptionPane.showOptionDialog(frame, namePanel, "Player name",
				JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options , options[0]);
		String playerName = txt.getText();
		while(playerName.length() < 1){
			lbl.setText("Name must be at least one character long.");
			JOptionPane.showOptionDialog(frame, namePanel, "Player name",
					JOptionPane.NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options , options[0]);
			playerName = txt.getText();
		}
		return playerName;
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
			JOptionPane.showOptionDialog(frame, panel, "# Players",
					JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options , options[0]);
			String numPlayersResponse = txt.getText();
			
			// determine input validity
			int numPlayers = 0;
			try{
				numPlayers = Integer.parseInt(numPlayersResponse);
				// check for out of bounds number
				while(numPlayers < 2 || numPlayers > 6){
					panel.remove(lbl);
					txt.setText("");
					lbl = new JLabel("Invalid number. Must be 2-6 players.");
					panel.add(lbl);
					JOptionPane.showOptionDialog(frame, panel, "# Players",
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
	private void generatePlayerFromInput(JPanel panel, String playerName, JRadioButton greenBtn,
			JRadioButton mustardBtn, JRadioButton peacockBtn,
			JRadioButton plumBtn, JRadioButton scarlettBtn,
			JRadioButton whiteBtn) {
		if(greenBtn.isSelected()){
			game.addPlayer(new Player(GameOfCluedo.GREEN, playerName));
		} else if(mustardBtn.isSelected()){
			game.addPlayer(new Player(GameOfCluedo.MUSTARD, playerName));
		} else if(peacockBtn.isSelected()){
			game.addPlayer(new Player(GameOfCluedo.PEACOCK, playerName));
		} else if(plumBtn.isSelected()){
			game.addPlayer(new Player(GameOfCluedo.PLUM, playerName));
		} else if(scarlettBtn.isSelected()){
			game.addPlayer(new Player(GameOfCluedo.SCARLETT, playerName));
		} else if(whiteBtn.isSelected()){
			game.addPlayer(new Player(GameOfCluedo.WHITE, playerName));
		} else {
			// the player hasn't selected an option
			JOptionPane.showMessageDialog(frame, panel);
			generatePlayerFromInput(panel, playerName, greenBtn, mustardBtn, peacockBtn,
					plumBtn, scarlettBtn, whiteBtn);
		}
	}

	////////////////////////////////////////////////////////
	//              GAMEPLAY DISPLAY METHODS              //
	////////////////////////////////////////////////////////
	
	/**
	 * Allows the player to select a character and room for a suggestion.
	 * @param room The room the player is in.
	 * @return A String array of length 2, where the item at index 0
	 * is the suggested character, and the item at index 1 is the 
	 * suggested weapon.
	 */
	public String[] showSuggestionDialog(String room){
		frame.enableSuggestBtn(false);
		game.endTurn();
		String characterSuggestion = showCharacterSuggestions(room);
		String weaponSuggestion = showWeaponSuggestions(room);
		return new String[]{frame.unDave(characterSuggestion), frame.unDave(weaponSuggestion)};
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
			JLabel lbl = new JLabel("You are in the "+frame.makeDave(room)+"."
					+ "\n Select the weapon that you suspect.");
			weaponPanel.add(lbl);
	        
	        for(JRadioButton b : btns){
	        	weaponButtons.add(b);
	        	weaponPanel.add(b);
	        }
			
	        JOptionPane.showMessageDialog(frame, weaponPanel);
	        
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
	    JOptionPane.showMessageDialog(frame, characterPanel);
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
		frame.enableAccuseBtn(false);
		return new String[]{frame.unDave(showCharacterAccusations()),
				frame.unDave(showWeaponAccusations()),frame.unDave(showRoomAccusations())};
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
	    JOptionPane.showMessageDialog(frame, weaponPanel);
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
	    JOptionPane.showMessageDialog(frame, characterPanel);
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
	    JOptionPane.showMessageDialog(frame, roomPanel);
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
		JRadioButton greenBtn = new JRadioButton(frame.makeDave(GameOfCluedo.GREEN));
		greenBtn.setSelected(true);
		// add buttons to list
		btns.add(greenBtn);
		btns.add(new JRadioButton(frame.makeDave(GameOfCluedo.MUSTARD)));
		btns.add(new JRadioButton(frame.makeDave(GameOfCluedo.PEACOCK))); 
		btns.add(new JRadioButton(frame.makeDave(GameOfCluedo.PLUM))); 
		btns.add(new JRadioButton(frame.makeDave(GameOfCluedo.SCARLETT))); 
		btns.add(new JRadioButton(frame.makeDave(GameOfCluedo.WHITE))); 
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
	    JRadioButton candlestickBtn = new JRadioButton(frame.makeDave(GameOfCluedo.CANDLESTICK));
	    candlestickBtn.setSelected(true);
	    // add buttons to list
		btns.add(candlestickBtn);
		btns.add(new JRadioButton(frame.makeDave(GameOfCluedo.DAGGER)));
		btns.add(new JRadioButton(frame.makeDave(GameOfCluedo.LEAD_PIPE))); 
		btns.add(new JRadioButton(frame.makeDave(GameOfCluedo.REVOLVER))); 
		btns.add(new JRadioButton(frame.makeDave(GameOfCluedo.ROPE))); 
		btns.add(new JRadioButton(frame.makeDave(GameOfCluedo.SPANNER)));
		
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
		JRadioButton kitchenBtn = new JRadioButton(frame.makeDave(GameOfCluedo.KITCHEN));
	    kitchenBtn.setSelected(true);
	    // add buttons to list
	    btns.add(kitchenBtn);
    	btns.add(new JRadioButton(frame.makeDave(GameOfCluedo.BALL_ROOM)));
	    btns.add(new JRadioButton(frame.makeDave(GameOfCluedo.CONSERVATORY))); 
	    btns.add(new JRadioButton(frame.makeDave(GameOfCluedo.BILLIARD_ROOM))); 
	    btns.add(new JRadioButton(frame.makeDave(GameOfCluedo.LIBRARY))); 
	    btns.add(new JRadioButton(frame.makeDave(GameOfCluedo.STUDY))); 
	    btns.add(new JRadioButton(frame.makeDave(GameOfCluedo.HALL))); 
	    btns.add(new JRadioButton(frame.makeDave(GameOfCluedo.LOUNGE))); 
	    btns.add(new JRadioButton(frame.makeDave(GameOfCluedo.DINING_ROOM))); 
		return btns;
	}

}
