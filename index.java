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



public class index {

    public static void main(String[] args) {
        System.out.println("Election Management System");
        
    }
}