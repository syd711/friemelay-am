package de.friemelay.am.ui;

import de.friemelay.am.model.OrderItem;
import javafx.util.StringConverter;

/**
 *
 */
public class PriceStringConverter extends StringConverter<String> {

  private OrderItem item;

  public PriceStringConverter(OrderItem item) {
    this.item = item;
  }

  @Override
  public String toString(String object) {
    return String.valueOf(item.getFormattedTotalPrice());
  }

  @Override
  public String fromString(String string) {
    return String.valueOf(item.getFormattedTotalPrice());
  }
}
