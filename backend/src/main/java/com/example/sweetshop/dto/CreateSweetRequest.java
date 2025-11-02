package com.example.sweetshop.dto;

import java.math.BigDecimal;

public class CreateSweetRequest {
  private String name;
  private String category;
  private BigDecimal price;
  private Integer quantity;
  // getters & setters
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getCategory() { return category; }
  public void setCategory(String category) { this.category = category; }
  public BigDecimal getPrice() { return price; }
  public void setPrice(BigDecimal price) { this.price = price; }
  public Integer getQuantity() { return quantity; }
  public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
