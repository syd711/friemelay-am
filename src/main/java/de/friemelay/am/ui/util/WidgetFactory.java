package de.friemelay.am.ui.util;

import de.friemelay.am.resources.ResourceLoader;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.nio.charset.Charset;

/**
 *
 */
public class WidgetFactory {
  public static void showError(String message, Throwable e) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Fehler");
    alert.setHeaderText("Fehler");
    alert.setContentText(new String(("Ups, das hätte nicht passieren dürfen: " + message+ " [" + e.getMessage() + "]").getBytes(), Charset.forName("utf-8")));
    alert.showAndWait();
  }

  public static boolean showConfirmation(String header, String message) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("");
    alert.setHeaderText(header);
    alert.setContentText(new String((message).getBytes(), Charset.forName("utf-8")));
    alert.showAndWait();
    ButtonType result = alert.getResult();
    return result.getButtonData().isDefaultButton();
  }

  public static Label addBindingFormLabel(GridPane grid, String label, StringProperty property, int row, StringConverter<String> converter) {
    Label condLabel = new Label(label);
    GridPane.setHalignment(condLabel, HPos.RIGHT);
    GridPane.setConstraints(condLabel, 0, row);
    Label condValue = new Label(property.get());
    if(converter != null) {
      condValue.textProperty().bindBidirectional(property, converter);
    }
    else {
      condValue.textProperty().bindBidirectional(property);
    }
    GridPane.setMargin(condValue, new Insets(5, 5, 5, 10));
    GridPane.setConstraints(condValue, 1, row);
    grid.getChildren().addAll(condLabel, condValue);
    return condValue;
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

  public static Label addFormLabel(GridPane grid, String label, int row, DoubleProperty property, StringConverter<Number> converter) {
    Label condLabel = new Label(label);
    GridPane.setHalignment(condLabel, HPos.RIGHT);
    GridPane.setConstraints(condLabel, 0, row);
    Label condValue = new Label(String.valueOf(property.get()));
    condValue.textProperty().bindBidirectional(property, converter);
    GridPane.setMargin(condValue, new Insets(5, 5, 5, 10));
    GridPane.setConstraints(condValue, 1, row);
    grid.getChildren().addAll(condLabel, condValue);
    return condValue;
  }

  public static Label addFormLabel(GridPane grid, String label, int row, StringProperty property, StringConverter<String> converter) {
    Label condLabel = new Label(label);
    GridPane.setHalignment(condLabel, HPos.RIGHT);
    GridPane.setConstraints(condLabel, 0, row);
    Label condValue = new Label(property.get());
    if(converter != null) {
      condLabel.textProperty().bindBidirectional(property, converter);
    }
    else {
      condLabel.textProperty().bindBidirectional(property);
    }

    GridPane.setMargin(condValue, new Insets(5, 5, 5, 10));
    GridPane.setConstraints(condValue, 1, row);
    grid.getChildren().addAll(condLabel, condValue);
    return condValue;
  }

  public static TextField addBindingFormTextfield(GridPane grid, String label, StringProperty property, int row, boolean editable, ChangeListener<String> listener) {
    Label condLabel = new Label(label);
    GridPane.setHalignment(condLabel, HPos.RIGHT);
    GridPane.setConstraints(condLabel, 0, row);
    TextField textBox = new TextField(property.get());
    Bindings.bindBidirectional(property, textBox.textProperty());
    if(listener != null) {
      property.addListener(listener);
    }
    textBox.setEditable(editable);
    GridPane.setMargin(textBox, new Insets(5, 5, 5, 10));
    GridPane.setConstraints(textBox, 1, row);
    grid.getChildren().addAll(condLabel, textBox);
    return textBox;
  }


  public static TextField addFormTextfield(GridPane grid, String label, String text, int row, boolean editable) {
    Label condLabel = new Label(label);
    GridPane.setHalignment(condLabel, HPos.RIGHT);
    GridPane.setConstraints(condLabel, 0, row);
    TextField textBox = new TextField(text);
    textBox.setEditable(editable);
    GridPane.setMargin(textBox, new Insets(5, 5, 5, 10));
    GridPane.setConstraints(textBox, 1, row);
    grid.getChildren().addAll(condLabel, textBox);
    return textBox;
  }

  public static TextField addFormTextfield(GridPane grid, String label, String text, int row) {
    return addFormTextfield(grid, label, text, row, false);
  }

  public static void addFormPane(GridPane grid, String label, Pane pane, int row) {
    Label condLabel = new Label(label);
    GridPane.setHalignment(condLabel, HPos.RIGHT);
    GridPane.setValignment(condLabel, VPos.TOP);
    GridPane.setConstraints(condLabel, 0, row);
    GridPane.setMargin(pane, new Insets(5, 5, 5, 10));
    GridPane.setConstraints(pane, 1, row);
    grid.getChildren().addAll(condLabel, pane);
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

  public static GridPane createFormGrid(int... percentages) {
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(10, 10, 10, 10));
    for(int percentage : percentages) {
      ColumnConstraints colInfo2 = new ColumnConstraints();
      colInfo2.setPercentWidth(percentage);
      grid.getColumnConstraints().add(colInfo2);
    }
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

  public static Spinner createSpinner(int min, int max, boolean disabled, IntegerProperty property, ChangeListener<String> changeListener) {
    final Spinner spinner = new Spinner(min, max, property.get());
    spinner.setDisable(disabled);
    spinner.setMaxWidth(70);
    StringConverter<Number> converter = new NumberStringConverter();
    Bindings.bindBidirectional(spinner.getEditor().textProperty(), property, converter);
    if(changeListener != null) {
      spinner.getEditor().textProperty().addListener(changeListener);
    }
    return spinner;
  }

  public static Label createLabel(String label, StringProperty property, StringConverter<String> converter) {
    Label l = new Label(label);
    l.textProperty().bindBidirectional(property, converter);
    return l;
  }
}
