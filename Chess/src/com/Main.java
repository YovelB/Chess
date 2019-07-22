package com;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Main.primaryStage = primaryStage;
        Parent root1 = FXMLLoader.load(getClass().getResource("gui/PrimaryScene.fxml"));
        Scene primaryScene = new Scene(root1);
        primaryStage.getIcons().add(new Image("/wq.png"));
        primaryStage.setTitle("Chess");
        primaryStage.setScene(primaryScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

     /**
     *
     * An RFCOMM Bluetooth URL follows the structure:
     * <ul>
     * <li>btspp://</li>
     * <li>bluetooth address</li>
     * <li>CN (equivalent of a TCP/IP port for the service you want to use)</li>
     * </ul>
     *
     * For reference, here's an example address from my Arduino:
     * btspp://98D3318041DE:1.
     */
    public static void main(String[] args) {
        launch(args);
    }
}