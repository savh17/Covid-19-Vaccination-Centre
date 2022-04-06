package Task2;

import java.util.Scanner;

public class Booth {
    private String patientName;
    private static Scanner input = new Scanner(System.in);


    Booth(String patientName) {
        this.patientName = patientName;
    }


    public String getName() {
        return this.patientName;
    }
}
