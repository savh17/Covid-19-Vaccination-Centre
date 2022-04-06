package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class Controller {
    @FXML
    private TextField patientFirstName;
    @FXML
    private TextField patientSurname;
    @FXML
    private TextField patientPassportNumber;
    @FXML
    private TextField patientVaccineName;
    @FXML
    private TextField patientAge;
    @FXML
    private TextField patientCity;

    private Parent root;


    static String firstName;
    static String surname;
    static String passportNum;
    static String vaccineName;
    static String age;
    static String city;

    public void displayReceipt() throws Exception {
        if (validateInputs()) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("receipt.fxml"));
            root = loader.load();

            ReceiptController rc = loader.getController();
            rc.setLabels(firstName, surname, passportNum, vaccineName, Integer.parseInt(age), city);

            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            newStage.setTitle("Record");
            newStage.setScene(scene);
            newStage.show();
        }
    }

    public void clearInputs() {
        patientFirstName.clear();
        patientSurname.clear();
        patientPassportNumber.clear();
        patientVaccineName.clear();
        patientAge.clear();
        patientCity.clear();
    }

    public boolean validateInputs() {
        firstName = patientFirstName.getText().trim();
        surname = patientSurname.getText().trim();
        passportNum = patientPassportNumber.getText().trim();
        vaccineName = patientVaccineName.getText().trim();
        age = patientAge.getText().trim();
        city = patientCity.getText().trim();

        if (firstName.equals("") || surname.equals("") || passportNum.equals("") || vaccineName.equals("") || age.equals("") || city.equals("")) {
            new Alert(Alert.AlertType.INFORMATION, "Every field should be filled").showAndWait();
            return false;
        }
        try {
            int validatedAge = Integer.parseInt(age);
            if (validatedAge <= 0) {
                new Alert(Alert.AlertType.INFORMATION, "Age can't be less than or equal to 0.\nPlease try again.").showAndWait();
                return false;
            }

            return true;
        }
        catch (NumberFormatException e) {
            new Alert(Alert.AlertType.INFORMATION, "Age should be a numeric value.\nPlease enter a valid age.").showAndWait();
            return false;
        }
    }
}
