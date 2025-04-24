/**
 *  Daniel Liao - 2025
 */

package Project.src;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

public class Window{

    private Minesweeper ms;

    private JFrame frame;
    private final int BOARD_WIDTH = 500;  // Width and height of the game board

    private JPanel contentPane;
    private JPanel gamePanel;
    private JPanel uiPanel;
    private final int UI_HEIGHT = 50;  // Height of the UI panel

    private Border lowerBevel;
    private Border raisedBevel;

    // Save images so they don't have to be recreated every time
    private Image flagImg;
    private Image mineImg;

    // Referenced: https://codewithcurious.com/projects/stop-watch-using-java-swing-2/
    private JLabel timeLabel;
    private Timer timer;
    private int elapsedTime = 0;  // Elapsed time in seconds

    private JLabel flagLabel;
    private int numFlags;

    private boolean gameOver = false;

    public Window(Minesweeper ms){
        this.ms = ms;

        numFlags = ms.getNumMines();

        generateFrame();

        // Make frame visible
        frame.setVisible(true);
    }

    private void generateFrame(){
        // For layouts ref: https://docs.oracle.com/javase/tutorial/uiswing/layout/visual.html
        // For borders ref: https://docs.oracle.com/javase/tutorial/uiswing/components/border.html

        // Setup window frame
        frame = new JFrame("Minesweeper");
        frame.setSize(BOARD_WIDTH, BOARD_WIDTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Makes program end when window closed
        frame.setResizable(false);
        // Ref https://www.tutorialspoint.com/how-to-display-a-jframe-to-the-center-of-a-screen-in-java
        frame.setLocationRelativeTo(null);  // Centers the Window in the screen

        // Create content pane to house UI and game panels
        contentPane =  new JPanel();
        // Ref: https://stackoverflow.com/questions/21167133/adding-vertical-spacing-to-north-component-in-borderlayout
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));  // Adds padding between edges of window and panels
        contentPane.setLayout(new BorderLayout(0, 5));  // Adds padding between panels
        frame.setContentPane(contentPane);

        // Create borders
        lowerBevel = BorderFactory.createLoweredBevelBorder();
        raisedBevel = BorderFactory.createRaisedBevelBorder();

        // Initialize Image Icons
        try{
            flagImg = ImageIO.read(new File(System.getProperty("user.dir") + "\\res\\images\\flag.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try{
            mineImg = ImageIO.read(new File(System.getProperty("user.dir") + "\\res\\images\\mine.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Change the icon of the JFrame to be the mine
        frame.setIconImage(mineImg);

        // Setup UI Panel
        uiPanel = new JPanel(new BorderLayout(0, 0));
        // Create a compound border to add padding to the bevel border
        Border padding = new EmptyBorder(5, 5, 5, 5);
        Border compound = BorderFactory.createCompoundBorder(lowerBevel, padding);
        uiPanel.setBorder(compound);
        uiPanel.setPreferredSize(new Dimension(BOARD_WIDTH, UI_HEIGHT));
        contentPane.add(uiPanel, BorderLayout.PAGE_START);

        // Setup Game Timer
        // timeLabel holds the text
        timeLabel = new JLabel("000", JLabel.CENTER);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        timeLabel.setBorder(new EmptyBorder(0, 10, 0, 10));
        timeLabel.setOpaque(true);
        timeLabel.setForeground(Color.red);
        timeLabel.setBackground(Color.black);
        uiPanel.add(timeLabel, BorderLayout.LINE_END);

        // Setup actual timer
        // 1000 ms delay to count seconds
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                elapsedTime += 1;
                updateTimeLabel();
            }
        });

        // Setup label that displays # of flags left
        flagLabel = new JLabel("" + numFlags, JLabel.CENTER);
        flagLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        flagLabel.setBorder(new EmptyBorder(0, 10, 0, 10));
        flagLabel.setOpaque(true);
        flagLabel.setForeground(Color.black);
        Image newImg = flagImg.getScaledInstance(25, 25, Image.SCALE_FAST);
        flagLabel.setIcon(new ImageIcon(newImg));
        uiPanel.add(flagLabel, BorderLayout.LINE_START);

        // Setup Game Board Panel
        gamePanel = new JPanel(new GridLayout(ms.getBoardWidth(), ms.getBoardHeight(), 0, 0));  // Grid Layout used to automatically organize buttons
        gamePanel.setBorder(lowerBevel);
        gamePanel.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_WIDTH));
        contentPane.add(gamePanel, BorderLayout.CENTER);

