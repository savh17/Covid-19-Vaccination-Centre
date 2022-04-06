package Task1Extended;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    static final String FILE_PATH_PATIENT = "data/task1Extended_data/patient_data.csv";
    static final String FILE_PATH_STOCK = "data/task1Extended_data/stock_data.csv";
    static String[] patientsFirstNames = new String[6];
    static String[] patientsSurnames = new String[6];
    static String[] patientsRequestedVaccineTypes = new String[6];
    static HashMap<String, Integer> vaccineStocks = new HashMap<>();
    static boolean isRunning = true;
    static Scanner input = new Scanner(System.in);


    public static void main(String[] args) {
        initialise(patientsFirstNames);
        initialise(patientsSurnames);
        initialise(patientsRequestedVaccineTypes);

        vaccineStocks.put("AstraZeneca", 150);
        vaccineStocks.put("Sinopharm", 150);
        vaccineStocks.put("Pfizer", 150);


        while (isRunning) {
            System.out.println(Arrays.toString(patientsFirstNames));
            System.out.println(Arrays.toString(patientsSurnames));
            System.out.println(Arrays.toString(patientsRequestedVaccineTypes));
            System.out.println();
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
            case "999", "EXT" -> isRunning = false;
            default -> System.out.println("Oops, invalid Command!!!\nPleae enter a valid command.");
        }
    }

    private static void viewAllVaccinationBooths() {
        for (int x = 0; x < 6; x++) {
            if (patientsFirstNames[x].equals("e")) {
                System.out.println("booth " + (x+1) + " is empty.");
                continue;
            }
            System.out.println("booth " + (x+1) + " occupied by " + patientsFirstNames[x] + " " + patientsSurnames[x]);
        }
    }

    private static void viewAllEmptyBooths() {
        for (int x = 0; x < 6; x++) {
            if (patientsFirstNames[x].equals("e")) {
                System.out.println("booth " + (x + 1) + " is empty.");
            }
        }
    }

    private static void addPatientToBooth() {
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

    private static void removePatientFromBooth() {
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

            for (int i = 0; i<patientsFirstNames.length; i++) {
                // if name matches setting that position to "e"
                if (patientsFirstNames[i].equals(patientNameToRemove)) {
                    patientsFirstNames[i] = "e";
                    patientsSurnames[i] = "e";
                    patientsRequestedVaccineTypes[i] = "e";
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
        int noPatients = 0;

        // counting number of patients
        for (int i=0; i<patientsFirstNames.length; i++) {
            if (!(patientsFirstNames[i].equals("e")))
                noPatients ++;
        }


        if (noPatients > 0) {
            String[] patientsNames = new String[noPatients];

            // appending names to the array
            for (int i=0, j=0; i<patientsFirstNames.length; i++) {
                if (!(patientsFirstNames[i].equals("e"))) {
                    patientsNames[j++] = patientsFirstNames[i];
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

    public static void assignPatientToBooth(String vaccineName, int[] boothNumbers) {
        int currentStock = vaccineStocks.get(vaccineName);

        if (currentStock > 0) {
            int emptyBoothNum = checkEmptyBooths(boothNumbers);

            if (emptyBoothNum >= 0) {
                String[] patientDetails = createPatient(vaccineName);
                patientsFirstNames[emptyBoothNum] = patientDetails[0];
                patientsSurnames[emptyBoothNum] = patientDetails[1];
                patientsRequestedVaccineTypes[emptyBoothNum] = vaccineName;
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

    public static int checkEmptyBooths(int[] boothNumbers) {
        for (int boothNum:boothNumbers) {
            if (patientsFirstNames[boothNum].equals("e")) {
                return boothNum;
            }
        }
        return -1;
    }

    public static String[] createPatient(String vaccineName) {
        System.out.printf("Enter patient name for booth %s: ", vaccineName);
        String patientFirstName = input.next().toLowerCase();

        System.out.printf("Hi %s, please enter your surname: ", patientFirstName);
        String patientSurname = input.next().toLowerCase();

        return new String[] {patientFirstName, patientSurname};
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

    public static boolean isBoothArrayEmpty() {
        for (String name:patientsFirstNames) {
            if (name.equals("e")) {
                continue;
            }
            return false;
        }
        return true;
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
        dataList.add("requestedVaccine");
        dataList.add("\n");


        // adding patient details to the list
        String[] patientDetailList;
        String firstName;
        String surname;
        String requestedVaccine;

        for (int boothNum=0; boothNum<patientsFirstNames.length; boothNum++) {
            dataList.add(Integer.toString(boothNum));
            dataList.add(",");

            patientDetailList = getPatientDetailList(boothNum);
            System.out.print("List");
            System.out.println(Arrays.toString(patientDetailList));


            if (patientDetailList[0].equals("e")) {
                dataList.add("NULL");
                dataList.add(",");
                dataList.add("NULL");
                dataList.add(",");
                dataList.add("NULL");
                dataList.add("\n");
                continue;
            }

            for (String field:patientDetailList) {
                System.out.print("Field");
                System.out.println(field);
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
        String requestedVaccine;

        for (int i=1; i<dataArray.size(); i++) {
            patientData = dataArray.get(i).split(",");

            boothNum = Integer.parseInt(patientData[0]);
            patientFirstName = patientData[1];
            if (patientFirstName.equals("NULL")) {
                patientsFirstNames[boothNum] = "e";
                patientsSurnames[boothNum] = "e";
                patientsRequestedVaccineTypes[boothNum] = "e";
                continue;
            }
            patientSurname = patientData[2];
            requestedVaccine = patientData[3];

            patientsFirstNames[boothNum] = patientFirstName;
            patientsSurnames[boothNum] = patientSurname;
            patientsRequestedVaccineTypes[boothNum] = requestedVaccine;
        }
        System.out.println("Patients loaded successfully");
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

    public static String[] getPatientDetailList(int boothNum) {
        String firstName = patientsFirstNames[boothNum];
        String surname = patientsSurnames[boothNum];
        String requestedVaccine = patientsRequestedVaccineTypes[boothNum];

        return new String[] {firstName, surname, requestedVaccine};

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
}