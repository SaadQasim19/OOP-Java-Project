package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

//* ===== Abstract Person class =====
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

//* ===== Candidate class =====
class Candidate extends Person {
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

    public boolean register() {
        return isEligible();
    }

    public String toFileString() {
        return name + "," + age + "," + nationality + "," + party.name;
    }
}

//* ===== Voter class =====
class Voter extends Person {
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

    public boolean register() {
        return isEligible();
    }

    public String toFileString() {
        return name + "," + age + "," + nationality + "," + station.location;
    }
}

//* ===== PoliticalParty class =====
class PoliticalParty {
    String name, symbol;

    PoliticalParty(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }
}

//* ===== PollingStation class =====
class PollingStation {
    int stationNumber;
    String location;

    PollingStation(int number, String location) {
        this.stationNumber = number;
        this.location = location;
    }
}

//* ===== ResultDisplay class =====
class ResultDisplay {
    ArrayList<Candidate> candidates;
    int[] votes;

    ResultDisplay(ArrayList<Candidate> candidates, int[] votes) {
        this.candidates = candidates;
        this.votes = votes;
    }

    public String getResultsText() {
        StringBuilder sb = new StringBuilder("Election Results:\n");
        int[] copy = votes.clone();
        for (int i = 0; i < candidates.size(); i++) {
            int maxIndex = 0;
            for (int j = 0; j < copy.length; j++) {
                if (copy[j] > copy[maxIndex]) maxIndex = j;
            }
            Candidate c = candidates.get(maxIndex);
            sb.append((i + 1)).append(". ").append(c.name).append(" (").append(c.party.name).append(") - ").append(copy[maxIndex]).append(" votes\n");
            copy[maxIndex] = -1;
        }
        return sb.toString();
    }

