package de.friemelay.am.model;

import de.friemelay.am.ui.util.WidgetFactory;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
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
      String details= resultSet.getString("details");
      String short_description = resultSet.getString("short_description");
      Image image = readImage("image", resultSet);


      Category item = new Category();
      item.setImage(image);
      item.setParentId(parentId);
      item.setId(id);
      item.setDetails(details);
      item.setImage(image);
      item.setShortDescription(short_description);
      item.setTitle(title);
      item.setTopLevel(topLevel == 1);

      return item;
    } catch (SQLException e) {
      Logger.getLogger(ModelFactory.class.getName()).error("Failed create category model: " + e.getMessage(), e);
      WidgetFactory.showError("Failed create category model: " + e.getMessage(), e);
    }
    return null;
  }

  public static Product createProduct(ResultSet resultSet) {
    try {
      int id = resultSet.getInt("id");
      int category_id = resultSet.getInt("category_id");
      int stock = resultSet.getInt("stock");
      int amount = resultSet.getInt("amount");
      double price = resultSet.getDouble("price");
      String variant_label = resultSet.getString("variant_label");
      String title = resultSet.getString("title");
      String variant_name= resultSet.getString("variant_name");
      String details = resultSet.getString("details");

      Product item = new Product();
      item.setParentId(category_id);
      item.setId(id);
      item.setStock(stock);
      item.setTitle(title);
      item.setVariantLabel(variant_label);
      item.setVariantName(variant_name);
      item.setDetails(details);
      item.setPrice(price);
      item.setAmount(amount != 0);

      return item;
    } catch (SQLException e) {
      Logger.getLogger(ModelFactory.class.getName()).error("Failed create product model: " + e.getMessage(), e);
      WidgetFactory.showError("Failed create product model: " + e.getMessage(), e);
    }
    return null;
  }

  public static Product createVariant(ResultSet resultSet) {
    try {
      int id = resultSet.getInt("id");
      int product_id = resultSet.getInt("product_id");
      int stock = resultSet.getInt("stock");
      double price = resultSet.getDouble("price");
      String variant_name= resultSet.getString("variant_name");
      String variant_short_description= resultSet.getString("variant_short_description");
      String details = resultSet.getString("details");

      Product item = new Product();
      item.setVariant(true);
      item.setParentId(product_id);
      item.setId(id);
      item.setDetails(details);
      item.setShortDescription(variant_short_description);
      item.setVariantName(variant_name);
      item.setPrice(price);
      item.setStock(stock);

      return item;
    } catch (SQLException e) {
      Logger.getLogger(ModelFactory.class.getName()).error("Failed create variant model: " + e.getMessage(), e);
      WidgetFactory.showError("Failed create variant model: " + e.getMessage(), e);
    }
    return null;
  }

  private static Image readImage(String column, ResultSet resultSet) {
    try {
      Blob blob = resultSet.getBlob(column);
      if(blob == null) {
        return null;
      }
      InputStream is = blob.getBinaryStream();
      BufferedImage bufferedImage = ImageIO.read(is);
      return SwingFXUtils.toFXImage(bufferedImage, null);
    } catch (Exception e) {
      Logger.getLogger(ModelFactory.class).error("Error reading image from database: " + e.getMessage(), e);
    }
    return null;
  }
}
