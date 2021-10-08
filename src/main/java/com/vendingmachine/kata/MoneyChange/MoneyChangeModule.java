package com.vendingmachine.kata.MoneyChange;

public interface MoneyChangeModule {
    boolean isMoneyAvailable();
    Double getAvailableMoney();
    void decreaseMoneyBy(Double moneyChange);
}
