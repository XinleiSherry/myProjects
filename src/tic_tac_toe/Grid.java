package tic_tac_toe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Grid {
    private static final int boardLength = 400;

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

    private static final String CROSS = "×";
    private static final String CIRCLE = "○";

    private JFrame main;
    private tic_tac_toe.AI AI = null;
    private boolean isAI = false;
    private String currentTurn = CIRCLE;
    private int[][] placement = new int[3][3];

    public Grid(tic_tac_toe.AI ai, Welcome wl, String title) {
        this.AI = ai;
        $$$setupUI$$$();
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
        window.setBounds(0, 0, boardLength + 200, boardLength);
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

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        window.setLayout(new GridBagLayout());
        grid.setBackground(new Color(-1114125));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        window.add(grid, gbc);
        ui = new JPanel();
        ui.setLayout(new GridBagLayout());
        ui.setBackground(new Color(-1900801));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 4;
        gbc.fill = GridBagConstraints.BOTH;
        window.add(ui, gbc);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
        panel1.setBackground(new Color(-1900801));
        panel1.setFont(new Font(panel1.getFont().getName(), panel1.getFont().getStyle(), panel1.getFont().getSize()));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        ui.add(panel1, gbc);
        nextLabel = new JLabel();
        nextLabel.setFont(new Font("Calibri", Font.BOLD, 60));
        nextLabel.setText("Next: ○");
        panel1.add(nextLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(10, 10, 0, 10), -1, -1));
        panel2.setBackground(new Color(-1900801));
        panel2.setEnabled(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.VERTICAL;
        ui.add(panel2, gbc);
        final JLabel label1 = new JLabel();
        label1.setFont(new Font("Calibri", Font.BOLD, 30));
        label1.setText(" Who goes next ");
        panel2.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 10, 0, 10), -1, -1));
        panel3.setBackground(new Color(-1900801));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        ui.add(panel3, gbc);
        playerFirst = new JRadioButton();
        playerFirst.setBackground(new Color(-1900801));
        playerFirst.setFont(new Font("Calibri", playerFirst.getFont().getStyle(), 26));
        playerFirst.setText("○");
        panel3.add(playerFirst, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        AIFirst = new JRadioButton();
        AIFirst.setBackground(new Color(-1900801));
        AIFirst.setFont(new Font("Calibri", AIFirst.getFont().getStyle(), 26));
        AIFirst.setText("× (AI)");
        panel3.add(AIFirst, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        panel4.setBackground(new Color(-1900801));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        ui.add(panel4, gbc);
        resetButton = new JButton();
        resetButton.setFont(new Font("Calibri", resetButton.getFont().getStyle(), 30));
        resetButton.setText("Reset");
        panel4.add(resetButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        backButton = new JButton();
        backButton.setFont(new Font("Calibri", backButton.getFont().getStyle(), 30));
        backButton.setText("Back");
        panel4.add(backButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 10, 0), -1, -1));
        panel5.setBackground(new Color(-1900801));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        ui.add(panel5, gbc);
        winnableCheckBox.setBackground(new Color(-1900801));
        winnableCheckBox.setFont(new Font("Calibri", winnableCheckBox.getFont().getStyle(), 24));
        winnableCheckBox.setHideActionText(true);
        winnableCheckBox.setText("Winnable");
        panel5.add(winnableCheckBox, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return window;
    }
}
