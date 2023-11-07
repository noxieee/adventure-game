package cz.vse.java.kedv00.adventura.main;

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

    @FXML
    private void sendUserInput(ActionEvent actionEvent)
    {
        consoleOutput.appendText(userInput.getText() + '\n');
        userInput.clear();
    }
}
