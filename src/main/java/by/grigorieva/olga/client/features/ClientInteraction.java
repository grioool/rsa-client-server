package by.grigorieva.olga.client.features;

import by.grigorieva.olga.client.controller.ClientDataBinding;
import by.grigorieva.olga.client.ui.ColoredMessage;
import by.grigorieva.olga.constants.DateTime;
import by.grigorieva.olga.cryptography.RSAUtil;
import by.grigorieva.olga.messages.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.Base64;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class ClientInteraction {
    private static Logger logger = LoggerFactory.getLogger(ClientInteraction.class);

    private final ClientDataBinding binding;
    private ObjectOutputStream objectOutputStream;
    private ClientReadThread readThread;
    private Socket socket;
    private RSAUtil cipher;

    public ClientInteraction(ClientDataBinding binding) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException {
        cipher = new RSAUtil();
        this.binding = binding;
    }

    public Socket getSocket(){
        return socket;
    }

    public void connect(){
        try {
            socket = new Socket(binding.getHost(), binding.getPort());

            logger.info("connection " + binding.getHost() + ":" + binding.getPort() + "established");
            Platform.runLater(() -> binding.setChatMessages(FXCollections.observableArrayList(Collections.singletonList(new ColoredMessage("[" + DateTime.getTime() + "] connection " + binding.getHost() + ":" + binding.getPort() + " established", Color.PINK, 10)))));

            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(new ClientConnectedSystemMessage(binding.getUsername(), cipher.getPublic()));

            readThread = new ClientReadThread(socket, this);
            readThread.start();

            binding.setIsConnected(true);

        }catch(IOException e){
            logger.error(e.getMessage());
        }
    }

    public void disconnect(){
        try{
            sendSystemMessage(new ClientDisconnectedSystemMessage(getDataBinding().getUsername()));
            socket.close();
        }catch (IOException ignored){
        }
    }

    public void sendMessage(String text) throws IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
       text = cipher.encrypt(text);
        objectOutputStream.writeObject(new TextMessage(binding.getUsername(), text));
    }

    public void sendFileMessage(String filename, byte[] content) throws IOException {
        objectOutputStream.writeObject(new FileMessage(binding.getUsername(), content, filename));
    }

    public void sendSystemMessage(SystemMessage systemMessage) throws IOException{
        objectOutputStream.writeObject(systemMessage);
    }

    public ClientDataBinding getDataBinding(){
        return binding;
    }

    public RSAUtil getCipher() {
        return cipher;
    }
}
