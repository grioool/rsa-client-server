package by.grigorieva.olga.client.ui;

import by.grigorieva.olga.client.controller.ClientDataBinding;
import by.grigorieva.olga.constants.StringNumberConverter;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class ClientConnectView {

        private final Dialog<Boolean> settingsDialog = new Dialog<>();
        private final GridPane gridPane = new GridPane();
        private final Label labelHost = new Label();
        private final TextField textFieldHost = new TextField();
        private final Label labelPort = new Label();
        private final TextField textFieldPort = new TextField();
        private final Label labelUsername = new Label();
        private final TextField textFieldUsername = new TextField();

        private final ClientDataBinding chatClientModel;

        public ClientConnectView(ClientDataBinding chatClientModel) {
            this.chatClientModel = chatClientModel;

            createUI();
        }

        private void createUI() {
            settingsDialog.setTitle("Client");

            ButtonType buttonTypeConnect = new ButtonType("Connect", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeExit = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);
            settingsDialog.getDialogPane().getButtonTypes().addAll(buttonTypeConnect, buttonTypeExit);
            settingsDialog.getDialogPane().setStyle("-fx-background-color: #f8b3cb");
            settingsDialog.getDialogPane().lookupButton(buttonTypeConnect).setStyle("-fx-background-color: #e888ad; -fx-font-family: monospace");
            settingsDialog.getDialogPane().lookupButton(buttonTypeExit).setStyle("-fx-background-color: #e888ad; -fx-font-family: monospace");

            gridPane.setHgap(20);
            gridPane.setVgap(20);
            gridPane.setPadding(new Insets(10));
            gridPane.setStyle("-fx-background-color: #f8b3cd; -fx-font-family: monospace");

            labelHost.setText("Host:");
            labelHost.setLabelFor(textFieldHost);
            textFieldHost.setText(chatClientModel.getHost());

            labelPort.setText("Port:");
            labelPort.setLabelFor(textFieldPort);
            textFieldPort.setText(String.valueOf(chatClientModel.getPort()));

            labelUsername.setText("Username:");
            labelUsername.setLabelFor(textFieldUsername);
            textFieldUsername.setText(chatClientModel.getUsername());
            textFieldUsername.setStyle("-fx-font-family: monospace");
            textFieldHost.setStyle("-fx-font-family: monospace");
            textFieldPort.setStyle("-fx-font-family: monospace");

            textFieldHost.textProperty().bindBidirectional(chatClientModel.hostProperty());
            textFieldPort.textProperty().bindBidirectional(chatClientModel.portProperty(), new StringNumberConverter());
            textFieldUsername.textProperty().bindBidirectional(chatClientModel.usernameProperty());

            gridPane.add(labelHost, 0, 0);
            gridPane.add(textFieldHost, 1, 0);
            gridPane.add(labelPort, 0, 1);
            gridPane.add(textFieldPort, 1, 1);
            gridPane.add(labelUsername, 0, 2);
            gridPane.add(textFieldUsername, 1, 2);

            Node buttonConnect = settingsDialog.getDialogPane().lookupButton(buttonTypeConnect);

            textFieldHost.textProperty().addListener((observable, oldValue, newValue) -> buttonConnect.setDisable(newValue.trim().isEmpty()));
            textFieldPort.textProperty().addListener((observable, oldValue, newValue) -> buttonConnect.setDisable(newValue.trim().isEmpty()));
            textFieldUsername.textProperty().addListener((observable, oldValue, newValue) -> buttonConnect.setDisable(newValue.trim().isEmpty()));

            settingsDialog.getDialogPane().setContent(gridPane);

            settingsDialog.setResultConverter(dialogButton -> dialogButton == buttonTypeConnect);
        }

        public boolean showAndWait() {
            return settingsDialog.showAndWait().orElse(false);
        }
    }
