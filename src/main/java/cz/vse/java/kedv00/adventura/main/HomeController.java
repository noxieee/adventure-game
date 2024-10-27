package cz.vse.java.kedv00.adventura.main;

import cz.vse.java.kedv00.adventura.api.*;
import cz.vse.java.kedv00.adventura.src.Game;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

import static cz.vse.java.kedv00.adventura.src.Scenarios.*;

/*******************************************************************
 * Třída zajišťující správu UI prvků
 */
public class HomeController {

    /** Random generator */
    private final Random randomGenerator = new Random();

    /** Tajné menu. */
    @FXML
    private Menu secretMenu;

    /** Panel itemů v prostoru. */
    @FXML
    private ListView<IItem> placeItemsPanel;

    /** Panel sousedních prostorů. */
    @FXML
    private ListView<IPlace> locationPanel;

    /** Panel itemů v batohu. */
    @FXML
    private ListView<IItem> bagItemsPanel;

    /** Label panelu batohu. */
    @FXML
    private Label bagLabel;

    /** Ikona hráče na mapě. */
    @FXML
    private ImageView playerIcon;

    /** Konzole, kam se vypisují odpovědi hry. */
    @FXML
    private TextArea consoleOutput;

    /** Textfield pro uživatelský vstup. */
    @FXML
    private TextField userInput;

    /** Reference na instanci hry. */
    private final IGame game = Game.getInstance();

    /** Panel itemů v prostoru. */
    private final ObservableList<IPlace> neighbourLocations = FXCollections.observableArrayList();

    /** List itemů v batohu */
    private final ObservableList<IItem> bagItems = FXCollections.observableArrayList();

    /** List itemů v prostoru */
    private final ObservableList<IItem> placeItems = FXCollections.observableArrayList();

    /** Mapa název prostoru - souřadnice na herním plánu */
    private final Map<String, Point2D> locationCoords = new HashMap<>();

