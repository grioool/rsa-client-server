package by.grigorieva.olga.server.features;

import by.grigorieva.olga.messages.*;
import by.grigorieva.olga.server.controller.ServerDataBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatServerSocket extends Thread{
    private static Logger logger = LoggerFactory.getLogger(ChatServerSocket.class);
    private ServerSocket serverSocket;
    private final ServerDataBinding binding;
    private Set<ServerSocketThread> socketServerClient = new HashSet<>();

    public ChatServerSocket(ServerDataBinding binding){
        this.binding = binding;
    }

    @Override
    public void run(){
        startServer();
    }

    public void removeClient(ServerSocketThread serverSocketThread){
        socketServerClient.remove(serverSocketThread);
    }

    public ServerDataBinding getBinding(){
        return binding;
    }

    private void startServer(){
        try{
            serverSocket = new ServerSocket(binding.getPort(),50, InetAddress.getByName(binding.getHost()));
        }catch(IOException e){
            logger.error("", e);
        }

        logger.info("Server is listening on " + binding.getHost() + ":" + binding.getPort());

        do{
            Socket clientSocket;
            try{
                clientSocket = serverSocket.accept();
                ServerSocketThread serverSocketThread = new ServerSocketThread(clientSocket, this);
                serverSocketThread.start();

                socketServerClient.add(serverSocketThread);

            }catch(IOException | NoSuchPaddingException | NoSuchAlgorithmException ignored){
            }
        }while(binding.isRunning());
    }

    void sendBroadcastMessage(Message message){
        sendBroadcastMessage(message, null);
    }

    void sendBroadcastMessage(Message message, List<ServerSocketThread> excludedServerSocketThreads){
        String text= "";
        if(message instanceof TextMessage)
            text = ((TextMessage) message).getText();

        for(ServerSocketThread serverSocketThread : socketServerClient){
            if(excludedServerSocketThreads == null || !excludedServerSocketThreads.contains(serverSocketThread)){
                try{
                    if(message instanceof TextMessage)
                        ((TextMessage) message).setText(
                                serverSocketThread.getCipher().encrypt(text)
                        );
                       serverSocketThread.sendMessage(message);
                }catch (IOException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e){
                    binding.addLogMessage(serverSocketThread.getUsername() + " is probably disconnected: remove connection by timeout");
                    logger.error(serverSocketThread.getUsername() + " is probably disconnected: remove connection by timeout");
                    serverSocketThread.interrupt();
                }
            }
        }
    }

    public void sendUserListUpdated(){
        final List<String> userList = new ArrayList<>(socketServerClient.size());
        for(ServerSocketThread serverSocketThread : socketServerClient){
            userList.add(serverSocketThread.getUsername());
        }

        final UserListUpdatedSystemMessage userListUpdatedSystemMessage = new UserListUpdatedSystemMessage("server");
        userListUpdatedSystemMessage.setUserList(userList);
        sendBroadcastMessage(userListUpdatedSystemMessage);
    }

    public void stopServer(){
        sendBroadcastMessage(new ServerGoingDownMessage());
        for(ServerSocketThread serverSocketThread : socketServerClient){
            serverSocketThread.disconnect();
        }
        try{
            serverSocket.close();
        }catch (IOException e){
            logger.error("", e);
        }
        logger.info("Server is stopped");
    }
}
