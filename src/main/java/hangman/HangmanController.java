package hangman;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HangmanController {

    @FXML
    private Hyperlink logInLink;

    @FXML
    private Text logText;

    @FXML
    private Label menuName;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField nameField;

    @FXML
    private AnchorPane menuPane;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Hyperlink signUpLink;

    @FXML
    private TextField usernameField;

    private int menu = 0;

    @FXML
    void confirmInputs(ActionEvent event) {
        logText.setText("");
        boolean confirmed = false;
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (checkInput(username) && checkInput(password)) {
            System.out.println("[System] Inputs format confirmed. Validating inputs now...");
        }
        else {
            logText.setText("Invalid input: Entered phrases should be at least 8 characters and only contain alphabets, numbers and underscores.");
            System.out.println("[System] Inputs format is wrong!");
            return;
        }

        switch (menu) {
            case 1:
                validateLogin(username, password);
                break;
            case 2:
                validateSignUp(username);
                break;
            default:
                System.out.println("[System] Menu not found!");
        }
    }

    private void validateSignUp(String username) {
        String name = nameField.getText();
        if (name.isEmpty()) {
            logText.setText("Sign up failed: Please complete the \"Name\" field.");
        }


    }

    private void validateLogin(String username, String password) {
        if (!DatabaseManager.checkUserAvailability(username)) {
            logText.setText("Authentication failed: Username was not found.");
            System.out.println("[System] Login failed.");
            return;
        }






    }

    @FXML
    void getLogInPage(ActionEvent event) {
        menuName.setText("Log In");
        signUpLink.setVisible(true);
        logText.setText("");
        logInLink.setVisible(false);
        nameField.setVisible(false);
        nameLabel.setVisible(false);
        passwordField.clear();
        usernameField.clear();
        menu = 1;
        menuPane.setVisible(true);
        System.out.println("[System] User has entered the login menu.");
    }

    @FXML
    void getSignUpPage(ActionEvent event) {
        menuName.setText("Sign Up");
        logInLink.setVisible(true);
        logText.setText("");
        signUpLink.setVisible(false);
        nameField.setVisible(true);
        nameLabel.setVisible(true);
        passwordField.clear();
        usernameField.clear();
        menu = 2;
        menuPane.setVisible(true);
        System.out.println("[System] User has entered the sign up menu.");
    }

    @FXML
    void returnToMenu(ActionEvent event) {
        menuPane.setVisible(false);
    }

    public boolean checkInput(String input) {

        String regex = "^[a-z0-9_]{8,}$"; // Regex to match at least 8 characters, containing only alphabets, numbers, and underscores
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);

        return matcher.find();
    }

}
