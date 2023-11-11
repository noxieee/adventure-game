package cz.vse.java.kedv00.adventura.main;

import cz.vse.java.kedv00.adventura.api.*;
import cz.vse.java.kedv00.adventura.src.Game;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static cz.vse.java.kedv00.adventura.src.Scenarios.*;

public class HomeController {
    @FXML
    private ListView<IItem> placeItemsPanel;
    @FXML
    private Label bagLabel;
    @FXML
    private ListView<IItem> bagItemsPanel;
    @FXML
    private ImageView playerIcon;
    @FXML
    private ListView<IPlace> locationPanel;
    @FXML
    private TextArea consoleOutput;
    @FXML
    private TextField userInput;
    private final IGame game = Game.getInstance();
    private final ObservableList<IPlace> neighbourLocations = FXCollections.observableArrayList();
    private final ObservableList<IItem> bagItems = FXCollections.observableArrayList();
    private final ObservableList<IItem> placeItems = FXCollections.observableArrayList();
    private final Map<String, Point2D> locationCoords = new HashMap<>();

    @FXML
    private void initialize() {
        consoleOutput.clear();
        executeCommand("");
        Platform.runLater(() -> userInput.requestFocus());

        locationPanel.setItems(neighbourLocations);
        bagItemsPanel.setItems(bagItems);
        placeItemsPanel.setItems(placeItems);

        game.world().registerObserver(TypeOfChange.CHANGE_OF_PLACE,
                () -> {updateLocationPanel(); updatePlayerCoords();});

        game.bag().registerObserver(TypeOfChange.CHANGE_OF_BAG_ITEMS,
                this::updateItemsPanels);

        updateLocationPanel();
        updateItemsPanels();

        locationCoords.put(BEDROOM_NAME, new Point2D(222.0, 239.0));
        locationCoords.put(HALLWAY_NAME, new Point2D(222.0, 136.0));
        locationCoords.put(BATHROOM_NAME, new Point2D(222.0, 34.0));
        locationCoords.put(BALCONY_NAME, new Point2D(349.0, 134.0));
        locationCoords.put(KITCHEN_NAME, new Point2D(64.0, 134.0));
        locationCoords.put(SINK_NAME, new Point2D(30.0, 244.0));

        locationPanel.setCellFactory(param -> new ListCellPlace());
        bagItemsPanel.setCellFactory(param -> new ListCellItem());
        placeItemsPanel.setCellFactory(param -> new ListCellItem());
    }

    @FXML
    private void updateLocationPanel() {
        IPlace currentPlace = game.world().currentPlace();

        neighbourLocations.clear();

        if (currentPlace != null) {
            neighbourLocations.addAll(currentPlace.neighbors());
            placeItems.clear();
            placeItems.addAll(currentPlace.items());
        }
    }

    @FXML
    private void updateOnGameEnd()
    {
        locationPanel.setDisable(!game.isAlive());
    }

    @FXML
    private void updateItemsPanels() {
        IBag bag = game.bag();
        IPlace currentPlace = game.world().currentPlace();

        bagLabel.setText("Předměty v rukou (" + bag.items().size() + "/" + bag.capacity() + ")");
        bagItems.clear();
        bagItems.addAll(bag.items());

        if(currentPlace != null)
        {
            placeItems.clear();
            placeItems.addAll(currentPlace.items());
        }
    }

    @FXML
    private void sendUserInput(ActionEvent actionEvent)
    {
        String command = userInput.getText();
        userInput.clear();

        executeCommand(command);
    }

    @FXML
    private void updatePlayerCoords()
    {
        IPlace currentPlace = game.world().currentPlace();

        playerIcon.setLayoutX(locationCoords.get(currentPlace.name()).getX());
        playerIcon.setLayoutY(locationCoords.get(currentPlace.name()).getY());
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

    public void clickBagPanel(MouseEvent mouseEvent)
    {
        IItem selectedItem = bagItemsPanel.getSelectionModel().getSelectedItem();

        if(selectedItem != null)
        {
            String command = COMMAND_PUT + " " + selectedItem.name();
            executeCommand(command);
        }
    }

    public void clickPlaceItemPanel(MouseEvent mouseEvent)
    {
        IItem selectedItem = placeItemsPanel.getSelectionModel().getSelectedItem();

        if(selectedItem != null)
        {
            String command = COMMAND_TAKE + " " + selectedItem.name();
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
