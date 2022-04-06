package Task2Extended;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class VaccinationCenter {
    static final String FILE_PATH_PATIENT = "data/task2Extended_data/patient_data.csv";
    static final String FILE_PATH_STOCK = "data/task2Extended_data/stock_data.csv";
    static Scanner input = new Scanner(System.in);
    static Booth[] serviceCenter = new Booth[6];
    static HashMap<String, Integer> vaccineStocks = new HashMap<>();
    static boolean isRunning = true;

    public static void main(String[] args) {
        vaccineStocks.put("AstraZeneca", 150);
        vaccineStocks.put("Sinopharm", 150);
        vaccineStocks.put("Pfizer", 150);

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

    private static void displayVaccineMenu() {
        System.out.println("""
                              
                Please select vaccination type,
                
                Enter 01 or ASZ for AstraZeneca
                Enter 02 or SPM for Sinopharm
                Enter 03 or FZR for Pfizer
                Or enter -1 to go back to main menu.""");
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
        displayVaccineMenu();
        String requestedVaccine = input.next().toUpperCase();
        switch (requestedVaccine) {
            case "01", "ASZ" -> assignPatientToBooth("AstraZeneca", new int[]{0, 1});
            case "02", "SPM" -> assignPatientToBooth("Sinopharm", new int[]{2, 3});
            case "03", "FZR" -> assignPatientToBooth("Pfizer", new int[]{4, 5});
            case "-1" -> {
                System.out.println("Returning to main menu ...");
                return;
            }
            default -> System.out.printf("Oops, %s is an invalid input.\nPlease try again.", requestedVaccine);
        }
        addPatientToBooth();
    }

    public static void removePatientFromBooth() {
        // checks whether the booth array is empty
        if (isBoothArrayEmpty()) {
            System.out.println("Sorry, At the moment there are no patients to remove.");
        }
        else {
            System.out.print("Enter the first name of patient you wish to remove or -1 to go back to the main menu: ");
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
        storePatientData();
        storeStockData();
    }

    public static void loadProgramData() {
        loadPatientData();
        loadStockData();
    }

    public static void viewRemainingVaccinations() {
        int stock;
        for (String vaccineName:vaccineStocks.keySet()) {
            stock = vaccineStocks.get(vaccineName);
            System.out.printf("Currently we have %s %s vaccine(s) left in the stock!\n",stock, vaccineName);
        }
    }

    public static void addVaccinationsToStock() {
        displayVaccineMenu();
        String vaccineType = input.next().toUpperCase();
        switch (vaccineType) {
            case "01", "ASZ" -> updateStock("AstraZeneca");
            case "02", "SPM" -> updateStock("Sinopharm");
            case "03", "FZR" -> updateStock("Pfizer");
            case "-1" -> {
                System.out.println("Returning to main menu ...");
                return;
            }
            default -> System.out.printf("Oops, %s is an invalid input.\nPlease try again.", vaccineType);
        }
        addVaccinationsToStock();
    }

    public static int checkEmptyBooths(int[] boothNumbers) {
        for (int boothNum:boothNumbers) {
            if (serviceCenter[boothNum] == null) {
                return boothNum;
            }
        }
        return -1;
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

    public static void assignPatientToBooth(String vaccineName, int[] boothNumbers) {
        int currentStock = vaccineStocks.get(vaccineName);

        if (currentStock > 0) {
            int emptyBoothNum = checkEmptyBooths(boothNumbers);

            if (emptyBoothNum >= 0) {
                serviceCenter[emptyBoothNum] = new Booth(createPatient(vaccineName));
                System.out.println("Patient added successfully");
                decrementStock(vaccineName);
            }
            else {
                System.out.printf("Sorry, we have no enough rooms to accommodate more %s requests\n", vaccineName);
            }
        }
        else {
            System.out.println("\nOops! Sorry, We are running out of vaccines!!!\nTry again later.");
        }
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
            File file = new File(filePath);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            }

            try {
                // writing to file
                FileWriter myWriter = new FileWriter(filePath);
                myWriter.write(data);
                myWriter.close();
                System.out.println("Successfully wrote to the file " + file.getName());
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

    public static ArrayList<String> readFile(String filePath) {
        try {
             // creating an file object
            File patientFile = new File(filePath);
            Scanner myReader = new Scanner(patientFile);
            ArrayList<String> dataList = new ArrayList<>();

            // reading file
            while (myReader.hasNextLine()) {
//                data += myReader.nextLine() + "\n";
                dataList.add(myReader.nextLine());
            }
            myReader.close();
            return dataList;
        }
        catch (FileNotFoundException e) {
            // return an empty string if file was not found
            return new ArrayList<>();
        }
    }

    public static void storePatientData() {
        // creating a data list

        // defining the header fields
        ArrayList<String> dataList = new ArrayList<>();
        dataList.add("BoothNumber");
        dataList.add(",");
        dataList.add("FirstName");
        dataList.add(",");
        dataList.add("Surname");
        dataList.add(",");
        dataList.add("Age");
        dataList.add(",");
        dataList.add("City");
        dataList.add(",");
        dataList.add("PassportNo");
        dataList.add(",");
        dataList.add("requestedVaccine");
        dataList.add("\n");


        // adding patient details to the list
        int boothNum = 0;
        String[] patientDetailList;
        for (Booth booth:serviceCenter) {
            dataList.add(Integer.toString(boothNum++));
            dataList.add(",");
            if (booth == null) {
                dataList.add("NULL");
                dataList.add(",");
                dataList.add("NULL");
                dataList.add(",");
                dataList.add("NULL");
                dataList.add(",");
                dataList.add("NULL");
                dataList.add(",");
                dataList.add("NULL");
                dataList.add(",");
                dataList.add("NULL");
                dataList.add("\n");
                continue;
            }
            patientDetailList = booth.getPatientDetailList();
            for (String field:patientDetailList) {
                dataList.add(field);
                dataList.add(",");
            }
            dataList.set(dataList.size()-1, "\n");
        }

        // creating data string using pre-generated data list
        String dataString = "";
        for (String item:dataList) {
            dataString += item;
        }

        // writing generated data string to file
        writeToFile(FILE_PATH_PATIENT, dataString);
    }

    public static void storeStockData() {
        // creating a data list
        ArrayList<String> dataList = new ArrayList<>();
        // defining header fields
        dataList.add("Vaccine Name");
        dataList.add(",");
        dataList.add("Stock Available");
        dataList.add("\n");

        // adding stock details
        for (String vaccineName:vaccineStocks.keySet()) {
            dataList.add(vaccineName);
            dataList.add(",");
            dataList.add(Integer.toString(vaccineStocks.get(vaccineName)));
            dataList.add("\n");
        }

        // creating data string using pre-generated data list
        String dataString = "";
        for (String item:dataList) {
            dataString += item;
        }

        // writing generated data string to file
        writeToFile(FILE_PATH_STOCK, dataString);

    }

    public static void loadPatientData() {
        // reading data file and adding data to an array
        ArrayList<String> dataArray = readFile(FILE_PATH_PATIENT);

        int boothNum;
        String[] patientData;
        String patientFirstName;
        String patientSurname;
        int age;
        String patientCity;
        String patientPassportNumber;
        String requestedVaccine;

        for (int i=1; i<dataArray.size(); i++) {
            patientData = dataArray.get(i).split(",");

            boothNum = Integer.parseInt(patientData[0]);
            patientFirstName = patientData[1];
            if (patientFirstName.equals("NULL")) {
                serviceCenter[boothNum] = null;
                continue;
            }
            patientSurname = patientData[2];
            age = Integer.parseInt(patientData[3]);
            patientCity = patientData[4];
            patientPassportNumber = patientData[5];
            requestedVaccine = patientData[6];

            Patient patient = new Patient(patientFirstName, patientSurname, age, patientCity, patientPassportNumber, requestedVaccine);
            serviceCenter[boothNum] = new Booth(patient);
        }
    }

    public static void loadStockData() {
        ArrayList<String> stockDataList = readFile(FILE_PATH_STOCK);
        if (stockDataList.isEmpty()) {
            System.out.println("Oops, Stock data file is empty.\nWe couldn't load stocks.");
            return;
        }

        String[] vaccineRow;
        String vaccineName;
        int vaccineStock;
        vaccineStocks.clear();

        for (int i=1; i<stockDataList.size(); i++) {
            vaccineRow = stockDataList.get(i).split(",");
            vaccineName = vaccineRow[0];
            vaccineStock = Integer.parseInt(vaccineRow[1]);
            vaccineStocks.put(vaccineName, vaccineStock);
        }
        System.out.println("Stock data loaded successfully");
    }

    public static Patient createPatient(String vaccineName) {
        System.out.printf("Enter patient name for booth %s: ", vaccineName);
        String patientFirstName = input.next().toLowerCase();

        System.out.printf("Hi %s, please enter your surname: ", patientFirstName);
        String patientSurname = input.next().toLowerCase();

        int patientAge;
        while (true) {
            try {
                System.out.printf("%s, please enter your age: ", patientFirstName);
                patientAge = input.nextInt();
                if (patientAge <= 0) {
                    System.out.println("Age can't be less than 0 or equal to 0");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                input.nextLine();
                System.out.println("Age should be a numeric value. Please enter a numeric value");
            }
        }
        System.out.printf("%s, please enter the city you live: ", patientFirstName);
        String patientCity = input.next().toLowerCase();

        System.out.printf("%s, please enter your passport number: ", patientFirstName);
        String patientPassportNumber = input.next().toLowerCase();

        return new Patient(patientFirstName, patientSurname, patientAge, patientCity, patientPassportNumber, vaccineName);
    }

    public static void updateStock(String vaccineName) {
        System.out.print("Enter the amount of new arrival vaccines: ");
        try {
            int amount = input.nextInt();

            if (amount >= 0) {
                vaccineStocks.put(vaccineName, vaccineStocks.get(vaccineName) + amount);
                System.out.printf("%s new vaccines added to the stock.", amount);
                System.out.printf("\nCurrently we have %s %s vaccines in the stock.\n", vaccineStocks.get(vaccineName), vaccineName);
            }
            else {
                System.out.println("Amount of vaccines can't be less than zero.\nTry again.\n");
                updateStock(vaccineName);
            }
        }
        catch (InputMismatchException e) {
            input.nextLine();
            System.out.println("The amount of vaccines should be a numeric value." +
                    "\nPlease enter a numeric value and try again.\n");
            updateStock(vaccineName);
        }
    }

    public static void decrementStock(String vaccineName) {
        vaccineStocks.put(vaccineName, vaccineStocks.get(vaccineName)-1);
        displayWarning(vaccineName);
    }

    public static void displayWarning(String vaccineName) {
        int currentStock = vaccineStocks.get(vaccineName);
        if (currentStock <= 20) {
            System.out.printf("""
                        WARNING!!!
                        Currently we have only %s more %s vaccines.""", currentStock,  vaccineName
            );
        }
    }
}

