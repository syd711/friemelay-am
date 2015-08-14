package de.friemelay.am.ui.util;

import de.friemelay.am.resources.ResourceLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

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
    textBox.setEditable(false);
    GridPane.setMargin(textBox, new Insets(5, 5, 5, 10));
    GridPane.setConstraints(textBox, 1, row);
    grid.getChildren().addAll(condLabel, textBox);
    return textBox;
  }

  public static GridPane createFormGrid() {
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(10, 10, 10, 10));
    ColumnConstraints colInfo2 = new ColumnConstraints();
    colInfo2.setPercentWidth(20);
    ColumnConstraints colInfo3 = new ColumnConstraints();
    colInfo3.setPercentWidth(80);
    grid.getColumnConstraints().add(colInfo2); //25 percent
    grid.getColumnConstraints().add(colInfo3); //50 percent
    return grid;
  }

  public static TitledPane createSection(Pane root, Node child, String title, boolean collapsed) {
    TitledPane group = new TitledPane(title, child);
    group.setPadding(new Insets(10, 10, 5, 10));
    root.getChildren().add(group);
    group.setExpanded(!collapsed);
    return group;
  }

  public static TitledPane createSection(Pane root, Node child, String title) {
    return createSection(root, child, title, false);
  }

  public static Button createButton(Pane parent, String label, EventHandler<ActionEvent> handler) {
    return createButton(parent, label, null, handler);
  }

  public static Button createButton(Pane parent, String label, String image, EventHandler<ActionEvent> handler) {
    Button button = new Button(label);
    if(image != null) {
      button = new Button(label, ResourceLoader.getImageView(image));
    }
    parent.getChildren().addAll(button);
    button.setOnAction(handler);

    return button;
  }
}
