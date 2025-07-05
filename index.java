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




public class index {

    public static void main(String[] args) {
        System.out.println("Election Management System");
        
    }
}