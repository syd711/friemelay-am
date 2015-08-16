package de.friemelay.am.ui;

import de.friemelay.am.model.Order;
import de.friemelay.am.model.OrderItem;
import javafx.util.StringConverter;

import java.text.DecimalFormat;

/**
 *
 */
public class TotalPriceConverter extends StringConverter<Number> {
  private Order order;

  public TotalPriceConverter(Order order) {
    this.order = order;
  }

  @Override
  public String toString(Number object) {
    double value = 0;
    for(OrderItem orderItem : order.getOrderItems()) {
      value = value + (orderItem.getAmount().get()*orderItem.getPrice());
    }
    DecimalFormat df = new DecimalFormat("#.00");
    return df.format(value) + " Euro";
  }

  @Override
  public Number fromString(String string) {
    double value = 0;
    for(OrderItem orderItem : order.getOrderItems()) {
      value = value + (orderItem.getAmount().get()*orderItem.getPrice());
    }
    return value;
  }
}
