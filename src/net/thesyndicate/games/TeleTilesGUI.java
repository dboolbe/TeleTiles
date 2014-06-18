package net.thesyndicate.games;

import net.thesyndicate.games.utilities.HighScore;
import net.thesyndicate.games.utilities.HighScoreTable;
import net.thesyndicate.games.utilities.Timer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Vector;

/**
 * Created by dboolbe on 6/12/14.
 */
public class TeleTilesGUI extends JFrame implements ActionListener, ItemListener {

    final int DEFAULT_CELLS_PER_SIDE = 4;
    final String REALNAME = "Teleporting Tiles";
    final String REALSHORTNAME = "TeleTiles";
    final String NAME = "Clapham Noyd's Refine Toffee Nut Puzzle";
    final String SHORTNAME = "RefineToffeeNutPuzzle";
    final double VERSION = 1.5;

    private enum Action {
        NEW("NEW"),
        ROW("ROW"),
        COLUMN("COLUMN"),
        QUIT("QUIT"),
        HIGHSCORE("HIGHSCORE"),
        ABOUT("ABOUT");

        private final String actionString;

        Action(String actionString) {
            this.actionString = actionString;
        }

        @Override
        public String toString() {
            return this.actionString;
        }
    }

    JButton[][] buttons;
    TeleTiles game;
    int cellsPerRow, cellsPerColumn;

    // the path of the temporary directory
    String temporaryDir = null;
    Timer timer = null;
    HighScoreTable highScoreTable = null;

    boolean invisibleMode = false;
    int moveCounter = 0;
    JLabel moveLabel = new JLabel(String.format("%03d Moves", moveCounter));

    // The name of the file to open.
    String fileName = SHORTNAME + ".myapp";

    public TeleTilesGUI() {
        highScoreTable = readHighScoreFile();
        setupGrid((DEFAULT_CELLS_PER_SIDE + 2), (DEFAULT_CELLS_PER_SIDE + 2));
    }

    public TeleTilesGUI(int cellsPerSide) {
        highScoreTable = readHighScoreFile();
        setupGrid((cellsPerSide + 2), (cellsPerSide + 2));
    }

    public TeleTilesGUI(int cellsPerRow, int cellsPerColumn) {
        highScoreTable = readHighScoreFile();
        setupGrid((cellsPerRow + 2), (cellsPerColumn + 2));
    }

