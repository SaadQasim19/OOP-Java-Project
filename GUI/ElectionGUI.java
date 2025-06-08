package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

// ===== Abstract Person class =====
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

// ===== Candidate class =====
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
}

// ===== Voter class =====
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
        return canVote();
    }

    public boolean register() {
        return canVote();
    }

    public boolean canVote() {
        return age >= 18 && age <= 60 && isMentallyStable;
    }
}

// ===== PoliticalParty class =====
class PoliticalParty {
    String name, symbol;

    PoliticalParty(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }
}

// ===== PollingStation class =====
class PollingStation {
    int stationNumber;
    String location;

    PollingStation(int number, String location) {
        this.stationNumber = number;
        this.location = location;
    }
}

// ===== Complaint class =====
class Complaint {
    String name, issue;

    Complaint(String name, String issue) {
        this.name = name;
        this.issue = issue;
    }

    void showComplaint() {
        System.out.println("Complaint from " + name + ": " + issue);
    }
}

// ===== ResultDisplay class =====
class ResultDisplay {
    ArrayList<Candidate> candidates;
    int[] votes;

    ResultDisplay(ArrayList<Candidate> candidates, int[] votes) {
        this.candidates = candidates;
        this.votes = votes;
    }

    public void displayResultsConsole() {
        System.out.println("\nElection Results:");
        int[] copy = votes.clone();
        for (int i = 0; i < candidates.size(); i++) {
            int maxIndex = 0;
            for (int j = 0; j < copy.length; j++) {
                if (copy[j] > copy[maxIndex]) maxIndex = j;
            }
            Candidate c = candidates.get(maxIndex);
            System.out.println((i + 1) + ". " + c.name + " (" + c.party.name + ") - " + copy[maxIndex] + " votes");
            copy[maxIndex] = -1;
        }
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

    public void saveResultsToFile(String filename) {
        try (FileWriter fw = new FileWriter(filename)) {
            fw.write(getResultsText());
        } catch (IOException e) {
            System.out.println("Error saving results: " + e.getMessage());
        }
    }
}

// ===== ElectionCommission class =====
class ElectionCommission {
    String head, electionType;

    ElectionCommission(String head, String type) {
        this.head = head;
        this.electionType = type;
    }

    void showDetails() {
        System.out.println("Election Type: " + electionType);
        System.out.println("Election Head: " + head);
    }
}

// ===== Main GUI Class =====
public class ElectionGUI extends JFrame {
    ArrayList<Candidate> candidates = new ArrayList<>();
    ArrayList<Voter> voters = new ArrayList<>();
    int[] votes;

    JTextArea resultArea;

    ElectionGUI() {
        setTitle("Election Management System");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        ElectionCommission ec = new ElectionCommission("Election Commission Of Pakistan", "National");
        ec.showDetails();

        JPanel panel = new JPanel();
        JButton regCand = new JButton("Register Candidate");
        JButton regVoter = new JButton("Register Voter");
        JButton startVote = new JButton("Start Voting");
        JButton results = new JButton("Show Results");

        panel.add(regCand);
        panel.add(regVoter);
        panel.add(startVote);
        panel.add(results);

        resultArea = new JTextArea(15, 50);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        regCand.addActionListener(e -> registerCandidate());
        regVoter.addActionListener(e -> registerVoter());
        startVote.addActionListener(e -> vote());
        results.addActionListener(e -> displayResults());

        setVisible(true);
    }

    void registerCandidate() {
        String name = JOptionPane.showInputDialog("Candidate Name:");
        int age = Integer.parseInt(JOptionPane.showInputDialog("Age:"));
        String nationality = JOptionPane.showInputDialog("Nationality:");
        boolean dual = JOptionPane.showConfirmDialog(null, "Dual Nationality?", "Info", JOptionPane.YES_NO_OPTION) == 0;
        boolean ahmadi = JOptionPane.showConfirmDialog(null, "Is Ahmadi?", "Info", JOptionPane.YES_NO_OPTION) == 0;
        boolean assets = JOptionPane.showConfirmDialog(null, "Declared Assets?", "Info", JOptionPane.YES_NO_OPTION) == 0;

        PoliticalParty party = new PoliticalParty("Party A", "Star"); // for simplicity
        Candidate c = new Candidate(name, age, nationality, dual, ahmadi, assets, party);
        if (c.register()) {
            candidates.add(c);
            JOptionPane.showMessageDialog(null, "Candidate Registered.");
        } else {
            JOptionPane.showMessageDialog(null, "Not Eligible.");
        }
    }

    void registerVoter() {
        String name = JOptionPane.showInputDialog("Voter Name:");
        int age = Integer.parseInt(JOptionPane.showInputDialog("Age:"));
        String nationality = JOptionPane.showInputDialog("Nationality:");
        boolean stable = JOptionPane.showConfirmDialog(null, "Mentally Stable?", "Info", JOptionPane.YES_NO_OPTION) == 0;

        PollingStation ps = new PollingStation(1, "Station A");
        Voter v = new Voter(name, age, nationality, stable, ps);
        if (v.register()) {
            voters.add(v);
            JOptionPane.showMessageDialog(null, "Voter Registered.");
        } else {
            JOptionPane.showMessageDialog(null, "Not Eligible.");
        }
    }

    void vote() {
        if (candidates.isEmpty() || voters.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Need at least one candidate and voter.");
            return;
        }
        votes = new int[candidates.size()];
        for (Voter v : voters) {
            String[] names = candidates.stream().map(c -> c.name).toArray(String[]::new);
            int choice = JOptionPane.showOptionDialog(null, v.name + ", cast your vote:", "Voting",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, names, names[0]);
            if (choice >= 0) {
                votes[choice]++;
            }
        }
    }

    void displayResults() {
        ResultDisplay rd = new ResultDisplay(candidates, votes);
        rd.displayResultsConsole();
        resultArea.setText(rd.getResultsText());
        rd.saveResultsToFile("Results.txt");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ElectionGUI::new);
    }
}
