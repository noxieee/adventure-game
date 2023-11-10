package cz.vse.java.kedv00.adventura.main;

import cz.vse.java.kedv00.adventura.api.*;
import cz.vse.java.kedv00.adventura.src.Game;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.Optional;

import static cz.vse.java.kedv00.adventura.src.Scenarios.COMMAND_END;
import static cz.vse.java.kedv00.adventura.src.Scenarios.COMMAND_GOTO;

public class HomeController {
    @FXML
    private ListView<IPlace> locationPanel;
    @FXML
    private TextArea consoleOutput;
    @FXML
    private TextField userInput;
    private final IGame game = Game.getInstance();
    private final ObservableList<IPlace> neighbourLocations = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        consoleOutput.clear();
        executeCommand("");
        Platform.runLater(() -> userInput.requestFocus());
        locationPanel.setItems(neighbourLocations);
        game.world().registerObserver(TypeOfChange.CHANGE_OF_PLACE, this::updateLocationPanel);
        updateLocationPanel();
    }

    @FXML
    private void updateLocationPanel() {
        IPlace currentPlace = game.world().currentPlace();

        neighbourLocations.clear();

        if (currentPlace != null) {
            neighbourLocations.addAll(currentPlace.neighbors());
        }
    }

    @FXML
    private void updateOnGameEnd()
    {
        locationPanel.setDisable(!game.isAlive());
    }

    @FXML
    private void sendUserInput(ActionEvent actionEvent)
    {
        String command = userInput.getText();
        userInput.clear();

        executeCommand(command);
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
            executeCommand(COMMAND_END);
            consoleOutput.clear();
            initialize();
        }
    }

    @FXML
    private void clickLocationPanel(MouseEvent mouseEvent)
    {
        IPlace selectedLocation = locationPanel.getSelectionModel().getSelectedItem();

        if(selectedLocation != null) {
            String command = COMMAND_GOTO + " " + selectedLocation.name();
            executeCommand(command);
        }
    }

    private void executeCommand(String command)
    {
        consoleOutput.appendText("> " + command.toUpperCase() + "\n\n");
        String gameOutput = game.executeCommand(command);
        consoleOutput.appendText(gameOutput + "\n\n");
        updateOnGameEnd();
    }
}
