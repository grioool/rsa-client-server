package by.grigorieva.olga.server.ui;

import by.grigorieva.olga.constants.DateTime;
import by.grigorieva.olga.constants.StringNumberConverter;
import by.grigorieva.olga.server.controller.ServerController;
import by.grigorieva.olga.server.controller.ServerDataBinding;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import java.util.Collections;

public class ServerView {

    private final ServerDataBinding binding;
    private final ServerController serverController;

    private final AnchorPane anchorPane = new AnchorPane();
    private final VBox vBoxManagement = new VBox();
    private final HBox hBoxServerHostPort = new HBox();
    private final Label labelServerHost = new Label();
    private final TextField textFieldServerHost = new TextField();
    private final Label labelServerPort = new Label();
    private final TextField textFieldServerPort = new TextField();
    private final Label labelServerState = new Label();
    private final ToggleButton toggleButtonStartStop = new ToggleButton();
    private final ListView<String> listViewLog = new ListView<>();

    public ServerView(ServerDataBinding binding){
        this.binding = binding;
        this.serverController = new ServerController(binding, this);

        composeLayout();
        bind();
    }

    private void composeLayout(){
        anchorPane.setPrefHeight(500);
        anchorPane.setPrefWidth(500);
        anchorPane.setStyle("-fx-background-color: #f8b3cb" );

        vBoxManagement.setAlignment(Pos.CENTER);
        vBoxManagement.setSpacing(5);

        hBoxServerHostPort.setAlignment(Pos.CENTER);
        hBoxServerHostPort.setSpacing(5);
        hBoxServerHostPort.setPadding(new Insets(10));

        labelServerHost.setLabelFor(textFieldServerHost);
        labelServerHost.setFocusTraversable(false);
        labelServerHost.setText("Host: ");
        labelServerHost.setStyle("-fx-font-family: monospace");

        textFieldServerHost.setPrefWidth(150);
        textFieldServerHost.setStyle("-fx-font-family: monospace");

        labelServerPort.setLabelFor(textFieldServerPort);
        labelServerPort.setFocusTraversable(false);
        labelServerPort.setText("Port: ");
        labelServerPort.setStyle("-fx-font-family: monospace");

        textFieldServerPort.setPrefWidth(60);
        textFieldServerPort.setAlignment(Pos.CENTER_RIGHT);
        textFieldServerPort.setStyle("-fx-font-family: monospace");

        labelServerState.setLabelFor(toggleButtonStartStop);
        labelServerState.setFocusTraversable(false);
        labelServerState.setStyle("-fx-font-family: monospace");
        labelServerState.setText("Server is not running");

        toggleButtonStartStop.setPadding(new Insets(10));
        toggleButtonStartStop.setText("Start server");
        toggleButtonStartStop.setOnAction(this::buttonStartStopOnAction);
        toggleButtonStartStop.setStyle("-fx-background-color: #e888ad; -fx-font-family: monospace");

        listViewLog.setPrefWidth(600);
        listViewLog.setPrefHeight(336);
        listViewLog.setEditable(false);
        listViewLog.setMouseTransparent(false);
        listViewLog.setStyle("-fx-font-family: monospace; -fx-font-size: 10;");
        Platform.runLater(() -> binding.setLogMessages(FXCollections.observableArrayList(Collections.singletonList("[" + DateTime.getTime() + "]: Server application started"))));

        hBoxServerHostPort.getChildren().addAll(labelServerHost, textFieldServerHost, labelServerPort, textFieldServerPort);
        vBoxManagement.getChildren().addAll(hBoxServerHostPort, labelServerState, toggleButtonStartStop, listViewLog);
        anchorPane.getChildren().addAll(vBoxManagement);


    }

    private void bind() {
        textFieldServerHost.textProperty().bindBidirectional(binding.hostProperty());
        textFieldServerPort.textProperty().bindBidirectional(binding.portProperty(), new StringNumberConverter());

        binding.isRunningProperty().addListener((observable, oldValue, newValue) -> labelServerState.setText("Server is " + (newValue ? "" : "NOT") + " running"));
        listViewLog.itemsProperty().bindBidirectional(binding.logMessagesProperty());
    }

    public void buttonStartStopOnAction(ActionEvent actionEvent){
        if (binding.isRunning()) {
            serverController.stopServer();
            binding.setIsRunning(false);
        } else {
            binding.setIsRunning(true);
            serverController.startServer();
        }

        toggleButtonStartStop.setText((binding.isRunning() ? "Stop" : "Start") + " Server");
        textFieldServerHost.setDisable(binding.isRunning());
        textFieldServerPort.setDisable(binding.isRunning());

        binding.addLogMessage("Server " + (binding.isRunning() ? "is listening on " + binding.getHost() + ":" + binding.getPort() : "stopped"));

    }


    public ServerController getServerController() {
        return serverController;
    }

    public Parent getRoot() {
        return anchorPane;
    }
}
