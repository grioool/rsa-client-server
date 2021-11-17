package by.grigorieva.olga.server.features;

import by.grigorieva.olga.cryptography.RSAUtil;
import by.grigorieva.olga.messages.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.FileNameMap;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class ServerSocketThread extends Thread {
    private static Logger logger = LoggerFactory.getLogger(ServerSocketThread.class);

    private final Socket socket;
    private final ChatServerSocket chatServerSocket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private boolean isDisconnected;
    private String username;

    public RSAUtil getCipher() {
        return cipher;
    }

    private RSAUtil cipher;

    public ServerSocketThread(Socket socket, ChatServerSocket chatServerSocket) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException {
        this.socket = socket;
        this.chatServerSocket = chatServerSocket;
        cipher = new RSAUtil();
    }

@Override
    public void run() {
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            logger.error("", e);
        }

        do {
            try {
                processMessages();
            } catch (IOException | ClassNotFoundException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException | InvalidKeyException e) {
                e.printStackTrace();
                isDisconnected = true;
                chatServerSocket.removeClient(this);
                logger.error(e.getMessage());
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        } while (!isDisconnected);
    }

    private void processMessages() throws IOException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        final Message message = (Message) objectInputStream.readObject();
        logger.info("message received = " + message);

        if (message instanceof SystemMessage) {

            if (message instanceof ClientConnectedSystemMessage) {
                username = message.getSender();
                cipher.setForeignPublicKey(((ClientConnectedSystemMessage) message).getPublicKey());
                chatServerSocket.getBinding().addLogMessage(username + " is connected");
                sendMessage(new ServerPublicKey("server", cipher.getPublic()));
                chatServerSocket.sendBroadcastMessage(message);
                chatServerSocket.sendUserListUpdated();

            } else if (message instanceof ClientDisconnectedSystemMessage) {
                chatServerSocket.getBinding().addLogMessage(username + " is disconnected");
                chatServerSocket.removeClient(this);
                chatServerSocket.sendBroadcastMessage(message);
                chatServerSocket.sendUserListUpdated();
            }
        } else if (message instanceof TextMessage) {
            ((TextMessage) message).setText(
                    cipher.decrypt(((TextMessage) message).getText())
            );
            chatServerSocket.sendBroadcastMessage(message);
        } else if(message instanceof FileMessage) {
            chatServerSocket.sendBroadcastMessage(message);
        }
    }

    public void sendMessage(Message message) throws IOException {
        logger.info("sending message " + message);
        objectOutputStream.writeObject(message);
    }

    public String getUsername() {
        return username;
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }
}
