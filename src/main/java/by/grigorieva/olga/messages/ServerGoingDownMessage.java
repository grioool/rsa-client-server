package by.grigorieva.olga.messages;

public class ServerGoingDownMessage extends SystemMessage {
    public ServerGoingDownMessage() {
        super("server");
    }

    @Override
    public String toString() {
        return "ServerGoingDownMessage{" +
                "sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                '}';
    }
}
