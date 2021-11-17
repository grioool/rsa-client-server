package by.grigorieva.olga.client;

import by.grigorieva.olga.client.controller.ClientController;
import by.grigorieva.olga.client.controller.ClientDataBinding;
import by.grigorieva.olga.client.ui.ClientView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class ClientApp extends Application {
    private ClientDataBinding binding;
    private ClientView clientView;
    private ClientController clientController;

    @Override
    public void start(Stage primaryStage){
        binding = new ClientDataBinding();
        clientView = new ClientView(binding);
        clientController = clientView.getClientController();
        try {
            clientController.connect();
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            // TODO
        }

        if(binding.isConnected()){
            primaryStage.setOnCloseRequest(e -> {
                if(binding.isConnected()){
                    clientController.disconnect();
                }
                Platform.exit();
                System.exit(0);
            });
            Scene scene = new Scene(clientView.getRoot());
            primaryStage.setTitle(binding.getUsername() + " client");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(binding.getUsername() + " client ERROR");
            alert.setHeaderText("Connection error");
            alert.setContentText("Looks like server is not started: " + System.lineSeparator() + "please start the server or check the connection settings");
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
