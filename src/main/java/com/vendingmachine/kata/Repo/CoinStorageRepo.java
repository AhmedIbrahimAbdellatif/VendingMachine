package com.vendingmachine.kata.Repo;

import java.util.List;

public interface CoinStorageRepo {
    void addAccepted(String coinName, Double coinValue);

    Double getStoredAmount();

    void addRejected(String coin);

    List<String> popRejectedCoins();

    Double popAcceptedAmount();
}
