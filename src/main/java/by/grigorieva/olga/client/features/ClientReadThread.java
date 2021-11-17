package by.grigorieva.olga.client.features;

import by.grigorieva.olga.client.controller.ClientDataBinding;
import by.grigorieva.olga.cryptography.RSAUtil;
import by.grigorieva.olga.messages.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.paint.Color;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class ClientReadThread extends Thread {
    private static Logger logger = LoggerFactory.getLogger(ClientReadThread.class);
    private ObjectInputStream objectInputStream;
    private final Socket clientSocket;
    private final ClientInteraction client;

    public ClientReadThread(Socket clientSocket, ClientInteraction client) throws IOException {
        this.client = client;
        this.clientSocket = clientSocket;
        this.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
    }

    @Override
    public void run() {
        boolean connected = true;
        while (connected) {
            try {
                final Message message = (Message) objectInputStream.readObject();
                logger.info("message " + message);

                if (message instanceof SystemMessage) {

                    if (message instanceof ClientConnectedSystemMessage) {
                        client.getDataBinding().systemMessage(message.getSender() + " is connected");

                    } else if (message instanceof ServerPublicKey) {
                        client.getCipher().setForeignPublicKey(((ServerPublicKey) message).getPublicKey());

                    } else if (message instanceof ClientDisconnectedSystemMessage) {
                        client.getDataBinding().systemMessage(message.getSender() + " is disconnected");

                    } else if (message instanceof UserListUpdatedSystemMessage) {
                        final List<String> userList = ((UserListUpdatedSystemMessage) message).getUserList();
                        Platform.runLater(() -> client.getDataBinding().setConnectedUsers(FXCollections.observableList(userList)));

                    } else if (message instanceof ServerGoingDownMessage) {
                        client.getDataBinding().systemMessage("server is going down -> closing connection..");
                        client.getSocket().close();
                        System.exit(0);
                    }

                } else if (message instanceof TextMessage) {
                    String content = ((TextMessage) message).getText();
                    String textMessage = message.getSender() + ": " + client.getCipher().decrypt(content);
                    client.getDataBinding().chatMessage(textMessage, Color.GRAY);
                } else if (message instanceof FileMessage) {
                    byte[] content = ((FileMessage) message).getFileMessage();
                    String fileName = ((FileMessage) message).getFileName();
                    String fileMessage = message.getSender() + ": " + fileName;

                    Files.write(Paths.get(ClientDataBinding.temporalFileStorage + "/" + fileName), content);
                    client.getDataBinding().fileMessage(fileMessage);
                }
            } catch (IOException | ClassNotFoundException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException ignored) {
                ignored.printStackTrace();
                if (ignored instanceof EOFException) break;
            }
        }
    }
}
