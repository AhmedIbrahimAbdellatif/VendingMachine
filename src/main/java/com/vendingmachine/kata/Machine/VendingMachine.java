package com.vendingmachine.kata.Machine;

import java.util.ArrayList;
import java.util.List;

import com.vendingmachine.kata.CoinInsertion.CoinInsertionModule;
import org.javatuples.Pair;

public class VendingMachine {
    private static final String DEFAULT_MESSAGE = "INSERT COIN";
    public static enum ParameterOrder {
        MESSAGE,
        COIN_RETURN
    }

    private final CoinInsertionModule coinInsertionModule;

    public VendingMachine(CoinInsertionModule coinInsertionModule) {
        this.coinInsertionModule = coinInsertionModule;
    }

    public String start() {
        return DEFAULT_MESSAGE;
    }

    public Pair<String, List<String>> insertCoins(List<String> coins) {
        String message = "";
        List<String> rejectedCoins = new ArrayList<>();
        if(coinInsertionModule.acceptCoins(coins)) {
            message = "$" + coinInsertionModule.getAcceptedAmount().toString();
        }
        return Pair.with(message, rejectedCoins);
    }
}
