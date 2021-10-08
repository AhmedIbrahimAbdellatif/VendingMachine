package com.vendingmachine.kata.Repo;

public interface ProductRepo {
    void select(String selectedProduct);

    boolean isProductAvailable(String product);

    String getSelected();
}
