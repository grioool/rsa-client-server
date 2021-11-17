package by.grigorieva.olga.constants;

import javafx.util.StringConverter;

public class StringNumberConverter extends StringConverter<Number> {
    @Override
    public String toString(Number number) {
        return String.valueOf(number.intValue());
    }

    @Override
    public Number fromString(String string){
        return Integer.valueOf(string);
    }
}
