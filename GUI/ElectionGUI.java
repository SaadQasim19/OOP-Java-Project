package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

//* ============ Interface for Registerable entities =============
interface Registrable {
    boolean register();
    String toStoreStringFile();
}

// * ============ Abstract Person class =================
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

// * ================== Candidate class ====================
class Candidate extends Person implements Registrable {
    boolean hasDualNationality, isAhmadi, hasDeclaredAssets;
    PoliticalParty party;

    Candidate(String name, int age, String nationality, boolean dual, boolean ahmadi, boolean assets, PoliticalParty party) {
        super(name, age, nationality);
        hasDualNationality = dual;
        isAhmadi = ahmadi;
        hasDeclaredAssets = assets;
        this.party = party;
    }

    @Override
    boolean isEligible() {
        return age >= 25 && age <= 40 && nationality.equalsIgnoreCase("pakistani") && !isAhmadi && hasDeclaredAssets;
    }

    @Override
    public boolean register() {
        return isEligible();
    }

    @Override
    public String toStoreStringFile() {
        return "Name: " + name + "\n" + "Age: " + age + "\n" + "Nationality: " + nationality + "\n" + "Party: " + party.name;
    }
}

// * ===== Voter class =====
class Voter extends Person implements Registrable {
    boolean isMentallyStable;
    PollingStation station;

    Voter(String name, int age, String nationality, boolean stable, PollingStation station) {
        super(name, age, nationality);
        isMentallyStable = stable;
        this.station = station;
    }

    @Override
    boolean isEligible() {
        return age >= 18 && age <= 60 && isMentallyStable;
    }

    @Override
    public boolean register() {
        return isEligible();
    }

    @Override
    public String toStoreStringFile() {
        return "Name: " + name + "\n" + "Age: " + age + "\n" + "Nationality: " + nationality + "\n" + "Polling Station: " + station.location;
    }
}

// * ===== PoliticalParty class =====
class PoliticalParty {
    String name, symbol;

    PoliticalParty(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }
}

// * ===== PollingStation class =====
class PollingStation {
    int stationNumber;
    String location;

    PollingStation(int number, String location) {
        this.stationNumber = number;
        this.location = location;
    }
}

// * ===== ResultDisplay class =====
class ResultDisplay {
    ArrayList<Candidate> candidates;
    int[] votes;

    ResultDisplay(ArrayList<Candidate> candidates, int[] votes) {
        this.candidates = candidates;
        this.votes = votes;
    }

    public String getResultsText() {
        String result = "Election Results:\n";
        int[] copy = votes.clone();
        for (int i = 0; i < candidates.size(); i++) {
            int maxIndex = 0;
            for (int j = 0; j < copy.length; j++) {
                if (copy[j] > copy[maxIndex])
                    maxIndex = j;
            }
            Candidate c = candidates.get(maxIndex);
            result += (i + 1) + ". " + c.name + " (" + c.party.name + ") - " + copy[maxIndex] + " votes\n";
            copy[maxIndex] = -1;
        }
        return result;
    }

