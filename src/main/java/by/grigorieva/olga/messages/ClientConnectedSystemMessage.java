package by.grigorieva.olga.messages;

public class ClientConnectedSystemMessage extends SystemMessage{

    private byte[] publicKey;

    public ClientConnectedSystemMessage(String sender, byte[] publicKey){
        super(sender);
        this.publicKey = publicKey;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    @Override
    public String toString(){
        return "ClientConnectedSystemMessage{" +
                "sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' + '}';
    }
}
