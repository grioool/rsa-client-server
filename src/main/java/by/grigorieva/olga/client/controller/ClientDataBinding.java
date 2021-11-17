package by.grigorieva.olga.client.controller;

import by.grigorieva.olga.client.ui.ColoredMessage;
import by.grigorieva.olga.constants.DateTime;
import by.grigorieva.olga.constants.ExampleParam;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import org.apache.commons.lang.RandomStringUtils;

public class ClientDataBinding {

    public final static String temporalFileStorage = "/Users/olga/Курсовая/new-new-rsa-client-server/downloads";
    private final StringProperty username = new SimpleStringProperty(ExampleParam.USERNAME + RandomStringUtils.randomNumeric(5));
    private final StringProperty host = new SimpleStringProperty(ExampleParam.HOST);
    private final IntegerProperty port = new SimpleIntegerProperty(ExampleParam.PORT);
    private final BooleanProperty isConnected = new SimpleBooleanProperty(false);
    private final ListProperty<String> connectedUsers = new SimpleListProperty<>();
    private final ListProperty<ColoredMessage> chatMessages = new SimpleListProperty();

    public String getUsername(){
        return username.get();
    }

    public StringProperty usernameProperty(){
        return username;
    }

    public void setUsername(String username){
        this.username.set(username);
    }

    public String getHost(){
        return host.get();
    }

    public StringProperty hostProperty(){
        return host;
    }

    public void setHost(String host){
        this.host.set(host);
    }

    public int getPort(){
        return port.get();
    }

    public IntegerProperty portProperty(){
        return port;
    }

    public void setPort(int port){
        this.port.set(port);
    }

    public boolean isConnected(){
        return isConnected.get();
    }

    public void setIsConnected(boolean isConnected){
        this.isConnected.set(isConnected);
    }

    public ObservableList<String> getConnectedUsers(){
        return connectedUsers.get();
    }

    public ListProperty<String> connectedUsersProperty(){
        return connectedUsers;
    }

    public void setConnectedUsers(ObservableList<String> connectedUsers){
        this.connectedUsers.set(connectedUsers);
    }

    public ObservableList<ColoredMessage> getChatMessages(){
        return chatMessages.get();
    }

    public ListProperty<ColoredMessage> chatMessagesProperty(){
        return chatMessages;
    }

    public void setChatMessages(ObservableList<ColoredMessage> chatMessages){
        this.chatMessages.set(chatMessages);
    }

    public void systemMessage(String text){
        final String message = "[" + DateTime.getTime() + "]" + text;
        ColoredMessage colored = new ColoredMessage(message, Color.PINK, 10);
        Platform.runLater(() -> getChatMessages().add(colored));
    }

    public void chatMessage(String text, Color color){
        final String message = "[" + DateTime.getTime() + "]" + text;
        ColoredMessage colored = new ColoredMessage(message, color);
        Platform.runLater(() -> getChatMessages().add(colored));
    }

    public void fileMessage(String fileName){
        final String message = "[" + DateTime.getTime() + "]" + fileName;
        ColoredMessage colored = new ColoredMessage(message, Color.HOTPINK);
        Platform.runLater(() -> getChatMessages().add(colored));
    }
}
