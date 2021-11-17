package by.grigorieva.olga.client.ui;

import by.grigorieva.olga.client.controller.ClientController;
import by.grigorieva.olga.client.controller.ClientDataBinding;
import by.grigorieva.olga.messages.FileMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;

public class ClientView {
    private static Logger logger = LoggerFactory.getLogger(ClientView.class);

    private final ClientDataBinding binding;
    private final ClientController controller;

    private final AnchorPane anchorPane = new AnchorPane();
    private final HBox hBoxChat = new HBox();
    private final ListView<String> listViewConnectedUsers = new ListView<>();

    private final VBox vBox = new VBox();
    private final ListView<ColoredMessage> listViewMessages = new ListView<>();

    private final HBox hBoxMessageSend = new HBox();
    private final TextField textFieldMessage = new TextField();
    private final Button buttonSend = new Button();
    private final ImageView buttonFileImg = new ImageView("image/pinkPlus.png");
    private final Button buttonFile = new Button("", buttonFileImg);


    public ClientView(ClientDataBinding binding) {
        this.binding = binding;
        this.controller = new ClientController(binding, this);

        final ClientConnectView clientConnectView = new ClientConnectView(binding);
        final boolean actionConnect = clientConnectView.showAndWait();
        if (!actionConnect) {
            System.exit(0);
        }

        composeLayout();
        bind();
    }

    private void composeLayout() {
        anchorPane.setPrefSize(400, 620);
        anchorPane.setStyle("-fx-background-color: #f8b3cb");

        hBoxChat.setAlignment(Pos.CENTER);
        hBoxChat.setSpacing(10);
        hBoxChat.setPadding(new Insets(10));
        hBoxChat.setStyle("-fx-font-family: monospace");


        listViewConnectedUsers.setPrefSize(140, 600);
        listViewConnectedUsers.setEditable(false);
        listViewConnectedUsers.setMouseTransparent(false);
        listViewConnectedUsers.setStyle("-fx-font-family: monospace");

        vBox.setSpacing(10);

        listViewMessages.setPrefSize(400, 570);
        listViewMessages.setEditable(false);
        listViewMessages.setMouseTransparent(false);
        listViewMessages.setStyle("-fx-font-family: monospace");
        listViewMessages.setCellFactory(coloredTextListView -> new ListCell<ColoredMessage>() {
            @Override
            protected void updateItem(ColoredMessage item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setText(null);
                    setTextFill(null);
                } else {
                    setText(item.getText());
                    setTextFill(item.getColor());
                    if (item.getFontSize() != 0) {
                        setFont(new Font(item.getFontSize()));
                    }
                }
            }
        });

        listViewMessages.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY
                    && e.getClickCount() == 1) {
                ColoredMessage message = listViewMessages.getSelectionModel().getSelectedItem();
                if (message == null) return;
                String filePath = ClientDataBinding.temporalFileStorage + "/" + message.getText();
                filePath = filePath.substring(filePath.indexOf(":", filePath.indexOf("]")) + 1).trim();

                if (Files.exists(Paths.get(filePath))) {
                    Desktop desktop;
                    if (Desktop.isDesktopSupported()) {
                        desktop = Desktop.getDesktop();

                        try {
                            desktop.open(new File(filePath));
                        } catch (IOException ioe) {
                            logger.error("", ioe);
                        }
                    }
                }
            }
        });

        hBoxMessageSend.setPrefSize(400, 10);
        hBoxMessageSend.setSpacing(10);
        hBoxMessageSend.setStyle("-fx-background-color: #f8b3cb; -fx-font-family: monospace");


        textFieldMessage.setPrefWidth(280);

        buttonSend.setText("Send");
        buttonSend.setPrefWidth(60);
        buttonSend.setOnAction(this::buttonSend);
        buttonSend.setDefaultButton(true);
        buttonSend.setStyle("-fx-background-color: #e888ad; -fx-font-family: monospace");

        buttonFileImg.setFitHeight(18);
        buttonFileImg.setFitWidth(20);

        buttonFile.setPrefWidth(40);
        buttonFile.setStyle("-fx-background-color: #e888ad; -fx-font-family: monospace");
        buttonFile.setOnAction(this::buttonFile);

        hBoxMessageSend.getChildren().addAll(buttonFile, textFieldMessage, buttonSend);
        vBox.getChildren().addAll(listViewMessages, hBoxMessageSend);
        hBoxChat.getChildren().addAll(listViewConnectedUsers, vBox);
        anchorPane.getChildren().addAll(hBoxChat);

        Platform.runLater(textFieldMessage::requestFocus);
    }

    public void buttonSend(ActionEvent actionEvent) {
        String text = textFieldMessage.getText();
        if (!text.isEmpty()) {
            textFieldMessage.clear();
            try {
                controller.sendTextMessage(text);
            } catch (IOException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException | InvalidKeyException e) {
                logger.error("", e);
            }
        }
    }

    public void buttonFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose files");
        fc.setInitialDirectory(new File("."));
        File file = fc.showOpenDialog(buttonFile.getScene().getWindow());
        if(file != null) {
            try {
                controller.sendFileMessage(file.getName(), Files.readAllBytes(Paths.get(file.getAbsolutePath())));
            } catch (IOException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException | InvalidKeyException e) {
                logger.error("", e);
            }
        }
    }

    public ClientController getClientController() {
        return controller;
    }

    private void bind() {
        listViewMessages.itemsProperty().bind(binding.chatMessagesProperty());
        listViewConnectedUsers.itemsProperty().bind(binding.connectedUsersProperty());
    }

    public Parent getRoot() {
        return anchorPane;
    }
}
