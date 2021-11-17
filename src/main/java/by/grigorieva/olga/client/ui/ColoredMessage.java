package by.grigorieva.olga.client.ui;

import javafx.scene.paint.Color;

public class ColoredMessage {
    private final String text;
    private final Color color;
    private final double fontSize;

    public ColoredMessage(String text, Color color){
        this(text, color, 0);
    }

    public ColoredMessage(String text, Color color, double fontSize){
        this.text = text;
        this.color = color;
        this.fontSize = fontSize;
    }

    public String getText(){
        return text;
    }

    public Color getColor(){
        return color;
    }

    public double getFontSize(){
        return fontSize;
    }
}
