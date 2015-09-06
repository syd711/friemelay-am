package de.friemelay.am.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 */
public class Address extends AbstractModel{
  private StringProperty firstname = new SimpleStringProperty();
  private StringProperty lastname = new SimpleStringProperty();
  private StringProperty company = new SimpleStringProperty();
  private StringProperty additional = new SimpleStringProperty();
  private StringProperty street = new SimpleStringProperty();
  private StringProperty zip = new SimpleStringProperty();
  private StringProperty city = new SimpleStringProperty();
  private StringProperty country = new SimpleStringProperty();

  public StringProperty getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname.setValue(firstname);
  }

  public StringProperty getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname.setValue(lastname);
  }

  public StringProperty getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company.setValue(company);
  }

  public StringProperty getAdditional() {
    return additional;
  }

  public void setAdditional(String additional) {
    this.additional.setValue(additional);
  }

  public StringProperty getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip.setValue(zip);
  }

  public StringProperty getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city.setValue(city);
  }

  public StringProperty getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country.setValue(country);
  }

  public StringProperty getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street.setValue(street);
  }

  @Override
  public String getStatusIcon() {
    return null;
  }

  @Override
  public int getType() {
    return AbstractModel.TYPE_ADDRESS;
  }
}
