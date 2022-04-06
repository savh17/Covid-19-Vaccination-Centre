package Task1;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    static final String FILE_PATH = "data/task1_data/data.txt";
    static String[] ServiceCenter = new String[6];
    static int vaccineStock = 2;
    static boolean isRunning = true;
    static Scanner input = new Scanner(System.in);


    public static void main(String[] args) {
        initialise(ServiceCenter);

        while (isRunning) {
            displayConsoleMenu();
            String operator = input.next().toUpperCase();
            executeOperation(operator);
        }
    }

    public static void initialise(String[] hotelRef){
            for (int x = 0; x < 6; x++) {
                hotelRef[x] = "e";
            }
            System.out.println("initialised");
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
                999 or EXT: Exit the Program"""
        );
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
            case "999", "EXT" -> isRunning = false;
            default -> System.out.println("Oops, invalid Command!!!\nPleae enter a valid command.");
        }
    }

    private static void viewAllVaccinationBooths() {
        for (int x = 0; x < 6; x++) {
            if (ServiceCenter[x].equals("e")) {
                System.out.println("booth " + (x+1) + " is empty.");
                continue;
            }
            System.out.println("booth " + (x+1) + " occupied by " + ServiceCenter[x]);
        }
    }

    private static void viewAllEmptyBooths() {
        for (int x = 0; x < 6; x++) {
            if (ServiceCenter[x].equals("e")) {
                System.out.println("booth " + (x + 1) + " is empty.");
            }
        }
    }

    private static void addPatientToBooth() {
        if (vaccineStock > 0) {
            int boothNum;
            while (true) {
                System.out.print("Enter booth number (1-6) or 7 to go back to main menu: ");
                try {
                    boothNum = input.nextInt();
                    if (boothNum > 0 && boothNum <= 7) {
                        break;
                    }
                    System.out.println("Please enter a valid booth number (1-6).\n");
                }
                catch (InputMismatchException e) {
                    input.nextLine();
                    System.out.println("Booth number should be a numeric value." +
                            "\nPlease enter a numeric value and try again.\n");
                }
            }

            if (boothNum == 7) {
                System.out.println("Returning to main menu ...");
                return;
            }
            else {
                if (ServiceCenter[boothNum - 1].equals("e")) {
                    System.out.printf("Enter patient name for booth %s: ", boothNum);
                    String customerName = input.next().toLowerCase();
                    ServiceCenter[boothNum - 1] = customerName;
                    System.out.println("Patient added to booth successfully.\n");

                    vaccineStock --;

                    if (vaccineStock <= 20) {
                        System.out.printf("""
                        WARNING!!!
                        Currently we have only %s more vaccines.""", vaccineStock
                        );
                    }

                }
                else {
                    System.out.printf("Sorry Booth number %s is not available at the moment.", boothNum);
                    System.out.println("\nPlease enter another number or try again later.");
                }
                addPatientToBooth();

            }
        }
        else {
            System.out.println("Oops, We are running out of vaccines!!!\nTry again later.");
        }
    }

    private static void removePatientFromBooth() {
        System.out.print("Enter the name of patient you wish to remove: ");
        String patientName = input.next().toLowerCase();
        boolean nameNotFound = true;

        for (int i=0; i<ServiceCenter.length; i++) {
            String name = ServiceCenter[i];

            if (name.equals(patientName)) {
                ServiceCenter[i] = null;
                System.out.println("Patient removed successfully.");
                nameNotFound = false;
                break;
            }
        }

        if (nameNotFound) {
            System.out.println("Sorry there's no one called %s in the booth at the moment.\nPlease try again.");
            removePatientFromBooth();
        }
    }

    public static void viewPatientSorted() {
        int noPatients = 0;

        // counting number of patients
        for (int i=0; i<ServiceCenter.length; i++) {
            if (!(ServiceCenter[i].equals("e")))
                noPatients ++;
        }


        if (noPatients > 0) {
            String[] patientsNames = new String[noPatients];

            // appending names to the array
            for (int i=0, j=0; i<ServiceCenter.length; i++) {
                if (!(ServiceCenter[i].equals("e"))) {
                    patientsNames[j++] = ServiceCenter[i];
                }
            }

            // sorting array using bubble sort
            for(int i=0; i<patientsNames.length-1; i++) {
                String temp;
                int comparisonResult;
                for (int j=0; j<patientsNames.length-1-i; j++) {
                    comparisonResult = patientsNames[j].compareTo(patientsNames[j+1]);
                    if (comparisonResult > 0) {
                        temp = patientsNames[j];
                        patientsNames[j] = patientsNames[j+1];
                        patientsNames[j+1] = temp;
                    }
                }
            }
            System.out.println(Arrays.toString(patientsNames));
        }
        else {
            System.out.println("Sorry, no patients found in booth.");
        }
    }

    public static void storeProgramData() {
        try {
            File patientFile = new File(FILE_PATH);

            if (patientFile.createNewFile()) {
                System.out.println("File created: " + patientFile.getName());
            }

            try {
                String data = Integer.toString(vaccineStock);
                for (String name:ServiceCenter) {
                    data = data.concat("\n" + name);
                }

                FileWriter myWriter = new FileWriter(FILE_PATH);
                myWriter.write(data);
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            }
            catch (IOException e) {
                System.out.println("An error occurred when writing to the file.");
            }


        }
        catch (IOException e) {
            System.out.println("An error occurred when creating the file.");
        }
    }

    public static void loadProgramData() {
        try {
            File patientFile = new File(FILE_PATH);
            Scanner myReader = new Scanner(patientFile);
            int index = 0;
            String[] dataList = new String[7];

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                dataList[index++] = data;
            }
            myReader.close();

            initialise(ServiceCenter);
            vaccineStock = Integer.parseInt(dataList[0]);

            System.arraycopy(dataList, 1, ServiceCenter, 0, ServiceCenter.length);
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
    }

    public static void viewRemainingVaccinations() {
        System.out.printf("Currently we have %s vaccine(s) left in the stock!\n", vaccineStock);
    }

    public static void addVaccinationsToStock() {
        int amount;
        while(true) {
            System.out.print("Enter the amount of new arrival vaccines: ");
            try {
                amount = input.nextInt();
                if (amount >= 0) {
                    vaccineStock += amount;
                    System.out.printf("%s new vaccines added to the stock\n", amount);
                    viewRemainingVaccinations();
                    break;
                }
                System.out.println("Amount of vaccines can't be less than zero.\nTry again.\n");
            } catch (InputMismatchException e) {
                input.nextLine();
                System.out.println("Amount should be a numeric value.\nPlease enter a numeric value.\n");
            }
        }
    }
}