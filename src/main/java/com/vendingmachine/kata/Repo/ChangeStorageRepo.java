package com.vendingmachine.kata.Repo;

public interface ChangeStorageRepo {
    Double getStoredMoney();

    void decreaseMoney(Double amount);
}
