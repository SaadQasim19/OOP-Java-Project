import java.util.*;

// ^For file handling
// import java.io.*;  

abstract class Person {

    // & Encapsulated data members.

    String name, nationality;
    int age;

    Person(String name, int age, String nationality) {
        this.name = name;
        this.age = age;
        this.nationality = nationality;
    }

    // & Abstract method to be implemented-overriden by subclasses
    abstract boolean isEligible();
}

class Candidate extends Person {
    boolean hasDualNationality, isAhmadi, hasDeclaredAssets;

    // & Composition
    PoliticalParty party;

    Candidate(String name, int age, String nationality, boolean dual, boolean ahmadi, boolean assets,
            PoliticalParty party) {
        // & Parent Constructor
        super(name, age, nationality);
        hasDualNationality = dual;
        isAhmadi = ahmadi;
        hasDeclaredAssets = assets;
        this.party = party;
    }

    @Override
    boolean isEligible() {
        if (age < 25 || age > 40)
            return false;
        if (!nationality.equalsIgnoreCase("pakistani"))
            return false;
        if (hasDualNationality) {
            System.out.print(name + ", you have dual nationality. Do you want to drop it? (yes/no): ");
            Scanner keyboard = new Scanner(System.in);
            String response = keyboard.nextLine();
            if (response.equalsIgnoreCase("yes")) {
                hasDualNationality = false; 
            } else {
                return false;
            }
        }
        if (isAhmadi)
            return false;
        if (!hasDeclaredAssets)
            return false;
        return true;
    }

    public boolean register() {
        return isEligible();
    }

    public boolean canVote() {
        return isEligible();
    }
    
}

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

class PoliticalParty {
    String name, symbol;

    PoliticalParty(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }
}

class ElectionCommission {
    String head, electionType;

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
    String name, issue;

    Complaint(String name, String issue) {
        this.name = name;
        this.issue = issue;
    }

    void showComplaint() {
        System.out.println("Complaint from " + name + ": " + issue);
    }
}

class ResultDisplay {
    ArrayList<Candidate> candidates;
    int[] votes;

    ResultDisplay(ArrayList<Candidate> candidates, int[] votes) {
        this.candidates = candidates;
        this.votes = votes;
    }

    public void displayResults() {
        System.out.println("\nElection Results:");
        int[] votesCopy = votes.clone();
        for (int i = 0; i < candidates.size(); i++) {
            int maxIndex = 0;
            for (int j = 0; j < votesCopy.length; j++) {
                if (votesCopy[j] > votesCopy[maxIndex]) {
                    maxIndex = j;
                }
            }
            Candidate c = candidates.get(maxIndex);
            System.out.println((i + 1) + ". " + c.name + " (" + c.party.name + ") - " + votesCopy[maxIndex] + " votes");
            votesCopy[maxIndex] = -1;
        }
    }
    
}

public class index {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Election Management System");

        ElectionCommission ec = new ElectionCommission("Election Commission Of Pakistan", "National");
        ec.showElectionDetails();

        PoliticalParty p1 = new PoliticalParty("Party A", "Star");
        PoliticalParty p2 = new PoliticalParty("Party B", "Moon");

        PollingStation ps1 = new PollingStation(1, "Station 1 - Lahore");
        PollingStation ps2 = new PollingStation(2, "Station 2 - Karachi");

        ArrayList<Person> allPeople = new ArrayList<>();

        for (int i = 0; i <= 2; i++) {
            System.out.println("\nRegister Candidate " + (i + 1));
            try {
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
                allPeople.add(c);
                if (c.register()) {
                    System.out.println(name + " is registered.");
                } else {
                    System.out.println(name + " is not eligible.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter the correct data type.");
                keyboard.nextLine();
                i--;
            }
        }

        for (int i = 0; i <= 2; i++) {
            System.out.println("\nRegister Voter " + (i + 1));
            try {
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
                allPeople.add(v);
                if (v.register()) {
                    System.out.println(name + " is registered at " + assignedStation.location);
                } else {
                    System.out.println(name + " is not eligible.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter the correct data type.");
                keyboard.nextLine();
                i--;
            }
        }

        ArrayList<Candidate> validCandidates = new ArrayList<>();
        ArrayList<Voter> validVoters = new ArrayList<>();
        for (Person p : allPeople) {
            if (p instanceof Candidate c && c.isEligible()) {
                validCandidates.add(c);
            } else if (p instanceof Voter v && v.canVote()) {
                validVoters.add(v);
            }
        }

        int[] votes = new int[validCandidates.size()];
        for (Voter voter : validVoters) {
            System.out.println("\n" + voter.name + ", vote for:");
            for (int j = 0; j < validCandidates.size(); j++) {
                System.out.println(
                        (j + 1) + ". " + validCandidates.get(j).name + " (" + validCandidates.get(j).party.name + ")");
            }
            try {
                int choice = keyboard.nextInt();
                if (choice > 0 && choice <= validCandidates.size()) {
                    votes[choice - 1]++;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid vote input. Skipping vote.");
                keyboard.nextLine();
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

        ResultDisplay resultDisplay = new ResultDisplay(validCandidates, votes);
        resultDisplay.displayResults();

//* -------------------- FILE HANDLING -----------------------------



        keyboard.close();
    }
}
