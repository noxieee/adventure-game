package cz.vse.java.kedv00.adventura.main;

import cz.vse.java.kedv00.adventura.api.IGame;
import cz.vse.java.kedv00.adventura.src.Game;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Optional;

public class HomeController
{
    @FXML
    private TextArea consoleOutput;
    @FXML
    private TextField userInput;
    private final IGame game = Game.getInstance();

    @FXML
    private void initialize()
    {
        consoleOutput.appendText("Zmáčkni enter pro odstartování hry.\n");
        Platform.runLater(() -> userInput.requestFocus());
    }

    @FXML
    private void sendUserInput(ActionEvent actionEvent)
    {
        if(!game.isAlive()) { consoleOutput.clear(); }

        String command = userInput.getText();
        consoleOutput.appendText("> " + command.toUpperCase() + "\n\n");
        String gameOutput = game.executeCommand(command);
        consoleOutput.appendText(gameOutput + "\n\n");
        userInput.clear();
    }

    @FXML
    private void closeGame(ActionEvent actionEvent)
    {
        Alert exitGameAlert = new Alert(Alert.AlertType.CONFIRMATION, "Jsi si jistý, že chceš zavřít hru?");
        Optional<ButtonType> result = exitGameAlert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK)
        {
            game.stop();
            Platform.exit();
        }
    }

    @FXML
    private void newGame(ActionEvent actionEvent)
    {
        Alert newGameAlert = new Alert(Alert.AlertType.CONFIRMATION, "Jsi si jistý, že chceš novou hru?");
        Optional<ButtonType> result = newGameAlert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK)
        {
            game.stop();
            consoleOutput.clear();
            initialize();
        }
    }
}
