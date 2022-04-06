package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ReceiptController {
    @FXML
    private Label nameLabel;
    @FXML
    private Label passportNumLabel;
    @FXML
    private Label vaccineLabel;
    @FXML
    private Label ageLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label dateLabel;

    public void setLabels(String firstName, String surname, String passportNum, String vaccineName, int age, String city) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        dateLabel.setText(dtf.format(now));
        nameLabel.setText(firstName + " " + surname);
        passportNumLabel.setText(passportNum);
        vaccineLabel.setText(vaccineName);
        ageLabel.setText(Integer.toString(age));
        cityLabel.setText(city);

    }
}
