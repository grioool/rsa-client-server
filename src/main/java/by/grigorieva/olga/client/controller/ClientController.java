package by.grigorieva.olga.client.controller;

import by.grigorieva.olga.client.features.ClientInteraction;
import by.grigorieva.olga.client.ui.ClientView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class ClientController {
    private static Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final ClientDataBinding binding;
    private final ClientView clientView;
    private ClientInteraction client;

    public ClientController(ClientDataBinding binding, ClientView clientView){
        this.binding = binding;
        this.clientView = clientView;
    }

    public void connect() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException {
        client = new ClientInteraction(binding);
        client.connect();
    }

    public void sendTextMessage(String text) throws IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        logger.info("sending text message: " + text);
        client.sendMessage(text);
    }

    public void sendFileMessage(String filename, byte[] content) throws IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        logger.info("sending file message: " + filename);
        client.sendFileMessage(filename, content);
    }

    public void disconnect(){
        client.disconnect();
    }
}
