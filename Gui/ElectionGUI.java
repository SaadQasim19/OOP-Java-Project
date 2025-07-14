package Gui;
import javax.swing.*;
import java.awt.*;


public class ElectionGUI extends JFrame {
    JTextArea resultArea;
    String electionType;

    public ElectionGUI(String type) {
        electionType = type;

        setTitle("Election Management System");
        setSize(700, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setResizable(false);

        JLabel titleLabel = new JLabel("Election Management System - " + electionType, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        JButton btnRegCand = new JButton("Register Candidate");
        JButton btnDropCand = new JButton("Drop Candidate");
        JButton btnRegVoter = new JButton("Register Voter");
        JButton btnVote = new JButton("Start Voting");
        JButton btnResult = new JButton("Show Results");

        buttonPanel.add(btnRegCand);
        buttonPanel.add(btnDropCand);
        buttonPanel.add(btnRegVoter);
        buttonPanel.add(btnVote);
        buttonPanel.add(btnResult);
        add(buttonPanel, BorderLayout.SOUTH);

        resultArea = new JTextArea(15, 50);
        resultArea.setFont(new Font("Calibri Light", Font.BOLD, 18));
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        setVisible(true);
    }
}

