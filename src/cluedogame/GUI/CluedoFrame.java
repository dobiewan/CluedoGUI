
package cluedogame.GUI;

import javax.swing.*;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import java.awt.*;
import java.awt.event.*;

/**
 * Window containing all GUI components.
 * 
 * @author Sarah Dobie, Chris Read
 */
public class CluedoFrame extends JFrame implements MouseListener {
	
	public static int BOARD_CANVAS_WIDTH = 400;
	public static int BOARD_CANVAS_HEIGHT = 400;
	public static int DASH_CANVAS_WIDTH = 400;
	public static int DASH_CANVAS_HEIGHT = 100;
	public static int PREF_BUTTON_SIZE = GroupLayout.DEFAULT_SIZE;
	public static int MAX_BUTTON_SIZE = Short.MAX_VALUE;

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
    
    /**
     * Constructor for class CluedoFrame
     */
    public CluedoFrame() {
        initialiseUI();
    }

    /**
     * Initialises all GUI components.
     */
    @SuppressWarnings("unchecked")                          
    private void initialiseUI() {
    	setVisible(true);
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
		boardCanvas = new BoardCanvas();
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
        hzGroup.addComponent(suggestBtn, GroupLayout.Alignment.TRAILING, PREF_BUTTON_SIZE, PREF_BUTTON_SIZE, MAX_BUTTON_SIZE);
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
        // TODO add your handling code here:
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
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}    

    /**
     * Main method for CluedoFrame
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        /*try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CluedoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CluedoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CluedoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CluedoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch(Exception e){
        	
        }*/

        // Create and display the form 
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CluedoFrame()/*.setVisible(true)*/;
            }
        });
    }   
}
