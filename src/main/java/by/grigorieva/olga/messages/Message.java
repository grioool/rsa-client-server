package by.grigorieva.olga.messages;

import java.io.Serializable;

public abstract class Message implements Serializable {
    protected String sender;
    protected String recipient;

    public Message(String sender){
        this.sender = sender;
    }

    public Message(String sender, String recipient){
        this.sender = sender;
        this.recipient = recipient;
    }

    public String getSender(){
        return sender;
    }
    @Override
    public String toString(){
        return "Message{" +
            "sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' + '}';
    }
}
