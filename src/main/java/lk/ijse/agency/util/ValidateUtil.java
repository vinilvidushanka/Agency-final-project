package lk.ijse.agency.util;

import javafx.scene.control.TextField;

import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ValidateUtil {
    public static Object validation(LinkedHashMap<TextField, Pattern> map) {
        for (TextField key : map.keySet()) {
            Pattern pattern = map.get(key);
            if (pattern.matcher(key.getText()).matches()) {
                removeError(key);
                return key;
            } else {
                addError(key);
            }
        }
        return true;
    }

    private static void addError(TextField key) {
        key.setStyle("-fx-border-color: red; -fx-border-radius: 10; -fx-background-radius: 10");
    }

    private static void removeError(TextField key) {
        key.setStyle("-fx-border-color: green; -fx-border-radius: 10; -fx-background-radius: 10");
    }
}