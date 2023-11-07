package cz.vse.java.kedv00.adventura.main;

import cz.vse.java.kedv00.adventura.api.IGame;
import cz.vse.java.kedv00.adventura.src.Game;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
}
