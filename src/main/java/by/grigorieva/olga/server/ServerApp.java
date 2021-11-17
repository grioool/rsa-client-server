package by.grigorieva.olga.server;

import by.grigorieva.olga.server.controller.ServerController;
import by.grigorieva.olga.server.controller.ServerDataBinding;
import by.grigorieva.olga.server.ui.ServerView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.crypto.KeyGenerator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class ServerApp extends Application {
    private ServerDataBinding binding;
    private ServerView serverView;
    private ServerController serverController;

    @Override
    public void start(Stage primaryStage){
        binding = new ServerDataBinding();
        serverView = new ServerView(binding);
        serverController = serverView.getServerController();

        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        Scene scene = new Scene(serverView.getRoot());
        primaryStage.setTitle("Server");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        /*KeyPairGenerator keyGenerator = null;
        try {
            keyGenerator = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = keyGenerator.generateKeyPair();
        Files.write(Paths.get("public.txt"), keyPair.getPublic().getEncoded());
        Files.write(Paths.get("private.txt"), keyPair.getPrivate().getEncoded());
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }*/
        launch(args);
    }
}
