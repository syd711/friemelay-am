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
        orders.add(order);
      }
      
      statement.close();
    } catch (SQLException e) {
      Logger.getLogger(Connection.class.getName()).error("Failed to get orders: " + e.getMessage(), e);
      WidgetFactory.showError("Failed to get orders: " + e.getMessage(), e);
    }
    return orders;
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
      Logger.getLogger(Connection.class.getName()).error("Failed to get address: " + e.getMessage(), e);
      WidgetFactory.showError("Failed to get address: " + e.getMessage(), e);
    }
  }

  public static void loadBillingAddress(Customer customer) {
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
}
