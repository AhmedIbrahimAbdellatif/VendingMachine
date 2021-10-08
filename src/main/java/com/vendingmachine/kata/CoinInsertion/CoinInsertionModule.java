package com.vendingmachine.kata.CoinInsertion;

import java.util.List;

public interface CoinInsertionModule {
    boolean acceptCoins(List<String> coins);
    Double getAcceptedAmount();
    List<String> returnRejectedCoinsBack();
    Double returnAllAcceptedAmount();
}
