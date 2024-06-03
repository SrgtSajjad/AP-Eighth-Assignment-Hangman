package hangman;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainMenuController {

    private UserInfo userInfo = null;

    @FXML
    private Button logOutButton;

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

    @FXML
    private TableView<LeaderBoardEntry> leaderBoard;

    @FXML
    private TableColumn<LeaderBoardEntry, Integer> rankColumn;

    @FXML
    private TableColumn<LeaderBoardEntry, String> userColumn;

    @FXML
    private TableColumn<LeaderBoardEntry, Integer> winsColumn;

    @FXML
    private TableView<PastGamesEntry> pastGamesTable;

    @FXML
    private TableColumn<PastGamesEntry, String> pastWordColumn;

    @FXML
    private TableColumn<PastGamesEntry, String> pastWinColumn;

    @FXML
    private TableColumn<PastGamesEntry, Integer> pastTimeColumn;


    private int menu = 0;

    @FXML
    void confirmInputs(ActionEvent event) {
        logText.setText("");
        boolean confirmed = false;
        String username = usernameField.getText();
        String password = passwordField.getText();
        String name = nameField.getText();

        if (checkInput(username) && checkInput(password)) {
            System.out.println("[System] Inputs format confirmed. Validating inputs now...");
        } else {
            logText.setText("Invalid input: Entered phrases should be at least 8 characters and only contain alphabets, numbers and underscores.");
            System.out.println("[System] Inputs format is wrong!");
            return;
        }

        switch (menu) {
            case 1:
                validateLogin(username, password, event);
                break;
            case 2:
                validateSignUp(name, username, password, event);
                break;
            default:
                System.out.println("[System] Menu not found!");
        }
    }

    private void validateSignUp(String name, String username, String password, ActionEvent event) {
        DatabaseManager database = new DatabaseManager();

        if (database.checkUserAvailability(username)) {
            logText.setText("Invalid credentials: Username is already taken, please try again.");
            System.out.println("[System]: Sign up failed (username already taken)");
            return;
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setName(name);
        userInfo.setUsername(username);
        userInfo.setPassword(password);
        database.insertUserInfo(userInfo);

        enterGame(userInfo, event);

    }

    private void validateLogin(String username, String password, ActionEvent event) {
        DatabaseManager database = new DatabaseManager();

        if (!database.checkUserAvailability(username)) {
            logText.setText("Invalid credentials: Username or password is incorrect, please try again.");
            System.out.println("[System]: Login failed (username unavailable).");
            return;
        }

        UserInfo userInfo = database.selectUserInfo(username);

        if (!userInfo.getPassword().equals(password)) {
            logText.setText("Invalid credentials: Username or password is incorrect, please try again.");
            System.out.println("[System]: Login failed (password incorrect).");
            return;
        }

        enterGame(userInfo, event);

    }

    public void enterGame(UserInfo userInfo, ActionEvent event) {
        Stage stage;
        Scene scene;
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hangman-view.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HangmanController controller = loader.getController();
        controller.setGame(userInfo);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
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
    void openSesame(ActionEvent event) {
        if (userInfo == null) {
            getLogInPage(event);
        } else
            enterGame(userInfo, event);
    }

    @FXML
    private Button pastGamesbutton;

    @FXML
    void logOut(ActionEvent event) {
        userInfo = null;
        logOutButton.setDisable(true);
        pastGamesbutton.setDisable(true);
    }

    @FXML
    AnchorPane leaderBoardPane;

    @FXML
    AnchorPane pastGamesPane;

    @FXML
    void displayLeaderBoard(ActionEvent event) {

        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        winsColumn.setCellValueFactory(new PropertyValueFactory<>("wins"));

        DatabaseManager databaseManager = new DatabaseManager();


        ArrayList<DatabaseManager.LeaderBoard> leaderBoards = databaseManager.selectLeaderBoard();
        leaderBoards.sort(Comparator.comparing(DatabaseManager.LeaderBoard::getWins).reversed());

        int i = 0;

        for (DatabaseManager.LeaderBoard info : leaderBoards) {
            i++;
            leaderBoard.getItems().add(new LeaderBoardEntry(i, info.getUsername(), info.getWins()));
        }

        leaderBoardPane.setVisible(true);
    }

    @FXML
    void displayPastGames(ActionEvent event) {
        pastWordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        pastTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        pastWinColumn.setCellValueFactory(new PropertyValueFactory<>("win"));

        DatabaseManager databaseManager = new DatabaseManager();
        ArrayList<DatabaseManager.PastGame> pastGames = databaseManager.selectPastGames(userInfo);

        for (DatabaseManager.PastGame info : pastGames) {
            pastGamesTable.getItems().add(new PastGamesEntry(info.getWord(), info.time, info.isWin()?"Yes" : "No"));
        }

        pastGamesPane.setVisible(true);
    }


    @FXML
    void returnToMenu(ActionEvent event) {
        menuPane.setVisible(false);
        leaderBoardPane.setVisible(false);
        pastGamesPane.setVisible(false);
        System.out.println("[System]: User has entered the Main Menu.");
    }

    public boolean checkInput(String input) {

        String regex = "^[a-z0-9_]{8,}$"; // Regex to match at least 8 characters, containing only alphabets, numbers, and underscores
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);

        return matcher.find();
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Button getLogOutButton() {
        return logOutButton;
    }

    public Button getPastGamesbutton() {
        return pastGamesbutton;
    }

    public class LeaderBoardEntry {
        private final SimpleIntegerProperty rank;
        private final SimpleStringProperty user;
        private final SimpleIntegerProperty wins;


        public LeaderBoardEntry(int rank, String user, int wins) {
            this.rank = new SimpleIntegerProperty(rank);
            this.user = new SimpleStringProperty(user);
            this.wins = new SimpleIntegerProperty(wins);

        }

        public int getRank() {
            return rank.get();
        }

        public String getUser() {
            return user.get();
        }

        public int getWins() {
            return wins.get();
        }

    }

    public class PastGamesEntry {
        private final SimpleStringProperty word;
        private final SimpleStringProperty win;
        private final SimpleIntegerProperty time;


        public PastGamesEntry(String word, int time, String win) {
            this.word = new SimpleStringProperty(word);
            this.win = new SimpleStringProperty(win);
            this.time = new SimpleIntegerProperty(time);

        }

        public String getWord() {
            return word.get();
        }

        public SimpleStringProperty wordProperty() {
            return word;
        }

        public String getWin() {
            return win.get();
        }

        public SimpleStringProperty winProperty() {
            return win;
        }

        public int getTime() {
            return time.get();
        }

        public SimpleIntegerProperty timeProperty() {
            return time;
        }
    }

}