    /***************************************************************
     * Inicializační metoda, která nastaví UI do původního stavu
     * po skončení hry, či při založení nové
     */
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
                () -> updateItemsPanels());

        game.world().registerObserver(TypeOfChange.CHANGE_OF_TELEPORT_UNLOCKED,
                () -> updateSecretMenuVisibility());

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

        secretMenu.setVisible(false);

        ScaleTransition st = new ScaleTransition();
        st.setAutoReverse(true);
        st.setCycleCount(Animation.INDEFINITE);
        st.setDuration(Duration.millis(500.0));
        st.setByX(0.2);
        st.setByY(0.2);
        st.setNode(playerIcon);
        st.play();
    }

    /************************************************************************
     * Metoda, která udělá tajné menu visible.
     */
    private void updateSecretMenuVisibility()
    {
        secretMenu.setVisible(true);
    }

    /***************************************************************
     * Metoda aktualizující panel sousedních prostorů a panel
     * předmětů v prostoru.
     */
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

    /***************************************************************
     * Metoda aktualizující panely itemů v batohu a v prostoru.
     */
    @FXML
    private void updateItemsPanels() {
        IPlace currentPlace = game.world().currentPlace();

        bagLabel.setText("Předměty v rukou (" + game.bag().items().size() + "/" + game.bag().capacity() + ")");
        bagItems.clear();
        bagItems.addAll(game.bag().items());

        if(currentPlace != null)
        {
            placeItems.clear();
            placeItems.addAll(currentPlace.items());
        }
    }

    /*******************************************************************
     * Metoda, která aktualizuje polohu ikony hráče na herním plánu.
     */
    @FXML
    private void updatePlayerCoords()
    {
        IPlace currentPlace = game.world().currentPlace();

        playerIcon.setLayoutX(locationCoords.get(currentPlace.name()).getX());
        playerIcon.setLayoutY(locationCoords.get(currentPlace.name()).getY());
    }

    /***************************************************************
     * Metoda aktualizující panel sousedních prostorů.
     */
    @FXML
    private void updateOnGameEnd()
    {
        locationPanel.setDisable(!game.isAlive());
        bagItemsPanel.setDisable(!game.isAlive());
        placeItemsPanel.setDisable(!game.isAlive());
    }

    /*******************************************************************
     * Metoda, která zachycuje click event u panelu sousedních prostorů
     * a zajišťuje poslání příkazu hře, aby změnila prostor.
     */
    @FXML
    private void clickLocationPanel(MouseEvent mouseEvent)
    {
        IPlace selectedLocation = locationPanel.getSelectionModel().getSelectedItem();

        if(selectedLocation != null) {
            String command = COMMAND_GOTO + " " + selectedLocation.name();
            executeCommand(command);
        }
    }

    /*******************************************************************
     * Metoda, která zachycuje click event u panelu batohu a zajišťuje
     * poslání příkazu hře, aby položila item do aktuálního prostoru.
     */
    @FXML
    private void clickBagPanel(MouseEvent mouseEvent)
    {
        IItem selectedItem = bagItemsPanel.getSelectionModel().getSelectedItem();

        if(selectedItem != null)
        {
            String command = COMMAND_PUT + " " + selectedItem.name();
            executeCommand(command);
        }
    }

    /*******************************************************************
     * Metoda, která zachycuje click event u panelu itemů v prostoru
     * a zajišťuje poslání příkazu hře, aby vzala item z aktuálního
     * prostoru do batohu.
     */
    @FXML
    private void clickPlaceItemPanel(MouseEvent mouseEvent)
    {
        IItem selectedItem = placeItemsPanel.getSelectionModel().getSelectedItem();

        if(selectedItem != null)
        {
            String command = COMMAND_TAKE + " " + selectedItem.name();
            executeCommand(command);
        }
    }

    /*********************************************************************
     * Metoda, která čte user input z textfieldu a předává příkaz hře.
     */
    @FXML
    private void sendUserInput(ActionEvent actionEvent)
    {
        String command = userInput.getText();
        userInput.clear();

        executeCommand(command);
    }

    /*******************************************************************
     * Metoda, která posílá příkazy hře a popřípadě updatuje panely
     * na začátku/konci hry.
     */
    private void executeCommand(String command)
    {
        consoleOutput.appendText("> " + command.toUpperCase() + "\n\n");
        String gameOutput = game.executeCommand(command);
        consoleOutput.appendText(gameOutput + "\n\n");

        if(command.equalsIgnoreCase(COMMAND_END) || command.isEmpty() || command.equalsIgnoreCase(COMMAND_WIN))
        {
            updateItemsPanels();
            updateOnGameEnd();
        }
    }

    /*******************************************************************
     * Metoda, která vytvoří alert a případně ukončí hru a zavře okno.
     */
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

    /*******************************************************************
     * Metoda, která zobrazí webview nápovědy.
     */
    @FXML
    private void showHelp(ActionEvent actionEvent)
    {
        Stage helpStage = new Stage();
        helpStage.setTitle("Nápověda");
        WebView vw = new WebView();
        Scene helpScene = new Scene(vw);
        helpStage.setScene(helpScene);
        helpStage.show();
        vw.getEngine().load(getClass().getResource("help.html").toExternalForm());
    }

    /*******************************************************************
     * Metoda, která založí novou hru.
     */
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

    /************************************************************************
     * Metoda, která teleportne hráče do náhodného prostoru.
     */
    public void teleportPlayer(ActionEvent actionEvent)
    {
        List<IPlace> allPlaces = new ArrayList<>(game.world().places());
        allPlaces.remove(game.world().currentPlace());

        int randomIndex = randomGenerator.nextInt(allPlaces.size());
        IPlace randomPlace = allPlaces.get(randomIndex);

        game.world().setCurrentPlace(randomPlace);

        consoleOutput.appendText("> " + "TELEPORT" + "\n\n");
        consoleOutput.appendText("Byl jsi teleportován do náhodného prostoru: " + randomPlace.name() + "\n\n");
    }
}
