package by.grigorieva.olga.messages;

public class FileMessage extends Message{
   private byte[] fileMessage;
   private String fileName;

    public FileMessage(String sender, byte[] fileMessage, String fileName) {
        super(sender);
        this.fileMessage = fileMessage;
        this.fileName = fileName;
    }

    public void setFileMessage(byte[] fileMessage) {
        this.fileMessage = fileMessage;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileMessage() {
        return fileMessage;
    }

    public String getFileName() {
        return fileName;
    }

}
