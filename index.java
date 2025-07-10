import java.util.*;
abstract class Person{
    String name, nationality;
    int age;

    Person(String name, int age, String nationality) {
        this.name = name;
        this.age = age;
        this.nationality = nationality;
    }

    abstract boolean isEligible();
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

    PollingStation(int stationNumber, String location) {
        this.stationNumber = stationNumber;
        this.location = location;
    }
}
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
        return age >= 18 && age <= 60 && isMentallyStable;
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
//* ArrayList to Register Candidate */
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
        keyboard.close();
    }
}