    public void saveToFile(String filePath) {
        try (FileWriter fw = new FileWriter(filePath, true)) {
            fw.write(getResultsText() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//* ===== ElectionCommission class =====
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

//* ===== Main GUI Class =====
public class ElectionGUI extends JFrame {
    ArrayList<Candidate> candidates = new ArrayList<>();
    ArrayList<Voter> voters = new ArrayList<>();
    int[] votes;
    JTextArea resultArea;
    String electionType;

    ElectionGUI(String type) {
        this.electionType = type;
        ElectionCommission ec = new ElectionCommission("Election Commission Of Pakistan", electionType);
        ec.showDetails();

        setTitle("Election Management System");
        setSize(700, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

//*  Create a title label
JLabel titleLabel = new JLabel("Election Management System - " + electionType, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);
        setLocationRelativeTo(null);
        setResizable(false);
        titleLabel.setForeground(Color.BLUE);
titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));


        JPanel panel = new JPanel();
        JButton regCand = new JButton("Register Candidate");
        JButton dropCand = new JButton("Drop Candidate");
        JButton regVoter = new JButton("Register Voter");
        JButton startVote = new JButton("Start Voting");
        JButton results = new JButton("Show Results");

        panel.add(regCand);
        panel.add(dropCand);
        panel.add(regVoter);
        panel.add(startVote);
        panel.add(results);

        resultArea = new JTextArea(15, 50);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        regCand.addActionListener(e -> registerCandidate());
        regVoter.addActionListener(e -> registerVoter());
        dropCand.addActionListener(e -> dropCandidate());
        startVote.addActionListener(e -> vote());
        results.addActionListener(e -> displayResults());

        setVisible(true);
    }

    void registerCandidate() {
        try {
            String name = JOptionPane.showInputDialog("Candidate Name:");
            int age = Integer.parseInt(JOptionPane.showInputDialog("Age:"));
            String nationality = JOptionPane.showInputDialog("Nationality:");
            boolean dual = JOptionPane.showConfirmDialog(null, "Dual Nationality?", "Info", JOptionPane.YES_NO_OPTION) == 0;

            if (dual) {
                int drop = JOptionPane.showConfirmDialog(null, "Candidate has dual nationality.\nDo you agree to drop the second nationality to proceed?", "Dual Nationality", JOptionPane.YES_NO_OPTION);
                if (drop == JOptionPane.YES_OPTION) {
                    dual = false;
                } else {
                    JOptionPane.showMessageDialog(null, "Candidate not registered due to dual nationality.");
                    return;
                }
            }

            boolean ahmadi = JOptionPane.showConfirmDialog(null, "Is Ahmadi?", "Info", JOptionPane.YES_NO_OPTION) == 0;
            boolean assets = JOptionPane.showConfirmDialog(null, "Declared Assets?", "Info", JOptionPane.YES_NO_OPTION) == 0;

            String partyName = JOptionPane.showInputDialog("Enter Political Party Name:");
            String symbol = JOptionPane.showInputDialog("Enter Party Symbol:");
            PoliticalParty party = new PoliticalParty(partyName, symbol);

            Candidate c = new Candidate(name, age, nationality, dual, ahmadi, assets, party);
            if (c.register()) {
                candidates.add(c);
                saveToFile("candidates.txt", c.toFileString());
                JOptionPane.showMessageDialog(null, "Candidate Registered.");
            } else {
                JOptionPane.showMessageDialog(null, "Not Eligible.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid Input.");
        }
    }

    void registerVoter() {
        try {
            String name = JOptionPane.showInputDialog("Voter Name:");
            int age = Integer.parseInt(JOptionPane.showInputDialog("Age:"));
            String nationality = JOptionPane.showInputDialog("Nationality:");
            boolean stable = JOptionPane.showConfirmDialog(null, "Mentally Stable?", "Info", JOptionPane.YES_NO_OPTION) == 0;

            String[] stations = {"Station A", "Station B", "Station C"};
            String selectedStation = (String) JOptionPane.showInputDialog(null, "Select Polling Station:", "Polling Station", JOptionPane.PLAIN_MESSAGE, null, stations, stations[0]);

            if (selectedStation == null) {
                JOptionPane.showMessageDialog(null, "No polling station selected.");
                return;
            }

            PollingStation ps = new PollingStation(new Random().nextInt(1000), selectedStation);
            Voter v = new Voter(name, age, nationality, stable, ps);

            if (v.register()) {
                voters.add(v);
                saveToFile("voters.txt", v.toFileString());
                JOptionPane.showMessageDialog(null, "Voter Registered.");
            } else {
                JOptionPane.showMessageDialog(null, "Not Eligible.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid Input.");
        }
    }

    void dropCandidate() {
        if (candidates.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No candidates to drop.");
            return;
        }
        String[] candNames = candidates.stream().map(c -> c.name).toArray(String[]::new);
        String selected = (String) JOptionPane.showInputDialog(null, "Select candidate to drop:", "Drop Candidate", JOptionPane.PLAIN_MESSAGE, null, candNames, candNames[0]);
        candidates.removeIf(c -> c.name.equals(selected));
        JOptionPane.showMessageDialog(null, "Candidate dropped from party.");
    }

    void vote() {
        if (candidates.isEmpty() || voters.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Need at least one candidate and voter.");
            return;
        }
        votes = new int[candidates.size()];
        for (Voter v : voters) {
            String[] names = candidates.stream().map(c -> c.name).toArray(String[]::new);
            int choice = JOptionPane.showOptionDialog(null, v.name + ", cast your vote:", "Voting", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, names, names[0]);
            if (choice >= 0) {
                votes[choice]++;
            }

            int comp = JOptionPane.showConfirmDialog(null, "Do you want to file a complaint?", "Complaint", JOptionPane.YES_NO_OPTION);
            if (comp == JOptionPane.YES_OPTION) {
                String complaint = JOptionPane.showInputDialog("Enter your complaint:");
                saveToFile("complaints.txt", "From: " + v.name + " | Polling Station: " + v.station.location + " | Complaint: " + complaint);
                JOptionPane.showMessageDialog(null, "Your complaint has been recorded.");
            }
        }
    }

    void displayResults() {
        if (votes == null) {
            JOptionPane.showMessageDialog(null, "No votes recorded yet.");
            return;
        }
        ResultDisplay rd = new ResultDisplay(candidates, votes);
        resultArea.setText(rd.getResultsText());
        rd.saveToFile("results.txt");
    }

    void saveToFile(String fileName, String content) {
        try (FileWriter fw = new FileWriter(fileName, true)) {
            fw.write(content + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String[] types = {"National", "Provincial"};
        String type = (String) JOptionPane.showInputDialog(null, "Select Election Type:", "Election Setup", JOptionPane.PLAIN_MESSAGE, null, types, types[0]);
        if (type != null) {
            SwingUtilities.invokeLater(() -> new ElectionGUI(type));
        }
    }
}
