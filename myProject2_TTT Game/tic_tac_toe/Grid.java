package tic_tac_toe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This class contains all the features and functions the game interface need.
 * (this part was learned online)
 */
public class Grid {
    private static final int boardLength = 600;

    private JRadioButton AIFirst;
    private JRadioButton playerFirst;
    private JButton resetButton;
    private JButton backButton;
    private JPanel window;
    private JPanel grid;
    private JPanel ui;
    private JLabel nextLabel;
    private JCheckBox winnableCheckBox;
    private JLabel[][] cells = new JLabel[3][3];

    private static final String CROSS = "X";
    private static final String CIRCLE = "O";

    private JFrame main;
    private tic_tac_toe.AI AI = null;
    private boolean isAI = false;
    private String currentTurn = CIRCLE;
    private int[][] placement = new int[3][3];

    public Grid(tic_tac_toe.AI ai, Welcome wl, String title) {
        this.AI = ai;
        if (this.AI == null)
            winnableCheckBox.setVisible(false);
        AIFirst.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    playerFirst.setSelected(false);
                    reset();
                }
            }
        });
        playerFirst.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    AIFirst.setSelected(false);
                    reset();
                }
            }
        });
        playerFirst.setSelected(true);
        main = new JFrame(title);
        main.setContentPane(window);
        main.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                wl.setVisible(true);
                main.dispose();
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        main.setResizable(false);
        main.pack();
        main.setLocationRelativeTo(null);
        main.setVisible(true);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wl.setVisible(true);
                main.dispose();
            }
        });
        winnableCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                    AI.setWinnable(true);
                else
                    AI.setWinnable(false);
            }
        });
        if (AI == null)
            AIFirst.setText(CROSS);
        else
            AIFirst.setText(CROSS + " (AI)");
    }

    private void reset() {
        placement = new int[3][3];
        for (JLabel cellss[] : cells) {
            for (JLabel cell : cellss)
                cell.setText("");
        }
        // Determine who goes first...
        if (AI != null) {
            if (AIFirst.isSelected()) {
                currentTurn = CIRCLE;
                isAI = true;
                next();
            } else {
                currentTurn = CIRCLE;
                isAI = false;
            }
        } else {
            if (AIFirst.isSelected()) {
                currentTurn = CROSS;
            } else {
                currentTurn = CIRCLE;
            }
        }
    }

    private void createUIComponents() {
        winnableCheckBox = new JCheckBox();
        window = new JPanel();
        window.setBounds(0, 0, boardLength - 400, boardLength);
        int unitDistance = (boardLength - 20) / 3;
        final int padding = 4;
        // draw the grid
        grid = new JPanel() {
            private Rectangle[] gridLines = new Rectangle[]{
                    new Rectangle(0, unitDistance, boardLength, 10),
                    new Rectangle(0, 2 * unitDistance + 10, boardLength, 10),
                    new Rectangle(unitDistance, 0, 10, boardLength),
                    new Rectangle(unitDistance * 2 + 10, 0, 10, boardLength)};

            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                for (Rectangle r : gridLines) g.fillRect(r.x, r.y, r.width, r.height);
            }
        };
        grid.setBounds(0, 0, boardLength, boardLength);
        grid.setPreferredSize(new Dimension(boardLength, boardLength));
        grid.setLayout(null);
        // initialize the cells
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final JLabel cell = new JLabel();
                final int _i = i;
                final int _j = j;
                cell.setBounds(j * unitDistance + j * 10 + padding, i * unitDistance + i * 10 + padding, unitDistance - padding, unitDistance - padding);
                cell.setFont(new Font("Calibri", Font.BOLD, 210));
                cell.setText("");
                cell.setHorizontalTextPosition(SwingConstants.CENTER);
                cell.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (placement[_i][_j] == 0) {
                            if (AI != null && (!isAI))
                                isAI = true;
                            place(_i, _j);
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (placement[_i][_j] == 0) {
                            cell.setForeground(new Color(200, 200, 200));
                            cell.setText(currentTurn);
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (placement[_i][_j] == 0) cell.setText("");
                    }
                });
                cells[i][j] = cell;
                grid.add(cell);
            }
        }
    }

    /**
     * check if one wins
     * if not, switch turn
     */
    private void next() {
        int sumRow = 0;
        int sumColumn = 0;
        int sumMjDiagonal = 0;
        int sumMnDiagonal = 0;
        boolean isFulfilled = true;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (placement[i][j] == 0) isFulfilled = false;
                sumRow += placement[i][j];
                sumColumn += placement[j][i];
                if (i == j) sumMjDiagonal += placement[i][j];
                if (i == Math.abs(j - 2)) sumMnDiagonal += placement[i][j];
            }
            if (sumRow == 3 || sumColumn == 3) {
                winOperation(CIRCLE);
                return;
            }
            if (sumRow == -3 || sumColumn == -3) {
                winOperation(CROSS);
                return;
            }
            sumRow = 0;
            sumColumn = 0;
        }
        if (sumMjDiagonal == -3 || sumMnDiagonal == -3) {
            winOperation(CROSS);
            return;
        }
        if (sumMjDiagonal == 3 || sumMnDiagonal == 3) {
            winOperation(CIRCLE);
            return;
        }
        if (isFulfilled) {
            currentTurn = "";
            JOptionPane.showMessageDialog(main, "draw!", "Tic-Tac-Toe", JOptionPane.INFORMATION_MESSAGE);
            for (int[] row : placement)
                for (int i = 0; i < row.length; i++)
                    row[i] = 1;
            return;
        }
        if (currentTurn.equals(CROSS))
            currentTurn = CIRCLE;
        else
            currentTurn = CROSS;
        nextLabel.setText("Next: " + currentTurn);
        if (AI != null && isAI) {
            isAI = false;
            int[] AIplacement = AI.place(placement, -1);
            place(AIplacement[0], AIplacement[1]);
        }
        while (true) if (System.currentTimeMillis() == System.currentTimeMillis()) break;
    }

    /**
     * fill the board to prevent further operation
     *
     * @param who Either cross or circle, the one who wins the game
     */
    private void winOperation(String who) {
        currentTurn = "";
        JOptionPane.showMessageDialog(main, who + " win!", "Tic-Tac-Toe", JOptionPane.INFORMATION_MESSAGE);
        for (int[] row : placement)
            for (int i = 0; i < row.length; i++)
                row[i] = 1;
    }

    /**
     * @param i row number
     * @param j column number
     */
    private void place(int i, int j) {
        if (currentTurn.equals(CROSS))
            placement[i][j] = -1;
        else
            placement[i][j] = 1;
        cells[i][j].setForeground(new Color(0, 0, 0));
        cells[i][j].setText(currentTurn);
        next();
    }
}