    private JMenuBar createMenuBar() {
        //Where the GUI is created:
        JMenuBar menuBar;
        JMenu menu, submenu, subsubmenu, subsubsubmenu;
        JMenuItem menuItem;
        ButtonGroup buttonGroup;
        JRadioButtonMenuItem rbMenuItem;
        JCheckBoxMenuItem cbMenuItem;

        //Create the menu bar.
        menuBar = new JMenuBar();

        // Build the first menu.
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menu.getAccessibleContext().setAccessibleDescription(
                "The main menu option containing the major menu items.");
        menuBar.add(menu);

        // add the 'New Game' menu item.
        menuItem = new JMenuItem("New Game", KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Starts a new game.");
        menuItem.setName(Action.NEW.toString());
        menuItem.addActionListener(this);
        menu.add(menuItem);

        // add the 'Edit Game' menu item.
        submenu = new JMenu("Edit Game");
        submenu.setMnemonic(KeyEvent.VK_E);
        submenu.getAccessibleContext().setAccessibleDescription("Edit the game settings.");

        subsubmenu = new JMenu("Change Dimensions");
        submenu.add(subsubmenu);

        subsubsubmenu = new JMenu("Cells Per Row");
        subsubmenu.add(subsubsubmenu);

        buttonGroup = new ButtonGroup();

        for (int i = 3; i <= 11; i++) {
            rbMenuItem = new JRadioButtonMenuItem("" + i);
            if (i == cellsPerRow)
                rbMenuItem.setSelected(true);
            buttonGroup.add(rbMenuItem);
            rbMenuItem.setName(Action.ROW.toString() + "," + i);
            rbMenuItem.addActionListener(this);
            subsubsubmenu.add(rbMenuItem);
        }

        subsubsubmenu = new JMenu("Cells Per Column");
        subsubmenu.add(subsubsubmenu);

        buttonGroup = new ButtonGroup();

        for (int i = 3; i <= 11; i++) {
            rbMenuItem = new JRadioButtonMenuItem("" + i);
            if (i == cellsPerColumn)
                rbMenuItem.setSelected(true);
            buttonGroup.add(rbMenuItem);
            rbMenuItem.setName(Action.COLUMN.toString() + "," + i);
            rbMenuItem.addActionListener(this);
            subsubsubmenu.add(rbMenuItem);
        }

        submenu.addSeparator();

        cbMenuItem = new JCheckBoxMenuItem("Impossible(No Improbable) Mode");
        cbMenuItem.setMnemonic(KeyEvent.VK_I);
        cbMenuItem.addItemListener(this);
        submenu.add(cbMenuItem);

        menu.add(submenu);

        menu.addSeparator();

        // add the 'Quit' menu item.
        menuItem = new JMenuItem("Quit", KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Quit the game.");
        menuItem.setName(Action.QUIT.toString());
        menuItem.addActionListener(this);
        menu.add(menuItem);

        // Build the second menu.
        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        menu.getAccessibleContext().setAccessibleDescription(
                "The help menu contain menu items for details about the game.");
        menuBar.add(menu);

        // add the 'High Scores' menu item.
        menuItem = new JMenuItem("High Scorers", KeyEvent.VK_H);
        menuItem.getAccessibleContext().setAccessibleDescription("Display a table of the high scoring players.");
        menuItem.setName(Action.HIGHSCORE.toString());
        menuItem.addActionListener(this);
        menu.add(menuItem);

        // add the 'About' menu item.
        menuItem = new JMenuItem("About " + NAME, KeyEvent.VK_A);
        menuItem.getAccessibleContext().setAccessibleDescription("Tells you about the game.");
        menuItem.setName(Action.ABOUT.toString());
        menuItem.addActionListener(this);
        menu.add(menuItem);

        return menuBar;
    }

    private void setupGrid(int row, int column) {
        getContentPane().removeAll();

        cellsPerRow = (row - 2);
        cellsPerColumn = (column - 2);

        game = new TeleTiles(cellsPerRow, cellsPerColumn);
        buttons = new JButton[row][column];

        // set up the menu bar
        setJMenuBar(createMenuBar());

        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(row, column));

        for (int r = 0; r < row; r++) {
            for (int c = 0; c < column; c++) {
                // instantiate each button
                buttons[r][c] = new JButton();

                // remove the corner buttons
                if (((r == 0) || (r == (row - 1))) && ((c == 0) || (c == (column - 1)))) {
                    buttons[r][c].setVisible(false);
                } else if (r == 0) {
                    buttons[r][c].setText("U");
                    buttons[r][c].setActionCommand("U," + (c - 1));
                    buttons[r][c].addActionListener(this);
                } else if (c == 0) {
                    buttons[r][c].setText("L");
                    buttons[r][c].setActionCommand("L," + (r - 1));
                    buttons[r][c].addActionListener(this);
                } else if (c == (column - 1)) {
                    buttons[r][c].setText("R");
                    buttons[r][c].setActionCommand("R," + (r - 1));
                    buttons[r][c].addActionListener(this);
                } else if (r == (row - 1)) {
                    buttons[r][c].setText("D");
                    buttons[r][c].setActionCommand("D," + (c - 1));
                    buttons[r][c].addActionListener(this);
                } else {
                    buttons[r][c].setForeground(Color.BLACK);
                }

                // add the button to the panel
                panel.add(buttons[r][c]);
            }
        }

        setTitle(NAME + " Ver." + VERSION);
        Border panelBorder = BorderFactory.createLoweredBevelBorder();
        panel.setBorder(panelBorder);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        moveCounter = 0;
        final JLabel timeLabel = new JLabel();

        timer = new Timer(timeLabel);

        // configuring move and time thread
        Thread moveTimeThread = new Thread(timer);

        moveTimeThread.start();

        Panel footerBar = new Panel(new BorderLayout());
        footerBar.add(timeLabel, BorderLayout.EAST);
        footerBar.add(moveLabel, BorderLayout.WEST);

        // add the footer to the frame
        add(footerBar, BorderLayout.SOUTH);

        // screen size varies in order for button text to appear
        setSize(65 * column, 60 * row);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        updateText();
        setVisible(false);
        validate();
        setVisible(true);
    }

    private void updateText() {
        for (int row = 0; row < game.getMaxRow(); row++) {
            for (int column = 0; column < game.getMaxColumn(); column++) {
                // assign the button to a temporary variable
                JButton tempButton = buttons[row + 1][column + 1];

                // update the text of the button
                if (invisibleMode)
                    tempButton.setText("");
                else
                    tempButton.setText("" + game.getGridElement(row, column));

                // update the background color of the button
                if (game.testGridElement(row, column)) {
                    tempButton.setBackground(Color.GREEN);
                    tempButton.setForeground(Color.BLACK);
                } else {
                    tempButton.setOpaque(true);
                    tempButton.setForeground(Color.BLACK);
                    tempButton.setBackground(new Color(Color.BLACK.getRGB() ^ Integer.MAX_VALUE));
                }
            }
        }
    }

    private void incrementMove() {
        moveCounter++;
        moveLabel.setText(String.format("%03d Moves", moveCounter));
    }

    private HighScore getHighScorerDetails(String bracket, int totalMoves, long totalTime) {
        String username = JOptionPane.showInputDialog(this, "You were a high scorer in the " + bracket + " difficulty.",
                "Enter your name:", JOptionPane.QUESTION_MESSAGE);
        return new HighScore(username.trim().toUpperCase(), totalMoves, totalTime);
    }

    private void gameOverState() {
        timer.pauseTime();

        System.out.println("Game Over");

        JOptionPane.showMessageDialog(this, "Congratulations!!!\n" + "It took you " + moveCounter + " moves in " + timer.getTotalTimeString());

        // add score to high score table
        if (cellsPerRow * cellsPerColumn <= 20) {
            if (invisibleMode) {
                if (highScoreTable.isEasyBlankHighScore(new HighScore("tmp", moveCounter, timer.getTotalTime())))
                    highScoreTable.addEasyBlankHighScore(getHighScorerDetails("easy (blank)", moveCounter, timer.getTotalTime()));
            } else {
                if (highScoreTable.isEasyHighScore(new HighScore("tmp", moveCounter, timer.getTotalTime())))
                    highScoreTable.addEasyHighScore(getHighScorerDetails("easy", moveCounter, timer.getTotalTime()));
            }
        } else if (cellsPerRow * cellsPerColumn >= 77) {
            if (invisibleMode)
                if (highScoreTable.isMediumBlankHighScore(new HighScore("tmp", moveCounter, timer.getTotalTime())))
                    highScoreTable.addMediumBlankHighScore(getHighScorerDetails("medium (blank)", moveCounter, timer.getTotalTime()));
                else if (highScoreTable.isMediumHighScore(new HighScore("tmp", moveCounter, timer.getTotalTime())))
                    highScoreTable.addMediumHighScore(getHighScorerDetails("medium", moveCounter, timer.getTotalTime()));
        } else {
            if (invisibleMode)
                if (highScoreTable.isHardBlankHighScore(new HighScore("tmp", moveCounter, timer.getTotalTime())))
                    highScoreTable.addHardBlankHighScore(getHighScorerDetails("hard (blank)", moveCounter, timer.getTotalTime()));
                else if (highScoreTable.isHardHighScore(new HighScore("tmp", moveCounter, timer.getTotalTime())))
                    highScoreTable.addHardHighScore(getHighScorerDetails("hard", moveCounter, timer.getTotalTime()));
        }
        // write the results to the high score table
        writeHighScoreFile(highScoreTable);

        // debug purposes only
        printScoreTable();

        // reset the game and update the board
        reset();
    }

    public void printScoreTable() {
        int tmpCount;

        String title = "High Scores";

        // this panel will host all of the high score tables
        JPanel panel = new JPanel(new GridLayout(3, 2));

        // configure the header
        final Vector headers = new Vector();
        headers.add("#");
        headers.add("Username");
        headers.add("Moves");
        headers.add("Time");

        // debug purposes only
        System.out.println("Easy Mode");
        JPanel easyPanel = new JPanel();
        easyPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        "Easy Difficulty(20 cells or less):", TitledBorder.CENTER, TitledBorder.TOP));
        tmpCount = 1;
        Vector easyRows = new Vector(); // row array
        for (HighScore highScore : highScoreTable.getHighScoreArrayEasy()) {
            System.out.format("%d: Username: %s Moves: %d Time: %d%n", tmpCount, highScore.getUsername(), highScore.getMoves(), highScore.getElapsedTime());
            Vector easyRow = new Vector();
            easyRow.add(tmpCount);
            if (highScore.getMoves() == Integer.MAX_VALUE) {
                easyRow.add("");
                easyRow.add("");
                easyRow.add("");
            } else {
                easyRow.add(highScore.getUsername());
                easyRow.add(highScore.getMoves());
                easyRow.add(Timer.convertToTimeString(highScore.getElapsedTime()));
            }
            easyRows.add(easyRow);
            tmpCount++;
        }
        JTable easyTable = new JTable(easyRows, headers);
        easyTable.setPreferredScrollableViewportSize(easyTable.getPreferredSize());
        easyPanel.add(new JScrollPane(easyTable));
        panel.add(easyPanel);

