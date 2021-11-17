package by.grigorieva.olga.messages;

public class ClientDisconnectedSystemMessage extends SystemMessage{
    public ClientDisconnectedSystemMessage(String sender){
        super(sender);
    }

    @Override
    public String toString(){
        return "ClientDisconnectedSystemMessage{" +
                "sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' + '}';
    }

}
