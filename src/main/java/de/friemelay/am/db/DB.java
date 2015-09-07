package de.friemelay.am.db;

import de.friemelay.am.config.Config;
import de.friemelay.am.model.*;
import de.friemelay.am.ui.util.ImageUtil;
import de.friemelay.am.ui.util.WidgetFactory;
import org.apache.log4j.Logger;

import java.awt.image.BufferedImage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DB {

  private static Connection connection;

  public static void connect() throws Exception {
    try {
      Class.forName("com.mysql.jdbc.Driver").newInstance();

      String host = Config.getString("db.host");
      String schema = Config.getString("db.schema");
      String login = Config.getString("db.login");
      String password = Config.getString("db.password");

      connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + schema + "?" +
          "user=" + login + "&password=" + password);
      Logger.getLogger(Connection.class.getName()).info("Connection to database succussful");
    } catch (Exception ex) {
      Logger.getLogger(Connection.class.getName()).error("Failed connect to database: " + ex.getMessage(), ex);
      WidgetFactory.showError("Failed connect to database: " + ex.getMessage(), ex);
      throw ex;
    }
  }

  public static List<Order> getOrders() {
    List<Order> orders = new ArrayList<Order>();
    try {
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("select * from orders order by id desc");
      while(rs.next()) {

        Order order = ModelFactory.createOrder(rs);
        DB.loadOrderItems(order);
        DB.loadCustomer(order);
        DB.loadAddress(order.getCustomer());
        DB.loadBillingAddress(order.getCustomer());
        orders.add(order);
      }

      statement.close();
    } catch (SQLException e) {
      Logger.getLogger(Connection.class.getName()).error("Failed to get orders: " + e.getMessage(), e);
      WidgetFactory.showError("Failed to get orders: " + e.getMessage(), e);
    }
    return orders;
  }

  public static Order getOrder(int id) {
    try {
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("select * from orders where id = " + id);
      while(rs.next()) {
        Order order = ModelFactory.createOrder(rs);
        DB.loadOrderItems(order);
        DB.loadCustomer(order);
        DB.loadAddress(order.getCustomer());
        DB.loadBillingAddress(order.getCustomer());
        return order;
      }

      statement.close();
    } catch (SQLException e) {
      Logger.getLogger(Connection.class.getName()).error("Failed to get orders: " + e.getMessage(), e);
      WidgetFactory.showError("Failed to get orders: " + e.getMessage(), e);
    }
    return null;
  }

  private static void loadOrderItems(Order order) {
    try {
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("select * from order_items where order_id = " + order.getId());
      while(rs.next()) {
        OrderItem orderItem = ModelFactory.createOrderItem(rs);
        order.getOrderItems().add(orderItem);
      }

      statement.close();
    } catch (SQLException e) {
      Logger.getLogger(Connection.class.getName()).error("Failed to get order items: " + e.getMessage(), e);
      WidgetFactory.showError("Failed to get order items: " + e.getMessage(), e);
    }
  }

  public static void loadCustomer(Order order) {
    try {
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("select * from customers where id = " + order.getCustomerId());
      while(rs.next()) {
        Customer customer = ModelFactory.createCustomer(rs);
        order.setCustomer(customer);
        break;
      }

      statement.close();
    } catch (SQLException e) {
      Logger.getLogger(Connection.class.getName()).error("Failed to get customer: " + e.getMessage(), e);
      WidgetFactory.showError("Failed to get customer: " + e.getMessage(), e);
    }
  }

  public static void loadAddress(Customer customer) {
    if(customer == null) {
      return;
    }
    try {
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("select * from addresses where id = " + customer.getAddressId());
      while(rs.next()) {
        Address address = ModelFactory.createAddress(rs);
        customer.setAddress(address);
        break;
      }

      statement.close();
    } catch (SQLException e) {
      Logger.getLogger(Connection.class.getName()).error("Failed to get address [" + "select * from addresses where id = " + customer.getAddressId() + ": " + e.getMessage(), e);
      WidgetFactory.showError("Failed to get address: " + e.getMessage(), e);
    }
  }

  public static void loadBillingAddress(Customer customer) {
    if(customer == null) {
      return;
    }

    try {
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("select * from addresses where id = " + customer.getBillingAddressId());
      while(rs.next()) {
        Address address = ModelFactory.createAddress(rs);
        customer.setBillingAddress(address);
        break;
      }

      statement.close();
    } catch (SQLException e) {
      Logger.getLogger(Connection.class.getName()).error("Failed to get address: " + e.getMessage(), e);
      WidgetFactory.showError("Failed to get address: " + e.getMessage(), e);
    }
  }

  public static void delete(Order order) {
    try {
      //load full order
      order = getOrder(order.getId());

      if(order.getCustomer() != null) {
        String query = "delete from addresses where id = ? or id = ?";
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.setInt(1, order.getCustomer().getAddressId());
        preparedStmt.setInt(2, order.getCustomer().getBillingAddressId());
        preparedStmt.executeUpdate();
        preparedStmt.close();

        query = "delete from customers where id = ?";
        preparedStmt = connection.prepareStatement(query);
        preparedStmt.setInt(1, order.getCustomer().getId());
        preparedStmt.executeUpdate();
        preparedStmt.close();
      }

      String query = "delete from orders where id = ?";
      PreparedStatement preparedStmt = connection.prepareStatement(query);
      preparedStmt.setInt(1, order.getId());
      preparedStmt.executeUpdate();
      preparedStmt.close();

      query = "delete from order_items where order_id = ?";
      preparedStmt = connection.prepareStatement(query);
      preparedStmt.setInt(1, order.getId());
      preparedStmt.executeUpdate();
      preparedStmt.close();


    } catch (SQLException e) {
      Logger.getLogger(Connection.class.getName()).error("Failed to get address: " + e.getMessage(), e);
      WidgetFactory.showError("Failed to get address: " + e.getMessage(), e);
    }
  }

  public static void save(Order order) {
    try {
      String query = "update customers set customer_status = ?, email = ?, phone = ?, newsletter =? where id = ?";
      PreparedStatement preparedStmt = connection.prepareStatement(query);
      preparedStmt.setString(1, String.valueOf(order.getCustomer().getId()));
      preparedStmt.setString(2, order.getCustomer().getEmail().get());
      preparedStmt.setString(3, order.getCustomer().getPhone().get());
      preparedStmt.setInt(4, order.getCustomer().getNewsletter().get());
      preparedStmt.setInt(5, order.getCustomer().getId());
      preparedStmt.executeUpdate();
      preparedStmt.close();

      query = "update orders set order_status = ?, total_price = ?, comments = ? where id = ?";
      preparedStmt = connection.prepareStatement(query);
      preparedStmt.setInt(1, order.getOrderStatus().get());
      preparedStmt.setDouble(2, order.getTotalPrice().get());
      preparedStmt.setString(3, order.getComments().get());
      preparedStmt.setInt(4, order.getId());
      preparedStmt.executeUpdate();
      preparedStmt.close();

      query = "update addresses set firstname = ?, lastname = ?, company = ?, additional =?, street =?, zip = ?, city = ?, country = ? where id = ?";
      preparedStmt = connection.prepareStatement(query);
      Address address = order.getCustomer().getAddress();
      preparedStmt.setString(1, address.getFirstname().get());
      preparedStmt.setString(2, address.getLastname().get());
      preparedStmt.setString(3, address.getCompany().get());
      preparedStmt.setString(4, address.getAdditional().get());
      preparedStmt.setString(5, address.getStreet().get());
      preparedStmt.setString(6, address.getZip().get());
      preparedStmt.setString(7, address.getCity().get());
      preparedStmt.setString(8, address.getCountry().get());
      preparedStmt.setInt(9, address.getId());
      preparedStmt.executeUpdate();
      preparedStmt.close();

      if(order.getCustomer().getBillingAddress() != null) {
        query = "update addresses set firstname = ?, lastname = ?, company = ?, additional =?, street =?, zip = ?, city = ?, country = ? where id = ?";
        preparedStmt = connection.prepareStatement(query);
        address = order.getCustomer().getBillingAddress();
        preparedStmt.setString(1, address.getFirstname().get());
        preparedStmt.setString(2, address.getLastname().get());
        preparedStmt.setString(3, address.getCompany().get());
        preparedStmt.setString(4, address.getAdditional().get());
        preparedStmt.setString(5, address.getStreet().get());
        preparedStmt.setString(6, address.getZip().get());
        preparedStmt.setString(7, address.getCity().get());
        preparedStmt.setString(8, address.getCountry().get());
        preparedStmt.setInt(9, address.getId());
        preparedStmt.executeUpdate();
        preparedStmt.close();
      }


    } catch (SQLException e) {
      Logger.getLogger(Connection.class.getName()).error("Failed to get address: " + e.getMessage(), e);
      WidgetFactory.showError("Failed to get address: " + e.getMessage(), e);
    }
  }

  public static List<Category> loadCatalog() {
    List<Category> items = new ArrayList<Category>();
    int count = 0;
    try {
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("select * from categories");
      while(rs.next()) {
        Category item = ModelFactory.createCategory(rs);
        item.setProducts(getProducts(item.getId()));
        items.add(item);
        count++;
      }
      statement.close();
      Logger.getLogger(Connection.class.getName()).info("Loaded " + count + " categories");
    } catch (SQLException e) {
      Logger.getLogger(Connection.class.getName()).error("Failed to get catalog: " + e.getMessage(), e);
      WidgetFactory.showError("Failed to get catalog: " + e.getMessage(), e);
    }
    return items;
  }

  public static Category createCategory(String name, Category parent) {
    Logger.getLogger(Connection.class.getName()).info("Creating child category for " + parent);
    try {
      int topLevel = 1;
      Integer parentId = null;
      if(parent != null) {
        topLevel = 0;
        parentId = parent.getId();
      }
      Statement statement = connection.createStatement();
      statement.executeUpdate("insert into categories (title, model_type, top_level, parent_id) VALUES ('" + name + "', " + CatalogItem.TYPE_CATEGORY + ", " + topLevel + ", " + parentId + ")");
      statement.close();

      statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM categories ORDER BY id DESC LIMIT 0, 1");
      while(resultSet.next()) {
        Category item = ModelFactory.createCategory(resultSet);
        statement.close();
        return item;
      }
      statement.close();
    } catch (SQLException e) {
      Logger.getLogger(Connection.class.getName()).error("Failed to insert category in catalog: " + e.getMessage(), e);
      WidgetFactory.showError("Failed to insert category in catalog: " + e.getMessage(), e);
    }
    return null;
  }

  public static void deleteCategory(Category category) {
    Logger.getLogger(Connection.class.getName()).info("Deleting category " + category);
    try {
      String query = "delete from categories where id = " + category.getId();
      PreparedStatement preparedStmt = connection.prepareStatement(query);
      preparedStmt.executeUpdate();
      preparedStmt.close();

      for(Category child : category.getChildren()) {
        deleteCategory(child);
      }

      List<Product> products = category.getProducts();
      for(Product product : products) {
        deleteProduct(product);
      }


    } catch (SQLException e) {
      Logger.getLogger(Connection.class.getName()).error("Failed to delete category: " + e.getMessage(), e);
      WidgetFactory.showError("Failed to delete category: " + e.getMessage(), e);
    }
  }

  public static void deleteProduct(Product product) {
    Logger.getLogger(Connection.class.getName()).info("Deleting product " + product);
    try {
      String query = "delete from products where id = " + product.getId();
      PreparedStatement preparedStmt = connection.prepareStatement(query);
      preparedStmt.executeUpdate();
      preparedStmt.close();

      List<Product> variants = product.getVariants();
      for(Product variant : variants) {
        deleteProduct(variant);
      }
    } catch (SQLException e) {
      Logger.getLogger(Connection.class.getName()).error("Failed to delete product: " + e.getMessage(), e);
      WidgetFactory.showError("Failed to delete product: " + e.getMessage(), e);
    }
  }

  public static void save(Category category) {
    try {
      String query = "update categories set title = ?, short_description = ?, details = ? , image = ? where id = " + category.getId();
      PreparedStatement preparedStmt = connection.prepareStatement(query);
      preparedStmt.setString(1, String.valueOf(category.getTitle().get()));
      preparedStmt.setString(2, category.getShortDescription().get());
      preparedStmt.setString(3, category.getDetails().get());
      if(category.getImage() != null) {
        preparedStmt.setBinaryStream(4, ImageUtil.getImageInputStream(category.getImage()));
      }
      else {
        preparedStmt.setNull(4, Types.BLOB);
      }
      preparedStmt.executeUpdate();
      preparedStmt.close();

    } catch (SQLException e) {
      Logger.getLogger(Connection.class.getName()).error("Failed to save category: " + e.getMessage(), e);
      WidgetFactory.showError("Failed to save category: " + e.getMessage(), e);
    }
  }

  public static void save(Product product) {
    try {
      String query = "update products set title = ?, variant_label = ?, variant_name = ?, variant_short_description = ?, short_description = ?, details = ?, stock = ?, price = ?, amount = ? where id = " + product.getId();
      PreparedStatement preparedStmt = connection.prepareStatement(query);
      preparedStmt.setString(1, String.valueOf(product.getTitle().get()));
      preparedStmt.setString(2, product.getVariantLabel().get());
      preparedStmt.setString(3, product.getVariantName().get());
      preparedStmt.setString(4, product.getVariantShortDescription().get());
      preparedStmt.setString(5, product.getShortDescription().get());
      preparedStmt.setString(6, product.getDetails().get());
      preparedStmt.setInt(7, product.getStock().get());
      preparedStmt.setDouble(8, product.getPrice().get());
      preparedStmt.setInt(9, product.getAmountValue());
      preparedStmt.executeUpdate();
      preparedStmt.close();

      query = "DELETE FROM productimages where product_id = " + product.getId();
      preparedStmt = connection.prepareStatement(query);
      preparedStmt.executeUpdate();
      preparedStmt.close();

      List<BufferedImage> images = product.getImages();
      for(BufferedImage image : images) {
        try {
          query = "INSERT INTO productimages (product_id, mime_type, image, teaser_image, thumbnail_image) VALUES (?,?,?,?,?)";
          preparedStmt = connection.prepareStatement(query);
          preparedStmt.setInt(1, product.getId());
          preparedStmt.setString(2, "image/jpeg");
          preparedStmt.setBinaryStream(3, ImageUtil.getImageInputStream(image));
          preparedStmt.setBinaryStream(4, ImageUtil.getImageInputStream(ImageUtil.createThumbnail(image, Config.getInt("teaser.width"), Config.getInt("teaser.height"))));
          preparedStmt.setBinaryStream(5, ImageUtil.getImageInputStream(ImageUtil.createThumbnail(image, Config.getInt("thumnail.width"), Config.getInt("thumnail.height"))));
          preparedStmt.executeUpdate();
          preparedStmt.close();
        } catch (RuntimeException e) {
          Logger.getLogger(Connection.class.getName()).error("Failed to insert image: " + e.getMessage(), e);
        }
      }

    } catch (SQLException e) {
      Logger.getLogger(Connection.class.getName()).error("Failed to save product: " + e.getMessage(), e);
      WidgetFactory.showError("Failed to save product: " + e.getMessage(), e);
    }
  }

  public static Category getCategory(int id) {
    try {
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("select * from categories where id = " + id);
      while(rs.next()) {
        Category item = ModelFactory.createCategory(rs);
        statement.close();
        return item;
      }

    } catch (SQLException e) {
      Logger.getLogger(Connection.class.getName()).error("Failed to get category: " + e.getMessage(), e);
      WidgetFactory.showError("Failed to get category: " + e.getMessage(), e);
    }
    return null;
  }


  public static Product getProduct(int id) {
    try {
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("select * from products where id = " + id);
      while(rs.next()) {
        Product item = ModelFactory.createProduct(rs);
        statement.close();
        return item;
      }

    } catch (SQLException e) {
      Logger.getLogger(Connection.class.getName()).error("Failed to get product: " + e.getMessage(), e);
      WidgetFactory.showError("Failed to get product: " + e.getMessage(), e);
    }
    return null;
  }

  public static Product loadImages(Product p) {
    try {
      p.getImages().clear();
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("select * from productimages where product_id = " + p.getId());
      while(rs.next()) {
        BufferedImage image = ModelFactory.createImage(rs);
        p.getImages().add(image);
      }
      statement.close();
    } catch (SQLException e) {
      Logger.getLogger(Connection.class.getName()).error("Failed to get product: " + e.getMessage(), e);
      WidgetFactory.showError("Failed to get product: " + e.getMessage(), e);
    }
    return null;
  }

  public static List<Product> getProducts(int parentId) {
    List<Product> items = new ArrayList();
    try {
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("select * from products where parent_id = " + parentId + " AND model_type = " + AbstractModel.TYPE_PRODUCT);
      while(rs.next()) {
        Product item = ModelFactory.createProduct(rs);
        if(!item.isVariant()) {
          item.setVariants(getVariants(item.getId()));
        }
        items.add(item);
      }
      statement.close();
    } catch (SQLException e) {
      Logger.getLogger(Connection.class.getName()).error("Failed to get products: " + e.getMessage(), e);
      WidgetFactory.showError("Failed to get products: " + e.getMessage(), e);
    }
    return items;
  }

  public static List<Product> getVariants(int parentId) {
    List<Product> items = new ArrayList();
    try {
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("select * from products where parent_id = " + parentId + " AND model_type = " + AbstractModel.TYPE_VARIANT);
      while(rs.next()) {
        Product item = ModelFactory.createProduct(rs);
        items.add(item);
      }
      statement.close();
    } catch (SQLException e) {
      Logger.getLogger(Connection.class.getName()).error("Failed to get variants: " + e.getMessage(), e);
      WidgetFactory.showError("Failed to get variants: " + e.getMessage(), e);
    }
    return items;
  }

  public static void deleteCatalogItem(CatalogItem selection) {
    if(selection instanceof Category) {
      deleteCategory((Category) selection);
    }
    else if(selection instanceof Product) {
      deleteProduct((Product) selection);
    }
  }

  public static Product createProduct(String name, int parentId, boolean variant) {
    try {
      Statement statement = connection.createStatement();
      int type = CatalogItem.TYPE_PRODUCT;
      if(variant) {
        type = AbstractModel.TYPE_VARIANT;
      }
      statement.executeUpdate("insert into products (title, model_type, parent_id, amount) VALUES ('" + name + "', " + type + ", " + parentId + ", 1)");
      statement.close();

      statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM products ORDER BY id DESC LIMIT 0, 1");
      while(resultSet.next()) {
        Product item = ModelFactory.createProduct(resultSet);
        statement.close();
        return item;
      }
    } catch (SQLException e) {
      Logger.getLogger(Connection.class.getName()).error("Failed to insert product in catalog: " + e.getMessage(), e);
      WidgetFactory.showError("Failed to insert product in catalog: " + e.getMessage(), e);
    }
    return null;
  }
}
