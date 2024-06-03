package hangman;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class HangmanController {

    private UserInfo userInfo;

    private int wrongGuesses = 0;

    private int correctGuesses = 0;

    private String word;

    private Timeline timeline;

    private int timeSeconds = 0;

    private List<Label> letterLabels = new ArrayList<>();

    private boolean gameOver = false;

    private boolean win = false;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private FlowPane flowPane;

    @FXML
    private FlowPane wordBox;

    @FXML
    private Label timerLabel;


    public void setGame(UserInfo userInfo) {
        this.userInfo = userInfo;

        try {
            word = getWord().toUpperCase();
            populateWordBox();
            startTimer();
            System.out.println("[System]: wordBox populated and timer started successfully.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getWord() {
        try {
            URL url = new URL("https://api.api-ninjas.com/v1/randomword");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("X-Api-Key", "g6xmG45Gyp4MrdnXYQkW1g==EfClMRrFdwpizhBP");
            InputStream responseStream = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseStream);
            return root.path("word").asText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void startTimer() {
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            timeSeconds++;
            timerLabel.setText("Time: " + timeSeconds);
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.playFromStart();
    }

    private void populateWordBox() throws IOException {
        for (char letter : word.toCharArray()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("letter-view.fxml"));
            AnchorPane letterPane = loader.load();
            Label letterLabel = (Label) letterPane.lookup("#letterLabel");
            letterLabel.setText(String.valueOf(letter));
            wordBox.getChildren().add(letterPane);
            letterLabels.add(letterLabel);
        }
    }


    @FXML
    public void initialize() {
        for (int i = 0; i < flowPane.getChildren().size(); i++) {
            if (flowPane.getChildren().get(i) instanceof Button) {
                Button button = (Button) flowPane.getChildren().get(i);
                button.setOnAction(event -> handleButtonAction(button));
            }
        }
    }

    private void handleButtonAction(Button button) {
        String letter = button.getText();
        if (word.toUpperCase().contains(letter)) {
            button.setStyle("-fx-background-color: green;");

            activateLetter(letter);
            if (correctGuesses == word.length()) {
                gameOver = true;
                win = true;
            }
        } else {
            button.setStyle("-fx-background-color: red;");
            wrongGuesses++;
            drawPole();
            if (wrongGuesses >= 7) gameOver = true;
        }
        button.setDisable(true);

        if (gameOver) {
            endGame();
        }
    }

    @FXML
    Label winLabel;

    @FXML
    Text playerText;

    @FXML
    Text wordText;

    @FXML
    Label timeLabel;

    @FXML
    Label wrongGuessesLabel;

    @FXML
    AnchorPane gameOverPane;

    private void endGame() {
        timeline.stop();
        flowPane.setDisable(true);
        winLabel.setText("You " + (win ? "WON!" : "LOST!"));
        playerText.setText("Player: " + userInfo.getUsername());
        wordText.setText("Word: " + word.toLowerCase());
        timeLabel.setText("Time: " + timeSeconds + " seconds");
        wrongGuessesLabel.setText("Wrong Guesses: " + wrongGuesses);
        gameOverPane.setVisible(true);
        createGameInfo();

    }

    private void createGameInfo() {
        GameInfo gameInfo = new GameInfo();
        gameInfo.setGameID(UUID.randomUUID());
        gameInfo.setTime(timeSeconds);
        gameInfo.setUsername(userInfo.getUsername());
        gameInfo.setWin(win);
        gameInfo.setWord(word);
        gameInfo.setUserInfo(userInfo);
        gameInfo.setWrongGuesses(wrongGuesses);
        Database database = new Database();
        database.insertGameInfo(gameInfo);
    }

    @FXML
    private void returnToMenu(ActionEvent event) {
        Stage stage;
        Scene scene;
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu-view.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MainMenuController controller = loader.getController();
        controller.setUserInfo(userInfo);
        controller.getLogOutButton().setDisable(false);
        controller.getPastGamesbutton().setDisable(false);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private Line manBody;

    @FXML
    private Circle manHead;

    @FXML
    private Line manLeftArm;

    @FXML
    private Line manLeftLeg;

    @FXML
    private Line manRightArm;

    @FXML
    private Line manRightLeg;

    @FXML
    private Line poleBottom;

    @FXML
    private Line poleMain;

    @FXML
    private Line poleRope;

    @FXML
    private Line poleTop;

    private void drawPole() {
        switch (wrongGuesses) {
            case 1:
                poleBottom.setVisible(true);
                break;
            case 2:
                poleMain.setVisible(true);
                break;
            case 3:
                poleTop.setVisible(true);
                poleRope.setVisible(true);
                break;
            case 4:
                manHead.setVisible(true);
                break;
            case 5:
                manBody.setVisible(true);
                break;
            case 6:
                manRightArm.setVisible(true);
                manLeftArm.setVisible(true);
                break;
            case 7:
                manRightLeg.setVisible(true);
                manLeftLeg.setVisible(true);
                break;
            default:
                break;
        }
    }

    private void activateLetter(String letter) {
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == letter.charAt(0)) {
                Label label = letterLabels.get(i);
                correctGuesses++;
                label.setVisible(true);
            }
        }
    }
}
