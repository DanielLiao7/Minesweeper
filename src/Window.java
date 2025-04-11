package Project.src;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class Window implements ActionListener{

    private Minesweeper ms;
    private int[][] board;

    private JFrame frame;
    private final int BOARD_WIDTH = 500;  // Width and height of the game board

    private JPanel contentPane;
    private JPanel gamePanel;
    private JPanel uiPanel;
    private final int UI_HEIGHT = 50;  // Height of the UI panel


    public Window(Minesweeper ms, int[][] board){
        this.ms = ms;
        this.board = board;

        generateFrame();

        // Make frame visible
        frame.setVisible(true);;
    }

    private void generateFrame(){
        // Setup window frame
        frame = new JFrame("Othello");
        frame.setSize(BOARD_WIDTH, BOARD_WIDTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Makes program end when window closed
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);  // Centers the Window in the screen

        // Create content pane to house UI and game panels
        contentPane =  new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));  // Adds padding between edges of window and panels
        contentPane.setLayout(new BorderLayout(0, 5));  // Adds padding between panels
        frame.setContentPane(contentPane);

        // Setup UI Panel
        uiPanel = new JPanel(null);
        uiPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        uiPanel.setPreferredSize(new Dimension(BOARD_WIDTH, UI_HEIGHT));
        contentPane.add(uiPanel, BorderLayout.PAGE_START);

        // Setup Game Board Panel
        gamePanel = new JPanel(new GridLayout(board.length, board[0].length, 0, 0));  // Grid Layout used to automatically organize buttons
        gamePanel.setBorder(BorderFactory.createLoweredBevelBorder());
        gamePanel.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_WIDTH));
        contentPane.add(gamePanel, BorderLayout.CENTER);
        
        for (int row = 0; row < board.length; row++){
            for (int col = 0; col < board[row].length; col++){
                JButton newButton = new JButton();
                newButton.setContentAreaFilled(false);
                newButton.setFocusPainted(false);
                newButton.setBorder(BorderFactory.createRaisedBevelBorder());
                newButton.setActionCommand(row + " " + col);
                newButton.addActionListener(this);
                gamePanel.add(newButton);
            }
        }

        frame.pack(); // Makes everything the desired size and removes extra space
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
    }
    
}
