package by.grigorieva.olga.messages;

public class TextMessage extends Message{
    protected String text;

    public TextMessage(String sender, String text) {
        super(sender);
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "TextMessage{" +
                "text='" + text + '\'' +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                '}';
    }
}
