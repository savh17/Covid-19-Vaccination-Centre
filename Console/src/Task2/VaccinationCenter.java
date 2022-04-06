package Task2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class VaccinationCenter {
    static final String FILE_PATH = "data/task2_data/patient_data.txt";
    static Scanner input = new Scanner(System.in);
    static Booth[] serviceCenter = new Booth[6];
    static  int vaccineStock = 150;
    static boolean isRunning = true;

    public static void main(String[] args) {

        while (isRunning) {
            displayConsoleMenu();
            String operator = input.next().toUpperCase();
            executeOperation(operator);
        }
    }

    private static void displayConsoleMenu() {
        System.out.println("""
                                
                100 or VVB: View all Vaccination Booths
                101 or VEB: View all Empty Booths
                102 or APB: Add Patient to a Booth
                103 or RPB: Remove Patient from a Booth
                104 or VPS: View Patients Sorted in alphabetical order
                105 or SPD: Store Program Data into file
                106 or LPD: Load Program Data from file
                107 or VRV: View Remaining Vaccinations
                108 or AVS: Add Vaccinations to the Stock
                999 or EXT: Exit the Program""");
    }

    public static void executeOperation(String operator) {
        switch (operator) {
            case "100", "VVB" -> viewAllVaccinationBooths();
            case "101", "VEB" -> viewAllEmptyBooths();
            case "102", "APB" -> addPatientToBooth();
            case "103", "RPB" -> removePatientFromBooth();
            case "104", "VPS" -> viewPatientSorted();
            case "105", "SPD" -> storeProgramData();
            case "106", "LPD" -> loadProgramData();
            case "107", "VRV" -> viewRemainingVaccinations();
            case "108", "AVS" -> addVaccinationsToStock();
            case "999", "EXT" -> {
                System.out.println("Exiting ...");
                isRunning = false;
            }
            default -> System.out.println("Oops, invalid Command!!!\nPlease enter a valid command.");
        }
    }

    public static void viewAllVaccinationBooths() {
        short boothNum = 1;
        for (Booth booth:serviceCenter) {
            if (booth == null) {
                System.out.println("booth " + (boothNum++) + " is empty.");
                continue;
            }
            System.out.println("booth " + (boothNum++) + " occupied by " + booth.getName());
        }
    }

    public static void viewAllEmptyBooths() {
        short boothNum = 1;
        for (Booth booth:serviceCenter) {
            if (booth == null) {
                System.out.println("booth " + boothNum + " is empty.");
            }
            boothNum ++;
        }
    }

    public static void addPatientToBooth() {
        if (vaccineStock > 0) {
            System.out.print("\nEnter booth number (1-6) or 7 to go back to main menu: ");
            try {
                int boothNum = input.nextInt();

                if (boothNum == 7) {
                    System.out.println("Returning to main menu ...");
                }
                else if (boothNum <= 0 || boothNum > 7) {
                    System.out.printf("%s is an invalid booth number.\nPlease enter a valid booth number (1-6).\n", boothNum);
                    addPatientToBooth();
                }
                else {
                    if (serviceCenter[boothNum - 1] == null) {
                        System.out.printf("Enter patient name for booth %s: ", boothNum);
                        String patientName = input.next().toLowerCase();
                        Booth newPatient = new Booth(patientName);
                        serviceCenter[boothNum - 1] = newPatient;
                        vaccineStock --;
                        System.out.println("Patient added successfully");

                        if (vaccineStock <= 20) {
                            System.out.printf("""
                        WARNING!!!
                        Currently we have only %s more vaccines.""", vaccineStock
                            );
                        }

                    }
                    else {
                        System.out.printf("Sorry Booth number %s is not available at the moment." +
                                "\nPlease enter another number or try again later.\n", boothNum);
                    }
                    addPatientToBooth();
                }
            }
            catch (InputMismatchException e) {
                input.nextLine();
                System.out.println("Booth number should be a numeric value.\nPlease enter a numeric value and try again");
                addPatientToBooth();
            }
        }
        else {
            System.out.println("\nOops! Sorry, We are running out of vaccines!!!\nTry again later.");
        }
    }

    public static void removePatientFromBooth() {
        // checks whether the booth array is empty
        if (isBoothArrayEmpty()) {
            System.out.println("Sorry, At the moment there are no patients to remove.");
        }
        else {
            System.out.print("Enter the name of patient you wish to remove or -1 to go back to the main menu: ");
            String patientNameToRemove = input.next().toLowerCase();

            // returning to main menu
            if (patientNameToRemove.equals("-1")) {
                System.out.println("Returning to main menu ...");
                return;
            }

            for (int i = 0; i<serviceCenter.length; i++) {
                if (serviceCenter[i] == null) {
                    continue;
                }
                // if name matches setting that position to NULL
                if (serviceCenter[i].getName().equals(patientNameToRemove)) {
                    serviceCenter[i] = null;
                    System.out.println("Patient removed successfully.");
                    return;
                }
            }
            // show this message only if name does not match with any name in booth array
            System.out.printf("Sorry there's no one called \"%s\" in the booth at the moment.\nPlease try again.\n\n", patientNameToRemove);
            removePatientFromBooth();
        }
    }

    public static void viewPatientSorted() {
        // collecting all patient names into an array
        ArrayList<String> patientsNames = new ArrayList<>();
        for (Booth booth:serviceCenter) {
            if (booth == null) {
                continue;
            }
            patientsNames.add(booth.getName());
        }

        if (patientsNames.isEmpty()) {
            System.out.println("Sorry, no patients found in booth to sort.");
        }
        else {
            sort(patientsNames);
            System.out.println(patientsNames);
        }
    }

    public static void storeProgramData() {
        String data = Integer.toString(vaccineStock);
        // creating a data string
        for (Booth booth:serviceCenter) {
            try {
                data += "\n" + booth.getName();
            }
            catch (NullPointerException e) {
                data += "\n" + "NULL";
            }
        }
        // writing generated data string to file
        writeToFile(FILE_PATH, data);
    }

    public static void loadProgramData() {
        // reading data file and adding data to an array
        String[] dataArray = readFile(FILE_PATH).split("\n");

        if (dataArray.length > 0) {
            // restoring vaccine stock
            vaccineStock = Integer.parseInt(dataArray[0]);

            // restoring patient data
            for (int i=1; i< dataArray.length; i++) {
                String patientName = dataArray[i];
                if (patientName.equals("NULL")) {
                    serviceCenter[i-1] = null;
                }
                else {
                    serviceCenter[i-1] = new Booth(patientName);
                }
            }
            System.out.println("Data loaded successfully!");
        }
        else {
            System.out.println("Oops, Data file is empty or not existing. We couldn't load any data.");
        }
    }

    public static void viewRemainingVaccinations() {
        System.out.printf("Currently we have %s vaccine(s) left in the stock!\n", vaccineStock);
    }

    public static void addVaccinationsToStock() {
        System.out.print("Enter the amount of new arrival vaccines: ");
        try {
            int amount = input.nextInt();

            if (amount >= 0) {
                vaccineStock += amount;
                System.out.printf("%s new vaccines added to the stock.", amount);
                System.out.printf("\nCurrently we have %s vaccines in the stock.\n", vaccineStock);
            }
            else {
                System.out.println("Amount of vaccines can't be less than zero.\nTry again.\n");
                addVaccinationsToStock();
            }
        }
        catch (InputMismatchException e) {
            input.nextLine();
            System.out.println("The amount of vaccines should be a numeric value." +
                    "\nPlease enter a numeric value and try again.\n");
            addVaccinationsToStock();
        }
    }

    public static boolean isBoothArrayEmpty() {
        for (Booth booth:serviceCenter) {
            if (booth == null) {
                continue;
            }
            return false;
        }
        return true;
    }


    private static void sort(ArrayList<String> array) {
        boolean isSorted;
        int comparisonResult;
        for (int i=0; i<array.size(); i++) {
            // setting IsSorted to TRUE, assuming that the array is sorted to reduce the number of unnecessary iterations
            isSorted = true;
            for (int j=1; j<array.size()-i; j++) {
                comparisonResult = array.get(j).compareToIgnoreCase(array.get(j-1));

                if (comparisonResult < 0) {
                    // swapping
                    String temp = array.get(j);
                    array.set(j, array.get(j-1));
                    array.set(j-1, temp);
                    // if swapping occurs that means, the array was not sorted. So I set IsSorted to FALSE
                    isSorted = false;
                }
            }
            if (isSorted) {
                return;
            }
        }
    }

    public static void writeToFile(String filePath, String data) {
        try {
            // creating a file
            File patientFile = new File(filePath);
            if (patientFile.createNewFile()) {
                System.out.println("File created: " + patientFile.getName());
            }

            try {
                // writing to file
                FileWriter myWriter = new FileWriter(filePath);
                myWriter.write(data);
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            }
            catch (IOException e) {
                // show this error message if writing to was unsuccessful
                System.out.println("An error occurred when writing to the file.");
            }
        }
        catch (IOException e) {
            // show this error message if creating the file was unsuccessful
            System.out.println("An error occurred when creating the file.");
        }
    }

    public static String readFile(String filePath) {
        try {
            // creating an file object
            File patientFile = new File(filePath);
            Scanner myReader = new Scanner(patientFile);
            String data = "";

            // reading file
            while (myReader.hasNextLine()) {
                data += myReader.nextLine() + "\n";
            }
            myReader.close();
            return data;
        }
        catch (FileNotFoundException e) {
            // return an empty string if file was not found
            return "";
        }
    }
}
