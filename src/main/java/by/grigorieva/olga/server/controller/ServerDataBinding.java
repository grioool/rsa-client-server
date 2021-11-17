package by.grigorieva.olga.server.controller;

import by.grigorieva.olga.constants.DateTime;
import by.grigorieva.olga.constants.ExampleParam;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.ObservableList;

public class ServerDataBinding {
    private final StringProperty host = new SimpleStringProperty(ExampleParam.HOST);
    private final IntegerProperty port = new SimpleIntegerProperty(ExampleParam.PORT);
    private final BooleanProperty isRunning = new SimpleBooleanProperty(false);
    private final ListProperty<String> logMessages = new SimpleListProperty<>();

    public String getHost(){
        return host.get();
    }

    public StringProperty hostProperty(){
        return host;
    }

    public int getPort(){
        return port.get();
    }

    public IntegerProperty portProperty(){
        return port;
    }

    public boolean isRunning(){
        return isRunning.get();
    }

    public BooleanProperty isRunningProperty(){
        return isRunning;
    }

    public void setIsRunning(boolean isRunning){
        this.isRunning.set(isRunning);
    }

    public ObservableList<String> getLogMessages() {
        return logMessages.get();
    }

    public ListProperty<String> logMessagesProperty() {
        return logMessages;
    }

    public void setLogMessages(ObservableList<String> logMessages) {
        this.logMessages.set(logMessages);
    }

    public void addLogMessage(String text) {
        final String logMessage = "[" + DateTime.getTime() + "]: " + text;
        Platform.runLater(() -> getLogMessages().add(logMessage));
    }
}
