package com.vendingmachine.kata.Repo;

public interface CoinStorageRepo {
    void addAccepted(String coinName, Double coinValue);

    Double getStoredAmount();

    void addRejected(String coin);
}
