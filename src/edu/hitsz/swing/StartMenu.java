package edu.hitsz.swing;

import edu.hitsz.application.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenu {


    private JButton commonButton;
    private JButton simpleTableButton;
    private JPanel MainPanel;
    private JButton easyButton;
    private JButton hardButton;
    private JComboBox comboBox;
    private JLabel musicLabel;


    public StartMenu() {
        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.game = new EasyGame();
                Main.cardPanel.add(Main.game);
                Main.cardLayout.last(Main.cardPanel);
                Main.game.action();
            }
        });
        commonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.game = new CommonGame();
                Main.cardPanel.add(Main.game);
                Main.cardLayout.last(Main.cardPanel);
                Main.game.action();
            }
        });
        hardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.game = new HardGame();
                Main.cardPanel.add(Main.game);
                Main.cardLayout.last(Main.cardPanel);
                Main.game.action();
            }
        });
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> combo = (JComboBox<String>) e.getSource();
                Game.isPlayMusic = !"关".equals(combo.getSelectedItem());
                System.out.println("Selected Option: " + Game.isPlayMusic);
            }
        });
    }

    public JPanel getMainPanel() {
        return MainPanel;
    }

}
