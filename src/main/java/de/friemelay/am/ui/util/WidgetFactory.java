package de.friemelay.am.ui.util;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.nio.charset.Charset;

/**
 *
 */
public class WidgetFactory {
  public static void showError(String message, Throwable e) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Fehler");
    alert.setHeaderText("Fehler");
    alert.setContentText(new String(("Ups, das hätte nicht passieren dürfen: " + e.getMessage()).getBytes(), Charset.forName("utf-8")));
    alert.showAndWait();
  }

  public static Label addFormLabel(GridPane grid, String label, String text, int row) {
    Label condLabel = new Label(label);
    GridPane.setHalignment(condLabel, HPos.RIGHT);
    GridPane.setConstraints(condLabel, 0, row);
    Label condValue = new Label(text);
    GridPane.setMargin(condValue, new Insets(5, 5, 5, 10));
    GridPane.setConstraints(condValue, 1, row);
    grid.getChildren().addAll(condLabel, condValue);
    return condValue;
  }

  public static TextField addFormTextfield(GridPane grid, String label, String text, int row) {
    Label condLabel = new Label(label);
    GridPane.setHalignment(condLabel, HPos.RIGHT);
    GridPane.setConstraints(condLabel, 0, row);
    TextField textBox = new TextField(text);
    GridPane.setMargin(textBox, new Insets(5, 5, 5, 10));
    GridPane.setConstraints(textBox, 1, row);
    grid.getChildren().addAll(condLabel, textBox);
    return textBox;
  }
}
