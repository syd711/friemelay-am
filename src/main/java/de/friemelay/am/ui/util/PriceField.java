package de.friemelay.am.ui.util;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.apache.commons.lang.StringUtils;

/**
 * Created by Matthias on 10.09.2015.
 */
public class PriceField extends HBox implements ChangeListener<String> {

  private final StringProperty firstProperty;
  private final StringProperty secondProperty;
  private boolean editable;
  private final TextField first;
  private final TextField second;
  private DoubleProperty property;

  public PriceField(DoubleProperty property) {
    super(3);
    this.property = property;

    String[] values = String.valueOf(property.get()).split("\\.");

    setAlignment(Pos.BASELINE_CENTER);
    first = new TextField(values[0]);
    firstProperty = first.textProperty();
    firstProperty.addListener(this);
    first.setMaxWidth(40);
    Label separator = new Label(",");
    second = new TextField(values[1]);
    second.setMaxWidth(30);
    secondProperty = second.textProperty();
    secondProperty.addListener(this);
    Label unit = new Label("Euro");
    this.getChildren().addAll(first,separator, second,unit);
  }

  public void setEnabled(boolean enabled) {
    this.first.setDisable(!enabled);
    this.second.setDisable(!enabled);
  }

  @Override
  public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
    try {
      if(StringUtils.isEmpty(newValue)) {

      }
      else {
        Integer.parseInt(newValue);
        String doubleValue = first.getText() + "." + second.getText();
        double v = Double.parseDouble(doubleValue);
        property.setValue(v);
      }

    }
    catch(NumberFormatException e) {
      if(observable == firstProperty) {
        first.setText(oldValue);
      }
      else if(observable == secondProperty) {
        second.setText(oldValue);
      }
    }
  }

  public void addListener(ChangeListener listener) {
    first.textProperty().addListener(listener);
    second.textProperty().addListener(listener);
  }
}
