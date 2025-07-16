package Gui;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

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
     ArrayList<Candidate> candidates = new ArrayList<>();
    ArrayList<Voter> voters = new ArrayList<>();
int [] votes ;
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
//* ---------------- Register Candidate ------- */
    public void registerCandidate() {
        try {
            String name = JOptionPane.showInputDialog("Candidate Name:");
            int age = Integer.parseInt(JOptionPane.showInputDialog("Age:"));
            String nationality = JOptionPane.showInputDialog("Nationality:");
    
            int dual = JOptionPane.showConfirmDialog(null, "Has Dual Nationality?");
            if (dual == JOptionPane.YES_OPTION) {
                int drop = JOptionPane.showConfirmDialog(null, "Drop second nationality?");
                if (drop != JOptionPane.YES_OPTION) return;
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
//* ---------------- Drop Candidate ------- */
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

//* ---------------- Register Voter ------- */

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

//* ------------------------ Votes --------------------------------- */

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

}

