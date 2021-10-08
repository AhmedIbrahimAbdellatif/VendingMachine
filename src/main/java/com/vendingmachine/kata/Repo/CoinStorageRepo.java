package com.vendingmachine.kata.Repo;

public interface CoinStorageRepo {
    void add(String coinName, Double coinValue);

    Double getStoredAmount();
}
