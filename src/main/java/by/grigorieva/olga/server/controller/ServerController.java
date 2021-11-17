package by.grigorieva.olga.server.controller;

import by.grigorieva.olga.client.controller.ClientDataBinding;
import by.grigorieva.olga.server.features.ChatServerSocket;
import by.grigorieva.olga.server.ui.ServerView;

public class ServerController {

    private final ServerDataBinding binding;
    private final ServerView serverView;
    private ChatServerSocket chatServerSocket;

    public ServerController(ServerDataBinding binding, ServerView serverView){
        this.binding = binding;
        this.serverView = serverView;
    }

    public void startServer(){
        chatServerSocket = new ChatServerSocket(binding);
        chatServerSocket.start();
    }

    public void stopServer(){
        chatServerSocket.stopServer();
    }
}
