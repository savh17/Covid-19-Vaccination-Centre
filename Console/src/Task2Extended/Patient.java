package Task2Extended;

public class Patient {
    private String firstName;
    private String surname;
    private int age;
    private String city;
    private String passportNumber;
    private String requestedVaccine;

    public Patient(String firstName, String surname, int age, String city, String passportNumber, String requestedVaccine) {
        this.firstName = firstName;
        this.surname = surname;
        this.age = age;
        this.city = city;
        this.passportNumber = passportNumber;
        this.requestedVaccine = requestedVaccine;
    }

    public String[] getPatientDetailList() {
        return new String[]{this.firstName, this.surname, Integer.toString(this.age), this.city, this.passportNumber, this.requestedVaccine};
    }

    public String getName() {
        return this.firstName;
    }


}