        for (int row = 0; row < ms.getBoardHeight(); row++){
            for (int col = 0; col < ms.getBoardWidth(); col++){
                JButton newButton = new JButton();
                newButton.setContentAreaFilled(false);
                newButton.setFocusPainted(false);
                newButton.setBorder(raisedBevel);
                newButton.setFont(new Font("Arial Black", Font.PLAIN, 15));
                newButton.setActionCommand(row + " " + col);
                newButton.setName("open");
                // Add Mouse Click detection to buttons 
                newButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (!gameOver){
                            // Check for left click press
                            if (SwingUtilities.isLeftMouseButton(e)){
                                if (newButton.getName().equals("open")){
                                    String[] split = newButton.getActionCommand().split(" ");
                                    int r = Integer.parseInt(split[0]);
                                    int c = Integer.parseInt(split[1]);
                                    // System.out.println(r + " " + c);
                                    cellLeftClick(newButton, r, c);
                                }
                                
                            }
                            // Check for right click press
                            else if (SwingUtilities.isRightMouseButton(e)){
                                if (newButton.getName().equals("open")){
                                    // Logic for placing and removing flags
                                    if (flagImg != null){
                                        Image newImg = flagImg.getScaledInstance(newButton.getWidth() - 10, newButton.getHeight() - 10, Image.SCALE_FAST);
                                        newButton.setIcon(new ImageIcon(newImg));
                                        newButton.setName("flagged");
                                    }
                                    else{
                                        newButton.setText("F");
                                        newButton.setName("flagged");
                                    }
                                    numFlags--;
                                    flagLabel.setText("" + numFlags);
                                }
                                else if (newButton.getName().equals("flagged")){
                                    newButton.setText("");
                                    newButton.setIcon(null);
                                    newButton.setName("open");
                                    numFlags++;
                                    flagLabel.setText("" + numFlags);
                                }
                            }
                        }
                    }
                });
                gamePanel.add(newButton);
            }
        }

        frame.pack(); // Makes everything the desired size and removes extra space
    }

    private void cellLeftClick(JButton cell, int row, int col) {
        // Generate the minesweeper board on the first click
        if (!ms.boardCreated()){
            ms.generateBoard(row, col);
            System.out.println("Board Generated");

            // Start the timer
            timer.start();
        }

        int cellValue = ms.getCellValue(row, col);
        // System.out.println(row + " " + col);

        if (cell.getName().equals("open")){
            if (cellValue == 0){
                revealZeros(row, col);
            }
            else if (cellValue == -1){
                // stop the timer
                timer.stop();

                // Check if mine image exists, if not use text instead
                if (mineImg != null){
                    Image newImg = mineImg.getScaledInstance(cell.getWidth() - 10, cell.getHeight() - 10, Image.SCALE_FAST);
                    cell.setIcon(new ImageIcon(newImg));
                    cell.setName("mine");
                }
                else{
                    cell.setText("X");
                    cell.setName("mine");
                }
                cell.setBorder(lowerBevel);
                cell.setBackground(Color.lightGray);
                cell.setOpaque(true);
                gameOver = true;
                System.out.println("Game Over!");

                revealMines();
            }
            else {
                revealNumber(cell, cellValue);
            }
        }
    }

    // Uses recursive algorithm to find all connected cells that have a value of 0
    // Stops when encounters cell that has already been seen or doesn't have a value of 0.
    private void revealZeros(int row, int col){
        JButton curCell = (JButton)gamePanel.getComponent(row * ms.getBoardWidth() + col);
        if (!curCell.getName().equals("open")){
            return;
        }
        
        // Check if the value of the cell is > 0
        if (ms.getCellValue(row, col) != 0){
            revealNumber(curCell, ms.getCellValue(row, col));
            return;
        }
        else {
            curCell.setBorder(lowerBevel);
            // Ref https://stackoverflow.com/questions/1065691/how-to-set-the-background-color-of-a-jbutton-on-the-mac-os
            curCell.setBackground(Color.lightGray);
            curCell.setOpaque(true);
            curCell.setName("checked");
        }

        // Check adjacent cells
        if (row > 0) revealZeros(row - 1, col);
        if (row < ms.getBoardHeight() - 1) revealZeros(row + 1, col);
        if (col > 0) revealZeros(row, col -1);
        if (col < ms.getBoardWidth() - 1) revealZeros(row, col + 1);

        // Check for diagonal cells
        if (row > 0 && col > 0) revealZeros(row - 1, col - 1);
        if (row < ms.getBoardHeight() - 1 && col < ms.getBoardWidth() - 1) revealZeros(row + 1, col + 1);
        if (row < ms.getBoardHeight() - 1 && col > 0) revealZeros(row + 1, col - 1);
        if (row > 0 && col < ms.getBoardWidth() - 1) revealZeros(row - 1, col + 1);
    }

    private void revealNumber(JButton cell, int cellValue){
        cell.setBorder(lowerBevel);
        // Ref https://stackoverflow.com/questions/1065691/how-to-set-the-background-color-of-a-jbutton-on-the-mac-os
        cell.setBackground(Color.lightGray);
        cell.setOpaque(true);

        // Ref https://stackoverflow.com/questions/15393385/how-to-change-text-color-of-a-jbutton
        // Ref https://www.reddit.com/r/Minesweeper/comments/g7jzri/what_are_the_google_minesweeper_colors/
        if (cellValue == 1){
            cell.setForeground(Color.blue);
        }
        else if (cellValue == 2){
            cell.setForeground(Color.green);
        }
        else if (cellValue == 3){
            cell.setForeground(Color.red);
        }
        else if (cellValue == 4){
            cell.setForeground(Color.magenta);
        }
        else if (cellValue == 5){
            cell.setForeground(Color.yellow);
        }
        else if (cellValue == 6){
            cell.setForeground(Color.cyan);
        }
        else if (cellValue == 7){
            cell.setForeground(Color.darkGray);
        }
        else if (cellValue == 8){
            cell.setForeground(Color.lightGray);
        }

        cell.setText(cellValue + "");
        cell.setName("checked");
    }

    private void revealMines(){
        JButton button = (JButton)gamePanel.getComponent(0);
        Image newImg = mineImg.getScaledInstance(button.getWidth() - 10, button.getHeight() - 10, Image.SCALE_FAST);
        ImageIcon mineIcon = new ImageIcon(newImg);

        for (int row = 0; row < ms.getBoardHeight(); row++){
            for (int col = 0; col < ms.getBoardWidth(); col++){
                if (ms.getCellValue(row, col) == -1){
                    JButton curCell = (JButton)gamePanel.getComponent(row * ms.getBoardWidth() + col);
                    if (curCell.getName().equals("open")){
                        if (mineImg != null){
                            curCell.setIcon(mineIcon);
                            curCell.setName("mine");
                        }
                        else{
                            curCell.setText("X");
                            curCell.setName("mine");
                        }
                        curCell.setBorder(lowerBevel);
                        curCell.setBackground(Color.lightGray);
                        curCell.setOpaque(true);
                    }
                }
            }
        }
    }

    private void updateTimeLabel() {
        String time = String.format("%03d", elapsedTime);
        timeLabel.setText(time);
    }
}
