package de.friemelay.am.db;

import de.friemelay.am.config.Config;
import de.friemelay.am.model.*;
import de.friemelay.am.ui.util.WidgetFactory;
import org.apache.log4j.Logger;

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
      while (rs.next()) {

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
      while (rs.next()) {
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
      while (rs.next()) {
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
      while (rs.next()) {
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
      while (rs.next()) {
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
      while (rs.next()) {
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
}
