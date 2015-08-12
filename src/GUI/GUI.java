
package GUI;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Sarah
 */
public class GUI extends JFrame {

    private BoardCanvas boardCanvas;
    private DashboardCanvas dashboardCanvas;
    private JButton suggestBtn;
    private JButton accuseBtn;
    private JMenu menuFile;
    private JMenuBar menuBar;
    private JMenuItem fileNewGame;
    private JMenuItem fileExit;
    private Panel btnPanel;
    private JButton rollDiceBtn;   
    
    public GUI() {
        initialiseUI();
    }

    
    @SuppressWarnings("unchecked")                          
    private void initialiseUI() {

        initialiseFields();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initialiseButtons();

        initialisePanel();

        initialiseMenu();

        initialiseFrame();

        pack();
    }


	private void initialiseFrame() {
		GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(boardCanvas, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(dashboardCanvas, GroupLayout.PREFERRED_SIZE, 392, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(boardCanvas, GroupLayout.PREFERRED_SIZE, 212, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(dashboardCanvas, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPanel, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)))
        );
	}


	private void initialiseMenu() {
		menuFile.setText("File");

        fileNewGame.setText("New Game");
        fileNewGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                newGameActionPerformed(evt);
            }
        });
        menuFile.add(fileNewGame);

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


	private void initialisePanel() {
		GroupLayout panel1Layout = new GroupLayout(btnPanel);
        btnPanel.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(accuseBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(rollDiceBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(suggestBtn, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addComponent(rollDiceBtn)
//                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(accuseBtn)
//                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(suggestBtn)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
	}


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

    private void newGameActionPerformed(ActionEvent evt) {                                           
        // TODO add your handling code here:
    }                                          

    private void exitActionPerformed(ActionEvent evt) {                                           
        // TODO add your handling code here:
    }                                          

    private void rollDiceBtnActionPerformed(ActionEvent evt) {                                         
        // TODO add your handling code here:
    }                                           

    private void suggestBtnActionPerformed(ActionEvent evt) {                                         
        // TODO add your handling code here:
    }                                        

    private void accuseBtnActionPerformed(ActionEvent evt) {                                         
        // TODO add your handling code here:
    }                                     

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }       
}