    public void saveToFile(String filePath) {
        try (FileWriter fw = new FileWriter(filePath, true)) {
            fw.write(getResultsText() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// * ===== ElectionCommission class =========
class ElectionCommission {
    String head, electionType;

    ElectionCommission(String head, String type) {
        this.head = head;
        this.electionType = type;
    }

    void showDetails() {
        System.out.println("==== Welcome to Election Management System ====");
        System.out.println("Election Type: " + electionType);
        System.out.println("Election Head: " + head);
    }
}

// * ====================== Main GUI Class  ===========================

public class ElectionGUI extends JFrame {
    ArrayList<Candidate> candidates = new ArrayList<>();
    ArrayList<Voter> voters = new ArrayList<>();
    int[] votes;
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
        btnRegCand.setBackground(new Color(52, 152, 219));

        JButton btnDropCand = new JButton("Drop Candidate");
        btnDropCand.setBackground(new Color(231, 76, 60));

        JButton btnRegVoter = new JButton("Register Voter");
        btnRegVoter.setBackground(new Color(52, 152, 219));

        JButton btnVote = new JButton("Start Voting");
        btnVote.setBackground(new Color(241, 196, 15));

        JButton btnResult = new JButton("Show Results");
        btnResult.setBackground(new Color(46, 204, 113));

        buttonPanel.add(btnRegCand);
        buttonPanel.add(btnDropCand);
        buttonPanel.add(btnRegVoter);
        buttonPanel.add(btnVote);
        buttonPanel.add(btnResult);
        add(buttonPanel, BorderLayout.SOUTH);

        resultArea = new JTextArea(15, 50);
        resultArea.setFont(new Font("Calibri Light", Font.BOLD, 18));
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        btnRegCand.addActionListener(e -> registerCandidate());
        btnDropCand.addActionListener(e -> dropCandidate());
        btnRegVoter.addActionListener(e -> registerVoter());
        btnVote.addActionListener(e -> vote());
        btnResult.addActionListener(e -> displayResults());

        setVisible(true);
    }

    public void registerCandidate() {
        try {
            String name = JOptionPane.showInputDialog("Candidate Name:");
            int age = Integer.parseInt(JOptionPane.showInputDialog("Age:"));
            String nationality = JOptionPane.showInputDialog("Nationality:");

            int dual = JOptionPane.showConfirmDialog(null, "Has Dual Nationality?");
            if (dual == JOptionPane.YES_OPTION) {
                int drop = JOptionPane.showConfirmDialog(null, "Drop second nationality?");
                if (drop != JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "Candidate not registered.");
                    return;
                }
            }

            boolean ahmadi = JOptionPane.showConfirmDialog(null, "Is Ahmadi?") == 0;
            boolean assets = JOptionPane.showConfirmDialog(null, "Declared Assets?") == 0;

            String partyName = JOptionPane.showInputDialog("Party Name:");
            String symbol = JOptionPane.showInputDialog("Party Symbol:");
            PoliticalParty party = new PoliticalParty(partyName, symbol);

            Candidate c = new Candidate(name, age, nationality, false, ahmadi, assets, party);
            if (c.register()) {
                candidates.add(c);
                saveToFile("candidates.txt", c.toStoreStringFile());
                JOptionPane.showMessageDialog(null, "Candidate Registered.");
            } else {
                JOptionPane.showMessageDialog(null, "Not Eligible.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid input.");
        }
    }

    public void dropCandidate() {
        if (candidates.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No candidates.");
            return;
        }

        String[] candList = new String[candidates.size()];
        for (int i = 0; i < candidates.size(); i++) {
            candList[i] = candidates.get(i).name;
        }

        String selected = (String) JOptionPane.showInputDialog(null, "Select to drop:", "Drop",
                JOptionPane.PLAIN_MESSAGE, null, candList, candList[0]);

        if (selected != null) {
            for (int i = 0; i < candidates.size(); i++) {
                if (candidates.get(i).name.equals(selected)) {
                    candidates.remove(i);
                    JOptionPane.showMessageDialog(null, "Candidate Dropped.");
                    break;
                }
            }
        }
    }

    public void registerVoter() {
        try {
            String name = JOptionPane.showInputDialog("Voter Name:");
            int age = Integer.parseInt(JOptionPane.showInputDialog("Age:"));
            String nationality = JOptionPane.showInputDialog("Nationality:");
            boolean stable = JOptionPane.showConfirmDialog(null, "Mentally Stable?") == 0;

            String[] stations = { "Station A", "Station B", "Station C" };
            String station = (String) JOptionPane.showInputDialog(null, "Polling Station:", "Select",
                    JOptionPane.PLAIN_MESSAGE, null, stations, stations[0]);

            PollingStation ps = new PollingStation(new Random().nextInt(1000), station);
            Voter v = new Voter(name, age, nationality, stable, ps);

            if (v.register()) {
                voters.add(v);
                saveToFile("voters.txt", v.toStoreStringFile());
                JOptionPane.showMessageDialog(null, "Voter Registered.");
            } else {
                JOptionPane.showMessageDialog(null, "Not Eligible.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid input.");
        }
    }

    public void vote() {
        if (candidates.size() == 0 || voters.size() == 0) {
            JOptionPane.showMessageDialog(null, "Need candidates and voters.");
            return;
        }

        votes = new int[candidates.size()];
        for (int i = 0; i < voters.size(); i++) {
            Voter v = voters.get(i);
            String[] candList = new String[candidates.size()];
            for (int j = 0; j < candidates.size(); j++) {
                candList[j] = candidates.get(j).name;
            }

            int choice = JOptionPane.showOptionDialog(null, v.name + ", cast your vote:", "Vote",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, candList, candList[0]);

            if (choice >= 0) {
                votes[choice]++;
            }

            int complaint = JOptionPane.showConfirmDialog(null, "Want to file a complaint?");
            if (complaint == JOptionPane.YES_OPTION) {
                String text = JOptionPane.showInputDialog("Enter complaint:");
                saveToFile("complaints.txt", "From: " + v.name + " | Station: " + v.station.location + " | " + text);
                JOptionPane.showMessageDialog(null, "Complaint recorded.");
            }
        }
    }

    public void displayResults() {
        if (votes == null) {
            JOptionPane.showMessageDialog(null, "No votes yet.");
            return;
        }

        String result = "----- ELECTION RESULTS -----\n";

        int maxVotes = -1;
        String winner = "";

        for (int i = 0; i < candidates.size(); i++) {
            Candidate c = candidates.get(i);
            result += c.name + " (" + c.party.name + ") - " + votes[i] + " votes\n";

            if (votes[i] > maxVotes) {
                maxVotes = votes[i];
                winner = c.name;
            }
        }

        result += "\nWinner: " + winner + " with " + maxVotes + " votes.";
        resultArea.setText(result);
        saveToFile("results.txt", result);
    }

    void saveToFile(String fileName, String content) {
        try (FileWriter fw = new FileWriter(fileName, true)) {
            fw.write(content + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String[] types = { "National", "Provincial" };
        String type = (String) JOptionPane.showInputDialog(null, "Select Election Type:", "Setup",
                JOptionPane.PLAIN_MESSAGE, null, types, types[0]);
        if (type != null) {
            new ElectionGUI(type);
        }
    }
}
