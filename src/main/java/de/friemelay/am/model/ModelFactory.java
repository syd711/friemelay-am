package de.friemelay.am.model;

import de.friemelay.am.ui.util.WidgetFactory;
import org.apache.log4j.Logger;

import java.sql.Blob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ModelFactory {

  public static Order createOrder(ResultSet resultSet) {
    try {
      int id = resultSet.getInt("id");
      Date creationDate = new Date(resultSet.getTimestamp("creation_date").getTime());
      int orderStatus = resultSet.getInt("order_status");
      int customerId = resultSet.getInt("customer_id");
      int paymentType = resultSet.getInt("payment_type");
      double totalPrice = resultSet.getDouble("total_price");
      double shippingCosts = resultSet.getDouble("shipping_costs");
      String customerComments = resultSet.getString("customer_comments");
      String comments = resultSet.getString("comments");

      Order order = new Order();
      order.setId(id);
      order.setComments(comments);
      order.setCreationDate(creationDate);
      order.setCustomerComments(customerComments);
      order.setOrderStatus(orderStatus);
      order.setPaymentType(paymentType);
      order.setTotalPrice(totalPrice);
      order.setShippingCosts(shippingCosts);
      order.setCustomerId(customerId);
      return order;
    } catch (SQLException e) {
      Logger.getLogger(ModelFactory.class.getName()).error("Failed create order model: " + e.getMessage(), e);
      WidgetFactory.showError("Failed create order model: " + e.getMessage(), e);
    }
    return null;
  }

  public static OrderItem createOrderItem(ResultSet resultSet) {
    try {
      int amount = resultSet.getInt("amount");
      double price = resultSet.getInt("price");
      String productId = resultSet.getString("product_id");
      String imageUrl = resultSet.getString("image_url");
      String url = resultSet.getString("url");
      String productDescription = resultSet.getString("product_description");

      OrderItem item = new OrderItem();
      item.setAmount(amount);
      item.setPrice(price);
      item.setProduct_id(productId);
      item.setProductDescription(productDescription);
      item.setImageUrl(imageUrl);
      item.setUrl(url);
      return item;
    } catch (SQLException e) {
      Logger.getLogger(ModelFactory.class.getName()).error("Failed create order item model: " + e.getMessage(), e);
      WidgetFactory.showError("Failed create order item model: " + e.getMessage(), e);
    }
    return null;
  }

  public static Customer createCustomer(ResultSet resultSet) {
    try {
      int id = resultSet.getInt("id");
      int addressId = resultSet.getInt("address_id");
      int billingAddressId = resultSet.getInt("billing_address_id");
      int newsletter = resultSet.getInt("newsletter");
      Date creationDate = new Date(resultSet.getTimestamp("creation_date").getTime());
      Date lastLogin = new Date(resultSet.getTimestamp("last_login").getTime());
      String email = resultSet.getString("email");
      String password = resultSet.getString("customer_password");
      String customerStatus = resultSet.getString("customer_status");
      String phone = resultSet.getString("phone");

      Customer customer = new Customer();
      customer.setAddressId(addressId);
      customer.setBillingAddressId(billingAddressId);
      customer.setCreationDate(creationDate);
      customer.setCustomerStatus(customerStatus);
      customer.setEmail(email);
      customer.setPhone(phone);
      customer.setPassword(password);
      customer.setLastLogin(lastLogin);
      customer.setNewsletter(newsletter);
      customer.setId(id);
      
      return customer;
    } catch (SQLException e) {
      Logger.getLogger(ModelFactory.class.getName()).error("Failed create order item model: " + e.getMessage(), e);
      WidgetFactory.showError("Failed create order item model: " + e.getMessage(), e);
    }
    return null;
  }

  public static Address createAddress(ResultSet resultSet) {
    try {
      int id = resultSet.getInt("id");
      String firstname = resultSet.getString("firstname");
      String lastname = resultSet.getString("lastname");
      String company = resultSet.getString("company");
      String additional = resultSet.getString("additional");
      String zip = resultSet.getString("zip");
      String city = resultSet.getString("city");
      String street = resultSet.getString("street");
      String country = resultSet.getString("country");

      Address address = new Address();
      address.setId(id);
      address.setAdditional(additional);
      address.setCity(city);
      address.setCompany(company);
      address.setFirstname(firstname);
      address.setLastname(lastname);
      address.setCountry(country);
      address.setStreet(street);
      address.setZip(zip);
      
      return address;
    } catch (SQLException e) {
      Logger.getLogger(ModelFactory.class.getName()).error("Failed create address model: " + e.getMessage(), e);
      WidgetFactory.showError("Failed create address model: " + e.getMessage(), e);
    }
    return null;
  }

  public static Category createCategory(ResultSet resultSet) {
    try {
      int id = resultSet.getInt("id");
      int parentId = resultSet.getInt("parent_id");
      int topLevel = resultSet.getInt("top_level");
      String title = resultSet.getString("title");
      String description= resultSet.getString("description");
      String titleText = resultSet.getString("title_text");
      Blob image = resultSet.getBlob("image");

      Category item = new Category();
      item.setParentId(parentId);
      item.setId(id);
      item.setDescription(description);
      item.setImage(image);
      item.setTitleText(titleText);
      item.setTitle(title);
      item.setTopLevel(topLevel == 1);

      return item;
    } catch (SQLException e) {
      Logger.getLogger(ModelFactory.class.getName()).error("Failed create address model: " + e.getMessage(), e);
      WidgetFactory.showError("Failed create address model: " + e.getMessage(), e);
    }
    return null;
  }
}
