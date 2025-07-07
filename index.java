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
        return age >= 25 && age <= 40 && nationality.equalsIgnoreCase("pakistani") && !isAhmadi && hasDeclaredAssets;
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
        keyboard.close();
    }
}
