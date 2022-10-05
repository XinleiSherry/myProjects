package tic_tac_toe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 继承JFrame
 */
public class Welcome extends JFrame {

    private static final long serialVersionUID = 1L;
    private Grid gd;
    private AI ai;

    public Welcome() {
        setTitle("Tic-Tac-Toe");
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 400);
        this.setLocation(200, 200);
        this.getContentPane().setBackground(new Color(255, 255, 255));
        this.setResizable(false);
        this.setLayout(null);
        this.setLocationRelativeTo(null);

        initialize_Buttons();
        ai = new AI();

    }

    /**
     * 封装
     */
    private void initialize_Buttons() {
        Button bt = new Button("Single Player");
        bt.setBounds(75, 75, 250, 100);
        bt.setFont(new Font("Calibri", Font.BOLD, 30));
        bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gd = new Grid(ai, Welcome.this, "Tic-Tac-Toe Single Player");
                setVisible(false);
            }
        });
        this.add(bt);

        bt = new Button("Double Players");
        bt.setBounds(75, 225, 250, 100);
        bt.setFont(new Font("Calibri", Font.BOLD, 30));
        bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gd = new Grid(null, Welcome.this, "Tic-Tac-Toe Double Player");
                setVisible(false);
            }
        });
        this.add(bt);
    }
}
