package de.friemelay.am.db;

import org.junit.Before;
import org.junit.Test;

public class DatabaseTest {
  
  @Before
  public void init() {
    try {
//      DB.connect();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testOrders() {

//    assertTrue(!DB.getOrders().isEmpty());
  }
}