        System.out.println("Easy Mode (Blank)");
        JPanel easyBlankPanel = new JPanel();
        easyBlankPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        "Easy Blank Difficulty(20 cells or less):", TitledBorder.CENTER, TitledBorder.TOP));
        tmpCount = 1;
        Vector easyBlankRows = new Vector(); // row array
        for (HighScore highScore : highScoreTable.getHighScoreArrayEasyBlank()) {
            System.out.format("%d: Username: %s Moves: %d Time: %d%n", tmpCount, highScore.getUsername(), highScore.getMoves(), highScore.getElapsedTime());
            Vector easyBlankRow = new Vector();
            easyBlankRow.add(tmpCount);
            if (highScore.getMoves() == Integer.MAX_VALUE) {
                easyBlankRow.add("");
                easyBlankRow.add("");
                easyBlankRow.add("");
            } else {
                easyBlankRow.add(highScore.getUsername());
                easyBlankRow.add(highScore.getMoves());
                easyBlankRow.add(Timer.convertToTimeString(highScore.getElapsedTime()));
            }
            easyBlankRows.add(easyBlankRow);
            tmpCount++;
        }
        JTable easyBlankTable = new JTable(easyBlankRows, headers);
        easyBlankTable.setPreferredScrollableViewportSize(easyBlankTable.getPreferredSize());
        easyBlankPanel.add(new JScrollPane(easyBlankTable));
        panel.add(easyBlankPanel);

        System.out.println("Medium Mode");
        JPanel mediumPanel = new JPanel();
        mediumPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        "Medium Difficulty(between 21 and 76 cells):", TitledBorder.CENTER, TitledBorder.TOP));
        tmpCount = 1;
        Vector mediumRows = new Vector(); // row array
        for (HighScore highScore : highScoreTable.getHighScoreArrayMedium()) {
            System.out.format("%d: Username: %s Moves: %d Time: %d%n", tmpCount, highScore.getUsername(), highScore.getMoves(), highScore.getElapsedTime());
            Vector mediumRow = new Vector();
            mediumRow.add(tmpCount);
            if (highScore.getMoves() == Integer.MAX_VALUE) {
                mediumRow.add("");
                mediumRow.add("");
                mediumRow.add("");
            } else {
                mediumRow.add(highScore.getUsername());
                mediumRow.add(highScore.getMoves());
                mediumRow.add(Timer.convertToTimeString(highScore.getElapsedTime()));
            }
            mediumRows.add(mediumRow);
            tmpCount++;
        }
        JTable mediumTable = new JTable(mediumRows, headers);
        mediumTable.setPreferredScrollableViewportSize(mediumTable.getPreferredSize());
        mediumPanel.add(new JScrollPane(mediumTable));
        panel.add(mediumPanel);

        System.out.println("Medium Mode (Blank)");
        JPanel mediumBlankPanel = new JPanel();
        mediumBlankPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        "Medium Blank Difficulty(between 21 and 76 cells):", TitledBorder.CENTER, TitledBorder.TOP));
        tmpCount = 1;
        Vector mediumBlankRows = new Vector(); // row array
        for (HighScore highScore : highScoreTable.getHighScoreArrayMediumBlank()) {
            System.out.format("%d: Username: %s Moves: %d Time: %d%n", tmpCount, highScore.getUsername(), highScore.getMoves(), highScore.getElapsedTime());
            Vector mediumBlankRow = new Vector();
            mediumBlankRow.add(tmpCount);
            if (highScore.getMoves() == Integer.MAX_VALUE) {
                mediumBlankRow.add("");
                mediumBlankRow.add("");
                mediumBlankRow.add("");
            } else {
                mediumBlankRow.add(highScore.getUsername());
                mediumBlankRow.add(highScore.getMoves());
                mediumBlankRow.add(Timer.convertToTimeString(highScore.getElapsedTime()));
            }
            mediumBlankRows.add(mediumBlankRow);
            tmpCount++;
        }
        JTable mediumBlankTable = new JTable(mediumBlankRows, headers);
        mediumBlankTable.setPreferredScrollableViewportSize(mediumBlankTable.getPreferredSize());
        mediumBlankPanel.add(new JScrollPane(mediumBlankTable));
        panel.add(mediumBlankPanel);

        System.out.println("Hard Mode");
        JPanel hardPanel = new JPanel();
        hardPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        "Hard Difficulty(77 cells or more):", TitledBorder.CENTER, TitledBorder.TOP));
        tmpCount = 1;
        Vector hardRows = new Vector(); // row array
        for (HighScore highScore : highScoreTable.getHighScoreArrayHard()) {
            System.out.format("%d: Username: %s Moves: %d Time: %d%n", tmpCount, highScore.getUsername(), highScore.getMoves(), highScore.getElapsedTime());
            Vector hardRow = new Vector();
            hardRow.add(tmpCount);
            if (highScore.getMoves() == Integer.MAX_VALUE) {
                hardRow.add("");
                hardRow.add("");
                hardRow.add("");
            } else {
                hardRow.add(highScore.getUsername());
                hardRow.add(highScore.getMoves());
                hardRow.add(Timer.convertToTimeString(highScore.getElapsedTime()));
            }
            hardRows.add(hardRow);
            tmpCount++;
        }
        JTable hardTable = new JTable(hardRows, headers);
        hardTable.setPreferredScrollableViewportSize(hardTable.getPreferredSize());
        hardPanel.add(new JScrollPane(hardTable));
        panel.add(hardPanel);

        System.out.println("Hard Mode (Blank)");
        JPanel hardBlankPanel = new JPanel();
        hardBlankPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        "Hard Blank Difficulty(77 cells or more):", TitledBorder.CENTER, TitledBorder.TOP));
        tmpCount = 1;
        Vector hardBlankRows = new Vector(); // row array
        for (HighScore highScore : highScoreTable.getHighScoreArrayHardBlank()) {
            System.out.format("%d: Username: %s Moves: %d Time: %d%n", tmpCount, highScore.getUsername(), highScore.getMoves(), highScore.getElapsedTime());
            Vector hardBlankRow = new Vector();
            hardBlankRow.add(tmpCount);
            if (highScore.getMoves() == Integer.MAX_VALUE) {
                hardBlankRow.add("");
                hardBlankRow.add("");
                hardBlankRow.add("");
            } else {
                hardBlankRow.add(highScore.getUsername());
                hardBlankRow.add(highScore.getMoves());
                hardBlankRow.add(Timer.convertToTimeString(highScore.getElapsedTime()));
            }
            hardBlankRows.add(hardBlankRow);
            tmpCount++;
        }
        JTable hardBlankTable = new JTable(hardBlankRows, headers);
        hardBlankTable.setPreferredScrollableViewportSize(hardBlankTable.getPreferredSize());
        hardBlankPanel.add(new JScrollPane(hardBlankTable));
        panel.add(hardBlankPanel);

        JOptionPane.showMessageDialog(this, panel, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void reset() {
        game.reset();
        timer.resetTime();
        moveCounter = 0;
        updateText();
        timer.resumeTime();
    }

    public static HighScoreTable initializeTable() {
        HighScoreTable highScoreTable = new HighScoreTable();

        for (int i = 0; i < 5; i++) {
            highScoreTable.addEasyHighScore(new HighScore("", Integer.MAX_VALUE, Long.MAX_VALUE));
            highScoreTable.addMediumHighScore(new HighScore("", Integer.MAX_VALUE, Long.MAX_VALUE));
            highScoreTable.addHardHighScore(new HighScore("", Integer.MAX_VALUE, Long.MAX_VALUE));
            highScoreTable.addEasyBlankHighScore(new HighScore("", Integer.MAX_VALUE, Long.MAX_VALUE));
            highScoreTable.addMediumBlankHighScore(new HighScore("", Integer.MAX_VALUE, Long.MAX_VALUE));
            highScoreTable.addHardBlankHighScore(new HighScore("", Integer.MAX_VALUE, Long.MAX_VALUE));
        }

        return highScoreTable;
    }

    private String getTemporaryDir() {
        String tmpDirPath = null;
        try {
            // create a temporary file
            Path pathToTempFile = Files.createTempFile(null, ".tmp");

            System.out.println("Recently created Temp file : " + pathToTempFile);

            // get temporary file path
            String absolutePath = pathToTempFile.toString();
            tmpDirPath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator) + 1);

            System.out.println("Tmp directory path : " + tmpDirPath);

            // delete the temporary file
            Files.delete(pathToTempFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // returns the temporary directory path
        return tmpDirPath;
    }

    public void writeHighScoreFile(HighScoreTable scoreTable) {
        if (temporaryDir == null)
            temporaryDir = getTemporaryDir();
        try {
            // creates a object output stream
            FileOutputStream fileOutputStream = new FileOutputStream(temporaryDir + fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            // write the Java object to the file
            objectOutputStream.writeObject(scoreTable);

            // close buffered writer object to save file
            objectOutputStream.close();
        } catch (IOException e) {
            System.out.println("Error writing to file '" + fileName + "'");

            e.printStackTrace();
        }
    }

    private HighScoreTable readHighScoreFile() {
        HighScoreTable tempTable = new HighScoreTable();

        if (temporaryDir == null)
            temporaryDir = getTemporaryDir();
        try {
            // create a byte buffer array for reading the data.
            byte[] buffer = new byte[1000];

            // create an input stream object
            FileInputStream inputStream = new FileInputStream(temporaryDir + fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            // read fin the object data and cast it
            tempTable = (HighScoreTable) objectInputStream.readObject();

            // Always close files.
            objectInputStream.close();

        } catch (FileNotFoundException e) {
            System.out.println("Unable to open file '" + temporaryDir + fileName + "'");

            tempTable = initializeTable();
        } catch (IOException e) {
            System.out.println("Error reading file '" + temporaryDir + fileName + "'");

            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return tempTable;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() instanceof JButton) {
            String actionCommand = ((JButton) actionEvent.getSource()).getActionCommand();
            String[] actionCommands = actionCommand.split(",");
            String direction = actionCommands[0].trim();
            int index = Integer.parseInt(actionCommands[1].trim());

            if (direction.equals("U"))
                game.columnXup(index);
            else if (direction.equals("D"))
                game.columnXdown(index);
            else if (direction.equals("L"))
                game.rowXleft(index);
            else
                game.rowXright(index);

            incrementMove();

            updateText();

            if (game.isGameOver())
                gameOverState();
        } else if (actionEvent.getSource() instanceof JMenuItem) {
            JMenuItem source = (JMenuItem) (actionEvent.getSource());
            String[] actionString = source.getName().split(",");
            Action action = Action.valueOf(actionString[0]);

            switch (action) {
                case NEW:
                    reset();
                    break;
                case ROW:
                    System.out.println("Calling action: " + action + " with value: " + actionString[1] + ".");

                    setupGrid(Integer.parseInt(actionString[1]) + 2, cellsPerColumn + 2);
                    validate();
                    break;
                case COLUMN:
                    System.out.println("Calling action: " + action + " with value: " + actionString[1] + ".");

                    setupGrid(cellsPerRow + 2, Integer.parseInt(actionString[1]) + 2);
                    validate();
                    break;
                case QUIT:
                    System.exit(0);
                    break;
                case HIGHSCORE:
                    printScoreTable();
                    break;
                case ABOUT:
                    JOptionPane.showMessageDialog(this, NAME + "\n"
                            + " Ver. " + VERSION + "\n"
                            + "Formally known as " + REALNAME + " A.K.A. as " + REALSHORTNAME + "\n"
                            + "is designed by theSyndicate,\n"
                            + "an open-source group providing simple computer\n"
                            + "programs for educational and entertainment purposes.");
                    break;
                default:
                    System.out.println("Action: " + action + " is currently not implemented.");
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        JCheckBoxMenuItem checkboxMenuItem = (JCheckBoxMenuItem) itemEvent.getSource();

        invisibleMode = checkboxMenuItem.getState();

        reset();
    }

    public static void main(String[] args) {
        TeleTilesGUI teleTilesGUI = new TeleTilesGUI();
    }
}
