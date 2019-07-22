package com.gui;

import com.Main;
import com.engine.Alliance;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PrimaryController implements Initializable {

    @FXML
    private Button playButton;

    @FXML
    private Button connectButton;

    @FXML
    private Label addressLabel;

    @FXML
    private TextField addressField;

    @FXML
    private Label allianceLabel;

    @FXML
    private ChoiceBox<String> allianceChoiceBox;

    private static StreamConnection connection;
    private static DataOutputStream dataOut;
    private static String chosenAlliance;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allianceChoiceBox.getItems().addAll("White", "Black");
        allianceChoiceBox.setValue("White");
        chosenAlliance =  allianceChoiceBox.getValue();
        allianceChoiceBox.setVisible(false);
        playButton.setVisible(false);
    }

    @FXML
    void connectOnMouseClick() {
        try {
            if(addressField.getText().isEmpty() || addressField.getText().length() != 12) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                if(addressField.getText().isEmpty()) {
                    alert.setHeaderText("Error: Address Field is empty!");
                    alert.setContentText("Please enter the bluetooth address in the field");
                } else if(addressField.getText().length() < 12) {
                    alert.setHeaderText("Error: Address Field is too short!");
                    alert.setContentText("Please enter the full bluetooth address in the field");
                } else {
                    alert.setHeaderText("Error: Address Field is too long!");
                    alert.setContentText("Please enter the full bluetooth address in the field");
                }
                alert.showAndWait();
            } else {
                connection = (StreamConnection) Connector.open("btspp://" + addressField.getText() + ":1");
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error: bluetooth with this address not found!");
            alert.setContentText("Please enter the right bluetooth address in the field");
            alert.showAndWait();
        }
        if (connection != null) {
            try {
                // Initializes the stream.
                dataOut = connection.openDataOutputStream();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error: bluetooth communication failed!");
                alert.setContentText("Please try connecting again");
                alert.showAndWait();
            }
            if (dataOut != null) {
                addressLabel.setVisible(false);
                addressField.setVisible(false);
                allianceLabel.setVisible(true);
                allianceChoiceBox.setVisible(true);
                connectButton.setVisible(false);
                playButton.setVisible(true);
            }
        }
    }

    @FXML
    void playOnMouseClicked() {
        // load the second fxml file
        chosenAlliance = allianceChoiceBox.getValue();
        Parent root2 = null;
        try {
            root2 = FXMLLoader.load(getClass().getResource("Chess.fxml"));
            dataOut.writeUTF("Start");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (root2 != null) {
            Scene gameScene = new Scene(root2);
            Main.primaryStage.setScene(gameScene);
        }
    }

    static Alliance getChosenPlayerAlliance() {
        if(chosenAlliance.equals("White")) {
            return Alliance.WHITE;
        } else {
            return Alliance.BLACK;
        }
    }

    static StreamConnection getBluetoothConnection() {
        return connection;
    }

    static DataOutputStream getDataOutputStream() {
        return dataOut;
    }
}
