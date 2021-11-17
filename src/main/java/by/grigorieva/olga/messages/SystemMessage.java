package by.grigorieva.olga.messages;

public abstract class SystemMessage extends Message{
    public SystemMessage(String sender){
        super(sender);
    }

    public SystemMessage(String sender, String recipient){
        super(sender, recipient);
    }
}
