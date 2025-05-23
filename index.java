
import java.util.*;

interface Registrable {
    boolean register();
}

interface Voteable {
    boolean canVote();
}

abstract class Person {
    String name, nationality;
    int age;

    Person(String name, int age, String nationality) {
        this.name = name;
        this.age = age;
        this.nationality = nationality;
    }

    // Abstract method to force subclasses to provide eligibility logic if needed
    abstract boolean isEligible();
}

class Candidate extends Person implements Registrable, Voteable {
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
        boolean ageValid = age >= 25 && age <= 40;
        boolean nationalityValid = nationality.equalsIgnoreCase("pakistani");
        return ageValid && nationalityValid && !hasDualNationality && !isAhmadi && hasDeclaredAssets;
    }

    @Override
    public boolean register() {
        return isEligible();
    }

    @Override
    public boolean canVote() {
        return isEligible();
    }
}

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
        return canVote();
    }

    @Override
    public boolean register() {
        return canVote();
    }

    public boolean canVote() {
        return age >= 18 && age <= 60 && isMentallyStable;
    }
}

class PoliticalParty {
    String name;
    String symbol;

    PoliticalParty(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }
}

class ElectionCommission {
    String head;
    String electionType;

    ElectionCommission(String head, String electionType) {
        this.head = head;
        this.electionType = electionType;
    }

    void showElectionDetails() {
        System.out.println("Election Type: " + electionType);
        System.out.println("Election Head: " + head);
    }
}

class PollingStation {
    int stationNumber;
    String location;

    PollingStation(int stationNumber, String location) {
        this.stationNumber = stationNumber;
        this.location = location;
    }
}

class Complaint {
    String name;
    String issue;

    Complaint(String name, String issue) {
        this.name = name;
        this.issue = issue;
    }

    void showComplaint() {
        System.out.println("Complaint from " + name + ": " + issue);
    }
}

class ResultDisplay {
    void displayResults(ArrayList<Candidate> candidates, int[] votes) {
        System.out.println("\nElection Results:");
        for (int i = 0; i < candidates.size(); i++) {
            int maxIndex = 0;
            for (int j = 0; j < votes.length; j++) {
                if (votes[j] > votes[maxIndex]) {
                    maxIndex = j;
                }
            }
            Candidate c = candidates.get(maxIndex);
            System.out.println((i + 1) + ". " + c.name + " (" + c.party.name + ") - " + votes[maxIndex] + " votes");
            votes[maxIndex] = -1; 
        }
    }
}

public class index {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        System.out.println("Election Management System");

        ElectionCommission ec = new ElectionCommission("Mr. Ali", "National");
        ec.showElectionDetails();

        PoliticalParty p1 = new PoliticalParty("Party A", "Star");
        PoliticalParty p2 = new PoliticalParty("Party B", "Moon");

        PollingStation ps1 = new PollingStation(1, "Station 1 - Lahore");
        PollingStation ps2 = new PollingStation(2, "Station 2 - Karachi");

        ArrayList<Candidate> allCandidates = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            System.out.println("\nRegister Candidate " + (i + 1));
            System.out.print("Name: ");
            String name = keyboard.nextLine();

            System.out.print("Age: ");
            int age = keyboard.nextInt();
            keyboard.nextLine();

            System.out.print("Nationality: ");
            String nationality = keyboard.nextLine();

            System.out.print("Dual nationality (true/false): ");
            boolean dualNat = keyboard.nextBoolean();

            System.out.print("Ahmadi/Qadyani (true/false): ");
            boolean isAhmadi = keyboard.nextBoolean();

            System.out.print("Declared assets (true/false): ");
            boolean assets = keyboard.nextBoolean();
            keyboard.nextLine();

            PoliticalParty party = (i % 2 == 0) ? p1 : p2;

            Candidate c = new Candidate(name, age, nationality, dualNat, isAhmadi, assets, party);
            allCandidates.add(c);

            if (c.register()) {
                System.out.println(name + " is registered.");
            } else {
                System.out.println(name + " is not eligible.");
            }
        }

        ArrayList<Voter> allVoters = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            System.out.println("\nRegister Voter " + (i + 1));
            System.out.print("Name: ");
            String name = keyboard.nextLine();

            System.out.print("Age: ");
            int age = keyboard.nextInt();
            keyboard.nextLine();

            System.out.print("Nationality: ");
            String nat = keyboard.nextLine();

            System.out.print("Mentally stable (true/false): ");
            boolean stable = keyboard.nextBoolean();
            keyboard.nextLine();

            PollingStation assignedStation = (i % 2 == 0) ? ps1 : ps2;

            Voter v = new Voter(name, age, nat, stable, assignedStation);
            allVoters.add(v);

            if (v.register()) {
                System.out.println(name + " is registered at " + assignedStation.location);
            } else {
                System.out.println(name + " is not eligible.");
            }
        }

        ArrayList<Candidate> validCandidates = new ArrayList<>();
        for (Candidate c : allCandidates) {
            if (c.isEligible()) {
                validCandidates.add(c);
            }
        }

        ArrayList<Voter> validVoters = new ArrayList<>();
        for (Voter v : allVoters) {
            if (v.canVote()) {
                validVoters.add(v);
            }
        }

        int[] votes = new int[validCandidates.size()];
        for (Voter voter : validVoters) {
            System.out.println("\n" + voter.name + ", vote for:");
            for (int j = 0; j < validCandidates.size(); j++) {
                System.out.println((j + 1) + ". " + validCandidates.get(j).name + " (" + validCandidates.get(j).party.name + ")");
            }

            int choice = keyboard.nextInt();
            if (choice > 0 && choice <= validCandidates.size()) {
                votes[choice - 1]++;
            }

            keyboard.nextLine();
            System.out.print("Do you want to file a complaint? (yes/no): ");
            String comp = keyboard.nextLine();
            if (comp.equalsIgnoreCase("yes")) {
                System.out.print("Enter your complaint: ");
                String issue = keyboard.nextLine();
                Complaint complaint = new Complaint(voter.name, issue);
                complaint.showComplaint();
            }
        }

        ResultDisplay resultDisplay = new ResultDisplay();
        resultDisplay.displayResults(validCandidates, votes);

        keyboard.close();
    }
}
