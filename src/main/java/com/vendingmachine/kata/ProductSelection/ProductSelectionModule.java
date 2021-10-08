package com.vendingmachine.kata.ProductSelection;

public interface ProductSelectionModule {
    boolean selectProduct(String product);
    Double getSelectedProductPrice();
}
