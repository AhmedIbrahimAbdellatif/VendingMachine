package com.vendingmachine.kata.ProductSelection;

public interface ProductSelectionModule {
    boolean selectProduct(String product);
    void disposeSelectedProduct();
    Double getSelectedProductPrice();
}
