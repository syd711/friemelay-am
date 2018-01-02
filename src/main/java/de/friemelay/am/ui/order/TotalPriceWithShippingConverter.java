package de.friemelay.am.ui.order;

import de.friemelay.am.model.Order;
import de.friemelay.am.model.OrderItem;
import javafx.util.StringConverter;

import java.text.DecimalFormat;

/**
 *
 */
public class TotalPriceWithShippingConverter extends StringConverter<Number> {
  private Order order;

  public TotalPriceWithShippingConverter(Order order) {
    this.order = order;
  }

  @Override
  public String toString(Number object) {
    double value = 0;
    for(OrderItem orderItem : order.getOrderItems()) {
      value = value + (orderItem.getAmount().get()*orderItem.getPrice());
    }
    value+=order.getShippingCosts().get();
    DecimalFormat df = new DecimalFormat("0.00");
    return df.format(value) + " Euro";
  }

  @Override
  public Number fromString(String string) {
    double value = 0;
    for(OrderItem orderItem : order.getOrderItems()) {
      value = value + (orderItem.getAmount().get()*orderItem.getPrice());
    }
    value+=order.getShippingCosts().get();
    return value;
  }
}
