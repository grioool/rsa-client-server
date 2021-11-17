package by.grigorieva.olga.messages;

public class ServerPublicKey extends SystemMessage{

    private final byte[] publicKey;

    public ServerPublicKey(String sender, byte[] publicKey){
        super(sender);
        this.publicKey = publicKey;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }
}
