package cz.vse.java.kedv00.adventura.main;

import cz.vse.java.kedv00.adventura.src.Game;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Scanner;

/******************************************************************
 * Třída {@code Start} je hlavní třídou projektu,
 * který ...
 *
 * @author Vojtěch Keder
 * @version 2023_Winter
 */
public class Start extends Application
{
    /**************************************************************
     * Metoda, přes kterou se spoužtí celá aplikace
     * buď v textovém, nebo grafickém módu.
     *
     * @param args Parametry příkazového řádku
     */
    public static void main(String[] args)
    {
        if(args.length > 0 && args[0].equals("-text"))
        {
            Game game = Game.getInstance();
            Scanner input = new Scanner(System.in);
            String userInput;

            while (!game.isAlive()) {
                System.out.println("Zmáčkni ENTER pro odstartování hry.");
                userInput = input.nextLine();
                System.out.println(game.executeCommand(userInput));
            }

            while (game.isAlive()) {
                System.out.println("\nZadej příkaz:\n>");
                userInput = input.nextLine();
                System.out.println(game.executeCommand(userInput));
            }

            Platform.exit();
        }
        else
        {
            launch();
        }
    }

    /*************************************************************************
     * Startovací metoda pro přípravu stage.
     *
     * @param primaryStage Primární stage
     * @throws Exception JavaFX exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home.fxml")));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Adventura");
        primaryStage.show();
    }
}
