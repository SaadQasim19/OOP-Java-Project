package Gui;
import javax.swing.*;
import java.awt.*;

interface Registrable {
    boolean register();
    String toStoreStringFile();
}

abstract class Person {
    String name, nationality;
    int age;
    Person(String name, int age, String nationality) {
        this.name = name;
        this.age = age;
        this.nationality = nationality;
    }
    abstract boolean isEligible();
}

class Candidate extends Person implements Registrable {
    boolean hasDualNationality, isAhmadi, hasDeclaredAssets;
    PoliticalParty party;
    Candidate(String name, int age, String nationality, boolean dual, boolean ahmadi, boolean assets, PoliticalParty party) {
        super(name, age, nationality);
        this.hasDualNationality = dual;
        this.isAhmadi = ahmadi;
        this.hasDeclaredAssets = assets;
        this.party = party;
    }
    public boolean register() {
        return isEligible();
    }
    public boolean isEligible() {
        return age >= 25 && age <= 40 && nationality.equalsIgnoreCase("pakistani") && !isAhmadi && hasDeclaredAssets;
    }
    public String toStoreStringFile() {
        return "Name: " + name + "\nAge: " + age + "\nNationality: " + nationality + "\nParty: " + party.name;
    }
}

class Voter extends Person implements Registrable {
    boolean isMentallyStable;
    PollingStation station;
    Voter(String name, int age, String nationality, boolean stable, PollingStation station) {
        super(name, age, nationality);
        this.isMentallyStable = stable;
        this.station = station;
    }
    public boolean register() {
        return isEligible();
    }
    public boolean isEligible() {
        return age >= 18 && age <= 60 && isMentallyStable;
    }
    public String toStoreStringFile() {
        return "Name: " + name + "\nAge: " + age + "\nNationality: " + nationality + "\nPolling Station: " + station.location;
    }
}

class PoliticalParty {
    String name, symbol;
    PoliticalParty(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }
}

class PollingStation {
    int stationNumber;
    String location;
    PollingStation(int num, String loc) {
        this.stationNumber = num;
        this.location = loc;
    }
}


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

