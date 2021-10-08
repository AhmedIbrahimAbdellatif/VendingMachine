package com.vendingmachine.kata.Machine;

import java.util.ArrayList;
import java.util.List;

import com.vendingmachine.kata.CoinInsertion.CoinInsertionModule;
import org.javatuples.Pair;

public class VendingMachine {
    private static final String DEFAULT_MESSAGE = "INSERT COIN";
    private static final String INSERTION_PROMPT_MESSAGE = "INSERT COIN";
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

        boolean isAllCoinsAccepted = coinInsertionModule.acceptCoins(coins);

        String message = updateCoinInsertionMessage();

        List<String> rejectedCoins = new ArrayList<>();
        if(!isAllCoinsAccepted) {
            rejectedCoins = coinInsertionModule.takeRejectedCoinsBack();
        }

        return Pair.with(message, rejectedCoins);
    }

    private String updateCoinInsertionMessage() {
        String message;
        if(coinInsertionModule.getAcceptedAmount() == 0.0) {
            message = INSERTION_PROMPT_MESSAGE;
        }
        else {
            message = "$" + coinInsertionModule.getAcceptedAmount().toString();
        }
        return message;
    }
}
